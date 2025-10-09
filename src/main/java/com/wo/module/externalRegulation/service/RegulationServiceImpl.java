/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.externalRegulation.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.constant.Constants;
import com.wo.module.externalRegulation.dao.RegulationDao;
import com.wo.module.externalRegulation.model.Regulation;
import com.wo.module.externalRegulation.model.RegulationAttachment;
import com.wo.module.externalRegulation.model.RegulationTrackRecord;

@Transactional
@Service("regulationService")
public class RegulationServiceImpl implements RegulationService {
    @Autowired
    @Qualifier("regulationDao")
    private RegulationDao regulationDao;

	public RegulationDao getRegulationDao() {
		return regulationDao;
	}

	public void setRegulationDao(RegulationDao regulationDao) {
		this.regulationDao = regulationDao;
	}
    

	public void save(Regulation regulation) {
		regulationDao.save(regulation);
	}
	
	public void update(Regulation regulation) {
		regulationDao.update(regulation);
	}
	
	public void delete(Regulation regulation) {
		regulationDao.delete(regulation);
	}
  
    public Regulation findById(Long id) {
    	return regulationDao.getById(id);
    }
    
    public Integer getRegulationByDocNoAndDocName(String docNo,String docNameIn,String docNameEn,Long regulationId) throws Exception {
    	return regulationDao.getRegulationByDocNoAndDocName(docNo, docNameIn, docNameEn,regulationId);
    }

	@Override
	public Integer getCheckDataRegulationSocialization(Long regulationId) throws Exception {
		return regulationDao.getCheckDataRegulationSocialization(regulationId);
	}

	@Override
	public Regulation getCheckDataRegulation(Long regulationId, String jenisRegulation, String docNo, String nameIn)
			throws Exception {
		return regulationDao.getCheckDataRegulation(regulationId, jenisRegulation, docNo, nameIn);
	}
        
	@Override
	public void updateDataAlreadyExist(Regulation regulationNew, Regulation regulationDb, String userLogin) throws Exception {
		regulationDb.setJenisKetentuan(regulationNew.getJenisKetentuan());
		regulationDb.setDocumentType(regulationNew.getDocumentType());
		regulationDb.setDocumentCategory(regulationNew.getDocumentCategory());		
		regulationDb.setDocumentNo(regulationNew.getDocumentNo());
		regulationDb.setNameIn(regulationNew.getNameIn());
		regulationDb.setNameEn(regulationNew.getNameEn());
		regulationDb.setPublishedDate(regulationNew.getPublishedDate());
		regulationDb.setExpiredDate(regulationNew.getExpiredDate());
		regulationDb.setEffectiveDate(regulationNew.getEffectiveDate());
		regulationDb.setStatus(regulationNew.getStatus());	
		
		regulationDb.setDocumentTopic(regulationNew.getDocumentTopic());
		regulationDb.setDescriptionIn(regulationNew.getDescriptionIn());
		regulationDb.setReceiver(regulationNew.getReceiver());
		regulationDb.setCounterType(regulationNew.getCounterType());
		regulationDb.setDirectorate(regulationNew.getDirectorate());
		regulationDb.setPuk(regulationNew.getPuk());
		regulationDb.setPic(regulationNew.getPic());
		
//		regulationDb.getRegulationAttachments().addAll(regulationNew.getRegulationAttachments()); 
		List<RegulationAttachment> childListAttchReal = regulationDb.getRegulationAttachments(); 
		List<RegulationAttachment> childListAttchNew = regulationNew.getRegulationAttachments(); 
		if(childListAttchNew != null) {
			RegulationAttachment regulationAttachment = null;
			RegulationAttachment regulationAttachmentDb = null;
			RegulationAttachment regulationAttachmentNew = null;
			boolean exist = false;
			
			// untuk insert data baru dan update data lama 
			for(int x=0; x<childListAttchNew.size(); x++)
			{
				regulationAttachmentNew = (RegulationAttachment) childListAttchNew.get(x);
				
				exist = false;
				for(int i=0; i<childListAttchReal.size(); i++)
				{
					regulationAttachmentDb = (RegulationAttachment) childListAttchReal.get(i);										
					if(regulationAttachmentDb.getRegulationAttachmentId() != null && regulationAttachmentNew.getRegulationAttachmentId() != null
							&& regulationAttachmentDb.getRegulationAttachmentId().equals(regulationAttachmentNew.getRegulationAttachmentId())
					){
						exist = true;
						break;
					}
				}
				
				if (exist)
				{	// update 			
					regulationAttachmentDb.setAttachmentCode(regulationAttachmentNew.getAttachmentCode());
					regulationAttachmentDb.setAttachmentFile(regulationAttachmentNew.getAttachmentFile());
					regulationAttachmentDb.setLastUpdateBy(userLogin);
					regulationAttachmentDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
					regulationAttachmentDb.setDelId(new Long(0));
					regulationAttachmentDb.setEnabledFlag(Constants.CONSTANT_YES);
				}
				else
				{	// insert 
					regulationAttachment = new RegulationAttachment();
					regulationAttachment.setRegulation(regulationDb);				
					regulationAttachment.setAttachmentCode(regulationAttachmentNew.getAttachmentCode());
					regulationAttachment.setAttachmentFile(regulationAttachmentNew.getAttachmentFile());					
					regulationAttachment.setCreatedBy(userLogin);
					regulationAttachment.setCreationDate(new Timestamp(new Date().getTime()));
					regulationAttachment.setDelId(new Long(0));
					regulationAttachment.setEnabledFlag(Constants.CONSTANT_YES);				
					regulationAttachment.setFileId(regulationAttachmentNew.getFileId());
					regulationAttachment.setFileSize(regulationAttachmentNew.getFileSize());
					childListAttchReal.add(regulationAttachment);
				}
			}
							
			for(int i=0; i<childListAttchReal.size(); i++)
			{
				regulationAttachmentDb = (RegulationAttachment) childListAttchReal.get(i);

				for(int x=0; x<childListAttchNew.size(); x++)
				{
					regulationAttachmentNew =(RegulationAttachment) childListAttchNew.get(x);
					exist = false;				
					if((regulationAttachmentDb.getRegulationAttachmentId() != null && regulationAttachmentNew.getRegulationAttachmentId() != null
						&& regulationAttachmentDb.getRegulationAttachmentId().equals(regulationAttachmentNew.getRegulationAttachmentId())) 
						||
						(regulationAttachmentDb.getRegulationAttachmentId() == null && regulationAttachmentNew.getRegulationAttachmentId() == null)
					){
						exist = true;
						break;
					}
				}

				if (!exist)
				{	// delete 
					i--;
					childListAttchReal.remove(regulationAttachmentDb);
				}
			}				
		}		
		
		
	
		//regulationDb.getRegulationTrackRecords().addAll(regulationNew.getRegulationTrackRecords());
		List<RegulationTrackRecord> childListRecordsReal = regulationDb.getRegulationTrackRecords(); 
		List<RegulationTrackRecord> childListRecordsNew = regulationNew.getRegulationTrackRecords(); 
		if(childListRecordsNew != null) {
			RegulationTrackRecord regulationTrackRecord = null;
			RegulationTrackRecord regulationTrackRecordDb = null;
			RegulationTrackRecord regulationTrackRecordNew = null;
			boolean exist = false;
			
			// untuk insert data baru dan update data lama 
			for(int x=0; x<childListRecordsNew.size(); x++)
			{
				regulationTrackRecordNew = (RegulationTrackRecord) childListRecordsNew.get(x);
				
				exist = false;
				for(int i=0; i<childListRecordsReal.size(); i++)
				{
					regulationTrackRecordDb = (RegulationTrackRecord) childListRecordsReal.get(i);										
					if((regulationTrackRecordDb.getRegulationTrackRecordId() != null && regulationTrackRecordNew.getRegulationTrackRecordId() != null
							&& regulationTrackRecordDb.getRegulationTrackRecordId().equals(regulationTrackRecordNew.getRegulationTrackRecordId()))) 
					{
						exist = true;
						break;
					}
				}
				
				if (exist)
				{	// update 			
					regulationTrackRecordDb.setTrackCode(regulationTrackRecordNew.getTrackCode());
					
//					if(regulationTrackRecordNew.getRegulationLinkId() !=null && regulationTrackRecordNew.getRegulationLinkId() > 0) {
						regulationTrackRecordDb.setRegulationLinkId(regulationTrackRecordNew.getRegulationLinkId());		
//					}
					if((regulationTrackRecordNew.getRegulationLinkId() ==null || regulationTrackRecordNew.getRegulationLinkId() <= 0)
							&& regulationTrackRecordNew.getRegulationLinkName() != null && !regulationTrackRecordNew.getRegulationLinkName().isEmpty()) {
						regulationTrackRecordDb.setRegulationLinkName(regulationTrackRecordNew.getRegulationLinkName());
					} else {
						regulationTrackRecordDb.setRegulationLinkName("");
					}
					regulationTrackRecordDb.setTrackNote(regulationTrackRecordNew.getTrackNote());
					regulationTrackRecordDb.setInActiveFlag(regulationTrackRecordNew.getInActiveFlag());
					regulationTrackRecordDb.setLastUpdateBy(userLogin);
					regulationTrackRecordDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
					regulationTrackRecordDb.setDelId(new Long(0));
					regulationTrackRecordDb.setEnabledFlag(Constants.CONSTANT_YES);
				}
				else
				{	// insert 
					regulationTrackRecord = new RegulationTrackRecord();
					regulationTrackRecord.setRegulation(regulationDb);				
					regulationTrackRecord.setTrackCode(regulationTrackRecordNew.getTrackCode());
					
//					if(regulationTrackRecordNew.getRegulationLinkId() !=null && regulationTrackRecordNew.getRegulationLinkId() > 0) {
						regulationTrackRecord.setRegulationLinkId(regulationTrackRecordNew.getRegulationLinkId());		
//					}
					if((regulationTrackRecordNew.getRegulationLinkId() ==null || regulationTrackRecordNew.getRegulationLinkId() <= 0)
							&& regulationTrackRecordNew.getRegulationLinkName() != null && !regulationTrackRecordNew.getRegulationLinkName().isEmpty()) {
						regulationTrackRecord.setRegulationLinkName(regulationTrackRecordNew.getRegulationLinkName());
					} else {
						regulationTrackRecord.setRegulationLinkName("");
					}
					regulationTrackRecord.setTrackNote(regulationTrackRecordNew.getTrackNote());
					regulationTrackRecord.setInActiveFlag(regulationTrackRecordNew.getInActiveFlag());
					regulationTrackRecord.setCreatedBy(userLogin);
					regulationTrackRecord.setCreationDate(new Timestamp(new Date().getTime()));
					regulationTrackRecord.setDelId(new Long(0));
					regulationTrackRecord.setEnabledFlag(Constants.CONSTANT_YES);					
					childListRecordsReal.add(regulationTrackRecord);
				}
			}

			for(int i=0; i<childListRecordsReal.size(); i++)
			{
				regulationTrackRecordDb = (RegulationTrackRecord) childListRecordsReal.get(i);

				for(int x=0; x<childListRecordsNew.size(); x++)
				{
					regulationTrackRecordNew = (RegulationTrackRecord) childListRecordsNew.get(x);
					exist = false;				
					if((regulationTrackRecordDb.getRegulationTrackRecordId() != null && regulationTrackRecordNew.getRegulationTrackRecordId() != null
							&& regulationTrackRecordDb.getRegulationTrackRecordId().equals(regulationTrackRecordNew.getRegulationTrackRecordId()))
							||
							(regulationTrackRecordDb.getRegulationTrackRecordId() == null && regulationTrackRecordNew.getRegulationTrackRecordId() == null))
					{
						exist = true;
						break;
					}
				}

				if (!exist)
				{	// delete 
					i--;
					childListRecordsReal.remove(regulationTrackRecordDb);
				}
			}				
		}		
		
		
		regulationDb.setLastUpdateBy(userLogin);
		regulationDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
		regulationDb.setDelId(new Long(0));
		regulationDb.setEnabledFlag(Constants.CONSTANT_YES);
		
		regulationDao.update(regulationDb);
	}
	
	public Integer getCountHitRegulation(Long regulationId, String accessAction) throws Exception {
		return regulationDao.getCountHitRegulation(regulationId, accessAction);
	}
	
	
}
