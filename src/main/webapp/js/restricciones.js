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
    $.fn.actualizaralmacenes = function () {
        $.getJSON("restricciones?opcion=1", function (data) {
            let cadena = "<tr><th scope='col'>CodPro</th><th scope='col'>Descripción</th>";
            if (data.resultado === "ok") {
                listaAlmacen = data.mensaje;
                $.each(listaAlmacen, function (index, item) {
                    cadena = cadena + "<th scope='col'>" + item.desalm + "</th>";
                });
                cadena = cadena + "</tr>";
                $('#cabecera').html(cadena);
            } else {
                alert("Error: Problemas con el servidor.");
            }
        });
    };
    $.fn.actualizaralmacenes();
    $.fn.ListarProductos = function () {
        $.fn.validarSession();
        $.fn.actualizaralmacenes();
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
        if (codlab !== "" || codfam !== "" || codgen !== "" || codtip !== "" || codpro !== "") {
            $("#loadingproductos").css("display", "block");
            let json = {codlab: codlab, codfam: codfam, codgen: codgen, codtip: codtip, codpro: codpro};

            $.getJSON('restricciones?opcion=5', json, function (data1) {
                let listaColumnas = [
                    {data: 'codpro'},
                    {data: 'despro'}
                ];
                listatodos = [];
                $.each(listaAlmacen, function (index, item) {
                    let codalm = item.codalm; // Captura el valor de item.codalm

                    let columna = {
                        data: null,
                        render: function (data, type, row) {
                            let codproBuscado = data.codpro;
                            // Usa el valor capturado de codalm

                            let encontrado = data1.restricciones.some(registro => registro.codpro === codproBuscado && registro.codalm === codalm);
                            if (!encontrado && data.codpro!=="-----") {
                                let existe = listatodos.some(item => item === codalm);
                                if (!existe) {
                                    listatodos.push(codalm);
                                }
                            }
                            return encontrado ? '<input type="checkbox" data-codpro="' + data.codpro + '" data-codalm="' + codalm + '"></input>' :
                                    '<input type="checkbox" data-codpro="' + data.codpro + '" data-codalm="' + codalm + '" checked></input>';

                        }
                    };
                    listaColumnas.push(columna);
                });
                if (data1.data !== undefined && data1.data.length > 1) {
                    let nuevoRegistro = {"codpro": "-----", "despro": "TODOS"};
                    data1.data.unshift(nuevoRegistro);
                }
                tabla = $("#tabladatos").DataTable({
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
                    }, data: data1.data
                    ,
                    columns: listaColumnas
                    , initComplete: function (settings, json) {
                        let codalmCompleta = listaAlmacen.map(item => item.codalm);

                        let faltantes = codalmCompleta.filter(codalm => !listatodos.includes(codalm));
                        console.log(listatodos);
                        console.log(faltantes);
                        faltantes.forEach(codalm => {
                            let checkbox = $(`#tabladatos input[type="checkbox"][data-codalm="${codalm}"][data-codpro="-----"]`);

                            if (checkbox.length) {
                                checkbox.prop('checked', false);
                            }
                        });
                    }
                });
            });
            $("#loadingproductos").css("display", "none");
        }
    };
    //Eventos
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
        var codalm = $(this).data('codalm').toString();
        var estado = $(this).is(':checked') ? 'true' : 'false';
        var checkbox = $(this);
        var estadoOriginal = !checkbox.prop('checked');
        if (codpro !== "-----") {
            $.getJSON('restricciones?opcion=6', {codpro: codpro, codalm: codalm, estado: estado}, function (data) {
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
            let tabla = $('#tabladatos').DataTable();

            // Obtener los datos de la columna específica
            let datosColumna = tabla.column(0).data().toArray();

            // Crear un array de objetos JSON
            let listaJSON = [];
            datosColumna.forEach(function (valor) {
                if (valor !== "-----") {
                    let objeto = {codpro: valor,codalm:codalm};
                    listaJSON.push(objeto);
                }
            });
            console.log(listaJSON);
            $.ajax({
                url: 'restricciones?opcion=8&estado=' + estado + '&codalm=' + codalm, // URL de la API
                method: 'POST', // Cambiado a POST
                contentType: 'application/json', // Asegúrate de que el servidor espera JSON
                data: JSON.stringify(listaJSON, null), // Convertir los datos a una cadena JSON
                dataType: 'json', // Espera una respuesta JSON del servidor
                success: function (data) {
                    if (data.resultado === "ok") {
                        $.fn.ListarProductos();
                    } else {
                        if (data.mensaje === "nosession") {
                            $.fn.validarSession();
                        } else if (data.mensaje === "aplicres") {
                            alert("Error al aplicar las restricciones, verifique por favor");
                            $.fn.ListarProductos();
                        }
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    if (jqXHR.status === 400) {
                        alert('Error: Demasiados productos a restringir');
                    } else {
                        alert('Error en la solicitud: ' + textStatus);
                    }
                }
            });
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