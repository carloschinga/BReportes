$(document).ready(function () {
  $("#refresh").on("click", function () {
    reloadScript("js/inventarioalmacendetalle.js");
  });

  let estado = "normal";
  let cambio = false;
  let sup = false;
  let tabla;
  let sesi = localStorage.getItem("inventarioestadoalmacen");
  if (sesi) {
    estado = sesi;
  }
  let codinv = localStorage.getItem("inventariocodinv");
  let codinvcab = localStorage.getItem("inventariocodinvcab");
  let codalm = localStorage.getItem("inventariocodalm");

  function verificarFoto(codinvcab, codinv) {
    return new Promise((resolve) => {
      $.ajax({
        type: "GET",
        url: "CRUDinvalmdetalle",
        data: { opcion: 1, codinvcab: codinvcab, codinv: codinv },
        success: function (response) {
          resolve(response.success || false);
        },
        error: function (xhr, status, error) {
          console.error("Error en la solicitud AJAX:", error);
          resolve(false);
        },
      });
    });
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
  async function initTable() {
    const tieneFoto = await verificarFoto(codinvcab, codinv);
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
        url: "CRUDInventarioAlmacen?opcion=1",
        type: "POST",
        data: function (d) {
          d.estado = estado;
          d.codinvcab = codinvcab;
        },
      },
      columns: [
        { data: "desalm" },
        { data: "desinvalm" },
        {
          data: "fecape",
          render: function (data, type, row) {
            const fecha = data
              ? data.split("T")[0]
              : new Date().toISOString().split("T")[0];
            return `<input type="date" class="input-date fecape" value="${fecha}" ${
              estado === "edit" ? "" : "disabled"
            }>`;
          },
        },
        {
          data: "feccir",
          render: function (data, type, row) {
            const fecha = data ? data.split("T")[0] : "";
            return `<input type="date" class="input-date feccir" value="${fecha}" ${
              estado === "edit" ? "disabled" : "disabled"
            }>`;
          },
        },
        {
          data: "estdet",
          render: function (data, type, row) {
            if (!row.codinvalm) {
              return "no registrado.";
            }
            return data === "A" ? "Abierto." : "Cerrado.";
          },
        },
        {
          data: "codalm",
          render: function (data, type, row) {
            if (estado === "edit") {
              return `<input class="check" type="checkbox" data-codalm="${data}" data-codinvalm="${
                row.codinvalm
              }" ${row.codinvalm ? "checked" : ""}>`;
            } else {
              let res = "";
              if (row.estdet === "A") {
                res =
                  `<button ${
                    tieneFoto ? "" : "disabled"
                  } class="btn mb-1 btn-primary btn-sm mr-1 entrar" data-codinvalm="` +
                  row.codinvalm +
                  '"><i class="fa fa-edit"></i></button>';
              }
              if ((sup || row.codrol === "A") && row.estdet === "C") {
                //res += '<button class="btn mb-1 btn-primary btn-sm mr-1 ajuste" data-codinvalm="' + row.codinvalm + '"><i class="fa fa-bars"></i></button>';
              }
              if (sup && row.estdet === "A") {
                res +=
                  '<button class="btn mb-1 btn-primary btn-sm mr-1 usu" data-codinvalm="' +
                  row.codinvalm +
                  '"><i class="fa fa-user"></i></button>';
              }
              if ((sup || row.codrol === "A") && row.estdet === "A") {
                res +=
                  '<button class="btn mb-1 btn-primary btn-sm mr-1 cerrar" data-codinvalm="' +
                  row.codinvalm +
                  '"><i class="fa fa-door-closed"></i></button>';
              }
              res +=
                '<button class="btn mb-1 btn-primary btn-sm mr-1 consolidado" data-codinvalm="' +
                row.codinvalm +
                '"><i class="fa fa-file"></i></button>';
              res +=
                '<button class="btn mb-1 btn-primary btn-sm mr-1 pdf" data-codalm="' +
                data +
                '"><i class="fa fa-file-pdf"></i></button>';
              return res;
            }
          },
        },
      ],
    });
  }

  initTable();

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
          "CRUDInventarioAlmacen",
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
          Toast.fire({
            icon: "error",
            title:
              "No se ha podido cerrar el inventario debido a un error de conexion con el servidor.",
          });
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
    const codinvalm = $(this).data("codinvalm");

    localStorage.setItem("inventariocodinvalm", codinvalm);

    $("#contenido").load("inventarioproductos.html");
  });
  $("#tabla").on("change", ".check, .feccir, .fecape", function () {
    cambio = true;
  });
  $("#guardar").on("click", function () {
    // Crear el JSON con los valores seleccionados
    let registros = [];
    $("#tabla tbody .check:checked").each(function () {
      const row = $(this).closest("tr");
      const codalm = $(this).data("codalm");
      const fecape = row.find(".fecape").val();
      const feccir = row.find(".feccir").val();

      registros.push({
        codalm: codalm,
        fecape: fecape || new Date().toISOString().split("T")[0],
        feccir: feccir || "",
      });
    });

    if (registros.length > 0) {
      // Enviar el JSON al servidor
      $(".check").prop("disabled", true);
      $(".feccir").prop("disabled", true);
      $(".fecape").prop("disabled", true);
      $.ajax({
        url: "CRUDInventarioAlmacen?opcion=2&codinv=" + codinv,
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
              const row = $(this).closest("tr");
              const codalm = $(this).data("codalm");
              const fecape = row.find(".fecape").val();
              const feccir = row.find(".feccir").val();

              registros.push({
                codalm: codalm,
                fecape: fecape || new Date().toISOString().split("T")[0],
                feccir: feccir || new Date().toISOString().split("T")[0],
              });
            });
            if (registros.length > 0) {
              $.ajax({
                url: "CRUDInventarioAlmacen?opcion=2&codinv=" + codinv,
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
        $("#contenido").load("inventarioalmacen.html");
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
              const row = $(this).closest("tr");
              const codalm = $(this).data("codalm");
              const fecape = row.find(".fecape").val();
              const feccir = row.find(".feccir").val();

              registros.push({
                codalm: codalm,
                fecape: fecape || new Date().toISOString().split("T")[0],
                feccir: feccir || new Date().toISOString().split("T")[0],
              });
            });
            if (registros.length > 0) {
              $.ajax({
                url: "CRUDInventarioAlmacen?opcion=2&codinv=" + codinv,
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(registros),
                dataType: "json",
                success: function (response) {
                  if (response.resultado === "ok") {
                    estado = "normal";
                    $("#contenido").load("inventarioalmacen.html");
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
            $("#contenido").load("inventarioalmacen.html");
          }
        });
      }
    } else {
      $("#contenido").load("inventarioalmacen.html");
    }
  });

  $("#recargar").on("click", function () {
    $("#contenido").load("inventarioalmacendetalle.html");
  });
  $("#addEntryForm").on("submit", function (event) {
    event.preventDefault();

    const formData = {
      codinv: codinv,
      codinvcab: codinvcab,
      codalm: codalm,
      opcion: 4,
      ...Object.fromEntries(new URLSearchParams($(this).serialize())),
    };
    $("#guardaragregar").prop("disabled", true);
    $.getJSON("CRUDInventarioAlmacen", formData, function (response) {
      $("#guardaragregar").prop("disabled", false);
      if (response.resultado === "ok") {
        Toast.fire({
          icon: "success",
          title: "Datos guardados exitosamente.",
        });
        tabla.ajax.reload();
        $("#modalagregar").modal("hide");
      } else if (response.mensaje === "cerrado") {
        Toast.fire({
          icon: "error",
          title: "Este Inventario ya ha sido cerrado.",
        });
      } else if (response.mensaje === "abierto") {
        Toast.fire({
          icon: "error",
          title: "La anterior toma aun no ha sido cerrada.",
        });
      } else {
        Toast.fire({
          icon: "error",
          title: "Error al guardar los datos.",
        });
      }
    }).fail(function () {
      $("#guardaragregar").prop("disabled", false);
      Toast.fire({
        icon: "error",
        title: "Error al conectar con el servidor.",
      });
    });
  });
  $("#consolidado").click(function () {
    localStorage.setItem("inventariocodinv", codinv);
    localStorage.setItem("inventariocodalm", codalm);
    localStorage.setItem("inventarioant", "almacen");

    $("#contenido").load("consolidadoinventarioagrupado.html");
  });
  $("#consolidadoproducto").click(function () {
    localStorage.setItem("inventariocodinv", codinv);
    localStorage.setItem("inventariocodalm", codalm);
    localStorage.setItem("inventarioant", "almacen");

    $("#contenido").load("consolidadoinventarioproducto.html");
  });
});
