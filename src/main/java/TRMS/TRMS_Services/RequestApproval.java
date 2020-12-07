package TRMS.TRMS_Services;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.apache.log4j.Logger;

import TRMS.TRMSDao.EmployeeDao;
import TRMS.TRMSDao.ReimbursementDao;
import TRMS.TRMSDao.RequestDao;
import TRMS.TRMSPojos.Employee;
import TRMS.TRMSPojos.Reimbursement;
import TRMS.TRMSPojos.Request;
import TRMS.TRMSPojos.Reimbursement.status;
import TRMS.TRMSPojos.Request.CurrentStatus;
import TRMS.TRMSPojos.Request.eventType;

public class RequestApproval {

    private static Logger log = Logger.getLogger("Web"); 
    private RequestDao getReq = new RequestDao();
    private Request currReq = new Request();
    public Reimbursement newReimb;
    public ReimbursementDao addReimb = new ReimbursementDao();
    public EmployeeDao getEmpReq = new EmployeeDao();
    public Employee empReq = new Employee();

    public int createRequest(String eventDate, String eventTime, String eventLoc, eventType eventType, Double eventCost, int empId){
        Request newReq = new Request(eventDate, eventTime, eventLoc, eventType, eventCost);
        try {
            dateCheck(newReq);
            int reqId = getReq.insertWithEmpId(newReq, empId);
            return reqId;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return 0;
        }
    }
    
    public List<Request> getReqByEmpId(int empId){
        try {
            return getReq.selectByEmpId(empId);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return null;
        }
    }

    public List<Request> getReqByDepartment(int department){
        try {
            return getReq.selectByDepartment(department);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException:" + e);
            return null;
        }
    }

    public void dateCheck(Request req){
        LocalDate withinWeek = LocalDate.parse(req.getRequestDate()).plusDays(7);
        LocalDate eventDate = LocalDate.parse(req.getEventDate());
        if (eventDate.compareTo(withinWeek) <= 0){
            req.setCurrentStatus(CurrentStatus.URGENT);
        }
    }

    public boolean employeeNonBencoApprove(int reqId, Employee emp){
        Employee nonBenco = emp;
        try {
            currReq = getReq.select(reqId);
            switch(nonBenco.getTitle()){
                case SUPERVISOR:
                    currReq.setSupervisor(true);
                    getReq.updateSupStatus(currReq);
                    double proj = 0;
                    double eventCost = currReq.getEvent_cost();

                    switch(currReq.getEventType()){
                        case UNIVERSITY_COURSE:
                            proj = eventCost*.8;
                            break;
                        case SEMINAR:
                            proj = eventCost*.6;
                            break;
                        case CERTIFICATION:
                            proj = eventCost;
                            break;
                        case CERTIFICATION_PREP:
                            proj = eventCost*.75;
                            break;
                        case TECH_TRAINING:
                            proj = eventCost*.9;
                            break;
                        default:
                            proj = eventCost*.3;
                            break;
                    }
                    if (proj > 1000){
                        proj = 1000;
                    }

                    empReq = getEmpReq.select(currReq.getEmpId());
                    proj = Math.min(proj, empReq.getBalance());
                    empReq.setBalance(empReq.getBalance()-proj);
                    getEmpReq.updateBalance(empReq);
                    newReimb = new Reimbursement(status.PENDING, proj, 0, reqId);
                    addReimb.insert(newReimb);
                    break;
                case DEPARTMENT_HEAD:
                    currReq.setDeptHead(true);
                    getReq.updateDHStatus(currReq);
                    break;
                case BENCO:
                    currReq.setBenco(true);
                    getReq.updateBencoStatus(currReq);
                    break;
                default:
                    return false;
            }
            
            return true;
        } catch (SQLException e) {
            // Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public boolean employeeNonBencoDeny(int reqId, Employee emp){
        Employee nonBenco = emp;
        try {
            currReq = getReq.select(reqId);
            switch(nonBenco.getTitle()){
                case SUPERVISOR:
                    currReq.setSupervisor(false);
                    getReq.updateSupStatus(currReq);
                    break;
                case DEPARTMENT_HEAD:
                    currReq.setDeptHead(false);
                    getReq.updateDHStatus(currReq);
                    break;
                case BENCO:
                    currReq.setBenco(false);
                    getReq.updateBencoStatus(currReq);
                    break;
                default:
                    return true;
            }
            currReq.setCurrentStatus(CurrentStatus.DENIED);
            empReq = getEmpReq.select(currReq.getEmpId());
            newReimb = addReimb.selectByReqId(reqId);
            empReq.setBalance(empReq.getBalance()+newReimb.getProjectedAmount());
            newReimb.setActualAmount(0);
            newReimb.setProjectedAmount(0);
            newReimb.setStatus(status.DENIED);
            getEmpReq.update(empReq);
            addReimb.update(newReimb);
            getReq.update(currReq);

            return false;
        } catch (SQLException e) {
            // Auto-generated catch block
            e.printStackTrace();
            return true;
        }
    }

    public boolean employeeIsBencoApprove(int reqId, Employee emp){
        Employee isBenco = emp;
        try {
            int empId = getReq.getEmpIdByReq(reqId);
            currReq = getReq.select(reqId);
            if (isBenco.getEmpId() != empId){
                switch(isBenco.getTitle()){
                    case BENCO:
                        currReq.setSupervisor(true);
                        getReq.updateBencoStatus(currReq);
                        break;
                    case BENCO_SUPERVISOR:
                        currReq.setDeptHead(true);
                        getReq.updateSupStatus(currReq);
                        double proj = 0;
                        double eventCost = currReq.getEvent_cost();
        
                        switch(currReq.getEventType()){
                            case UNIVERSITY_COURSE:
                                proj = eventCost*.8;
                                break;
                            case SEMINAR:
                                proj = eventCost*.6;
                                break;
                            case CERTIFICATION:
                                proj = eventCost;
                                break;
                            case CERTIFICATION_PREP:
                                proj = eventCost*.75;
                                break;
                            case TECH_TRAINING:
                                proj = eventCost*.9;
                                break;
                            default:
                                proj = eventCost*.3;
                                break;
                        };
                        if (proj > 1000){
                            proj = 1000;
                        }
    
                        empReq = getEmpReq.select(currReq.getEmpId());
                        proj = Math.min(proj, empReq.getBalance());
                        empReq.setBalance(empReq.getBalance()-proj);
                        getEmpReq.updateBalance(empReq);
                        newReimb = new Reimbursement(status.PENDING, proj, 0, reqId);
                        addReimb.insert(newReimb);
                        break;
                    case BENCO_DEPARTMENT_HEAD:
                        currReq.setBenco(true);
                        currReq.setCurrentStatus(CurrentStatus.DENIED);
                        getReq.updateDHStatus(currReq);
                        break;
                    default:
                        return false;
                }
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException e) {
            // Auto-generated catch block
            e.printStackTrace();
            return true;
        }

    }

    public boolean employeeIsBencoDeny(int reqId, Employee emp){
        Employee isBenco = emp;
        try {
            int empId = getReq.getEmpIdByReq(reqId);
            currReq = getReq.select(reqId);
            if (isBenco.getEmpId() != empId){
                switch(isBenco.getTitle()){
                    case BENCO:
                        currReq.setSupervisor(false);
                        getReq.updateBencoStatus(currReq);
                        break;
                    case BENCO_SUPERVISOR:
                        currReq.setDeptHead(false);
                        getReq.updateSupStatus(currReq);
                        break;
                    case BENCO_DEPARTMENT_HEAD:
                        currReq.setBenco(false);
                        currReq.setCurrentStatus(CurrentStatus.DENIED);
                        getReq.updateDHStatus(currReq);
                        break;
                    default:
                        return true;
                }
                currReq.setCurrentStatus(CurrentStatus.DENIED);
                empReq = getEmpReq.select(currReq.getEmpId());
                newReimb = addReimb.selectByReqId(reqId);
                empReq.setBalance(empReq.getBalance()+newReimb.getProjectedAmount());
                newReimb.setActualAmount(0);
                newReimb.setProjectedAmount(0);
                newReimb.setStatus(status.DENIED);
                getEmpReq.update(empReq);
                addReimb.update(newReimb);
                getReq.update(currReq);

                return false;
            }
            else{
                return true;
            }
        } catch (SQLException e) {
            // Auto-generated catch block
            e.printStackTrace();
            return true;
        }
    }

    public void finalApproval(Reimbursement reimbur){
        try {
            newReimb = addReimb.selectByReqId(reimbur.getReqId());
            currReq = getReq.select(reimbur.getReqId());
            empReq = getEmpReq.select(currReq.getEmpId());
            if (reimbur.getActualAmount() == 0){
                reimbur.setStatus(status.DENIED);
                currReq.setCurrentStatus(CurrentStatus.DENIED);
                empReq.setBalance(empReq.getBalance()+newReimb.getProjectedAmount());
            }
            else{
                currReq.setCurrentStatus(CurrentStatus.APPROVED);
                reimbur.setStatus(status.APPROVED);
                empReq.setBalance(empReq.getBalance()+newReimb.getProjectedAmount()-reimbur.getActualAmount());
            }
            reimbur.setReimburId(newReimb.getReimburId());
            addReimb.update(reimbur);
            getReq.updateReqStatus(currReq);
            getEmpReq.update(empReq);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
