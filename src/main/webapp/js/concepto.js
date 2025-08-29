let table, codiUsua, ip, hostname;
$(document).ready(function () {
    //FUNCIONES
    $.fn.getCookie = function (nombre) {
        var name = nombre + "=";
        var cookies = document.cookie.split(';');
        for (var i = 0; i < cookies.length; i++) {
            var cookie = cookies[i];
            while (cookie.charAt(0) === ' ') {
                cookie = cookie.substring(1);
            }
            if (cookie.indexOf(name) === 0) {
                return cookie.substring(name.length, cookie.length);
            }
        }
        return "";
    };
    $.fn.mostrarMensaje = function (nombreid, mensaje) {
        $("#" + nombreid).text(mensaje);
        $("#" + nombreid).css("display", "block");
        setTimeout(function () {
            $("#" + nombreid).fadeOut(2000);
        }, 2000);
    };

    globalhost = decodeURIComponent($.fn.getCookie("globalhost"));
    $.getJSON(globalhost + "/Matricula/obteneripyhost", function (data) {
        ip = data.ipAddress;
        hostname = data.hostName;
    }).fail(function (error) {
        ip = 'iperror';
        hostname = 'hosterror';
    });

    $.getJSON("validarsesion", function (data) {
        codiUsua = data.codi;
        if (data.resultado === "ok") {
            //CARGA DE INTERFACES
            $('#topbar').load('topbar.html');
            //$('#sidebar').load('sidebar.html');
            $('#footer').load('footer.html');
            table = $('#example').DataTable({
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
                paging: false,
                ajax: {
                    url: decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/mostrarviewconceptos/procesar",
                    type: 'GET',
                    data: function (d) {
                        d.token = $.fn.getCookie("token");
                    }
                },
                columns: [
                    {data: 'codiConc'},
                    {data: 'nombConc'},
                    {data: 'nombmes', render: function (data, type, row, meta) {
                            if (data === undefined)
                                return "";
                            else
                                return data;
                        }},
                    {data: 'ordeConc'},
                    {data: 'nombTipoConc'},
                    {data: 'matrTipoConc',
                        render: function (data, type, row, meta) {
                            if (data)
                                return "<input type=checkbox checked disabled/>";
                            else
                                return "<input type=checkbox disabled/>";
                        }
                    },
                    {data: 'codiConc',
                        render: function (data, type, row, meta) {
                            let nombre = row.nombConc;
                            return "<button class='btn btn-primary btn-user btnSeleccionarConcepto'  data-json='{\"codiConc\":\"" + row.codiConc + "\",\"nombConc\":\"" + encodeURIComponent(nombre) + "\",\"codiMes\":\"" + row.codiMes + "\",\"actiConc\":\"" + row.actiConc + "\",\"ordeConc\":\"" + row.ordeConc + "\",\"codiTipoConc\":\"" + row.codiTipoConc + "\"}'>Modificar</button>";
                        }}
                ]
            });

            //CONTINUA CARGAR COMBOS
            let url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudmes/listar";
            parametro = {token: $.fn.getCookie("token")};
            $.getJSON(url, parametro, function (data) {
                for (const element of data.data) {
                    $("#cmbMes").append("<option value=" + element.codiMes + " selected>" + element.nombMes + "</option>");
                }
            });

            //CONTINUA CARGAR COMBOS
            url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudtipoconcepto/listar";
            parametro = {token: $.fn.getCookie("token")};
            $.getJSON(url, parametro, function (data) {
                for (const element of data.data) {
                    $("#cmbTipoConc").append("<option value=" + element.codiTipoConc + " selected>" + element.nombTipoConc + "</option>");
                }
            });
        } else {
            $(location).attr('href', "index.html");
        }


    });
    //PROGRAMACIÓN DE EVENTOS
    //MOSTRAR DIALOGOS
    $("#btnNuevo").click(function () {
        $("#txtAccion").val("1");
        $("#modalTituloConcepto").text("Agregar Concepto");
        //$("#txtAnio").val($("#cmbMes option:selected").text());
        $("#txtCodigo").val("AUTOMATICO");
        $("#txtNombre").val("");
        $("#txtOrden").val("");

        $("#modalModificar").modal("show");
    });
    $(document).on('click', '.btnSeleccionarConcepto', function () {
        var obj = $(this).data("json");
        $("#txtAccion").val("0");
        $("#txtCodigo").val(obj.codiConc);
        $("#txtNombre").val(decodeURIComponent(obj.nombConc));
        $("#cmbMes").val(obj.codiMes);
        $("#cmbTipoConc").val(obj.codiTipoConc);
        $("#miCheckbox").prop('checked', obj.actiConc === true);
        $("#txtOrden").val(obj.ordeConc);
        $("#modalTituloConcepto").val("Modificar Concepto");
        $("#modalModificar").modal("show");

    });

    //GRABAR OPERACIONES
    $("#btnAceptarCambios").click(function () {
        let codiConc = $("#txtCodigo").val();
        let codiMes = $("#cmbMes").val();
        let codiTipoConc = $("#cmbTipoConc").val();
        let nombConc = $("#txtNombre").val();
        let ordeConc = $("#txtOrden").val();
        if ($("#txtAccion").val() === "1") {
            let url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudconcepto/agregar";
            let parametro;
            if ($("#miCheckbox").is(":checked")) {
                parametro = {token: $.fn.getCookie("token"), nombConc: nombConc, actiAula: true, ordeConc: ordeConc, codiTipoConc: codiTipoConc};
            } else {
                parametro = {token: $.fn.getCookie("token"), codiMes: codiMes, nombConc: nombConc, actiAula: true, ordeConc: ordeConc, codiTipoConc: codiTipoConc};
            }
            $.getJSON(url, parametro, function (data) {
                if (data.resultado === "ok") {
                    table.ajax.reload();
                    $("#modalModificar").modal("hide");
                } else {
                    $.fn.mostrarMensaje("alertModificar", data.mensaje);
                }
            });
        } else {
            let url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudconcepto/modificar";
            let parametro;
            if ($("#miCheckbox").is(":checked")) {
                parametro = {token: $.fn.getCookie("token"), codiConc: codiConc, codiConc: codiConc, nombConc: nombConc, actiAula: true, ordeConc: ordeConc, codiTipoConc: codiTipoConc};
            } else {
                parametro = {token: $.fn.getCookie("token"), codiConc: codiConc, codiConc: codiConc, codiMes: codiMes, nombConc: nombConc, actiAula: true, ordeConc: ordeConc, codiTipoConc: codiTipoConc};
            }
            $.getJSON(url, parametro, function (data) {
                if (data.resultado === "ok") {
                    table.ajax.reload();
                    $("#modalModificar").modal("hide");
                } else {
                    $.fn.mostrarMensaje("alertModificar", data.mensaje);
                }
            });
        }
    });

    //MOSTRAR MENSAJES
    /*let mostrarMensaje = function (nombreid, mensaje) {
     $("#" + nombreid).text(mensaje);
     $("#" + nombreid).css("display", "block");
     setTimeout(function () {
     $("#" + nombreid).fadeOut(2000);
     }, 2000);
     };*/

    //OTROS EVENTOS
    $("#miCheckbox").change(function () {
        // Verifica si el checkbox está marcado
        if ($("#miCheckbox").is(":checked")) {
            // Si está marcado, muestra un mensaje
            $("#cmbMes").hide();
        } else {
            // Si no está marcado, muestra otro mensaje
            $("#cmbMes").show();
        }
    });
    $("#salir").click(function () {
        $.getJSON("cerrarsesion", function (data) {});
        $(location).attr('href', "index.html");
    });
});
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
 */
        