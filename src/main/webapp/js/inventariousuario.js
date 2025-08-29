
$(document).ready(function () {
    let estado = "normal";
    let cambio = false;
    let codinvalm = localStorage.getItem('inventariocodinvalm');
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        }
    });
    function sanitizeInput(value) {
        // Remueve \r\n o cualquier salto de línea
        if (value) {
            return value.replace(/[\r\n]+/g, '');
        } else {
            return "";
        }
    }
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

    $('.select2').select2({
        language: {
            noResults: function () {
                return "No se encontraron resultados.";
            }
        }
    });

    $('.select2').on('select2:open', function (e) {
        setTimeout(function () {
            let searchField = $('.select2-search__field:visible')[0];
            if (searchField) {
                searchField.focus();
            }
        }, 100);
    });

    $.ajax({
        url: 'CRUDUsuarioInventario?opcion=1',
        method: 'GET',
        dataType: 'json',
        success: function (response) {
            if (response.resultado === "ok") {
                $('#select-usuario').empty().append('<option value="" selected disabled>Seleccione un usuario</option>');
                $.each(response.data, function (index, usuario) {
                    var option = new Option(usuario.usenam, usuario.usecod);
                    $(option).data('usedoc', usuario.usedoc);
                    $(option).data('usenam', usuario.usenam);
                    $('#select-usuario').append(option);
                });

                cargarTabla(); // Carga los usuarios en la tabla
            } else if (response.mensaje === "nosession") {
                $.fn.validarSession();
            } else {
                alert('Error al cargar los usuarios');
            }
        },
        error: function () {
            alert('Error al cargar los usuarios');
        }
    });

    function cargarTabla() {
        let tabla = $('#tabla').DataTable({
            paging: false,
            language: {
                decimal: "",
                emptyTable: "No hay datos",
                info: "Mostrando desde el _START_ al _END_ del total de _TOTAL_ registros",
                infoEmpty: "Mostrando desde el 0 al 0 del total de 0 registros",
                infoFiltered: "(Filtrados del total de _MAX_ registros)",
                thousands: ",",
                lengthMenu: "Mostrar _MENU_ registros por página",
                loadingRecords: "Cargando...",
                processing: "Procesando...",
                search: "Buscar:",
                zeroRecords: "No se ha encontrado nada atraves de ese filtrado.",
                paginate: {
                    first: "Primero",
                    last: "Última",
                    next: "Siguiente",
                    previous: "Anterior"
                },
                aria: {
                    sortAscending: ": active para ordenar ascendentemente",
                    sortDescending: ": active para ordenar descendentemente"
                }
            },
            ajax: {
                url: 'CRUDInventarioDetalle?opcion=1',
                type: 'POST',
                data: function (d) {
                    d.codinvalm = codinvalm;
                }
            },
            columns: [
                {data: 'usenam'},
                {data: 'usedoc',
                    render: function (data, type, row) {
                        return data ? data : "";
                    }
                },
                {
                    data: 'codrol',
                    render: function (data, type, row) {
                        return `<select class="form-control form-control-sm" ${estado === "edit" ? "" : "disabled"}>
                                    <option value="O" ${data === "O" ? "selected" : ""}>Operador</option>
                                    <option value="A" ${data === "A" ? "selected" : ""}>Auditor</option>
                                </select>`;
                    }
                },
                {
                    data: 'coddeta',
                    render: function (data, type, row) {
                        if (estado === "edit") {
                            return `<button class="btn btn-primary btn-sm mr-1"><i class="fa fa-trash"></i></button>`;
                        } else {
                            return `<button class="btn btn-primary btn-sm mr-1" disabled><i class="fa fa-trash"></i></button>`;
                        }
                    }
                }
            ]
        });
        $("#agregar").on("click", function () {
            let selectedOption = $('#select-usuario').find(':selected');
            if (selectedOption.val()) {
                let usecod = selectedOption.val();
                let exists = false;

                // Verificar si el usecod ya existe en la tabla
                tabla.rows().every(function () {
                    let data = this.data();
                    if (data.usecod === usecod) {
                        exists = true;
                        return false; // Salir del loop
                    }
                });

                if (exists) {
                    // Mostrar alerta si el usuario ya existe
                    Toast.fire({
                        icon: 'warning',
                        title: 'El usuario ya está agregado'
                    });
                } else {
                    // Agregar nuevo usuario si no está repetido
                    let nombre = selectedOption.data('usenam');
                    let dni = selectedOption.data('usedoc');
                    let rol = 'O'; // Por defecto operador
                    cambio = true;
                    tabla.row.add({
                        usenam: nombre,
                        usedoc: dni,
                        codrol: rol,
                        coddeta: '',
                        usecod: usecod
                    }).draw(false);
                }
            }
        });
        $('#tabla tbody').on('click', '.btn-primary', function () {
            let row = tabla.row($(this).closest('tr'));
            row.remove().draw();
            cambio = true;
        });
        $('#tabla tbody').on('change', 'select', function () {
            var data = tabla.row($(this).closest('tr')).data();
            data.codrol = $(this).val();
            tabla.row($(this).closest('tr')).data(data).draw();
            cambio = true;
        });

        // Guardar funcionalidad

        $("#guardar").on("click", function () {
            let data = [];
            tabla.rows().every(function () {
                let rowData = this.data();
                data.push({
                    rol: rowData.codrol,
                    usecod: rowData.usecod
                });
            });

            let jsonData = JSON.stringify(data);
            console.log('JSON Data:', jsonData);

            $.ajax({
                url: 'CRUDInventarioDetalle?opcion=2&codinvalm=' + codinvalm,
                type: 'POST',
                contentType: 'application/json',
                data: jsonData,
                dataType: 'json',
                success: function (data) {
                    if (data.resultado === "ok") {
                        Toast.fire({
                            icon: 'success',
                            title: 'Datos guardados exitosamente'
                        });
                        cambio = false;
                        estado = "normal";
                        $('#divagregar').hide();
                        $('#guardar').hide();
                        $("#editar").html('<i class="fa fa-edit"></i> Editar');
                        $('#tabla tbody').find('button').prop('disabled', true);
                        $('#tabla tbody').find('select').prop('disabled', true);
                        tabla.ajax.reload();
                    } else {
                        Toast.fire({
                            icon: "error",
                            title: "Error al guardar los datos."
                        });
                    }
                },
                error: function () {
                    Toast.fire({
                        icon: 'error',
                        title: 'Error al guardar los datos'
                    });
                }
            });
        });
        $("#editar").on("click", function () {

            if (estado === "normal") {
                estado = "edit";

                // Mostrar y habilitar select2
                $('#divagregar').show();
                        $('#guardar').show();

                // Habilitar botones de borrar y selects
                $('#tabla tbody').find('button').prop('disabled', false);
                $('#tabla tbody').find('select').prop('disabled', false);
                $("#editar").html('Cancelar');
            } else {
                if (cambio) {
                    Swal.fire({
                        title: "¿Desea Guardar los cambios realizados?",
                        text: "Se han relizado cambios.",
                        icon: "warning",
                        showCancelButton: true,
                        showDenyButton: true,
                        confirmButtonText: "Guardar",
                        denyButtonText: 'No Guardar',
                        cancelButtonText: `Cerrar`
                    }).then((result) => {
                        /* Read more about isConfirmed, isDenied below */
                        if (result.isConfirmed) {
                            let data = [];
                            tabla.rows().every(function () {
                                let rowData = this.data();
                                data.push({
                                    rol: rowData.codrol,
                                    usecod: rowData.usecod
                                });
                            });

                            let jsonData = JSON.stringify(data);
                            console.log('JSON Data:', jsonData);

                            $.ajax({
                                url: 'CRUDInventarioDetalle?opcion=2&codinvalm=' + codinvalm,
                                type: 'POST',
                                contentType: 'application/json',
                                data: jsonData,
                                dataType: 'json',
                                success: function (data) {
                                    if (data.resultado === "ok") {
                                        Toast.fire({
                                            icon: 'success',
                                            title: 'Datos guardados exitosamente'
                                        });
                                        cambio = false;

                                        estado = "normal";
                                        $('#divagregar').hide();
                        $('#guardar').hide();
                                        $("#editar").html('<i class="fa fa-edit"></i> Editar');
                                        $('#tabla tbody').find('button').prop('disabled', true);
                                        $('#tabla tbody').find('select').prop('disabled', true);
                                        tabla.ajax.reload();

                                    } else {
                                        Toast.fire({
                                            icon: "error",
                                            title: "Error al guardar los datos."
                                        });
                                    }
                                },
                                error: function () {
                                    Toast.fire({
                                        icon: 'error',
                                        title: 'Error al guardar los datos'
                                    });
                                }
                            });
                        } else if (result.isDenied) {
                            estado = "normal";
                            $('#divagregar').hide();
                        $('#guardar').hide();
                            $("#editar").html('<i class="fa fa-edit"></i> Editar');
                            $('#tabla tbody').find('button').prop('disabled', true);
                            $('#tabla tbody').find('select').prop('disabled', true);
                            tabla.ajax.reload();
                        }
                    });
                } else {
                    estado = "normal";
                    $('#divagregar').hide();
                        $('#guardar').hide();
                    $("#editar").html('<i class="fa fa-edit"></i> Editar');
                    $('#tabla tbody').find('button').prop('disabled', true);
                    $('#tabla tbody').find('select').prop('disabled', true);
                    tabla.ajax.reload();
                }
            }
        });

        // Agregar funcionalidad
        $("#volver").on("click", function () {
            if (estado === "edit") {
                if (!cambio) {
                    $("#contenido").load('inventarioalmacendetalle.html');
                } else {
                    Swal.fire({
                        title: "¿Desea Guardar los cambios realizados?",
                        text: "Se han relizado cambios.",
                        icon: "warning",
                        showCancelButton: true,
                        showDenyButton: true,
                        confirmButtonText: "Guardar",
                        denyButtonText: 'No Guardar',
                        cancelButtonText: `Cerrar`
                    }).then((result) => {
                        /* Read more about isConfirmed, isDenied below */
                        if (result.isConfirmed) {
                            let data = [];
                            tabla.rows().every(function () {
                                let rowData = this.data();
                                data.push({
                                    rol: rowData.codrol,
                                    usecod: rowData.usecod
                                });
                            });

                            let jsonData = JSON.stringify(data);
                            console.log('JSON Data:', jsonData);

                            $.ajax({
                                url: 'CRUDInventarioDetalle?opcion=2&codinvalm=' + codinvalm,
                                type: 'POST',
                                contentType: 'application/json',
                                data: jsonData,
                                dataType: 'json',
                                success: function (data) {
                                    if (data.resultado === "ok") {
                                        Toast.fire({
                                            icon: 'success',
                                            title: 'Datos guardados exitosamente'
                                        });
                                        cambio = false;
                                        estado = "normal";
                                        $('#tabla tbody').find('button').prop('disabled', true);
                                        $('#tabla tbody').find('select').prop('disabled', true);
                                        $("#contenido").load('inventarioalmacendetalle.html');
                                    } else {
                                        Toast.fire({
                                            icon: "error",
                                            title: "Error al guardar los datos."
                                        });
                                    }
                                },
                                error: function () {
                                    Toast.fire({
                                        icon: 'error',
                                        title: 'Error al guardar los datos'
                                    });
                                }
                            });
                        } else if (result.isDenied) {
                            $("#contenido").load('inventarioalmacendetalle.html');
                        }
                    });
                }
            } else {
                $("#contenido").load('inventarioalmacendetalle.html');
            }
        });
    }


});