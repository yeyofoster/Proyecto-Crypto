package paquete;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {

    public static Connection getConexion(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url="jdbc:mysql://localhost:3306/crypto";
            String usuario="root";
            String clave="n0m3l0";
             System.out.println("It connects chido");
            return DriverManager.getConnection(url,usuario,clave);          
        }catch(SQLException ex){
            ex.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
public static int num(String table) throws Exception {
    try {

        Connection connect = getConexion();

        // Statements allow to issue SQL queries to the database
        Statement statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery("select count(*) from "+table);

        while (resultSet.next()) {
            int ret=resultSet.getInt(1);
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

}
