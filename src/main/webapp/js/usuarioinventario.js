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
                "url": "CRUDUsuarioInventario?opcion=1"
            },
            "columns": [
                {"data": "usecod"},
                {"data": "useusr"},
                {"data": "usenam"},
                {"data": null, "render": function (data, type, row) {
                        return '<button type="button" class="btn btn-primary btn-sm edit-btn" data-usecod="' + row.usecod + '" data-useusr="' + row.useusr + '" data-usenam="' + row.usenam + '" data-toggle="tooltip" data-placement="top" title="Editar"><i class="fas fa-edit"></i></button><button type="button" class="btn btn-danger btn-sm delete-btn" data-usecod="' + row.usecod + '" data-toggle="tooltip" data-placement="top" title="Eliminar"><i class="fas fa-trash"></i></button>';
                    }
                }
            ]
        });
    };
    $.fn.listar();
    $("#botonmodalagregar").on("click", function () {
        $('#addUserModal').modal('show');

    });
    // Agregar usuario
    $('#addUserForm').on('submit', function (e) {
        e.preventDefault();
        let usenam = $('#addUsenam').val();
        let useusr = $('#addUseusr').val();
        let pass = CryptoJS.SHA256($("#addPass").val()).toString(CryptoJS.enc.Hex);

        $.getJSON("CRUDUsuarioInventario?opcion=2", {usenam: usenam, useusr: useusr, usepas: pass}, function (data) {
            if (data.resultado === "ok") {
                $('#addUserModal').modal('hide');
                $.fn.listar();
            } else {
                if (data.mensaje === "nosession") {
                    $.fn.validarSession();
                } else if (data.mensaje === "encontrado") {
                    alert("Ya existe este usuario");
                } else {
                    alert("Error al agregar el usuario");
                }
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            alert("Error: No se logro hacer conexion con el servidor");
        });
    });

    // Editar usuario
    $('#usuariosTable tbody').on('click', '.edit-btn', function () {
        $('#editUsecod').val(String($(this).data("usecod")));
        $('#editUsenam').val(String($(this).data("usenam")));
        $('#editUseusr').val(String($(this).data("useusr")));
        $('#editUserModal').modal('show');
    });

    $('#editUserForm').on('submit', function (e) {
        e.preventDefault();
        let usecod = $('#editUsecod').val();
        let usenam = $('#editUsenam').val();
        let useusr = $('#editUseusr').val();
        $.getJSON("CRUDUsuarioInventario?opcion=3", {usenam: usenam, useusr: useusr, usecod: usecod}, function (data) {
            if (data.resultado === "ok") {
                $.fn.listar();
                $('#editUserModal').modal('hide');
            } else {
                if (data.mensaje === "nosession") {
                    $.fn.validarSession();
                } else if (data.mensaje === "encontrado") {
                    alert("Ya existe este usuario");
                } else {
                    alert("Error al modificar el usuario");
                }
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            alert("Error: No se logro hacer conexion con el servidor");
        });
    });

    // Eliminar usuario
    $('#usuariosTable tbody').on('click', '.delete-btn', function () {
        $('#deleteUsecod').val(String($(this).data("usecod")));
        $('#deleteUserModal').modal('show');
    });

    $('#confirmDelete').on('click', function () {
        let usecod = $('#deleteUsecod').val();
        $.getJSON("CRUDUsuarioInventario?opcion=4", {usecod: usecod}, function (data) {
            if (data.resultado === "ok") {
                $.fn.listar();
                $('#deleteUserModal').modal('hide');
            } else {
                if (data.mensaje === "nosession") {
                    $.fn.validarSession();
                } else {
                    alert("Error al eliminar el usuario");
                }
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            alert("Error: No se logro hacer conexion con el servidor");
        });
    });
});