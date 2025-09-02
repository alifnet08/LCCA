/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.user.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAOOther;
import com.wo.module.user.model.User;


public interface UserOtherDao extends GenericDAOOther<User, Long> {

	User getUserByNik(String nik);
	List<User> getAllUser() throws Exception;
}
