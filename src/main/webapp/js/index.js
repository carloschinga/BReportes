let globalhost;
$(document).ready(function () {
    $.fn.mostrarMensaje = function (nombreid, mensaje) {
        $("#" + nombreid).text(mensaje);
        $("#" + nombreid).css("display", "block");
        setTimeout(function () {
            $("#" + nombreid).fadeOut(2000);
        }, 2000);
    };
    $.fn.setCookie = function (nombre, valor, diasExpiracion) {
        var fechaExpiracion = new Date();
        fechaExpiracion.setDate(fechaExpiracion.getDate() + diasExpiracion);
        //var valorCookie = encodeURIComponent(valor) + "; expires=" + fechaExpiracion.toUTCString() + ";path=/;secure;SameSite=Strict;HttpOnly";
        var valorCookie =
                encodeURIComponent(valor) +
                "; expires=" +
                fechaExpiracion.toUTCString() +
                ";";
        document.cookie = nombre + "=" + valorCookie;
    };
    $.fn.getCookie = function (nombre) {
        var name = nombre + "=";
        var cookies = document.cookie.split(";");
        for (var i = 0; i < cookies.length; i++) {
            var cookie = cookies[i];
            while (cookie.charAt(0) === " ") {
                cookie = cookie.substring(1);
            }
            if (cookie.indexOf(name) === 0) {
                return cookie.substring(name.length, cookie.length);
            }
        }
        return "";
    };

    $("#btnLogin").click(function () {
        $("#btnLogin").css("display", "none");
        $("#loading").css("display", "block");
        var queryString = window.location.search;

        // Eliminar el '?' del principio
        queryString = queryString.substring(1);

        // Dividir la cadena de consulta en pares clave=valor
        var queryParams = queryString.split("&");

        // Objeto para almacenar los parámetros
        var params = {};

        // Recorrer los pares clave=valor y almacenarlos en el objeto params
        queryParams.forEach(function (param) {
            var pair = param.split("=");
            var key = decodeURIComponent(pair[0]);
            var value = decodeURIComponent(pair[1] || "");
            params[key] = value;
        });
        let empr = $("#Empresa").val();
        if (params["empr"] === undefined) {
            empr = $("#Empresa").val();
        } else {
            empr = params["empr"];
        }
        let logi = $("#txtUsuario").val();
        let pass = CryptoJS.SHA256($("#txtClave").val()).toString(CryptoJS.enc.Hex);
        let pass2 = $("#txtClave").val();
        let tip = $("#tip").val();
        console.log(pass);
        if (!(logi.trim() === 0 || pass.trim().length === 0)) {
            let parametro = {
                logi: logi,
                pass: pass2,
                passc: pass,
                empr: empr,
                tip: tip,
            };
            $.getJSON("validar", parametro, function (data) {
                $.fn.setCookie("token", data.token, 7);
                if (data.resultado === "OK") {
                    if (tip === "b") {
                        switch (data.siscod) {
                            case 1:
                                $(location).attr(
                                        "href",
                                        "/BPickingCentral/principal.html?token=" + data.token
                                        );
                                break;
                            case 2:
                                $(location).attr(
                                        "href",
                                        "/BPickingAlejandrina/principal.html?token=" + data.token
                                        );
                                break;
                            case 3:
                                $(location).attr(
                                        "href",
                                        "/BPickingMassalud/principal.html?token=" + data.token
                                        );
                                break;
                            case 4:
                                $(location).attr(
                                        "href",
                                        "/BPicking7Sabores/principal.html?token=" + data.token
                                        );
                                break;
                            case 5:
                                $(location).attr(
                                        "href",
                                        "/BPickingNino/principal.html?token=" + data.token
                                        );
                                break;
                            case 6:
                                $(location).attr(
                                        "href",
                                        "/BPickingMerced/principal.html?token=" + data.token
                                        );
                                break;
                            case 7:
                                $(location).attr(
                                        "href",
                                        "/BPickingSanMartin/principal.html?token=" + data.token
                                        );
                                break;
                            case 8:
                                $(location).attr(
                                        "href",
                                        "/BPickingAcevedo/principal.html?token=" + data.token
                                        );
                                break;
                            case 9:
                                $(location).attr(
                                        "href",
                                        "/BPicking28Julio/principal.html?token=" + data.token
                                        );
                                break;
                            case 10:
                                $(location).attr(
                                        "href",
                                        "/BPickingMuniSalud/principal.html?token=" + data.token
                                        );
                                break;
                            case 11:
                                $(location).attr(
                                        "href",
                                        "/BPickingRockys/principal.html?token=" + data.token
                                        );
                                break;
                            case 12:
                                $(location).attr(
                                        "href",
                                        "/BPickingGoya/principal.html?token=" + data.token
                                        );
                                break;
                        }
                    } else if (
                            data.grucod === "VENDEL" ||
                            data.grucod === "VENPIN" ||
                            data.grucod === "VENTAS" ||
                            data.grucod === "VEPIAN"
                            )
                    {
                        switch (data.siscod) {
                            case 1:
                                $(location).attr(
                                        "href",
                                        "/BFarmaciasCentral/principal.html?token=" + data.token
                                        );
                                break;
                            case 2:
                                $(location).attr(
                                        "href",
                                        "/BFarmaciasAlejandrina/principal.html?token=" + data.token
                                        );
                                break;
                            case 3:
                                $(location).attr(
                                        "href",
                                        "/BFarmaciasMassalud/principal.html?token=" + data.token
                                        );
                                break;
                            case 4:
                                $(location).attr(
                                        "href",
                                        "/BFarmacias7Sabores/principal.html?token=" + data.token
                                        );
                                break;
                            case 5:
                                $(location).attr(
                                        "href",
                                        "/BFarmaciasNino/principal.html?token=" + data.token
                                        );
                                break;
                            case 6:
                                $(location).attr(
                                        "href",
                                        "/BFarmaciasMerced/principal.html?token=" + data.token
                                        );
                                break;
                            case 7:
                                $(location).attr(
                                        "href",
                                        "/BFarmaciasSanMartin/principal.html?token=" + data.token
                                        );
                                break;
                            case 8:
                                $(location).attr(
                                        "href",
                                        "/BFarmaciasAcevedo/principal.html?token=" + data.token
                                        );
                                break;
                            case 9:
                                $(location).attr(
                                        "href",
                                        "/BFarmacias28Julio/principal.html?token=" + data.token
                                        );
                                break;
                            case 10:
                                $(location).attr(
                                        "href",
                                        "/BFarmaciasMuniSalud/principal.html?token=" + data.token
                                        );
                                break;
                            case 11:
                                $(location).attr(
                                        "href",
                                        "/BFarmaciasRockys/principal.html?token=" + data.token
                                        );
                                break;
                            case 12:
                                $(location).attr(
                                        "href",
                                        "/BFarmaciasGoya/principal.html?token=" + data.token
                                        );
                                break;
                        }
                    } else if (tip === "i") {
                        $(location).attr(
                                "href",
                                "/BInventario/principal.html?token=" + data.token
                                );

                    } else {
                        $(location).attr('href', "principal.html");
                    }
                } else {
                    $("#btnLogin").css("display", "block");
                    $("#loading").css("display", "none");
                    $.fn.mostrarMensaje("alerta", data.mensaje);
                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
                $("#btnLogin").css("display", "block");
                $("#loading").css("display", "none");
                $.fn.mostrarMensaje(
                        "alerta",
                        "Los servicios REST no estan disponibles"
                        );
            });
        } else {
            $("#btnLogin").css("display", "block");
            $("#loading").css("display", "none");
            mostrarMensaje(
                    "alerta",
                    "El usuario y/o contraseña debe contener valores"
                    );
        }
    });
    $("#txtClave").keypress(function (e) {
        if (e.which === 13) {
            //e.preventDefault();
            $("#btnLogin").trigger("click");
        }
    });
});

/*function setCookie(nombre, valor, diasExpiracion) {
 var fechaExpiracion = new Date();
 fechaExpiracion.setDate(fechaExpiracion.getDate() + diasExpiracion);
 //var valorCookie = encodeURIComponent(valor) + "; expires=" + fechaExpiracion.toUTCString() + ";path=/;secure;SameSite=Strict;HttpOnly";
 var valorCookie = encodeURIComponent(valor) + "; expires=" + fechaExpiracion.toUTCString() + ";";
 document.cookie = nombre + "=" + valorCookie;
 }*/
/*function getCookie(nombre) {
 // Obtener todas las cookies presentes en el documento
 var cookies = document.cookie.split(';');
 
 // Buscar la cookie por nombre
 for (var i = 0; i < cookies.length; i++) {
 var cookie = cookies[i].trim();
 // Verificar si la cookie comienza con el nombre buscado
 if (cookie.indexOf(nombre + '=') === 0) {
 // Devolver el valor de la cookie
 return decodeURIComponent(cookie.substring(nombre.length + 1));
 }
 }
 
 // Si no se encuentra la cookie, devolver null
 return null;
 }*/
