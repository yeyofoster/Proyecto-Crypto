/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paquete;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Master
 */
public class EncryptPurposal extends HttpServlet {

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
        String mensaje = request.getParameter("texto");
        String receiver = request.getParameter("r2");
        System.out.println("Texto: "+mensaje);
        System.out.println("receiver: "+receiver);
        try {
        /*
        String selectSQL = "SELECT * FROM user WHERE user=?";
        ResultSet rs = null, rs2 = null;
        String text = "0";
        Connection conn = Conexion.getConexion();
        int id = -1;

        try (
                PreparedStatement pstmt = conn.prepareStatement(selectSQL);) {
            // set parameter;
            pstmt.setString(1, receiver);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
            }
            System.out.println("Se pudo consultar del usuario");

        } catch (SQLException e) {
            text = "2";
            System.out.println("Hubo un error al consultar del usuario: ");
            System.out.println(e.getMessage());

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    String rutaLlave="C:/Users/Master/Documents/NetBeansProjects/crypto2/Llaves/tempprivk"+id+receiver+".txt";
                    String res = crypto.readBlob2(id,rutaLlave,"C:/Users/Master/Documents/NetBeansProjects/crypto2/Llaves/tempprivk"+id+receiver+".txt");

                } else {
                    System.out.println("No se encontro algun usuario relacionado en la base");
                }
            } catch (SQLException e) {
                text = "5";
                System.out.println("Error al intentar grabar en la tabla image");
                System.out.println(e.getMessage());
            } catch (Exception ex) {
                Logger.getLogger(UploadImage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
            new Message(mensaje, "C:/Users/Master/Documents/NetBeansProjects/crypto2/Llaves/userprivk1985.txt").writeToFile("C:/Users/Master/Documents/NetBeansProjects/crypto2/Mensaje/tempMessartistuser.txt");
        } catch (Exception ex) {
            Logger.getLogger(EncryptPurposal.class.getName()).log(Level.SEVERE, null, ex);
        }
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
