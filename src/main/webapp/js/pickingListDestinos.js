window.onscroll = function () {
    var backButton = document.getElementById('back-button');
    if (document.body.scrollTop > 50 || document.documentElement.scrollTop > 50) {
        backButton.style.top = '20px'; // Nueva altura cuando el usuario hace scroll
    } else {
        backButton.style.top = '100px'; // Altura inicial
    }
};
$(document).ready(function () {
    let Toast = Swal.mixin({
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
    let tipousuario = "";
    $.fn.validarSession = function () {
        $.getJSON("validarsesion", function (data) {
            if (data.resultado === "ok") {
                tipousuario = data.de;
            } else {
                $(location).attr('href', "index.html");
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            $(location).attr('href', "index.html");
        });
    };
    $.fn.validarSession();
    let fechainicio = "";
    let fechafin = "";
    let sec = localStorage.getItem('pik_sec');
    $("#nombre").text("Picking y Despacho Numero: " + sec);


    $('#picking-table').DataTable({
        paging: false,
        searching: true,
        info: false,
        language: {
            decimal: "",
            emptyTable: "No hay datos disponibles",
            info: "Mostrando desde el _START_ al _END_ del total de _TOTAL_ registros",
            infoEmpty: "Mostrando desde el 0 al 0 del total de 0 registros",
            infoFiltered: "(Filtrados del total de _MAX_ registros)",
            thousands: ",",
            lengthMenu: "Mostrar _MENU_ registros por página",
            loadingRecords: "Cargando...",
            processing: "Procesando...",
            search: "Buscar:",
            zeroRecords: "No se encontraron coincidencias",
            paginate: {
                first: "Primero",
                last: "Última",
                next: "Siguiente",
                previous: "Anterior"
            },
            aria: {
                sortAscending: ": activar para ordenar la columna de forma ascendente",
                sortDescending: ": activar para ordenar la columna de forma descendente"
            }
        },
        ajax: {
            url: 'picking', // URL del servidor
            type: 'GET', // Método de la solicitud
            data: function (d) {
                // Enviar los parámetros que necesitas con la solicitud
                d.opcion = 34;//d.opcion = 1;d.opcion = 34;
                d.fechainicio = fechainicio;
                d.fechafin = fechafin;
                d.sec = sec;
            },
            dataSrc: function (json) {
                if (json.resultado === "ok") {
                    return json.data; // Retornar los datos que necesita el DataTable
                } else {
                    if (json.mensaje === "nosession") {
                        $.fn.validarSession();
                    } else {
                        alert("Error con el servidor al cargar los datos de la tabla.");
                        return []; // Retorna un array vacío si no hay resultados
                    }
                }
            }
        },
        columns: [
            {data: 'destino'},
            {data: 'cantidad'},
            {data: 'avance', render: function (data, type, row) {
                    return !isNaN(data) ? data.toFixed(2) + "%" : "0%";
                }
            },
            {data: null, render: function (data, type, row) {
                    let nuevoBoton = $("<button>")
                            .addClass("btn btn-primary establecimiento-btn")
                            .attr("data-codigo", row.codigo)
                            .attr("data-destino", row.destino)
                            .html('<i class="fa fa-edit"></i> ');
                    return nuevoBoton.prop('outerHTML')+(row.estado==="S"?" ✔":" ✖");
                }
            }
        ],
        "createdRow": function (row, data, dataIndex) {
            // Accede a la propiedad 'checkenvio' desde 'data' en lugar de 'row'
            if (data.avance === 100) {
                $(row).addClass('table-warning');
            }
        }
    });
    /*
     $.getJSON("picking", {fechainicio: fechainicio, fechafin: fechafin, sec: sec, opcion: 1}, function (data) {
     if (data.resultado === "ok") {
     let disabled = false;
     let listusu = data.usu;
     for (let item in data.data) {
     
     // Botón principal
     let nuevoBoton = $("<button>")
     .text(data.data[item].destino + " (" + data.data[item].cantidad + " - " + Math.round(data.data[item].avance) + "%)")
     .addClass("btn btn-primary btn-block mb-2 establecimiento-btn list-group-item list-group-item-action")
     .attr("data-codigo", data.data[item].codigo)
     .attr("data-destino", data.data[item].destino);
     /*
     if (data.data[item].completado !== undefined && data.data[item].completado === 'S') {
     disabled = true;
     nuevoBoton.prop("disabled", true);
     }
     
     //if (tipousuario === "l") {
     if (1 !== 1) {
     let groupContainer = $("<div>").addClass("col-12 col-md-6 col-lg-4 mb-3 group-container");
     groupContainer.append(nuevoBoton);
     // Contenedor para el select y su botón
     let selectGroup = $("<div>").addClass("d-flex align-items-center");
     
     // Select
     val = "igual";
     if (data.data[item].operario === undefined) {
     data.data[item].operario = "Usuario";
     val = "";
     }
     let select = $("<select>")
     .html('<option value="' + val + '">' + data.data[item].operario + '</option>')
     .addClass("form-control mr-2");
     $.each(listusu, function (index, option) {
     if (option.usenam !== data.data[item].operario) {
     select.append($('<option>', {
     value: option.usecod,
     text: option.usenam
     }));
     }
     });
     selectGroup.append(select);
     
     // Botón para el select
     let botonSelect = $("<button>")
     .text("Asig.")
     .addClass("btn btn-secondary asing-btn")
     .attr("data-codigo", data.data[item].codigo)
     .on('click', function () {
     let id = String($(this).closest('.group-container').find('select').val());
     if (id === "igual") {
     alert("Este usuario ya esta asignado a este establecimiento");
     } else if (id === '') {
     } else {
     let siscod = String($(this).data("codigo"));
     let sec = localStorage.getItem('pik_sec');
     $('#lista button').prop('disabled', true);
     $('#lista select').prop('disabled', true);
     $.getJSON("picking", {usu: id, siscod: siscod, sec: sec, opcion: 8}, function (data) {
     if (data.resultado === "ok") {
     let url = 'pickingListDestinos.html';
     $("#contenido").load(url);
     } else {
     if (data.mensaje === "nosession") {
     $.fn.validarSession();
     } else {
     alert("Error: " + data.mensaje);
     let url = 'pickingListDestinos.html';
     $("#contenido").load(url);
     }
     }
     }).fail(function (jqXHR, textStatus, errorThrown) {
     alert("error, vuelve a recargar la pagina");
     });
     }
     });
     
     selectGroup.append(botonSelect);
     
     // Añadir el contenedor del select y botón al contenedor del grupo
     groupContainer.append(selectGroup);
     $("#lista").append(groupContainer);
     } else {
     $("#lista").append(nuevoBoton);
     }
     }
     /*
     if (!disabled) {
     let nuevoBoton = $("<button>")
     .text("CERRAR ORDEN TRANSPORTE")
     .addClass("btn btn-danger btn-block mb-2 cerrar-btn");
     $("#lista").append(nuevoBoton);
     if (data.data.length === 0) {
     $("#lista").text("NO SE ENCONTRO DATOS EN ESTA ORDEN");
     }
     }
     
     } else {
     if (data.mensaje === "nosession") {
     $.fn.validarSession();
     } else {
     alert(data.mensaje);
     }
     }
     }).fail(function (jqXHR, textStatus, errorThrown) {
     alert("error, vuelve a recargar la pagina");
     });
     */
    $("#picking-table").on("click", ".establecimiento-btn", function () {
        console.log("codigo:" + $(this).data("codigo"));
        localStorage.setItem('pik_destino', $(this).data("destino"));
        localStorage.setItem('pik_siscod', $(this).data("codigo"));
        localStorage.setItem('scroll', 0);
        $("#contenido").load('pickingDetalle.html');
    });
    $("#back-button").click(function () {
        $("#contenido").load('pickingList.html');
    });
    $("#lista").on("click", ".cerrar-btn", function () {
        let sec = localStorage.getItem('pik_sec');
        if (confirm('¿Estás seguro de que deseas cerrar el picking de esta OT? al confirmar se cerrara todo picking y se tomara como que ya se estara trabajando el translado, Se llenara los reportes de Cajas y aparecera esta OT en los establecimientos destino. Este proceso no es reversible')) {
            $.getJSON("picking", {opcion: 14, orden: sec}, function (data) {
                if (data.resultado === "ok") {
                    $("#contenido").load('pickingListDestinos.html');
                } else {
                    if (data.mensaje === "nosession") {
                        $.fn.validarSession();
                    } else {
                        alert("Error al cerrar la OT");
                    }
                }
            });
        } else {
        }

    });
    $("#txt").on("click", function () {
        $.getJSON("picking", {opcion: 39, pickcod: sec, conf: "N"}, function (data) {
            if (data.resultado === "ok") {
                let jsonData = data.data;
                let fechaActual = new Date();
                let año = fechaActual.getFullYear();
                let mes = ('0' + (fechaActual.getMonth() + 1)).slice(-2);
                let dia = ('0' + fechaActual.getDate()).slice(-2);
                let horas = ('0' + fechaActual.getHours()).slice(-2);
                let minutos = ('0' + fechaActual.getMinutes()).slice(-2);
                let segundos = ('0' + fechaActual.getSeconds()).slice(-2);
                let fechaHoraActual = `${año}${mes}${dia}${horas}${minutos}${segundos}`;

                for (let invnum in jsonData) {
                    let clavePrincipal = jsonData[invnum].siscod;
                    let cadena = jsonData[invnum].texto;
                    let nombreArchivo = `DESCARGOENVIO_${clavePrincipal}_${fechaHoraActual}_${parseInt(invnum)}.txt`;

                    descargartxt(cadena, nombreArchivo);
                }
                Toast.fire({
                    icon: "success",
                    title: "Se generaron los TXT correctamente."
                });
            } else {
                if (data.mensaje === "nosession") {
                    $.fn.validarSession();
                } else if (data.mensaje === "yatxt") {
                    Swal.fire({
                        title: "¿Estas seguro que quieres volver a generar las secuencias?",
                        text: "Este picking ya tiene asignado secuencias, si vuelves a generar se sobreescribiran nuevas secuencias y se reemplazaran las anteriores, esto solo hacer cuando en el LolFar no se cargo correctamente el TXT",
                        icon: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#3085d6",
                        cancelButtonColor: "#d33",
                        confirmButtonText: "Si, cambiar las secuencias"
                    }).then((result) => {
                        if (result.isConfirmed) {
                            $.getJSON("picking", {opcion: 39, pickcod: sec, conf: "S"}, function (data) {
                                if (data.resultado === "ok") {
                                    let jsonData = data.data;
                                    let fechaActual = new Date();
                                    let año = fechaActual.getFullYear();
                                    let mes = ('0' + (fechaActual.getMonth() + 1)).slice(-2);
                                    let dia = ('0' + fechaActual.getDate()).slice(-2);
                                    let horas = ('0' + fechaActual.getHours()).slice(-2);
                                    let minutos = ('0' + fechaActual.getMinutes()).slice(-2);
                                    let segundos = ('0' + fechaActual.getSeconds()).slice(-2);
                                    let fechaHoraActual = `${año}${mes}${dia}${horas}${minutos}${segundos}`;

                                    for (let invnum in jsonData) {
                                        let clavePrincipal = jsonData[invnum].siscod;
                                        let cadena = jsonData[invnum].texto;
                                        let nombreArchivo = `DESCARGOENVIO_${clavePrincipal}_${fechaHoraActual}_${parseInt(invnum)}.txt`;

                                        descargartxt(cadena, nombreArchivo);
                                    }
                                    Toast.fire({
                                        icon: "success",
                                        title: "Se generaron actualizaron las secuencias y los TXT correctamente."
                                    });
                                } else {
                                    if (data.mensaje === "nosession") {
                                        $.fn.validarSession();
                                        
                                    } else {

                                        Toast.fire({
                                            icon: "error",
                                            title: "Error al generar los TXT."
                                        });
                                    }
                                }
                            });
                        }
                    });
                } else {

                    Toast.fire({
                        icon: "error",
                        title: "Error al generar los TXT."
                    });
                }
            }
        });
    });
    function descargartxt(contenido, nombreArchivo) {
        const blob = new Blob([contenido], {type: 'text/plain'});
        // Crear un objeto URL para el blob
        const url = window.URL.createObjectURL(blob);
        // Crear un enlace temporal
        const enlace = document.createElement('a');
        enlace.href = url;
        enlace.download = nombreArchivo;
        // Simular un clic en el enlace
        document.body.appendChild(enlace);
        enlace.click();
        // Limpiar
        window.URL.revokeObjectURL(url);
        document.body.removeChild(enlace);
    }
    ;
});