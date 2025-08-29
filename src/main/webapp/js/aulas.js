let table, codiUsua, ip, hostname;
$(document).ready(function () {
    //FUNCIONES
    $.fn.seleccionarCliente = function (obj) {
        let url = "http://" + globalhost + "/matriculaservicio/webresources/mostrarviewgrados/procesar";
        let parametro = {token: $.fn.getCookie("token")};
        $("#txtAccion").val("0");
        $("#txtCodigo").val(obj.codiAula);
        $("#txtAnio").val($("#cmbAnio option:selected").text());
        $("#cmbGrado").val(obj.codiGrad);
        $("#cmbSeccion").val(obj.codiSecc);
        $("#txtAforo").val(obj.numeAula);
        $("#modalTituloAula").val("Modificar Aula");
        $("#modalModificar").modal("show");



    };
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
            // $('#sidebar').load('sidebar.html');
            $('#footer').load('footer.html');
            $('#topbar').load('topbar.html');

           

            //CONFIGUAR LA TABLA
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
                    url: decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/mostrarviewaulas/procesar",
                    type: 'GET',
                    data: function (d) {
                        d.token = $.fn.getCookie("token");
                        d.codiAnio = $("#cmbAnio").val();
                    }
                },
                columns: [
                    {data: 'codiAula'},
                    {data: 'nombAula'},
                    {data: 'nombSede'},
                    {data: 'numeAula'},
                    {data: 'codiAula',
                        render: function (data, type, row, meta) {
                            let objJson = {codiAula: row.codiAula, codiAnio: row.codiAnio, nombAnio: row.nombAnio, codiGrad: row.codiGrad, codiSecc: row.codiSecc, numeAula: row.numeAula};
                            return "<button onclick=$.fn.seleccionarCliente(" + JSON.stringify(objJson) + ") class=\"btn btn-primary btn-user \">Modificar</button>";
                        }
                    }
                ]
            });



            //CONTINUA CARGAR COMBOS
            let url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudanio/listar";
            parametro = {token: $.fn.getCookie("token")};
            $.getJSON(url, parametro, function (data) {
                let codiAnio;
                for (const element of data.data) {
                    $("#cmbAnio").append("<option value=" + element.codiAnio + " selected>" + element.nombAnio + "</option>");
                    codiAnio = element.codiAnio;
                }
               
                table.ajax.reload();

                url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudsede/listar";
                parametro = {token: $.fn.getCookie("token")};
                $.getJSON(url, parametro, function (data) {
                    for (const element of data.data) {
                        $("#cmbSede").append("<option value=" + element.codiSede + " selected>" + element.nombSede + "</option>");
                    }
                    url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/mostrarviewgrados/procesar";
                    let parametro = {token: $.fn.getCookie("token")};
                    $.getJSON(url, parametro, function (data) {
                        for (const element of data.data) {
                            $("#cmbGrado").append("<option value=" + element.codiGrad + " selected>" + element.nombGrado + "</option>");
                        }
                        url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudseccion/listar";
                        parametro = {token: $.fn.getCookie("token")};
                        $.getJSON(url, parametro, function (data) {
                            for (const element of data.data) {
                                $("#cmbSeccion").append("<option value=" + element.codiSecc + " selected>" + element.nombSecc + "</option>");
                            }
                        });
                    });
                });
            });

        } else {
            $(location).attr('href', "index.html");
        }


    });

    //PROGRAMACION DE EVENTOS
    $('#cmbAnio').on('change', function () {
        table.ajax.reload();
    });
    $("#btnNuevo").click(function () {
        $("#txtAccion").val("1");
        $("#modalTituloAula").text("Agregar Aula");
        $("#txtAnio").val($("#cmbAnio option:selected").text());
        $("#txtCodigo").val("AUTOMATICO");
        $("#txtAforo").val("30");
        $("#modalModificar").modal("show");
    });
    $("#salir").click(function () {
        $.getJSON("cerrarsesion", function (data) {});
        $(location).attr('href', "index.html");
    });
    $("#btnAceptarCambios").click(function () {
        let codiAula = $("#txtCodigo").val();
        let codiAnio = $("#cmbAnio").val();
        let codiSede = $("#cmbSede").val();
        let codiGrad = $("#cmbGrado").val();
        let codiSecc = $("#cmbSeccion").val();
        let numeAula = $("#txtAforo").val();
        if ($("#txtAccion").val() === "1") {
            let url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudaula/agregar";
            let parametro = {token: getCookie("token"), codiSede: codiSede, codiGrad: codiGrad, codiSecc: codiSecc, codiAnio: codiAnio, numeAula: numeAula, codiUsuaAlta: codiUsua, actiAula: true, ipRegiAlta: ip, hostRegiAlta: hostname};
            $.getJSON(url, parametro, function (data) {
                if (data.resultado === "ok") {
                    table.ajax.reload();
                    $("#modalModificar").modal("hide");
                } else {
                    mostrarMensaje("alertModificar", data.mensaje);
                }
            });
        } else {
            let url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudaula/modificar";
            let parametro = {token: $.fn.getCookie("token"), codiAula: codiAula, codiSede: codiSede, codiGrad: codiGrad, codiSecc: codiSecc, codiAnio: codiAnio, numeAula: numeAula, codiUsuaModi: codiUsua, actiAula: true, ipRegiModi: ip, hostRegiModi: hostname};
            $.getJSON(url, parametro, function (data) {
                if (data.resultado === "ok") {
                    table.ajax.reload();
                    $("#modalModificar").modal("hide");
                } else {
                    mostrarMensaje("alertModificar", data.mensaje);
                }
            });
        }
    });


});

        