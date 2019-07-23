package mail.client.org.neeraj.service;

import mail.client.org.neeraj.manager.EmailManager;

public interface EmailServiceManager {
    public static final String EXCHANGE_SERVER = "MSEXCHANGE";
	public static final String SMTP_SERVER = "SMTP";
	public EmailManager fetchEmailManager(final String Type);
}
