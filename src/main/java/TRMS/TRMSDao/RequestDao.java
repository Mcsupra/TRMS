package TRMS.TRMSDao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import TRMS.ConnectionUtil.ConnectionUtil;
import TRMS.TRMSPojos.Request;
import TRMS.TRMSPojos.Request.CurrentStatus;
import TRMS.TRMSPojos.Request.eventType;

public class RequestDao implements CRUD<Request> {

    private PreparedStatement stmt;
    private static Logger log = Logger.getLogger("Web");
    public ConnectionUtil connUtil = new ConnectionUtil();
    
    public void setConnUtil(ConnectionUtil connUtil) {
        this.connUtil = connUtil;
    }

    @Override
    public int insert(Request req) throws SQLException {
        
        //Prepared SQL statement prototype
        String sql = "INSERT into Requests (event_date, event_time, event_loc, event_type, event_cost, request_date,"
        +"supervisor, dept_head, benco, request_status, additional_docs) "
		+ "values(?,?,?,?::eventtype,?,?,?,?,?,?::request_status,?) RETURNING reqid;";
        
        try (Connection conn = connUtil.createConnection()){
            stmt = connUtil.createConnection().prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(req.getEventDate()));
			stmt.setTime(2, Time.valueOf(req.getEventTime()));
            stmt.setString(3, req.getEventLoc());
            stmt.setString(4, req.getEventType().toString());
			stmt.setDouble(5, req.getEvent_cost());
            stmt.setDate(6, Date.valueOf(req.getRequestDate()));
            stmt.setBoolean(7, req.getSupervisor());
			stmt.setBoolean(8, req.getDeptHead());
            stmt.setBoolean(9, req.getBenco());
            stmt.setString(10, req.getCurrentStatus().toString());
			stmt.setBoolean(11, req.getAdditional_docs());

            ResultSet rs = stmt.executeQuery();
            rs. next();

            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return 0;		
        }
    }

    public int insertWithEmpId(Request req,int empId) throws SQLException {
        
        //Prepared SQL statement prototype
        String sql = "INSERT into Requests (event_date, event_time, event_loc, event_type, event_cost, request_date,"
        +"supervisor, dept_head, benco, request_status, additional_docs,empid) "
		+ "values(?,?,?,?::eventtype,?,?,?,?,?,?::request_status,?,?) RETURNING reqid;";
        
        try (Connection conn = connUtil.createConnection()){
            stmt = connUtil.createConnection().prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(req.getEventDate()));
			stmt.setTime(2, Time.valueOf(req.getEventTime()));
            stmt.setString(3, req.getEventLoc());
            stmt.setString(4, req.getEventType().toString());
			stmt.setDouble(5, req.getEvent_cost());
            stmt.setDate(6, Date.valueOf(req.getRequestDate()));
            stmt.setBoolean(7, req.getSupervisor());
			stmt.setBoolean(8, req.getDeptHead());
            stmt.setBoolean(9, req.getBenco());
            stmt.setString(10, req.getCurrentStatus().toString());
            stmt.setBoolean(11, req.getAdditional_docs());
            stmt.setInt(12, empId);

            ResultSet rs = stmt.executeQuery();
            rs. next();

            return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return 0;		
        }
    }

    @Override
    public Request select(int reqId) throws SQLException {
        //Prepared SQL statement prototype
        String sql = "Select * FROM requests where reqid = ?;";

        try (Connection conn = connUtil.createConnection()){
            stmt = connUtil.createConnection().prepareStatement(sql);
            stmt.setInt(1,reqId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            
            Request returnedReq = new Request(rs.getInt(1),
                                            rs.getString(2),
                                            rs.getString(3),
                                            rs.getString(4),
                                            eventType.valueOf(rs.getString(5)),
                                            rs.getDouble(6),
                                            rs.getString(7),
                                            rs.getBoolean(8),
                                            rs.getBoolean(9),
                                            rs.getBoolean(10),
                                            CurrentStatus.valueOf(rs.getString(11)),
                                            rs.getBoolean(12),
                                            rs.getInt(13));
            return returnedReq;

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return null;		
        }
    }

    public List<Request> selectByEmpId(int empId) throws SQLException {
        //Prepared SQL statement prototype
        String sql = "select * FROM requests where empid = ?;";
        List<Request> allReq = new ArrayList<>();
        
        try (Connection conn = connUtil.createConnection()){
            stmt = connUtil.createConnection().prepareStatement(sql);
            stmt.setInt(1,empId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Request returnedReq = new Request(rs.getInt(1),
                                                rs.getString(2),
                                                rs.getString(3),
                                                rs.getString(4),
                                                eventType.valueOf(rs.getString(5)),
                                                rs.getDouble(6),
                                                rs.getString(7),
                                                rs.getBoolean(8),
                                                rs.getBoolean(9),
                                                rs.getBoolean(10),
                                                CurrentStatus.valueOf(rs.getString(11)),
                                                rs.getBoolean(12),
                                                rs.getInt(13));
                
                allReq.add(returnedReq);
            }
            return allReq;

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return null;		
        }
    }

    public List<Request> selectByDepartment(int department) throws SQLException {
        //Prepared SQL statement prototype
        String sql = "select * from requests where empid in (select empid from employee where department = ?);";
        List<Request> allReq = new ArrayList<>();
        
        try (Connection conn = connUtil.createConnection()){
            stmt = connUtil.createConnection().prepareStatement(sql);
            stmt.setInt(1,department);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Request returnedReq = new Request(rs.getInt(1),
                                                rs.getString(2),
                                                rs.getString(3),
                                                rs.getString(4),
                                                eventType.valueOf(rs.getString(5)),
                                                rs.getDouble(6),
                                                rs.getString(7),
                                                rs.getBoolean(8),
                                                rs.getBoolean(9),
                                                rs.getBoolean(10),
                                                CurrentStatus.valueOf(rs.getString(11)),
                                                rs.getBoolean(12),
                                                rs.getInt(13));  
                allReq.add(returnedReq);
            }
            return allReq;

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return null;		
        }
    }

    public int getEmpIdByReq(int reqId) throws SQLException {
        //Prepared SQL statement prototype
        String sql = "select empId FROM requests where reqid = ?;";
        int empId;
        
        try (Connection conn = connUtil.createConnection()){
            stmt = connUtil.createConnection().prepareStatement(sql);
            stmt.setInt(1,reqId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            empId = rs.getInt(1);
            return empId;

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return 0;		
        }
    }

    @Override
    public List<Request> selectAll() throws SQLException {
        List<Request> allReq = new ArrayList<>();
			
		String sql = "SELECT * FROM requests;";
			
		try(Connection conn = connUtil.createConnection()){
			stmt = connUtil.createConnection().prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Request returnedReq = new Request(rs.getInt(1),
                                                rs.getString(2),
                                                rs.getString(3),
                                                rs.getString(4),
                                                eventType.valueOf(rs.getString(5)),
                                                rs.getDouble(6),
                                                rs.getString(7),
                                                rs.getBoolean(8),
                                                rs.getBoolean(9),
                                                rs.getBoolean(10),
                                                CurrentStatus.valueOf(rs.getString(11)),
                                                rs.getBoolean(12),
                                                rs.getInt(13));					
				allReq.add(returnedReq);
					
			}
			return allReq;
					
		}catch(SQLException e) {
			log.error("SQLException:" + e);
			return null;
		}
    }

    @Override
    public boolean update(Request req) throws SQLException {
        
        try(Connection conn = connUtil.createConnection()){
            //Prepared SQL statement
            String sql = "UPDATE requests SET event_date = ?, event_time = ?, event_loc = ?, event_type = ?::eventtype, event_cost = ?, request_date = ?,"
            +"supervisor = ?, dept_head = ?, benco = ?, request_status = ?::request_status, additional_docs = ? "
            +" WHERE reqid = ?;";

            stmt = connUtil.createConnection().prepareStatement(sql);
            
            stmt.setDate(1, Date.valueOf(req.getEventDate()));
			stmt.setTime(2, Time.valueOf(req.getEventTime()));
            stmt.setString(3, req.getEventLoc());
            stmt.setString(4, req.getEventType().toString());
			stmt.setDouble(5, req.getEvent_cost());
            stmt.setDate(6, Date.valueOf(req.getRequestDate()));
            stmt.setBoolean(7, req.getSupervisor());
			stmt.setBoolean(8, req.getDeptHead());
            stmt.setBoolean(9, req.getBenco());
            stmt.setString(10, req.getCurrentStatus().toString());
			stmt.setBoolean(11, req.getAdditional_docs());
            stmt.setInt(12, req.getReqId());
            
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException in application:" + e);
            return false;		
        }
    }

    public boolean updateReqStatus(Request req) throws SQLException {
        
        try(Connection conn = connUtil.createConnection()){
            //Prepared SQL statement
            String sql = "UPDATE requests SET request_status = ?::request_status "
            +" WHERE reqid = ?;";

            stmt = connUtil.createConnection().prepareStatement(sql);
            
            stmt.setString(1, req.getCurrentStatus().toString());
            stmt.setInt(2, req.getReqId());
            
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException in application:" + e);
            return false;		
        }
    }

    public boolean updateSupStatus(Request req) throws SQLException {
        
        try(Connection conn = connUtil.createConnection()){
            //Prepared SQL statement
            String sql = "UPDATE requests SET supervisor = ?::boolean"
            +" WHERE reqid = ?;";
            Boolean bool = req.getSupervisor();
            stmt = connUtil.createConnection().prepareStatement(sql);
            
            stmt.setString(1, bool.toString());
            stmt.setInt(2, req.getReqId());
            
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException in application:" + e);
            return false;		
        }
    }

    public boolean updateDHStatus(Request req) throws SQLException {
        
        try(Connection conn = connUtil.createConnection()){
            //Prepared SQL statement
            String sql = "UPDATE requests SET dept_head = ?::boolean"
            +" WHERE reqid = ?;";
            Boolean bool = req.getDeptHead();
            stmt = connUtil.createConnection().prepareStatement(sql);
            
            stmt.setString(1, bool.toString());
            stmt.setInt(2, req.getReqId());
            
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException in application:" + e);
            return false;		
        }
    }

    public boolean updateBencoStatus(Request req) throws SQLException {
        
        try(Connection conn = connUtil.createConnection()){
            //Prepared SQL statement
            String sql = "UPDATE requests SET benco = ?::boolean"
            +" WHERE reqid = ?;";
            Boolean bool = req.getBenco();
            stmt = connUtil.createConnection().prepareStatement(sql);
            
            stmt.setString(1, bool.toString());
            stmt.setInt(2, req.getReqId());
            
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException in application:" + e);
            return false;		
        }
    }

    @Override
    public boolean delete(int reqId) throws SQLException {
        try(Connection conn = connUtil.createConnection()){
            String sql = "DELETE FROM requests WHERE reqid = ?;";

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
