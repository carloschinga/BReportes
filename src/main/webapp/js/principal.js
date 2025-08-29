$(document).ready(function () {
  $.fn.validarSession = function () {
    $.getJSON("validarsesion", function (data) {
      if (data.resultado === "ok") {
        console.log({ data });
        $("#lblUsuario").text(data.logi);
        $("#lblGrupo").text(data.grudes);
        $("#lblEstablecimiento").text(data.sisent);
        if (data.empr === "a") {
          $(".emprB").hide();
          if (data.de === "l") {
            if (data.central === "S") {
              $(".nocentral").hide();
              $(".barto").hide();
              $(".inventario").hide();
              $(".emprA").show();
              $(".boss").show();
            } else {
              $(".emprA").hide();
              $(".barto").hide();
              $(".inventario").hide();
              $(".nocentral").show();
              if (data.codalminv === "A5") {
                $(".privada-para-ti").show();
                $(".nino").show();
              }
            }
          } else if (data.de === "b") {
            $(".emprA").hide();
            $(".nocentral").hide();
            $(".inventario").hide();
            $(".barto").show();
          } else if (data.de === "i") {
            $(".inventario").show();
          }
          if (data.grucod === "JEFINA") {
            $(".JEFINA").show();
          }
        } else {
          $(".emprA").hide();
          $(".nocentral").hide();
          $(".emprB").show();
          $(".emprA").show();
          $(".barto").show();
          $(".nocentral").show();
        }
      } else {
        $(location).attr("href", "index.html");
      }
    }).fail(function (jqXHR, textStatus, errorThrown) {
      $(location).attr("href", "index.html");
    });
  };
  $.fn.validarSession();

  $("#cerrarSesion").click(function () {
    $.getJSON("CerrarSesion", function (data) {
      if (data.resultado === "ok") {
        if (data.empr === undefined) {
          $(location).attr("href", "index.html");
        } else {
          $(location).attr("href", "index.html?empr=" + data.empr);
        }
      } else {
        alert("Error del servicio");
      }
    }).fail(function (jqXHR, textStatus, errorThrown) {
      alert("Error: No se logro hacer la conexion con el servicio");
    });
  });
  $("#btnDistribucion").click(function () {
    $("#contenido").load("distribucion.html");
  });
  $("#btnGuiaTransferencia").click(function () {
    $("#contenido").load("guiatransferencia.html");
  });

  $("#btnDashboard").click(function () {
    $("#contenido").load("dashboard.html");
  });
  $("#btnReceta").click(function () {
    console.log("Redirigiendo a clinica - receta");
    $("#contenido").load("clinica/receta.html");
  });
  $("#btnRecetaPrivada").click(function () {
    console.log("Redirigiendo a clinica - receta");
    $("#contenido").load("clinica/receta.html");
  });

  $("#btnProductos").click(function () {
    $("#contenido").load("productos.html");
  });
  $("#btnalmacenes").click(function () {
    $("#contenido").load("almacenes.html");
  });
  $("#btnCajas").click(function () {
    $("#contenido").load("cajas.html");
  });
  $("#btnAperturaCaja").click(function () {
    $("#contenido").load("aperturacaja.html");
  });
  $("#btnCierreCaja").click(function () {
    $("#contenido").load("cierrecaja.html");
  });
  $("#btnFacturacion").click(function () {
    $("#contenido").load("facturacion.html");
  });
  $("#btnKardex").click(function () {
    $("#contenido").load("kardex.html");
  });
  $("#btnRestricciones").click(function () {
    $("#contenido").load("restricciones.html");
  });
  $("#btnCargosDescargos").click(function () {
    $("#contenido").load("cargosDescargos.html");
  });
  $("#btnPickingList").click(function () {
    $("#contenido").load("pickingList.html");
  });
  $("#btnRepRes").click(function () {
    $("#contenido").load("reporterestricciones.html");
  });
  $("#btnUsuariosBartolito").click(function () {
    $("#contenido").load("usuariosbartolito.html");
  });
  $("#btnRepTiem").click(function () {
    $("#contenido").load("reportetiempopicking.html");
  });
  $("#btnRepOTCaja").click(function () {
    $("#contenido").load("reporteOrdenCaja.html");
  });
  $("#btnGestionOT").click(function () {
    $("#contenido").load("gestionOrdenTrabajo.html");
  });
  $("#btnApliFrac").click(function () {
    $("#contenido").load("aplicFrac.html");
  });
  $("#btnInmovLot").click(function () {
    $("#contenido").load("inmobilizar.html");
  });
  $("#btnRepQRProductos").click(function () {
    $("#contenido").load("repQRProductos.html");
  });
  $("#btnConsultarStock").click(function () {
    $("#contenido").load("consultarStock.html");
  });
  $("#btnparambart").click(function () {
    $("#contenido").load("opciones.html");
  });
  $("#btnProductosPlantilla").click(function () {
    $("#contenido").load("ReporteProductosPrecio.html");
  });
  $("#btnRecepcionCajas").click(function () {
    $("#contenido").load("recepcioncajas.html");
  });
  $("#btnOrdenReposicion").click(function () {
    $("#contenido").load("ordenreposicion.html");
  });
  $("#btnOrdenesReposicionCentral").click(function () {
    $("#contenido").load("ordenreposicioncentral.html");
  });
  $("#btnPetitorio").click(function () {
    $("#contenido").load("petitorio.html");
  });
  $("#btnPetitorioTecnico").click(function () {
    $("#contenido").load("petitorioTecnico.html");
  });
  $("#btnPetitorioPorEspecialidad").click(function () {
    $("#contenido").load("petitorioEspecial.html");
  });
  $("#btnEmbalaje").click(function () {
    $("#contenido").load("reporteEtiquetasEmbalaje.html");
  });
  $("#btnRepPetitorio").click(function () {
    $("#contenido").load("reportepetitorio.html");
  });
  $("#btncompras").click(function () {
    $("#contenido").load("comprasreposicion.html");
  });
  $("#btnEmbalajenuevo").click(function () {
    $("#contenido").load("reporteetiquetasenvalajenuevo.html");
  });
  $("#btnRepFaltanteexcedente").click(function () {
    $("#contenido").load("reportefaltanteexcedente.html");
  });
  $("#btnabrirot").click(function () {
    $("#contenido").load("abrirotrecepcion.html");
  });
  $("#btnobjetivos").click(function () {
    $("#contenido").load("objetivos.html");
  });
  $("#btnotrosobjetivos").click(function () {
    $("#contenido").load("otrosobjetivos.html");
  });
  $("#btnrepocicionrecepcion").click(function () {
    $("#contenido").load("reposicionrecepcion.html");
  });
  $("#btnRepTiemRecep").click(function () {
    $("#contenido").load("reportetiemposrecepcion.html");
  });
  $("#btnDistribucionGenerico").click(function () {
    $("#contenido").load("distribucion_generico.html");
  });
  $("#btnreceta").click(function () {
    $("#contenido").load("hechrecetas.html");
  });
  $("#btnseguimiento").click(function () {
    $("#contenido").load("seguimientoentrega.html");
  });
  $("#btninventario").click(function () {
    $("#contenido").load("inventario.html");
  });
  $("#btnUsuariosInventario").click(function () {
    $("#contenido").load("usuarioinventario.html");
  });
  $("#btnRepRegistroProductos").click(function () {
    $("#contenido").load("reporteordentrabajoserdoc.html");
  });
  $("#btnpagosvirtuales").click(function () {
    $("#contenido").load("pagosvirtuales.html");
  });
  $("#btnGuiaTransferencia2").click(function () {
    $("#contenido").load("pickinglistagrupado.html");
  });
  $("#btnmatchpickmov").click(function () {
    $("#contenido").load("matchpickmov.html");
  });
  $("#btnAlmacenamiento").click(function () {
    $("#contenido").load("almacenamiento.html");
  });
  $("#btnSincronizacion").click(function () {
    $("#contenido").load("sincronizacion.html");
  });
  $("#btnManuales").click(function () {
    $("#contenido").load("manuales.html");
  });
  $("#btnalmacenesbartolito").click(function () {
    $("#contenido").load("almacenes_bartolito.html");
  });
  $("#btnGruposPermisos").click(function () {
    $("#contenido").load("grupospermisos.html");
  });
  $("#btnPermisos").click(function () {
    $("#contenido").load("permisos.html");
  });
});
