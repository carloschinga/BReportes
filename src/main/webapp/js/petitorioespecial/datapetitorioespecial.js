$(document).ready(function () {
  let tabla;
  let myLineChart;
  let myLineChart2;
  let productos;
  let peticiongrafica;
  let productosSeleccionados = [];
  let peticiongrafica2;
  let codproArray = [];
  let modalProductTable;
  let modalOrigin = null;

  const Swal = window.Swal;
  const Toast = Swal.mixin({
    toast: true,
    position: "top-end",
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: true,
    didOpen: (toast) => {
      toast.addEventListener("mouseenter", Swal.stopTimer);
      toast.addEventListener("mouseleave", Swal.resumeTimer);
    },
  });

  $("#btnMasReportes").click(function () {
    console.log("Boton de mas reportes");
    $("#contenido").load("reportepetitorio.html");
  });

  // Tooltips para encabezados
  $("#table-indicadorcompra thead th").tooltip({
    placement: "top",
    trigger: "hover",
  });

  function limpiarFiltros() {
    // Para el select de laboratorio
    $("#filter-codlab").val(null).trigger("change");

    // Para el select de genérico
    $("#filter-codgen").val(null).trigger("change");

    // También puedes limpiar los filtros de la tabla
    modalProductTable.column(2).search("").draw();
    modalProductTable.column(3).search("").draw();
  }

  $.fn.listarTablaDistribucion = function (codpro, despro, origin) {
    $.fn.validarSession();

    modalOrigin = origin || null;

    if (codpro !== null) {
      if (modalOrigin === "product-modal") {
        $("#product-modal").modal("hide");
      }

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
              console.log("Data para la grafica");
              console.log({ data });
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
                data.ventas12 = data.ventas12.map(
                  (value) => Math.round(value * 10) / 10
                );
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

  $("#modal-product-table").on("click", ".link-grafica", function (e) {
    e.preventDefault();
    var codpro = $(this).data("codpro");
    var despro = $(this).data("despro");
    $.fn.listarTablaDistribucion(codpro, despro, "product-modal");
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

  // Manejar el cambio en el select de servicios
  function listarEspecialidades() {
    $.ajax({
      url: "CRUDMedico?opcion=4",
      method: "GET",
      dataType: "json",
      success: function (response) {
        if (response.resultado === "ok") {
          $("#select-service").append(
            '<option value="">Seleccione una especialidad</option>'
          );
          $.each(response.data, function (index, service) {
            $("#select-service").append(
              new Option(service.espdes, service.espcod)
            );
          });
          $("#show-modal").prop("disabled", false);
          $("#btnPrintPDF").prop("disabled", false);
          $("#btnPrintExcel").prop("disabled", false);
          $("#btnMasReportes").prop("disabled", false);
        } else if (response.mensaje === "nosession") {
          $.fn.validarSession();
        } else {
          alert("Error al cargar los médicos");
        }
      },
      error: function () {
        alert("Error al cargar los médicos");
      },
    });
  }

  listarEspecialidades();

  function cargarMedicosPorServicio(servicioId) {
    console.log("Cargando médicos para servicio:", servicioId);

    const $medicosContainer = $("#modal-medicos-body");

    if (!servicioId) {
      $medicosContainer.html(
        '<div class="alert alert-info">Seleccione un servicio primero</div>'
      );
      return;
    }

    $medicosContainer.html(
      '<div class="text-center"><i class="fas fa-spinner fa-spin"></i> Cargando médicos...</div>'
    );

    $.ajax({
      url: "CRUDMedico?opcion=3",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify({ servicios: [servicioId] }),
      dataType: "json",
      success: function (response) {
        console.log("Respuesta completa del servidor:", response);

        try {
          // Asegurarnos de que response sea un objeto
          const responseData =
            typeof response === "string" ? JSON.parse(response) : response;
          console.log("Datos procesados:", responseData);

          if (responseData.resultado === "ok" && responseData.data) {
            const medicos = responseData.data;
            console.log("Médicos recibidos:", medicos);

            if (medicos && medicos.length > 0) {
              let html = `
                            <div class="medicos-list" style="max-height: 200px; overflow-y: auto;">
                                <div class="custom-control custom-checkbox mb-2">
                                    <input type="checkbox" class="custom-control-input" id="todos-medicos">
                                    <label class="custom-control-label" for="todos-medicos">Todos los médicos</label>
                                </div>
                                <div class="medicos-items">`;

              medicos.forEach((medico) => {
                html += `
                                <div class="custom-control custom-checkbox mb-2 medico-item">
                                    <input type="checkbox" class="custom-control-input medico-checkbox" 
                                           id="medico-${medico.medcod}" value="${medico.medcod}">
                                    <label class="custom-control-label" for="medico-${medico.medcod}">
                                        ${medico.mednam}
                                    </label>
                                </div>`;
              });

              html += `
                                </div>
                            </div>`;

              $medicosContainer.html(html); // NO mostrar aquí, solo actualizar el contenido

              // Inicializar búsqueda
              $("#medicos-search").on("keyup", function () {
                const searchText = $(this).val().toLowerCase();
                $(".medico-item").each(function () {
                  const medicoName = $(this).find("label").text().toLowerCase();
                  $(this).toggle(medicoName.includes(searchText));
                });
              });

              // Manejar selección de "Todos"
              $("#todos-medicos").on("change", function () {
                const isChecked = $(this).is(":checked");
                $(".medico-checkbox").prop("checked", isChecked);
                if (isChecked) {
                  $(".medico-item").hide();
                } else {
                  $(".medico-item").show();
                }
              });

              // Manejar selección individual
              $(".medico-checkbox").on("change", function () {
                if ($(this).is(":checked")) {
                  $("#todos-medicos").prop("checked", false);
                }
              });
            } else {
              console.warn("No se encontraron médicos en la respuesta");
              $medicosContainer.html(
                '<div class="alert alert-warning">No hay médicos disponibles</div>'
              );
            }
          } else {
            console.warn("Respuesta no válida:", responseData);
            $medicosContainer.html(
              '<div class="alert alert-warning">No hay médicos disponibles</div>'
            );
          }
        } catch (error) {
          console.error("Error al procesar la respuesta:", error);
          $medicosContainer.html(
            '<div class="alert alert-danger">Error al procesar los datos</div>'
          );
        }
      },
      error: function (xhr, status, error) {
        console.error("Error al cargar médicos:", error);
        $medicosContainer.html(
          '<div class="alert alert-danger">Error al cargar médicos</div>'
        );
      },
    });
  }

  // Modificar el manejador del select de servicio
  $("#select-service").on("change", function () {
    const sercod = $(this).val();
    console.log("Servicio seleccionado:", sercod);

    // Limpiar selección previa
    $("#filter-medicos-container").empty();

    if (sercod) {
      cargarMedicosPorServicio(sercod);
      $.fn.ListarProductos();
    } else {
      $("#filter-medicos-container").html(
        '<div class="alert alert-info">Seleccione un servicio primero</div>'
      );
    }
  });

  function getMedicosSeleccionados() {
    const medicosSeleccionados = [];
    if ($("#todos-medicos").is(":checked")) {
      return null; // Retorna null si "Todos" está seleccionado
    }
    $(".medico-checkbox:checked").each(function () {
      medicosSeleccionados.push($(this).val());
    });
    return medicosSeleccionados;
  }

  // Modificar el botón de agregar para usar la nueva función
  $("#btn-agregar").on("click", function () {
    const medicosSeleccionados = getMedicosSeleccionados();

    if (medicosSeleccionados !== null && medicosSeleccionados.length === 0) {
      alert("Debe seleccionar al menos un médico o marcar 'Todos'");
      return;
    }

    if (productosSeleccionados.length === 0) {
      alert("Debe seleccionar al menos un producto");
      return;
    }

    // Construir objeto de datos según la selección
    const requestData = {
      productos: productosSeleccionados,
    };

    // Solo añadir médicos al objeto si no se seleccionó "Todos"
    if (medicosSeleccionados !== null) {
      requestData.medicos = medicosSeleccionados;
    } else {
      console.log("Utilizando todos los médicos disponibles");
    }

    // Aquí podrías hacer la llamada AJAX en vez de solo mostrar en consola
    console.log({ requestData });

    $.ajax({
      url: "CRUDPetitorio?opcion=5&sercod=" + $("#select-service").val(),
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(requestData),
      dataType: "json",
      success: function (data) {
        if (data.resultado === "ok") {
          productosSeleccionados = [];
          let tablaDatos = $("#tabladatos").DataTable();
          if (tablaDatos) tablaDatos.ajax.reload();
          $("#product-modal").modal("hide");
          limpiarFiltros();
        } else if (data.mensaje === "nosession") {
          $.fn.validarSession();
        } else {
          alert("Error al grabar.");
        }
      },
      error: function () {
        alert("Error de conexión con el servidor");
      },
    });
  });

  // Recuperar el JSON del servidor
  $.getJSON("CRUDProductos?opcion=7", function (data) {
    productos = data.data;
    modalProductTable = $("#modal-product-table").DataTable({
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
      data: productos,
      columns: [
        { data: "codpro" },
        {
          data: "despro",
          render: function (data, type, row) {
            return data
              ? `<a class="link-grafica" data-codpro="${row.codpro}" data-despro="${data}">${data}</a>`
              : "";
          },
        },
        {
          data: "codlab",
          render: function (data, type, row) {
            return data ? data : "";
          },
        },
        {
          data: "desgen",
          render: function (data, type, row) {
            return data ? data : "";
          },
        },
        { data: "estra" },
        { data: "precio" },
        { data: "costo_con_igv" },
        { data: "utilidad" },
        { data: "porcentaje_utilidad" },
        {
          data: "codpro",
          orderable: false,
          render: function (data, type, row) {
            // Verifica si el codpro actual está en el codproArray
            if (codproArray.includes(String(data))) {
              // Si está en el array, no mostrar el checkbox
              return "Agregado.";
            } else {
              // Si no está en el array, mostrar el checkbox
              return (
                '<input type="checkbox" class="chk-product" data-codpro="' +
                data +
                '">'
              );
            }
          },
        },
      ],
      paging: true, // Activar paginación si es necesario
      searching: true, // Activar búsqueda en el modal
      info: false,
      pageLength: 5,
      lengthChange: false,
      order: [[1, "asc"]],
      dom: '<"d-flex flex-wrap justify-content-between align-items-center mb-4"<"#filter-container">f>rt<"d-flex justify-content-between align-items-center mt-3"<"col-md-6"l><"col-md-6"p>>',
    });
    $("#filter-container").html(`
    <div class="d-flex flex-column flex-md-row align-items-stretch align-items-md-center gap-2 w-100">
        <div class="flex-grow-1" style="min-width: 180px;">
            <select id="filter-codlab" class="form-select select2">
                <option value="">Todos Laboratorios</option>
            </select>
        </div>
        <div class="flex-grow-1" style="min-width: 180px;">
            <select id="filter-codgen" class="form-select select2">
                <option value="">Todos Genéricos</option>
            </select>
        </div>
        <div class="flex-shrink-0" style="min-width: 200px;">
            <button type="button" class="btn btn-outline-primary w-100 py-2 d-flex align-items-center justify-content-center gap-2" id="btn-medicos-modal">
                <i class="fas fa-user-md"></i>
                <span>Seleccionar médicos</span>
            </button>
        </div>
    </div>
`);

    $("#btn-medicos-modal").on("click", function (e) {
      e.preventDefault();
      $("#modal-medicos").modal("show");
    });
    // Cargar opciones dinámicamente en los selectores
    $.getJSON("restricciones?opcion=2", function (data) {
      if (data.resultado === "ok") {
        $.each(data.data, function (key, value) {
          $("#filter-codlab").append(
            $("<option></option>")
              .attr("value", value.codlab)
              .text(value.deslab + "(" + value.codlab + ")")
          );
        });
        $("#filter-codlab").select2({
          dropdownParent: $("#product-modal"),
          language: {
            noResults: function () {
              return "No se encontraron resultados.";
            },
          },
        });
        $("#filter-codlab").on("select2:open", function (e) {
          setTimeout(function () {
            // Busca el campo de búsqueda visible con la clase .select2-search__field
            let searchField = $(".select2-search__field:visible")[0];

            if (searchField) {
              searchField.focus(); // Hace focus en el campo de búsqueda visible
            }
          }, 100);
        });
      } else if (data.mensaje === "nosession") {
        $.fn.validarSession();
      } else {
        alert("Error al cargar los médicos");
      }
    });

    $.getJSON("restricciones?opcion=4", function (data) {
      if (data.resultado === "ok") {
        $.each(data.data, function (key, value) {
          $("#filter-codgen").append(
            $("<option></option>")
              .attr("value", value.desgen)
              .text(value.desgen + "(" + value.codgen + ")")
          );
        });
        $("#filter-codgen").select2({
          dropdownParent: $("#product-modal"),
          language: {
            noResults: function () {
              return "No se encontraron resultados.";
            },
          },
        });
        $("#filter-codgen").on("select2:open", function (e) {
          setTimeout(function () {
            // Busca el campo de búsqueda visible con la clase .select2-search__field
            let searchField = $(".select2-search__field:visible")[0];

            if (searchField) {
              searchField.focus(); // Hace focus en el campo de búsqueda visible
            }
          }, 100);
        });
      } else if (data.mensaje === "nosession") {
        $.fn.validarSession();
      } else {
        alert("Error al cargar los médicos");
      }
    });

    // Función para filtrar por codlab
    $("#filter-codlab").on("change", function () {
      var selectedTipo = $(this).val();
      console.log("Filtrando por lab:", selectedTipo);

      if (selectedTipo) {
        modalProductTable.column(2).search(selectedTipo).draw();
      } else {
        modalProductTable.column(2).search("").draw();
      }
    });

    // Función para filtrar por codgen
    $("#filter-codgen").on("change", function () {
      var selectedTipo = $(this).val();
      console.log("Filtrando por gen:", selectedTipo);

      if (selectedTipo) {
        modalProductTable.column(3).search(selectedTipo).draw();
      } else {
        modalProductTable.column(3).search("").draw();
      }
    });

    inicializarSelectMedicos();
  });

  function inicializarSelectMedicos() {
    $("#filter-medicos").select2({
      placeholder: "Seleccione médico(s)",
      allowClear: true,
      multiple: true,
      dropdownParent: $("#product-modal"),
      width: "100%",
      language: {
        noResults: function () {
          return "No se encontraron médicos";
        },
      },
    });
  }

  $("#modal-product-table").on("change", ".chk-product", function () {
    var codpro = String($(this).data("codpro")); // Obtener el código del producto
    var isChecked = $(this).is(":checked"); // Saber si está marcado o no
    if (isChecked) {
      // Si el checkbox está marcado, agregamos el producto a la lista
      productosSeleccionados.push(codpro);
    } else {
      // Si el checkbox está desmarcado, removemos el producto de la lista
      productosSeleccionados = productosSeleccionados.filter(function (item) {
        return item !== codpro;
      });
    }
    console.log(productosSeleccionados);
  });
  $("#show-modal").on("click", function () {
    if (
      $("#select-service").val() !== "" &&
      $("#select-service").val() !== undefined &&
      $("#select-service").val() !== null
    ) {
      $("#product-modal").modal("show");
    } else {
      alert("Eliga una especialidad.");
    }
  });

  // Cerrar el modal
  $(".close").on("click", function () {
    $("#product-modal").modal("hide");
    limpiarFiltros();
  });

  $(".btn-cancel").on("click", function () {
    $("#product-modal").modal("hide");
    limpiarFiltros();
  });

  $.fn.ListarProductos = function () {
    $.fn.validarSession();
    if (tabla) {
      $("#tabladatos").empty();
      tabla.destroy();
    }
    $("#tabladatos").html(
      "<thead><tr><th>Mas</th><th>Pre</th><th>CodPro</th><th>Descripción</th><th>Genérico</th><th>Principio Activo</th><th>Lab.</th><th>Estrategia</th><th>Stock</th><th>Acciones</th></tr></thead><tbody></tbody>"
    );
    $("#loadingproductos").css("display", "block");

    function formatMedicos(medicos, codpro) {
      // Validación de datos vacíos
      if (!medicos || medicos.length === 0) {
        return '<div class="text-center p-2">No hay médicos asignados</div>';
      }

      // Ordenar médicos alfabéticamente por nombre
      const medicosOrdenados = [...medicos].sort((a, b) => {
        const nombreA = (a.nombre || a.mednam || "").toLowerCase();
        const nombreB = (b.nombre || b.mednam || "").toLowerCase();
        return nombreA.localeCompare(nombreB);
      });

      // Construir la tabla HTML
      let html = `
          <div class="medicos-container p-2">
              <table class="table table-sm table-bordered">
                  <thead>
                      <tr>
                          <th>Código</th>
                          <th>Médico</th>
                          <th>Acciones</th>
                      </tr>
                  </thead>
                  <tbody>`;

      // Filas de médicos
      medicosOrdenados.forEach((medico) => {
        const codigoMedico = medico.codigo || medico.medcod || "";
        const nombreMedico = medico.nombre || medico.mednam || "";

        html += `
              <tr>
                  <td>${codigoMedico}</td>
                  <td>${nombreMedico}</td>
                  <td>
                      <button class="btn btn-sm btn-danger eliminar-medico" 
                          data-codpro="${codpro || ""}" 
                          data-medcod="${codigoMedico}">
                          <i class="fa fa-trash"></i> 
                      </button>
                  </td>
              </tr>`;
      });

      // Cierre de la tabla
      html += `
                  </tbody>
              </table>
          </div>`;

      return html;
    }

    tabla = $("#tabladatos").DataTable({
      paging: false, // Activar paginación si es necesario
      searching: true, // Activar búsqueda en el modal
      info: false,
      scrollY: "800px", // Altura fija para el scroll
      scrollCollapse: true,
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
        url: "CRUDPetitorio?opcion=6",
        type: "POST",
        data: function (d) {
          d.sercod = $("#select-service").val();
        },
        dataSrc: function (json) {
          // Actualizar codproArray cada vez que se reciban nuevos datos
          codproArray = json.data.map(function (row) {
            return String(row.codpro);
          });

          // Si no hay datos, mostrar mensaje claro
          if (json.data.length === 0) {
            return [];
          }

          // Devolver los datos a DataTables
          return json.data.map((item) => {
            item.expanded = false;
            return item;
          });
        },
      },
      columns: [
        {
          className: "details-control",
          orderable: false,
          data: null,
          defaultContent: '<i class="fas fa-plus-circle"></i>',
          width: "20px",
        },
        {
          data: "predesac",
          render: function (data, type, row) {
            // Asegurarse de devolver 'true' o 'false' como strings
            const value = data ? "true" : "false";
            return `<input type="checkbox" ${data ? "checked" : ""} 
                        disabled="disabled" data-value="${value}">`;
          },
        },
        { data: "codpro" },
        {
          data: "despro",
          render: function (data, type, row) {
            return data
              ? `<a class="link-grafica" data-codpro="${row.codpro}" data-despro="${data}">${data}</a>`
              : "";
          },
        },
        { data: "codgen" },
        { data: "desgen" },
        { data: "codlab" },
        { data: "categvta" },
        { data: "stock_total" },
        {
          data: null,
          render: function (data, type, row) {
            // Convertir el array de médicos a JSON seguro para atributo HTML
            const medicosJson = row.medicos
              ? encodeURIComponent(
                  JSON.stringify(row.medicos.map((m) => m.medcod || m.codigo))
                )
              : "[]";
            return (
              '<button class="btn btn-info grafico" data-codpro="' +
              row.codpro +
              '" data-despro="' +
              $("<div>").text(row.despro).html() +
              '"><i class="fas fa-chart-line"></i></button> ' +
              '<button class="btn btn-danger eliminar-muchos" data-codpro="' +
              row.codpro +
              '" data-medicos="' +
              medicosJson +
              '"><i class="fa fa-trash"></i> </button>'
            );
          },
        },
      ],
      drawCallback: function () {
        actualizarTablamodal();

        // Evento para expandir/contraer filas
        $("#tabladatos tbody").off("click", "td.details-control");
        $("#tabladatos tbody").on("click", "td.details-control", function () {
          var tr = $(this).closest("tr");
          var row = tabla.row(tr);
          var rowData = row.data();

          if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass("shown");
            $(this).html('<i class="fas fa-plus-circle"></i>');
            rowData.expanded = false;
          } else {
            // Mostrar spinner mientras carga
            row
              .child(
                '<div class="text-center p-3"><i class="fas fa-spinner fa-spin"></i> Cargando médicos...</div>'
              )
              .show();
            tr.addClass("shown");
            $(this).html('<i class="fas fa-minus-circle"></i>');

            // Simular carga asíncrona (en tu caso sería una llamada real al servidor)
            setTimeout(() => {
              // Aquí deberías hacer una llamada AJAX para obtener los médicos del producto
              // Por ahora usamos los datos que ya vienen en la fila (si los tienes)
              let medicos = rowData.medicos || [];
              row.child(formatMedicos(medicos, rowData.codpro)).show();
            }, 500);

            rowData.expanded = true;
          }
        });
      },
      initComplete: function () {
        // Configuración para columnas normales (código y genérico)
        this.api()
          .columns([2, 3, 4, 5, 6, 7])
          .every(function () {
            let column = this;
            let header = $(column.header());
            let title = header.text().trim();

            let container = $("<div>").css({
              display: "flex",
              "flex-direction": "column",
            });

            let titleElement = $("<span>").text(title);
            let select = $("<select>")
              .addClass("form-control form-control-sm")
              .append($("<option>").val("").text("Todos"));

            container.append(titleElement).append(select);
            header.html(container);

            select.on("change", function () {
              column.search($(this).val(), { exact: true }).draw();
            });

            select.on("click", function (e) {
              e.stopPropagation();
            });

            column
              .data()
              .unique()
              .sort()
              .each(function (d, j) {
                select.append($("<option>").val(d).text(d));
              });
          });
      },
      order: [[1, "asc"]],
    });
    $("#loadingproductos").css("display", "none");
  };

  $(document).on("click", ".product-detail-link", function (e) {
    e.preventDefault();
    e.stopPropagation(); // Importante para DataTables
    const rowData = JSON.parse($(this).attr("data-row"));
    $("#product-detail-modal").modal("show");

    // Llena el modal aquí mismo
    fillModalContent(rowData);
  });

  $(document).on("click", ".eliminar-muchos", function (e) {
    e.preventDefault();
    e.stopPropagation(); // Importante para DataTables
    const codpro = $(this).data("codpro");
    const medicosJson = JSON.parse(decodeURIComponent($(this).data("medicos")));

    Swal.fire({
      title: "¿Eliminar item?",
      html: `¿Estás seguro de eliminar todos los médicos de este item?<b>${codpro}</b>`,
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      cancelButtonColor: "#3085d6",
      confirmButtonText: "Sí, eliminar",
      cancelButtonText: "Cancelar",
    }).then((result) => {
      if (result.isConfirmed) {
        $.ajax({
          url: `CRUDPetitorio?opcion=7&codpro=${codpro}&medcods=${encodeURIComponent(
            JSON.stringify(medicosJson)
          )}`,
          type: "GET",
          dataType: "json",
          success: function (response) {
            if (response.resultado !== "ok") {
              showError(
                "Error",
                response.error || "No se pudo eliminar el item"
              );
              return;
            }
            tabla.ajax.reload(null, false);
            showSuccess("Item eliminado correctamente");
          },
          error: function (xhr) {
            let errorMsg = "Error al eliminar el item";
            try {
              const response = JSON.parse(xhr.responseText);
              errorMsg = response.error || errorMsg;
            } catch (e) {
              console.error("Error parsing response:", e);
            }
            showError("Error al eliminar", errorMsg);
          },
        });
      }
    });
  });

  $("#tabladatos").on("click", ".link-grafica", function () {
    codpro = String($(this).data("codpro"));
    let despro = $(this).data("despro");
    $.fn.listarTablaDistribucion(codpro, despro, "other-modal");
  });

  function fillModalContent(rowData) {
    // Limpiar el modal primero
    const $modalBody = $("#product-detail-modal .modal-body");
    $modalBody.empty();

    // Actualizar el título
    $("#product-detail-modal .modal-title").text(`Detalle: ${rowData.despro}`);

    // Crear contenido
    const modalContent = `
      <div class="container-fluid">
        <div class="row">
          <div class="col-md-6">
            <h6>Información Básica</h6>
            <table class="table table-sm">
              <tbody>
                <tr><th>Código</th><td>${rowData.codpro}</td></tr>
                <tr><th>Descripción</th><td>${rowData.despro}</td></tr>
                <tr><th>Laboratorio</th><td>${rowData.codlab || "N/A"}</td></tr>
              </tbody>
            </table>
          </div>
          <div class="col-md-6">
            <h6>Existencias</h6>
            <table class="table table-sm">
              <tbody>
                <tr><th>Stock</th><td>${rowData.stock || "0"}</td></tr>
                <tr><th>Estrac</th><td>${rowData.estra || "N/A"}</td></tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    `;

    $modalBody.html(modalContent);
  }

  function actualizarTablamodal() {
    // Guarda los filtros actuales
    var currentLabFilter = $("#filter-codlab").val();
    var currentGenFilter = $("#filter-codgen").val();

    // Recarga los datos
    modalProductTable.clear().rows.add(productos).draw();

    // Reaplica los filtros si existían
    if (currentLabFilter) {
      modalProductTable.column(2).search(currentLabFilter).draw();
    }
    if (currentGenFilter) {
      modalProductTable.column(3).search(currentGenFilter).draw();
    }
  }
  $("#tabladatos").on("click", ".eliminar-medico", function () {
    const medcod = $(this).data("medcod");
    const codpro = $(this).data("codpro");

    Swal.fire({
      title: "¿Eliminar item?",
      html: `¿Estás seguro de eliminar el medico?<b>`,
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      cancelButtonColor: "#3085d6",
      confirmButtonText: "Sí, eliminar",
      cancelButtonText: "Cancelar",
    }).then((result) => {
      if (result.isConfirmed) {
        $.ajax({
          url: `CRUDPetitorio?opcion=3&codpro=${codpro}&medcod=${medcod}`,
          type: "GET",
          dataType: "json",
          success: function (response) {
            if (response.resultado !== "ok") {
              showError(
                "Error",
                response.error || "No se pudo eliminar el item"
              );
              return;
            }
            tabla.ajax.reload(null, false);
            showSuccess("Item eliminado correctamente");
          },
          error: function (xhr) {
            let errorMsg = "Error al eliminar el item";
            try {
              const response = JSON.parse(xhr.responseText);
              errorMsg = response.error || errorMsg;
            } catch (e) {
              console.error("Error parsing response:", e);
            }
            showError("Error al eliminar", errorMsg);
          },
        });
      }
    });
  });

  // Función para mostrar éxito
  function showSuccess(message) {
    Toast.fire({
      icon: "success",
      title: message,
    });
  }

  // Función para mostrar errores
  function showError(title, message) {
    Toast.fire({
      icon: "error",
      title: title,
      text: message,
    });
    console.error(title + ":", message);
  }

  $("#btnPrintPDF").on("click", function () {
    if (
      $("#select-service").val() !== "" &&
      $("#select-service").val() !== undefined &&
      $("#select-service").val() !== null
    ) {
      const sercod = $("#select-service").val();
      const sercodText = $("#select-service").find("option:selected").text();

      // Construir la URL para el PDF
      var urlPDF =
        "reportepetitorioespecial?sercod=" +
        encodeURIComponent(sercod) +
        "&sercodText=" +
        encodeURIComponent(sercodText) +
        "&tipo=pdf";

      // Abrir una nueva ventana con la URL
      window.open(urlPDF, "_blank");
    } else {
      alert("Eliga una especialidad.");
    }
  });

  // Evento para el botón Imprimir Excel
  $("#btnPrintExcel").on("click", function () {
    if (
      $("#select-service").val() !== "" &&
      $("#select-service").val() !== undefined &&
      $("#select-service").val() !== null
    ) {
      var sercod = $("#select-service").val();
      var sercodText = $("#select-service").find("option:selected").text();
      // Construir la URL para el Excel
      var urlExcel =
        "reportepetitorioespecial?sercod=" +
        encodeURIComponent(sercod) +
        "&sercodText=" +
        encodeURIComponent(sercodText) +
        "&tipo=excel";
      console.log(urlExcel);
      // Abrir una nueva ventana con la URL
      window.open(urlExcel, "_blank");
    } else {
      alert("Eliga una especialidad.");
    }
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
  $("#tabladatos").on("click", ".grafico", function () {
    codpro = String($(this).data("codpro"));
    let despro = $(this).data("despro");
    $.fn.listarTablaDistribucion(codpro, despro);
  });
  $("#cerrarmodal").click(function () {
    $("#modaltablaproductos").css("display", "none");
    $("body").removeClass("modal-open");
    $(".modal-backdrop").remove();

    // Reabrir el modal principal solo si venimos de él
    if (modalOrigin === "product-modal") {
      setTimeout(function () {
        $("#product-modal").modal("show");
      }, 300);
    }

    // Resetear el origen
    modalOrigin = null;
  });
  $("#cerrarmodal2").click(function () {
    $("#modaltablaproductos").css("display", "none");
    $("body").removeClass("modal-open");
    $(".modal-backdrop").remove();
  });
});
