$(document).ready(function () {

    function mostrarSoles(soles) {
        $("#soles").text(soles);
    }
    ;

    function cargarTabla(codObj, sisCod, periodo) {
        $.ajax({
            url: "cuoteoservlet",
            type: "GET",
            data: {
                codObj: codObj, // Código de objetivo seleccionado
                sisCod: sisCod,  // Código de sistema seleccionado
                periodo:periodo
            },
            success: function (data) {
                console.log("Monto soles: " + data.soles);
                mostrarSoles(data.soles);

                let tabla = $("#tablaDias tbody");
                tabla.empty(); // Limpiar la tabla antes de agregar nuevos datos

                // Recorrer los días y agregarlos a la tabla
                data.dias.forEach(function (dia) {
                    let fila = `<tr>
                        <td>${dia.dia}</td>
                        <td>${dia.numeroDias}</td>
                        <td><input type="number" class="form-control peso-input" value="0" /></td>
                        <td class="unidades">0</td>
                        <td class="cuotas-dias-mes">0.00</td>
                        <td class="cuota-por-dia">0.00</td>
                    </tr>`;
                    tabla.append(fila);
                });

                // Agregar fila de totales
                let filaTotales = `<tr class="table-secondary">
                    <td><strong>TOTAL</strong></td>
                    <td id="total-dias"><strong>0</strong></td>
                    <td id="total-peso"><strong>0</strong></td>
                    <td id="total-unidades"><strong>0</strong></td>
                    <td id="total-cuotas"><strong>0.00</strong></td>
                    <td id="total-cuota-dia"><strong>0.00</strong></td>
                </tr>`;
                tabla.append(filaTotales);

                // Calcular total inicial de días
                calcularTotalDias();

                // Función para calcular y actualizar el total de días
                function calcularTotalDias() {
                    let totalDias = 0;
                    $("#tablaDias tbody tr:not(:last-child)").each(function () {
                        totalDias += parseFloat($(this).find("td").eq(1).text()) || 0;
                    });
                    $("#total-dias").text(totalDias);
                }

                // Función para calcular y actualizar el total de pesos
                function calcularTotalPeso() {
                    let totalPeso = 0;
                    $(".peso-input").each(function () {
                        totalPeso += parseFloat($(this).val()) || 0;
                    });
                    $("#total-peso").text(totalPeso.toFixed(2));
                }

                // Función para calcular y actualizar el total de unidades
                function calcularTotalUnidades() {
                    let totalUnidades = 0;
                    $(".unidades").each(function () {
                        if (!$(this).closest("tr").is(":last-child")) { // Excluir la fila de totales
                            totalUnidades += parseFloat($(this).text()) || 0;
                        }
                    });
                    $("#total-unidades").text(totalUnidades.toFixed(2));
                    return totalUnidades;
                }

                // Función para calcular y actualizar el total de cuotas
                function calcularTotalCuotas() {
                    let totalCuotas = 0;
                    $(".cuotas-dias-mes").each(function () {
                        if (!$(this).closest("tr").is(":last-child")) { // Excluir la fila de totales
                            totalCuotas += parseFloat($(this).text()) || 0;
                        }
                    });
                    $("#total-cuotas").text(totalCuotas.toFixed(2));
                }

                // Función para calcular y actualizar el total de cuotas por día
                function calcularTotalCuotaPorDia() {
                    let totalCuotaPorDia = 0;
                    $(".cuota-por-dia").each(function () {
                        if (!$(this).closest("tr").is(":last-child")) { // Excluir la fila de totales
                            totalCuotaPorDia += parseFloat($(this).text()) || 0;
                        }
                    });
                    $("#total-cuota-dia").text(totalCuotaPorDia.toFixed(2));
                }

                // Función para calcular la cuota por día para una fila
                function calcularCuotaPorDia(fila) {
                    let cuotaDiasMes = parseFloat($(fila).find(".cuotas-dias-mes").text()) || 0;
                    let numeroDias = parseFloat($(fila).find("td").eq(1).text()) || 0;

                    let cuotaPorDia = 0;
                    if (numeroDias > 0) {
                        cuotaPorDia = cuotaDiasMes / numeroDias;
                    }

                    $(fila).find(".cuota-por-dia").text(cuotaPorDia.toFixed(2));
                }

                // Función para calcular y actualizar las cuotas
                function actualizarCuotas() {
                    let totalUnidades = calcularTotalUnidades();

                    // Calcular el valor de "Cuotas Dias Mes" para cada registro
                    if (totalUnidades > 0) {
                        let valorPorUnidad = parseFloat(data.soles) / totalUnidades;

                        $("#tablaDias tbody tr:not(:last-child)").each(function () {
                            let unidades = parseFloat($(this).find(".unidades").text()) || 0;
                            let cuotaDiasMes = (unidades * valorPorUnidad).toFixed(2);
                            $(this).find(".cuotas-dias-mes").text(cuotaDiasMes);

                            // Calcular la cuota por día para esta fila
                            calcularCuotaPorDia(this);
                        });
                    } else {
                        // Si no hay unidades, establecer las cuotas en 0
                        $(".cuotas-dias-mes").text("0.00");
                        $(".cuota-por-dia").text("0.00");
                    }

                    // Actualizar los totales
                    calcularTotalCuotas();
                    calcularTotalCuotaPorDia();
                }

                // Escuchar cambios en los inputs de "Peso"
                $(".peso-input").on("input", function () {
                    let peso = parseFloat($(this).val()) || 0;
                    let numeroDias = parseFloat($(this).closest("tr").find("td").eq(1).text()) || 0;
                    let unidades = peso * numeroDias;

                    // Actualizar la columna "Unidades"
                    $(this).closest("tr").find(".unidades").text(unidades.toFixed(2));

                    // Actualizar totales
                    calcularTotalPeso();
                    calcularTotalUnidades();

                    // Actualizar las cuotas y la cuota por día
                    actualizarCuotas();
                });

                // Calcular todos los totales iniciales
                calcularTotalDias();
                calcularTotalPeso();
                calcularTotalUnidades();
                actualizarCuotas();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Error al obtener los datos:", textStatus, errorThrown);
            }
        });
    }

    function loadCombos() {
        const combos = [{url: "combosistema", selector: "#comboSisCod"}, {url: "comboobjetivos", selector: "#comboCodObj"}];

        combos.forEach((combo) => loadCombo(combo.url, combo.selector, combo.data));
    }

    function loadCombo(
            url,
            selector,
            data = {},
            defaultValue = null,
            callback = null
            ) {
        $.ajax({
            url: url,
            type: "GET",
            data: data,
            success: function (response) {
                const combo = $(selector);
                combo.empty().append('<option value="">Selecciona</option>');
                response.forEach((item) => {
                    combo.append(
                            `<option value="${item.siscod || item.codobj}">${item.sisent || item.desobj}</option>`
                            );
                });
                if (defaultValue)
                    combo.val(defaultValue);
                if (callback)
                    callback();
            },
            error: function () {
                mostrarAlerta(`Error al cargar los datos de ${selector}`, "error");
            },
        });
    }

    function mostrarAlerta(mensaje, icono) {
        Swal.fire({
            toast: true,
            position: "top-end",
            icon: icono,
            title: mensaje,
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: true,
            customClass: {popup: "swal2-sm"},
        });
    }

    loadCombos();

    $("#filtrarBtn").on("click", function () {
        let codObj = $("#comboCodObj").val();
        let sisCod = $("#comboSisCod").val();
        let periodo=$("#fechaMesAno").val();
       
        cargarTabla(codObj, sisCod,periodo);
    });

    $("#agregarboton").on("click", function () {
        let datosTabla = [];

        $("#tablaDias tbody tr:not(:last-child)").each(function () {
            let fila = {
                dia: $(this).find("td").eq(0).text(),
                numeroDias: parseFloat($(this).find("td").eq(1).text()) || 0,
                peso: parseFloat($(this).find(".peso-input").val()) || 0,
                unidades: parseFloat($(this).find(".unidades").text()) || 0,
                cuotasDiasMes: parseFloat($(this).find(".cuotas-dias-mes").text()) || 0,
                cuotaPorDia: parseFloat($(this).find(".cuota-por-dia").text()) || 0
            };
            datosTabla.push(fila);
        });

        let codObj = $("#comboCodObj").val();
        let sisCod = $("#comboSisCod").val();

        let dataFinal = {
            datosTabla: datosTabla,
            codObj: codObj,
            sisCod: sisCod
        };

        console.log({dataFinal});

        $.ajax({
            url: "ventapesodia",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(dataFinal),
            success: function (response) {
                console.log("Datos guardados correctamente:", response);
                mostrarAlerta("Datos guardados correctamente", "success");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Error al guardar los datos:", textStatus, errorThrown);
                mostrarAlerta("Error al guardar los datos", "error");
            }
        });
    });
});