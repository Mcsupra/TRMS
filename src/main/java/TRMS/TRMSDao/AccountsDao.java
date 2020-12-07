package TRMS.TRMSDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import TRMS.ConnectionUtil.ConnectionUtil;
import TRMS.TRMSPojos.Account;

public class AccountsDao implements CRUD<Account> {

    private PreparedStatement stmt;
    private static Logger log = Logger.getLogger("Web");
    public ConnectionUtil connUtil = new ConnectionUtil();

    public void setConnUtil(ConnectionUtil connUtil) {
        this.connUtil = connUtil;
    }

    @Override
    public int insert(Account acct) throws SQLException {
        String sql = "INSERT into accounts (username, passphrase, empid) "
                + "values(?,?,?);";

        try (Connection conn = connUtil.createConnection()){
            stmt = connUtil.createConnection().prepareStatement(sql);
            stmt.setString(1,acct.getUsername());
            stmt.setString(2,acct.getPassphrase());
            stmt.setInt(3, acct.getEmpId());

            stmt.executeUpdate();
            
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return 0;		
        }
    }

    @Override
    public Account select(int empId) throws SQLException {
        //Prepared SQL statement prototype
        String sql = "Select * from Accounts where empid = ?;";

        try (Connection conn = connUtil.createConnection()){
            stmt = connUtil.createConnection().prepareStatement(sql);
            stmt.setInt(1,empId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            
            Account returnedAcct = new Account(rs.getString(1),
                                                rs.getString(2),
                                                rs.getInt(3));

            return returnedAcct;

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return null;		
        }
    }

    public int getEmpId(String username) throws SQLException {
        //Prepared SQL statement prototype
        String sql = "Select empid from Accounts where username = ?;";

        try (Connection conn = connUtil.createConnection()){
            stmt = connUtil.createConnection().prepareStatement(sql);
            stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return -999;		
        }
    }

    public Account selectByUsername(String username) throws SQLException {
        //Prepared SQL statement prototype
        String sql = "Select * from Accounts where username = ?;";

        try (Connection conn = connUtil.createConnection()){
            stmt = connUtil.createConnection().prepareStatement(sql);
            stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            
            Account returnedAcct = new Account(rs.getString(1),
                                                rs.getString(2),
                                                rs.getInt(3));

            return returnedAcct;

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return null;		
        }
    }

    @Override
    public List<Account> selectAll() throws SQLException {
        List<Account> allAccts = new ArrayList<>();
			
		String sql = "SELECT * FROM accounts;";
			
		try(Connection conn = connUtil.createConnection()){
			stmt = connUtil.createConnection().prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Account returnedAcct = new Account(rs.getString(1),
                                                rs.getString(2),
                                                rs.getInt(3));
					
				allAccts.add(returnedAcct);
					
			}
			return allAccts;
					
		}catch(SQLException e) {
			log.error("SQLException:" + e);
			return null;
		}
    }

    @Override
    public boolean update(Account acct) throws SQLException {
        
        try(Connection conn = connUtil.createConnection()){ 
            //Prepared SQL statement
            String sql = "UPDATE accounts SET username = ?, passphrase= ?"
    		+ " WHERE empid = ?;";

            stmt = connUtil.createConnection().prepareStatement(sql);
            
            stmt.setString(1, acct.getUsername());
            stmt.setString(2, acct.getPassphrase());
            stmt.setInt(3, acct.getEmpId());

            stmt.executeUpdate();
            
            return true;
        } catch (SQLException e) {
            log.error("SQLException:" + e);
            return false;
        }
    }

    @Override
    public boolean delete(int empId) throws SQLException {
        
        try(Connection conn = connUtil.createConnection()){
            String sql = "DELETE FROM accounts WHERE empid = ?;";

            stmt = connUtil.createConnection().prepareStatement(sql);
            
            stmt.setInt(1, empId);
            stmt.executeUpdate();
            
            return true;
        }catch (SQLException e) {
            log.error("SQLException:" + e);
            return false;
        }
    }
    
}
