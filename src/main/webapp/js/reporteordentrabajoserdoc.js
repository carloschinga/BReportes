$(document).ready(function () {
    let central = false;
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
    let pik_fecini = localStorage.getItem('rep_fecini');
    let pik_fecfin = localStorage.getItem('rep_fecfin');
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
                    "url": "picking?opcion=43",
                    "data": function (d) {
                        d.fechainicio = fechainicio;
                        d.fechafin = fechafin;
                    }
                },
                "columns": [
                    {"data": "ortrcod"},
                    {"data": "fecha"},
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
    $.fn.listarsecuencias();
    $('#inputFechaInicio').change(function () {
        let fechaFin = $("#inputFechaInicio").val();
        $("#inputFechaFin").val(fechaFin);
    });
    $("#inprimirfec").click(function () {
        $.fn.listarsecuencias();
    });
    $.getJSON("validarsesion", function (data) {
        if (data.resultado === "ok") {
            if (data.central === "S") {
                $("#divalmacen").show();
                central = true;
                $.getJSON("CRUDFaAlmacenes?opcion=2", function (data) {//ant 2, act 33
                    if (data.resultado === "ok") {
                        let almacen = $('#almacen');
                        almacen.empty();
                        almacen.append('<option value="">TODOS</option>');
                        $.each(data.data, function (key, value) {
                            //if(value.codalm!=="A1" && value.codalm!=="A2")
                            almacen.append('<option value="' + value.siscod + '">' + value.desalm + '</option>');
                        });
                    } else {
                        if (data.mensaje === "nosession") {
                            $.fn.validarSession();
                        } else {
                            alert("Error: Problemas con el servidor.");
                        }
                    }
                });
            }
        } else {
            $(location).attr('href', "index.html");
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        $(location).attr('href', "index.html");
    });
    $("#btnPDF").click(function () {
        inprimir("pdf");
    });
    $("#btnEXCEL").click(function () {
        inprimir("excel");
    });
    $('#table-secuencias').on('click', '.pdf', function () {
        $.fn.validarSession();
        let sec = $(this).data('id');
        let siscod = $("#almacen").val();
        if (siscod === undefined || siscod === null) {
            siscod = "";
        }
        window.open("ReporteOrdenTrabajoSerdoc?tipo=pdf&orden=" + sec + "&siscod=" + siscod, "_blank");
    });
    $('#table-secuencias').on('click', '.excel', function () {
        $.fn.validarSession();
        let sec = $(this).data('id');
        let siscod = $("#almacen").val();
        if (siscod === undefined || siscod === null) {
            siscod = "";
        }
        window.open("ReporteOrdenTrabajoSerdoc?tipo=excel&orden=" + sec + "&siscod=" + siscod, "_blank");
    });
    function inprimir(tipo) {
        let sec = $("#secuencia").val();
        let siscod = $("#almacen").val();
        if (sec !== "") {
            if (central) {
                let siscod = $("#almacen").val();
                if (siscod === undefined || siscod === null) {
                    siscod = "";
                }
                window.open("ReporteOrdenTrabajoSerdoc?siscod=" + siscod + "&tipo=" + tipo + "&orden=" + sec, "_blank");
            } else {
                window.open("ReporteOrdenTrabajoSerdoc?tipo=" + tipo + "&orden=" + sec + "&siscod=" + siscod, "_blank");
            }
        }
    }
});