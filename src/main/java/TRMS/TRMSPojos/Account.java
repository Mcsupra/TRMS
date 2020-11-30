package TRMS.TRMSPojos;

import java.util.Objects;

public class Account {

    private String username;

    private String passphrase;

    private int empId;

    public Account() {
    }

    public Account(String username, String passphrase, int empId) {
        this.username = username;
        this.passphrase = passphrase;
        this.empId = empId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassphrase() {
        return this.passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public int getEmpId() {
        return this.empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public Account username(String username) {
        this.username = username;
        return this;
    }

    public Account passphrase(String passphrase) {
        this.passphrase = passphrase;
        return this;
    }

    public Account empId(int empId) {
        this.empId = empId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Account)) {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(username, account.username) && Objects.equals(passphrase, account.passphrase) && empId == account.empId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, passphrase, empId);
    }

    @Override
    public String toString() {
        return "{" +
            " username='" + getUsername() + "'" +
            ", passphrase='" + getPassphrase() + "'" +
            ", empId='" + getEmpId() + "'" +
            "}";
    }
}
