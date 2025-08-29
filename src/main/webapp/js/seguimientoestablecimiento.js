window.onscroll = function () {
    var backButton = document.getElementById('back-button');
    if (document.body.scrollTop > 50 || document.documentElement.scrollTop > 50) {
        backButton.style.top = '20px'; // Nueva altura cuando el usuario hace scroll
    } else {
        backButton.style.top = '100px'; // Altura inicial
    }
};
$(document).ready(function () {
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
    let pik_fecini = localStorage.getItem('seg_fecini');
    let pik_fecfin = localStorage.getItem('seg_fecfin');
    let orden = localStorage.getItem('seg_sec');
    tabla = $('#tabla').DataTable({
        ajax: {
            url: "picking",
            type: "GET",
            data: function (d) {
                d.opcion = 29;
                d.fecini = pik_fecini;
                d.fecfin = pik_fecfin;
                d.orden = orden;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                if (textStatus === "abort") {
                    // Ignorar el error si es de tipo "abort"
                    console.log("Solicitud abortada, no se muestra alerta.");
                } else {
                    console.log("Error en la solicitud: " + textStatus, errorThrown);
                    alert("Hubo un problema al cargar los datos. Por favor, intenta de nuevo.");
                }
            }
        },
        searching: false,
        paging: false,
        info: false,
        columns: [
            {data: 'fecha', render: function (data, type, row) {
                    return data ? data.split(" ")[0] : "";
                }
            },
            {data: 'hora', render: function (data, type, row) {
                    return data ? data.split(" ")[0] : "";
                }
            },
            {data: 'sisent', render: function (data, type, row) {
                    return data ? data.split(" ").slice(1).join(" ") : "";
                }
            },
            {data: 'orden'},
            {data: 'conforme', render: function (data, type, row) {
                    return row.estatus !== "S" ? "-" : (data === "S" ? "Conforme" : (row.regul === "S" ? "<span style='color:blue'>Regularizado</span>" : "<span style='color:red'>Observado</span>"));
                }
            },
            {data: 'estatus', render: function (data, type, row) {
                    return data === "S" ? "Entrega Completa" : "Transito";
                }
            },
            {data: null, render: function (data, type, row) {
                    return `<button class="btn btn-sm ${row.conforme === "S" ? "btn-primary" : "btn-danger"}"
                            data-orden=${row.orden} data-siscod=${row.siscod} data-sisent="${row.sisent}"><i class="fas fa-edit"></i></button>`;
                }
            }

        ],
        "order": [[0, "asc"]]
    });

    $("#tabla").on("click", ".btn", function () {
        let orden = $(this).data("orden");
        let siscod = $(this).data("siscod");
        let sisent = $(this).data("sisent");
        console.log(orden + " - " + siscod);
        if (orden && siscod) {
            localStorage.setItem('seg_orden', orden);
            localStorage.setItem('seg_siscod', siscod);
            localStorage.setItem('seg_sisent', sisent);

            let url = 'seguimientodetalle.html';
            $("#contenido").load(url);
        } else {
            alert("ocurrio un error, vuelva a cargar");
        }
    });

    $("#back-button").click(function () {
        $("#contenido").load('seguimientoentrega.html');
    });
});