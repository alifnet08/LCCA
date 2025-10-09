package com.wo.module.engine.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.util.SFTPUtil;
import com.wo.module.engine.ReadXlsController.Wrapper;
import com.wo.module.engine.dao.OscarJobDao;
import com.wo.module.log.model.LogDetail;
import com.wo.module.log.model.LogHeader;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("oscarJobService")
public class OscarJobServiceImpl implements OscarJobService {

	@Autowired
	@Qualifier("oscarJobDao")
	private OscarJobDao oscarJobDao;
	
	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;
	
//	private UserOtherDao userOtherDao = UserOtherDaoImpl.getInstance();
	
//	private OscarJobDao oscarJobOracleDao = OscarJobOracleDaoImpl.getInstance();
	
//	@Autowired
//	@Qualifier("oscarJobOracleDao")
//	private OscarJobDao oscarJobOracleDao;

//	@Override
//	@Transactional("oracleSession")
//	public List<User> getUserFromOracle() throws Exception {
//		return oscarJobOracleDao.getListUserFromOracle();
//	}
	
	@Override
	public String execInboundCommon(String procedureName, String jsonData) throws Exception {
		return oscarJobDao.execInboundCommon(procedureName, jsonData);
	}

	@Override
	public void connectSftp(String hostname, String username, String password, String outboundPath,
			List<String> encryptedFiles) {
		try {

			if (encryptedFiles != null && encryptedFiles.size() > 0) {
				SFTPUtil sftp = null;
				try {
					sftp = new SFTPUtil(hostname);
					sftp.connectByUsername(username, password);
					sftp.cd(outboundPath);
					for (String file : encryptedFiles) {
						sftp.put(file);

						(new File(file)).delete();
					}
				} catch (Exception ex) {
					throw ex;
				} finally {
					if (sftp != null) {
						sftp.disconnect();
						sftp = null;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void retrieveSftp(String hostname, String username, String password, String inboundPath,
			Map<String, String> generatedFiles) {
		try {
			if (generatedFiles != null && generatedFiles.size() > 0) {
				SFTPUtil sftp = null;
				String fileRemote = null;
				try {
					sftp = new SFTPUtil(hostname);
					sftp.connectByUsername(username, password);
					sftp.cd(inboundPath);
					for (String file : generatedFiles.keySet()) {
						fileRemote = FilenameUtils.getName(file);
						sftp.get(fileRemote, file);
						//sftp.delete(fileRemote);
					}
				} catch (Exception ex) {
					throw ex;
				} finally {
					if (sftp != null) {
						sftp.disconnect();
						sftp = null;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public OscarJobDao getOscarJobDao() {
		return oscarJobDao;
	}

	public void setOscarJobDao(OscarJobDao oscarJobDao) {
		this.oscarJobDao = oscarJobDao;
	}

	@Override
	public String getSystemProperty(String propertyCode) throws Exception {
		return oscarJobDao.getSystemProperty(propertyCode);
	}
	
	
	@Override
	public void updateUser(List<User> users,LogHeader logH, com.wo.module.engine.ReadXlsController.Wrapper seq, String location) throws Exception {
		int newlyAdd = 0;
		int updatedUser = 0;
		int noactivatedUser = 0;
//			userOtherDao.beginTransaction();
		if (users != null && !users.isEmpty()) {
			List<User> deletedUser = new ArrayList<User>();
			/*List<User> allOscarUser = userDao.getAllUser();
			for (User user : allOscarUser) {
				boolean exists = users.stream().anyMatch(u -> user.getNik().equals(u.getNik()));
				if (!exists) {
					deletedUser.add(user);
					noactivatedUser++;
				}
			}*/

			for (User user : users) {
				try{
				User mUser = userDao.getUserByNik(user.getNik());

				if (mUser != null) { // update oscar user
					mUser.setName(user.getName());
					if(!StringUtils.isEmpty(user.getEmail())){
						mUser.setEmail(user.getEmail());
					}
					mUser.setJobId(user.getJobId());
					mUser.setJobName(user.getJobName());
					mUser.setPositionId(user.getPositionId());
					mUser.setPositionName(user.getPositionName());
					mUser.setPukNik(user.getPukNik());
					mUser.setBranchCode(user.getBranchCode());
					mUser.setDivisionId(user.getDivisionId());
					mUser.setDivisionName(user.getDivisionName());
					mUser.setLastUpdateBy("SYSTEM");
					mUser.setLastUpdateDate(new Timestamp(new Date().getTime()));
					mUser.setStatus(CommonConstants.RECORD_FLAG_YES);
					mUser.setEnabledFlag(CommonConstants.RECORD_FLAG_YES);
					mUser.setBranch(user.getBranch());
					mUser.setDirectorate(user.getDirectorate());
					mUser.setSubBranch(user.getSubBranch());
					mUser.setRegion(user.getRegion());
					userDao.update(mUser);
					userDao.flush();
					updatedUser++;
				} else if (mUser == null) {
					mUser = new User();
					mUser.setNik(user.getNik());
					mUser.setName(user.getName());
					mUser.setEmail(user.getEmail());
					mUser.setJobId(user.getJobId());
					mUser.setJobName(user.getJobName());
					mUser.setPositionId(user.getPositionId());
					mUser.setPositionName(user.getPositionName());
					mUser.setPukNik(user.getPukNik());
					mUser.setBranchCode(user.getBranchCode());
					mUser.setDivisionId(user.getDivisionId());
					mUser.setDivisionName(user.getDivisionName());
					mUser.setStatus(CommonConstants.RECORD_FLAG_YES);
					mUser.setEnabledFlag(CommonConstants.RECORD_FLAG_YES);
					mUser.setDelId(new Long(0));
					mUser.setCreatedBy("SYSTEM");
					mUser.setCreationDate(new Timestamp(new Date().getTime()));
					mUser.setBranch(user.getBranch());
					mUser.setDirectorate(user.getDirectorate());
					mUser.setSubBranch(user.getSubBranch());
					mUser.setRegion(user.getRegion());
					userDao.save(mUser);
					userDao.flush();
					newlyAdd++;
				}
				}catch(Exception e){
					e.printStackTrace();
				
					//LogDetail logD = new LogDetail(logH, ((Long) seq.ref).longValue(), LogDetail.MSG_TYPE_ERROR, location, e.getMessage());
					//logH.getLogDetailList().add(logD);
				}
			}

			/*for (User user : deletedUser) {
				User mUser = userDao.getUserByNik(user.getNik());
				mUser.setEnabledFlag(CommonConstants.RECORD_FLAG_NO);
				mUser.setLastUpdateBy("SYSTEM");
				mUser.setLastUpdateDate(new Timestamp(new Date().getTime()));
				userDao.update(mUser);
			}*/
//				userOtherDao.commit();
			System.out.println("newlyAdd = " + newlyAdd + ", updatedUser = " + updatedUser + ", noactivatedUser = "
					+ noactivatedUser);
		}
	}
	
	@Override
	public void updateUser(List<User> users) throws Exception {
		int newlyAdd = 0;
		int updatedUser = 0;
		int noactivatedUser = 0;
//			userOtherDao.beginTransaction();
		if (users != null && !users.isEmpty()) {
			List<User> deletedUser = new ArrayList<User>();
			/*List<User> allOscarUser = userDao.getAllUser();
			for (User user : allOscarUser) {
				boolean exists = users.stream().anyMatch(u -> user.getNik().equals(u.getNik()));
				if (!exists) {
					deletedUser.add(user);
					noactivatedUser++;
				}
			}*/

			for (User user : users) {
				try{
				User mUser = userDao.getUserByNik(user.getNik());

				if (mUser != null) { // update oscar user
					mUser.setName(user.getName());
					mUser.setEmail(user.getEmail());
					mUser.setJobId(user.getJobId());
					mUser.setJobName(user.getJobName());
					mUser.setPositionId(user.getPositionId());
					mUser.setPositionName(user.getPositionName());
					mUser.setPukNik(user.getPukNik());
					mUser.setBranchCode(user.getBranchCode());
					mUser.setDivisionId(user.getDivisionId());
					mUser.setDivisionName(user.getDivisionName());
					mUser.setLastUpdateBy("SYSTEM");
					mUser.setLastUpdateDate(new Timestamp(new Date().getTime()));
					mUser.setStatus(CommonConstants.RECORD_FLAG_YES);
					mUser.setEnabledFlag(CommonConstants.RECORD_FLAG_YES);
					mUser.setBranch(user.getBranch());
					mUser.setDirectorate(user.getDirectorate());
					mUser.setSubBranch(user.getSubBranch());
					mUser.setRegion(user.getRegion());
					userDao.update(mUser);
					updatedUser++;
				} else if (mUser == null) {
					mUser = new User();
					mUser.setNik(user.getNik());
					mUser.setName(user.getName());
					mUser.setEmail(user.getEmail());
					mUser.setJobId(user.getJobId());
					mUser.setJobName(user.getJobName());
					mUser.setPositionId(user.getPositionId());
					mUser.setPositionName(user.getPositionName());
					mUser.setPukNik(user.getPukNik());
					mUser.setBranchCode(user.getBranchCode());
					mUser.setDivisionId(user.getDivisionId());
					mUser.setDivisionName(user.getDivisionName());
					mUser.setStatus(CommonConstants.RECORD_FLAG_YES);
					mUser.setEnabledFlag(CommonConstants.RECORD_FLAG_YES);
					mUser.setDelId(new Long(0));
					mUser.setCreatedBy("SYSTEM");
					mUser.setCreationDate(new Timestamp(new Date().getTime()));
					mUser.setBranch(user.getBranch());
					mUser.setDirectorate(user.getDirectorate());
					mUser.setSubBranch(user.getSubBranch());
					mUser.setRegion(user.getRegion());
					userDao.save(mUser);
					newlyAdd++;
				}
				}catch(Exception e){
					e.printStackTrace();
				}
			}

			/*for (User user : deletedUser) {
				User mUser = userDao.getUserByNik(user.getNik());
				mUser.setEnabledFlag(CommonConstants.RECORD_FLAG_NO);
				mUser.setLastUpdateBy("SYSTEM");
				mUser.setLastUpdateDate(new Timestamp(new Date().getTime()));
				userDao.update(mUser);
			}*/
//				userOtherDao.commit();
			System.out.println("newlyAdd = " + newlyAdd + ", updatedUser = " + updatedUser + ", noactivatedUser = "
					+ noactivatedUser);
		}
	}
	
	public void updateDivisionId(){
		  userDao.updateDivisionId();
	 }
	
	public void updateEnableFlagToN(){
		userDao.updateEnableFlagToN();
	}
	
	public void updateEnableFlagToY(){
		userDao.updateEnableFlagToY();
	}

//	@Override
//	public List<User> getUserFromOracle() {
//		return null;
//	}

//	public OscarJobDao getOscarJobOracleDao() {
//		return oscarJobOracleDao;
//	}
//
//	public void setOscarJobOracleDao(OscarJobDao oscarJobOracleDao) {
//		this.oscarJobOracleDao = oscarJobOracleDao;
//	}

//	public UserOtherDao getUserOtherDao() {
//		return userOtherDao;
//	}
//
//	public void setUserOtherDao(UserOtherDao userOtherDao) {
//		this.userOtherDao = userOtherDao;
//	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
