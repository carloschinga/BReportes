$(document).ready(function () {
  let tabla;
  let productosSetear = [];
  let productosUbicar = [];

  // Tooltips para encabezados
  $("#tabla thead th").tooltip({
    placement: "top",
    trigger: "hover",
  });

  function mostrarAlerta(mensaje, icono) {
    Swal.fire({
      toast: true,
      position: "top-end",
      icon: icono,
      title: mensaje,
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true,
      customClass: { popup: "swal2-sm" },
    });
  }

  function initDataTable() {
    tabla = $("#tabla").DataTable({
      ajax: {
        url: "sincronizacion",
        type: "GET",
        dataSrc: function (json) {
          // Clasificar productos para cada acción
          productosSetear = json.filter(
            (item) => item.qtymov <= item.cante && item.qtymov_m <= item.cantf
          );
          productosUbicar = json.filter(
            (item) => item.qtymov > item.cante || item.qtymov_m > item.cantf
          );

          // Habilitar/deshabilitar botones según corresponda
          $("#btnSetearTodo").prop("disabled", productosSetear.length === 0);
          $("#btnUbicarTodo").prop("disabled", productosUbicar.length === 0);

          return json;
        },
        error: function () {
          mostrarAlerta("Error al cargar la tabla", "error");
        },
      },
      columns: [
        { data: "nombre" },
        { data: "lote" },
        { data: "qtymov" },
        { data: "qtymov_m" },
        { data: "cante" },
        { data: "cantf" },
        {
          data: null,
          render: function (data, type, row) {
            if (row.qtymov > row.cante || row.qtymov_m > row.cantf) {
              return '<span class="badge bg-warning">Requiere sincronizar</span>';
            } else {
              return '<span class="badge bg-info">Requiere sincronizar</span>';
            }
          },
        },
      ],
      pageLength: 10,
      language: {
        url: "https://cdn.datatables.net/plug-ins/1.10.21/i18n/Spanish.json",
      },
      scrollX: true,
    });
  }

  // Botón para setear todo automáticamente
  $("#btnSetearTodo").click(function () {
    if (productosSetear.length === 0) {
      mostrarAlerta("No hay productos para sincronizar", "info");
      return;
    }

    Swal.fire({
      title: "¿Sincronizar todos los productos?",
      text: `Se sincronizarán ${productosSetear.length} productos con respecto a LolFarm`,
      icon: "question",
      showCancelButton: true,
      confirmButtonText: "Sí, sincronizar",
      cancelButtonText: "Cancelar",
    }).then((result) => {
      if (result.isConfirmed) {
        setearTodo();
      }
    });
  });

  // Botón para ubicar todo automáticamente
  $("#btnUbicarTodo").click(function () {
    if (productosUbicar.length === 0) {
      mostrarAlerta("No hay productos para ubicar", "info");
      return;
    }

    Swal.fire({
      title: "¿Ubicar todos los productos?",
      text: `Se ubicarán ${productosUbicar.length} productos en nuevas ubicaciones`,
      icon: "question",
      showCancelButton: true,
      confirmButtonText: "Sí, ubicar",
      cancelButtonText: "Cancelar",
    }).then((result) => {
      if (result.isConfirmed) {
        ubicarTodo();
      }
    });
  });

  function setearTodo() {
    if (productosSetear.length === 0) {
      mostrarAlerta("No hay productos para sincronizar", "info");
      return;
    }

    const $btn = $("#btnSetearTodo");
    $btn
      .prop("disabled", true)
      .html(
        '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Procesando...'
      );

    // Preparar datos para el seteo masivo
    const productos = productosSetear.map((p) => ({
      lote: p.lote,
      codpro: p.codpro,
      nombre: p.nombre,
      qtymov: p.cante - p.qtymov,
      qtymov_m: p.cantf - p.qtymov_m,
    }));

    console.log({ productos });

    $.ajax({
      type: "PUT",
      url: "seteoMasivo",
      contentType: "application/json",
      data: JSON.stringify(productos),
      success: function (response) {
        let message = `Procesados ${
          response.exitosos + response.fallidos
        } productos`;
        if (response.exitosos > 0) {
          message += `, ${response.exitosos} actualizados correctamente`;
        }
        if (response.fallidos > 0) {
          message += `, ${response.fallidos} fallados`;
        }

        Swal.fire({
          icon: response.success === "ok" ? "success" : "warning",
          title: "Resultado de la operación",
          html:
            `<div>${message}</div>` +
            (response.fallidos > 0
              ? `<div class="text-danger mt-2">Revise el reporte PDF para detalles de los errores</div>`
              : ""),
          confirmButtonColor: "#17a2b8",
          showDenyButton: true,
          denyButtonText: "Descargar Reporte Completo",
          confirmButtonText: "Aceptar",
          allowOutsideClick: false,
        }).then((result) => {
          if (result.isDenied && response.pdf) {
            const link = document.createElement("a");
            link.href = "data:application/pdf;base64," + response.pdf;
            link.download = `reporte_sincronizacion-_${new Date()
              .toISOString()
              .slice(0, 10)}.pdf`;
            link.click();
          }
          tabla.ajax.reload(null, false);
        });
      },
      error: function (xhr) {
        let errorMsg = "Error al procesar la solicitud";
        try {
          const errorJson = JSON.parse(xhr.responseText);
          errorMsg = errorJson.message || errorMsg;
        } catch (e) {
          console.error("Error parsing error response", e);
        }

        Swal.fire({
          icon: "error",
          title: "Error",
          text: errorMsg,
          confirmButtonColor: "#dc3545",
        });
      },
      complete: function () {
        $btn
          .prop("disabled", false)
          .html('<i class="fas fa-sync-alt me-1"></i> Sincronizar Todo');
      },
    });
  }

  initDataTable();

  function ubicarTodo() {
    if (productosUbicar.length === 0) {
      mostrarAlerta("No hay productos para sincronizar", "info");
      return;
    }
    const $btn = $("#btnUbicarTodo");
    $btn
      .prop("disabled", true)
      .html(
        '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Procesando...'
      );

    // Preparar datos para la ubicación masiva
    const productos = productosUbicar.map((p) => ({
      lote: p.lote,
      codpro: p.codpro,
      nombre: p.nombre,
      qtymov: p.qtymov - p.cante,
      qtymov_m: p.qtymov_m - p.cantf,
    }));

    console.log({ productos });

    $.ajax({
      type: "PUT",
      url: "ubicarMasivo",
      contentType: "application/json",
      data: JSON.stringify(productos),
      success: function (response) {
        let message = `Procesados ${
          response.exitosos + response.fallidos
        } productos`;
        if (response.exitosos > 0) {
          message += `, ${response.exitosos} actualizados correctamente`;
        }
        if (response.fallidos > 0) {
          message += `, ${response.fallidos} fallados`;
        }

        Swal.fire({
          icon: response.success === "ok" ? "success" : "warning",
          title: "Resultado de la operación",
          html:
            `<div>${message}</div>` +
            (response.fallidos > 0
              ? `<div class="text-danger mt-2">Revise el reporte PDF para detalles de los errores</div>`
              : ""),
          confirmButtonColor: "#17a2b8",
          showDenyButton: true,
          denyButtonText: "Descargar Reporte Completo",
          confirmButtonText: "Aceptar",
          allowOutsideClick: false,
        }).then((result) => {
          if (result.isDenied && response.pdf) {
            const link = document.createElement("a");
            link.href = "data:application/pdf;base64," + response.pdf;
            link.download = `reporte_sincronizacion+_${new Date()
              .toISOString()
              .slice(0, 10)}.pdf`;
            link.click();
          }
          tabla.ajax.reload(null, false);
        });
      },
      error: function (xhr) {
        let errorMsg = "Error al procesar la solicitud";
        try {
          const errorJson = JSON.parse(xhr.responseText);
          errorMsg = errorJson.message || errorMsg;
        } catch (e) {
          console.error("Error parsing error response", e);
        }

        Swal.fire({
          icon: "error",
          title: "Error",
          text: errorMsg,
          confirmButtonColor: "#dc3545",
        });
      },
      complete: function () {
        $btn
          .prop("disabled", false)
          .html('<i class="fas fa-sync-alt me-1"></i> Sincronizar a -');
      },
    });
  }
});
