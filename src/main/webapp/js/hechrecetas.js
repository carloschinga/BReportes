$(document).ready(function () {
    let table;
    // Evento del botón "Filtrar"
    $('#inputFechaInicio').on("change", function () {
        $('#inputFechaFin').val($('#inputFechaInicio').val());
    })
    $('#filterBtn').on('click', function () {
        const fecini = $('#inputFechaInicio').val();
        const fecfin = $('#inputFechaFin').val();
        const actomedico = $('#acto').val();

        if (!fecini && !fecfin && !actomedico) {
            alert('Por favor, ingrese al menos un filtro.');
            return;
        }

        if ($.fn.DataTable.isDataTable('#tabla')) {
            // Si la tabla ya está inicializada, recarga los datos
            table.ajax.reload();
        } else {
            // Configurar DataTable
            table = $('#tabla').DataTable({
                ajax: {
                    url: 'CRUDHechRecetas', // Cambia esto a la URL de tu servlet
                    type: 'POST',
                    data: function (d) {
                        // Añade los filtros de búsqueda
                        d.fecini = $('#inputFechaInicio').val();
                        d.fecfin = $('#inputFechaFin').val();
                        d.actomedico = $('#acto').val();
                        d.opcion = 1;
                    },
                    dataSrc: function (json) {
                        // Devuelve los datos en el formato que DataTables espera
                        return json.data;
                    }
                },
                columns: [
                    {data: 'OrigenAtencion', render: data => data || ''},
                    {data: 'ActoMedico', render: data => data || ''},
                    {data: 'Prefactura', render: data => data || ''},
                    {data: 'FechaAtencion', render: data => data || ''},
                    {data: 'Coditm', render: data => data || ''},
                    {data: 'CodproLolcli', render: data => data || ''},
                    {data: 'CodproLolfar', render: data => data || ''},
                    {data: 'Producto', render: data => data || ''},
                    {data: 'Cantidad', render: data => data || ''},
                    {data: 'Generico', render: data => data || ''},
                    {data: 'IndicProducto', render: data => data || ''},
                    {data: 'Dosis', render: data => data || ''},
                    {data: 'Paciente', render: data => data || ''},
                    {data: 'NumdocId', render: data => data || ''},
                    {data: 'NumHc', render: data => data || ''},
                    {data: 'FechaNac', render: data => data || ''},
                    {data: 'TelfPac', render: data => data || ''},
                    {data: 'PlanAtencion', render: data => data || ''},
                    {data: 'Servicio', render: data => data || ''},
                    {data: 'Medico', render: data => data || ''},
                    {data: 'FechaRegistro', render: data => data || ''},
                    {
                        data: 'check',
                        render: function (data, type, row) {
                            // Si row.isChecked es verdadero, marcar el checkbox
                            const checked = data === "S" ? 'checked' : ''; // Asume que 'isChecked' es un campo booleano

                            return `<div class="container">
                            <input type="checkbox" id="cbx_${row.ActoMedico + "_" + row.Coditm + "_" + row.CodproLolcli}" class="cbx" ${checked} style="display: none;"
                                data-acto=${row.ActoMedico} data-coditm=${row.Coditm} data-codpro="${row.CodproLolcli}">
                            <label for="cbx_${row.ActoMedico + "_" + row.Coditm + "_" + row.CodproLolcli}" class="check">
                            <svg width="18px" height="18px" viewBox="0 0 18 18">
                            <path d="M1,9 L1,3.5 C1,2 2,1 3.5,1 L14.5,1 C16,1 17,2 17,3.5 L17,14.5 C17,16 16,17 14.5,17 L3.5,17 C2,17 1,16 1,14.5 L1,9 Z"></path>
                            <polyline points="1 9 7 14 15 4"></polyline>
                            </svg>
                            </label>
                            </div>`;
                        }

                    }
                ],
                order: [[1, 'asc']],
                responsive: true,
                language: {
                    url: '//cdn.datatables.net/plug-ins/1.13.1/i18n/es-ES.json'
                }
            });
            $('#tabla tbody').on('click', '.cbx', function () {
                let acto = $(this).data("acto");
                let coditm = $(this).data("coditm");
                let codpro = $(this).data("codpro");
                let check = $(this).prop('checked')?"S":"N";
                $(".cbx").prop("disabled", true);
                console.log(acto);
                $.getJSON("CRUDHechRecetas", {opcion: 2, acto: acto, coditm: coditm, codpro: codpro,check:check}, function (data) {
                    if (data.resultado === "ok") {
                        $(".cbx").prop("disabled", false);
                    } else {
                        if (data.mensaje === "nosession") {
                            $.fn.validarSession();
                        } else {
                            alert("Error al actualizar");
                            table.ajax.reload();
                        }
                    }
                });
            });
            // Agregar eventos para mostrar detalles de fila
            $('#tabla tbody').on('click', 'td', function () {
                let tr = $(this).closest('tr');
                let row = table.row(tr);

                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                } else {
                    row.child(format(row.data())).show();
                    tr.addClass('shown');
                }
            });
        }
    });

    // Formato para Row Details
    function format(d) {
        return `
    <table style="width: 100%; border-collapse: collapse;">
        <tr>
            <th style="border: 1px solid #ddd; padding: 8px; text-align: left;">Acto Médico</th>
            <th style="border: 1px solid #ddd; padding: 8px; text-align: left;">Coditm</th>
            <th style="border: 1px solid #ddd; padding: 8px; text-align: left;">Prefactura</th>
            <th style="border: 1px solid #ddd; padding: 8px; text-align: left;">Fecha Atención</th>
            <th style="border: 1px solid #ddd; padding: 8px; text-align: left;">Dia Cod</th>
            <th style="border: 1px solid #ddd; padding: 8px; text-align: left;">Dia Descripción</th>
            <th style="border: 1px solid #ddd; padding: 8px; text-align: left;">Tipo Día</th>
            <th style="border: 1px solid #ddd; padding: 8px; text-align: left;">Prioridad Día</th>
            <th style="border: 1px solid #ddd; padding: 8px; text-align: left;">Fecha Registro</th>
        </tr>
        <tr>
            <td style="border: 1px solid #ddd; padding: 8px;">${d.ActoMedico2 || 'N/A'}</td>
            <td style="border: 1px solid #ddd; padding: 8px;">${d.Coditm2 || 'N/A'}</td>
            <td style="border: 1px solid #ddd; padding: 8px;">${d.Prefactura2 || 'N/A'}</td>
            <td style="border: 1px solid #ddd; padding: 8px;">${d.FechaAtencion2 || 'N/A'}</td>
            <td style="border: 1px solid #ddd; padding: 8px;">${d.DiaCod || 'N/A'}</td>
            <td style="border: 1px solid #ddd; padding: 8px;">${d.Diades || 'N/A'}</td>
            <td style="border: 1px solid #ddd; padding: 8px;">${d.TipDia || 'N/A'}</td>
            <td style="border: 1px solid #ddd; padding: 8px;">${d.PriDia || 'N/A'}</td>
            <td style="border: 1px solid #ddd; padding: 8px;">${d.FechaRegistro2 || 'N/A'}</td>
        </tr>
    </table>`;
    }
});