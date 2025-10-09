/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.Branch;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<User> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		return userDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return userDao.searchCountData(searchCriteria);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<User> getAllUser() throws Exception {
		return userDao.getAllUser();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<SelectItem> getAllUserLabelValue() throws Exception {
		List<User> users = userDao.getAllUser();
		List<SelectItem> userSelectItems = new ArrayList<SelectItem>();
		for (User user : users) {
			userSelectItems.add(new SelectItem(user.getUserId(), user.getNik()));
		}
		return userSelectItems;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<SelectItem> getAllDivisionLabelValue() {
		List<Division> divisions = userDao.getAllDivision();
		List<SelectItem> divisionSelectItems = new ArrayList<SelectItem>();
		for (Division div : divisions) {
			divisionSelectItems.add(new SelectItem(div.getDivisionId(), div.getDivisionName()));
		}
		return divisionSelectItems;
	}

	public void save(User entity) {
		userDao.save(entity);
	}

	public void update(User entity) {
		userDao.update(entity);
	}

	public void delete(User entity) {
		userDao.delete(entity);
	}

	public User findById(Long id) {
		return userDao.getById(id);
	}
	
	 public List<Division> getAllDivision() {
		 return userDao.getAllDivision();
	 }

	 public User getUserByNik(String nik) {
		 return userDao.getUserByNik(nik);
	 }
	 
	 public User getUserByUserLogin(String userLogin){
		 return userDao.getUserByUserLogin(userLogin);
	 }
	 
	 public String getDivisionNameByDivisionId(Long divisionId) {
		 return userDao.getDivisionNameByDivisionId(divisionId);
	 }
	 
	 public List<Branch> getBranchByDivisionId(Long divisionId){
		 return userDao.getBranchByDivisionId(divisionId);
	 }
	 
	 public void updateDivisionId(){
		  userDao.updateDivisionId();
	 }
	 
	 public List<String> getDivisionByDirectorate(String directorate){
		 return userDao.getDivisionByDirectorate(directorate);
	 }
	 
	 public List<String> getAllDirectorate() {
		 return userDao.getAllDirectorate();
	 }
	 
	 public String getDivisionNameByUserId(Long userId){
		 return userDao.getDivisionNameByUserId(userId);
	 }

	@Override
	public Division findUsedDivisionByDivisionId(Long divId) {
		return userDao.findUsedDivisionByDivisionId(divId);
	}

	@Override
	public Map<Long, Division> getUserDivisionAsMap() {
		List<Division> divisionList = userDao.getAllDivision();
		return divisionList.stream().collect(Collectors.toMap(Division::getDivisionId, division->division));
	}
	 
	@Override
	public String getBranchNameByBranchCode(String branchCode) {
		return userDao.getBranchNameByBranchCode(branchCode);
	}

	@Override
	public String getDirectorateByDivisionId(Long divisionId) {
		return userDao.getDirectorateByDivisionId(divisionId);
	}	 
	 public Branch getBranchByBranchCode(String branchCode) {
		 return userDao.getBranchByBranchCode(branchCode);
	 }
	 
	 @Override
	public List<Branch> getAllBranch() {
		return userDao.getAllBranch();
	}

	@Override
	public String getRegionByBranchCode(String branchCode) {
		return userDao.getRegionByBranchCode(branchCode);
	}

	@Override
	public String getSubBranchNameByBranchCode(String branchCode) {
		return userDao.getSubBranchNameByBranchCode(branchCode);
	}
	
}
