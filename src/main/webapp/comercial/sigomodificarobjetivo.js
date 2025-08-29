$(document).ready(function () {
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
    parametros = JSON.stringify(
      {
        nomb: nomb,
      },
      null
    );
    if (agregar) {
      // agregar
      opcion = 3;
    } else {
      // modificar
      opcion = 6;
    }
    if (
      fechaMesAno !== "" &&
      fechaMesAno !== undefined &&
      fechaMesAno !== null
    ) {
      if (nomb !== "" && nomb !== undefined && nomb !== null) {
        $.ajax({
          url:
            "CRUDsigoobje?opcion=" +
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
              $("#contenido").load("sigoobjetivoestablecimientos.html");
            } else if (data.mensaje === "duplicado") {
              alert(
                "¡Error! Ya existe un objetivo con el mismo tipo y periodo (mes-año)."
              );
            } else {
              if (data.mensaje === "nosession") {
                $.fn.validarSession();
              } else {
                alert("Ya existe un objetivo con el mismo año y mes.");
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
  });

  $("#atras").on("click", function () {
    $("#contenido").load("comercial/sigoObjetivos.html");
  });
});
