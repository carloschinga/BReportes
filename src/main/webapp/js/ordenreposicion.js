$(document).ready(function () {

    var tabla = $('#usuariosTable').DataTable();
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

    $.fn.listar = function () {
        if (tabla) {
            tabla.destroy();
        }
        tabla = $('#usuariosTable').DataTable({
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
                "url": "CRUDFaOrdenReposicion?opcion=1"
            },
            "columns": [
                {"data": "invnum"},
                {"data": "feccre"},
                {"data": "usenam"},
                {"data": "enviado", "render": function (data, type, row) {
                        if (data === undefined) {
                            return '<button type="button" class="btn btn-primary btn-sm edit-btn" data-invnum="' + row.invnum + '"  data-toggle="tooltip" data-placement="top" title="Editar"><i class="fas fa-edit"></i></button><button type="button" class="btn btn-warning btn-sm aprobar-btn" data-invnum="' + row.invnum + '" data-toggle="tooltip" data-placement="top" title="aprobar"><i class="fas fa-check"></i></button><button type="button" class="btn btn-danger btn-sm delete-btn" data-invnum="' + row.invnum + '" data-toggle="tooltip" data-placement="top" title="Eliminar"><i class="fas fa-trash"></i></button>';
                        } else {
                            return 'Ya enviado';
                        }
                    }
                }
            ],
            "order": [[0, "desc"]]
        });
    };
    $.fn.listar();
    // Agregar usuario
    $('#agregar').click(function () {
        $.getJSON("CRUDFaOrdenReposicion?opcion=2", function (data) {
            if (data.resultado === "ok") {
                $.fn.listar();
            } else {
                if (data.mensaje === "nosession") {
                    $.fn.validarSession();
                } else {
                    alert("Error al agregar la orden");
                }
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            alert("Error: No se logro hacer conexion con el servidor");
        });
    });
    $('#usuariosTable tbody').on('click', '.delete-btn', function () {
        let invnum = String($(this).data("invnum"));
        if (confirm("Estas seguro que quieres eliminar esta orden")) {
            $.getJSON("CRUDFaOrdenReposicion?opcion=3", {invnum: invnum}, function (data) {
                if (data.resultado === "ok") {
                    $.fn.listar();
                } else {
                    if (data.mensaje === "nosession") {
                        $.fn.validarSession();
                    } else {
                        alert("Error al eliminar la orden de reposicion");
                    }
                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
                alert("Error: No se logro hacer conexion con el servidor");
            });
        }
    });
    $('#usuariosTable tbody').on('click', '.aprobar-btn', function () {
        let invnum = String($(this).data("invnum"));
        if (confirm("Estas seguro que quieres enviar esta orden")) {
            $.getJSON("CRUDFaOrdenReposicion?opcion=4", {invnum: invnum}, function (data) {
                if (data.resultado === "ok") {
                    $.fn.listar();
                } else {
                    if (data.mensaje === "nosession") {
                        $.fn.validarSession();
                    } else {
                        alert("Error al enviar la orden");
                    }
                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
                alert("Error: No se logro hacer conexion con el servidor");
            });
        }
    });
    $('#usuariosTable tbody').on('click', '.edit-btn', function () {
        let invnum = String($(this).data("invnum"));
        localStorage.setItem('ordenreposicion', invnum);

        $("#contenido").load('ordenreposiciondetalle.html');
    });
});