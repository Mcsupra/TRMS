package TRMS.ConnectionUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    
    public Connection conn;
	
	public Connection createConnection() throws SQLException {
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:postgresql://trms-postrges.postgres.database.azure.com:5432/TRMS?user=mzide@trms-postrges&password=yYkRRuaF2c9P&sslmode=require");
			return conn;
		}
		catch (SQLException e) {
			System.out.println("Caught SQL exception on jdbc connection");
			return null;
		}
		
		
		
	}
}
