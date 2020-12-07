package TRMS.ConnectionUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    
    public Connection conn;
	
	public Connection createConnection() throws SQLException {
		
		try {
			Connection conn = DriverManager.getConnection("postgres://nsomjhaz:tkSlT_C7QXRSmAtyboLUCYu4VX0XHzVk@suleiman.db.elephantsql.com:5432/nsomjhaz");
			return conn;
		}
		catch (SQLException e) {
			System.out.println("Caught SQL exception on jdbc connection");
			return null;
		}
		
		
		
	}
}
