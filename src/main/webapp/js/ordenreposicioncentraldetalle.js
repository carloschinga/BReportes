
$(document).ready(function () {
    var tabla;
    $.fn.validarSession = function () {
        $.getJSON("validarsesion", function (data) {
            if (data.resultado === "ok") {
            } else {
                $(location).attr('href', "index.html");
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            $(location).attr('href', "index.html");
        });
    };
    $.fn.validarSession();
    $.fn.listar = function () {
        let invnum = localStorage.getItem('ordenreposicion');
        let sisent = localStorage.getItem('sisent');
        $.getJSON("CRUDFaOrdenReposicionDetalle", {opcion: 1, invnum: invnum}, function (data) {
            if (data.resultado === "ok") {
                
            $("#titulo").text("Orden Reposicion: " + sisent+", Sec: "+invnum);
            $("#titulo2").text("Orden Reposicion: " + sisent+", Sec: "+invnum);
                if (tabla) {
                    tabla.destroy();
                }
                tabla = $('#example').DataTable({
                    data: data.data,
                    paging: false,
                    fixedHeader: {
                        header: true,
                        headerOffset: $('#fixed-title').outerHeight() // Ajusta según el alto de tu cabecera
                    },
                    language: {
                        decimal: "",
                        emptyTable: "No hay datos",
                        info: "Hay un total de _TOTAL_ productos en esta orden",
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
                    columns: [
                        {data: 'numitm'},
                        {data: 'codpro'},
                        {data: 'despro'},
                        {data: 'cante',
                            render: function (data, type, row, meta) {
                                if (data === undefined)
                                    return "";
                                else
                                    return data;
                            }},
                        {data: 'cantf',
                            render: function (data, type, row, meta) {
                                if (data === undefined)
                                    return "";
                                else
                                    return data;
                            }}
                    ]
                });
            } else {
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
        });
    };
    $.fn.listar();



    var fixedTitle = $('#fixed-title');
    var cardHeader = $('#card-header');
    var cardHeaderOffset = cardHeader.offset().top;

    $(window).on('scroll', function () {
        if ($(window).scrollTop() > cardHeaderOffset) {
            fixedTitle.css({
                'display': 'block',
                'width': cardHeader.outerWidth()
            });
        } else {
            fixedTitle.css('display', 'none');
        }
    });

    $(window).on('resize', function () {
        if (fixedTitle.css('display') === 'block') {
            fixedTitle.css('width', cardHeader.outerWidth());
        }
    });

    $("#back-button").click(function () {
        $("#contenido").load('ordenreposicioncentral.html');
    });
});