$(document).ready(function () {
    cargarObjetivos();
    cargarFarmacias();

    $('#consultarMonto').click(function () {
        const idObjetivo = $('#selectObjetivo').val();
        const idFarmacia = $('#selectFarmacia').val();

        if (!idObjetivo || !idFarmacia) {
            alert("Seleccione un objetivo y una farmacia.");
            return;
        }

        obtenerMontoAsignado(idObjetivo, idFarmacia);
        cargarDiasConPesos(idObjetivo, idFarmacia);
        obtenerDatosObjetivo(idObjetivo); // Mostrar info del objetivo, como mes y año
    });

    $('#guardarDistribucion').click(function () {
        guardarPesos();
    });
});

function cargarObjetivos() {
    $.ajax({
        type: 'POST',
        url: 'CRUDobjetivos',
        data: {opcion: 1},
        dataType: 'json',
        success: function (data) {
            console.log("DATA RECIBIDA:", data);

            if (data.resultado === "ok" && Array.isArray(data.data)) {
                $('#selectObjetivo').empty().append('<option value="">Seleccione</option>');
                data.data.forEach(function (item) {
                    $('#selectObjetivo').append(`<option value="${item.codobj}">${item.desobj}</option>`);
                });
            } else {
                console.error("Error al cargar objetivos:", data.mensaje || "Respuesta inválida del servidor");
            }
        },
        error: function (xhr, status, error) {
            console.error("Error AJAX:", status, error);
        }
    });
}



function cargarFarmacias() {
    console.log("Llamando a CRUDobjetivos con opcion=14"); // para confirmar que llega
    $.ajax({
        type: 'GET',
        url: 'CRUDobjetivos',
        data: {opcion: "14"},
        dataType: 'json',
        success: function (data) {
            console.log("Respuesta AJAX:", data);
            $('#selectFarmacia').empty().append('<option value="">Seleccione</option>');
            data.data.forEach(function (item) {
                $('#selectFarmacia').append(`<option value="${item.id}">${item.nombre}</option>`);
            });
        },
        error: function () {
            console.error("Error al cargar las farmacias.");
        }
    });
}




function obtenerMontoAsignado(idObjetivo, idFarmacia) {
    $.ajax({
        url: 'CRUDpesodia',
        type: 'GET',
        dataType: 'json',
        data: {
            opcion: "4",
            idObjetivo: idObjetivo,
            idFarmacia: idFarmacia
        },
        success: function (response) {
            console.log("✅ Monto recibido:", response);
            if (response.resultado === "ok" && response.monto !== undefined) {
                $('#montoAsignado').val(parseFloat(response.monto).toFixed(2));
            } else {
                console.warn("⚠️ No se recibió el monto esperado en la respuesta:", response);
                $('#montoAsignado').val("0.00");
            }
        },
        error: function (xhr, status, error) {
            console.error("❌ Error al consultar el monto asignado.");
            console.error("Status:", status);
            console.error("XHR:", xhr);
            console.error("Error:", error);
            console.log("Respuesta cruda:", xhr.responseText);
        }
    });
}




function cargarDiasConPesos(idObjetivo, idFarmacia) {
    $.ajax({
        url: 'CRUDpesodia',
        type: 'GET',
        data: {
            opcion: 1,
            idObjetivo: idObjetivo,
            idFarmacia: idFarmacia
        },
        success: function (response) {
            $('#diasContainer').empty();

            if (response && response.length > 0) {
                // Ya existen datos
                response.forEach(dia => {
                    $('#diasContainer').append(`
                        <div class="form-group col-md-2">
                            <label>Día ${dia.dia}</label>
                            <input type="number" class="form-control peso-dia" data-dia="${dia.dia}" value="${dia.peso}">
                        </div>
                    `);
                });
            } else {
                // No existen datos: generar campos vacíos
                const diasMes = 30; // Por ahora fijos
                for (let d = 1; d <= diasMes; d++) {
                    $('#diasContainer').append(`
                        <div class="form-group col-md-2">
                            <label>Día ${d}</label>
                            <input type="number" class="form-control peso-dia" data-dia="${d}" value="">
                        </div>
                    `);
                }
            }
        },
        error: function () {
            console.error("Error al cargar los días.");
        }
    });
}

function guardarPesos() {
    const idObjetivo = $('#selectObjetivo').val();
    const idFarmacia = $('#selectFarmacia').val();

    const pesos = [];
    $('.peso-dia').each(function () {
        const dia = $(this).data('dia');
        const peso = parseFloat($(this).val()) || 0;
        pesos.push({dia, peso});
    });

    $.ajax({
        url: 'CRUDpesodia',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            opcion: 2,
            idObjetivo: idObjetivo,
            idFarmacia: idFarmacia,
            pesos: pesos
        }),
        success: function () {
            console.error("Distribución guardada correctamente.");
        },
        error: function () {
            console.error("Error al guardar la distribución.");
        }
    });
}

function obtenerDatosObjetivo(idObjetivo) {
    $.ajax({
        url: 'CRUDobjetivos',
        type: 'GET',
        data: {
            opcion: 5, // Supongamos que esta opción devuelve un objetivo por ID
            idObjetivo: idObjetivo
        },
        success: function (response) {
            // Suponiendo que response.periodo = "2024-06"
            const periodo = response.periodo;
            if (periodo) {
                const partes = periodo.split("-");
                const year = partes[0];
                const month = partes[1];
                const nombreMes = obtenerNombreMes(month);
                $('#infoSeleccion').text(`Objetivo correspondiente a ${nombreMes} ${year}`);
            }
        },
        error: function () {
            console.warn("No se pudo obtener la información del objetivo.");
        }
    });
}

function obtenerNombreMes(numeroMes) {
    const meses = ["enero", "febrero", "marzo", "abril", "mayo", "junio", "julio",
        "agosto", "septiembre", "octubre", "noviembre", "diciembre"];
    const index = parseInt(numeroMes, 10) - 1;
    return meses[index] || "Mes desconocido";
}
