/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcFineApproval.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowup;


@Repository("trcFinePicFollowupDao")
public class TrcFinePicFollowupDaoImpl extends GenericDAOHibernate<TrcFinePicFollowup, Long> implements TrcFinePicFollowupDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TrcFinePicFollowupDaoImpl.class);
	
	
	
	

}
