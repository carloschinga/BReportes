$(document).ready(function () {
    let central = false;
    $.fn.validarSession = function () {
        $.getJSON("validarsesion", function (data) {
            if (data.resultado === "ok") {
            } else {
                $(location).attr('href', "index.html");
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            $(location).attr('href', "index.html");
        });
    };
    $.getJSON("validarsesion", function (data) {
        if (data.resultado === "ok") {
            if (data.central === "S" || data.de === "b") {
                $("#divalmacen").show();
                central = true;
                $.getJSON("CRUDFaAlmacenes?opcion=2", function (data) {
                    if (data.resultado === "ok") {
                        let almacen = $('#almacen');
                        almacen.empty();
                        almacen.append('<option value="">TODOS</option>');
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
            }
        } else {
            $(location).attr('href', "index.html");
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        $(location).attr('href', "index.html");
    });
    $("#btnPDF").click(function () {
        inprimir("pdf");
    });
    $("#btnEXCEL").click(function () {
        inprimir("excel");
    });
    function inprimir(tipo) {
        let sec = $("#secuencia").val();
        if (sec !== "") {
            if (central) {
                let siscod = $("#almacen").val();
                if(siscod===undefined || siscod===null){
                    siscod="";
                }
                window.open("ReporteOrdenTransporteCaja?siscod="+siscod+"&tipo=" + tipo + "&orden=" + sec, "_blank");
            } else {
                window.open("ReporteOrdenTransporteCaja?tipo=" + tipo + "&orden=" + sec, "_blank");
            }
        }
    }
});