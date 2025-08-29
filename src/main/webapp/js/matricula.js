let table, codiUsua, ip, hostname;
let tableAlumno, tableServicios;
let globalhost;
$(document).ready(function () {
    
    //FUNCIONES
    $.fn.mostrarMensaje = function (nombreid, mensaje) {
        $("#" + nombreid).text(mensaje);
        $("#" + nombreid).css("display", "block");
        setTimeout(function () {
            $("#" + nombreid).fadeOut(2000);
        }, 2000);
    };
    $.fn.cargarAulas = function () {
        let url = globalhost + "/matriculaservicio/webresources/mostrarviewaulas/buscarxanioxnivel";
        //let url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/mostrarviewaulas/buscarxanioxnivel";
        let parametros = {
            token: $.fn.getCookie("token"),
            nombAnio: $("#txtAnio").val(),
            codiNive: $("#cmbViewNivel").val()
        };

        $.getJSON(url, parametros, function (data) {
            //for (const element of data.data) {
            console.log(data);
            for (const element of data.data) {
                console.log(element);
                $("#cmbViewAula").append("<option value=" + element.codiAula + ">" + element.nombAula + "</option>");
            }
        });

        return this;
    };
    $.fn.validarConfirmarAnulacion = function (code) {
        $("#txtNombAulaAnular").val(decodeURI(code.nombAula));
        $("#txtNombAlumnoAnular").val($("#txtBuscarAlumno").val());
        //code.codiServ        
        $("#modalAnular").modal("show");
    };
    $.fn.validarFormularioAgregar = function (numedocu, paterno, materno, nombre) {
        if ($.trim(numedocu) === "") {
            $("#txtNumeDocu").focus();
            return false;
        }
        if ($.trim(paterno) === "") {
            $("#txtPaterno").focus();
            return false;
        }
        if ($.trim(materno) === "") {
            $("#txtMaterno").focus();
            return false;
        }
        if ($.trim(nombre) === "") {
            $("#txtNombre").focus();
            return false;
        }
        return true;
    };
    $.fn.validarFormularioServicio = function (direccion, numero) {
        if ($.trim(direccion) === "") {
            //$("#advAgregarCliente").val="Debe ingresar el apellido paterno";
            $("#txtDireccion").focus();
            return false;
        }
        if ($.trim(numero) === "") {
            //$("#advAgregarCliente").val="Debe ingresar el apellido paterno";
            $("#txtNumero").focus();
            return false;
        }
        return true;
    };
    $.fn.seleccionarCliente = function (code) {

        $("#btnBuscar").prop("disabled", true);
        $("#txtCodigoAlumno").val(code.codiAlum);

        tableServicios.ajax.reload();
        $("#lblTitulodeServicios").text("Listado de Matriculas del Alumno: " + decodeURI(code.nombre));
        $("#txtBuscarAlumno").val(decodeURI(code.nombre));
        $("#txtBuscarAlumno").prop("disabled", true);
        $("#divListadoServicio").show();

        $("#resultadoBusqueda").hide();

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
        codiUsua=data.codi;
        if (data.resultado === "ok") {
            //CARGA DE INTERFACES Y PARAMETROS GENERALES
            $('#footer').load('footer.html');
            $('#topbar').load('topbar.html');

            $("#divServicio").hide();
            $("#txtCodigoCliente").hide();
            $("#resultadoBusqueda").hide();
            $("#divRazonSocial").hide();
            $("#divListadoServicio").hide();

            //CARGA DATOS CLIENTE
            let url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudtipodocu/listar";
            $.getJSON(url, {token: $.fn.getCookie("token")}, function (data) {
                for (const element of data.data) {
                    $("#cmbTipoDocu").append("<option value=" + element.codiTipoDocu + ">" + element.nombTipoDocu + "</option>");
                }
                url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudubigeo/listar";
                $.getJSON(url, {token: $.fn.getCookie("token")}, function (data) {
                    for (const element of data.data) {
                        $("#cmbUbig").append("<option value=" + element.codiUbig + ">" + element.nombUbig + "</option>");
                    }
                    url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudvia/listar";
                    $.getJSON(url, {token: $.fn.getCookie("token")}, function (data) {
                        for (const element of data.data) {
                            $("#cmbVia").append("<option value=" + element.codiVia + ">" + element.nombVia + "</option>");
                        }
                    });
                });

            });
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
                lengthChange: false,
                responsive: true,
                ajax: {
                    url: decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/mostrarviewaalumnos/procesar",
                    type: 'GET',
                    data: function (d) {
                        d.token = $.fn.getCookie("token");
                        d.letra = $("#txtBuscarAlumno").val();
                    },
                    beforeSend: function () {

                    }, complete: function () {

                    }
                },
                columns: [
                    {data: 'codialum'},
                    {data: 'nombre'},
                    {data: 'numeDocu'},
                    {data: 'codiEstdMinedu'},
                    {data: 'codialum',
                        render: function (data, type, row, meta) {
                            let objJson = {codiAlum: row.codialum, nombre: encodeURIComponent(row.appaAlum + " " + row.apmaAlum + " " + row.nombAlum)};
                            return "<button onclick=$.fn.seleccionarCliente(" + JSON.stringify(objJson) + ") class=\"btn btn-success btn-user \">Seleccionar</button>";
                        }
                    }
                ]
            });

            //CARGA DATOS MATRICULA
            url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudanio/listar";
            $.getJSON(url, {token: $.fn.getCookie("token")}, function (data) {
                for (const element of data.data) {
                    $("#cmbAnio").append("<option value=" + element.codiAnio + ">" + element.nombAnio + "</option>");
                }
                url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/mostrarviewniveles/procesar";
                $.getJSON(url, {token: $.fn.getCookie("token"), codiAnio: $("#cmbAnio").val()}, function (data) {
                    for (const element of data.data) {
                        $("#cmbViewNivel").append("<option value=" + element.codiNive + ">" + element.nombNive + "</option>");
                    }
                    url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/mostrarviewaulas/buscarxanioxnivel";
                    $.getJSON(url, {token: $.fn.getCookie("token"), codiAnio: $("#cmbAnio").val(), codiNive: $("#cmbViewNivel").val()}, function (data) {
                        //console.log(data);
                        for (const element of data.data) {
                            $("#cmbViewAula").append("<option value=" + element.codiAula + ">" + element.nombAula + "</option>");
                        }
                    });
                });

            });
            tableServicios = $('#exampleDetaServicios').DataTable({
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
                lengthChange: false,
                responsive: true,
                ajax: {
                    url: decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/mostrarviewmatriculas/procesar",
                    type: 'GET',
                    data: function (d) {
                        d.token = $.fn.getCookie("token"),
                                d.codiAlum = $("#txtCodigoAlumno").val();
                    },
                    beforeSend: function () {
                    }, complete: function () {
                    }
                },
                columns: [
                    {data: 'codiServ'},
                    {data: 'nombAula'},
                    {data: 'nombAnio'},
                    {data: 'codiAlum'},
                    {data: 'codiServ',
                        render: function (data, type, row, meta) {
                            let objJson = {codiServ: row.codiServ, nombAula: encodeURIComponent(row.nombAula)};
                            return "<button onclick=$.fn.validarConfirmarAnulacion(" + JSON.stringify(objJson) + ") class=\"btn btn-danger btn-user \">Anular</button>";
                        }
                    }
                ]
            });




        } else {
            $(location).attr('href', "index.html");
        }
    });


    //PROGRAMACION DE EVENTOS
    $("#btnRestablecerServicio").click(function () {
        let codiServ = $("#txtCodiServ").val();
        let parametro = {codiServ: codiServ, codiEstd: 'V', tipo: 5};
        $.getJSON("serviciocrud", parametro, function (data) {
            if (data.resultado === "ok") {
                tableServicios.ajax.reload();
            } else {
                alert("Error");
            }
        });
        $("#modalRestablecer").modal("hide");
    });
    $("#btnAnularServicio").click(function () {
        /*let codiServ = $("#txtCodiServ").val();
         let parametro = {codiServ: codiServ, codiEstd: 'A', tipo: 5};
         $.getJSON("serviciocrud", parametro, function (data) {
         if (data.resultado === "ok") {
         tableServicios.ajax.reload();
         } else {
         alert("Error");
         }
         });
         $("#modalAnular").modal("hide");*/
        alert('Falta programar');
    });
    $("#btnSuspenderServicio").click(function () {
        let codiServ = $("#txtCodiServ").val();
        let parametro = {codiServ: codiServ, codiEstd: 'S', tipo: 5};
        $.getJSON("serviciocrud", parametro, function (data) {
            if (data.resultado === "ok") {
                tableServicios.ajax.reload();
            } else {
                alert("Error");
            }
        });
        $("#modalSuspender").modal("hide");
    });
    $("#btnCambiarPlanBD").click(function () {
        let codiServ = $("#txtCodiServ").val();
        let codiPlan = $("#cmbPlanModi").val();
        let diaFact = $("#txtDiaFact1").val();


        let parametro = {codiServ: codiServ, codiPlan: codiPlan, diaFact: diaFact, tipo: 4};
        $.getJSON("serviciocrud", parametro, function (data) {
            if (data.resultado === "ok") {
                tableServicios.ajax.reload();
                $("#modalCambiarPlan").modal("hide");
            } else {
                setMensajeAlert("alertModificarPlan", "No pudo realizar el Cambio de Plan");
            }
        });
    });
    $("#btnGrabar").click(function () {
        console.log("se realizo el click");
        $("#btnGrabar").css("display", "none");
        $("#loadingNuevaMatri").css("loadingNuevaMatri", "block");
        
        let codiAlum = $("#txtCodigoAlumno").val();
        let codiAula = $("#cmbViewAula").val();
        console.log(codiAlum+" - "+codiAula);
        let url = decodeURIComponent($.fn.getCookie("globalhost")) +  "/matriculaservicio/webresources/matricula/procesar";
        let parametro = {codiAlum: codiAlum, codiAula: codiAula,
            ipRegiAlta: ip, hostRegiAlta: hostname, token: $.fn.getCookie("token")};
        console.log("codiAlum: "+codiAlum+" codiAula: "+codiAula+" idRegistro: "+ip+" hostRegiAlra: "+hostname+" token: "+$.fn.getCookie("token"));
        $.getJSON(url, parametro, function (data) {
            
            console.log(data);
            if (data.resultado === "ok") {
                tableServicios.ajax.reload();
                $("#nuevoServicioModal").modal("hide");
            } else {
                mostrarMensaje("advAgregarServicio", data.mensaje);
                $("#btnGrabar").css("display", "block");
                $("#loading").css("loadingNuevaMatri", "none");
            }
        });

        $("#btnGrabar").css("display", "block");
        $("#loading").css("loadingNuevaMatri", "none");



    });
    $("#btnAceptarCambios").click(function () {
        $("#btnAceptar").css('visibility', 'hidden');

        let codigo = $("#txtCodigo").val();
        let tipodocu = $("#cmbTipoDocu").val();
        let numedocu = $("#txtNumeDocu").val();
        let paterno = $("#txtPaterno").val().toUpperCase();
        let materno = $("#txtMaterno").val().toUpperCase();
        let nombre = $("#txtNombre").val().toUpperCase();
        let celular = $("#txtCelular1").val();
        let mail = $("#txtMail").val().toUpperCase();
        let sexo = "";
        if ($('#rdbHombre').is(':checked')) {
            sexo = "V";
        } else {
            sexo = "M";
        }
        let codiUbig = $("#cmbUbig").val();
        let codiVia = $("#cmbVia").val();
        let direServ = $("#txtDireccion").val().toUpperCase();
        let numeDireServ = $("#txtNumero").val().toUpperCase();
        let numInteServ = $("#txtInterior").val().toUpperCase();
        let refeDireServ = $("#txtReferencia").val().toUpperCase();
        let codiEstdMinedu = $("#txtCodiMinedu").val().toUpperCase();

        if (!$.fn.validarFormularioAgregar(numedocu, paterno, materno, nombre)) {
            $.fn.mostrarMensaje("advAgregarCliente", "Para continuar debe completar los datos");
            $("#btnAceptar").css('visibility', 'visible');
        } else {
            let url = globalhost + "/matriculaservicio/webresources/crudalumno/agregar";
            let parametro = {codiAlum: codigo, codiTipoDocu: tipodocu, codiUbig: codiUbig,
                codiVia: codiVia, numeDocu: numedocu,
                appaAlum: paterno, apmaAlum: materno, nombAlum: nombre,
                mailAlum: mail, sexoAlum: sexo, celuAlum: celular, direServ: direServ,
                numeDireServ: numeDireServ, numInteServ: numInteServ, refeDireServ: refeDireServ,
                codiEstdMinedu: codiEstdMinedu, codiUsuaAlta: codiUsua, actiAlum: true, ipRegiModi: ip, hostRegiModi: hostname, token: $.fn.getCookie("token")};
            $.getJSON(url, parametro, function (data) {
                console.log(data);
                if (data.resultado === "ok") {
                    //console.log("codigo new add", data);
                    tableAlumno.ajax.reload();         
                    let objJson = {codiAlum: data.codiAlum, nombre: encodeURIComponent(paterno + " " + materno + " " + nombre)};
                          
                    $.fn.seleccionarCliente(objJson);
                    $("#modalAgregar").modal("hide");
                } else {
                    mostrarMensaje("advAgregarCliente", data.mensaje);
                }
            });
            $("#btnAceptar").css('visibility', 'visible');
        }
    });
    $("#cmbTipoDocu").change(function () {

        $("#divPaterno").show();
        $("#divMaterno").show();
        $("#divNombre").show();
        $("#divSexo").show();
        $("#divRazonSocial").hide();


        //alert($(this).val());
    });
    $("#cmbAnio").change(function () {
        url = globalhost + "/matriculaservicio/webresources/mostrarviewniveles/procesar";
        $.getJSON(url, {token: $.fn.getCookie("token"), codiAnio: $("#cmbAnio").val()}, function (data) {
            for (const element of data.data) {
                $("#cmbViewNivel").append("<option value=" + element.codiNive + ">" + element.nombNive + "</option>");
            }
            url = globalhost + "/matriculaservicio/webresources/mostrarviewaulas/buscarxanioxnivel";
            $.getJSON(url, {token: $.fn.getCookie("token"), codiAnio: $("#cmbAnio").val(), codiNive: $("#cmbViewNivel").val()}, function (data) {
                //console.log(data);
                for (const element of data.data) {
                    $("#cmbViewAula").append("<option value=" + element.codiAula + ">" + element.nombAula + "</option>");
                }
            });
        });
    });
    $("#cmbViewNivel").change(function () {
        url = globalhost + "/matriculaservicio/webresources/mostrarviewaulas/buscarxanioxnivel";
        $.getJSON(url, {token: $.fn.getCookie("token"), codiAnio: $("#cmbAnio").val(), codiNive: $("#cmbViewNivel").val()}, function (data) {
            console.log(data);
            for (const element of data.data) {
                $("#cmbViewAula").append("<option value=" + element.codiAula + ">" + element.nombAula + "</option>");
            }
        });
    });
    $("#btnNuevo").click(function () {

        /*$("#txtNumeDocu").val("");
         $("#txtPaterno").val("");
         $("#txtMaterno").val("");
         $("#txtNombre").val("");
         $("#txtRazon").val("");
         $("#txtCelular").val("");
         $("#txtMail").val("");
         $("#txtCodiMinedu").val("");
         $("#txtCelular1").val("");
         $("#txtDireccion").val("");
         $("#txtNumero").val("");
         $("#txtInterior").val("");
         $("#txtReferencia").val("");*/



        $("#modalAgregar").modal("show");
    });
    $("#btnCancelar").click(function () {
        $("#btnBuscar").prop("disabled", false);
        $("#resultadoBusqueda").hide();
        $("#txtBuscarAlumno").val("");
        $("#txtCodigoAlumno").val("");
        $("#txtBuscarAlumno").prop("disabled", false);
        $("#divListadoServicio").hide();
        $("#divServicio").hide();
        $("#txtBuscarAlumno").focus();
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
    $("#btnAgregarServicio").click(function () {
        $("#lblTituloServicio").text("Registrar nuevo Servicio");
        $("#nuevoServicioModal").modal("show");
    });
    $("#txtNombreCliente").on('keydown', function (e) {
        if (e.which === 8) {
            $("#resultadoBusqueda").hide();
            return false;
        }
    });
    $('#cmbViewNivel').on('change', function () {
        $("#cmbViewAula").empty();
        $("#cmbViewNivel").cargarAulas();
    });             
    $("#salir").click(function () {
        $.getJSON("cerrarsesion", function (data) {});
        $(location).attr('href', "index.html");
    });



    

});
