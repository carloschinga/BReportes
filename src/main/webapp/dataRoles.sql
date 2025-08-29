INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES ('COMERCIAL', '', 'T', 0, 1);


--necesitamos hasta el 59

INSERT INTO
    grupos_permisos (grucod, pag_id, aigPerm)
VALUES ('supinv', 1, 1),
    ('supinv', 2, 1),
    ('supinv', 3, 1),
    ('supinv', 4, 1),
    ('supinv', 5, 1),
    ('supinv', 6, 1),
    ('supinv', 7, 1),
    ('supinv', 8, 1),
    ('supinv', 9, 1),
    ('supinv', 10, 1),
    ('supinv', 11, 1),
    ('supinv', 12, 1),
    ('supinv', 13, 1),
    ('supinv', 14, 1),
    ('supinv', 15, 1),
    ('supinv', 16, 1),
    ('supinv', 17, 1),
    ('supinv', 18, 1),
    ('supinv', 19, 1),
    ('supinv', 20, 1),
    ('supinv', 21, 1),
    ('supinv', 22, 1),
    ('supinv', 23, 1),
    ('supinv', 24, 1),
    ('supinv', 25, 1),
    ('supinv', 26, 1),
    ('supinv', 27, 1),
    ('supinv', 28, 1),
    ('supinv', 29, 1),
    ('supinv', 30, 1),
    ('supinv', 31, 1),
    ('supinv', 32, 1),
    ('supinv', 33, 1),
    ('supinv', 34, 1),
    ('supinv', 35, 1),
    ('supinv', 36, 1),
    ('supinv', 37, 1),
    ('supinv', 38, 1),
    ('supinv', 39, 1),
    ('supinv', 40, 1),
    ('supinv', 41, 1),
    ('supinv', 42, 1),
    ('supinv', 43, 1),
    ('supinv', 44, 1),
    ('supinv', 45, 1),
    ('supinv', 46, 1),
    ('supinv', 47, 1),
    ('supinv', 48, 1),
    ('supinv', 49, 1),
    ('supinv', 50, 1),
    ('supinv', 51, 1),
    ('supinv', 52, 1),
    ('supinv', 53, 1),
    ('supinv', 54, 1),
    ('supinv', 55, 1),
    ('supinv', 56, 1),
    ('supinv', 57, 1),
    ('supinv', 58, 1),
    ('supinv', 59, 1);

--GESTION DE ALMACENES
INSERT INTO grupos_permisos (grucod, pag_id, aigPerm)

--LOGISTICA
---> Rotacion de Producto -
---> Gestion de Pedido
---> Objetivos
---> Otros Objetivos
--CLINICA
----> seguimiento receta
----> list de Clasi
----> petitorio por ess.
-- DISTRIBUCION Y TRANPORTE
----> disrtibucion
----> crear pedido distribucion
----> lista de picking
----> sincroni, picking - mov
----> modificar distribucion
-- GESTION DE ALMACENES
----> consliliacion y pagos virl
----> Gestion almacenes
----> Almacenamiento
----> Actualizacion de inventario
----> Consulatr stocks
-- reportes
----> U. Bartolitos
----> .U. C-Barolito
----> Parma Ba

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES ('LOGISTICA', '', 'T', 0, 1);

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Rotacion de Productos',
        'dashboard.html',
        'M',
        1,
        0
    );

-- solo debemos insertar las cabeceras, osea el nombre
INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Gestion de pedido',
        'comprasreposicion.html',
        'M',
        1,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Objetivos',
        'objetivos.html',
        'M',
        1,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Objetivos',
        'otrosobjetivos.html',
        'M',
        1,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES ('CLINICIA', '', 'T', 0, 1);

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Seguimiento Receta',
        'clinica/receta.html',
        'M',
        2,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Petitorio Medico',
        'petitorio.html',
        'M',
        2,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Lista de Clasificacion',
        'petitorioTecnico.html',
        'M',
        2,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Petitorio Por Especialidad',
        'petitorioEspecial.html',
        'M',
        2,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'DISTRIBUCION y TRANSPORTE',
        '',
        'T',
        0,
        1
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Distribución',
        'distribucion.html',
        'M',
        3,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Crear Pedido Distribucion',
        'pickinglistagrupado.html',
        'M',
        3,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Lista de Picking',
        'pickingList.html',
        'M',
        3,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Sincroni. Picking - mov',
        'matchpickmov.html',
        'M',
        3,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Modificar Distribucion',
        'matchpickmov.html',
        'M',
        3,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Modificar Distribucion',
        'matchpickmov.html',
        'M',
        3,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'MODIFICAR DISTRIBUCION',
        '',
        'T',
        3,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Gestion Aplic. Fracción',
        'aplicFrac.html',
        'M',
        4,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'RESTRICCIONES',
        'restricciones.html',
        'M',
        4,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'GESTION SUBLOTES',
        'inmobilizar.html',
        'M',
        4,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'GESTION DE ALMACENES',
        '',
        'T',
        0,
        1
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Conciliacion Pagos Virtua.',
        'pagosvirtuales.html',
        'M',
        5,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Gestion Almacenes',
        'almacenes_bartolito.html',
        'M',
        5,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Almacenamiento',
        'almacenamiento.html',
        'M',
        5,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Actualizacion de inventario',
        'sincronizacion.html',
        'M',
        5,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Consultar Stocks',
        'btnConsultarStock.html',
        'M',
        5,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Recepcion Picking',
        'recepcioncajas.html',
        'M',
        5,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Inventario',
        'inventario.html',
        'M',
        5,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES ('REPORTES', '', 'T', 0, 1);

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Etiquetas de Embalaje-nuevo',
        'reporteetiquetasenvalajenuevo.html',
        'M',
        6,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Etiquetas de Embalaje',
        'reporteEtiquetasEmbalaje.html',
        'M',
        6,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Tira Productos',
        'ReporteProductosPrecio.html',
        'M',
        6,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Restricciones',
        'reporterestricciones.html',
        'M',
        6,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Tiempo Picking',
        'reportetiemposrecepcion.html',
        'M',
        6,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Tiempo Recepcion',
        'reportetiempopicking.html',
        'M',
        6,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Picking - Bultos',
        'reporteOrdenCaja.html',
        'M',
        6,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Productos',
        'repQRProductos.html',
        'M',
        6,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Petitorio',
        'reportepetitorio.html',
        'M',
        6,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Faltante excedente',
        'reportefaltanteexcedente.html',
        'M',
        6,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Registro Productos por orden',
        'reporteordentrabajoserdoc.html',
        'M',
        6,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES ('FACTURACION', '', 'T', 0, 1);

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Facturación',
        'facturacion.html',
        'M',
        7,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES ('INVENTARIOS', '', 'T', 0, 1);

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Productos',
        'productos.html',
        'M',
        8,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Almacenes',
        'almacenes_bartolito.html',
        'M',
        8,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES ('CAJAS', '', 'T', 0, 1);

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Cajas',
        'cajas.html',
        'M',
        9,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Apertura de Caja',
        'aperturacaja.html',
        'M',
        9,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Cierre de Caja',
        'cierrecaja.html',
        'M',
        9,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES ('REGISTROS', '', 'T', 0, 1);

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Kardex',
        'kardex.html',
        'M',
        10,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES ('OPCIONES', '', 'T', 0, 1);

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Usuarios Bartolito',
        'usuariosbartolito.html',
        'M',
        11,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Usuarios Inventario',
        'usuarioinventario.html',
        'M',
        11,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Param. Bartolito',
        'opciones.html',
        'M',
        11,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES ('PERMISOS', '', 'T', 0, 1);

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Gestion de Permisos',
        'permisos.html',
        'M',
        12,
        0
    );

INSERT INTO
    paginas (
        pag_nombre,
        pag_ruta,
        tip_menu,
        pag_padre,
        iclass
    )
VALUES (
        'Gestion de Grupos',
        'grupospermisos.html',
        'M',
        12,
        0
    );

-- Tabla paginas
CREATE TABLE paginas (
    pag_id INT PRIMARY KEY IDENTITY (1, 1),
    pag_nombre VARCHAR(100) NOT NULL,
    pag_ruta VARCHAR(255) NOT NULL,
    tip_menu CHAR(1) not null,
    pag_padre int not null,
    iclass int not null,
    created_at DATETIME DEFAULT GETDATE ()
);

--- crear una tabla grupos_permisos para sql server
CREATE TABLE grupos_permisos (
    grucod VARCHAR(20) NOT NULL,
    pag_id INT NOT NULL,
    asigPerm BIT NOT NULL
);