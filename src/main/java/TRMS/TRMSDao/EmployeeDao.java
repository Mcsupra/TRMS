package TRMS.TRMSDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import TRMS.ConnectionUtil.ConnectionUtil;
import TRMS.TRMSPojos.Employee;
import TRMS.TRMSPojos.Employee.Title;

public class EmployeeDao implements CRUD<Employee> {

    private PreparedStatement stmt;
    private static Logger log = Logger.getLogger("Web");
    public ConnectionUtil connUtil = new ConnectionUtil();
    
    public void setConnUtil(ConnectionUtil connUtil) {
        this.connUtil = connUtil;
    }
    
    /**
     * @param Employee to insert
     * @return true pending success
     */
    @Override
    public int insert(Employee emp) throws SQLException {
        
        String sql = "INSERT into EMPLOYEE (first_name, last_name, title, balance, department) "
                + "values(?,?,?::title,?,?);";

			try (Connection conn = connUtil.createConnection()){
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,emp.getFirstName());
				stmt.setString(2,emp.getLastName());
				stmt.setString(3,emp.getTitle().toString());
                stmt.setDouble(4,emp.getBalance());
                stmt.setInt(5,emp.getDepartment());
                
				stmt.executeUpdate();
				
				return 1;
			} catch (SQLException e) {
				e.printStackTrace();
				log.error("SQLException:" + e);
				return 0;		
			}
    }

    @Override
    public Employee select(int empId) throws SQLException {
        //Prepared SQL statement prototype
        String sql = "Select * from EMPLOYEE where empid = ?;";

        try (Connection conn = connUtil.createConnection()){
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1,empId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            
            Employee returnedEmp = new Employee(rs.getInt(1),
                                                rs.getString(2),
                                                rs.getString(3),
                                                Title.valueOf(rs.getString(4)),
                                                rs.getDouble(5),
                                                rs.getInt(6));

            return returnedEmp;

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return null;		
        }
    }

    @Override
    public List<Employee> selectAll() throws SQLException {
        
        List<Employee> allEmp = new ArrayList<>();
			
		String sql = "SELECT * FROM employee;";
			
		try(Connection conn = connUtil.createConnection()){
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Employee returnedEmp = new Employee(rs.getInt(1),
                                                rs.getString(2),
                                                rs.getString(3),
                                                Title.valueOf(rs.getString(4)),
                                                rs.getDouble(5),
                                                rs.getInt(6));
					
				allEmp.add(returnedEmp);
					
			}
			return allEmp;
					
		}catch(SQLException e) {
			log.error("SQLException:" + e);
			return null;
		}
    }

    @Override
    public boolean update(Employee emp) throws SQLException {
        
        try(Connection conn = connUtil.createConnection()){ 
            String sql = "UPDATE employee SET first_name = ?, last_name = ?, title = ?::title, balance = ?, department = ?"
    			+ " WHERE empid = ?;";

            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, emp.getFirstName());
            stmt.setString(2, emp.getLastName());
            stmt.setString(3, emp.getTitle().toString());
            stmt.setDouble(4, emp.getBalance());
            stmt.setInt(5,emp.getDepartment());
            stmt.setInt(6, emp.getEmpId());

            stmt.executeUpdate();
            
            return true;
        } catch (SQLException e) {
            log.error("SQLException:" + e);
            return false;
        }
    }

    public boolean updateBalance(Employee emp) throws SQLException {
        
        try(Connection conn = connUtil.createConnection()){ 
            String sql = "UPDATE employee SET balance = ?"
    			+ " WHERE empid = ?;";

            stmt = conn.prepareStatement(sql);
            
            stmt.setDouble(1, emp.getBalance());
            stmt.setInt(2, emp.getEmpId());

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
            String sql = "DELETE FROM employee WHERE empid = ?;";

            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, empId);
            stmt.executeUpdate();
            
            return true;
        }catch (SQLException e) {
            log.error("SQLException:" + e);
            return false;
        }
    }
    
}
