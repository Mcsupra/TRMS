package TRMS.Driver;

import org.apache.log4j.Logger;

import TRMS.TRMS_Controller.AuthController;
import TRMS.TRMS_Controller.WebController;
import io.javalin.Javalin;

public interface ServerDriver {
    public static WebController web = new WebController();
    public static AuthController auth = new AuthController();
    static Logger log = Logger.getLogger("Web");

    public static void main(String[] args) {
        
        Javalin app = Javalin.create(
                config -> {config.addStaticFiles("/public"); }).start(9091);
                
        app.get("/hello", ctx -> ctx.html("Hello World"));
        app.get("", ctx -> ctx.redirect("login.html"));
        app.post("/login", ctx -> auth.login(ctx));
        app.get("/login", ctx -> auth.checkUser(ctx));
        app.get("/logout", ctx -> auth.logout(ctx)); 
        app.post("/account", ctx -> web.callCreateAccount(ctx));
        app.post("/employee", ctx -> web.callCreateEmployee(ctx));
        app.post("/request", ctx -> web.callCreateRequest(ctx));

        app.get("/view-requests-department", ctx -> ctx.redirect("view-requests-department.html"));
        app.post("/view-requests-department.html", ctx -> web.callGetReqbyDepartment(ctx));
        app.post("/approve-request", ctx -> web.approveRequest(ctx));
        app.get("/view-employee-requests", ctx -> ctx.redirect("view-employee-requests.html"));
        app.post("/view-employee-requests.html", ctx -> web.callGetReqbyEmpId(ctx));
        app.get("/view-employee", ctx -> ctx.redirect("view-employee.html"));
        app.post("/view-employee.html", ctx -> web.callGetEmployee(ctx));
        app.get("/view-reimbursement/:id", ctx -> ctx.req.getRequestDispatcher("/view-reimbursement.html").forward(ctx.req, ctx.res));
        app.post("/view-reimbursement/:id", ctx -> web.callGetSingleReimbursement(ctx));
        app.get("/view-all-requests", ctx -> ctx.redirect("view-all-requests.html"));
        app.post("/view-all-requests.html", ctx -> web.callGetAllRequests(ctx));   
        app.get("/reimbursement-approval/:id", ctx -> ctx.req.getRequestDispatcher("/reimbursement-approval.html").forward(ctx.req, ctx.res));
        app.post("/reimbursement-approval/:id", ctx -> web.callGetSingleReimbursement(ctx));
        app.post("/update-reimbursement", ctx -> web.callFinalApproval(ctx));
        app.get("/message/:id", ctx -> ctx.req.getRequestDispatcher("/message.html").forward(ctx.req, ctx.res));
        app.post("/message/:id", ctx -> web.callGetMessages(ctx));
        app.get("/send-message", ctx -> ctx.req.getRequestDispatcher("/send-message.html").forward(ctx.req, ctx.res));
        app.post("send-message", ctx -> web.callSendMessage(ctx));
        app.get("/upload/:id", ctx -> ctx.req.getRequestDispatcher("/upload.html").forward(ctx.req, ctx.res));
        //app.post("/upload", ctx -> web.callUpload(ctx));

        app.get("/reimbursement-approval", ctx -> {
            ctx.req.getRequestDispatcher("reimbursement-approval.html").forward(ctx.req, ctx.res);
        }); 
        app.post("/reimbursement-approval.html", ctx -> web.callGetSingleReimbursement(ctx)); 
        
        app.get("/employee", ctx -> {
            if(ctx.cookieStore("security") != null)
                ctx.req.getRequestDispatcher("employee.html").forward(ctx.req, ctx.res);
            else
                ctx.redirect("login.html");
        });

        app.get("/account", ctx -> {
            if(ctx.cookieStore("security") != null)
                ctx.req.getRequestDispatcher("account.html").forward(ctx.req, ctx.res);                   
            else
                ctx.redirect("login.html");
        });
                               
        app.get("/request", ctx -> {
            if(ctx.cookieStore("security") != null)
                ctx.req.getRequestDispatcher("request.html").forward(ctx.req, ctx.res);
            else
                ctx.redirect("login.html");
        });

    }
}
