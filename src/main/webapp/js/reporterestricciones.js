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
    $.getJSON("CRUDFaAlmacenes?opcion=2", function (data) {
        if (data.resultado === "ok") {
            let almacen = $('#almacen');
            almacen.empty();
            almacen.append('<option value="">TODOS</option>');
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
    $("#btnimprimir").click(function () {
        let sec = $("#almacen").val();
        window.open("ReporteRestricciones?tipo=pdf&codalm=" + sec, "_blank");
    });
    $("#btnimprimirexcel").click(function () {
        let sec = $("#almacen").val();
        window.open("ReporteRestricciones?tipo=excel&codalm=" + sec, "_blank");
    });
});