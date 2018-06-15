package com.ipn.servlets;

import com.ipn.cifradores.CifradorRSA;
import com.ipn.util.Conexion;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;
import com.ipn.cifradores.ImageEncDec;
import com.ipn.util.crypto;
import java.nio.file.Files;
import java.security.PublicKey;

/**
 *
 * @author Master
 */
@MultipartConfig
public class UploadImage extends HttpServlet {

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
            throws ServletException{

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

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws java.security.NoSuchProviderException
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
        
        String img_name = request.getParameter("img_name");
        String page = request.getParameter("page");
        String receiver = request.getParameter("receiver");
        System.out.println("Receiver: " + receiver);
        int user_id = Integer.valueOf(request.getParameter("user_id"));

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

                    Part filePart = request.getPart("image"); // Retrieves <input type="file" name="file">
                    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
                    InputStream fileContent = filePart.getInputStream();

                    int num_rows = Conexion.num("image");
                    File image = crypto.InputStreamToFile(fileContent, "Enc"+String.valueOf(num_rows)+img_name);
                    
                    //File image = new File("C:/Users/Master/Documents/NetBeansProjects/crypto2/src/java/paquete/img/image1.jpg");

                    File llaveAES = ImageEncDec.llaveAES("Enc_key" + num_rows + ".key");
                    
                    /*
                    El código de abajo obtiene la llave publica del id ingresado. Lo hace accediendo a la base de datos.
                    Después, obtenemos en bytes[] la llaveAES cifrada con RSA. CifrarRSA recibirá llavepublica que es
                    la llave publica que obtuvimos en el paso anterior y llaveAES pero en forma de bytes(se hace con
                    el File.readAllBytes(archivollave.toPath()).
                    Despues, guardaremos esa llave cifrada con RSA en la carpeta Temp del proyecto.
                    Generamos un nuevo File con la ruta donde guardamos la llave cifrada con RSA en el paso anterior.
                    */
                    PublicKey llavepublica = Conexion.getPublicKey(id);
                    byte[] keyAEScif = CifradorRSA.CifrarRSA(llavepublica, Files.readAllBytes(llaveAES.toPath()));
                    
                    ImageEncDec.writeToFile("C:/Users/yeyof/Desktop/AES-image-cipher-system/Temp/llaveAESwithRSA" + num_rows + ".key", keyAEScif);
                    File llaveAESwithRSA = new File("C:/Users/yeyof/Desktop/AES-image-cipher-system/Temp/llaveAESwithRSA" + num_rows + ".key");
                    //File llaveAES = new File("C:/Users/Master/Documents/NetBeansProjects/crypto2/src/java/paquete/img/llave6.txt");
                    
                    //ImageEncDec.saveFile(ImageEncDec.encryptPdfFile(llaveAES, ImageEncDec.getFile(image) ) , "C:/Users/Master/Documents/NetBeansProjects/crypto2/Enc/",num_rows+img_name) ;
                    ImageEncDec.saveFile(ImageEncDec.encryptPdfFile(llaveAES, ImageEncDec.getFile(image) ) , "C:/Users/yeyof/Desktop/AES-image-cipher-system/Enc/",num_rows+img_name) ;
                    //File enc_img = new File("C:/Users/Master/Documents/NetBeansProjects/crypto2/Enc/"+num_rows+img_name);
                    File enc_img = new File("C:/Users/yeyof/Desktop/AES-image-cipher-system/Enc/"+num_rows+img_name);
                    int ret = crypto.writeBlob(user_id, img_name, enc_img, llaveAESwithRSA, id);

                    fileContent.close();
                    if (ret == 0) {
                        text="The image could not be saved, with another.";
                        System.out.println("No grabo en la BD");
                    } else if (ret == 1) {
                        text="The image was successfully saved.";
                        System.out.println("Grabo en la BD");
                    } else {
                        text="The image could not be saved, with another.";
                        System.out.println("Hubo un error al grabar en la BD");
                    }

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
        }
        System.out.println(text);
        
        response.setContentType("text/html;charset=UTF-8");
        
        response.getWriter().write("<script>"+
                "alert('"+text+"');"+
                "location='"+page+"';"+
                "</script>");
        //response.sendRedirect(page);
        

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
