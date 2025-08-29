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
    $('#inputFechaInicio').val(today);
    $('#inputFechaFin').val(today);
    $.fn.validarSession();
    let id;
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
                    "url": "picking?opcion=2",
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
                            /*
                            if (row.completado !== undefined && row.completado === "S") {
                                return Math.round(data) + "% <i class='fa fa-check'></i>";
                            } else {
                                return Math.round(data) + "%";
                            }
                            */
                        }
                    },
                    /*{
                     "data": "usuario",
                     "render": function (data, type, row) {
                     return data ? data : '';
                     }
                     },*/
                    {"data": "ortrcod", "render": function (data, type, row) {
                            return '<button class="btn ord btn-info" data-id=' + data + ' ><i class="fas fa-trash"></i></button>';
                        }
                    }
                ]
            });
        } else {
            alert("seleccione las fechas");
        }
    };
    $.fn.validarSession();
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
            id = sec;
            $('#alertModal').modal('show');
        } else {
            alert("Eligue la orden a anular");
        }
    });
    $('#table-secuencias').on('click', '.ord', function () {
        $.fn.validarSession();
        //let fechainicio = $("#inputFechaInicio").val();
        //let fechafin = $("#inputFechaFin").val();
        let sec = $(this).data('id');
        //fechainicio !== "" || fechafin !== ""
        if (sec !== "") {
            id = sec;
            $('#alertModal').modal('show');
        } else {
            alert("Error: No se encontro la orden, vuelve a cargar");
        }
    });
    $("#confirmYes").click(function () {
        $.fn.validarSession();
        $('#alertModal').modal('hide');
        console.log('El usuario ha confirmado.');
        $.getJSON("picking?opcion=13&id=" + id, function (data) {
            if (data.resultado === "ok") {
                $.fn.listarsecuencias();
            } else {

            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            alert("Error al eliminar la ot");
        });
    });
});