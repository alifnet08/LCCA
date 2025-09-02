package com.wo.module.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import com.wo.module.common.util.SFTPUtil;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.sshtools.sftp.SftpFile;
import com.wo.module.common.config.ApplicationContextProvider;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.util.LDAPApi;
import com.wo.module.division.model.Division;
import com.wo.module.division.service.DivisionService;
import com.wo.module.engine.ReadXlsController.Wrapper;
import com.wo.module.engine.service.OscarJobOracleService;
import com.wo.module.engine.service.OscarJobService;
import com.wo.module.engine.service.SendEmailService;
import com.wo.module.log.model.LogDetail;
import com.wo.module.log.model.LogHeader;
import com.wo.module.log.service.LogService;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.user.model.User;

public class ReadXlsController implements Job {

	@SuppressWarnings("unused")
	private static final String OSCAR_IN_ = "oscar_";
	private static Logger log = Logger.getLogger(InboundController.class);
	private static ReadXlsController readXlsController;

	private final String functionId = "Inbound";
	private final String userId = "SYSTEM";
	
	private ApplicationContext appContext;
	private LogService logService;
	private DivisionService divisionService;
	private OscarJobOracleService oscarJobOracleService;
	private OscarJobService oscarJobService;
	private ParameterDetailService parameterDetailService;

	public ReadXlsController() {
	}

	public static synchronized ReadXlsController getInstance() {
		if (readXlsController == null) {
			readXlsController = new ReadXlsController();
		}
		return readXlsController;
	}

	public class Wrapper {
		public Object ref;

		public Wrapper(Object ref) {
			this.ref = ref;
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Start Session Inbound");

		try {
			initInjection(context);
			
			LogHeader logH = new LogHeader(DateUtil.currentSqlTimestamp(), functionId,
					ParameterDetail.PARAM_DET_LOG_PROCESS_STATUS_I, userId);
			
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String dateStr = sdf.format(date);
			
			logH.setLogDetailList(new ArrayList<LogDetail>());
			String location = "Create Log Header";
			String msg = "";
			Wrapper seq = new Wrapper(new Long(0));
			boolean cont = true;
			boolean error = false;
			try {
				getLogService().save(logH);
			} catch (Exception ex) {
				msg = "Saving Log fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			String host = oscarJobService
					.getSystemProperty(ParameterDetail.PARAM_DET_CODE_SFTP_HOST);
			
			String user = oscarJobService
					.getSystemProperty(ParameterDetail.PARAM_DET_CODE_SFTP_USER);
			
			String password = oscarJobService
					.getSystemProperty(ParameterDetail.PARAM_DET_CODE_SFTP_PASS);
			
			location = "Connect to SFTP";
			SFTPUtil sftp = null;
			try {
				sftp = new SFTPUtil(host);
				sftp.connectByUsername(user, password);
			} catch (Exception ex) {
				msg = "Connect to " + host + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			
			String remoteDir = oscarJobService
				.getSystemProperty(ParameterDetail.PARAM_DET_CODE_SFTP_PATH);
			
			String fileName = oscarJobService
					.getSystemProperty(ParameterDetail.PARAM_DET_CODE_SFTP_FILENAME);
			
			String slocal = oscarJobService
					.getSystemProperty(ParameterDetail.PARAM_DET_CODE_SFTP_LOCAL);
						
			location = "Retrieve file(s) from SFTP"; 
			List<SftpFile> sftpFiles = null; 
			int success = 0; 
			int fail = 0; 
			Map<String, String> retrievedFiles = null; 
			SftpFile sFile = null;
			try {
			  if (!error && sftp != null) { 
			  sftpFiles = sftp.getFiles(remoteDir);
			  System.out.print("sftpFiles"+ sftpFiles);
			  if(StringUtils.isEmpty(fileName)){
				  		sFile = sftpFiles.get(0); // get file pertama
			  }else{
				  for (int i = 0; i < sftpFiles.size(); i++) { 
					  SftpFile sFileNew = sftpFiles.get(i); 
					  if(sFileNew.getFilename()!=null && fileName.equals(sFileNew.getFilename())){
						  sFile = sFileNew;
						  sftp.get(sFile.getAbsolutePath(), slocal+fileName);
						  break;
					  }
			       } 
			  }
			  
			  
			  }
			} catch (Exception ex) {
				ex.printStackTrace();
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
				cont = false;
			}
			
			location = "Connect to LDAP";
			Boolean flag = false;
			try {
				if (!error) { 
				flag = LDAPApi.performAuthentication(parameterDetailService);
				}
			} catch (Exception ex) {
				msg = "Connect to LDAP fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			if(!flag){
				msg = "Connect to LDAP fail because " + " Authentication failed ";
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
			}
			
			location = "Retrieve User from CSV File";
			List<User> users = null;
			try {
				if (!error && sFile != null) { 
					
				users = readCsvFile(slocal+fileName,logH, seq, location);
				System.out.println("size==" + users.size());
				System.out.println(location + " success");
				}
				
				//users = readCsvFile("G:\\apps\\HC_NomiREG_2.txt",logH, seq, location);
			} catch (Exception ex) {
				ex.printStackTrace();
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
				cont = false;
			}
			
			location = "Update Enable Flag to N";
			try {
				if (!error) {
				oscarJobService.updateEnableFlagToN();
				System.out.println(location + " success");
				}
			} catch (Exception ex) {
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
				cont = false;
			}
		
			location = "Update user data";
			try {
				if (!error) {
				oscarJobService.updateUser(users,logH, seq, location);
				System.out.println(location + " success");
				}
			} catch (Exception ex) {
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
				cont = false;
				oscarJobService.updateEnableFlagToY();
				
			}
			
			location = "Update division id";
			try {
				if (!error) {
				oscarJobService.updateDivisionId();
				System.out.println(location + " success");
				}
			} catch (Exception ex) {
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
				cont = false;
			}
			
			/*location = "Update Enable Flag to Y";
			try {
				if (!error) {
				oscarJobService.updateEnableFlagToY();
				System.out.println(location + " success");
				}
			} catch (Exception ex) {
				msg = location + " fail because " + ex.getMessage();
				addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, msg);
				error = true;
				cont = false;
			}*/
			
			logH.setEndDate(DateUtil.currentSqlTimestamp());
			if (error)
				logH.setProcessSts(ParameterDetail.PARAM_DET_LOG_PROCESS_STATUS_E);
			else
				logH.setProcessSts(ParameterDetail.PARAM_DET_LOG_PROCESS_STATUS_S);
			
			getLogService().update(logH);
			
			if (sftp != null) { sftp.disconnect(); }
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("Finish Session Inbound");
		}
	}
	
	public List<User> readCsvFile(String absolutePath,LogHeader logH, Wrapper seq, String location) {

		List<User> listUser = new ArrayList<User>();
        String csvFile = absolutePath;
        String line = "";
        String cvsSplitBy = "\\|\\*\\|";
        
        
        HashMap<String, String> division = new HashMap<String, String>();
        
        String dataRow = null;
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

        	int i=0;
            while ((line = br.readLine()) != null) {

            	if(i >=0 ){
            	dataRow = "";
            	try{
            	User user = new User();
                User user2 = new User();
                
                
                String[] data = line.split(cvsSplitBy);
                

                if(!StringUtils.isEmpty(data[0])){
                	dataRow = dataRow + " Nik == "+data[0]+", ";
                	user.setNik(data[0]);
                	try {
						user.setEmail(LDAPApi.getEmailByUserID(parameterDetailService,"*"+user.getNik().substring(user.getNik().length()-5)));
					} catch (NamingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location,e.getMessage());
					}
                }
                if(!StringUtils.isEmpty(data[1])){
                	dataRow = dataRow + " Name == "+data[1]+", ";
                	user.setName(data[1]);
                }
                if(!StringUtils.isEmpty(data[2])){
                	dataRow = dataRow + " JobId == "+data[2]+", ";
                	user.setJobId(new Long(data[2]));
                }
                if(!StringUtils.isEmpty(data[3])){
                	dataRow = dataRow + " Job Name == "+data[3]+", ";
                	user.setJobName(data[3]);
                }
                if(!StringUtils.isEmpty(data[4])){
                	dataRow = dataRow + " Position Id == "+data[4]+", ";
                	user.setPositionId(new Long(data[4]));
                }
                if(!StringUtils.isEmpty(data[5])){
                	dataRow = dataRow + " Position Name == "+data[5]+", ";
                	user.setPositionName(data[5]);
                }
                if(!StringUtils.isEmpty(data[6])){
                	dataRow = dataRow + " Sub Area == "+data[6]+", ";
                	user.setSubArea(data[6]);
                	user.setBranchCode(data[6]);
                }
                if(!StringUtils.isEmpty(data[7])){
                	dataRow = dataRow + " Sub Branch == "+data[7]+", ";
                	user.setSubBranch(data[7]);
                }
                if(!StringUtils.isEmpty(data[8])){
                	dataRow = dataRow + " Branch == "+data[8]+", ";
                	user.setBranch(data[8]);
                }
                if(!StringUtils.isEmpty(data[10])){
                	dataRow = dataRow + " Region == "+data[10]+", ";
                	user.setRegion(data[10]);
                }
                if(!StringUtils.isEmpty(data[11])){
                	dataRow = dataRow + " Division Name == "+data[11]+", ";
                	user.setDivisionName(data[11]);
                	division.put(user.getDivisionName(),user.getDivisionName());
                }
                if(!StringUtils.isEmpty(data[12])){
                	dataRow = dataRow + " Directorate == "+data[12]+", ";
                	user.setDirectorate(data[12]);
                }
                if(!StringUtils.isEmpty(data[13])){
                	dataRow = dataRow + " PUK NIK == "+data[13]+", ";
                	user.setPukNik(data[13]);
                	user2.setNik(data[13]);
                	/*try {
						user2.setEmail(LDAPApi.getEmailByUserID(parameterDetailService,user2.getNik()));
					} catch (NamingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
						
					}*/
                }
                
                if(!StringUtils.isEmpty(data[14])){
                	dataRow = dataRow + " PUK Name == "+data[14]+", ";
                	user2.setName(data[14]);
                }
                if(!StringUtils.isEmpty(data[15])){
                	dataRow = dataRow + " PUK Job Id == "+data[15]+", ";
                	user2.setJobId(new Long(data[15]));
                }
                if(!StringUtils.isEmpty(data[16])){
                	dataRow = dataRow + " PUK Job Name == "+data[16]+", ";
                	user2.setJobName(data[16]);
                }
                if(!StringUtils.isEmpty(data[17])){
                	dataRow = dataRow + " PUK Position Id == "+data[17]+", ";
                	user2.setPositionId(new Long(data[17]));
                }
                if(!StringUtils.isEmpty(data[18])){
                	dataRow = dataRow + " PUK Position Name == "+data[18]+", ";
                	user2.setPositionName(data[18]);
                }
                if(!StringUtils.isEmpty(data[19])){
                	dataRow = dataRow + " PUK Sub Area == "+data[19]+", ";
                	user2.setSubArea(data[19]);
                	user2.setBranchCode(data[19]);
                }
                if(!StringUtils.isEmpty(data[20])){
                	dataRow = dataRow + " PUK Sub Branch == "+data[20]+", ";
                	user2.setSubBranch(data[19]);
                }
                /*if(!StringUtils.isEmpty(data[23])){
                	dataRow = dataRow + " PUK Branch == "+data[23]+", ";
                	user2.setBranch(data[23]);
                }*/
                if(!StringUtils.isEmpty(data[23])){
                	dataRow = dataRow + " PUK Region == "+data[23]+", ";
                	user2.setRegion(data[23]);
                }
                if(!StringUtils.isEmpty(data[24])){
                	dataRow = dataRow + " PUK Division Name == "+data[24]+", ";
                	user2.setDivisionName(data[24]);
                	division.put(user.getDivisionName(),user.getDivisionName());
                }
                if(!StringUtils.isEmpty(data[25])){
                	dataRow = dataRow + " PUK Directorate == "+data[25]+", ";
                	user2.setDirectorate(data[25]);
                }
                
                if(!StringUtils.isEmpty(user.getName())){
                    listUser.add(user);
                }
                /*if(!StringUtils.isEmpty(user2.getName())){
                    listUser.add(user2);
                }*/
            	}catch(Exception e){
            		e.printStackTrace();
            		addLogDetail(logH, seq, LogDetail.MSG_TYPE_ERROR, location, dataRow+" err msg== "+e.getMessage());
            	}
            	}
                //System.out.println("Country [code= " + country[4] + " , name=" + country[5] + "]");
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        for (String i : division.keySet()) {
            Number countData = divisionService.getCountDivisionByName(division.get(i));
            if(countData == null || countData.intValue() == 0){
            	Division div = new Division();
            	div.setDivisionName(division.get(i));
            	div.setCreatedBy("ADMIN");
            	div.setCreationDate(new Timestamp(new Date().getTime()));
            	divisionService.save(div);
            }
          }
        
       
        
        return listUser;

    }
	
	public List<User> readXlsFile() 
    {
		List<User> listUser = new ArrayList<User>();
        try
        {
        	
        	
        	
            FileInputStream file = new FileInputStream(new File("G:\\Data XLS\\Data HC.xlsx"));
 
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
 
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
 
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            int i=0;
            int x=0;
            String val="";
            while (rowIterator.hasNext()) 
            {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                User user = new User();
                User user2 = new User();
                
                if(i>0){
                	x=0;
	                while (cellIterator.hasNext()) 
	                {
	                    Cell cell = cellIterator.next();
	                    //Check the cell type and format accordingly
	                    System.out.println("Cell Type == "+cell.getCellType());
	                    val="";
	                    
	                    switch (cell.getCellType().toString()) 
	                    {
	                    	
	                        case "NUMERIC":
	                            val = ""+cell.getNumericCellValue();
	                            break;
	                        case "STRING":
	                            val = cell.getStringCellValue();
	                            break;
	                    }
	                    
	                    if(x==0 && !StringUtils.isEmpty(val)){
	                    	user.setNik(val);
	                    }
	                    else if(x==1 && !StringUtils.isEmpty(val)){
	                    	user.setName(val);
	                    }
	                    else if(x==2 && !StringUtils.isEmpty(val)){
	                    	user.setJobId(new Long(val));
	                    }
	                    else if(x==3 && !StringUtils.isEmpty(val)){
	                    	user.setJobName(val);
	                    }
	                    else if(x==4 && !StringUtils.isEmpty(val)){
	                    	user.setPositionId(new Long(val));
	                    }
	                    else if(x==5 && !StringUtils.isEmpty(val)){
	                    	user.setPositionName(val);
	                    }
	                    else if(x==6 && !StringUtils.isEmpty(val)){
	                    	user.setSubArea(val);
	                    	user.setBranchCode(val);
	                    }
	                    else if(x==7 && !StringUtils.isEmpty(val)){
	                    	user.setSubBranch(val);
	                    }
	                    else if(x==8 && !StringUtils.isEmpty(val)){
	                    	user.setBranch(val);
	                    }
	                    else if(x==9 && !StringUtils.isEmpty(val)){
	                    	user.setRegion(val);
	                    }
	                    else if(x==10 && !StringUtils.isEmpty(val)){
	                    	user.setDivisionName(val);
	                    }
	                    else if(x==11 && !StringUtils.isEmpty(val)){
	                    	user.setDirectorate(val);
	                    }
	                    else if(x==12 && !StringUtils.isEmpty(val)){
	                    	user.setPukNik(val);
	                    	user2.setNik(val);
	                    }
	                    
	                    
	                    else if(x==13 && !StringUtils.isEmpty(val)){
	                    	user.setName(val);
	                    }
	                    else if(x==14 && !StringUtils.isEmpty(val)){
	                    	user.setJobId(new Long(val));
	                    }
	                    else if(x==15 && !StringUtils.isEmpty(val)){
	                    	user.setJobName(val);
	                    }
	                    else if(x==16 && !StringUtils.isEmpty(val)){
	                    	user.setPositionId(new Long(val));
	                    }
	                    else if(x==17 && !StringUtils.isEmpty(val)){
	                    	user.setPositionName(val);
	                    }
	                    else if(x==18 && !StringUtils.isEmpty(val)){
	                    	user.setSubArea(val);
	                    	user.setBranchCode(val);
	                    }
	                    else if(x==19 && !StringUtils.isEmpty(val)){
	                    	user.setSubBranch(val);
	                    }
	                    else if(x==20 && !StringUtils.isEmpty(val)){
	                    	user.setBranch(val);
	                    }
	                    else if(x==21 && !StringUtils.isEmpty(val)){
	                    	user.setRegion(val);
	                    }
	                    else if(x==22 && !StringUtils.isEmpty(val)){
	                    	user.setDivisionName(val);
	                    }
	                    else if(x==23 && !StringUtils.isEmpty(val)){
	                    	user.setDirectorate(val);
	                    }
	                    /*else if(i==25){
	                    	user.setNikAtasan(val);
	                    }*/
	                    
	                    
	                    x++;
	                }
	                
	                if(!StringUtils.isEmpty(user.getName())){
	                    listUser.add(user);
	                    }
	                    if(!StringUtils.isEmpty(user2.getName())){
	                    listUser.add(user2);
	                }
                }
                System.out.println("");
                i++;
            }
            file.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        return listUser;
    }
	
	private void initInjection(JobExecutionContext context) {
		ApplicationContext appContext = (ApplicationContext) context.getMergedJobDataMap().get("applicationContextKey");
		parameterDetailService = appContext.getBean("parameterDetailService", ParameterDetailService.class);
		logService = appContext.getBean("logService", LogService.class);
		oscarJobService = appContext.getBean("oscarJobService", OscarJobService.class);
		divisionService = appContext.getBean("divisionService", DivisionService.class);
		oscarJobOracleService = appContext.getBean("oscarJobOracleService", OscarJobOracleService.class);
		
//		logService = ApplicationContextProvider.getApplicationContext().getBean("logService", LogService.class);
//		parameterDetailService = ApplicationContextProvider.getApplicationContext().getBean("parameterDetailService", ParameterDetailService.class);
//		oscarJobService = ApplicationContextProvider.getApplicationContext().getBean("oscarJobService", OscarJobService.class);
//		divisionService = ApplicationContextProvider.getApplicationContext().getBean("divisionService", DivisionService.class);
//		oscarJobOracleService = ApplicationContextProvider.getApplicationContext().getBean("oscarJobOracleService", OscarJobOracleService.class);
	}
	
	@SuppressWarnings("unused")
	private void convertCsvToJson(String fileName, String localFolder) {
		File input = new File(localFolder + fileName);
        File output = new File(localFolder + fileName +".json");
        CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build(); 
        CsvMapper csvMapper = new CsvMapper();
        
        try {
			// Read data from CSV file
			List<Object> readAll = csvMapper.readerFor(Map.class).with(csvSchema).readValues(input).readAll();
 
			ObjectMapper mapper = new ObjectMapper();
 
			// Write JSON formated data to output.json file
			mapper.writerWithDefaultPrettyPrinter().writeValue(output, readAll);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private boolean checkRetrievedFiles(String fileName, String localFolder) {
		boolean exist = false;
		File check = null;
		check = new File(localFolder + fileName);
		if (check.exists()) {
			exist = true; 
		}else {
			exist = false;
			log.error("File " + localFolder + fileName + " Not exist");
		}
		
		return exist;
	}

	public void addLogDetail(LogHeader logH, Wrapper seq, String type, String location, String msg) {
		if (seq == null) {
			seq = new Wrapper(new Long(1));
		} else {
			seq.ref = ((Long) seq.ref).longValue() + 1;
		}

		LogDetail logD = new LogDetail(logH, ((Long) seq.ref).longValue(), type, location, msg);
		logH.getLogDetailList().add(logD);
	}

	@SuppressWarnings("unused")
	private void checkExistingFiles(Map<String, String> retrievedFiles, String fileName,
			ParameterDetail inboundFolder) {
		File check = null;
		String csvFileName = null;

		csvFileName = inboundFolder.getNameIn() + ((inboundFolder.getNameIn().endsWith(CommonConstants.FILE_SEPARATOR))
				? fileName + DateUtil.dateToStringDDMMYYYY(new Date(), true)
				: CommonConstants.FILE_SEPARATOR + fileName + DateUtil.dateToStringDDMMYYYY(new Date(), true));
		csvFileName = csvFileName + CommonConstants.CSV_EXT;
		check = new File(csvFileName);
		if (check.exists()) {
			retrievedFiles.put(csvFileName, csvFileName);
		}
	}

	public OscarJobService getOscarJobService() {
		return oscarJobService;
	}

	public void setOscarJobService(OscarJobService oscarJobService) {
		this.oscarJobService = oscarJobService;
	}

	public ApplicationContext getAppContext() {
		return appContext;
	}

	public void setAppContext(ApplicationContext appContext) {
		this.appContext = appContext;
	}

	public LogService getLogService() {
		return logService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public OscarJobOracleService getOscarJobOracleService() {
		return oscarJobOracleService;
	}

	public void setOscarJobOracleService(OscarJobOracleService oscarJobOracleService) {
		this.oscarJobOracleService = oscarJobOracleService;
	}

	public DivisionService getDivisionService() {
		return divisionService;
	}

	public void setDivisionService(DivisionService divisionService) {
		this.divisionService = divisionService;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}
	
	
}
