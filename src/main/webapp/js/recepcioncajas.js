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
    let pik_fecini = localStorage.getItem('caja_fecini');
    let pik_fecfin = localStorage.getItem('caja_fecfin');
    if (pik_fecini === undefined || pik_fecini === "" || pik_fecini === null) {
        $('#inputFechaInicio').val(today);
        $('#inputFechaFin').val(today);
    } else {
        $('#inputFechaInicio').val(pik_fecini);
        $('#inputFechaFin').val(pik_fecfin);
    }
    $.fn.validarSession();
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
                    "url": "picking?opcion=43",// ant 12 - nuev 43
                    "data": function (d) {
                        d.fechainicio = fechainicio;
                        d.fechafin = fechafin;
                    }
                },
                "columns": [
                    {"data": "ortrcod"},
                    {"data": "fecha"},
                    {"data": "avance", "render": function (data, type, row) {
                            return Math.round(data) + "%";
                        }
                    },
                    /*{
                     "data": "usuario",
                     "render": function (data, type, row) {
                     return data ? data : '';
                     }
                     },*/
                    {"data": "ortrcod", "render": function (data, type, row) {
                            return '<button class="btn ord btn-info" data-id=' + data + ' ><i class="fas fa-external-link-square-alt"></i></button>';
                        }
                    },
                    {"data": "ortrcod", "render": function (data, type, row) {
                            return '<button class="btn pdf btn-info" data-id=' + data + ' ><i class="fas fa-file-pdf"></i></button><button class="btn excel btn-info" data-id=' + data + ' ><i class="fas fa-file-excel"></i></button>';
                        }
                    }
                ]
            });
        } else {
            alert("seleccione las fechas");
        }
    };
    $.fn.validarSession();
    $.fn.listarsecuencias();
    $('#inputFechaInicio').change(function () {
        let fechaFin = $("#inputFechaInicio").val();
        $("#inputFechaFin").val(fechaFin);
    });
    $("#inprimirfec").click(function () {

        $.fn.listarsecuencias();
    });
    $("#inprimirsec").click(function () {
        $.fn.validarSession();
        //let fechainicio = $("#inputFechaInicio").val();
        //let fechafin = $("#inputFechaFin").val();
        let sec = $('#secuencia').val();
        //fechainicio !== "" || fechafin !== ""
        if (sec !== "") {
            localStorage.setItem('caja_orden', sec);
            let url = 'recepcioncajascaja.html';
            $("#contenido").load(url);
        } else {
            alert("Eligue la orden a buscar");
        }
    });
    $('#table-secuencias').on('click', '.ord', function () {
        $.fn.validarSession();
        let fechainicio = $("#inputFechaInicio").val();
        let fechafin = $("#inputFechaFin").val();
        localStorage.setItem('caja_fecini', fechainicio);
        localStorage.setItem('caja_fecfin', fechafin);
        let sec = $(this).data('id');
        //fechainicio !== "" || fechafin !== ""
        if (sec !== "") {
            localStorage.setItem('caja_orden', sec);
            let url = 'recepcioncajascaja.html';
            $("#contenido").load(url);
        } else {
            alert("Error: No se encontro la orden, vuelve a cargar");
        }
    });
    $('#table-secuencias').on('click', '.pdf', function () {
        $.fn.validarSession();
        let sec = $(this).data('id');
        window.open("ReporteOrdenTransporteCaja?tipo=pdf&orden=" + sec, "_blank");
    });
    $('#table-secuencias').on('click', '.excel', function () {
        $.fn.validarSession();
        let sec = $(this).data('id');
        window.open("ReporteOrdenTransporteCaja?tipo=excel&orden=" + sec, "_blank");
    });
    $("#btnPDF").click(function () {
        let sec = $("#secuencia").val();
        if (sec !== "") {
            window.open("ReporteOrdenTransporteCaja?tipo=pdf&orden=" + sec, "_blank");
        }
    });
    $("#btnEXCEL").click(function () {
        let sec = $("#secuencia").val();
        if (sec !== "") {
            window.open("ReporteOrdenTransporteCaja?tipo=excel&orden=" + sec, "_blank");
        }
    });
});