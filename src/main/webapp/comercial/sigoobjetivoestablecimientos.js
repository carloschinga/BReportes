$(document).ready(function () {
  let entero;
  let soles;
  let tipo;
  let codobj = localStorage.getItem("codobj");

  $.getJSON("CRUDsigoobje?opcion=7&codobj=" + codobj, function (data) {
    tipo = data.tipo;
    if (tipo === "p") {
      $("#cantidad").val("Soles.");
    } else {
      $("#cantidad").val("Unidades.");
    }

    let table = $("#tabla").DataTable({
      language: {
        // Configuración de idioma
      },
      paging: false,
      info: false,
      searching: false,
      ajax: {
        url: "CRUDsigoobje",
        type: "GET",
        data: function (d) {
          d.opcion = 8;
          d.codobj = codobj;
        },
      },
      columns: [
        { data: "nombre" },
        {
          data: null,
          render: function (data, type, row) {
            if (row.monto) {
              return `<div class="input-group input-group-sm">
                                <span class="input-group-text">S/.</span>
                                <input class="form-control cant-soles" data-id="${row.codigo}" type="number" value="${row.monto}">
                            </div>`;
            } else if (row.cantidad) {
              return `<input class="form-control cant-cantidad" data-id="${row.codigo}" type="number" value="${row.cantidad}">`;
            } else {
              return tipo === "p"
                ? `<div class="input-group input-group-sm">
                                <span class="input-group-text">S/.</span>
                                <input class="form-control cant-soles" data-id="${row.codigo}" type="number" value="">
                            </div>`
                : `<input class="form-control cant-cantidad" data-id="${row.codigo}" type="number" value="">`;
            }
          },
        },
        {
          data: "porc_estra", // % Estra (editable)
          render: function (data, type, row) {
            return `<input class="form-control form-control-sm porc-estra" data-id="${
              row.codigo
            }" type="number" value="${
              row.porc_estra || 0
            }" step="0.01" min="0" max="100">`;
          },
        },
        {
          data: null, // Venta Esta (calculado)
          render: function (data, type, row) {
            let vta = parseFloat(row.monto) || 0;
            let porc = parseFloat(row.porc_estra) || 0;
            let ventaEsta = ((vta * porc) / 100).toFixed(2);
            return `<span class="venta-esta" data-id="${row.codigo}">${ventaEsta}</span>`;
          },
          orderable: false,
        },
        {
          data: null, // Venta No EE (calculado)
          render: function (data, type, row) {
            let vta = parseFloat(row.monto) || 0;
            let porc = parseFloat(row.porc_estra) || 0;
            let ventaNoEE = (vta - (vta * porc) / 100).toFixed(2);
            return `<span class="venta-noee" data-id="${row.codigo}">${ventaNoEE}</span>`;
          },
          orderable: false,
        },
        {
          data: null,
          render: function (data, type, row) {
            let value = row.monto
              ? (row.monto * 100) / row.sumsoles
              : row.cantidad
              ? (row.cantidad * 100) / row.sumentero
              : "";
            if (value !== "") {
              value = value.toFixed(2);
            }
            return `<div class="input-group input-group-sm">
                            <input class="form-control cant-porcentaje" data-id="${row.codigo}" type="number" value="${value}">
                            <span class="input-group-text">%</span>
                        </div>`;
          },
        },
      ],
      initComplete: function (settings, json) {
        actualizarTotal();
      },
    });

    // Actualiza el total en el footer
    function actualizarTotal() {
      let totalSoles = 0;
      let totalCantidad = 0;

      // Calcular los totales de Soles y Cantidad
      $("#tabla .cant-soles").each(function () {
        totalSoles += parseFloat($(this).val()) || 0;
      });
      $("#tabla .cant-cantidad").each(function () {
        totalCantidad += parseFloat($(this).val()) || 0;
      });

      // Calcular y actualizar los porcentajes para cada fila
      $("#tabla tr").each(function () {
        let $row = $(this);
        let $inputSoles = $row.find(".cant-soles");
        let $inputCantidad = $row.find(".cant-cantidad");
        let $inputPorcentaje = $row.find(".cant-porcentaje");

        let newPercentage = 0;

        // Si hay soles, calcular el porcentaje basado en soles
        if (totalSoles > 0) {
          let rowSoles = parseFloat($inputSoles.val()) || 0;
          newPercentage = ((rowSoles * 100) / totalSoles).toFixed(2);
        }
        // Si no hay soles, calcular el porcentaje basado en cantidad
        else if (totalCantidad > 0) {
          let rowCantidad = parseFloat($inputCantidad.val()) || 0;
          newPercentage = ((rowCantidad * 100) / totalCantidad).toFixed(2);
        }

        // Actualizar el porcentaje en la fila
        $inputPorcentaje.val(newPercentage);
      });

      // Actualizar el total de Monto Unidad
      $("#totalMontoUnidad").text(
        totalCantidad > 0
          ? totalCantidad.toFixed(0) + " unid."
          : "S/. " + totalSoles.toFixed(2)
      );

      // Calcular el porcentaje total acumulado
      let totalPorcentaje = 0;
      $("#tabla .cant-porcentaje").each(function () {
        totalPorcentaje += parseFloat($(this).val()) || 0;
      });

      $("#totalPorcentaje").text(totalPorcentaje.toFixed(2) + "%");
    }

    // Detecta cambios y actualiza los valores
    $("#tabla").on(
      "input",
      ".cant-soles, .cant-cantidad, .cant-porcentaje, .porc-estra",
      function () {
        let $input = $(this);
        let id = $input.data("id");

        // Si se ingresa Soles
        if ($input.hasClass("cant-soles")) {
          let newSoles = parseFloat($input.val());
          if ($input.val() === "") {
            $(`#tabla .cant-porcentaje[data-id="${id}"]`).val(""); // Si está vacío, borra el porcentaje
          } else if (!isNaN(newSoles)) {
            // No es necesario actualizar el porcentaje directamente, se recalcula en actualizarTotal
          }
        }
        // Si se ingresa Cantidad
        else if ($input.hasClass("cant-cantidad")) {
          let newCantidad = parseFloat($input.val());
          if ($input.val() === "") {
            $(`#tabla .cant-porcentaje[data-id="${id}"]`).val(""); // Si está vacío, borra el porcentaje
          } else if (!isNaN(newCantidad)) {
            // No es necesario actualizar el porcentaje directamente, se recalcula en actualizarTotal
          }
        }
        // Si se ingresa Porcentaje
        else if ($input.hasClass("cant-porcentaje")) {
          let newPercentage = parseFloat($input.val());
          if ($input.val() === "") {
            $(`#tabla .cant-soles[data-id="${id}"]`).val(""); // Si está vacío, borra soles
            $(`#tabla .cant-cantidad[data-id="${id}"]`).val(""); // Si está vacío, borra cantidad
          } else if (!isNaN(newPercentage)) {
            // No es necesario actualizar el porcentaje directamente, se recalcula en actualizarTotal
          }
        }
        // Si se cambia el porcentaje Estra
        else if ($input.hasClass("porc-estra")) {
          // Actualizar las ventas para esta fila
          actualizarVentas(id);
        }

        // Llamar a la función para actualizar los totales
        actualizarTotal();

        // Si el cambio fue en cant-soles o cant-cantidad, actualizar también las ventas
        if ($input.hasClass("cant-soles") || $input.hasClass("cant-cantidad")) {
          actualizarVentas(id);
        }
      }
    );
  });

  $("#tabla").on("focusout", ".cant-porcentaje", function () {
    let $input = $(this);
    let id = $input.data("id");

    // Obtener el valor actualizado de la cantidad correspondiente
    if (tipo === "p") {
      let cantidadSoles = parseFloat(
        $(`#tabla .cant-soles[data-id="${id}"]`).val()
      );
      if (!isNaN(cantidadSoles) && soles) {
        let porcentajeReal = ((cantidadSoles * 100) / soles).toFixed(2);
        $input.val(porcentajeReal); // Actualizar el valor del porcentaje
      }
    } else {
      let cantidadCantidad = parseFloat(
        $(`#tabla .cant-cantidad[data-id="${id}"]`).val()
      );
      if (!isNaN(cantidadCantidad) && entero) {
        let porcentajeReal = ((cantidadCantidad * 100) / entero).toFixed(2);
        $input.val(porcentajeReal); // Actualizar el valor del porcentaje
      }
    }
  });

  $("#atras").on("click", function () {
    localStorage.setItem("codobj", codobj);
    $("#contenido").load("comercial/sigoObjetivos.html");
  });

  $("#siguiente").on("click", function () {
    let tipo;
    if (tipo === "e") {
      tipo = "entero";
    } else {
      tipo = "soles";
    }
    let resultadoJSON = [];
    $("#tabla tbody tr .cant-soles, #tabla .cant-cantidad").each(function () {
      let id = $(this).data("id");
      $row = $(this).closest("tr");
      let cantidad = parseFloat($(this).val());
      if (entero) {
        cantidad = parseFloat(cantidad.toFixed(0));
      } else {
        cantidad = parseFloat(cantidad.toFixed(2));
      }
      let porc_estra = parseFloat($row.find(".porc-estra").val()) || 0;

      if (!isNaN(cantidad)) {
        resultadoJSON.push({
          siscod: id,
          cantidad: cantidad,
          porc_estra: porc_estra,
        });
      }
    });

    console.log({ resultadoJSON });

    $.ajax({
      url: "CRUDsigoobje?opcion=9&codobj=" + codobj + "&tipo=" + tipo,
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(resultadoJSON), // Enviar resultadoJSON como JSON en el cuerpo
      success: function (data) {
        data = JSON.parse(data);
        if (data.resultado === "ok") {
            
            
          $("#contenido").load("sigoObjetivos.html");
          if (window.Swal) {
                    Swal.fire({
                        icon: "success",
                        title: "Se guardaron los datos correctamente",
                        timer: 2000,
                        showConfirmButton: false,
                    });
                } else {
                    alert("Se guardaron los datos correctamente");
                }
          
        } else {
          if (data.mensaje === "nosession") {
            $.fn.validarSession();
          } else {
            alert("Error al cargar los datos");
          }
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        alert("Error de conexion con el servidor");
      },
    });
  });

  $("#tabla").on("change", ".porc-estra", ".cant-soles", function () {
    let tr = $(this).closest("tr");

    let soles = parseFloat(tr.find(".cant-soles").val()) || 0;
    let porcentajeEstra = parseFloat(tr.find(".porc-estra").val()) || 0;
    let ventaEsta = ((soles * porcentajeEstra) / 100).toFixed(2);
    let ventaNoEE = (soles - (soles * porcentajeEstra) / 100).toFixed(2);

    console.log({
      soles: soles,
      porcentajeEstra: porcentajeEstra,
      ventaEsta: ventaEsta,
      ventaNoEE: ventaNoEE,
    });

    // Actualizar las celdas de venta Esta y No EE
    tr.find(".venta-esta").text(ventaEsta);
    tr.find(".venta-noee").text(ventaNoEE);
  });
});
