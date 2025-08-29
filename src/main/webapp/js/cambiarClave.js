$(document).ready(function () {
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
    $('#btnCambiarClave').on('click', function () {
        var claveActual = $('#txtClaveActual').val();
        var claveNueva = $('#txtClaveNueva').val();
        var claveConfirma = $('#txtClaveConfirma').val();

        if (claveNueva !== claveConfirma) {
            mostrarMensaje('Las claves nuevas no coinciden.', 'danger');
            return;
        }

        if (claveNueva.length < 6) {
            mostrarMensaje('La nueva clave debe tener al menos 6 caracteres.', 'danger');
            return;
        }

        var claveActualHashed = CryptoJS.SHA256(claveActual).toString(CryptoJS.enc.Hex);
        var claveNuevaHashed = CryptoJS.SHA256(claveNueva).toString(CryptoJS.enc.Hex);
        $.getJSON("CRUDUsuarioBartolito", {opcion: 5, usepas: claveActualHashed, usepas2: claveNuevaHashed}, function (data) {
            if (data.resultado === 'ok') {
                $.getJSON("CerrarSesion", function (data) {
                    if (data.resultado === "ok") {
                        if (data.empr === undefined) {
                            $(location).attr('href', "index.html");
                        } else {
                            $(location).attr('href', "index.html?empr=" + data.empr);
                        }
                    } else {
                        alert("Error del servicio");
                    }

                }).fail(function (jqXHR, textStatus, errorThrown) {
                    alert("Error: No se logro hacer la conexion con el servicio");
                });
            } else if (data.mensaje === 'nocoinc') {
                mostrarMensaje('La clave actual no es correcta.', 'danger');
            } else if (data.mensaje === 'nosession') {
                $.fn.validarSession();
            } else {
                mostrarMensaje('Error al actualizar la clave.', 'danger');
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            mostrarMensaje('Error en la solicitud. Intente nuevamente.', 'danger');
        });
    });
    function mostrarMensaje(mensaje, tipo) {
        $('#resultado').html(`<div class="alert alert-${tipo}">${mensaje}</div>`);
        setTimeout(function () {
            $('#resultado').fadeOut('slow', function () {
                $(this).html('').fadeIn();
            });
        }, 3000); // 3 segundos antes de limpiar el mensaje
    }

});