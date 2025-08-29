$(document).ready(function () {
  // Configuración de Toast
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

  // Variables globales
  let sup = false;
  let tabla;
  let progressBars = {};

  // Función para validar sesión
  $.fn.validarSession = function () {
    $.getJSON("validarsesion", function (data) {
      if (data.resultado === "ok") {
        $("#lblUsuario").text(data.logi);
        if (
          data.grucod === "SUPINV" ||
          data.grucod === "SUPERV" ||
          data.grucod === "JEFALM"
        ) {
          $("#agregar").show();

          $("#editar").show();
          sup = true;
        }
      } else {
        $(location).attr("href", "index.html");
      }
    }).fail(function (jqXHR, textStatus, errorThrown) {
      $(location).attr("href", "index.html");
    });
  };

  $(document).on("click", 'button[data-bs-target="#modalAvance"]', function () {
    const codinv = $(this).data("codinv");

    // Luego cargar los datos
    cargarDetalleInventario(codinv);
  });

  async function cargarDetalleInventario(codinv) {
    try {
      // Mostrar estado de carga en todas las pestañas
      mostrarEstadosCarga();

      // Obtener datos del servidor
      const response = await $.ajax({
        type: "POST",
        url: "DetalleInventario",
        data: { codinv: codinv },
        dataType: "json",
      });

      if (!response.success) {
        throw new Error(
          response.message || "Error en la respuesta del servidor"
        );
      }

      // Actualizar resumen general
      actualizarResumenGeneral(response);

      // Renderizar datos por farmacia
      renderizarFarmacias(response.detalleFarmacias);

      // Renderizar datos por categoría (usando los datos de la primera farmacia)
      renderizarCategorias(response.detalleFarmacias);

      // Renderizar datos por conteo
      renderizarConteos(response.detalleFarmacias);

      // Renderizar productos idénticos
      renderizarProductosIdenticos(response.detalleIdenticos);

      // Inicializar eventos y plugins después de renderizar
      inicializarComponentes();
    } catch (error) {
      console.error("Error al cargar detalle del inventario:", error);
      mostrarErroresCarga(error);
    }
  }

  function renderizarProductosIdenticos(productosIdenticos) {
    const $tablaBody = $("#cuerpoTablaProductosIdenticos");

    if (!productosIdenticos || productosIdenticos.length === 0) {
      $tablaBody.html(`
            <tr>
                <td colspan="6" class="text-center text-muted py-4">
                    <i class="bi bi-info-circle-fill me-2"></i>
                    No se encontraron productos con conteos idénticos
                </td>
            </tr>
        `);
      return;
    }

    const html = productosIdenticos
      .map((item) => {
        const porcentaje = item.progreso || 0;
        const estado = obtenerEstadoIdenticos(porcentaje);

        return `
            <tr>
                <td>
                    <div class="d-flex align-items-center">
                        <i class="bi bi-box-seam me-2 fs-5" style="color: #4dabf7;"></i>
                        <span>${item.farmacia || "Sin nombre"}</span>
                    </div>
                </td>
                <td>
                    <div class="d-flex align-items-center">
                        <i class="bi bi-list-check me-2 fs-5" style="color: #4dabf7;"></i>
                        <span>${item.conteo || "Sin nombre"}</span>
                    </div>
                </td>
                <td>${item.total_registros || 0}</td>
                <td>${item.coincidencias || 0}</td>
                <td>${
                  (item.total_registros || 0) - (item.coincidencias || 0)
                }</td>
                <td>
                    <div class="progress position-relative" style="height: 20px;">
                        <div class="progress-bar ${estado.clase}" 
                             role="progressbar" style="width: ${porcentaje}%" 
                             aria-valuenow="${porcentaje}" 
                             aria-valuemin="0" 
                             aria-valuemax="100">
                        </div>
                        <span class="position-absolute w-100 text-center ${
                          porcentaje < 15 ? "text-dark" : "text-white"
                        }" 
                              style="line-height: 24px; font-weight: ${
                                porcentaje < 15 ? "bold" : "normal"
                              }">
                            ${porcentaje.toFixed(1)}%
                        </span>
                    </div>
                </td>
                <td>
                    <span class="badge ${estado.badge}">
                        <i class="bi ${estado.icono} me-1"></i>
                        ${estado.texto}
                    </span>
                </td>
            </tr>`;
      })
      .join("");

    $tablaBody.html(html);
  }

  function obtenerEstadoIdenticos(porcentaje) {
    if (porcentaje >= 90)
      return {
        clase: "bg-success",
        badge: "bg-success",
        icono: "bi-check-circle-fill",
        texto: "Excelente",
      };
    if (porcentaje >= 70)
      return {
        clase: "bg-primary",
        badge: "bg-primary",
        icono: "bi-check-circle",
        texto: "Bueno",
      };
    if (porcentaje >= 50)
      return {
        clase: "bg-info",
        badge: "bg-info",
        icono: "bi-arrow-repeat",
        texto: "Aceptable",
      };
    return {
      clase: "bg-warning",
      badge: "bg-warning text-dark",
      icono: "bi-exclamation-triangle-fill",
      texto: "Bajo",
    };
  }

  // Función para renderizar los conteos
  function renderizarConteos(farmacias) {
    const $tablaBody = $("#cuerpoTablaConteos");

    if (!farmacias || farmacias.length === 0) {
      $tablaBody.html(`
          <tr>
              <td colspan="6" class="text-center text-muted py-4">
                  <i class="bi bi-info-circle-fill me-2"></i>
                  No se encontraron datos de conteos
              </td>
          </tr>
      `);
      return;
    }

    try {
      console.log({ farmacias });
      const html = farmacias
        .map((farmacia) => {
          const conteos = farmacia.conteos || [];
          const nombreFarmacia = farmacia.nombreAlmacen || "Sin nombre";
          console.log({ conteos, nombreFarmacia });
          return conteos
            .map((conteo) => {
              const estado = obtenerEstadoConteo(conteo.estado);
              const progreso = conteo.progresoConteo || 0;

              console.log({ estado, progreso });

              return `
              <tr>
                  <td>
                      <div class="d-flex align-items-center">
                          <i class="bi bi-building me-2 fs-5 text-primary"></i>
                          <span>${nombreFarmacia}</span>
                      </div>
                  </td>
                  <td>
                      <div class="d-flex align-items-center">
                          <i class="bi bi-list-check me-2 fs-5 text-info"></i>
                          <span>${conteo.nombreConteo || "Sin nombre"}</span>
                      </div>
                  </td>
                  <td>
                      <span class="badge ${estado.badge}">
                          <i class="bi ${estado.icono} me-1"></i>
                          ${estado.texto}
                      </span>
                  </td>
                  <td>${conteo.totalItemsConStock || 0}</td>
                  <td>
                      <div class="progress position-relative" style="height: 20px;">
                          <div class="progress-bar ${estado.claseProgreso}" 
                               role="progressbar" style="width: ${progreso}%" 
                               aria-valuenow="${progreso}" 
                               aria-valuemin="0" 
                               aria-valuemax="100">
                              ${progreso.toFixed(1)}%
                          </div>
                          <span class="position-absolute w-100 text-center ${
                            progreso < 15 ? "text-dark" : "text-white"
                          }" 
                                  style="line-height: 24px; font-weight: ${
                                    progreso < 15 ? "bold" : "normal"
                                  }">
                                ${progreso.toFixed(1)}%
                            </span>
                      </div>
                  </td>
              </tr>`;
            })
            .join("");
        })
        .join("");

      $tablaBody.html(
        html ||
          `
          <tr>
              <td colspan="6" class="text-center text-muted py-4">
                  <i class="bi bi-info-circle-fill me-2"></i>
                  No se encontraron datos de conteos
              </td>
          </tr>
      `
      );
    } catch (error) {
      console.error("Error al renderizar conteos:", error);
      $tablaBody.html(`
          <tr>
              <td colspan="6" class="text-center text-danger py-3">
                  <i class="bi bi-exclamation-triangle-fill me-2"></i>
                  Error al cargar los conteos
              </td>
          </tr>
      `);
    }
  }

  // Función para determinar el estado del conteo
  function obtenerEstadoConteo(estadoCodigo) {
    if (estadoCodigo === "A") {
      return {
        badge: "bg-primary",
        icono: "bi-arrow-repeat",
        texto: "Activo",
        claseProgreso: "bg-primary",
      };
    } else if (estadoCodigo === "C") {
      return {
        badge: "bg-success",
        icono: "bi-check-circle",
        texto: "Cerrado",
        claseProgreso: "bg-success",
      };
    } else {
      return {
        badge: "bg-secondary",
        icono: "bi-question-circle",
        texto: "Desconocido",
        claseProgreso: "bg-secondary",
      };
    }
  }

  // Funciones auxiliares
  function mostrarEstadosCarga() {
    const loadingHTML =
      '<tr><td colspan="6" class="text-center"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Cargando...</span></div></td></tr>';

    $("#cuerpoTablaFarmacias").html(loadingHTML);
    $("#cuerpoTablaCategorias").html(loadingHTML);
    $("#cuerpoTablaConteos").html(loadingHTML);
    $("#cuerpoTablaProductosIdenticos").html(loadingHTML);

    // Mostrar skeleton loader para las cards de resumen
    $("#progresoGlobalBar").html('<div class="skeleton-loader"></div>');
    $("#totalFarmacias").html('<div class="skeleton-loader"></div>');
    $("#totalProductos").html('<div class="skeleton-loader"></div>');
  }

  function actualizarResumenGeneral(response) {
    // Progreso global con animación
    const progresoGlobal = response.progresoGlobal || 0;
    $("#progresoGlobalBar")
      .css("width", "0%")
      .animate({ width: progresoGlobal + "%" }, 800, function () {
        $(this)
          .find(".progress-text")
          .text(progresoGlobal.toFixed(1) + "%");
      });

    $("#txtProgresoGlobal").text(progresoGlobal.toFixed(1));

    // Contador animado para farmacias
    animateValue("totalFarmacias", 0, response.totalFarmacias || 0, 800);

    // Calcular y animar total de productos
    const totalProductos = response.detalleFarmacias.reduce(
      (sum, farmacia) => sum + (farmacia.totalProductosConStock || 0),
      0
    );
    animateValue("totalProductos", 0, totalProductos, 800);
  }

  function renderizarFarmacias(farmacias) {
    const farmaciasHtml = farmacias
      .map((farmacia) => {
        const estado = obtenerEstadoFarmacia(farmacia.progreso);
        const porcentaje = farmacia.progreso || 0;

        return `
            <tr class="align-middle">
                <td>
                    <div class="d-flex align-items-center">
                        <i class="bi bi-building me-2 fs-5 text-primary"></i>
                        <span>${farmacia.nombreAlmacen || "Sin nombre"}</span>
                    </div>
                </td>
                <td>
                    <div class="progress position-relative" style="height: 24px;">
                        <div class="progress-bar ${
                          estado.claseProgreso
                        } progress-bar-striped ${
          farmacia.progreso === 100 ? "progress-bar-animated" : ""
        }" 
                             role="progressbar" style="width: ${porcentaje}%" 
                             aria-valuenow="${porcentaje}" 
                             aria-valuemin="0" 
                             aria-valuemax="100">
                        </div>
                        <span class="position-absolute w-100 text-center ${
                          porcentaje < 15 ? "text-dark" : "text-white"
                        }" 
                              style="line-height: 24px; font-weight: ${
                                porcentaje < 15 ? "bold" : "normal"
                              }">
                            ${porcentaje.toFixed(1)}%
                        </span>
                    </div>
                </td>
                <td>${farmacia.totalConteos || 0}</td>
                <td>${farmacia.totalProductosConStock || 0}</td>
                <td>
                    <span class="badge ${estado.claseBadge} rounded-pill">
                        <i class="bi ${estado.icono} me-1"></i>
                        ${estado.texto}
                    </span>
                </td>
            </tr>`;
      })
      .join("");

    $("#cuerpoTablaFarmacias").html(farmaciasHtml);
  }

  function renderizarCategorias(farmacias) {
    // Renderizar vista con filtros
    renderizarVistaFiltrosCategorias(farmacias);
  }

  function renderizarVistaFiltrosCategorias(farmacias) {
    // Crear el HTML para los filtros si no existe
    const $tabFiltros = $("#categorias-filtros");
    if ($tabFiltros.find(".row.mb-3").length === 0) {
      const filtrosHTML = `
        <div class="row mb-3">
            <div class="col-md-4">
                <div class="input-group">
                    <span class="input-group-text">Farmacia:</span>
                    <select class="form-select" id="filtroFarmacia">
                        <option value="todas">Todas las farmacias</option>
                    </select>
                </div>
            </div>
            <div class="col-md-4">
                <div class="input-group">
                    <span class="input-group-text">Conteo:</span>
                    <select class="form-select" id="filtroConteo">
                        <option value="todas">Todos los conteos</option>
                <option value="General">General</option>
                    </select>
                </div>
            </div>
            <div class="col-md-4">
                <div class="input-group">
                    <span class="input-group-text">Categoría:</span>
                    <select class="form-select" id="filtroCategoria">
                        <option value="todas">Todas las categorías</option>
                    </select>
                </div>
            </div>
        </div>
    `;
      $tabFiltros.prepend(filtrosHTML);
    }

    // Llenar los filtros
    llenarFiltrosCategorias(farmacias);

    // Establecer valores por defecto
    $("#filtroFarmacia").val("todas");
    $("#filtroConteo").val("General");
    $("#filtroCategoria").val("todas");

    // Aplicar filtro inicial
    aplicarFiltrosCategorias(farmacias);

    // Configurar eventos
    configurarEventosFiltros(farmacias);
  }

  function llenarFiltrosCategorias(farmacias) {
    const $filtroFarmacia = $("#filtroFarmacia");
    const $filtroConteo = $("#filtroConteo");
    const $filtroCategoria = $("#filtroCategoria");

    // Limpiar y agregar opciones por defecto
    $filtroFarmacia
      .empty()
      .append('<option value="todas">Todas las farmacias</option>');
    $filtroConteo
      .empty()
      .append('<option value="todas">Todos los conteos</option>');
    $filtroConteo.append('<option value="General">General</option>');
    $filtroCategoria
      .empty()
      .append('<option value="todas">Todas las categorías</option>');

    if (!farmacias || farmacias.length === 0) return;

    // Llenar farmacias
    farmacias.forEach((farmacia) => {
      $filtroFarmacia.append(
        `<option value="${farmacia.codalm}">${
          farmacia.nombreAlmacen || "Sin nombre"
        }</option>`
      );
    });

    // Llenar conteos (de todas las farmacias)
    const conteosUnicos = new Set();
    farmacias.forEach((farmacia) => {
      if (farmacia.conteos && farmacia.conteos.length > 0) {
        farmacia.conteos.forEach((conteo) => {
          const nombreConteo =
            conteo.nombreConteo || `Conteo ${conteo.codinvalm}`;
          conteosUnicos.add(nombreConteo);
        });
      }
    });

    conteosUnicos.forEach((conteo) => {
      $filtroConteo.append(`<option value="${conteo}">${conteo}</option>`);
    });

    // Llenar categorías (todas las categorías únicas)
    const categoriasUnicas = new Set();
    farmacias.forEach((farmacia) => {
      if (farmacia.categorias && farmacia.categorias.length > 0) {
        farmacia.categorias.forEach((cat) => {
          const nombre = cat.nombreTipo || "Sin nombre";
          categoriasUnicas.add(nombre);
        });
      }

      if (farmacia.conteos && farmacia.conteos.length > 0) {
        farmacia.conteos.forEach((conteo) => {
          if (conteo.categorias && conteo.categorias.length > 0) {
            conteo.categorias.forEach((cat) => {
              const nombre = cat.nombreTipo || "Sin nombre";
              categoriasUnicas.add(nombre);
            });
          }
        });
      }
    });

    categoriasUnicas.forEach((categoria) => {
      $filtroCategoria.append(
        `<option value="${categoria}">${categoria}</option>`
      );
    });
  }

  function configurarEventosFiltros(farmacias) {
    // Evento para cambiar el filtro de farmacia
    $("#filtroFarmacia")
      .off("change")
      .on("change", function () {
        // Actualizar inmediatamente los otros filtros
        actualizarFiltrosDependientes("farmacia", $(this).val(), farmacias);
        aplicarFiltrosCategorias(farmacias);
      });

    // Evento para cambiar el filtro de conteo
    $("#filtroConteo")
      .off("change")
      .on("change", function () {
        // Actualizar inmediatamente los otros filtros
        actualizarFiltrosDependientes("conteo", $(this).val(), farmacias);
        aplicarFiltrosCategorias(farmacias);
      });

    // Evento para cambiar el filtro de categoría
    $("#filtroCategoria")
      .off("change")
      .on("change", function () {
        aplicarFiltrosCategorias(farmacias);
      });
  }

  function actualizarFiltrosDependientes(tipo, valor, farmacias) {
    if (tipo === "farmacia") {
      // Si cambia la farmacia, actualizar los conteos disponibles
      const $filtroConteo = $("#filtroConteo");
      const valorActualConteo = $filtroConteo.val(); // Guardar el valor actual

      $filtroConteo
        .empty()
        .append('<option value="todas">Todos los conteos</option>');
      $filtroConteo.append('<option value="General">General</option>');

      if (valor === "todas") {
        // Mostrar todos los conteos únicos
        const conteosUnicos = new Set();
        farmacias.forEach((farmacia) => {
          if (farmacia.conteos && farmacia.conteos.length > 0) {
            farmacia.conteos.forEach((conteo) => {
              const nombreConteo =
                conteo.nombreConteo || `Conteo ${conteo.codinvalm}`;
              conteosUnicos.add(nombreConteo);
            });
          }
        });
        conteosUnicos.forEach((conteo) => {
          $filtroConteo.append(`<option value="${conteo}">${conteo}</option>`);
        });
      } else {
        // Mostrar solo los conteos de la farmacia seleccionada
        const farmacia = farmacias.find((f) => f.codalm === valor);
        if (farmacia && farmacia.conteos && farmacia.conteos.length > 0) {
          farmacia.conteos.forEach((conteo) => {
            const nombreConteo =
              conteo.nombreConteo || `Conteo ${conteo.codinvalm}`;
            $filtroConteo.append(
              `<option value="${nombreConteo}">${nombreConteo}</option>`
            );
          });
        }
      }

      // Restaurar el valor anterior si aún está disponible
      if (
        $filtroConteo.find(`option[value="${valorActualConteo}"]`).length > 0
      ) {
        $filtroConteo.val(valorActualConteo);
      } else {
        // Si el valor anterior ya no está disponible, seleccionar "General"
        $filtroConteo.val("General");
      }
    } else if (tipo === "conteo") {
      // Si cambia el conteo, actualizar las categorías disponibles
      const $filtroCategoria = $("#filtroCategoria");
      const valorActualCategoria = $filtroCategoria.val(); // Guardar el valor actual

      // No vaciamos el select de categorías, solo actualizamos las opciones disponibles
      const categoriasUnicas = new Set();

      if (valor === "todas" || valor === "General") {
        // Mostrar todas las categorías únicas
        farmacias.forEach((farmacia) => {
          if (farmacia.categorias && farmacia.categorias.length > 0) {
            farmacia.categorias.forEach((cat) => {
              const nombre = cat.nombreTipo || "Sin nombre";
              categoriasUnicas.add(nombre);
            });
          }
          if (farmacia.conteos && farmacia.conteos.length > 0) {
            farmacia.conteos.forEach((conteo) => {
              if (conteo.categorias && conteo.categorias.length > 0) {
                conteo.categorias.forEach((cat) => {
                  const nombre = cat.nombreTipo || "Sin nombre";
                  categoriasUnicas.add(nombre);
                });
              }
            });
          }
        });
      } else {
        // Mostrar solo las categorías del conteo seleccionado
        farmacias.forEach((farmacia) => {
          if (farmacia.conteos && farmacia.conteos.length > 0) {
            farmacia.conteos.forEach((conteo) => {
              const nombreConteo =
                conteo.nombreConteo || `Conteo ${conteo.codinvalm}`;
              if (
                nombreConteo === valor &&
                conteo.categorias &&
                conteo.categorias.length > 0
              ) {
                conteo.categorias.forEach((cat) => {
                  const nombre = cat.nombreTipo || "Sin nombre";
                  categoriasUnicas.add(nombre);
                });
              }
            });
          }
        });
      }

      // Mantener el valor actual y solo actualizar las opciones disponibles
      const opcionesActuales = new Set();
      $filtroCategoria.find("option").each(function () {
        opcionesActuales.add($(this).val());
      });

      // Agregar nuevas opciones que no existan
      categoriasUnicas.forEach((categoria) => {
        if (!opcionesActuales.has(categoria)) {
          $filtroCategoria.append(
            `<option value="${categoria}">${categoria}</option>`
          );
        }
      });

      // Mantener el valor seleccionado actual
      $filtroCategoria.val(valorActualCategoria);
    }
  }

  function aplicarFiltrosCategorias(farmacias) {
    const filtroFarmacia = $("#filtroFarmacia").val();
    const filtroConteo = $("#filtroConteo").val();
    const filtroCategoria = $("#filtroCategoria").val();

    const categoriasFiltradas = [];

    farmacias.forEach((farmacia) => {
      // Aplicar filtro de farmacia
      if (filtroFarmacia !== "todas" && farmacia.codalm !== filtroFarmacia) {
        return;
      }

      // Procesar vista General (último conteo para productos repetidos)
      if (filtroConteo === "General") {
        // Objeto para rastrear las categorías ya procesadas
        const categoriasProcesadas = {};

        // Procesar los conteos en orden inverso (últimos primero)
        if (farmacia.conteos && farmacia.conteos.length > 0) {
          // Ordenar conteos por codinvalm (mayor = más reciente)
          const conteosOrdenados = [...farmacia.conteos].sort(
            (a, b) => b.codinvalm - a.codinvalm
          );

          conteosOrdenados.forEach((conteo) => {
            if (conteo.categorias && conteo.categorias.length > 0) {
              conteo.categorias.forEach((cat) => {
                const nombre = cat.nombreTipo || "Sin nombre";
                if (filtroCategoria === "todas" || nombre === filtroCategoria) {
                  // Solo agregamos si no existe ya esta categoría
                  if (!categoriasProcesadas[nombre]) {
                    categoriasProcesadas[nombre] = {
                      ...cat,
                      farmacia: farmacia.nombreAlmacen || "Sin nombre",
                      codalm: farmacia.codalm,
                      conteo:
                        "General (" +
                        (conteo.nombreConteo || `Conteo ${conteo.codinvalm}`) +
                        ")",
                      codinvalm: conteo.codinvalm,
                      pendientes:
                        (cat.totalProductos || 0) -
                        (cat.productosInventariados || 0),
                    };
                  }
                }
              });
            }
          });
        }

        // Agregamos las categorías procesadas al resultado final
        Object.values(categoriasProcesadas).forEach((cat) => {
          categoriasFiltradas.push(cat);
        });
      }
      // Procesar conteos específicos
      else if (
        filtroConteo !== "todas" &&
        farmacia.conteos &&
        farmacia.conteos.length > 0
      ) {
        farmacia.conteos.forEach((conteo) => {
          const nombreConteo =
            conteo.nombreConteo || `Conteo ${conteo.codinvalm}`;
          if (nombreConteo === filtroConteo && conteo.categorias) {
            conteo.categorias.forEach((cat) => {
              const nombre = cat.nombreTipo || "Sin nombre";
              if (filtroCategoria === "todas" || nombre === filtroCategoria) {
                categoriasFiltradas.push({
                  ...cat,
                  farmacia: farmacia.nombreAlmacen || "Sin nombre",
                  codalm: farmacia.codalm,
                  conteo: nombreConteo,
                  codinvalm: conteo.codinvalm,
                  pendientes:
                    (cat.totalProductos || 0) -
                    (cat.productosInventariados || 0),
                });
              }
            });
          }
        });
      }
      // Procesar vista "todas" (mostrar todo)
      else if (filtroConteo === "todas") {
        // Conteos
        if (farmacia.conteos) {
          farmacia.conteos.forEach((conteo) => {
            if (conteo.categorias) {
              conteo.categorias.forEach((cat) => {
                const nombre = cat.nombreTipo || "Sin nombre";
                if (filtroCategoria === "todas" || nombre === filtroCategoria) {
                  categoriasFiltradas.push({
                    ...cat,
                    farmacia: farmacia.nombreAlmacen || "Sin nombre",
                    codalm: farmacia.codalm,
                    conteo: conteo.nombreConteo || `Conteo ${conteo.codinvalm}`,
                    codinvalm: conteo.codinvalm,
                    pendientes:
                      (cat.totalProductos || 0) -
                      (cat.productosInventariados || 0),
                  });
                }
              });
            }
          });
        }
      }
    });

    // Calcular porcentajes si no están presentes
    categoriasFiltradas.forEach((cat) => {
      if (cat.porcentajeAvance === undefined) {
        cat.porcentajeAvance =
          cat.totalProductos > 0
            ? (cat.productosInventariados / cat.totalProductos) * 100
            : 0;
      }
    });

    window.categoriasFiltrosData = categoriasFiltradas;

    if (categoriasFiltradas.length === 0) {
      // Mostrar alerta y limpiar tabla
      Swal.fire({
        icon: "info",
        title: "Sin resultados",
        text: "No se encontraron categorías con los filtros seleccionados",
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
      });
      $("#cuerpoTablaCategorias").html(mostrarMensajeSinDatosCategorias());
    } else {
      renderizarTablaCategorias(categoriasFiltradas);
    }
  }

  function renderizarTablaCategorias(categoriasArray) {
    const categoriasHtml = categoriasArray
      .map((cat) => {
        const nombre = cat.nombreTipo || "Sin nombre";
        const porcentaje = cat.porcentajeAvance || 0;
        const estado = obtenerEstadoCategoria(porcentaje);

        return `
            <tr>
                <td>
                    <div class="d-flex align-items-center">
                        <i class="bi bi-tag-fill me-2" style="color: ${getColorForCategory(
                          nombre
                        )}"></i>
                        <span>${nombre}</span>
                    </div>
                </td>
                <td>${cat.farmacia || "Sin nombre"}</td>
                <td>${cat.conteo || "General"}</td>
                <td>${cat.totalProductos || 0}</td>
                <td>${cat.productosInventariados || 0}</td>
                <td>${
                  (cat.totalProductos || 0) - (cat.productosInventariados || 0)
                }</td>
                <td>
                    <div class="progress position-relative" style="height: 24px;">
                        <div class="progress-bar ${estado.clase}" 
                             role="progressbar" style="width: ${porcentaje}%" 
                             aria-valuenow="${porcentaje}" 
                             aria-valuemin="0" 
                             aria-valuemax="100">
                        </div>
                        <span class="position-absolute w-100 text-center ${
                          porcentaje < 15 ? "text-dark" : "text-white"
                        }" 
                              style="line-height: 24px; font-weight: ${
                                porcentaje < 15 ? "bold" : "normal"
                              }">
                            ${porcentaje.toFixed(1)}%
                        </span>
                    </div>
                </td>
                <td>
                    <span class="badge ${estado.badge}">
                        ${estado.texto}
                    </span>
                </td>
            </tr>`;
      })
      .join("");

    $("#cuerpoTablaCategorias").html(
      categoriasHtml || mostrarMensajeSinDatosCategorias()
    );
  }

  function mostrarMensajeSinDatosCategorias() {
    return `
        <tr>
        <td colspan="8" class="text-center text-muted py-4">
                <i class="bi bi-info-circle-fill me-2"></i>
                No hay datos de categorías disponibles
            </td>
        </tr>
    `;
  }

  function inicializarComponentes() {
    // Inicializar tooltips
    $('[data-bs-toggle="tooltip"]').each(function () {
      new bootstrap.Tooltip(this);
    });

    // Inicializar filtro de categorías
    $("#filtroCategoria").change(function () {
      const filtro = $(this).val().toLowerCase();
      $("#cuerpoTablaCategorias tr").each(function () {
        const textoCategoria = $(this).find("td:first").text().toLowerCase();
        $(this).toggle(textoCategoria.includes(filtro) || filtro === "todas");
      });
    });

    // Inicializar tabs con animación
    $("#inventarioTabs button").on("click", function () {
      const target = $(this).data("bs-target");
      $(target).addClass("show active").siblings().removeClass("show active");
    });
  }

  function mostrarErroresCarga(error) {
    const errorHTML = `
    <tr>
        <td colspan="6" class="text-center text-danger py-3">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>
            ${error.message || "Error al cargar información"}
        </td>
    </tr>`;

    $("#cuerpoTablaFarmacias").html(errorHTML);
    $("#cuerpoTablaCategorias").html(errorHTML);
    $("#cuerpoTablaConteos").html(errorHTML);
    $("#cuerpoTablaProductosIdenticos").html(errorHTML);
  }

  // Funciones de ayuda
  function obtenerEstadoFarmacia(porcentaje) {
    if (porcentaje === 0)
      return {
        claseProgreso: "bg-danger",
        claseBadge: "bg-danger",
        icono: "bi-x-circle",
        texto: "Sin iniciar",
      };
    if (porcentaje === 100)
      return {
        claseProgreso: "bg-success",
        claseBadge: "bg-success",
        icono: "bi-check-circle",
        texto: "Completado",
      };
    if (porcentaje < 30)
      return {
        claseProgreso: "bg-warning",
        claseBadge: "bg-warning text-dark",
        icono: "bi-exclamation-triangle",
        texto: "Iniciando",
      };
    return {
      claseProgreso: "bg-primary",
      claseBadge: "bg-primary",
      icono: "bi-arrow-repeat",
      texto: "En progreso",
    };
  }

  function obtenerEstadoCategoria(porcentaje) {
    if (porcentaje === 0)
      return {
        clase: "bg-danger",
        badge: "bg-danger",
        texto: "Sin iniciar",
      };
    if (porcentaje === 100)
      return {
        clase: "bg-success",
        badge: "bg-success",
        texto: "Completado",
      };
    if (porcentaje < 20)
      return {
        clase: "bg-warning",
        badge: "bg-warning text-dark",
        texto: "Atrasado",
      };
    if (porcentaje < 50)
      return {
        clase: "bg-info",
        badge: "bg-info",
        texto: "En progreso",
      };
    return {
      clase: "bg-primary",
      badge: "bg-primary",
      texto: "Avanzado",
    };
  }

  function getColorForCategory(categoryName) {
    // Implementación existente...
    const colors = [
      "#4e79a7",
      "#f28e2b",
      "#e15759",
      "#76b7b2",
      "#59a14f",
      "#edc948",
      "#b07aa1",
      "#ff9da7",
      "#9c755f",
      "#bab0ac",
    ];

    let hash = 0;
    for (let i = 0; i < categoryName.length; i++) {
      hash = categoryName.charCodeAt(i) + ((hash << 5) - hash);
    }

    return colors[Math.abs(hash) % colors.length];
  }

  function animateValue(id, start, end, duration) {
    const obj = document.getElementById(id);
    let startTimestamp = null;
    const step = (timestamp) => {
      if (!startTimestamp) startTimestamp = timestamp;
      const progress = Math.min((timestamp - startTimestamp) / duration, 1);
      obj.innerHTML = Math.floor(progress * (end - start) + start);
      if (progress < 1) {
        window.requestAnimationFrame(step);
      }
    };
    window.requestAnimationFrame(step);
  }

  //actualizar barra de progreso
  async function actualizarBarraProgreso(codinv) {
    try {
      const response = await $.ajax({
        type: "GET",
        url: "servletprogreso",
        data: { codinv: codinv },
        dataType: "json",
        timeout: 10000,
      });

      if (!response.success) {
        throw new Error(
          response.message || "Error en la respuesta del servidor"
        );
      }

      // Actualizar el objeto de barras de progreso
      progressBars[codinv] = {
        porcentaje: response.progresoGlobal || 0,
        detalle: response.detalleFarmacias || [],
        timestamp: new Date().getTime(),
      };

      // Buscar y actualizar todas las instancias de esta barra en la tabla
      $(`div[data-codinv="${codinv}"] .progress-bar`).each(function () {
        const $bar = $(this);
        const porcentaje = progressBars[codinv].porcentaje;

        // Configuración de la barra
        $bar
          .css("width", `${porcentaje}%`)
          .removeClass("bg-primary bg-success bg-danger bg-warning bg-info")
          .addClass(
            porcentaje >= 100
              ? "bg-success"
              : porcentaje >= 70
              ? "bg-primary"
              : porcentaje >= 30
              ? "bg-info"
              : "bg-warning"
          );

        // Configuración del texto
        const $text = $bar.find(".progress-text");
        $text.text(
          porcentaje >= 100 ? "✓ Completado" : `${porcentaje.toFixed(1)}%`
        );

        // Asegurar contraste del texto
        if (porcentaje < 15) {
          $text.css({
            color: "#333", // Texto oscuro para barras con poco progreso
            "font-weight": "bold",
            "margin-left": "5px", // Separar del borde
          });
        } else {
          $text.css({
            color: "#fff", // Texto blanco para barras con suficiente progreso
            "font-weight": "normal",
            "margin-left": "0",
          });
        }
      });
    } catch (error) {
      console.error(`Error al actualizar progreso para ${codinv}:`, error);
      $(`div[data-codinv="${codinv}"] .progress-bar`).each(function () {
        $(this)
          .removeClass("bg-primary bg-success bg-info bg-warning")
          .addClass("bg-danger")
          .css("width", "100%")
          .find(".progress-text")
          .text("Error")
          .css("color", "#fff");
      });
    }
  }

  // Función para inicializar DataTable
  function inicializarDataTable() {
    if ($.fn.DataTable.isDataTable("#tabla")) {
      tabla.destroy();
      $("#tabla").empty();
    }

    tabla = $("#tabla").DataTable({
      lengthChange: false,
      order: [[0, "desc"]],
      language: {
        // ... (configuración de idioma igual que antes)
      },
      ajax: {
        url: "CRUDInventario?opcion=1",
        type: "POST",
      },
      columns: [
        { data: "codinv" },
        { data: "desinv" },
        {
          data: "codinv",
          render: function (data, type, row) {
            if (type !== "display") return data;

            let html = `
  <div class="d-flex align-items-center gap-2">
    <!-- Grupo de botones principales -->
    <div class="d-flex align-items-center gap-1">`;

            // Botones principales
            if (row.estinv === "A") {
              html += `
      <button class="btn btn-primary btn-sm entrar" data-codinv="${data}" title="Editar">
        <i class="fa fa-edit"></i>
      </button>`;

              if (sup) {
                html += `
      <button class="btn btn-primary btn-sm cerrar" data-codinv="${data}" title="Cerrar inventario">
        <i class="fa fa-door-closed"></i>
      </button>`;
              }
            }

            // Dropdown de reportes
            html += `
      <div class="btn-group">
        <button class="btn btn-primary btn-sm dropdown-toggle" type="button" 
                data-bs-toggle="dropdown" aria-expanded="false" title="Reportes">
          <i class="fa fa-file"></i>
        </button>
        <div class="dropdown-menu dropdown-menu-end">
          <button class="dropdown-item consolidadoproducto" data-codinv="${data}">
            <i class="fa fa-boxes me-2"></i>Consolidado por Producto
          </button>
          <button class="dropdown-item consolidado2" data-codinv="${data}">
            <i class="fa fa-barcode me-2"></i>Consolidado por Lote
          </button>
          <button class="dropdown-item consolidado" data-codinv="${data}">
            <i class="fa fa-users me-2"></i>Consolidado por Usuario
          </button>
        </div>
      </div>

      <button type="button" class="btn btn-primary btn-sm" 
              data-bs-toggle="modal" data-bs-target="#modalAvance"
              data-codinv="${data}" title="Ver detalles">
        <i class="fa fa-list-check me-1"></i>Detalles
      </button>
    </div>`;

            // Barra de progreso solo para inventarios activos
            if (row.estinv === "A") {
              html += `
                <div class="ms-2 progress-container" data-codinv="${data}">
                  <div class="d-flex align-items-center gap-1">
                    <div class="progress flex-grow-1" style="height: 20px; min-width: 80px;">
                      <div class="progress-bar progress-bar-striped bg-primary" 
                           role="progressbar" style="width: 0%" 
                           aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
                        <span class="progress-text small" style="display: inline-block; max-width: 50px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">0%</span>
                      </div>
                    </div>
                    <button class="btn btn-sm btn-outline-primary refresh-progress p-1" title="Actualizar progreso" style="line-height: 1;">
                      <i class="fa fa-sync-alt fs-6"></i>
                    </button>
                  </div>
                </div>`;
            }

            html += `</div>`;

            return html;
          },
        },
      ],
      createdRow: function (row, data) {
        // Si el inventario está activo, iniciar la carga del progreso
        if (data.estinv === "A") {
          // Usar setTimeout para permitir que el DOM se renderice primero
          setTimeout(() => {
            actualizarBarraProgreso(data.codinv);
          }, 100);
        }
      },
      drawCallback: function () {
        // Manejar eventos de actualización
        $(".refresh-progress")
          .off("click")
          .on("click", function (e) {
            e.stopPropagation();
            const codinv = $(this).closest("[data-codinv]").data("codinv");
            const $progressBar = $(this)
              .siblings(".progress")
              .find(".progress-bar");

            $progressBar
              .removeClass("bg-success bg-danger")
              .addClass("bg-primary")
              .css("width", "0%")
              .find(".progress-text")
              .text("Actualizando...");

            actualizarBarraProgreso(codinv);
          });

        $('button[data-bs-target="#modalAvance"]')
          .off("click")
          .on("click", function () {
            const codinv = $(this).data("codinv");
            // Aquí puedes agregar lógica adicional si necesitas
            // hacer algo con el codinv cuando se abre el modal
            console.log("Mostrar lista para inventario:", codinv);
          });
      },
    });
  }

  $.fn.validarSession();
  inicializarDataTable();

  // Evento para agregar inventario
  $("#agregar")
    .off("click")
    .on("click", function () {
      $("#modalagregar").modal("show");
    });

  // Eventos de la tabla
  $("#tabla").on("click", ".entrar", function () {
    const codinv = $(this).data("codinv");
    localStorage.setItem("inventariocodinv", codinv);
    $("#contenido").load("inventarioalmacen.html");
  });

  $("#tabla").on("click", ".consolidado", function () {
    const codinv = $(this).data("codinv");
    localStorage.setItem("inventariocodinv", codinv);
    $("#contenido").load("consolidadoinventario.html");
  });

  $("#tabla").on("click", ".consolidado2", function () {
    const codinv = $(this).data("codinv");
    localStorage.setItem("inventariocodinv", codinv);
    localStorage.setItem("inventariocodalm", "");
    localStorage.setItem("inventarioant", "");
    $("#contenido").load("consolidadoinventarioagrupado.html");
  });

  $("#tabla").on("click", ".consolidadoproducto", function () {
    const codinv = $(this).data("codinv");
    localStorage.setItem("inventariocodinv", codinv);
    localStorage.setItem("inventariocodalm", "");
    localStorage.setItem("inventarioant", "");
    $("#contenido").load("consolidadoinventarioproducto.html");
  });

  $("#tabla").on("click", ".cerrar", function () {
    const codinv = $(this).data("codinv");

    Swal.fire({
      title: "¿Desea Cerrar el proceso de inventario?",
      text: "Este proceso no se puede deshacer.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Cerrar Inventario",
      cancelButtonText: "Cancelar",
    }).then((result) => {
      if (result.isConfirmed) {
        $.getJSON(
          "CRUDInventario",
          { codinv: codinv, opcion: 6 },
          function (data) {
            if (data.resultado === "ok") {
              tabla.ajax.reload(null, false);
              Toast.fire({
                icon: "success",
                title: "Cerrado correctamente.",
              });
            } else {
              Toast.fire({
                icon: "error",
                title:
                  "No se ha podido cerrar el inventario debido a un error del sistema.",
              });
            }
          }
        ).fail(function () {
          Toast.fire({
            icon: "error",
            title:
              "No se ha podido cerrar el inventario debido a un error de conexión con el servidor.",
          });
        });
      }
    });
  });

  // Evento para confirmar agregar inventario
  $("#confirmaragregar")
    .off("click")
    .on("click", function () {
      const nomb = $("#nombre").val();
      if (!nomb) {
        Toast.fire({
          icon: "error",
          title: "Agregue un nombre correcto.",
        });
        return;
      }

      // Validar que se haya seleccionado un tipo de inventario
      const selectedInventario = $("input[name='inventarioType']:checked");
      if (!selectedInventario.length) {
        Toast.fire({
          icon: "error",
          title: "Seleccione un tipo de inventario.",
        });
        return;
      }

      // Obtener el valor del radio seleccionado
      const tipoInventario = selectedInventario.val();

      // Enviar solo el tipo seleccionado (ya no usamos "captura" y "direccionado" como booleanos separados)
      $.getJSON(
        "CRUDInventario",
        {
          opcion: 2,
          nomb: nomb,
          tipoInventario: tipoInventario, // Enviamos "estatico" o "dinamico" directamente
        },
        function (data) {
          if (data.resultado === "ok") {
            Toast.fire({
              icon: "success",
              title: "Agregado correctamente.",
            });
            tabla.ajax.reload(null, false);
            $("#modalagregar").modal("hide");
          } else if (data.mensaje === "nosession") {
            $.fn.validarSession();
          } else {
            Toast.fire({
              icon: "error",
              title: "Error al agregar.",
            });
          }
        }
      );
    });

  // Configurar recarga
  $("#recargar").click(async function (e) {
    $("#contenido").load("inventario.html");
  });
});
