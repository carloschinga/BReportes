$(document).ready(function () {
  let sup = false;
  $.fn.validarSession = function () {
    $.getJSON("validarsesion", function (data) {
      if (data.resultado === "ok") {
        $("#lblUsuario").text(data.logi);
        if (data.grucod === "SUPINV") {
          $("#agregar").show();
          sup = true;
        }
      } else {
        $(location).attr("href", "index.html");
      }
    }).fail(function (jqXHR, textStatus, errorThrown) {
      $(location).attr("href", "index.html");
    });
  };
  $.fn.validarSession();

  let codinv = localStorage.getItem("inventariocodinv");
  let codalm = localStorage.getItem("inventariocodalm");
  if (codalm === null || codalm === undefined) {
    codalm = "";
  }
  let ant = localStorage.getItem("inventarioant");
  if (ant === null || ant === undefined) {
    ant = "";
  }

  // Mostrar código del inventario
  $("#codigoInventario").text(codinv);

  $.getJSON("CRUDFaAlmacenes?opcion=2", function (data) {
    if (data.resultado === "ok") {
      let almacen = $("#almacen");
      almacen.empty();
      almacen.append('<option value="">TODOS</option>');
      $.each(data.data, function (key, value) {
        almacen.append(
          '<option value="' + value.codalm + '">' + value.desalm + "</option>"
        );
      });
      almacen.val(codalm);
    } else {
      if (data.mensaje === "nosession") {
        $.fn.validarSession();
      } else {
        alert("Error: Problemas con el servidor.");
      }
    }
  });

  // Función para aplicar el filtro de diferencia
  function aplicarFiltroDiferencia() {
    const filtro = $("#filtroDiferencia").val();

    $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
      if (filtro === "todos") {
        return true;
      }

      const dife = parseFloat(data[8]) || 0; // Columna dife
      const diff = parseFloat(data[9]) || 0; // Columna diff

      if (filtro === "con") {
        return dife !== 0 || diff !== 0;
      } else if (filtro === "sin") {
        return dife === 0 && diff === 0;
      }
      return true;
    });
    table.draw();
    $.fn.dataTable.ext.search.pop(); // Limpiar el filtro después de aplicarlo
  }

  // Inicializa DataTables
  const table = $("#tabla").DataTable({
    ajax: {
      url:
        "CRUDInventario?opcion=8&codinv=" +
        codinv +
        "&codalm=" +
        $("#almacen").val(),
    },
    columns: [
      { data: "codpro" },
      { data: "despro" },
      { data: "codalm" },
      { data: "cantidad_lotes" },
      { data: "cante" },
      { data: "cantf" },
      { data: "stkalm" },
      { data: "stkalm_m" },
      { data: "dife" },
      { data: "diff" },
      {
        data: "cospro",
        className: sup ? "" : "nomostrar",
        visible: false,
        render: function (data, type, row) {
          return (data * row.cante).toFixed(2);
        },
      },
    ],
    pageLength: 10,
    lengthChange: false,
    responsive: true,
    dom: "Bfrtip",
    buttons: [
      {
        extend: "excel",
        text: "Excel",
        className: "btn btn-success",
        exportOptions: {
          columns: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9], // Índices de columnas a exportar
        },
        filename:
          "Reporte_Inventario_Por_Producto_" + new Date().toLocaleDateString(),
      },
      /*
      {
        extend: "copy",
        text: "Copiar",
        className: "btn btn-info",
        exportOptions: {
          columns: ":not(.novisible),:hidden",
        },
      },
      {
        extend: "csv",
        text: "CSV",
        className: "btn btn-primary",
        exportOptions: {
          columns: ":not(.novisible),:hidden",
        },
      },
      {
        extend: "pdf",
        text: "PDF",
        className: "btn btn-danger",
        orientation: "landscape",
        pageSize: "A4",
        exportOptions: {
          columns: ":not(.novisible),:hidden",
        },
      },
      {
        extend: "print",
        text: "Imprimir",
        className: "btn btn-warning",
        exportOptions: {
          columns: ":not(.novisible),:hidden",
        },
      },
      */
    ],
  });

  // Manejar cambios en el almacén
  $("#almacen").change(function () {
    const codalm = $("#almacen").val();
    table.ajax
      .url("CRUDInventario?opcion=8&codinv=" + codinv + "&codalm=" + codalm)
      .load();
  });

  // Manejar cambios en el filtro de diferencia
  $("#filtroDiferencia").change(function () {
    aplicarFiltroDiferencia();
  });

  // Manejo del botón Volver
  $("#volver").on("click", function () {
    if (ant === "almacen") {
      $("#contenido").load("inventarioalmacendetalle.html");
    } else {
      $("#contenido").load("inventario.html");
    }
  });
  $("#recargar").on("click", function () {
    $("#contenido").load("consolidadoinventarioagrupado.html");
  });
});
