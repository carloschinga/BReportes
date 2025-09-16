
$(document).ready(function () {
    var tabla;
    $('#addCodpro').select2({
        placeholder: "Selecciona un producto.",
        dropdownParent: $('#addProductoModal'),
        language: {
            noResults: function () {
                return "No se encontraron resultados.";
            }
        }
    });
    $('#editCodpro').on('select2:open', function (e) {
        let searchField = document.querySelector('.select2-search__field');
        if (searchField) {
            searchField.focus(); // Hace focus en el campo de búsqueda
        }
    });
    $('#editCodpro').select2({
        placeholder: "Selecciona un producto.",
        dropdownParent: $('#editProductoModal'),
        language: {
            noResults: function () {
                return "No se encontraron resultados.";
            }
        }
    });
    $('#addCodpro').on('select2:open', function (e) {
        let searchField = document.querySelector('.select2-search__field');
        if (searchField) {
            searchField.focus(); // Hace focus en el campo de búsqueda
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
    $.fn.validarSession();
    $.getJSON("CRUDProductos", {opcion: 5}, function (data) {
        if (data.resultado === "ok") {
            $("#addCodpro").append("<option value='' selected disabled>Selecciona el producto</option>");
            $("#editCodpro").append("<option value='' selected disabled>Selecciona el producto</option>");
            $.each(data.data, function (id, value) {
                $("#addCodpro").append("<option value=" + value.codpro + ">" + value.despro + "</option>");
                $("#editCodpro").append("<option value=" + value.codpro + ">" + value.despro + "</option>");
            });
        }
    });
    $.fn.listar = function () {
        let invnum = sessionStorage.getItem('ordenreposicion');
        $.getJSON("CRUDFaOrdenReposicionDetalle", {opcion: 1, invnum: invnum}, function (data) {
            if (data.resultado === "ok") {
                if (tabla) {
                    tabla.destroy();
                }
                tabla = $('#example').DataTable({
                    data: data.data,
                    paging: false,
                    fixedHeader: {
                        header: true,
                        headerOffset: $('#fixed-title').outerHeight() // Ajusta según el alto de tu cabecera
                    },
                    language: {
                        decimal: "",
                        emptyTable: "No hay datos",
                        info: "Hay un total de _TOTAL_ productos en esta caja",
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
                    columns: [
                        {data: 'numitm'},
                        {data: 'codpro'},
                        {data: 'despro'},
                        {data: 'cante',
                            render: function (data, type, row, meta) {
                                if (data === undefined)
                                    return "";
                                else
                                    return data;
                            }},
                        {data: 'cantf',
                            render: function (data, type, row, meta) {
                                if (data === undefined)
                                    return "";
                                else
                                    return data;
                            }},
                        {data: 'invnum',
                            render: function (data, type, row, meta) {
                                return '<button class="btn btn-sm btn-primary edit-btn" data-codpro="' + row.codpro + '"><i class="fas fa-edit"></i></button><button type="button" class="btn btn-danger btn-sm delete-btn" data-numitm="' + row.numitm + '" data-toggle="tooltip" data-placement="top" title="Eliminar"><i class="fas fa-trash"></i></button>';
                            }
                        }
                    ]
                });
            } else {
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
        });
    };
    $.fn.listar();
    // Abrir modal para agregar
    $('#addProductoBtn').on('click', function () {
        $('#addProductoForm')[0].reset(); // Resetear formulario
        $('#addProductoModal').modal('show');
    });

    // Guardar producto nuevo
    $('#saveAddProducto').on('click', function () {
        let invnum = sessionStorage.getItem('ordenreposicion');
        var codpro = $('#addCodpro').val();
        var cante = $('#addCante').val();
        var cantf = $('#addCantf').val();
        if (codpro !== null && codpro !== undefined && codpro !== "") {
            if (cante !== "" || cantf !== "") {
                $.getJSON("CRUDFaOrdenReposicionDetalle", {opcion: 2, codpro: codpro, cante: cante, cantf: cantf, invnum: invnum}, function (data) {
                    if (data.resultado === "ok") {
                        $.fn.listar();
                        $('#addProductoModal').modal('hide');
                    } else {
                        if (data.mensaje === "nosession") {
                            $.fn.validarSession();
                        } else if (data.mensaje === "cerrado") {
                            alert("Esta orden ya fue enviada");
                        } else {
                            alert("Error al agregar la orden");
                        }
                    }
                }).fail(function (jqXHR, textStatus, errorThrown) {
                    alert("Error: No se logro hacer conexion con el servidor");
                });
                $('#addProductoModal').modal('hide');
            } else {
                alert("Ingresa almenos una cantidad");
            }
        } else {
            alert("Eligue el producto");
        }
    });

    // Abrir modal para modificar
    $('#example tbody').on('click', '.edit-btn', function () {
        var data = tabla.row($(this).parents('tr')).data();
        // Rellenar el formulario del modal de editar
        $('#editCodpro').val(String($(this).data('codpro'))).trigger('change');
        $('#editCante').val(data.cante);
        $('#editCantf').val(data.cantf);
        $('#editNumitm').val(data.numitm);

        $('#editProductoModal').modal('show');
    });

    // Guardar cambios en producto existente
    $('#saveEditProducto').on('click', function () {
        var codpro = $('#editCodpro').val();
        var cante = $('#editCante').val();
        var cantf = $('#editCantf').val();
        let invnum = sessionStorage.getItem('ordenreposicion');
        var numitm = $('#editNumitm').val();
        if (codpro !== null && codpro !== undefined && codpro !== "") {
            if (cante !== "" || cantf !== "") {
                $.getJSON("CRUDFaOrdenReposicionDetalle", {opcion: 3, codpro: codpro, cante: cante, cantf: cantf, invnum: invnum, numitm: numitm}, function (data) {
                    if (data.resultado === "ok") {
                        $('#editProductoModal').modal('hide');
                        $.fn.listar();
                    } else {
                        if (data.mensaje === "nosession") {
                            $.fn.validarSession();
                        } else if (data.mensaje === "cerrado") {
                            alert("Esta orden ya fue enviada");
                        } else {
                            alert("Error al agregar la orden");
                        }
                    }
                }).fail(function (jqXHR, textStatus, errorThrown) {
                    alert("Error: No se logro hacer conexion con el servidor");
                });
                $('#addProductoModal').modal('hide');
            } else {
                alert("Ingresa almenos una cantidad");
            }
        } else {
            alert("Eligue el producto");
        }
    });
    $('#example tbody').on('click', '.delete-btn', function () {
        let invnum = sessionStorage.getItem('ordenreposicion');
        let numitm = String($(this).data("numitm"));
        if (confirm("Estas seguro que quieres eliminar esta solicitud")) {
            $.getJSON("CRUDFaOrdenReposicionDetalle?opcion=4", {invnum: invnum, numitm: numitm}, function (data) {
                if (data.resultado === "ok") {
                    $.fn.listar();
                } else {
                    if (data.mensaje === "nosession") {
                        $.fn.validarSession();
                    } else if (data.mensaje === "cerrado") {
                        alert("Esta orden ya fue enviada");
                    } else {
                        alert("Error al eliminar el usuario");
                    }
                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
                alert("Error: No se logro hacer conexion con el servidor");
            });
        }
    });



    var fixedTitle = $('#fixed-title');
    var cardHeader = $('#card-header');
    var cardHeaderOffset = cardHeader.offset().top;

    $(window).on('scroll', function () {
        if ($(window).scrollTop() > cardHeaderOffset) {
            fixedTitle.css({
                'display': 'block',
                'width': cardHeader.outerWidth()
            });
        } else {
            fixedTitle.css('display', 'none');
        }
    });

    $(window).on('resize', function () {
        if (fixedTitle.css('display') === 'block') {
            fixedTitle.css('width', cardHeader.outerWidth());
        }
    });

    $("#back-button").click(function () {
        $("#contenido").load('ordenreposicion.html');
    });
});