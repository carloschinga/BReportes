package dao;

/**
 *
 * @author USUARIO
 */
public class FaTiposSPJpaController extends FaProductosJpaController {

    public FaTiposSPJpaController(String empresa) {
        super(empresa);
    }

    public String listar() {
        FaProductosSPJpaController productosDAO = new FaProductosSPJpaController(empresa);

        // Instanciar FaProductosSPJpaController y llamar al método listar con los
        // parámetros
        String resultado = productosDAO.ListarTiposProductos();
        return resultado;
    }
}
