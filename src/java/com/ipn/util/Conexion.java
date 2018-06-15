package com.ipn.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

public class Conexion {

    public static Connection getConexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/crypto";
            String usuario = "root";
            //String clave="n0m3l0";
            String clave = "y3y0mysql";
            System.out.println("It connects chido");
            return DriverManager.getConnection(url, usuario, clave);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static int num(String table) throws Exception {
        try {

            Connection connect = getConexion();

            // Statements allow to issue SQL queries to the database
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery("select count(*) from " + table);

            while (resultSet.next()) {
                int ret = resultSet.getInt(1);
                return ret;
            }
            resultSet.close();
        } catch (Exception e) {
            System.out.println("ERROR. Reading rows");
        }
        return 0;
    }

    public static int getNumberOfRows(ResultSet r) throws Exception {
        Connection conn = getConexion();
        Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        r.last();
        int count = r.getRow();
        r.beforeFirst();
        return count;
    }

    public static PublicKey getPublicKey(int id_user) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        String selectSQL = "SELECT * FROM user WHERE id=?";
        Connection conn = Conexion.getConexion();
        ResultSet rs = null;
        byte[] readblob;
        Blob pubkey;
        try (
                PreparedStatement pstmt = conn.prepareStatement(selectSQL);) {
            // set parameter;
            pstmt.setInt(1, id_user);
            rs = pstmt.executeQuery();
            System.out.println("Si hizo el query");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            pubkey = rs.getBlob("public_key");
            readblob = pubkey.getBytes(1, (int)pubkey.length());
            System.out.println(readblob);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(readblob));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            System.out.println("Obtuvimos la llave desde la base");
            conn.close();
            rs.close();
            return kf.generatePublic(spec);
        }
    }

    public static void main(String[] args) {
        
    }
}
