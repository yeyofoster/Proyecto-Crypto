package paquete;

import paquete.Conexion;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        String user = request.getParameter("user2");
        String pass = request.getParameter("pass2");

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

        String selectSQL = "SELECT * FROM user WHERE user=?";
        ResultSet rs = null, rs2 = null, rs3 = null;
        String text = "0";
        int n = -1;

        Connection conn = Conexion.getConexion();
        try (
                PreparedStatement pstmt = conn.prepareStatement(selectSQL);) {
            // set parameter;

            pstmt.setString(1, user);
            rs = pstmt.executeQuery();
            n = Conexion.getNumberOfRows(rs);
            while (rs.next()) {
                System.out.println("id: " + rs.getInt("id"));
            }

        } catch (SQLException e) {
            text = "2";

            System.out.println(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(Signin.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                if (n != 0) {
                    text = "1";
                    System.out.println("nn1: " + n);

                } else {
                    System.out.println("nn2: " + n);

                    text = "3";
                    GenerateKeys myKeys = null;
                    try {
                        myKeys = new GenerateKeys(1024);
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(GenerateSERVLET.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchProviderException ex) {
                        Logger.getLogger(GenerateSERVLET.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    myKeys.createKeys();
                    String pubk = Arrays.toString(myKeys.getPublicKey().getEncoded());
                    String privk = Arrays.toString(myKeys.getPrivateKey().getEncoded());
                    System.out.println("Pubk :" + pubk);
                    System.out.println("Privk :" + privk);

                    int num_rows = Conexion.num("user");
                    myKeys.writeToFile("C:/Users/Master/Documents/NetBeansProjects/crypto2/Llaves/temppubk" + num_rows + ".txt", Base64.getEncoder().encode(myKeys.getPublicKey().getEncoded()));
                    myKeys.writeToFile("C:/Users/Master/Documents/NetBeansProjects/crypto2/Llaves/tempprivk" + num_rows + ".txt", Base64.getEncoder().encode(myKeys.getPrivateKey().getEncoded()));

                    File pubkF = new File("C:/Users/Master/Documents/NetBeansProjects/crypto2/Llaves/temppubk" + num_rows + ".txt");
                    File privkF = new File("C:/Users/Master/Documents/NetBeansProjects/crypto2/Llaves/tempprivk" + num_rows + ".txt");
                    int ret = crypto.writeBlob3(user, passsha256, pubkF, privkF);

                    selectSQL = "SELECT * FROM user WHERE user=? AND pass=?";
                    rs2 = null;
                    try (
                            PreparedStatement pstmt2 = conn.prepareStatement(selectSQL);) {
                        // set parameter;
                        pstmt2.setString(1, user);
                        pstmt2.setString(2, passsha256);
                        rs2 = pstmt2.executeQuery();
                        while (rs2.next()) {
                            HttpSession session = request.getSession();
                            text = rs2.getString("user");
                            session.setAttribute("user", text);
                            session.setAttribute("id", rs2.getInt("id"));
                            session.setMaxInactiveInterval(20 * 60);
                            Cookie userName = new Cookie("user", user);
                            response.addCookie(userName);

                            //Get the encoded URL string
                        }
                    } catch (SQLException e) {
                        text = "6";

                        System.out.println(e.getMessage());
                    } finally {
                        try {
                            if (rs2 != null) {

                                rs2.close();
                            }
                        } catch (SQLException e) {
                            text = "7";
                            System.out.println(e.getMessage());
                        }
                    }
                }
            } catch (SQLException e) {
                text = "5";
                System.out.println(e.getMessage());
            } catch (Exception ex) {
                Logger.getLogger(Signin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("n= " + n);

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
