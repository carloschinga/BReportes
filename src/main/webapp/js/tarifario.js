
let table, codiUsua, ip, hostname;
$(document).ready(function () {
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
    $.fn.seleccionarCliente = function (obj) {
        let url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/mostrarviewgrados/procesar";
        let parametro = {token: $.fn.getCookie("token")};
        $("#txtAccion").val("0");
        $("#txtCodigo").val(obj.codiTari);
        $("#txtAnio").val($("#cmbAnio option:selected").text());
        $("#cmbGrado").val(obj.codiGrad);
        $("#cmbTipoConc").val(obj.codiTipoConc);
        $("#txtMonto").val(obj.montTari);
        $("#cmbEstaTari").val('' + obj.actiTari + '');
        $("#modalTituloAula").val("Modificar Aula");
        $("#modalModificar").modal("show");
    };

    /*$.getJSON("http://" + globalhost + "/Matricula/obteneripyhost", function (data) {
     ip = data.ipAddress;
     hostname = data.hostName;
     }).fail(function (error) {
     ip = 'iperror';
     hostname = 'hosterror';
     });*/

    globalhost = decodeURIComponent($.fn.getCookie("globalhost"));
    console.log("======== global host ==========");
    console.log(globalhost);
    $.getJSON(globalhost + "/Matricula/obteneripyhost", function (data) {
        ip = data.ipAddress;
        hostname = data.hostName;
    }).fail(function (error) {
        ip = 'iperror';
        hostname = 'hosterror';
        console.log(ip);
        console.log(hostname);
    });



    $.getJSON("validarsesion", function (data) {
        codiUsua = data.codi;
        if (data.resultado === "ok") {
            //CARGA DE INTERFACES
            $('#topbar').load('topbar.html');
            $('#sidebar').load('sidebar.html');
            $('#footer').load('footer.html');


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
                    url: decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/mostrarviewtarifas/procesar",
                    type: 'GET',
                    data: function (d) {
                        d.token = $.fn.getCookie("token");
                        d.codiAnio = $("#cmbAnio").val();
                    }
                },
                columns: [
                    {data: 'codiTari'},
                    {data: 'nombSede'},
                    {data: 'nombGrad'},
                    {data: 'nombTipoConc'},
                    {data: 'montTari'},
                    {data: 'actiTari'},
                    {data: 'codiTari', render: function (data, type, row, meta) {
                            let objJson = {codiTari: row.codiTari, codiSede: row.codiSede, codiGrad: row.codiGrad, codiTipoConc: row.codiTipoConc, montTari: row.montTari, actiTari: row.actiTari};
                            return "<button onclick=$.fn.seleccionarCliente(" + JSON.stringify(objJson) + ") class=\"btn btn-primary btn-user \">Modificar</button>";
                        }}
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
            });

            url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/mostrarviewgrados/procesar";
            parametro = {token: $.fn.getCookie("token")};
            $.getJSON(url, parametro, function (data) {
                //let codiAnio;
                for (const element of data.data) {
                    $("#cmbGrado").append("<option value=" + element.codiGrad + " selected>" + element.nombGrado + "</option>");
                }
            });
            url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudsede/listar";
            parametro = {token: $.fn.getCookie("token")};
            $.getJSON(url, parametro, function (data) {
                //let codiAnio;
                for (const element of data.data) {
                    $("#cmbSede").append("<option value=" + element.codiSede + " selected>" + element.nombSede + "</option>");
                }
            });
            url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudtipoconcepto/listar";
            parametro = {token: $.fn.getCookie("token")};
            $.getJSON(url, parametro, function (data) {
                //let codiAnio;
                for (const element of data.data) {
                    $("#cmbTipoConc").append("<option value=" + element.codiTipoConc + " selected>" + element.nombTipoConc + "</option>");
                }
            });

            //let codiAnio;
            var estaTarifa = [];
            estaTarifa.push("true");
            estaTarifa.push("false");
            $.each(estaTarifa, function (index, element) {
                $("#cmbEstaTari").append("<option value=" + element + " selected>" + element + "</option>");
            });
            /*for (const element of estaTarifa) {
             $("#cmbTipoConc").append("<option value=" + element.codiTipoConc + " selected>" + element.nombTipoConc + "</option>");
             console.log(element);
             }
             for (var i = 0, max = 10; i < max; i++) {
             $("#cmbEstaTari").append("<option value=" + element.codiTipoConc + " selected>" + element.nombTipoConc + "</option>");
             
             }*/

        } else {
            $(location).attr('href', "index.html");
        }


    });


    $('#cmbAnio').on('change', function () {
        //table.ajax.reload();

        let url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/mostrarviewaulas/procesar";
        parametro = {token: $.fn.getCookie("token"), codiAnio: $('#cmbAnio').val()};
        $.getJSON(url, parametro, function (data) {
            for (const element of data.data) {
                $("#cmbAula").append("<option value=" + element.codiAula + " selected>" + element.nombAula + "</option>");
            }
        });
    });
    $("#btnNuevo").click(function () {
        $("#txtAccion").val("1");
        $("#modalTituloAula").text("Agregar Aula");
        $("#txtAnio").val($("#cmbAnio option:selected").text());
        $("#txtCodigo").val("AUTOMATICO");
        $("#txtMonto").val("0");
        $("#modalModificar").modal("show");
    });


    //CERRAR SESIÓN   
    $("#salir").click(function () {
        $.getJSON("cerrarsesion", function (data) {});
        $(location).attr('href', "index.html");
    });


    $("#btnAceptarCambios").click(function () {
        let codiTari = $("#txtCodigo").val();
        let codiAnio = $("#cmbAnio").val();
        let codiSede = $("#cmbSede").val();
        let codiGrad = $("#cmbGrado").val();
        let codiTipoConc = $("#cmbTipoConc").val();
        let montTari = $("#txtMonto").val();
        let actiTari = $("#cmbEstaTari").val();
        console.log("esto es el estado: " + actiTari);
        if ($("#txtAccion").val() === "1") {
            let url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudtarifa/agregar";
            let parametro = {token: $.fn.getCookie("token"),
                codiSede: codiSede, codiGrad: codiGrad, codiTipoConc: codiTipoConc, codiAnio: codiAnio, montTari: montTari, codiUsuaAlta: codiUsua, actiTari: actiTari, ipRegiAlta: ip, hostRegiAlta: hostname};
            $.getJSON(url, parametro, function (data) {
                if (data.resultado === "ok") {
                    table.ajax.reload();
                    $("#modalModificar").modal("hide");
                } else {
                    mostrarMensaje("alertModificar", data.mensaje);
                }
            });
        } else {
            console.log("entro en el else");
            let url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudtarifa/modificar";
            let parametro = {token: $.fn.getCookie("token"),
                codiTari: codiTari, codiSede: codiSede, codiGrad: codiGrad, codiTipoConc: codiTipoConc, codiAnio: codiAnio, montTari: montTari, codiUsuaModi: codiUsua, actiTari: actiTari, ipRegiModi: ip, hostRegiModi: hostname};
            $.getJSON(url, parametro, function (data) {
                console.log(data);
                if (data.resultado === "ok") {
                    table.ajax.reload();
                    $("#modalModificar").modal("hide");
                } else {
                    mostrarMensaje("alertModificar", data.mensaje);
                }
            });
        }
    });
    let mostrarMensaje = function (nombreid, mensaje) {
        $("#" + nombreid).text(mensaje);
        $("#" + nombreid).css("display", "block");
        setTimeout(function () {
            $("#" + nombreid).fadeOut(2000);
        }, 2000);
    };

});

/*
 function seleccionarCliente(obj) {
 let url = "http://" + globalhost + "/matriculaservicio/webresources/mostrarviewgrados/procesar";
 let parametro = {token: getCookie("token")};
 $("#txtAccion").val("0");
 $("#txtCodigo").val(obj.codiTari);
 $("#txtAnio").val($("#cmbAnio option:selected").text());
 $("#cmbGrado").val(obj.codiGrad);
 $("#cmbTipoConc").val(obj.codiTipoConc);
 $("#txtMonto").val(obj.montTari);
 $("#modalTituloAula").val("Modificar Aula");
 $("#modalModificar").modal("show");
 
 
 
 }
 
 
 
 
 function getCookie(nombre) {
 var nombreCookie = nombre + "=";
 var cookies = document.cookie.split(';');
 for (var i = 0; i < cookies.length; i++) {
 var cookie = cookies[i].trim();
 if (cookie.indexOf(nombreCookie) === 0) {
 return decodeURIComponent(cookie.substring(nombreCookie.length, cookie.length));
 }
 }
 return null;
 }*/



/*$("#btnAceptarCambios").click(function () {
 let codiTari = $("#txtCodigo").val();
 let codiAnio = $("#cmbAnio").val();
 let codiSede = $("#cmbSede").val();
 let codiGrad = $("#cmbGrado").val();
 let codiTipoConc = $("#cmbTipoConc").val();
 let montTari = $("#txtMonto").val();
 let actiAula = $("#cmbEstaTari").val();
 if ($("#txtAccion").val() === "1") {
 let url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudtarifa/agregar";
 let parametro = {token: $.fn.getCookie("token"),
 codiSede: codiSede, codiGrad: codiGrad, codiTipoConc: codiTipoConc, codiAnio: codiAnio, montTari: montTari, codiUsuaAlta: codiUsua, actiAula: true, ipRegiAlta: ip, hostRegiAlta: hostname};
 $.getJSON(url, parametro, function (data) {
 if (data.resultado === "ok") {
 table.ajax.reload();
 $("#modalModificar").modal("hide");
 } else {
 mostrarMensaje("alertModificar", data.mensaje);
 }
 });
 } else {
 
 let url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudtarifa/modificar";
 let parametro = {token: $.fn.getCookie("token"),
 codiTari: codiTari, codiSede: codiSede, codiGrad: codiGrad, codiTipoConc: codiTipoConc, codiAnio: codiAnio, montTari: montTari, codiUsuaModi: codiUsua, actiAula: true, ipRegiModi: ip, hostRegiModi: hostname};
 $.getJSON(url, parametro, function (data) {
 if (data.resultado === "ok") {
 table.ajax.reload();
 $("#modalModificar").modal("hide");
 } else {
 mostrarMensaje("alertModificar", data.mensaje);
 }
 });
 }
 });*/