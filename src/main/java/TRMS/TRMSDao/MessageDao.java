package TRMS.TRMSDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import TRMS.ConnectionUtil.ConnectionUtil;
import TRMS.TRMSPojos.Message;

public class MessageDao implements CRUD<Message> {

    private PreparedStatement stmt;
    private static Logger log = Logger.getLogger("Web");
    public ConnectionUtil connUtil = new ConnectionUtil();
    
    public void setConnUtil(ConnectionUtil connUtil) {
        this.connUtil = connUtil;
    }

    @Override
    public int insert(Message message) throws SQLException {
        //Prepared SQL statement prototype
        String sql = "INSERT into message (fromEmp, toEmp, message, reqId)"
		+ "values(?,?,?, ?);"; 
        
        try (Connection conn = connUtil.createConnection()){
            stmt = connUtil.createConnection().prepareStatement(sql);
            stmt.setInt(1, message.getFromEmp());
            stmt.setInt(2, message.getToEmp());
            stmt.setString(3, message.getMessage());
            stmt.setInt(4, message.getReqId());

            

            stmt.executeUpdate();
            
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return 0;		
        }
    }

    @Override
    public Message select(int toEmp) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Message> selectAll() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Message> selectAllByEmpId(int toEmp) throws SQLException {
        String sql = "SELECT * FROM message where toEmp = ?;";
        
        List<Message> allMessages = new ArrayList<>();
			
			
		try(Connection conn = connUtil.createConnection()){
			stmt = connUtil.createConnection().prepareStatement(sql);
            stmt.setInt(1,toEmp);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
                Message returnedMessages = new Message(rs.getInt(1),
                rs.getInt(2),
                rs.getInt(3),
                rs.getInt(4),
                rs.getString(5));
					
				allMessages.add(returnedMessages);
					
			}
			return allMessages;
					
		}catch(SQLException e) {
			log.error("SQLException:" + e);
			return null;
		}
    }

    public List<Message> selectAllByEmpPair(int toEmp, int fromEmp) throws SQLException {
        String sql = "SELECT * FROM message where fromEmp = ? and toEmp = ?;";
        
        List<Message> allMessages = new ArrayList<>();
			
			
		try(Connection conn = connUtil.createConnection()){
            stmt = connUtil.createConnection().prepareStatement(sql);
            stmt.setInt(1,fromEmp);
            stmt.setInt(2,toEmp);

			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Message returnedMessages = new Message(rs.getInt(1),
                rs.getInt(2),
                rs.getInt(3),
                rs.getInt(4),
                rs.getString(5));

				allMessages.add(returnedMessages);
					
			}
			return allMessages;
					
		}catch(SQLException e) {
			log.error("SQLException:" + e);
			return null;
		}
    }

    public List<Message> selectAllByReqId(int reqId) throws SQLException {
        String sql = "SELECT * FROM message where reqId = ?;";
        
        List<Message> allMessages = new ArrayList<>();
	
		try(Connection conn = connUtil.createConnection()){
            stmt = connUtil.createConnection().prepareStatement(sql);
            stmt.setInt(1,reqId);;

			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Message returnedMessages = new Message(rs.getInt(1),
                                                        rs.getInt(2),
                                                        rs.getInt(3),
                                                        rs.getInt(4),
                                                        rs.getString(5));
                System.out.println(returnedMessages);
				allMessages.add(returnedMessages);
					
			}
			return allMessages;
					
		}catch(SQLException e) {
			log.error("SQLException:" + e);
			return null;
		}
    }


    @Override
    public boolean update(Message message) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean delete(int t) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    
    
}
