package TRMS.ConnectionUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    
    public Connection conn;
	
	public Connection createConnection() throws SQLException {
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:9091/TRMS?", "mzide", "yYkRRuaF2c9P");
			return conn;
		}
		catch (SQLException e) {
			System.out.println("Caught SQL exception on jdbc connection");
			return null;
		}
		
		
		
	}
}
