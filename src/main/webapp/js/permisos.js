let paginasTraidas = [];
let paginasIntactas = [];
let nuevasPaginas = [];
let paginasEliminadas = [];

function renderPages(paginas) {
  const container = document.getElementById("pagesList");
  // Al cargar nuevas páginas, guardar el estado original
  paginasTraidas = JSON.parse(JSON.stringify(paginas)); // Copia profunda para no modificar la referencia
  paginasIntactas = [];
  nuevasPaginas = [];
  paginasEliminadas = [];

  container.innerHTML = "";
  if (!paginas || paginas.length === 0) {
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
          paginas === null
            ? "Seleccione un permiso"
            : "No hay páginas para mostrar"
        }
      </div>
      <div class="empty-pages-desc" style="color: #64748b; font-size: 0.875rem;">
        ${
          paginas === null
            ? "Elija un permiso del combo superior para visualizar las páginas asociadas."
            : "El permiso seleccionado no tiene páginas asignadas."
        }
      </div>
    `;

    container.appendChild(emptyState);
    return;
  }

  paginas.forEach((pag) => {
    const div = document.createElement("div");
    div.className = "page-option";
    div.innerHTML = `
      <input type="checkbox" class="page-checkbox" id="pag_${pag.codpag}" 
        data-codpag="${pag.codpag}" ${pag.isThis ? "checked" : ""}>
      <label for="pag_${pag.codpag}" style="color:#222;cursor:pointer;">${
      pag.nombpag
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
  const codpag = checkbox.getAttribute("data-codpag");
  const isChecked = checkbox.checked;

  // Buscar la página en la lista original
  const paginaOriginal = paginasTraidas.find((pag) => pag.codpag == codpag);

  if (!paginaOriginal) return;

  // Determinar si es una página nueva o eliminada
  if (paginaOriginal.isThis && !isChecked) {
    // Era checked y ahora no lo está -> página eliminada
    agregarAPaginasEliminadas(paginaOriginal);
  } else if (!paginaOriginal.isThis && isChecked) {
    // No era checked y ahora lo está -> página nueva
    agregarANuevasPaginas(paginaOriginal);
  } else {
    // No hubo cambio o se revirtió al estado original -> página intacta
    agregarAPaginasIntactas(paginaOriginal);
  }
}

function agregarANuevasPaginas(pagina) {
  // Eliminar de otras listas si existe
  paginasEliminadas = paginasEliminadas.filter(
    (p) => p.codpag != pagina.codpag
  );
  paginasIntactas = paginasIntactas.filter((p) => p.codpag != pagina.codpag);

  // Verificar si ya existe en nuevasPaginas
  const existe = nuevasPaginas.some((p) => p.codpag == pagina.codpag);
  if (!existe) {
    nuevasPaginas.push({ ...pagina });
  }

  console.log("Estado actual:", {
    intactas: paginasIntactas.map((p) => p.codpag),
    nuevas: nuevasPaginas.map((p) => p.codpag),
    eliminadas: paginasEliminadas.map((p) => p.codpag),
  });
}

function agregarAPaginasEliminadas(pagina) {
  // Eliminar de otras listas si existe
  nuevasPaginas = nuevasPaginas.filter((p) => p.codpag != pagina.codpag);
  paginasIntactas = paginasIntactas.filter((p) => p.codpag != pagina.codpag);

  // Verificar si ya existe en paginasEliminadas
  const existe = paginasEliminadas.some((p) => p.codpag == pagina.codpag);
  if (!existe) {
    paginasEliminadas.push({ ...pagina });
  }

  console.log("Estado actual:", {
    intactas: paginasIntactas.map((p) => p.codpag),
    nuevas: nuevasPaginas.map((p) => p.codpag),
    eliminadas: paginasEliminadas.map((p) => p.codpag),
  });
}

function agregarAPaginasIntactas(pagina) {
  // Si se revierte a estado original, quitar de nuevas o eliminadas
  nuevasPaginas = nuevasPaginas.filter((p) => p.codpag != pagina.codpag);
  paginasEliminadas = paginasEliminadas.filter(
    (p) => p.codpag != pagina.codpag
  );

  // Verificar si ya existe en paginasIntactas
  const existe = paginasIntactas.some((p) => p.codpag == pagina.codpag);
  if (!existe) {
    paginasIntactas.push({ ...pagina });
  }

  console.log("Estado actual:", {
    intactas: paginasIntactas.map((p) => p.codpag),
    nuevas: nuevasPaginas.map((p) => p.codpag),
    eliminadas: paginasEliminadas.map((p) => p.codpag),
  });
}

// Función para guardar los cambios en el servidor
function guardarCambiosPermisos(permId) {
  if (!permId) {
    Swal.fire({
      icon: "warning",
      title: "Selección requerida",
      text: "Debe seleccionar un permiso.",
    });
    return;
  }

  // Comprobar si hay cambios para enviar
  if (nuevasPaginas.length === 0 && paginasEliminadas.length === 0) {
    Swal.fire({
      icon: "info",
      title: "Sin cambios",
      text: "No se han detectado cambios en la selección de páginas.",
    });
    return;
  }

  // Preparar objeto de datos para enviar
  const datosEnvio = {
    permId: permId,
    nuevasPaginas: nuevasPaginas.map((p) => p.codpag),
    paginasEliminadas: paginasEliminadas.map((p) => p.codpag),
  };

  // Enviar datos al servidor
  fetch("permisosPaginas", {
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

// Modificación de la función cargarPaginasPorPermiso para mantener el permId actual
let permisoSeleccionadoId = null;

function cargarPaginasPorPermiso(permId) {
  permisoSeleccionadoId = permId; // Guardar el ID del permiso seleccionado

  if (!permId) {
    renderPages([]);
    return;
  }

  fetch(`permisosPaginas?opcion=1&permId=${encodeURIComponent(permId)}`)
    .then((response) => response.json())
    .then((data) => {
      if (data.resultado === "ok" && Array.isArray(data.data)) {
        renderPages(data.data);
      } else {
        renderPages([]);
        Swal.fire({
          icon: "error",
          title: "Error",
          text: data.mensaje || "No se pudieron cargar las páginas.",
        });
      }
    })
    .catch(() => {
      renderPages([]);
      Swal.fire({
        icon: "error",
        title: "Error de red",
        text: "No se pudo conectar con el servidor.",
      });
    });
}

// funcion para crear un nuevo permiso
function guardarPermiso() {
  const nombre = document.getElementById("name").value.trim();
  const descripcion = document.getElementById("description").value.trim();

  if (!nombre) {
    Swal.fire({
      icon: "warning",
      title: "Campo obligatorio",
      text: "El nombre es obligatorio.",
    });
    return;
  }

  fetch("permisos", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      nombre: nombre,
      descripcion: descripcion,
    }),
  })
    .then((response) => response.json())
    .then((data) => {
      if (data.resultado === "ok") {
        Swal.fire({
          icon: "success",
          title: "Permiso creado",
          text: data.mensaje || "Permiso creado correctamente.",
          timer: 1500,
          showConfirmButton: false,
        });
        closeModal();
        cargarPermisosCombo();
        // Limpia los campos del modal
        document.getElementById("name").value = "";
        document.getElementById("description").value = "";
      } else {
        Swal.fire({
          icon: "error",
          title: "Error",
          text: data.mensaje || "No se pudo crear el permiso.",
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

// abrir modal de creación de permiso
function openModal() {
  document.getElementById("createModal").style.display = "flex";
  // Asociar evento aquí si el modal/botón se genera dinámicamente
  const btnGuardar = document.getElementById("btn-guardar-permiso");
  if (btnGuardar) {
    btnGuardar.onclick = function (e) {
      e.preventDefault();
      console.log("Guardando permiso...");
      guardarPermiso();
    };
  }
}

// cerrar modal de creación de permiso
function closeModal() {
  document.getElementById("createModal").style.display = "none";
}

// cargar combo de permisos
function cargarPermisosCombo() {
  const select = document.querySelector(".select-roles");
  if (!select) return; // Asegura que el select existe

  select.innerHTML = '<option value="">Seleccione un permiso</option>';

  fetch("permisos?opcion=1")
    .then((response) => response.json())
    .then((data) => {
      if (data.resultado === "ok" && Array.isArray(data.data)) {
        data.data.forEach((permiso) => {
          const option = document.createElement("option");
          option.value = permiso.id;
          option.textContent = permiso.nombre;
          select.appendChild(option);
        });
        // Asocia el evento después de poblar el combo
        select.onchange = function () {
          cargarPaginasPorPermiso(this.value);
        };
      } else {
        Swal.fire({
          icon: "error",
          title: "Error",
          text: data.mensaje || "No se pudieron cargar los permisos.",
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

cargarPermisosCombo();

document
  .getElementById("guardar-mejoras")
  .addEventListener("click", function () {
    guardarCambiosPermisos(permisoSeleccionadoId);
  });
