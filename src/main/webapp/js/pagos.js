let montoMaximo, tableAlumno, codiUsua,tableDeuda;
$(document).ready(function () {

    //FUNCIONES    
    $.fn.seleccionarClienteServicio = function (code) {
        let parametro = {token : 'matricula',codiServ : code};
        let url =decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/mostrarviewdeudas/procesar";
       
        $.getJSON(url, parametro, function (data) {
            /*$("#txtCodiServ").val(data.data.codiServ);
            $("#txtCodiPlan").val(data.data.codiPlan);

            $("#tituloPagar").text("PENDIENTE(S) DE PAGO :  " + " " + data.data.direccion);
            $("#tituloAbonado").text("PAGADO(S):  " + " " + data.data.direccion);

            $('#aleAdvertenciaMonto').hide();
            $("#btnBuscar").prop("disabled", true);
            $("#txtCodigoCliente").val(code);
            parametro = {tipo: 7, codigo: code};*/
            tableDeuda.ajax.reload();
            $("#divServicio").show();

            /*$.getJSON("clienteCRUD", parametro, function (data) {
                $("#txtBuscarCliente").val(data.raznSociClie);
                $("#txtBuscarCliente").prop("disabled", true);
                $("#resultadoBusqueda").hide();
                
                $("#divServicioAbono").show();

            });*/
        });

    };
    $.fn.seleccionarClienteServicioAbonado = function (code) {
        $(".transparentCover").show();
        $(".loading").show();
        $(document).ready(function () {

            let parametro = {tipo: 1, codigo: code};
            $.getJSON("serviciocrud", parametro, function (data) {
                //console.log(data);
                $("#txtCodiServ").val(data.data.codiServ);
                $("#txtCodiPlan").val(data.data.codiPlan);

                $("#tituloPagar").text("ABONOS A LA CUENTA DEL SERVICIO:   " + data.data.direccion);

                $('#aleAdvertenciaMonto').hide();
                $("#btnBuscar").prop("disabled", true);
                $("#txtCodigoCliente").val(code);
                parametro = {tipo: 7, codigo: code};
                tableAbono.ajax.reload();

                $.getJSON("clienteCRUD", parametro, function (data) {
                    console.log(data);
                    $("#txtBuscarCliente").val(data.raznSociClie);
                    $("#txtBuscarCliente").prop("disabled", true);
                    $("#resultadoBusqueda").hide();
                    $("#divServicio").show();

                    $(".transparentCover").hide();
                    $(".loading").hide();
                });
            });

        });
    };
    $.fn.anularCuota = function (cuota) {
        $(".transparentCover").show();
        $(".loading").show();
        $(document).ready(function () {
            $.getJSON("anularpago", {cuota: cuota}, function (data) {
                tableDeuda.ajax.reload();
                $(".transparentCover").hide();
                $(".loading").hide();
            });
        });
    };
    $.fn.mostrarFormPago = function (code, monto) {
        montoMaximo = monto;
        $("#txtMontoPago").val(monto);
        $("#txtCode").val(code);
        $("#txtMontoPago").prop('min', 1);
        $("#txtMontoPago").prop('max', monto);

        $("#modalMontoPago").modal("show");
    };
    $.fn.imprimirRecibo = function (code) {
        window.open("mostarrecibo?numeReci=" + code, 'Reporte', 'width=800, height=600');
    };
    $.fn.imprimirTicket = function (code) {
        window.open("mostrarticket?numeReci=" + code, 'Reporte', 'width=800, height=600');
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

            //CARGA DE INTERFACES Y PARAMETROS GENERALES
            $('#footer').load('footer.html');
            $('#topbar').load('topbar.html');


            $("#divServicio").hide();
            $("#divServicioAbono").hide();
            $("#txtCodigoCliente").hide();
            $("#resultadoBusqueda").hide();
            $("#divRazonSocial").hide();

            //PROGRAMACION DE FUNCIONALIDADES

            tableAlumno = $('#example').DataTable({
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
                ordering: false,
                info: false,
                searching: false,
                ajax: {
                    url: decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/mostrarviewaalumnomatriculados/procesar",
                    type: 'GET',
                    data: function (d) {
                        d.token = $.fn.getCookie("token");
                        //d.codiServ = $("#txtCodiServ").val();
                        d.letra=$("#txtBuscarAlumno").val();
                        
                    },
                    beforeSend: function () {

                    }, complete: function () {

                    }
                },
                columns: [
                    {data: 'codiServ'},
                    {data: 'nombres'},
                    {data: 'nombAnio'},
                    {data: 'nombAula'},
                    {data: 'estdServ'},
                    {data: 'codiServ',
                        render: function (data, type, row, meta) {
                            return "<button onclick=$.fn.seleccionarClienteServicio(" + data + ") class=\"btn btn-success btn-user \">Seleccionar</button>";
                        }
                    }
                ]
            });
            tableDeuda = $('#tblDeuda').DataTable({
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
                ordering: false,
                info: false,
                searching: false,
                ajax: {                    
                    url: decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/mostrarviewdeudas/procesar",
                    type: 'GET',
                    data: function (d) {
                        d.token = $.fn.getCookie("token");
                        d.codiServ = 1;
                    },
                    beforeSend: function () {

                    }, complete: function () {

                    }
                },
                columns: [
                    {data: 'codiCuot'},
                    {data: 'nombConc'},
                    {data: 'montAbon'},
                    {data: 'montDesc'},
                    {data: 'montDeud'},
                    {data: 'estdCuot'},
                    {data: 'codiServ',
                        render: function (data, type, row, meta) {
                         //   let objJson = {codiAlum: row.codialum, nombre: encodeURIComponent(row.appaAlum + " " + row.apmaAlum + " " + row.nombAlum)};
                            return "<button onclick=$.fn.seleccionarCliente(1) class=\"btn btn-success btn-user \">Pagar</button>";
                        }}
                ]
            });
            /*tableAbono = $('#tblAbono').DataTable({
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
             ordering: false,
             info: false,
             searching: false,
             ajax: {
             url: 'mostrarabono',
             type: 'POST',
             data: function (d) {
             d.tipo = 9,
             d.codigo = $('#txtCodigoCliente').val();
             }
             },
             columns: [
             {data: 'codiserv'},
             {data: 'codiAnio'},
             {data: 'codiCuot'},
             {data: 'codiUsuaModi'},
             {data: 'fechRegiModi',
             render: function (data, type, row, meta) {
             if (data === undefined)
             return "";
             else
             return moment(data).format("DD/MM/yyyy");
             }},
             {data: 'codiConc'},
             {data: 'nombConc'},
             {data: 'montReci'},
             {data: 'codiReci'},
             {data: 'estdReci'},
             {data: 'usuReci'},
             {data: 'fechRegiAlta',
             render: function (data, type, row, meta) {
             if (data === undefined)
             return "";
             else
             return moment(data).format("DD/MM/yyyy");
             }},
             {data: 'codiReci',
             render: function (data, type, row, meta) {
             return "<button onclick=imprimirRecibo(" + data + ") class=\"btn btn-primary btn-user \">Recibo</button>\n\
             <button onclick=imprimirTicket(" + data + ") class=\"btn btn-primary btn-user \">Ticket</button>";
             }
             }
             ]
             });
             $.getJSON("formPagoCRUD", {tipo: 9}, function (data) {
             for (const element of data) {
             $("#cmbFormPago").append("<option value=" + element.codiFormPago + ">" + element.nombFormPago + "</option>");
             }
             });*/

        } else {
            $(location).attr('href', "index.html");
        }
    });



    //PROGRAMACION DE EVENTOS

    $("#btnGrabar").click(function () {
        //alert("hola");
        //$("#modalConfirmar").modal("show");

        window.$('#modalConfirmar').modal();
        console.log("aa");
    });
    $("#btnBuscar").click(function () {
        let nombreAbuscar = $("#txtBuscarAlumno").val();

        if (nombreAbuscar.length >= 2) {
            $("#btnBuscar").css("display", "none");
            $("#loading").css("display", "block");
            tableAlumno.ajax.reload();
            $("#resultadoBusqueda").show();
            $("#btnBuscar").css("display", "block");
            $("#loading").css("display", "none");
        } else {
            $.fn.mostrarMensaje("aleAdvertencia", "Debe ingresar minimamente 2 caracteres...");
        }
    });
    $("#txtNombreCliente").on('keydown', function (e) {
        if (e.which === 8) {
            $("#resultadoBusqueda").hide();
            return false;
        }
    });
    $("#btnCancelar").click(function () {
        $("#btnBuscar").prop("disabled", false);
        $("#resultadoBusqueda").hide();
        $("#txtBuscarAlumno").val("");
        $("#txtCodigoAlumno").val("");
        $("#txtBuscarAlumno").prop("disabled", false);
        $("#divListadoServicio").hide();
        $("#divServicio").hide();
        $("#divServicioAbono").hide();
        $("#txtBuscarAlumno").focus();
    });
    $("#btnAgregarCuota").click(function () {
        $.getJSON("parametrocrud", {codigo: 3}, function (data) {
            if (data.resultado === "ok") {
                $("#txtAnio").val(data.valor);
                $.getJSON("mescrud", function (data) {
                    //console.log(data);
                    $("#cmbMes").empty();
                    for (const element of data) {
                        $("#cmbMes").append("<option value=" + element.codiMes + ">" + element.nombMes + "</option>");
                    }
                });
                $("#agregarCuotaModal").modal("show");
            } else {
                alert("No puede continuar, no se ha especificado el año en parametro");
            }
        });

    });
    $("#btnAgrearCuotaCuenta").click(function () {
        $(".transparentCover").show();
        $(".loading").show();
        let codiserv = $("#txtCodiServ").val();
        let codiplan = $("#txtCodiPlan").val();
        let mes = $("#cmbMes").val();
        let anio = $("#txtAnio").val();
        let parametro = {tipo: 3, codiserv: codiserv, codiplan: codiplan, mes: mes, anio: anio};
        //console.log(parametro);

        $.getJSON("cuotacrud", parametro, function (data) {
            if (data.resultado === "ok") {
                tableDeuda.ajax.reload();
                $(".transparentCover").hide();
                $(".loading").hide();
            } else {
                alert("Error No pudo agregar la cuota");
                $(".transparentCover").hide();
                $(".loading").hide();
            }

        });

        $("#agregarCuotaModal").modal("hide");
    });
    $("#salir").click(function () {
        $.getJSON("cerrarsesion", function (data) {});
        $(location).attr('href', "index.html");
    });
    $("#btnCerrarSesion").click(function () {
        $.getJSON("cerrarSesion", function (data) {
            if (data.resultado === "ok") {
                $(window).attr('location', 'index.html');
            }
        });
    });
    $("#btnGrabarPago").click(function () {
        $(".transparentCover").show();
        $(".loading").show();

        $("#btnGrabarPago").prop("disabled", true);
        let monto = $("#txtMontoPago").val();
        let formPago = $("#cmbFormPago").val();
        let operacion = $("#txtOperación").val();

        if (monto > 0 && montoMaximo >= monto) {
            let code = $("#txtCode").val();
            let parametro = {cuota: code, monto: monto, formPago: formPago, operacion: operacion, codiusua: 1};

            $.getJSON("pagarCuota", parametro, function (data) {

                if (data.resultado === "ok") {
                    $("#modalMontoPago").modal("hide");
                    $("#divServicio").modal("hide");

                    tableDeuda.ajax.reload();
                    tableAbono.ajax.reload();


                    $("#btnBuscar").prop("disabled", false);
                    $("#resultadoBusqueda").hide();
                    $("#txtBuscarCliente").val("");
                    $("#txtCodigoCliente").val("");
                    $("#txtBuscarCliente").prop("disabled", false);

                    $("#txtBuscarCliente").focus();

                    $(".transparentCover").hide();
                    $(".loading").hide();
                    window.open("mostarrecibo?numeReci=" + data.numerReci, 'Reporte', 'width=800, height=600');

                }
                $("#btnGrabarPago").prop("disabled", false);

            });
        } else
            $('#aleAdvertenciaMonto').slideDown();
        $(".transparentCover").hide();
        $(".loading").hide();
        $("#btnGrabarPago").prop("disabled", false);
    });
});








