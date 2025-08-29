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
    $("#btnimprimir").click(function () {
        window.open("ReporteQR?tipo=pdf", "_blank");
    });
    $("#btnimprimirexcel").click(function () {
        window.open("ReporteQR?tipo=excel", "_blank");
    });
    $("#btnimprimirfrac").click(function () {
        window.open("ReporteFracProductos?tipo=pdf", "_blank");
    });
    $("#btnimprimirexcelfrac").click(function () {
        window.open("ReporteFracProductos?tipo=excel", "_blank");
    });
});