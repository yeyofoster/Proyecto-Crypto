/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paquete;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

/**
 *
 * @author Master
 */
public class GenerateSERVLET extends HttpServlet {

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
        GenerateKeys myKeys = null;
        try {
            myKeys = new GenerateKeys(1024);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GenerateSERVLET.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(GenerateSERVLET.class.getName()).log(Level.SEVERE, null, ex);
        }
        String user_id = request.getParameter("user_id");
        String name = request.getParameter("name");
        String page = request.getParameter("page");
        
        myKeys.createKeys();
        String pubk= Arrays.toString(myKeys.getPublicKey().getEncoded());
        String privk= Arrays.toString(myKeys.getPrivateKey().getEncoded());
        System.out.println("Pubk :"+pubk);
        System.out.println("Privk :"+privk);
        Random randomGenerator = new Random();
        int rand=randomGenerator.nextInt(1000);
        myKeys.writeToFile("C:/Users/Master/Documents/NetBeansProjects/crypto2/Llaves/"+name+"pubk"+user_id+rand+".txt", Base64.getEncoder().encode(myKeys.getPublicKey().getEncoded()));
        myKeys.writeToFile("C:/Users/Master/Documents/NetBeansProjects/crypto2/Llaves/"+name+"privk"+user_id+rand+".txt", Base64.getEncoder().encode(myKeys.getPrivateKey().getEncoded()));
        
        File pubkF= new File("C:/Users/Master/Documents/NetBeansProjects/crypto2/Llaves/"+name+"pubk"+user_id+rand+".txt");
        File privkF= new File("C:/Users/Master/Documents/NetBeansProjects/crypto2/Llaves/"+name+"privk"+user_id+rand+".txt");
                    
        try {
            crypto.writeBlob2(Integer.valueOf(user_id),pubkF,privkF);
        } catch (SQLException ex) {
            Logger.getLogger(GenerateSERVLET.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("<script>"+
                "alert('Your public key is: "+pubk+"');"+
                "alert('Your private key is:"+privk+"');"+
                "location='"+page+"';"+
                "</script>");
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
