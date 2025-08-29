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
  $.fn.validarSession();
  $("#btnPDF").click(function () {
    $.fn.validarSession();
    let sec1 = $("#secuencia").val();
    let sec2 = $("#secuencia1").val();
    let fechainicio = formatoMesDiaAnio($("#inputFechaInicio").val());
    let fechafin = formatoMesDiaAnio($("#inputFechaFin").val());

    let url =
      "reportefaltante_excedente?tipo=pdf&ordenini=" +
      sec1 +
      "&ordenfin=" +
      sec2 +
      "&fechafin=" +
      fechafin +
      "&fechainicio=" +
      fechainicio;

    window.open(url, "_blank");
  });
  $("#btnEXCEL").click(function () {
    $.fn.validarSession();
    let sec1 = $("#secuencia").val();
    let sec2 = $("#secuencia1").val();
    let fechainicio = formatoMesDiaAnio($("#inputFechaInicio").val());
    let fechafin = formatoMesDiaAnio($("#inputFechaFin").val());

    let url =
      "reportefaltante_excedente?tipo=excel&ordenini=" +
      sec1 +
      "&ordenfin=" +
      sec2 +
      "&fechafin=" +
      fechafin +
      "&fechainicio=" +
      fechainicio;
    window.open(url, "_blank");
  });
  $("#secuencia").change(function () {
    let fechaFin = $("#secuencia").val();
    $("#secuencia1").val(fechaFin);
  });
  function formatoMesDiaAnio(fecha) {
    if (fecha) {
      const [anio, mes, dia] = fecha.split("-");
      return `${dia}-${mes}-${anio}`;
    } else {
      return "";
    }
  }
});
