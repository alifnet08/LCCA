/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpComplianceReview.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.constant.Constants;
import com.wo.module.tmpComplianceReview.dao.TmpComplianceReviewDao;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReview;
import com.wo.module.tmpComplianceReview.model.TmpComplianceReviewPicCompliance;

@Transactional
@Service("tmpComplianceReviewService")
public class TmpComplianceReviewServiceImpl implements TmpComplianceReviewService {
    @Autowired
    @Qualifier("tmpComplianceReviewDao")
    private TmpComplianceReviewDao tmpComplianceReviewDao;

	public TmpComplianceReviewDao getTmpComplianceReviewDao() {
		return tmpComplianceReviewDao;
	}

	public void setTmpComplianceReviewDao(TmpComplianceReviewDao tmpComplianceReviewDao) {
		this.tmpComplianceReviewDao = tmpComplianceReviewDao;
	}

	@Override
	public void save(TmpComplianceReview entity) {
		tmpComplianceReviewDao.save(entity);
	}

	@Override
	public void update(TmpComplianceReview entity) {
		tmpComplianceReviewDao.update(entity);
	}

	@Override
	public void delete(TmpComplianceReview entity) {
		tmpComplianceReviewDao.delete(entity);
	}

	@Override
	public TmpComplianceReview findById(Long id) {
		return tmpComplianceReviewDao.getById(id);
	}

	@Override
	public void updateDataAlreadyExist(TmpComplianceReview tmpComplianceReviewNew,
			TmpComplianceReview tmpComplianceReviewDb, String userLogin) throws Exception {
		tmpComplianceReviewDb.setDocumentNo(tmpComplianceReviewNew.getDocumentNo());
		tmpComplianceReviewDb.setDocumentDate(tmpComplianceReviewNew.getDocumentDate());
		tmpComplianceReviewDb.setReviewCategory(tmpComplianceReviewNew.getReviewCategory());
		tmpComplianceReviewDb.setReviewedBranch(tmpComplianceReviewNew.getReviewedBranch());
		tmpComplianceReviewDb.setFollowUpPoints(tmpComplianceReviewNew.getFollowUpPoints());
		tmpComplianceReviewDb.setPerihalIn(tmpComplianceReviewNew.getPerihalIn());
		tmpComplianceReviewDb.setPerihalEn(tmpComplianceReviewNew.getPerihalEn());
		tmpComplianceReviewDb.setCounterType(tmpComplianceReviewNew.getCounterType());
		tmpComplianceReviewDb.setFollowUp(tmpComplianceReviewNew.getFollowUp());
		tmpComplianceReviewDb.setNotes(tmpComplianceReviewNew.getNotes());
		tmpComplianceReviewDb.setReminderStatus(tmpComplianceReviewNew.getReminderStatus());
		tmpComplianceReviewDb.setStatus(tmpComplianceReviewNew.getStatus());
		tmpComplianceReviewDb.setTmpComplianceReviewDocuments(tmpComplianceReviewNew.getTmpComplianceReviewDocuments());
		
		//PIC Complience List
		List<TmpComplianceReviewPicCompliance> childListComplianceReal = tmpComplianceReviewDb.getTmpComplianceReviewPicCompliances(); 
		List<TmpComplianceReviewPicCompliance> childListComplianceNew = tmpComplianceReviewNew.getTmpComplianceReviewPicCompliances(); 
		if (childListComplianceNew != null) {
			TmpComplianceReviewPicCompliance tmpComplianceReviewPicCompliance = null;
			TmpComplianceReviewPicCompliance tmpComplianceReviewPicComplianceDb = null;
			TmpComplianceReviewPicCompliance tmpComplianceReviewPicComplianceNew = null;
			boolean exist = false;
			
			// untuk insert data baru dan update data lama 
			for (int x=0; x < childListComplianceNew.size(); x++) {
				tmpComplianceReviewPicComplianceNew = (TmpComplianceReviewPicCompliance) childListComplianceNew.get(x);
				
				exist = false;
				for (int i=0; i<childListComplianceReal.size(); i++) {
					tmpComplianceReviewPicComplianceDb = (TmpComplianceReviewPicCompliance) childListComplianceReal.get(i);										
					if (tmpComplianceReviewPicComplianceDb.getComplianceReviewPicComplianceId() != null && 
						tmpComplianceReviewPicComplianceNew.getComplianceReviewPicComplianceId() != null && 
						tmpComplianceReviewPicComplianceDb.getComplianceReviewPicComplianceId().equals(tmpComplianceReviewPicComplianceNew.getComplianceReviewPicComplianceId())
					) {
						exist = true;
						break;
					}
				}
				
				if (exist) {	// update 			
					tmpComplianceReviewPicComplianceDb.setUser(tmpComplianceReviewPicComplianceNew.getUser());
						
					tmpComplianceReviewPicComplianceDb.setLastUpdateBy(userLogin);
					tmpComplianceReviewPicComplianceDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tmpComplianceReviewPicComplianceDb.setDelId(new Long(0));
					tmpComplianceReviewPicComplianceDb.setEnabledFlag(Constants.CONSTANT_YES);
				} else {	// insert 
					tmpComplianceReviewPicCompliance = new TmpComplianceReviewPicCompliance();
					tmpComplianceReviewPicCompliance.setUser(tmpComplianceReviewPicComplianceNew.getUser());				
					tmpComplianceReviewPicCompliance.setTmpComplianceReview(tmpComplianceReviewDb);			
					tmpComplianceReviewPicCompliance.setCreatedBy(userLogin);
					tmpComplianceReviewPicCompliance.setCreationDate(new Timestamp(new Date().getTime()));
					tmpComplianceReviewPicCompliance.setDelId(new Long(0));
					tmpComplianceReviewPicCompliance.setEnabledFlag(Constants.CONSTANT_YES);
					
					childListComplianceReal.add(tmpComplianceReviewPicCompliance);
				}
			}
							
			for (int i = 0; i < childListComplianceReal.size(); i++) {
				tmpComplianceReviewPicComplianceDb = (TmpComplianceReviewPicCompliance) childListComplianceReal.get(i);

				for(int x = 0; x < childListComplianceNew.size(); x++) {
					tmpComplianceReviewPicComplianceNew =(TmpComplianceReviewPicCompliance) childListComplianceNew.get(x);
					exist = false;				
					if ((tmpComplianceReviewPicComplianceDb.getComplianceReviewPicComplianceId() != null && 
						tmpComplianceReviewPicComplianceNew.getComplianceReviewPicComplianceId() != null && 
						tmpComplianceReviewPicComplianceDb.getComplianceReviewPicComplianceId().equals(tmpComplianceReviewPicComplianceNew.getComplianceReviewPicComplianceId())) ||
						(tmpComplianceReviewPicComplianceDb.getComplianceReviewPicComplianceId() == null && 
						tmpComplianceReviewPicComplianceNew.getComplianceReviewPicComplianceId() == null)
					) {
						exist = true;
						break;
					}
				}

				if (!exist) {	// delete 
					i--;
					childListComplianceReal.remove(tmpComplianceReviewPicComplianceDb);
				}
			}				
		}	
		//PIC Complience List
		
		//PIC Followup List
//		List<TmpComplianceReviewPicFollowup> childListFollowupReal = tmpComplianceReviewDb.getTmpComplianceReviewPicFollowups(); 
//		List<TmpComplianceReviewPicFollowup> childListFollowupNew = tmpComplianceReviewNew.getTmpComplianceReviewPicFollowups(); 
//		if(childListFollowupNew != null) {
//			TmpComplianceReviewPicFollowup tmpComplianceReviewPicFollowup = null;
//			TmpComplianceReviewPicFollowup tmpComplianceReviewPicFollowupDb = null;
//			TmpComplianceReviewPicFollowup tmpComplianceReviewPicFollowupNew = null;
//			boolean exist = false;
//			
//			// untuk insert data baru dan update data lama 
//			for (int x = 0; x < childListFollowupNew.size(); x++) {
//				tmpComplianceReviewPicFollowupNew = (TmpComplianceReviewPicFollowup) childListFollowupNew.get(x);
//				
//				exist = false;
//				for (int i = 0; i < childListFollowupReal.size(); i++) {
//					tmpComplianceReviewPicFollowupDb = (TmpComplianceReviewPicFollowup) childListFollowupReal.get(i);										
//					if (tmpComplianceReviewPicFollowupDb.getComplianceReviewPicFollowupId() != null && 
//						tmpComplianceReviewPicFollowupNew.getComplianceReviewPicFollowupId() != null && 
//						tmpComplianceReviewPicFollowupDb.getComplianceReviewPicFollowupId().equals(tmpComplianceReviewPicFollowupNew.getComplianceReviewPicFollowupId())
//					) {
//						exist = true;
//						break;
//					}
//				}
//				
//				if (exist)
//				{	// update 			
//					tmpComplianceReviewPicFollowupDb.setUser1(tmpComplianceReviewPicFollowupNew.getUser1());
//					tmpComplianceReviewPicFollowupDb.setUser2(tmpComplianceReviewPicFollowupNew.getUser2());
//					tmpComplianceReviewPicFollowupDb.setUser3(tmpComplianceReviewPicFollowupNew.getUser3());
//					tmpComplianceReviewPicFollowupDb.setTargetDate(tmpComplianceReviewPicFollowupNew.getTargetDate());
//					tmpComplianceReviewPicFollowupDb.setNotes(tmpComplianceReviewPicFollowupNew.getNotes());
//					tmpComplianceReviewPicFollowupDb.setDivisionId(tmpComplianceReviewPicFollowupNew.getDivisionId());
//						
//					tmpComplianceReviewPicFollowupDb.setLastUpdateBy(userLogin);
//					tmpComplianceReviewPicFollowupDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
//					tmpComplianceReviewPicFollowupDb.setDelId(new Long(0));
//					tmpComplianceReviewPicFollowupDb.setEnabledFlag(Constants.CONSTANT_YES);
//				}
//				else
//				{	// insert 
//					tmpComplianceReviewPicFollowup = new TmpComplianceReviewPicFollowup();
//					tmpComplianceReviewPicFollowup.setUser1(tmpComplianceReviewPicFollowupNew.getUser1());
//					tmpComplianceReviewPicFollowup.setUser2(tmpComplianceReviewPicFollowupNew.getUser2());
//					tmpComplianceReviewPicFollowup.setUser3(tmpComplianceReviewPicFollowupNew.getUser3());
//					tmpComplianceReviewPicFollowup.setTargetDate(tmpComplianceReviewPicFollowupNew.getTargetDate());
//					tmpComplianceReviewPicFollowup.setNotes(tmpComplianceReviewPicFollowupNew.getNotes());	
//					tmpComplianceReviewPicFollowup.setTmpComplianceReview(tmpComplianceReviewDb);
//					tmpComplianceReviewPicFollowup.setDivisionId(tmpComplianceReviewPicFollowupNew.getDivisionId());
//									
//					tmpComplianceReviewPicFollowup.setCreatedBy(userLogin);
//					tmpComplianceReviewPicFollowup.setCreationDate(new Timestamp(new Date().getTime()));
//					tmpComplianceReviewPicFollowup.setDelId(new Long(0));
//					tmpComplianceReviewPicFollowup.setEnabledFlag(Constants.CONSTANT_YES);		
//					
//					childListFollowupReal.add(tmpComplianceReviewPicFollowup);
//				}
//			}
//							
//			for (int i = 0; i < childListFollowupReal.size(); i++) {
//				tmpComplianceReviewPicFollowupDb = (TmpComplianceReviewPicFollowup) childListFollowupReal.get(i);
//
//				for (int x = 0; x < childListFollowupNew.size(); x++) {
//					tmpComplianceReviewPicFollowupNew =(TmpComplianceReviewPicFollowup) childListFollowupNew.get(x);
//					exist = false;				
//					if ((tmpComplianceReviewPicFollowupDb.getComplianceReviewPicFollowupId() != null && 
//						tmpComplianceReviewPicFollowupDb.getComplianceReviewPicFollowupId() != null && 
//						tmpComplianceReviewPicFollowupDb.getComplianceReviewPicFollowupId().equals(tmpComplianceReviewPicFollowupNew.getComplianceReviewPicFollowupId())) ||
//						(tmpComplianceReviewPicFollowupDb.getComplianceReviewPicFollowupId() == null && tmpComplianceReviewPicFollowupNew.getComplianceReviewPicFollowupId() == null)
//					) {
//						exist = true;
//						break;
//					}
//				}
//
//				if (!exist) {	// delete 
//					i--;
//					childListFollowupReal.remove(tmpComplianceReviewPicFollowupDb);
//				}
//			}				
//		}	
		//PIC Followup List


		tmpComplianceReviewDb.setLastUpdateBy(userLogin);
		tmpComplianceReviewDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
		tmpComplianceReviewDb.setDelId(new Long(0));
		tmpComplianceReviewDb.setEnabledFlag(Constants.CONSTANT_YES);
		
		tmpComplianceReviewDao.update(tmpComplianceReviewDb);
	}	
        
}
