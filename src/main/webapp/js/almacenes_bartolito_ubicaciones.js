$(document).ready(function () {
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        }
    });
    let codalmbar=localStorage.getItem('codalmbar');
    let desc=localStorage.getItem('desc');
    $("#titulo").text(desc);
    let tabla = $("#tablaUbicaciones").DataTable({
        searching: true,
        destroy: true,
        pageLength: 20,
        "lengthChange": false,
        language: {
            decimal: "",
            emptyTable: "No hay datos disponibles",
            info: "Mostrando desde el _START_ al _END_ del total de _TOTAL_ registros",
            infoEmpty: "Mostrando desde el 0 al 0 del total de 0 registros",
            infoFiltered: "(Filtrados del total de _MAX_ registros)",
            thousands: ",",
            lengthMenu: "Mostrar _MENU_ registros por página",
            loadingRecords: "Cargando...",
            processing: "Procesando...",
            search: "Buscar:",
            zeroRecords: "No se encontraron coincidencias",
            paginate: {
                first: "Primero",
                last: "Última",
                next: "Siguiente",
                previous: "Anterior"
            },
            aria: {
                sortAscending: ": activar para ordenar la columna de forma ascendente",
                sortDescending: ": activar para ordenar la columna de forma descendente"
            }
        },
        ajax: {
            url: "CRUDAlmacenesBartolitoUbicaciones?opcion=1&codalmbar=" + codalmbar,
            dataSrc: "data"
        },
        columns: [
            {data: "codigo"},
            {data: "rotacion"},
            {data: "m3"},
            {
                data: "balance",
                render: function (data) {
                    return `$${parseFloat(data).toFixed(2)}`; // Formato de valor en dólares
                }
            },
            {
                data: "codubi",
                render: function (data, type, row) {
                    return `
                        <button class="btn btn-warning btn-sm editar" 
                            data-codubi="${row.codubi}" 
                            data-codigo="${row.codigo}" 
                            data-codbar="${row.codbar}" 
                            data-rotacion="${row.rotacion}" 
                            data-m3="${row.m3}">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-danger btn-sm eliminar" data-id="${data}">
                            <i class="fas fa-trash"></i>
                        </button>
                    `;
                }
            }
        ]
    });

    $("#agregarboton").click(function () {
        $("#formUbicacion")[0].reset();
        $("#modalUbicacion").modal("show");
        $("#codubi").val(""); // Para diferenciar entre agregar y editar
    });
    $("#codigo").on("change",function(){
        if($("#codbar").val()===""){
            $("#codbar").val($("#codigo").val());
        }
    });
    $("#tablaUbicaciones tbody").on("click", ".editar", function () {
        let btn = $(this);
        $("#codubi").val(btn.data("codubi"));
        $("#codigo").val(btn.data("codigo"));
        $("#codbar").val(btn.data("codbar"));
        $("#rotacion").val(btn.data("rotacion"));
        $("#m3").val(btn.data("m3"));
        $("#modalUbicacion").modal("show");
    });

    $("#btnGuardar").click(function () {
        let datos = {
            codubi: $("#codubi").val(),
            codigo: $("#codigo").val(),
            codbar: $("#codbar").val(),
            rotacion: $("#rotacion").val(),
            m3: $("#m3").val(),
            codalmbar: codalmbar
        };
        
        let opcion = datos.codubi ? "3" : "2";

        $.post(`CRUDAlmacenesBartolitoUbicaciones?opcion=${opcion}`, datos, function (respuesta) {
            let json = JSON.parse(respuesta);
            if (json.resultado === "ok") {
                $("#modalUbicacion").modal("hide");
                tabla.ajax.reload();
                Toast.fire({icon: "success", title: "Ubicación guardada correctamente"});
            } else {
                Swal.fire("Error", json.mensaje, "error");
            }
        });
    });

    $('#volver').on('click', function () {
        let url = 'almacenes_bartolito.html';
        $("#contenido").load(url);

    });
    $("#tablaUbicaciones tbody").on("click", ".eliminar", function () {
        let codubi = $(this).data("id");
        
        Swal.fire({
            title: "¿Estás seguro?",
            text: "No podrás revertir esto!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Sí, eliminar!"
        }).then((result) => {
            if (result.isConfirmed) {
                $.post("CRUDAlmacenesBartolitoUbicaciones?opcion=4", {codubi}, function (respuesta) {
                    let json = JSON.parse(respuesta);
                    if (json.resultado === "ok") {
                        tabla.ajax.reload();
                        Toast.fire({icon: "success", title: "Ubicación eliminada correctamente"});
                    } else {
                        Swal.fire("Error", json.mensaje, "error");
                    }
                });
            }
        });
    });
});