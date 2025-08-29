window.onscroll = function () {
    var backButton = document.getElementById('back-button');
    if (document.body.scrollTop > 50 || document.documentElement.scrollTop > 50) {
        backButton.style.top = '20px'; // Nueva altura cuando el usuario hace scroll
    } else {
        backButton.style.top = '100px'; // Altura inicial
    }
};
$(document).ready(function () {
    let central = "N";
    $.fn.validarSession = function () {
        $.getJSON("validarsesion", function (data) {
            if (data.resultado === "ok") {
                if (data.central === "S") {
                    central = "S";
                }
            } else {
                $(location).attr('href', "index.html");
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            $(location).attr('href', "index.html");
        });
    };
    $.fn.validarSession();
    let orden = localStorage.getItem('seg_orden');
    let siscod = localStorage.getItem('seg_siscod');
    let sisent = localStorage.getItem('seg_sisent');
    $("#nombre").text("Productos Observados - "+sisent+" - OT. N° "+orden);
    let tabla = $('#tabla').DataTable({
        ajax: {
            url: "picking",
            type: "GET",
            data: function (d) {
                d.opcion = 30;
                d.siscod = siscod;
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
            {data: 'invnum'},
            {data: 'codpro', render: function (data, type, row) {
                    return data ? data : "";
                }
            },
            {data: 'despro', render: function (data, type, row) {
                    return data ? data : "";
                }
            },
            {data: 'calculo'},
            {data: 'estobs', render: function (data, type, row) {
                    return `<select class="form-select central" 
                            data-codpro="${row.codpro}" data-calculo="${row.calculo}"
                            ${central === "S" ? "" : "disabled"}> 
                            <option value="P" ${data !== "R" ? "selected" : ""}>Pendiente</option> 
                            <option value="R" ${data === "R" ? "selected" : ""}>Regularizado</option> 
                            </select>`;
                }
            },
            {data: 'estobsbot', render: function (data, type, row) {
                    return `<select class="form-select botica" 
                            data-codpro="${row.codpro}"  data-calculo="${row.calculo}"
                            ${central === "S" ? "disabled" : ""}>
                            <option value="P" ${data !== "R" ? "selected" : ""}>Pendiente</option>
                            <option value="R" ${data === "R" ? "selected" : ""}>Regularizado</option>
                            </select>`;
                }
            }

        ]
    });
    $("#tabla").on("change", ".form-select", function () {

        let codpro = String($(this).data("codpro"));
        let calculo = String($(this).data("calculo"));
        let valor = $(this).val();
        $(".form-select").prop("disabled", true);
        if (codpro) {
            $.getJSON("picking", {opcion: 31, orden: orden, codpro: codpro, siscod: siscod, valor: valor, calculo: calculo}, function (data) {
                if (data.resultado === "ok") {
                    central === "S" ? $(".central").prop("disabled", false) : $(".botica").prop("disabled", false);
                } else {
                    if (data.mensaje === "nosession") {
                        $.fn.validarSession();
                    } else {
                        alert("Error: Problemas con el servidor.");
                        tabla.ajax.reload();
                    }
                }
            });
        } else {
            alert("ocurrio un error, vuelva a cargar");
                        tabla.ajax.reload();
        }
    });

    $("#back-button").click(function () {
        $("#contenido").load('seguimientoestablecimiento.html');
    });
});