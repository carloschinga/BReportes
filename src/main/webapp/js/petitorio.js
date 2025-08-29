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
  $("#contenido-petitorio").load("datapetitorio.html");
  // Inicializar Select2
  $(".boton-menu").click(function () {
    let menu = String($(this).data("menu"));
    $("#contenido-petitorio").html("");
    $(".boton-menu").removeClass("active");
    if (menu === "petitorio") {
      $("#contenido-petitorio").load("datapetitorio.html");
    } else {
      $("#contenido-petitorio").load("reportepetitorio.html");
    }
    $(this).addClass("active");
  });
});
