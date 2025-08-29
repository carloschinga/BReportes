$(document).ready(function () {
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

  // metodo para poner por defacto el valor de fechaInicio y fechaFin
  function setFechasInicioFin() {
    let fechaInicio = formatoMesDiaAnio($("#inputFechaInicio").val());
    let fechaFin = formatoMesDiaAnio($("#inputFechaFin").val());
    $("#fechaInicio").val(fechaInicio);
    $("#fechaFin").val(fechaFin);
  }
  setFechasInicioFin();

  function formatoMesDiaAnio(fecha) {
    if (!fecha) return "";
    const [anio, mes, dia] = fecha.split("/");
    return `${dia}-${mes}-${anio}`;
  }

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
  let fechaUTC = moment().utc();

  let solicitudmedico;
  // Obtener la fecha actual en la zona horaria de Perú
  let fechaPeru = fechaUTC.subtract(5, "hours").format("YYYY-MM-DD");

  $("#fechaInicio").val(fechaPeru);
  $("#fechaFin").val(fechaPeru);

  $("#fechaInicio").change(function () {
    let fechaFin = $("#fechaInicio").val();
    $("#fechaFin").val(fechaFin);
  });
  const formatFecha = (fecha) => {
    if (fecha) {
      let partes = fecha.split("-");
      return `${partes[2]}-${partes[1]}-${partes[0]}`;
    }
    return "";
  };
  const formatFechamasdia = (fecha) => {
    if (fecha) {
      let partes = fecha.split("-");
      let nuevaFecha = new Date(partes[0], partes[1] - 1, partes[2]); // Meses en JS van de 0 a 11
      nuevaFecha.setDate(nuevaFecha.getDate() + 1);

      let dia = nuevaFecha.getDate().toString().padStart(2, "0");
      let mes = (nuevaFecha.getMonth() + 1).toString().padStart(2, "0");
      let año = nuevaFecha.getFullYear();

      return `${dia}-${mes}-${año}`;
    }
    return "";
  };
  let fechaInicio = formatFecha($("#fechaInicio").val());
  let fechaFin = formatFechamasdia($("#fechaFin").val());
  let med;
  let selectedmed = [];
  let ser;
  let selectedser = [];
  let table;
  $.ajax({
    url: "CRUDMedico?opcion=2",
    method: "GET",
    dataType: "json",
    success: function (response) {
      if (response.resultado === "ok") {
        med = response.data;
        selectedmed = med.map((tip) => String(tip.medcod));
        renderMedicos("");
        $("#chkmed-todos").prop(
          "checked",
          $("#contenidomedico .form-check-input").length ===
            $("#contenidomedico .form-check-input:checked").length
        );

        // Cargar lista de servicios desde el servidor
        $.ajax({
          url: "CRUDServicio?opcion=1", // URL para obtener la lista de servicios
          method: "GET",
          dataType: "json",
          success: function (response) {
            if (response.resultado === "ok") {
              ser = response.data;
              selectedser = ser.map((tip) => String(tip.sercod));
              renderServicios("");
              $("#chkser-todos").prop(
                "checked",
                $("#contenidoServicio .form-check-input").length ===
                  $("#contenidoServicio .form-check-input:checked").length
              );

              initializeDataTable();
            } else if (response.mensaje === "nosession") {
              $.fn.validarSession();
            } else {
              alert("Error al cargar los servicios");
            }
          },
          error: function () {
            alert("Error al cargar los servicios");
          },
        });
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

  function initializeDataTable() {
    table = $("#tablaRecetas").DataTable({
      lengthChange: false,
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
        aria: {
          sortAscending: ": activate to sort column ascending",
          sortDescending: ": activate to sort column descending",
        },
      },
      ajax: {
        url: "RecetasCRUD?opcion=2",
        type: "POST",
        contentType: "application/json", // Importante para enviar como JSON
        data: function (d) {
          // Aquí se obtienen los valores actualizados cada vez que se hace una petición
          return JSON.stringify({
            fechainicio: fechaInicio,
            fechafin: fechaFin,
            servicios: selectedser,
            paciente: $("#txtpaciente").val(),
            medicos: selectedmed,
          });
        },
        beforeSend: function (xhr) {
          xhr.setRequestHeader("Content-Type", "application/json");
        },
        dataSrc: function (json) {
          if (json.resultado === "ok") {
            return json.data;
          } else {
            alert("Error al cargar la lista");
            return {};
          }
        },
      },
      processing: true,
      columnDefs: [
        { width: "120px", targets: 0 }, // Primera columna (FechaAtencion)
        { width: "90px", targets: 1 }, // Segunda columna (ActoMedico)
        { width: "90px", targets: 2 }, // Quinta columna (Medico)
      ],
      columns: [
        {
          data: "FechaAtencion",
          render: function (data, type, row) {
            if (!data) return "";
            return moment(data).format("YYYY-MM-DD HH:mm:ss");
          },
        },
        { data: "ActoMedico" },
        { data: "Prefactura", defaultContent: "N/A" },
        {
          data: "Servicio",
          render: function (data, type, row) {
            return data ? data : "";
          },
        },
        { data: "Medico" },
        { data: "Paciente" },
        { data: "TelfPac", defaultContent: "N/A" },
        { data: "PlanAtencion" },
        {
          data: "cantimpr",
          render: function (data, type, row) {
            return `<button class="btn btn-primary btnpdf" data-acto='${
              row.ActoMedico
            }'>Imprimir(${data ? data : "0"})</button>`;
          },
        },
        {
          data: null,
          render: function (data, type, row) {
            return `<button class="btn btn-info detalle-btn" data-acto='${row.ActoMedico}'>Ver Detalle</button>`;
          },
        },
      ],
      order: [[1, "desc"]],
    });
  }

  $("#tablaRecetas").on("click", ".btnpdf", function () {
    let actomedico = $(this).data("acto");
    window.open("ReporteReceta?tipo=pdf&actomedico=" + actomedico, "_blank");
    setTimeout(() => {
      table.ajax.reload();
    }, 2000);
  });

  function renderMedicos(filter) {
    const container = $("#contenidomedico");
    container.empty();

    // Mostrar contador de seleccionados
    const counter = $(
      `<div class="selection-counter">Seleccionados: ${selectedmed.length}/${med.length}</div>`
    );
    container.append(counter);

    // Agregar opción "Todos" con comportamiento mejorado
    const divTodos = $("<div>").addClass("form-check todos-option");
    const inputTodos = $("<input>")
      .addClass("form-check-input")
      .attr("type", "checkbox")
      .attr("id", "chkmed-todos")
      .prop("checked", selectedmed.length === med.length);
    const labelTodos = $("<label>")
      .addClass("form-check-label")
      .attr("for", "chkmed-todos")
      .text("Todos los médicos");

    divTodos.append(inputTodos).append(labelTodos);
    container.append(divTodos);

    // Filtrar y renderizar médicos
    const filteredMedicos = med.filter(
      (obj) =>
        obj.mednam.toLowerCase().includes(filter.toLowerCase()) ||
        String(obj.medcod).includes(filter)
    );

    if (filteredMedicos.length === 0) {
      container.append(
        $('<div class="no-results">No se encontraron médicos</div>')
      );
      return;
    }

    filteredMedicos.forEach((obj) => {
      const div = $("<div>").addClass("form-check");
      const input = $("<input>")
        .addClass("form-check-input")
        .attr("type", "checkbox")
        .attr("value", obj.medcod)
        .attr("id", `chkmed-${obj.medcod}`)
        .prop("checked", selectedmed.includes(String(obj.medcod)));
      const label = $("<label>")
        .addClass("form-check-label")
        .attr("for", `chkmed-${obj.medcod}`)
        .text(`${obj.mednam} (${obj.medcod})`);

      div.append(input).append(label);
      container.append(div);
    });
  }

  // Manejo mejorado de selección/deselección
  $("#contenidomedico").on("change", ".form-check-input", function () {
    if ($(this).parent().hasClass("todos-option")) {
      // Caso especial para "Todos"
      const isChecked = $(this).is(":checked");
      selectedmed = isChecked ? med.map((m) => String(m.medcod)) : [];

      // Actualizar todos los checkboxes visibles
      $("#contenidomedico .form-check-input:not(#chkmed-todos)").prop(
        "checked",
        isChecked
      );
    } else {
      // Caso normal para médico individual
      const value = String($(this).val());
      if ($(this).is(":checked")) {
        if (!selectedmed.includes(value)) {
          selectedmed.push(value);
        }
      } else {
        selectedmed = selectedmed.filter((item) => item !== value);
      }

      $("#chkmed-todos").prop("checked", selectedmed.length === med.length);
    }

    // Actualizar contador
    $(".selection-counter").text(
      `Seleccionados: ${selectedmed.length}/${med.length}`
    );
    console.log("Médicos seleccionados:", selectedmed);
  });

  $("#chkmed-todos").on("change", function () {
    const isChecked = $(this).is(":checked");
    $("#contenidomedico .form-check-input").each(function () {
      $(this).prop("checked", isChecked);
      const value = $(this).val();
      if (isChecked && !selectedmed.includes(String(value))) {
        selectedmed.push(String(value));
      } else if (!isChecked) {
        selectedmed = selectedmed.filter((item) => item !== String(value));
      }
    });
    console.log("Médicos seleccionados:", selectedmed);
  });

  // Función similar para servicios (omitiendo por brevedad, pero aplica los mismos principios)
  function renderServicios(filter) {
    const container = $("#contenidoServicio");
    container.empty();

    // Contador de seleccionados
    const counter = $(
      `<div class="selection-counter">Seleccionados: ${selectedser.length}/${ser.length}</div>`
    );
    container.append(counter);

    // Opción "Todos"
    const divTodos = $("<div>").addClass("form-check todos-option");
    const inputTodos = $("<input>")
      .addClass("form-check-input")
      .attr("type", "checkbox")
      .attr("id", "chkser-todos")
      .prop("checked", selectedser.length === ser.length);
    const labelTodos = $("<label>")
      .addClass("form-check-label")
      .attr("for", "chkser-todos")
      .text("Todos los servicios");

    divTodos.append(inputTodos).append(labelTodos);
    container.append(divTodos);

    // Filtrar servicios
    const filteredServicios = ser.filter(
      (obj) =>
        obj.serdes.toLowerCase().includes(filter.toLowerCase()) ||
        String(obj.sercod).includes(filter)
    );

    if (filteredServicios.length === 0) {
      container.append(
        $('<div class="no-results">No se encontraron servicios</div>')
      );
      return;
    }

    filteredServicios.forEach((obj) => {
      const div = $("<div>").addClass("form-check");
      const input = $("<input>")
        .addClass("form-check-input")
        .attr("type", "checkbox")
        .attr("value", obj.sercod)
        .attr("id", `chkser-${obj.sercod}`)
        .prop("checked", selectedser.includes(String(obj.sercod)));
      const label = $("<label>")
        .addClass("form-check-label")
        .attr("for", `chkser-${obj.sercod}`)
        .text(`${obj.serdes} (${obj.sercod})`);

      div.append(input).append(label);
      container.append(div);
    });
  }

  // Manejo de servicios similar al de médicos
  $("#contenidoServicio").on("change", ".form-check-input", function () {
    if ($(this).parent().hasClass("todos-option")) {
      const isChecked = $(this).is(":checked");
      selectedser = isChecked ? ser.map((s) => String(s.sercod)) : [];

      $("#contenidoServicio .form-check-input:not(#chkser-todos)").prop(
        "checked",
        isChecked
      );
    } else {
      const value = String($(this).val());
      if ($(this).is(":checked")) {
        if (!selectedser.includes(value)) {
          selectedser.push(value);
        }
      } else {
        selectedser = selectedser.filter((item) => item !== value);
      }

      $("#chkser-todos").prop("checked", selectedser.length === ser.length);
    }

    $(".selection-counter").text(
      `Seleccionados: ${selectedser.length}/${ser.length}`
    );
    actualizarmedicos();
  });

  $("#searchservicio").on("input", function () {
    const filterText = $(this).val();
    renderServicios(filterText);
  });

  // Búsqueda mejorada
  $("#searchmedico").on("input", function () {
    const filterText = $(this).val();
    renderMedicos(filterText);
  });

  $("#chkser-todos").on("change", function () {
    const isChecked = $(this).is(":checked");
    $("#contenidoServicio .form-check-input").each(function () {
      $(this).prop("checked", isChecked);
      const value = $(this).val();
      if (isChecked && !selectedser.includes(String(value))) {
        selectedser.push(String(value));
      } else if (!isChecked) {
        selectedser = selectedser.filter((item) => item !== String(value));
      }
    });
    actualizarmedicos();
    console.log("Servicios seleccionados:", selectedser);
  });

  function actualizarmedicos() {
    if (solicitudmedico && solicitudmedico.readyState !== 4) {
      solicitudmedico.abort();
    }
    solicitudmedico = $.ajax({
      url: "CRUDMedico?opcion=3", // URL de la API
      method: "POST", // Cambiado a POST
      contentType: "application/json", // Asegúrate de que el servidor espera JSON
      data: JSON.stringify({ servicios: selectedser }), // Convertir los datos a una cadena JSON
      dataType: "json", // Espera una respuesta JSON del servidor
      success: function (data) {
        if (data.resultado === "ok") {
          let nuevosMedicos = data.data;
          let nuevosCodigos = new Set(
            nuevosMedicos.map((medico) => String(medico.medcod))
          ); // O(n)
          let anterioresCodigos = new Set(
            med.map((medico) => String(medico.medcod))
          ); // O(m)

          // Identificar deseleccionados de manera más eficiente
          let deseleccionadosSet = new Set(
            [...anterioresCodigos].filter(
              (codigo) => !selectedmed.includes(codigo)
            )
          ); // O(m)

          // Mantener seleccionados solo los que no fueron deseleccionados
          selectedmed = [...nuevosCodigos].filter(
            (codigo) => !deseleccionadosSet.has(codigo)
          ); // O(n)

          // Actualizar la lista global `med`
          med = nuevosMedicos;
          console.log("Médicos actualizados:", selectedmed);
          renderMedicos(""); // Renderizamos la lista con la nueva selección
          $("#searchmedico").val("");
          $("#chkmed-todos").prop(
            "checked",
            $("#contenidomedico .form-check-input").length ===
              $("#contenidomedico .form-check-input:checked").length
          );
        }
      },
      error: function (jqXHR, textStatus, errorThrown) {
        if (textStatus !== "abort") {
          console.error("Error en la solicitud:", textStatus, errorThrown);
        }
      },
    });
  }

  $("#filtrar").click(function () {
    console.log("filtrando");
    localStorage.setItem("receta_fecini", fechaInicio);
    localStorage.setItem("receta_fecfin", fechaFin);

    fechaInicio = formatFecha($("#fechaInicio").val());
    fechaFin = formatFechamasdia($("#fechaFin").val());

    console.log("Filtrando con:", {
      fechaInicio: fechaInicio,
      fechaFin: fechaFin,
      servicios: selectedser,
      medicos: selectedmed,
      paciente: $("#txtpaciente").val(),
    });

    table.ajax.reload();
  });

  $("#mostrarFiltros").click(function () {
    $("#filtrosAdicionales").toggle();
  });

  $("#tablaRecetas tbody").on("click", ".detalle-btn", function () {
    let tr = $(this).closest("tr");
    let row = table.row(tr);
    let actoMedico = $(this).data("acto");

    if (row.child.isShown()) {
      row.child.hide();
      tr.removeClass("shown");
    } else {
      // Ocultar cualquier otra fila abierta
      $("tr.shown").each(function () {
        let otherRow = table.row(this);
        if (otherRow.child.isShown()) {
          otherRow.child.hide();
          $(this).removeClass("shown");
        }
      });
      $.ajax({
        url: "RecetasCRUD", // Reemplaza con la URL de tu API
        method: "POST",
        dataType: "json",
        data: { actomedico: actoMedico, opcion: 3 },
        success: function (response) {
          console.log(response);
          if (response.resultado === "ok") {
            row.child(mostrarDetalle(response.data)).show();
            tr.addClass("shown");
          } else {
            Toast.fire({
              icon: "error",
              title: "Error al cargar los productos.",
            });
          }
        },
      });
    }
  });

  function mostrarDetalle(datos) {
    let detalleHtml = `<table class="table table-sm">
                         <thead>
                             <tr>
                                 <th>Código</th>
                                 <th>Genérico</th>
                                 <th>Producto</th>
                                 <th>Cantidad</th>
                             </tr>
                         </thead>
                         <tbody>`;

    datos.forEach(function (item) {
      detalleHtml += `<tr>
                          <td>${item.CodproLolfar ? item.CodproLolfar : ""}</td>
                          <td>${item.generico}</td>
                          <td>${item.producto}</td>
                          <td>${item.cantidad}</td>
                      </tr>`;
    });

    detalleHtml += `</tbody></table>`;
    return detalleHtml;
  }
});
