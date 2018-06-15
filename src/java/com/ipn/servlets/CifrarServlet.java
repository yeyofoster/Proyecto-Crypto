/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.servlets;

import com.ipn.cifradores.ImageEncDec;
import com.ipn.util.Conexion;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.ipn.util.crypto;
import static sun.security.krb5.Confounder.bytes;

@MultipartConfig
public class CifrarServlet extends HttpServlet {

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

        String key_name = request.getParameter("enc_key_name");
        String img_name = request.getParameter("enc_img_name");
        String page = request.getParameter("page");
        System.out.println("Page: " + page);
        String receiver = request.getParameter("receiver");
        System.out.println("Receiver: " + receiver);
        //int user_id = Integer.valueOf(request.getParameter("user_id"));

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

                Part filePart = request.getPart("enc_img"); // Retrieves <input type="file" name="file">
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
                InputStream fileContent = filePart.getInputStream();

                Part filePart2 = request.getPart("enc_key"); // Retrieves <input type="file" name="file">
                String fileName2 = Paths.get(filePart2.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
                InputStream fileContent2 = filePart2.getInputStream();

                int num_rows = Conexion.num("image");
                File image = crypto.InputStreamToFile(fileContent, "Dec" + String.valueOf(num_rows) + img_name);
                File key = crypto.InputStreamToFile(fileContent2, "Dec" + String.valueOf(num_rows) + key_name);

                byte[] hola = ImageEncDec.getFile(image);
                //ImageEncDec.saveFile(ImageEncDec.decryptPdfFile(key, hola ) , "C:/Users/Master/Documents/NetBeansProjects/crypto2/Dec/",num_rows+img_name) ;
                //File downloadFile = new File("C:/Users/Master/Documents/NetBeansProjects/crypto2/Dec/"+num_rows+img_name);
                ImageEncDec.saveFile(ImageEncDec.decryptPdfFile(key, hola), "C:/Users/yeyof/Desktop/AES-image-cipher-system/Dec/", num_rows + img_name);
                File downloadFile = new File("C:/Users/yeyof/Desktop/AES-image-cipher-system/Dec/" + num_rows + img_name);

                fileContent.close();
                fileContent2.close();
 
                FileInputStream inStream = new FileInputStream(downloadFile);

                // if you want to use a relative path to context root:
                String relativePath = getServletContext().getRealPath("");
                System.out.println("relativePath = " + relativePath);

                // obtains ServletContext
                ServletContext context = getServletContext();
                String filePath = null;

                // gets MIME type of the file
                String mimeType = context.getMimeType(filePath);
                if (mimeType == null) {
                    // set to binary type if MIME mapping not found
                    mimeType = "application/octet-stream";
                }
                System.out.println("MIME type: " + mimeType);

                // modifies response
                response.setContentType(mimeType);
                response.setContentLength((int) downloadFile.length());

                // forces download
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
                response.setHeader(headerKey, headerValue);

                // obtains response's output stream
                OutputStream outStream = response.getOutputStream();

                byte[] buffer = new byte[4096];
                int bytesRead = -1;

                while ((bytesRead = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }

                inStream.close();
                outStream.close();

            }
            text = "The user exists";
            System.out.println(text);

        } catch (SQLException e) {

            text = "The user doesnt exists";
            System.out.println(text);
            System.out.println(e.getMessage());

        } catch (Exception ex) {
            Logger.getLogger(CifrarServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();

                }
            } catch (SQLException ex) {
                Logger.getLogger(CifrarServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
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
