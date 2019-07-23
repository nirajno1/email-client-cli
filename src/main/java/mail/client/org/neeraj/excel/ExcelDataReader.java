package mail.client.org.neeraj.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import mail.client.org.neeraj.client.EmailDetail;

public class ExcelDataReader {
	private String dataFilePath;
	private final static String DATA_FILE="app.email.addresses";
	
	public ExcelDataReader(String dataFilePath) {
		this.dataFilePath=dataFilePath;
	}
	
	private String readDataFile() throws FileNotFoundException, IOException {
		Properties prop=new Properties();
		File file=new File(dataFilePath);
		prop.load(new FileInputStream(file));
		return prop.getProperty(DATA_FILE);
	}

	public List<EmailDetail> readEmailDetails() throws EncryptedDocumentException, IOException {
		File excelFile=new File(readDataFile());
		List<EmailDetail> emails=null;
		if(excelFile.exists()) {
			Workbook workBook=WorkbookFactory.create(excelFile);
			Sheet sheet=workBook.getSheetAt(0);
			emails=getEmailDetails(sheet);
			
			workBook.close();
		}else {
			System.out.println("Unable to find the file "+excelFile.getAbsolutePath());
		}
		return emails;
	}
	 	
	private List<EmailDetail> getEmailDetails(Sheet sheet) {
		List<EmailDetail> emails=null;
		for(Row row:sheet) {
			emails=(emails== null)?new ArrayList<EmailDetail>():emails;
			Cell cell=row.getCell(0);
			if(cell != null ){
				String emailAdds=cell.getStringCellValue();
				if(emailAdds!= null && ! emailAdds.isEmpty()) {
					EmailDetail emailDetail=new EmailDetail();
					emailDetail.setReceiver(cell.getStringCellValue());

					cell=row.getCell(1);
					if(cell!=null) {
						emailDetail.setSubject(cell.getStringCellValue());
					}
					cell=row.getCell(2);
					if(cell!=null) {
						emailDetail.setBody(cell.getStringCellValue());
					}

					emails.add(emailDetail);
				}
			}
		}
		return emails;
	}
	
}
