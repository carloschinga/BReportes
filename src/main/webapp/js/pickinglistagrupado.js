$(document).ready(function () {
    let novolver = false;
    let primeravez = false;
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
    $.fn.listarsecuencias = function () {
        $.fn.validarSession();
        let sec1 = $('#secuencia1').val();
        let sec2 = $('#secuencia2').val();
        let fechainicio = $("#inputFechaInicio").val();
        let fechafin = $("#inputFechaFin").val();
        let tipo;
        let filtro = $("#filtro").val();
        if (!$('#checksecuencia').is(':checked')) {
            tipo = "S";
        } else {
            tipo = "N";
            sec2 = "";
        }
        if (sec1 !== "" || sec2 !== "" || fechainicio !== "" || fechafin !== "") {
            var tabla = $('#table-secuencias').DataTable();
            if (tabla) {
                tabla.destroy();
            }
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
                    "url": "picking?opcion=40",
                    "data": function (d) {
                        d.invnumMin = sec1;
                        d.invnumMax = sec2;
                        d.tipo = tipo;
                        d.fecMin = fechainicio;
                        d.fecMax = fechafin;
                        d.filtro = filtro;
                    }
                },
                "columns": [
                    {"data": "invnum", "render": function (data, type, row) {
                            return data;
                        }
                    }, {"data": "fecmov"},
                    {"data": "usado", "render": function (data, type, row) {
                            if (row.estado === "E") {
                                return "anulado.";
                            } else {
                                if (data === undefined || data === 0) {
                                    return '<input type="checkbox" data-invnum=' + row.invnum + '>';
                                } else {
                                    return data;
                                }
                            }
                        }
                    }, {"data": null, "render": function (data, type, row) {
                            let string = '<button type="button" class="btn btn-primary btn-sm listar" data-invnum="' + row.invnum + '" data-toggle="tooltip" data-placement="top" title="Ver lista de productos"><i class="fas fa-list"></i></button>';
                            if (row.estado !== "E" && !row.usado) {
                                string += '<button type="button" class="btn btn-danger btn-sm eliminar" data-invnum="' + row.invnum + '" data-toggle="tooltip" data-placement="top" title="Eliminar"><i class="fas fa-trash"></i></button>';
                            }
                            return string;
                        }
                    }
                ]
            });
        } else {
            alert("Intenta agregar almenos un filtro para hacer la busqueda.");
        }
    };
    $.fn.validarSession();
    let today = new Date().toISOString().slice(0, 10);
    //$('#inputFechaInicio').val(today);
    //$('#inputFechaFin').val(today);
    $('#inputFechaInicio').change(function () {
        let fechaFin = $("#inputFechaInicio").val();
        $("#inputFechaFin").val(fechaFin);
    });
    $("#btnPDF").click(function () {
        $.fn.validarSession();
        let fechainicio = $("#inputFechaInicio").val();
        let fechafin = $("#inputFechaFin").val();
        window.open('ReporteGuiaTransferencia?opcion=1&fechainicio=' + fechainicio + '&fechafin=' + fechafin, "_blank");
    });
    $("#buscarsec").click(function () {
        if (primeravez) {
            if (novolver) {
                $.fn.listarsecuencias();
            } else {
                $('#limpiarmodal').modal('show');
            }
        } else {
            primeravez = true;
            $.fn.listarsecuencias();
        }
    });
    $("#btnBuscar").click(function () {
        $('#limpiarmodal').modal('hide');
        $.fn.listarsecuencias();
    });
    $('#tipo').change(function () {
        if ($(this).is(':checked')) {
            $('#secuencias').show();
            $('#fechas').hide();
        } else {
            $('#secuencias').hide();
            $('#fechas').show();
        }
    });
    $('#secuencia1').change(function () {
        $("#secuencia2").val($("#secuencia1").val());
    });
    $('#secuencia2').change(function () {
    });
    $('#checksecuencia').change(function () {
        if ($(this).is(':checked')) {
            $('#secuencia2').prop('disabled', true);
        } else {
            $('#secuencia2').prop('disabled', false);
        }
    });
    $('#checknovolver').change(function () {
        if ($(this).is(':checked')) {
            novolver = true;
        } else {
            novolver = false;
        }
    });
    $("#btnimpr").click(function () {
        var checkboxes = $('#table-secuencias').find('input[type="checkbox"]');
        $.fn.validarSession();
        let sec1 = $('#secuencia1').val();
        let sec2 = $('#secuencia2').val();
        let fechainicio = $("#inputFechaInicio").val();
        let fechafin = $("#inputFechaFin").val();
        if (!$('#checksecuencia').is(':checked')) {
        } else {
            sec2 = "";
        }
        var unselectedInvnums = [];
        checkboxes.each(function () {
            if (!this.checked) {
                unselectedInvnums.push($(this).data('invnum'));
            }
        });
        let unselectedInvnumString = unselectedInvnums.map(invnum => `${invnum}`).join(',');
        let url = 'ReporteGuiaTransferencia?fechafin=' + fechafin + '&fechainicio=' + fechainicio + '&secini=' + sec1 + '&secfin=' + sec2 + '&res=' + unselectedInvnumString;
        var encodedString = encodeURIComponent(url);

        var longitud = encodedString.length;
        var tamanioMaximoCabecera = 8 * 1024;

        if (longitud > tamanioMaximoCabecera) {
            alert("se han selecionado demasiadas secuencias para excluir, el reporte no soporta excluir tantas");
        } else {
            window.open(url, "_blank");
        }
    });
    $("#cerrarmodal").click(function () {
        $("#modal-detalle").modal("hide");
    });
    $("#guardar").click(function () {
        var checkboxes = $('#table-secuencias').find('input[type="checkbox"]');
        $.fn.validarSession();
        if (!$('#checksecuencia').is(':checked')) {
        } else {
            sec2 = "";
        }
        var selectedInvnums = [];
        checkboxes.each(function () {
            if (this.checked) {
                selectedInvnums.push($(this).data('invnum'));
            }
        });
        if (selectedInvnums.length === 0) {
            alert("Primero haga una busqueda para seleccionar las secuencias");
        } else {

            var checkboxes = $('#table-secuencias').find('input[type="checkbox"]');
            $.fn.validarSession();
            if (!$('#checksecuencia').is(':checked')) {
            } else {
                sec2 = "";
            }
            var selectedInvnums = [];
            checkboxes.each(function () {
                if (this.checked) {
                    selectedInvnums.push($(this).data('invnum'));
                }
            });
            if (selectedInvnums.length === 0) {
                alert("Primero haga una busqueda para seleccionar las secuencias");
            } else {

                let jsonData = JSON.stringify({invnum: selectedInvnums});

                $.ajax({
                    url: 'picking?opcion=42',
                    type: 'POST',
                    contentType: 'application/json',
                    data: jsonData,
                    success: function (response) {
                        let jsonResponse = JSON.parse(response);
                        if (jsonResponse.resultado === "ok") {
                            Swal.fire({
                                title: "Picking List",
                                text: "código de picking list asignado a: " + jsonResponse.codpik,
                                icon: "success"
                            });
                            $.fn.listarsecuencias();
                        } else {
                            alert("error");
                        }
                    },
                    error: function (error) {
                        console.error('Error al enviar los datos: ', error);
                    }
                });
            }
            /*
             $("#modal-usuario").modal("show");
             var tabla = $('#table-usuario').DataTable();
             if (tabla) {
             tabla.destroy();
             }
             var table = $('#table-usuario').DataTable({
             paginate: false,
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
             "url": "CRUDUsuario?opcion=1"
             },
             "columns": [
             {"data": "usenam"},
             {"data": null, "render": function (data, type, row) {
             return '<button type="button" class="btn btn-primary btn-sm" data-usecod="' + row.usecod + '"  data-usenam="' + row.usenam + '" data-toggle="tooltip" data-placement="top" title="Ver lista de productos"><i class="fas fa-box"></i></button>';
             }
             }
             ]
             });
             * 
             */

        }
    });
    /*
     $('#table-usuario').on('click', '.btn', function () {
     $('#modal-usuario').modal('hide');
     let usecod = String($(this).data("usecod"));
     let usenam = String($(this).data("usenam"));
     var checkboxes = $('#table-secuencias').find('input[type="checkbox"]');
     $.fn.validarSession();
     if (!$('#checksecuencia').is(':checked')) {
     } else {
     sec2 = "";
     }
     var selectedInvnums = [];
     checkboxes.each(function () {
     if (this.checked) {
     selectedInvnums.push($(this).data('invnum'));
     }
     });
     if (selectedInvnums.length === 0) {
     alert("Primero haga una busqueda para seleccionar las secuencias");
     } else {
     
     let jsonData = JSON.stringify({invnum: selectedInvnums});
     
     $.ajax({
     url: 'picking?opcion=7&usecod=' + usecod,
     type: 'POST',
     contentType: 'application/json',
     data: jsonData,
     success: function (response) {
     let jsonResponse = JSON.parse(response);
     if (jsonResponse.resultado === "ok") {
     alert("OT con codigo: " + jsonResponse.codpik + " asignado al usuario " + usenam);
     $.fn.listarsecuencias();
     } else {
     alert("error");
     }
     },
     error: function (error) {
     console.error('Error al enviar los datos:', error);
     }
     });
     }
     });
     */
    $('#table-secuencias').on('click', '.eliminar', function () {
        let invnum = String($(this).data("invnum"));

        Swal.fire({
            title: "¿Realmente desea anular?",
            text: "Los productos se quitaran del transito y no se podra selecionar en un picking",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Eliminar",
            cancelButtonText: `Cerrar`
        }).then((result) => {
            /* Read more about isConfirmed, isDenied below */
            if (result.isConfirmed) {
                $.ajax({
                    url: 'picking',
                    type: 'POST',
                    data: {invnum: invnum, opcion: "54"},
                    success: function (response) {
                        let jsonResponse = JSON.parse(response);
                        if (jsonResponse.resultado === "ok") {
                            Swal.fire({
                                title: "Eliminado correctamente",
                                text: "Distribucion anulada correctamente.",
                                icon: "success"
                            });
                            $.fn.listarsecuencias();
                        } else {
                            alert("error");
                        }
                    },
                    error: function (error) {
                        console.error('Error al enviar los datos: ', error);
                    }
                });
            }
        });
    });
    $('#table-secuencias').on('click', '.listar', function () {
        $('#modal-detalle').modal('show');
        let invnum = String($(this).data("invnum"));
        var tabla = $('#table-detalle').DataTable();
        if (tabla) {
            tabla.destroy();
        }
        var table = $('#table-detalle').DataTable({
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
                "url": "picking?opcion=41",
                "data": function (d) {
                    d.invnum = invnum;
                }
            },
            "columns": [
                {"data": "despro"},
                {"data": "cante"},
                {"data": "cantf"},
                {"data": "codlot"},
                {"data": "sisent"}
            ]
        });
    });

});