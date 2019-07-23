package mail.client.org.neeraj.manager;

public interface EmailManager {
	void sendMessage(String recipient, String subject, String content);
	void readMessage();
}