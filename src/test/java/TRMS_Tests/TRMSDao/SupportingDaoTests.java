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
import TRMS.TRMSDao.SupportingDao;
import TRMS.TRMSPojos.Supporting;

@RunWith(MockitoJUnitRunner.class)
public class SupportingDaoTests {

    private static Logger log = Logger.getLogger("Web");

    @Mock
    private static ConnectionUtil connUtil;
	
	@Mock
	private static Connection fakeConn;
	
	private static PreparedStatement stmt;
	
	private static PreparedStatement spy;
	
    private static Connection realConnection;
    
	public static Supporting testSupporting = new Supporting();

	public static SupportingDao supportingDao = new SupportingDao();
    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
    }

	@Before
	public void setUp() throws Exception {
		
        realConnection = new ConnectionUtil().createConnection();
		testSupporting = new Supporting(0, "JPEG");

		String sql = "INSERT into supportingdocs (docid, file_type)"
		+ "values(0, 'JPEG');"; 
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();	
	}

	@After
	public void tearDown() throws Exception {
		
		String sql = "delete from supportingdocs where docid = 0;";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();

		if (realConnection != null) {
			realConnection.close();
		}
		
	}

	@Test
	public void insertSupportingTest() throws SQLException {
		
		testSupporting = new Supporting(-1, "JPEG");
        //Prepared SQL statement prototype
        String sql = "INSERT into supportingdocs (docid, file_type)"
		+ "values(?,?);"; 
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
            supportingDao.insert(testSupporting);
            
			verify(spy).setInt(1, testSupporting.getDocId());
			verify(spy).setString(2, testSupporting.getFileType());
            
			verify(spy).executeUpdate();

		}catch (SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}finally {
			try {
				stmt = realConnection.prepareStatement("DELETE from supportingdocs WHERE docid = -1");
				stmt.executeUpdate();
			}catch(SQLException e) {
				log.error("SQL Exception:" + e);
				fail("Error: Could not remove added Supporting doc");
			}
		}	 
		
	}
	
	@Test
	public void selectSupportingDocTest() throws SQLException {
		//Prepared SQL statement prototype
        String sql = "Select * FROM supportingdocs where docid = ?;";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}

		try {
			Supporting sample = supportingDao.select(testSupporting.getDocId());

			verify(spy).setInt(1, testSupporting.getDocId());
			verify(spy).executeQuery();

			assertEquals(testSupporting, sample);

		}catch (SQLException e) {
			log.error("SQL Exception:" + e);
		}

	}

	@Test
	public void selectAllDocumentsTest() throws SQLException {
		
		List<Supporting> allSupporting = new ArrayList<>();
		
		//Baseline of test is number in table
		String sql = "SELECT count(*) FROM supportingdocs";
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
		sql = "SELECT * FROM supportingdocs;";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			allSupporting = supportingDao.selectAll();
			
			verify(spy).executeQuery();
			if (allSupporting.size() != num_in_table) {
				fail("Error: Queried data does not match current DB config");
			}
				
			for (Supporting r: allSupporting) {
				assertFalse("Non-nullable docid is null",  r.getDocId() < 0);
				assertFalse("Non-nullable file type is null","".equals(r.getFileType()));
			}

			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}
	}
	
	
	@Test
	public void updateSupportingDocTest() throws SQLException {
		
		//Prepared SQL statement
		String sql = "UPDATE supportingdocs SET file_type = ? WHERE docid = ?;";
		
		try {
			preparedHelper(sql);
		} catch (SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			testSupporting.setFileType("PDF");
 
			
			supportingDao.update(testSupporting);
			
			verify(spy).setString(1, testSupporting.getFileType());
			verify(spy).setInt(2, testSupporting.getDocId());
			
			verify(spy).executeUpdate();
			
			//Pull modified employee object from database for comparison
			stmt = realConnection.prepareStatement("SELECT * FROM supportingdocs WHERE docid = ?;");
			stmt.setInt(1, testSupporting.getDocId());
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
            Supporting supportingMod = new Supporting(rs.getInt(1),
                                                      rs.getString(2));
           
			assertEquals("Database object does not match as modified", testSupporting, supportingMod);
			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
	}
	
	@Test
	public void deleteSupportingDocTest() throws SQLException {
		
		String sql = "INSERT into supportingdocs (docid, file_type)"
		+ "values(-1, 'BitMap');"; 
		
		Supporting temp = new Supporting(-1, "BitMap");

		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();	
		
		sql = "DELETE FROM supportingdocs WHERE docid = ?;";


		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}

		try {
			supportingDao.delete(temp.getDocId());
			
			verify(spy).setInt(1, temp.getDocId());
			verify(spy).executeUpdate();
			
			stmt = realConnection.prepareStatement(sql);
			stmt.setInt(1, temp.getDocId());
			assertEquals("Object was not deleted properly", 0, stmt.executeUpdate());
			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}
		
	}
	
	
	private void preparedHelper(String sql) throws SQLException {
		
		supportingDao.setConnUtil(connUtil);

		//creating a real stmt from a connection
		stmt = realConnection.prepareStatement(sql); 
		
		//spying on that real stmt
		spy = Mockito.spy(stmt);
		
		//mock our connection and util, so we will only use the stmt we are spying on
		when(connUtil.createConnection()).thenReturn(fakeConn);
		when(fakeConn.prepareStatement(sql)).thenReturn(spy);
		
		
	}
}
