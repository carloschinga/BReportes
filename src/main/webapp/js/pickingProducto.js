window.onscroll = function () {
    if (document.body.scrollTop > 50 || document.documentElement.scrollTop > 50) {
        backButton.style.top = '20px'; // Nueva altura cuando el usuario hace scroll
    } else {
        backButton.style.top = '100px'; // Altura inicial
    }
};
var backButton = document.getElementById('back-button');
if (document.body.scrollTop > 50 || document.documentElement.scrollTop > 50) {
    backButton.style.top = '20px'; // Nueva altura cuando el usuario hace scroll
} else {
    backButton.style.top = '100px'; // Altura inicial
}

$(document).ready(function () {
    $('html, body').scrollTop(0);
    let fecini;
    if (localStorage.getItem('pik_fecinicheck') === "" || localStorage.getItem('pik_fecinicheck') === undefined) {
        //localStorage.setItem('pik_fecinicheck', fecini);
    }
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
    $.fn.validarSession();
    let codlot = localStorage.getItem('pik_codlot');
    let codpro = localStorage.getItem('pik_codpro');
    let siscod = localStorage.getItem('pik_siscod');
    let secuencia = localStorage.getItem('pik_secuencia');
    $.getJSON("picking", {codlot: codlot, codpro: codpro, siscod: siscod, opcion: 4, secuencia: secuencia}, function (data) {
        if (data.resultado === "ok") {

            $("#titulo").text(data.data.despro + "-" + codpro + "(" + data.data.codlab + ")");
            $("#titulo2").text(data.data.despro + "-" + codpro + "(" + data.data.codlab + ")");
            $("#Entero").val(data.data.cante);
            $("#Fraccion").val(data.data.cantf);

            //$("#cante-1").val(data.data.cante);
            //$("#cantf-1").val(data.data.cantf);

            const partes = data.data.fecven.split("-");
            const fechaFormateada = `${partes[2]}-${partes[1]}-${partes[0].slice(2)}`;
            $("#Fecha").val(fechaFormateada);
            $("#Secuencia").val(data.data.secuencia);
            $("#EAN13").val(data.data.codalt);
            $("#Lote").val(data.data.lote);
            $("#caja").val(data.data.caja);
            if (data.data.entero !== undefined) {
                $("#cante").val(data.data.entero);
            }
            if (data.data.fraccion !== undefined) {
                $("#cantf").val(data.data.fraccion);
            }
            if (data.cajas !== undefined && data.cajas.length > 0) {
                let container = $('#cajas-container');
                container.empty(); // Limpiar el contenedor antes de agregar nuevos elementos
                let cajaCounter = 1;
                data.cajas.forEach((item, index) => {
                    // Crear un nuevo bloque para cada caja
                    let newCaja = `
                <div class="row mb-2 caja-entry">
                    <div class="col-12 col-md-6">
                        <div class="form-group">
                            <label for="caja-${cajaCounter}" class="form-label">${index === 0 ? 'Caja' : 'Caja ' + cajaCounter}</label>
                            <input type="text" class="form-control caja-input" id="caja-${cajaCounter}" value="${item.caja}">
                        </div>
                    </div>
                    <div class="col-6 col-md-3">
                        <div class="form-group">
                            <label for="cante-${cajaCounter}" class="form-label">Cant. E</label>
                            <input type="number" class="form-control cante-input" id="cante-${cajaCounter}" value="${item.cante}">
                        </div>
                    </div>
                    <div class="col-6 col-md-3">
                        <div class="form-group">
                            <label for="cantf-${cajaCounter}" class="form-label">Cant. F</label>
                            <input type="number" class="form-control cantf-input" id="cantf-${cajaCounter}" value="${item.cantf}">
                        </div>
                    </div>
                    ${index === 0 ? '' :
                            '<div class="col-12"><button type="button" class="btn btn-danger btn-sm remove-caja">Eliminar Caja</button></div>'}
                        </div>
                        `;

                    // Agregar el bloque al contenedor
                    container.append(newCaja);
                    cajaCounter++;
                });
            }
            fecini = data.fecha;
            if (localStorage.getItem('pik_fecinicheck') === "" || localStorage.getItem('pik_fecinicheck') === undefined) {
                localStorage.setItem('pik_fecinicheck', fecini);
            }
            if (data.data.check1 === "S") {
                $("#confean13").text("OK");
                $("#confean13").css("background-color", "#28D137");
            }
            if (data.data.check2 === "S") {
                $("#confpick").text("OK");
                $("#confpick").css("background-color", "#28D137");
            }
        } else {
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
    });
    $("#EAN13input").focus();
    var fixedTitle = $('#fixed-title');
    var cardHeader = $('#card-header');
    var cardHeaderOffset = cardHeader.offset().top;

    $(window).on('scroll', function () {
        if ($(window).scrollTop() > cardHeaderOffset) {
            fixedTitle.css({
                'display': 'block',
                'width': cardHeader.outerWidth()
            });
        } else {
            fixedTitle.css('display', 'none');
        }
    });

    $(window).on('resize', function () {
        if (fixedTitle.css('display') === 'block') {
            fixedTitle.css('width', cardHeader.outerWidth());
        }
    });
    $("#confean13").click(function () {
        let codlot = localStorage.getItem('pik_codlot');
        let codpro = localStorage.getItem('pik_codpro');
        let secuencia = localStorage.getItem('pik_secuencia');
        let ean13 = $("#EAN13").val().toString();
        let input = $("#EAN13input").val().toString();
        if (ean13 === input || $("#confean13").text() === "OK") {
            $.getJSON("picking", {codlot: codlot, codpro: codpro, opcion: 5, secuencia: secuencia}, function (data) {
                if (data.resultado === "ok") {
                    $("#contenido").load('pickingProducto.html');
                } else {
                    if (data.mensaje === "yaenviado") {
                        alert("Esta OT ya ha sido cerrada");
                    } else {
                        alert("ocurrio un problema, vuelve a intentarlo");
                    }
                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
            });
        }
    });
    $("#EAN13input").keypress(function (event) {
        if (event.which === 13) {
            let ean13 = $("#EAN13").val().toString();
            let input = $("#EAN13input").val().toString();
            console.log(ean13);
            console.log(input);
            if (ean13 === input && $("#confean13").text() !== "OK") {

                let codlot = localStorage.getItem('pik_codlot');
                let codpro = localStorage.getItem('pik_codpro');
                let secuencia = localStorage.getItem('pik_secuencia');
                $.getJSON("picking", {codlot: codlot, codpro: codpro, opcion: 5, secuencia: secuencia}, function (data) {
                    if (data.resultado === "ok") {
                        $("#contenido").load('pickingProducto.html');
                    } else {
                        if (data.mensaje === "yaenviado") {
                            alert("Esta OT ya ha sido cerrada");
                        } else {
                            alert("ocurrio un problema, vuelve a intentarlo");
                        }
                    }
                }).fail(function (jqXHR, textStatus, errorThrown) {
                });
            }
        }

    });
    $("#confpick").click(function () {
        let Entero = $("#Entero").val().toString();
        let Fraccion = $("#Fraccion").val().toString();
        //let cante = $("#cante").val().toString();
        //let cantf = $("#cantf").val().toString();


        let codlot = localStorage.getItem('pik_codlot');
        let codpro = localStorage.getItem('pik_codpro');
        let secuencia = localStorage.getItem('pik_secuencia');
        fecini = localStorage.getItem('pik_fecinicheck');
        let cajasData = [];
        let entra = true;
        $('.caja-entry').each(function (index) {
            let caja = $(this).find('.caja-input').val();
            let cante = $(this).find('.cante-input').val();
            let cantf = $(this).find('.cantf-input').val();
            if (caja === "") {
                entra = false;
            }
            if (isNaN(cante) || cante <= 0) {
                cante = "";
            }
            if (isNaN(cantf) || cantf <= 0) {
                cantf = "";
            }
            if (cante === "" && cantf === "") {
                entra = false;
            }
            // Añadir los datos de la caja a la lista
            cajasData.push({
                caja: caja,
                cantE: cante,
                cantF: cantf
            });
        });

        if ($("#confpick").text() !== "OK") {
            if (entra) {
                /*if(isNaN(cante) || cante<=0){
                 cante="";
                 }
                 if(isNaN(cantf) || cantf<=0){
                 cantf="";
                 }
                 if (cante !== "" || cantf !== "") {
                 if (cante === "") {
                 cante = 0;
                 }
                 if (cantf === "") {
                 cantf = 0;
                 }
                 $.getJSON("picking", {codlot: codlot, codpro: codpro, opcion: 6, secuencia: secuencia, fecha: fecini, cante: cante, cantf: cantf,caja:caja}, function (data) {
                 if (data.resultado === "ok") {
                 $("#contenido").load('pickingDetalle.html');
                 } else {
                 alert("ocurrio un problema, vuelve a intentarlo");
                 }
                 }).fail(function (jqXHR, textStatus, errorThrown) {
                 });
                 } else {
                 alert("por favor ingrese cantidades");
                 }*/
                console.log(JSON.stringify(cajasData));
                $.ajax({
                    url: "picking?codlot=" + codlot + "&codpro=" + codpro + "&opcion=6&secuencia=" + secuencia + "&fecha=" + fecini + "&cante=" + Entero + "&cantf=" + Fraccion,
                    type: "POST",
                    contentType: "application/json",
                    data: JSON.stringify(cajasData),
                    dataType: "json",
                    success: function (data) {
                        if (data.resultado === "ok") {
                            $("#contenido").load('pickingDetalle.html');
                        } else {
                            if (data.mensaje === "yaenviado") {
                                alert("Esta OT ya ha sido cerrada");
                            } else {
                                alert("ocurrio un problema, vuelve a intentarlo");
                            }
                        }
                    },
                    error: function (xhr, status, error) {
                        alert("ocurrio un problema, vuelve a intentarlo");
                    }
                });
            } else {
                alert("Ingrese correctamente los datos de las cajas");
            }
        } else {
            $.getJSON("picking", {codlot: codlot, codpro: codpro, opcion: 6, secuencia: secuencia, fecha: fecini, cante: 0, cantf: 0}, function (data) {
                if (data.resultado === "ok") {
                    $("#contenido").load('pickingProducto.html');
                } else {
                    if (data.mensaje === "yaenviado") {
                        alert("Esta OT ya ha sido cerrada");
                    } else {
                        alert("ocurrio un problema, vuelve a intentarlo");
                    }
                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
            });
        }
    });
    $("#back-button").click(function () {
        $("#contenido").load('pickingDetalle.html');
    });
    $(document).ready(function () {
        let cajaCounter = 1;

        function updateLabels() {
            $('.caja-entry').each(function (index) {
                if (index === 0) {
                    $(this).find('.form-label').first().text('Caja');
                } else {
                    $(this).find('.form-label').first().text('Caja ' + (index + 1));
                }
                $(this).find('.caja-input').attr('id', 'caja-' + (index + 1));
                $(this).find('.cante-input').attr('id', 'cante-' + (index + 1));
                $(this).find('.cantf-input').attr('id', 'cantf-' + (index + 1));
            });
            cajaCounter = $('.caja-entry').length;
        }

        $('#add-caja').click(function () {
            cajaCounter++;
            const newCaja = `
                        <div class="row mb-2 caja-entry">
    <div class="col-12 col-md-6">
        <div class="form-group">
            <label for="caja-${cajaCounter}" class="form-label">Caja ${cajaCounter - 1}</label>
            <input type="text" class="form-control caja-input" id="caja-${cajaCounter}">
        </div>
    </div>
    <div class="col-6 col-md-3">
        <div class="form-group">
            <label for="cante-${cajaCounter}" class="form-label">Cant. E</label>
            <input type="number" class="form-control cante-input" id="cante-${cajaCounter}">
        </div>
    </div>
    <div class="col-6 col-md-3">
        <div class="form-group">
            <label for="cantf-${cajaCounter}" class="form-label">Cant. F</label>
            <input type="number" class="form-control cantf-input" id="cantf-${cajaCounter}">
        </div>
    </div>
    <div class="col-12">
        <button type="button" class="btn btn-danger btn-sm remove-caja">Eliminar Caja</button>
    </div>
</div>`;
            $('#cajas-container').append(newCaja);
            updateLabels();
        });

        $(document).on('click', '.remove-caja', function () {
            $(this).closest('.caja-entry').remove();
            updateLabels();
        });
    });
    $("#cajas-container").on('change', '.cante-input, .cantf-input', function () {
        // Define tus máximos permitidos para cantE y cantF
        let maxCantE = $("#Entero").val();
        let maxCantF = $("#Fraccion").val();
        // Identificar si es un campo cantE o cantF el que cambió
        let isCantE = $(this).hasClass('cante-input');
        let maxAllowed = isCantE ? maxCantE : maxCantF;
        // Sumar todos los valores de los campos del mismo tipo (cantE o cantF)
        let sum = 0;
        if (isCantE) {
            $('.cante-input').each(function () {
                sum += parseFloat($(this).val()) || 0;
            });
        } else {
            $('.cantf-input').each(function () {
                sum += parseFloat($(this).val()) || 0;
            });
        }

        // Verificar si la suma excede el máximo permitido
        if (sum > maxAllowed) {
            alert(`La suma total de ${isCantE ? 'Cant. E' : 'Cant. F'} no puede exceder ${maxAllowed}.`);
            $(this).val(''); // Borrar el valor del campo que causó el exceso
        }
    });
    $("#cajas-container").on('change', '.caja-input', function () {
        // Obtener el valor del campo que cambió
        let currentValue = $(this).val();
        let cuadro = $(this);
        console.log(currentValue);
        // Verificar si el valor ya existe en otro campo .caja-input
        let duplicateFound = false;
        $('.caja-input').each(function () {
            console.log("currentValue");
            if ($(this).val() === currentValue && cuadro.get(0) !== $(this).get(0)) {
                duplicateFound = true;
                return false; // Romper el bucle si se encuentra un duplicado
            }
        });
        // Si se encontró un duplicado, mostrar una alerta y borrar el valor duplicado
        if (duplicateFound) {
            alert('Esta caja ya se esta utilizando en este producto.');
            $(this).val(''); // Borrar el valor duplicado
        }
    });
});