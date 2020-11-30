package TRMS.TRMSPojos;

import java.util.Objects;

public class Employee {

    public enum Title{
            ASSOCIATE,
            SUPERVISOR,
            DEPARTMENT_HEAD,
            BENCO,
            BENCO_SUPERVISOR,
            BENCO_DEPARTMENT_HEAD,
            IT
    }

    private int empId;
    private String firstName;
    private String lastName;
    private Title title;
    private double balance = 1000.00;
    private int department;


    public Employee() {
    }
    
    /**
     * 
     * @param empId
     * @param firstName
     * @param lastName
     * @param title enum with ASSOCIATE, SUPERVISOR, DEPARTMENT_HEAD,
     * BENCO, BENCO_SUPERVISOR, BENCO_DEPARTMENT_HEAD
     * @param balance
     */
    public Employee(int empId, String firstName, String lastName, Title title, double balance, int department) {
        this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.balance = balance;
        this.department = department;
    }

    public Employee(String firstName, String lastName, Title title, double balance,int department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.balance = balance;
        this.department = department;
    }

    public Employee(String firstName, String lastName, Title title,int department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.department = department;
    }

    public int getEmpId() {
        return this.empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Title getTitle() {
        return this.title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getDepartment() {
        return this.department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public Employee empId(int empId) {
        this.empId = empId;
        return this;
    }

    public Employee firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Employee lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Employee title(Title title) {
        this.title = title;
        return this;
    }

    public Employee balance(double balance) {
        this.balance = balance;
        return this;
    }

    public Employee department(int department) {
        this.department = department;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Employee)) {
            return false;
        }
        Employee employee = (Employee) o;
        return empId == employee.empId && Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName) && Objects.equals(title, employee.title) && balance == employee.balance && department == employee.department;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId, firstName, lastName, title, balance, department);
    }

    @Override
    public String toString() {
        return "{" +
            " empId='" + getEmpId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", title='" + getTitle() + "'" +
            ", balance='" + getBalance() + "'" +
            ", department='" + getDepartment() + "'" +
            "}";
    }

}