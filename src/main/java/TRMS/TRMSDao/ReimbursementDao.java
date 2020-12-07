package TRMS.TRMSDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import TRMS.ConnectionUtil.ConnectionUtil;
import TRMS.TRMSPojos.Reimbursement;
import TRMS.TRMSPojos.Reimbursement.status;

public class ReimbursementDao implements CRUD<Reimbursement> {

    private PreparedStatement stmt;
    private static Logger log = Logger.getLogger("Web");
    public ConnectionUtil connUtil = new ConnectionUtil();
    
    public void setConnUtil(ConnectionUtil connUtil) {
        this.connUtil = connUtil;
    }

    @Override
    public int insert(Reimbursement reimb) throws SQLException {
        
        //Prepared SQL statement prototype
        String sql = "INSERT into REIMBURSEMENT (reimbur_status, projected_amount, actual_amount, reqid) "
		+ "values(?::reimbur_status,?,?,?);";
        
        try (Connection conn = connUtil.createConnection()){
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,reimb.getStatus().toString());
            stmt.setDouble(2,reimb.getProjectedAmount());
            stmt.setDouble(3,reimb.getActualAmount());
            stmt.setInt(4,reimb.getReqId());

            stmt.executeUpdate();
            
            return 1;

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return 0;		
        }
    }

    @Override
    public Reimbursement select(int remibId) throws SQLException {
        
        //Prepared SQL statement prototype
        String sql = "Select * FROM reimbursement where reimburid = ?;";

        try (Connection conn = connUtil.createConnection()){
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1,remibId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            
            Reimbursement returnedReimbur = new Reimbursement(rs.getInt(1),
                                                status.valueOf(rs.getString(2)),
                                                rs.getDouble(3),
                                                rs.getDouble(4),
                                                rs.getInt(5));

            return returnedReimbur;

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return null;		
        }
    }

    public Reimbursement selectByReqId(int reqId) throws SQLException {
        
        //Prepared SQL statement prototype
        String sql = "Select * FROM reimbursement where reqid = ?;";

        try (Connection conn = connUtil.createConnection()){
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1,reqId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            
            Reimbursement returnedReimbur = new Reimbursement(rs.getInt(1),
                                                status.valueOf(rs.getString(2)),
                                                rs.getDouble(3),
                                                rs.getDouble(4),
                                                rs.getInt(5));

            return returnedReimbur;

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return null;		
        }
    }

    @Override
    public List<Reimbursement> selectAll() throws SQLException {
        
        //Prepared SQL statement prototype*/
        String sql = "SELECT * FROM reimbursement;";
        
        List<Reimbursement> allReimbur = new ArrayList<>();
			
		try(Connection conn = connUtil.createConnection()){
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Reimbursement returnedReimbur = new Reimbursement(rs.getInt(1),
                                                                status.valueOf(rs.getString(2)),
                                                                rs.getDouble(3),
                                                                rs.getDouble(4),
                                                                rs.getInt(5));					
				allReimbur.add(returnedReimbur);
					
			}
			return allReimbur;
					
		}catch(SQLException e) {
			log.error("SQLException:" + e);
			return null;
		}
    }

    @Override
    public boolean update(Reimbursement reimb) throws SQLException {
        
        System.out.println(reimb);

        try(Connection conn = connUtil.createConnection()){ 
            //Prepared SQL statement
            String sql = "UPDATE reimbursement SET reimbur_status = ?::reimbur_status, projected_amount = ?, actual_amount = ?"
            + " WHERE reimburid = ?;";

            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, reimb.getStatus().toString());
            stmt.setDouble(2, reimb.getProjectedAmount());
            stmt.setDouble(3, reimb.getActualAmount());
            stmt.setInt(4, reimb.getReimburId());

            stmt.executeUpdate();
            
            return true;

        } catch (SQLException e) {
            log.error("SQLException:" + e);
            return false;
        }
    }

    @Override
    public boolean delete(int remibId) throws SQLException {
        
        try(Connection conn = connUtil.createConnection()){
            String sql = "DELETE FROM reimbursement WHERE reimburid = ?;";

            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, remibId);
            stmt.executeUpdate();
            
            return true;
        }catch (SQLException e) {
            log.error("SQLException:" + e);
            return false;
        }
    }
    
}
