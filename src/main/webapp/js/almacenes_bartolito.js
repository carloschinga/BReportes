$(document).ready(function () {
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer
        }
    });

    let tabla = $('#tablaAlmacenes').DataTable({
        searching: true,
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
        ajax: 'CRUDAlmacenesBartolito?opcion=1',
        columns: [
            {data: 'descripcion'},
            {data: 'codalmbar'},
            {data: 'm3'},
            {data: 'num'},
            {data: 'codalmbar',
                "render": function (data, type, row) {
                    return `<button class="btn btn-primary btn-sm entrar" data-id="${data}"  data-desc="${row.descripcion}"><i class="fa fa-external-link"></i></button>`;
                }
            },
            {data: 'codalmbar',
                "render": function (data, type, row) {
                    return `
                        <button class="btn btn-warning btn-sm editar" data-id="${data}" data-desc="${row.descripcion}" data-m3="${row.m3}" data-num="${row.num}"><i class="fa fa-edit"></i></button>
                        <button class="btn btn-danger btn-sm eliminar" data-id="${data}"><i class="fa fa-trash"></i></button>
                    `;
                }
            }
        ]
    });

    $('#btnGuardar').click(function () {
        let datos = {
            codalmbar: $('#codalmbar').val() || "", // Si está vacío, es una inserción
            descripcion: $('#almacen').val(),
            m3: $('#m3').val(),
            num: $('#numero').val()
        };

        let opcion = datos.codalmbar ? 3 : 2; // 2 = Insertar, 3 = Modificar

        $.post(`CRUDAlmacenesBartolito?opcion=${opcion}`, datos, function () {
            $('#modalAgregar').modal('hide');
            $('#formAgregar')[0].reset();
            tabla.ajax.reload();
        });
    });

    $('#tablaAlmacenes tbody').on('click', '.entrar', function () {
        let btn = $(this);
        localStorage.setItem('codalmbar', btn.data('id'));
        localStorage.setItem('desc', btn.data('desc'));
        let url = 'almacenes_bartolito_ubicaciones.html';
        $("#contenido").load(url);

    });

    $('#tablaAlmacenes tbody').on('click', '.editar', function () {
        let btn = $(this);
        $('#codalmbar').val(btn.data('id'));
        $('#almacen').val(btn.data('desc'));
        $('#m3').val(btn.data('m3'));
        $('#numero').val(btn.data('num'));
        $('#modalAgregar').modal('show');
    });
    function abrirModalAgregar() {
        $("#formAgregar")[0].reset();
        $('#modalAgregar').modal('show');
        $('#codalmbar').val(""); // Para diferenciar entre agregar y editar
    }
    $("#agregarboton1").on("click", function () {
        abrirModalAgregar();
    });

    $('#tablaAlmacenes tbody').on('click', '.eliminar', function () {
        let codalmbar = $(this).data('id');

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
                $.post('CRUDAlmacenesBartolito?opcion=4', {codalmbar}, function () {
                    tabla.ajax.reload();
                    Toast.fire({icon: "success", title: "Almacén eliminado correctamente"});
                });
            }
        });
    });
});
