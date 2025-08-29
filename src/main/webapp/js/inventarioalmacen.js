$(document).ready(function () {
  let estado = "normal";
  let cambio = false;
  let sup = false;
  let tabla;
  let sesi = localStorage.getItem("inventarioestadoalmacen");
  if (sesi) {
    estado = sesi;
  }
  let codinv = localStorage.getItem("inventariocodinv");
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
  $.fn.validarSession = function () {
    $.getJSON("validarsesion", function (data) {
      if (data.resultado === "ok") {
        $("#lblUsuario").text(data.logi);
        if (data.grucod === "SUPINV") {
          $("#editar").show();
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
  tabla = $("#tabla").DataTable();
  if (tabla) {
    $("#tabla").empty();
    tabla.destroy();
  }
  tabla = $("#tabla").DataTable({
    paging: false,
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
        last: "Última",
        next: "Siguiente",
        previous: "Anterior",
      },
      aria: {
        sortAscending: ": activate to sort column ascending",
        sortDescending: ": activate to sort column descending",
      },
    },
    ajax: {
      url: "CRUDInventarioAlmacenCabecera?opcion=1",
      type: "POST",
      data: function (d) {
        d.estado = estado;
        d.codinv = codinv;
      },
    },
    columns: [
      { data: "desalm" },
      {
        data: "codalm",
        render: function (data, type, row) {
          if (estado === "edit") {
            return `<input class="check" type="checkbox" data-codalm="${data}" data-codinvalm="${
              row.codinvcab
            }" ${row.codinvcab ? "checked disabled" : ""}>`;
          } else {
            // Botón de editar siempre visible
            let buttons = `
                <button class="btn mb-1 btn-primary btn-sm mr-1 entrar" 
                        data-codinvcab="${row.codinvcab}" 
                        data-codalm="${data}">
                    <i class="fa fa-edit"></i>
                </button>`;

            // Botón de captura condicional
            if (
              row.invcaptura !== undefined &&
              row.cabcaptura === undefined &&
              row.invcaptura === "S" &&
              sup
            ) {
              buttons += `
                    <button class="btn mb-1 btn-primary btn-sm mr-1 capturar" 
                            data-codinvcab="${row.codinvcab}" 
                            data-codalm="${data}">
                        <i class="fa fa-download"></i>
                    </button>`;
            }

            return buttons;
          }
        },
      },
    ],
  });
  $("#tabla").on("click", ".capturar", function () {
    const codinvalm = $(this).data("codinvcab");
    Swal.fire({
      title: "¿Desea capturar los Stocks de este establecimiento?",
      text: "",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Capturar Stocks",
      cancelButtonText: `Cancelar`,
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        $.getJSON(
          "CRUDInventarioAlmacenCabecera",
          { codinvalm: codinvalm, opcion: 3 },
          function (data) {
            if (data.resultado === "ok") {
              tabla.ajax.reload();
              Toast.fire({
                icon: "success",
                title: "Capturado correctamente.",
              });
            } else {
              Toast.fire({
                icon: "error",
                title:
                  "No se ha podido Capturar debido a un error del sistema.",
              });
            }
          }
        ).fail(function (jqXHR, textStatus, errorThrown) {
          $(location).attr("href", "index.html");
        });
      }
    });
  });
  $("#tabla").on("click", ".pdf", function () {
    let codalm = $(this).data("codalm");
    var urlPDF =
      "reporteinventario?tipo=pdf&codalm=" + codalm + "&codinv=" + codinv;
    window.open(urlPDF, "_blank");
  });
  $("#tabla").on("click", ".ajuste", function () {
    let codinv = $(this).data("codinvalm");

    localStorage.setItem("inventariocodinvalm", codinv);

    $("#contenido").load("inventarioajuste.html");
  });

  $("#tabla").on("click", ".consolidado", function () {
    let codinv = $(this).data("codinvalm");

    localStorage.setItem("inventariocodinvalm", codinv);

    $("#contenido").load("consolidadoinventarioalmacen.html");
  });

  $("#tabla").on("click", ".cerrar", function () {
    const codinvalm = $(this).data("codinvalm");

    Swal.fire({
      title: "¿Desea Cerrar el proceso de inventario?",
      text: "Este proceso no se puede deshacer.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Cerrar Inventario",
      cancelButtonText: `Cancelar`,
    }).then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        $.getJSON(
          "CRUDInventarioAlmacenCabecera",
          { codinvalm: codinvalm, opcion: 3 },
          function (data) {
            if (data.resultado === "ok") {
              tabla.ajax.reload();
              Toast.fire({
                icon: "success",
                title: "Cerrado correctamente.",
              });
            } else {
              Toast.fire({
                icon: "error",
                title:
                  "No se ha podido cerrar el inventario debido a un error del sistema.",
              });
            }
          }
        ).fail(function (jqXHR, textStatus, errorThrown) {
          $(location).attr("href", "index.html");
        });
      }
    });
  });
  $("#tabla").on("click", ".usu", function () {
    const codinvalm = $(this).data("codinvalm");

    localStorage.setItem("inventariocodinvalm", codinvalm);

    $("#contenido").load("inventariousuario.html");
  });
  $("#tabla").on("click", ".entrar", function () {
    const codinvalm = $(this).data("codinvcab");
    const codalm = $(this).data("codalm");

    localStorage.setItem("inventariocodinvcab", codinvalm);
    localStorage.setItem("inventariocodalm", codalm);

    $("#contenido").load("inventarioalmacendetalle.html");
  });
  $("#tabla").on("change", ".check, .feccir, .fecape", function () {
    cambio = true;
  });
  $("#guardar").on("click", function () {
    // Crear el JSON con los valores seleccionados
    let registros = [];
    $("#tabla tbody .check:checked").each(function () {
      const codalm = $(this).data("codalm");

      registros.push({
        codalm: codalm,
      });
    });

    if (registros.length > 0) {
      // Enviar el JSON al servidor
      $(".check").prop("disabled", true);
      $.ajax({
        url: "CRUDInventarioAlmacenCabecera?opcion=2&codinv=" + codinv,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(registros),
        dataType: "json",
        success: function (response) {
          if (response.resultado === "ok") {
            estado = "normal";
            cambio = false;
            $("#editar").html('<i class="fa fa-edit"></i> Editar');
            $("#guardar").hide();
            Toast.fire({
              icon: "success",
              title: "Guardado correctamente.",
            });
            tabla.ajax.reload();
          } else {
            Toast.fire({
              icon: "error",
              title: "Error al guardar los datos.",
            });
          }
        },
        error: function () {
          Toast.fire({
            icon: "error",
            title: "Error de conexión con el servidor.",
          });
        },
      });
    } else {
      Toast.fire({
        icon: "info",
        title: "No hay registros seleccionados.",
      });
    }
  });
  $("#editar").on("click", function () {
    if (estado === "edit") {
      if (cambio) {
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
            let registros = [];
            $("#tabla tbody .check:checked").each(function () {
              const codalm = $(this).data("codalm");

              registros.push({
                codalm: codalm,
              });
            });
            if (registros.length > 0) {
              $.ajax({
                url: "CRUDInventarioAlmacenCabecera?opcion=2&codinv=" + codinv,
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(registros),
                dataType: "json",
                success: function (response) {
                  if (response.resultado === "ok") {
                    $("#editar").html('<i class="fa fa-edit"></i> Editar');
                    estado = "normal";
                    $("#guardar").hide();
                    tabla.ajax.reload();
                  } else {
                    Toast.fire({
                      icon: "error",
                      title: "Error al guardar los datos.",
                    });
                  }
                },
                error: function () {
                  Toast.fire({
                    icon: "error",
                    title: "Error de conexión con el servidor.",
                  });
                },
              });
            } else {
              Toast.fire({
                icon: "info",
                title: "No hay registros seleccionados.",
              });
            }
          } else if (result.isDenied) {
            $("#editar").html('<i class="fa fa-edit"></i> Editar');
            $("#guardar").hide();
            estado = "normal";
            tabla.ajax.reload();
          }
        });
      } else {
        $("#editar").html('<i class="fa fa-edit"></i> Editar');
        $("#guardar").hide();
        estado = "normal";
        tabla.ajax.reload();
      }
    } else {
      $("#editar").html("cancelar");
      $("#guardar").show();
      estado = "edit";
      tabla.ajax.reload();
    }
    cambio = false;
  });
  $("#volver").on("click", function () {
    if (estado === "edit") {
      if (!cambio) {
        $("#contenido").load("inventario.html");
      } else {
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
            let registros = [];
            $("#tabla tbody .check:checked").each(function () {
              const codalm = $(this).data("codalm");
              registros.push({
                codalm: codalm,
              });
            });
            if (registros.length > 0) {
              $.ajax({
                url: "CRUDInventarioAlmacenCabecera?opcion=2&codinv=" + codinv,
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(registros),
                dataType: "json",
                success: function (response) {
                  if (response.resultado === "ok") {
                    estado = "normal";
                    $("#contenido").load("inventario.html");
                  } else {
                    Toast.fire({
                      icon: "error",
                      title: "Error al guardar los datos.",
                    });
                  }
                },
                error: function () {
                  Toast.fire({
                    icon: "error",
                    title: "Error de conexión con el servidor.",
                  });
                },
              });
            } else {
              Toast.fire({
                icon: "info",
                title: "No hay registros seleccionados.",
              });
            }
          } else if (result.isDenied) {
            $("#contenido").load("inventario.html");
          }
        });
      }
    } else {
      $("#contenido").load("inventario.html");
    }
  });

  $("#recargar").on("click", function () {
    $("#contenido").load("inventarioalmacen.html");
  });
});
