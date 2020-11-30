package TRMS.TRMS_Controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import TRMS.TRMSDao.AccountsDao;
import TRMS.TRMSDao.EmployeeDao;
import TRMS.TRMSPojos.Employee;
import TRMS.TRMS_Services.AuthServiceImpl;
import io.javalin.http.Context;

public class AuthController {

	public static Map<String, Employee> loggedIn = new HashMap<>();
	private EmployeeDao getEmp = new EmployeeDao();
	private AccountsDao getId = new AccountsDao();
	private AuthServiceImpl auth = new AuthServiceImpl();

	public void login(Context ctx) {
		logout(ctx);
		String username = ctx.formParam("username");
		String password = ctx.formParam("password");
		boolean authenticated = auth.authenticateUser(username, password);
		if (authenticated) {
			ctx.status(200);
			ctx.cookieStore("security", auth.createToken(username));
			try {
				loggedIn.put(ctx.cookieStore("security"),getEmp.select(getId.getEmpId(username)));
			} catch (SQLException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
			
			switch(loggedIn.get(ctx.cookieStore("security")).getTitle()){
				case ASSOCIATE:
					ctx.redirect("index-assoc.html");
					break;
				case SUPERVISOR:
					ctx.redirect("index-manager.html");
					break;
				case DEPARTMENT_HEAD:
					ctx.redirect("index-manager.html");
					break;
				case IT:
					ctx.redirect("index.html");
					break;
				default:
					ctx.redirect("index-benco.html");
					break;
			}
		} else {
            ctx.status(401);
            ctx.html("login fail");
			ctx.redirect("login.html?error=failed-login");
		}
	}
	
	public void logout(Context ctx) {
		loggedIn.remove(ctx.cookieStore("security"));
		ctx.clearCookieStore();
		ctx.redirect("login.html");
	}
	public void checkUser(Context ctx) {
		ctx.html(auth.validateToken(ctx.cookieStore("security")));
	}
    
}
