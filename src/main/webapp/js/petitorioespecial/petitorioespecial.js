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
  // Inicializar Select2
  $(".boton-btnMasReportes").click(function () {
    console.log("Boton de mas reportes");
    $("#contenido-petitorio").load("reportepetitorio.html");
  });
});
