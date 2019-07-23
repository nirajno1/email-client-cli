package mail.client.org.neeraj.executor;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import org.apache.poi.EncryptedDocumentException;

import mail.client.org.neeraj.client.EmailDetail;
import mail.client.org.neeraj.excel.ExcelDataReader;
import mail.client.org.neeraj.manager.EmailManager;
import mail.client.org.neeraj.service.EmailServiceManager;

public class EmailSenderManager {
    private final String DATA_FILE;
    private List<EmailDetail> emailDetails;
    private EmailManager emailManager;
	private ExcelDataReader reader;
	private EmailServiceManager serviceManager;
	
    public EmailSenderManager(String DATA_FILE,EmailServiceManager serviceManager){
    	this.DATA_FILE=DATA_FILE;
    	this.serviceManager=serviceManager;
    }
    
    public List<EmailDetail> fetchEmailDetails() throws EncryptedDocumentException, IOException{
    	if(reader== null) {
    		reader=new ExcelDataReader(DATA_FILE);
    	}
    	if(emailDetails==null) {
    		emailDetails=reader.readEmailDetails();
    	}
    	return emailDetails;
    }
    
    public void sendEmails(final String Type) throws EncryptedDocumentException, IOException {
    	fetchEmailDetails();
    	serviceManager.fetchEmailManager(Type);
    	if(emailDetails!= null && !emailDetails.isEmpty() && emailManager!= null ) {
    		System.out.println("Email sending started, total number of emails to send: "+(emailDetails.size()-1));
    		emailDetails.remove(0);
    		for(EmailDetail email:emailDetails) {
    			Timer timer = new Timer();
    			int delay = (5 + new Random().nextInt(5)) * 1000;
    			timer.schedule(new EmailSender(timer,email,emailManager), delay);
    		}
    	}
    }
    
    public void sendEmails(String bodyContent,final String Type) throws EncryptedDocumentException, IOException {
    	fetchEmailDetails();
    	emailManager=serviceManager.fetchEmailManager(Type);
    	if(emailDetails!= null && !emailDetails.isEmpty() && emailManager!= null ) {
    		System.out.println("Email sending started, total number of emails to send: "+(emailDetails.size()-1));
    		emailDetails.remove(0);
    		for(EmailDetail email:emailDetails) {
    			Timer timer = new Timer();
    			int delay = (30 + new Random().nextInt(15)) * 1000;
    			email.setBody(bodyContent);
    			timer.schedule(new EmailSender(timer,email,emailManager), delay);
    		}
    	}
    }

}
