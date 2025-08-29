$(document).ready(function () {
    let searchBox;
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        }
    });
    $.fn.validarSession = function () {
        $.getJSON("validarsesion", function (data) {
            if (data.resultado === "ok") {
                $("#lblUsuario").text(data.logi);
            } else {
                $(location).attr('href', "index.html");
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            $(location).attr('href', "index.html");
        });
    };
    $.fn.validarSession();
    // Inicializar DataTable para la tabla principal
    var mainTable = $('#productosTable').DataTable({
        searching: false,
        paging: false,
        info: false,
        language: {
            decimal: "",
            emptyTable: "No hay datos",
            info: "Mostrando desde el _START_ al _END_ del total de _TOTAL_ registros",
            infoEmpty: "Mostrando desde el 0 al 0 del total de  0 registros",
            infoFiltered: "(Filtrados del total de _MAX_ registros)",
            infoPostFix: "",
            thousands: ",",
            lengthMenu: "Mostrar _MENU_ registros por página",
            loadingRecords: "Cargando...",
            processing: "Procesando...",
            search: "Buscar:",
            zeroRecords: "No se ha encontrado nada  atraves de ese filtrado.",
            paginate: {
                first: "Primero",
                last: "Última",
                next: "Siguiente",
                previous: "Anterior"
            },
            aria: {
                sortAscending: ": activate to sort column ascending",
                sortDescending: ": activate to sort column descending"
            }
        },
        columns: [
            {data: 'codpro'},
            {data: 'despro'},
            {
                data: 'qr',
                "render": function (data, type, row) {
                    return data ? data : '';
                }
            },
            {
                data: 'precio',
                "render": function (data, type, row) {
                    return data ? 'S/. ' + data : '';
                }
            },
            {
                data: null,
                defaultContent: '<button class="btn btn-danger btn-sm delete-btn"><i class="fas fa-trash-alt"></i></button>'
            }
        ]
    });

    // Inicializar DataTable para el modal
    var modalTable = $('#modalTable').DataTable({
        info: false,
        "lengthChange": false,
        "pageLength": 8,
        language: {
            decimal: "",
            emptyTable: "No hay datos",
            info: "Mostrando desde el _START_ al _END_ del total de _TOTAL_ registros",
            infoEmpty: "Mostrando desde el 0 al 0 del total de  0 registros",
            infoFiltered: "(Filtrados del total de _MAX_ registros)",
            infoPostFix: "",
            thousands: ",",
            lengthMenu: "Mostrar _MENU_ registros por página",
            loadingRecords: "Cargando...",
            processing: "Procesando...",
            search: "Buscar:",
            zeroRecords: "No se ha encontrado nada  atraves de ese filtrado.",
            paginate: {
                first: "Primero",
                last: "Última",
                next: "Siguiente",
                previous: "Anterior"
            },
            aria: {
                sortAscending: ": activate to sort column ascending",
                sortDescending: ": activate to sort column descending"
            }
        },
        "ajax": {
            url: 'CRUDProductos?opcion=6',
            type: 'POST',
            "dataSrc": function (json) {
                // Verificar si la respuesta contiene "OK"
                if (json.resultado === "ok") {
                    return json.data;
                } else {
                    alert('Error: ' + json.mensaje);
                    return []; // Retorna un array vacío si no es "OK"
                }
            }
        },
        columns: [
            {data: 'codpro'},
            {data: 'despro', "width": '60%'},
            {
                data: 'qr',
                "render": function (data, type, row) {
                    return data ? data : '';
                }
            },
            {
                data: 'precio',
                "render": function (data, type, row) {
                    return data ? 'S/. ' + data : '';
                }
            },
            {
                data: null,
                defaultContent: '<button class="btn btn-primary btn-sm add-btn"><i class="fas fa-plus"></i></button>'
            }
        ]
    });
    // Manejar el evento de agregar producto desde el modal
    $('#modalTable').on('click', '.add-btn', function () {
        var rowData = modalTable.row($(this).parents('tr')).data();
        var existingCodes = getExistingProductCodes();

        // Verificar si el producto ya está en la tabla principal
        if (existingCodes.includes(rowData.codpro)) {
            alert('Este producto ya se encuentra agregado.');
        } else {
            // Agregar el producto a la tabla principal
            mainTable.row.add(rowData).draw();
        }
        searchBox.focus();
    });
    $('#addProductModal').on('show.bs.modal', function () {
        searchBox = $('#modalTable_filter input');
        setTimeout(function () {
            searchBox.focus();
        }, 600); // Ajusta el retraso si es necesario
    });
    // Manejar el evento de eliminar producto
    $('#productosTable').on('click', '.delete-btn', function () {
        mainTable.row($(this).parents('tr')).remove().draw();
    });

    // Manejar el evento de exportar
    $('#exportButton').on('click', function () {
        var selectedRows = mainTable.rows({selected: true}).data().toArray();
        var jsonData = JSON.stringify(selectedRows);
        if (selectedRows.length > 0) {
            // Puedes enviar jsonData al servidor o descargarlo como un archivo
            console.log(jsonData);
            generatePDF(selectedRows);
        } else {
            Toast.fire({
                icon: "error",
                title: "Seleccione almenos un producto."
            });
        }
    });

    function getExistingProductCodes() {
        var codes = mainTable.column(0).data().toArray();
        return codes;
    }
    $("#guardar").on("click", function () {
        guardarListaConCodigo();
    });
    function guardarListaConCodigo() {
        var data = mainTable.rows().data().toArray();
        var codpros = data.map(row => row.codpro);

        // Envía los codpro junto con un nombre/código generado
        var codigoLista = prompt("Ingresa un código o nombre para esta lista:");
        if (!codigoLista) {
            Toast.fire({
                icon: "info",
                title: "Debe ingresar un nombre."
            });
            return;
        }

        $.ajax({
            url: 'CRUDtiraproductos?opcion=3',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({codigo: codigoLista, codpros: codpros}),
            dataType: 'json',
            success: function (response) {
                if (response.resultado === 'ok') {
                    Toast.fire({
                        icon: "success",
                        title: "Lista guardada correctamente."
                    });
                } else {
                    Toast.fire({
                        icon: "error",
                        title: "No se ha podido guardar la lista por problemas con el servidor."
                    });
                }
            },
            error: function (xhr, status, error) {
                Toast.fire({
                    icon: "error",
                    title: "Error de conexion con el servidor."
                });
            }
        });
    }

    $("#cargar").on("click", function () {
        mostrarModalListasPorCodigo();
    });
    function mostrarModalListasPorCodigo() {
        $('#modalGuardados').modal('show'); // Muestra el modal

        // Solicita al servidor los códigos disponibles
        $.ajax({
            url: 'CRUDtiraproductos?opcion=1',
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                if (response.resultado === 'ok') {
                    var tablaOpciones = $('#tablaListasGuardadas tbody'); // Contenedor de la tabla
                    tablaOpciones.empty(); // Limpia los datos previos

                    response.data.forEach(codigo => {
                        tablaOpciones.append(`
                        <tr>
                            <td>${codigo.tirnam}</td>
                            <td>
                                <button class="btn btn-primary mr-2 btn-sm lista-btn" data-codigo='${codigo.codtir}'>
                                    <i class="fa fa-plus"></i> Agregar
                                </button>
                                <button class="btn btn-danger btn-sm eliminar-btn" data-codigo='${codigo.codtir}'>
                                    <i class="fa fa-trash"></i>
                                </button>
                            </td>
                        </tr>
                    `);
                    });

                    // Manejar el evento del botón "Eliminar"
                    $('.eliminar-btn').click(function () {
                        var codigo = $(this).data('codigo');

                        // Confirmación antes de eliminar
                        if (confirm('¿Estás seguro de que deseas eliminar esta lista?')) {
                            eliminarLista(codigo);
                        }
                    });
                } else {
                    Toast.fire({
                        icon: "error",
                        title: "No se ha podido cargar la lista del servidor."
                    });
                }
            },
            error: function (xhr, status, error) {
                Toast.fire({
                    icon: "error",
                    title: "Error de conexion con el servidor."
                });
            }
        });
    }
    $("#cerrar").click(function () {
        $("#modalGuardados").modal("close");
    });
    function eliminarLista(codigo) {
        $.ajax({
            url: 'CRUDtiraproductos?opcion=4&codtir=' + codigo,
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (response.resultado === 'ok') {
                    Toast.fire({
                        icon: "success",
                        title: "Eliminado correctamente."
                    });
                    mostrarModalListasPorCodigo(); // Recarga las listas
                } else {
                    Toast.fire({
                        icon: "error",
                        title: "Error al eliminar la lista."
                    });
                }
            },
            error: function (xhr, status, error) {
                Toast.fire({
                    icon: "error",
                    title: "Error de conexion con el servidor."
                });
            }
        });
    }
    $('#tablaListasGuardadas').on('click', '.lista-btn', function () {
        var codigoLista = $(this).data('codigo');

        // Solicita al servidor los codpro asociados al código
        $.ajax({
            url: 'CRUDtiraproductos?opcion=2&codtir=' + codigoLista,
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (response.resultado === 'ok') {
                    var codpros = response.data; // Lista de codpros recibida

                    // Limpia la tabla principal antes de agregar los nuevos datos
                    mainTable.clear().draw();

                    // Recorrer modalTable para buscar y agregar los registros coincidentes
                    modalTable.rows().every(function () {
                        var rowData = this.data();
                        if (codpros.includes(rowData.codpro)) {
                            mainTable.row.add(rowData).draw();
                        }
                    });

                    $('#modalGuardados').modal('hide'); // Cierra el modal
                    Toast.fire({
                        icon: "success",
                        title: "Lista cargada correctamente."
                    });
                } else {
                    Toast.fire({
                        icon: "error",
                        title: "Error al cargar la lista."
                    });
                }
            },
            error: function (xhr, status, error) {
                Toast.fire({
                    icon: "error",
                    title: "Error de conexion con el servidor."
                });
            }
        });
    });




    async function generatePDF(products) {
        const { PDFDocument, rgb, StandardFonts } = PDFLib;

        async function generateBarcodeBase64(text) {
            try {
                const canvas = document.createElement('canvas');
                JsBarcode(canvas, text, {
                    format: "CODE128",
                    displayValue: false,
                    width: 1.5,
                    height: 30,
                    margin: 0
                });
                return canvas.toDataURL('image/png');
            } catch (error) {
                console.error('Error al generar la imagen del código de barras:', error);
                return null;
            }
        }

        const pdfDoc = await PDFDocument.create();

        // Configuración de dimensiones
        const boxWidth = (2.8 * 72) - 28.35;
        const boxHeight = 1.2 * 72;
        const margin = 0.1 * 72;
        const spacing = 0.3 * 72;
        const verticalSpacing = 0.1 * 72;

        // Cálculo de columnas y filas
        const maxProductsPerColumn = 2;
        const productsPerRow = Math.ceil(products.length / maxProductsPerColumn);
        const totalColumns = Math.ceil(products.length / maxProductsPerColumn);

        const pageWidth = (boxWidth * totalColumns) + (spacing * (totalColumns - 1)) + (margin * 2);
        const pageHeight = (boxHeight * maxProductsPerColumn) + (verticalSpacing * (maxProductsPerColumn - 1)) + (margin * 2);

        const page = pdfDoc.addPage([pageWidth, pageHeight]);
        const font = await pdfDoc.embedFont(StandardFonts.Helvetica);

        function splitTextIntoLines(text, maxWidth, fontSize) {
            const words = text.split(' ');
            let lines = [];
            let currentLine = '';

            words.forEach((word) => {
                const testLine = currentLine ? `${currentLine} ${word}` : word;
                const testWidth = font.widthOfTextAtSize(testLine, fontSize);

                if (testWidth <= maxWidth) {
                    currentLine = testLine;
                } else {
                    lines.push(currentLine);
                    currentLine = word;
                }
            });

            if (currentLine)
                lines.push(currentLine);
            return lines;
        }

        for (let index = 0; index < products.length; index++) {
            const product = products[index];
            const column = Math.floor(index / maxProductsPerColumn);
            const row = index % maxProductsPerColumn;

            const x = margin + (column * (boxWidth + spacing));
            const y = pageHeight - margin - boxHeight - (row * (boxHeight + verticalSpacing));

            // Rectángulo de la etiqueta
            page.drawRectangle({
                x: x,
                y: y,
                width: boxWidth,
                height: boxHeight,
                borderColor: rgb(0, 0, 0),
                borderWidth: 0.25
            });

            // Descripción del producto
            const nameFontSize = 8;
            const lines = splitTextIntoLines(product.despro, boxWidth - 10, nameFontSize);
            let textYPosition = y + boxHeight - 12;

            lines.forEach((line) => {
                page.drawText(line, {
                    x: x + 2,
                    y: textYPosition,
                    size: nameFontSize,
                    font,
                    color: rgb(0, 0, 0)
                });
                textYPosition -= nameFontSize + 2;
            });

            // Precio
            const priceSize = 24;
            const currencySize = 10;
            const currencySymbol = 'S/. ';
            const priceText = `${product.precio.toFixed(2)}`;
            const priceYPosition = y + boxHeight / 2 - 6;

            const currencyWidth = font.widthOfTextAtSize(currencySymbol, currencySize);
            const priceWidth = font.widthOfTextAtSize(priceText, priceSize);
            const totalPriceWidth = currencyWidth + priceWidth;
            const priceX = x + (boxWidth - totalPriceWidth) / 2;

            page.drawText(currencySymbol, {
                x: priceX,
                y: priceYPosition + 2,
                size: currencySize,
                font,
                color: rgb(0, 0, 0)
            });

            page.drawText(priceText, {
                x: priceX + currencyWidth,
                y: priceYPosition,
                size: priceSize,
                font,
                color: rgb(0, 0, 0)
            });

            // Código de producto
            const codeYPosition = y + 8;
            page.drawText(product.codpro, {
                x: x + 2,
                y: codeYPosition,
                size: 8,
                font,
                color: rgb(0, 0, 0)
            });

            // Código de barras
            if (product.qr) {
                const barcodeImage = await generateBarcodeBase64(product.qr);
                if (barcodeImage) {
                    const response = await fetch(barcodeImage);
                    const barcodeBytes = await response.arrayBuffer();
                    const embeddedBarcode = await pdfDoc.embedPng(barcodeBytes);
                    page.drawImage(embeddedBarcode, {
                        x: x + boxWidth - 95,
                        y: codeYPosition - 4,
                        width: 90,
                        height: 20
                    });
                }
            }
        }

        const pdfBytes = await pdfDoc.save();
        const blob = new Blob([pdfBytes], { type: 'application/pdf' });
        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = 'etiquetas_tsc_te200.pdf';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }

    /*async function generatePDF(products) {
     const {PDFDocument, rgb, StandardFonts} = PDFLib;
     function generateBarcodeBase64(text) {
     try {
     const canvas = document.createElement('canvas');
     JsBarcode(canvas, text, {format: "CODE128",
     displayValue: false});
     return canvas.toDataURL('image/png'); // Generar base64
     } catch (error) {
     console.error('Error al generar la imagen del código de barras:', error);
     throw error;
     }
     }
     const pdfDoc = await PDFDocument.create();
     
     const boxWidthCm = 6;
     const boxHeightCm = 3.5;
     const marginCm = 0.5;
     
     const boxWidth = boxWidthCm * 28.35;
     const boxHeight = boxHeightCm * 28.35;
     const margin = marginCm * 28.35;
     
     let productsPerRow = 2;
     if (products.length === 1) {
     productsPerRow = 1;
     }
     const totalColumns = Math.ceil(products.length / productsPerRow);
     
     const pageWidth = (boxWidth + margin) * totalColumns + margin;
     const pageHeight = (boxHeight + margin) * productsPerRow + margin;
     
     const page = pdfDoc.addPage([pageWidth, pageHeight]);
     
     const font = await pdfDoc.embedFont(StandardFonts.Helvetica);
     
     function splitTextIntoLines(text, maxWidth, fontSize) {
     const words = text.split(' ');
     let lines = [];
     let currentLine = '';
     
     words.forEach((word) => {
     const testLine = currentLine ? `${currentLine} ${word}` : word;
     const testWidth = font.widthOfTextAtSize(testLine, fontSize);
     
     if (testWidth <= maxWidth) {
     currentLine = testLine;
     } else {
     lines.push(currentLine);
     currentLine = word;
     }
     });
     
     if (currentLine)
     lines.push(currentLine);
     return lines;
     }
     
     for (let index = 0; index < products.length; index++) {
     const product = products[index];
     const rowIndex = index % productsPerRow;
     const columnIndex = Math.floor(index / productsPerRow);
     
     const xPosition = margin + columnIndex * (boxWidth + margin);
     const yPosition = pageHeight - (rowIndex + 1) * (boxHeight + margin);
     
     page.drawRectangle({
     x: xPosition,
     y: yPosition,
     width: boxWidth,
     height: boxHeight,
     borderColor: rgb(0, 0, 0),
     borderWidth: 1
     });
     
     const nameFontSize = 12;
     const nameMarginTop = 10;
     const nameMarginBottom = 30; // Space between name and price
     const espaciado = 0.5;
     const maxTextHeight = boxHeight - nameMarginTop - nameMarginBottom;
     
     const lines = splitTextIntoLines(product.despro, boxWidth - 20, nameFontSize);
     const totalTextHeight = -lines.length * (nameFontSize + espaciado);
     
     // Calculate y position for name
     let textYPosition = yPosition + boxHeight - nameMarginBottom - (totalTextHeight / 2);
     
     if (totalTextHeight > maxTextHeight) {
     textYPosition = yPosition + boxHeight - nameMarginBottom - (maxTextHeight / 2);
     }
     
     lines.forEach((line) => {
     const textWidth = font.widthOfTextAtSize(line, nameFontSize);
     const textXPosition = xPosition + (boxWidth - textWidth) / 2;
     
     page.drawText(line, {
     x: textXPosition,
     y: textYPosition,
     size: nameFontSize,
     font: font,
     color: rgb(0, 0, 0)
     });
     
     textYPosition -= nameFontSize + espaciado;
     });
     
     // Price with different font sizes for the currency symbol
     const priceSize = 32;
     const smallCurrencySize = 12; // Smaller font size for the currency symbol
     const priceText = `${product.precio.toFixed(2)}`;
     
     const currencySymbol = 'S/. ';
     
     // Position for the currency symbol
     const currencySymbolWidth = font.widthOfTextAtSize(currencySymbol, smallCurrencySize);
     const priceWidth = currencySymbolWidth + font.widthOfTextAtSize(priceText, priceSize);
     const priceXPosition = xPosition + (boxWidth - priceWidth) / 2;
     const currencyXPosition = priceXPosition;
     const priceYPosition = yPosition + (boxHeight - priceSize) / 2;
     const currencyYPosition = -10 + priceYPosition + (priceSize - smallCurrencySize) / 2;
     
     page.drawText(currencySymbol, {
     x: currencyXPosition,
     y: currencyYPosition,
     size: smallCurrencySize,
     font: font,
     color: rgb(0, 0, 0)
     });
     
     const amountXPosition = priceXPosition + currencySymbolWidth;
     page.drawText(priceText, {
     x: amountXPosition,
     y: priceYPosition,
     size: priceSize,
     font: font,
     color: rgb(0, 0, 0)
     });
     
     const code5Size = 15;
     const code5Text = product.codpro;
     const code5YPosition = yPosition + 5;
     page.drawText(code5Text, {
     x: xPosition + 5,
     y: code5YPosition + 3,
     size: code5Size,
     font: font,
     color: rgb(0, 0, 0)
     });
     
     if (product.qr !== undefined) {
     const code13Text = product.qr;
     try {
     const barcodeImage = await generateBarcodeBase64(code13Text); // Generar imagen en base64
     const response = await fetch(barcodeImage); // Obtener la imagen a partir de base64
     const barcodeImageBytes = await response.arrayBuffer(); // Convertir a array buffer
     const embeddedImage = await pdfDoc.embedPng(barcodeImageBytes); // Incrustar PNG en el PDF
     const barcodeImageEmbed = embeddedImage;
     const code13Width = 72;
     const code13XPosition = xPosition + boxWidth - code13Width - 5;
     try {
     page.drawImage(barcodeImageEmbed, {
     x: code13XPosition,
     y: code5YPosition,
     width: code13Width,
     height: code13Width / 3
     });
     } catch (error) {
     console.error('No se pudo incrustar la imagen del código de barras:', error);
     }
     } catch (error) {
     console.error('Error al generar o incrustar el código de barras:', error);
     throw error; // Asegúrate de propagar el error si algo falla
     }
     }
     }
     ;
     
     const pdfBytes = await pdfDoc.save();
     const blob = new Blob([pdfBytes], {type: 'application/pdf'});
     const link = document.createElement('a');
     link.href = URL.createObjectURL(blob);
     link.download = 'productos.pdf';
     document.body.appendChild(link);
     link.click();
     document.body.removeChild(link);
     }*/

});
