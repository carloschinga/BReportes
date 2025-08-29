$(document).ready(function () {
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
    let listaAlmacen = {};
    let listatodos = {};
    //Funciones
    $.fn.ListarProductos = function () {
        $.fn.validarSession();
        let tabla = $('#tabladatos').DataTable();
        if (tabla) {
            $("#tabladatos").empty();
            tabla.destroy();
        }
        let codlab = String($('#txtCodigoLaboratorio').val());
        let codfam = String($('#txtCodigoFamilia').val());
        let codgen = String($('#txtCodigoGenerico').val());
        let codtip = String($('#txtCodigoTipo').val());
        let codpro = String($('#txtCodigoProducto').val());
        $("#loadingproductos").css("display", "block");
        let json = {codlab: codlab, codfam: codfam, codgen: codgen, codtip: codtip, codpro: codpro};

        tabla = $("#tabladatos").DataTable({
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
            ajax: {
                url: 'CRUDProductos?opcion=4',
                type: 'POST',
                "data": function (d) {
                    d.codlab = codlab;
                    d.codfam = codfam;
                    d.codgen = codgen;
                    d.codtip = codtip;
                    d.codpro = codpro;
                }
            },
            columns: [
                {data: 'codpro'},
                {data: 'despro'},
                {data: 'blister',
                    "render": function (data, type, row) {
                        if (row.stkfra === 1) {
                            return 'no fra.';
                        } else {
                            if (data === undefined) {
                                return '<input type="text" data-stkfra="' + row.stkfra + '" class="blister" size="5"></input>';
                            } else {
                                return '<input type="text" data-stkfra="' + row.stkfra + '" class="blister" size="5" value="' + data + '"></input>';
                            }
                        }
                    }
                },
                {data: 'aplicfrac',
                    "render": function (data, type, row) {
                        if (row.stkfra === 1) {
                            return 'no fra.';
                        } else {
                            if (data === undefined || data === 'N') {
                                if (row.aplicmastpack === 'S') {
                                    return '<input type="checkbox" data-codpro="' + row.codpro + '" class="checkblister" disabled></input>';
                                } else {
                                    return '<input type="checkbox" data-codpro="' + row.codpro + '" class="checkblister"></input>';
                                }
                            } else {
                                return '<input type="checkbox" data-codpro="' + row.codpro + '" class="checkblister" checked></input>';
                            }
                        }
                    }
                },
                {data: 'masterpack',
                    "render": function (data, type, row) {
                        if (data === undefined) {
                            return '<input type="text" class="masterpack" size="5"></input>';
                        } else {
                            return '<input type="text" class="masterpack" size="5" value="' + data + '"></input>';
                        }
                    }
                },
                {data: 'aplicmastpack',
                    "render": function (data, type, row) {
                        if (data === undefined || data === 'N') {
                            if (row.aplicfrac === 'S') {
                                return '<input type="checkbox" data-codpro="' + row.codpro + '" class="checkaplicmastpack" disabled></input>';
                            } else {
                                return '<input type="checkbox" data-codpro="' + row.codpro + '" class="checkaplicmastpack"></input>';
                            }
                        } else {
                            return '<input type="checkbox" data-codpro="' + row.codpro + '" class="checkaplicmastpack" checked></input>';
                        }
                    }
                }
            ]
        });
        $("#loadingproductos").css("display", "none");

    };
    $.fn.ListarProductos();
    //Eventos
    $('#tabladatos').on('change', '.blister', function () {
        var limpiar = false;
        // Obtener el valor de codpro del input actual
        var stkfra = Number($(this).data('stkfra'));
        // Obtener el valor actual del input
        var valorInput = $(this).val();
        // Verificar si el valor ingresado es un número
        if (isNaN(valorInput) || parseFloat(valorInput) < 2 || valorInput % 1 !== 0) {
            alert('Por favor, ingrese un número válido mayor a 1.');
            $(this).val("");
            return;
        }
        if (valorInput !== "") {
            valorInput = Number(valorInput);
            if (stkfra % valorInput !== 0) {
                alert('Por favor, ingrese un número valido que se pueda dividir en la cantidad de ' + stkfra + ' fraciones.');
                $(this).val("");
                return;
            }
        }
    });
    $('#tabladatos').on('change', '.masterpack', function () {
        var limpiar = false;
        // Obtener el valor de codpro del input actual
        var stkfra = Number($(this).data('stkfra'));
        // Obtener el valor actual del input
        var valorInput = $(this).val();
        // Verificar si el valor ingresado es un número
        if (isNaN(valorInput) || parseFloat(valorInput) < 2 || valorInput % 1 !== 0) {
            alert('Por favor, ingrese un número válido mayor a 1.');
            $(this).val("");
            return;
        }
    });
    $('#tabladatos').on('change', '.checkblister', function () {
        $.fn.validarSession();
        $('#tabladatos input[type="checkbox"]').prop('disabled', true);
        var codpro = $(this).data('codpro').toString();
        var estado = $(this).is(':checked') ? 'true' : 'false';
        var fila = $(this).closest('tr');
        var cant = fila.find('.blister').val();
        var checkbox = $(this);
        $.getJSON('CRUDProductos?opcion=2', {codpro: codpro, estado: estado, blister: cant}, function (data) {
            if (data.resultado === "ok") {
                $('#tabladatos input[type="checkbox"]').prop('disabled', false);
                if (estado === 'true') {
                    fila.find('.checkaplicmastpack').prop('disabled', true);
                } else {
                    fila.find('.checkaplicmastpack').prop('disabled', false);
                }
            } else if (data.mensaje === "nosession") {
                $.fn.validarSession();
            } else {
                alert("Error General: actualize y vuelva a intentarlo");
                $.fn.ListarProductos();
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            console.error("Error en la solicitud: " + textStatus, errorThrown);
            alert("Error General: Verifique su conexion con el servidor");
            $.fn.ListarProductos();
        });
    });
    $('#tabladatos').on('change', '.checkaplicmastpack', function () {
        $.fn.validarSession();
        $('#tabladatos input[type="checkbox"]').prop('disabled', true);
        var codpro = $(this).data('codpro').toString();
        var estado = $(this).is(':checked') ? 'true' : 'false';
        var fila = $(this).closest('tr');
        var cant = fila.find('.masterpack').val();
        var checkbox = $(this);
        if (cant !== "" || estado === 'false') {
            $.getJSON('CRUDProductos?opcion=3', {codpro: codpro, estado: estado, masterpack: cant}, function (data) {
                if (data.resultado === "ok") {
                    $('#tabladatos input[type="checkbox"]').prop('disabled', false);
                    if (estado === 'true') {
                        fila.find('.checkblister').prop('disabled', true);
                    } else {
                        fila.find('.checkblister').prop('disabled', false);
                    }
                } else if (data.mensaje === "nosession") {
                    $.fn.validarSession();
                } else {
                    alert("Error General: actualize y vuelva a intentarlo");
                    $.fn.ListarProductos();
                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
                console.error("Error en la solicitud: " + textStatus, errorThrown);
                alert("Error General: Verifique su conexion con el servidor");
                $.fn.ListarProductos();
            });
        } else {
            alert("Ingrese una cantidad en masterpack");
            $.fn.ListarProductos();
        }
    });
    $("#btnBuscarLaboratorio").click(function () {
        $.fn.validarSession();
        $('#modal-laboratorio').modal('show');

        let tabla = $('#table-laboratorio').DataTable();
        if (tabla) {
            tabla.destroy();
        }
        ;
        tabla = $("#table-laboratorio").DataTable({
            paging: false,
            fixedHeader: true,
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
            }, ajax: {
                url: 'restricciones?opcion=2',
                type: 'POST',
                beforeSend: function () {
                    $("#loadinglaboratorio").css("display", "block");
                }, complete: function () {
                    $("#loadinglaboratorio").css("display", "none");
                }
            },
            columns: [
                {data: 'codlab'},
                {data: 'deslab'},
                {data: null,
                    render: function (data, type, row) {
                        return '<button class="btn btn-info btn-sm" data-codlab="' + data.codlab + '" data-deslab="' + data.deslab + '"><i class="fas fa-check"></i></button>';
                    }
                }
            ]
        });

    });
    $('#table-laboratorio').on('click', '.btn', function () {
        $.fn.validarSession();
        codlab = String($(this).data("codlab"));
        deslab = String($(this).data("deslab"));
        $("#txtCodigoLaboratorio").val(codlab);
        $("#txtNombreLaboratorio").val(deslab);
        $('#btnEliminarLaboratorio').show();
        $('#modal-laboratorio').modal('hide');
        $.fn.ListarProductos();
    });
    $("#btnBuscarFamilia").click(function () {
        $.fn.validarSession();
        $('#modal-familia').modal('show');

        let tabla = $('#table-familia').DataTable();
        if (tabla) {
            tabla.destroy();
        }
        ;
        tabla = $("#table-familia").DataTable({
            paging: false,
            fixedHeader: true,
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
            }, ajax: {
                url: 'restricciones?opcion=3',
                type: 'POST',
                beforeSend: function () {
                    $("#loadinglaboratorio").css("display", "block");
                }, complete: function () {
                    $("#loadinglaboratorio").css("display", "none");
                }
            },
            columns: [
                {data: 'codfam'},
                {data: 'desfam'},
                {data: null,
                    render: function (data, type, row) {
                        return '<button class="btn btn-info btn-sm" data-codfam="' + data.codfam + '" data-desfam="' + data.desfam + '"><i class="fas fa-check"></i></button>';
                    }
                }
            ]
        });
    });
    $('#table-familia').on('click', '.btn', function () {
        $.fn.validarSession();
        codfam = String($(this).data("codfam"));
        desfam = String($(this).data("desfam"));
        $("#txtCodigoFamilia").val(codfam);
        $("#txtNombreFamilia").val(desfam);
        $('#btnEliminarFamilia').show();
        $('#modal-familia').modal('hide');
        $.fn.ListarProductos();
    });
    $("#btnBuscarGenerico").click(function () {
        $.fn.validarSession();
        $('#modal-generico').modal('show');

        let tabla = $('#table-generico').DataTable();
        if (tabla) {
            tabla.destroy();
        }
        ;
        tabla = $("#table-generico").DataTable({
            paging: false,
            fixedHeader: true,
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
            }, ajax: {
                url: 'restricciones?opcion=4',
                type: 'POST',
                beforeSend: function () {
                    $("#loadinglaboratorio").css("display", "block");
                }, complete: function () {
                    $("#loadinglaboratorio").css("display", "none");
                }
            },
            columns: [
                {data: 'codgen'},
                {data: 'desgen'},
                {data: null,
                    render: function (data, type, row) {
                        return '<button class="btn btn-info btn-sm" data-codgen="' + data.codgen + '" data-desgen="' + data.desgen + '"><i class="fas fa-check"></i></button>';
                    }
                }
            ]
        });
    });
    $('#table-generico').on('click', '.btn', function () {
        $.fn.validarSession();
        codgen = String($(this).data("codgen"));
        desgen = String($(this).data("desgen"));
        $("#txtCodigoGenerico").val(codgen);
        $("#txtNombreGenerico").val(desgen);
        $('#btnEliminarGenerico').show();
        $('#modal-generico').modal('hide');
        $.fn.ListarProductos();
    });
    $("#btnBuscarTipo").click(function () {
        $('#modal-tipo').modal('show');

        let tabla = $('#table-tipo').DataTable();
        if (tabla) {
            tabla.destroy();
        }
        ;
        tabla = $("#table-tipo").DataTable({
            paging: false,
            fixedHeader: true,
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
            }, ajax: {
                url: 'crudtiposproducto?opcion=1',
                type: 'POST',
                beforeSend: function () {
                    $("#loadinglaboratorio").css("display", "block");
                }, complete: function () {
                    $("#loadinglaboratorio").css("display", "none");
                }
            },
            columns: [
                {data: 'codtip'},
                {data: 'destip'},
                {data: null,
                    render: function (data, type, row) {
                        return '<button class="btn btn-info btn-sm" data-codtip="' + data.codtip + '" data-destip="' + data.destip + '"><i class="fas fa-check"></i></button>';
                    }
                }
            ]
        });
    });
    $('#table-tipo').on('click', '.btn', function () {
        $.fn.validarSession();
        codtip = String($(this).data("codtip"));
        destip = String($(this).data("destip"));
        $("#txtCodigoTipo").val(codtip);
        $("#txtNombreTipo").val(destip);
        $('#btnEliminarTipo').show();
        $('#modal-tipo').modal('hide');
        $.fn.ListarProductos();
    });

    $("#btnBuscarProducto").click(function () {
        $.fn.validarSession();
        $('#modal-producto').modal('show');

        let tabla = $('#table-producto').DataTable();
        if (tabla) {
            tabla.destroy();
        }
        ;
        tabla = $("#table-producto").DataTable({
            paging: false,
            fixedHeader: true,
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
            }, ajax: {
                url: 'restricciones?opcion=7',
                type: 'POST',
                beforeSend: function () {
                    $("#loadinglaboratorio").css("display", "block");
                }, complete: function () {
                    $("#loadinglaboratorio").css("display", "none");
                }
            },
            columns: [
                {data: 'codpro'},
                {data: 'despro'},
                {data: null,
                    render: function (data, type, row) {
                        return '<button class="btn btn-info btn-sm" data-codpro="' + data.codpro + '" data-despro="' + data.despro + '"><i class="fas fa-check"></i></button>';
                    }
                }
            ]
        });
    });
    $('#table-producto').on('click', '.btn', function () {
        $.fn.validarSession();
        let codpro = String($(this).data("codpro"));
        let despro = String($(this).data("despro"));
        $("#txtCodigoProducto").val(codpro);
        $("#txtNombreProducto").val(despro);
        $('#btnEliminarProducto').show();
        $('#modal-producto').modal('hide');
        $.fn.ListarProductos();
    });
    $("#btnEliminarProducto").click(function () {
        $.fn.validarSession();
        $("#txtCodigoProducto").val("");
        $("#txtNombreProducto").val("");
        $("#btnEliminarProducto").hide();
        $.fn.ListarProductos();
    });
    $("#btnEliminarFamilia").click(function () {
        $.fn.validarSession();
        $("#txtCodigoFamilia").val("");
        $("#txtNombreFamilia").val("");
        $("#btnEliminarFamilia").hide();
        $.fn.ListarProductos();
    });
    $("#btnEliminarGenerico").click(function () {
        $.fn.validarSession();
        $("#txtCodigoGenerico").val("");
        $("#txtNombreGenerico").val("");
        $("#btnEliminarGenerico").hide();
        $.fn.ListarProductos();
    });
    $("#btnEliminarTipo").click(function () {
        $.fn.validarSession();
        $("#txtCodigoTipo").val("");
        $("#txtNombreTipo").val("");
        $("#btnEliminarTipo").hide();
        $.fn.ListarProductos();
    });
    $("#btnEliminarLaboratorio").click(function () {
        $.fn.validarSession();
        $("#txtCodigoLaboratorio").val("");
        $("#txtNombreLaboratorio").val("");
        $("#btnEliminarLaboratorio").hide();
        $.fn.ListarProductos();
    });
});