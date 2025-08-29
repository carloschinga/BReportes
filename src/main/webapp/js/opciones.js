$(document).ready(function () {
    //VALIDAR LA SESION
    $.fn.validarSession = function () {
        $.getJSON("validarsesion", function (data) {
            if (data.resultado === "ok") {
                $("#lblUsuario").text(data.logi);
            } else {
                $(location).attr('href', "index.html");
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            $(location).attr('href', "index.html");
        });
    };
    $.fn.validarSession();
    $.getJSON("ParametrosBartolito?opcion=1", function (data) {
        if (data.resultado === "ok") {
            $("#dias").val(data.num);
        } else {
            if (data.mensaje === "nosession") {
                $.fn.validarSession();
            } else {
                alert("Error al cargar los datos");
            }
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        alert("Error con la conexion con del servidor");
    });

    $("#btndias").click(function () {
        let valor = $("#dias").val();
        if (valor !== undefined && valor !== "" && valor > 0 && valor % 1 === 0 && !isNaN(valor)) {
            $.getJSON("ParametrosBartolito?opcion=2&valor=" + valor, function (data) {
                if (data.resultado === "ok") {
                    alert("modificado correctamente");
                } else {
                    if (data.mensaje === "nosession") {
                        $.fn.validarSession();
                    } else {
                        alert("Error al modificar");
                    }
                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
                alert("Error con la conexion con del servidor");
            });
        } else {
            alert("ingrese un numero valido");
            $.getJSON("ParametrosBartolito?opcion=1", function (data) {
                if (data.resultado === "ok") {
                    $("#dias").val(data.num);
                } else {
                    if (data.mensaje === "nosession") {
                        $.fn.validarSession();
                    } else {
                        alert("Error al cargar los datos");
                    }
                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
                alert("Error con la conexion con del servidor");
            });

        }
    });
});