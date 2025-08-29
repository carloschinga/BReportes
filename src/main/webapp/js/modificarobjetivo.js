$(document).ready(function () {
  let lab;
  let selectedLabs = [];
  let cantlabs = 0;
  let esta;
  let selectedEsta = [];
  let cantesta = 0;
  let tip;
  let selectedTip = [];
  let canttip = 0;
  let estra;
  let selectedEstra = [];
  let cantestra = 0;
  let producto;
  let selectedProducto = [];
  let estrasolo;
  let selectedEstrasolo = [];
  let cantestrasolo = 0;
  var tabla;
  let agregar = true;
  let codobj = 0;
  let tablaproductos;
  if (localStorage.getItem("codobj") !== null) {
    agregar = false;
    codobj = localStorage.getItem("codobj");
    $("#titulo").text("Modificar Objetivo");
  }
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
  $.getJSON("CRUDobjetivos?opcion=2&codobj=" + codobj, function (data) {
    if (data.resultado === "ok") {
      let text = "";
      lab = data.lab;
      cantlabs = lab.length;
      esta = data.sis;
      cantesta = esta.length;
      tip = data.tip;
      canttip = tip.length;
      estra = data.estr;
      cantestra = estra.length;

      estrasolo = data.estrsolo;
      cantestrasolo = estrasolo.length;

      producto = data.productos;
      if (agregar) {
        selectedLabs = lab.map((lab) => String(lab.codlab));
        //selectedEsta = esta.map(esta => String(esta.siscod));

        selectedEsta = esta
          .filter((esta) => esta.selected === "S")
          .map((esta) => String(esta.siscod));

        selectedTip = tip.map((tip) => String(tip.codtip));
        selectedEstra = estra.map((estra) => String(estra.compro));

        selectedEstrasolo = estrasolo.map((estrasolo) =>
          String(estrasolo.categvta)
        );
      } else {
        selectedProducto = producto
          .filter((producto) => producto.selected === "S")
          .map((producto) => String(producto.codpro));

        selectedEsta = esta
          .filter((esta) => esta.selected === "S")
          .map((esta) => String(esta.siscod));
        if (selectedProducto.length > 0) {
          $("#btnlaboratorio").prop("disabled", true);
          $("#btntipo").prop("disabled", true);
          $("#btnestrategico").prop("disabled", true);
          $("#btnestrategicosolo").prop("disabled", true);
          selectedLabs = lab.map((lab) => String(lab.codlab));
          selectedTip = tip.map((tip) => String(tip.codtip));
          selectedEstra = estra.map((estra) => String(estra.compro));
          selectedEstrasolo = estrasolo.map((estrasolo) =>
            String(estrasolo.categvta)
          );
        } else {
          selectedLabs = lab
            .filter((lab) => lab.selected === "S")
            .map((lab) => String(lab.codlab));
          selectedTip = tip
            .filter((tip) => tip.selected === "S")
            .map((tip) => String(tip.codtip));

          selectedEstra = estra
            .filter((estra) => estra.selected === "S")
            .map((estra) => String(estra.compro));

          selectedEstrasolo = estrasolo
            .filter((estrasolo) => estrasolo.selected === "S")
            .map((estrasolo) => String(estrasolo.categvta));
        }
      }

      renderLaboratorios("");
      renderEstablecimiento("");
      renderTipo("");
      renderEstrategico("");
      renderEstrategicosolo();
      validarproductosdisabled();

      $("#chklab-todos").prop(
        "checked",
        $("#contenidoLaboratorio .form-check-input").length ===
          $("#contenidoLaboratorio .form-check-input:checked").length
      );
      $("#chkestra-todos").prop(
        "checked",
        $("#contenidoEstra .form-check-input").length ===
          $("#contenidoEstra .form-check-input:checked").length
      );
      $("#chkest-todos").prop(
        "checked",
        $("#contenidoEstablecimiento .form-check-input").length ===
          $("#contenidoEstablecimiento .form-check-input:checked").length
      );
      $("#chktip-todos").prop(
        "checked",
        $("#contenidoTipo .form-check-input").length ===
          $("#contenidoTipo .form-check-input:checked").length
      );
      tablaproductos = $("#tablaProductos").DataTable({
        info: false, // Mostrar información de la paginación
        paging: true, // Activar la paginación
        searching: true, // Habilitar búsqueda nativa de DataTables
        pageLength: 10,
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
            last: "Último",
            next: "Sig.",
            previous: "Ant.",
          },
          aria: {
            sortAscending: ": activate to sort column ascending",
            sortDescending: ": activate to sort column descending",
          },
        },
        data: data.productos,
        columns: [
          {
            data: "codpro",
            render: function (data, type, row) {
              const isChecked = selectedProducto.includes(String(row.codpro));

              // Crear el HTML del input con el atributo "checked" si es necesario
              const checkedAttribute = isChecked ? "checked" : "";

              // Retornar el HTML del checkbox directamente
              return `<input type="checkbox" class="check-producto" value="${row.codpro}" id="chkprod-${row.codpro}" ${checkedAttribute}>`;
            },
          },
          {
            data: "despro",
            render: function (data, type, row) {
              const label = $("<label>")
                .addClass("form-check-label")
                .attr("for", `chkprod-${row.codpro}`)
                .text(`${row.despro} (${row.codpro})`);

              // Devolvemos el elemento HTML como cadena
              return label.prop("outerHTML");
            },
          },
        ],
        order: [[0, "desc"]],
      });
      if (!agregar) {
        $.getJSON("CRUDobjetivos?opcion=7&codobj=" + codobj, function (data) {
          if (data.resultado === "ok") {
            $("#nombre").val(data.desobj);
            $("#fechaMesAno").val(data.periodo);
            if (data.tipo === "p") {
              $("#flexRadioDefault1").prop("checked", true);
            } else {
              $("#flexRadioDefault2").prop("checked", true);
            }
            $("#vistaprevia").trigger("click");
          } else {
            if (data.mensaje === "nosession") {
              $.fn.validarSession();
            } else {
              alert("Error al cargar los datos");
            }
          }
        });
      }
    } else {
      if (data.mensaje === "nosession") {
        $.fn.validarSession();
      } else {
        alert("Error al cargar los datos");
      }
    }
  }).fail(function (jqXHR, textStatus, errorThrown) {
    alert("Error de conexion con el servidor");
  });

  $("#tablaProductos").on("change", ".check-producto", function () {
    $("#contenedorvistaprevia").hide();
    const value = $(this).val();
    if ($(this).is(":checked")) {
      selectedProducto.push(String(value)); // Agregar al array si se selecciona
    } else {
      selectedProducto = selectedProducto.filter((item) => item !== value); // Quitar del array si se deselecciona
    }
    console.log(selectedProducto);
    if (selectedProducto.length > 0) {
      $("#btnlaboratorio").prop("disabled", true);
      $("#btntipo").prop("disabled", true);
      $("#btnestrategico").prop("disabled", true);
      $("#btnestrategicosolo").prop("disabled", true);
    } else {
      $("#btnlaboratorio").prop("disabled", false);
      $("#btntipo").prop("disabled", false);
      $("#btnestrategico").prop("disabled", false);
      $("#btnestrategicosolo").prop("disabled", false);
    }
  });

  function validarproductosdisabled() {
    console.log(cantlabs + " " + canttip + " " + cantestra);
    if (
      cantlabs > selectedLabs.length ||
      canttip > selectedTip.length ||
      cantestra > selectedEstra.length ||
      cantestrasolo > selectedEstrasolo.length
    ) {
      $("#botonProductos").prop("disabled", true);
    } else {
      $("#botonProductos").prop("disabled", false);
    }
  }

  //todo relacionado a laboratorios
  function renderLaboratorios(filter) {
    const container = $("#contenidoLaboratorio");
    container.empty(); // Limpiar el contenedor antes de agregar nuevos elementos

    for (const obj of lab) {
      // Filtrar según el texto de búsqueda
      if (obj.deslab.toLowerCase().includes(filter.toLowerCase())) {
        const div = $("<div>").addClass("form-check");
        const input = $("<input>")
          .addClass("form-check-input")
          .attr("type", "checkbox")
          .attr("value", obj.codlab)
          .attr("id", `chklab-${obj.codlab}`)
          .prop("checked", selectedLabs.includes(String(obj.codlab)));
        const label = $("<label>")
          .addClass("form-check-label")
          .attr("for", `chklab-${obj.codlab}`)
          .text(obj.deslab + "(" + obj.codlab + ")");

        // Agregar input y label al div, y luego al contenedor
        div.append(input).append(label);
        container.append(div);
      }
    }
  }
  // Actualizar el array de seleccionados al cambiar el estado de los checkboxes
  $("#contenidoLaboratorio").on("change", ".form-check-input", function () {
    $("#contenedorvistaprevia").hide();
    const value = $(this).val();
    if ($(this).is(":checked")) {
      selectedLabs.push(String(value)); // Agregar al array si se selecciona
    } else {
      selectedLabs = selectedLabs.filter((item) => item !== value); // Quitar del array si se deselecciona
    }
    $("#chklab-todos").prop(
      "checked",
      $("#contenidoLaboratorio .form-check-input").length ===
        $("#contenidoLaboratorio .form-check-input:checked").length
    );
    validarproductosdisabled();
  });
  $("#searchLaboratorios").on("input", function () {
    const filterText = $(this).val();
    renderLaboratorios(filterText);
    $("#chklab-todos").prop(
      "checked",
      $("#contenidoLaboratorio .form-check-input").length ===
        $("#contenidoLaboratorio .form-check-input:checked").length
    );
  });
  $("#chklab-todos").on("change", function () {
    $("#contenedorvistaprevia").hide();
    const isChecked = $(this).is(":checked");
    $("#contenidoLaboratorio .form-check-input").each(function () {
      $(this).prop("checked", isChecked);
      const value = $(this).val();
      if (isChecked && !selectedLabs.includes(value)) {
        selectedLabs.push(value);
      } else if (!isChecked) {
        selectedLabs = selectedLabs.filter((item) => item !== value);
      }
    });
    validarproductosdisabled();
  });

  //todo relacionado a establecimientos
  function renderEstablecimiento(filter) {
    const container = $("#contenidoEstablecimiento");
    container.empty(); // Limpiar el contenedor antes de agregar nuevos elementos

    for (const obj of esta) {
      // Filtrar según el texto de búsqueda
      if (obj.sisent.toLowerCase().includes(filter.toLowerCase())) {
        const div = $("<div>").addClass("form-check");
        const input = $("<input>")
          .addClass("form-check-input")
          .attr("type", "checkbox")
          .attr("value", obj.siscod)
          .attr("id", `chkest-${obj.siscod}`)
          .prop("checked", selectedEsta.includes(String(obj.siscod)));
        const label = $("<label>")
          .addClass("form-check-label")
          .attr("for", `chkest-${obj.siscod}`)
          .text(obj.sisent);

        // Agregar input y label al div, y luego al contenedor
        div.append(input).append(label);
        container.append(div);
      }
    }
  }
  // Actualizar el array de seleccionados al cambiar el estado de los checkboxes
  $("#contenidoEstablecimiento").on("change", ".form-check-input", function () {
    const value = $(this).val();
    if ($(this).is(":checked")) {
      selectedEsta.push(String(value)); // Agregar al array si se selecciona
    } else {
      selectedEsta = selectedEsta.filter((item) => item !== value); // Quitar del array si se deselecciona
    }
    $("#chkest-todos").prop(
      "checked",
      $("#contenidoEstablecimiento .form-check-input").length ===
        $("#contenidoEstablecimiento .form-check-input:checked").length
    );
  });
  $("#searchestablecimiento").on("input", function () {
    const filterText = $(this).val();
    renderEstablecimiento(filterText);
    $("#chkest-todos").prop(
      "checked",
      $("#contenidoEstablecimiento .form-check-input").length ===
        $("#contenidoEstablecimiento .form-check-input:checked").length
    );
  });
  $("#chkest-todos").on("change", function () {
    const isChecked = $(this).is(":checked");
    $("#contenidoEstablecimiento .form-check-input").each(function () {
      $(this).prop("checked", isChecked);
      const value = $(this).val();
      if (isChecked && !selectedEsta.includes(value)) {
        selectedEsta.push(value);
      } else if (!isChecked) {
        selectedEsta = selectedEsta.filter((item) => item !== value);
      }
    });
  });

  //todo relacionado a Tipo
  function renderTipo(filter) {
    const container = $("#contenidoTipo");
    container.empty(); // Limpiar el contenedor antes de agregar nuevos elementos

    for (const obj of tip) {
      // Filtrar según el texto de búsqueda
      if (obj.destip.toLowerCase().includes(filter.toLowerCase())) {
        const div = $("<div>").addClass("form-check");
        const input = $("<input>")
          .addClass("form-check-input")
          .attr("type", "checkbox")
          .attr("value", obj.codtip)
          .attr("id", `chktipo-${obj.codtip}`)
          .prop("checked", selectedTip.includes(String(obj.codtip)));
        const label = $("<label>")
          .addClass("form-check-label")
          .attr("for", `chktipo-${obj.codtip}`)
          .text(obj.destip);

        // Agregar input y label al div, y luego al contenedor
        div.append(input).append(label);
        container.append(div);
      }
    }
  }
  // Actualizar el array de seleccionados al cambiar el estado de los checkboxes
  $("#contenidoTipo").on("change", ".form-check-input", function () {
    $("#contenedorvistaprevia").hide();
    const value = $(this).val();
    if ($(this).is(":checked")) {
      selectedTip.push(String(value)); // Agregar al array si se selecciona
    } else {
      selectedTip = selectedTip.filter((item) => item !== value); // Quitar del array si se deselecciona
    }
    $("#chktip-todos").prop(
      "checked",
      $("#contenidoTipo .form-check-input").length ===
        $("#contenidoTipo .form-check-input:checked").length
    );
    validarproductosdisabled();
  });
  $("#searchTipo").on("input", function () {
    const filterText = $(this).val();
    renderTipo(filterText);
    $("#chktip-todos").prop(
      "checked",
      $("#contenidoTipo .form-check-input").length ===
        $("#contenidoTipo .form-check-input:checked").length
    );
  });
  $("#chktip-todos").on("change", function () {
    $("#contenedorvistaprevia").hide();
    const isChecked = $(this).is(":checked");
    $("#contenidoTipo .form-check-input").each(function () {
      $(this).prop("checked", isChecked);
      const value = $(this).val();
      if (isChecked && !selectedTip.includes(value)) {
        selectedTip.push(value);
      } else if (!isChecked) {
        selectedTip = selectedTip.filter((item) => item !== value);
      }
    });
    validarproductosdisabled();
  });

  //todo relacionado a Estrategico
  function renderEstrategico(filter) {
    const container = $("#contenidoEstra");
    container.empty(); // Limpiar el contenedor antes de agregar nuevos elementos

    for (const obj of estra) {
      // Filtrar según el texto de búsqueda
      if (obj.descripcion.toLowerCase().includes(filter.toLowerCase())) {
        const div = $("<div>").addClass("form-check");
        const input = $("<input>")
          .addClass("form-check-input")
          .attr("type", "checkbox")
          .attr("value", obj.compro)
          .attr("id", `chkestra-${obj.compro}`)
          .prop("checked", selectedEstra.includes(String(obj.compro)));
        const label = $("<label>")
          .addClass("form-check-label")
          .attr("for", `chkestra-${obj.compro}`)
          .text(obj.descripcion);

        // Agregar input y label al div, y luego al contenedor
        div.append(input).append(label);
        container.append(div);
      }
    }
  }
  // Actualizar el array de seleccionados al cambiar el estado de los checkboxes
  $("#contenidoEstra").on("change", ".form-check-input", function () {
    $("#contenedorvistaprevia").hide();
    const value = $(this).val();
    if ($(this).is(":checked")) {
      selectedEstra.push(String(value)); // Agregar al array si se selecciona
    } else {
      selectedEstra = selectedEstra.filter((item) => item !== value); // Quitar del array si se deselecciona
    }
    $("#chkestra-todos").prop(
      "checked",
      $("#contenidoEstra .form-check-input").length ===
        $("#contenidoEstra .form-check-input:checked").length
    );
    validarproductosdisabled();
  });
  $("#searchEstra").on("input", function () {
    const filterText = $(this).val();
    renderEstrategico(filterText);
    $("#chkestra-todos").prop(
      "checked",
      $("#contenidoEstra .form-check-input").length ===
        $("#contenidoEstra .form-check-input:checked").length
    );
  });
  $("#chkestra-todos").on("change", function () {
    $("#contenedorvistaprevia").hide();
    const isChecked = $(this).is(":checked");
    $("#contenidoEstra .form-check-input").each(function () {
      $(this).prop("checked", isChecked);
      const value = $(this).val();
      if (isChecked && !selectedEstra.includes(value)) {
        selectedEstra.push(value);
      } else if (!isChecked) {
        selectedEstra = selectedEstra.filter((item) => item !== value);
      }
    });
    validarproductosdisabled();
  });

  function renderEstrategicosolo() {
    const container = $("#contenidoEstrategicoSolo");
    container.empty(); // Limpiar el contenedor antes de agregar nuevos elementos

    for (const obj of estrasolo) {
      const div = $("<div>").addClass("form-check");
      const input = $("<input>")
        .addClass("form-check-input")
        .attr("type", "checkbox")
        .attr("value", obj.categvta)
        .attr("id", `chkestrasolo-${obj.categvta}`)
        .prop("checked", selectedEstrasolo.includes(String(obj.categvta)));
      const label = $("<label>")
        .addClass("form-check-label")
        .attr("for", `chkestrasolo-${obj.categvta}`)
        .text(obj.categvta);

      // Agregar input y label al div, y luego al contenedor
      div.append(input).append(label);
      container.append(div);
    }
  }
  // Actualizar el array de seleccionados al cambiar el estado de los checkboxes
  $("#contenidoEstrategicoSolo").on("change", ".form-check-input", function () {
    $("#contenedorvistaprevia").hide();
    const value = $(this).val();
    if ($(this).is(":checked")) {
      selectedEstrasolo.push(String(value)); // Agregar al array si se selecciona
    } else {
      selectedEstrasolo = selectedEstrasolo.filter((item) => item !== value); // Quitar del array si se deselecciona
    }
    validarproductosdisabled();
  });

  //demas
  $("#cant").on("input", function () {
    if ($(this).val() === "") {
      $("#soles").prop("disabled", false);
    } else {
      $("#soles").prop("disabled", true);
    }
  });
  $("#soles").on("input", function () {
    if ($(this).val() === "") {
      $("#cant").prop("disabled", false);
    } else {
      $("#cant").prop("disabled", true);
    }
  });

  $("#agregarbutton").on("click", function () {
    let nomb = $("#nombre").val();
    let fechaMesAno = $("#fechaMesAno").val();
    let fechMod = fechaMesAno.replace("-", "");
    let tipo = $(".radiotipo:checked").val();

    let opcion;
    let parametros;
    let siguiente = false;
    parametros = JSON.stringify(
      {
        lab: selectedLabs,
        esta: selectedEsta,
        tip: selectedTip,
        estra: selectedEstra,
        nomb: nomb,
        producto: selectedProducto,
        estrasolo: selectedEstrasolo,
      },
      null
    );
    if (selectedProducto.length === 0) {
      if (agregar) {
        opcion = 3;
      } else {
        opcion = 6;
      }
      if (
        selectedEsta.length > 0 &&
        selectedEstra.length > 0 &&
        selectedTip.length > 0 &&
        selectedEstrasolo.length > 0 &&
        selectedLabs.length > 0
      ) {
        siguiente = true;
      }
    } else {
      if (selectedEsta.length > 0) {
        siguiente = true;
      }
      if (agregar) {
        opcion = 10;
      } else {
        opcion = 11;
      }
    }
    if (siguiente) {
      if (
        fechaMesAno !== "" &&
        fechaMesAno !== undefined &&
        fechaMesAno !== null
      ) {
        if (nomb !== "" && nomb !== undefined && nomb !== null) {
          $.ajax({
            url:
              "CRUDobjetivos?opcion=" +
              opcion +
              "&tipo=" +
              tipo +
              "&periodo=" +
              fechaMesAno +
              "&codobj=" +
              codobj +
              "&mesano=" +
              fechMod, // URL de la API
            method: "POST", // Cambiado a POST
            contentType: "application/json", // Asegúrate de que el servidor espera JSON
            data: parametros, // Convertir los datos a una cadena JSON
            dataType: "json", // Espera una respuesta JSON del servidor
            success: function (data) {
              if (data.resultado === "ok") {
                let titulo;
                if (agregar) {
                  titulo = "¡Objetivo agregado correctamente!";
                  localStorage.setItem("codobj", data.codobj);
                } else {
                  titulo = "¡Objetivo modificado correctamente!";
                }
                if (selectedProducto.length === 0) {
                  $("#contenido").load("objetivoestablecimientos.html");
                } else {
                  $("#contenido").load("objetivoproductoespecifico.html");
                }
              } else if (data.mensaje === "duplicado") {
                alert(
                  "¡Error! Ya existe un objetivo con el mismo tipo y periodo (mes-año)."
                );
              } else {
                if (data.mensaje === "nosession") {
                  $.fn.validarSession();
                }
                if (data.mensaje === "yaexistecodpro") {
                  alert(
                    "Ya existe uno de los productos seleccionados en otro objetivo"
                  );
                } else {
                  alert("Ya existe combinacion de periodo y tipo");
                }
              }
            },
            error: function (jqXHR, textStatus, errorThrown) {
              if (jqXHR.status === 400) {
                alert("Error: Demasiados productos a restringir");
              } else {
                alert("Error en la solicitud: " + textStatus);
              }
            },
          });
        } else {
          alert("Ingrese El nombre del objetivo");
        }
      } else {
        alert("Seleccione el periodo");
      }
    } else {
      alert("Seleccione filtros validos");
    }
  });

  $("#vistaprevia").on("click", function () {
    $("#contenedorvistaprevia").show();
    $("#tablabody").html(
      '<div class="d-flex justify-content-center">Cargando... <div class="spinner-border" role="status"><span class="visually-hidden">Loading...</span></div></div>'
    );
    if (selectedProducto.length === 0) {
      $.ajax({
        url: "CRUDobjetivos?opcion=5", // URL de la API
        method: "POST", // Cambiado a POST
        contentType: "application/json", // Asegúrate de que el servidor espera JSON
        data: JSON.stringify(
          {
            lab: selectedLabs,
            tip: selectedTip,
            estra: selectedEstra,
            estrasolo: selectedEstrasolo,
          },
          null
        ), // Convertir los datos a una cadena JSON
        dataType: "json", // Espera una respuesta JSON del servidor
        success: function (data) {
          if (data.resultado === "ok") {
            $("#tablabody").html("");
            if (tabla) {
              tabla.destroy();
            }
            tabla = $("#tabla").DataTable({
              paginate: false,
              searching: false,
              fixedHeader: {
                header: true,
                footer: true,
              },
              language: {
                decimal: "",
                emptyTable: "No hay datos",
                info: "Total _TOTAL_ Productos",
                infoEmpty: "No hay productos",
                infoFiltered: "(Filtrados del total de _MAX_ registros)",
                infoPostFix: "",
                thousands: ",",
                lengthMenu: "Mostrar _MENU_ registros por página",
                loadingRecords: "Cargando...",
                processing: "Procesando...",
                search: "Buscar:",
                zeroRecords:
                  "No se ha encontrado nada  atraves de ese filtrado.",
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
              data: data.data,
              columns: [{ data: "codpro" }, { data: "despro" }],
              order: [[1, "asc"]],
            });
          } else {
            if (data.mensaje === "nosession") {
              $.fn.validarSession();
            } else {
              alert("Error al aplicar.");
            }
          }
        },
        error: function (jqXHR, textStatus, errorThrown) {
          if (jqXHR.status === 400) {
            alert("Error: Demasiados productos a restringir");
          } else {
            alert("Error en la solicitud: " + textStatus);
          }
        },
      });
    } else {
      $("#tablabody").html("");
      if (tabla) {
        tabla.destroy();
      }
      tabla = $("#tabla").DataTable({
        paginate: false,
        searching: false,
        fixedHeader: {
          header: true,
          footer: true,
        },
        language: {
          decimal: "",
          emptyTable: "No hay datos",
          info: "Total _TOTAL_ Productos",
          infoEmpty: "No hay productos",
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
        data: producto.filter((producto) =>
          selectedProducto.includes(producto.codpro)
        ),
        columns: [{ data: "codpro" }, { data: "despro" }],
        order: [[1, "asc"]],
      });
    }
  });
  $("#atras").on("click", function () {
    $("#contenido").load("objetivos.html");
  });
});
