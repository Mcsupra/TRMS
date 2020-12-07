package TRMS.TRMSDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import TRMS.ConnectionUtil.ConnectionUtil;
import TRMS.TRMSPojos.Supporting;

public class SupportingDao implements CRUD<Supporting> {

    private PreparedStatement stmt;
    private static Logger log = Logger.getLogger("Web");
    public ConnectionUtil connUtil = new ConnectionUtil();
    
    public void setConnUtil(ConnectionUtil connUtil) {
        this.connUtil = connUtil;
    }
    
    @Override
    public int insert(Supporting sup) throws SQLException {
        //Prepared SQL statement prototype
        String sql = "INSERT into supportingdocs (file_type, file, reqId)"
		+ "values(?,?,?);"; 
        
        try (Connection conn = connUtil.createConnection()){
            stmt = connUtil.createConnection().prepareStatement(sql);
            stmt.setString(1,sup.getFileType());
            stmt.setBytes(2,sup.getFile());
            stmt.setInt(3,sup.getReqId());
            

            stmt.executeUpdate();
            
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return 0;		
        }
    }

    @Override
    public Supporting select(int reqId) throws SQLException {
        //Prepared SQL statement prototype
        String sql = "Select * FROM supportingdocs where reqId = ?;";

        try (Connection conn = connUtil.createConnection()){
            stmt = connUtil.createConnection().prepareStatement(sql);
            stmt.setInt(1,reqId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            
            Supporting returnedDoc = new Supporting(rs.getInt(1),
                                                rs.getString(2),
                                                rs.getBytes(3),
                                                rs.getInt(4));
                                        

            return returnedDoc;

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return null;		
        }
    }

    @Override
    public List<Supporting> selectAll() throws SQLException {
        String sql = "SELECT * FROM supportingdocs;";
        
        List<Supporting> allEmp = new ArrayList<>();
			
			
		try(Connection conn = connUtil.createConnection()){
			stmt = connUtil.createConnection().prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Supporting returnedDocs = new Supporting(rs.getInt(1),
                                                        rs.getString(2),
                                                        rs.getBytes(3),
                                                        rs.getInt(4));
					
				allEmp.add(returnedDocs);
					
			}
			return allEmp;
					
		}catch(SQLException e) {
			log.error("SQLException:" + e);
			return null;
		}
    }

    @Override
    public boolean update(Supporting sup) throws SQLException {
        
        try(Connection conn = connUtil.createConnection()){ 
            //Prepared SQL statement
		    String sql = "UPDATE supportingdocs SET file_type = ? WHERE reqId = ?;";

            stmt = connUtil.createConnection().prepareStatement(sql);
            
            stmt.setString(1, sup.getFileType());
            stmt.setInt(2, sup.getDocId());

            stmt.executeUpdate();
            
            return true;

        } catch (SQLException e) {
            log.error("SQLException:" + e);
            return false;
        }
    }

    @Override
    public boolean delete(int reqId) throws SQLException {
        try(Connection conn = connUtil.createConnection()){
            String sql = "DELETE FROM supportingdocs WHERE reqId = ?;";

            stmt = connUtil.createConnection().prepareStatement(sql);
            
            stmt.setInt(1, reqId);
            stmt.executeUpdate();
            
            return true;
        }catch (SQLException e) {
            log.error("SQLException:" + e);
            return false;
        }
    }
    
}
