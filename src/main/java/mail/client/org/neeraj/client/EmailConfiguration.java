package mail.client.org.neeraj.client;

public class EmailConfiguration {
	private String hostname;
	private String username;
	private String password;

	public EmailConfiguration(String hostname, String username, String password) {
		super();
		this.hostname = hostname;
		this.username = username;
		this.password = password;
	}

	public String getHostname() {
		return hostname;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
}
