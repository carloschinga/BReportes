$(document).ready(function () {
  let tabla;
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
  // Inicializar Select2
  $(".select2").select2({
    language: {
      noResults: function () {
        return "No se encontraron resultados.";
      },
    },
  });
  $(".select2").on("select2:open", function (e) {
    setTimeout(function () {
      // Busca el campo de búsqueda visible con la clase .select2-search__field
      let searchField = $(".select2-search__field:visible")[0];

      if (searchField) {
        searchField.focus(); // Hace focus en el campo de búsqueda visible
      }
    }, 100);
  });
  function listar() {
    var tabla = $("#tabladatos").DataTable();
    if (tabla) {
      tabla.destroy();
    }
    let codlab = $("#select-lab").val();
    let tipo = $("#select-tipo").val();
    let gen = $("#select-gen").val();
    let pet = $("#select-pet").val();
    let estr = $("#select-estr").val();
    if (codlab === undefined || codlab === null) {
      codlab = "";
    }
    if (tipo === undefined || tipo === null) {
      tipo = "";
    }
    if (gen === undefined || gen === null) {
      gen = "";
    }
    if (pet === undefined || pet === null) {
      pet = "";
    }
    if (estr === undefined || estr === null) {
      estr = "";
    }
    tabla = $("#tabladatos").DataTable({
      paging: false,
      searching: true,
      info: false,
      language: {
        decimal: "",
        emptyTable: "No hay datos disponibles",
        info: "Mostrando desde el _START_ al _END_ del total de _TOTAL_ registros",
        infoEmpty: "Mostrando desde el 0 al 0 del total de 0 registros",
        infoFiltered: "(Filtrados del total de _MAX_ registros)",
        thousands: ",",
        lengthMenu: "Mostrar _MENU_ registros por página",
        loadingRecords: "Cargando...",
        processing: "Procesando...",
        search: "Buscar:",
        zeroRecords: "No se encontraron coincidencias",
        paginate: {
          first: "Primero",
          last: "Última",
          next: "Siguiente",
          previous: "Anterior",
        },
        aria: {
          sortAscending:
            ": activar para ordenar la columna de forma ascendente",
          sortDescending:
            ": activar para ordenar la columna de forma descendente",
        },
      },
      ajax: {
        url: "CRUDProductos", // URL del servidor
        type: "GET", // Método de la solicitud
        data: function (d) {
          // Enviar los parámetros que necesitas con la solicitud
          d.opcion = 8;
          d.codlab = codlab;
          d.codgen = gen;
          d.codtip = tipo;
          d.estr = estr;
          d.pet = pet;
        },
        dataSrc: function (json) {
          if (json.resultado === "ok") {
            return json.data; // Retornar los datos que necesita el DataTable
          } else {
            if (json.mensaje === "nosession") {
              $.fn.validarSession();
            } else {
              alert("Error con el servidor al cargar los datos de la tabla.");
              return []; // Retorna un array vacío si no hay resultados
            }
          }
        },
      },
      columns: [
        {
          data: "codpro",
          render: function (data, type, row) {
            return `<input class="checkpredesa" style='width: 50px;' type="checkbox" ${
              row.predesac === "S" ? "checked" : ""
            } data-codpro='${data}'>`;
          },
          width: "70px",
        },
        {
          data: "pet",
          render: function (data, type, row) {
            if (type === "filter" || type === "sort") {
              return data === "S" ? "Sí" : "No"; // Para búsqueda y ordenamiento
            }
            return `<input type="checkbox" ${
              data === "S" ? "checked" : ""
            } readonly disabled>`;
          },
          width: "50px",
        },
        { data: "codpro", width: "50px" }, // Código del producto
        {
          data: "despro",
          render: function (data, type, row) {
            return data
              ? `<a class="link-grafica" data-codpro="${row.codpro}" data-despro="${data}">${data}</a>`
              : "";
          },
        },
        { data: "codlab" }, // Código de laboratorio
        {
          data: "stke",
          render: function (data, type, row) {
            return data ? data : "";
          },
          width: "50px",
        },
        {
          data: "stkf",
          render: function (data, type, row) {
            return data ? data : "";
          },
          width: "50px",
        },
        {
          data: "transitoE",
          render: function (data, type, row) {
            return data ? data : 0;
          },
          width: "50px",
        },
        {
          data: "transitoF",
          render: function (data, type, row) {
            return data ? data : 0;
          },
          width: "50px",
        },
        {
          data: "stkmin2",
          render: function (data, type, row) {
            return data ? data : "";
          },
          width: "50px",
        },
        {
          data: "meses",
          render: function (data, type, row) {
            return data ? parseFloat(data.toFixed(2)) : "";
          },
          width: "50px",
        },
        {
          data: null,
          render: function (data, type, row) {
            return "<input type='text'  style='width: 50px;'/>";
          },
          width: "50px",
        },
        {
          data: "cospro",
          render: function (data, type, row) {
            return data ? data : "";
          },
          width: "50px",
        },
        {
          data: "desgen",
          render: function (data, type, row) {
            return data ? data : "";
          },
          width: "70px",
        },
      ],
    });
  }

  $("#tabladatos thead th").tooltip({
    placement: "top",
    trigger: "hover",
  });

  $("#tabladatos").on("click", ".checkpredesa", function () {
    let codpro = String($(this).data("codpro"));
    $(".checkpredesa").prop("disabled", true);
    $.getJSON("CRUDProductos?opcion=11&codpro=" + codpro, function (data) {
      if (data.resultado === "ok") {
        $(".checkpredesa").prop("disabled", false);
        Toast.fire({
          icon: "success",
          title: "Modificado correctamente.",
        });
        tabla.ajax.draw();
      } else {
        Toast.fire({
          icon: "error",
          title: "Modificado al cambiar.",
        });
      }
    });
  });
  //carga de datos
  $.getJSON("restricciones?opcion=2", function (data) {
    if (data.resultado === "ok") {
      $.each(data.data, function (key, value) {
        $("#select-lab").append(
          $("<option></option>")
            .attr("value", value.codlab)
            .text(value.deslab + "(" + value.codlab + ")")
        );
      });

      $.getJSON("restricciones?opcion=4", function (data) {
        if (data.resultado === "ok") {
          $.each(data.data, function (key, value) {
            $("#select-gen").append(
              $("<option></option>")
                .attr("value", value.codgen)
                .text(value.desgen + "(" + value.codgen + ")")
            );
          });

          $.getJSON("crudtiposproducto?opcion=1", function (data) {
            $.each(data.data, function (key, value) {
              $("#select-tipo").append(
                $("<option></option>")
                  .attr("value", value.codtip)
                  .text(value.destip + "(" + value.codtip + ")")
              );
            });
          });

          //listar();
        } else if (data.mensaje === "nosession") {
          $.fn.validarSession();
        } else {
          alert("Error al cargar los genericos");
        }
      });
    } else if (data.mensaje === "nosession") {
      $.fn.validarSession();
    } else {
      alert("Error al cargar los laboratorios");
    }
  });

  $("#select-tipo").change(function () {});
  $("#select-lab").change(function () {});
  $("#select-gen").change(function () {});
  $("#select-pet").change(function () {});
  $("#select-estr").change(function () {});
  $("#filtrar").on("click", function () {
    listar();
  });

  let peticiongrafica;
  let peticiongrafica2;
  let myLineChart2;
  let myLineChart;

  $.fn.graficar = function (codalm) {
    if (myLineChart2) {
      myLineChart2.destroy();
    }
    console.log(listagraficas);
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

  $("#table-establecimientos thead th").tooltip({
    placement: "top",
    trigger: "hover",
  });
  $.fn.listarTablaDistribucion = function (codpro, despro) {
    $.fn.validarSession();

    if (codpro !== null) {
      var tabla = $("#table-establecimientos").DataTable();
      if (tabla) {
        tabla.destroy();
      }
      $("#nombre-grafica").text("CARGANDO...");
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
      $("#datosproducto").html("<b>" + despro + "</b>(<b>" + codpro + "</b>)");
      listagraficas = {};
      tabla = $("#table-establecimientos").DataTable({
        searching: false,
        paging: false,
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
          url: "CRUDFaStockAlmacenes?opcion=4",
          type: "POST",
          data: function (d) {
            d.codpro = codpro;
          },
          beforeSend: function () {
            $("#loadingproductos").css("display", "block");
          },
          complete: function () {
            $("#loadingproductos").css("display", "none");
          },
        },
        columns: [
          {
            data: "desalm",
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
          {
            data: "stkalm",
            render: function (data, type, row) {
              return data ? data : "";
            },
            width: "50px",
          },
          {
            data: "stkalm_m",
            render: function (data, type, row) {
              return data ? data : "";
            },
            width: "50px",
          },
          {
            data: "meses",
            render: function (data, type, row) {
              return data ? data.toFixed(2) : "";
            },
            width: "50px",
          },
          {
            data: "stkmin2",
            render: function (data, type, row) {
              return data ? data.toFixed(2) : "";
            },
            width: "50px",
          },
        ],
        initComplete: function (settings, json) {
          peticiongrafica = $.getJSON(
            "distribucion?opcion=4",
            { codpro: codpro },
            function (data) {
              $("#tab_stkmin2").text(
                data.stkmin2 ? data.stkmin2.toFixed(2) : ""
              );
              $("#ta_stkalm").text(data.stkalm ? data.stkalm.toFixed(2) : "");
              $("#ta_repone").text(data.meses ? data.meses.toFixed(2) : "");
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

              var tablacompra = $("#table-indicadorcompra").DataTable();
              if (tablacompra) {
                tablacompra.destroy();
              }
              $("#table-indicadorcompra").DataTable({
                paging: false,
                searching: false,
                info: false,
                language: {
                  decimal: "",
                  emptyTable: "No hay datos disponibles",
                  info: "Mostrando desde el _START_ al _END_ del total de _TOTAL_ registros",
                  infoEmpty:
                    "Mostrando desde el 0 al 0 del total de 0 registros",
                  infoFiltered: "(Filtrados del total de _MAX_ registros)",
                  thousands: ",",
                  lengthMenu: "Mostrar _MENU_ registros por página",
                  loadingRecords: "Cargando...",
                  processing: "Procesando...",
                  search: "Buscar:",
                  zeroRecords: "No se encontraron coincidencias",
                  paginate: {
                    first: "Primero",
                    last: "Última",
                    next: "Siguiente",
                    previous: "Anterior",
                  },
                  aria: {
                    sortAscending:
                      ": activar para ordenar la columna de forma ascendente",
                    sortDescending:
                      ": activar para ordenar la columna de forma descendente",
                  },
                },
                ajax: {
                  url: "CRUDProductos", // URL del servidor
                  type: "GET", // Método de la solicitud
                  data: function (d) {
                    // Enviar los parámetros que necesitas con la solicitud
                    d.opcion = 12;
                    d.codpro = codpro;
                  },
                  dataSrc: function (json) {
                    if (json.resultado === "ok") {
                      return json.data; // Retornar los datos que necesita el DataTable
                    } else {
                      if (json.mensaje === "nosession") {
                        $.fn.validarSession();
                      } else {
                        alert(
                          "Error con el servidor al cargar los datos de la tabla."
                        );
                        return []; // Retorna un array vacío si no hay resultados
                      }
                    }
                  },
                },
                columns: [
                  { data: "descrip" },
                  { data: "desprv" },
                  { data: "costo" },
                  { data: "cant", width: "50px" },
                  { data: "feccre" },
                  { data: "doc" },
                ],
                initComplete: function (settings, json) {
                  var ultimacompra = $("#table-ultimacompra").DataTable();
                  if (ultimacompra) {
                    ultimacompra.destroy();
                  }
                  $("#table-ultimacompra").DataTable({
                    paging: false,
                    searching: false,
                    info: false,
                    language: {
                      decimal: "",
                      emptyTable: "No hay datos disponibles",
                      info: "Mostrando desde el _START_ al _END_ del total de _TOTAL_ registros",
                      infoEmpty:
                        "Mostrando desde el 0 al 0 del total de 0 registros",
                      infoFiltered: "(Filtrados del total de _MAX_ registros)",
                      thousands: ",",
                      lengthMenu: "Mostrar _MENU_ registros por página",
                      loadingRecords: "Cargando...",
                      processing: "Procesando...",
                      search: "Buscar:",
                      zeroRecords: "No se encontraron coincidencias",
                      paginate: {
                        first: "Primero",
                        last: "Última",
                        next: "Siguiente",
                        previous: "Anterior",
                      },
                      aria: {
                        sortAscending:
                          ": activar para ordenar la columna de forma ascendente",
                        sortDescending:
                          ": activar para ordenar la columna de forma descendente",
                      },
                    },
                    ajax: {
                      url: "CRUDProductos", // URL del servidor
                      type: "GET", // Método de la solicitud
                      data: function (d) {
                        // Enviar los parámetros que necesitas con la solicitud
                        d.opcion = 13;
                        d.codpro = codpro;
                      },
                      dataSrc: function (json) {
                        if (json.resultado === "ok") {
                          return json.data; // Retornar los datos que necesita el DataTable
                        } else {
                          if (json.mensaje === "nosession") {
                            $.fn.validarSession();
                          } else {
                            alert(
                              "Error con el servidor al cargar los datos de la tabla."
                            );
                            return []; // Retorna un array vacío si no hay resultados
                          }
                        }
                      },
                    },
                    columns: [
                      { data: "desprv" },
                      { data: "cante" },
                      { data: "feccre" },
                      { data: "precio" },
                    ],
                    initComplete: function (settings, json) {
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
                              listagraficas[key].ventas = listagraficas[
                                key
                              ].ventas.map((value) => Math.round(value));
                            }
                          }
                        }
                      );
                    },
                  });
                },
              });
            }
          );
        },
      });
    }
  };

  $("#table-establecimientos").on("click", ".name-link", function (e) {
    if (listagraficas.TOTAL !== undefined) {
      $("#modal-grafica").modal("show");
      $("#nombre-grafica2").text("Unidades vendidas de: " + this.innerText);
      e.preventDefault();
      var codigo = String($(this).data("codalm"));
      $.fn.graficar(codigo);
    }
  });
  $("#tabladatos").on("click", ".link-grafica", function () {
    codpro = String($(this).data("codpro"));
    let despro = $(this).data("despro");
    $.fn.listarTablaDistribucion(codpro, despro);
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
});
