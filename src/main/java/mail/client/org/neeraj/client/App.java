package mail.client.org.neeraj.client;

import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;

import mail.client.org.neeraj.executor.EmailSenderManager;
import mail.client.org.neeraj.manager.EmailManager;
import mail.client.org.neeraj.msg.MessageBodyBuilder;
import mail.client.org.neeraj.service.EmailServiceManager;
import mail.client.org.neeraj.service.EmailServiceManagerImpl;


public class App 
{
    private static final String CONFIG_PATH = "./resource/email.properties";
    private static final String DATA_FILE = "./resource/app.properties";
    
    public static void main(String[] args) {
    	callSMTP();
    	System.out.println("done");
    }
	
    private static void callSMTP(){
    	EmailServiceManager serviceManager=new EmailServiceManagerImpl(CONFIG_PATH);
    	MessageBodyBuilder builder=new MessageBodyBuilder(CONFIG_PATH);
    	try {
    		EmailSenderManager emailSenderManager=new EmailSenderManager(DATA_FILE,serviceManager);
    		emailSenderManager.sendEmails(builder.buildMessageBody(),EmailServiceManager.SMTP_SERVER);
    	} catch (EncryptedDocumentException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    private static void callExchange(){
    	EmailServiceManager serviceManager=new EmailServiceManagerImpl(CONFIG_PATH);
    	EmailManager manager=serviceManager.fetchEmailManager(EmailServiceManager.EXCHANGE_SERVER);
    	manager.readMessage();
    }
	
	
}
