<%@page import="dto.Usuario"%>
<%@page import="dao.UsuarioDAO"%>
<%@page import="security.MD5"%>
<%
    String logi = request.getParameter("logi");
    Usuario u=UsuarioDAO.buscar(logi);
    if(u== null){
        out.print("{\"resultado\":\"error\"}");
    }
    else{
        out.print("{\"tipoUsua\":\""+u.getTipoUsua() +"\"}");
    }

%>