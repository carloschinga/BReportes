$(document).ready(function () {
    let productos;
    let datos;
    let entero;
    let soles;
    let codobj;
    let tabla;
    let farmacias;
    let tipo;
    codobj = localStorage.getItem('codobj');
    $("#atras").on("click", function () {
        localStorage.setItem('codobj', codobj);
        $("#contenido").load("modificarobjetivo.html");
    });

    let listaColumnas = [
        {data: 'sisent'}
    ];
    $.getJSON("CRUDobjetivos?opcion=12&codobj=" + codobj, function (data) {
        let cadena = "<tr><th scope='col'>Farmacia</th>";
        if (data.resultado === "ok") {
            productos = data.data;
            datos = data.detalle;
            farmacias = data.farmacias;
            $.each(productos, function (index, item) {
                cadena = cadena + "<th scope='col'>" + item.despro + "</th>";
            });

            cadena = cadena + "<th scope='col'>Total</th></tr>";
            $('#cabecera').html(cadena);


            $.getJSON("CRUDobjetivos?opcion=7&codobj=" + codobj, function (data) {
                 tipo= data.tipo;
                if (tipo==="p") {
                    $("#cantidad").val("Soles." );
                } else {
                    $("#cantidad").val("Unidades.");
                }



                $.each(productos, function (index, item) {
                    let codpro = item.codpro;

                    let columna = {
                        data: null,
                        render: function (data, type, row) {
                            let siscodbuscado = row.siscod;
                            // Usa el valor capturado de codalm
                            let encontrado = datos.find(registro => registro.siscod === siscodbuscado && registro.codpro === codpro);
                            if (encontrado) {
                                console.log(encontrado);
                                if (tipo==="p") {
                                    return `<div class="input-group input-group-sm" style="min-width: 100px;">
                                <span class="input-group-text">S/.</span>
                                <input class="form-control cant-soles" data-siscod="${row.siscod}" data-codpro="${codpro}" type="number" value="${encontrado.soles}">
                            </div>`;
                                } else {
                                    return `<input class="form-control cant-cantidad" style="min-width: 100px;" data-siscod="${row.siscod}"  data-codpro="${codpro}" type="number" value="${encontrado.entero}">`;
                                }
                            } else {
                                return tipo==="p" ? `<div class="input-group input-group-sm" style="min-width: 100px;">
                                <span class="input-group-text">S/.</span>
                                <input class="form-control cant-soles" data-siscod="${row.siscod}"  data-codpro="${codpro}" type="number" value="">
                            </div>` : `<input class="form-control cant-cantidad" style="min-width: 100px;" data-siscod="${row.siscod}"  data-codpro="${codpro}" type="number" value="">`;
                            }
                        }
                    };
                    listaColumnas.push(columna);
                });
                listaColumnas.push({
                    data: null,
                    render: function (data, type, row) {
                        let totalFila = 0;
                        $(row).find('.cant-soles, .cant-cantidad').each(function () {
                            totalFila += parseFloat($(this).val()) || 0;
                        });
                        return `<span class="total-fila">${totalFila.toFixed(2)}</span>`;
                    }
                });
                tabla = $("#tabla").DataTable({
                    paging: false,
                    fixedHeader: true,
                    searching: false,
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
                    }, data: farmacias
                    ,
                    columns: listaColumnas,
                    initComplete: function (settings, json) {

                        let pie = "<tr><th scope='row'>Total</th>";
                        $.each(productos, function () {
                            pie += "<th class='columna-total'>0</th>";
                        });
                        pie += "<th class='total-final'>0</th></tr>";
                        $('#pie').html(pie);
                        actualizarTotales();
                    }, rowCallback: function (row, data) {
                        let totalFila = 0;

                        // Selecciona los elementos .cant-soles y .cant-cantidad en la fila actual
                        $(row).find('.cant-soles, .cant-cantidad').each(function () {
                            totalFila += parseFloat($(this).val()) || 0;
                        });

                        // Actualiza la columna de total para la fila actual
                        $(row).find('.total-fila').text(totalFila.toFixed(2));
                    }
                });

            });


        } else {
            alert("Error: Problemas con el servidor.");
        }
    });

    function actualizarTotales() {
        let totalFinal = 0;

        // Calcular total por columna
        $('#tabla tfoot th.columna-total').each(function (colIndex) {
            let totalColumna = 0;
            $('#tabla tbody tr').each(function () {
                let valor = $(this).find('td').eq(colIndex + 1).find('.cant-soles, .cant-cantidad').val();
                totalColumna += parseFloat(valor) || 0;
            });
            $(this).text(totalColumna.toFixed(2));
            totalFinal += totalColumna;
        });

        // Actualizar total final en la última celda del pie
        $('#tabla tfoot th.total-final').text(totalFinal.toFixed(2));
    }

// Llama a actualizarTotales cada vez que cambia el valor de una cantidad
    $('#tabla').on('input', '.cant-soles, .cant-cantidad', function () {
        let totalFila = 0;
        $(this).closest('tr').find('.cant-soles, .cant-cantidad').each(function () {
            totalFila += parseFloat($(this).val()) || 0;
        });
        $(this).closest('tr').find('.total-fila').text(totalFila.toFixed(2));
        actualizarTotales();
    });

    function recogerDatos() {
        let json = [];

        let total = 0;
        // Itera sobre cada fila de la tabla
        $('#tabla tbody tr').each(function () {
            let fila = $(this);
            // Itera sobre cada columna de productos
            fila.find('.cant-soles, .cant-cantidad').each(function () {
                let input = $(this);
                let codpro = input.data('codpro');
                let siscod = input.data('siscod');
                let cantidad = input.val();
                if (tipo==="p") {
                    cantidad = parseFloat(cantidad);
                } else {
                    cantidad = parseInt(cantidad);
                }
                // Solo agrega al JSON si hay un valor ingresado
                if (cantidad) {
                    total += cantidad;
                    json.push({
                        codpro: codpro,
                        siscod: siscod,
                        cantidad: cantidad
                    });
                }
            });
        });
        let entra = true;
        let tipo2;
        if (tipo==="p") {
            tipo2 = "soles";
        } else {
            tipo2 = "entero";
        }
        console.log(total);
        if (entra) {

            $.ajax({
                url: "CRUDobjetivos?opcion=13&codobj=" + codobj + "&tipo=" + tipo2,
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(json), // Enviar resultadoJSON como JSON en el cuerpo
                success: function (data) {
                    data = JSON.parse(data);
                    if (data.resultado === "ok") {
                        $("#contenido").load("objetivos.html");
                    } else {
                        if (data.mensaje === "nosession") {
                            $.fn.validarSession();
                        } else {
                            alert("Error al cargar los datos");
                        }
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert("Error de conexion con el servidor");
                }
            });
        } else {
            alert("No se ha distribuido todo el monto");
        }


    }
    $("#siguiente").on("click", function () {
        recogerDatos();
    });
});