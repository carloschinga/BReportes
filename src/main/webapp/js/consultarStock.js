$(document).ready(function () {
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
    let searchBox;
    let listadata;
    $.fn.ListarProductos = function () {
        $.fn.validarSession();
        $.getJSON("CRUDFaAlmacenes?opcion=2", function (data) {
            if (data.resultado === "ok") {
                let almacen = $('#almacen');
                almacen.empty();
                //almacen.append('<option value="">TODOS</option>');
                $.each(data.data, function (key, value) {
                    //if(value.codalm!=="A1" && value.codalm!=="A2")
                    almacen.append('<option value="' + value.codalm + '">' + value.desalm + '</option>');
                });
            } else {
                if (data.mensaje === "nosession") {
                    $.fn.validarSession();
                } else {
                    alert("Error: Problemas con el servidor.");
                }
            }
        });
        let tabla = $('#tabladatos').DataTable();
        if (tabla) {
            $("#tabladatos").empty();
            tabla.destroy();
        }
        $("#loadingproductos").css("display", "block");
        tabla = $("#tabladatos").DataTable({
            "lengthChange": false,
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
            ajax: {
                url: 'CRUDProductos?opcion=1',
                type: 'POST',
                "data": function (d) {
                    d.codlab = "";
                    d.codfam = "";
                    d.codgen = "";
                    d.codtip = "";
                    d.codpro = "";
                },
                dataSrc: function (json) {
                    listadata = json.data;
                    return json.data;
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
                {data: null,
                    "render": function (data, type, row) {
                        return '<button class="btn btn-info etiqueta" data-codpro="' + row.codpro + '"><i class="fa fa-tag"></i></button><button class="btn btn-info consulta" data-codpro="' + row.codpro + '"><i class="fa fa-tasks"></i></button>';
                    }
                }
            ]
        });
        $("#loadingproductos").css("display", "none");
        searchBox = $('#tabladatos_filter input');
        searchBox.focus();
    };
    $.fn.ListarProductos();
    $('#tabladatos').on('click', '.etiqueta', function () {

        let val = String($(this).data("codpro"));
        if (val !== undefined && val !== null && val !== "") {
            $.getJSON('CRUDProductos', {opcion: 9, codpro: val}, function (response) {
                if (response.resultado === 'ok') {
                    if (response.data.length > 0) {
                        generatePDF(response.data);
                    } else {
                        alert("No se encontro el producto");
                    }
                } else {
                    if (response.mensaje === "nosession") {
                        $.fn.validarSession();
                    } else {
                        alert("Error del servidor");
                    }

                }
            });
        }
    });
    $('#tabladatos').on('click', '.consulta', function () {
        $.fn.validarSession();
        let codpro = String($(this).data("codpro"));
        $('#modal-stocks').modal('show');
        let codalm = $('#almacen').val();
        let tabla = $('#table-stocks').DataTable();
        if (tabla) {
            tabla.destroy();
        }
        searchBox.focus();
        tabla = $("#table-stocks").DataTable({
            "lengthChange": false,
            searching: false,
            paging: false,
            fixedHeader: true,

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
            }, ajax: {
                url: 'CRUDFaStockAlmacenes?opcion=3',
                type: 'POST',
                "data": function (d) {
                    d.codpro = codpro;
                    d.codalm = codalm;
                },
                beforeSend: function () {
                    $("#loadingstocks").css("display", "block");
                }, complete: function () {
                    $("#loadingstocks").css("display", "none");
                }
            },
            columns: [
                {data: 'desalm'},
                {data: 'stkalm'},
                {data: 'stkalm_m'}
            ]
        });

    });
    $('#almacen').on('change', function () {
        searchBox.focus();
    });
    $('#modal-stocks').on('hidden.bs.modal', function (e) {
        searchBox.focus();
    });
    searchBox.on('keyup', function (event) {
        if (event.key === 'Enter') {
            let qr = searchBox.val();
            if (qr !== "") {
                $.fn.validarSession();
                let result = listadata.find(item => item.qr === qr);
                if (result !== undefined) {
                    let codpro = result.codpro;
                    $('#modal-stocks').modal('show');
                    let codalm = $('#almacen').val();
                    let tabla = $('#table-stocks').DataTable();
                    if (tabla) {
                        tabla.destroy();
                    }
                    searchBox.focus();
                    tabla = $("#table-stocks").DataTable({
                        "lengthChange": false,
                        searching: false,
                        paging: false,
                        fixedHeader: true,

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
                        }, ajax: {
                            url: 'CRUDFaStockAlmacenes?opcion=3',
                            type: 'POST',
                            "data": function (d) {
                                d.codpro = codpro;
                                d.codalm = codalm;
                            },
                            beforeSend: function () {
                                $("#loadingstocks").css("display", "block");
                            }, complete: function () {
                                $("#loadingstocks").css("display", "none");
                            }
                        },
                        columns: [
                            {data: 'desalm'},
                            {data: 'stkalm'},
                            {data: 'stkalm_m'}
                        ]
                    });
                } else {

                }
            } else {
            }

        }
    });
    async function generatePDF(products) {
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

        const boxWidthMm = 100; // 100 mm
        const boxHeightMm = 50; // 50 mm
        const marginMm = 5; // 5 mm

        // Convertir milímetros a puntos (1 mm = 2.835 puntos)
        const boxWidth = boxWidthMm * 2.835;
        const boxHeight = boxHeightMm * 2.835;
        const margin = marginMm * 2.835;

        let productsPerRow = 2;
        if (products.length === 1) {
            productsPerRow = 1;
        }
        const totalColumns = Math.ceil(products.length / productsPerRow);

        const pageWidth = (boxWidth + margin) * totalColumns + margin;
        const pageHeight = (boxHeight + margin) * productsPerRow + margin;

        const page = pdfDoc.addPage([pageWidth, pageHeight]);

        const font = await pdfDoc.embedFont(StandardFonts.Helvetica);
        const fontb = await pdfDoc.embedFont(StandardFonts.HelveticaBold);

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

            const nameFontSize = 24;
            const nameMarginBottom = 28; // Space between name and price


            // Calculate y position for name
            let textYPosition = yPosition + boxHeight - nameMarginBottom;

            let tam = nameFontSize;
            let nuevotam = fontb.widthOfTextAtSize(product.despro, tam);
            let maxtam = boxWidth - 20;

            while (nuevotam > maxtam) {
                tam -= 2;
                textYPosition += 0.25;
                nuevotam = fontb.widthOfTextAtSize(product.despro, tam);
            }
            const textWidth = fontb.widthOfTextAtSize(product.despro, tam);
            const textXPosition = xPosition + (boxWidth - textWidth) / 2;

            page.drawText(product.despro, {
                x: textXPosition,
                y: textYPosition,
                size: tam,
                font: fontb,
                color: rgb(0, 0, 0)
            });




            page.drawText(product.codpro, {
                x: xPosition + boxWidth / 2 - 50,
                y: yPosition + 10,
                size: 50,
                font: fontb,
                color: rgb(0, 0, 0)
            });


            page.drawText(product.codlab, {
                x: xPosition + 30,
                y: yPosition + 10,
                size: 13.5, // Cambiado a tamaño 20
                font: fontb,
                color: rgb(0, 0, 0),
                rotate: PDFLib.degrees(90)  // Rotación de 90 grados
            });
            page.drawText("LOLFAR", {
                x: xPosition + 60,
                y: yPosition + 10,
                size: 10, // Cambiado a tamaño 20
                font: font,
                color: rgb(0, 0, 0),
                rotate: PDFLib.degrees(90)  // Rotación de 90 grados
            });

            const code5Size = 10;
            const code5Text = product.codpro;
            const code5YPosition = yPosition + 5;
            if (product.codpro !== undefined) {
                const code13Text = product.codpro;
                try {
                    const barcodeImage = await generateBarcodeBase64(code13Text); // Generar imagen en base64
                    const response = await fetch(barcodeImage); // Obtener la imagen a partir de base64
                    const barcodeImageBytes = await response.arrayBuffer(); // Convertir a array buffer
                    const embeddedImage = await pdfDoc.embedPng(barcodeImageBytes); // Incrustar PNG en el PDF
                    const barcodeImageEmbed = embeddedImage;
                    try {
                        page.drawImage(barcodeImageEmbed, {
                            x: xPosition + 20,
                            y: yPosition + boxHeight - 90,
                            width: boxWidth - 40,
                            height: 50
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
        const blobUrl = URL.createObjectURL(blob);

        // Abrir el PDF en una nueva pestaña o ventana
        window.open(blobUrl, '_blank');
    }
});