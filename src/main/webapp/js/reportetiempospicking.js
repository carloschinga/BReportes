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
    $.getJSON("CRUDUsuarioBartolito?opcion=1", function (data) {
        if (data.resultado === "ok") {
            let operario = $('#operario');
            operario.empty();
            operario.append('<option value="">TODOS</option>');
            $.each(data.data, function (key, value) {
                operario.append('<option value="' + value.usecod + '">' + value.usenam + '</option>');
            });
        } else {
            if (data.mensaje === "nosession") {
                $.fn.validarSession();
            } else {
                alert("Error: Problemas con el servidor.");
            }
        }
    });
    $("#btnPDF").click(function () {
        $.fn.validarSession();
        let sec1 = $('#secuencia').val();
        let fechainicio = $("#inputFechaInicio").val();
        let fechafin = $("#inputFechaFin").val();
        let operario = $("#operario").val();
        if (!$('#checksecuencia').is(':checked')) {
        } else {
            sec2 = "";
        }
        let url = 'reportetiempospicking?tipo=pdf&fechafin=' + fechafin + '&fechainicio=' + fechainicio + '&orden=' + sec1+"&operario="+operario;

        window.open(url, "_blank");
    });
    $("#btnEXCEL").click(function () {
        $.fn.validarSession();
        let sec1 = $('#secuencia').val();
        let fechainicio = $("#inputFechaInicio").val();
        let fechafin = $("#inputFechaFin").val();
        let operario = $("#operario").val();
        if (!$('#checksecuencia').is(':checked')) {
        } else {
            sec2 = "";
        }
        let url = 'reportetiempospicking?tipo=excel&fechafin=' + fechafin + '&fechainicio=' + fechainicio + '&orden=' + sec1+"&operario="+operario;

        window.open(url, "_blank");
    });
    $('#inputFechaInicio').change(function () {
        let fechaFin = $("#inputFechaInicio").val();
        $("#inputFechaFin").val(fechaFin);
    });
});