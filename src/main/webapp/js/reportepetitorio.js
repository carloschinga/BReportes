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

  $("#btnAtras").click(function (e) {
    $("#contenido").load("petitorioEspecial.html");
  });
  $.fn.validarSession();
  $.ajax({
    url: "CRUDMedico?opcion=6",
    method: "GET",
    dataType: "json",
    success: function (response) {
      if (response.resultado === "ok") {
        $("#select-doctor").append(
          '<option value="">Seleccione un médico</option>'
        );
        $.each(response.data, function (index, doctor) {
          $("#select-doctor").append(new Option(doctor.mednam, doctor.medcod));
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
  $("#btnimprimir").click(function () {
    const urlPDF = "reporte_petitorio_todo?tipo=pdf";
    window.open(urlPDF, "_blank");
  });
  $("#btnimprimirexcel").click(function () {
    const urlExcel = "reporte_petitorio_todo?tipo=excel";
    window.open(urlExcel, "_blank");
  });
  $("#btnimprimir2").click(function () {
    window.open("reportepetitorioproducto?tipo=pdf", "_blank");
  });
  $("#btnimprimirexcel2").click(function () {
    const urlExcel = "reportepetitorioproducto?tipo=excel";

    // Abrir una nueva ventana con la URL
    window.open(urlExcel, "_blank");
  });
  $("#btnimprimir3").click(function () {
    window.open("reporte_petitorio_especialidad?tipo=pdf", "_blank");
  });
  $("#btnimprimirexcel3").click(function () {
    const urlExcel = "reporte_petitorio_especialidad?tipo=excel";

    // Abrir una nueva ventana con la URL
    window.open(urlExcel, "_blank");
  });
  $("#btnimprimir4").click(function () {
    if (
      $("#select-doctor").val() !== "" &&
      $("#select-doctor").val() !== undefined &&
      $("#select-doctor").val() !== null
    ) {
      const medcod = $("#select-doctor").val();
      const medcodText = $("#select-doctor").find("option:selected").text();

      // Construir la URL para el PDF
      const urlPDF =
        "reportepetitorio?medcod=" +
        encodeURIComponent(medcod) +
        "&medcodText=" +
        encodeURIComponent(medcodText) +
        "&tipo=pdf";

      // Abrir una nueva ventana con la URL
      window.open(urlPDF, "_blank");
    } else {
      alert("Eliga un Médico.");
    }
  });
  $("#btnimprimirexcel4").click(function () {
    if (
      $("#select-doctor").val() !== "" &&
      $("#select-doctor").val() !== undefined &&
      $("#select-doctor").val() !== null
    ) {
      const medcod = $("#select-doctor").val();
      const medcodText = $("#select-doctor").find("option:selected").text();
      // Construir la URL para el Excel
      const urlExcel =
        "reportepetitorio?medcod=" +
        encodeURIComponent(medcod) +
        "&medcodText=" +
        encodeURIComponent(medcodText) +
        "&tipo=excel";
      console.log(urlExcel);
      // Abrir una nueva ventana con la URL
      window.open(urlExcel, "_blank");
    } else {
      alert("Eliga un Médico.");
    }
  });
  $("#select-doctor").select2({
    placeholder: "Seleccione un médico",
    width: "100%",
  });
});
