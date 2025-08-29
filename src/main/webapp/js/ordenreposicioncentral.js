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
                "url": "CRUDFaOrdenReposicion?opcion=5"
            },
            "columns": [
                {"data": "invnum"},
                {"data": "sisent"},
                {"data": "feccre"},
                {"data": "usenam"},
                {"data": "enviado", "render": function (data, type, row) {
                        return '<button type="button" class="btn btn-primary btn-sm edit-btn" data-sisent="'+row.sisent+'" data-invnum="' + row.invnum + '"  data-toggle="tooltip" data-placement="top" title="Ver"><i class="fas fa-eye"></i></button>';
                    }
                }
            ],
            "order": [[0, "desc"]]
        });
    };
    $.fn.listar();
    $('#usuariosTable tbody').on('click', '.edit-btn', function () {
        let invnum = String($(this).data("invnum"));
        let sisent = String($(this).data("sisent"));
        localStorage.setItem('ordenreposicion', invnum);
        localStorage.setItem('sisent', sisent);

        $("#contenido").load('ordenreposiciondetallecentral.html');
    });
});