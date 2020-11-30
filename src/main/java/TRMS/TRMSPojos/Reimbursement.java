package TRMS.TRMSPojos;

import java.util.Objects;

public class Reimbursement {
    
    public enum status{
        APPROVED,
        DENIED,
        PENDING
    }
    private int reimburId;
    private status status;
    private double projectedAmount;
    private double actualAmount;
    private int reqId;


    public Reimbursement() {
    }

    public Reimbursement(int reimburId, status status, double projectedAmount, double actualAmount, int reqId) {
        this.reimburId = reimburId;
        this.status = status;
        this.projectedAmount = projectedAmount;
        this.actualAmount = actualAmount;
        this.reqId = reqId;
    }
    public Reimbursement(status status, double projectedAmount, double actualAmount, int reqId) {
        this.status = status;
        this.projectedAmount = projectedAmount;
        this.actualAmount = actualAmount;
        this.reqId = reqId;
    }

    public int getReimburId() {
        return this.reimburId;
    }

    public void setReimburId(int reimburId) {
        this.reimburId = reimburId;
    }

    public status getStatus() {
        return this.status;
    }

    public void setStatus(status status) {
        this.status = status;
    }

    public double getProjectedAmount() {
        return this.projectedAmount;
    }

    public void setProjectedAmount(double projectedAmount) {
        this.projectedAmount = projectedAmount;
    }

    public double getActualAmount() {
        return this.actualAmount;
    }

    public void setActualAmount(double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public int getReqId() {
        return this.reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }

    public Reimbursement reimburId(int reimburId) {
        this.reimburId = reimburId;
        return this;
    }

    public Reimbursement status(status status) {
        this.status = status;
        return this;
    }

    public Reimbursement projectedAmount(double projectedAmount) {
        this.projectedAmount = projectedAmount;
        return this;
    }

    public Reimbursement actualAmount(double actualAmount) {
        this.actualAmount = actualAmount;
        return this;
    }

    public Reimbursement reqId(int reqId) {
        this.reqId = reqId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Reimbursement)) {
            return false;
        }
        Reimbursement reimbursement = (Reimbursement) o;
        return reimburId == reimbursement.reimburId && Objects.equals(status, reimbursement.status) && projectedAmount == reimbursement.projectedAmount && actualAmount == reimbursement.actualAmount && reqId == reimbursement.reqId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reimburId, status, projectedAmount, actualAmount, reqId);
    }

    @Override
    public String toString() {
        return "{" +
            " reimburId='" + getReimburId() + "'" +
            ", status='" + getStatus() + "'" +
            ", projectedAmount='" + getProjectedAmount() + "'" +
            ", actualAmount='" + getActualAmount() + "'" +
            ", reqId='" + getReqId() + "'" +
            "}";
    }
    
}
