$(document).ready(function () {
  // ==============================================
  // 1. VARIABLES GLOBALES Y CONFIGURACIONES INICIALES
  // ==============================================
  let productos = [];
  let lotesSeleccionados = [];
  let productosSeleccionados = [];
  let lotesEliminados = [];
  let codinvalm = localStorage.getItem("inventariocodinvalm");
  let codinv = localStorage.getItem("inventariocodinv");
  let codproaux;
  let cambio = false;
  let tabla;
  let isLista;
  let currentPage = 1;
  let totalPages = 0;
  let segundotempo;
  let num = "";
  let qr = null;
  let tieneOriginales = null;

  // Configuración de toasts
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

  const Toast2 = Swal.mixin({
    toast: true,
    position: "top-end",
    showConfirmButton: false,
    timer: 5000,
    timerProgressBar: true,
    didOpen: (toast) => {
      toast.onmouseenter = Swal.stopTimer;
      toast.onmouseleave = Swal.resumeTimer;
    },
  });

  // Elementos del DOM
  $("#searchInput").focus();

  // ==============================================
  // 2. FUNCIONES DE INICIALIZACIÓN PRINCIPALES
  // ==============================================
  function initConLista() {
    const parametros = { opcion: 14, codinvalm: codinvalm, codinv: codinv };
    inicializarDataTableProductos(parametros);
  }

  function initSinLista() {
    const parametrosSinLista = {
      opcion: 10,
      codinv: codinv,
      codinvalm: codinvalm,
    };
    inicializarDataTableProductos(parametrosSinLista);
  }

  // Verificación inicial al cargar la página
  $.ajax({
    type: "GET",
    url: "CRUDinvlispro",
    data: { opcion: 2, codinv: codinv },
    success: function (response) {
      if (response.success) {
        $("#btnOpenModalBuscar, #btnOpenModalListar").show();
        initConLista();
        isLista = true;
      } else {
        initSinLista();
        isLista = false;
      }
    },
    error: function () {
      initSinLista();
    },
  });

  // ==============================================
  // 3. FUNCIONES DE UTILIDAD (GENÉRICAS)
  // ==============================================

  function limpiarEstadoModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
      // Remover backdrop si existe
      const backdrop = document.querySelector(".modal-backdrop");
      if (backdrop) {
        backdrop.remove();
      }

      // Resetear clases del body
      document.body.classList.remove("modal-open");
      document.body.style.paddingRight = "";
      document.body.style.overflow = "";

      // Resetear estado del modal
      modal.classList.remove("show");
      modal.style.display = "none";
      modal.setAttribute("aria-hidden", "true");
    }
  }
  function mostrarErrorCarga(mensaje) {
    $("#tabla")
      .closest(".table-responsive")
      .append(
        '<div class="alert alert-danger mt-3">' +
          '<i class="fas fa-exclamation-triangle me-2"></i>' +
          "<strong>Error al cargar datos</strong>" +
          '<div class="small mt-1">' +
          mensaje +
          "</div>" +
          '<button class="btn btn-sm btn-outline-danger mt-2" onclick="inicializarDataTableProductos(lastParams)">' +
          '<i class="fas fa-sync-alt me-1"></i> Reintentar' +
          "</button>" +
          "</div>"
      );
  }
  function actualizarContadoresLeyenda(productos) {
    // Obtener todos los productos de la tabla
    //const productos = tabla.rows().data().toArray();

    // Contadores
    let conLotes = 0;
    let sinLotes = 0;

    console.log({ productos });

    // Recorrer los productos y contar
    productos.forEach((producto) => {
      if (producto.tiene_lotes === 1) {
        conLotes++;
      } else {
        sinLotes++;
      }
    });

    console.log({ conLotes, sinLotes });

    // Actualizar los spans
    document.getElementById("cantidadConLotes").textContent = conLotes;
    document.getElementById("cantidadSinLotes").textContent = sinLotes;
  }
  function limpiarModalUnoXuno() {
    $("#buscarProductoInput").val("");
    $("#resultadosBusqueda").html(`
      <tr>
        <td colspan="3" class="text-center">
          Ingrese un término de búsqueda
        </td>
      </tr>
    `);
    $("#paginacionContainer").empty();
    currentPage = 1;
  }
  function mostrarMensajeResultado(titulo, htmlMessage, icono) {
    Swal.fire({
      title: titulo,
      html: htmlMessage,
      icon: icono,
      confirmButtonText: "Entendido",
      width: "600px",
    });
  }

  // ==============================================
  // 4. FUNCIONES RELACIONADAS CON DATATABLE
  // ==============================================
  function inicializarDataTableProductos(parametros) {
    // Destruir tabla existente si hay una
    if ($.fn.DataTable.isDataTable("#tabla")) {
      $("#tabla").DataTable().destroy();
      $("#tabla tbody").empty();
    }

    // Mostrar loader mientras se carga
    $("#tabla")
      .closest(".table-responsive")
      .prepend(
        '<div class="datatable-loading-overlay">' +
          '<div class="spinner-border text-primary"></div>' +
          "<span>Cargando productos...</span>" +
          "</div>"
      );

    tabla = $("#tabla").DataTable({
      lengthChange: false,
      ajax: {
        url: `CRUDProductos`,
        data: parametros,
        type: "GET",
        dataSrc: function (json) {
          // Ocultar loader cuando los datos están listos
          $(".datatable-loading-overlay").remove();

          if (!json || !json.data) {
            mostrarErrorCarga("Datos recibidos en formato incorrecto");
            return [];
          }

          if (productos && Array.isArray(productos)) {
            productos.push(...json.data);
          }

          try {
            actualizarContadoresLeyenda(json.data);
          } catch (e) {
            console.error("Error actualizando contadores:", e);
          }

          return json.data;
        },
        error: function (xhr, error, thrown) {
          $(".datatable-loading-overlay").remove();
          mostrarErrorCarga(thrown || "Error al cargar los datos");

          // Devuelve array vacío para que DataTable no falle
          return [];
        },
        timeout: 30000, // 30 segundos timeout
      },
      language: {
        decimal: "",
        emptyTable: "No hay datos disponibles",
        info: "Mostrando _START_ a _END_ de _TOTAL_ registros",
        infoEmpty: "No hay datos disponibles",
        infoFiltered: "(filtrado de _MAX_ registros totales)",
        infoPostFix: "",
        thousands: ",",
        lengthMenu: "Mostrar _MENU_ registros",
        loadingRecords:
          '<div class="spinner-border spinner-border-sm"></div> Cargando...',
        processing:
          '<div class="spinner-border spinner-border-sm"></div> Procesando...',
        search: "Buscar:",
        zeroRecords: "No se encontraron coincidencias",
        paginate: {
          first: "Primero",
          last: "Último",
          next: "Siguiente",
          previous: "Anterior",
        },
      },
      columns: [
        {
          data: "codpro",
          render: function (data, type, row) {
            let html = data;
            if (row.isInventariado) {
              html +=
                ' <i class="fas fa-check-circle text-success" title="Ya inventariado"></i>';
            }
            return html;
          },
        },
        {
          data: "despro",
          render: function (data, type, row) {
            return data;
          },
        },
        {
          data: "tiene_lotes",
          render: function (data, type, row) {
            const tieneLotes = data === 1;
            const inventariado = row.isInventariado;

            let btnClass = tieneLotes ? "btn-success" : "btn-danger";
            if (inventariado) btnClass = "btn-secondary";

            return `<button class="btn ${btnClass} btn-sm fuente-min btn-lotes"
                              data-codpro="${row.codpro}" 
                              ${
                                inventariado
                                  ? 'title="Ya inventariado"'
                                  : 'title="Sin inventariar"'
                              }>
                              <i class="fas fa-boxes"></i> Lotes
                              ${
                                inventariado
                                  ? '<i class="fas fa-check ms-1"></i>'
                                  : ""
                              }
                            </button>`;
          },
        },
        {
          data: "qr",
          defaultContent: "",
          render: function (data, type, row) {
            return data || "";
          },
        },
      ],
      columnDefs: [
        {
          targets: [3], // Índice de la columna QR
          visible: false, // Oculta completamente la columna
        },
      ],
      createdRow: function (row, data, dataIndex) {
        if (data.isInventariado) {
          $(row).addClass("inventariado");
        }
      },
      initComplete: function () {
        // Asegurar que el loader se quite incluso si hay éxito
        $(".datatable-loading-overlay").remove();
      },
    });

    return tabla;
  }

  // ==============================================
  // 5. FUNCIONES RELACIONADAS CON LOTES
  // ==============================================
  function obtenerLotesDisponibles(lotesVaciosDisponibles, tablaLotes) {
    const lotesSeleccionados = [];
    const lotesNormalesMostrados = [];

    tablaLotes.find("tr").each(function () {
      const $tr = $(this);
      const lote = $tr.find("td:first-child").text().trim();
      const isSelect = $tr.find(".select-lote-vacio").length > 0;

      if (isSelect) {
        const selectedLote = $tr.find(".select-lote-vacio").val();
        if (selectedLote) lotesSeleccionados.push(selectedLote);
      } else if (lote) {
        lotesNormalesMostrados.push(lote);
      }
    });

    return lotesVaciosDisponibles.filter((lote) => {
      return (
        !lotesSeleccionados.includes(lote.lote) &&
        !lotesNormalesMostrados.includes(lote.lote)
      );
    });
  }
  function agregarFilaLoteVacio(tablaLotes, lotesVaciosFiltrados, stkfra) {
    const existeFilaVacia =
      tablaLotes.find(".select-lote-vacio").filter(function () {
        return $(this).val() === "";
      }).length > 0;

    if (existeFilaVacia) {
      Toast.fire({
        icon: "warning",
        title: "Complete el lote vacío actual antes de agregar otro",
      });
      return;
    }

    const selectRow = $(`
      <tr data-stkalm="0" data-stkalm_m="0">
        <td>
          <select class="form-control form-control-sm select-lote-vacio" style="width: 100%">
            <option value="">Seleccione un lote vacío</option>
            ${lotesVaciosFiltrados
              .map(
                (lote) => `<option value="${lote.lote}">${lote.lote}</option>`
              )
              .join("")}
          </select>
        </td>
        <td><input type="number" class="form-control form-control-sm tome fuente-min" value="0"></td>
        <td><input type="number" class="form-control form-control-sm tomf fuente-min" value="0" ${
          stkfra === 1 ? "disabled" : ""
        }></td>
        <td></td>
        <td></td>
        <td>
          <button type="button" class="btn btn-sm btn-danger btn-eliminar-fila">
            <i class="fas fa-trash"></i>
          </button>
        </td>
      </tr>
    `);

    tablaLotes.append(selectRow);

    // Inicializar Select2
    selectRow
      .find(".select-lote-vacio")
      .select2({
        theme: "bootstrap-5",
        placeholder: "Buscar lote vacío...",
        allowClear: true,
        width: "resolve",
        dropdownParent: $("#modalLotes"),
      })
      .on("select2:select", function () {
        $("#btnAgregarFila").prop("disabled", false);
      });

    // Manejar evento para eliminar fila
    selectRow.find(".btn-eliminar-fila").click(function () {
      const loteSeleccionado = selectRow.find(".select-lote-vacio").val();

      //Eliminar el lote del objeto lotesSeleccionados
      if (codproaux && loteSeleccionado && lotesSeleccionados[codproaux]) {
        lotesSeleccionados[codproaux] = lotesSeleccionados[codproaux].filter(
          (lote) => lote.lote !== loteSeleccionado
        );
      }

      selectRow.remove();
      $("#btnAgregarFila").prop("disabled", false);
    });
  }
  function configurarEventoAgregarFila(lotesVaciosDisponibles, stkfra) {
    $("#btnAgregarFila")
      .off("click")
      .on("click", function () {
        const tablaLotes = $("#tablaLotes tbody");
        const lotesDisponibles = obtenerLotesDisponibles(
          lotesVaciosDisponibles,
          tablaLotes
        );

        if (lotesDisponibles.length === 0) {
          Swal.fire({
            title: "No hay lotes disponibles",
            text: "No existen más lotes vacíos para agregar a este producto.",
            icon: "info",
            confirmButtonText: "Entendido",
            confirmButtonColor: "#3085d6",
          });
          return;
        }

        $(this).prop("disabled", true);
        agregarFilaLoteVacio(tablaLotes, lotesDisponibles, stkfra);
        setTimeout(() => {
          $("#btnAgregarFila").prop("disabled", false);
        }, 300);
      });
  }
  function abrirModalLotes(
    codpro,
    despro,
    stkfra,
    qr = null,
    requiereValidacion = true
  ) {
    console.log("Abriendo modal de lotes para:", {
      codpro,
      despro,
      stkfra,
      qr,
    });
    console.log(
      "Estado actual de lotesSeleccionados antes de abrir:",
      lotesSeleccionados
    );

    $("#qrForValidate").val("");
    $("#guardarLotes").attr("disabled", true);

    // Limpiar completamente cualquier estado anterior del modal
    $(".modal-backdrop").remove();
    $("body").removeClass("modal-open");
    $("body").css("padding-right", "");
    $("body").css("overflow", "");
    $("#modalLotes").removeClass("show");
    $("#modalLotes").css("display", "none");
    $("#modalLotes").attr("aria-hidden", "true");

    // Mostrar el modal inmediatamente
    const modalLotes = new bootstrap.Modal(
      document.getElementById("modalLotes")
    );

    // Ya no limpiamos lotesSeleccionados aquí
    // lotesSeleccionados = {};

    if (requiereValidacion) {
      ocultarLotesContainer();
      mostrarValidarContainer();
    } else {
      ocultarValidarContainer();
      mostrarLotesContainer();
      $("#guardarLotes").attr("disabled", false);
    }

    modalLotes.show();

    // Configurar eventos de validación solo si es necesario
    if (requiereValidacion || qr) {
      $("#btnSkip")
        .off("click")
        .on("click", function (e) {
          console.log("Botón Skip clickeado");
          $("#guardarLotes").attr("disabled", false);
          ocultarValidarContainer();
          mostrarLotesContainer();
        });

      $(document)
        .off("keypress.picking")
        .on("keypress.picking", "#qrForValidate", function (e) {
          if (e.keyCode === 13) {
            console.log("Validando QR:", $("#qrForValidate").val());
            validarQR($("#qrForValidate").val());
          }
        });

      $("#btnValidate")
        .off("click")
        .on("click", function (e) {
          const qrForVal = $("#qrForValidate").val();
          console.log("Validando QR desde botón:", qrForVal);
          validarQR(qrForVal);
        });
    }

    // Mostrar spinner mientras carga
    $("#modalLotesLabel").html(`
    <div class="d-flex align-items-center">
      <span>Lotes del producto: ${despro}(${codpro})</span>
      <div class="spinner-border spinner-border-sm ms-2"></div>
    </div>
  `);

    // Cargar los lotes bajo demanda
    $.getJSON(
      `CRUDInventarioToma?opcion=2&codpro=${codpro}&codinvalm=${codinvalm}&codinv=${codinv}`
    )
      .done(function (response) {
        console.log("Respuesta de carga de lotes:", response);
        if (response.resultado?.toLowerCase() === "ok") {
          mostrarLotesModal(codpro, despro, stkfra, {
            normales: response.data.lotes || [],
            vacios: response.data.lotes_vacios || [],
            stkfra: stkfra,
          });
        }
      })
      .fail(function (error) {
        console.error("Error al cargar lotes:", error);
        Toast.fire({ icon: "error", title: "Error al cargar lotes" });
        modalLotes.hide();
      });
  }
  function mostrarLotesModal(codpro, despro, stkfra, datos) {
    console.log("Mostrando modal de lotes con datos:", {
      codpro,
      despro,
      stkfra,
      datos,
    });
    console.log(
      "Estado actual de lotesSeleccionados antes de mostrar:",
      lotesSeleccionados
    );

    if (!lotesSeleccionados[codpro]) {
      lotesSeleccionados[codpro] = [];
    }

    $("#modalLotesLabel").text(
      `Lotes del producto: ${despro}(${codpro}) - stkfra:${stkfra}`
    );

    const tablaLotes = $("#tablaLotes tbody");
    tablaLotes.empty();

    // Cargar los lotes existentes si hay
    if (lotesSeleccionados[codpro] && lotesSeleccionados[codpro].length > 0) {
      console.log(
        "Cargando lotes existentes para el producto:",
        codpro,
        lotesSeleccionados[codpro]
      );
      lotesSeleccionados[codpro].forEach((l) => {
        tablaLotes.append(`
        <tr data-stkalm="${l.stkalm}" data-stkalm_m="${
          l.stkalm_m
        }" data-tome="${l.tome}" data-tomf="${l.tomf}">
          <td>${l.lote}</td>
          <td><input type="number" class="form-control form-control-sm tome fuente-min" value="${
            l.tome
          }"></td>
          <td><input type="number" class="form-control form-control-sm tomf fuente-min" value="${
            l.tomf
          }" ${stkfra === 1 ? "disabled" : ""}></td>
          <td>${l.stkalm === 0 ? "" : l.stkalm}</td>
          <td>${l.stkalm_m === 0 ? "" : l.stkalm_m}</td>
          <td></td>
        </tr>
      `);
      });
    } else {
      // Cargar los lotes normales del servidor
      datos.normales.forEach((l) => {
        console.log("Agregando lote normal:", l);
        tablaLotes.append(`
        <tr data-stkalm="${l.stkalm}" data-stkalm_m="${
          l.stkalm_m
        }" data-tome="${l.tome}" data-tomf="${l.tomf}">
          <td>${l.lote}</td>
          <td><input type="number" class="form-control form-control-sm tome fuente-min" value="${
            l.tome
          }"></td>
          <td><input type="number" class="form-control form-control-sm tomf fuente-min" value="${
            l.tomf
          }" ${stkfra === 1 ? "disabled" : ""}></td>
          <td>${l.stkalm === 0 ? "" : l.stkalm}</td>
          <td>${l.stkalm_m === 0 ? "" : l.stkalm_m}</td>
          <td></td>
        </tr>
      `);
      });
    }

    const todosLotesVaciosDisponibles = datos.vacios.map((lote) => ({
      lote: lote.lote,
      tome: lote.tome || 0,
      tomf: lote.tomf || 0,
      stkalm: lote.stkalm || 0,
      stkalm_m: lote.stkalm_m || 0,
    }));

    console.log("Lotes vacíos disponibles:", todosLotesVaciosDisponibles);

    codproaux = codpro;
    configurarEventoAgregarFila(todosLotesVaciosDisponibles, stkfra);
  }
  function validarQR(qrForVal) {
    if (qrForVal === "") {
      Toast.fire({ icon: "error", title: "Codigo de barras vacio" });
      return;
    }

    if (qr === qrForVal) {
      $("#guardarLotes").attr("disabled", false);
      ocultarValidarContainer();
      mostrarLotesContainer();
    } else {
      Toast.fire({ icon: "error", title: "Codigo de barras incorrecto" });
    }
  }

  // Funciones auxiliares para mostrar/ocultar contenedores
  function mostrarValidarContainer() {
    $("#validar-container").attr("style", "display:block;");
  }
  function ocultarValidarContainer() {
    $("#validar-container").attr("style", "display:none;");
  }
  function ocultarLotesContainer() {
    $("#lotes-container").attr("style", "display:none;");
  }
  function mostrarLotesContainer() {
    $("#lotes-container").attr("style", "display:block;");
    const $input = $("#modalLotes").find(".tome:visible").first();
    $input.focus();
    // Mover el cursor al final del valor
    const val = $input.val();
    $input.val(""); // Vacía temporalmente
    $input.val(val); // Restaura el valor, el cursor queda al final
  }

  // ==============================================
  // 6. FUNCIONES DE GESTIÓN DE PRODUCTOS
  // ==============================================
  function cargarLaboratorios() {
    $.ajax({
      url: "productosPorLaboratorio",
      method: "GET",
      data: { opcion: 1 },
      dataType: "json",
      beforeSend: function () {
        $("#selectLaboratorio").html(
          '<option selected disabled value="">Cargando laboratorios...</option>'
        );
      },
      success: function (data) {
        try {
          if (!data?.laboratorios?.length) {
            throw new Error("No se recibieron laboratorios");
          }

          let options =
            '<option selected disabled value="">Seleccione laboratorio</option>';

          const sortedLabs = [...data.laboratorios].sort((a, b) => {
            if (a.id === "0000") return -1;
            if (b.id === "0000") return 1;
            return a.nombre.localeCompare(b.nombre);
          });

          sortedLabs.forEach((lab) => {
            if (lab.id && lab.nombre) {
              const nombreLimpio = lab.nombre.replace(/\[.*?\]/g, "").trim();
              options += `<option value="${lab.id}">${nombreLimpio}</option>`;
            }
          });

          $("#selectLaboratorio").html(options);

          if (typeof $.fn.select2 === "function") {
            $("#selectLaboratorio").select2({
              placeholder: "Buscar laboratorio...",
              width: "100%",
              dropdownParent: $("#modalBuscarProducto"),
              language: "es",
              minimumInputLength: 1,
              escapeMarkup: function (markup) {
                return markup;
              },
            });
          }
        } catch (e) {
          console.error("Error:", e);
          $("#selectLaboratorio").html(
            '<option selected disabled value="">Error al cargar</option>'
          );
          Toast.fire({
            icon: "error",
            title: "Error en los datos",
            text: "No se pudo cargar la lista completa",
            timer: 5000,
          });
        }
      },
      error: function (xhr) {
        $("#selectLaboratorio").html(
          '<option selected disabled value="">Error de conexión</option>'
        );
        Toast.fire({
          icon: "error",
          title: "Error de red",
          text: "No se pudo conectar al servidor",
          timer: 5000,
        });
      },
    });
  }
  function buscarProductos() {
    const terminoBusqueda = $("#buscarProductoInput").val().trim();

    if (terminoBusqueda.length < 2) {
      $("#resultadosBusqueda").html(
        `<tr><td colspan="4" class="text-center text-danger">Ingrese al menos 2 caracteres</td></tr>`
      );
      $("#paginacionContainer").empty();
      return;
    }

    $("#resultadosBusqueda").html(
      `<tr><td colspan="4" class="text-center"><div class="spinner-border text-primary"></div></td></tr>`
    );

    $.ajax({
      url: "allproducts",
      method: "GET",
      data: { q: terminoBusqueda, page: currentPage },
      dataType: "json",
      success: function (response) {
        try {
          if (!response.success) {
            throw new Error(response.error || "Error en la respuesta");
          }

          if (response.data.length === 0) {
            $("#resultadosBusqueda").html(
              `<tr><td colspan="4" class="text-center">No se encontraron productos</td></tr>`
            );
            $("#paginacionContainer").empty();
            return;
          }

          const rows = response.data
            .map(
              (producto) => `
          <tr>
            <td>${producto.codigo}</td>
            <td>${producto.nombre}</td>
            <td>
              <button class="btn btn-sm btn-success btn-agregar" 
                      data-codigo="${producto.codigo}"
                      data-nombre="${producto.nombre}">
                <i class="bi bi-plus-circle"></i> Agregar
              </button>
            </td>
          </tr>
        `
            )
            .join("");

          $("#resultadosBusqueda").html(rows);

          const totalItems = response.totalItems;
          const pageSize = response.pageSize;
          totalPages = Math.ceil(totalItems / pageSize);
          renderPaginacion(totalItems);

          $(".btn-agregar")
            .off("click")
            .on("click", agregarProductoSeleccionado);
        } catch (e) {
          console.error("Error procesando respuesta:", e);
          $("#resultadosBusqueda").html(
            `<tr><td colspan="4" class="text-center text-danger">${e.message}</td></tr>`
          );
          $("#paginacionContainer").empty();
        }
      },
      error: function (xhr, status, error) {
        console.error("Error AJAX:", error);
        $("#resultadosBusqueda").html(
          `<tr><td colspan="4" class="text-center text-danger">Error al buscar productos</td></tr>`
        );
        $("#paginacionContainer").empty();
      },
    });
  }
  function renderPaginacion(totalItems) {
    $("#paginacionContainer").off("click", ".page-link");

    if (totalPages <= 1) {
      $("#paginacionContainer").empty();
      return;
    }

    let paginacionHTML = `
        <nav aria-label="Navegación de productos">
            <ul class="pagination pagination-sm justify-content-center mb-0">
                <li class="page-item ${currentPage === 1 ? "disabled" : ""}">
                    <a class="page-link" href="#" data-page="${
                      currentPage - 1
                    }" aria-label="Anterior">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
    `;

    let startPage = Math.max(1, currentPage - 2);
    let endPage = Math.min(totalPages, startPage + 4);

    if (endPage - startPage < 4) {
      startPage = Math.max(1, endPage - 4);
    }

    for (let i = startPage; i <= endPage; i++) {
      paginacionHTML += `
            <li class="page-item ${i === currentPage ? "active" : ""}">
                <a class="page-link" href="#" data-page="${i}">${i}</a>
            </li>
        `;
    }

    paginacionHTML += `
                <li class="page-item ${
                  currentPage === totalPages ? "disabled" : ""
                }">
                    <a class="page-link" href="#" data-page="${
                      currentPage + 1
                    }" aria-label="Siguiente">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
        <div class="text-center text-muted small mt-2">
            Mostrando página ${currentPage} de ${totalPages} (Total: ${totalItems} productos)
        </div>
    `;

    $("#paginacionContainer").html(paginacionHTML);

    $("#paginacionContainer").on("click", ".page-link", function (e) {
      e.preventDefault();
      const pageNumber = parseInt($(this).data("page"));
      if (!isNaN(pageNumber) && pageNumber >= 1 && pageNumber <= totalPages) {
        currentPage = pageNumber;
        buscarProductos();
      }
    });
  }
  function agregarProductoSeleccionado() {
    const $btn = $(this);
    const codigo = $btn.data("codigo");
    const nombre = $btn.data("nombre");

    if (productosSeleccionados.some((p) => p.codigo === codigo)) {
      alert("Este producto ya fue agregado");
      return;
    }

    productosSeleccionados.push({
      codigo: codigo,
      nombre: nombre,
    });

    actualizarTablaSeleccionados();
  }
  function actualizarTablaSeleccionados() {
    const rows = productosSeleccionados
      .map(
        (producto, index) => `
        <tr>
            <td>${producto.codigo}</td>
            <td>${producto.nombre}</td>
            <td>
                <button class="btn btn-sm btn-danger btn-eliminar" data-index="${index}">
                    <i class="bi bi-trash"></i> Eliminar
                </button>
            </td>
        </tr>
    `
      )
      .join("");

    $("#productosSeleccionados").html(rows);

    $(".btn-eliminar").off("click").on("click", eliminarProductoSeleccionado);
  }
  function eliminarProductoSeleccionado() {
    const index = $(this).data("index");
    productosSeleccionados.splice(index, 1);
    actualizarTablaSeleccionados();
  }
  function cargarListaProductos() {
    $.ajax({
      url: "CRUDinvlispro",
      method: "GET",
      data: { opcion: 1, codinvalm: codinvalm },
      beforeSend: function () {
        $("#cuerpoTablaLista").html(
          '<tr><td colspan="3" class="text-center">Cargando productos...</td></tr>'
        );
      },
      success: function (response) {
        if (response.success) {
          if ($.fn.DataTable.isDataTable("#tablaListaProductos")) {
            $("#tablaListaProductos").DataTable().destroy();
          }

          let html = "";
          response.data.forEach(function (producto) {
            html += `<tr>
                        <td>${producto.codigo}</td>
                        <td>${producto.nombre}</td>
                        <td>
                          <button class="btn btn-sm btn-outline-primary btn-seleccionar" 
                                  data-codigo="${producto.codigo}" 
                                  data-nombre="${producto.nombre}">
                            <i class="bi bi-plus-circle"></i> Seleccionar
                          </button>
                        </td>
                      </tr>`;
          });

          $("#cuerpoTablaLista").html(html);

          $("#tablaListaProductos").DataTable({
            language: {
              url: "//cdn.datatables.net/plug-ins/1.10.25/i18n/Spanish.json",
            },
            dom: '<"top"lf>rt<"bottom"ip>',
            pageLength: 10,
          });

          $(".filtros").hide();
        } else {
          Toast.fire({
            icon: "error",
            title: response.message,
          });
        }
      },
    });
  }

  // ==============================================
  // 7. FUNCIONES DE GUARDADO
  // ==============================================
  $("#comprobarexistencia").val(Math.random());

  num = $("#comprobarexistencia").val();

  // Función común para preparar y validar los datos antes de guardar
  function prepararDatosParaGuardar() {
    console.log(
      "Preparando datos para guardar. Estado actual de lotesSeleccionados:",
      lotesSeleccionados
    );

    const jsonProductos = [];

    // Iterar sobre todos los productos en lotesSeleccionados
    Object.entries(lotesSeleccionados).forEach(([codpro, lotes]) => {
      console.log(`Procesando producto ${codpro} con lotes:`, lotes);

      // Filtrar lotes con valores positivos
      const lotesValidos = lotes.filter(
        (lote) => lote.tome >= 0 || lote.tomf >= 0
      );

      if (lotesValidos.length > 0) {
        console.log(`Lotes válidos para producto ${codpro}:`, lotesValidos);

        // Mapear los lotes válidos al formato requerido
        const lotesFormateados = lotesValidos.map((lote) => ({
          codpro: codpro,
          lote: lote.lote,
          tome: lote.tome,
          tomf: lote.tomf,
          stkalm: lote.stkalm || 0,
          stkalm_m: lote.stkalm_m || 0,
        }));

        jsonProductos.push(...lotesFormateados);
      }
    });

    const resultado = {
      agregar: jsonProductos,
      eliminar: lotesEliminados,
    };

    console.log("Datos preparados para guardar:", resultado);
    return resultado;
  }

  // Función común para mostrar el mensaje de confirmación
  function mostrarConfirmacionGuardado(datos) {
    console.log("Mostrando confirmación con datos:", datos);

    if (datos.agregar.length === 0 && datos.eliminar.length === 0) {
      console.log("No hay datos para guardar");
      return Promise.resolve(false);
    }

    // Agrupar los lotes por producto para mejor visualización
    const lotesPorProducto = datos.agregar.reduce((acc, lote) => {
      if (!acc[lote.codpro]) {
        acc[lote.codpro] = [];
      }
      acc[lote.codpro].push(lote);
      return acc;
    }, {});

    const mensajeConfirmacion = Object.entries(lotesPorProducto)
      .map(([codpro, lotes]) => {
        const producto = productos.find((p) => p.codpro === codpro);
        const nombreProducto = producto ? producto.despro : codpro;
        return `
          <div class="mb-2">
            <strong>Producto: ${nombreProducto} (${codpro})</strong>
            <ul class="list-unstyled ms-3">
              ${lotes
                .map(
                  (lote) =>
                    `<li>Lote ${lote.lote}: Tome=${lote.tome}, Tomf=${lote.tomf}</li>`
                )
                .join("")}
            </ul>
          </div>
        `;
      })
      .join("");

    return Swal.fire({
      title: "¿Confirmar guardado?",
      html: `<div class="text-start">
        <p>Se guardarán los siguientes productos y lotes:</p>
        ${mensajeConfirmacion}
      </div>`,
      icon: "question",
      showCancelButton: true,
      confirmButtonText: "Sí, guardar",
      cancelButtonText: "Cancelar",
    }).then((result) => result.isConfirmed);
  }

  // Función común para realizar el guardado
  function realizarGuardado(datos, esAutoguardado = false) {
    console.log("Datos a guardar:", datos);

    return $.ajax({
      url: "CRUDInventarioToma?opcion=3&codinvalm=" + codinvalm,
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(datos),
      dataType: "json",
    })
      .then(function (response) {
        if (response.resultado === "ok") {
          cambio = false;
          Toast.fire({
            icon: "success",
            title: esAutoguardado
              ? "Autoguardado correctamente."
              : "Guardado correctamente.",
          });
          lotesSeleccionados = {};
          if (isLista) {
            initConLista();
            // resetar el array de lotesSeleccionados
          } else {
            initSinLista();
          }
          return true;
        } else {
          if (response.mensaje === "cerrado") {
            Toast.fire({
              icon: "warning",
              title: "Este inventario ya esta cerrado, vuelva y verifique.",
            });
          } else {
            Toast.fire({
              icon: "error",
              title: "Error al guardar los datos.",
            });
          }
          return false;
        }
      })
      .fail(function () {
        Toast.fire({
          icon: "error",
          title: "Error de conexión con el servidor.",
        });
        return false;
      });
  }

  // ==============================================
  // 8. MANEJADORES DE EVENTOS PRINCIPALES
  // ==============================================

  // Eventos generales
  $("#modalLotes").on("hidden.bs.modal", function () {
    limpiarEstadoModal(this.id);
  });
  $(".modal").on("hidden.bs.modal", function () {
    limpiarEstadoModal(this.id);
  });

  // Eventos de búsqueda
  $("#searchInput").on("input", function () {
    const filtro = $('input[name="searchOption"]:checked').val();
    const valor = $(this).val().toLowerCase();

    if (valor.length > 3) {
      const coincidencias = productos.filter((p) => {
        if (p[filtro] && p[filtro].toLowerCase) {
          return p[filtro] === valor;
        }
        return false;
      });

      if (coincidencias.length === 1) {
        console.log("Coincidencia encontrada:", coincidencias[0]);
      }

      coincidencias.forEach((c) => {
        const codproExistente = tabla
          .data()
          .toArray()
          .some((row) => row.codpro === c.codpro);

        if (!codproExistente) {
          // Agregar a la tabla si no existe
          //tabla.row.add(c).draw(false);
          //lotesmodal(c.codpro);
        } else {
          qr = c.qr;
          tieneOriginales = c.isInventariado;
          abrirModalLotes(c.codpro, c.despro, c.stkfra, c.qr, true);
        }
      });

      if (coincidencias.length > 0) {
        $("#searchInput").val("");
      }
    }
  });
  $("#btnLimpiarBusqueda").on("click", function () {
    limpiarModalUnoXuno();
  });

  // Eventos de productos
  $("#tabla").on("click", ".btn-eliminar", function () {
    const row = $(this).closest("tr");
    const data = tabla.row(row).data();
    const codpro = data.codpro;

    Swal.fire({
      title: "¿Estás seguro?",
      text: `¿Deseas eliminar el producto "${data.despro}"?`,
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      cancelButtonColor: "#3085d6",
      confirmButtonText: "Sí, eliminar",
      cancelButtonText: "Cancelar",
    }).then((result) => {
      if (result.isConfirmed) {
        tabla.row(row).remove().draw(false);
        delete lotesSeleccionados[codpro];
      }
    });
  });
  $("#tabla").on("click", ".btn-lotes", function () {
    const rowData = tabla.row($(this).closest("tr")).data();
    console.log({ rowData });
    const codpro = rowData.codpro;
    const qrProd = rowData.qr;
    qr = rowData.qr;
    const stkfraActual = rowData.stkfra;
    tieneOriginales = rowData.isInventariado;

    abrirModalLotes(codpro, rowData.despro, stkfraActual, qrProd, true);
  });

  // Eventos de lotes
  $("#guardarLotes").on("click", function () {
    const codpro = codproaux;
    const lotesModificados = [];
    let tieneConteo = false;

    // Verificar cambios en los lotes
    $("#tablaLotes tbody tr").each(function () {
      const $tr = $(this);
      const $firstTd = $tr.find("td").eq(0);
      let loteValue;

      // Obtener valor del lote (select o texto)
      if ($firstTd.find("select").length > 0) {
        loteValue = $firstTd.find("select").val();
        if (!loteValue) return true; // Saltar si no hay lote seleccionado
      } else {
        loteValue = $firstTd.text().trim();
      }

      const tomeIngresado = parseFloat($tr.find(".tome").val()) || 0;
      const tomfIngresado = parseFloat($tr.find(".tomf").val()) || 0;
      const tomeOriginal = parseFloat($tr.data("tome")) || 0;
      const tomfOriginal = parseFloat($tr.data("tomf")) || 0;
      const stkalmOriginal = parseFloat($tr.data("stkalm")) || 0;
      const stkalm_mOriginal = parseFloat($tr.data("stkalm_m")) || 0;

      // Validar si hay cambios reales
      const esNuevoLote = $firstTd.find("select").length > 0;
      let tomeCambiado = true;
      let tomfCambiado = true;
      let excepcionCero = false;

      if (tieneOriginales === 1) {
        // Solo para lotes que ya existen en BD
        excepcionCero =
          (tomeOriginal !== 0 || tomfOriginal !== 0) &&
          tomeIngresado === 0 &&
          tomfIngresado === 0;
        tomeCambiado = tomeIngresado !== tomeOriginal;
        tomfCambiado = tomfIngresado !== tomfOriginal;
      }

      const tieneCambiosReales =
        esNuevoLote || tomeCambiado || tomfCambiado || excepcionCero;
      const tieneValoresPositivos = tomeIngresado >= 0 || tomfIngresado >= 0;

      // Para nuevos lotes (tieneOriginales === 0), siempre permitir guardar aunque ambos sean 0
      if (
        (tieneCambiosReales && tieneValoresPositivos) ||
        (tieneOriginales === 0 && esNuevoLote)
      ) {
        tieneConteo = true;

        lotesModificados.push({
          lote: loteValue,
          tome: tomeIngresado,
          tomf: tomfIngresado,
          tomeOriginal: tomeOriginal,
          tomfOriginal: tomfOriginal,
          stkalm: stkalmOriginal,
          stkalm_m: stkalm_mOriginal,
          esNuevo: esNuevoLote,
          cambios: {
            tome: tomeCambiado,
            tomf: tomfCambiado,
          },
          excepcionCero: excepcionCero,
        });
      }
    });

    // Validar si hay lotes con cambios para guardar
    if (lotesModificados.length === 0) {
      Toast.fire({
        icon: "info",
        title: "No hay cambios válidos para guardar.",
        text: "Los valores ingresados deben ser diferentes a los originales y tener al menos un valor positivo.",
      });
      return;
    }

    // Filtrar solo los lotes con cambios reales
    const lotesConCambiosReales = lotesModificados.filter(
      (lote) =>
        lote.esNuevo ||
        lote.cambios.tome ||
        lote.cambios.tomf ||
        lote.excepcionCero
    );

    if (lotesConCambiosReales.length === 0) {
      Toast.fire({
        icon: "warning",
        title: "No se detectaron cambios reales.",
        text: "Los valores ingresados son iguales a los valores originales.",
      });
      return;
    }

    // Mostrar confirmación con detalles de cambios
    const mensajeConfirmacion = lotesConCambiosReales
      .map((lote) => {
        const cambios = [];
        if (lote.esNuevo) {
          cambios.push("Lote nuevo");
        }
        if (lote.cambios.tome) {
          cambios.push(`Tome: ${lote.tomeOriginal} → ${lote.tome}`);
        }
        if (lote.cambios.tomf) {
          cambios.push(`Tomf: ${lote.tomfOriginal} → ${lote.tomf}`);
        }
        return `Lote ${lote.lote}: ${cambios.join(", ")}`;
      })
      .join("<br>");

    Swal.fire({
      title: "¿Confirmar cambios?",
      html: `<div class="text-start">
            <p>Se guardarán los siguientes cambios:</p>
            <div class="alert alert-info p-2 small">${mensajeConfirmacion}</div>
            <p class="text-muted small">Solo se guardarán los campos modificados.</p>
        </div>`,
      icon: "question",
      showCancelButton: true,
      confirmButtonText: "Sí, guardar solo cambios",
      cancelButtonText: "Cancelar",
      focusConfirm: false,
    }).then((result) => {
      if (result.isConfirmed) {
        // Inicializar array si no existe
        if (!lotesSeleccionados[codpro]) {
          lotesSeleccionados[codpro] = [];
        }

        // Procesar cada lote con cambios
        lotesConCambiosReales.forEach((lote) => {
          const loteExistenteIndex = lotesSeleccionados[codpro].findIndex(
            (l) => l.lote === lote.lote
          );

          if (loteExistenteIndex !== -1) {
            // Actualizar lote existente solo con campos modificados
            if (lote.cambios.tome) {
              lotesSeleccionados[codpro][loteExistenteIndex].tome = lote.tome;
            }
            if (lote.cambios.tomf) {
              lotesSeleccionados[codpro][loteExistenteIndex].tomf = lote.tomf;
            }
            // Mantener los valores originales de stkalm y stkalm_m
            lotesSeleccionados[codpro][loteExistenteIndex].stkalm = lote.stkalm;
            lotesSeleccionados[codpro][loteExistenteIndex].stkalm_m =
              lote.stkalm_m;
          } else {
            // Agregar nuevo lote
            lotesSeleccionados[codpro].push({
              lote: lote.lote,
              tome: lote.tome,
              tomf: lote.tomf,
              stkalm: lote.stkalm,
              stkalm_m: lote.stkalm_m,
            });
          }
        });

        // Actualizar estado
        cambio = true;

        // Marcar producto como inventariado
        const producto = productos.find((p) => p.codpro === codpro);
        if (producto) {
          producto.isInventariado = tieneConteo;
        }

        // Actualizar DataTable
        tabla.rows().every(function () {
          const rowData = this.data();
          if (rowData.codpro === codpro) {
            rowData.isInventariado = tieneConteo;
            this.data(rowData).draw(false);
          }
        });

        // Cerrar modal
        const modalLotes = bootstrap.Modal.getInstance(
          document.getElementById("modalLotes")
        );
        modalLotes.hide();
        limpiarEstadoModal("modalLotes");

        Toast.fire({
          icon: "success",
          title: "Cambios guardados correctamente.",
          text: `Se actualizaron ${lotesConCambiosReales.length} lotes con cambios.`,
        });
      }
    });

    // Enfocar campo de búsqueda después de cerrar
    setTimeout(() => $("#searchInput").focus(), 500);
  });
  // Eventos de guardado
  $("#guardar").on("click", function () {
    const datos = prepararDatosParaGuardar();

    if (datos.agregar.length > 0 || datos.eliminar.length > 0) {
      mostrarConfirmacionGuardado(datos).then((confirmado) => {
        if (confirmado) {
          realizarGuardado(datos);
        }
      });
    } else {
      Toast.fire({
        icon: "info",
        title: "No hay registros seleccionados.",
      });
    }
  });
  $("#volver").on("click", function () {
    if (!cambio) {
      $("#contenido").load("inventarioalmacendetalle.html");
    } else {
      Swal.fire({
        title: "¿Desea Guardar los cambios realizados?",
        text: "Se han realizado cambios.",
        icon: "warning",
        showCancelButton: true,
        showDenyButton: true,
        confirmButtonText: "Guardar",
        denyButtonText: "No Guardar",
        cancelButtonText: "Cerrar",
      }).then((result) => {
        if (result.isConfirmed) {
          const datos = prepararDatosParaGuardar();
          mostrarConfirmacionGuardado(datos).then((confirmado) => {
            if (confirmado) {
              realizarGuardado(datos).then((success) => {
                if (success) {
                  $("#contenido").load("inventarioalmacendetalle.html");
                }
              });
            }
          });
        } else if (result.isDenied) {
          $("#contenido").load("inventarioalmacendetalle.html");
        }
      });
    }
  });
  $("#recargar").on("click", function () {
    $("#contenido").load("inventarioproductos.html");
  });

  // Eventos de modales
  $("#modalBuscarProducto").on("show.bs.modal", function () {
    $("#buscar-tab").tab("show");
    console.log("Modal de búsqueda abierto");
    limpiarModalUnoXuno();
    productosSeleccionados.length = 0;
    console.log(productosSeleccionados);
    actualizarTablaSeleccionados();
  });
  $("#laboratorio-tab").on("click", function () {
    if ($("#selectLaboratorio").find("option").length <= 1) {
      cargarLaboratorios();
    }
  });
  $("#btnConfirmarProductos").on("click", function () {
    if (productosSeleccionados.length === 0) {
      Toast.fire({
        icon: "warning",
        title: "No hay productos seleccionados",
      });
      return;
    }

    const codigos = productosSeleccionados.map((p) => String(p.codigo));

    const parametros = {
      codigos: codigos,
      codinvalm: codinvalm,
    };

    const loadingSwal = Swal.fire({
      title: "Procesando productos",
      html: "Por favor espere mientras se agregan los productos...",
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      },
    });

    $.ajax({
      type: "POST",
      url: "CRUDinvlispro",
      data: JSON.stringify(parametros),
      contentType: "application/json",
      dataType: "json",
      success: function (response) {
        loadingSwal.close();

        let htmlMessage = `
          <div class="text-start">
            <p>Total de productos procesados: <strong>${
              response.total || productosSeleccionados.length
            }</strong></p>
            <p>Productos agregados exitosamente: <strong class="text-success">${
              response.exitosos || 0
            }</strong></p>
        `;

        if (response.errores && response.errores.length > 0) {
          htmlMessage += `
            <p>Errores encontrados: <strong class="text-danger">${response.errores.length}</strong></p>
            <div class="mt-2" style="max-height: 200px; overflow-y: auto;">
              <ul class="list-unstyled small">
          `;

          response.errores.forEach((error) => {
            htmlMessage += `<li class="text-danger mb-1">${error}</li>`;
          });

          htmlMessage += `
              </ul>
            </div>
          `;
        }

        htmlMessage += `</div>`;

        mostrarMensajeResultado(
          response.success ? "Proceso completado" : "Proceso con observaciones",
          htmlMessage,
          response.success ? "success" : "warning"
        );

        productosSeleccionados.length = 0;
        console.log(productosSeleccionados);
        actualizarTablaSeleccionados();

        initConLista();
      },
      error: function (xhr, status, error) {
        loadingSwal.close();
        mostrarMensajeResultado(
          "Error",
          `<div class="text-start">
            <p class="text-danger">Error al procesar los productos:</p>
            <p>${error}</p>
          </div>`,
          "error"
        );
      },
    });
  });
  $("#btnAgregarPorLab").on("click", function () {
    const labId = $("#selectLaboratorio").val();
    if (!labId) {
      Toast.fire({ icon: "warning", title: "Seleccione un laboratorio" });
      return;
    }

    const loadingSwal = Swal.fire({
      title: "Procesando productos",
      html: "Por favor espere mientras se agregan los productos del laboratorio...",
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      },
    });

    $.ajax({
      url: "productosPorLaboratorio",
      method: "POST",
      data: {
        laboratorioId: labId,
        codinvalm: codinvalm,
      },
      success: function (response) {
        loadingSwal.close();

        let htmlMessage = `
          <div class="text-start">
            <p>Total de productos procesados: <strong>${response.total}</strong></p>
            <p>Productos agregados exitosamente: <strong class="text-success">${response.exitosos}</strong></p>
        `;

        if (response.errores && response.errores.length > 0) {
          htmlMessage += `
            <p>Errores encontrados: <strong class="text-danger">${response.errores.length}</strong></p>
            <div class="mt-2" style="max-height: 200px; overflow-y: auto;">
              <ul class="list-unstyled small">
          `;

          response.errores.forEach((error) => {
            htmlMessage += `<li class="text-danger mb-1">${error}</li>`;
          });

          htmlMessage += `
              </ul>
            </div>
          `;
        }

        htmlMessage += `</div>`;

        mostrarMensajeResultado(
          response.success ? "Proceso completado" : "Proceso con observaciones",
          htmlMessage,
          response.success ? "success" : "warning"
        );
        initConLista();
      },
      error: function (xhr, status, error) {
        loadingSwal.close();
        mostrarMensajeResultado(
          "Error",
          `<div class="text-start">
            <p class="text-danger">Error al procesar los productos del laboratorio:</p>
            <p>${error}</p>
          </div>`,
          "error"
        );
      },
    });
  });
  $("#btnBuscarProducto").on("click", function () {
    buscarProductos();
  });
  $("#buscarProductoInput").on("keypress", function (e) {
    if (e.which === 13) {
      currentPage = 1;
      buscarProductos();
    }
  });
  $("#modalTraerLista").on("show.bs.modal", function () {
    cargarListaProductos();
  });

  // Eventos de Excel
  $(document).on("click", "#btnProcesarExcel", function () {
    const fileInput = $("#formFile")[0];

    if (fileInput.files.length === 0) {
      Swal.fire({
        title: "Archivo requerido",
        text: "Por favor seleccione un archivo Excel",
        icon: "warning",
        confirmButtonText: "Entendido",
      });
      return;
    }

    const file = fileInput.files[0];
    const formData = new FormData();
    formData.append("excelFile", file);
    formData.append("codinvalm", codinvalm);

    const loadingSwal = Swal.fire({
      title: "Procesando archivo",
      html: "Por favor espere mientras se procesa el Excel...",
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      },
    });

    $.ajax({
      url: "ExcelXListPro",
      type: "POST",
      data: formData,
      processData: false,
      contentType: false,
      success: function (response) {
        loadingSwal.close();

        let htmlMessage = `
      <div class="text-start">
          <p>Total de SKUs procesados: <strong>${response.total}</strong></p>
          <p>SKUs agregados exitosamente: <strong class="text-success">${response.exitosos}</strong></p>
  `;

        if (response.errores && response.errores.length > 0) {
          htmlMessage += `
          <p>Errores encontrados: <strong class="text-danger">${response.errores.length}</strong></p>
          <div class="mt-2" style="max-height: 200px; overflow-y: auto;">
              <ul class="list-unstyled small">
      `;

          response.errores.forEach((error) => {
            htmlMessage += `<li class="text-danger mb-1">${error}</li>`;
          });

          htmlMessage += `
              </ul>
          </div>
      `;
        }

        htmlMessage += `</div>`;

        Swal.fire({
          title: response.success
            ? "Proceso completado"
            : "Proceso con observaciones",
          html: htmlMessage,
          icon: response.success ? "success" : "warning",
          confirmButtonText: "Entendido",
          width: "600px",
        });

        initConLista();
      },
      error: function (xhr, status, error) {
        loadingSwal.close();
        Swal.fire({
          title: "Error",
          text: "Error al enviar el archivo: " + error,
          icon: "error",
          confirmButtonText: "Entendido",
        });
      },
    });
  });
  $(document).on("click", "#btnProcesarBarrido", function () {
    const loadingSwal = Swal.fire({
      title: "Procesando barrido",
      html: "Por favor espere mientras se procesa el barrido...",
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      },
    });

    $.ajax({
      url: "barrido",
      type: "POST",
      data: { codinvalm: codinvalm },
      success: function (response) {
        loadingSwal.close();

        let htmlMessage = `
      <div class="text-start">
          <p>Total de productos procesados: <strong>${response.total}</strong></p>
          <p>Productos agregados exitosamente: <strong class="text-success">${response.exitosos}</strong></p>
  `;

        if (response.errores && response.errores.length > 0) {
          htmlMessage += `
          <p>Errores encontrados: <strong class="text-danger">${response.errores.length}</strong></p>
          <div class="mt-2" style="max-height: 200px; overflow-y: auto;">
              <ul class="list-unstyled small">
      `;

          response.errores.forEach((error) => {
            htmlMessage += `<li class="text-danger mb-1">${error}</li>`;
          });

          htmlMessage += `
              </ul>
          </div>
      `;
        }

        htmlMessage += `</div>`;

        Swal.fire({
          title: response.success
            ? "Proceso completado"
            : "Proceso con observaciones",
          html: htmlMessage,
          icon: response.success ? "success" : "warning",
          confirmButtonText: "Entendido",
          width: "600px",
        });

        initConLista();
      },
      error: function (xhr, status, error) {
        loadingSwal.close();
        Swal.fire({
          title: "Error",
          text: "Error al enviar el archivo: " + error,
          icon: "error",
          confirmButtonText: "Entendido",
        });
      },
    });
  });

  // Funciones de autoguardado

  $("#contenido").on("load", function () {
    clearTimeout(primerauto);
    if (segundotempo) {
      clearTimeout(segundotempo);
    }
    console.log("Temporizador limpiado antes de descargar la página.");
  });
});
