/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.user.service;

import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.user.model.Branch;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;

public interface UserService extends RetrieverDataPage<User> {
	public void save(User entity); 
	
	public void update(User entity);
	
	public void delete(User entity);
  
    public User findById(Long id) ;
    
    public List<Division> getAllDivision();
    
    public User getUserByNik(String nik);
    
    public User getUserByUserLogin(String userLogin); 

	List<User> getAllUser() throws Exception;

	List<SelectItem> getAllUserLabelValue() throws Exception;

	List<SelectItem> getAllDivisionLabelValue();
	
	public String getDivisionNameByDivisionId(Long divisionId);
	
	public List<Branch> getBranchByDivisionId(Long divisionId);
	
	public List<String> getAllDirectorate(); 
	
	public List<String> getDivisionByDirectorate(String directorate);
	
	public String getDivisionNameByUserId(Long userId);

	public Division findUsedDivisionByDivisionId(Long parseLong);

	public Map<Long, Division> getUserDivisionAsMap();
	
	public String getBranchNameByBranchCode(String branchCode);
	
	public String getDirectorateByDivisionId(Long divisionId);
	
	public String getRegionByBranchCode(String branchCode);
	
	public Branch getBranchByBranchCode(String branchCode);
	
	public List<Branch> getAllBranch();

	public String getSubBranchNameByBranchCode(String branchCode);
}
