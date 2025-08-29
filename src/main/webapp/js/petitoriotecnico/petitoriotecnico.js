$(document).ready(function () {
  // Configuración global de SweetAlert2
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

  // Variables de estado
  let modoActual = "crear"; // 'crear' o 'editar'
  let itemActual = null;
  let productosSeleccionados = [];
  let id = "";

  jQuery.extend(jQuery.fn.dataTableExt.oSort, {
    "natural-asc": function (a, b) {
      return naturalSort(a, b);
    },
    "natural-desc": function (a, b) {
      return naturalSort(a, b) * -1;
    },
  });

  function naturalSort(a, b) {
    const ax = [],
      bx = [];

    a.replace(/(\d+)|(\D+)/g, function (_, $1, $2) {
      ax.push([$1 || Infinity, $2 || ""]);
    });

    b.replace(/(\d+)|(\D+)/g, function (_, $1, $2) {
      bx.push([$1 || Infinity, $2 || ""]);
    });

    while (ax.length && bx.length) {
      const an = ax.shift();
      const bn = bx.shift();
      const nn = an[0] - bn[0] || an[1].localeCompare(bn[1]);
      if (nn) return nn;
    }

    return ax.length - bx.length;
  }

  // DataTable con AJAX
  let table = $("#petitorioTable").DataTable({
    ajax: {
      url: "CRUDpetitorioservlet?opcion=2",
      dataSrc: "data", // Asegúrate de que coincida con la estructura de tu respuesta
      error: function (xhr, error, thrown) {
        showError(
          "Error al cargar datos",
          "No se pudieron cargar los datos del petitorio técnico"
        );
      },
    },
    columns: [
      {
        data: "codigo",
        className: "font-weight-bold",
        render: function (data, type, row) {
          // Para ordenación, siempre usamos el código (aunque exista codigoProducto)
          if (type === "sort" || type === "type") {
            return data; // Usamos el código jerárquico para ordenar siempre
          }
          // Para visualización: mostramos vacío si existe codigoProducto
          return row.codigoProducto ? "" : data;
        },
        type: "natural", // Usamos nuestro tipo de ordenación natural
      },
      {
        data: "codigoProducto",
        render: function (data) {
          return data || "<span class='text-muted'>NP</span>";
        },
      },
      {
        data: "nombre",
        render: function (data) {
          return data || "<span class='text-muted'>Sin nombre</span>";
        },
      },
      {
        data: "padre",
        render: function (data) {
          return data
            ? `<span class="badge badge-info">${data}</span>`
            : '<span class="badge badge-success">Raíz</span>';
        },
      },
      {
        data: null,
        render: function (data, type, row) {
          const prsntcn = row.prsntcn;
          const codprod = row.codigoProducto;
          let html = `
            <div class="btn-group btn-group-sm" role="group">
          `;
          if (!codprod) {
            html += `
              <button class="btn btn-outline-primary btn-editar" data-id="${row.codigo}" title="Editar">
                <i class="fas fa-edit"></i>
              </button>
            `;
          }

          html += `
            <button class="btn btn-outline-danger btn-eliminar" data-id="${row.codigo}" title="Eliminar">
                <i class="fas fa-trash"></i>
              </button>
          `;

          if (!prsntcn == "") {
            html += `
            <button class="btn btn-outline-primary btn-sm" id="btnBatch" data-gen="${row.nombre}" data-id="${row.codigo}" title="Agregar Genérico">
              <i class="fas fa-plus"></i>
            </button>
          `;
          }

          html += `</div>`;

          return html;
        },
        orderable: false,
        className: "text-center",
      },
    ],
    order: [[0, "asc"]],
    language: {
      url: "https://cdn.datatables.net/plug-ins/1.13.1/i18n/es-ES.json",
    },
    initComplete: function () {
      $("td:empty").css("background-color", "#f9f9f9");
    },
  });

  // Función para mostrar errores
  function showError(title, message) {
    Toast.fire({
      icon: "error",
      title: title,
      text: message,
    });
    console.error(title + ":", message);
  }

  // Función para mostrar éxito
  function showSuccess(message) {
    Toast.fire({
      icon: "success",
      title: message,
    });
  }

  // Cargar opciones de padres
  function cargarPadres(excludeId = null, callback) {
    $.ajax({
      url: "CRUDpetitorioservlet?opcion=3",
      dataType: "json",
      success: function (response) {
        console.log("Respuesta del servidor:", response);

        let options = '<option value="">-- Ninguno (Item raíz) --</option>';

        if (response && Array.isArray(response.data)) {
          response.data.forEach(function (item) {
            if (item.codigo && item.nombre && item.codigo !== excludeId) {
              options += `<option value="${item.codigo}">${item.codigo} - ${item.nombre}</option>`;
            }
          });
        } else {
          showError(
            "Error de formato",
            "La respuesta del servidor no tiene el formato esperado"
          );
        }

        $("#padreItem").html(options);
        if (typeof callback === "function") callback();
      },
      error: function (xhr, status, error) {
        showError(
          "Error de conexión",
          "No se pudieron cargar los items padres"
        );
      },
    });
  }

  // Función para abrir modal en modo creación
  function abrirModalCrear() {
    modoActual = "crear";
    $("#itemModalLabel").text("Nuevo Item");
    $("#codigoGroup").hide();
    $("#itemForm")[0].reset();
    $("#nombreItem").removeClass("is-invalid");

    cargarPadres(null, function () {
      $("#itemModal").modal("show");
      $("#nombreItem").focus();
    });
  }

  // Función para abrir modal en modo edición
  function abrirModalEditar(codigo) {
    modoActual = "editar";
    $("#itemModalLabel").text("Editar Item");
    $("#codigoGroup").show();
    $("#nuevoCodigoGroup").show();
    $("#nombreItem").removeClass("is-invalid");

    $.get(`CRUDpetitorioservlet?codigo=${codigo}&opcion=1`)
      .done(function (response) {
        console.log("Datos del item:", response);

        // Asegúrate de acceder a response.data en lugar de response directamente
        if (response && response.data) {
          itemActual = response.data;

          localStorage.setItem("codigoAntiguo", itemActual.codigo);

          $("#codigoItem").val(itemActual.codigo);
          $("#nombreItem").val(itemActual.nombre);

          if (!itemActual.tieneHijos) {
            // ocultar el checkbox de padre
            $("#isPadreItemDiv").show();
          } else {
            $("#isPadreItemDiv").hide();
          }

          $("#isPadreItem").prop("checked", itemActual.prsntcn === "1");

          // Cargar padres excluyendo el item actual para evitar ciclos
          cargarPadres(itemActual.codigo, function () {
            $("#padreItem")
              .val(itemActual.padre || "")
              .trigger("change");
            $("#itemModal").modal("show");
            $("#nombreItem").focus();
          });
        } else {
          showError(
            "Error de datos",
            "La estructura de la respuesta no es la esperada"
          );
        }
      })
      .fail(function (xhr, status, error) {
        showError(
          "Error al cargar",
          "No se pudo cargar la información del item"
        );
        console.error("Error en la solicitud:", status, error);
      });
  }

  // Función de cierre del modal
  function cerrarModal() {
    $("#itemModal").modal("hide");
    // Pequeño retraso para permitir que la animación del modal se complete
    setTimeout(function () {
      $("#itemForm")[0].reset();
      itemActual = null;
    }, 500);
  }

  // Evento para abrir modal de creación
  $("#btnNuevoItem").click(function () {
    abrirModalCrear();
  });

  // Evento para abrir modal de edición
  $("#petitorioTable").on("click", ".btn-editar", function () {
    var codigo = $(this).data("id");
    abrirModalEditar(codigo);
  });

  // Validación del formulario
  $("#itemForm").on("submit", function (e) {
    e.preventDefault();
    $("#btnGuardar").click();
  });

  // Guardar item (crear o actualizar)
  $("#btnGuardar").click(function () {
    const nombre = $("#nombreItem").val().trim();
    const padre = $("#padreItem").val();
    const codiguoAntiguo = localStorage.getItem("codigoAntiguo");
    const codigoNuevo = $("#codigoItem").val();
    const cambioCodigo = codiguoAntiguo !== codigoNuevo;

    if (!nombre) {
      $("#nombreItem").addClass("is-invalid");
      $("#nombreItem").focus();
      return;
    }

    const isPadre = $("#isPadreItem").is(":checked");

    const datos = {
      nombre: nombre,
    };

    if (isPadre) {
      datos.prsntcn = "1";
    }

    if (padre) {
      datos.padre = padre;
    }

    // Asegúrate de incluir el código cuando estás editando
    if (modoActual === "editar") {
      if (cambioCodigo) {
        datos.codigoNuevo = codigoNuevo;
        datos.codigoAntiguo = codiguoAntiguo;
      } else {
        datos.codigoAntiguo = codiguoAntiguo;
      }
      editarItem(datos);
    } else {
      crearItem(datos);
    }
  });

  // Función para crear un item nuevo (POST)
  function crearItem(datos) {
    console.log("Creando nuevo item:", datos); // Para depuración

    $("#btnGuardar")
      .prop("disabled", true)
      .html('<i class="fas fa-spinner fa-spin"></i> Guardando...');

    $.ajax({
      url: "CRUDpetitorioservlet",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(datos),
      success: function (response) {
        manejarRespuestaBatch(response);
        $("#btnGuardar").prop("disabled", false).html("Guardar");
        table.ajax.reload(null, false);
        cerrarModal();
      },
      error: function (xhr) {
        try {
          const error = JSON.parse(xhr.responseText);
          mostrarErrorPersonalizado(error.error, error.detail, error.errorCode);
        } catch (e) {
          mostrarErrorPersonalizado(
            "Error",
            "No se pudo procesar la respuesta del servidor",
            "network-error"
          );
        }
      },
    });
  }

  // Función para editar un item existente (PUT)
  function editarItem(datos) {
    console.log("Actualizando item existente:", datos);

    // Agregar validación para nuevo código si existe
    if (datos.nuevoCodigo && datos.nuevoCodigo !== datos.codigo) {
      // Validar formato del nuevo código
      if (!validarFormatoCodigo(datos.nuevoCodigo)) {
        showError("Error", "El formato del nuevo código no es válido");
        $("#btnGuardar").prop("disabled", false).html("Guardar");
        return;
      }
    }

    $("#btnGuardar")
      .prop("disabled", true)
      .html('<i class="fas fa-spinner fa-spin"></i> Guardando...');

    $.ajax({
      url: "CRUDpetitorioservlet",
      type: "PUT",
      contentType: "application/json",
      data: JSON.stringify(datos),
      success: function (response) {
        if (response.success === false) {
          showError("Error", response.error || "Operación fallida");
          return;
        }

        // Manejar cambio de código si ocurrió
        if (response.nuevoCodigo) {
          showSuccess(
            `Item actualizado. Nuevo código: ${response.nuevoCodigo}`
          );
        } else {
          showSuccess("Item actualizado correctamente");
        }

        table.ajax.reload(null, false);
        $("#btnGuardar").prop("disabled", false).html("Guardar");
        cerrarModal();
      },
      error: function (xhr) {
        let errorMsg = "Error en la operación";
        try {
          const response = JSON.parse(xhr.responseText);
          errorMsg = response.error || errorMsg;
          // Manejar específicamente error de código duplicado
          if (response.errorCode === "duplicate-code") {
            errorMsg = "El nuevo código ya existe en el sistema";
          }
        } catch (e) {
          console.error("Error parsing response:", e);
        }
        showError("Error", errorMsg);
        $("#btnGuardar").prop("disabled", false).html("Guardar");
      },
    });
  }

  function validarFormatoCodigo(codigo) {
    if (!codigo) return false;
    // Expresión regular para validar formato (ajusta según tus necesidades)
    return /^[A-Za-z0-9._-]+$/.test(codigo);
  }

  // Eliminar item
  $("#petitorioTable").on("click", ".btn-eliminar", function () {
    const codigo = $(this).data("id");
    const rowData = table.row($(this).parents("tr")).data();
    const nombre = rowData ? rowData.nombre : "";

    Swal.fire({
      title: "¿Eliminar item?",
      html: `¿Estás seguro de eliminar el item <b>${codigo}${
        nombre ? " - " + nombre : ""
      }</b>?`,
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      cancelButtonColor: "#3085d6",
      confirmButtonText: "Sí, eliminar",
      cancelButtonText: "Cancelar",
    }).then((result) => {
      if (result.isConfirmed) {
        $.ajax({
          url: `CRUDpetitorioservlet?codigo=${codigo}`,
          type: "DELETE",
          success: function (response) {
            if (response.success === false) {
              showError(
                "Error",
                response.error || "No se pudo eliminar el item"
              );
              return;
            }
            table.ajax.reload(null, false);
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

  // Eventos del modal para limpiar al cerrar
  $("#itemModal").on("hidden.bs.modal", function () {
    $("#itemForm")[0].reset();
    $("#nombreItem").removeClass("is-invalid");
    itemActual = null;
  });

  //MODAL POR LOTES
  // Evento para abrir modal de creación por lotes
  $(document).on("click", "#btnBatch", function () {
    const gen = $(this).data("gen");
    id = $(this).data("id");
    abrirModalBatch(gen, id);
  });

  // Función para abrir modal de creación por lotes
  function abrirModalBatch(gen, id) {
    productosSeleccionados = [];
    $("#batchModal").modal("show");

    $("#textGenerico").text(` para ${gen} - ${id}`);

    // Cargar los genéricos y luego los productos
    cargarGenericos().then(function () {
      tableProductos.ajax.reload();
    });
  }

  // Evento para procesar el lote
  $("#btnGuardarBatch").click(function () {
    procesarBatch();
  });

  // Función para procesar la creación por lotes
  function procesarBatch() {
    // Validación inicial
    if (!productosSeleccionados.length) {
      mostrarError("No hay productos seleccionados para procesar");
      return;
    }

    try {
      const copyArray = JSON.parse(JSON.stringify(productosSeleccionados));

      // Preparar datos (sin copia innecesaria si no la requieres después)
      const datosEnviar = copyArray.map((item) => ({
        ...item,
        padre: id, // Agregar el campo padre directamente
      }));

      // Mostrar carga
      $("#btnGuardarBatch")
        .prop("disabled", true)
        .html('<i class="fas fa-spinner fa-spin"></i> Procesando...');

      $.ajax({
        url: "CRUDpetitorioservlet", // Cambia a tu endpoint real
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ items: datosEnviar }),
        success: function (response) {
          manejarRespuestaBatch(response);

          // Éxito - limpiar y cerrar
          productosSeleccionados = [];
          renderizarProductos();
          $("#btnGuardarBatch")
            .prop("disabled", false)
            .html('<i class="fas fa-save"></i> Guardar Lote');
          table.ajax.reload(null, false);
          $("#batchModal").modal("hide");
        },
        error: function (xhr) {
          try {
            const error = JSON.parse(xhr.responseText);
            mostrarErrorPersonalizado(
              error.error,
              error.detail,
              error.errorCode
            );
          } catch (e) {
            mostrarErrorPersonalizado(
              "Error",
              "No se pudo procesar la respuesta del servidor",
              "network-error"
            );
          }
        },
      });

      console.log("Datos a enviar:", { datosEnviar });
    } catch (e) {
      mostrarError("Error al preparar los datos: " + e.message);
      console.error("Error en procesarBatch:", e);
    }
  }

  let tableProductos = $("#tablaProductos").DataTable({
    ajax: {
      url: "CRUDpetitorioservlet?opcion=6",
      dataSrc: "data", // Asegúrate de que coincida con la estructura de tu respuesta
      error: function (xhr, error, thrown) {
        showError(
          "Error al cargar datos",
          "No se pudieron cargar los datos del petitorio técnico"
        );
      },
    },
    columns: [
      {
        data: "codigo",
        className: "font-weight-bold",
      },
      {
        data: "nombre",
        render: function (data) {
          return data || "<span class='text-muted'>Sin nombre</span>";
        },
      },
      {
        data: null,
        render: function (data, type, row) {
          return `
            <div class="btn-group btn-group-sm" role="group">
              <button class="btn btn-outline-primary btn-editar" id="btnProAgre" data-nick="${row.nombre}" data-codigo="${row.codigo}" title="Agregar">
                <i class="fas fa-plus"></i>
              </button>
            </div>
          `;
        },
        orderable: false,
        className: "text-center",
      },
    ],
    language: {
      url: "https://cdn.datatables.net/plug-ins/1.13.1/i18n/es-ES.json",
    },
  });

  $(document).on("click", "#btnProAgre", function () {
    const nombre = $(this).data("nick");
    const codigo = $(this).data("codigo");

    const jsonProducto = {
      codigo: codigo,
      nombre: nombre,
    };

    // Validar que el producto no esté ya seleccionado
    if (estaSeleccionado(codigo)) {
      showError("El producto ya está seleccionado");
      return;
    }

    productosSeleccionados.push(jsonProducto);
    showSuccess("Producto agregado correctamente");
    renderizarProductos();
    $("#totalProductos").text(productosSeleccionados.length);
    console.log({ productosSeleccionados });
  });

  function renderizarProductos() {
    const tabla = document.getElementById("tablaSeleccionados");
    const tbody = tabla.querySelector("tbody");

    // Limpiar el contenido actual
    tbody.innerHTML = "";

    if (productosSeleccionados.length === 0) {
      abledAgregar(true);
      // Mostrar fila vacía si no hay productos
      tbody.innerHTML = `
        <tr class="fila-vacia">
          <td colspan="3" class="text-center text-muted">
            No hay productos seleccionados
          </td>
        </tr>
      `;
      return;
    }

    abledAgregar(false);

    // Crear filas para cada producto
    productosSeleccionados.forEach((producto, index) => {
      const fila = document.createElement("tr");

      fila.innerHTML = `
        <td>${producto.codigo}</td>
        <td>${producto.nombre}</td>
        <td>
          <button class="btn btn-sm btn-danger" data-index="${index}" id="btnEliPro">
            Eliminar
          </button>
        </td>
      `;

      tbody.appendChild(fila);
    });
  }

  $(document).on("click", "#btnEliPro", function () {
    const index = $(this).data("index");
    eliminarProducto(index);
  });

  // Función para eliminar un producto (ejemplo)
  function eliminarProducto(index) {
    productosSeleccionados.splice(index, 1);
    showSuccess("Producto eliminado correctamente");
    renderizarProductos();
    $("#totalProductos").text(productosSeleccionados.length);
  }

  // Función para verificar si un producto está seleccionado
  function estaSeleccionado(codigo) {
    return productosSeleccionados.some((p) => p.codigo === codigo);
  }

  function abledAgregar(boo) {
    $("#btnGuardarBatch").prop("disabled", boo);
  }

  function manejarRespuestaBatch(respuesta) {
    // validar que respuesta.resulta exista y sea un array
    if (Array.isArray(respuesta.results)) {
      // Procesamiento por lotes
      const summary = respuesta.summary;

      if (summary.errors > 0) {
        Swal.fire({
          title: "Proceso completado con errores",
          html: `Se procesaron ${summary.total} items:<br>
                       <b>${summary.success} exitosos</b><br>
                       <b class="text-danger">${summary.errors} con errores</b>`,
          icon: "warning",
          confirmButtonText: "Ver detalles",
        }).then(() => {
          mostrarErroresEnModal(respuesta.results);
        });
      } else {
        Swal.fire({
          title: "Éxito",
          text: `Todos los ${summary.total} items se procesaron correctamente`,
          icon: "success",
        });
      }
    } else if (respuesta.success) {
      // Item individual exitoso
      Swal.fire({
        title: "Ítem creado",
        html: `<b>${respuesta.message}</b>`,
        icon: "success",
      });
    } else {
      // Error individual
      mostrarErrorPersonalizado(
        respuesta.error,
        respuesta.detail,
        respuesta.errorCode
      );
    }
  }

  function mostrarErroresEnModal(resultados) {
    const errores = resultados.filter((r) => !r.success);
    let html =
      '<table class="table table-sm"><thead><tr><th>#</th><th>Error</th><th>Detalle</th></tr></thead><tbody>';

    errores.forEach((error, index) => {
      html += `<tr>
            <td>${index + 1}</td>
            <td><span class="badge badge-danger">${error.error}</span></td>
            <td>${error.detail}</td>
        </tr>`;
    });

    html += "</tbody></table>";

    Swal.fire({
      title: "Errores encontrados",
      html: html,
      width: "800px",
      confirmButtonText: "Entendido",
    });
  }

  function mostrarErrorPersonalizado(titulo, mensaje, codigoError) {
    let icono = "error";
    if (codigoError === "duplicate-code") icono = "warning";
    if (codigoError === "auth-required") icono = "info";

    Swal.fire({
      title: titulo,
      html: `${mensaje}<br><small>Código: ${codigoError}</small>`,
      icon: icono,
      confirmButtonText: "Entendido",
    });
  }
});
