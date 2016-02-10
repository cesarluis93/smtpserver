package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connector {
	Connection conn;
	
	public Connector(){
		conn = null; 
	}
	
	public void connect(){
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/stmp_redes", "root", "");
			System.out.println("Connection success");
		} catch (SQLException e) {
			System.out.println(e);
		}	
	}
	
	public ResultSet execute(String query){
		Statement stmt;
		ResultSet rs;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	public int update(String query){
		Statement stmt;
		int rowsAffected;
		try {
			stmt = conn.createStatement();
			rowsAffected = stmt.executeUpdate(query);
			
			return rowsAffected;
		} catch (SQLException e) {
			e.printStackTrace();
			
			return -1;
		}
	}
	
    /**
     * Permite obtener el result set de cierta tabla.
     * @param nombreTabla Nombre de la tabla cuyo resultSet se desea hallar.
     * @return ResultSet con el resultado. Puede obtenerse null si no se concreta la conexión.
     */
    public ResultSet datosDeTabla(String nombreTabla){
        ResultSet resultado = null;
        try{
            Statement instruccionesBD = conn.createStatement();
            resultado = instruccionesBD.executeQuery("SELECT * FROM " + nombreTabla);
        }catch (Exception noConecto){
            System.out.println("Error inesperado en método datosDeTabla, ConexionBD. Contacte al proveedor de su programa.");
        }
        return resultado;
    }
	
}
