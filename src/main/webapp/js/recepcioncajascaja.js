window.onscroll = function () {
    var backButton = document.getElementById('back-button');
    if (document.body.scrollTop > 50 || document.documentElement.scrollTop > 50) {
        backButton.style.top = '20px'; // Nueva altura cuando el usuario hace scroll
    } else {
        backButton.style.top = '100px'; // Altura inicial
    }
};

$(document).ready(function () {
    let tipousuario = "";
    $.fn.validarSession = function () {
        $.getJSON("validarsesion", function (data) {
            if (data.resultado === "ok") {
                tipousuario = data.de;
            } else {
                $(location).attr('href', "index.html");
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            $(location).attr('href', "index.html");
        });
    };
    $.fn.validarSession();
    let sec = localStorage.getItem('caja_orden');
    $.getJSON("picking", {ord: sec, opcion: 44}, function (data) {// ant 9, act: 44
        if (data.resultado === "ok") {
            let com = "S";
            if (data.com === null || data.com === undefined || data.com === "N") {
                $("#divboton").show();
                com = "N";
            } else {
                $("#divbotonrep").show();

                $("#divdif").show();
                $('#faltantes').DataTable({
                    searching: false,
                    paging: false,
                    info: false,
                    ajax: {
                        url: "picking",
                        type: "GET",
                        data: function (d) {
                            d.opcion = 45;// ant 27 nuev 45
                            d.ord = sec;
                        }
                    },
                    language: {
                        decimal: "",
                        emptyTable: "No hay datos",
                        info: "Mostrando desde el _START_ al _END_ del total de _TOTAL_ registros",
                        infoEmpty: "Mostrando desde el 0 al 0 del total de 0 registros",
                        infoFiltered: "(Filtrados del total de _MAX_ registros)",
                        infoPostFix: "",
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
                            previous: "Anterior"
                        },
                        aria: {
                            sortAscending: ": activar para ordenar la columna de forma ascendente",
                            sortDescending: ": activar para ordenar la columna de forma descendente"
                        }
                    },
                    columns: [
                        {data: 'invnum'},
                        {data: 'codpro'},
                        {data: 'despro'},
                        {data: 'calculo'}
                    ]
                });
            }
            $("#nombre").text("Recepción de Picking: " + sec);

            $('#example').DataTable({
                searching: false,
                paging: false,
                info: false,
                data: data.secuencias, // Cargar datos de la variable
                language: {
                    decimal: "",
                    emptyTable: "No hay datos",
                    info: "Mostrando desde el _START_ al _END_ del total de _TOTAL_ registros",
                    infoEmpty: "Mostrando desde el 0 al 0 del total de 0 registros",
                    infoFiltered: "(Filtrados del total de _MAX_ registros)",
                    infoPostFix: "",
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
                        previous: "Anterior"
                    },
                    aria: {
                        sortAscending: ": activar para ordenar la columna de forma ascendente",
                        sortDescending: ": activar para ordenar la columna de forma descendente"
                    }
                },
                columns: [
                    {data: 'invnum', render: function (data, type, row) {
                            return data ? data:"";
                        }
                    },
                    {data: 'avance', render: function (data, type, row) {
                            return data ? data.toFixed(2) + "%" : "0%";
                        }
                    },
                    {data: 'conform', render: function (data, type, row) {
                            return data ? data + " / " + row.total : "0 / " + row.total;
                        }
                    }
                ],
                createdRow: function (row, data, dataIndex) {
                    if (data.avance === 100) {
                        $(row).addClass('table-success');
                    }
                }
            });

            for (let item in data.data) {
                // Determinar el color de fondo y borde basado en el avance
                let backgroundColor, borderColor;
                if (com === "S") {
                    // Colores en modo disabled
                    if (data.data[item].avance === 0) {
                        backgroundColor = "#f1948a"; // Rojo deshabilitado (más claro)
                        borderColor = "#e6b0aa"; // Rojo más claro para el borde
                    } else if (data.data[item].avance === 100) {
                        backgroundColor = "#a9dfbf"; // Verde deshabilitado (más claro)
                        borderColor = "#82e0aa"; // Verde más claro para el borde
                    } else {
                        backgroundColor = "#aed6f1"; // Azul deshabilitado (más claro)
                        borderColor = "#85c1e9"; // Azul más claro para el borde
                    }
                } else {
                    if (data.data[item].avance === 0) {
                        backgroundColor = "#e74c3c"; // Rojo
                        borderColor = "#c0392b"; // Rojo más fuerte
                    } else if (data.data[item].avance === 100) {
                        backgroundColor = "#2ecc71"; // Verde
                        borderColor = "#27ae60"; // Verde más fuerte
                    } else {
                        backgroundColor = "#f7dc6f"; // Azul
                        borderColor = "#f7dc6f"; // Azul más fuerte
                    }
                }
                // Contenedor del botón
                let nuevoContenedor = $("<button>")
                        .addClass("d-flex flex-column align-items-center justify-content-center p-3 mb-3 btn-detalle")
                        .css({
                            "border": `2px solid ${borderColor}`, // Borde con color más fuerte
                            "border-radius": "12px",
                            "width": "100%", // Ancho adaptado al contenedor
                            "min-height": "120px",
                            "text-align": "center",
                            "background-color": backgroundColor,
                            "color": "#fff",
                            "font-weight": "bold",
                            "box-shadow": "0 4px 6px rgba(0, 0, 0, 0.1)",
                            "transition": "transform 0.2s, box-shadow 0.2s",
                            "cursor": "pointer",
                            "overflow": "hidden"
                        })
                        .hover(
                                function () {
                                    $(this).css({
                                        "transform": "scale(1.05)",
                                        "box-shadow": "0 8px 12px rgba(0, 0, 0, 0.2)"
                                    });
                                },
                                function () {
                                    $(this).css({
                                        "transform": "scale(1)",
                                        "box-shadow": "0 4px 6px rgba(0, 0, 0, 0.1)"
                                    });
                                }
                        ).data("caja", data.data[item].caja);

                if (com === "S") {
                    nuevoContenedor.prop('disabled', true);
                }
                // Texto de la caja
                let nuevoTextoCaja = $("<div>")
                        .text(data.data[item].caja)
                        .css({
                            "font-size": "1.5rem",
                            "margin-bottom": "10px"
                        });

                // Cantidad y porcentaje

                let nuevoTextoCantidad = $("<div>")
                        .text(data.data[item].cantidad + " - " + Math.round(data.data[item].avance) + "%")
                        .css({
                            "font-size": "1.2rem",
                            "width": "100%",
                            "display": "flex",
                            "justify-content": "space-between",
                            "margin-top": "auto",
                            "padding-top": "10px",
                            "border-top": "1px solid rgba(255, 255, 255, 0.2)"
                        });

                nuevoContenedor.append(nuevoTextoCaja).append(nuevoTextoCantidad);
                $("#lista").append(nuevoContenedor);

            }
            if (data.data.length === 0) {
                $("#lista").text("NO SE ENCONTRO DATOS EN ESTA ORDEN");
            }
        } else {
            if (data.mensaje === "nosession") {
                $.fn.validarSession();
            } else {
                alert(data.mensaje);
            }
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        alert("error, vuelve a recargar la pagina");
    });
    $("#lista").on("click", ".btn-detalle", function () {
        console.log("codigo:" + $(this).data("caja"));
        localStorage.setItem('caja_caja', $(this).data("caja"));
        localStorage.setItem('scroll', 0);
        $("#contenido").load('recepcioncajasdetalle.html');
    });
    $("#back-button").click(function () {
        $("#contenido").load('recepcioncajas.html');
    });
    $("#grabar").click(function () {
        if (confirm("Estas seguro que quieres cerrar la OT")) {
            let sec = localStorage.getItem('caja_orden');
            $.getJSON("picking", {ord: sec, opcion: 46, chk: "S"}, function (data) {//ant 22, nuev 46
                if (data.resultado === "ok") {
                    alert("Recepcion cerrada correctamente");
                    let url = 'recepcioncajascaja.html';
                    $("#contenido").load(url);
                } else {
                    if (data.mensaje === "nosession") {
                        $.fn.validarSession();
                    } else {
                        alert(data.mensaje);
                    }
                }
            });
        }
    });
    $("#reporte").click(function () {
        $.fn.validarSession();
        let sec = localStorage.getItem('caja_orden');

        let url = 'reportefaltante_excedente?tipo=pdf&ordenini=' + sec + "&ordenfin=" + sec+'&fechafin=&fechainicio=';

        window.open(url, "_blank");
    });
    $("#reporte-excel").click(function () {
        $.fn.validarSession();
        let sec = localStorage.getItem('caja_orden');

        let url = 'reportefaltante_excedente?tipo=excel&ordenini=' + sec + "&ordenfin=" + sec+'&fechafin=&fechainicio=';
        window.open(url, "_blank");
    });
});