$(document).ready(function () {
    $.fn.validarSession = function () {
        $.getJSON("validarsesion", function (data) {
            if (data.resultado === "ok") {
            } else {
                $(location).attr('href', "index.html");
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            $(location).attr('href', "index.html");
        });
    };
    $.fn.validarSession();
    function imprimir() {
        let val = String($('#impresion-input').val().trim());
        if (val !== undefined && val !== null && val !== "") {
            $.getJSON('picking', {opcion: 21, caja: val}, function (response) {
                if (response.resultado === 'ok') {
                    if (response.data.length > 0) {
                        $('#impresion-input').val("");
                        generatePDF(response.data);
                    } else {
                        alert("No se encontro el bulto");
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
    }
    ;
    $('#impresion-input').on('keypress', function (e) {
        if (e.which == 13) { // Si presiona Enter
            imprimir();
        }
    });

    $('#validate-impresion').on('click', function () {
        imprimir();
    });

    async function generatePDF(products) {
        const {PDFDocument, rgb, StandardFonts} = PDFLib;
        const pdfDoc = await PDFDocument.create();

        // Definir las nuevas dimensiones en milímetros
        const boxWidthMm = 60; // 100 mm
        const boxHeightMm = 47; // 50 mm
        const marginMm = 3; // 5 mm
        const marginderMn =10;

        // Convertir milímetros a puntos (1 mm = 2.835 puntos)
        const boxWidth = boxWidthMm * 2.835;
        const boxHeight = boxHeightMm * 2.835;
        const margin = marginMm * 2.835;
        const marginder = marginderMn * 2.835;

        let productsPerRow = 2;
        if (products.length === 1) {
            productsPerRow = 1;
        }
        const totalColumns = Math.ceil(products.length / productsPerRow);

        const pageWidth = (boxWidth + margin) * totalColumns + margin + marginder;
        const pageHeight = (boxHeight + margin) * productsPerRow + margin;

        const page = pdfDoc.addPage([pageWidth, pageHeight]);

        const font = await pdfDoc.embedFont(StandardFonts.Helvetica);
        const fontb = await pdfDoc.embedFont(StandardFonts.HelveticaBold);

        products.forEach((product, index) => {
            const rowIndex = index % productsPerRow;
            const columnIndex = Math.floor(index / productsPerRow);

            const xPosition = margin + columnIndex * (boxWidth + margin) + marginder;
            const yPosition = pageHeight - (rowIndex + 1) * (boxHeight + margin);

            page.drawRectangle({
                x: xPosition,
                y: yPosition,
                width: boxWidth,
                height: boxHeight,
                borderColor: rgb(0, 0, 0),
                borderWidth: 1
            });

            const nameFontSize = 45;
            const nameMarginTop = 30;
            const nameMarginBottom = 50;
            const espaciado = 0.5;
            const maxTextHeight = boxHeight - nameMarginTop - nameMarginBottom;

            const initialSmallFontSize = 18;
            const initialValueFontSize = 24;

            // Posiciones y texto de ejemplo
            let textYPosition = yPosition + boxHeight - nameMarginBottom;
            let palabras = product.destino.split(" ");
            palabras.shift();
            let nombre = palabras.join(" ");

            let tam = nameFontSize;
            let nuevotam = fontb.widthOfTextAtSize(nombre, tam);
            let maxtam = boxWidth - 20;

            while (nuevotam > maxtam) {
                tam -= 2;
                nuevotam = fontb.widthOfTextAtSize(nombre, tam);
            }
            const textWidth = fontb.widthOfTextAtSize(nombre, tam);
            const textXPosition = xPosition + (boxWidth - textWidth) / 2;

            page.drawText(nombre, {
                x: textXPosition,
                y: textYPosition,
                size: tam,
                font: fontb,
                color: rgb(0, 0, 0)
            });

            textYPosition -= tam + espaciado;

            let ordenTransLabel = `ORDEN TRANS: `;
            let ordenTransValue = `${product.orden}`;
            let bultoLabel = `BULTO: `;
            let bultoValue = `${product.caja}`;
            const totalCountText = `NUMERO:`;
            const currentCountText = `${product.cant}`;

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
            let ordenFontSizes = adjustFontSize(ordenTransLabel + totalCountText, ordenTransValue, initialSmallFontSize, initialValueFontSize);
            let bultoFontSizes = adjustFontSize(bultoLabel + totalCountText, bultoValue, initialSmallFontSize, initialValueFontSize);
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
            /*
             let tam = ordenFontSizes.labelSize;
             let nuevotam = fontb.widthOfTextAtSize(totalCountText, tam);
             let maxtam = fontb.widthOfTextAtSize(currentCountText, ordenFontSizes.valueSize);
             
             while (nuevotam > maxtam) {
             tam -= 1;
             nuevotam = font.widthOfTextAtSize(totalCountText, tam);
             }
             */
            page.drawText(totalCountText, {
                x: totalXPosition,
                y: totalYPosition,
                size: ordenFontSizes.labelSize,
                font: font,
                color: rgb(0, 0, 0)
            });

            page.drawText(currentCountText, {
                x: totalXPosition+margin,
                y: bultoYPosition,
                size: ordenFontSizes.valueSize,
                font: fontb,
                color: rgb(0, 0, 0)
            });
        });
        const pdfBytes = await pdfDoc.save();
        const blob = new Blob([pdfBytes], {type: 'application/pdf'});
        const blobUrl = URL.createObjectURL(blob);

        // Abrir el PDF en una nueva pestaña o ventana
        window.open(blobUrl, '_blank');
    }
});