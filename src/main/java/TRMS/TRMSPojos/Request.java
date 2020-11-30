package TRMS.TRMSPojos;

import java.time.LocalDate;
import java.util.Objects;

public class Request {
    public enum eventType{
            UNIVERSITY_COURSE,
            SEMINAR,
            CERTIFICATION_PREP,
            CERTIFICATION,
            TECH_TRAINING,
            OTHER             
    };
    
    public enum CurrentStatus{
        APPROVED,
        DENIED,
        URGENT,
        PENDING
    };

    private int reqId; 
    private String eventDate;
    private String eventTime;
    private String eventLoc;
    private eventType eventType;
    private double event_cost;
    private String requestDate = LocalDate.now().toString();
    private boolean supervisor = false;
    private boolean deptHead = false;
    private boolean benco = false;
    private CurrentStatus currentStatus = CurrentStatus.PENDING;
    private boolean additional_docs = false;
    private int empId;


    public Request(String eventDate, String eventTime, String eventLoc, eventType eventType, double event_cost) {
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventLoc = eventLoc;
        this.eventType = eventType;
        this.event_cost = event_cost;
    }

    

    public Request() {
    }

    public Request(int reqId, String eventDate, String eventTime, String eventLoc, eventType eventType, double event_cost, String requestDate, boolean supervisor, boolean deptHead, boolean benco, CurrentStatus currentStatus, boolean additional_docs, int empId) {
        this.reqId = reqId;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventLoc = eventLoc;
        this.eventType = eventType;
        this.event_cost = event_cost;
        this.requestDate = requestDate;
        this.supervisor = supervisor;
        this.deptHead = deptHead;
        this.benco = benco;
        this.currentStatus = currentStatus;
        this.additional_docs = additional_docs;
        this.empId = empId;
    }

    public int getReqId() {
        return this.reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }

    public String getEventDate() {
        return this.eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return this.eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventLoc() {
        return this.eventLoc;
    }

    public void setEventLoc(String eventLoc) {
        this.eventLoc = eventLoc;
    }

    public eventType getEventType() {
        return this.eventType;
    }

    public void setEventType(eventType eventType) {
        this.eventType = eventType;
    }

    public double getEvent_cost() {
        return this.event_cost;
    }

    public void setEvent_cost(double event_cost) {
        this.event_cost = event_cost;
    }

    public String getRequestDate() {
        return this.requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public boolean isSupervisor() {
        return this.supervisor;
    }

    public boolean getSupervisor() {
        return this.supervisor;
    }

    public void setSupervisor(boolean supervisor) {
        this.supervisor = supervisor;
    }

    public boolean isDeptHead() {
        return this.deptHead;
    }

    public boolean getDeptHead() {
        return this.deptHead;
    }

    public void setDeptHead(boolean deptHead) {
        this.deptHead = deptHead;
    }

    public boolean isBenco() {
        return this.benco;
    }

    public boolean getBenco() {
        return this.benco;
    }

    public void setBenco(boolean benco) {
        this.benco = benco;
    }

    public CurrentStatus getCurrentStatus() {
        return this.currentStatus;
    }

    public void setCurrentStatus(CurrentStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public boolean isAdditional_docs() {
        return this.additional_docs;
    }

    public boolean getAdditional_docs() {
        return this.additional_docs;
    }

    public void setAdditional_docs(boolean additional_docs) {
        this.additional_docs = additional_docs;
    }

    public int getEmpId() {
        return this.empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public Request reqId(int reqId) {
        this.reqId = reqId;
        return this;
    }

    public Request eventDate(String eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    public Request eventTime(String eventTime) {
        this.eventTime = eventTime;
        return this;
    }

    public Request eventLoc(String eventLoc) {
        this.eventLoc = eventLoc;
        return this;
    }

    public Request eventType(eventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public Request event_cost(double event_cost) {
        this.event_cost = event_cost;
        return this;
    }

    public Request requestDate(String requestDate) {
        this.requestDate = requestDate;
        return this;
    }

    public Request supervisor(boolean supervisor) {
        this.supervisor = supervisor;
        return this;
    }

    public Request deptHead(boolean deptHead) {
        this.deptHead = deptHead;
        return this;
    }

    public Request benco(boolean benco) {
        this.benco = benco;
        return this;
    }

    public Request currentStatus(CurrentStatus currentStatus) {
        this.currentStatus = currentStatus;
        return this;
    }

    public Request additional_docs(boolean additional_docs) {
        this.additional_docs = additional_docs;
        return this;
    }

    public Request empId(int empId) {
        this.empId = empId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Request)) {
            return false;
        }
        Request request = (Request) o;
        return reqId == request.reqId && Objects.equals(eventDate, request.eventDate) && Objects.equals(eventTime, request.eventTime) && Objects.equals(eventLoc, request.eventLoc) && Objects.equals(eventType, request.eventType) && event_cost == request.event_cost && Objects.equals(requestDate, request.requestDate) && supervisor == request.supervisor && deptHead == request.deptHead && benco == request.benco && Objects.equals(currentStatus, request.currentStatus) && additional_docs == request.additional_docs && empId == request.empId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reqId, eventDate, eventTime, eventLoc, eventType, event_cost, requestDate, supervisor, deptHead, benco, currentStatus, additional_docs, empId);
    }

    @Override
    public String toString() {
        return "{" +
            " reqId='" + getReqId() + "'" +
            ", eventDate='" + getEventDate() + "'" +
            ", eventTime='" + getEventTime() + "'" +
            ", eventLoc='" + getEventLoc() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", event_cost='" + getEvent_cost() + "'" +
            ", requestDate='" + getRequestDate() + "'" +
            ", supervisor='" + isSupervisor() + "'" +
            ", deptHead='" + isDeptHead() + "'" +
            ", benco='" + isBenco() + "'" +
            ", currentStatus='" + getCurrentStatus() + "'" +
            ", additional_docs='" + isAdditional_docs() + "'" +
            ", empId='" + getEmpId() + "'" +
            "}";
    }
    

}
