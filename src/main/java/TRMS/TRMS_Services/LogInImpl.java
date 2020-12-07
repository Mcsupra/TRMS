package TRMS.TRMS_Services;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import TRMS.TRMSDao.AccountsDao;
import TRMS.TRMSDao.EmployeeDao;
import TRMS.TRMSPojos.Account;
import TRMS.TRMSPojos.Employee;
import TRMS.TRMSPojos.Employee.Title;

public class LogInImpl implements LogIn {
    EmployeeDao currEmp = new EmployeeDao();
    AccountsDao currAcct = new AccountsDao();
    private static Logger log = Logger.getLogger("Web");
    
    @Override
        public boolean createEmployee(String firstName, String lastName, Title title, int department) {
            try{
                Employee newEmp = new Employee(firstName,lastName,title,department);
                System.out.println(newEmp);
                currEmp.insert(newEmp);
                return true;
            }catch (SQLException e) {
				e.printStackTrace();
				log.error("SQLException:" + e);
				return false;		
			}
    }    

    @Override
    public boolean createAccount(String username, String password, int empId) {
        try{
            currAcct.insert(new Account(username,password,empId));
            return true; 
        }catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return false;		
        }
    }  
}
