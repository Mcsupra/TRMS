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
import TRMS.TRMSDao.EmployeeDao;
import TRMS.TRMSPojos.Employee;
import TRMS.TRMSPojos.Employee.Title;


@RunWith(MockitoJUnitRunner.class)
public class EmployeeDaoTests {

    private static Logger log = Logger.getLogger("Web");
	
	@Mock
	private static ConnectionUtil connUtil;
	
	@Mock
	private static Connection fakeConn;
	
	private static PreparedStatement stmt;
	
	private static PreparedStatement spy;
	
    private static Connection realConnection;
    
	public static Employee testEmp = new Employee();

	public static EmployeeDao empDao = new EmployeeDao();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
        realConnection = new ConnectionUtil().createConnection();
		testEmp = new Employee(0, "Michael", "Zide", Title.SUPERVISOR, 1000, 2);

		String sql = "INSERT into EMPLOYEE (empid, first_name, last_name, title, balance, department) "
		+ "values(0, 'Michael', 'Zide', 'SUPERVISOR', 1000, 2);";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();	
	}

	@After
	public void tearDown() throws Exception {
		
		String sql = "delete from employee where empid = 0;";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();

		if (realConnection != null) {
			realConnection.close();
		}
		
	}

	@Test
	public void insertEmployeeTest() throws SQLException {
	
        //Prepared SQL statement prototype
        String sql = "INSERT into EMPLOYEE (first_name, last_name, title, balance, department) "
                + "values(?,?,?::title,?,?);";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			empDao.insert(testEmp);

			verify(spy).setString(1, testEmp.getFirstName());
			verify(spy).setString(2, testEmp.getLastName());
			verify(spy).setString(3, testEmp.getTitle().toString());
			verify(spy).setDouble(4, testEmp.getBalance());
			verify(spy).setInt(5, testEmp.getDepartment());
            
			verify(spy).executeUpdate();

		}catch (SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}finally {
			try {
				stmt = realConnection.prepareStatement("DELETE from EMPLOYEE WHERE last_name = 'Zide' ");
				stmt.executeUpdate();
			}catch(SQLException e) {
				log.error("SQL Exception:" + e);
				fail("Error: Could not remove added employee");
			}
		}	
		
	}
	
	@Test
	public void selectEmployeeTest() throws SQLException {
		//Prepared SQL statement prototype
        String sql = "Select * from EMPLOYEE where empid = ?;";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}

		try {
			Employee dummy = empDao.select(testEmp.getEmpId());

			verify(spy).setInt(1, testEmp.getEmpId());
			verify(spy).executeQuery();

			assertEquals(testEmp, dummy);

		}catch (SQLException e) {
			log.error("SQL Exception:" + e);
		}

	}

	@Test
	public void selectAllEmployeesTest() throws SQLException {
		
		List<Employee> allEmp = new ArrayList<>();
		
		//Baseline of test is number in table
		String sql = "SELECT count(*) FROM employee";
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
		sql = "SELECT * FROM employee;";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			allEmp = empDao.selectAll();
			
			verify(spy).executeQuery();
			if (allEmp.size() != num_in_table) {
				fail("Error: Queried data does not match current DB config");
			}
				
			for (Employee c: allEmp) {
				assertFalse("Non-nullable empId is null",  c.getEmpId() < 0);
				assertFalse("Non-nullable first_name is null","".equals(c.getFirstName()));
				assertFalse("Non-nullable last_name is null","".equals(c.getLastName()));
			}

			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}
	}
	
	
	@Test
	public void updateEmployeeTest() throws SQLException {
		
		//Prepared SQL statement
		String sql = "UPDATE employee SET first_name = ?, last_name = ?, title = ?::title, balance = ?, department = ?"
    			+ " WHERE empid = ?;";
		
		try {
			preparedHelper(sql);
		} catch (SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			testEmp.setFirstName("Kris");
			testEmp.setLastName("Kringle");
			testEmp.setTitle(Title.BENCO);
			testEmp.setBalance(873.51); 
			testEmp.setDepartment(3);

			empDao.update(testEmp);
			
			verify(spy).setString(1, testEmp.getFirstName());
			verify(spy).setString(2, testEmp.getLastName());
			verify(spy).setString(3, testEmp.getTitle().toString());
			verify(spy).setDouble(4, testEmp.getBalance());
			verify(spy).setInt(5, testEmp.getDepartment());
			verify(spy).setInt(6, testEmp.getEmpId());
			
			verify(spy).executeUpdate();
			
			//Pull modified employee object from database for comparison
			stmt = realConnection.prepareStatement("SELECT * FROM employee WHERE empid = ?;");
			stmt.setInt(1, testEmp.getEmpId());
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
			Employee employeeMod = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), Title.valueOf(rs.getString(4)), rs.getDouble(5), rs.getInt(6));

			assertEquals("Database object does not match as modified", testEmp, employeeMod);
			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
	}
	
	@Test
	public void deleteEmployeeTest() throws SQLException {
		
		String sql = "INSERT into EMPLOYEE (empid, first_name, last_name, title, balance, department) "
		+ "values(-1, 'Conner', 'Bosch', 'ASSOCIATE', 500, 2);";
		
		Employee temp = new Employee(-1, "Conner", "Bosch", Title.ASSOCIATE, 500.00, 2);

		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();	
		
		sql = "DELETE FROM employee WHERE empid = ?;";


		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}

		try {
			empDao.delete(temp.getEmpId());
			
			verify(spy).setInt(1, temp.getEmpId());
			verify(spy).executeUpdate();
			
			stmt = realConnection.prepareStatement(sql);
			stmt.setInt(1, temp.getEmpId());
			assertEquals("Object was not deleted properly", 0, stmt.executeUpdate());
			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}
		
	}
	
	
	private void preparedHelper(String sql) throws SQLException {
		
		empDao.setConnUtil(connUtil);

		//creating a real stmt from a connection
		stmt = realConnection.prepareStatement(sql); 
		
		//spying on that real stmt
		spy = Mockito.spy(stmt);
		
		//mock our connection and util, so we will only use the stmt we are spying on
		when(connUtil.createConnection()).thenReturn(fakeConn);
		when(fakeConn.prepareStatement(sql)).thenReturn(spy);
		
		
	}
    
}
