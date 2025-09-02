/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcFineApproval.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowup;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowupHistory;


@Repository("trcFinePicFpHistoryDao")
public class TrcFinePicFpHistoryDaoImpl extends GenericDAOHibernate<TrcFinePicFollowupHistory, Long> implements TrcFinePicFpHistoryDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TrcFinePicFpHistoryDaoImpl.class);
	
	
	
	

}
