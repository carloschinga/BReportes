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
    let id;
    $.fn.listarsecuencias = function () {
        $.fn.validarSession();
        let sec = $("#secuencia").val();
        var tabla = $('#table-secuencias').DataTable();
        if (tabla) {
            tabla.destroy();
        }
        if (sec !== "") {
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
                    "url": "picking?opcion=23",
                    "data": function (d) {
                        d.orden = sec;
                    }
                },
                "columns": [
                    {"data": "ortrcod"},
                    {"data": "sisent"},
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
                            if (row.completado==="S") {
                                return '<button class="btn ord btn-info" data-id=' + data + ' data-siscod=' + row.siscod + '>Abrir</button>';
                            } else {
                                return 'Abierto.';
                            }
                        }
                    }
                ]
            });
        }
    };
    $.fn.validarSession();
    $("#inprimirsec").click(function () {
        $.fn.validarSession();
        //let fechainicio = $("#inputFechaInicio").val();
        //let fechafin = $("#inputFechaFin").val();
        let sec = $('#secuencia').val();
        //fechainicio !== "" || fechafin !== ""
        if (sec !== "") {
            id = sec;
            $.fn.listarsecuencias();
        } else {
            alert("Eligue la orden a abrir");
        }
    });
    $('#table-secuencias').on('click', '.ord', function () {
        //let fechainicio = $("#inputFechaInicio").val();
        //let fechafin = $("#inputFechaFin").val();
        let sec = $(this).data('id');
        let siscod = $(this).data('siscod');
        //fechainicio !== "" || fechafin !== ""
        if (sec !== "") {
            if (confirm("Estas seguro que quieres abrir la recepcion de este establecimiento?")) {
                $.getJSON("picking?opcion=24&chk=N&ord=" + sec + "&siscod=" + siscod, function (data) {
                    if (data.resultado === "ok") {
                        $.fn.listarsecuencias();
                    } else {
                        if (data.mensaje === "nosession") {
                            $.fn.validarSession();
                        } else {
                            alert("Error general al abrir la ot");
                        }
                    }
                }).fail(function (jqXHR, textStatus, errorThrown) {
                    alert("Error de conexion con el servidor al abrir la ot");
                });
            }
        } else {
            alert("Error: No se encontro la orden, vuelve a cargar");
        }
    });
});