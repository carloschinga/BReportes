$(document).ready(function() {
    
    let sup = false;
    $.fn.validarSession = function () {
        $.getJSON("validarsesion", function (data) {
            if (data.resultado === "ok") {
                $("#lblUsuario").text(data.logi);
                if (data.grucod === "SUPINV") {
                    $("#agregar").show();
                    sup = true;
                }
            } else {
                $(location).attr('href', "index.html");
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            $(location).attr('href', "index.html");
        });
    };
    $.fn.validarSession();
    let codinvalm = localStorage.getItem('inventariocodinvalm');
    // Inicializa DataTables
    const table = $('#tabla').DataTable({
        ajax: {
            url: "CRUDInventario?opcion=4&codinvalm="+codinvalm
        },
        columns: [
            { data: 'codpro' },
            { data: 'despro' },
            {
                data: 'usenam',
                render: function (data, type, row) {
                    return data?data:"";
                }
            },
            { data: 'lote' },
            { data: 'codalm' },
            { data: 'cante' },
            { data: 'cantf' },
            { data: 'stkalm' },
            { data: 'stkalm_m' },
            { data: 'dife' },
            { data: 'diff' },
            {
                data: 'cospro',
                className: sup?'':'nomostrar', // Añadir clase para ocultar
                visible: false, // Ocultar la columna en la tabla
                render: function (data, type, row) {
                    return (data * row.cante).toFixed(2);
                }
            }
        ],
        pageLength: 10, // Fija la paginación a 10 elementos por página
        lengthChange: false, // Oculta la opción para cambiar el número de filas mostradas
        responsive: true,
        dom: 'Bfrtip', // Define la posición de los botones
        buttons: [
            {
                extend: 'copy', // Botón de Copiar
                text: 'Copiar',
                className: 'btn btn-info',
                exportOptions: {
                    columns: ':not(.novisible),:hidden' // Excluye las columnas con la clase novisible
                }
            },
            {
                extend: 'csv', // Botón de CSV
                text: 'CSV',
                className: 'btn btn-primary',
                exportOptions: {
                    columns: ':not(.novisible),:hidden' // Excluye las columnas
                }
            },
            {
                extend: 'excel', // Botón de Excel
                text: 'Excel',
                className: 'btn btn-success',
                exportOptions: {
                    columns: ':not(.novisible),:hidden' // Excluye las columnas
                }
            },
            {
                extend: 'pdf', // Botón de PDF
                text: 'PDF',
                className: 'btn btn-danger',
                orientation: 'landscape',
                pageSize: 'A4',
                exportOptions: {
                    columns: ':not(.novisible),:hidden' // Excluye las columnas
                }
            },
            {
                extend: 'print', // Botón de Imprimir
                text: 'Imprimir',
                className: 'btn btn-warning',
                exportOptions: {
                    columns: ':not(.novisible),:hidden' // Excluye las columnas
                }
            }
        ]
    });

    // Manejo del botón Volver
    $('#volver').on('click', function() {
        $("#contenido").load('inventarioalmacendetalle.html');
        
    });
});