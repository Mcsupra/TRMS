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
    
        Javalin app=Javalin.create(
                config -> {config.addStaticFiles("/public"); }).start(9091);
                
        app.get("/hello", ctx -> ctx.html("Hello World"));
        app.get("", ctx -> ctx.redirect("login.html"));
        app.post("/login", ctx -> auth.login(ctx));
        app.get("/login", ctx -> auth.checkUser(ctx));
        app.get("/view-requests-department", ctx -> ctx.redirect("view-requests-department.html"));
        app.post("/view-requests-department.html", ctx -> web.callGetReqbyDepartment(ctx));
        app.post("/approve-request", ctx -> web.approveRequestNonBenco(ctx));
        app.get("/view-all-requests", ctx -> web.callGetAllRequests(ctx));
        app.get("/logout",ctx -> auth.logout(ctx)); 
        app.post("/account", ctx -> web.callCreateAccount(ctx));
        app.post("/employee", ctx -> web.callCreateEmployee(ctx));
        app.post("/request", ctx -> web.callCreateRequest(ctx));

        app.get("/employee", ctx -> {
            if(ctx.cookieStore("security") != null)
                ctx.redirect("employee.html");
            else
                ctx.redirect("login.html");
        });
        app.get("/account", ctx -> {
            if(ctx.cookieStore("security") != null)
                ctx.redirect("account.html");
            else
                ctx.redirect("login.html");
        });
        app.get("/request", ctx -> {
            if(ctx.cookieStore("security") != null)
                ctx.redirect("request.html");
            else
                ctx.redirect("login.html");
        });
    }
}
