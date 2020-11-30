package TRMS.TRMS_Controller;

import java.sql.SQLException;
import java.util.List;

import TRMS.TRMSDao.RequestDao;
import TRMS.TRMSPojos.Request;
import TRMS.TRMSPojos.Employee.Title;
import TRMS.TRMSPojos.Request.eventType;
import TRMS.TRMS_Services.LogInImpl;
import TRMS.TRMS_Services.RequestApproval;
import io.javalin.http.Context;

public class WebController {
    private LogInImpl currUser = new LogInImpl();
    private RequestApproval currReq = new RequestApproval();

    public void callCreateEmployee(Context ctx) {
        
        if (ctx.cookieStore("security")!=null){ 
            
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

            boolean newReq = currReq.createRequest(eventDate, eventTime, eventLoc, event, eventCost, empId);

            if (newReq) {
                ctx.status(200);
                ctx.redirect("index.html");
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
            int empId = Integer.parseInt(ctx.pathParam("id")); 
            List<Request> reqByEmpId = currReq.getReqByEmpId(empId);

            reqByEmpId = currReq.getReqByEmpId(empId);
            ctx.json(reqByEmpId);
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
                ctx.redirect("view-requests.html");
                e.printStackTrace();
            }
        }
        else{
            ctx.redirect("login.html");
        }
    }

    public void callGetRequest(Context ctx){
        if (ctx.cookieStore("security")!=null){    
            int reqId = Integer.parseInt(ctx.pathParam("id"));
            RequestDao getReq = new RequestDao();
            Request thisReq = new Request();
            System.out.println(reqId);
            try {
                thisReq = getReq.select(reqId);
                ctx.json(thisReq);
            } catch (SQLException e) {
                ctx.redirect("view-requests-department.html");
                e.printStackTrace();
            }
        }
        else{
            ctx.redirect("login.html");
        }
    }

    public void approveRequestNonBenco(Context ctx){
        if (ctx.cookieStore("security")!=null){
            String approval = ctx.formParam("approval");
            int reqId = Integer.parseInt(ctx.formParam("reqId"));
            if(approval.equals("APPROVE")){
                currReq.employeeNonBencoApprove(reqId,AuthController.loggedIn.get(ctx.cookieStore("security")));
            }else{
                currReq.employeeNonBencoDeny(reqId,AuthController.loggedIn.get(ctx.cookieStore("security")));
            }
            ctx.redirect("view-requests-department.html");
        } 
    } 
}
