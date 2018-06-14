package paquete;

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
                    File image = crypto.InputStreamToFile(fileContent, String.valueOf(num_rows)+img_name);
                    //File image = new File("C:/Users/Master/Documents/NetBeansProjects/crypto2/src/java/paquete/img/image1.jpg");

                    File llaveAES = ImageEncDec.llaveAES("llave" + num_rows + ".key");
                    //File llaveAES = new File("C:/Users/Master/Documents/NetBeansProjects/crypto2/src/java/paquete/img/llave6.txt");
                    //File llavenueva = ImageEncDec.llaveAES("C:/Users/Master/Documents/NetBeansProjects/crypto2/src/java/paquete/img/llave6");
                    int ret = crypto.writeBlob(user_id, img_name, image, llaveAES, id);
                    if (ret == 0) {
                        System.out.println("No grabo en la BD");
                    } else if (ret == 1) {
                        System.out.println("Grabo en la BD");
                    } else {
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
