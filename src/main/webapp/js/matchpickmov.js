$(document).ready(function () {
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        }
    });
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
    let pik_fecini = localStorage.getItem('pik_fecini');
    let pik_fecfin = localStorage.getItem('pik_fecfin');
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
                    "url": "picking?opcion=53", //"url": "picking?opcion=2", "url": "picking?opcion=53",
                    "data": function (d) {
                        d.fechainicio = fechainicio;
                        d.fechafin = fechafin;
                    }
                },
                "columns": [
                    {"data": "ortrcod"},
                    {"data": "fecha"},
                    {"data": "avance", "render": function (data, type, row) {
                            if (row.completado !== undefined && row.completado === "S" && 1 !== 1) {
                                return Math.round(data) + "% <i class='fa fa-check'></i>";
                            } else {
                                return Math.round(data) + "%";
                            }
                        }
                    },
                    {"data": "ortrcod", "render": function (data, type, row) {
                            return '<button class="btn ' + (row.chktxt === "S" ? 'btn-primary' : 'btn-danger') + '" data-id=' + data + ' ><i class="fas fa-check"></i></button>';
                        }
                    }
                ]
            });
        } else {
            alert("seleccione las fechas");
        }
    };
    $("#table-secuencias").on("click", ".btn", function () {
        let cod = $(this).data("id");
        $.getJSON("picking?opcion=52&codpicklist=" + cod, function (data) {
            if (data.resultado === "ok") {
                Swal.fire({
                    title: "Picking convalidada con exito a la secuencia",
                    text: "",
                    icon: "success"
                });
                $.fn.listarsecuencias();
            } else {
                Swal.fire({
                    title: "Error al matchear picking",
                    text: "",
                    icon: "error"
                });
            }$(document).ready(function () {
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        }
    });
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
    let pik_fecini = localStorage.getItem('pik_fecini');
    let pik_fecfin = localStorage.getItem('pik_fecfin');
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
                    "url": "picking?opcion=53", //"url": "picking?opcion=2", "url": "picking?opcion=53",
                    "data": function (d) {
                        d.fechainicio = fechainicio;
                        d.fechafin = fechafin;
                    }
                },
                "columns": [
                    {"data": "ortrcod"},
                    {"data": "fecha"},
                    {"data": "avance", "render": function (data, type, row) {
                            if (row.completado !== undefined && row.completado === "S" && 1 !== 1) {
                                return Math.round(data) + "% <i class='fa fa-check'></i>";
                            } else {
                                return Math.round(data) + "%";
                            }
                        }
                    },
                    {"data": "ortrcod", "render": function (data, type, row) {
                            return '<button class="btn ' + (row.chktxt === "S" ? 'btn-primary' : 'btn-danger') + '" data-id=' + data + ' ><i class="fas fa-check"></i></button>';
                        }
                    }
                ]
            });
        } else {
            alert("seleccione las fechas");
        }
    };
    $("#table-secuencias").on("click", ".btn", function () {
        let cod = $(this).data("id");
        $.getJSON("picking?opcion=52&codpicklist=" + cod, function (data) {
            if (data.resultado === "ok") {
                Swal.fire({
                    title: "Picking convalidada con exito a la secuencia",
                    text: "",
                    icon: "success"
                });
                $.fn.listarsecuencias();
            } else if(data.mensaje="notxt"){
                Swal.fire({
                    title: "Aun no se ha generado un TXT en este picking",
                    text: "",
                    icon: "error"
                });
                
            }else{
                Swal.fire({
                    title: "Error al matchear picking",
                    text: "",
                    icon: "error"
                });
            }
        });
    });

    $.fn.validarSession();
    $.fn.listarsecuencias();
    $('#inputFechaInicio').change(function () {
        let fechaFin = $("#inputFechaInicio").val();
        $("#inputFechaFin").val(fechaFin);
    });
    $("#inprimirfec").click(function () {

        $.fn.listarsecuencias();
    });

});
        });
    });

    $.fn.validarSession();
    $.fn.listarsecuencias();
    $('#inputFechaInicio').change(function () {
        let fechaFin = $("#inputFechaInicio").val();
        $("#inputFechaFin").val(fechaFin);
    });
    $("#inprimirfec").click(function () {

        $.fn.listarsecuencias();
    });

});