package TRMS_Tests.TRMSDao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import TRMS.TRMSDao.ReimbursementDao;
import TRMS.TRMSPojos.Reimbursement;
import TRMS.TRMSPojos.Reimbursement.status;



@RunWith(MockitoJUnitRunner.class)
public class ReimbursementDaoTests {

    private static Logger log = Logger.getLogger("Web");
	
	@Mock
	private static ConnectionUtil connUtil;
	
	@Mock
	private static Connection fakeConn;
	
	private static PreparedStatement stmt;
	
	private static PreparedStatement spy;
	
    private static Connection realConnection;
    
	public static Reimbursement testReimb = new Reimbursement();

	public static ReimbursementDao reimbDao = new ReimbursementDao();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
        realConnection = new ConnectionUtil().createConnection();
		testReimb = new Reimbursement(0, status.PENDING, 124.33, 0.00,0);

	
		String sql = "INSERT into REIMBURSEMENT (reimburid, reimbur_status, projected_amount, actual_amount) "
		+ "values(0, 'PENDING', 124.33, 0.00);"; 
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();	
	}

	@After
	public void tearDown() throws Exception {
		
		String sql = "delete from reimbursement where reimburid = 0;";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();

		if (realConnection != null) {
			realConnection.close();
		}
		
	}

	@Test
	public void insertReimbursementTest() throws SQLException {
	
        //Prepared SQL statement prototype
        String sql = "INSERT into REIMBURSEMENT (reimbur_status, projected_amount, actual_amountm, reqid) "
		+ "values(?::reimbur_status,?,?,?);";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
            reimbDao.insert(testReimb);
            
			verify(spy).setString(1, testReimb.getStatus().toString());
			verify(spy).setDouble(2, testReimb.getProjectedAmount());
			verify(spy).setDouble(3, testReimb.getActualAmount());
			verify(spy).setInt(4, testReimb.getReqId());
            
			verify(spy).executeUpdate();

		}catch (SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}finally {
			try {
				stmt = realConnection.prepareStatement("DELETE from reimbursement WHERE projected_amount = 124.33");
				stmt.executeUpdate();
			}catch(SQLException e) {
				log.error("SQL Exception:" + e);
				fail("Error: Could not remove added reimbursement");
			}
		}	 
		
	}
	
	@Test
	public void selectReimbursementTest() throws SQLException {
		//Prepared SQL statement prototype
        String sql = "Select * FROM reimbursement where reimburid = ?;";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}

		try {
			Reimbursement sample = reimbDao.select(testReimb.getReimburId());

			verify(spy).setInt(1, testReimb.getReimburId());
			verify(spy).executeQuery();

			assertEquals(testReimb, sample);

		}catch (SQLException e) {
			log.error("SQL Exception:" + e);
		}

	}

	@Test
	public void selectAllReimbursementsTest() throws SQLException {
		
		List<Reimbursement> allReimbur = new ArrayList<>();
		
		//Baseline of test is number in table
		String sql = "SELECT count(*) FROM reimbursement";
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
		sql = "SELECT * FROM reimbursement;";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			allReimbur = reimbDao.selectAll();
			
			verify(spy).executeQuery();
			if (allReimbur.size() != num_in_table) {
				fail("Error: Queried data does not match current DB config");
			}
				
			for (Reimbursement r: allReimbur) {
				assertFalse("Non-nullable empId is null",  r.getReimburId() < 0);
				assertFalse("Non-nullable status is null","".equals(r.getStatus().toString()));
                assertFalse("Non-nullable projected amount is null",r.getProjectedAmount() < 0);
                assertFalse("Non-nullable actual amount is null",r.getActualAmount() < 0);
			}

			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}
	}
	
	
	@Test
	public void updateReimbursementTest() throws SQLException {
		
		//Prepared SQL statement
		String sql = "UPDATE reimbursement SET reimbur_status = ?::reimbur_status, projected_amount = ?, actual_amount = ?"
            + " WHERE reimburid = ?;";
		
		try {
			preparedHelper(sql);
		} catch (SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			testReimb.setStatus(status.APPROVED);
			testReimb.setProjectedAmount(0.00);
			testReimb.setActualAmount(124.33); 
			
			reimbDao.update(testReimb);
			
			verify(spy).setString(1, testReimb.getStatus().toString());
			verify(spy).setDouble(2, testReimb.getProjectedAmount());
			verify(spy).setDouble(3, testReimb.getActualAmount());
			verify(spy).setInt(4, testReimb.getReimburId());
			
			verify(spy).executeUpdate();
			
			//Pull modified employee object from database for comparison
			stmt = realConnection.prepareStatement("SELECT * FROM reimbursement WHERE reimburid = ?;");
			stmt.setInt(1, testReimb.getReimburId());
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
            Reimbursement reimburMod = new Reimbursement(rs.getInt(1),
                                                        status.valueOf(rs.getString(2)),
                                                        rs.getDouble(3),
														rs.getDouble(4),
														rs.getInt(5));
           
			assertEquals("Database object does not match as modified", testReimb, reimburMod);
			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
	}
	
	@Test
	public void deleteReimbursementTest() throws SQLException {
		
		String sql = "INSERT into REIMBURSEMENT (reimburid, reimbur_status, projected_amount, actual_amount) "
		+ "values(-1, 'DENIED', 124.33, 0.00);"; 
		
		Reimbursement temp = new Reimbursement(-1, status.DENIED, 456.87, 254.14,0);

		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();	
		
		sql = "DELETE FROM reimbursement WHERE reimburid = ?;";


		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}

		try {
			reimbDao.delete(temp.getReimburId());
			
			verify(spy).setInt(1, temp.getReimburId());
			verify(spy).executeUpdate();
			
			stmt = realConnection.prepareStatement(sql);
			stmt.setInt(1, temp.getReimburId());
			assertEquals("Object was not deleted properly", 0, stmt.executeUpdate());
			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}
		
	}
	
	
	private void preparedHelper(String sql) throws SQLException {
		
		reimbDao.setConnUtil(connUtil);

		//creating a real stmt from a connection
		stmt = realConnection.prepareStatement(sql); 
		
		//spying on that real stmt
		spy = Mockito.spy(stmt);
		
		//mock our connection and util, so we will only use the stmt we are spying on
		when(connUtil.createConnection()).thenReturn(fakeConn);
		when(fakeConn.prepareStatement(sql)).thenReturn(spy);
		
		
	}
    
}

