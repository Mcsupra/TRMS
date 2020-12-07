package TRMS.TRMS_Controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import TRMS.TRMSDao.EmployeeDao;
import TRMS.TRMSDao.MessageDao;
import TRMS.TRMSDao.ReimbursementDao;
import TRMS.TRMSDao.RequestDao;
import TRMS.TRMSPojos.Employee;
import TRMS.TRMSPojos.Employee.Title;
import TRMS.TRMSPojos.Message;
import TRMS.TRMSPojos.Reimbursement;
import TRMS.TRMSPojos.Reimbursement.status;
import TRMS.TRMSPojos.Request;
import TRMS.TRMSPojos.Request.eventType;
import TRMS.TRMS_Services.LogInImpl;
import TRMS.TRMS_Services.MessDoc;
import TRMS.TRMS_Services.RequestApproval;
import io.javalin.http.Context;


public class WebController {
    private LogInImpl currUser = new LogInImpl();
    private RequestApproval currReq = new RequestApproval();
    //private MessDoc other = new MessDoc();

    public void callCreateEmployee(Context ctx) {

        if (ctx.cookieStore("security") != null) {
            if (AuthController.loggedIn.get(ctx.cookieStore("security")).getEmpId() == -99) {
                String firstName = ctx.formParam("firstName");
                String lastName = ctx.formParam("lastName");
                Title title = Title.valueOf(ctx.formParam("title"));
                int department = Integer.parseInt(ctx.formParam("department"));
                boolean newEmp = currUser.createEmployee(firstName, lastName, title, department);
                

                if (newEmp) {
                    ctx.status(200);
                    ctx.redirect("index.html");
                } else {
                    ctx.status(401);
                    ctx.redirect("employee.html");
                }
            }else{
                ctx.redirect("index.html");
            }
        }
        else{
            ctx.redirect("login.html");
        }
    }

    public void callCreateAccount(Context ctx) {
       
        if (ctx.cookieStore("security")!=null){ 
            if(AuthController.loggedIn.get(ctx.cookieStore("security")).getEmpId() == -99){
                String username = ctx.formParam("username");
                String password = ctx.formParam("password");
                if (password.length() < 5){
                    password = null;
                }

                int empId = Integer.parseInt(ctx.formParam("empId"));

                boolean newAcct = currUser.createAccount(username, password, empId);

                if (newAcct) {
                    ctx.status(200);
                    ctx.redirect("index.html");
                } else {
                    ctx.status(401);
                    ctx.redirect("account.html");
                }
            }else{
                ctx.redirect("index.html");
            }
        }
        else{
            ctx.redirect("login.html");
        }
    }

    public void callCreateRequest(Context ctx) {
        
        if (ctx.cookieStore("security")!=null){

            int empId = AuthController.loggedIn.get(ctx.cookieStore("security")).getEmpId();
            String eventDate = ctx.formParam("eventDate");
            String eventTime = ctx.formParam("eventTime");
            String eventLoc = ctx.formParam("eventLoc");
            eventType event = eventType.valueOf(ctx.formParam("eventType"));
            Double eventCost = Double.parseDouble(ctx.formParam("eventCost"));

            int newReq = currReq.createRequest(eventDate, eventTime, eventLoc, event, eventCost, empId);
            

            if (newReq != 0) {
                ctx.status(200);
                ctx.redirect("index-assoc.html");
            } else {
                ctx.status(401);
                ctx.redirect("request.html");
            }
        }
        else{
            ctx.redirect("login.html");
        }    
    }

    public void callGetReqbyEmpId(Context ctx) {
        if (ctx.cookieStore("security")!=null){
            int empId = AuthController.loggedIn.get(ctx.cookieStore("security")).getEmpId();
            List<Request> reqByEmpId = currReq.getReqByEmpId(empId);

            reqByEmpId = currReq.getReqByEmpId(empId);
            ctx.json(reqByEmpId);
            reqByEmpId.clear();
        }
        else{
            ctx.redirect("login.html");
        }    
    }
    
    public void callGetReqbyDepartment(Context ctx) {
        if (ctx.cookieStore("security")!=null){
            int department = AuthController.loggedIn.get(ctx.cookieStore("security")).getDepartment();
            List<Request> reqByEmpId = currReq.getReqByDepartment(department);
            ctx.json(reqByEmpId);
        }
        else{
            ctx.redirect("login.html");
        }    
    } 

    public void callGetAllRequests(Context ctx){
        if (ctx.cookieStore("security")!=null){    
            List<Request> reqByEmpId;
            RequestDao allReq = new RequestDao();
            try {
                reqByEmpId = allReq.selectAll(); 
                ctx.json(reqByEmpId);
            } catch (SQLException e) {
                ctx.redirect("index-benco.html");
                e.printStackTrace();
            }
        }
        else{
            ctx.redirect("login.html");
        }
    }

    public void callGetSingleReimbursement(Context ctx){
        if (ctx.cookieStore("security")!=null){    
            int reqId = Integer.parseInt(ctx.pathParam("id"));
            ReimbursementDao getReimbur = new ReimbursementDao();
            Reimbursement thisReimbur = new Reimbursement();
            try {
                thisReimbur = getReimbur.selectByReqId(reqId);
                ctx.json(thisReimbur);
            } catch (SQLException e) {
                if(AuthController.loggedIn.get(ctx.cookieStore("security")).getTitle() != Title.BENCO)
                    ctx.redirect("view-employee-requests.html");
                else{
                    ctx.redirect("view-all-requests.html");
                }
                e.printStackTrace();
            }
        }
        else{
            ctx.redirect("login.html");
        }
    }

    public void approveRequest(Context ctx){
        if (ctx.cookieStore("security")!=null){
            String approval = ctx.formParam("approval");
            String approver = ctx.formParam("title");
            int reqId = Integer.parseInt(ctx.formParam("reqId"));
            RequestDao getReq = new RequestDao();
            Request thisReq = new Request();
            EmployeeDao getEmp = new EmployeeDao();
            Employee thisEmp = new Employee();
            try {
                thisReq = getReq.select(reqId);
                thisEmp = getEmp.select(thisReq.getEmpId());
            } catch (SQLException e) {
                if(AuthController.loggedIn.get(ctx.cookieStore("security")).getTitle() != Title.BENCO)
                    ctx.redirect("view-requests-department.html");
                else{
                    ctx.redirect("view-all-requests.html");
                }
                e.printStackTrace();
            }
            if (thisEmp.getTitle() != Title.BENCO)
                if(AuthController.loggedIn.get(ctx.cookieStore("security")).getTitle().toString().equals(approver)){
                    if(approval.equals("APPROVE")){
                        currReq.employeeNonBencoApprove(reqId,AuthController.loggedIn.get(ctx.cookieStore("security")));
                    }else{
                        currReq.employeeNonBencoDeny(reqId,AuthController.loggedIn.get(ctx.cookieStore("security")));
                    }
                    if(AuthController.loggedIn.get(ctx.cookieStore("security")).getTitle() != Title.BENCO)
                        ctx.redirect("view-requests-department.html");
                    else{
                        ctx.redirect("view-all-requests.html");
                    }
                }else{
                    ctx.redirect("view-requests-department.html");
                }
            else{
                if (approver.equals("SUPERVISOR")){
                    approver = "BENCO_SUPERVISOR";
                };
                if (approver.equals("DEPARTMENT_HEAD")){
                    approver = "BENCO_DEPARTMENT_HEAD";
                };
                if(AuthController.loggedIn.get(ctx.cookieStore("security")).getTitle().toString().equals(approver)){
                    if(approval.equals("APPROVE")){
                        currReq.employeeIsBencoApprove(reqId,AuthController.loggedIn.get(ctx.cookieStore("security")));
                    }else{
                        currReq.employeeIsBencoDeny(reqId,AuthController.loggedIn.get(ctx.cookieStore("security")));
                    }
                    if(AuthController.loggedIn.get(ctx.cookieStore("security")).getTitle() != Title.BENCO)
                        ctx.redirect("view-requests-department.html");
                    else{
                        ctx.redirect("view-all-requests.html");
                    }
                }else{
                    ctx.redirect("view-requests-department.html");
                }
            }
        }else{
            ctx.redirect("login.html");
        } 
    } 

    public void callGetEmployee(Context ctx){
        if (ctx.cookieStore("security")!=null){
            EmployeeDao getEmp = new EmployeeDao();
            Employee emp = new Employee();
            try {
                emp = getEmp.select(AuthController.loggedIn.get(ctx.cookieStore("security")).getEmpId());
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ctx.json(emp);
        }else{
            ctx.redirect("login.html");
        }
    }

    public void callGetAllReimbursements(Context ctx){
        if (ctx.cookieStore("security")!=null){    
            List<Reimbursement> reimburs;
            ReimbursementDao getReimbur = new ReimbursementDao();
            try {
                reimburs = getReimbur.selectAll();
                ctx.json(reimburs);
            } catch (SQLException e) {
                ctx.redirect("index-assoc.html");
                e.printStackTrace();
            }
        }
        else{
            ctx.redirect("login.html");
        }
    }

    public void callSendMessage(Context ctx){
        String message = ctx.formParam("message");
        if (message.equals("")){
            message = null;
            ctx.redirect("view-employee-requests.html");
        }

        int toEmp = Integer.parseInt(ctx.formParam("toEmp"));
        int fromEmp = AuthController.loggedIn.get(ctx.cookieStore("security")).getEmpId();
        int reqId = Integer.parseInt(ctx.formParam("reqId"));
        MessDoc send = new MessDoc();
        
        send.SendMessage(new Message(fromEmp,toEmp,message,reqId));
        if ((AuthController.loggedIn.get(ctx.cookieStore("security")).getTitle() == Title.ASSOCIATE))
            ctx.redirect("view-employee-requests.html");
        else if (AuthController.loggedIn.get(ctx.cookieStore("security")).getTitle() == Title.BENCO)
            ctx.redirect("view-all-requests.html");
        else
        ctx.redirect("view-requests-department.html");
    }

    public void callGetMessages(Context ctx){
        List<Message> messages = new ArrayList<>();
        MessageDao getMessages = new MessageDao();
        int reqId = Integer.parseInt(ctx.pathParam("id"));
        

        try {
            messages = getMessages.selectAllByReqId(reqId);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ctx.json(messages);
    }

    public void callFinalApproval(Context ctx){
        int reqId = Integer.parseInt(ctx.formParam("reqId"));
        double actualAmount = Double.parseDouble(ctx.formParam("actualAmount"));

        System.out.println(reqId);
        System.out.println(actualAmount);

        Reimbursement approve = new Reimbursement(status.PENDING, 0.00, actualAmount, reqId);

        currReq.finalApproval(approve);

        ctx.redirect("view-all-requests.html");

    }

    /* public void callUpload(Context ctx) throws IOException {
        UploadedFile file = ctx.uploadedFile("myFile");
        System.out.println(file);
        String fileType = ctx.formParam("fileType");
        int reqId = Integer.parseInt(ctx.formParam("reqId"));
        //Supporting sup = new Supporting(fileType, reqId);
        //sup.setFile(file.getContent().readAllBytes());
        //other.Upload(sup);
    } */
    
}
