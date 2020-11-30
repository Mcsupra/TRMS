package TRMS.TRMS_Services;

import TRMS.TRMSPojos.Employee.Title;

public interface LogIn {

    public boolean createAccount(String username, String password, int empId);

    public boolean createEmployee(String firstName,String lastName, Title title, int department);
    
}
