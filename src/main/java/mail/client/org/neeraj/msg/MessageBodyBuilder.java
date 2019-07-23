package mail.client.org.neeraj.msg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

import org.apache.commons.compress.utils.IOUtils;

public class MessageBodyBuilder {
	private FileInputStream emailSignImg;
	private File emailSignTxt;
	private File emailBodyTxt;
	private static final String IMAGE_TAG="##image##";
	private static final String SIGN_TAG="##sign##";
	
	public MessageBodyBuilder(String mailConfig) {
		Properties prop=new Properties();
		try {
			prop.load(new FileInputStream(mailConfig));
			String imgLoc=prop.getProperty("app.email.signImg");
			emailSignImg =new FileInputStream(new File(imgLoc));
			String signLoc=prop.getProperty("app.email.signTxt");
			emailSignTxt=new File(signLoc);
			String bodyTxtLoc=prop.getProperty("app.email.bodyTxt");
			emailBodyTxt=new File(bodyTxtLoc);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String buildMessageBody() throws IOException {
		StringBuffer bodyBuffer=readBody();
		StringBuffer signBuffer=readSign();
		if(signBuffer!= null) {
			replaceTagWithValue(bodyBuffer, SIGN_TAG,signBuffer.toString());
			replaceTagWithValue(bodyBuffer, IMAGE_TAG,encodeBase64Image());
		}
		return bodyBuffer.toString();
	}
	
	private StringBuffer readBody() {
		return readTextFile(emailBodyTxt);
	}
	private StringBuffer readSign() {
		return readTextFile(emailSignTxt);
	}	
	private String encodeBase64Image() throws IOException {
		byte[] fileContent = IOUtils.toByteArray(emailSignImg);
		String encodedString = Base64.getEncoder().encodeToString(fileContent);
		return encodedString;
	}
	
	private StringBuffer readTextFile(File txtFile) {
		StringBuffer strBuf=new StringBuffer();
		BufferedReader reader=null;
		try {
			reader=new BufferedReader(new FileReader(txtFile));
			String str=null;
			while((str=reader.readLine())!=null) {
				strBuf.append(str);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(reader!= null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return strBuf;
	}
	
	private void replaceTagWithValue(StringBuffer sBuffer, final String tag, String value) {
		int startIndex=sBuffer.indexOf(tag);
		int endIndex=startIndex+tag.length();
		sBuffer.replace(startIndex, endIndex,value);
	}
}
