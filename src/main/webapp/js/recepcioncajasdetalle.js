$(document).ready(function () {
  let searchBox = $("#search-qr");
  let jsonData;
  let buscado = false;
  let boton;
  let cambio = false;
  let fecini;
  let completado = false;
  let productData = [];
  let productTable;
  let caja = localStorage.getItem("caja_caja");
  let orden = localStorage.getItem("caja_orden");
  let datosParaEnviar = []; //productos normales
  let datosExtraParaEnviar = []; //productos extra agregados
  let jsonEliminados = []; //productos extra eliminados
  let jsonbulto;
  $.fn.validarSession = function () {
    $.getJSON("validarsesion", function (data) {
      if (data.resultado === "ok") {
      } else {
        $(location).attr("href", "index.html");
      }
    }).fail(function (jqXHR, textStatus, errorThrown) {
      $(location).attr("href", "index.html");
    });
  };
  $.fn.validarSession();
  $("#titulo").text(
    "Recepción de Picking - Bulto: " + localStorage.getItem("caja_caja")
  );
  $("#titulo2").text(
    "Recepción de Picking - Bulto: " + localStorage.getItem("caja_caja")
  );
  $.fn.listarsecuencias = function () {
    let caja = localStorage.getItem("caja_caja");
    let orden = localStorage.getItem("caja_orden");
    $.getJSON(
      "picking",
      { opcion: 48, caja: caja, orden: orden },
      function (data) {
        // ant 19 nuev 48
        if (data.resultado === "ok") {
          Swal.fire({
            title: "Secuencias",
            text: data.data.join(", "),
            icon: "info",
          });
        } else {
          if (data.mensaje === "nosession") {
            $.fn.validarSession();
          } else {
            alert("Ocurrio un problema al buscar las secuencias");
          }
        }
      }
    );
  };
  let modalProductTable = $("#modal-product-table").DataTable({
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
        sortAscending: ": activar para ordenar la columna de forma ascendente",
        sortDescending:
          ": activar para ordenar la columna de forma descendente",
      },
    },
    columns: [
      { data: "codpro" },
      { data: "despro" },
      {
        data: "qr",
        render: function (data, type, row) {
          return data ? data : "";
        },
      },
      {
        data: null,
        className: "dt-center",
        orderable: false,
        render: function (data, type, row) {
          return '<button type="button" class="btn btn-success btn-add btn-sm">+</button>';
        },
      },
    ],
    paging: true, // Activar paginación si es necesario
    searching: true, // Activar búsqueda en el modal
    info: true, // Mostrar información de registros
  });

  // Recuperar el JSON del servidor
  $.getJSON("CRUDProductos?opcion=15", function (data) {
    //picking ant 10 nuev 49, se puso 9, devuelta 10
    productData = data.data;
    modalProductTable.rows.add(productData).draw();
  });
  productTable = $("#reference-table").DataTable({
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
      search: "Busqueda en el Bulto:",
      zeroRecords: "No se encontraron coincidencias",
      paginate: {
        first: "Primero",
        last: "Última",
        next: "Siguiente",
        previous: "Anterior",
      },
      aria: {
        sortAscending: ": activar para ordenar la columna de forma ascendente",
        sortDescending:
          ": activar para ordenar la columna de forma descendente",
      },
    },

    dom: "rtp",
    ajax: {
      url: "picking",
      type: "GET",
      data: function (d) {
        d.opcion = 49; //picking ant 10 nuev 49
        d.ord = orden;
        d.caja = caja;
      },
      dataSrc: function (json) {
        if (json.resultado === "ok") {
          secuenciastabla.ajax.reload();
          if (json.completado.avance === 100) {
            if (cambio && !completado) {
              completado = true;
              cambio = false;
              Swal.fire({
                title: "¡Recepcion Completada!",
                text: "¡Todos los productos ingresados!",
                icon: "success",
                showCancelButton: true,
                confirmButtonText: "Mostrar Secuencias",
                cancelButtonText: `Cerrar`,
              }).then((result) => {
                /* Read more about isConfirmed, isDenied below */
                if (result.isConfirmed) {
                  $.fn.listarsecuencias();
                }
              });
            }
          } else {
            completado = false;
          }
          fecini = json.fecha;
          jsonbulto = json.data;
          return json.data; // Devuelve el array de datos a DataTables
        } else {
          alert("Error en la recepción de los datos");
          return []; // Retorna una tabla vacía si hubo un error
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        if (textStatus === "abort") {
          // Ignorar el error si es de tipo "abort"
          console.log("Solicitud abortada, no se muestra alerta.");
        } else {
          console.log("Error en la solicitud: " + textStatus, errorThrown);
          alert(
            "Hubo un problema al cargar los datos. Por favor, intenta de nuevo."
          );
        }
      },
    },
    paging: false,
    info: false,
    columnDefs: [
      {
        targets: ".no-display", // Aplica a las columnas con la clase 'no-display'
        visible: false, // Oculta estas columnas en la tabla
      },
    ],
    columns: [
      { data: "codpro" },
      { data: "Descripcion" },
      { data: "Lote" },
      {
        data: "fecven",
        render: function (data, type, row) {
          const [año, mes, día] = data.split("-");
          return `${día}-${mes}-${año.slice(2)}`;
        },
      },
      {
        data: "canter",
        render: function (data, type, row) {
          return `<input type="number" style="width: 3em;" class="cante-input" value="${
            data || 0
          }"
        data-numitm="${row.numitm}" data-invnum="${
            row.Secuencia
          }" data-cant="e" data-ant="${data || 0}">`;
        },
      },
      {
        data: "cantfr",
        render: function (data, type, row) {
          if (row.stkfra === 1) {
            return "";
          } else {
            return `<input type="number" style="width: 3em;" class="cantf-input" value="${
              data || 0
            }"
        data-numitm="${row.numitm}" data-invnum="${
              row.Secuencia
            }" data-cant="f" data-ant="${data || 0}">`;
          }
        },
      },
    ],
    rowCallback: function (row, data, dataIndex) {
      let objcanter = parseInt(data.canter ?? 0, 10);
      let cantecaja = parseInt(data.cantecaja ?? 0, 10);
      let cantfr = parseInt(data.cantfr ?? 0, 10);
      let cantfcaja = parseInt(data.cantfcaja ?? 0, 10);
      if (objcanter === cantecaja && cantfr === cantfcaja) {
        $(row).addClass("table-success");
      } else if (data.checkenvio === "S") {
        $(row).addClass("table-warning");
      } else {
        $(row).addClass("table-danger");
      }
    },
    drawCallback: function () {
      if (buscado) {
        buscado = false;
        setTimeout(function () {
          $(productTable.cell(boton, 4).node()).find("input").focus();
        }, 0);
      }
      $(".cante-input, .cantf-input").prop("disabled", false);
    },
  });
  productTable.columns(".no-display").visible(false, false);
  let secuenciastabla = $("#example").DataTable({
    searching: false,
    paging: false,
    info: false,
    ajax: {
      url: "picking",
      type: "GET",
      data: function (d) {
        d.opcion = 50; //ant 26 - nuev 50
        d.ord = orden;
        d.caja = caja;
      },
    },
    language: {
      decimal: "",
      emptyTable: "No hay datos",
      info: "Mostrando desde el _START_ al _END_ del total de _TOTAL_ registros",
      infoEmpty: "Mostrando desde el 0 al 0 del total de 0 registros",
      infoFiltered: "(Filtrados del total de _MAX_ registros)",
      infoPostFix: "",
      thousands: ",",
      lengthMenu: "Mostrar _MENU_ registros por página",
      loadingRecords: "Cargando...",
      processing: "Procesando...",
      zeroRecords: "No se ha encontrado nada a través de ese filtrado.",
      paginate: {
        first: "Primero",
        last: "Última",
        next: "Siguiente",
        previous: "Anterior",
      },
      aria: {
        sortAscending: ": activar para ordenar la columna de forma ascendente",
        sortDescending:
          ": activar para ordenar la columna de forma descendente",
      },
    },
    columns: [
      {
        data: "invnum",
        render: function (data, type, row) {
          return data ? data : "";
        },
      },
      {
        data: "avance",
        render: function (data, type, row) {
          return data ? data.toFixed(2) + "%" : "0%";
        },
      },
      {
        data: "conform",
        render: function (data, type, row) {
          return data ? data + " / " + row.total : "0 / " + row.total;
        },
      },
    ],
    rowCallback: function (row, data, dataIndex) {
      if (data.avance === 100) {
        $(row).addClass("table-success");
      }
    },
  });
  function actualizarSecuenciasLocal() {
    // Agrupar los datos por invnum y calcular avance y conformidad
    let groupedData = [];
    let allChecked = true;
    let grouped = jsonbulto.reduce((acc, item) => {
      if (!acc[item.Secuencia]) {
        acc[item.Secuencia] = {
          invnum: item.Secuencia,
          total: 0,
          checkenvioS: 0,
          conforms: 0,
        };
      }
      acc[item.Secuencia].total += 1;
      if (item.checkenvio === "S") {
        acc[item.Secuencia].checkenvioS += 1;
      } else {
        allChecked = false; // Si hay un elemento que no es "S", no cumple la condición
        completado = false;
      }

      let objcanter = parseInt(item.canter ?? 0, 10);
      let cantecaja = parseInt(item.cantecaja ?? 0, 10);
      let cantfr = parseInt(item.cantfr ?? 0, 10);
      let cantfcaja = parseInt(item.cantfcaja ?? 0, 10);
      if (objcanter === cantecaja && cantfr === cantfcaja) {
        acc[item.Secuencia].conforms += 1;
      }

      return acc;
    }, {});

    // Calcular `avance` y `conform` para cada grupo de invnum
    for (let invnum in grouped) {
      let group = grouped[invnum];
      group.avance = (group.checkenvioS / group.total) * 100;
      group.conform = `${group.conforms}`;
      groupedData.push(group);
    }

    // Limpiar la tabla y actualizarla con los datos locales
    let table = $("#example").DataTable();
    table.clear();
    table.rows.add(groupedData); // Añadir los datos calculados
    table.draw(false); // Dibujar la tabla con los datos actualizados
    if (allChecked) {
      console.log("entro");
      if (!completado) {
        completado = true;
        cambio = false;
        Swal.fire({
          title: "¡Recepcion Completada!",
          text: "¡Todos los productos ingresados!",
          icon: "success",
          showCancelButton: true,
          confirmButtonText: "Guardar Recepcion",
          cancelButtonText: `Cerrar`,
        }).then((result) => {
          /* Read more about isConfirmed, isDenied below */
          if (result.isConfirmed) {
            guardar();
          }
        });
      }
    }
  }
  $.fn.actualizardata = function (input) {
    let invnum = $(input).data("invnum");
    let numitm = $(input).data("numitm");
    let cant = $(input).data("cant");
    let valor = $(input).val();

    // Buscar si ya existe un objeto con el mismo invnum y numitm en datosParaEnviar
    let registro = datosParaEnviar.find(
      (item) => item.invnum === invnum && item.numitm === numitm
    );
    let objeto = jsonbulto.find(
      (item) => item.Secuencia === invnum && item.numitm === numitm
    );
    $(input).data("ant", valor);
    if (cant === "e") {
      objeto.canter = valor === "" ? null : valor;
    } else if (cant === "f") {
      objeto.cantfr = valor === "" ? null : valor;
    }
    let objcanter = parseInt(objeto.canter ?? 0, 10);
    let cantecaja = parseInt(objeto.cantecaja ?? 0, 10);
    let cantfr = parseInt(objeto.cantfr ?? 0, 10);
    let cantfcaja = parseInt(objeto.cantfcaja ?? 0, 10);

    if (objcanter >= cantecaja && cantfr >= cantfcaja) {
      objeto.checkenvio = "S";
    } else {
      objeto.checkenvio = "N";
    }
    let fila = $(input).closest("tr");

    // Remover cualquier clase previa
    fila.removeClass("table-success table-warning table-danger");

    if (objcanter === cantecaja && cantfr === cantfcaja) {
      fila.addClass("table-success");
    } else if (objeto.checkenvio === "S") {
      fila.addClass("table-warning");
    } else {
      fila.addClass("table-danger");
    }
    actualizarSecuenciasLocal();
    if (registro) {
      // Si el objeto ya existe, solo actualizar cante o cantf según el valor de `cant`
      if (cant === "e") {
        registro.cante = valor === "" ? null : valor;
      } else if (cant === "f") {
        registro.cantf = valor === "" ? null : valor;
      }
    } else {
      // Si no existe, crear un nuevo objeto y agregarlo a datosParaEnviar
      registro = {
        invnum: invnum,
        numitm: numitm,
        cante: cant === "e" ? (valor === "" ? null : valor) : null,
        cantf: cant === "f" ? (valor === "" ? null : valor) : null,
      };
      datosParaEnviar.push(registro);
    }

    if (buscado) {
      setTimeout(function () {
        buscado = false;
        $(productTable.cell(boton, 4).node()).find("input").focus();
      }, 0);
    } else {
      $("#search-qr").val("").focus();
    }
  };
  $("#reference-table").on(
    "change keypress",
    ".cante-input, .cantf-input",
    function (e) {
      if (e.type === "keypress" && e.which !== 13) {
        return; // Ignoramos cualquier tecla que no sea "Enter"
      }
      let currentValue = $(this).val().trim();

      // Si el valor tiene 13 dígitos, lo tratamos como código de barras
      if (currentValue.length >= 7 && $.isNumeric(currentValue)) {
      } else {
        let val = $(this).val();
        let textbox = $(this);
        $(this).val($(this).data("ant"));
        borrarbusqueda();
        setTimeout(function () {
          $(this).blur();
          textbox.val(val);
          $.fn.actualizardata(textbox);
        }, 0);
      }
    }
  );
  $("#reference-table").on("input", ".cante-input, .cantf-input", function (e) {
    let currentValue = $(this).val().trim();

    if (currentValue.length > 7 && $.isNumeric(currentValue)) {
      agregarProducto(currentValue, $(this));
    }
  });
  $("#product-table").on(
    "change keypress",
    ".cante-input, .cantf-input",
    function (e) {
      if (e.type === "keypress" && e.which !== 13) {
        return; // Ignoramos cualquier tecla que no sea "Enter"
      }
      let currentValue = $(this).val().trim();

      // Si el valor tiene 13 dígitos, lo tratamos como código de barras
      if (currentValue.length > 7 && $.isNumeric(currentValue)) {
      } else {
        let val = $(this).val();
        let textbox = $(this);
        $(this).val($(this).data("ant"));
        borrarbusqueda();
        setTimeout(function () {
          $(this).blur();
          textbox.val(val);
          $.fn.actualizardataextra(textbox);
        }, 0);
      }
    }
  );
  $("#product-table").on("input", ".cante-input, .cantf-input", function (e) {
    let currentValue = $(this).val().trim();

    if (currentValue.length > 7 && $.isNumeric(currentValue)) {
      agregarProducto(currentValue, $(this));
    }
  });
  $.fn.actualizarreferencia = function () {
    productTable.ajax.reload();
  };
  $("#compare-btn").on("click", function () {
    let tableData = productTable
      .rows()
      .nodes()
      .to$()
      .map(function () {
        let row = $(this);
        return {
          codpro: row.find("td").eq(0).text(),
          despro: row.find("td").eq(1).text(),
          cante: parseFloat(row.find(".cante-input").val()),
          cantf: parseFloat(row.find(".cantf-input").val()),
        };
      })
      .get();

    let jsonProducts = jsonData.reduce((acc, item) => {
      acc[item.codpro] = {
        despro: item.Descripcion,
        cante: item.cantecaja ? item.cantecaja : 0,
        cantf: item.cantfcaja ? item.cantfcaja : 0,
      };
      return acc;
    }, {});

    let tableProducts = tableData.reduce((acc, item) => {
      acc[item.codpro] = {
        despro: item.despro,
        cante: item.cante,
        cantf: item.cantf,
      };
      return acc;
    }, {});

    let missingProducts = [];
    let extraProducts = [];
    let differentProducts = [];
    let coincidencia = [];

    for (let code in jsonProducts) {
      if (!tableProducts[code]) {
        missingProducts.push({
          codpro: code,
          despro: jsonProducts[code].despro,
        });
      } else if (
        jsonProducts[code].cante !== tableProducts[code].cante ||
        jsonProducts[code].cantf !== tableProducts[code].cantf
      ) {
        differentProducts.push({
          codpro: code,
          despro: jsonProducts[code].despro,
          json: jsonProducts[code],
          table: tableProducts[code],
        });
      } else {
        coincidencia.push({ codpro: code });
      }
    }

    for (let code in tableProducts) {
      if (!jsonProducts[code]) {
        extraProducts.push({
          codpro: code,
          despro: tableProducts[code].despro,
        });
      }
    }

    // Oculta ambos resultados inicialmente
    $("#results").hide();
    $("#equal-message").hide();
    $("#error-message").hide();

    if (
      missingProducts.length === 0 &&
      extraProducts.length === 0 &&
      differentProducts.length === 0
    ) {
      // Si no hay diferencias, muestra el mensaje de igualdad
      let caja = localStorage.getItem("caja_caja");
      let orden = localStorage.getItem("caja_orden");
      $.ajax({
        url: "picking?opcion=15&ord=" + orden + "&caja=" + caja,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(coincidencia),
        dataType: "json",
        success: function (data) {
          if (data.resultado === "ok") {
            $("#equal-message").show();
            $.fn.actualizarreferencia();
          } else {
            $("#error-message").show();
          }
        },
        error: function (xhr, status, error) {
          alert("Error con la conexion con el servidor");
        },
      });
    } else {
      $("#missing-products").html(
        missingProducts
          .map((item) => `<li>${item.codpro} - ${item.despro}</li>`)
          .join("")
      );
      $("#extra-products").html(
        extraProducts
          .map((item) => `<li>${item.codpro} - ${item.despro}</li>`)
          .join("")
      );
      $("#different-products").html(
        differentProducts
          .map(
            (item) =>
              `<li>${item.codpro} - ${item.despro}: 
                    EN EL BULTO (Cante: ${item.json.cante}, Cantf: ${item.json.cantf}), 
                    INGRESADO (Cante: ${item.table.cante}, Cantf: ${item.table.cantf})
                </li>`
          )
          .join("")
      );
      $.ajax({
        url: "picking?opcion=15&ord=" + orden + "&caja=" + caja,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(coincidencia),
        dataType: "json",
        success: function (data) {
          if (data.resultado === "ok") {
            $("#results").show();
            $.fn.actualizarreferencia();
          } else {
            $("#error-message").show();
          }
        },
        error: function (xhr, status, error) {
          alert("Error con la conexion con el servidor");
        },
      });
    }
  });
  let tabla_extra = $("#product-table").DataTable({
    searching: false,
    paging: false,
    info: false,
    ajax: {
      url: "CRUDProductosExtra",
      type: "GET",
      data: function (d) {
        d.opcion = 1;
        d.caja = caja;
      },
      error: function (jqXHR, textStatus, errorThrown) {
        if (textStatus === "abort") {
          // Ignorar el error si es de tipo "abort"
          console.log("Solicitud abortada, no se muestra alerta.");
        } else {
          console.log("Error en la solicitud: " + textStatus, errorThrown);
          alert(
            "Hubo un problema al cargar los datos. Por favor, intenta de nuevo."
          );
        }
      },
      dataSrc: function (json) {
        if (json.resultado === "ok") {
          fecini = json.fecha;
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
    language: {
      decimal: "",
      emptyTable: "No hay datos",
      info: "Mostrando desde el _START_ al _END_ del total de _TOTAL_ registros",
      infoEmpty: "Mostrando desde el 0 al 0 del total de 0 registros",
      infoFiltered: "(Filtrados del total de _MAX_ registros)",
      infoPostFix: "",
      thousands: ",",
      lengthMenu: "Mostrar _MENU_ registros por página",
      loadingRecords: "Cargando...",
      processing: "Procesando...",
      search: "Buscar:",
      zeroRecords: "No se ha encontrado nada a través de ese filtrado.",
      paginate: {
        first: "Primero",
        last: "Última",
        next: "Siguiente",
        previous: "Anterior",
      },
      aria: {
        sortAscending: ": activar para ordenar la columna de forma ascendente",
        sortDescending:
          ": activar para ordenar la columna de forma descendente",
      },
    },

    columnDefs: [
      {
        targets: ".no-display", // Aplica a las columnas con la clase 'no-display'
        visible: false, // Oculta estas columnas en la tabla
      },
    ],
    columns: [
      { data: "codpro" },
      { data: "despro" },
      {
        data: "cante",
        render: function (data, type, row) {
          return `<input type="number" style="width: 3em;" class="cante-input" value="${
            data || 0
          }"
                    data-codpro="${row.codpro}" data-cant="e" data-ant="${
            data || 0
          }">`;
        },
      },
      {
        data: "cantf",
        render: function (data, type, row) {
          if (row.stkfra === 1) {
            return "";
          } else {
            return `<input type="number" style="width: 3em;" class="cantf-input" value="${
              data || 0
            }"
                    data-codpro="${row.codpro}" data-cant="f" data-ant="${
              data || 0
            }">`;
          }
        },
      },
      {
        data: "codpro",
        render: function (data, type, row) {
          return (
            '<button class="delete-btn btn btn-danger" data-codpro="' +
            String(data) +
            '"><i class="fa fa-trash" aria-hidden="true"></i></button>'
          );
        },
        orderable: false,
      },
    ],
    drawCallback: function () {
      if (buscado) {
        setTimeout(function () {
          $(tabla_extra.cell(boton, 2).node()).find("input").focus();
          buscado = false;
        }, 0);
      }
      $(".cante-input, .cantf-input").prop("disabled", false);
    },
  });

  tabla_extra.columns(".no-display").visible(false, false);
  $("#product-table").on("focus", ".cante-input, .cantf-input", function () {
    let input = this;
    // Usamos un pequeño delay para asegurarnos de que el campo tenga el foco correctamente
    $(input).attr("type", "text");

    // Usamos un pequeño delay para asegurarnos de que el campo tenga el foco correctamente
    setTimeout(function () {
      let length = $(input).val().length;
      input.setSelectionRange(0, length); // Movemos el cursor al final del texto

      // Regresamos a tipo 'number'
      $(input).attr("type", "number");
    }, 0);
  });
  $("#reference-table").on("focus", ".cante-input, .cantf-input", function () {
    let input = this;

    // Cambiamos temporalmente a tipo 'text' para poder mover el cursor
    $(input).attr("type", "text");

    // Usamos un pequeño delay para asegurarnos de que el campo tenga el foco correctamente
    setTimeout(function () {
      let length = $(input).val().length;
      input.setSelectionRange(0, length); // Movemos el cursor al final del texto

      // Regresamos a tipo 'number'
      $(input).attr("type", "number");
    }, 0); // 0ms timeout para que se ejecute justo después del foco
  });
  $.fn.actualizardataextra = function (input) {
    let codpro = String($(input).data("codpro"));
    let cant = $(input).data("cant");
    let valor = $(input).val();
    // Buscar si ya existe un objeto con el mismo codpro en datosExtraParaEnviar
    let registro = datosExtraParaEnviar.find((item) => item.codpro === codpro);
    $(input).data("ant", valor);

    if (registro) {
      // Si el objeto ya existe, solo actualizar cante o cantf según el valor de `cant`
      if (cant === "e") {
        registro.cante = valor === "" ? null : valor;
      } else if (cant === "f") {
        registro.cantf = valor === "" ? null : valor;
      }
    } else {
      // Si no existe, crear un nuevo objeto y agregarlo a datosExtraParaEnviar
      registro = {
        codpro: codpro,
        cante: cant === "e" ? (valor === "" ? null : valor) : null,
        cantf: cant === "f" ? (valor === "" ? null : valor) : null,
      };
      datosExtraParaEnviar.push(registro);
    }
    if (buscado) {
      setTimeout(function () {
        buscado = false;
        $(tabla_extra.cell(boton, 2).node()).find("input").focus();
      }, 0);
    } else {
      $("#search-qr").val("").focus();
    }
  };
  $("#product-table").on("click", ".delete-btn", function () {
    let codpro = String($(this).data("codpro"));

    // Elimina el producto de la tabla `tabla_extra` sin recargar desde el servidor
    let rowIndex = tabla_extra
      .rows()
      .data()
      .toArray()
      .findIndex((row) => row.codpro === codpro);
    if (rowIndex !== -1) {
      // Remueve la fila de la tabla y redibuja
      tabla_extra.row(rowIndex).remove().draw(false);

      // Agrega el registro al JSON de eliminados
      jsonEliminados.push(codpro);

      // Elimina el producto del JSON `jsonExtra`
      datosExtraParaEnviar = datosExtraParaEnviar.filter(
        (item) => item.codpro !== codpro
      );
    }
    // Opcional: reenfoca el campo de búsqueda
    $("#search-qr").val("").focus();
  });

  function agregarProducto(qr, txt) {
    if (qr !== "") {
      let productoEncontrado = productData.find(
        (producto) => producto.qr === qr
      );

      if (productoEncontrado) {
        borrarbusqueda();
        let existeEnTabla = productTable
          .rows()
          .data()
          .toArray()
          .find((row) => row.codpro === productoEncontrado.codpro);

        txt.val(txt.data("ant"));

        setTimeout(function () {
          $(this).blur();
        }, 0);

        if (existeEnTabla) {
          // Encuentra el índice de la fila donde está el producto
          let rowIndex = productTable
            .row(function (idx, data, node) {
              return data.codpro === productoEncontrado.codpro;
            })
            .index();

          // Encuentra el valor actual del input de la cantidad (cantecaja)
          let cantidadE = $(productTable.cell(rowIndex, 4).node())
            .find("input")
            .val();

          // Actualiza el valor sumando 1
          let nuevaCantidadE = parseInt(cantidadE) + 1;

          // Actualiza el valor del input
          let inputElement = $(productTable.cell(rowIndex, 4).node()).find(
            "input"
          );
          inputElement.val(nuevaCantidadE);
          buscado = true;
          boton = rowIndex;

          $.fn.actualizardata(inputElement);
        } else {
          agregarProductoExtra(productoEncontrado);
        }
      } else {
      }
    }
  }
  function agregarProductoExtra(productoEncontrado) {
    if (productoEncontrado) {
      borrarbusqueda();
      let existeEnTabla = tabla_extra
        .rows()
        .data()
        .toArray()
        .find((row) => row.codpro === productoEncontrado.codpro);

      if (existeEnTabla) {
        // Encuentra el índice de la fila donde está el producto
        let rowIndex = tabla_extra
          .row(function (idx, data, node) {
            return data.codpro === productoEncontrado.codpro;
          })
          .index();

        // Encuentra el valor actual del input de la cantidad (cante)
        let cantidadE = $(tabla_extra.cell(rowIndex, 2).node())
          .find("input")
          .val();

        // Actualiza el valor sumando 1
        let nuevaCantidadE = parseInt(cantidadE) + 1;

        // Actualiza el valor del input
        let inputElement = $(tabla_extra.cell(rowIndex, 2).node()).find(
          "input"
        );
        inputElement.val(nuevaCantidadE);

        buscado = true;
        boton = rowIndex;
        // Actualiza los datos internos de DataTables
        $.fn.actualizardataextra(inputElement);
      } else {
        jsonEliminados = jsonEliminados.filter(
          (item) => item !== productoEncontrado.codpro
        );
        // Agregar producto nuevo al JSON y a la tabla
        let nuevoRegistro = {
          codpro: productoEncontrado.codpro,
          cante: 1, // Inicializa con cante = 1
          cantf: null, // Inicializa con cantf = null
        };
        datosExtraParaEnviar.push(nuevoRegistro);
        buscado = true;
        boton = tabla_extra.rows().count();
        // Agregar el producto directamente a la tabla DataTables localmente
        tabla_extra.row
          .add({
            codpro: productoEncontrado.codpro,
            despro: productoEncontrado.despro,
            cante: 1,
            cantf: null,
            stkfra: productoEncontrado.stkfra, // Para determinar si debe mostrar input en cantf
          })
          .draw(false);
      }
    } else {
      alert("Producto no encontrado");
    }
  }

  // Evento al presionar Enter en el cuadro de texto
  $("#search-qr").on("input", function () {
    let qr = $(this).val().trim();
    if (qr.length > 7) {
      agregarProducto(qr, $("#search-qr"));
    }
  });

  // Evento para el botón de búsqueda
  $("#search-button").on("click", function () {
    let qr = $("#search-qr").val().trim();
    agregarProducto(qr, $("#search-qr"));
    $("#search-qr").val("").focus();
  });
  $("#search-bulto").on("keyup", function () {
    productTable.search(this.value).draw(); // Aplica el filtro con el texto ingresado
  });
  function borrarbusqueda() {
    $("#search-bulto").val("");
    productTable.search("").draw();
  }
  // Evento para agregar un producto desde la tabla del modal a la tabla principal
  $("#modal-product-table tbody").on("click", ".btn-add", function () {
    let data = modalProductTable.row($(this).closest("tr")).data(); // Obtiene los datos de la fila seleccionada

    borrarbusqueda();
    // Verifica si el producto ya está en la tabla principal por 'codpro'
    let existingRow = productTable
      .rows()
      .data()
      .toArray()
      .find((row) => row.codpro === data.codpro);

    if (existingRow) {
      // Encuentra el índice de la fila donde está el producto
      let rowIndex = productTable
        .row(function (idx, rowData, node) {
          return rowData.codpro === data.codpro;
        })
        .index();

      // Encuentra el valor actual del input de la cantidad (cantecaja)
      let cantidadE = $(productTable.cell(rowIndex, 4).node())
        .find("input")
        .val();

      // Actualiza el valor sumando 1
      let nuevaCantidadE = parseInt(cantidadE) + 1;

      // Actualiza el valor del input
      let inputElement = $(productTable.cell(rowIndex, 4).node()).find("input");
      inputElement.val(nuevaCantidadE);

      buscado = true;
      boton = rowIndex;
      // Actualiza los datos internos de DataTables
      $.fn.actualizardata(inputElement);
    } else {
      agregarProductoExtra(data); // Agrega el producto si no existe en la tabla principal
    }
    $("#product-modal").modal("hide"); // Cierra el modal
  });
  // Mostrar el modal
  $("#show-modal").on("click", function () {
    $("#product-modal").modal("show");
  });

  // Cerrar el modal
  $(".close").on("click", function () {
    $("#product-modal").modal("hide");
  });

  var fixedTitle = $("#fixed-title");
  var cardHeader = $("#card-header");
  var cardHeaderOffset = cardHeader.offset().top;

  $(window).on("scroll", function () {
    if ($(window).scrollTop() > cardHeaderOffset) {
      fixedTitle.css({
        display: "block",
        width: cardHeader.outerWidth(),
      });
    } else {
      fixedTitle.css("display", "none");
    }
  });

  $(window).on("resize", function () {
    if (fixedTitle.css("display") === "block") {
      fixedTitle.css("width", cardHeader.outerWidth());
    }
  });
  $("#example").on("click", ".btn-tabla", function () {
    // Obtener los datos del botón
    let invnum = $(this).data("invnum");
    let numitm = $(this).data("numitm");
    let checkenvio = $(this).data("checkenvio");
    let $button = $(this);
    if (checkenvio === undefined || checkenvio === "") {
      checkenvio = "N";
    }
    let caja = localStorage.getItem("caja_caja");
    $.getJSON(
      "picking",
      {
        opcion: 11,
        caja: caja,
        invnum: invnum,
        numitm: numitm,
        check: checkenvio,
      },
      function (data) {
        if (data.resultado === "ok") {
          cambio = true;
          if (checkenvio === "S") {
            $button.removeClass("btn-success").addClass("btn-danger");
            $button.find("i").removeClass("fa-check").addClass("fa-times");
            $button.data("checkenvio", "N");
            searchBox.focus();
          } else {
            $button.removeClass("btn-danger").addClass("btn-success");
            $button.find("i").removeClass("fa-times").addClass("fa-check");
            $button.data("checkenvio", "S");
            searchBox.focus();
          }
        } else {
          if (data.mensaje === "cambio") {
            alert("Parece que ya se cambio este check, actualizando...");
            $("#contenido").load("recepcioncajasdetalle.html");
          } else if (data.mensaje === "nosession") {
            $.fn.validarSession();
          } else {
            alert("Ocurrio un problema al aplicar");
          }
        }
      }
    );
  });
  $("#back-button").click(function () {
    if (
      jsonEliminados.length > 0 ||
      datosParaEnviar.length > 0 ||
      datosExtraParaEnviar.length > 0
    ) {
      Swal.fire({
        title: "¿Desea Guardar los cambios realizados?",
        text: "Se han relizado cambios.",
        icon: "warning",
        showCancelButton: true,
        showDenyButton: true,
        confirmButtonText: "Guardar",
        denyButtonText: "No Guardar",
        cancelButtonText: `Cerrar`,
      }).then((result) => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
          json = {
            eliminar: jsonEliminados,
            recepcion: datosParaEnviar,
            extra: datosExtraParaEnviar,
            caja: caja,
            orden: orden,
          };
          $(".cante-input, .cantf-input").prop("disabled", true);
          $.ajax({
            url: "picking?opcion=28",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(json), // Enviar resultadoJSON como JSON en el cuerpo
            success: function (data) {
              data = JSON.parse(data);
              if (data.resultado === "ok") {
                $("#contenido").load("recepcioncajascaja.html");
              } else {
                if (data.mensaje === "nosession") {
                  $.fn.validarSession();
                } else {
                  alert("Error al guardar");
                }
              }
            },
            error: function (jqXHR, textStatus, errorThrown) {
              alert("Error de conexion con el servidor");
            },
          });
        } else if (result.isDenied) {
          $("#contenido").load("recepcioncajascaja.html");
        }
      });
    } else {
      $("#contenido").load("recepcioncajascaja.html");
    }
  });
  $("#sec-button").click(function () {
    $.fn.listarsecuencias();
  });
  $("#codigoprod").on("change", function () {
    productTable
      .columns(".no-display")
      .visible($(this).prop("checked"), $(this).prop("checked"));
    tabla_extra
      .columns(".no-display")
      .visible($(this).prop("checked"), $(this).prop("checked"));
  });
  $("#guardar").click(function () {
    guardar();
  });
  function guardar() {
    if (
      jsonEliminados.length > 0 ||
      datosParaEnviar.length > 0 ||
      datosExtraParaEnviar.length > 0
    ) {
      json = {
        eliminar: jsonEliminados,
        recepcion: datosParaEnviar,
        extra: datosExtraParaEnviar,
        caja: caja,
        orden: orden,
      };
      $(".cante-input, .cantf-input").prop("disabled", true);
      $.ajax({
        url: "picking?opcion=51", // ant 28 - nuev 51
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(json), // Enviar resultadoJSON como JSON en el cuerpo
        success: function (data) {
          data = JSON.parse(data);
          if (data.resultado === "ok") {
            Swal.fire({
              title: "¡Guardado Correctamente!",
              text: "¡Se han guardado los cambios!",
              icon: "success",
              showCancelButton: true,
              confirmButtonText: "Volver",
              cancelButtonText: `Cerrar`,
            }).then((result) => {
              /* Read more about isConfirmed, isDenied below */
              if (result.isConfirmed) {
                $("#contenido").load("recepcioncajascaja.html");
              }
            });
            datosParaEnviar = []; //productos normales
            datosExtraParaEnviar = []; //productos extra agregados
            jsonEliminados = []; //productos extra eliminados
            tabla_extra.ajax.reload();
            productTable.ajax.reload();
            secuenciastabla.ajax.reload();
          } else {
            if (data.mensaje === "nosession") {
              $.fn.validarSession();
            } else {
              alert("Error al guardar");
            }
          }
        },
        error: function (jqXHR, textStatus, errorThrown) {
          alert("Error de conexion con el servidor");
        },
      });
    } else {
      alert("No se realizo ningun cambio");
    }
  }
  function alert(mensaje) {
    Swal.fire({
      title: "",
      text: mensaje,
      icon: "warning",
    });
  }
});
