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
    let today = new Date().toISOString().slice(0, 10);
    let pik_fecini = localStorage.getItem('seg_fecini');
    let pik_fecfin = localStorage.getItem('seg_fecfin');
    if (pik_fecini === undefined || pik_fecini === "" || pik_fecini === null) {
        $('#inputFechaInicio').val(today);
        $('#inputFechaFin').val(today);
    }else{
        $('#inputFechaInicio').val(pik_fecini);
        $('#inputFechaFin').val(pik_fecfin);
    }
    $.fn.validarSession();
    $('#inputFechaInicio').change(function () {
        let fechaFin = $("#inputFechaInicio").val();
        $("#inputFechaFin").val(fechaFin);
    });
    $("#inprimirfec").click(function () {
        let fecini=$('#inputFechaInicio').val();
        let fecfin=$('#inputFechaFin').val();
        if (fecini !== "" || fecini !== undefined ) {
            localStorage.setItem('seg_sec', "");
            localStorage.setItem('seg_fecini', fecini);
            localStorage.setItem('seg_fecfin', fecfin);
            let url = 'seguimientoestablecimiento.html';
            $("#contenido").load(url);
        } else {
            alert("Ingrese la fecha");
        }
    });
    $("#inprimirsec").click(function () {
        $.fn.validarSession();
        //let fechainicio = $("#inputFechaInicio").val();
        //let fechafin = $("#inputFechaFin").val();
        let sec = $('#secuencia').val();
        //fechainicio !== "" || fechafin !== ""
        if (sec !== "") {
            localStorage.setItem('seg_sec', sec);
            localStorage.setItem('seg_fecini', "");
            localStorage.setItem('seg_fecfin', "");
            let url = 'seguimientoestablecimiento.html';
            $("#contenido").load(url);
        } else {
            alert("Eligue la orden a buscar");
        }
    });
});