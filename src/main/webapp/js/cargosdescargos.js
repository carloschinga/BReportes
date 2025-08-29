$(document).ready(function () {

    let idsSeleccionados = [];
    let estabSeleccionados = [];
    let dias = 30;
    let tipoDistrib = 'TODOS';
    let distpor = 'STKMIN';
    let codpro = null;
    let secuencia = 0;
    let indicaFecha = 'N';
    let mostrarrojos = 'N';
    let soloalm = 'S';
    let mostrarrojoscentral = 'N';
    let dataLista = {};
    let datosExtraproductos = {};
    let datos = "";
    let codtip = {};
    let codalm = {};
    let datosExtralotes = {};
    let listado = false;
    let llenarautomaticamente = 'S';
    let listagraficas;
    /* $('#topbar').load('topbar.html');
     $('#sidebar').load('sidebar.html');
     $('#footer').load('footer.html');*/
    let today = new Date().toISOString().slice(0, 10);
    $('#inputFecha').val(today);
    let fecha1 = $("#inputFecha").val();
    let funcdescargar = true;
    let myLineChart;
    let myLineChart2;
    let peticiongrafica;
    codalm = JSON.stringify(estabSeleccionados);
    //FUNCIONES
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
    $.fn.actualizarnumdoc = function () {
        $("#documento1").prop("disabled", false);
        $("#documento2").prop("disabled", false);
        $("#documento3").prop("disabled", false);
        $("#documento1").val("00");
        $("#documento2").val("");
        $("#documento3").val("");
        let displayStyle = $('#divserie').css('display');
        if (displayStyle !== 'none') {
            let tipo = $("#timovimiento").val();
            let tdoser = $("#serie").val();
            $.getJSON("CRUDDocSeries", {opcion: 1, tipkar: tipo, tdoser: tdoser}, function (data) {
                if (data.resultado === "ok") {
                    if (data.data.resultado === "ok") {
                        $("#documento1").val(data.data.tdofac);
                        $("#documento2").val(data.data.tdoidser);
                        $("#documento3").val(data.data.tdonum);
                        $("#documento1").prop("disabled", true);
                        $("#documento2").prop("disabled", true);
                        $("#documento3").prop("disabled", true);
                    }
                } else {
                    if (data.mensaje === "nosession") {
                        $.fn.validarSession();
                    }
                }
            });
        }
    };
    $.fn.actualizarTipoMovimiento = function () {
        let tipo = $("#timovimiento").val();
        $("#divestab").hide();
        $("#selectestabfiltro").prop("disabled", true);
        $("#divserie").hide();
        $("#divtransl").hide();
        if (tipo !== "") {
            $.getJSON("CRUPFaTipoMovimientos?opcion=2&tipkar=" + tipo, function (data) {
                if (data.resultado === "ok") {
                    $("#sectipmovimiento").val(data.data);
                    if (Object.keys(data.doc).length > 0) {
                        $("#divserie").show();
                        let serie = $('#serie');
                        serie.empty();
                        $.each(data.doc, function (key, value) {
                            let cadena = '<option value="' + value.tdoser + '" ';
                            if (value.estado === "N") {
                                cadena += "disabled";
                            }
                            cadena += '>' + value.destse + '</option>';
                            serie.append(cadena);
                        });
                        $.fn.actualizarnumdoc();
                    }
                } else {
                    if (data.mensaje === "nosession") {
                        $.fn.validarSession();
                    }
                }
            });
            if (tipo === "DE") {
                $("#divestab").show();
                $("#divtransl").show();
                $("#labelestab").text("Establec. D");
                $("#selectestabfiltro").prop("disabled", false);
            }
            if (tipo === "CE") {
                $("#divestab").show();
                $("#labelestab").text("Establec. O");
            }
        } else {
            $("#sectipmovimiento").val("");
        }
        $.fn.actualizarnumdoc();
    };
    $.fn.actualizarTransportista = function () {
        let select = $("#transportista").children('option:selected');
        $("#placa-vehiculo").val(select.data("placa"));
    };

    $.fn.ListarTablaCentral = function () {
        $.fn.validarSession();
        let tabla = $('#table-central').DataTable();
        if (tabla) {
            tabla.destroy();
        }
        ;
        tabla = $("#table-central").DataTable({
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
                //url: 'distribucion?opcion=2&tipoStkMin='+dias+'&tipo_distrib='+codpro+'&mostrarrojos='+mostrarrojos,
                url: 'distribucion?opcion=1',
                type: 'POST',
                data: function (d) {
                    d.tipoStkMin = dias, d.tipoDistrib = tipoDistrib, d.codtip = codtip, d.codalm = codalm, d.soloalm = soloalm,
                            d.secuencia = secuencia, d.indicaFecha = indicaFecha, d.fecha1 = fecha1, d.solorojos = mostrarrojoscentral, d.distpor = distpor;
                },
                beforeSend: function () {
                    $("#loadingcentral").css("display", "block");
                }, complete: function () {
                    $("#loadingcentral").css("display", "none");
                }
            },
            columns: [
                {data: 'destip'},
                {data: 'codpro'},
                {data: 'despro'},
                {data: 'codlab'},
                {data: 'stkfra'},
                {data: 'stkalm'},
                {data: 'stkalm_m'},
                {data: null,
                    render: function (data, type, row) {
                        if (dataLista[row.codpro]) {
                            return '<button class="btn btn-warning btn-sm" data-tienerojos="' + row.tienerojos + '"  data-codpro="' + row.codpro + '" data-despro="' + row.despro + '" data-stockfra="' + row.stockfra + '" data-stkalm="' + row.stkalm + '" data-stkalm_m="' + row.stkalm_m + '"><i class="fas fa-edit"></i></button>';
                        } else {
                            if (row.tienerojos === "S") {
                                return '<button class="btn btn-danger btn-sm" data-tienerojos="' + row.tienerojos + '"  data-codpro="' + row.codpro + '" data-despro="' + row.despro + '" data-stockfra="' + row.stockfra + '" data-stkalm="' + row.stkalm + '" data-stkalm_m="' + row.stkalm_m + '"><i class="fas fa-edit"></i></button>';
                            } else {
                                return '<button class="btn btn-primary btn-sm" data-tienerojos="' + row.tienerojos + '"  data-codpro="' + row.codpro + '" data-despro="' + row.despro + '" data-stockfra="' + row.stockfra + '" data-stkalm="' + row.stkalm + '" data-stkalm_m="' + row.stkalm_m + '"><i class="fas fa-edit"></i></button>';
                            }
                        }
                    }
                }
            ]
        });
    };
    $.fn.listarTablaDistribucion = function (codpro, despro, stockFra, stkalm, stkalm_m) {
        $.fn.validarSession();
        if (codpro !== null) {
            var tabla = $('#table-establecimientos').DataTable();
            if (tabla) {
                tabla.destroy();
            }
            $("#nombre-grafica").text("CARGANDO...");
            if (myLineChart) {
                myLineChart.destroy();
            }
            if (peticiongrafica) {
                peticiongrafica.abort();
                console.log('Solicitud abortada');
            }
            if (despro !== undefined) {
                /*$('#datosproducto').html("Producto: <b>" + despro + "</b>, Codigo: " + codpro + ", StockAlm: " +
                 $.fn.obtenerMaximoPorCodpro(codpro) + ",  StockFra: " + $.fn.obtenerStockFraPorCodpro(codpro) +
                 ",   StockAlm_m: " + $.fn.obtenerStockUniPorCodpro(codpro));*/
                /*$('#datosproducto').html("<row><col>Codigo: <b>" + codpro + "</b><col><col> Producto: <b>" + despro + "</b></col><col>StockFra: <b>" +
                 $.fn.obtenerStockFraPorCodpro(codpro)+"</b></col></row>");*/
                $('#datosproducto').html("<row><col>Producto a Distribuir: <b>" + despro + "</b><col>, Codigo: <b>" + codpro + "</b></col></row>");
            }
            let maximadistribucion = 0;
            let maximorequerido = 0;
            let totalesPorLote = [];
            let totalesPorEsta = {};
            listagraficas = {};
            let sugeridos = {};
            $('#footlotE').text(0);
            $('#footlotF').text(0);
            let menorFecha = null;
            let codlotMenorFecha = null;
            let codlotsRegistrados = {};
            let codlotsRegis = {};
            let actualizarbtn = false;
            tabla = $('#table-establecimientos').DataTable({
                searching: false,
                paging: false,
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
                }, ajax: {
                    //url: 'distribucion.jsp?opcion=2&tipoStkMin='+dias+'&tipo_distrib='+codpro+'&mostrarrojos='+mostrarrojos,
                    url: 'distribucion?opcion=2',
                    type: 'POST',
                    data: function (d) {
                        d.tipoStkMin = dias,
                                d.tipo_distrib = tipoDistrib,
                                d.codpro = codpro, d.mostrarrojos = mostrarrojos, d.soloalm = soloalm,
                                d.codtip = codtip, d.codalm = codalm, d.distpor = distpor;
                    },
                    beforeSend: function () {
                        $("#loadingproductos").css("display", "block");
                    }, complete: function () {
                        $("#loadingproductos").css("display", "none");
                    },
                    dataSrc: function (json) {
                        let dataCopy = [...json.data];
                        // Ordenar la copia de los datos por fecha (más antiguos primero)
                        dataCopy.sort(function (a, b) {
                            return ($.fn.convertirAFecha(a.fecven) - $.fn.convertirAFecha(b.fecven));
                        });
                        dataCopy.forEach(item => {
                            if (item.restringido === 'N') {
                                let lote = String(item.lote);
                                let esta = String(item.siscod);
                                let formula = 0;
                                if (distpor === "STKMIN") {
                                    formula = item.stkalm * stockFra + (item.stkalm_m);
                                }
                                let promventa = parseFloat(item.stkmin2);
                                let valor = ((promventa * stockFra) - formula);
                                if (valor > 0) {
                                    if (stockFra === 1) {
                                        valor = Math.ceil(valor);
                                    }
                                    if (!totalesPorEsta[esta]) {
                                        totalesPorEsta[esta] = {totalCantidad: valor};
                                        maximorequerido += valor;
                                    }
                                }
                                let a = true;
                                for (let i = 0; i < totalesPorLote.length; i++) {
                                    if (totalesPorLote[i].lote === lote) {
                                        a = false;
                                    }
                                }

                                if (a) {
                                    let cantidadlote = item.qtymov * stockFra + item.qtymov_m;
                                    totalesPorLote.push({lote: lote, cantidad: cantidadlote});
                                    maximadistribucion += cantidadlote;
                                }
                            }
                        });
                        console.log("lotes", totalesPorLote);
                        console.log(totalesPorEsta);
                        console.log(totalesPorLote);
                        if (maximadistribucion >= maximorequerido && maximorequerido > 0) {

                            for (let esta in totalesPorEsta) {
                                sugeridos[esta] = {};
                                let cantidad = totalesPorEsta[esta]["totalCantidad"];
                                for (let i = 0; i < totalesPorLote.length; i++) {
                                    let lote = totalesPorLote[i].lote;
                                    console.log("cantidad");
                                    console.log(totalesPorLote[i]);
                                    if (totalesPorLote[i].cantidad > 0 && cantidad > 0) {
                                        if (totalesPorLote[i].cantidad >= cantidad) {
                                            sugeridos[esta][lote] = cantidad;
                                            totalesPorLote[i].cantidad -= cantidad;
                                            cantidad = 0;
                                        } else {
                                            sugeridos[esta][lote] = totalesPorLote[i].cantidad;
                                            cantidad -= totalesPorLote[i].cantidad;
                                            totalesPorLote[i].cantidad = 0;
                                        }
                                    }
                                }
                            }
                        }

                        console.log(sugeridos);
                        return json.data;
                    }
                },
                columns: [
                    {data: 'sisent',
                        "render": function (data, type, row) {
                            return '<a href="#" class="open-modal name-link" data-toggle="modal" data-target="#myModal" data-codalm="' + row.codalm + '">' + data + '</a>';
                        }
                    },
                    {data: 'lote'},
                    {data: 'fecven',
                        "render": function (data, type, row) {
                            if (!codlotsRegistrados.hasOwnProperty(row.fecven)) {
                                codlotsRegistrados[row.fecven] = true;
                                let fecha = $.fn.convertirAFecha(row.fecven);
                                if (menorFecha === null || fecha < menorFecha) {
                                    menorFecha = fecha;
                                    codlotMenorFecha = row.fecven;
                                }
                            }
                            if (!codlotsRegis.hasOwnProperty(row.lote)) {
                                codlotsRegis[row.lote] = true;
                            }
                            return data;
                        }
                    },
                    {data: 'qtymov'},
                    {data: 'qtymov_m'},
                    {data: 'stkalm',
                        "render": function (data, type, row) {
                            if (parseFloat(row.stkalm) + (parseFloat(row.stkalm_m) / stockFra) < parseFloat(row.stkmin2)) {
                                return '<b style="color: red;">' + data + '</b>';
                            } else {
                                return data;
                            }
                        }},
                    {data: 'stkalm_m',
                        "render": function (data, type, row) {
                            if (parseFloat(row.stkalm) + (parseFloat(row.stkalm_m) / stockFra) < parseFloat(row.stkmin2)) {
                                return '<b style="color: red;">' + data + '</b>';
                            } else {
                                return data;
                            }
                        }},
                    {data: 'stkmin2',
                        "render": function (data, type, row) {

                            if (parseFloat(row.stkalm) + (parseFloat(row.stkalm_m) / stockFra) < parseFloat(row.stkmin2)) {
                                return '<b style="color: red;" class="danger-row">' + data + '</b>';
                            } else {
                                return data;
                            }
                        }},
                    {data: null,
                        render: function (data, type, row) {
                            let cadena = "<div class='input-inline'>";
                            let formula = 0;
                            if (distpor === "STKMIN") {
                                formula = row.stkalm * stockFra + (row.stkalm_m);
                            }
                            let valor = parseFloat(row.stkmin2) * stockFra - formula;
                            if (stockFra === 1) {
                                valor = Math.ceil(valor / stockFra);
                            } else {
                                valor = Math.floor(valor / stockFra);
                            }
                            if (valor > 0) {
                                cadena += '<input type="text" size="5" value="' + valor + '" disabled>';
                            } else {
                                cadena += '<input type="text" size="5" value="' + 0 + '" disabled>';
                            }
                            if (stockFra > 1) {
                                let formula = 0;
                                if (distpor === "STKMIN") {
                                    formula = parseFloat(row.stkalm) * stockFra + (parseFloat(row.stkalm_m));
                                }
                                let valor = parseFloat(row.stkmin2) * stockFra - formula;
                                valor = Math.ceil(valor % stockFra);
                                if (valor > 0) {
                                    cadena += '<input type="text" size="5" value="' + valor + '" disabled>';
                                } else {
                                    cadena += '<input type="text" size="5" value="' + 0 + '" disabled>';
                                }
                            } else {
                                cadena += '<input type="text" size="5" disabled>';
                            }


                            cadena += '</div>';
                            return cadena;
                        }},
                    {data: null,
                        render: function (data, type, row) {
                            let cadena = "<div class='input-inline'>";
                            if (data.restringido === 'N') {
                                if (dataLista[row.codpro] && dataLista[row.codpro][row.siscod] && dataLista[row.codpro][row.siscod][row.lote] && dataLista[row.codpro][row.siscod][row.lote]["cantE"] !== undefined) {
                                    cadena += '<input type="text" class="cantE" id="inputcantE"  value="' +
                                            dataLista[row.codpro][row.siscod][row.lote]["cantE"] + '" data-codpro="' +
                                            row.codpro + '" data-codalm="' + row.siscod + '" data-lote="' + row.lote +
                                            '" size="5" data-coscom="' + row.coscom + '" data-igvpro="' + row.igvpro + '" autocomplete="off">';
                                } else {
                                    if (sugeridos[row.siscod] && sugeridos[row.siscod][row.lote] !== undefined && !dataLista[row.codpro] && Math.ceil(sugeridos[row.siscod][row.lote] / stockFra) > 0) {
                                        if (llenarautomaticamente === 'S') {
                                            actualizarbtn = true;
                                            if (stockFra > 1 && Math.floor(sugeridos[row.siscod][row.lote] / stockFra) > 0) {
                                                cadena += '<input type="text" class="cantE" id="inputcantE"  value="' +
                                                        Math.floor(sugeridos[row.siscod][row.lote] / stockFra) + '" data-codpro="' +
                                                        row.codpro + '" data-codalm="' + row.siscod + '" data-lote="' + row.lote +
                                                        '" size="5" data-coscom="' + row.coscom + '" data-igvpro="' + row.igvpro + '" autocomplete="off">';
                                            } else if (stockFra === 1 && Math.ceil(sugeridos[row.siscod][row.lote] / stockFra) > 0) {
                                                cadena += '<input type="text" class="cantE" id="inputcantE"  value="' +
                                                        Math.ceil(sugeridos[row.siscod][row.lote] / stockFra) + '" data-codpro="' +
                                                        row.codpro + '" data-codalm="' + row.siscod + '" data-lote="' + row.lote +
                                                        '" size="5" data-coscom="' + row.coscom + '" data-igvpro="' + row.igvpro + '" autocomplete="off">';
                                            } else {
                                                cadena += '<input type="text" class="cantE" id="inputcantE"  value="" data-codpro="' +
                                                        row.codpro + '" data-codalm="' + row.siscod + '" data-lote="' + row.lote +
                                                        '" size="5" data-coscom="' + row.coscom + '" data-igvpro="' + row.igvpro + '" autocomplete="off">';
                                            }

                                        } else {
                                            cadena += '<input type="text" class="cantE" id="inputcantE"  value="" data-codpro="' +
                                                    row.codpro + '" data-codalm="' + row.siscod + '" data-lote="' + row.lote +
                                                    '" size="5" data-coscom="' + row.coscom + '" data-igvpro="' + row.igvpro + '" autocomplete="off">';
                                            /*
                                             return '<input type="text" class="cantE" id="inputcantE"  placeholder="' +
                                             Math.floor(sugeridos[row.siscod][row.lote]) + '" data-codpro="' +
                                             row.codpro + '" data-codalm="' + row.siscod + '" data-lote="' + row.lote +
                                             '" size="5" data-coscom="' + row.coscom + '" data-igvpro="' + row.igvpro + '" autocomplete="off">';
                                             */
                                        }
                                    } else {
                                        cadena += '<input type="text" class="cantE" id="inputcantE"  value="" data-codpro="' +
                                                row.codpro + '" data-codalm="' + row.siscod + '" data-lote="' + row.lote +
                                                '" size="5" data-coscom="' + row.coscom + '" data-igvpro="' + row.igvpro + '" autocomplete="off">';
                                    }
                                }
                            } else {
                                cadena += '<input type="text" class="cantE" size="5" disabled>';
                            }
                            if (data.restringido === 'N' && stockFra !== 1) {
                                if (dataLista[row.codpro] && dataLista[row.codpro][row.siscod] && dataLista[row.codpro][row.siscod][row.lote] && dataLista[row.codpro][row.siscod][row.lote]["cantF"] !== undefined) {
                                    cadena += '<input type="text" class="cantF" id="inputcantF" value="' +
                                            dataLista[row.codpro][row.siscod][row.lote]["cantF"] + '" data-codpro="' +
                                            row.codpro + '" data-codalm="' + row.siscod + '" data-lote="' + row.lote +
                                            '" size="5" data-coscom="' + row.coscom + '" data-igvpro="' + row.igvpro + '" autocomplete="off">';
                                } else {

                                    if (sugeridos[row.siscod] && sugeridos[row.siscod][row.lote] !== undefined && !dataLista[row.codpro] && sugeridos[row.siscod][row.lote] % stockFra > 0) {
                                        if (llenarautomaticamente === 'S' && stockFra > 1) {
                                            actualizarbtn = true;
                                            cadena += '<input type="text" class="cantF" id="inputcantF"  value="' + Math.ceil(sugeridos[row.siscod][row.lote] % stockFra) + '" data-codpro="' +
                                                    row.codpro + '" data-codalm="' + row.siscod + '" data-lote="' + row.lote +
                                                    '" size="5" data-coscom="' + row.coscom + '" data-igvpro="' + row.igvpro + '" autocomplete="off">';
                                        }
                                    } else {
                                        cadena += '<input type="text" class="cantF" id="inputcantF"  value="" data-codpro="' +
                                                row.codpro + '" data-codalm="' + row.siscod + '" data-lote="' + row.lote +
                                                '" size="5" data-coscom="' + row.coscom + '" data-igvpro="' + row.igvpro + '" autocomplete="off">';
                                    }
                                }
                            } else {
                                cadena += '<input type="text" class="cantF" size="5" disabled>';
                            }
                            cadena += '</div>';
                            return cadena;
                        }
                    }
                ], rowCallback: function (row, data) {
                    // Selecciona las celdas específicas de las columnas de lote y fecha de vencimiento
                    let loteCell = $(row).find('td').eq(1); // Ajusta el índice según la posición de la columna 'lote'
                    let fechaVencCell = $(row).find('td').eq(2); // Ajusta el índice según la posición de la columna 'fecha de vencimiento'
                    $(row).find('td').eq(7).addClass('danger-row');
                    if (data.restringido === "S") {
                        $(row).addClass('disabled-row');
                    } else {
                        let venc = $.fn.convertirAFecha(data.fecven).getTime();
                        let hoy = new Date().getTime();
                        var diferenciaMs = venc - hoy;
                        var dias = Math.floor(diferenciaMs / (1000 * 60 * 60 * 24));
                        if (dias < 122) {
                            $(row).addClass('danger-row');
                        } else {
                            if (Object.keys(codlotsRegis).length > 1) {
                                if (data.fecven === codlotMenorFecha) {
                                    loteCell.addClass('danger-row');
                                    fechaVencCell.addClass('danger-row');
                                } else {
                                    loteCell.addClass('min-row');
                                    fechaVencCell.addClass('min-row');
                                }
                            }
                        }
                    }
                }, initComplete: function (settings, json) {
                    if (actualizarbtn) {
                        $.fn.leerCantE();
                        $.fn.leerCantF();
                        if ($.fn.calcularStock(codpro, stkalm, stkalm_m)) {
                            $("#table-establecimientos input[type='text'].cantE").val('');
                            $("#table-establecimientos input[type='text'].cantF").val('');
                            if (llenarautomaticamente === 'S') {
                                alert("Error al llenar la distribucion automaticamente, porfavor llene manualmente");
                            }
                            $.fn.leerCantE();
                            $.fn.leerCantF();
                            $.fn.calcularStock(codpro, stkalm, stkalm_m);
                        }
                    } else {
                        $.fn.calcularStock(codpro);
                    }
                    $.fn.obtenerSumaTotalPorLote();
                    if (stockFra === 1) {
                        $('.cantF').prop('disabled', true);
                    } else {
                        $('.cantF').prop('disabled', false);
                    }
                    /*if (Object.keys(codlotsRegistrados).length === 1) {
                     $("#table-establecimientos").addClass('table-striped');
                     } else {
                     $("#table-establecimientos").removeClass('table-striped');
                     }*/

                    peticiongrafica = $.getJSON("distribucion?opcion=3", {codpro: codpro, tipoStkMin: dias}, function (data) {
                        //$("#tab_stkmin2").text(data.stkmin2);
                        //$("#ta_stkalm").text(data.stkalm);
                        //if (data.stkmin2 - data.stkalm > 0) {
                        //    $("#ta_repone").text(data.stkmin2 - data.stkalm);
                        //}
                        $("#nombre-grafica").text("Unidades vendidas de todos los establecimientos:");
                        listagraficas = data.data;
                        for (const key in listagraficas) {
                            if (listagraficas[key].ventas) {
                                listagraficas[key].ventas = listagraficas[key].ventas.map(value => Math.round(value));
                            }
                        }
                        if (myLineChart) {
                            myLineChart.destroy();
                        }
                        console.log(listagraficas);
                        if (listagraficas["TOTAL"] === undefined) {
                            $("#nombre-grafica").html("<p style='color:red;'>NO HAY VENTAS EN ESTE PRODUCTO</p>");
                        } else {
                            var ctx = document.getElementById("myAreaChart").getContext('2d');
                            myLineChart = new Chart(ctx, {
                                type: 'line',
                                data: {
                                    labels: listagraficas["TOTAL"]["fec"],
                                    datasets: [{
                                            label: "Ventas",
                                            lineTension: 0.3,
                                            backgroundColor: "rgba(78, 115, 223, 0.05)",
                                            borderColor: "rgba(78, 115, 223, 1)",
                                            pointRadius: 3,
                                            pointBackgroundColor: "rgba(78, 115, 223, 1)",
                                            pointBorderColor: "rgba(78, 115, 223, 1)",
                                            pointHoverRadius: 3,
                                            pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
                                            pointHoverBorderColor: "rgba(78, 115, 223, 1)",
                                            pointHitRadius: 10,
                                            pointBorderWidth: 2,
                                            data: listagraficas["TOTAL"]["ventas"],
                                            fill: true,
                                            tension: 0.1
                                        }]
                                },
                                options: {
                                    animation: {
                                        onComplete: function () {
                                            var chartInstance = this.chart;
                                            var ctx = chartInstance.ctx;
                                            ctx.font = Chart.helpers.fontString(Chart.defaults.global.defaultFontSize, 'normal', Chart.defaults.global.defaultFontFamily);
                                            ctx.textAlign = 'center';
                                            ctx.textBaseline = 'bottom';
                                            this.data.datasets.forEach(function (dataset, i) {
                                                var meta = chartInstance.controller.getDatasetMeta(i);
                                                meta.data.forEach(function (bar, index) {
                                                    var data = dataset.data[index];
                                                    var offsetY = bar._model.y - 10; // Ajusta el offset vertical según tu necesidad
                                                    if (offsetY < 20) {
                                                        offsetY = bar._model.y + 20; // Ajuste alternativo si la etiqueta se sale arriba del gráfico
                                                    }
                                                    ctx.fillText(data, bar._model.x, offsetY);
                                                });
                                            });

                                        }
                                    },
                                    scales: {
                                        yAxes: [{
                                                type: 'linear', // Tipo de escala lineal
                                                display: false, // Oculta el eje Y
                                                ticks: {
                                                    beginAtZero: true  // Comienza en cero si es necesario
                                                }
                                            }]
                                    },
                                    responsive: true,
                                    maintainAspectRatio: false,
                                    plugins: {
                                        tooltip: {
                                            enabled: false // Desactivar tooltips
                                        }
                                    },
                                    legend: {
                                        display: false
                                    },
                                    interaction: {
                                        mode: 'none' // Desactivar todas las interacciones
                                    },
                                    hover: {
                                        mode: null, // Desactivar hover
                                        onHover: null, // No hacer nada en hover
                                        animationDuration: 0
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    };
    $.fn.convertirAFecha = function (cadenaFecha) {
        var partes = cadenaFecha.split('/');
        // El formato de fecha en JavaScript toma el mes como base 0, por lo que restamos 1 al mes
        return new Date(partes[2], partes[1] - 1, partes[0]);
    };
    $.fn.calcularStock = function (codpro, stkfra, stkalm, stkalm_m) {
        let valorStock = stkfra;
        let valorMaximo = stkalm * valorStock + stkalm_m;
        let sumas = 0;
        let sumasPorLote = {};
        let sumascantE = 0;
        let sumascantF = 0;
        console.log(sumas);
        if (dataLista.hasOwnProperty(codpro)) {
            let valcodproData = dataLista[codpro];
            for (let valcodalm in valcodproData) {
                if (valcodproData.hasOwnProperty(valcodalm)) {
                    let valloteData = valcodproData[valcodalm];
                    for (let lote in valloteData) {
                        if (!sumasPorLote.hasOwnProperty(lote)) {
                            sumasPorLote[lote] = 0;
                        }
                        if (valloteData.hasOwnProperty(lote)) {
                            if (dataLista[codpro][valcodalm][lote]["cantE"] !== undefined) {
                                sumasPorLote[lote] = sumasPorLote[lote] + (dataLista[codpro][valcodalm][lote]["cantE"] * valorStock);
                                sumascantE = sumascantE + (dataLista[codpro][valcodalm][lote]["cantE"]);
                            }
                            if (dataLista[codpro][valcodalm][lote]["cantF"] !== undefined) {
                                sumasPorLote[lote] = sumasPorLote[lote] + (dataLista[codpro][valcodalm][lote]["cantF"]);
                                sumascantF = sumascantF + (dataLista[codpro][valcodalm][lote]["cantF"]);
                            }
                        }
                    }
                }
            }
        }
        for (let vallote in sumasPorLote) {
            if (sumasPorLote.hasOwnProperty(vallote)) {
                let valloteData = sumasPorLote[vallote];
                let valormaximo = $.fn.obtenerMaximoPorLote(vallote) * valorStock + $.fn.obtenerMaximoFPorLote(vallote);
                if (valormaximo < valloteData) {
                    return true;
                }
            }
        }
        $('#footcantE').text(sumascantE);
        $('#footcantF').text(sumascantF);
        return false;
    };
    $.fn.obtenerSumaTotalPorLote = function () {
        var sumasPorLote = {}; // Objeto para almacenar las sumas por lote
        var sumaTotal = 0; // Variable para almacenar la suma total
        var sumaTotal2 = 0;
        // Iterar sobre todas las filas en la tabla
        $('#table-establecimientos').DataTable().rows().every(function () {
            // Obtener los datos de la fila
            var data = this.data();
            var lote = String(data.lote); // Obtener el código de lote

            // Verificar si el código de lote ya ha sido procesado
            if (!sumasPorLote.hasOwnProperty(lote)) {
                var cantidad = parseFloat(data.qtymov); // Obtener la cantidad
                var cantidad2 = parseFloat(data.qtymov_m);
                // Si es un nuevo lote, crear una nueva entrada en el objeto y agregar la cantidad al total
                sumasPorLote[lote] = true;
                sumaTotal += cantidad;
                sumaTotal2 += cantidad2;
            }
        });
        $('#footlotE').text(sumaTotal);
        $('#footlotF').text(sumaTotal2);
    };
    $.fn.graficar = function (codalm) {
        if (myLineChart2) {
            myLineChart2.destroy();
        }
        console.log(listagraficas);
        if (listagraficas[codalm] === undefined) {
            $("#nombre-grafica2").html("<p style='color:red;'>NO HAY VENTAS EN ESTE ESTABLECIMIENTO</p>");
        } else {
            var ctx = document.getElementById("myAreaChart2").getContext('2d');
            myLineChart2 = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: listagraficas[codalm]["fec"],
                    datasets: [{
                            label: "Ventas",
                            lineTension: 0.3,
                            backgroundColor: "rgba(78, 115, 223, 0.05)",
                            borderColor: "rgba(78, 115, 223, 1)",
                            pointRadius: 3,
                            pointBackgroundColor: "rgba(78, 115, 223, 1)",
                            pointBorderColor: "rgba(78, 115, 223, 1)",
                            pointHoverRadius: 3,
                            pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
                            pointHoverBorderColor: "rgba(78, 115, 223, 1)",
                            pointHitRadius: 10,
                            pointBorderWidth: 2,
                            data: listagraficas[codalm]["ventas"],
                            fill: true,
                            tension: 0.1
                        }]
                },
                options: {
                    animation: {
                        onComplete: function () {
                            var chartInstance = this.chart;
                            var ctx = chartInstance.ctx;
                            ctx.font = Chart.helpers.fontString(Chart.defaults.global.defaultFontSize, 'normal', Chart.defaults.global.defaultFontFamily);
                            ctx.textAlign = 'center';
                            ctx.textBaseline = 'bottom';
                            this.data.datasets.forEach(function (dataset, i) {
                                var meta = chartInstance.controller.getDatasetMeta(i);
                                meta.data.forEach(function (bar, index) {
                                    var data = dataset.data[index];
                                    var offsetY = bar._model.y - 10; // Ajusta el offset vertical según tu necesidad
                                    if (offsetY < 40) {
                                        offsetY = bar._model.y + 20; // Ajuste alternativo si la etiqueta se sale arriba del gráfico
                                    }
                                    ctx.fillText(data, bar._model.x, offsetY);
                                });
                            });

                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    },
                    responsive: true,
                    maintainAspectRatio: false,
                    legend: {
                        display: false
                    },
                    interaction: {
                        mode: 'none' // Desactivar todas las interacciones
                    },
                    hover: {
                        mode: null, // Desactivar hover
                        onHover: null // No hacer nada en hover
                        , animationDuration: 0
                    }
                }
            });
        }
    };
    //SE EJECUTA AL CARGAR LA VENTANA
    $.fn.validarSession();
    $.getJSON("CargosDescargos?opcion=1", function (data) {
        if (data.resultado === "ok") {
            $("#secuencia").val(data.secuencia);

            let almacen = $('#almacen');
            almacen.empty();
            almacen.append('<option value=""></option>');
            $.each(data.almsis, function (key, value) {
                almacen.append('<option value="' + value.codalm + '">' + value.desalm + '</option>');
            });

            let referencia = $('#referencia');
            referencia.empty();
            $.each(data.listrefmov, function (key, value) {
                referencia.append('<option value="' + value.codrefmov + '">' + value.desrefmov + '</option>');
            });

            listaEstab = data.listsis;
            let selectestab = $('#selectestab');
            selectestab.empty();
            selectestab.append('<option value=""></option>');
            let selectestabfiltro = $('#selectestabfiltro');
            selectestabfiltro.empty();
            selectestabfiltro.append('<option value=""></option>');
            $.each(data.listsis, function (key, value) {
                if (value.siscod !== data.siscod) {
                    selectestab.append('<option value="' + value.siscod + '">' + value.sisent + '</option>');
                }
                selectestabfiltro.append('<option value="' + value.siscod + '">' + value.sisent + '</option>');
            });

            $('#f-movimiento').val(new Date().toISOString().slice(0, 10));
            $('#f-documento').val(new Date().toISOString().slice(0, 10));
            $('#f-pago').val(new Date().toISOString().slice(0, 10));
            $('#f-traslado').val(new Date().toISOString().slice(0, 10));

            let proveedor = $('#proveedor');
            proveedor.empty();
            $.each(data.listprov, function (key, value) {
                proveedor.append('<option value="' + value.codprv + '">' + value.desprv + '</option>');
            });

            let tipago = $('#tipago');
            tipago.empty();
            $.each(data.listtippagfac, function (key, value) {
                tipago.append('<option value="' + value.tpacod + '">' + value.tpades + '</option>');
            });

            let moneda = $('#moneda');
            moneda.empty();
            $.each(data.listmon, function (key, value) {
                moneda.append('<option value="' + value.moncod + '">' + value.mondes + '</option>');
            });

            let transportista = $('#transportista');
            transportista.empty();
            $.each(data.listtransp, function (key, value) {
                transportista.append('<option value="' + value.codtrans + '" data-placa="' + value.placavehic + '">' + value.namtrans + '</option>');
            });
            $.fn.actualizarTransportista();

            let motivotraslado = $('#motivotraslado');
            motivotraslado.empty();
            motivotraslado.append('<option value=""></option>');
            $.each(data.listmottrans, function (key, value) {
                motivotraslado.append('<option value="' + value.codmot + '">' + value.desmot + '</option>');
            });

            let undmedida = $('#undmedida');
            undmedida.empty();
            undmedida.append('<option value=""></option>');
            $.each(data.listunid, function (key, value) {
                undmedida.append('<option value="' + value.codund + '">' + value.desund + '</option>');
            });
        } else {
            if (data.mensaje === "nosession") {
                $(location).attr('href', "index.html");
            } else {
                alert("error general");
            }
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        alert("Error al conectarse con el servidor");
    });
    $('#almacen').on('change', function () {
        var selectedOption = $(this).val();
        if (selectedOption !== "") {
            $.getJSON("CRUPFaTipoMovimientos?opcion=1&codalm=" + selectedOption, function (data) {
                if (data.resultado === "ok") {
                    var timovimiento = $('#timovimiento');
                    timovimiento.empty();
                    if (data.data === undefined) {
                        timovimiento.append('<option value=""></option>');
                    } else {
                        $.each(data.data, function (key, value) {
                            timovimiento.append('<option value="' + value.tipkar + '">' + value.destkar + '</option>');
                        });
                    }
                    $.fn.actualizarTipoMovimiento();

                } else {
                    if (data.mensaje === "nosession") {
                        $.fn.validarSession();
                    }
                }
            });
        } else {
            var timovimiento = $('#timovimiento');
            timovimiento.empty();
            timovimiento.append('<option value=""></option>');
            $.fn.actualizarTipoMovimiento();
        }
    });
    $('#timovimiento').on('change', function () {
        $.fn.actualizarTipoMovimiento();

    });
    $('#transportista').on('change', function () {
        $.fn.actualizarTransportista();
    });

    $('#serie').on('change', function () {
        $.fn.actualizarnumdoc();
    });
    $("#cerrarmodal").click(function () {
        $("#modaltablaproductos").css("display", "none");
        $('body').removeClass('modal-open');
        $('.modal-backdrop').remove();
    });
    $("#agregar").click(function () {
        $("#modaltablaproductos").css("display", "block");
        $('#vista-table-establecimientos').show();
        $('body').removeClass('modal-open');
        $('.modal-backdrop').remove();
        $('body').addClass('modal-open');
        $('<div class="modal-backdrop"></div>').appendTo('body');
        $('#detalle').hide();
        $('#producto').show();
    });
    $("#btnNuevaDistribucion").click(function () {
        $('#modal-distribucion').modal('show');
    });
    $('#selectFiltro').on('change', function () {
        var selectedOption = $(this).val();
        // Ocultar todos los campos adicionales al principio
        $('#secuenciaInput').hide();
        $('#tipoProductoTable').hide();
        idsSeleccionados = [];
        // Mostrar los campos adicionales según la opción seleccionada
        if (selectedOption === 'SECUENCIA') {
            $('#secuenciaInput').show();
        } else if (selectedOption === 'TIPO') {
            let parametro = {opcion: 1};
            $.getJSON("crudtiposproducto", parametro, function (resp) {
                $('#tipoProductoTable').show();
                var tabla = $('#tabletipos').DataTable();
                if (tabla) {
                    tabla.destroy();
                }
                tabla = $("#tabletipos").DataTable({
                    "lengthChange": false,
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
                    }, data: resp.data,
                    columns: [
                        {data: 'codtip'},
                        {data: 'destip'},
                        {data: null,
                            render: function (data, type, row) {
                                return '<input type="checkbox" class="checkbox" value="' + row.codtip + '">';
                            }}
                    ]
                });
            }).fail(function (jqXHR, textStatus, errorThrown) {
                console.error("Error en la solicitud AJAX:", textStatus, errorThrown);
            });
        }
    });
    $("#btnAceptar").click(function () {
        if (idsSeleccionados.length !== 0 || $("#selectFiltro").val() === "SECUENCIA" || $("#selectFiltro").val() === "TODOS") {
            if ($("#selectStockMinimo").val() !== "") {
                if ($("#inputSecuencia").val() !== "" || $("#selectFiltro").val() !== "SECUENCIA") {
                    dias = $("#selectStockMinimo").val();
                    tipoDistrib = $("#selectFiltro").val();
                    codtip = JSON.stringify(idsSeleccionados);
                    if (tipoDistrib === "SECUENCIA") {
                        secuencia = $("#inputSecuencia").val();
                    }
                    if (tipoDistrib === "TIPO") {
                        secuencia = "0";
                    }
                    if (tipoDistrib === "TODOS") {
                        secuencia = "0";
                    }
                    if ($("#checkboxFecha").prop('checked')) {
                        indicaFecha = "S";
                        fecha1 = $("#inputFecha").val();
                    } else {
                        indicaFecha = "N";
                        fecha1 = "2024-01-01";
                    }

                    $.fn.ListarTablaCentral();
                    $('#modal-distribucion').modal('hide');
                    listado = true;
                } else {
                    alert("Debe ingresar la secuencia");
                }
            } else {
                alert("Debe ingresar los dias");
            }
        } else {
            if ($("#selectFiltro").val() === "TIPO") {
                alert("Seleccione almenos un tipo de producto");
            }
        }
    });
    $('#table-central').on('click', '.btn', function () {
        codpro = String($(this).data("codpro"));
        let despro = $(this).data("despro");
        let stockFra = $(this).data("stockfra");
        let stkalm = $(this).data("stkalm");
        let stkalm_m = $(this).data("stkalm_m");
        $.fn.listarTablaDistribucion(codpro, despro, stockFra, stkalm, stkalm_m);
        $('#footcantE').text(0);
        $('#footcantF').text(0);
        $('#detalle').show();
        $('#producto').hide();
    });
    $('#tabletipos').on('click', '.checkbox', function () {
        var id = $(this).val();
        // Desmarcar todas las casillas de verificación
        if ($(this).is(':checked')) {
// Añadir el ID a la matriz JSON
            idsSeleccionados.push(id);
        } else {
// Remover el ID de la matriz JSON
            var index = idsSeleccionados.indexOf(id);
            if (index !== -1) {
                idsSeleccionados.splice(index, 1);
            }
        }
    });
    $('#table-establecimientos').on('click', '.name-link', function (e) {
        if (listagraficas.TOTAL !== undefined) {
            $('#modal-grafica').modal('show');
            $("#nombre-grafica2").text("Unidades vendidas de: " + this.innerText);
            e.preventDefault();
            var codigo = String($(this).data('codalm'));
            $.fn.graficar(codigo);
        }
    });
    $('#selectFiltro').on('change', function () {
        var selectedOption = $(this).val();
        // Ocultar todos los campos adicionales al principio
        
        codalm = {selectedOption};
    });
});