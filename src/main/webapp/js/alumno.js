
let table, codiUsua, ip, hostname;
$(document).ready(function () {
    //FUNCIONES

    $.fn.seleccionarLetra = function (letra) {
        //console.log(letra);
        $("#txtLetra").val(letra);
        // alert($("#txtLetra").val());
        table.ajax.reload();


    };
    $.fn.mostrarMensaje = function (nombreid, mensaje) {
        $("#" + nombreid).text(mensaje);
        $("#" + nombreid).css("display", "block");
        setTimeout(function () {
            $("#" + nombreid).fadeOut(2000);
        }, 2000);
    };
    $.fn.seleccionarAlumno = function (row) {


        $("#txtCodigo").val(row.codialum);
        $("#cmbTipodocu").val(row.codiTipoDocu);
        $("#txtNumeDocu").val(row.numeDocu);
        $("#txtPaterno").val(row.appaAlum);
        $("#txtMaterno").val(row.apmaAlum);
        $("#txtNombre").val(row.nombAlum);
        $("#txtMail").val(row.mailAlum);
        $("#txtCelular1").val(row.celuAlum);
        $("#cmbUbig").val(row.codiUbig);
        $("#cmbVia").val(row.codiVia);
        $("#txtDireccion").val(row.direServ);
        $("#txtNumero").val(row.numeDireServ);
        $("#txtInterior").val(row.numInteServ);
        $("#txtReferencia").val(row.refeDireServ);
        $("#txtCodiMinedu").val(row.codiEstdMinedu);


        if (row.sexoAlum === 'V') {
            $("#rdbHombre").attr('checked', true);
        } else {
            $("#rdbMujer").attr('checked', true);
        }

        $("#modalModificar").modal("show");



    };
    $.fn.anularPlan = function (code) {
        $(document).ready(function () {
            $("#txtCodigoAnular").val(code);
            $("#anularModal").modal("show");
        });
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
            //$('#sidebar').load('sidebar.html');
            $('#footer').load('footer.html');
            $('#topbar').load('topbar.html');
            
            //CARGA DE COMBOS
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

            //PROGRAMACION DE FUNCIONALIDADES


            let letras = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'Ñ', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
            for (var i = 0; i < letras.length; i++) {
                $('#btnAbecedeario').append("<label class=\"btn btn-primary\"><input type=\"radio\" name=\"letra\" onclick=\"$.fn.seleccionarLetra('" + letras[i] + "')\" value=" + letras[i] + ">" + letras[i] + "</label>");
            }
            //       seleccionarLetra('A')        ;



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
                /*dom: 'Bfrtip',
                 buttons: [
                 'copy', 'csv', 'excel', 'pdf', 'print'
                 ],*/
                paging: false,
                ajax: {
                    url: decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/mostrarviewaalumnos/procesar",
                    type: 'GET',
                    data: function (d) {
                        // d.tipo = 5,
                        d.token = $.fn.getCookie("token");
                        d.letra = $("#txtLetra").val();
                    }

                },
                columns: [
                    {data: 'codialum'},
                    {data: 'nombTipoDocu'},
                    {data: 'numeDocu'},
                    {data: 'nombre'},
                    {data: 'mailAlum'},
                    {data: 'celuAlum'},
                    {data: 'sexoAlum'},
                    {data: 'codiEstdMinedu'},
                    {data: 'codiAlum',
                        render: function (data, type, row, meta) {
                            let objJson = {codialum: row.codialum, codiTipoDocu: row.codiTipoDocu, nombTipoDocu: row.nombTipoDocu, numeDocu: row.numeDocu, appaAlum: row.appaAlum, apmaAlum: row.apmaAlum, nombAlum: row.nombAlum.replace(/ /g, '&nbsp;'), mailAlum: row.mailAlum, celuAlum: row.celuAlum, sexoAlum: row.sexoAlum,
                                codiEstdMinedu: row.codiEstdMinedu, codiUbig: row.codiUbig, codiVia: row.codiVia, direServ: row.direServ, numeDireServ: row.numeDireServ, numInteServ: row.numInteServ, refeDireServ: row.refeDireServ
                            };
                            return "<button onclick=$.fn.seleccionarAlumno(" + JSON.stringify(objJson) + ") class=\"btn btn-success btn-user \">Modificar</button>";
                        }
                    }
                ]
            });
            $(".transparentCover").hide();
            $(".loading").hide();


        } else {
            $(location).attr('href', "index.html");
        }


    });

    //PROGRAMACIÓN DE EVENTOS

    $("#btnGrabar").click(function () {

        $("#btnGrabar").prop("disabled", true);
        let accion = $("#accion").val();
        var modal = $('#modalAgregar');
        //Metodo Agregar Plan
        if (accion === "1") {
            let nombre = $("#txtNombre").val();
            let monto = $("#txtMonto").val();
            let tipoPlan = $("#cmbTipoPlan").val();
            let estado = $("#txtEstado").val();

            let parametro = {tipo: 3, nombre: nombre, monto: monto, estado: estado, tipoPlan: tipoPlan};

            $.getJSON("planCRUD", parametro, function (data) {
                table.ajax.reload();
                modal.modal('hide');
            });
        } else {
            //Metodo Modificar Plan
            let codigo = $("#txtCodigo").val();
            let nombre = $("#txtNombre").val();
            let monto = $("#txtMonto").val();
            let tipoPlan = $("#cmbTipoPlan").val();
            let estado = $("#txtEstado").val();

            let parametro = {tipo: 4, codigo: codigo, nombre: nombre, monto: monto, estado: estado, tipoPlan: tipoPlan};
            $.getJSON("planCRUD", parametro, function (data) {
                table.ajax.reload();




                modal.modal('hide');
            });
        }
        $("#btnGrabar").prop("disabled", false);
    });
    $("#btnNuevo").click(function () {
        $("#tituloModal").val("Agregar Plan");
        $("#accion").val("1");
        $("#txtCodigo").val("AUTOMATICO");
        $("#txtNombre").val("");
        $("#txtMonto").val("");
        $("#cmbTipoPlan").val($("#cmbTipoPlanl").val());

        $("#modalAgregar").modal("show");
    });
    //CERRAR SESIÓN   
    $("#salir").click(function () {
        $.getJSON("cerrarsesion", function (data) {});
        $(location).attr('href', "index.html");
    });
    $("#btnAceptarCambios").click(function () {
        //$("#btnAceptar").prop("disabled", true);
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
            let url = decodeURIComponent($.fn.getCookie("globalhost")) + "/matriculaservicio/webresources/crudalumno/modificar";
            let parametro = {codiAlum: codigo, codiTipoDocu: tipodocu, codiUbig: codiUbig,
                codiVia: codiVia, numeDocu: numedocu,
                appaAlum: paterno, apmaAlum: materno, nombAlum: nombre,
                mailAlum: mail, sexoAlum: sexo, celuAlum: celular, direServ: direServ,
                numeDireServ: numeDireServ, numInteServ: numInteServ, refeDireServ: refeDireServ,
                codiEstdMinedu: codiEstdMinedu, codiUsuaAlta: codiUsua, actiAlum: true, ipRegiModi: ip, hostRegiModi: hostname, token: $.fn.getCookie("token")};
            $.getJSON(url, parametro, function (data) {
                if (data.resultado === "ok") {
                    table.ajax.reload();
                    $("#modalModificar").modal("hide");
                } else {
                    $.fn.mostrarMensaje("advAgregarCliente", data.mensaje);
                }
            });
            $("#btnAceptar").css('visibility', 'visible');
        }
    });



    /*let validarFormularioAgregar = function (numedocu, paterno, materno, nombre) {
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
     };*/
});

/*function seleccionarLetra(letra) {
 //console.log(letra);
 $("#txtLetra").val(letra);
 // alert($("#txtLetra").val());
 table.ajax.reload();
 
 
 }
 function seleccionarAlumno(row) {
 
 
 $("#txtCodigo").val(row.codialum);
 $("#cmbTipodocu").val(row.codiTipoDocu);
 $("#txtNumeDocu").val(row.numeDocu);
 $("#txtPaterno").val(row.appaAlum);
 $("#txtMaterno").val(row.apmaAlum);
 $("#txtNombre").val(row.nombAlum);
 $("#txtMail").val(row.mailAlum);
 $("#txtCelular1").val(row.celuAlum);
 $("#cmbUbig").val(row.codiUbig);
 $("#cmbVia").val(row.codiVia);
 $("#txtDireccion").val(row.direServ);
 $("#txtNumero").val(row.numeDireServ);
 $("#txtInterior").val(row.numInteServ);
 $("#txtReferencia").val(row.refeDireServ);
 $("#txtCodiMinedu").val(row.codiEstdMinedu);
 
 
 if (row.sexoAlum === 'V') {
 $("#rdbHombre").attr('checked', true);
 } else {
 $("#rdbMujer").attr('checked', true);
 }
 
 $("#modalModificar").modal("show");
 
 
 
 }
 function anularPlan(code) {
 $(document).ready(function () {
 $("#txtCodigoAnular").val(code);
 $("#anularModal").modal("show");
 });
 }
 function getCookie(nombre) {
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
 }*/
        