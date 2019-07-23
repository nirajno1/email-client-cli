package mail.client.org.neeraj.executor;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.poi.EncryptedDocumentException;

import mail.client.org.neeraj.client.EmailDetail;
import mail.client.org.neeraj.manager.EmailManager;

public class EmailSender extends TimerTask {
	
	EmailDetail email;
	EmailManager emailManager;
	Timer timer;
   
	public EmailSender(Timer timer, EmailDetail email,EmailManager emailManager){
    	this.email=email;
    	this.emailManager=emailManager;
    	this.timer=timer;
    }
   
    public EmailSender() {
    	
    }
	@Override
	public void run() {
		try {
			emailManager.sendMessage(email.getReceiver(),email.getSubject(),email.getBody());
			System.out.println("Email sent to :"+email.getReceiver() + " at: "+new Date());

		} catch (EncryptedDocumentException e) {
			System.err.println("There is an error while sending emails");
			e.printStackTrace();
		}finally {
			timer.cancel();
		}

	}

}
