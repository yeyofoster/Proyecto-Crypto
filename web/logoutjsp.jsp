<%-- 
    Document   : logoutjsp
    Created on : 10/06/2018, 03:55:29 PM
    Author     : Master
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%

//allow access only if session exists
    String user = null;
    if (session.getAttribute("user") == null) {
        response.sendRedirect("index.html");

        System.out.println("Por que?");

    } else {
        user = (String) session.getAttribute("user");
    }
    System.out.println("user: " + session.getAttribute("user"));

    String userName = null;
    String sessionID = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user")) {
                userName = cookie.getValue();
            }
            if (cookie.getName().equals("JSESSIONID")) {
                sessionID = cookie.getValue();
            }
        }
    } else {
        sessionID = session.getId();
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="<%=response.encodeURL("Logout")%>" method="post">
            <input type="submit" class="btn red" value="Logout" >
        </form> 
    </body>
</html>
