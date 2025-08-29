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

    let codinvalm = localStorage.getItem('inventariocodinvalm'); // General de la página

    // Inicializa DataTables
    const table = $('#tabla').DataTable({
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
                previous: "Anterior"
            },
            aria: {
                sortAscending: ": activate to sort column ascending",
                sortDescending: ": activate to sort column descending"
            }
        },
        ajax: {
            url: "CRUDInventario?opcion=5&codinvalm=" + codinvalm
        },
        columns: [
            {data: 'codpro'},
            {data: 'despro'},
            {data: 'lote'},
            {data: 'codalm'},
            {data: 'cante'},
            {data: 'cantf'},
            {data: 'stkalm'},
            {data: 'stkalm_m'},
            {data: 'dife'},
            {data: 'diff'},
            {
                data: null,
                render: function (data, type, row) {
                    return `
                        <button class="btn btn-primary btn-sm ajustar" 
                                data-codpro="${row.codpro}"
                                data-lote="${row.lote}"
                                data-cante="${row.cantea}" 
                                data-cantf="${row.cantfa}">
                            <i class="fa fa-edit"></i>
                        </button>
                    `;
                }
            }
        ],
        pageLength: 10,
        lengthChange: false,
        responsive: true
    });

    // Manejo del botón Ajustar
    let selectedCodpro = null;
    let selectedLote = null;

    $('#tabla tbody').on('click', '.ajustar', function () {
        const button = $(this);
        selectedCodpro = button.data('codpro');
        selectedLote = button.data('lote');

        $('#modalCodpro').val(selectedCodpro);
        $('#modalCante').val(button.data('cante'));
        $('#modalCantf').val(button.data('cantf'));
        $('#ajusteModal').modal('show');
    });

    // Guardar Ajuste
    $('#guardarAjuste').on('click', function () {
        const cante = $('#modalCante').val();
        const cantf = $('#modalCantf').val();

        $.ajax({
            url: `CRUDInventarioToma?opcion=4&codinvalm=${codinvalm}&codpro=${selectedCodpro}&lote=${selectedLote}&cante=${cante}&cantf=${cantf}`,
            method: 'GET',
            dataType: 'json',
            success: function (response) {
                if (response.resultado === "ok") {
                    $('#ajusteModal').modal('hide');
                    table.ajax.reload(null, false); // Recargar tabla sin reiniciar paginación
                    Toast.fire({
                        icon: "success",
                        title: "Ajuste guardado correctamente."
                    });
                } else {
                    Toast.fire({
                        icon: "error",
                        title: "Error al guardar el ajuste."
                    });
                }
            },
            error: function () {
                Toast.fire({
                    icon: "error",
                    title: "Error de conexión al servidor."
                });
            }
        });
    });

    // Manejo del botón Volver
    $('#volver').on('click', function () {
        $("#contenido").load('inventarioalmacen.html');
    });
});