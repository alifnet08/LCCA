/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.user.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.user.model.Branch;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;

/**
 *
 * @author hendra
 */
public interface UserDao extends GenericDAO<User, Long>, RetrieverDataPage<User> {

	public List<Division> getAllDivision();

	public User getUserByNik(String nik);
	
	public User getUserByUserLogin(String userLogin); 

	List<User> getAllUser() throws Exception;
	
	public String getDivisionNameByDivisionId(Long divisionId);
	
	public List<Branch> getBranchByDivisionId(Long divisionId);
	
	public void updateDivisionId();
	
	public void updateEnableFlagToN();
	
	public void updateEnableFlagToY();
	
	public List<String> getAllDirectorate();
	
	public List<String> getDivisionByDirectorate(String directorate);
	
	public String getDivisionNameByUserId(Long userId);
	
	public String getBranchNameByBranchCode(String branchCode);
	
	public String getDirectorateByDivisionId(Long divisionId);
	
	public String getRegionByBranchCode(String branchCode);

	public Branch getBranchByBranchCode(String branchCode);

	public Division findUsedDivisionByDivisionId(Long divId);
	
	public List<Branch> getAllBranch();

	public String getSubBranchNameByBranchCode(String branchCode);

}
