package com.wo.module.user.bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.springframework.util.StringUtils;

import com.sshtools.sftp.SftpFile;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.util.LDAPApi;
import com.wo.module.common.util.SFTPUtil;
import com.wo.module.division.model.Division;
import com.wo.module.division.service.DivisionService;
import com.wo.module.engine.ReadXlsController.Wrapper;
import com.wo.module.engine.service.OscarJobOracleService;
import com.wo.module.engine.service.OscarJobService;
import com.wo.module.log.model.LogDetail;
import com.wo.module.log.model.LogHeader;
import com.wo.module.log.service.LogService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.responsibility.model.Responsibility;
import com.wo.module.responsibility.service.ResponsibilityService;
import com.wo.module.user.constant.UserConstants;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class UserBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(UserBean.class);
	private final String functionId = "Inbound";
	private String searchUsername;
	private String searchName;
	private String searchRole;
	
	private String searchDivision;
	private String searchEmail;
	private String searchPosition;

	private int paging;

	private UserService userService;
	private ResponsibilityService responsibilityService;
	private LogService logService;
	private OscarJobService oscarJobService;
	private OscarJobOracleService oscarJobOracleService;
	private DivisionService divisionService;

	private List<User> userList;

	private DBLazyDataModel<User> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = UserConstants.NAVIGATE_EDIT;

	private List<SelectItem> roleList;

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@PostConstruct
	public void init() {
		super.init();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<User>(userService, paging);
		initResponsibility();
	}

	public void initResponsibility() {
		roleList = new ArrayList<SelectItem>();
		try {
			List<Responsibility> listRepsonsibility = responsibilityService.getAllResponsibility();
			for (int i = 0; i < listRepsonsibility.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Responsibility) listRepsonsibility.get(i)).getName());
				si.setValue(((Responsibility) listRepsonsibility.get(i)).getResponsibilityId());
				roleList.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchUsername != null && !searchUsername.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(UserConstants.SEARCH_BY_NIK, searchUsername));
		}
		
		if (searchName != null && !searchName.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(UserConstants.SEARCH_BY_NAME, searchName));
		}
		
		if (searchRole != null && !searchRole.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(UserConstants.SEARCH_BY_RESPONSIBILITY_ID, searchRole));
		}
		if(searchDivision != null && !searchDivision.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(UserConstants.SEARCH_BY_DIVISION,searchDivision));
		}
		if(searchEmail != null && !searchEmail.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(UserConstants.SEARCH_BY_EMAIL, searchEmail));
		}
		if(searchPosition != null && !searchPosition.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(UserConstants.SEARCH_BY_POSITION, searchPosition));
		}

		tableModel.setSearchCriteria(searchCriteria);
		/*
		 * tableModel.setSearchCriteria( Arrays.asList( new
		 * DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
		 */
	}
	
	public void syncUser(ActionEvent actionEvent){
		ExecutorService emailExecutor = Executors.newCachedThreadPool();

		String userId = facesUtil.retrieveUserLogin();
        // from you sendEmail() method
        emailExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                	syncUserProcess(userId,functionId,oscarJobService,parameterDetailService,logService);
                } catch (Exception e) {
                    logger.error("Process syncUser failed", e);
                }
            }
        });
        
        facesUtil.addFacesMsg(
                FacesMessage.SEVERITY_INFO, 
                null, 
                "Proses Sinkronisasi Sedang Berjalan, Silahkan Cek di Menu Log Monitoring", "");
	}
	
	@SuppressWarnings("unused")
	public void syncUserProcess(String userId,String functionId,OscarJobService oscarJobService,ParameterDetailService parameterDetailService,LogService logService) {
		logger.info("Start Session Inbound");

		try {
			
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
				logService.save(logH);
			} catch (Exception ex) {
				ex.printStackTrace();
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
				//users = readCsvFile("G:\\apps\\HC_NomiREG.txt",logH, seq, location);
				System.out.println("size==" + users.size());
				System.out.println(location + " success");
				}
				
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
				oscarJobService.updateUser(users);
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
			
			logH.setEndDate(DateUtil.currentSqlTimestamp());
			if (error)
				logH.setProcessSts(ParameterDetail.PARAM_DET_LOG_PROCESS_STATUS_E);
			else
				logH.setProcessSts(ParameterDetail.PARAM_DET_LOG_PROCESS_STATUS_S);
			
			logService.update(logH);
			
			if (sftp != null) { sftp.disconnect(); }
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.info("Finish Session Inbound");
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
	private class Wrapper {
		public Object ref;

		public Wrapper(Object ref) {
			this.ref = ref;
		}
	}
	
	private void addLogDetail(LogHeader logH, Wrapper seq, String type, String location, String msg) {
		if (seq == null) {
			seq = new Wrapper(new Long(1));
		} else {
			seq.ref = ((Long) seq.ref).longValue() + 1;
		}

		LogDetail logD = new LogDetail(logH, ((Long) seq.ref).longValue(), type, location, msg);
		logH.getLogDetailList().add(logD);
	}
	
	public void reset(ActionEvent actionEvent) {
		searchUsername = "";
		searchName = "";
		searchRole = "";
		searchDivision = "";
		searchEmail = "";
		searchPosition = "";
		
		search(actionEvent);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public Boolean getIsLogin() {
		if (facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null)
			return true;
		else
			return false;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public DBLazyDataModel<User> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<User> tableModel) {
		this.tableModel = tableModel;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public String getSearchUsername() {
		return searchUsername;
	}

	public void setSearchUsername(String searchUsername) {
		this.searchUsername = searchUsername;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getSearchRole() {
		return searchRole;
	}

	public void setSearchRole(String searchRole) {
		this.searchRole = searchRole;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public List<SelectItem> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<SelectItem> roleList) {
		this.roleList = roleList;
	}

	public ResponsibilityService getResponsibilityService() {
		return responsibilityService;
	}

	public void setResponsibilityService(ResponsibilityService responsibilityService) {
		this.responsibilityService = responsibilityService;
	}

	public LogService getLogService() {
		return logService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public OscarJobService getOscarJobService() {
		return oscarJobService;
	}

	public void setOscarJobService(OscarJobService oscarJobService) {
		this.oscarJobService = oscarJobService;
	}

	public OscarJobOracleService getOscarJobOracleService() {
		return oscarJobOracleService;
	}

	public void setOscarJobOracleService(OscarJobOracleService oscarJobOracleService) {
		this.oscarJobOracleService = oscarJobOracleService;
	}

	public String getFunctionId() {
		return functionId;
	}

	public String getSearchDivision() {
		return searchDivision;
	}

	public void setSearchDivision(String searchDivision) {
		this.searchDivision = searchDivision;
	}

	public String getSearchEmail() {
		return searchEmail;
	}

	public void setSearchEmail(String searchEmail) {
		this.searchEmail = searchEmail;
	}

	public String getSearchPosition() {
		return searchPosition;
	}

	public void setSearchPosition(String searchPosition) {
		this.searchPosition = searchPosition;
	}

	public DivisionService getDivisionService() {
		return divisionService;
	}

	public void setDivisionService(DivisionService divisionService) {
		this.divisionService = divisionService;
	}
	
	

}