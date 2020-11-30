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
import TRMS.TRMSDao.AccountsDao;
import TRMS.TRMSPojos.Account;
import TRMS.TRMSPojos.Employee;
import TRMS.TRMSPojos.Employee.Title;

@RunWith(MockitoJUnitRunner.class)
public class AccountDaoTests {
    private static Logger log = Logger.getLogger("Web");
	
	@Mock
	private static ConnectionUtil connUtil;
	
	@Mock
	private static Connection fakeConn;
	
	private static PreparedStatement stmt;
	
	private static PreparedStatement spy;
	
    private static Connection realConnection;
    
    public static Account testAcct = new Account();
    public static Employee testEmp = new Employee();
    public static AccountsDao acctDao = new AccountsDao();
    
    @BeforeClass
	public static void setUpBeforeClass() throws Exception {	
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {        
	}

	@Before
	public void setUp() throws Exception {
		
        realConnection = new ConnectionUtil().createConnection();
        testAcct = new Account("mcsupra", "p@$$VV0RD", 0);
        testEmp = new Employee(0, "Michael", "Zide", Title.SUPERVISOR, 1000,2);

        String sql = "INSERT into EMPLOYEE (empid, first_name, last_name, title, balance, department) "
		+ "values(0, 'Michael', 'Zide', 'SUPERVISOR', 1000, 2);";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();	
        
        sql = "INSERT into Accounts (username, passphrase, empid) "
        + "values('mcsupra', 'p@$$VV0RD', 0);";
        
        stmt = realConnection.prepareStatement(sql);
        stmt.executeUpdate();	
	}

	@After
	public void tearDown() throws Exception { 
        
        String sql = "delete from accounts where empid = 0;";

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
	public void insertAccountTest() throws SQLException {
		realConnection = new ConnectionUtil().createConnection();
        testAcct = new Account("mczsupra", "p@$$VV0RD", -1);
        testEmp = new Employee(-1, "Michael", "Zide", Title.SUPERVISOR, 1000, 2);

        String sql = "INSERT into EMPLOYEE (empid, first_name, last_name, title, balance, department) "
		+ "values(-1, 'Michael', 'Zide', 'SUPERVISOR', 1000, 2);";
		
        
        stmt = realConnection.prepareStatement(sql);
        stmt.executeUpdate();	

        //Prepared SQL statement prototype
        sql = "INSERT into accounts (username, passphrase, empid) "
                + "values(?,?,?);";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			acctDao.insert(testAcct);

			verify(spy).setString(1, testAcct.getUsername());
            verify(spy).setString(2, testAcct.getPassphrase());
            verify(spy).setInt(3, testAcct.getEmpId());
            
			verify(spy).executeUpdate();

		}catch (SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}finally {
			try {
				stmt = realConnection.prepareStatement("DELETE from accounts WHERE empid = -1");
				stmt.executeUpdate();
				stmt = realConnection.prepareStatement("DELETE from EMPLOYEE WHERE empid = -1");
				stmt.executeUpdate();
			}catch(SQLException e) {
				log.error("SQL Exception:" + e);
				fail("Error: Could not remove added employee");
			}
		}	
		
	}
	
	@Test
	public void selectAccountTest() throws SQLException {
		//Prepared SQL statement prototype
        String sql = "Select * from Accounts where empid = ?;";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}

		try {
			Account dummy = acctDao.select(testAcct.getEmpId());

			verify(spy).setInt(1, testAcct.getEmpId());
			verify(spy).executeQuery();

			assertEquals(testAcct, dummy);

		}catch (SQLException e) {
			log.error("SQL Exception:" + e);
		}

	}

	@Test
	public void selectAllEmployeesTest() throws SQLException {
		
		List<Account> allAccts = new ArrayList<>();
		
		//Baseline of test is number in table
		String sql = "SELECT count(*) FROM accounts";
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
		sql = "SELECT * FROM accounts;";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			allAccts = acctDao.selectAll();
			
			verify(spy).executeQuery();
			if (allAccts.size() != num_in_table) {
				fail("Error: Queried data does not match current DB config");
			}
				
			for (Account a: allAccts) {
				assertFalse("Non-nullable empId is null",  a.getEmpId() < 0);
				assertFalse("Non-nullable first_name is null","".equals(a.getUsername()));
				assertFalse("Non-nullable last_name is null","".equals(a.getUsername()));
			}

			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}
	}
	
	
	@Test
	public void updateAccountTest() throws SQLException {
		
		//Prepared SQL statement
		String sql = "UPDATE accounts SET username = ?, passphrase= ?"
    	+ " WHERE empid = ?;";
		
		try {
			preparedHelper(sql);
		} catch (SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			testAcct.setUsername("mczsupra");
			testAcct.setPassphrase("passphrase123"); 
			
			acctDao.update(testAcct);
			
			verify(spy).setString(1, testAcct.getUsername());
			verify(spy).setString(2, testAcct.getPassphrase());
			verify(spy).setInt(3, testAcct.getEmpId());
			
			verify(spy).executeUpdate();
			
			//Pull modified employee object from database for comparison
			stmt = realConnection.prepareStatement("SELECT * FROM accounts WHERE empid = ?;");
			stmt.setInt(1, testEmp.getEmpId());
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
			Account acctMod = new Account(rs.getString(1), rs.getString(2), rs.getInt(3));

			assertEquals("Database object does not match as modified", testAcct, acctMod);
			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
	}
	
	@Test
	public void deleteAccountTest() throws SQLException {
        
        String sql = "INSERT into EMPLOYEE (empid, first_name, last_name, title, balance, department) "
		+ "values(-1, 'Michael', 'Zide', 'SUPERVISOR', 1000, 2);";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();

		sql = "INSERT into accounts (username, passphrase, empid) "
        + "values('mczsupra', 'p@$$VV0RD', -1);";
        
        Account temp = new Account("mczsupra", "p@$$VV0RD", -1);
        
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();	
		
		sql = "DELETE FROM accounts WHERE empid = ?;";


		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown in first try of test: " + e.toString());
		}

		try {
			acctDao.delete(temp.getEmpId());
			
			verify(spy).setInt(1, temp.getEmpId());
			verify(spy).executeUpdate();
			
			stmt = realConnection.prepareStatement(sql);
			stmt.setInt(1, temp.getEmpId());
			assertEquals("Object was not deleted properly", 0, stmt.executeUpdate());
			
		}catch(SQLException e) {
			log.error("SQL Exception in main try block:" + e);
			fail("Exception thrown: " + e);
		}finally{
            try {
                stmt = realConnection.prepareStatement("DELETE from accounts WHERE empid = -1");
				stmt.executeUpdate();
				stmt = realConnection.prepareStatement("DELETE from EMPLOYEE WHERE empid = -1");
				stmt.executeUpdate();
			}catch(SQLException e) {
				log.error("SQL Exception in finally of test:" + e);
				fail("Error: Could not remove added employee");
			}
        }
		
	}
	
	
	private void preparedHelper(String sql) throws SQLException {
		
		acctDao.setConnUtil(connUtil);

		//creating a real stmt from a connection
		stmt = realConnection.prepareStatement(sql); 
		
		//spying on that real stmt
		spy = Mockito.spy(stmt);
		
		//mock our connection and util, so we will only use the stmt we are spying on
		when(connUtil.createConnection()).thenReturn(fakeConn);
		when(fakeConn.prepareStatement(sql)).thenReturn(spy);
		
		
	}
}
