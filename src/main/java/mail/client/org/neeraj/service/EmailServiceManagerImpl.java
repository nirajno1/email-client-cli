package mail.client.org.neeraj.service;

import mail.client.org.neeraj.manager.EmailManager;
import mail.client.org.neeraj.manager.MSExchangeEmailManager;
import mail.client.org.neeraj.manager.SMTPEmailManager;

public class EmailServiceManagerImpl implements EmailServiceManager{
	private EmailManager emailManager;
	private final String CONFIG_PATH;
    
    public EmailServiceManagerImpl(String CONFIG_PATH) {
		this.CONFIG_PATH=CONFIG_PATH;
	}
    public EmailManager fetchEmailManager(final String Type) {
    	if(emailManager== null) {
    		if(EXCHANGE_SERVER.equals(Type)){
    		emailManager= new MSExchangeEmailManager(CONFIG_PATH);
    		}else if(SMTP_SERVER.equals(Type)){
    			emailManager=new SMTPEmailManager(CONFIG_PATH);
    		}
    	}
    	return emailManager;
    }
}
