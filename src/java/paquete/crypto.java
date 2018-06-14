package paquete;




import paquete.Conexion;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.mysql.jdbc.Connection.*;
import com.mysql.jdbc.util.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import org.apache.tomcat.util.http.fileupload.IOUtils;
/**
 *
 * @author mysqltutorial.org
 */
public class crypto {

    public static File InputStreamToFile(InputStream initialStream,String aux) 
  throws IOException {
  

    File targetFile = new File("C:/Users/Master/Documents/NetBeansProjects/crypto2/Temp/"+aux);
    OutputStream outStream = new FileOutputStream(targetFile);
 
    byte[] buffer = new byte[8 * 1024];
    int bytesRead;
    while ((bytesRead = initialStream.read(buffer)) != -1) {
        outStream.write(buffer, 0, bytesRead);
    }
    initialStream.close();
    outStream.close();
    return targetFile;
}


    public static int writeBlob(int user_id, String img_name,File image, File key, int receiver) throws SQLException {
        // update sql
       
         Connection conn=Conexion.getConexion();
        String updateSQL = "INSERT image (user_id,img_name,image,key2,receiver)"
                        + " VALUES(?,?,?,?,?)";
        int ret = 0;
            

        try ( 
                PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
 
            // read the file
            FileInputStream inputImage = new FileInputStream(image);
            FileInputStream inputKey = new FileInputStream(key);

            // set parameters
            pstmt.setInt(1, user_id);
            pstmt.setString(2, img_name);
            pstmt.setBinaryStream(3, inputImage);
            pstmt.setBinaryStream(4, inputKey);
            pstmt.setInt(5, receiver);
 
            // store the resume file in database
            System.out.println("Reading file " + image.getAbsolutePath());
            System.out.println("Store file in the database.");
            pstmt.executeUpdate();
            ret = 1;
 
        } catch (SQLException | FileNotFoundException e) {
            System.out.println(e.getMessage());
            ret=2;
            System.out.println("crypto: "+ret);
        }
        return ret;
    }

        public static void writeBlob2(int user_id, File pubk, File privk) throws SQLException {
            String selectSQL = "SELECT public_key FROM user WHERE id=?";
 Connection conn=Conexion.getConexion();
        ResultSet rs = null;
        FileOutputStream output = null;
        InputStream input = null;
        File file = null;
        String ret = "";

        try (
                PreparedStatement pstmt = conn.prepareStatement(selectSQL);) {
            // set parameter;
            pstmt.setInt(1, user_id);
            rs = pstmt.executeQuery();
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    String updateSQL = "UPDATE user SET public_key=? , private_key=?"
                        + " WHERE id=?";
 
                    try (
                         Connection conn2=Conexion.getConexion();
                        PreparedStatement pstmt2 = conn2.prepareStatement(updateSQL)) {
                 
                        // read the file
                        FileInputStream inputPublic = new FileInputStream(pubk);
                        FileInputStream inputPrivate = new FileInputStream(privk);
                
                        // set parameters
                        pstmt2.setBinaryStream(1, inputPublic);
                        pstmt2.setBinaryStream(2, inputPrivate);
                        pstmt2.setInt(3, user_id);
                 
                        // store the resume file in database
         
                        System.out.println("Store file in the database.Blob2");
                        pstmt2.executeUpdate();
                        
                 
                    } catch (SQLException | FileNotFoundException e) {
                        System.out.println(e.getMessage());
                        
                    }
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        
    }
    public static int writeBlob3(String user,String pass,File pubk, File privk) throws SQLException {
        // update sql
        Connection conn=Conexion.getConexion();
 
        String updateSQL = "INSERT user (user,pass,public_key,private_key)"
                        + " VALUES(?,?,?,?)";
        int ret = 0;

        try (
                PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
 
            // read the file
            FileInputStream inputPublic = new FileInputStream(pubk);
            FileInputStream inputPrivate = new FileInputStream(privk);

            // set parameters
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.setBinaryStream(3, inputPublic);
            pstmt.setBinaryStream(4, inputPrivate);

 
            // store the resume file in database
            System.out.println("Store file in the database.");
            pstmt.executeUpdate();
            ret = 1;
 
        } catch (SQLException | FileNotFoundException e) {
            System.out.println(e.getMessage());
            ret=2;
            System.out.println("crypto: "+ret);
        }
        return ret;
    }
   public static String readBlob(int candidateId,String name,int action) throws IOException {
        // update sql
        

            String selectSQL = "SELECT * FROM image WHERE id=?";
        ResultSet rs = null;
        FileOutputStream output = null;
        InputStream input = null;
        File file = null;
        String ret = "";

        try (Connection conn=Conexion.getConexion();
                PreparedStatement pstmt = conn.prepareStatement(selectSQL);) {
            // set parameter;
            pstmt.setInt(1, candidateId);
            rs = pstmt.executeQuery();
 
            // write binary stream into file
            int cont = 0;
 
            System.out.println("action: "+action);
            while (rs.next()) {
                if(cont==0){
                    if(action==0)
                        ret="C:/Users/Master/Documents/NetBeansProjects/crypto2/Temp/"+rs.getInt("id")+""+name;
                    else
                        ret="C:/Users/Master/Documents/NetBeansProjects/crypto2/Temp/"+rs.getInt("id")+""+name+".key";
                    file = new File(ret);
                    output = new FileOutputStream(file);
                }
                if(action==0)
                    input = rs.getBinaryStream("image");
                else
                    input = rs.getBinaryStream("key2");
                byte[] buffer = new byte[1024];
                while (input.read(buffer) > 0) {
                    output.write(buffer);
                }
                cont++;
                output.close();
                input.close();
            }
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return ret;
 
    }
   public static String readBlob2(int candidateId,String pubk,String privk) throws IOException {
        // update sql
        

            String selectSQL = "SELECT * FROM user WHERE id=?";
        ResultSet rs = null;
        FileOutputStream outputPub = null;
        FileOutputStream outputPriv = null;
        InputStream inputPub = null;
        InputStream inputPriv = null;
        File filePub = null;
        File filePriv = null;
        String ret = "";

        try (Connection conn=Conexion.getConexion();
                PreparedStatement pstmt = conn.prepareStatement(selectSQL);) {
            // set parameter;
            pstmt.setInt(1, candidateId);
            rs = pstmt.executeQuery();
 
            // write binary stream into file
 
            while (rs.next()) {
                filePub = new File(pubk);
                outputPub = new FileOutputStream(filePub);

                filePriv = new File(privk);
                outputPriv = new FileOutputStream(filePriv);
                
                inputPub = rs.getBinaryStream("public_key");
                inputPriv = rs.getBinaryStream("private_key");

                byte[] buffer1 = new byte[1024];
                while (inputPub.read(buffer1) > 0) {
                    outputPub.write(buffer1);
                }

                byte[] buffer2 = new byte[1024];
                while (inputPriv.read(buffer2) > 0) {
                    outputPriv.write(buffer2);
                }
                outputPriv.close();
                inputPriv.close();

                outputPub.close();
                inputPub.close();
            }
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return ret;
 
    }
    public static void main(String[] args) throws SQLException, IOException {
        
       //String newf= readBlob(1);
   }


}