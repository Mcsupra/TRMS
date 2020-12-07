package TRMS.TRMS_Services;

import java.sql.SQLException;

import TRMS.TRMSDao.MessageDao;
import TRMS.TRMSDao.SupportingDao;
import TRMS.TRMSPojos.Message;
import TRMS.TRMSPojos.Supporting;

public class MessDoc {

    private MessageDao mess = new MessageDao();
    private SupportingDao supporting = new SupportingDao();
    
    public void SendMessage(Message message) {
        try {
            mess.insert(message);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void Upload(Supporting doc){
    
        try {
            supporting.insert(doc);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }   
    
}
