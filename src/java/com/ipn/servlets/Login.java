package com.ipn.servlets;

import com.ipn.util.Conexion;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Master
 */
public class Login extends HttpServlet {

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
            throws ServletException, IOException, SQLException {

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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        System.out.println("Entro");
        String user = request.getParameter("user");
        String pass = request.getParameter("pass");

        /*
        Fracción try para pasar la llave ingresada por el usuario a un SHA-256
         */
        String passsha256 = null;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(pass.getBytes("UTF-8"), 0, pass.length());
            //passsha256 = DatatypeConverter.printHexBinary(digest.digest());
            passsha256 = new String(Base64.getEncoder().encode(digest.digest()));
            System.out.println("LLave pasada a SHA256");
            System.out.println(passsha256);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Signin.class.getName()).log(Level.SEVERE, null, ex);
        }

        String selectSQL = "SELECT * FROM user WHERE user=? AND pass=?";
        ResultSet rs = null;
        String text = "0";

        try (Connection conn = Conexion.getConexion();
                PreparedStatement pstmt = conn.prepareStatement(selectSQL);) {
            // set parameter;
            pstmt.setString(1, user);
            pstmt.setString(2, passsha256);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                HttpSession session = request.getSession();
                text = rs.getString("user");
                session.setAttribute("user", text);
                session.setAttribute("id", rs.getInt("id"));
                session.setMaxInactiveInterval(20 * 60);
                Cookie userName = new Cookie("user", user);
                response.addCookie(userName);

                //Get the encoded URL string
            }
        } catch (SQLException e) {
            text = "2";

            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {

                    rs.close();
                }
            } catch (SQLException e) {
                text = "3";
                System.out.println(e.getMessage());
            }
        }

        response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
        response.setCharacterEncoding("UTF-8"); // You want world domination, huh?
        response.getWriter().write(text);       // Write response body.

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
