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
    $.getJSON("CRUDFaSubAlmacenes?opcion=1", function (data) {
        if (data.resultado === "ok") {
            let almacen = $('#almacen');
            almacen.empty();
            //almacen.append('<option value="">TODOS</option>');
            $.each(data.data, function (key, value) {
                //if(value.codalm!=="A1" && value.codalm!=="A2")
                almacen.append('<option value="' + value.codsubalm + '">' + value.dessubalm + '</option>');
            });
            $.fn.ListarProductos();
        } else {
            if (data.mensaje === "nosession") {
                $.fn.validarSession();
            } else {
                alert("Error: Problemas con el servidor.");
            }
        }
    });
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
        let codsubalm = String($('#almacen').val());
        if (codlab !== "" || codfam !== "" || codgen !== "" || codtip !== "" || codpro !== "" || codsubalm!=="") {
            $("#loadingproductos").css("display", "block");
            let json = {codlab: codlab, codfam: codfam, codgen: codgen, codtip: codtip, codpro: codpro, codsubalm: codsubalm};

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
                    url: 'CRUDFaStockAlmacenes?opcion=1',
                    type: 'POST',
                    "data": function (d) {
                        d.codlab = codlab;
                        d.codfam = codfam;
                        d.codgen = codgen;
                        d.codtip = codtip;
                        d.codpro = codpro;
                        d.codsubalm = codsubalm;
                    }
                },
                columns: [
                    {data: 'codpro'},
                    {data: 'despro'},
                    {data: 'codlot'},
                    {data: 'fecven'},
                    {data: 'qtymov'},
                    {data: 'qtymov_m'},
                    {data: 'cante',
                        "render": function (data, type, row) {
                            if (row.inmovil !== "S") {
                                if (data === undefined) {
                                    return '<input type="text" data-max="' + row.qtymov + '" class="cante" size="5"></input>';
                                } else {
                                    return '<input type="text" data-max="' + row.qtymov + '" class="cante" size="5" value="' + data + '"></input>';
                                }
                            } else {
                                return '<input type="text" size="5" disabled></input>';
                            }
                        }
                    },
                    {data: 'cantf',
                        "render": function (data, type, row) {
                            if (row.inmovil !== "S") {
                                if (data === undefined) {
                                    return '<input type="text" data-max="' + row.qtymov_m + '" class="cantf" size="5"></input>';
                                } else {
                                    return '<input type="text" data-max="' + row.qtymov_m + '" class="cantf" size="5" value="' + data + '"></input>';
                                }
                            } else {
                                return '<input type="text" size="5" disabled></input>';
                            }
                        }
                    },
                    {data: 'inmov',
                        "render": function (data, type, row) {
                            if (row.inmovil !== "S") {
                                if (data === undefined || data === 'N') {
                                    return '<input type="checkbox" data-codpro="' + row.codpro + '" data-codlot="' + row.codlot + '"></input>';
                                } else {
                                    return '<input type="checkbox" data-codpro="' + row.codpro + '" data-codlot="' + row.codlot + '" checked></input>';
                                }
                            } else {
                                return '<input type="checkbox" disabled></input>';
                            }
                        }
                    }
                ]
            });
            $("#loadingproductos").css("display", "none");
        }
    };
    //Eventos
    $('#almacen').change(function () {
        $('#nomb').text($(this).find('option:selected').text());
        $.fn.ListarProductos();
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
    $('#tabladatos').on('change', 'input[type="checkbox"]', function () {
        $.fn.validarSession();
        $('#tabladatos input[type="checkbox"]').prop('disabled', true);
        var codpro = $(this).data('codpro').toString();
        var codlot = $(this).data('codlot').toString();
        var fila = $(this).closest('tr');
        var cante = fila.find('.cante').val();
        var cantf = fila.find('.cantf').val();
        let codsubalm = String($('#almacen').val());
        var estado = $(this).is(':checked') ? 'true' : 'false';
        let entro = true;
        if (isNaN(cante)) {
            entro = false;
        }
        if (isNaN(cantf)) {
            entro = false;
        }

        if (entro) {
            $.getJSON('CRUDFaStockAlmacenes?opcion=2', {codpro: codpro, estado: estado, codlot: codlot, cante: cante, cantf: cantf, codsubalm: codsubalm}, function (data) {
                if (data.resultado === "ok") {
                    $('#tabladatos input[type="checkbox"]').prop('disabled', false);
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
            alert("Ingrese cantidades correctas, o dejalas vacias para que cuente todo el stock");
            $('#tabladatos input[type="checkbox"]').prop('disabled', false);
        }
        ;
    });
    $('#tabladatos').on('change', 'input[type="text"]', function () {
        var limpiar = false;
        // Obtener el valor de codpro del input actual
        var max = Number($(this).data('max'));
        // Obtener el valor actual del input
        var valorInput = $(this).val();
        // Verificar si el valor ingresado es un número
        if (isNaN(valorInput) || parseFloat(valorInput) < 0 || valorInput % 1 !== 0) {
            alert('Por favor, ingrese un número válido.');
            $(this).val("");
            return;
        }
        valorInput = Number(valorInput);
        if (max < valorInput) {
            alert('Por favor, ingrese un número menor o igual al stock.');
            $(this).val("");
            return;
        }
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