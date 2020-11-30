package TRMS.TRMS_Services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import TRMS.TRMSDao.AccountsDao;
import TRMS.TRMSPojos.Account;

public class AuthServiceImpl implements AuthService {

    private static byte[] salt = SecureRandom.getSeed(16);
    private Map<String, String> tokenRepo = new HashMap<>();
    private AccountsDao getAccount = new AccountsDao();
    private Account currAcct = new Account();
    private static Logger log = Logger.getLogger("Web");
    
    @Override
    public boolean authenticateUser(String username, String password) {
        try {
            currAcct = getAccount.selectByUsername(username);
        } catch (SQLException e) {
            log.error("SQL Exception:" + e);
            e.printStackTrace();
        }
		if (currAcct.getUsername().equals(username) && currAcct.getPassphrase().equals(password)) {
			return true;
		}
		
		return false;
	}

	@Override
	public String createToken(String username) {
		String token = simpleHash(username);
		tokenRepo.put(token, username);
		return token;
	}

	@Override
	public String validateToken(String token) {
		return tokenRepo.get(token);
	}
	
	private String simpleHash(String username) {
		
		String hash = null;
		
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-512");
			md.update(salt);
			
			byte[] bytes = md.digest(username.getBytes());

			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(0));
			}
			
			hash = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			log.error("No such algorithm:" + e);
			e.printStackTrace();
		}
		
		return hash;
	}
    
}
