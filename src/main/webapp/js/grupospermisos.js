let permisosTraidos = [];
let permisosIntactas = [];
let nuevosPermisos = [];
let permisosEliminadas = [];

function llenarComboGrucod() {
  const combo = document.querySelector(".select-roles");
  if (!combo) return;

  combo.innerHTML = '<option value="">Seleccione un grupo</option>';

  fetch("grupospermisos?opcion=1")
    .then((response) => response.json())
    .then((data) => {
      if (data.resultado === "ok" && Array.isArray(data.data)) {
        data.data.forEach((grupo) => {
          const option = document.createElement("option");
          option.value = grupo.codigo;
          option.textContent = grupo.nombre;
          combo.appendChild(option);
        });
        // Asocia el evento después de poblar el combo
        combo.onchange = function () {
          cargarPermisosPorGrupo(this.value);
        };
      } else {
        Swal.fire({
          icon: "error",
          title: "Error",
          text: data.mensaje || "No se pudieron cargar los grupos.",
        });
      }
    })
    .catch(() => {
      Swal.fire({
        icon: "error",
        title: "Error de red",
        text: "No se pudo conectar con el servidor.",
      });
    });
}

let grucodSeleccionado = null;

function cargarPermisosPorGrupo(grucod) {
  grucodSeleccionado = grucod;

  if (!grucod) {
    renderPermisos([]);
    return;
  }

  fetch(`grupospermisos?opcion=2&permId=${encodeURIComponent(permId)}`)
    .then((response) => response.json())
    .then((data) => {
      if (data.resultado === "ok" && Array.isArray(data.data)) {
        renderPermisos(data.data);
      } else {
        renderPermisos([]);
        Swal.fire({
          icon: "error",
          title: "Error",
          text: data.mensaje || "No se pudieron cargar las páginas.",
        });
      }
    })
    .catch(() => {
      renderPermisos([]);
      Swal.fire({
        icon: "error",
        title: "Error de red",
        text: "No se pudo conectar con el servidor.",
      });
    });
}

llenarComboGrucod();

function renderPermisos(permisos) {
  const container = document.getElementById("roles-grid");

  permisosTraidos = JSON.parse(JSON.stringify(permisos));
  permisosIntactas = [];
  nuevosPermisos = [];
  permisosEliminadas = [];

  container.innerHTML = "";
  if (!permisos || permisos.length === 0) {
    // Crear el mensaje de páginas vacías con el nuevo diseño
    const emptyState = document.createElement("div");
    emptyState.className = "empty-pages-info";
    emptyState.style.textAlign = "center";
    emptyState.style.padding = "2rem";
    emptyState.style.color = "#222";

    emptyState.innerHTML = `
      <svg width="48" height="48" fill="none" viewBox="0 0 48 48" style="margin-bottom: 1rem;">
        <rect width="48" height="48" rx="12" fill="#e0e7ef" />
        <path
          d="M16 24h16M16 30h10"
          stroke="#2563eb"
          stroke-width="2"
          stroke-linecap="round"
        />
        <rect
          x="12"
          y="12"
          width="24"
          height="24"
          rx="4"
          stroke="#2563eb"
          stroke-width="2"
          fill="#fff"
        />
      </svg>
      <div class="empty-pages-title" style="font-weight: 600; margin-bottom: 0.5rem; color: #1e293b;">
        ${
          permisos === null
            ? "Seleccione un permiso"
            : "No hay páginas para mostrar"
        }
      </div>
      <div class="empty-pages-desc" style="color: #64748b; font-size: 0.875rem;">
        ${
          permisos === null
            ? "Elija un permiso del combo superior para visualizar las páginas asociadas."
            : "El permiso seleccionado no tiene páginas asignadas."
        }
      </div>
    `;

    container.appendChild(emptyState);
    return;
  }

  permisos.forEach((perm) => {
    const div = document.createElement("div");
    div.className = "role-option";
    div.innerHTML = `
      <input type="checkbox" class="role-checkbox page-checkbox" id="role_${
        perm.codperm
      }" data-codperm="${perm.codperm}" ${perm.isThis ? "checked" : ""}>
      <label for="role_${perm.codperm}" style="color:#222;cursor:pointer;">${
      perm.nombperm
    }</label>
    `;
    container.appendChild(div);
  });

  // Agregar eventos a los checkboxes para detectar cambios
  document.querySelectorAll(".page-checkbox").forEach((checkbox) => {
    checkbox.addEventListener("change", handleCheckboxChange);
  });
}

function handleCheckboxChange(event) {
  const checkbox = event.target;
  const codperm = checkbox.getAttribute("data-codperm");
  const isChecked = checkbox.checked;

  // Buscar la página en la lista original
  const permisoOriginal = permisosTraidos.find((pag) => pag.codperm == codperm);

  if (!permisoOriginal) return;

  // Determinar si es una página nueva o eliminada
  if (permisoOriginal.isThis && !isChecked) {
    // Era checked y ahora no lo está -> página eliminada
    agregarAPermisosEliminados(permisoOriginal);
  } else if (!permisoOriginal.isThis && isChecked) {
    // No era checked y ahora lo está -> página nueva
    agregarANuevosPermisos(permisoOriginal);
  } else {
    // No hubo cambio o se revirtió al estado original -> página intacta
    agregarAPermisosIntactas(permisoOriginal);
  }
}

function agregarANuevosPermisos(permsiso) {
  // Eliminar de otras listas si existe
  permisosEliminadas = permisosEliminadas.filter(
    (p) => p.codperm != permsiso.codperm
  );
  permisosIntactas = permisosIntactas.filter(
    (p) => p.codperm != permsiso.codperm
  );

  // Verificar si ya existe en nuevosPermisos
  const existe = nuevosPermisos.some((p) => p.codperm == permsiso.codperm);
  if (!existe) {
    nuevosPermisos.push({ ...permsiso });
  }

  console.log("Estado actual:", {
    intactas: permisosIntactas.map((p) => p.codperm),
    nuevas: nuevosPermisos.map((p) => p.codperm),
    eliminadas: permisosEliminadas.map((p) => p.codperm),
  });
}

function agregarAPermisosEliminados(permsiso) {
  // Eliminar de otras listas si existe
  nuevosPermisos = nuevosPermisos.filter((p) => p.codperm != permsiso.codperm);
  permisosIntactas = permisosIntactas.filter(
    (p) => p.codperm != permsiso.codperm
  );

  // Verificar si ya existe en permisosEliminadas
  const existe = permisosEliminadas.some((p) => p.codperm == permsiso.codperm);
  if (!existe) {
    permisosEliminadas.push({ ...permsiso });
  }

  console.log("Estado actual:", {
    intactas: permisosIntactas.map((p) => p.codperm),
    nuevas: nuevosPermisos.map((p) => p.codperm),
    eliminadas: permisosEliminadas.map((p) => p.codperm),
  });
}

function agregarAPermisosIntactas(permsiso) {
  // Si se revierte a estado original, quitar de nuevas o eliminadas
  nuevosPermisos = nuevosPermisos.filter((p) => p.codperm != permsiso.codperm);
  permisosEliminadas = permisosEliminadas.filter(
    (p) => p.codperm != permsiso.codperm
  );

  // Verificar si ya existe en permisosIntactas
  const existe = permisosIntactas.some((p) => p.codperm == permsiso.codperm);
  if (!existe) {
    permisosIntactas.push({ ...permsiso });
  }

  console.log("Estado actual:", {
    intactas: permisosIntactas.map((p) => p.codperm),
    nuevas: nuevosPermisos.map((p) => p.codperm),
    eliminadas: permisosEliminadas.map((p) => p.codperm),
  });
}

function guardarCambiosGruposPermisos(grucod) {
  if (!grucod) {
    Swal.fire({
      icon: "warning",
      title: "Selección requerida",
      text: "Debe seleccionar un grupo.",
    });
    return;
  }

  // Comprobar si hay cambios para enviar
  if (nuevosPermisos.length === 0 && permisosEliminadas.length === 0) {
    Swal.fire({
      icon: "info",
      title: "Sin cambios",
      text: "No se han detectado cambios en la selección de permisos.",
    });
    return;
  }

  // Preparar objeto de datos para enviar
  const datosEnvio = {
    grucod: grucod,
    nuevosPermisos: nuevosPermisos.map((p) => p.codpag),
    permisosEliminadas: permisosEliminadas.map((p) => p.codpag),
  };

  // Enviar datos al servidor
  fetch("grupospermisos", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(datosEnvio),
  })
    .then((response) => response.json())
    .then((data) => {
      if (data.resultado === "ok") {
        Swal.fire({
          icon: "success",
          title: "Cambios guardados",
          text: data.mensaje || "Se guardaron los cambios correctamente.",
          timer: 1500,
          showConfirmButton: false,
        });

        // Recargar las páginas para actualizar la vista
        cargarPaginasPorPermiso(permId);
      } else {
        Swal.fire({
          icon: "error",
          title: "Error",
          text: data.mensaje || "No se pudieron guardar los cambios.",
        });
      }
    })
    .catch(() => {
      Swal.fire({
        icon: "error",
        title: "Error de red",
        text: "No se pudo conectar con el servidor.",
      });
    });
}

document.getElementById("btnActualizar").addEventListener("click", function () {
  guardarCambiosGruposPermisos(grucodSeleccionado);
});
