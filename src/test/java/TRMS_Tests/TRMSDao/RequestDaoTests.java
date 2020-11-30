package TRMS_Tests.TRMSDao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import TRMS.ConnectionUtil.ConnectionUtil;
import TRMS.TRMSDao.RequestDao;
import TRMS.TRMSPojos.Employee;
import TRMS.TRMSPojos.Request;
import TRMS.TRMSPojos.Employee.Title;
import TRMS.TRMSPojos.Request.CurrentStatus;
import TRMS.TRMSPojos.Request.eventType;

@RunWith(MockitoJUnitRunner.class)
public class RequestDaoTests {

    private static Logger log = Logger.getLogger("Web");
	
	@Mock
	private static ConnectionUtil connUtil;
	
	@Mock
	private static Connection fakeConn;
	
	private static PreparedStatement stmt;
	
	private static PreparedStatement spy;
	
    private static Connection realConnection;

    public static Request testReq = new Request();

    public static RequestDao reqDao = new RequestDao();

	public static Employee testEmp = new Employee();

    @BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
        realConnection = new ConnectionUtil().createConnection();
		testReq = new Request(0, "2021-03-18", "11:00:00","Super Center", eventType.SEMINAR, 536.42, "2021-01-20", false, false, false, CurrentStatus.PENDING, false,0);
		testEmp = new Employee(0, "Michael", "Zide", Title.SUPERVISOR, 1000, 2);

		String sql = "INSERT into EMPLOYEE (empid, first_name, last_name, title, balance, department) "
		+ "values(0, 'Michael', 'Zide', 'SUPERVISOR', 1000, 2);";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();	
	
        sql = "INSERT into Requests (reqid, event_date, event_time, event_loc, event_type, event_cost, request_date,"
        +"supervisor, dept_head, benco, request_status, additional_docs,empid) "
		+ "values(0, '2021-03-18', '11:00:00','Super Center', 'SEMINAR', 536.42, '2021-01-20', false, false, false, 'PENDING', false,0);"; 
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();	
	}

	@After
	public void tearDown() throws Exception {
		
		String sql = "delete from requests where reqid = 0;";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();

		sql = "delete from employee where empid = 0;";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();

		if (realConnection != null) {
			realConnection.close();
		}
		
	}

	@Test
	public void insertRequestTest() throws SQLException {
		
        //Prepared SQL statement prototype
        String sql = "INSERT into Requests (event_date, event_time, event_loc, event_type, event_cost, request_date,"
        +"supervisor, dept_head, benco, request_status, additional_docs) "
		+ "values(?,?,?,?::eventtype,?,?,?,?,?,?::request_status,?);";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
            reqDao.insert(testReq);
            
			verify(spy).setDate(1, Date.valueOf(testReq.getEventDate()));
			verify(spy).setTime(2, Time.valueOf(testReq.getEventTime()));
            verify(spy).setString(3, testReq.getEventLoc());
            verify(spy).setString(4, testReq.getEventType().toString());
			verify(spy).setDouble(5, testReq.getEvent_cost());
            verify(spy).setDate(6, Date.valueOf(testReq.getRequestDate()));
            verify(spy).setBoolean(7, testReq.getSupervisor());
			verify(spy).setBoolean(8, testReq.getDeptHead());
            verify(spy).setBoolean(9, testReq.getBenco());
            verify(spy).setString(10, testReq.getCurrentStatus().toString());
			verify(spy).setBoolean(11, testReq.getAdditional_docs());
            
			verify(spy).executeUpdate();

		}catch (SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}finally {
			try {
				stmt = realConnection.prepareStatement("DELETE from requests WHERE event_loc = 'Super Center'");
				stmt.executeUpdate();
			}catch(SQLException e) {
				log.error("SQL Exception:" + e);
				fail("Error: Could not remove added requests");
			}
		}	 
		
	}
	
	@Test
	public void selectRequestTest() throws SQLException {
		//Prepared SQL statement prototype
        String sql = "Select * FROM requests where reqid = ?;";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}

		try {
			Request sample = reqDao.select(testReq.getReqId());

			verify(spy).setInt(1, testReq.getReqId());
			verify(spy).executeQuery();

			assertEquals(testReq, sample);

		}catch (SQLException e) {
			log.error("SQL Exception:" + e);
		}

	}

	@Test
	public void selectAllRequestsTest() throws SQLException {
		
		List<Request> allReq = new ArrayList<>();
		
		//Baseline of test is number in table
		String sql = "SELECT count(*) FROM requests";
		int num_in_table = -1;

		try {
			stmt = realConnection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			num_in_table = rs.getInt(1);
			
		}catch (SQLException e) {
			log.error("SQL Exception:" + e);
			System.out.println("SQLException thrown: " + e.toString());
		}
		
		
		//Prepared SQL statement prototype*/
		sql = "SELECT * FROM requests;";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			allReq = reqDao.selectAll();
			
			verify(spy).executeQuery();
			if (allReq.size() != num_in_table) {
				fail("Error: Queried data does not match current DB config");
			}
				
			for (Request r: allReq) {
				assertFalse("Non-nullable reqid is null",  r.getReqId() < 0);
				assertFalse("Non-nullable event date is null","".equals(r.getEventDate()));
                assertFalse("Non-nullable event time is null","".equals(r.getEventTime()));
                assertFalse("Non-nullable event loc is null","".equals(r.getEventLoc()));
                assertFalse("Non-nullable event type is null",  "".equals(r.getEventType().toString()));
				assertFalse("Non-nullable cost is null", r.getEvent_cost() < 0);
                assertFalse("Non-nullable request date is null","".equals(r.getRequestDate()));
                assertFalse("Non-nullable status is null","".equals(r.getCurrentStatus().toString()));
			}

			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}
	}
	
	
	@Test
	public void updateRequestTest() throws SQLException {
		
		//Prepared SQL statement
		String sql = "UPDATE requests SET event_date = ?, event_time = ?, event_loc = ?, event_type = ?::eventtype, event_cost = ?, request_date = ?,"
        +"supervisor = ?, dept_head = ?, benco = ?, request_status = ?::request_status, additional_docs = ? "
        +" WHERE reqid = ?;";
		
		try {
			preparedHelper(sql);
		} catch (SQLException e) {
			log.error("SQL Exception in test:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			testReq.setEventDate("2021-03-25");
			testReq.setEventTime("13:00:00");
            testReq.setEventLoc("Mega Center");
            testReq.setEventType(eventType.OTHER);
			testReq.setEvent_cost(451.12);
            testReq.setRequestDate("2021-01-23");
            testReq.setSupervisor(true);
			testReq.setDeptHead(true);
            testReq.setBenco(true);
            testReq.setCurrentStatus(CurrentStatus.URGENT);
			testReq.setAdditional_docs(true);
			
			reqDao.update(testReq);
			
			verify(spy).setDate(1, Date.valueOf(testReq.getEventDate()));
			verify(spy).setTime(2, Time.valueOf(testReq.getEventTime()));
            verify(spy).setString(3, testReq.getEventLoc());
            verify(spy).setString(4, testReq.getEventType().toString());
			verify(spy).setDouble(5, testReq.getEvent_cost());
            verify(spy).setDate(6, Date.valueOf(testReq.getRequestDate()));
            verify(spy).setBoolean(7, testReq.getSupervisor());
			verify(spy).setBoolean(8, testReq.getDeptHead());
            verify(spy).setBoolean(9, testReq.getBenco());
            verify(spy).setString(10, testReq.getCurrentStatus().toString());
			verify(spy).setBoolean(11, testReq.getAdditional_docs());
            verify(spy).setInt(12, testReq.getReqId());
            
			verify(spy).executeUpdate();
			
			//Pull modified employee object from database for comparison
			stmt = realConnection.prepareStatement("SELECT * FROM requests WHERE reqid = ?;");
			stmt.setInt(1, testReq.getReqId());
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
            Request reqMod = new Request(rs.getInt(1),
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
           
            
			assertEquals("Database object does not match as modified", testReq, reqMod);
			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
	}
	
	@Test
	public void deleteRequestTest() throws SQLException {
		
		String sql = "INSERT into Requests (reqid, event_date, event_time, event_loc, event_type, event_cost, request_date,"
        +"supervisor, dept_head, benco, request_status, additional_docs, empid) "
		+ "values(-1, '2021-03-18', '11:00:00','Super Center', 'SEMINAR', 536.42, '2021-01-20', false, false, false, 'PENDING', false, 0);"; 
		
		Request temp = new Request(-1, "2021-03-18", "11:00:00","Super Center", eventType.SEMINAR, 536.42, "2021-01-20", false, false, false, CurrentStatus.PENDING, false, 0);

		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();	
		
		sql = "DELETE FROM requests WHERE reqid = ?;";


		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}

		try {
			reqDao.delete(temp.getReqId());
			
			verify(spy).setInt(1, temp.getReqId());
			verify(spy).executeUpdate();
			
			stmt = realConnection.prepareStatement(sql);
			stmt.setInt(1, temp.getReqId());
			assertEquals("Object was not deleted properly", 0, stmt.executeUpdate());
			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}
		
	}
	
	
	private void preparedHelper(String sql) throws SQLException {
		
		reqDao.setConnUtil(connUtil);

		//creating a real stmt from a connection
		stmt = realConnection.prepareStatement(sql); 
		
		//spying on that real stmt
		spy = Mockito.spy(stmt);
		
		//mock our connection and util, so we will only use the stmt we are spying on
		when(connUtil.createConnection()).thenReturn(fakeConn);
		when(fakeConn.prepareStatement(sql)).thenReturn(spy);
		
		
	}
    
}
