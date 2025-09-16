$(document).ready(() => {
  // Objeto principal de la aplicación
  const PickingApp = {
    // Configuración inicial
    config: {
      initialized: false,
      currentTable: null,
      fecini: null,
      ubicacionesEnUso: {},
      ubicacionesDisponibles: [],
      cajaCounter: 1,
      botonchk: true,
      activo: "N",
    },

    // Inicialización principal
    init: function () {
      console.log("Iniciando aplicación Picking...");
      this.cleanUp();
      this.initSession();
      this.initDataTable();
      this.bindEvents();
      this.updateTitle();
      this.config.initialized = true;
      console.log("Aplicación inicializada correctamente");
    },

    // Limpieza completa
    cleanUp: function () {
      console.log("Realizando limpieza completa...");

      // 1. Destruir DataTable si existe
      if ($.fn.DataTable.isDataTable("#picking-table")) {
        try {
          this.config.currentTable.destroy(true);
          console.log("DataTable destruida correctamente");
        } catch (e) {
          console.error("Error al destruir DataTable:", e);
        }
        $("#picking-table").empty();
      }

      // 2. Limpiar otros elementos
      $("#cajas-container").empty();
      $(".modal").modal("hide");

      // 3. Resetear configuración
      this.config = {
        initialized: false,
        currentTable: null,
        fecini: null,
        ubicacionesEnUso: {},
        ubicacionesDisponibles: [],
        cajaCounter: 1,
        botonchk: true,
        activo: "N",
      };

      // 4. Remover eventos
      $(document).off(".picking");
      $("#picking-table").off();
      $(window).off(".picking");
    },

    // Inicializar sesión
    initSession: function () {
      console.log("Validando sesión...");
      $.fn.validarSession();
    },

    // Actualizar título
    updateTitle: function () {
      $("#titulo, #titulo2").text(
        "Picking y Despacho: " + sessionStorage.getItem("pik_destino")
      );
    },

    // Inicializar DataTable
    initDataTable: function () {
      console.log("Inicializando DataTable...");

      const tableConfig = {
        destroy: true,
        processing: true,
        serverSide: false,
        stateSave: false,
        paging: false,
        searching: true,
        info: false,
        ajax: {
          url: "picking",
          type: "GET",
          data: (d) => ({
            opcion: 36,
            fechainicio: sessionStorage.getItem("pik_fecini"),
            fechafin: sessionStorage.getItem("pik_fecfin"),
            sec: sessionStorage.getItem("pik_sec"),
            siscod: sessionStorage.getItem("pik_siscod"),
            orden: sessionStorage.getItem("orden"),
            _: Date.now(),
          }),
          cache: false,
          error: (xhr, error, thrown) => {
            console.error("Error en AJAX:", xhr, error, thrown);
            if (xhr.responseJSON?.mensaje === "nosession") {
              $.fn.validarSession();
            } else {
              alert("Error al cargar los datos. Por favor recargue la página.");
            }
          },
        },
        columns: [
          { data: "codpro" },
          { data: "despro" },
          { data: "codlab" },
          { data: "ubipro", render: (data) => data || "Recepcion" },
          { data: "cante", render: (data) => data || "" },
          { data: "cantf", render: (data) => data || "" },
          {
            data: null,
            render: (data, type, row) => this.renderEAN13Button(row),
            orderable: true,
          },
          {
            data: null,
            render: (data, type, row) => this.renderPickButton(row),
            orderable: true,
          },
        ],
        createdRow: (row, data) => {
          if (data.check2 === "S" && data.check1 === "S" && data.equis <= 0) {
            $(row).addClass("table-warning");
          }
        },
        initComplete: function (settings, json) {
          console.log("DataTable inicializada correctamente");
          this.config.fecini = json?.fecha || null;
          if (this.config.fecini) {
            sessionStorage.setItem("pik_fecinicheck", this.config.fecini);
          }

          $("#picking-table_processing").hide();
        },
        error: function (xhr, error, thrown) {
          console.log("Error en DataTable", error, thrown);
          $("#picking-table_processing").hide();
          if (xhr.responseJSON?.mensaje === "nosession") {
            $.fn.validarSession();
          } else {
            alert("Error al cargar los datos. Por favor recargue la pagina.");
          }
        },
      };

      this.config.currentTable = $("#picking-table").DataTable(tableConfig);
      $("#picking-table_processing").hide();
    },

    // Renderizar botón EAN13
    renderEAN13Button: function (row) {
      const btnClass = row.check1 === "S" ? "btn-info" : "btn-danger";
      const btnText = row.check1 === "S" ? "ean13 √" : "ean13 X";

      return `
        <button class="btn btn-sm btn-checkean13 ${btnClass}" 
          data-fecven="${row.fecven || ""}"
          data-check1="${row.check1 || "N"}"
          data-codalt="${row.codalt || ""}"
          data-codpro="${row.codpro || ""}"
          data-codlot="${row.codlot || ""}"
          data-invnum="${row.secuencia || ""}"
          data-ubipro="${row.ubipro || ""}">
          ${btnText}
        </button>`;
    },

    // Renderizar botón Pick
    renderPickButton: function (row) {
      const btnClass = row.check2 === "S" ? "btn-info" : "btn-danger";
      const btnText = row.check2 === "S" ? "Pick √" : "Pick X";
      const statusIcon = row.equis > 0 ? "✖" : "✔";

      return `
        <button class="btn btn-sm btn-checkpick ${btnClass}"
          data-check2="${row.check2 || "N"}"
          data-codpro="${row.codpro || ""}"
          data-codlot="${row.codlot || ""}"
          data-invnum="${row.secuencia || ""}"
          data-ubipro="${row.ubipro || ""}">
          ${btnText} ${statusIcon}
        </button>`;
    },

    // Configurar eventos
    bindEvents: function () {
      console.log("Configurando eventos...");

      // Limpiar eventos antiguos primero
      $(document).off(".picking");
      $("#picking-table").off();
      $(window).off(".picking");

      // Navegación atrás
      $(window).on("pageshow.picking", (event) => {
        if (
          event.originalEvent.persisted ||
          performance.navigation?.type === 2
        ) {
          console.log("Detectada navegación atrás - Reiniciando aplicación...");
          this.init();
        }
      });

      // Botón de retroceso
      $("#back-button").on("click.picking", () => {
        console.log("Click en botón retroceso - Navegando hacia atrás...");
        this.cleanUp();
        $("#contenido").load("pickingListDestinos.html");
      });

      // Botón de etiqueta
      $("#etiqueta-button").on("click.picking", () => {
        $("#impresionModal").modal("show");
        $("#impresion-input").val("").focus();
      });

      // Eventos de la tabla
      $("#picking-table")
        .on("click.picking", ".btn-checkpick", (e) =>
          this.handleCheckPick(e.currentTarget)
        )
        .on("click.picking", ".btn-checkean13", (e) =>
          this.handleCheckEAN13(e.currentTarget)
        );

      // Eventos de teclado
      $(document)
        .on("keypress.picking", ".ubicacion-input", (e) => {
          if (e.which === 13) this.handleUbicacionInput(e);
        })
        .on("keypress.picking", "#impresion-input", (e) => {
          if (e.which === 13) this.imprimir();
        })
        .on("keypress.picking", "#ean13-input", (e) => {
          if (e.which === 13) this.validarEAN13();
        });

      // Eventos de modales
      $("#validate-impresion").on("click.picking", () => this.imprimir());
      $("#validate-ean13").on("click.picking", () => this.validarEAN13());
      $("#cerrarean13").on("click.picking", () =>
        $("#ean13Modal").modal("hide")
      );
      $(".cerrarmodal").on("click.picking", () =>
        $("#impresionModal").modal("hide")
      );

      // Evento de scroll
      $(window).on("scroll.picking", () => this.handleWindowScroll());

      // Evento de cambio de orden
      $("#orden").on("change.picking", () => this.handleOrdenChange());

      // Evento de confirmación de picking
      $("#confpick").on("click.picking", () => this.handleConfirmPick());

      // Evento para agregar caja
      $("#add-caja").on("click.picking", () => this.handleAddCaja());
    },

    // Manejar scroll de ventana
    handleWindowScroll: function () {
      const backButton = document.getElementById("back-button");
      const etiqueta = document.getElementById("etiqueta-button");
      const cardHeader = $("#card-header");
      const fixedTitle = $("#fixed-title");

      if (
        document.body.scrollTop > 50 ||
        document.documentElement.scrollTop > 50
      ) {
        etiqueta.style.top = "60px";
        backButton.style.top = "20px";
        fixedTitle.css({
          display: "block",
          width: cardHeader.outerWidth(),
        });
      } else {
        backButton.style.top = "100px";
        etiqueta.style.top = "140px";
        fixedTitle.css("display", "none");
      }
    },

    // Manejar input de ubicación
    handleUbicacionInput: function (e) {
      if (e.which === 13 || e.key === "Enter") {
        e.preventDefault();
        const $input = $(e.currentTarget);
        const ubicacionIngresada = $input.val().trim();
        const $container = $input.closest(".caja-container");
        const cajaId = $container.data("caja-id");

        console.log(
          `Validando ubicación para caja ${cajaId}:`,
          ubicacionIngresada
        );

        if (!ubicacionIngresada) {
          this.showAlert("Error", "Debe ingresar una ubicación", "error");
          return;
        }

        const ubicacionEncontrada = this.findUbicacion(ubicacionIngresada);
        if (!ubicacionEncontrada) {
          this.clearUbicacionFields($container);
          return;
        }

        if (this.isUbicacionEnUso(ubicacionEncontrada.ubicacion, cajaId)) {
          this.showAlert(
            "Ubicación en uso",
            "Esta ubicación ya está asignada a otra caja",
            "error"
          );
          this.clearUbicacionFields($container);
          return;
        }

        const entero = this.parseNumeroSeguro(ubicacionEncontrada.cante);
        const fraccion = this.parseNumeroSeguro(ubicacionEncontrada.cantf);

        if (entero <= 0 && fraccion <= 0) {
          this.showAlert(
            "Ubicación inválida",
            "La ubicación no tiene cantidades válidas (entero o fracción)",
            "error"
          );
          this.clearUbicacionFields($container);
          return;
        }

        this.registerUbicacionForCaja(cajaId, {
          ...ubicacionEncontrada,
          entero: entero,
          fraccion: fraccion,
        });

        this.updateUbicacionFields($container, {
          ubicacion: ubicacionEncontrada.ubicacion,
          entero: entero,
          fraccion: fraccion,
        });
      }
    },

    // Funciones auxiliares para manejo de ubicaciones
    parseNumeroSeguro: function (valor) {
      const num = parseFloat(valor);
      return isNaN(num) ? 0 : Math.max(0, num);
    },

    updateUbicacionFields: function ($container, ubicacion) {
      const $entero = $container.find(".ubicacion-entero");
      const $fraccion = $container.find(".ubicacion-fraccion");
      const cajaId = $container.data("caja-id");

      const enteroDisabled = $entero.prop("disabled");
      const fraccionDisabled = $fraccion.prop("disabled");

      $entero.prop("disabled", false);
      $fraccion.prop("disabled", false);

      $entero.val(ubicacion.entero.toString());
      $fraccion.val(ubicacion.fraccion.toString());

      if (enteroDisabled) $entero.prop("disabled", true);
      if (fraccionDisabled) $fraccion.prop("disabled", true);

      $container
        .find(".ubicacion-input")
        .removeClass("is-invalid")
        .addClass("is-valid");

      this.updateCantidadesDisponibles();
    },

    clearUbicacionFields: function ($container, releaseLocation = true) {
      const cajaId = $container.data("caja-id");

      if (releaseLocation && cajaId) {
        this.releaseUbicacionForCaja(cajaId);
      }

      const $inputUbicacion = $container.find(".ubicacion-input");
      const $entero = $container.find(".ubicacion-entero");
      const $fraccion = $container.find(".ubicacion-fraccion");

      const ubicacionDisabled = $inputUbicacion.prop("disabled");
      const enteroDisabled = $entero.prop("disabled");
      const fraccionDisabled = $fraccion.prop("disabled");

      $inputUbicacion
        .prop("disabled", false)
        .val("")
        .removeClass("is-valid is-invalid");

      $entero.prop("disabled", false).val("");
      $fraccion.prop("disabled", false).val("");

      if (ubicacionDisabled) $inputUbicacion.prop("disabled", true);
      if (enteroDisabled) $entero.prop("disabled", true);
      if (fraccionDisabled) $fraccion.prop("disabled", true);

      if (releaseLocation) {
        this.updateCantidadesDisponibles();
      }

      $inputUbicacion.focus();
    },

    findUbicacion: function (ubicacionCodigo) {
      if (
        !this.config.ubicacionesDisponibles ||
        !Array.isArray(this.config.ubicacionesDisponibles)
      ) {
        this.showAlert(
          "Error",
          "Las ubicaciones no están cargadas correctamente",
          "error"
        );
        return null;
      }

      const ubicacion = this.config.ubicacionesDisponibles.find(
        (ubi) => ubi && ubi.ubicacion === ubicacionCodigo
      );

      if (!ubicacion) {
        this.showAlert("No encontrada", "Ubicación no existe", "error");
        return null;
      }

      return ubicacion;
    },

    isUbicacionEnUso: function (ubicacionCodigo, currentCajaId) {
      return Object.values(this.config.ubicacionesEnUso).some(
        (uso) =>
          uso.ubicacion === ubicacionCodigo && uso.cajaId !== currentCajaId
      );
    },

    registerUbicacionForCaja: function (cajaId, ubicacion) {
      this.releaseUbicacionForCaja(cajaId);

      this.config.ubicacionesEnUso[cajaId] = {
        ubicacion: ubicacion.ubicacion,
        enteroOriginal: ubicacion.entero,
        fraccionOriginal: ubicacion.fraccion,
        enteroDisponible: ubicacion.entero,
        fraccionDisponible: ubicacion.fraccion,
        cajaId: cajaId,
      };

      console.log(
        `Ubicación registrada para caja ${cajaId}:`,
        ubicacion.ubicacion
      );
    },

    releaseUbicacionForCaja: function (cajaId) {
      if (this.config.ubicacionesEnUso[cajaId]) {
        console.log(`Liberando ubicación de caja ${cajaId}`);
        delete this.config.ubicacionesEnUso[cajaId];
      }
    },

    updateCantidadesDisponibles: function () {
      console.log("Actualizando cantidades disponibles...");

      Object.keys(this.config.ubicacionesEnUso).forEach((cajaId) => {
        const uso = this.config.ubicacionesEnUso[cajaId];
        uso.enteroDisponible = uso.enteroOriginal;
        uso.fraccionDisponible = uso.fraccionOriginal;
      });

      $(".caja-container").each((_, container) => {
        const $container = $(container);
        const cajaId = $container.data("caja-id");
        const uso = this.config.ubicacionesEnUso[cajaId];

        if (!uso) return;

        const cantE = this.parseNumeroSeguro(
          $container.find(".cante-input").val()
        );
        const cantF = this.parseNumeroSeguro(
          $container.find(".cantf-input").val()
        );

        uso.enteroDisponible -= cantE;
        uso.fraccionDisponible -= cantF;

        $container.find(".ubicacion-entero").val(uso.enteroDisponible);
        $container.find(".ubicacion-fraccion").val(uso.fraccionDisponible);
      });
    },

    // Mostrar alerta
    showAlert: function (title, message, type = "error") {
      console.log(`[${type}] ${title}: ${message}`);
      alert(`${title}\n${message}`);
    },

    // Manejar clic en botón Pick
    handleCheckPick: function (button) {
      const $button = $(button);
      const codpro = $button.data("codpro");
      const codlot = $button.data("codlot");
      const secuencia = $button.data("invnum");
      const siscod = sessionStorage.getItem("pik_siscod");

      console.log(
        `Iniciando picking para: ${codpro} - Lote: ${codlot} - Secuencia: ${secuencia}`
      );

      $.getJSON(
        "picking",
        {
          codlot: codlot,
          codpro: codpro,
          siscod: siscod,
          opcion: 37,
          secuencia: secuencia,
          _: Date.now(),
        },
        (data) => {
          if (data.resultado === "ok") {
            console.log("Datos recibidos:", data);
            if (!data.ubi && !Array.isArray(data.ubi) && !data.ubi.length > 0) {
              this.showAlert(
                "Error",
                "Este producto no tiene ubicaciones disponibles"
              );
            }
            this.config.ubicacionesDisponibles = data.ubi || [];
            this.config.activo = data.data.check2 || "N";
            this.config.cajaCounter = 1;

            this.fillModalData(data.data, codpro);
            this.renderCajas(data.cajas || []);

            this.config.ubicacionesEnUso = {};
            $("#modalPicking").modal("show");
          } else {
            this.showAlert(
              "Error",
              data.mensaje || "Error al obtener los datos"
            );
          }
        }
      ).fail((jqXHR, textStatus) => {
        this.showAlert("Error", "Error en la solicitud: " + textStatus);
      });
    },

    // Llenar datos del modal
    fillModalData: function (data, codpro) {
      $("#caja-producto").text(`${data.despro || ""} (${data.codlab || ""})`);
      $("#codigo").text(codpro);
      $("#Entero").text(data.cante || "0");
      $("#Fraccion").text(data.cantf || "0");
      $("#Fecha").text(data.fecven || "");
      $("#Secuencia").text(data.secuencia || "");
      $("#Lote").text(data.lote || "");

      if (data.check2 === "S") {
        $("#confpick").text("Quitar Check").css("background-color", "#dc3545");
      } else {
        $("#confpick").text("Validar").css("background-color", "#17a2b8");
      }
    },

    // Renderizar cajas
    renderCajas: function (cajas) {
      const container = $("#cajas-container").empty();
      console.log(`Renderizando ${cajas.length} cajas...`);

      if (cajas && cajas.length > 0) {
        cajas.forEach((item, index) => {
          container.append(
            this.generateCajaHtml(
              this.config.cajaCounter,
              item,
              this.config.activo,
              index === 0
            )
          );
          this.config.cajaCounter++;
        });
      } else {
        container.append(
          this.generateCajaHtml(1, {}, this.config.activo, true)
        );
      }

      $("#add-caja").prop("disabled", this.config.activo === "S");
      this.initCajasEvents();
    },

    // Generar HTML de caja
    generateCajaHtml: function (
      cajaId,
      item = {},
      activo = "S",
      esPrimera = false
    ) {
      const disabledAttr = activo === "S" ? "disabled" : "";
      const cajaValue = item.caja || "";
      const canteValue = item.cante || "";
      const cantfValue = item.cantf || "";

      return `
<div class="caja-container mb-3 p-3 border rounded" data-caja-id="${cajaId}">
  <div class="row mb-2">
    <div class="col-12">
      <div class="form-group">
        <label for="ubicacion-${cajaId}" class="form-label">Ubicación</label>
        <input type="text" class="form-control ubicacion-input" id="ubicacion-${cajaId}" 
          value="${item.ubicacion || ""}" ${disabledAttr}>
      </div>
    </div>
    <input type="number" class="ubicacion-entero" id="ubicacion-entero-${cajaId}" 
          readonly style="display: none;">
    <input type="number" class="ubicacion-fraccion" id="ubicacion-fraccion-${cajaId}" 
          readonly style="display: none;">
  </div>
  <div class="row mb-2">
    <div class="col-4">
      <div class="form-group">
        <label for="caja-${cajaId}" class="form-label">${
        esPrimera ? "Caja" : "Caja " + cajaId
      }</label>
        <input type="text" class="form-control caja-input" id="caja-${cajaId}" 
          value="${cajaValue}" ${disabledAttr}>
      </div>
    </div>
    <div class="col-4">
      <div class="form-group">
        <label for="cante-${cajaId}" class="form-label">Cant. E</label>
        <input type="number" class="form-control cante-input" id="cante-${cajaId}" 
          value="${canteValue}" ${disabledAttr}>
      </div>
    </div>
    <div class="col-4">
      <div class="form-group">
        <label for="cantf-${cajaId}" class="form-label">Cant. F</label>
        <input type="number" class="form-control cantf-input" id="cantf-${cajaId}" 
          value="${cantfValue}" ${disabledAttr}>
      </div>
    </div>
  </div>
  <div class="row">
    <div class="col-12 text-end">
      <button type="button" class="btn btn-danger btn-sm remove-caja" 
        data-caja-id="${cajaId}" ${disabledAttr}>
        Eliminar Caja
      </button>
    </div>
  </div>
</div>`;
    },

    // Inicializar eventos de cajas
    initCajasEvents: function () {
      console.log("Inicializando eventos de cajas...");

      $(document)
        .off("click.picking-cajas", ".remove-caja")
        .on("click.picking-cajas", ".remove-caja", (e) => {
          this.removeCaja(e.currentTarget);
        });

      $(document)
        .off("change.picking-cajas", ".cante-input, .cantf-input")
        .on("change.picking-cajas", ".cante-input, .cantf-input", (e) => {
          this.validateCantidades(e.currentTarget);
        });

      $(document)
        .off("change.picking-cajas", ".caja-input")
        .on("change.picking-cajas", ".caja-input", (e) => {
          this.validateNombreCajaUnico(e.currentTarget);
        });
    },

    // Eliminar caja
    removeCaja: function (button) {
      const $container = $(button).closest(".caja-container");
      const cajaId = $container.data("caja-id");

      if (!confirm("¿Está seguro de eliminar esta caja?")) return;

      this.releaseUbicacionForCaja(cajaId);
      this.updateCantidadesDisponibles();

      $container.slideUp(300, () => {
        $container.remove();
        this.updateLabels();
      });
    },

    // Validar nombres de caja únicos
    validateNombreCajaUnico: function (input) {
      const $input = $(input);
      const currentValue = $input.val();
      if (!currentValue) return;

      let duplicateFound = false;
      const currentId = $input.closest(".caja-container").data("caja-id");

      $(".caja-input").each(function () {
        const $thisInput = $(this);
        if (
          $thisInput.val() === currentValue &&
          $thisInput.closest(".caja-container").data("caja-id") !== currentId
        ) {
          duplicateFound = true;
          return false;
        }
      });

      if (duplicateFound) {
        this.showAlert(
          "Error",
          "Esta caja ya está siendo utilizada en este producto.",
          "error"
        );
        $input.val("").focus();
      }
    },

    // Validar cantidades
    validateCantidades: function (input) {
      const $input = $(input);
      const $container = $input.closest(".caja-container");
      const cajaId = $container.data("caja-id");
      let value = this.parseNumeroSeguro($input.val());

      if (value < 0) {
        this.showAlert("Error", "La cantidad no puede ser negativa", "error");
        $input.val("");
        return false;
      }

      const uso = this.config.ubicacionesEnUso[cajaId];
      const isEntero = $input.hasClass("cante-input");

      if (uso) {
        const disponible = isEntero
          ? uso.enteroDisponible
          : uso.fraccionDisponible;
        if (value > disponible) {
          this.showAlert(
            "Límite excedido",
            `Cantidad ${
              isEntero ? "entera" : "fracción"
            } no puede superar ${disponible} disponible(s)`,
            "error"
          );
          $input.val(disponible);
          return false;
        }
      }

      if (uso) {
        this.updateCantidadesDisponibles();
      }

      return true;
    },

    // Manejar agregar caja
    handleAddCaja: function () {
      const nuevoId = this.config.cajaCounter++;
      $("#cajas-container").append(
        this.generateCajaHtml(nuevoId, {}, this.config.activo)
      );
      this.updateLabels();
    },

    // Actualizar etiquetas
    updateLabels: function () {
      $(".caja-container").each(function (index) {
        const label = $(this).find(".form-label").first();
        label.text(index === 0 ? "Cajita" : `Caja ${index + 1}`);
      });
    },

    // Validar EAN13
    validarEAN13: function () {
      const codpro = $("#modal-codpro").text();
      const secuencia = $("#modal-secuencia").text();
      const lote = $("#modal-lote").text();
      const inputEAN13 = $("#ean13-input").val();
      const button = $(
        `button[data-codpro="${codpro}"][data-invnum="${secuencia}"][data-codlot="${lote}"]`
      );
      const codalt = String(button.data("codalt"));

      console.log(`Validando EAN13: ${inputEAN13} vs ${codalt}`);

      if (
        (inputEAN13 === codalt && inputEAN13 !== "") ||
        $("#validate-ean13").text() === "Quitar Check"
      ) {
        $.getJSON(
          "picking",
          {
            opcion: 35,
            codpro: codpro,
            secuencia: secuencia,
            codlot: lote,
            _: Date.now(),
          },
          (response) => {
            if (response.resultado === "ok") {
              $("#ean13Modal").modal("hide");
              this.config.currentTable.ajax.reload();
              $("#ean13-input").val("");
            } else if (response.mensaje === "nosession") {
              $.fn.validarSession();
            } else {
              this.showAlert("Error", response.mensaje || "Error del servidor");
            }
          }
        );
      } else {
        this.showAlert("Error", "El código de barras no coincide");
      }
    },

    // Manejar clic en botón EAN13
    handleCheckEAN13: function (button) {
      const $button = $(button);
      const row = $button.closest("tr");
      const codpro = $button.data("codpro");
      const secuencia = $button.data("invnum");
      const lote = $button.data("codlot");
      const ubipro = $button.data("ubipro");
      const despro = row.find("td").eq(1).text();
      const codalt = $button.data("codalt");
      const fecven = $button.data("fecven");

      console.log(`Mostrando modal EAN13 para: ${codpro} - Lote: ${lote}`);

      $("#modal-codalt").text(
        codalt === "undefined"
          ? "Este producto no tiene registrado cod. Barras."
          : codalt
      );
      $("#modal-codpro").text(codpro);
      $("#modal-secuencia").text(secuencia);
      $("#modal-lote").text(lote);
      $("#modal-ubipro").text(ubipro !== "undefined" ? ubipro : "");
      $("#modal-despro").text(despro);
      $("#modal-fecven").text(fecven);

      if ($button.data("check1") === "S") {
        $("#validate-ean13")
          .text("Quitar Check")
          .css("background-color", "#dc3545");
      } else {
        $("#validate-ean13").text("Validar").css("background-color", "#17a2b8");
      }

      $("#ean13Modal").modal("show");
      $("#ean13-input").val("").focus();
    },

    // Imprimir
    imprimir: function () {
      const val = String($("#impresion-input").val().trim());
      const sec = sessionStorage.getItem("pik_sec");
      const siscod = sessionStorage.getItem("pik_siscod");

      console.log(`Solicitando impresión para bulto: ${val}`);

      if (val) {
        $.getJSON(
          "picking",
          {
            opcion: 20,
            ord: sec,
            caja: val,
            siscod: siscod,
            _: Date.now(),
          },
          (response) => {
            if (response.resultado === "ok") {
              if (response.data.length > 0) {
                $("#impresion-input").val("");
                this.generatePDF(response.data);
              } else {
                this.showAlert("Advertencia", "No se encontró el bulto");
              }
            } else if (response.mensaje === "nosession") {
              $.fn.validarSession();
            } else {
              this.showAlert("Error", response.mensaje || "Error del servidor");
            }
          }
        );
      }
    },

    // Manejar cambio de orden
    handleOrdenChange: function () {
      const nuevaOrden = $("#orden").val();
      console.log(`Cambiando orden a: ${nuevaOrden}`);
      sessionStorage.setItem("orden", nuevaOrden);
      sessionStorage.setItem("scroll", 0);
      $("#contenido").load("pickingDetalle.html");
    },

    // Manejar confirmación de picking - CORREGIDO
    handleConfirmPick: function () {
      if (!this.config.botonchk) return;

      const $button = $("#confpick");
      $button.prop("disabled", true);

      try {
        this.config.botonchk = false;
        const isQuitarCheck = $("#confpick").text() === "Quitar Check";

        const codlot = $("#Lote").text();
        const codpro = $("#codigo").text();
        const secuencia = $("#Secuencia").text();
        const fecini = sessionStorage.getItem("pik_fecinicheck");

        if (isQuitarCheck) {
          this.sendPickData(codlot, codpro, secuencia, fecini, 0, 0, []);
        } else {
          // CAMBIO: Usar el nuevo método que garantiza cajas completamente válidas
          this.validateAndProcessBoxes(codlot, codpro, secuencia, fecini);
        }
      } catch (error) {
        $button.prop("disabled", false);
        throw error;
      }
    },

    // Método mejorado para recolectar datos (con mejor manejo de valores por defecto)
    collectCajasData: function () {
      console.log("Recolectando datos de cajas...");
      return $(".caja-container")
        .map((_, container) => {
          const $container = $(container);
          const cajaData = {
            caja: $container.find(".caja-input").val()?.trim() || "",
            cantE: $container.find(".cante-input").val()?.trim() || "0",
            cantF: $container.find(".cantf-input").val()?.trim() || "0",
            ubicacion: $container.find(".ubicacion-input").val()?.trim() || "",
          };
          console.log("Datos de caja recolectados:", cajaData);
          return cajaData;
        })
        .get();
    },
    // Método que retorna las cajas que pasan TODOS los filtros (incluyendo suma correcta)
    getFullyValidatedBoxes: function (cajasData, cante, cantf) {
      console.log("Obteniendo cajas completamente validadas:", cajasData);

      // Verificar que haya al menos una caja
      if (!this.hasMinimumBoxes(cajasData)) {
        return null;
      }

      // Obtener cajas válidas individualmente
      const cajasValidas = this.getValidBoxes(cajasData);

      if (cajasValidas.length === 0) {
        this.showAlert("Error", "No hay cajas válidas para procesar", "error");
        return null;
      }

      // Validar que las cantidades sumen correctamente
      if (
        !this.validateQuantities(cajasValidas, parseInt(cante), parseInt(cantf))
      ) {
        return null; // Las cajas individualmente válidas no suman correctamente
      }

      // Si llegamos aquí, las cajas son válidas Y suman correctamente
      return cajasValidas;
    },

    // Verificar si hay al menos una caja
    hasMinimumBoxes: function (cajasData) {
      if (cajasData.length === 0) {
        this.showAlert("Error", "Debe agregar al menos una caja", "error");
        return false;
      }
      return true;
    },

    // Validar datos individuales de una caja
    isValidBox: function (caja, index) {
      // Validar nombre de caja (solo si hay más de una)
      if (index !== undefined && (!caja.caja || caja.caja.trim() === "")) {
        this.showAlert(
          `Error en Caja ${index + 1}`,
          "El nombre de la caja es requerido",
          "error"
        );
        return false;
      }

      // Validar cantidades
      const cantE = parseInt(caja.cantE) || 0;
      const cantF = parseInt(caja.cantF) || 0;

      if (cantE <= 0 && cantF <= 0) {
        this.showAlert(
          `Error en Caja ${index !== undefined ? index + 1 : ""}`,
          "Debe ingresar al menos una cantidad (entero o fracción)",
          "error"
        );
        return false;
      }

      // Validar ubicación si es necesario
      if (
        this.config.activo === "N" &&
        (!caja.ubicacion || caja.ubicacion.trim() === "")
      ) {
        this.showAlert(
          `Error en Caja ${index !== undefined ? index + 1 : ""}`,
          "Debe seleccionar una ubicación",
          "error"
        );
        return false;
      }

      return true;
    },

    // Obtener solo las cajas que tienen datos válidos
    getValidBoxes: function (cajasData) {
      const cajasValidas = [];

      for (let i = 0; i < cajasData.length; i++) {
        const caja = cajasData[i];

        // Para una sola caja, no validamos el nombre
        const indexToValidate = cajasData.length === 1 ? undefined : i;

        if (this.isValidBox(caja, indexToValidate)) {
          cajasValidas.push({
            ...caja,
            cantE: parseInt(caja.cantE) || 0,
            cantF: parseInt(caja.cantF) || 0,
          });
        }
      }

      return cajasValidas;
    },

    // Calcular la suma total de cantidades de las cajas válidas
    calculateTotalQuantities: function (cajasValidas) {
      return cajasValidas.reduce(
        (totales, caja) => {
          totales.totalE += caja.cantE;
          totales.totalF += caja.cantF;
          return totales;
        },
        { totalE: 0, totalF: 0 }
      );
    },

    // Validar que las cantidades coincidan con las asignadas
    validateQuantities: function (cajasValidas, cante, cantf) {
      const { totalE, totalF } = this.calculateTotalQuantities(cajasValidas);

      console.log(
        `Validando cantidades: Total E=${totalE}, Asignado E=${cante}, Total F=${totalF}, Asignado F=${cantf}`
      );

      if (
        parseInt(totalE) > parseInt(cante) ||
        parseInt(totalF) > parseInt(cantf)
      ) {
        this.showAlert(
          "Error de Cantidades",
          `Las cantidades no coinciden con las asignadas.\n` +
            `Enteros: ${totalE} (ingresado) vs ${cante} (asignado)\n` +
            `Fracciones: ${totalF} (ingresado) vs ${cantf} (asignado)`,
          "error"
        );
        return false;
      }

      return true;
    },

    // Método principal para validar y procesar cajas - CORREGIDO
    validateAndProcessBoxes: function (codlot, codpro, secuencia, fecini) {
      const cajasData = this.collectCajasData();
      const cante = $("#Entero").text();
      const cantf = $("#Fraccion").text();

      // Obtener cajas que pasan TODOS los filtros (incluyendo suma correcta)
      const cajasCompletamenteValidas = this.getFullyValidatedBoxes(
        cajasData,
        cante,
        cantf
      );

      if (cajasCompletamenteValidas !== null) {
        // Garantizado: estas cajas son válidas Y suman correctamente
        this.sendPickData(
          codlot,
          codpro,
          secuencia,
          fecini,
          cante,
          cantf,
          cajasCompletamenteValidas
        );
      } else {
        this.config.botonchk = true;
        // Rehabilitar el botón en caso de error
        $("#confpick").prop("disabled", false);
      }
    },

    // Enviar datos de picking
    sendPickData: function (
      codlot,
      codpro,
      secuencia,
      fecha,
      cante,
      cantf,
      cajasData
    ) {
      const url = `picking?codlot=${codlot}&codpro=${codpro}&opcion=38&secuencia=${secuencia}&fecha=${fecha}&cante=${cante}&cantf=${cantf}`;
      const $button = $("#confpick");

      // Limpia los datos antes de enviar (convierte "" a null y asegura los tipos)
      const cleanedCajasData = cajasData.map((caja) => ({
        caja: caja.caja || "",
        // mandar convertido en string
        cantE: caja.cantE && caja.cantE !== "" ? caja.cantE.toString() : "",
        cantF: caja.cantF && caja.cantF !== "" ? caja.cantF.toString() : "",
        ubicacion: caja.ubicacion || "",
      }));

      $.ajax({
        url: url,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(cleanedCajasData),
        dataType: "json",
        success: (data) => {
          if (data.resultado === "ok") {
            $button.prop("disabled", false);
            sessionStorage.setItem("pik_fecinicheck", data.fecha);
            $("#modalPicking").modal("hide");
            this.config.currentTable.ajax.reload();
          } else if (data.mensaje === "yaenviado") {
            this.showAlert("Error", "Esta OT ya ha sido cerrada");
          } else {
            this.showAlert(
              "Error",
              data.mensaje || "Ocurrió un problema, vuelve a intentarlo"
            );
          }
          this.config.botonchk = true;
        },
        error: (xhr, status, error) => {
          $button.prop("disabled", false);
          this.showAlert("Error", "Ocurrió un problema, vuelve a intentarlo");
          this.config.botonchk = true;
        },
      });
    },

    // Generar PDF con las etiquetas
    generatePDF: async function (products) {
      console.log("Generando PDF para productos:", products);

      try {
        // Verificar si PDFLib está disponible
        if (typeof PDFLib === "undefined") {
          await this.loadPDFLibrary();
        }

        const { PDFDocument, rgb, StandardFonts } = PDFLib;
        const pdfDoc = await PDFDocument.create();

        // Configuración de dimensiones (en puntos)
        const mmToPoints = (mm) => mm * 2.835;
        const config = {
          pageWidth: mmToPoints(210), // A4 width
          pageHeight: mmToPoints(297), // A4 height
          margin: mmToPoints(10),
          labelWidth: mmToPoints(90),
          labelHeight: mmToPoints(50),
          labelsPerRow: 2,
          labelsPerPage: 8,
          fontSize: {
            large: 12,
            medium: 10,
            small: 8,
          },
        };

        let currentPage;
        let currentLabelIndex = 0;

        // Procesar cada producto
        for (const product of products) {
          if (currentLabelIndex % config.labelsPerPage === 0) {
            // Crear nueva página
            currentPage = pdfDoc.addPage([config.pageWidth, config.pageHeight]);
            currentLabelIndex = 0;
          }

          // Calcular posición de la etiqueta
          const row = Math.floor(
            (currentLabelIndex % config.labelsPerPage) / config.labelsPerRow
          );
          const col = currentLabelIndex % config.labelsPerRow;

          const x = config.margin + col * (config.labelWidth + config.margin);
          const y =
            config.pageHeight - config.margin - (row + 1) * config.labelHeight;

          // Dibujar etiqueta
          this.drawLabel(
            currentPage,
            product,
            x,
            y,
            config.labelWidth,
            config.labelHeight,
            config
          );

          currentLabelIndex++;
        }

        // Guardar PDF
        const pdfBytes = await pdfDoc.save();
        this.downloadPDF(pdfBytes, `etiquetas_${this.getFormattedDate()}.pdf`);
      } catch (error) {
        console.error("Error al generar PDF:", error);
        this.showAlert("Error", "No se pudo generar el PDF: " + error.message);
      }
    },

    // Dibujar etiqueta individual
    drawLabel: function (page, product, x, y, width, height, config) {
      // Cargar fuentes
      const font = page.doc.embedFont(StandardFonts.Helvetica);
      const fontBold = page.doc.embedFont(StandardFonts.HelveticaBold);

      // Dibujar borde de etiqueta
      page.drawRectangle({
        x,
        y,
        width,
        height,
        borderColor: rgb(0, 0, 0),
        borderWidth: 0.5,
      });

      // Nombre del destino (primera línea)
      const nombreDestino =
        product.destino?.split(" ").slice(1).join(" ") || product.destino || "";
      page.drawText(nombreDestino.substring(0, 30), {
        // Limitar longitud
        x: x + 5,
        y: y + height - 15,
        size: config.fontSize.large,
        font: fontBold,
        maxWidth: width - 10,
      });

      // Información de orden
      page.drawText(`Orden: ${product.orden || ""}`, {
        x: x + 5,
        y: y + height - 30,
        size: config.fontSize.medium,
        font: font,
        maxWidth: width - 10,
      });

      // Información de bulto
      page.drawText(`Bulto: ${product.caja || ""}`, {
        x: x + 5,
        y: y + height - 45,
        size: config.fontSize.medium,
        font: font,
        maxWidth: width - 10,
      });
    },

    // Cargar PDF-Lib dinámicamente
    loadPDFLibrary: function () {
      console.log("Cargando PDF-Lib...");
      return new Promise((resolve, reject) => {
        const script = document.createElement("script");
        script.src = "https://unpkg.com/pdf-lib@1.16.0/dist/pdf-lib.min.js";
        script.onload = resolve;
        script.onerror = () => reject(new Error("Error al cargar PDF-Lib"));
        document.head.appendChild(script);
      });
    },

    // Descargar PDF
    downloadPDF: function (pdfBytes, filename) {
      console.log("Descargando PDF...");
      const blob = new Blob([pdfBytes], { type: "application/pdf" });
      const url = URL.createObjectURL(blob);

      const a = document.createElement("a");
      a.href = url;
      a.download = filename;
      document.body.appendChild(a);
      a.click();

      setTimeout(() => {
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
      }, 100);
    },

    // Obtener fecha formateada
    getFormattedDate: function () {
      const now = new Date();
      return [
        now.getFullYear(),
        String(now.getMonth() + 1).padStart(2, "0"),
        String(now.getDate()).padStart(2, "0"),
      ].join("-");
    },
  };

  // Inicializar la aplicación
  PickingApp.init();
});

// Extensión de jQuery para validar sesión
$.fn.validarSession = function () {
  console.log("Validando sesión...");
  $.getJSON("validarsesion", function (data) {
    if (data.resultado !== "ok") {
      console.warn("Sesión no válida, redirigiendo...");
      $(location).attr("href", "index.html");
    }
  }).fail(function () {
    console.error("Error al validar sesión, redirigiendo...");
    $(location).attr("href", "index.html");
  });
};
