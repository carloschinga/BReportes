$(document).ready(function () {
    let searchBox;
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


    let today = new Date().toISOString().slice(0, 10);
    let pik_fecini = sessionStorage.getItem('caja_fecini');
    let pik_fecfin = sessionStorage.getItem('caja_fecfin');
    if (pik_fecini === undefined || pik_fecini === "" || pik_fecini === null) {
        $('#inputFechaInicio').val(today);
        $('#inputFechaFin').val(today);
    } else {
        $('#inputFechaInicio').val(pik_fecini);
        $('#inputFechaFin').val(pik_fecfin);
    }
    $.fn.listarsecuencias = function () {
        $.fn.validarSession();
        let fechainicio = $("#inputFechaInicio").val();
        let fechafin = $("#inputFechaFin").val();
        var tabla = $('#table-secuencias').DataTable();
        if (tabla) {
            tabla.destroy();
        }
        if (fechainicio !== "" && fechafin !== "") {
            var table = $('#table-secuencias').DataTable({
                paginate: false,
                searching: false,
                fixedHeader: {
                    header: true,
                    footer: true
                },
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
                    "url": "picking?opcion=18",
                    "data": function (d) {
                        d.fechainicio = fechainicio;
                        d.fechafin = fechafin;
                    }
                },
                "columns": [
                    {"data": "ortrcod"},
                    {"data": "fecha"},
                    /*{
                     "data": "usuario",
                     "render": function (data, type, row) {
                     return data ? data : '';
                     }
                     },*/
                    {"data": "ortrcod", "render": function (data, type, row) {
                            return '<button class="btn pdf btn-info" data-id=' + data + ' ><i class="fas fa-file-pdf"></i></button>';
                        }
                    }
                ]
            });
        } else {
            alert("seleccione las fechas");
        }
    };
    $.fn.listarsecuencias();
    $('#inputFechaInicio').change(function () {
        let fechaFin = $("#inputFechaInicio").val();
        $("#inputFechaFin").val(fechaFin);
    });
    $("#inprimirfec").click(function () {
        let fechainicio = $("#inputFechaInicio").val();
        let fechafin = $("#inputFechaFin").val();
        sessionStorage.setItem('caja_fecini', fechainicio);
        sessionStorage.setItem('caja_fecfin', fechafin);

        $.fn.listarsecuencias();
    });
    $('#table-secuencias').on('click', '.pdf', function () {
        let sec = $(this).data('id');
        $.getJSON("picking", {opcion: "17", ord: sec}, function (data) {
            if (data.resultado === "ok") {
                generatePDF(data.data);
            } else if (data.mensaje === "nosession") {
                $.fn.validarSession();
            } else {
                alert("Error con el servidor.");
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            alert("Error de conexion con el servidor: " + textStatus);
        });
    });
    $("#btnPDF").click(function () {
        let sec = $("#secuencia").val();
        if (sec !== "") {
            $.getJSON("picking", {opcion: "17", ord: sec}, function (data) {
                if (data.resultado === "ok") {
                    generatePDF(data.data);
                } else if (data.mensaje === "nosession") {
                    $.fn.validarSession();
                } else {
                    alert("Error con el servidor.");
                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
                alert("Error de conexion con el servidor: " + textStatus);
            });
        }
    });

    async function generatePDF(products) {
        const {PDFDocument, rgb, StandardFonts} = PDFLib;
        const pdfDoc = await PDFDocument.create();

        const boxWidthCm = 6.5;
        const boxHeightCm = 4;
        const marginCm = 0.2;

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
        const fontb = await pdfDoc.embedFont(StandardFonts.HelveticaBold);

        products.forEach((product, index) => {
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

            const nameFontSize = 28;
            const nameMarginTop = 30;
            const nameMarginBottom = 50;
            const espaciado = 0.5;
            const maxTextHeight = boxHeight - nameMarginTop - nameMarginBottom;

            const initialSmallFontSize = 14;
            const initialValueFontSize = 20;

            // Posiciones y texto de ejemplo
            let textYPosition = yPosition + boxHeight - nameMarginBottom;
            let palabras = product.destino.split(" ");
            palabras.shift();
            let nombre = palabras.join(" ");
            const textWidth = font.widthOfTextAtSize(nombre, nameFontSize);
            const textXPosition = xPosition + (boxWidth - textWidth) / 2;

            page.drawText(nombre, {
                x: textXPosition,
                y: textYPosition,
                size: nameFontSize,
                font: fontb,
                color: rgb(0, 0, 0)
            });

            textYPosition -= nameFontSize + espaciado;

            let ordenTransLabel = `ORDEN TRANS: `;
            let ordenTransValue = `${product.orden}`;
            let bultoLabel = `BULTO: `;
            let bultoValue = `${product.caja}`;
            const totalCountText = `TOTAL BUL`;
            const currentCountText = `${index + 1} de ${products.length}`;

            let maxTextWidth = boxWidth - 20;
            const rightMargin = 5;

            // Función para ajustar dinámicamente el tamaño de la fuente
            function adjustFontSize(label, value, initialLabelSize, initialValueSize) {
                let labelSize = initialLabelSize;
                let valueSize = initialValueSize;

                let labelWidth = font.widthOfTextAtSize(label, labelSize);
                let valueWidth = fontb.widthOfTextAtSize(value, valueSize);

                // Si el ancho total excede el espacio disponible, reducir el tamaño
                while (labelWidth + valueWidth > maxTextWidth) {
                    labelSize -= 0.5; // Reducir tamaño de label
                    valueSize -= 0.5; // Reducir tamaño de valor (más importante)
                    labelWidth = font.widthOfTextAtSize(label, labelSize);
                    valueWidth = fontb.widthOfTextAtSize(value, valueSize);
                }
                return {labelSize, valueSize};
            }

            // Ajustar el tamaño de "ORDEN TRANS" y "BULTO"
            let ordenFontSizes = adjustFontSize(ordenTransLabel, ordenTransValue + currentCountText, initialSmallFontSize, initialValueFontSize);
            let bultoFontSizes = adjustFontSize(bultoLabel, bultoValue + currentCountText, initialSmallFontSize, initialValueFontSize);
            if (ordenFontSizes.labelSize > bultoFontSizes.labelSize) {
                ordenFontSizes = bultoFontSizes;
            }
            const ordenTransXPosition = xPosition + boxWidth - font.widthOfTextAtSize(ordenTransLabel, ordenFontSizes.labelSize) - fontb.widthOfTextAtSize(ordenTransValue, ordenFontSizes.valueSize) - rightMargin;
            const bultoXPosition = xPosition + boxWidth - font.widthOfTextAtSize(bultoLabel, ordenFontSizes.labelSize) - fontb.widthOfTextAtSize(bultoValue, ordenFontSizes.valueSize) - rightMargin;

            const bultoYPosition = yPosition + 10;
            const ordenTransYPosition = bultoYPosition + espaciado + 20;

            // Dibujar "ORDEN TRANS"
            page.drawText(ordenTransLabel, {
                x: ordenTransXPosition,
                y: ordenTransYPosition,
                size: ordenFontSizes.labelSize,
                font: font,
                color: rgb(0, 0, 0)
            });

            page.drawText(ordenTransValue, {
                x: ordenTransXPosition + font.widthOfTextAtSize(ordenTransLabel, ordenFontSizes.labelSize) + 2,
                y: ordenTransYPosition,
                size: ordenFontSizes.valueSize,
                font: fontb,
                color: rgb(0, 0, 0)
            });

            // Dibujar "BULTO"
            page.drawText(bultoLabel, {
                x: bultoXPosition,
                y: bultoYPosition,
                size: ordenFontSizes.labelSize,
                font: font,
                color: rgb(0, 0, 0)
            });

            page.drawText(bultoValue, {
                x: bultoXPosition + font.widthOfTextAtSize(bultoLabel, ordenFontSizes.labelSize) + 2,
                y: bultoYPosition,
                size: ordenFontSizes.valueSize,
                font: fontb,
                color: rgb(0, 0, 0)
            });

            const totalXPosition = xPosition + 5;
            const totalYPosition = ordenTransYPosition;
            let tam = ordenFontSizes.labelSize;
            let nuevotam = fontb.widthOfTextAtSize(totalCountText, tam);
            let maxtam = fontb.widthOfTextAtSize(currentCountText, ordenFontSizes.valueSize);

            while (nuevotam > maxtam) {
                tam -= 1; 
                nuevotam = font.widthOfTextAtSize(totalCountText, tam);
            }
            page.drawText(totalCountText, {
                x: totalXPosition,
                y: totalYPosition,
                size: tam,
                font: fontb,
                color: rgb(0, 0, 0)
            });

            page.drawText(currentCountText, {
                x: totalXPosition,
                y: bultoYPosition,
                size: ordenFontSizes.valueSize,
                font: fontb,
                color: rgb(0, 0, 0)
            });
        });

        const pdfBytes = await pdfDoc.save();
        const blob = new Blob([pdfBytes], {type: 'application/pdf'});
        const link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = 'EtiquetasEmbalaje.pdf';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }



});