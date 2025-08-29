/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import javax.servlet.http.HttpSession;

/**
 *
 * @author USUARIO
 */
public class Sesion {

    public static void crearsesion(HttpSession session, int codi, String logi, String empr, String grucod,String grudes, int siscod,String sisent,String de) {
        session.setAttribute("logueado", "1");
        session.setAttribute("codi", codi);
        session.setAttribute("logi", logi);
        session.setAttribute("empr", empr);
        session.setAttribute("grucod", grucod);
        session.setAttribute("grudes", grudes);
        session.setAttribute("siscod", siscod);
        session.setAttribute("sisent", sisent);
        session.setAttribute("de", de);
    }
    public static boolean sesionvalida(HttpSession session) {
        String logueado=(String)session.getAttribute("logueado");
        return logueado.equals("1");
    }
    public static void cerrarsesion(HttpSession session){
        session.removeAttribute("logueado");
        session.removeAttribute("codi");
        session.removeAttribute("codbar");
        session.removeAttribute("logi");
        session.removeAttribute("empr");
        session.removeAttribute("grucod");
        session.removeAttribute("grudes");
        session.removeAttribute("siscod");
        session.removeAttribute("sisent");
        session.removeAttribute("de");
        session.invalidate();
    }
    public static int getCodi(HttpSession session){
        Object ocodigo=session.getAttribute("codi");
        return Integer.parseInt(ocodigo.toString());
    }
     public static String getLogi(HttpSession session){
        Object ologi=session.getAttribute("logi");
        return ologi.toString();
    }
}
