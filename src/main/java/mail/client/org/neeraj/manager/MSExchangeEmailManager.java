package mail.client.org.neeraj.manager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import mail.client.org.neeraj.client.EmailConfiguration;
import microsoft.exchange.webservices.data.BasePropertySet;
import microsoft.exchange.webservices.data.BodyType;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.ExchangeCredentials;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.ExchangeVersion;
import microsoft.exchange.webservices.data.ExtendedPropertyCollection;
import microsoft.exchange.webservices.data.FindItemsResults;
import microsoft.exchange.webservices.data.Item;
import microsoft.exchange.webservices.data.ItemSchema;
import microsoft.exchange.webservices.data.ItemView;
import microsoft.exchange.webservices.data.MessageBody;
import microsoft.exchange.webservices.data.PropertySet;
import microsoft.exchange.webservices.data.WebCredentials;
import microsoft.exchange.webservices.data.WellKnownFolderName;

public class MSExchangeEmailManager implements EmailManager {
	private EmailConfiguration configuration;
	public MSExchangeEmailManager(String mailConfig) {
		Properties prop=new Properties();
		try {
			prop.load(new FileInputStream(mailConfig));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.configuration = new EmailConfiguration(prop.getProperty("app.mail.host"),
				prop.getProperty("app.email.username"),prop.getProperty("app.email.password"));
	}
	
	/* (non-Javadoc)
	 * @see mail.client.org.neeraj.client.EmailManagerI#connect()
	 */
	public ExchangeService connect() throws URISyntaxException {
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010);
		ExchangeCredentials credentials = new WebCredentials(configuration.getUsername(), configuration.getPassword());
		service.setCredentials(credentials);
		URI uri=new URI(configuration.getHostname());
		service.setUrl(uri);
		return service;
	}
	
	/* (non-Javadoc)
	 * @see mail.client.org.neeraj.client.EmailManagerI#sendMessage(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendMessage(String recipient, String subject, String content){
		ExchangeService service;
		try {
			service = connect();
			EmailMessage message = new EmailMessage(service);
			message.setSubject(subject);
			message.getToRecipients().add(recipient);
			MessageBody body = new MessageBody(BodyType.HTML,content);
			message.setBody(body);
			synchronized (this) {
				message.sendAndSaveCopy();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readMessage(){
		try {
			ExchangeService service = connect();
			ItemView view = new ItemView (2);
			FindItemsResults<Item> findResults = service.findItems(WellKnownFolderName.Inbox, view);

			for(Item item : findResults.getItems()){
				item.load(new PropertySet(BasePropertySet.FirstClassProperties, ItemSchema.MimeContent));
				//System.out.println("id==========" + item.getId());
				System.out.println("sub==========" + item.getSubject());
				System.out.println(item.getDisplayTo());
				System.out.println();
				ExtendedPropertyCollection collection =item.getExtendedProperties();
				collection.forEach(e->System.out.println(e.getValue()));
				
				//System.out.println("sub==========" + item.getMimeContent());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
