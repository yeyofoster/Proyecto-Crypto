
package paquete;

import paquete.Conexion;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Master
 */
public class Signin extends HttpServlet {

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
        String user =request.getParameter("user2");
        String pass = request.getParameter("pass2");

        String selectSQL = "SELECT * FROM user WHERE user=?";
        ResultSet rs = null,rs2 = null,rs3=null;
        String text = "0";
        Connection conn=Conexion.getConexion();
        try (
                PreparedStatement pstmt = conn.prepareStatement(selectSQL);) {
                        // set parameter;
                pstmt.setString(1, user);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    text="1";
                    
                    //Get the encoded URL string
                }
            } catch (SQLException e) {
                text="2";

            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    String insertSQL = "INSERT user (user,pass)"
                        + " VALUES(?,?)";

                    try (   
                        PreparedStatement pstmt2 = conn.prepareStatement(insertSQL)) {
 
                        // set parameters
                        pstmt2.setString(1, user);
                        pstmt2.setString(2,pass);
 
                        pstmt2.executeUpdate();
                        
                            text=user;

                            rs2 = pstmt2.executeQuery();
                            rs2.close();
     
                        
                    } catch (SQLException e) {
                        text="4";
                        System.out.println(e.getMessage());
                    }
                }
                else{

                }
            } catch (SQLException e) {
                text="5";
                System.out.println(e.getMessage());
            }
        }
        
        selectSQL = "SELECT * FROM user WHERE user=? AND pass=?";
        rs = null;

 
        try (
                PreparedStatement pstmt = conn.prepareStatement(selectSQL);) {
                        // set parameter;
                pstmt.setString(1, user);
                pstmt.setString(2, pass);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    HttpSession session = request.getSession();
                    text=rs.getString("user");
                    session.setAttribute("user",text);
                    session.setAttribute("id",rs.getInt("id"));
                    session.setMaxInactiveInterval(5*60);
                    Cookie userName = new Cookie("user", user);
                    response.addCookie(userName);
                    
                    //Get the encoded URL string
                }
            } catch (SQLException e) {
                text="6";

            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {

                    rs.close();
                }
            } catch (SQLException e) {
                text="7";
                System.out.println(e.getMessage());
            }
        }
        System.out.println(text);
        response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
        response.setCharacterEncoding("UTF-8"); // You want world domination, huh?
        response.getWriter().write(text);       // Write response body.
    
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
