$(document).ready(function () {
  let productData = [];
  let codpro = "";
  let lote = "";
  const $input = $("#searchInput");
  const $dropdownMenu = $("#dropdownMenu");
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
      } else {
        $(location).attr("href", "index.html");
      }
    }).fail(function (jqXHR, textStatus, errorThrown) {
      $(location).attr("href", "index.html");
    });
  };
  function filtrarOpciones() {
    const valor = $input.val().trim().toLowerCase();
    $dropdownMenu.empty();

    if (valor.length === 0) {
      $dropdownMenu.hide();
      return;
    }

    const resultados = productData.filter((op) =>
      op.despro.toLowerCase().includes(valor)
    );

    if (resultados.length > 0) {
      resultados.forEach((opcion, index) => {
        let $item = $(
          '<li><a class="dropdown-item" href="#" data-codpro="' +
            opcion.codpro +
            '">' +
            opcion.despro +
            "(" +
            opcion.codpro +
            ")</a></li>"
        );
        if (index === 0) $item.find(".dropdown-item").addClass("active"); // Resalta el primero
        $dropdownMenu.append($item);
      });
      $dropdownMenu.show();
    } else {
      $dropdownMenu.hide();
    }
  }
  $input.on("input", filtrarOpciones);
  $input.on("keydown", function (e) {
    let $items = $dropdownMenu.find(".dropdown-item");
    if ($items.length === 0) return;

    let $active = $dropdownMenu.find(".dropdown-item.active");
    let index = $items.index($active);

    if (e.key === "ArrowDown") {
      index = (index + 1) % $items.length;
    } else if (e.key === "ArrowUp") {
      index = (index - 1 + $items.length) % $items.length;
    } else if (e.key === "Enter") {
      seleccionarOpcion(String($active.data("codpro")), $active.text());
      return false;
    }

    $items.removeClass("active");
    $items.eq(index).addClass("active");
  });

  $(document).on("click", function (event) {
    if (!$(event.target).closest(".dropdown").length) {
      $dropdownMenu.hide();
    }
  });

  $dropdownMenu.on("click", ".dropdown-item", function () {
    seleccionarOpcion(String($(this).data("codpro")), $(this).text());
  });

  function seleccionarOpcion(valor, despro) {
    $dropdownMenu.hide();
    $input.val("");
    $("#codigo").val("");
    $("#nombre").text(despro);
    cargarDatos(valor);
  }
  $.getJSON("CRUDProductos?opcion=15", function (data) {
    //picking ant 10 nuev 49, se puso 9, devuelta 10
    productData = data.data;
    $("#codigo").prop("disabled", false);
    setTimeout(function () {
      $("#codigo").focus();
    }, 100);
  });
  $("#codigo").on("input", function () {
    let qr = String($(this).val());
    console.log(qr);
    if (qr !== "") {
      let productoEncontrado = productData.find(
        (producto) => producto.codpro === qr || producto.qr === qr
      );
      if (productoEncontrado) {
        codpro = productoEncontrado.codpro;
        $("#nombre").text(productoEncontrado.despro);
        cargarDatos(String(productoEncontrado.codpro));
      } else {
        $("#nombre").text("");
        codpro = "";
      }
    } else {
      $("#nombre").text("");
    }
  });
  var tabla = $("#tabla").DataTable({
    paging: false,
    fixedHeader: true,
    searching: false,
    language: {
      decimal: "",
      emptyTable: "No hay datos",
      info: "Mostrando desde el _START_ al _END_ del total de _TOTAL_ registros",
      infoEmpty: "Mostrando desde el 0 al 0 del total de 0 registros",
      infoFiltered: "(Filtrados del total de _MAX_ registros)",
      thousands: ",",
      lengthMenu: "Mostrar _MENU_ registros por página",
      loadingRecords: "Cargando...",
      processing: "Procesando...",
      search: "Buscar:",
      zeroRecords: "No se ha encontrado nada a través de ese filtrado.",
      paginate: {
        first: "Primero",
        last: "Última",
        next: "Siguiente",
        previous: "Anterior",
      },
      aria: {
        sortAscending: ": activar para ordenar columna ascendente",
        sortDescending: ": activar para ordenar columna descendente",
      },
    },
    data: [], // Inicializar sin datos
    columns: [
      { data: "lote" },
      { data: "fecven" },
      { data: "cant" },
      {
        data: "ubic",
        render: function (data, type, row) {
          return data ? data : "Recepción";
        },
      },
      {
        data: "lote",
        render: function (data, type, row) {
          return (
            "<button class='btn btn-info' data-lote='" +
            data +
            "'><i class='fa fa-edit'></i></button>"
          );
        },
      },
    ],
  });
  function cargarDatos(codpro1) {
    codpro = codpro1;
    $.ajax({
      url: "CRUDFaStockVencimientos", // Cambia esto por la URL real
      type: "POST", // O "GET" según corresponda
      data: { codpro: codpro1, opcion: "1" }, // Parámetros que envías
      dataType: "json",
      success: function (response) {
        tabla.clear(); // Limpia la tabla antes de insertar nuevos datos
        tabla.rows.add(response.data); // Agrega los datos obtenidos
        tabla.draw(); // Redibuja la tabla con los nuevos datos
      },
      error: function (xhr, status, error) {
        console.error("Error al cargar los datos:", error);
      },
    });
  }
  let cante = 0;
  let cantf = 0;
  let stkfra = 1;
  let tipo = "1";
  $("#tabla").on("click", ".btn", function () {
    var rowData = $("#tabla").DataTable().row($(this).closest("tr")).data();

    $("#modal-lote").text(rowData.lote);
    $("#modal-fecven").text(rowData.fecven);
    $("#modal-cant").text(rowData.cant);
    $("#modal-ubic").text(rowData.ubic ? rowData.ubic : "Recepción");
    $("#modal-producto").text($("#nombre").text());
    $("#origentxt").val(rowData.ubic ? rowData.ubic : "Recepción");
    cante = rowData.cante;
    cantf = rowData.cantf;
    stkfra = rowData.stkfra;
    tipo = rowData.tipo;
    lote = rowData.lote;
    $("#destinaciones").empty();
    agregarUbicacion(rowData.cante, rowData.cantf, 1, false); // Agrega la primera fila sin botón eliminar

    $("#modal-movimiento").modal("show");
  });

  $("#btn-agregar-ubic").on("click", function () {
    var numUbicaciones = $(".destino-item").length + 1;
    agregarUbicacion("", "", numUbicaciones, true);
  });

  function agregarUbicacion(cante = "", cantf = "", index, allowDelete) {
    var fila = `<div class="row mt-2 destino-item">
                        <div class="col-md-5">
                            <div class="ms-2">Ubicación ${index}</div>
                            <input class="form-control ubicacion" id="ubic-${index}" type="text" placeholder="Ubicación" required>
                        </div>
                        <div class="col-md-3">
                            <div class="ms-2">Cante ${index}</div>
                            <input class="form-control cante" type="number" min="1" placeholder="Cante" value="${cante}">
                        </div>
                        <div class="col-md-3">
                            <div class="ms-2">Cantf ${index}</div>
                            <input class="form-control cantf" type="number" min="1" placeholder="Cantf" value="${cantf}">
                        </div>
                        ${
                          allowDelete
                            ? '<div class="col-md-1"> <div class="ms-2">E:</div><button class="btn btn-danger btn-eliminar"><i class="fas fa-trash"></i></button></div>'
                            : ""
                        }
                    </div>`;
    $("#destinaciones").append(fila);
    let tiem = 100;
    if (index === 1) {
      tiem = 600;
    }
    setTimeout(function () {
      $("#ubic-" + index).focus();
    }, tiem);
  }

  $("#destinaciones").on("change", ".cante, .cantf", function () {
    if (!validarCantidadTotal()) {
      $(this).val("");
    }
  });
  function validarCantidadTotal() {
    var sumaTotal = 0;

    $(".destino-item").each(function () {
      var cante = parseInt($(this).find(".cante").val()) || 0;
      var cantf = parseInt($(this).find(".cantf").val()) || 0;
      sumaTotal += cante * stkfra + cantf;
    });

    if (sumaTotal > cante * stkfra + cantf) {
      Toast.fire({
        icon: "error",
        title: "El numero excede lo que hay en esta ubicacion.",
      });
      return false;
    }
    return true;
  }

  $("#destinaciones").on("change", ".ubicacion", function () {
    var origen = $("#origentxt").val();
    var ubicaciones = [];
    var error = false;
    var obj = $(this);

    $(".ubicacion").each(function () {
      var valor = $(this).val();
      if (valor) {
        if (ubicaciones.includes(valor) || valor === origen) {
          Toast.fire({
            icon: "error",
            title:
              "No se puede repetir una ubicación ni usar la ubicación de origen.",
          });
          obj.val(""); // Limpiar campo incorrecto
          error = true;
          return false; // Rompe el loop
        }
        ubicaciones.push(valor);
      }
    });
  });

  $("#destinaciones").on("click", ".btn-eliminar", function () {
    $(this).closest(".destino-item").remove();
  });
  let click = false;
  $("#guardar").on("click", function () {
    if (click) {
    } else {
      click = true;
      let ubi = $("#origentxt").val();

      if (!validarCantidadTotal()) {
        click = false;
        return;
      }
      var destinos = [];
      var error = false;
      let sumtotal = 0;
      $(".destino-item").each(function () {
        var ubicacion = $(this).find(".ubicacion").val();
        var cante = Number(
          $(this).find(".cante").val() ? $(this).find(".cante").val() : 0
        );
        var cantf = Number(
          $(this).find(".cantf").val() ? $(this).find(".cantf").val() : 0
        );
        sumtotal += cante * stkfra + cantf;
        console.log(sumtotal);
        if (!ubicacion || ((cante ? cante : 0) <= 0 && cantf <= 0)) {
          error = true;
        }

        destinos.push({ ubicacion: ubicacion, cante: cante, cantf: cantf });
      });
      if (error) {
        Toast.fire({
          icon: "error",
          title: "Se encontraron ubicaciones invalidas, verifique.",
        });
        click = false;
        return;
      }
      console.log(sumtotal + " - " + cante * stkfra);
      let nuev_cante = Math.floor((cante * stkfra + cantf - sumtotal) / stkfra);
      let nuev_cantf = Math.floor((cante * stkfra + cantf - sumtotal) % stkfra);

      $.ajax({
        url: "CRUDFaStockVencimientos?opcion=2",
        type: "POST",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
          codpro: codpro,
          lote: lote,
          tipo: tipo,
          cante: nuev_cante,
          cantf: nuev_cantf,
          ubicacion: ubi,
          ubicaciones: destinos,
        }),
        success: function (data) {
          if (data.resultado === "ok") {
            cargarDatos(codpro);
            $("#modal-movimiento").modal("hide");

            Toast.fire({
              icon: "success",
              title: "Movimiento Guardado Correctamente.",
            });
          } else {
            Toast.fire({
              icon: "error",
              title: "Error al actualizar.",
            });
          }
          click = false;
        },
        error: function () {
          click = false;
          Toast.fire({
            icon: "error",
            title: "Error en la solicitud.",
          });
        },
      });
    }
  });
  $("#modal-movimiento").on("hidden.bs.modal", function () {
    setTimeout(function () {
      $("#codigo").focus();
    }, 600);
  });
});
