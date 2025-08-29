/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.google.gson.Gson;
import dao.AlmacenesBartolitoDetalleJpaController;
import dao.FaCajasJpaController;
import dao.FaMovimientosDetalleJpaController;
import dao.FaMovimientosJpaController;
import dao.FaOrdenTrabajoJpaController;
import dao.PickingCajasJpaController;
import dao.PickingDetalleJpaController;
import dao.PickingJpaController;
import dao.PickinglistJpaController;
import dao.ReposicionRecepcionJpaController;
import dto.FaCajas;
import dto.FaCajasPK;
import dto.FaMovimientosDetalle;
import dto.FaOrdenTrabajo;
import dto.FaOrdenTrabajoPK;
import dto.PickingCajas;
import dto.PickingDetalle;
import dto.Pickinglist;
import dto.ReposicionRecepcion;
import dto.ReposicionRecepcionPK;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "Picking", urlPatterns = {"/picking"})
public class Picking extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try {
                HttpSession session = request.getSession(true);
                Object emprObj = session.getAttribute("empr");
                if (emprObj != null) {
                    String empr = emprObj.toString();
                    String opcion = request.getParameter("opcion");
                    switch (opcion) {
                        case "1":  //Lista los establecimientos para el picking
                            FaMovimientosJpaController MovDao = new FaMovimientosJpaController(empr);
                            String de = session.getAttribute("de").toString();
                            String fec_ini = request.getParameter("fechainicio");
                            String fec_fin = request.getParameter("fechafin");
                            String sec = request.getParameter("sec");
                            String codalminv = session.getAttribute("codalminv").toString();
                            if (de.equals("l")) {
                                out.print("{\"resultado\":\"ok\",\"data\":" + MovDao.listarPickingListDestinos_tot(sec, fec_ini, fec_fin, "", codalminv) + "}");

                            } else {
                                //String usecod = session.getAttribute("codbar").toString();
                                String usecod = "";
                                out.print("{\"resultado\":\"ok\",\"data\":" + MovDao.listarPickingListDestinos_tot(sec, fec_ini, fec_fin, usecod, codalminv) + "}");
                            }
                            break;
                        case "2": //Lista las ordenes de transporte por fechas
                            FaOrdenTrabajoJpaController pikDao = new FaOrdenTrabajoJpaController(empr);
                            fec_ini = request.getParameter("fechainicio");
                            fec_fin = request.getParameter("fechafin");
                            codalminv = session.getAttribute("codalminv").toString();
                            out.print("{\"resultado\":\"ok\",\"data\":" + pikDao.listarfechas(fec_ini, fec_fin, codalminv) + "}");
                            
                            break;
                        case "3":
                            MovDao = new FaMovimientosJpaController(empr);
                            fec_ini = request.getParameter("fechainicio");
                            fec_fin = request.getParameter("fechafin");
                            sec = request.getParameter("sec");
                            String orden = request.getParameter("orden");
                            int siscod = Integer.parseInt(request.getParameter("siscod"));
                            out.print("{\"resultado\":\"ok\",\"fecha\":\""
                                    + Timestamp.valueOf(LocalDateTime.now()).toString() + "\",\"data\":" + MovDao.listarPickingListDestinosDetTodo(sec, fec_ini, fec_fin, siscod, orden) + "}");
                            
                            break;
                        case "4"://lista un producto en la orden
                            siscod = Integer.parseInt(request.getParameter("siscod"));
                            String codpro = request.getParameter("codpro");
                            String codlot = request.getParameter("codlot");
                            int secuencia = Integer.parseInt(request.getParameter("secuencia"));
                            MovDao = new FaMovimientosJpaController(empr);
                            FaMovimientosDetalle Mov = MovDao.findByInvnumCodproCodlot(secuencia, codpro, codlot);
                            FaCajasJpaController cajadao = new FaCajasJpaController(empr);

                            out.print("{\"resultado\":\"ok\",\"data\":" + MovDao.listarPickingListDet(siscod, codpro, codlot, secuencia) + ",\"fecha\":\""
                                    + Timestamp.valueOf(LocalDateTime.now()).toString() + "\",\"cajas\":"
                                    + cajadao.listarJson(Mov.getFaMovimientosDetallePK().getInvnum(), Mov.getFaMovimientosDetallePK().getNumitm()) + "}");
                            
                            break;
                        case "5": // check para el codigo de barras
                            codpro = request.getParameter("codpro");
                            codlot = request.getParameter("codlot");
                            secuencia = Integer.parseInt(request.getParameter("secuencia"));
                            MovDao = new FaMovimientosJpaController(empr);
                            Mov = MovDao.findByInvnumCodproCodlot(secuencia, codpro, codlot);
                            de = session.getAttribute("de").toString();
                            //String resultado = ordendao.verificarCompletadoInvnum(secuencia);
                            String resultado = "N";
                            FaMovimientosDetalleJpaController MovDetDao = new FaMovimientosDetalleJpaController(empr);
                            if (!"S".equals(resultado)) {
                                if (de.equals("b")) {
                                    String codusu = session.getAttribute("codbar").toString();
                                    Mov.setUsuean13(Integer.parseInt(codusu));
                                } else {
                                    String codusu = session.getAttribute("codi").toString();
                                    Mov.setUsuean13adm(Integer.parseInt(codusu));
                                }
                                if (Mov.getChkean13() == null || Mov.getChkean13().equals("N")) {
                                    Mov.setChkean13("S");
                                } else {
                                    Mov.setChkean13("N");
                                    Mov.setUsuean13(null);
                                    Mov.setUsuean13adm(null);
                                }
                                MovDetDao.edit(Mov);
                                out.print("{\"resultado\":\"ok\"}");
                                
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"yaenviado\"}");
                            }
                            break;
                        case "6"://check picking
                            codpro = request.getParameter("codpro");
                            codlot = request.getParameter("codlot");
                            String fecha = request.getParameter("fecha");
                            int cante = Integer.parseInt(request.getParameter("cante"));
                            int cantf = Integer.parseInt(request.getParameter("cantf"));
                            secuencia = Integer.parseInt(request.getParameter("secuencia"));
                            MovDao = new FaMovimientosJpaController(empr);
                            Mov = MovDao.findByInvnumCodproCodlot(secuencia, codpro, codlot);
                            //resultado = ordendao.verificarCompletadoInvnum(secuencia);
                            resultado = "N";
                            if (!"S".equals(resultado)) {
                                if (Mov.getChkpick() == null || Mov.getChkpick().equals("N")) {

                                    Mov.setChkpick("S");
                                    Mov.setFecfpick(Timestamp.valueOf(LocalDateTime.now()));
                                    Mov.setFecipick(Timestamp.valueOf(fecha));
                                    Mov.setCante(cante);
                                    Mov.setCantf(cantf);

                                    de = session.getAttribute("de").toString();
                                    if (de.equals("b")) {
                                        String codusu = session.getAttribute("codbar").toString();
                                        Mov.setUsupick(Integer.parseInt(codusu));
                                    } else {
                                        String codusu = session.getAttribute("codi").toString();
                                        Mov.setUsupickadm(Integer.parseInt(codusu));
                                    }

                                    StringBuilder sb = new StringBuilder();
                                    try (BufferedReader reader = request.getReader()) {
                                        String line;
                                        while ((line = reader.readLine()) != null) {
                                            sb.append(line);
                                        }
                                    }

                                    // Parsear el contenido del cuerpo como un JSONArray
                                    String requestBody = sb.toString();
                                    JSONArray jsonArray = new JSONArray(requestBody);

                                    // Iterar las cajas en el JSONArray
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String caja = jsonObject.getString("caja");
                                        String cantE = jsonObject.getString("cantE");
                                        String cantF = jsonObject.getString("cantF");
                                        FaCajasPK cajapk = new FaCajasPK(Mov.getFaMovimientosDetallePK().getInvnum(), Mov.getFaMovimientosDetallePK().getNumitm(), caja);
                                        FaCajas cajaobj = new FaCajas(cajapk);
                                        if (!cantE.equals("")) {
                                            cajaobj.setCante(Integer.parseInt(cantE));
                                        } else {
                                            cajaobj.setCante(null);
                                        }
                                        if (!cantF.equals("")) {
                                            cajaobj.setCantf(Integer.parseInt(cantF));
                                        } else {
                                            cajaobj.setCantf(null);
                                        }
                                        cajadao = new FaCajasJpaController(empr);
                                        cajadao.create(cajaobj);
                                        
                                    }
                                } else {
                                    Mov.setChkpick("N");
                                    Mov.setFecfpick(null);
                                    Mov.setFecipick(null);
                                    Mov.setCaja(null);
                                    Mov.setCante(cante);
                                    Mov.setCantf(cantf);
                                    Mov.setUsupick(null);
                                    Mov.setUsupickadm(null);
                                    Mov.setPickmod("S");
                                    de = session.getAttribute("de").toString();
                                    if (de.equals("b")) {
                                        String codusu = session.getAttribute("codbar").toString();
                                        Mov.setUsudelpick(Integer.parseInt(codusu));
                                    } else {
                                        String codusu = session.getAttribute("codi").toString();
                                        Mov.setUsudelpickadm(Integer.parseInt(codusu));
                                    }
                                    cajadao = new FaCajasJpaController(empr);
                                    cajadao.EliminarCajas(Mov.getFaMovimientosDetallePK().getInvnum(), Mov.getFaMovimientosDetallePK().getNumitm());
                                    
                                }
                                MovDetDao = new FaMovimientosDetalleJpaController(empr);
                                MovDetDao.edit(Mov);
                                
                                out.print("{\"resultado\":\"ok\",\"fecha\":\""
                                        + Timestamp.valueOf(LocalDateTime.now()).toString() + "\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"yaenviado\"}");
                            }
                            break;
                        case "7": //Agregar guias transferencias(picking)
                            String usu = session.getAttribute("codi").toString();
                            StringBuilder sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            String body = sb.toString();
                            Gson gson = new Gson();

                            RequestData requestData = gson.fromJson(body, RequestData.class);
                            pikDao = new FaOrdenTrabajoJpaController(empr);
                            int codpik = pikDao.obtenerCodpikMax() + 1;
                            if (codpik != 0) {
                                for (int i = 0; i < requestData.invnum.length; i++) {
                                    FaOrdenTrabajoPK pikpk = new FaOrdenTrabajoPK(codpik, requestData.invnum[i]);
                                    FaOrdenTrabajo pik = new FaOrdenTrabajo(pikpk);
                                    pik.setUsecod(Integer.parseInt(usu));
                                    pik.setFeccre(Timestamp.valueOf(LocalDateTime.now()));
                                    pik.setEstado("S");
                                    pik.setCompletado("S");
                                    pikDao.create(pik);
                                }
                                out.print("{\"resultado\":\"ok\",\"codpik\":" + codpik + "}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorcodpik\"}");
                            }
                            break;
                        case "8"://asignar usuario - operario al picking
                            int usuario = Integer.parseInt(request.getParameter("usu"));
                            siscod = Integer.parseInt(request.getParameter("siscod"));
                            sec = request.getParameter("sec");
                            pikDao = new FaOrdenTrabajoJpaController(empr);
                            List<FaOrdenTrabajo> lista = pikDao.listarsiscodcodord(Integer.parseInt(sec), siscod);
                            if (lista == null) {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"No se encontro ninguna orden\"}");
                                break;
                            }
                            for (FaOrdenTrabajo item : lista) {
                                item.setUsecodortr(usuario);
                                item.setFecumv(Timestamp.valueOf(LocalDateTime.now()));
                                pikDao.edit(item);
                            }
                            out.print("{\"resultado\":\"ok\"}");
                            break;
                        case "9":  //Lista las cajas segun orden y establecimiento de la session

                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            sec = request.getParameter("ord");
                            FaCajasJpaController cajasdao = new FaCajasJpaController(empr);
                            FaOrdenTrabajoJpaController ordendao = new FaOrdenTrabajoJpaController(empr);
                            String com = ordendao.verificarComInvnum(Integer.parseInt(sec), siscod);
                            if (com == null) {
                                com = "N";
                            }
                            out.print("{\"resultado\":\"ok\",\"com\":\"" + com + "\",\"data\":" + cajasdao.listarcantavanceJson(siscod, Integer.parseInt(sec)) + ",\"secuencias\":" + cajasdao.listarporcentajesJson(Integer.parseInt(sec), siscod) + "}");
                            
                            break;
                        case "10":  //Lista las cajas segun orden y caja

                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            sec = request.getParameter("ord");
                            String caja = request.getParameter("caja");
                            cajasdao = new FaCajasJpaController(empr);

                            out.print("{\"resultado\":\"ok\",\"fecha\":\""
                                    + Timestamp.valueOf(LocalDateTime.now()).toString() + "\",\"completado\":" + cajasdao.comprobarCaja(siscod, Integer.parseInt(sec), caja) + ",\"data\":" + cajasdao.listardetJson(caja, Integer.parseInt(sec), siscod) + "}");
                            
                            break;
                        case "11":  //CHECKEAR LAS CAJAS - recepcion
                            int invnum = Integer.parseInt(request.getParameter("invnum"));
                            int numitm = Integer.parseInt(request.getParameter("numitm"));
                            String usecod = session.getAttribute("codi").toString();
                            caja = request.getParameter("caja");
                            String check = request.getParameter("check");
                            cajasdao = new FaCajasJpaController(empr);
                            FaCajasPK cajapk = new FaCajasPK(invnum, numitm, caja);
                            FaCajas cajadet = cajasdao.findFaCajas(cajapk);
                            if (check.equals("S")) {
                                if ("S".equals(cajadet.getCheckenvio())) {
                                    cajadet.setCheckenvio("N");
                                    cajadet.setUsecod(null);
                                    cajadet.setFecchk(null);
                                    cajasdao.edit(cajadet);
                                    out.print("{\"resultado\":\"ok\"}");
                                } else {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"cambio\"}");
                                }
                            } else {
                                if ("N".equals(cajadet.getCheckenvio()) || cajadet.getCheckenvio() == null) {
                                    cajadet.setCheckenvio("S");
                                    cajadet.setUsecod(Integer.parseInt(usecod));
                                    cajadet.setFecchk(Timestamp.valueOf(LocalDateTime.now()));
                                    cajasdao.edit(cajadet);
                                    out.print("{\"resultado\":\"ok\"}");
                                } else {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"cambio\"}");
                                }
                            }
                            break;
                        case "12": //Lista las ordenes de transporte por fechas
                            cajasdao = new FaCajasJpaController(empr);
                            fec_ini = request.getParameter("fechainicio");
                            fec_fin = request.getParameter("fechafin");
                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            out.print("{\"resultado\":\"ok\",\"data\":" + cajasdao.listarfechas(fec_ini, fec_fin, siscod) + "}");
                            
                            break;
                        case "13": //Anular OT
                            ordendao = new FaOrdenTrabajoJpaController(empr);
                            sec = request.getParameter("id");
                            resultado = ordendao.EliminarOt(Integer.parseInt(sec));
                            if (resultado == "S") {
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorproce\"}");
                            }
                            break;
                        case "14":
                            ordendao = new FaOrdenTrabajoJpaController(empr);
                            sec = request.getParameter("orden");
                            resultado = ordendao.CerrarOT(Integer.parseInt(sec));
                            if (resultado == "S") {
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorproce\"}");
                            }
                            break;
                        case "15":

                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            sec = request.getParameter("ord");
                            caja = request.getParameter("caja");
                            cajasdao = new FaCajasJpaController(empr);

                            sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            body = sb.toString();
                            usuario = Integer.parseInt(session.getAttribute("codi").toString());
                            resultado = cajasdao.updateCheckEnvio(caja, Integer.parseInt(sec), siscod, body, usuario
                            );
                            if (resultado.equals("S")) {
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"erroraplic\"}");
                            }
                            break;
                        case "16":  //Enviar cantidades a LAS CAJAS - recepcion
                            invnum = Integer.parseInt(request.getParameter("invnum"));
                            numitm = Integer.parseInt(request.getParameter("numitm"));
                            fecha = request.getParameter("fecha");
                            int val = Integer.parseInt(request.getParameter("val"));
                            usecod = session.getAttribute("codi").toString();
                            caja = request.getParameter("caja");
                            String cant = request.getParameter("cant");

                            ordendao = new FaOrdenTrabajoJpaController(empr);
                            com = ordendao.verificarComInvnuminvnum(invnum);
                            
                            if (!"S".equals(com)) {
                                cajasdao = new FaCajasJpaController(empr);
                                cajapk = new FaCajasPK(invnum, numitm, caja);
                                cajadet = cajasdao.findFaCajas(cajapk);
                                if (cajadet != null) {
                                    if ("e".equals(cant)) {
                                        cajadet.setCanter(val);
                                    } else if ("f".equals(cant)) {
                                        cajadet.setCantfr(val);
                                    }
                                    cante = 0;
                                    cantf = 0;
                                    int canteing = 0;
                                    int cantfing = 0;
                                    if (Objects.equals(cajadet.getCante(), 0)) {
                                        cajadet.setCante(null);
                                    }
                                    if (Objects.equals(cajadet.getCanter(), 0)) {
                                        cajadet.setCanter(null);
                                    }
                                    if (Objects.equals(cajadet.getCantf(), 0)) {
                                        cajadet.setCantf(null);
                                    }
                                    if (Objects.equals(cajadet.getCantfr(), 0)) {
                                        cajadet.setCantfr(null);
                                    }
                                    if (cajadet.getCante() != null) {
                                        cante = cajadet.getCante();
                                    }
                                    if (cajadet.getCanter() != null) {
                                        canteing = cajadet.getCanter();
                                    }
                                    if (cajadet.getCantf() != null) {
                                        cantf = cajadet.getCantf();
                                    }
                                    if (cajadet.getCantfr() != null) {
                                        cantfing = cajadet.getCantfr();
                                    }

                                    if (cante <= canteing && cantf <= cantfing) {
                                        cajadet.setCheckenvio("S");
                                        cajadet.setFecchk(Timestamp.valueOf(LocalDateTime.now()));
                                    } else {
                                        cajadet.setCheckenvio("N");
                                    }
                                    cajadet.setUsecod(Integer.parseInt(usecod));
                                    if (cajadet.getFecinirecep() == null) {
                                        cajadet.setFecinirecep(Timestamp.valueOf(fecha));
                                    }
                                    cajadet.setFecfinrecep(Timestamp.valueOf(LocalDateTime.now()));
                                    cajasdao.edit(cajadet);
                                    
                                    out.print("{\"resultado\":\"ok\"}");
                                } else {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"noencontrado\"}");
                                }
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"yacompletado\"}");
                            }
                            break;
                        case "17":  //Lista las cajas segun orden y establecimiento de la session

                            sec = request.getParameter("ord");
                            cajasdao = new FaCajasJpaController(empr);

                            out.print("{\"resultado\":\"ok\",\"data\":" + cajasdao.listarcajas(Integer.parseInt(sec)) + "}");
                            
                            break;
                        case "18": //Lista las ordenes de transporte por fechas
                            cajasdao = new FaCajasJpaController(empr);
                            fec_ini = request.getParameter("fechainicio");
                            fec_fin = request.getParameter("fechafin");
                            out.print("{\"resultado\":\"ok\",\"data\":" + cajasdao.listarfechasnosiscod(fec_ini, fec_fin) + "}");
                            
                            break;
                        case "19":
                            cajasdao = new FaCajasJpaController(empr);
                            caja = request.getParameter("caja");
                            orden = request.getParameter("orden");
                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            out.print("{\"resultado\":\"ok\",\"data\":" + cajasdao.secuencias(siscod, caja, Integer.parseInt(orden)) + "}");
                            
                            break;
                        case "20":// para etiqueta en el picking(previene el error)
                            cajasdao = new FaCajasJpaController(empr);
                            caja = request.getParameter("caja");
                            sec = request.getParameter("ord");
                            siscod = Integer.parseInt(request.getParameter("siscod"));
                            out.print("{\"resultado\":\"ok\",\"data\":" + cajasdao.listarcajas(Integer.parseInt(sec), caja, siscod) + "}");
                            
                            break;
                        case "21":// para etiqueta enbalaje general(no previene el error)
                            cajasdao = new FaCajasJpaController(empr);
                            caja = request.getParameter("caja");
                            out.print("{\"resultado\":\"ok\",\"data\":" + cajasdao.listarcajas(caja) + "}");
                            
                            break;
                        case "22":
                            ordendao = new FaOrdenTrabajoJpaController(empr);
                            usecod = session.getAttribute("codi").toString();
                            check = request.getParameter("chk");
                            orden = request.getParameter("ord");
                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            String res = ordendao.checkCajasRecepcion(Integer.parseInt(usecod), check, Integer.parseInt(orden), siscod);
                            if ("S".equals(res)) {
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorproce\"}");
                            }
                            
                            break;
                        case "23":// listar para ordenes abrir recepcion
                            ordendao = new FaOrdenTrabajoJpaController(empr);
                            orden = request.getParameter("orden");
                            out.print("{\"resultado\":\"ok\",\"data\":" + ordendao.listarOtabrirot(Integer.parseInt(orden)) + "}");
                            
                            break;
                        case "24"://abrir cerrar ot recepcion en el central
                            ordendao = new FaOrdenTrabajoJpaController(empr);
                            usecod = session.getAttribute("codi").toString();
                            check = request.getParameter("chk");
                            orden = request.getParameter("ord");
                            siscod = Integer.parseInt(request.getParameter("siscod"));
                            res = ordendao.checkCajasRecepcion(Integer.parseInt(usecod), check, Integer.parseInt(orden), siscod);
                            if ("S".equals(res)) {
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorproce\"}");
                            }
                            
                            break;
                        case "25":// listar faltantes excedentes
                            cajasdao = new FaCajasJpaController(empr);
                            int ordenini = Integer.parseInt(request.getParameter("ordenini"));
                            int ordenfin = Integer.parseInt(request.getParameter("ordenfin"));
                            siscod = Integer.parseInt(request.getParameter("siscod"));
                            out.print("{\"resultado\":\"ok\",\"data\":" + cajasdao.listarSobrantesFaltantesJson(ordenini, ordenfin, siscod) + "}");
                            
                            break;
                        case "26":
                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            sec = request.getParameter("ord");
                            caja = request.getParameter("caja");
                            cajasdao = new FaCajasJpaController(empr);
                            out.print("{\"resultado\":\"ok\",\"data\":" + cajasdao.listarporcentajesJson(Integer.parseInt(sec), siscod, caja) + "}");
                            break;
                        case "27":
                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            sec = request.getParameter("ord");
                            cajasdao = new FaCajasJpaController(empr);
                            out.print("{\"resultado\":\"ok\",\"data\":" + cajasdao.listarfaltantesexcedentes(Integer.parseInt(sec), siscod) + "}");
                            
                            break;
                        case "28":
                            sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            body = sb.toString();
                            cajasdao = new FaCajasJpaController(empr);
                            usecod = session.getAttribute("codi").toString();
                            res = cajasdao.recepcionar_guardar(body, Integer.parseInt(usecod));
                            if ("S".equals(res)) {
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorproce\"}");
                            }
                            break;
                        case "29":
                            String siscodstr;
                            sec = request.getParameter("orden");
                            String fecini = request.getParameter("fecini");
                            String fecfin = request.getParameter("fecfin");
                            String central = session.getAttribute("central").toString();
                            if (central.equals("S")) {
                                siscodstr = "";
                            } else {
                                siscodstr = session.getAttribute("siscod").toString();
                            }
                            cajasdao = new FaCajasJpaController(empr);
                            out.print("{\"resultado\":\"ok\",\"data\":" + cajasdao.listarordenesseguimiento(fecini, fecfin, sec, siscodstr) + "}");
                            
                            break;
                        case "30":// listar faltantes excedentes orden siscod
                            cajasdao = new FaCajasJpaController(empr);
                            orden = request.getParameter("orden");
                            siscod = Integer.parseInt(request.getParameter("siscod"));
                            out.print("{\"resultado\":\"ok\",\"data\":" + cajasdao.listarSobrantesFaltantesJson(Integer.parseInt(orden), siscod) + "}");
                            
                            break;
                        case "31":
                            orden = request.getParameter("orden");
                            codpro = request.getParameter("codpro");
                            siscod = Integer.parseInt(request.getParameter("siscod"));
                            String valor = request.getParameter("valor");
                            ReposicionRecepcionPK reppk = new ReposicionRecepcionPK(Integer.parseInt(orden), codpro, siscod);
                            ReposicionRecepcionJpaController repdao = new ReposicionRecepcionJpaController(empr);
                            ReposicionRecepcion repobj = repdao.findReposicionRecepcion(reppk);
                            central = session.getAttribute("central").toString();
                            usecod = session.getAttribute("codi").toString();
                            if (repobj == null) {
                                repobj = new ReposicionRecepcion(reppk);
                                repobj.setEstado("S");
                                repobj.setFeccre(new Date());
                                repobj.setUsecod(Integer.parseInt(usecod));
                                if (central.equals("S")) {
                                    repobj.setUsecodobs(Integer.parseInt(usecod));
                                    repobj.setFeccodobs(new Date());
                                    repobj.setEstobs(valor);
                                } else {
                                    repobj.setUsecodobsbot(Integer.parseInt(usecod));
                                    repobj.setFecestobsbot(new Date());
                                    repobj.setEstobsbot(valor);
                                }
                                repdao.create(repobj);
                            } else {
                                repobj.setEstado("S");
                                if (central.equals("S")) {
                                    repobj.setUsecodobs(Integer.parseInt(usecod));
                                    repobj.setFeccodobs(new Date());
                                    repobj.setEstobs(valor);
                                } else {
                                    repobj.setUsecodobsbot(Integer.parseInt(usecod));
                                    repobj.setFecestobsbot(new Date());
                                    repobj.setEstobsbot(valor);
                                }
                                if ("R".equals(repobj.getEstobs()) && "R".equals(repobj.getEstobsbot())) {
                                    String calculo = request.getParameter("calculo");
                                    BigDecimal valbigdecimal = new BigDecimal(calculo);
                                    repobj.setCantidad(valbigdecimal.negate());
                                } else {
                                    BigDecimal valbigdecimal = new BigDecimal("0");
                                    repobj.setCantidad(valbigdecimal);
                                }
                                repdao.edit(repobj);
                            }
                            out.print("{\"resultado\":\"ok\"}");
                            
                            break;
                        case "32"://empezando la otra funcion, con las nuevas tablas
                            sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            body = sb.toString();
                            PickingJpaController pickdao = new PickingJpaController(empr);
                            dto.Picking pickobj = new dto.Picking();
                            usecod = session.getAttribute("codi").toString();
                            pickobj.setDespick("");
                            pickobj.setFeccre(new Date());
                            pickobj.setUsecod(Integer.parseInt(usecod));
                            codalminv = session.getAttribute("codalminv").toString();
                            pickobj.setCodalm(codalminv);
                            pickdao.create(pickobj);
                            int pickcod = pickdao.obtenerUlt();
                            PickingDetalleJpaController pickdetdao = new PickingDetalleJpaController(empr);
                            if (pickcod > 0) {
                                res = pickdetdao.agregar(body, Integer.parseInt(usecod), pickcod);
                                if ("S".equals(res)) {
                                    out.print("{\"resultado\":\"ok\",\"mensaje\":\"" + pickcod + "\"}");
                                } else {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"errorproce\"}");
                                }
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorproce\"}");
                            }
                            break;

                        case "33": //Lista las ordenes de transporte por fechas
                            pickdao = new PickingJpaController(empr);

                            fec_ini = request.getParameter("fechainicio");
                            fec_fin = request.getParameter("fechafin");
                            codalminv = session.getAttribute("codalminv").toString();
                            out.print("{\"resultado\":\"ok\",\"data\":" + pickdao.listarfechas(fec_ini, fec_fin, codalminv) + "}");
                           
                            break;
                        case "34":  //Lista los establecimientos para el picking
                            de = session.getAttribute("de").toString();
                            fec_ini = request.getParameter("fechainicio");
                            fec_fin = request.getParameter("fechafin");
                            sec = request.getParameter("sec");
                            codalminv = session.getAttribute("codalminv").toString();
                            pickdao = new PickingJpaController(empr);
                            if (de.equals("l")) {
                                out.print("{\"resultado\":\"ok\",\"data\":" + pickdao.listarPickingListDestinos_tot(sec, fec_ini, fec_fin, "", codalminv) + "}");
                            } else {
                                //String usecod = session.getAttribute("codbar").toString();
                                usecod = "";
                                out.print("{\"resultado\":\"ok\",\"data\":" + pickdao.listarPickingListDestinos_tot(sec, fec_ini, fec_fin, usecod, codalminv) + "}");
                            }
                            break;
                        case "35": // check para el codigo de barras
                            codpro = request.getParameter("codpro");
                            codlot = request.getParameter("codlot");
                            secuencia = Integer.parseInt(request.getParameter("secuencia"));
                            pickdetdao = new PickingDetalleJpaController(empr);
                            PickingDetalle pickdet = pickdetdao.findPickingDetalle(secuencia);
                            de = session.getAttribute("de").toString();
                            //String resultado = ordendao.verificarCompletadoInvnum(secuencia);
                            resultado = "N";
                            pickdetdao = new PickingDetalleJpaController(empr);
                            if (!"S".equals(resultado)) {
                                if (de.equals("b")) {
                                    String codusu = session.getAttribute("codbar").toString();
                                    pickdet.setUsuean13(Integer.parseInt(codusu));
                                } else {
                                    String codusu = session.getAttribute("codi").toString();
                                    pickdet.setUsuean13adm(Integer.parseInt(codusu));
                                }
                                if (pickdet.getChkean13() == null || pickdet.getChkean13().equals("N")) {
                                    pickdet.setChkean13("S");
                                } else {
                                    pickdet.setChkean13("N");
                                    pickdet.setUsuean13(null);
                                    pickdet.setUsuean13adm(null);
                                }
                                pickdetdao.edit(pickdet);
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"yaenviado\"}");
                            }
                            break;
                        case "36":
                            pickdao = new PickingJpaController(empr);
                            fec_ini = request.getParameter("fechainicio");
                            fec_fin = request.getParameter("fechafin");
                            sec = request.getParameter("sec");
                            orden = request.getParameter("orden");
                            siscod = Integer.parseInt(request.getParameter("siscod"));
                            codalminv = session.getAttribute("codalminv").toString();
                            out.print("{\"resultado\":\"ok\",\"fecha\":\""
                                    + Timestamp.valueOf(LocalDateTime.now()).toString() + "\",\"data\":" + pickdao.listarPickingListDestinosDetTodo(sec, "", "", siscod, orden, codalminv) + "}");
                            
                            break;
                        case "37"://lista un producto en la orden
                            siscod = Integer.parseInt(request.getParameter("siscod"));
                            codalminv = session.getAttribute("codalminv").toString();
                            codpro = request.getParameter("codpro");
                            codlot = request.getParameter("codlot");

                            AlmacenesBartolitoDetalleJpaController bartoDAO = new AlmacenesBartolitoDetalleJpaController(empr);
                            String ubis = bartoDAO.buscarAllDetallePorLoteAlmacenProducto(codlot, codalminv, codpro);
                            JSONArray ubisArray = new JSONArray(ubis);
                            secuencia = Integer.parseInt(request.getParameter("secuencia"));
                            pickdao = new PickingJpaController(empr);
                            pickdetdao = new PickingDetalleJpaController(empr);
                            PickingCajasJpaController pickcajasdao = new PickingCajasJpaController(empr);

                            out.print("{\"resultado\":\"ok\",\"ubi\":" + ubisArray + ",\"data\":" + pickdao.listarPickingListDet(codalminv, secuencia) + ",\"fecha\":\""
                                    + Timestamp.valueOf(LocalDateTime.now()).toString() + "\",\"cajas\":"
                                    + pickcajasdao.listarJson(secuencia) + "}");
                           
                            break;

                        case "38"://check picking
                            codpro = request.getParameter("codpro");
                            codlot = request.getParameter("codlot");
                            fecha = request.getParameter("fecha");
                            cante = Integer.parseInt(request.getParameter("cante"));
                            cantf = Integer.parseInt(request.getParameter("cantf"));
                            secuencia = Integer.parseInt(request.getParameter("secuencia"));
                            pickdetdao = new PickingDetalleJpaController(empr);
                            pickdet = pickdetdao.findPickingDetalle(secuencia);
                            //resultado = ordendao.verificarCompletadoInvnum(secuencia);
                            resultado = "N";
                            if (!"S".equals(resultado)) {
                                if (pickdet.getChkpick() == null || pickdet.getChkpick().equals("N")) {

                                    pickcajasdao = new PickingCajasJpaController(empr);
                                    pickcajasdao.EliminarCajas(secuencia);
                                    pickdet.setChkpick("S");
                                    pickdet.setFecfpick(Timestamp.valueOf(LocalDateTime.now()));
                                    pickdet.setFecipick(Timestamp.valueOf(fecha));

                                    de = session.getAttribute("de").toString();
                                    String codusu = "";
                                    if (de.equals("b")) {
                                        codusu = session.getAttribute("codbar").toString();
                                        pickdet.setUsupick(Integer.parseInt(codusu));
                                    } else {
                                        codusu = session.getAttribute("codi").toString();
                                        pickdet.setUsupickadm(Integer.parseInt(codusu));
                                    }

                                    sb = new StringBuilder();
                                    try (BufferedReader reader = request.getReader()) {
                                        String line;
                                        while ((line = reader.readLine()) != null) {
                                            sb.append(line);
                                        }
                                    }

                                    // Parsear el contenido del cuerpo como un JSONArray
                                    String requestBody = sb.toString();
                                    JSONArray jsonArray = new JSONArray(requestBody);

                                    // Iterar las cajas en el JSONArray
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        pickcajasdao = new PickingCajasJpaController(empr);
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        caja = jsonObject.getString("caja");
                                        String cantE = jsonObject.getString("cantE");
                                        String cantF = jsonObject.getString("cantF");
                                        String ubicacion = jsonObject.getString("ubicacion");
                                        PickingCajas pickcajaobj = new PickingCajas();
                                        pickcajaobj.setCaja(caja);
                                        pickcajaobj.setPickdetcod(secuencia);
                                        if (!cantE.equals("")) {
                                            pickcajaobj.setCante(Integer.parseInt(cantE));
                                        } else {
                                            pickcajaobj.setCante(null);
                                        }
                                        if (!cantF.equals("")) {
                                            pickcajaobj.setCantf(Integer.parseInt(cantF));
                                        } else {
                                            pickcajaobj.setCantf(null);
                                        }
                                        if (!ubicacion.equals("")) {
                                            pickcajaobj.setUbicacion(ubicacion);
                                        } else {
                                            pickcajaobj.setUbicacion("");
                                        }

                                        if (de.equals("b")) {
                                            pickcajaobj.setUsecodcreebart(Integer.parseInt(codusu));
                                        } else {
                                            pickcajaobj.setUsecodcree(Integer.parseInt(codusu));
                                        }

                                        pickcajaobj.setFeccre(new Date());
                                        pickcajasdao.create(pickcajaobj);
                                        
                                    }
                                } else {
                                    pickdet.setChkpick("N");
                                    pickdet.setFecfpick(null);
                                    pickdet.setFecipick(null);
                                    pickdet.setUsupick(null);
                                    pickdet.setUsupickadm(null);
                                    de = session.getAttribute("de").toString();
                                    if (de.equals("b")) {
                                        String codusu = session.getAttribute("codbar").toString();
                                        pickdet.setUsudelpick(Integer.parseInt(codusu));
                                    } else {
                                        String codusu = session.getAttribute("codi").toString();
                                        pickdet.setUsudelpickadm(Integer.parseInt(codusu));
                                    }
                                    pickcajasdao = new PickingCajasJpaController(empr);
                                    pickcajasdao.EliminarCajas(secuencia);
                                    
                                }
                                pickdetdao.edit(pickdet);
                                
                                out.print("{\"resultado\":\"ok\",\"fecha\":\""
                                        + Timestamp.valueOf(LocalDateTime.now()).toString() + "\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"yaenviado\"}");
                            }
                            break;
                        case "39"://generar txt picking list
                            pickcod = Integer.parseInt(request.getParameter("pickcod"));
                            String conf = request.getParameter("conf");
                            pickdetdao = new PickingDetalleJpaController(empr);
                            PickinglistJpaController picklistdao = new PickinglistJpaController(empr);
                            pickdetdao.asignarsecuencia(pickcod);
                            pickdao = new PickingJpaController(empr);
                            codalminv = session.getAttribute("codalminv").toString();
                            Pickinglist piklist = picklistdao.findPickinglist(pickcod);
                            if ("S".equals(piklist.getTxtdescarga()) && !"S".equals(conf)) {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"yatxt\"}");
                                break;
                            }
                            out.print("{\"resultado\":\"ok\",\"data\":" + pickdao.generartxt(pickcod, codalminv) + "}");
                            piklist.setTxtdescarga("S");
                            piklist.setFectxt(new Date());
                            picklistdao.edit(piklist);
                            
                            break;
                        case "40"://para la lista agrupada de pickinglist
                            picklistdao = new PickinglistJpaController(empr);
                            String invnumMin2 = request.getParameter("invnumMin");
                            String invnumMax2 = request.getParameter("invnumMax");
                            String fecMin = request.getParameter("fecMin");
                            String fecMax = request.getParameter("fecMax");
                            codalminv = session.getAttribute("codalminv").toString();
                            String resp = picklistdao.Listar(invnumMin2, invnumMax2, fecMin, fecMax, codalminv);
                            
                            out.print("{\"resultado\":\"ok\",\"data\":" + resp + "}");
                            break;
                        case "41"://vista previa para picking list agrupado
                            invnum = Integer.parseInt(request.getParameter("invnum"));
                            picklistdao = new PickinglistJpaController(empr);
                            out.print("{\"resultado\":\"ok\",\"data\":" + picklistdao.vistaPrevia(invnum) + "}");
                            
                            break;
                        case "42": //Agregar guias transferencias(picking) ahora pickinglist
                            usu = session.getAttribute("codi").toString();
                            sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            body = sb.toString();
                            gson = new Gson();

                            requestData = gson.fromJson(body, RequestData.class);
                            picklistdao = new PickinglistJpaController(empr);
                            codpik = picklistdao.obtenerCodpikMax() + 1;
                            piklist = new Pickinglist(codpik);
                            pickdao = new PickingJpaController(empr);
                            piklist.setFeccre(new Date());
                            piklist.setUsecod(Integer.parseInt(usu));
                            if (codpik != 0) {
                                for (int i = 0; i < requestData.invnum.length; i++) {
                                    dto.Picking pik = pickdao.findPicking(requestData.invnum[i]);
                                    pik.setCompletado("S");
                                    pik.setCodpicklist(codpik);
                                    pickdao.edit(pik);
                                }
                                picklistdao.create(piklist);
                                out.print("{\"resultado\":\"ok\",\"codpik\":" + codpik + "}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorcodpik\"}");
                            }
                            break;
                        case "43": //Lista las ordenes de transporte por fechas
                            cajasdao = new FaCajasJpaController(empr);
                            fec_ini = request.getParameter("fechainicio");
                            fec_fin = request.getParameter("fechafin");
                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            out.print("{\"resultado\":\"ok\",\"data\":" + cajasdao.listarfechaspicking(fec_ini, fec_fin, siscod) + "}");
                            
                            break;
                        case "44":  //Lista las cajas segun orden y establecimiento de la session

                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            sec = request.getParameter("ord");
                            cajasdao = new FaCajasJpaController(empr);
                            picklistdao = new PickinglistJpaController(empr);
                            pickcajasdao = new PickingCajasJpaController(empr);
                            com = picklistdao.verificarComInvnum(Integer.parseInt(sec), siscod);
                            if (com == null) {
                                com = "N";
                            }
                            out.print("{\"resultado\":\"ok\",\"com\":\"" + com + "\",\"data\":" + cajasdao.listarcantavanceJsonpicking(siscod, Integer.parseInt(sec)) + ",\"secuencias\":" + pickcajasdao.listarporcentajesJson(Integer.parseInt(sec), siscod) + "}");
                            
                            break;
                        case "45":
                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            sec = request.getParameter("ord");
                            cajasdao = new FaCajasJpaController(empr);
                            out.print("{\"resultado\":\"ok\",\"data\":" + cajasdao.listarfaltantesexcedentespicking(Integer.parseInt(sec), siscod) + "}");
                            
                            break;
                        case "46":
                            picklistdao = new PickinglistJpaController(empr);
                            usecod = session.getAttribute("codi").toString();
                            check = request.getParameter("chk");
                            orden = request.getParameter("ord");
                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            res = picklistdao.checkCajasRecepcion(Integer.parseInt(usecod), check, Integer.parseInt(orden), siscod);
                            if ("S".equals(res)) {
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorproce\"}");
                            }
                            break;
                        case "47":
                            picklistdao = new PickinglistJpaController(empr);
                            usecod = session.getAttribute("codi").toString();
                            check = request.getParameter("chk");
                            orden = request.getParameter("ord");
                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            res = picklistdao.checkCajasRecepcion(Integer.parseInt(usecod), check, Integer.parseInt(orden), siscod);
                            if ("S".equals(res)) {
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorproce\"}");
                            }
                            break;
                        case "48":
                            cajasdao = new FaCajasJpaController(empr);
                            caja = request.getParameter("caja");
                            orden = request.getParameter("orden");
                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            out.print("{\"resultado\":\"ok\",\"data\":[\"No Disponible\"]}");
                            
                            break;
                        case "49":  //Lista las cajas segun orden y caja

                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            sec = request.getParameter("ord");
                            caja = request.getParameter("caja");
                            cajasdao = new FaCajasJpaController(empr);

                            out.print("{\"resultado\":\"ok\",\"fecha\":\""
                                    + Timestamp.valueOf(LocalDateTime.now()).toString() + "\",\"completado\":" + cajasdao.comprobarCajapicking(siscod, Integer.parseInt(sec), caja) + ",\"data\":" + cajasdao.listardetJsonpicking(caja, Integer.parseInt(sec), siscod) + "}");
                            
                            break;
                        case "50":
                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            sec = request.getParameter("ord");
                            caja = request.getParameter("caja");
                            pickcajasdao = new PickingCajasJpaController(empr);
                            out.print("{\"resultado\":\"ok\",\"data\":" + pickcajasdao.listarporcentajesJson(Integer.parseInt(sec), siscod, caja) + "}");
                            
                            break;
                        case "51":
                            sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            body = sb.toString();
                            pickcajasdao = new PickingCajasJpaController(empr);
                            usecod = session.getAttribute("codi").toString();
                            res = pickcajasdao.recepcionar_guardar(body, Integer.parseInt(usecod));
                            if ("S".equals(res)) {
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorproce\"}");
                            }
                            break;
                        case "52":
                            String codpicklist = request.getParameter("codpicklist");
                            picklistdao = new PickinglistJpaController(empr);

                            piklist = picklistdao.findPickinglist(Integer.parseInt(codpicklist));
                            if ("S".equals(piklist.getTxtdescarga())) {
                                if (picklistdao.matchpickmov(Integer.parseInt(codpicklist)).equals("S")) {
                                    piklist.setChktxt("S");
                                    picklistdao.edit(piklist);
                                    out.print("{\"resultado\":\"ok\"}");
                                } else {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"errorproce\"}");
                                }
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"notxt\"}");
                            }
                            break;
                        case "53": //Lista las ordenes de transporte por fechas
                            pickdao = new PickingJpaController(empr);

                            fec_ini = request.getParameter("fechainicio");
                            fec_fin = request.getParameter("fechafin");
                            codalminv = session.getAttribute("codalminv").toString();
                            out.print("{\"resultado\":\"ok\",\"data\":" + pickdao.listarfechasmatch(fec_ini, fec_fin, codalminv) + "}");
                            
                            break;
                        case "54":
                            pickdao = new PickingJpaController(empr);
                            invnum = Integer.parseInt(request.getParameter("invnum"));
                            dto.Picking pik = pickdao.findPicking(invnum);
                            pik.setEstado("E");
                            pickdao.edit(pik);
                            
                            out.print("{\"resultado\":\"ok\"}");

                            break;
                        default:
                            out.print("{\"resultado\":\"error\",\"mensaje\":\"noproce\"}");
                            break;
                    }

                } else {
                    out.print("{\"resultado\":\"error\",\"mensaje\":\"nosession\"}");
                }
            } catch (Exception e) {
                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorgeneral\"}");
            }
        }
    }

    private static class RequestData {

        int[] invnum;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
