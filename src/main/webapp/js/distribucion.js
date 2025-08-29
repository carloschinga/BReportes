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
    },
  });

  // VARIABLES GLOBALES
  let idsSeleccionados = [];
  let estabSeleccionados = [];
  let dias = 30;
  let tipoDistrib = "TODOS";
  let distpor = "STKMIN";
  let codpro = null;
  let secuencia = 0;
  let indicaFecha = "N";
  let mostrarrojos = "N";
  let soloalm = "N";
  let mostrarrojoscentral = "S";
  let dataLista = {};
  let datosExtraproductos = {};
  let datos = "";
  let codtip = {};
  let codalm = {};
  let tempbtn = "STKMIN";
  let datosExtralotes = {};
  let listado = false;
  let llenarautomaticamente = "S";
  let listagraficas;
  let multiplicador = 1;
  let mostrar0central = "N";
  let mostrar0estab = "S";
  /* $('#topbar').load('topbar.html');
     $('#sidebar').load('sidebar.html');
     $('#footer').load('footer.html');*/
  let today = new Date().toISOString().slice(0, 10);
  $("#inputFecha").val(today);
  let fecha1 = $("#inputFecha").val();
  let funcdescargar = true;
  let myLineChart;
  let myLineChart2;
  let peticiongrafica;
  let peticiongrafica2;
  let lotesAgrupados = {};
  codalm = JSON.stringify(estabSeleccionados);
  //VALIDAR LA SESION
  $.fn.validarSession = function () {
    $.getJSON("validarsesion", function (data) {
      if (data.resultado === "ok") {
        $("#lblUsuario").text(data.logi);
      } else {
        $(location).attr("href", "index.html");
      }
    }).fail(function (jqXHR, textStatus, errorThrown) {
      $(location).attr("href", "index.html");
    });
  };
  $.fn.validarSession();
  $.getJSON("ParametrosBartolito?opcion=1", function (data) {
    if (data.resultado === "ok") {
      $("#selectStockMinimo").val(data.num);
      $("#selectStockMinimoproyeccion").val(data.num);
      dias = data.num;
    } else {
      if (data.mensaje === "nosession") {
        $.fn.validarSession();
      } else {
        alert("Error al cargar los datos");
      }
    }
  }).fail(function (jqXHR, textStatus, errorThrown) {
    alert("Error con la conexion con del servidor");
  });
  //FUNCIONES
  $.fn.convertirAFecha = function (cadenaFecha) {
    var partes = cadenaFecha.split("/");
    // El formato de fecha en JavaScript toma el mes como base 0, por lo que restamos 1 al mes
    return new Date(partes[2], partes[1] - 1, partes[0]);
  };
  $.fn.obtenerStockUniPorCodpro = function (codpro) {
    var valorMaximo = 0;
    // Iterar sobre todas las filas en la tabla
    $("#table-central")
      .DataTable()
      .rows()
      .every(function () {
        // Obtener los datos de la fila
        var data = this.data();
        // Verificar si el codpro de esta fila coincide con el codpro buscado
        if (String(data.codpro) === codpro.toString()) {
          valorMaximo = parseFloat(data.stkalm_m);
        }
      });
    return valorMaximo;
  };
  $.fn.obtenerStockFraPorCodpro = function (codpro) {
    var valorMaximo = 0;
    $("#table-central")
      .DataTable()
      .rows()
      .every(function () {
        var data = this.data();
        if (String(data.codpro) === codpro.toString()) {
          valorMaximo = parseFloat(data.stkfra);
        }
      });
    return valorMaximo;
  };
  $.fn.obtenerMaximoPorCodpro = function (codpro) {
    var valorMaximo = 0;
    // Iterar sobre todas las filas en la tabla
    $("#table-central")
      .DataTable()
      .rows()
      .every(function () {
        // Obtener los datos de la fila
        var data = this.data();
        // Verificar si el codpro de esta fila coincide con el codpro buscado
        if (String(data.codpro) === codpro.toString()) {
          valorMaximo = parseFloat(data.stkalm);
        }
      });
    return valorMaximo;
  };
  $.fn.obtenerMaximoPorLote = function (lote) {
    var valorMaximo = 0;
    // Iterar sobre todas las filas en la tabla
    $("#table-establecimientos")
      .DataTable()
      .rows()
      .every(function () {
        // Obtener los datos de la fila
        var data = this.data();
        // Verificar si el codpro de esta fila coincide con el codpro buscado
        if (String(data.lote) === lote) {
          valorMaximo = parseFloat(data.qtymov);
        }
      });
    return valorMaximo;
  };
  $.fn.obtenerSumaTotalPorLote = function () {
    var sumasPorLote = {}; // Objeto para almacenar las sumas por lote
    var sumaTotal = 0; // Variable para almacenar la suma total
    var sumaTotal2 = 0;
    // Iterar sobre todas las filas en la tabla
    $("#table-establecimientos")
      .DataTable()
      .rows()
      .every(function () {
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
    $("#footlotE").text(sumaTotal);
    $("#footlotF").text(sumaTotal2);
  };
  $.fn.obtenerMaximoFPorLote = function (lote) {
    var valorMaximo = 0;
    // Iterar sobre todas las filas en la tabla
    $("#table-establecimientos")
      .DataTable()
      .rows()
      .every(function () {
        // Obtener los datos de la fila
        var data = this.data();
        // Verificar si el codpro de esta fila coincide con el codpro buscado
        if (String(data.lote) === lote) {
          valorMaximo = parseFloat(data.qtymov_m);
        }
      });
    return valorMaximo;
  };
  $.fn.obtenerFecVenPorLote = function (lote) {
    var fecven = "01/01/2099 00:00:00.000";
    // Iterar sobre todas las filas en la tabla
    $("#table-establecimientos")
      .DataTable()
      .rows()
      .every(function () {
        // Obtener los datos de la fila
        var data = this.data();
        // Verificar si el codpro de esta fila coincide con el codpro buscado
        if (String(data.lote) === lote) {
          fecven = data.fecven + " 00:00:00.000";
        }
      });
    return fecven;
  };
  $.fn.graficar = function (codalm) {
    if (myLineChart2) {
      myLineChart2.destroy();
    }
    if (listagraficas[codalm] === undefined) {
      $("#nombre-grafica2").html(
        "<p style='color:red;'>NO HAY VENTAS EN ESTE ESTABLECIMIENTO</p>"
      );
    } else {
      var ctx = document.getElementById("myAreaChart2").getContext("2d");
      myLineChart2 = new Chart(ctx, {
        type: "line",
        data: {
          labels: listagraficas[codalm]["fec"],
          datasets: [
            {
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
              tension: 0.1,
            },
          ],
        },
        options: {
          animation: {
            onComplete: function () {
              var chartInstance = this.chart;
              var ctx = chartInstance.ctx;
              ctx.font = Chart.helpers.fontString(
                Chart.defaults.global.defaultFontSize,
                "normal",
                Chart.defaults.global.defaultFontFamily
              );
              ctx.textAlign = "center";
              ctx.textBaseline = "bottom";
              ctx.fillStyle = "black";
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
            },
          },
          scales: {
            y: {
              beginAtZero: true,
            },
          },
          responsive: true,
          maintainAspectRatio: false,
          legend: {
            display: false,
          },
          interaction: {
            mode: "none", // Desactivar todas las interacciones
          },
          hover: {
            mode: null, // Desactivar hover
            onHover: null, // No hacer nada en hover
            animationDuration: 0,
          },
        },
      });
    }
  };
  $.fn.listarTablaDistribucion = function (codpro, despro) {
    lotesAgrupados = {};
    $.fn.validarSession();

    if (codpro !== null) {
      var tabla = $("#table-establecimientos").DataTable();
      if (tabla) {
        tabla.destroy();
      }

      $("#nombre-grafica").text("CARGANDO...");
      $("#predic").text("Sig mes estimado, Prediccion: Cargando...");
      if (myLineChart) {
        myLineChart.destroy();
      }
      if (peticiongrafica) {
        peticiongrafica.abort();
        console.log("Solicitud abortada");
      }
      if (peticiongrafica2) {
        peticiongrafica2.abort();
        console.log("Solicitud abortada");
      }
      $("#modaltablaproductos").css("display", "block");
      $("#vista-table-establecimientos").show();
      $("body").removeClass("modal-open");
      $(".modal-backdrop").remove();
      $("body").addClass("modal-open");
      $('<div class="modal-backdrop"></div>').appendTo("body");
      if (despro !== undefined) {
        let cadena = "";
        if (distpor === "STKMIN") {
          cadena = "<row><col>Distribucion por Stockminimo: <b>";
        } else if (distpor === "REPOS") {
          cadena = "<row><col>Distribucion por Venta: <b>";
        } else if (distpor === "PARETO") {
          cadena = "<row><col>Distribucion por Pareto: <b>";
        }
        /*$('#datosproducto').html("Producto: <b>" + despro + "</b>, Codigo: " + codpro + ", StockAlm: " +
                 $.fn.obtenerMaximoPorCodpro(codpro) + ",  StockFra: " + $.fn.obtenerStockFraPorCodpro(codpro) +
                 ",   StockAlm_m: " + $.fn.obtenerStockUniPorCodpro(codpro));*/
        /*$('#datosproducto').html("<row><col>Codigo: <b>" + codpro + "</b><col><col> Producto: <b>" + despro + "</b></col><col>StockFra: <b>" +
                 $.fn.obtenerStockFraPorCodpro(codpro)+"</b></col></row>");*/
        $("#datosproducto").html(
          cadena +
            despro +
            "</b><col>, Codigo: <b>" +
            codpro +
            "</b></col></row>"
        );
      }
      let maximadistribucion = 0;
      let maximorequerido = 0;
      let totalesPorLote = [];
      let totalesPorEsta = {};
      listagraficas = {};
      let sugeridos = {};
      $("#footlotE").text(0);
      $("#footlotF").text(0);
      let menorFecha = null;
      let codlotMenorFecha = null;
      let codlotsRegistrados = {};
      let codlotsRegis = {};
      let actualizarbtn = false;
      let stockFra = $.fn.obtenerStockFraPorCodpro(codpro);
      tabla = $("#table-establecimientos").DataTable({
        searching: false,
        paging: false,
        fixedHeader: {
          header: true,
          footer: true,
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
            previous: "Anterior",
          },
          aria: {
            sortAscending: ": activate to sort column ascending",
            sortDescending: ": activate to sort column descending",
          },
        },
        ajax: {
          //url: 'distribucion.jsp?opcion=2&tipoStkMin='+dias+'&tipo_distrib='+codpro+'&mostrarrojos='+mostrarrojos,
          url: "distribucion?opcion=2",
          type: "POST",
          data: function (d) {
            (d.tipoStkMin = dias),
              (d.tipo_distrib = tipoDistrib),
              (d.mostrar0estab = mostrar0estab),
              (d.codpro = codpro),
              (d.mostrarrojos = mostrarrojos),
              (d.soloalm = soloalm),
              (d.codtip = codtip),
              (d.codalm = codalm),
              (d.distpor = distpor),
              (d.multiplicador = 1);
          },
          beforeSend: function () {
            $("#loadingproductos").css("display", "block");
          },
          complete: function () {
            $("#loadingproductos").css("display", "none");
          },
          dataSrc: function (json) {
            let dataCopy = [...json.data];
            console.log("Data original", json.data);

            // Asegurarnos de que los valores de transE y transF se mantengan como vienen del API
            dataCopy.forEach((row) => {
              // Convertir a número y asegurar que no sea NaN
              row.transE = Number(row.transE) || 0;
              row.transF = Number(row.transF) || 0;
              row.qtymov = Number(row.qtymov) || 0;
              row.qtymov_m = Number(row.qtymov_m) || 0;
            });

            // Ordenar la copia de los datos por fecha (más antiguos primero)
            dataCopy.sort(function (a, b) {
              return (
                $.fn.convertirAFecha(a.fecven) - $.fn.convertirAFecha(b.fecven)
              );
            });

            // --- DISTRIBUCIÓN FIFO DE transE y transF ENTRE LOTES POR ALMACÉN ---
            // Agrupar datos por almacén (siscod)
            const datosPorAlmacen = {};
            dataCopy.forEach((item) => {
              const almacen = item.siscod;
              if (!datosPorAlmacen[almacen]) {
                datosPorAlmacen[almacen] = [];
              }
              datosPorAlmacen[almacen].push(item);
            });

            // Aplicar distribución FIFO para cada almacén independientemente
            Object.keys(datosPorAlmacen).forEach((almacen) => {
              const datosAlmacen = datosPorAlmacen[almacen];
              const lotesUnicos = [
                ...new Set(datosAlmacen.map((item) => item.lote)),
              ];

              if (lotesUnicos.length > 1) {
                // 1. Tomar el tránsito total del almacén (es el mismo valor en todas las filas)
                // Solo tomamos el valor de la primera fila ya que es el mismo para todo el almacén
                let totalTransE = Number(datosAlmacen[0].transE) || 0;
                let totalTransF = Number(datosAlmacen[0].transF) || 0;

                // 2. Distribuir el tránsito entre los lotes de este almacén (FIFO)
                let transERestante = totalTransE;
                let transFRestante = totalTransF;

                // Los datos ya están ordenados por fecha (FIFO)
                datosAlmacen.forEach((item) => {
                  let maxE = Number(item.qtymov) || 0;
                  let maxF = Number(item.qtymov_m) || 0;
                  let asignadoE = Math.min(transERestante, maxE);
                  let asignadoF = Math.min(transFRestante, maxF);

                  // Actualizar los valores en el item original
                  item.transE = asignadoE;
                  item.transF = asignadoF;

                  transERestante -= asignadoE;
                  transFRestante -= asignadoF;
                });
              }
            });
            // --- FIN DISTRIBUCIÓN FIFO ---

            // --- AHORA SÍ: CÁLCULO DE TRÁNSITOS Y STOCK POR LOTE (DESPUÉS DEL FIFO) ---
            // Calculamos el total de tránsitos por lote DESPUÉS de la distribución FIFO
            const transitosPorLote = {};
            const stockPorLote = {};

            dataCopy.forEach((row) => {
              const lote = row.lote;

              // Inicializar si no existe
              if (!transitosPorLote[lote]) {
                transitosPorLote[lote] = {
                  totalTransE: 0,
                  totalTransF: 0,
                };
              }
              if (!stockPorLote[lote]) {
                stockPorLote[lote] = {
                  totalStockE: row.qtymov,
                  totalStockF: row.qtymov_m,
                  disponibleE: 0,
                  disponibleF: 0,
                };
              }

              // Sumar tránsitos por lote (ya distribuidos por FIFO)
              transitosPorLote[lote].totalTransE += row.transE;
              transitosPorLote[lote].totalTransF += row.transF;

              // Calcular disponibles por lote (stock total - tránsitos totales del lote)
              Object.keys(stockPorLote).forEach((lote) => {
                const transitoLote = transitosPorLote[lote] || {
                  totalTransE: 0,
                  totalTransF: 0,
                };
                stockPorLote[lote].disponibleE =
                  stockPorLote[lote].totalStockE - transitoLote.totalTransE;
                stockPorLote[lote].disponibleF =
                  stockPorLote[lote].totalStockF - transitoLote.totalTransF;

                // Asegurar que no sean negativos
                stockPorLote[lote].disponibleE = Math.max(
                  0,
                  stockPorLote[lote].disponibleE
                );
                stockPorLote[lote].disponibleF = Math.max(
                  0,
                  stockPorLote[lote].disponibleF
                );
              });

              // --- ASIGNAR DISPONIBLES CALCULADOS A NIVEL DE LOTE ---
              // Asignar los disponibles calculados a nivel de lote a cada fila
              dataCopy.forEach((row) => {
                const lote = row.lote;
                if (stockPorLote[lote]) {
                  // Asignar el disponible del lote completo a esta fila
                  row.disponibleLoteE = stockPorLote[lote].disponibleE;
                  row.disponibleLoteF = stockPorLote[lote].disponibleF;
                }
              });

              // Agrupar por lote (manteniendo la lógica original)
              dataCopy.forEach((row) => {
                const lote = row.lote;

                if (!lotesAgrupados[lote]) {
                  lotesAgrupados[lote] = {
                    sumTransE: 0,
                    sumTransF: 0,
                    cant_e: row.qtymov || 0,
                    cant_f: row.qtymov_m || 0,
                    rows: [],
                  };
                }

                lotesAgrupados[lote].sumTransE += row.transE;
                lotesAgrupados[lote].sumTransF += row.transF;
                lotesAgrupados[lote].rows.push(row);
              });

              Object.keys(lotesAgrupados).forEach((lote) => {
                const loteData = lotesAgrupados[lote];
                loteData.CE_Resto = loteData.cant_e - loteData.sumTransE;
                loteData.CF_Resto = loteData.cant_f - loteData.sumTransF;
              });
            });

            // --- ASIGNAR DISPONIBLES CALCULADOS A NIVEL DE LOTE ---
            // Asignar los disponibles calculados a nivel de lote a cada fila
            dataCopy.forEach((row) => {
              const lote = row.lote;
              if (stockPorLote[lote]) {
                // Asignar el disponible del lote completo a esta fila
                row.disponibleLoteE = stockPorLote[lote].disponibleE;
                row.disponibleLoteF = stockPorLote[lote].disponibleF;
              }
            });

            let blister;
            dataCopy.forEach((item) => {
              if (item.restringido === "N" && item.inmov === "N") {
                let tableta = stockFra;
                blister = 1;
                let master = 1;
                if (item.blister !== undefined) {
                  tableta = stockFra / item.blister;
                  blister = item.blister;
                } else if (item.masterpack !== undefined) {
                  master = item.masterpack;
                }
                let lote = String(item.lote);
                let esta = String(item.siscod);
                let formula = 0;
                formula =
                  ((item.stkalm + item.transE) * stockFra +
                    (item.stkalm_m + item.transF)) /
                  blister;

                let promventa = parseFloat(item.stkmin2);
                valor = Math.ceil(promventa * tableta - formula);

                if (valor > 0) {
                  if (
                    stockFra === 1 ||
                    item.aplicfrac === undefined ||
                    item.aplicfrac === "N"
                  ) {
                    valor = Math.ceil(valor / stockFra) * stockFra;
                  }
                  if (item.masterpack !== undefined) {
                    valor =
                      Math.ceil(valor / (master * stockFra)) *
                      master *
                      stockFra;
                  }
                  if (!totalesPorEsta[esta]) {
                    totalesPorEsta[esta] = { totalCantidad: valor * blister };
                    maximorequerido += valor * blister;
                  }
                }
                let a = true;
                for (let i = 0; i < totalesPorLote.length; i++) {
                  if (totalesPorLote[i].lote === lote) {
                    a = false;
                  }
                }

                if (a) {
                  let cantidadlote =
                    Math.floor(item.qtymov * stockFra + item.qtymov_m) /
                    blister;
                  if (
                    stockFra === 1 ||
                    item.aplicfrac === undefined ||
                    item.aplicfrac === "N"
                  ) {
                    cantidadlote =
                      Math.floor(cantidadlote / stockFra) * stockFra;
                  }
                  if (item.masterpack !== undefined) {
                    cantidadlote =
                      Math.floor(cantidadlote / (master * stockFra)) *
                      master *
                      stockFra;
                  }
                  totalesPorLote.push({
                    lote: lote,
                    cantidad: cantidadlote * blister,
                  });
                  maximadistribucion += cantidadlote * blister;
                }
              }
            });

            if (maximadistribucion >= maximorequerido && maximorequerido > 0) {
              for (let esta in totalesPorEsta) {
                sugeridos[esta] = {};
                let cantidad = totalesPorEsta[esta]["totalCantidad"];
                for (let i = 0; i < totalesPorLote.length; i++) {
                  let lote = totalesPorLote[i].lote;
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

            console.log("Tránsitos por lote:", transitosPorLote);
            console.log("Stock por lote:", stockPorLote);
            console.log("Lotes agrupados:", lotesAgrupados);
            console.log("Data completa:", dataCopy);

            return json.data;
          },
        },
        columns: [
          {
            data: "sisent",
            render: function (data, type, row) {
              return (
                '<a href="#" class="open-modal name-link" data-toggle="modal" data-target="#myModal" data-codalm="' +
                row.codalm +
                '">' +
                data +
                "</a>"
              );
            },
          },
          { data: "lote" },
          {
            data: "fecven",
            render: function (data, type, row) {
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
            },
          },
          {
            data: "qtymov",
            render: function (data, type, row) {
              if (Number(row.cant_e) > 0) {
                return Number(data) + " inmv: " + row.cant_e;
              } else {
                return data;
              }
            },
          },
          {
            data: "qtymov_m",
            render: function (data, type, row) {
              if (Number(row.cant_f) > 0) {
                return Number(data) + " inmv: " + row.cant_f;
              } else {
                return data;
              }
            },
          },
          {
            data: null,
            render: function (data, type, row) {
              return row.disponibleLoteE;
            },
          },
          {
            data: null,
            render: function (data, type, row) {
              return row.disponibleLoteF;
            },
          },
          {
            data: "stkalm",
            render: function (data, type, row) {
              return (
                '<span style="color: red; font-weight: bold;">' +
                data +
                "</span>"
              );
            },
          },
          {
            data: "stkalm_m",
            render: function (data, type, row) {
              return data;
            },
          },
          {
            data: "stkmin2",
            render: function (data, type, row) {
              let entro = false;
              if (parseFloat(row.stkmin2) > 0) {
                let stock =
                  row.stkalm +
                  row.transE +
                  parseFloat(row.stkalm_m + row.transF) / stockFra;
                if (stock / parseFloat(row.stkmin2) < 1) {
                  entro = true;
                }
              }
              if (entro) {
                return (
                  '<b style="color: red;" class="danger-row">' + data + "</b>"
                );
              } else {
                return data;
              }
            },
          },
          {
            data: null,
            render: function (data, type, row) {
              let cadena = "<div class='input-inline'>";
              let formula = 0;
              let tableta = stockFra;
              let blister = 1;
              let master = 1;
              if (row.blister !== undefined) {
                tableta = stockFra / row.blister;
                blister = row.blister;
              } else if (row.masterpack !== undefined) {
                master = row.masterpack;
              }
              let noaplicfrac =
                row.aplicfrac === undefined || row.aplicfrac === "N";
              formula =
                ((row.stkalm + row.transE) * stockFra +
                  (row.stkalm_m + row.transF)) /
                blister;
              //let compr = formula / tableta;
              let valor = 0;
              valor = parseFloat(row.stkmin2) * tableta - formula;
              /*if (parseFloat(row.stkmin2) > 0) {
                             if (compr / parseFloat(row.stkmin2) < multiplicador) {
                             valor = parseFloat(row.stkmin2) * tableta - formula;
                             }
                             }*/
              console.log(row.sisent + ": " + valor);
              let valore;
              if (stockFra === 1 || noaplicfrac) {
                valore = Math.ceil(valor / tableta);
                if (row.masterpack !== undefined) {
                  valore = Math.ceil(valor / (master * tableta)) * master;
                }
              } else {
                valore = Math.floor(valor / tableta);
              }
              if (stockFra > 1 && !noaplicfrac) {
                let formula = 0;
                formula =
                  (parseFloat(row.stkalm + row.transE) * stockFra +
                    parseFloat(row.stkalm_m + row.transF)) /
                  blister;
                let valor = 0;
                valor = parseFloat(row.stkmin2) * tableta - formula;
                console.log(row.sisent + ": " + valor);
                valor = Math.ceil(valor % tableta);
                if (row.blister !== undefined) {
                  valor = valor * row.blister;
                }
                if (valor === stockFra) {
                  valor = 0;
                  valore += 1;
                }
              }
              if (valore > 0) {
                cadena +=
                  '<input type="text" size="5" value="' +
                  valore +
                  '" disabled>';
              } else {
                cadena +=
                  '<input type="text" size="5" value="' + 0 + '" disabled>';
              }
              if (valor > 0 && stockFra > 1 && !noaplicfrac) {
                cadena +=
                  '<input type="text" size="5" value="' + valor + '" disabled>';
              } else {
                cadena +=
                  '<input type="text" size="5" value="' + 0 + '" disabled>';
              }

              cadena += "</div>";
              return cadena;
            },
          },
          {
            data: null,
            render: function (data, type, row) {
              return row.transE + " - " + row.transF;
            },
          },
          {
            data: null,
            render: function (data, type, row) {
              let cadena = "<div class='input-inline'>";
              // añadir a todos como data-ceresto a la clase cantE
              const disponibleE = row.disponibleLoteE;
              // añadir a todos como data-cfresto a la clase cantF
              const disponibleF = row.disponibleLoteF;
              let noaplicfrac =
                row.aplicfrac === undefined || row.aplicfrac === "N";
              if (data.restringido === "N" && data.inmov === "N") {
                if (
                  dataLista[row.codpro] &&
                  dataLista[row.codpro][row.siscod] &&
                  dataLista[row.codpro][row.siscod][row.lote] &&
                  dataLista[row.codpro][row.siscod][row.lote]["cantE"] !==
                    undefined
                ) {
                  cadena +=
                    '<input type="text" class="cantE" id="inputcantE"  value="' +
                    dataLista[row.codpro][row.siscod][row.lote]["cantE"] +
                    '" data-codpro="' +
                    row.codpro +
                    '" data-ceresto="' +
                    disponibleE +
                    '" data-codalm="' +
                    row.siscod +
                    '" data-lote="' +
                    row.lote +
                    '" size="5" data-coscom="' +
                    row.coscom +
                    '" data-igvpro="' +
                    row.igvpro +
                    '" autocomplete="off">';
                } else {
                  if (
                    sugeridos[row.siscod] &&
                    sugeridos[row.siscod][row.lote] !== undefined &&
                    !dataLista[row.codpro] &&
                    Math.ceil(sugeridos[row.siscod][row.lote] / stockFra) > 0
                  ) {
                    if (llenarautomaticamente === "S") {
                      actualizarbtn = true;
                      if (
                        stockFra > 1 &&
                        !noaplicfrac &&
                        Math.floor(sugeridos[row.siscod][row.lote] / stockFra) >
                          0
                      ) {
                        cadena +=
                          '<input type="text" class="cantE" id="inputcantE"  value="' +
                          Math.floor(
                            sugeridos[row.siscod][row.lote] / stockFra
                          ) +
                          '" data-codpro="' +
                          row.codpro +
                          '" data-ceresto="' +
                          disponibleE +
                          '" data-codalm="' +
                          row.siscod +
                          '" data-lote="' +
                          row.lote +
                          '" size="5" data-coscom="' +
                          row.coscom +
                          '" data-igvpro="' +
                          row.igvpro +
                          '" autocomplete="off">';
                      } else if (
                        (stockFra === 1 || noaplicfrac) &&
                        Math.ceil(sugeridos[row.siscod][row.lote] / stockFra) >
                          0
                      ) {
                        cadena +=
                          '<input type="text" class="cantE" id="inputcantE"  value="' +
                          Math.ceil(
                            sugeridos[row.siscod][row.lote] / stockFra
                          ) +
                          '" data-codpro="' +
                          row.codpro +
                          '" data-codalm="' +
                          row.siscod +
                          '" data-ceresto="' +
                          disponibleE +
                          '" data-lote="' +
                          row.lote +
                          '" size="5" data-coscom="' +
                          row.coscom +
                          '" data-igvpro="' +
                          row.igvpro +
                          '" autocomplete="off">';
                      } else {
                        cadena +=
                          '<input type="text" class="cantE" id="inputcantE"  value="" data-codpro="' +
                          row.codpro +
                          '" data-codalm="' +
                          row.siscod +
                          '" data-lote="' +
                          row.lote +
                          '" size="5" data-coscom="' +
                          row.coscom +
                          '" data-ceresto="' +
                          disponibleE +
                          '" data-igvpro="' +
                          row.igvpro +
                          '" autocomplete="off">';
                      }
                    } else {
                      cadena +=
                        '<input type="text" class="cantE" id="inputcantE"  value="" data-codpro="' +
                        row.codpro +
                        '" data-codalm="' +
                        row.siscod +
                        '" data-ceresto="' +
                        disponibleE +
                        '" data-lote="' +
                        row.lote +
                        '" size="5" data-coscom="' +
                        row.coscom +
                        '" data-igvpro="' +
                        row.igvpro +
                        '" autocomplete="off">';
                      /*
                                             return '<input type="text" class="cantE" id="inputcantE"  placeholder="' +
                                             Math.floor(sugeridos[row.siscod][row.lote]) + '" data-codpro="' +
                                             row.codpro + '" data-codalm="' + row.siscod + '" data-lote="' + row.lote +
                                             '" size="5" data-coscom="' + row.coscom + '" data-igvpro="' + row.igvpro + '" autocomplete="off">';
                                             */
                    }
                  } else {
                    cadena +=
                      '<input type="text" class="cantE" id="inputcantE"  value="" data-codpro="' +
                      row.codpro +
                      '" data-codalm="' +
                      row.siscod +
                      '" data-ceresto="' +
                      disponibleE +
                      '" data-lote="' +
                      row.lote +
                      '" size="5" data-coscom="' +
                      row.coscom +
                      '" data-igvpro="' +
                      row.igvpro +
                      '" autocomplete="off">';
                  }
                }
              } else {
                cadena += '<input type="text" class="cantE" size="5" disabled>';
              }
              if (
                data.restringido === "N" &&
                stockFra !== 1 &&
                data.inmov === "N"
              ) {
                if (
                  dataLista[row.codpro] &&
                  dataLista[row.codpro][row.siscod] &&
                  dataLista[row.codpro][row.siscod][row.lote] &&
                  dataLista[row.codpro][row.siscod][row.lote]["cantF"] !==
                    undefined
                ) {
                  cadena +=
                    '<input type="text" class="cantF" id="inputcantF" value="' +
                    dataLista[row.codpro][row.siscod][row.lote]["cantF"] +
                    '" data-codpro="' +
                    row.codpro +
                    '" data-cfresto="' +
                    disponibleF +
                    '" data-codalm="' +
                    row.siscod +
                    '" data-lote="' +
                    row.lote +
                    '" size="5" data-coscom="' +
                    row.coscom +
                    '" data-igvpro="' +
                    row.igvpro +
                    '" autocomplete="off">';
                } else {
                  if (
                    sugeridos[row.siscod] &&
                    sugeridos[row.siscod][row.lote] !== undefined &&
                    !dataLista[row.codpro] &&
                    sugeridos[row.siscod][row.lote] % stockFra > 0
                  ) {
                    if (
                      llenarautomaticamente === "S" &&
                      stockFra > 1 &&
                      !noaplicfrac
                    ) {
                      actualizarbtn = true;
                      cadena +=
                        '<input type="text" class="cantF" id="inputcantF"  value="' +
                        Math.ceil(sugeridos[row.siscod][row.lote] % stockFra) +
                        '" data-codpro="' +
                        row.codpro +
                        '" data-cfresto="' +
                        disponibleF +
                        '" data-codalm="' +
                        row.siscod +
                        '" data-lote="' +
                        row.lote +
                        '" size="5" data-coscom="' +
                        row.coscom +
                        '" data-igvpro="' +
                        row.igvpro +
                        '" autocomplete="off">';
                    } else {
                      cadena +=
                        '<input type="text" class="cantF" id="inputcantF"  value="" data-codpro="' +
                        row.codpro +
                        '" data-cfresto="' +
                        disponibleF +
                        '" data-codalm="' +
                        row.siscod +
                        '" data-lote="' +
                        row.lote +
                        '" size="5" data-coscom="' +
                        row.coscom +
                        '" data-igvpro="' +
                        row.igvpro +
                        '" autocomplete="off">';
                    }
                  } else {
                    cadena +=
                      '<input type="text" class="cantF" id="inputcantF"  value="" data-codpro="' +
                      row.codpro +
                      '" data-cfresto="' +
                      disponibleF +
                      '" data-codalm="' +
                      row.siscod +
                      '" data-lote="' +
                      row.lote +
                      '" size="5" data-coscom="' +
                      row.coscom +
                      '" data-igvpro="' +
                      row.igvpro +
                      '" autocomplete="off">';
                  }
                }
              } else {
                cadena += '<input type="text" class="cantF" size="5" disabled>';
              }
              cadena += "</div>";
              return cadena;
            },
          },
        ],
        rowCallback: function (row, data) {
          // Calcular días hasta el vencimiento
          let venc = $.fn.convertirAFecha(data.fecven).getTime();
          let hoy = new Date().getTime();
          var diferenciaMs = venc - hoy;
          var dias = Math.floor(diferenciaMs / (1000 * 60 * 60 * 24));
          $(row).find("td").eq(9).addClass("danger-row");

          // Resaltar completamente en rojo si vence en menos de 3 meses (90 días)
          if (dias < 90) {
            $(row).addClass("danger-row-row");
            $(row).css({
              "background-color": "#ffcccc", // Fondo rojo claro
              color: "#cc0000", // Texto rojo oscuro
              "font-weight": "bold",
            });
          }
          // Mantener la lógica existente para otros casos
          else if (data.restringido === "S") {
            $(row).addClass("disabled-row");
          } else if (data.inmov === "S") {
            $(row).addClass("inmovilizado-row");
          } else {
            if (Object.keys(codlotsRegis).length > 1) {
              if (data.fecven === codlotMenorFecha) {
                $(row).find("td").eq(1).addClass("danger-row"); // Lote
                $(row).find("td").eq(2).addClass("danger-row"); // Fecha
              } else {
                $(row).find("td").eq(1).addClass("min-row"); // Lote
                $(row).find("td").eq(2).addClass("min-row"); // Fecha
              }
            }
          }
        },
        initComplete: function (settings, json) {
          if (actualizarbtn) {
            $.fn.leerCantE();
            $.fn.leerCantF();
            if ($.fn.calcularStock(codpro)) {
              $("#table-establecimientos input[type='text'].cantE").val("");
              $("#table-establecimientos input[type='text'].cantF").val("");
              if (llenarautomaticamente === "S") {
                alert(
                  "Error al llenar la distribucion automaticamente, porfavor llene manualmente"
                );
              }
              $.fn.leerCantE();
              $.fn.leerCantF();
              $.fn.calcularStock(codpro);
            }
          } else {
            $.fn.calcularStock(codpro);
          }
          $.fn.obtenerSumaTotalPorLote();
          if (stockFra === 1) {
            $(".cantF").prop("disabled", true);
          } else {
            $(".cantF").prop("disabled", false);
          }
          if (actualizarbtn) {
            $.fn.actualizarbtn(codpro);
          }
          /*if (Object.keys(codlotsRegistrados).length === 1) {
                     $("#table-establecimientos").addClass('table-striped');
                     } else {
                     $("#table-establecimientos").removeClass('table-striped');
                     }*/

          peticiongrafica = $.getJSON(
            "distribucion?opcion=4",
            { codpro: codpro },
            function (data) {
              //$("#tab_stkmin2").text(data.stkmin2);
              //$("#ta_stkalm").text(data.stkalm);
              //if (data.stkmin2 - data.stkalm > 0) {
              //    $("#ta_repone").text(data.stkmin2 - data.stkalm);
              //}
              $("#nombre-grafica").text(
                "Unidades vendidas de todos los establecimientos:"
              );
              if (myLineChart) {
                myLineChart.destroy();
              }
              console.log(listagraficas);
              if (data.ventas12 === undefined) {
                $("#nombre-grafica").html(
                  "<p style='color:red;'>NO HAY VENTAS EN ESTE PRODUCTO</p>"
                );
              } else {
                data.ventas12 = data.ventas12.map((value) => Math.round(value));
                var ctx = document
                  .getElementById("myAreaChart")
                  .getContext("2d");
                myLineChart = new Chart(ctx, {
                  type: "line",
                  data: {
                    labels: data.fechas12,
                    datasets: [
                      {
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
                        data: data.ventas12,
                        fill: true,
                        tension: 0.1,
                      },
                    ],
                  },
                  options: {
                    animation: {
                      onComplete: function () {
                        var chartInstance = this.chart;
                        var ctx = chartInstance.ctx;
                        ctx.font = Chart.helpers.fontString(
                          Chart.defaults.global.defaultFontSize,
                          "normal",
                          Chart.defaults.global.defaultFontFamily
                        );
                        ctx.textAlign = "center";
                        ctx.textBaseline = "bottom";
                        ctx.fillStyle = "black";
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
                      },
                    },
                    scales: {
                      yAxes: [
                        {
                          type: "linear", // Tipo de escala lineal
                          display: false, // Oculta el eje Y
                          ticks: {
                            beginAtZero: true, // Comienza en cero si es necesario
                          },
                        },
                      ],
                    },
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                      tooltip: {
                        enabled: false, // Desactivar tooltips
                      },
                    },
                    legend: {
                      display: false,
                    },
                    interaction: {
                      mode: "none", // Desactivar todas las interacciones
                    },
                    hover: {
                      mode: null, // Desactivar hover
                      onHover: null, // No hacer nada en hover
                      animationDuration: 0,
                    },
                  },
                });
              }
              /*
                         const envio = {
                         manual_data: [
                         //[96, 125, 124, 102, 94, 123, 111, 96, 111, 90, 103, 122, 121, 95, 95, 127, 94, 110, 121, 112, 50, 0, 0, 0]
                         data.ventas24
                         ]
                         };
                         const requestOptions = {
                         method: 'POST',
                         headers: {
                         'Content-Type': 'application/json'
                         },
                         body: JSON.stringify(envio)
                         };
                         fetch("http://localhost:5000/predict", requestOptions)
                         .then(response => {
                         if (!response.ok) {
                         throw new Error('Network response was not ok');
                         }
                         return response.json();
                         })
                         .then(data => {
                         // Manejar la respuesta del servidor
                         console.log('Predicción obtenida:', data.prediction);
                         if (data.prediction !== undefined) {
                         if (data.prediction > 0) {
                         $("#predic").text("Sig mes estimado, Prediccion: " + Math.round(data.prediction));
                         } else {
                         $("#predic").text("Sig mes estimado, Prediccion: 0");
                         }
                         } else {
                         $("#predic").text("Sig mes estimado, Prediccion: Error al hacer la prediccion");
                         }
                         })
                         .catch(error => {
                         $("#predic").text("Sig mes estimado, Prediccion: Error con la conexion con el servicio");
                         });*/
              /*
                         const envio = {
                         manual_data:
                         //[96, 125, 124, 102, 94, 123, 111, 96, 111, 90, 103, 122, 121, 95, 95, 127, 94, 110, 121, 112, 50, 0, 0, 0]
                         data.ventas24
                         };
                         const requestOptions = {
                         method: 'POST',
                         headers: {
                         'Content-Type': 'application/json'
                         },
                         body: JSON.stringify(envio)
                         };
                         fetch("http://localhost:5000/predictv2", requestOptions)
                         .then(response => {
                         if (!response.ok) {
                         throw new Error('Network response was not ok');
                         }
                         return response.json();
                         })
                         .then(data => {
                         // Manejar la respuesta del servidor
                         console.log('Predicción obtenida:', data.prediction);
                         if (data.prediction !== undefined) {
                         if (data.prediction > 0) {
                         $("#predic").text("Sig mes estimado, Prediccion: " + Math.round(data.prediction));
                         } else {
                         $("#predic").text("Sig mes estimado, Prediccion: 0");
                         }
                         } else {
                         $("#predic").text("Sig mes estimado, Prediccion: Error al hacer la prediccion");
                         }
                         })
                         .catch(error => {
                         $("#predic").text("Sig mes estimado, Prediccion: Error con la conexion con el servicio");
                         });
                         */
              peticiongrafica2 = $.getJSON(
                "distribucion?opcion=3",
                { codpro: codpro },
                function (data) {
                  //$("#tab_stkmin2").text(data.stkmin2);
                  //$("#ta_stkalm").text(data.stkalm);
                  //if (data.stkmin2 - data.stkalm > 0) {
                  //    $("#ta_repone").text(data.stkmin2 - data.stkalm);
                  //}
                  $("#nombre-grafica").text(
                    "Unidades vendidas de todos los establecimientos:"
                  );
                  listagraficas = data.data;
                  for (const key in listagraficas) {
                    if (listagraficas[key].ventas) {
                      listagraficas[key].ventas = listagraficas[key].ventas.map(
                        (value) => Math.round(value)
                      );
                    }
                  }
                }
              );
            }
          );
        },
      });
    }
  };
  $.fn.calcularStock = function (codpro) {
    let valorStock = $.fn.obtenerStockFraPorCodpro(codpro);
    let valorMaximo =
      $.fn.obtenerMaximoPorCodpro(codpro) * valorStock +
      $.fn.obtenerStockUniPorCodpro(codpro);
    let sumas = 0;
    let sumasPorLote = {};
    let sumascantE = 0;
    let sumascantF = 0;

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
                sumasPorLote[lote] =
                  sumasPorLote[lote] +
                  dataLista[codpro][valcodalm][lote]["cantE"] * valorStock;
                sumascantE =
                  sumascantE + dataLista[codpro][valcodalm][lote]["cantE"];
              }
              if (dataLista[codpro][valcodalm][lote]["cantF"] !== undefined) {
                sumasPorLote[lote] =
                  sumasPorLote[lote] +
                  dataLista[codpro][valcodalm][lote]["cantF"];
                sumascantF =
                  sumascantF + dataLista[codpro][valcodalm][lote]["cantF"];
              }
            }
          }
        }
      }
    }

    // Validar contra CE_Resto y CF_Resto de lotesAgrupados
    let hayError = false;
    $("#table-establecimientos")
      .DataTable()
      .rows()
      .every(function () {
        const rowData = this.data();
        const lote = String(rowData.lote);

        // Obtener los valores máximos de lotesAgrupados
        const loteData = lotesAgrupados[lote];
        const maxCE = parseFloat(loteData?.CE_Resto) || 0;
        const maxCF = parseFloat(loteData?.CF_Resto) || 0;

        let sumE = 0;
        let sumF = 0;

        if (dataLista[codpro]) {
          for (let codalm in dataLista[codpro]) {
            if (dataLista[codpro][codalm][lote]) {
              sumE += dataLista[codpro][codalm][lote]["cantE"] || 0;
              sumF += dataLista[codpro][codalm][lote]["cantF"] || 0;
            }
          }
        }

        if (sumE > maxCE || sumF > maxCF) {
          hayError = true;
          return false; // Detener la iteración
        }
        return true; // Continuar
      });

    if (hayError) {
      return true;
    }

    // Validación por lote individual
    for (let vallote in sumasPorLote) {
      if (sumasPorLote.hasOwnProperty(vallote)) {
        let valloteData = sumasPorLote[vallote];
        let valormaximo =
          $.fn.obtenerMaximoPorLote(vallote) * valorStock +
          $.fn.obtenerMaximoFPorLote(vallote);
        if (valormaximo < valloteData) {
          return true;
        }
      }
    }

    $("#footcantE").text(sumascantE);
    $("#footcantF").text(sumascantF);
    $.fn.actualizarbtn(codpro);
    return false;
  };

  $.fn.descargartxt = function (contenido, nombreArchivo) {
    const blob = new Blob([contenido], { type: "text/plain" });
    // Crear un objeto URL para el blob
    const url = window.URL.createObjectURL(blob);
    // Crear un enlace temporal
    const enlace = document.createElement("a");
    enlace.href = url;
    enlace.download = nombreArchivo;
    // Simular un clic en el enlace
    document.body.appendChild(enlace);
    enlace.click();
    // Limpiar
    window.URL.revokeObjectURL(url);
    document.body.removeChild(enlace);
  };
  $.fn.formatofecha = function (fechaISO) {
    let fecha = new Date(fechaISO);
    if (isNaN(fecha.getTime())) {
      return "";
    }

    let fechaFormateada = `${padLeft(fecha.getDate())}/${padLeft(
      fecha.getMonth() + 1
    )}/${fecha.getFullYear()} ${padLeft(fecha.getHours())}:${padLeft(
      fecha.getMinutes()
    )}:${padLeft(fecha.getSeconds())}.${padRight(fecha.getMilliseconds(), 3)}`;
    function padLeft(value) {
      return $.trim(value.toString()).padStart(2, "0");
    }

    function padRight(value, length) {
      return $.trim(value.toString()).padEnd(length, "0");
    }

    return fechaFormateada;
  };
  $.fn.formatonumero = function (numero) {
    if (isNaN(numero) || numero === undefined || numero === null) {
      return "0.00";
    }
    let numeroFormateado = parseFloat(numero).toFixed(2);
    return numeroFormateado.replace(",", ".");
  };
  $.fn.predef = function (value, type) {
    if (value === null || value === undefined) {
      switch (type.toLowerCase()) {
        case "string":
          return "";
        case "int":
        case "integer":
          return 0;
        case "float":
        case "number":
          return 0.0;
        case "boolean":
          return false;
        case "date":
          return new Date();
        default:
          return null;
      }
    }
    return value;
  };
  $.fn.actualizarbtn = function (codpro) {
    var tabla = $("#table-central").DataTable();
    var indiceFila = -1;
    console.log("llego");
    tabla.rows().every(function () {
      var datosFila = this.data();
      var codigoProductoFila = datosFila.codpro;
      if (codigoProductoFila === codpro.toString()) {
        indiceFila = this.index();
        return false;
      }
    });
    if (indiceFila !== -1) {
      var nodoFila = tabla.row(indiceFila).node();
      // Obtener el botón de la fila encontrada
      var boton = $(nodoFila).find(".btn");
      if (dataLista[codpro]) {
        if (boton.data("tienerojos") === "S") {
          boton.removeClass("btn-danger").addClass("btn-warning");
        } else {
          boton.removeClass("btn-info").addClass("btn-warning");
        }
      } else {
        if (boton.data("tienerojos") === "S") {
          boton.removeClass("btn-warning").addClass("btn-danger");
        } else {
          boton.removeClass("btn-warning").addClass("btn-info");
        }
      }
    }
  };
  $.fn.ListarTablaCentral = function () {
    $.fn.validarSession();
    let tabla = $("#table-central").DataTable();
    if (tabla) {
      tabla.destroy();
    }
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
          previous: "Anterior",
        },
        aria: {
          sortAscending: ": activate to sort column ascending",
          sortDescending: ": activate to sort column descending",
        },
      },
      ajax: {
        //url: 'distribucion?opcion=2&tipoStkMin='+dias+'&tipo_distrib='+codpro+'&mostrarrojos='+mostrarrojos,
        url: "distribucion?opcion=1",
        type: "POST",
        data: function (d) {
          (d.tipoStkMin = dias),
            (d.tipoDistrib = tipoDistrib),
            (d.codtip = codtip),
            (d.codalm = codalm),
            (d.soloalm = soloalm),
            (d.multiplicador = multiplicador),
            (d.secuencia = secuencia),
            (d.indicaFecha = indicaFecha),
            (d.fecha1 = fecha1),
            (d.solorojos = mostrarrojoscentral),
            (d.distpor = distpor),
            (d.mostrar0central = mostrar0central);
        },
        beforeSend: function () {
          $("#loadingcentral").css("display", "block");
        },
        complete: function () {
          $("#loadingcentral").css("display", "none");
        },
      },
      columns: [
        { data: "destip" },
        { data: "codpro" },
        { data: "despro" },
        { data: "codlab" },
        { data: "stkfra" },
        { data: "stkalm" },
        { data: "stkalm_m" },
        {
          data: null,
          render: function (data, type, row) {
            if (dataLista[row.codpro]) {
              return (
                '<button class="btn btn-warning btn-sm" data-tienerojos="' +
                row.tienerojos +
                '"  data-codpro="' +
                row.codpro +
                '" data-despro="' +
                $("<div>").text(row.despro).html() +
                '"><i class="fas fa-edit"></i></button>'
              );
            } else {
              if (row.tienerojos === "S") {
                return (
                  '<button class="btn btn-danger btn-sm" data-tienerojos="' +
                  row.tienerojos +
                  '"  data-codpro="' +
                  row.codpro +
                  '" data-despro="' +
                  $("<div>").text(row.despro).html() +
                  '"><i class="fas fa-edit"></i></button>'
                );
              } else {
                return (
                  '<button class="btn btn-primary btn-sm" data-tienerojos="' +
                  row.tienerojos +
                  '"  data-codpro="' +
                  row.codpro +
                  '" data-despro="' +
                  $("<div>").text(row.despro).html() +
                  '"><i class="fas fa-edit"></i></button>'
                );
              }
            }
          },
        },
      ],
    });
  };
  $("#guardartxt").click(function () {
    guardar();
  });

  let guardando = false;
  function guardar() {
    if (Object.keys(dataLista).length > 0) {
      const transformedData = [];

      Toast.fire({
        icon: "info",
        title: "Enviando datos...",
      });

      Object.entries(dataLista).forEach(([codpro, siscodData]) => {
        Object.entries(siscodData).forEach(([siscod, loteData]) => {
          Object.entries(loteData).forEach(([lote, values]) => {
            // Solo agregar si al menos uno de los valores es mayor que 0
            const cante = values.cantE || 0;
            const cantf = values.cantF || 0;

            if (cante > 0 || cantf > 0) {
              transformedData.push({
                codpro: codpro,
                siscod: Number(siscod),
                lote: lote,
                cante: cante,
                cantf: cantf,
              });
            }
          });
        });
      });

      // Verificar si hay datos válidos para enviar
      if (transformedData.length > 0) {
        if (!guardando) {
          guardando = true;
          $.ajax({
            url: "picking?opcion=32",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(transformedData, null),
            dataType: "json",
            success: function (data) {
              if (data.resultado === "ok") {
                let cod = data.mensaje;
                Toast.fire({
                  icon: "success",
                  title: "Guardado correctamente.",
                });
                dataLista = {};
                datosExtraproductos = {};
                $.fn.ListarTablaCentral();

                Swal.fire({
                  title: "Código de distribución guardado correctamente",
                  text:
                    "Código de distribución guardado con codigo de distribucion " +
                    cod,
                  icon: "success",
                  confirmButtonText: "Aceptar",
                }).then((result) => {});
              } else if (data.mensaje === "nosession") {
                $.fn.validarSession();
              } else {
                alert("Error al guardar la distribucion.");
              }
              guardando = false;
            },
            error: function (jqXHR, textStatus, errorThrown) {
              if (jqXHR.status === 400) {
                alert(
                  "Error: Demasiados archivos por generar, Debe hacer una limpieza para continuar"
                );
              } else {
                alert("Error en la solicitud: " + textStatus);
              }
              guardando = false;
            },
          });
        }
      } else {
        Toast.fire({
          icon: "warning",
          title:
            "No hay datos válidos para guardar. Por favor, ingrese cantidades mayores a 0.",
        });
      }
    } else {
      Toast.fire({
        icon: "warning",
        title: "Por favor, Ingresa datos en los almacenes.",
      });
    }
  }
  $("#limpiarTabla").click(function () {
    // Limpiar todos los inputs de cantidad E y F
    $("#table-establecimientos input.cantE").val("");
    $("#table-establecimientos input.cantF").val("");

    // Limpiar los datos almacenados
    if (codpro && dataLista[codpro]) {
      delete dataLista[codpro];
      $.fn.actualizarbtn(codpro);
    }
  });
  $.fn.descargar = function (cabecera2) {
    $.fn.validarSession();
    console.log("Ingreso a la funcion" + cabecera2);
    if (Object.keys(dataLista).length > 0) {
      let objetoInvertido = {};
      for (let codpro in dataLista) {
        let datosCodpro = dataLista[codpro];
        for (let siscod in datosCodpro) {
          let datosSiscod = datosCodpro[siscod];
          if (!objetoInvertido[siscod]) {
            objetoInvertido[siscod] = {};
          }

          objetoInvertido[siscod][codpro] = datosSiscod;
        }
      }
      let primerosIndices = [];
      for (let siscod in objetoInvertido) {
        let codpros = objetoInvertido[siscod];
        // Verificar si hay al menos un objeto dentro del segundo índice (codpros)
        let tieneObjetos = Object.keys(codpros).length > 0;
        // Verificar si alguno de los objetos en codpros tiene valores positivos en sus lotes
        let tieneValoresPositivos = false;
        for (let codpro in codpros) {
          let lotes = codpros[codpro];
          for (let lote in lotes) {
            let cantE = lotes[lote].cantE;
            let cantF = lotes[lote].cantF;
            if (
              (cantE !== undefined && cantE > 0) ||
              (cantF !== undefined && cantF > 0)
            ) {
              tieneValoresPositivos = true;
              break;
            }
          }

          if (tieneValoresPositivos) {
            break;
          }
        }

        if (tieneObjetos && tieneValoresPositivos) {
          primerosIndices.push(siscod);
        }
      }

      if (primerosIndices.length === 0) {
        alert("No se encontraron datos para procesar.");
        return;
      } else {
        let primerosIndicesJSON = JSON.stringify(primerosIndices);
        let objetoTransformado = [];
        for (let siscod in objetoInvertido) {
          let datos = [];
          for (let codpro in objetoInvertido[siscod]) {
            let lotes = objetoInvertido[siscod][codpro];
            for (let lote in lotes) {
              let valores = lotes[lote];
              datos.push({
                codpro: codpro,
                lote: lote,
                cantE: valores.cantE || 0,
                cantF: valores.cantF || 0,
              });
            }
          }

          objetoTransformado.push({
            siscod: siscod,
            datos: datos,
          });
        }
        let codalminv;
        $.getJSON("validarsesion", function (data) {
          if (data.resultado === "ok") {
            codalminv = data.codalminv;
          } else {
            $(location).attr("href", "index.html");
          }
        }).fail(function (jqXHR, textStatus, errorThrown) {
          $(location).attr("href", "index.html");
        });
        if (funcdescargar) {
          funcdescargar = false;
          json = { datos: objetoTransformado, indices: primerosIndices };
          $.ajax({
            url: "generartxtdistribucion", // URL de la API
            method: "POST", // Cambiado a POST
            contentType: "application/json", // Asegúrate de que el servidor espera JSON
            data: JSON.stringify(json, null), // Convertir los datos a una cadena JSON
            dataType: "json", // Espera una respuesta JSON del servidor
            success: function (data) {
              let cabecera = data.listacabecera;
              let detalle = data.listadetalle;
              let cadena = "";
              let numero = 1;

              for (let clavePrincipal in objetoInvertido) {
                cadena = "";
                let cabeceraesp = cabecera.filter(
                  (elemento) => elemento.siscod_d === parseInt(clavePrincipal)
                );
                let productos = objetoInvertido[clavePrincipal];
                let nuevoarc = true;
                let sumvalpar = 0;
                let sumigvpar = 0;
                let sumtotpar = 0;
                for (let clavesecundaria in productos) {
                  let lotes = productos[clavesecundaria];
                  for (let lote in lotes) {
                    let valpar = 0;
                    let igvpar = 0;
                    let totpar = 0;
                    //let detalleesp = detalle.filter(elemento => elemento.siscod === parseInt(clavePrincipal) && elemento.codpro === clavesecundaria && elemento.lote === lote);
                    if (
                      datosExtraproductos[clavesecundaria]["coscom"] !==
                      undefined
                    ) {
                      if (
                        dataLista[clavesecundaria][clavePrincipal][lote][
                          "cantE"
                        ] !== undefined
                      ) {
                        valpar =
                          dataLista[clavesecundaria][clavePrincipal][lote][
                            "cantE"
                          ] * datosExtraproductos[clavesecundaria]["coscom"];
                      }
                      sumvalpar += valpar;
                      if (datosExtraproductos[clavesecundaria]["igvpro"]) {
                        igvpar =
                          valpar *
                          datosExtraproductos[clavesecundaria]["igvpro"];
                        sumigvpar += igvpar;
                      }
                      totpar = valpar + igvpar;
                      sumtotpar += totpar;
                    }
                  }
                }
                if (cabecera2 !== 0) {
                  cadena = cadena + "invnum" + "\t"; //agregamos invnum
                  cadena = cadena + "codpro" + "\t"; //agregamos codpro
                  cadena = cadena + "qtppro" + "\t"; //agregamos qtppro
                  cadena = cadena + "qtppro_m" + "\t"; //agregamos qtppro_m
                  cadena = cadena + "qtbpro" + "\t"; //agregamos qtbpro
                  cadena = cadena + "qtypro" + "\t"; //agregamos qtbpro
                  cadena = cadena + "qtypro_m" + "\t"; //agregamos qtbpro
                  cadena = cadena + "vvfsal" + "\t"; //agregamos vvfsal
                  cadena = cadena + "pvfsal" + "\t"; //agregamos pvfsal
                  cadena = cadena + "pvpsal" + "\t"; //agregamos pvpsal
                  cadena = cadena + "dtopro1" + "\t"; //agregamos dtopro1
                  cadena = cadena + "dtopro2" + "\t"; //agregamos dtopro2
                  cadena = cadena + "dtopro3" + "\t"; //agregamos dtopro3
                  cadena = cadena + "dtopro4" + "\t"; //agregamos dtopro4
                  //cadena = cadena + $.fn.formatofecha(detalleesp[0].fecven) + "\t";//agregamos fecven
                  cadena = cadena + "fecven" + "\t"; //agregamos fecha vencimiento
                  cadena = cadena + "util_vta" + "\t"; //agregamos util_vta
                  cadena = cadena + "dscto_vta" + "\t"; //agregamos dscto_vta
                  cadena = cadena + "invnum_c" + "\t"; //agregamos invnum_c
                  cadena = cadena + "codalm" + "\t"; //agregamos codalm
                  cadena = cadena + "tipkar" + "\t"; //agregamos tipkar
                  cadena = cadena + "numdoc" + 1 + "\t"; //agregamos numdoc
                  cadena = cadena + "purnum" + "\t"; //agregamos purnum
                  cadena = cadena + "fecdoc" + "\t"; //agregamos fecdoc
                  cadena = cadena + "fecpag" + "\t"; //agregamos fecpag
                  cadena = cadena + "tpacod" + "\t"; //agregamos tpacod
                  //cadena = cadena + "totparsumatoria: ";
                  cadena = cadena + "totmov" + "\t"; //agregamos totmov

                  cadena = cadena + "dtomov" + "0\t"; //agregamos dtomov
                  cadena = cadena + "cargmov" + "0\t"; //agregamos cargmov

                  cadena = cadena + "igvmov" + "\t"; //agregamos igvmov
                  cadena = cadena + "vvfmov" + "\t"; //agregamos vvfmov

                  cadena = cadena + "obsmov" + "\t"; //agregamos obsmov
                  cadena = cadena + "codalm2\t"; //agregamos codalm2
                  cadena = cadena + "codprv" + "\t"; //agregamos codprv
                  cadena = cadena + "dtopro1" + "\t"; //agregamos dtopro1_c
                  cadena = cadena + "dtopro2" + "\t"; //agregamos dtopro2_c
                  cadena = cadena + "moncod" + "\t"; //agregamos moncod
                  cadena = cadena + "codccs\t"; //agregamos codccs
                  cadena = cadena + "invkar" + "\t"; //agregamos invkar
                  cadena = cadena + "fecmov" + "\t"; //agregamos fecmov
                  cadena = cadena + "serdoc" + "\t"; //agregamos serdoc
                  cadena = cadena + "campoX\t"; //agregamos campo_x
                  cadena = cadena + "movndias" + "\t"; //agregamos serdoc
                  cadena = cadena + "tdofac" + "\t"; //agregamos serdoc
                  cadena = cadena + "siscod_d" + "\t"; //agregamos siscod_d
                  cadena = cadena + "almtrsta" + "\t"; //agregamos almtrsta
                  cadena = cadena + "lote" + "\t"; //agregamos codlot
                  cadena = cadena + "campo1\t"; //agregamos campo1
                  cadena = cadena + "campo2\t"; //agregamos campo2
                  cadena = cadena + "campo3\t"; //agregamos campo3
                  cadena = cadena + "campo4\t"; //agregamos campo4
                  cadena = cadena + "codrefmov\t"; //agregamos codrefmov
                  cadena = cadena + "ccnoprgnsta\t"; //agregamos ccnoprgnsta
                  cadena = cadena + "campo5\t"; //agregamos campo5
                  cadena = cadena + "obsmovdet"; //agregamos obsmovdet
                  cadena = cadena + "\n";
                }
                for (let clavesecundaria in productos) {
                  let lotes = productos[clavesecundaria];
                  for (let lote in lotes) {
                    let detalleesp = detalle.filter(
                      (elemento) =>
                        elemento.siscod === parseInt(clavePrincipal) &&
                        elemento.codpro === clavesecundaria &&
                        elemento.lote === lote
                    );
                    let cantE = lotes[lote].cantE;
                    let cantF = lotes[lote].cantF;
                    if (
                      (cantE !== undefined && cantE > 0) ||
                      (cantF !== undefined && cantF > 0)
                    ) {
                      if (nuevoarc) {
                        nuevoarc = false;
                      } else {
                        //cadena = cadena + "\n";
                      }
                      cadena =
                        cadena +
                        (parseInt(cabeceraesp[0].invnum) + numero) +
                        "\t"; //agregamos invnum
                      cadena = cadena + clavesecundaria + "\t"; //agregamos codpro
                      cadena = cadena + detalleesp[0].qtppro + "\t"; //agregamos qtppro
                      cadena = cadena + detalleesp[0].qtppro_m + "\t"; //agregamos qtppro_m
                      cadena = cadena + detalleesp[0].qtbpro + "\t"; //agregamos qtbpro
                      if (cantE !== undefined) {
                        cadena = cadena + cantE + "\t"; //agregamos qtypro
                      } else {
                        cadena = cadena + "0\t";
                      }
                      if (cantF !== undefined) {
                        cadena = cadena + cantF + "\t"; //agregamos qtypro_m}
                      } else {
                        cadena = cadena + "0\t";
                      }
                      cadena =
                        cadena +
                        $.fn.formatonumero(detalleesp[0].vvfsal) +
                        "\t"; //agregamos vvfsal
                      cadena =
                        cadena +
                        $.fn.formatonumero(detalleesp[0].pvfsal) +
                        "\t"; //agregamos pvfsal
                      cadena =
                        cadena +
                        $.fn.formatonumero(detalleesp[0].pvpsal) +
                        "\t"; //agregamos pvpsal
                      cadena = cadena + detalleesp[0].dtopro1 + "\t"; //agregamos dtopro1
                      cadena = cadena + detalleesp[0].dtopro2 + "\t"; //agregamos dtopro2
                      cadena = cadena + detalleesp[0].dtopro3 + "\t"; //agregamos dtopro3
                      cadena = cadena + detalleesp[0].dtopro4 + "\t"; //agregamos dtopro4
                      //cadena = cadena + $.fn.formatofecha(detalleesp[0].fecven) + "\t";//agregamos fecven
                      cadena =
                        cadena +
                        datosExtralotes[clavesecundaria][lote]["fecven"] +
                        "\t"; //agregamos fecha vencimiento
                      cadena =
                        cadena +
                        $.fn.formatonumero(detalleesp[0].util_vta) +
                        "\t"; //agregamos util_vta
                      cadena =
                        cadena +
                        $.fn.formatonumero(detalleesp[0].dscto_vta) +
                        "\t"; //agregamos dscto_vta
                      cadena =
                        cadena +
                        (parseInt(cabeceraesp[0].invnum) + numero) +
                        "\t"; //agregamos invnum_c
                      //cadena = cadena + cabeceraesp[0].codalm + "\t"; //agregamos codalm
                      cadena = cadena + codalminv + "\t"; //agregamos codalm asignado
                      cadena = cadena + cabeceraesp[0].tipkar + "\t"; //agregamos tipkar
                      cadena =
                        cadena +
                        (parseInt(cabeceraesp[0].numdoc) + numero) +
                        "\t"; //agregamos numdoc
                      cadena = cadena + cabeceraesp[0].purnum + "\t"; //agregamos purnum
                      cadena =
                        cadena +
                        $.fn.formatofecha(cabeceraesp[0].fecdoc) +
                        "\t"; //agregamos fecdoc
                      cadena =
                        cadena +
                        $.fn.formatofecha(cabeceraesp[0].fecpag) +
                        "\t"; //agregamos fecpag
                      cadena = cadena + cabeceraesp[0].tpacod + "\t"; //agregamos tpacod
                      //cadena = cadena + "totparsumatoria: ";
                      cadena = cadena + $.fn.formatonumero(sumtotpar) + "\t"; //agregamos totmov

                      cadena = cadena + "0\t"; //agregamos dtomov
                      cadena = cadena + "0\t"; //agregamos cargmov

                      cadena = cadena + $.fn.formatonumero(sumigvpar) + "\t"; //agregamos igvmov
                      cadena = cadena + $.fn.formatonumero(sumvalpar) + "\t"; //agregamos vvfmov

                      cadena = cadena + cabeceraesp[0].obsmov + "\t"; //agregamos obsmov
                      cadena = cadena + "\t"; //agregamos codalm2
                      cadena = cadena + cabeceraesp[0].codprv + "\t"; //agregamos codprv
                      cadena = cadena + cabeceraesp[0].dtopro1 + "\t"; //agregamos dtopro1_c
                      cadena = cadena + cabeceraesp[0].dtopro2 + "\t"; //agregamos dtopro2_c
                      cadena = cadena + cabeceraesp[0].moncod + "\t"; //agregamos moncod
                      cadena = cadena + "\t"; //agregamos codccs
                      cadena =
                        cadena +
                        (parseInt(cabeceraesp[0].invkar) + numero) +
                        "\t"; //agregamos invkar
                      cadena =
                        cadena +
                        $.fn.formatofecha(cabeceraesp[0].fecmov) +
                        "\t"; //agregamos fecmov
                      cadena = cadena + cabeceraesp[0].serdoc + "\t"; //agregamos serdoc
                      cadena = cadena + "\t"; //agregamos campo_x
                      cadena = cadena + cabeceraesp[0].movndias + "\t"; //agregamos serdoc
                      cadena = cadena + cabeceraesp[0].tdofac + "\t"; //agregamos serdoc
                      cadena = cadena + cabeceraesp[0].siscod_d + "\t"; //agregamos siscod_d
                      cadena = cadena + cabeceraesp[0].almtrsta + "\t"; //agregamos almtrsta
                      cadena = cadena + detalleesp[0].lote + "\t"; //agregamos codlot
                      cadena = cadena + "\t"; //agregamos campo1
                      cadena = cadena + "\t"; //agregamos campo2
                      cadena = cadena + "\t"; //agregamos campo3
                      cadena = cadena + "\t"; //agregamos campo4
                      cadena = cadena + "0000\t"; //agregamos codrefmov
                      cadena = cadena + "N\t"; //agregamos ccnoprgnsta
                      cadena = cadena + "\t"; //agregamos campo5
                      cadena = cadena + ""; //agregamos obsmovdet

                      cadena = cadena + "\n";
                    }
                  }
                }
                if (!nuevoarc) {
                  let fechaActual = new Date();
                  let año = fechaActual.getFullYear();
                  let mes = ("0" + (fechaActual.getMonth() + 1)).slice(-2);
                  let dia = ("0" + fechaActual.getDate()).slice(-2);
                  let horas = ("0" + fechaActual.getHours()).slice(-2);
                  let minutos = ("0" + fechaActual.getMinutes()).slice(-2);
                  let segundos = ("0" + fechaActual.getSeconds()).slice(-2);
                  let fechaHoraActual =
                    año + mes + dia + horas + minutos + segundos;
                  let nombreArchivo =
                    "DESCARGOENVIO_" +
                    clavePrincipal +
                    "_" +
                    fechaHoraActual +
                    "_" +
                    (parseInt(cabeceraesp[0].invnum) + 1) +
                    ".txt";
                  $.fn.descargartxt(cadena, nombreArchivo);
                  numero = numero + 1;
                }
              }
            },
            error: function (jqXHR, textStatus, errorThrown) {
              if (jqXHR.status === 400) {
                alert(
                  "Error: Demasiados archivos por generar, Debe hacer una limpieza para continuar"
                );
              } else {
                alert("Error en la solicitud: " + textStatus);
              }
            },
          }).always(function () {
            funcdescargar = true;
          });
        }
      }
    } else {
      alert("Por favor ingrese datos en las almacenes");
    }
  };
  $.fn.leerCantE = function () {
    $("#table-establecimientos")
      .DataTable()
      .rows()
      .every(function () {
        // Obtener los datos de la fila
        var valorInput = $(this.node()).find(".cantE").val();
        var valcodpro = String($(this.node()).find(".cantE").data("codpro"));
        var valcodalm = String($(this.node()).find(".cantE").data("codalm"));
        var vallote = String($(this.node()).find(".cantE").data("lote"));
        var coscom = $(this.node()).find(".cantE").data("coscom");
        var igvpro = $(this.node()).find(".cantE").data("igvpro");
        var data = parseFloat(valorInput);
        if (!isNaN(data)) {
          // Comprobar si ya existe un registro con valcodpro y valcodalm iguales
          if (
            dataLista[valcodpro] &&
            dataLista[valcodpro][valcodalm] &&
            dataLista[valcodpro][valcodalm][vallote] &&
            dataLista[valcodpro][valcodalm][vallote]["cantE"] !== undefined
          ) {
            // Si existe, reemplazar el valorInput
            dataLista[valcodpro][valcodalm][vallote]["cantE"] = data;
          } else {
            // Si no existe, crear un nuevo registro
            if (!dataLista[valcodpro]) {
              dataLista[valcodpro] = {};
              datosExtraproductos[valcodpro] = {};
              datosExtraproductos[valcodpro]["igvpro"] = igvpro;
              datosExtraproductos[valcodpro]["coscom"] = coscom;
              datosExtralotes[valcodpro] = {};
            }
            if (!dataLista[valcodpro][valcodalm]) {
              dataLista[valcodpro][valcodalm] = {};
            }
            if (!dataLista[valcodpro][valcodalm][vallote]) {
              dataLista[valcodpro][valcodalm][vallote] = {};
              datosExtralotes[valcodpro][vallote] = {};
              datosExtralotes[valcodpro][vallote]["fecven"] =
                $.fn.obtenerFecVenPorLote(vallote);
            }
            dataLista[valcodpro][valcodalm][vallote]["cantE"] = data;
          }
        } else {
          if (
            dataLista[valcodpro] &&
            dataLista[valcodpro][valcodalm] &&
            dataLista[valcodpro][valcodalm][vallote] &&
            dataLista[valcodpro][valcodalm][vallote]["cantE"] !== undefined
          ) {
            delete dataLista[valcodpro][valcodalm][vallote]["cantE"];
          }
        }
      });
  };
  $.fn.leerCantF = function () {
    $("#table-establecimientos")
      .DataTable()
      .rows()
      .every(function () {
        // Obtener los datos de la fila
        var valorInput = $(this.node()).find(".cantF").val();
        var valcodpro = String($(this.node()).find(".cantF").data("codpro"));
        var valcodalm = String($(this.node()).find(".cantF").data("codalm"));
        var vallote = String($(this.node()).find(".cantF").data("lote"));
        var coscom = $(this.node()).find(".cantF").data("coscom");
        var igvpro = $(this.node()).find(".cantF").data("igvpro");
        var data = parseFloat(valorInput);
        if (!isNaN(data)) {
          // Comprobar si ya existe un registro con valcodpro y valcodalm iguales
          if (
            dataLista[valcodpro] &&
            dataLista[valcodpro][valcodalm] &&
            dataLista[valcodpro][valcodalm][vallote] &&
            dataLista[valcodpro][valcodalm][vallote]["cantF"] !== undefined
          ) {
            // Si existe, reemplazar el valorInput
            dataLista[valcodpro][valcodalm][vallote]["cantF"] = data;
          } else {
            // Si no existe, crear un nuevo registro
            if (!dataLista[valcodpro]) {
              dataLista[valcodpro] = {};
              datosExtraproductos[valcodpro] = {};
              datosExtralotes[valcodpro] = {};
              datosExtraproductos[valcodpro]["igvpro"] = igvpro;
              datosExtraproductos[valcodpro]["coscom"] = coscom;
            }
            if (!dataLista[valcodpro][valcodalm]) {
              dataLista[valcodpro][valcodalm] = {};
            }
            if (!dataLista[valcodpro][valcodalm][vallote]) {
              dataLista[valcodpro][valcodalm][vallote] = {};
              datosExtralotes[valcodpro][vallote] = {};
              datosExtralotes[valcodpro][vallote]["fecven"] =
                $.fn.obtenerFecVenPorLote(vallote);
            }
            dataLista[valcodpro][valcodalm][vallote]["cantF"] = data;
          }
        } else {
          if (
            dataLista[valcodpro] &&
            dataLista[valcodpro][valcodalm] &&
            dataLista[valcodpro][valcodalm][vallote] &&
            dataLista[valcodpro][valcodalm][vallote]["cantF"] !== undefined
          ) {
            delete dataLista[valcodpro][valcodalm][vallote]["cantF"];
          }
        }
      });
  };
  $.fn.number_format = function (number, decimals, dec_point, thousands_sep) {
    // *     example: number_format(1234.56, 2, ',', ' ');
    // *     return: '1 234,56'
    number = (number + "").replace(",", "").replace(" ", "");
    var n = !isFinite(+number) ? 0 : +number,
      prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
      sep = typeof thousands_sep === "undefined" ? "," : thousands_sep,
      dec = typeof dec_point === "undefined" ? "." : dec_point,
      s = "",
      toFixedFix = function (n, prec) {
        var k = Math.pow(10, prec);
        return "" + Math.round(n * k) / k;
      };
    // Fix for IE parseFloat(0.55).toFixed(0) = 0;
    s = (prec ? toFixedFix(n, prec) : "" + Math.round(n)).split(".");
    if (s[0].length > 3) {
      s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
    }
    if ((s[1] || "").length < prec) {
      s[1] = s[1] || "";
      s[1] += new Array(prec - s[1].length + 1).join("0");
    }
    return s.join(dec);
  };
  $.fn.cambiartitulo = function () {
    let cadena = "Productos ";
    if (soloalm === "S") {
      cadena += "de los almacenes";
      $.getJSON("CRUDFaAlmacenes?opcion=2", function (data) {
        if (data.resultado === "ok") {
          //let repetido=false;
          $.each(data.data, function (key, value) {
            if (codalm.includes(value.codalm)) {
              //if(repetido){
              //    cadena+=",";
              //}
              cadena = cadena + " " + value.desalm + ",";
            }
          });
          if (distpor === "STKMIN") {
            cadena += " Distribucion por Stkmin";
          } else {
            cadena += " Distribucion por reposicion";
          }
          $("#titulo").text(cadena);
        } else {
          alert("Error: Problemas con el servidor.");
        }
      });
    } else {
      cadena += "de todos los almacenes,";
      if (distpor === "STKMIN") {
        cadena += " Distribucion por Stkmin";
      } else {
        cadena += " Distribucion por reposicion";
      }
      console.log(cadena);
      $("#titulo").text(cadena);
    }
  };
  $.fn.cambiartitulo();
  //EVENTOS
  if (!$("#checkboxFecha").is(":checked")) {
    $("#inputFecha").prop("disabled", true);
  }
  $("#checkboxFecha").change(function () {
    // Verificar si el checkbox está marcado
    if ($(this).is(":checked")) {
      // Si está marcado, habilitar el campo de fecha
      $("#inputFecha").prop("disabled", false);
    } else {
      // Si no está marcado, deshabilitar el campo de fecha
      $("#inputFecha").prop("disabled", true);
    }
  });
  $("#selectFiltro").on("change", function () {
    var selectedOption = $(this).val();
    // Ocultar todos los campos adicionales al principio
    $("#secuenciaInput").hide();
    $("#tipoProductoTable").hide();
    idsSeleccionados = [];
    // Mostrar los campos adicionales según la opción seleccionada
    if (selectedOption === "SECUENCIA") {
      $("#secuenciaInput").show();
    } else if (selectedOption === "TIPO") {
      let parametro = { opcion: 1 };
      $.getJSON("crudtiposproducto", parametro, function (resp) {
        $("#tipoProductoTable").show();
        var tabla = $("#tabletipos").DataTable();
        if (tabla) {
          tabla.destroy();
        }
        tabla = $("#tabletipos").DataTable({
          lengthChange: false,
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
              previous: "Anterior",
            },
            aria: {
              sortAscending: ": activate to sort column ascending",
              sortDescending: ": activate to sort column descending",
            },
          },
          data: resp.data,
          columns: [
            { data: "codtip" },
            { data: "destip" },
            {
              data: null,
              render: function (data, type, row) {
                return (
                  '<input type="checkbox" class="checkbox" value="' +
                  row.codtip +
                  '">'
                );
              },
            },
          ],
        });
      }).fail(function (jqXHR, textStatus, errorThrown) {
        console.error("Error en la solicitud AJAX:", textStatus, errorThrown);
      });
    }
  });
  $("#descargar").click(function () {
    console.log("cabecerar(0)");
    $.fn.descargar(0);
  });
  $("#descargarcabecera").click(function () {
    console.log("cabecerar(1)");
    $.fn.descargar(1);
  });
  $("#btnAceptar").click(function () {
    if (
      idsSeleccionados.length !== 0 ||
      $("#selectFiltro").val() === "SECUENCIA" ||
      $("#selectFiltro").val() === "TODOS"
    ) {
      let comp =
        $("#selectStockMinimo").val() !== "" &&
        $("#selectStockMinimoproyeccion").val() !== "";
      if (comp || tempbtn === "REPOS") {
        let comp =
          $("#selectStockMinimo2").val() !== "" &&
          $("#selectStockMinimo2proyeccion").val() !== "";
        if (comp || tempbtn === "STKMIN") {
          if (
            $("#inputSecuencia").val() !== "" ||
            $("#selectFiltro").val() !== "SECUENCIA"
          ) {
            if (
              estabSeleccionados.length !== 0 ||
              $("#selectalm").val() === "TODOS"
            ) {
              distpor = tempbtn;
              if (distpor === "STKMIN") {
                $("#stkmin2").text("Prom.Ven.");
                $("#texto0estab").text(
                  "Mostrar establecimientos con stock minimo cero"
                );
                //multiplicador = 1;
                dias = $("#selectStockMinimo").val();
                let dias2 = $("#selectStockMinimoproyeccion").val();
                multiplicador = dias2 / dias;
                //$("#divsolo0central").show();
                //$("#divsolo0estab").show();
              } else if (distpor === "REPOS") {
                $("#stkmin2").text("Cant.Proy.");
                $("#texto0estab").text(
                  "Mostrar establecimientos con ventas cero"
                );
                dias = $("#selectStockMinimo2").val();
                let dias2 = $("#selectStockMinimo2proyeccion").val();
                multiplicador = dias2 / dias;
                //$('#solo0').prop('checked', false);
                //$("#divsolo0central").hide();
                //$('#solo0estab').prop('checked', false);
                //$("#divsolo0estab").hide();
                //mostrar0central = 'N';
                //mostrar0estab = 'N';
              } else if (distpor === "PARETO") {
              }
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
              if ($("#checkboxFecha").prop("checked")) {
                indicaFecha = "S";
                fecha1 = $("#inputFecha").val();
              } else {
                indicaFecha = "N";
                fecha1 = "2024-01-01";
              }
              let tipo = $("#selectalm").val();
              codalm = JSON.stringify(estabSeleccionados);
              if (tipo === "ESTAB") {
                soloalm = "S";
              } else if (tipo === "TODOS") {
                soloalm = "N";
              }
              /*
                             if ($("#solorojoscentral").is(':checked')) {
                             mostrarrojoscentral = "S";
                             } else {
                             mostrarrojoscentral = "N";
                             }
                             
                             if ($("#solorojoscentral").is(':checked')) {
                             mostrarrojoscentral = "S";
                             } else {
                             mostrarrojoscentral = "N";
                             }
                             
                             if ($('#solo0').is(':checked')) {
                             mostrar0central = "S";
                             } else {
                             mostrar0central = "N";
                             }
                             */
              let tiporojo = $("#solorojosselect").val();
              if (tiporojo === "TODOS") {
                mostrar0central = "N";
                mostrarrojoscentral = "N";
              } else if (tiporojo === "rojo") {
                mostrar0central = "N";
                mostrarrojoscentral = "S";
              } else if (tiporojo === "cero") {
                mostrar0central = "S";
                mostrarrojoscentral = "N";
              }

              $.fn.cambiartitulo();
              $.fn.ListarTablaCentral();
              $("#modal-distribucion").modal("hide");
              listado = true;
            } else {
              if ($("#selectalm").val() === "ESTAB") {
                alert("Seleccione almenos un almacen");
              }
            }
          } else {
            alert("Debe ingresar la secuencia");
          }
        } else {
          alert("Debe ingresar correctamente los dias");
        }
      } else {
        alert("Debe ingresar correctamente los dias");
      }
    } else {
      if ($("#selectFiltro").val() === "TIPO") {
        alert("Seleccione almenos un tipo de producto");
      }
    }
  });
  $("#cerrarmodal").click(function () {
    $("#modaltablaproductos").css("display", "none");
    $("body").removeClass("modal-open");
    $(".modal-backdrop").remove();
  });
  $("#cerrarmodal2").click(function () {
    $("#modaltablaproductos").css("display", "none");
    $("body").removeClass("modal-open");
    $(".modal-backdrop").remove();
  });
  $("#salir").click(function () {
    $.getJSON("cerrarsesion", function (data) {});
    $(location).attr("href", "index.html");
  });
  $("#tabletipos").on("click", ".checkbox", function () {
    var id = $(this).val();
    // Desmarcar todas las casillas de verificación
    if ($(this).is(":checked")) {
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
  $("#tableestablecimientofiltro").on("click", ".checkbox", function () {
    var id = $(this).val();
    // Desmarcar todas las casillas de verificación
    if ($(this).is(":checked")) {
      idsSeleccionados.push(id);
    } else {
      var index = idsSeleccionados.indexOf(id);
      if (index !== -1) {
        idsSeleccionados.splice(index, 1);
      }
    }
  });
  $("#table-central").on("click", ".btn", function () {
    codpro = String($(this).data("codpro"));
    let despro = $(this).data("despro");
    $.fn.listarTablaDistribucion(codpro, despro);
    $("#footcantE").text(0);
    $("#footcantF").text(0);
  });
  $("#table-establecimientos").on("change", ".cantE", function () {
    // Obtener el valor de codpro del input actual
    const lote = String($(this).data("lote"));
    const ceresto = String($(this).data("ceresto"));
    const maxCE = parseInt(ceresto) || 0;
    const valorInput = $(this).val();

    if (
      isNaN(valorInput) ||
      parseFloat(valorInput) < 0 ||
      valorInput % 1 !== 0
    ) {
      alert("Por favor, ingrese un número válido.");
      $(this).val("");
      return;
    }

    let sumE = 0;

    $("#table-establecimientos")
      .DataTable()
      .rows()
      .every(function () {
        const rowData = this.data();
        if (String(rowData.lote) === lote) {
          const $inputE = $(this.node()).find(".cantE");

          // Verificar si el input existe y tiene valor
          if ($inputE.length) {
            const inputValue = $inputE.val();
            // Convertir a número (0 si no es válido)
            sumE +=
              isNaN(inputValue) || inputValue === ""
                ? 0
                : parseInt(inputValue, 10);
          }
        }
      });

    console.log({ sumE, maxCE });

    if (sumE > maxCE) {
      alert(
        `La cantidad total para este lote no puede exceder ${maxCE} unidades.`
      );
      $(this).val("");
      return;
    }
    // Iterar sobre todas las filas en la tabla
    $.fn.leerCantE();
    if ($.fn.calcularStock(codpro)) {
      delete dataLista[codpro][codalm][lote]["cantE"];
    }
    if (
      dataLista[codpro][codalm][lote]["cantE"] === undefined &&
      dataLista[codpro][codalm][lote]["cantF"] === undefined
    ) {
      delete dataLista[codpro][codalm][lote];
    }
    if (Object.keys(dataLista[codpro][codalm]).length === 0) {
      delete dataLista[codpro][codalm];
    }
    if (Object.keys(dataLista[codpro]).length === 0) {
      delete dataLista[codpro];
    }
    $.fn.actualizarbtn(codpro);
  });
  $("#table-establecimientos").on("change", ".cantF", function () {
    // Obtener el valor de codpro del input actual
    const lote = String($(this).data("lote"));
    const cfresto = String($(this).data("cfresto"));
    const maxCF = parseInt(cfresto) || 0;
    const valorInput = $(this).val();
    if (
      isNaN(valorInput) ||
      parseFloat(valorInput) < 0 ||
      valorInput % 1 !== 0
    ) {
      alert("Por favor, ingrese un número válido.");
      $(this).val("");
      return;
    }

    let sumF = 0;

    $("#table-establecimientos")
      .DataTable()
      .rows()
      .every(function () {
        const rowData = this.data();
        if (String(rowData.lote) === lote) {
          const $inputE = $(this.node()).find(".cantF");

          // Verificar si el input existe y tiene valor
          if ($inputE.length) {
            const inputValue = $inputE.val();
            // Convertir a número (0 si no es válido)
            sumF +=
              isNaN(inputValue) || inputValue === ""
                ? 0
                : parseInt(inputValue, 10);
          }
        }
      });

    if (sumF > maxCF) {
      alert(
        `La cantidad total para este lote no puede exceder ${maxCF} unidades.`
      );
      $(this).val("");
      return;
    }

    // Iterar sobre todas las filas en la tabla
    $.fn.leerCantE();
    if ($.fn.calcularStock(codpro)) {
      delete dataLista[codpro][codalm][lote]["cantF"];
    }
    if (
      dataLista[codpro][codalm][lote]["cantE"] === undefined &&
      dataLista[codpro][codalm][lote]["cantF"] === undefined
    ) {
      delete dataLista[codpro][codalm][lote];
    }
    if (Object.keys(dataLista[codpro][codalm]).length === 0) {
      delete dataLista[codpro][codalm];
    }
    if (Object.keys(dataLista[codpro]).length === 0) {
      delete dataLista[codpro];
    }
    $.fn.actualizarbtn(codpro);
  });
  $("#generarPDF").click(function () {
    fetch("EnviarPDF", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(datos),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Error al enviar los datos.");
        }
        return response.blob();
      })
      .then((blob) => {
        // Crear un objeto URL para el blob y descargar el archivo
        var url = window.URL.createObjectURL(blob);
        var a = document.createElement("a");
        a.href = url;
        a.download = "HelloWorld.pdf";
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  });
  $("#automatico").change(function () {
    // Verificar si el checkbox está marcado
    if ($(this).is(":checked")) {
      llenarautomaticamente = "S";
    } else {
      llenarautomaticamente = "N";
    }
  });
  $("#solo0estab").change(function () {
    // Verificar si el checkbox está marcado
    if ($(this).is(":checked")) {
      mostrar0estab = "S";
    } else {
      mostrar0estab = "N";
    }
    $.fn.listarTablaDistribucion(codpro);
  });
  $("#solorojos").change(function () {
    // Verificar si el checkbox está marcado
    if ($(this).is(":checked")) {
      mostrarrojos = "S";
      //$("#divsolo0estab").hide();
    } else {
      mostrarrojos = "N";
      //$("#divsolo0estab").show();
    }
    $.fn.listarTablaDistribucion(codpro);
  });
  $("#btnLimpiarDatos").click(function () {
    if (Object.keys(dataLista).length > 0) {
      dataLista = {};
      datosExtraproductos = {};
      $.fn.ListarTablaCentral();
    }
    $("#limpiarmodal").modal("hide");
  });
  $("#total").click(function () {
    if (myLineChart) {
      $.fn.graficar("TOTAL");
      $("#nombre-grafica").text(
        "Unidades vendidas de todos los establecimientos:"
      );
    }
  });
  $("#btnLimpiarDatosModal").click(function () {
    $("#limpiarmodal").modal("show");
  });
  $("#btnNuevaDistribucion").click(function () {
    // Mostrar el modal al cargar la página
    tempbtn = "REPOS";
    $(".repoimput").show();
    $(".stkminimput").hide();
    $(".paretoimput").hide();
    $("#modal-distribucion").modal("show");
  });
  $("#btnNuevaAlmacen").click(function () {
    tempbtn = "STKMIN";
    $(".stkminimput").show();
    $(".repoimput").hide();
    $(".paretoimput").hide();
    $("#modal-distribucion").modal("show");
  });
  $("#btnNuevaPareto").click(function () {
    // Mostrar el modal al cargar la página
    tempbtn = "PARETO";
    $(".repoimput").hide();
    $(".stkminimput").hide();
    $(".paretoimput").show();
    $("#modal-distribucion").modal("show");
  });
  $("#table-establecimientos").on("click", ".name-link", function (e) {
    if (listagraficas.TOTAL !== undefined) {
      $("#modal-grafica").modal("show");
      $("#nombre-grafica2").text("Unidades vendidas de: " + this.innerText);
      e.preventDefault();
      var codigo = String($(this).data("codalm"));
      $.fn.graficar(codigo);
    }
  });
  $("#selectalm").on("change", function () {
    var selectedOption = $(this).val();
    $("#establecimientofiltroTable").hide();
    // Mostrar los campos adicionales según la opción seleccionada
    estabSeleccionados = [];
    if (selectedOption === "ESTAB") {
      let parametro = { opcion: 1 };
      $.getJSON("CRUDFaAlmacenes", parametro, function (resp) {
        $("#establecimientofiltroTable").show();
        var tabla = $("#tableestablecimientofiltro").DataTable();
        if (tabla) {
          tabla.destroy();
        }
        let idsAEliminar = ["A1", "A2"];
        let data = resp.data.filter(
          (producto) => !idsAEliminar.includes(producto.codalm)
        );
        tabla = $("#tableestablecimientofiltro").DataTable({
          lengthChange: false,
          paging: false,
          searching: false,
          info: false,
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
              previous: "Anterior",
            },
            aria: {
              sortAscending: ": activate to sort column ascending",
              sortDescending: ": activate to sort column descending",
            },
          },
          data: data,
          columns: [
            { data: "sisent" },
            {
              data: null,
              render: function (data, type, row) {
                return (
                  '<input type="checkbox" class="checkbox" value="' +
                  row.codalm +
                  '">'
                );
              },
            },
          ],
        });
      }).fail(function (jqXHR, textStatus, errorThrown) {
        console.error("Error en la solicitud AJAX:", textStatus, errorThrown);
      });
    }
  });
  $("#btnAceptarestab").click(function () {
    if (estabSeleccionados.length !== 0 || $("#selectalm").val() === "TODOS") {
      let tipo = $("#selectalm").val();
      distpor = $("#dispor").val();
      if (distpor === "STKMIN") {
        $("#stkmin2").text("Prom.Ven.");
        //$("#divsolorojoscentral").show();
        //$("#divsolorojosestab").show();
      } else {
        $("#stkmin2").text("Cant.Ven.");
        //$('#solorojoscentral').prop('checked', false);
        //$("#divsolorojoscentral").hide();
        //$('#solorojos').prop('checked', false);
        //$("#divsolorojosestab").hide();
        //mostrarrojos = 'N';
        //let mostrarrojoscentral = 'N';
      }
      codalm = JSON.stringify(estabSeleccionados);
      if (tipo === "ESTAB") {
        soloalm = "S";
      } else if (tipo === "TODOS") {
        soloalm = "N";
      }
      if (listado) {
        $.fn.ListarTablaCentral();
      }

      $.fn.cambiartitulo();
    } else {
      if ($("#selectalm").val() === "ESTAB") {
        alert("Seleccione almenos un almacen");
      }
    }
  });
  $("#tableestablecimientofiltro").on("click", ".checkbox", function () {
    var id = $(this).val();
    // Desmarcar todas las casillas de verificación
    if ($(this).is(":checked")) {
      estabSeleccionados.push(id);
    } else {
      var index = estabSeleccionados.indexOf(id);
      if (index !== -1) {
        estabSeleccionados.splice(index, 1);
      }
    }
  });
  $("#selectStockMinimoproyeccion").on("change", function () {
    dias = $("#selectStockMinimo").val();
    let dias2 = $("#selectStockMinimoproyeccion").val();
    let indice = dias2 / dias;
    if (indice > 1) {
      alert("Ingrese un numero menor a dias a proyectar");
      $("#selectStockMinimoproyeccion").val(dias);
    }
  });
  $(document).keydown(function (event) {
    if (event.key === "Escape") {
      $("#modaltablaproductos").css("display", "none");
      $("body").removeClass("modal-open");
      $(".modal-backdrop").remove();
    }
  });
});
