$(document).ready(function () {
    let ordenini;
    let ordenfin;
    let siscod;
    let tabla;
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

    $.getJSON("CRUDFaAlmacenes?opcion=2", function (data) {
        if (data.resultado === "ok") {
            let almacen = $('#almacen');
            almacen.empty();
            //almacen.append('<option value="">TODOS</option>');
            $.each(data.data, function (key, value) {
                //if(value.codalm!=="A1" && value.codalm!=="A2")
                almacen.append('<option value="' + value.siscod + '">' + value.desalm + '</option>');
            });
        } else {
            if (data.mensaje === "nosession") {
                $.fn.validarSession();
            } else {
                alert("Error: Problemas con el servidor.");
            }
        }
    });
    function actualizartabla() {
        tabla = $('#tabla').DataTable();
        if (tabla) {
            tabla.destroy();
        }
        tabla = $('#tabla').DataTable({
            ajax: {
                url: "picking",
                type: "GET",
                data: function (d) {
                    d.opcion = 25;
                    d.ordenini = ordenini;
                    d.ordenfin = ordenfin;
                    d.siscod = siscod;
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
                {data: 'orden'},
                {data: 'invnum', render: function (data, type, row) {
                        return data ? data : "";
                    }
                },
                {data: 'numitm', render: function (data, type, row) {
                        return data ? data : "";
                    }
                },
                {data: 'codpro'},
                {data: 'despro'},
                {data: 'calculo'},
                {data: null, render: function (data, type, row) {
                        if (row.cantidad) {
                            return '<div class="row"><div class="col-auto"><input type="number" class="form-control form-control-sm" disabled value=' + row.cantidad + '></div><div class="col-auto"><button class="btn btn-danger btn-sm"  data-codpro="' + row.codpro + '" data-orden=' + row.orden + ' data-siscod=' + row.siscod + '>Cancelar</button></div></div>';

                        } else {
                            return '<div class="row"><div class="col-auto"><input type="number" class="form-control form-control-sm"></div><div class="col-auto"><button class="btn btn-primary btn-sm" data-codpro="' + row.codpro + '" data-orden=' + row.orden + ' data-siscod=' + row.siscod + '>Grabar</button></div></div>';

                        }
                    }
                }

            ]
        });
    }
    $('#tabla').on("click", ".btn-primary", function () {
        // Recupera el valor del input number dentro de la misma fila
        const inputValue = $(this).closest('.row').find('input[type="number"]').val();

        // Recupera los valores de los atributos data del botón
        const codpro = $(this).data('codpro');
        const orden = $(this).data('orden');
        const siscod = $(this).data('siscod');

        $.getJSON("CRUDReposicionRecepcion", {opcion: 1, codpro: codpro, orden: orden, siscod: siscod, cant: inputValue}, function (data) {
            if (data.resultado === "ok") {
                $('#tabla').DataTable().ajax.reload();
            } else {
                if (data.mensaje === "nosession") {
                    $.fn.validarSession();
                } else if (data.mensaje === "yaexiste") {
                    alert("Error, este valor ya existe en el sistema, actualize");
                }else{
                    alert("Error al cambiar de estado");
                }
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            alert("Error de conexion con el servidor");
        });
    });
    $('#tabla').on("click", ".btn-danger", function () {
        const codpro = $(this).data('codpro');
        const orden = $(this).data('orden');
        const siscod = $(this).data('siscod');

        $.getJSON("CRUDReposicionRecepcion", {opcion: 2, codpro: codpro, orden: orden, siscod: siscod}, function (data) {
            if (data.resultado === "ok") {
                $('#tabla').DataTable().ajax.reload();
            } else {
                if (data.mensaje === "nosession") {
                    $.fn.validarSession();
                } else if (data.mensaje === "noexiste") {
                    alert("Error, este valor ya existe en el sistema, actualize");
                }else{
                    alert("Error al cambiar de estado");
                }
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            alert("Error de conexion con el servidor");
        });
    });
    $("#formulario").on("submit", function (e) {
        e.preventDefault();
        console.log("boton");
        ordenini = $("#ordenini").val();
        ordenfin = $("#ordenfin").val();
        siscod = $("#almacen").val();
        actualizartabla();
    });
    $("#ordenini").on("change",function(){
        $("#ordenfin").val($("#ordenini").val());
    });
});