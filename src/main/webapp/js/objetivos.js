$(document).ready(function () {
  var tabla = $("#tabla").DataTable();
  const Swal = window.Swal;
  const Toast = Swal.mixin({
    toast: true,
    position: "top-end",
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: true,
    didOpen: (toast) => {
      toast.addEventListener("mouseenter", Swal.stopTimer);
      toast.addEventListener("mouseleave", Swal.resumeTimer);
    },
  });
  function showSuccess(message) {
    Toast.fire({
      icon: "success",
      title: message,
    });
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
  $.fn.validarSession();
  $.fn.listar = function () {
    if (tabla) {
      tabla.destroy();
    }
    tabla = $("#tabla").DataTable({
      paginate: false,
      searching: false,
      fixedHeader: {
        header: true,
        footer: true,
      },
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
        url: "CRUDobjetivos?opcion=1",
      },
      columns: [
        { data: "codobj" },
        { data: "desobj" },
        { data: "feccre" },
        { data: "mesano" },
        {
          data: null,
          render: function (data, type, row) {
            return row.tipo === "p" ? "Soles." : "Unidades.";
          },
        },
        {
          data: null,
          render: function (data, type, row) {
            return (
              "<button class='btn btn-info' data-codobj='" +
              row.codobj +
              "'><i class='fa fa-edit'></i></button><button class='btn btn-danger' data-codobj='" +
              row.codobj +
              "'><i class='fa fa-trash'></i></button>"
            );
          },
        },
      ],
    });
  };
  $.fn.listar();
  $("#tabla").on("click", ".btn-danger", function () {
    let codobj = $(this).data("codobj");
    // Meter el swal para un preconfinmacion
    Swal.fire({
      title: "¿Eliminar item?",
      html: `¿Estás seguro de eliminar el item?`,
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      cancelButtonColor: "#3085d6",
      confirmButtonText: "Sí, eliminar",
      cancelButtonText: "Cancelar",
    }).then((result) => {
      if (result.isConfirmed) {
        $.getJSON(
          "CRUDobjetivos",
          { opcion: 4, codobj: codobj },
          function (data) {
            if (data.resultado === "ok") {
              showSuccess("Eliminado correctamente");
              tabla.ajax.reload();
            } else if (data.mensaje === "repetido") {
              alert("Ya se encuentra este producto agregado");
            } else if (data.mensaje === "nosession") {
              $.fn.validarSession();
            } else {
              alert("Error de conexion con el servidor");
            }
          }
        ).fail(function (jqXHR, textStatus, errorThrown) {
          alert("Error de conexion con el servidor");
        });
      }
    });
  });
  $("#tabla").on("click", ".btn-info", function () {
    let codobj = $(this).data("codobj");
    localStorage.setItem("codobj", codobj);

    $("#contenido").load("modificarobjetivo.html");
  });
  $("#agregarboton").click(function () {
    localStorage.removeItem("codobj");
    $("#contenido").load("modificarobjetivo.html");
  });
});
