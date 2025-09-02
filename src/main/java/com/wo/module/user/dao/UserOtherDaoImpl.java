/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.user.dao;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.wo.module.common.dao.GenericDAOHibernateOther;
import com.wo.module.user.model.User;

public class UserOtherDaoImpl extends GenericDAOHibernateOther<User, Long> implements UserOtherDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(UserOtherDaoImpl.class);

	private static UserOtherDao thisCodeDAO;

	public static synchronized UserOtherDao getInstance() {
		if (thisCodeDAO == null) {
			thisCodeDAO = new UserOtherDaoImpl();
		}
		return thisCodeDAO;
	}

	private UserOtherDaoImpl() {
	}

	@SuppressWarnings("unchecked")
	public User getUserByNik(String nik) {
//		getSession().beginTransaction();
		try {
			if (nik != null) {
				String hql = "FROM User where nik = :nik";
				Query result = getSession().createQuery(hql);
				result.setParameter("nik", nik);
				
				return (User) result.getResultList().stream().findFirst().orElse(null);
//				Object res = result.getSingleResult();
//				if(res != null)
//					return (User) result.getSingleResult();
//				else
//					return null;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
//		finally {
//			getSession().close();
//		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUser() throws Exception {
//		getSession().beginTransaction();
//		try {
			String hql = "FROM User";
			Query result = getSession().createQuery(hql);

			return result.getResultList();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		} finally {
//			getSession().close();
//		}
	}
}
