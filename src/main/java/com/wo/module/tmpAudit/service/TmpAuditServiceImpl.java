package com.wo.module.tmpAudit.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpAudit.dao.TmpAuditCheckPointDao;
import com.wo.module.tmpAudit.dao.TmpAuditDao;
import com.wo.module.tmpAudit.model.TmpAudit;
import com.wo.module.tmpAudit.model.TmpAuditDocument;
import com.wo.module.tmpAudit.model.TmpAuditPicCompliance;
import com.wo.module.tmpAudit.model.TmpAuditPicFollowup;
import com.wo.module.tmpAudit.vo.AuditConfirmationVO;
import com.wo.module.tmpAudit.vo.TmpAuditVO;
import com.wo.module.tmpAuditApproval.vo.TmpAuditApprovalVO;

@Transactional
@Service("tmpAuditService")
public class TmpAuditServiceImpl implements TmpAuditService {

	@Autowired
	@Qualifier("tmpAuditDao")
	private TmpAuditDao tmpAuditDao;
	
	@Autowired
	@Qualifier("tmpAuditCheckPointDao")
	private TmpAuditCheckPointDao tmpAuditCheckPointDao;

	@Override
	public void save(TmpAudit entity) {
		tmpAuditDao.save(entity);
	}

	@Override
	public void update(TmpAudit entity) {
		tmpAuditDao.update(entity);
	}
	
	@Override
	public void merge(TmpAudit entity) {
		tmpAuditDao.merge(entity);
	}

	@Override
	public void delete(TmpAudit entity) {
		tmpAuditDao.delete(entity);
	}

	@Override
	public TmpAudit findById(Long id) {
		return tmpAuditDao.getById(id);
	}

	@Override
	public Boolean hasReachedMaximumReschedule(Long socializationPicFollowupId) throws Exception {
		return tmpAuditDao.hasReachedMaximumReschedule(socializationPicFollowupId);
	}
	
	@Override
	public Boolean hasDuplicateBankCommitment(Long auditId,String auditFinding,String bankResponse,String bankCommitment) {
		return tmpAuditCheckPointDao.hasDuplicateBankCommitment(auditId, auditFinding, bankResponse, bankCommitment);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public List<TmpAuditVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		return tmpAuditDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return tmpAuditDao.searchCountData(searchCriteria);
	}

	public TmpAuditDao getTmpAuditDao() {
		return tmpAuditDao;
	}

	public void setTmpAuditDao(TmpAuditDao tmpAuditDao) {
		this.tmpAuditDao = tmpAuditDao;
	}
	
	

	public TmpAuditCheckPointDao getTmpAuditCheckPointDao() {
		return tmpAuditCheckPointDao;
	}

	public void setTmpAuditCheckPointDao(TmpAuditCheckPointDao tmpAuditCheckPointDao) {
		this.tmpAuditCheckPointDao = tmpAuditCheckPointDao;
	}

	@Override
	public void updateDataAlreadyExist(TmpAudit tmpAudit, TmpAudit tmpAuditDb, String userLogin) {
		tmpAuditDb.setAuditTopicEn(tmpAudit.getAuditTopicEn());
		tmpAuditDb.setAuditTopicIn(tmpAudit.getAuditTopicIn());
		tmpAuditDb.setAuditCategory(tmpAudit.getAuditCategory());
		tmpAuditDb.setAuditDateFrom(tmpAudit.getAuditDateFrom());
		tmpAuditDb.setAuditDateTo(tmpAudit.getAuditDateTo());
		tmpAuditDb.setCounterType(tmpAudit.getCounterType());
		tmpAuditDb.setFollowUp(tmpAudit.getFollowUp());
		tmpAuditDb.setReminderStatus(tmpAudit.getReminderStatus());
		tmpAuditDb.setStatus(tmpAudit.getStatus());
		tmpAuditDb.setAuditObject(tmpAudit.getAuditObject());
		tmpAuditDb.setScope(tmpAudit.getScope());
		
		// Audit doc
		List<TmpAuditDocument> childListDocumentReal = tmpAuditDb.getTmpAuditDocuments();
		List<TmpAuditDocument> childListDocumentNew = tmpAudit.getTmpAuditDocuments();
		if (childListDocumentNew != null) {
			TmpAuditDocument tmpAuditDocumentTmp = null;
			TmpAuditDocument tmpAuditDocumentDb = null;
			TmpAuditDocument tmpAuditDocumentNew = null;
			boolean exist = false;

			// untuk insert data baru dan update data lama
			for (int x = 0; x < childListDocumentNew.size(); x++) {
				tmpAuditDocumentNew = (TmpAuditDocument) childListDocumentNew.get(x);

				exist = false;
				for (int i = 0; i < childListDocumentReal.size(); i++) {
					tmpAuditDocumentDb = (TmpAuditDocument) childListDocumentReal.get(i);
					if (tmpAuditDocumentDb.getAuditDocumentId() != null
							&& tmpAuditDocumentNew.getAuditDocumentId() != null
							&& tmpAuditDocumentDb.getAuditDocumentId()
									.equals(tmpAuditDocumentNew.getAuditDocumentId())) {
						exist = true;
						break;
					}
				}

				if (exist) { // update
//					tmpAuditDocumentDb.setDocumentType(tmpAuditDocumentNew.getDocumentType());
					tmpAuditDocumentDb.setAttachmentFile(tmpAuditDocumentNew.getAttachmentFile());
					tmpAuditDocumentDb.setFileId(tmpAuditDocumentNew.getFileId());
					tmpAuditDocumentDb.setFileSize(tmpAuditDocumentNew.getFileSize());
					
					tmpAuditDocumentDb.setLastUpdateBy(userLogin);
					tmpAuditDocumentDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
					tmpAuditDocumentDb.setDelId(new Long(0));
					tmpAuditDocumentDb.setEnabledFlag(Constants.CONSTANT_YES);
				} else { // insert
					tmpAuditDocumentTmp = new TmpAuditDocument();
					tmpAuditDocumentTmp.setTmpAudit(tmpAuditDb);
//					tmpAuditDocumentTmp.setDocumentType(tmpAuditDocumentNew.getDocumentType());
					tmpAuditDocumentTmp.setAttachmentFile(tmpAuditDocumentNew.getAttachmentFile());
					tmpAuditDocumentTmp.setFileId(tmpAuditDocumentNew.getFileId());
					tmpAuditDocumentTmp.setFileSize(tmpAuditDocumentNew.getFileSize());
                    
					tmpAuditDocumentTmp.setCreatedBy(userLogin);
					tmpAuditDocumentTmp.setCreationDate(new Timestamp(new Date().getTime()));
					tmpAuditDocumentTmp.setDelId(new Long(0));
					tmpAuditDocumentTmp.setEnabledFlag(Constants.CONSTANT_YES);

					childListDocumentReal.add(tmpAuditDocumentTmp);
				}
			}

			for (int i = 0; i < childListDocumentReal.size(); i++) {
				tmpAuditDocumentDb = (TmpAuditDocument) childListDocumentReal.get(i);

				for (int x = 0; x < childListDocumentNew.size(); x++) {
					tmpAuditDocumentNew = (TmpAuditDocument) childListDocumentNew.get(x);
					exist = false;
					if ((tmpAuditDocumentDb.getAuditDocumentId() != null
							&& tmpAuditDocumentNew.getAuditDocumentId() != null
							&& tmpAuditDocumentDb.getAuditDocumentId()
									.equals(tmpAuditDocumentNew.getAuditDocumentId()))
							|| (tmpAuditDocumentDb.getAuditDocumentId() == null
									&& tmpAuditDocumentNew.getAuditDocumentId() == null)) {
						exist = true;
						break;
					}
				}

				if (!exist) { // delete
					i--;
					childListDocumentReal.remove(tmpAuditDocumentDb);
				}
			}
		}
		// Audit doc

		// PIC Complience List
		List<TmpAuditPicCompliance> childListComplianceReal = tmpAuditDb.getTmpAuditPicCompliances();
		List<TmpAuditPicCompliance> childListComplianceNew = tmpAudit.getTmpAuditPicCompliances();
		if (childListComplianceNew != null) {
			TmpAuditPicCompliance socializationPICComplianceTmp = null;
			TmpAuditPicCompliance socializationPICComplianceTmpDb = null;
			TmpAuditPicCompliance socializationPICComplianceTmpNew = null;
			boolean exist = false;

			// untuk insert data baru dan update data lama
			for (int x = 0; x < childListComplianceNew.size(); x++) {
				socializationPICComplianceTmpNew = (TmpAuditPicCompliance) childListComplianceNew.get(x);

				exist = false;
				for (int i = 0; i < childListComplianceReal.size(); i++) {
					socializationPICComplianceTmpDb = (TmpAuditPicCompliance) childListComplianceReal.get(i);
					if (socializationPICComplianceTmpDb.getAuditPicComplianceId() != null
							&& socializationPICComplianceTmpNew.getAuditPicComplianceId() != null
							&& socializationPICComplianceTmpDb.getAuditPicComplianceId()
									.equals(socializationPICComplianceTmpNew.getAuditPicComplianceId())) {
						exist = true;
						break;
					}
				}

				if (exist) { // update
					socializationPICComplianceTmpDb.setUser(socializationPICComplianceTmpNew.getUser());

					socializationPICComplianceTmpDb.setLastUpdateBy(userLogin);
					socializationPICComplianceTmpDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
					socializationPICComplianceTmpDb.setDelId(new Long(0));
					socializationPICComplianceTmpDb.setEnabledFlag(Constants.CONSTANT_YES);
				} else { // insert
					socializationPICComplianceTmp = new TmpAuditPicCompliance();
					socializationPICComplianceTmp.setUser(socializationPICComplianceTmpNew.getUser());
					socializationPICComplianceTmp.setTmpAudit(tmpAuditDb);
					socializationPICComplianceTmp.setCreatedBy(userLogin);
					socializationPICComplianceTmp.setCreationDate(new Timestamp(new Date().getTime()));
					socializationPICComplianceTmp.setDelId(new Long(0));
					socializationPICComplianceTmp.setEnabledFlag(Constants.CONSTANT_YES);

					childListComplianceReal.add(socializationPICComplianceTmp);
				}
			}

			for (int i = 0; i < childListComplianceReal.size(); i++) {
				socializationPICComplianceTmpDb = (TmpAuditPicCompliance) childListComplianceReal.get(i);

				for (int x = 0; x < childListComplianceNew.size(); x++) {
					socializationPICComplianceTmpNew = (TmpAuditPicCompliance) childListComplianceNew.get(x);
					exist = false;
					if ((socializationPICComplianceTmpDb.getAuditPicComplianceId() != null
							&& socializationPICComplianceTmpNew.getAuditPicComplianceId() != null
							&& socializationPICComplianceTmpDb.getAuditPicComplianceId()
									.equals(socializationPICComplianceTmpNew.getAuditPicComplianceId()))
							|| (socializationPICComplianceTmpDb.getAuditPicComplianceId() == null
									&& socializationPICComplianceTmpNew.getAuditPicComplianceId() == null)) {
						exist = true;
						break;
					}
				}

				if (!exist) { // delete
					i--;
					childListComplianceReal.remove(socializationPICComplianceTmpDb);
				}
			}
		}
		// PIC Complience List

		// PIC Followup List
		/*List<TmpAuditPicFollowup> childListFollowupReal = tmpAuditDb.getTmpAuditPicFollowups();
		List<TmpAuditPicFollowup> childListFollowupNew = tmpAudit.getTmpAuditPicFollowups();
		if (childListFollowupNew != null) {
			TmpAuditPicFollowup socializationPICFollowupTmp = null;
			TmpAuditPicFollowup socializationPICFollowupTmpDb = null;
			TmpAuditPicFollowup socializationPICFollowupTmpNew = null;
			boolean exist = false;

			// untuk insert data baru dan update data lama
			for (int x = 0; x < childListFollowupNew.size(); x++) {
				socializationPICFollowupTmpNew = (TmpAuditPicFollowup) childListFollowupNew.get(x);

				exist = false;
				for (int i = 0; i < childListFollowupReal.size(); i++) {
					socializationPICFollowupTmpDb = (TmpAuditPicFollowup) childListFollowupReal.get(i);
					if (socializationPICFollowupTmpDb.getAuditPicFollowupId() != null
							&& socializationPICFollowupTmpNew.getAuditPicFollowupId() != null
							&& socializationPICFollowupTmpDb.getAuditPicFollowupId()
									.equals(socializationPICFollowupTmpNew.getAuditPicFollowupId())) {
						exist = true;
						break;
					}
				}

				if (exist) { // update
					socializationPICFollowupTmpDb.setUser1(socializationPICFollowupTmpNew.getUser1());
					socializationPICFollowupTmpDb.setUser2(socializationPICFollowupTmpNew.getUser2());
					socializationPICFollowupTmpDb.setUser3(socializationPICFollowupTmpNew.getUser3());
					socializationPICFollowupTmpDb.setTargetDate(socializationPICFollowupTmpNew.getTargetDate());
					socializationPICFollowupTmpDb.setNotes(socializationPICFollowupTmpNew.getNotes());
					socializationPICFollowupTmpDb.setDivisionId(socializationPICFollowupTmpNew.getDivisionId());

					socializationPICFollowupTmpDb.setLastUpdateBy(userLogin);
					socializationPICFollowupTmpDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
					socializationPICFollowupTmpDb.setDelId(new Long(0));
					socializationPICFollowupTmpDb.setEnabledFlag(Constants.CONSTANT_YES);
				} else { // insert
					socializationPICFollowupTmp = new TmpAuditPicFollowup();
					socializationPICFollowupTmp.setUser1(socializationPICFollowupTmpNew.getUser1());
					socializationPICFollowupTmp.setUser2(socializationPICFollowupTmpNew.getUser2());
					socializationPICFollowupTmp.setUser3(socializationPICFollowupTmpNew.getUser3());
					socializationPICFollowupTmp.setTargetDate(socializationPICFollowupTmpNew.getTargetDate());
					socializationPICFollowupTmp.setNotes(socializationPICFollowupTmpNew.getNotes());
					//socializationPICFollowupTmp.setTmpAudit(tmpAuditDb);
					socializationPICFollowupTmp.setDivisionId(socializationPICFollowupTmpNew.getDivisionId());

					socializationPICFollowupTmp.setCreatedBy(userLogin);
					socializationPICFollowupTmp.setCreationDate(new Timestamp(new Date().getTime()));
					socializationPICFollowupTmp.setDelId(new Long(0));
					socializationPICFollowupTmp.setEnabledFlag(Constants.CONSTANT_YES);

					childListFollowupReal.add(socializationPICFollowupTmp);
				}
			}

			for (int i = 0; i < childListFollowupReal.size(); i++) {
				socializationPICFollowupTmpDb = (TmpAuditPicFollowup) childListFollowupReal.get(i);

				for (int x = 0; x < childListFollowupNew.size(); x++) {
					socializationPICFollowupTmpNew = (TmpAuditPicFollowup) childListFollowupNew.get(x);
					exist = false;
					if ((socializationPICFollowupTmpDb.getAuditPicFollowupId() != null
							&& socializationPICFollowupTmpNew.getAuditPicFollowupId() != null
							&& socializationPICFollowupTmpDb.getAuditPicFollowupId()
									.equals(socializationPICFollowupTmpNew.getAuditPicFollowupId()))
							|| (socializationPICFollowupTmpDb.getAuditPicFollowupId() == null
									&& socializationPICFollowupTmpNew.getAuditPicFollowupId() == null)) {
						exist = true;
						break;
					}
				}

				if (!exist) { // delete
					i--;
					childListFollowupReal.remove(socializationPICFollowupTmpDb);
				}
			}
		}*/
		// PIC Followup List

		tmpAuditDb.setLastUpdateBy(userLogin);
		tmpAuditDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
		tmpAuditDb.setDelId(new Long(0));
		tmpAuditDb.setEnabledFlag(Constants.CONSTANT_YES);

		tmpAuditDao.update(tmpAuditDb);

	}
	
	@Override
	public List<AuditConfirmationVO> getDataConfirmStatusByAuditId(Long auditId){
    	return tmpAuditDao.getDataConfirmStatusByAuditId(auditId);
    }
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<TmpAuditVO> searchDataXLS(List<? extends SearchObject> searchCriteria) {
		return tmpAuditDao.searchDataXLS(searchCriteria);
	}

	@Override
	public List<TmpAuditApprovalVO> getDataApprovalByAuditId(Long auditId) {
		return tmpAuditDao.getDataApprovalByAuditId(auditId);
	}
	
	@Override
	public Integer getTmpAuditByIdAndNameIn(Long id, String findingNameIn, Long mstAuditId) throws Exception {
		return tmpAuditDao.getTmpAuditByIdAndNameIn(id, findingNameIn, mstAuditId);
	}

}
