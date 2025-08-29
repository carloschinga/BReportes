$(document).ready(function () {
    var tabla = $("#tabla").DataTable();
    const Swal = window.Swal;
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.addEventListener("mouseenter", Swal.stopTimer);
            toast.addEventListener("mouseleave", Swal.resumeTimer);
        },
    });
    function showSuccess(message) {
        Toast.fire({
            icon: "success",
            title: message,
        });
    }
    function showError(message) {
        Toast.fire({
            icon: "error",
            title: message,
            timer: 7000,
        });
    }
    $.fn.validarSession = function () {
        $.getJSON("validarsesion", function (data) {
            if (data.resultado === "ok") {
                $("#lblUsuario").text(data.logi);
            } else {
                $(location).attr("href", "index.html");
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            $(location).attr("href", "index.html");
        });
    };
    $.fn.validarSession();

    $.fn.listar = function () {
        if (tabla) {
            tabla.destroy();
        }
        tabla = $("#tabla").DataTable({
            paginate: false,
            searching: false,
            fixedHeader: {
                header: true,
                footer: true,
            },
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
                    previous: "Anterior",
                },
                aria: {
                    sortAscending: ": activate to sort column ascending",
                    sortDescending: ": activate to sort column descending",
                },
            },
            ajax: {
                url: "CRUDsigoobje?opcion=1",
            },
            columns: [
                {data: "codobj"},
                {data: "desobj"},
                {data: "feccre"},
                {data: "mesano"},
                {
                    data: null,
                    render: function (data, type, row) {
                        if (row.tipo === "p" || row.tipo === "P") {
                            return "Soles.";
                        } else {
                            return "Unidades.";
                        }
                    },
                },
                {
                    data: "hecAct",
                    render: function (data, type, row) {
                        return row.hecAct ? "Si" : "No";
                    },
                },
                {
                    data: null,
                    render: function (data, type, row) {
                        var btnClass = row.hecAct ? "btn-success" : "btn-outline-secondary";
                        var iconClass = row.hecAct ? "fa-check" : "fa-times";

                        return `
        <div class="d-flex justify-content-start">
            <button class="btn btn-info btn-sm mx-1" data-codobj="${
                                row.codobj
                                }" title="Editar">
                <i class="fa fa-edit"></i>
            </button>
            <button class="btn btn-outline-secondary btn-sm mx-1"
            data-bs-toggle="modal" data-bs-target="#asignarRolesModal" data-codobj="${
                                row.codobj
                                }" title="Roles">
                <i class="fa fa-user"></i>
            </button>
            <button class="btn btn-danger btn-sm mx-1" data-codobj="${
                                row.codobj
                                }" title="Eliminar">
                <i class="fa fa-trash"></i>
            </button>
            <button class="btn ${btnClass} btn-sm mx-1 btn-activar" 
                    data-codobj="${row.codobj}" 
                    title="${row.hecAct ? "Desactivar" : "Activar"}">
                <i class="fa ${iconClass}"></i>
            </button>
        </div>`;
                    },
                },
            ],
        });
    };
    $.fn.listar();

    let currentCodobj = null;
    let rolesAsignados = [];
    let rolesDisponibles = [];
    let metaTotal = 0;
    let montoDeSucursal = 0;
    let rolesOriginales = [];
    let rolesEliminados = [];
    let vtaMesId = 0;
    let mesNombre = "";
    let anio = 0;

    // Evento para mostrar modal con datos completos
    $(document).on(
            "click",
            'button[data-bs-target="#asignarRolesModal"]',
            function () {
                currentCodobj = $(this).data("codobj");
                limpiarPrimerModal();
                cargaInicial();
            }
    );

    function limpiarPrimerModal() {
        rolesAsignados = [];
        rolesDisponibles = [];
        metaTotal = 0;
        montoDeSucursal = 0;
        vtaMesId = 0;
        mesNombre = "";
        anio = 0;

        $(".total-asignado").text(`S/. 0.00`);
        $(".total-porcentaje").text("0.00%");
        $(".restante-monto").text(`S/. 0.00`);
        $(".restante-porcentaje").text("0.00%");
        // limpiar el cuerpo de la tabla
        $("#rolesAsignadosTable").empty();
    }

    function cargaInicial() {
        // Cargar roles disponibles primero
        obtenerRoles()
                .then(() => {
                    // Luego cargar datos del objetivo y sucursales
                    obtenerObjetivo(currentCodobj);
                    return obtenerSucursales();
                })
                .catch((error) => showError(error));
    }

    // funcion para traer datos de objetivo
    function obtenerObjetivo(codobj) {
        $.getJSON("CRUDrolobje", {opcion: 1, codobj: codobj}, function (data) {
            if (data.resultado === "ok") {
                $("#nombreObjetivo").text(data.nombre);
                mesNombre = data.mes;
                $("#objMes").text(data.mes);
                anio = data.ano;
                $("#objAno").text(data.ano);
                if (data.tipo === "p" || data.tipo === "P") {
                    $("#objTipo").text("Soles.");
                } else {
                    $("#objTipo").text("Unidades.");
                }
                $("#objFeccre").text(parsearFecha(data.feccre));
                $("#metaTotal").text(data.montototal);
            } else if (data.mensaje === "nosession") {
                $.fn.validarSession();
            } else {
                alert("Error de conexion con el servidor - Servlet");
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            alert("Error de conexion con el servidor");
        });
    }

    // Función mejorada para obtener roles con Promise y manejo de errores
    function obtenerRoles() {
        return new Promise((resolve, reject) => {
            // Si ya tenemos los roles cargados, resolver inmediatamente
            if (rolesDisponibles.length > 0) {
                return resolve(true);
            }

            $.getJSON("CRUDrolobje", {opcion: 2})
                    .done(function (data) {
                        if (data.resultado === "ok") {
                            // Almacenar roles en el array global
                            rolesDisponibles = data.roles || [];

                            if (rolesDisponibles.length === 0) {
                                showError("No se encontraron roles disponibles");
                                return resolve(false);
                            }

                            return resolve(true);
                        }

                        if (data.mensaje === "nosession") {
                            $.fn.validarSession();
                            return reject("Sesión no válida");
                        } else {
                            showError(data.mensaje || "Error al obtener roles");
                            return reject("Error en la respuesta del servidor");
                        }
                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        const errorMsg = `Error al obtener roles: ${textStatus}`;
                        showError(errorMsg);
                        console.error("Error en obtenerRoles:", textStatus, errorThrown);
                        return reject(errorMsg);
                    });
        });
    }

    // Función para obtener sucursales
    function obtenerSucursales() {
        return new Promise((resolve, reject) => {
            $.getJSON("CRUDrolobje", {opcion: 4, codobj: currentCodobj})
                    .done(function (data) {
                        if (data.resultado === "ok") {
                            const $sucursalSelect = $("#sucursal").empty();

                            if (!data.sucursales || data.sucursales.length === 0) {
                                $sucursalSelect.append(
                                        '<option value="" disabled selected>No hay sucursales disponibles</option>'
                                        );
                                return resolve(false);
                            }

                            $sucursalSelect.append(
                                    '<option value="" disabled selected>Seleccione sucursal</option>'
                                    );

                            data.sucursales.forEach((sucursal) => {
                                $sucursalSelect.append(
                                        `<option value="${sucursal.VtaMesId}">${sucursal.nombre}</option>`
                                        );
                            });

                            return resolve(true);
                        }

                        if (data.mensaje === "nosession") {
                            $.fn.validarSession();
                            return reject("Sesión no válida");
                        } else {
                            showError(data.mensaje || "Error al obtener sucursales");
                            return reject("Error en la respuesta del servidor");
                        }
                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        showError("Error al obtener sucursales");
                        return reject("Error de conexión");
                    });
        });
    }

    // Función para renderizar la tabla de roles (SIN sucursal) con porcentaje editable
    function renderizarRoles() {
        const $tbody = $("#rolesAsignadosTable").empty();

        if (rolesAsignados.length === 0) {
            $tbody.append(`
            <tr>
                <td colspan="7" class="text-center text-muted">
                    No hay roles asignados para esta sucursal
                </td>
            </tr>
        `);
        }

        rolesAsignados.forEach((rol, index) => {
            // Determinar clase CSS según el estado
            let rowClass = "";
            if (rol.estado === "nuevo")
                rowClass = "table-success";
            else if (rol.estado === "modificado")
                rowClass = "table-warning";

            // Determinar atributos del botón de vendedores según el estado
            const vendedoresBtnAttributes =
                    rol.estado !== "nuevo"
                    ? 'data-bs-toggle="modal" data-bs-target="#asignarVendedoresModal"'
                    : "";

            // Calcular monto según porcentaje manual
            const montoSucursal = parseFloat(rol.montoSucursal) || 0;
            const montoCalculado = parseFloat(((rol.porcentaje || 0) / 100 * montoSucursal).toFixed(2));

            // Marcar como modificado si no es nuevo y hay cambios
            if (rol.estado !== "nuevo") {
                const rolOriginal = rolesOriginales.find((r) => r.id === rol.id);
                if (
                        rolOriginal &&
                        (parseFloat(rolOriginal.porcentaje) !== parseFloat(rol.porcentaje) ||
                                parseFloat(rolOriginal.monto) !== montoCalculado)
                        ) {
                    rol.estado = "modificado";
                    rowClass = "table-warning";
                }
            }

            // Actualizar rol con nuevos valores
            rol.monto = montoCalculado;

            const $row = $(`
            <tr data-id="${rol.id}" class="${rowClass}">
                <td>${index + 1}</td>
                <td>
                    <select class="form-select form-select-sm select-rol" data-id="${rol.id}">
                        ${generateRoleOptions(rol.rolId, rol.id)}
                    </select>
                </td>
                <td style="display: none;">
                    <div class="input-group input-group-sm">
                        <span class="input-group-text">S/.</span>
                        <input type="number" class="form-control input-monto" 
                               value="${montoCalculado}" data-id="${rol.id}" readonly>
                    </div>
                </td>
                <td style="display: none;">
                    <div class="input-group input-group-sm">
                        <span class="input-group-text">S/.</span>
                        <input readonly type="number" class="form-control input-monto-sucursal" 
                               value="${rol.montoSucursal}" data-id="${rol.id}">
                    </div>
                </td>
                <td>
                    <div class="input-group input-group-sm">
                        <input type="number" class="form-control input-porcentaje" 
                               value="${rol.porcentaje || 0}" data-id="${rol.id}" min="0" max="100" step="0.01">
                        <span class="input-group-text">%</span>
                    </div>
                </td>
                <td>
                    <button class="btn btn-sm btn-outline-primary btn-vendedores" 
                            data-id="${rol.id}" ${vendedoresBtnAttributes}
                            title="Asignar vendedores">
                        <i class="bi bi-people-fill"></i> 
                        <span class="badge bg-secondary">${rol.vendedores || 0}</span>
                    </button>
                </td>
                <td>
                    <button class="btn btn-sm btn-danger btn-eliminar-rol" data-id="${rol.id}">
                        <i class="fa fa-trash"></i>
                    </button>
                    ${rol.estado === "nuevo" ? '<span class="badge bg-success ms-1">Nuevo</span>' : ""}
                    ${rol.estado === "modificado" ? '<span class="badge bg-warning ms-1">Modificado</span>' : ""}
                </td>
            </tr>
        `);

            $tbody.append($row);
            $row.find(".select-rol").val(rol.rolId);
            $("#metaSucursal").text(rol.montoSucursal);
        });

        // Agregar fila para nuevo rol si hay sucursal seleccionada
        if ($("#sucursal").val()) {
            $tbody.append(`
            <tr id="nuevoRolRow" class="table-success">
                <td colspan="7" class="text-center">
                    <button class="btn btn-sm btn-success" id="agregarRolBtn">
                        <i class="bi bi-plus-circle"></i> Agregar Rol
                    </button>
                </td>
            </tr>
        `);
        }

        $('[data-bs-toggle="tooltip"]').tooltip();
    }

// Actualizar monto al cambiar porcentaje manualmente
    $(document).on("change", ".input-porcentaje", function () {
        const rolId = $(this).data("id");
        const rol = rolesAsignados.find(r => r.id == rolId);
        if (!rol)
            return;

        rol.porcentaje = parseFloat($(this).val()) || 0;

        // Recalcular monto según porcentaje ingresado
        const montoSucursal = parseFloat(rol.montoSucursal) || 0;
        rol.monto = parseFloat(((rol.porcentaje / 100) * montoSucursal).toFixed(2));

        // Marcar como modificado si no es nuevo
        if (rol.estado !== "nuevo") {
            rol.estado = "modificado";
        }

        // Actualizar input del monto visualmente
        $(`input.input-monto[data-id="${rolId}"]`).val(rol.monto);

        // Actualizar totales y barra
        actualizarResumen();
    });


    // Modificar la función generateRoleOptions para excluir roles ya seleccionados
    function generateRoleOptions(selectedId, excludeId = null) {
        let options = '<option value="">Seleccione rol</option>';

        // Obtener IDs de roles ya asignados (excepto el actual)
        const assignedRoleIds = rolesAsignados.filter((rol) => rol.id !== excludeId && rol.rolId).map((rol) => rol.rolId);

        rolesDisponibles.forEach((rol) => {
            // Solo incluir si no está asignado a otro registro o es el seleccionado actual
            if (!assignedRoleIds.includes(rol.rolId.toString()) ||rol.rolId == selectedId) {
                const selected = rol.rolId == selectedId ? "selected" : "";
                options += `<option value="${rol.rolId}" ${selected}>${rol.rolDes}</option>`;
            }
        });

        return options;
    }

    // Función para cargar roles asignados al objetivo
    function cargarRolesAsignados(VtaMesId) {
        $.getJSON(
                "CRUDrolobje",
                {opcion: 3, VtaMesId: VtaMesId},
                function (data) {
                    if (data.resultado === "ok") {
                        const datos = data.roles || [];
                        let $estado = "persistente"; // Marcamos como persistente

                        if (data.mensaje) {
                            $estado = "nuevo"; // Marcamos como nuevo
                        }

                        // Guardar copia de los roles originales
                        rolesOriginales = datos.map((rol) => ({
                                id: rol.CuotVtaRolId || 0,
                                rolId: rol.IdRol || 0,
                                monto: rol.CuotVtaRolMonto || 0,
                                montoSucursal: rol.CuotVtaRolMontoSucursal || 0,
                                porcentaje: rol.CuotVtaRolPorce || 0,
                                vendedores: rol.vendedores || 0,
                                estado: $estado, // Marcamos como persistente
                            }));

                        // Inicializar rolesAsignados con los mismos datos
                        rolesAsignados = JSON.parse(JSON.stringify(rolesOriginales));

                        // Obtener meta total del objetivo
                        metaTotal =
                                parseFloat(
                                        $("#metaTotal")
                                        .text()
                                        .replace(/[^0-9.]/g, "")
                                        ) || 0;

                        renderizarRoles();
                        actualizarResumen();
                    } else {
                        showError("Error al cargar roles asignados");
                    }
                }
        );
    }

    function actualizarResumen() {
        // Sumar montos y porcentajes de rolesAsignados con validación numérica
        const totalAsignado = rolesAsignados.reduce(
                (sum, rol) => sum + (Number(rol.monto) || 0),
                0
                );

        const totalPorcentaje = rolesAsignados.reduce(
                (sum, rol) => sum + (Number(rol.porcentaje) || 0),
                0
                );

        // Obtener montoSucursal único (asumiendo que es igual para todos)
        montoDeSucursal =
                rolesAsignados.length > 0
                ? Number(rolesAsignados[0].montoSucursal) || 0
                : 0;

        // Calcular valores restantes con límites mínimos para evitar negativos
        const restanteMonto = Math.max(montoDeSucursal - totalAsignado, 0);
        const restantePorcentaje = Math.max(100 - totalPorcentaje, 0);

        // Formatear números para UI con locale y decimales consistentes
        const formatMonto = (num) =>
            num.toLocaleString("es-PE", {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2,
            });

        const formatPorcentaje = (num) => num.toFixed(2);

        // Actualizar textos en la interfaz
        $(".total-asignado").text(`S/. ${formatMonto(totalAsignado)}`);
        $(".total-porcentaje").text(`${formatPorcentaje(totalPorcentaje)}%`);
        $(".restante-monto").text(`S/. ${formatMonto(restanteMonto)}`);
        $(".restante-porcentaje").text(`${formatPorcentaje(restantePorcentaje)}%`);

        // Calcular porcentaje para barra de progreso (entre 0 y 100)
        const porcentajeAsignado =
                montoDeSucursal > 0
                ? Math.min((totalAsignado / montoDeSucursal) * 100, 100)
                : 0;

        $(".progress-bar").css("width", `${porcentajeAsignado}%`);

        // Cambiar clases para indicar estado (excedido o dentro de límite)
        if (totalAsignado > montoDeSucursal) {
            $(".restante-monto").addClass("text-danger").removeClass("text-success");
            $(".progress-bar").addClass("bg-danger").removeClass("bg-success");
        } else {
            $(".restante-monto").addClass("text-success").removeClass("text-danger");
            $(".progress-bar").addClass("bg-success").removeClass("bg-danger");
        }
    }

    // Evento para cambiar sucursal
    $(document).on("change", "#sucursal", function () {
        vtaMesId = $(this).val();
        // cargar el select de roles
        obtenerRoles()
                .then(() => {
                    cargarRolesAsignados(vtaMesId);
                })
                .catch((error) => showError("Error al cargar roles"));
    });

    // Evento para cambiar monto sucursal
    $(document).on("input", ".input-monto-sucursal", function () {
        const id = $(this).data("id");
        const montoSucursal = parseFloat($(this).val()) || 0;
        const rol = rolesAsignados.find((r) => r.id == id);

        if (rol) {
            rol.montoSucursal = montoSucursal;
            actualizarResumen();
        }
    });

    $(document).on("click", "#agregarRolBtn", function () {
        const sucursalId = $("#sucursal").val();
        if (!sucursalId) {
            showError("Debe seleccionar una sucursal primero");
            return;
        }

        const nuevoRol = {
            id: Date.now(), // ID temporal (alto número)
            rolId: "",
            monto: 0,
            montoSucursal: montoDeSucursal,
            porcentaje: 0,
            vendedores: 0,
            vtaMesId: sucursalId,
            estado: "nuevo", // Marcamos como nuevo
        };

        rolesAsignados.push(nuevoRol);
        renderizarRoles();
    });

    // Eventos para actualizar valores
    $(document).on("change", ".select-rol", function () {
        const $select = $(this);
        const id = $select.data("id");
        const rolId = $select.val();
        const rol = rolesAsignados.find((r) => r.id == id);

        if (!rolId) {
            showError("Debe seleccionar un rol");
            $select.val(rol.rolId || ""); // Revertir al valor anterior
            return;
        }

        // Verificar si el rol ya está asignado a otro registro
        const rolYaAsignado = rolesAsignados.some(
                (r) => r.id != id && r.rolId == rolId && r.estado !== "eliminado"
        );

        if (rolYaAsignado) {
            showError("Este rol ya está asignado a otro registro");
            $select.val(rol.rolId || ""); // Revertir al valor anterior
            return;
        }

        if (rol) {
            // Solo marcamos como modificado si no es nuevo y el valor cambió
            if (rol.estado !== "nuevo") {
                const rolOriginal = rolesOriginales.find((r) => r.id == id);
                if (rolOriginal && rolOriginal.rolId != rolId) {
                    rol.estado = "modificado";
                }
            }
            rol.rolId = rolId;
            renderizarRoles(); // Recalcular porcentajes y montos
            actualizarResumen();
        }
    });

    $(document).on("click", ".btn-eliminar-rol", function () {
        const id = $(this).data("id");
        const rol = rolesAsignados.find((r) => r.id == id);

        if (!rol)
            return;

        // Verificar si el rol tiene vendedores asignados
        if (rol.vendedores > 0) {
            Swal.fire({
                title: "No se puede eliminar el rol",
                html: `
          <div class="text-center">
            <i class="fa fa-exclamation-triangle text-warning fa-3x mb-3"></i>
            <p>Este rol tiene <strong>${rol.vendedores} vendedor(es)</strong> asignado(s).</p>
            <p>Para poder eliminar el rol, primero debe eliminar todos los vendedores asignados.</p>
          </div>
        `,
                icon: "warning",
                confirmButtonColor: "#3085d6",
                confirmButtonText: "Entendido",
            });
            return;
        }

        const esRegistroLocal = rol.estado === "nuevo";
        const esRegistroPersistente =
                rol.estado === "persistente" || rol.estado === "modificado";

        Swal.fire({
            title: esRegistroLocal
                    ? "Eliminar registro local"
                    : "Eliminar registro de la base de datos",
            html: esRegistroLocal
                    ? "¿Estás seguro de eliminar este registro local? (No se ha guardado en la base de datos)"
                    : "¿Estás seguro de eliminar este registro permanentemente de la base de datos?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#3085d6",
            confirmButtonText: "Sí, eliminar",
            cancelButtonText: "Cancelar",
        }).then((result) => {
            if (result.isConfirmed) {
                // Si es un registro persistente, lo movemos a rolesEliminados
                if (esRegistroPersistente) {
                    rolesEliminados.push(rol.id);
                }

                // Eliminar el rol del array
                rolesAsignados = rolesAsignados.filter((r) => r.id != id);
                renderizarRoles();
                actualizarResumen();

                showSuccess(
                        esRegistroLocal
                        ? "Registro local eliminado"
                        : "Registro marcado para eliminación (se eliminará al guardar cambios)"
                        );
            }
        });
    });

    // Modificar el evento de guardar para mostrar el resumen
    $("#asignarRolesModal").on("click", ".btn-primary", function () {
        
        
        // Validar que todos los selects tengan un rol seleccionado
        let selectsInvalidos = false;
        $(".select-rol").each(function () {
            if (!$(this).val()) {
                $(this).addClass("is-invalid");
                selectsInvalidos = true;
            } else {
                $(this).removeClass("is-invalid");
            }
        });

        if (selectsInvalidos) {
            showError("Todos los roles deben estar seleccionados");
            return;
        }

        // Resto de la validación original...
        if (rolesAsignados.length === 0 && rolesEliminados.length === 0) {
            showError("No hay cambios para guardar");
            return;
        }

        const totalAsignado = rolesAsignados.reduce(
                (sum, rol) => sum + parseFloat(rol.monto),
                0
                );
// Mostrar en consola los valores antes de comparar
        console.log("totalAsignado:", totalAsignado, "metaTotal:", metaTotal, "montoDeSucursal:", montoDeSucursal);
        if (totalAsignado > montoDeSucursal) {
            showError("El total asignado supera la meta del objetivo");
            return;
        }

        mostrarResumenCambios().then((result) => {
            if (result.isConfirmed) {
                guardarRoles();
            }
        });
    });

    // Función para guardar roles en el servidor
    function guardarRoles() {
        // Separar roles según su estado
        const rolesParaAgregar = rolesAsignados
                .filter((rol) => rol.estado === "nuevo" && rol.rolId)
                .map((rol) => ({
                        rolId: rol.rolId,
                        vtaMesId: vtaMesId,
                        monto: rol.monto,
                        porcentaje: rol.porcentaje,
                    }));

        const rolesParaEditar = rolesAsignados
                .filter((rol) => rol.estado === "modificado" && hasChanges(rol))
                .map((rol) => ({
                        id: rol.id,
                        rolId: rol.rolId,
                        vtaMesId: vtaMesId,
                        monto: rol.monto,
                        porcentaje: rol.porcentaje,
                    }));

        const data = {
            rolesParaAgregar: rolesParaAgregar,
            rolesParaEditar: rolesParaEditar,
            rolesEliminados: rolesEliminados, // Enviamos los IDs de roles a eliminar
        };

        console.log({data});

        $.ajax({
            url: "CRUDrolobje?opcion=5",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function (response) {
                if (response.resultado === "ok") {
                    showSuccess("Cambios guardados correctamente");

                    // Resetear estados después de guardar
                    rolesAsignados.forEach((rol) => {
                        if (rol.estado === "nuevo" || rol.estado === "modificado") {
                            rol.estado = "persistente";
                        }
                    });

                    // Actualizar rolesOriginales con los nuevos datos
                    rolesOriginales = JSON.parse(JSON.stringify(rolesAsignados));
                    rolesEliminados = [];

                    renderizarRoles();
                } else {
                    showError(response.mensaje || "Error al guardar");
                }
            },
            error: function () {
                showError("Error de conexión con el servidor");
            },
        });
    }

    function mostrarResumenCambios() {
        const nuevos = rolesAsignados.filter((r) => r.estado === "nuevo").length;
        const modificados = rolesAsignados.filter(
                (r) => r.estado === "modificado"
        ).length;
        const eliminados = rolesEliminados.length;

        let mensaje = "Se aplicarán los siguientes cambios:<br><ul>";

        if (nuevos > 0)
            mensaje += `<li>${nuevos} nuevo(s) rol(es)</li>`;
        if (modificados > 0)
            mensaje += `<li>${modificados} rol(es) modificado(s)</li>`;
        if (eliminados > 0)
            mensaje += `<li>${eliminados} rol(es) eliminado(s)</li>`;

        mensaje += "</ul>¿Deseas continuar, a pesar que tiene <b>"+  $(".total-porcentaje").text()+"</b> ?";

        return Swal.fire({
            title: "Resumen de cambios",
            html: mensaje,
            icon: "info",
            showCancelButton: true,
            confirmButtonText: "Sí, guardar cambios",
            cancelButtonText: "Cancelar",
        });
    }

    function hasChanges(rol) {
        const original = rolesOriginales.find((r) => r.id === rol.id);
        if (!original)
            return false; // Si no hay original, no es modificado (es nuevo)

        return (
                rol.rolId !== original.rolId ||
                parseFloat(rol.monto) !== parseFloat(original.monto) ||
                parseFloat(rol.porcentaje) !== parseFloat(original.porcentaje) ||
                parseFloat(rol.montoSucursal) !== parseFloat(original.montoSucursal)
                );
    }

    $("#tabla").on("click", ".btn-activar", function () {
        const codobj = $(this).data("codobj");

        Swal.fire({
            title: "¿Activar objetivo?",
            html: `¿Estás seguro de activar el objetivo?`,
            icon: "info",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#3085d6",
            confirmButtonText: "Sí, activarr",
            cancelButtonText: "Cancelar",
        }).then((result) => {
            if (result.isConfirmed) {
                $.getJSON(
                        "CRUDsigoobje",
                        {opcion: 14, codobj: codobj},
                        function (data) {
                            if (data.resultado === "ok") {
                                showSuccess("Activado correctamente");
                                tabla.ajax.reload();
                            } else if (data.mensaje === "consulta") {
                                showError("No se encontró el objetivo");
                            } else if (data.mensaje === "nosession") {
                                $.fn.validarSession();
                            } else {
                                alert("Error de conexion con el servidor");
                            }
                        }
                ).fail(function (jqXHR, textStatus, errorThrown) {
                    alert("Error de conexion con el servidor");
                });
            }
        });
    });

    $("#tabla").on("click", ".btn-danger", function () {
        const codobj = $(this).data("codobj");
        // Meter el swal para un preconfinmacion
        Swal.fire({
            title: "¿Eliminar item?",
            html: `¿Estás seguro de eliminar el item?`,
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#3085d6",
            confirmButtonText: "Sí, eliminar",
            cancelButtonText: "Cancelar",
        }).then((result) => {
            if (result.isConfirmed) {
                $.getJSON(
                        "CRUDsigoobje",
                        {opcion: 4, codobj: codobj},
                        function (data) {
                            if (data.resultado === "ok") {
                                showSuccess("Eliminado correctamente");
                                tabla.ajax.reload();
                            } else if (data.mensaje === "repetido") {
                                alert("Ya se encuentra este producto agregado");
                            } else if (data.mensaje === "nosession") {
                                $.fn.validarSession();
                            } else {
                                alert("Error de conexion con el servidor");
                            }
                        }
                ).fail(function (jqXHR, textStatus, errorThrown) {
                    alert("Error de conexion con el servidor");
                });
            }
        });
    });

    $("#tabla").on("click", ".btn-info", function () {
        let codobj = $(this).data("codobj");
        localStorage.setItem("codobj", codobj);

        $("#contenido").load("comercial/sigoobjetivoestablecimientos.html");
    });

    $("#agregarboton").click(function () {
        localStorage.removeItem("codobj");
        $("#contenido").load("comercial/sigomodificarobjetivos.html");
    });

    function parsearFecha(fechaOriginal) {
        // Parsear la fecha
        const fecha = new Date(fechaOriginal.replace(" ", "T")); // Añade 'T' para formato ISO

        // Formatear a legible (ej: "1 de junio de 2025, 19:50")
        const opciones = {
            year: "numeric",
            month: "long",
            day: "numeric",
            hour: "2-digit",
            minute: "2-digit",
        };
        const fechaLegible = fecha.toLocaleDateString("es-ES", opciones);

        return fechaLegible;
        // Resultado: "1 de junio de 2025, 19:50"
    }

    

    //=============================================SEGUNDO MODAL================================================
    //=============================================VENDEDOR================================================
   
    // Variables globales para el segundo modal
    let currentRolId = null;
    let totalDiasMes = 0;
    let meses = {
        Enero: 0,
        Febrero: 1,
        Marzo: 2,
        Abril: 3,
        Mayo: 4,
        Junio: 5,
        Julio: 6,
        Agosto: 7,
        Septiembre: 8,
        Octubre: 9,
        Noviembre: 10,
        Diciembre: 11,
    };

    let vendedoresAsignados = []; // Vendedores actualmente asignados
    let vendedoresOriginales = []; // Copia de los vendedores originales
    let vendedoresEliminados = []; // IDs de vendedores eliminados
    let allVendedores = [];

    // Función para abrir el modal de vendedores
    $(document).on("click", ".btn-vendedores", function () {
        currentRolId = $(this).data("id");
        limpiarModalVendedores();

        // Mostrar el modal
        $("#asignarVendedoresModal").modal("show");

        // Inicializar después de que el modal esté visible
        $("#asignarVendedoresModal").on("shown.bs.modal", function () {
            cargarTodosLosVendedores().then(() => {
                cargarDatosVendedores(currentRolId);
            });
        });
    });

    function limpiarModalVendedores() {
        vendedoresAsignados = [];
        vendedoresOriginales = [];
        vendedoresEliminados = [];

        $("#vendedoresAsignadosTable").empty();
        $("#buscarVendedor").val(null).trigger("change");
        $(".total-asignado-vend").text("S/. 0.00");
        $(".total-porcentaje-vend").text("0.00%");
        $(".restante-monto-vend").text("S/. 0.00");
        $(".restante-porcentaje-vend").text("0.00%");
    }

    // Función para cargar todos los vendedores una sola vez
    function cargarTodosLosVendedores() {
        return $.getJSON("CRUDrolobje?opcion=6", function (data) {
            if (data.resultado === "ok") {
                const sucursal = getCurrentSucursal();
                const sucursalNombre = sucursal.nombre || "Sucursal no especificada";
                console.log(sucursalNombre);
                const palabras = sucursalNombre.split(/\s+/);
                console.log(palabras);
                const ultimaPalabra = palabras[palabras.length - 1].toLowerCase();
                console.log(ultimaPalabra);

                // Filtrar vendedores cuyo VendedDesc contenga la última palabra (insensible a mayúsculas)
                allVendedores = (data.vendedores || []).filter(
                        (v) =>
                    v.VendedDesc && v.VendedDesc.toLowerCase().includes(ultimaPalabra)
                );
            }
        }).fail(() => showError("Error al cargar vendedores"));
    }

    function cargarDatosVendedores(rolId) {
        const rol = getCurrentRol(rolId);
        if (!rol) {
            showError("No se pudo obtener información del rol");
            return;
        }

        // Actualizar cabecera del modal
        actualizarCabeceraModalVendedores(rol);

        // Calcular días del mes
        totalDiasMes = getDiasEnMes(mesNombre, anio);

        // funcion que retorna "2025-01-01" o "2025-01-31" desde "2025-01-12 00:00:00"
        function fechaInicio(fecha) {
            const fechaInicio = new Date(fecha);
            const fechaFin = new Date(fechaInicio);
            fechaFin.setDate(fechaFin.getDate() + 1);
            return fechaInicio.toISOString().split("T")[0];
        }

        // Cargar vendedores asignados
        $.getJSON("CRUDrolobje", {opcion: 7, rolId: rolId}, function (data) {
            if (data.resultado === "ok") {
                vendedoresOriginales = data.vendedores.map((v) => ({
                        id: v.CuotVtaVendId,
                        VendedId: v.VendId,
                        VendedDesc:
                                allVendedores.find((vd) => vd.VendedId == v.VendId)?.VendedDesc ||
                                "",
                        CucuVentVendVendMont: v.CuotVentVendMont,
                        CucuVentVendVendPorc: v.CuotVentVendPorc,
                        Fechinic: fechaInicio(v.FechInic),
                        FechFin: fechaInicio(v.FechFin),
                        estado: "persistente",
                    }));

                vendedoresAsignados = JSON.parse(JSON.stringify(vendedoresOriginales));

                // Agregar una fila vacía para nuevo vendedor
                if (vendedoresAsignados.length === 0) {
                    agregarFilaVendedorVacio();
                }

                renderizarVendedores();
                actualizarResumenVendedores();
            } else {
                showError(data.mensaje || "Error al cargar vendedores");
            }
        }).fail(() => showError("Error de conexión"));
    }

    function agregarFilaVendedorVacio() {
        const nuevoId = Date.now();
        const mesNum = String(meses[mesNombre] + 1).padStart(2, "0");
        const vendedoresNuevos = vendedoresAsignados.filter(
                (v) => v.estado === "nuevo"
        );
        const n = vendedoresNuevos.length;

        let primerDia = `${anio}-${mesNum}-01`;
        let ultimoDia = `${anio}-${mesNum}-${totalDiasMes}`;

        let diaInicio = 1;
        for (let i = 0; i < n; i++) {
            // Calcular el rango de días para cada vendedor
            const diaFin =
                    i === n ? totalDiasMes : Math.floor(((i + 1) * totalDiasMes) / n);

            primerDia = `${anio}-${mesNum}-${String(diaInicio).padStart(2, "0")}`;
            ultimoDia = `${anio}-${mesNum}-${String(diaFin).padStart(2, "0")}`;
            diaInicio = diaFin + 1;
        }

        vendedoresAsignados.push({
            id: nuevoId,
            VendedId: null,
            VendedDesc: "",
            CucuVentVendVendMont: 0,
            CucuVentVendVendPorc: 0,
            Fechinic: primerDia,
            FechFin: ultimoDia,
            estado: "nuevo",
        });

        console.log({vendedoresAsignados});
    }

    // Obtener datos del objetivo actual
    function getCurrentObjetivo() {
        return {
            codobj: currentCodobj,
            nombre: $("#nombreObjetivo").text(),
            mes: $("#objMes").text(),
            ano: $("#objAno").text(),
            tipo: $("#objTipo").text(),
            feccre: $("#objFeccre").text(),
            metaTotal:
                    parseFloat(
                            $("#metaTotal")
                            .text()
                            .replace(/[^0-9.]/g, "")
                            ) || 0,
        };
    }

    // Obtener datos de la sucursal seleccionada
    function getCurrentSucursal() {
        const selectedOption = $("#sucursal option:selected");
        return {
            id: $("#sucursal").val(),
            nombre: selectedOption.text(),
            meta: montoDeSucursal,
        };
    }

    function actualizarCabeceraModalVendedores(rol) {
        const objetivo = getCurrentObjetivo();
        const sucursal = getCurrentSucursal();

        $("#nombreObjetivoVend").text(objetivo.nombre);
        $("#metaTotalVend").text(objetivo.metaTotal.toLocaleString("es-PE"));
        $("#objMesAnoVend").text(`${objetivo.mes} ${objetivo.ano}`);
        $("#nombreSucursalVend").text(sucursal.nombre);
        $("#metaSucursalVend").text(sucursal.meta.toLocaleString("es-PE"));
        $("#nombreRolVend").text(rol.nombre);
        $("#metaRolVend").text(rol.monto).toLocaleString("es-PE");
        $("#porcRolVend").text(`${rol.porcentaje}%`);
    }
    function renderizarVendedores() {
        const $tbody = $("#vendedoresAsignadosTable").empty();
        const mesNum = String(meses[mesNombre] + 1).padStart(2, "0");
        const minDate = `${anio}-${mesNum}-01`;
        const maxDate = `${anio}-${mesNum}-${totalDiasMes}`;

        vendedoresAsignados.forEach((vendedor, index) => {
            const rowClass =
                    vendedor.estado === "nuevo"
                    ? "table-success"
                    : vendedor.estado === "modificado"
                    ? "table-warning"
                    : "";

            const $row = $(`
        <tr data-id="${vendedor.id}" class="${rowClass} align-middle">
          <td class="text-center fw-bold">${index + 1}</td>
          <td>
            <select class="form-select form-select-sm select-vendedor" data-id="${
                    vendedor.id
                    }">
              <option value="">Seleccionar vendedor...</option>
            </select>
          </td>
          <td>
            <input type="date" class="form-control form-control-sm fecha-input" 
                   value="${vendedor.Fechinic}" 
                   data-id="${vendedor.id}" 
                   data-type="inicio"
                   min="${minDate}" max="${maxDate}">
          </td>
          <td>
            <input type="date" class="form-control form-control-sm fecha-input" 
                   value="${vendedor.FechFin}" 
                   data-id="${vendedor.id}" 
                   data-type="fin"
                   min="${minDate}" max="${maxDate}">
          </td>
          <td>
            <div class="input-group input-group-sm">
              <span class="input-group-text">S/.</span>
              <input readonly type="number" class="form-control input-monto-vendedor" 
                     value="${vendedor.CucuVentVendVendMont}" 
                     data-id="${vendedor.id}"
                     min="0" step="0.01" style="background-color: #e9ecef; color: #495057;">
            </div>
          </td>
          <td>
            <div class="input-group input-group-sm">
              <input type="number" class="form-control input-porcentaje-vendedor" 
                     value="${vendedor.CucuVentVendVendPorc}" 
                     data-id="${vendedor.id}"
                     min="0" max="100" step="0.01">
              <span class="input-group-text">%</span>
            </div>
          </td>
          <td class="text-center">
            <button class="btn btn-sm btn-outline-danger btn-eliminar-vendedor" 
                    data-id="${vendedor.id}" title="Eliminar">
              <i class="fa fa-trash"></i>
            </button>
            ${
                    vendedor.estado === "nuevo"
                    ? '<span class="badge bg-success ms-1">Nuevo</span>'
                    : ""
                    }
            ${
                    vendedor.estado === "modificado"
                    ? '<span class="badge bg-warning ms-1">Modificado</span>'
                    : ""
                    }
          </td>
        </tr>
      `);

            $tbody.append($row);

            // Configurar el select2 para el vendedor
            const $select = $row.find(".select-vendedor");

            // Agregar opciones de vendedores
            allVendedores.forEach((v) => {
                $select.append(
                        `<option value="${v.VendedId}">${v.VendedDesc}</option>`
                        );
            });

            // Seleccionar el vendedor actual si existe
            if (vendedor.VendedId) {
                $select.val(vendedor.VendedId);
            }

            // Inicializar select2
            $select.select2({
                placeholder: "Seleccionar vendedor...",
                width: "100%",
                dropdownParent: $("#asignarVendedoresModal"),
            });
        });

        // Agregar fila para nuevo vendedor si no hay filas vacías
        const tieneFilasVacias = vendedoresAsignados.some(
                (v) => !v.VendedId && v.estado === "nuevo"
        );
        if (!tieneFilasVacias) {
            $tbody.append(`
        <tr>
          <td colspan="7" class="text-center py-2">
            <button class="btn btn-sm btn-success" id="btnAgregarFilaVendedor">
              <i class="bi bi-plus-circle"></i> Agregar Fila
            </button>
          </td>
        </tr>
      `);
        }
    }

    // Evento para cambiar vendedor en un select
    $(document).on("change", ".select-vendedor", function () {
        const id = $(this).data("id");
        const vendedorId = $(this).val();
        const vendedor = vendedoresAsignados.find((v) => v.id == id);

        if (!vendedor)
            return;

        // Verificar si el vendedor ya está asignado en otra fila
        const yaAsignado = vendedoresAsignados.some(
                (v) => v.id !== id && v.VendedId == vendedorId && v.estado !== "eliminado"
        );

        if (yaAsignado) {
            showError("Este vendedor ya está asignado en otra fila");
            $(this)
                    .val(vendedor.VendedId || "")
                    .trigger("change");
            return;
        }

        // Actualizar datos del vendedor
        const vendedorInfo = allVendedores.find((v) => v.VendedId == vendedorId);
        vendedor.VendedId = vendedorId;
        vendedor.VendedDesc = vendedorInfo ? vendedorInfo.VendedDesc : "";

        // Si es un vendedor persistente que cambió de vendedor, marcarlo como modificado
        if (vendedor.estado === "persistente") {
            vendedor.estado = "modificado";
        }

        recalcularFechasVendedores();
        renderizarVendedores();
        actualizarResumenVendedores();
    });

    // Evento para agregar nuevo vendedor
    /*
     $(document).on("click", "#btnAgregarVendedor", function () {
     if ($("#buscarVendedor").val() === "") {
     showError("Seleccione un vendedor primero");
     return;
     }
     
     const vendedorId = $("#buscarVendedor").val();
     const vendedorInfo = allVendedores.find((v) => v.VendedId == vendedorId);
     
     // Verificar si el vendedor ya está asignado
     if (
     vendedoresAsignados.some(
     (v) => v.VendedId == vendedorId && v.estado !== "eliminado"
     )
     ) {
     showError("Este vendedor ya está asignado");
     return;
     }
     
     const mesNum = String(meses[mesNombre] + 1).padStart(2, "0");
     const vendedoresNuevos = vendedoresAsignados.filter(
     (v) => v.estado === "nuevo"
     );
     const n = vendedoresNuevos.length;
     
     let primerDia = "";
     let ultimoDia = "";
     
     let diaInicio = 1;
     for (let i = 0; i < n; i++) {
     // Calcular el rango de días para cada vendedor
     const diaFin =
     i === n - 1 ? totalDiasMes : Math.floor(((i + 1) * totalDiasMes) / n);
     
     primerDia = `${anio}-${mesNum}-${String(diaInicio).padStart(2, "0")}`;
     ultimoDia = `${anio}-${mesNum}-${String(diaFin).padStart(2, "0")}`;
     diaInicio = diaFin + 1;
     }
     
     const nuevoVendedor = {
     id: Date.now(), // ID temporal
     VendedId: vendedorId,
     VendedDesc: vendedorInfo.VendedDesc,
     CucuVentVendVendMont: 0,
     CucuVentVendVendPorc: 0,
     Fechinic: primerDia,
     FechFin: ultimoDia,
     estado: "nuevo",
     };
     
     vendedoresAsignados.push(nuevoVendedor);
     renderizarVendedores();
     actualizarResumenVendedores();
     
     // Limpiar selección
     $("#buscarVendedor").val(null).trigger("change");
     });
     */
    $
   
   (document).on("click", ".btn-eliminar-vendedor", function () {
        const id = $(this).data("id");
        const vendedor = vendedoresAsignados.find((v) => v.id == id);

        Swal.fire({
            title: "¿Eliminar vendedor?",
            text: "¿Estás seguro de eliminar este vendedor?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#3085d6",
            confirmButtonText: "Sí, eliminar",
            cancelButtonText: "Cancelar",
        }).then((result) => {
            if (result.isConfirmed) {
                if (vendedor.estado === "persistente") {
                    vendedoresEliminados.push(vendedor.id);
                }

                vendedoresAsignados = vendedoresAsignados.filter((v) => v.id !== id);

                // Si no quedan filas, agregar una vacía
                if (vendedoresAsignados.length === 0) {
                    agregarFilaVendedorVacio();
                }

                recalcularFechasVendedores();
                renderizarVendedores();
                actualizarResumenVendedores();
            }
        });
    });

    // Eventos para actualizar valores de vendedores
    $(document).on("change", ".fecha-input", function () {
        const id = $(this).data("id");
        const tipo = $(this).data("type");
        const fecha = $(this).val();
        const vendedor = vendedoresAsignados.find((v) => v.id == id);

        if (!vendedor)
            return;

        // Guardar el valor original antes de cualquier cambio
        const valorOriginal =
                tipo === "inicio" ? vendedor.Fechinic : vendedor.FechFin;

        // validar si el cambio es el mismo que el original
        if (valorOriginal === fecha) {
            return;
        }

        // Aplicar el cambio temporalmente para la validación
        if (tipo === "inicio") {
            vendedor.Fechinic = fecha;
        } else {
            vendedor.FechFin = fecha;
        }

        // Validar solapamiento antes de calcular
        const validacionFechas = validarSolapamientoFechas();
        if (!validacionFechas.valido) {
            showError(validacionFechas.mensaje);
            // Revertir el cambio
            if (tipo === "inicio") {
                vendedor.Fechinic = valorOriginal;
            } else {
                vendedor.FechFin = valorOriginal;
            }
            $(this).val(valorOriginal); // Restablecer el valor en el input
            return;
        }

        // Si ambas fechas están definidas, calcular monto y porcentaje
        if (vendedor.Fechinic && vendedor.FechFin) {
            const rol = getCurrentRol(currentRolId);
            const calculo = calcularMontoPorcentaje(
                    vendedor.Fechinic,
                    vendedor.FechFin,
                    rol.monto,
                    mesNombre,
                    anio
                    );

            vendedor.CucuVentVendVendMont = calculo.monto;
            vendedor.CucuVentVendVendPorc = calculo.porcentaje;

            // Marcar como modificado solo si realmente hay cambios
            if (vendedor.estado === "persistente" && hasVendedorChanges(vendedor)) {
                vendedor.estado = "modificado";
            }

            renderizarVendedores();
            actualizarResumenVendedores();
        }
    });

    // Evento para cambiar monto o porcentaje
    $(document).on("change",".input-monto-vendedor, .input-porcentaje-vendedor",function () {
                const id = $(this).data("id");
                const vendedor = vendedoresAsignados.find((v) => v.id == id);
                if (!vendedor)
                    return;

                const esMonto = $(this).hasClass("input-monto-vendedor");
                const valor = parseFloat($(this).val()) || 0;

                if (esMonto) {
                    vendedor.CucuVentVendVendMont = valor;
                } else {
                    vendedor.CucuVentVendVendPorc = valor;
                }

                // Marcar como modificado solo si realmente hay cambios
                if (vendedor.estado === "persistente" && hasVendedorChanges(vendedor)) {
                    vendedor.estado = "modificado";
                }

                renderizarVendedores();
                actualizarResumenVendedores();
            }
    );

    function actualizarResumenVendedores() {
        const rol = getCurrentRol(currentRolId);
        if (!rol)
            return;

        const totalAsignado = vendedoresAsignados.reduce(
                (sum, v) => sum + (Number(v.CucuVentVendVendMont) || 0),
                0
                );

        const totalPorcentaje = vendedoresAsignados.reduce(
                (sum, v) => sum + (Number(v.CucuVentVendVendPorc) || 0),
                0
                );

        // Validar si el porcentaje total coincide con el porcentaje del rol
        if (Math.abs(totalPorcentaje - rol.porcentaje) > 0.01) {
            // Permitimos un pequeño margen de error
            // Mantener los porcentajes originales y recalcular los montos
            const vendedoresActivos = vendedoresAsignados.filter(
                    (v) => v.estado !== "eliminado"
            );

            vendedoresActivos.forEach((vendedor) => {
                // Mantener el porcentaje original
                const porcentajeOriginal = vendedor.CucuVentVendVendPorc;
                // Calcular el nuevo monto basado en el porcentaje original y el nuevo metaRolVend
                const nuevoMonto = ((porcentajeOriginal / 100) * rol.monto).toFixed(2);

                vendedor.CucuVentVendVendMont = parseFloat(nuevoMonto);

                // Marcar como modificado solo si realmente hay cambios
                if (vendedor.estado === "persistente" && hasVendedorChanges(vendedor)) {
                    vendedor.estado = "modificado";
                }
            });

            // Actualizar la tabla con los nuevos valores
            renderizarVendedores();
        }

        const restanteMonto = Math.max(rol.monto - totalAsignado, 0);
        const restantePorcentaje = Math.max(100 - totalPorcentaje, 0);

        $(".total-asignado-vend").text(`S/. ${totalAsignado.toFixed(2)}`);
        $(".total-porcentaje-vend").text(`${totalPorcentaje.toFixed(2)}%`);
        $(".restante-monto-vend").text(`S/. ${restanteMonto.toFixed(2)}`);
        $(".restante-porcentaje-vend").text(`${restantePorcentaje.toFixed(2)}%`);

        const porcentajeAsignado =
                rol.monto > 0 ? Math.min((totalAsignado / rol.monto) * 100, 100) : 0;

        $(".progress-bar-vend").css("width", `${porcentajeAsignado}%`);

        if (totalAsignado > rol.monto) {
            $(".restante-monto-vend")
                    .addClass("text-danger")
                    .removeClass("text-success");
            $(".progress-bar-vend").addClass("bg-danger").removeClass("bg-success");
        } else {
            $(".restante-monto-vend")
                    .addClass("text-success")
                    .removeClass("text-danger");
            $(".progress-bar-vend").addClass("bg-success").removeClass("bg-danger");
        }
    }

    // Función para calcular monto y porcentaje basado en fechas
    function calcularMontoPorcentaje(fechaInicio,fechaFin,metaRol,mesNombre,año) {
        const totalDiasMes = getDiasEnMes(mesNombre, año);
        const inicio = new Date(fechaInicio);
        const fin = new Date(fechaFin);

        if (inicio > fin) {
            return {error: "La fecha fin no puede ser anterior a la fecha inicio"};
        }

        const diasAsignados =
                Math.floor((fin - inicio) / (1000 * 60 * 60 * 24)) + 1;
        const valorPorDia = metaRol / totalDiasMes;
        const monto = valorPorDia * diasAsignados;
        const porcentaje = (diasAsignados / totalDiasMes) * 100;

        return {
            monto: parseFloat(monto.toFixed(2)),
            porcentaje: parseFloat(porcentaje.toFixed(2)),
        };
    }

    // Función para obtener días en un mes específico
    function getDiasEnMes(mesNombre, año) {
        const mesIndex = meses[mesNombre];
        if (mesIndex === undefined)
            return 30;
        return new Date(año, mesIndex + 1, 0).getDate();
    }
   // Manejar el cierre del modal de asignación de vendedores
    $("#asignarVendedoresModal").on("hidden.bs.modal", function () {
        // Ya no limpiamos el estado aquí
        // limpiarModalVendedores();

        // Mostrar el modal anterior sin recargar
        $("#asignarRolesModal").modal("show");
        if (vtaMesId) {
            cargarRolesAsignados(vtaMesId);
            actualizarResumen();
        }
    });

    // Obtener datos del rol seleccionado (cuando se hace clic en el botón de vendedores)
    function getCurrentRol(rolId) {
        const rol = rolesAsignados.find((r) => r.id == rolId);
        if (!rol)
            return null;

        const rolInfo = rolesDisponibles.find((r) => r.rolId == rol.rolId);
        return {
            id: rol.id,
            rolId: rol.rolId,
            nombre: rolInfo ? rolInfo.rolDes : "Rol desconocido",
            monto: rol.monto,
            porcentaje: rol.porcentaje,
            montoSucursal: rol.montoSucursal,
            vendedores: rol.vendedores || 0,
        };
    }
    
    // Función async para guardar los cambios
    $("#guardarAsignacionVendedores").click(async function () {
        try {
            
            // Validar antes de guardar
            const validacionExitosa = await validarAntesDeGuardar();
            if (!validacionExitosa)
                return;

            // Mostrar loading
            const $btn = $(this);
            $btn
                    .prop("disabled", true)
                    .html('<i class="fa fa-spinner fa-spin"></i> Guardando...');

            // Guardar cambios
            const resultado = await guardarCambiosVendedores();

            if (resultado.exito) {
                showSuccess("Cambios guardados correctamente");

                // Cerrar modal actual después de un breve retraso para que se vea el mensaje
                setTimeout(() => {
                    $("#asignarVendedoresModal").modal("hide");
                }, 800);
            } else {
                showError(resultado.mensaje || "Error al guardar cambios");
            }
        } catch (error) {
            showError("Error inesperado al guardar: " + error.message);
        } finally {
            // Restaurar botón
            $("#guardarAsignacionVendedores")
                    .prop("disabled", false)
                    .html('<i class="fa fa-save"></i> Guardar');
        }
    });

    // Función para validar antes de guardar
    async function validarAntesDeGuardar() {
        const rol = getCurrentRol(currentRolId);
        if (!rol) {
            showError("No se pudo identificar el rol actual");
            return false;
        }

        // Validar que no se exceda el monto del rol
        const totalAsignado = vendedoresAsignados.reduce(
                (sum, v) => sum + (Number(v.CucuVentVendVendMont) || 0),
                0
                );

        if (parseInt(totalAsignado) > parseInt(rol.monto)) {
            showError("El total asignado a vendedores excede el monto del rol");
            return false;
        }

        // Validar fechas
        for (const vendedor of vendedoresAsignados) {
            if (!vendedor.Fechinic || !vendedor.FechFin) {
                showError("Todos los vendedores deben tener fechas definidas");
                return false;
            }

            if (new Date(vendedor.FechFin) < new Date(vendedor.Fechinic)) {
                showError("La fecha fin no puede ser anterior a la fecha inicio");
                return false;
            }

            // Validar que todos los vendedores tengan un vendedor seleccionado
            /* if (!vendedor.VendedId) {
             showError("Todos los registros deben tener un vendedor asignado");
             return false;
             }*/
        }

        // Validar solapamiento de fechas
        const validacionFechas = validarSolapamientoFechas();
        if (!validacionFechas.valido) {
            showError(validacionFechas.mensaje);
            return false;
        }

        return true;
    }

    // Función async para guardar los cambios en el servidor
    async function guardarCambiosVendedores() {
        const rol = getCurrentRol(currentRolId);

        // Preparar datos para enviar (filtrando filas vacías)
        const vendedoresValidos = vendedoresAsignados.filter((v) => v.VendedId);

        const data = {
            rolId: rol.id,
            vendedoresParaAgregar: vendedoresValidos
                    .filter((v) => v.estado === "nuevo")
                    .map((v) => ({
                            VendedId: parseInt(v.VendedId),
                            CuotVentVendMont: v.CucuVentVendVendMont,
                            CuotVentVendPorc: v.CucuVentVendVendPorc,
                            FechInic: v.Fechinic,
                            FechFin: v.FechFin,
                        })),
            vendedoresParaEditar: vendedoresValidos
                    .filter((v) => v.estado === "modificado")
                    .map((v) => ({
                            CuotVtaVendId: v.id,
                            VendedId: v.VendedId,
                            CuotVentVendMont: v.CucuVentVendVendMont,
                            CuotVentVendPorc: v.CucuVentVendVendPorc,
                            FechInic: v.Fechinic,
                            FechFin: v.FechFin,
                        })),
            vendedoresParaEliminar: vendedoresEliminados,
        };

        console.log({data});

        // Enviar al servidor usando promesa
        return new Promise((resolve, reject) => {
            $.ajax({
                url: "CRUDrolobje?opcion=8",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(data),
                success: function (response) {
                    if (response.resultado === "ok") {
                        // Actualizar contador en el modal principal
                        const rolIndex = rolesAsignados.findIndex((r) => r.id == rol.id);
                        if (rolIndex !== -1) {
                            rolesAsignados[rolIndex].vendedores = vendedoresValidos.length;
                        }
                        vendedoresOriginales = vendedoresValidos.map((v) => ({
                                ...v,
                                estado: "persistente",
                            }));
                        vendedoresAsignados = JSON.parse(
                                JSON.stringify(vendedoresOriginales)
                                );
                        resolve({exito: true});
                    } else {
                        resolve({exito: false, mensaje: response.mensaje});
                    }
                },
                error: function (xhr, status, error) {
                    resolve({exito: false, mensaje: "Error de conexión: " + error});
                },
            });
        });
    }

    function validarSolapamientoFechas() {
        // Obtener todos los rangos de fechas (excepto los eliminados)
        const rangos = vendedoresAsignados
                .filter(
                        (v) => v.estado !== "eliminado" && v.VendedId && v.Fechinic && v.FechFin
                )
                .map((v) => ({
                        id: v.id,
                        inicio: new Date(v.Fechinic),
                        fin: new Date(v.FechFin),
                        vendedor: v.VendedDesc || `Vendedor ${v.VendedId}`,
                    }));

        // Ordenar por fecha de inicio
        rangos.sort((a, b) => a.inicio - b.inicio);

        // Verificar solapamientos
        for (let i = 0; i < rangos.length; i++) {
            for (let j = i + 1; j < rangos.length; j++) {
                // Si el inicio del siguiente está dentro del rango del actual
                if (rangos[j].inicio <= rangos[i].fin) {
                    return {
                        valido: false,
                        mensaje: `Solapamiento detectado: ${rangos[i].vendedor} (${rangos[
                                i
                        ].inicio.toLocaleDateString()} - ${rangos[
                                i
                        ].fin.toLocaleDateString()}) se solapa con ${
                                rangos[j].vendedor
                                } (${rangos[j].inicio.toLocaleDateString()} - ${rangos[
                                j
                        ].fin.toLocaleDateString()})`,
                    };
                }
            }
        }

        return {valido: true};
    }

    function hasVendedorChanges(vendedor) {
        const original = vendedoresOriginales.find((v) => v.id === vendedor.id);
        if (!original)
            return false; // Si no hay original, no es modificado (es nuevo)

        return (
                vendedor.VendedId !== original.VendedId ||
                parseFloat(vendedor.CucuVentVendVendMont) !==
                parseFloat(original.CucuVentVendVendMont) ||
                parseFloat(vendedor.CucuVentVendVendPorc) !==
                parseFloat(original.CucuVentVendVendPorc) ||
                vendedor.Fechinic !== original.Fechinic ||
                vendedor.FechFin !== original.FechFin
                );
    }

    // NUEVO: Función para recalcular fechas de vendedores
    function recalcularFechasVendedores() {
        // 1. Obtener los vendedores persistentes y no persistentes
        const persistentes = vendedoresAsignados.filter(
                (v) => v.estado === "persistente"
        );
        const nuevos = vendedoresAsignados.filter(
                (v) => v.estado === "nuevo" && v.VendedId
        );

        // 2. Determinar el rango ocupado por persistentes
        let diasOcupados = [];
        persistentes.forEach((v) => {
            const inicio = new Date(v.Fechinic);
            const fin = new Date(v.FechFin);
            for (let d = inicio; d <= fin; d.setDate(d.getDate() + 1)) {
                diasOcupados.push(d.toISOString().split("T")[0]);
            }
        });

        // 3. Calcular el rango libre
        const mesNum = String(meses[mesNombre] + 1).padStart(2, "0");
        const totalDias = getDiasEnMes(mesNombre, anio);
        const primerDia = `${anio}-${mesNum}-01`;
        const ultimoDia = `${anio}-${mesNum}-${String(totalDias).padStart(2, "0")}`;

        // Si hay un persistente que cubre todo el mes, bloquear
        if (diasOcupados.length === totalDias) {
            showError(
                    "No se pueden agregar más vendedores porque ya hay uno que cubre todo el mes. Modifique la fecha final del vendedor existente para agregar más."
                    );
            return false;
        }

        // 4. Encontrar el primer día libre después de los persistentes
        let diasLibres = [];
        for (let i = 1; i <= totalDias; i++) {
            const dia = `${anio}-${mesNum}-${String(i).padStart(2, "0")}`;
            if (!diasOcupados.includes(dia))
                diasLibres.push(dia);
        }

        // 5. Repartir los días libres entre los nuevos vendedores
        const n = nuevos.length;
        if (n === 0)
            return true;

        let chunk = Math.floor(diasLibres.length / n);
        let resto = diasLibres.length % n;
        let idx = 0;

        nuevos.forEach((v, i) => {
            let diasParaEste = chunk + (i < resto ? 1 : 0);
            const inicio = diasLibres[idx];
            const fin = diasLibres[idx + diasParaEste - 1];
            v.Fechinic = inicio;
            v.FechFin = fin;
            idx += diasParaEste;
        });

        // 6. Recalcular monto y porcentaje para todos los vendedores con fechas válidas
        const rol = getCurrentRol(currentRolId);
        if (!rol)
            return true;

        vendedoresAsignados.forEach((v) => {
            if (v.Fechinic && v.FechFin) {
                const calculo = calcularMontoPorcentaje(
                        v.Fechinic,
                        v.FechFin,
                        rol.monto,
                        mesNombre,
                        anio
                        );
                v.CucuVentVendVendMont = calculo.monto;
                v.CucuVentVendVendPorc = calculo.porcentaje;
            }
        });

        actualizarResumenVendedores();
        return true;
    }

    // Modifica el evento de agregar fila vendedor:
    $(document).on("click", "#btnAgregarFilaVendedor", function () {
        agregarFilaVendedorVacio();
        const exito = recalcularFechasVendedores();
        if (!exito) {
            // Si no se pudo recalcular (bloqueo), elimina la última fila "nuevo" sin vendedor asignado
            vendedoresAsignados = vendedoresAsignados.filter(
                    (v, i, arr) =>
                !(v.estado === "nuevo" && !v.VendedId && i === arr.length - 1)
            );
            renderizarVendedores();
            actualizarResumenVendedores();
            return;
        }
        renderizarVendedores();
    });

    // También llama a recalcularFechasVendedores() después de eliminar un vendedor nuevo
    /* $(document).on("click", ".btn-eliminar-vendedor", function () {
     // ...código existente...
     // Después de eliminar:
     recalcularFechasVendedores();
     renderizarVendedores();
     });*/
});
