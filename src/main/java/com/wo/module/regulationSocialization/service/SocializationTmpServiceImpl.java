/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationSocialization.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.constant.Constants;
import com.wo.module.regulationSocialization.dao.SocializationTmpDao;
import com.wo.module.regulationSocialization.model.SocializationPICComplianceTmp;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupTmp;
import com.wo.module.regulationSocialization.model.SocializationRegulationTmp;
import com.wo.module.regulationSocialization.model.SocializationTmp;

@Transactional
@Service("socializationTmpService")
public class SocializationTmpServiceImpl implements SocializationTmpService {
    @Autowired
    @Qualifier("socializationTmpDao")
    private SocializationTmpDao socializationTmpDao;

	public SocializationTmpDao getSocializationTmpDao() {
		return socializationTmpDao;
	}

	public void setSocializationTmpDao(SocializationTmpDao socializationTmpDao) {
		this.socializationTmpDao = socializationTmpDao;
	}

	@Override
	public void save(SocializationTmp sozializationTmp) {
		socializationTmpDao.save(sozializationTmp);
	}

	@Override
	public void update(SocializationTmp sozializationTmp) {
		socializationTmpDao.update(sozializationTmp);
	}

	@Override
	public void delete(SocializationTmp sozializationTmp) {
		socializationTmpDao.delete(sozializationTmp);
	}

	@Override
	public SocializationTmp findById(Long id) {
		return socializationTmpDao.getById(id);
	}

	@Override
	public void updateDataAlreadyExist(SocializationTmp socializationNew, SocializationTmp socializationDb, String userLogin) throws Exception {
		
		socializationDb.setCounterType(socializationNew.getCounterType());
		socializationDb.setFollowUp(socializationNew.getFollowUp());
		socializationDb.setJenisKetentuan(socializationNew.getJenisKetentuan());
		socializationDb.setNotes(socializationNew.getNotes());
		socializationDb.setReminderStatus(socializationNew.getReminderStatus());
		socializationDb.setStatus(socializationNew.getStatus());
		
		//RegulationList
		List<SocializationRegulationTmp> childListRegulationReal = socializationDb.getSocializationRegulationTmps(); 
		List<SocializationRegulationTmp> childListRegulationNew = socializationNew.getSocializationRegulationTmps(); 
		if(childListRegulationNew != null) {
			SocializationRegulationTmp socializationRegulationTmp = null;
			SocializationRegulationTmp socializationRegulationTmpDb = null;
			SocializationRegulationTmp socializationRegulationTmpNew = null;
			boolean exist = false;
			
			// untuk insert data baru dan update data lama 
			for(int x=0; x<childListRegulationNew.size(); x++)
			{
				socializationRegulationTmpNew = (SocializationRegulationTmp) childListRegulationNew.get(x);
				
				exist = false;
				for(int i=0; i<childListRegulationReal.size(); i++)
				{
					socializationRegulationTmpDb = (SocializationRegulationTmp) childListRegulationReal.get(i);										
					if(socializationRegulationTmpDb.getSocializationRegulationId() != null && socializationRegulationTmpNew.getSocializationRegulationId() != null
							&& socializationRegulationTmpDb.getSocializationRegulationId().equals(socializationRegulationTmpNew.getSocializationRegulationId())
					){
						exist = true;
						break;
					}
				}
				
				if (exist)
				{	// update 			
					socializationRegulationTmpDb.setRegulation(socializationRegulationTmpNew.getRegulation());
						
					socializationRegulationTmpDb.setLastUpdateBy(userLogin);
					socializationRegulationTmpDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
					socializationRegulationTmpDb.setDelId(new Long(0));
					socializationRegulationTmpDb.setEnabledFlag(Constants.CONSTANT_YES);
					socializationRegulationTmpDb.setPrimaryFlag(socializationRegulationTmpNew.getPrimaryFlag());
				}
				else
				{	// insert 
					socializationRegulationTmp = new SocializationRegulationTmp();
					socializationRegulationTmp.setRegulation(socializationRegulationTmpNew.getRegulation());				
					socializationRegulationTmp.setSocialization(socializationDb);				
					socializationRegulationTmp.setCreatedBy(userLogin);
					socializationRegulationTmp.setCreationDate(new Timestamp(new Date().getTime()));
					socializationRegulationTmp.setDelId(new Long(0));
					socializationRegulationTmp.setEnabledFlag(Constants.CONSTANT_YES);	
					socializationRegulationTmp.setPrimaryFlag(socializationRegulationTmpNew.getPrimaryFlag());
					childListRegulationReal.add(socializationRegulationTmp);
				}
			}
							
			for(int i=0; i<childListRegulationReal.size(); i++)
			{
				socializationRegulationTmpDb = (SocializationRegulationTmp) childListRegulationReal.get(i);

				for(int x=0; x<childListRegulationNew.size(); x++)
				{
					socializationRegulationTmpNew =(SocializationRegulationTmp) childListRegulationNew.get(x);
					exist = false;				
					if((socializationRegulationTmpDb.getSocializationRegulationId() != null && socializationRegulationTmpNew.getSocializationRegulationId() != null
						&& socializationRegulationTmpDb.getSocializationRegulationId().equals(socializationRegulationTmpNew.getSocializationRegulationId())) 
						||
						(socializationRegulationTmpDb.getSocializationRegulationId() == null && socializationRegulationTmpNew.getSocializationRegulationId() == null)
					){
						exist = true;
						break;
					}
				}

				if (!exist)
				{	// delete 
					i--;
					childListRegulationReal.remove(socializationRegulationTmpDb);
				}
			}				
		}	
		//RegulationList
		
		
				
				//PIC Complience List
				List<SocializationPICComplianceTmp> childListComplianceReal = socializationDb.getSocializationPICComplianceTmps(); 
				List<SocializationPICComplianceTmp> childListComplianceNew = socializationNew.getSocializationPICComplianceTmps(); 
				if(childListComplianceNew != null) {
					SocializationPICComplianceTmp socializationPICComplianceTmp = null;
					SocializationPICComplianceTmp socializationPICComplianceTmpDb = null;
					SocializationPICComplianceTmp socializationPICComplianceTmpNew = null;
					boolean exist = false;
					
					// untuk insert data baru dan update data lama 
					for(int x=0; x<childListComplianceNew.size(); x++)
					{
						socializationPICComplianceTmpNew = (SocializationPICComplianceTmp) childListComplianceNew.get(x);
						
						exist = false;
						for(int i=0; i<childListComplianceReal.size(); i++)
						{
							socializationPICComplianceTmpDb = (SocializationPICComplianceTmp) childListComplianceReal.get(i);										
							if(socializationPICComplianceTmpDb.getSocializationPicComplianceId() != null && socializationPICComplianceTmpNew.getSocializationPicComplianceId() != null
									&& socializationPICComplianceTmpDb.getSocializationPicComplianceId().equals(socializationPICComplianceTmpNew.getSocializationPicComplianceId())
							){
								exist = true;
								break;
							}
						}
						
						if (exist)
						{	// update 			
							socializationPICComplianceTmpDb.setUser(socializationPICComplianceTmpNew.getUser());
								
							socializationPICComplianceTmpDb.setLastUpdateBy(userLogin);
							socializationPICComplianceTmpDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
							socializationPICComplianceTmpDb.setDelId(new Long(0));
							socializationPICComplianceTmpDb.setEnabledFlag(Constants.CONSTANT_YES);
						}
						else
						{	// insert 
							socializationPICComplianceTmp = new SocializationPICComplianceTmp();
							socializationPICComplianceTmp.setUser(socializationPICComplianceTmpNew.getUser());				
							socializationPICComplianceTmp.setSocializationTmp(socializationDb);				
							socializationPICComplianceTmp.setCreatedBy(userLogin);
							socializationPICComplianceTmp.setCreationDate(new Timestamp(new Date().getTime()));
							socializationPICComplianceTmp.setDelId(new Long(0));
							socializationPICComplianceTmp.setEnabledFlag(Constants.CONSTANT_YES);
							
							childListComplianceReal.add(socializationPICComplianceTmp);
						}
					}
									
					for(int i=0; i<childListComplianceReal.size(); i++)
					{
						socializationPICComplianceTmpDb = (SocializationPICComplianceTmp) childListComplianceReal.get(i);

						for(int x=0; x<childListComplianceNew.size(); x++)
						{
							socializationPICComplianceTmpNew =(SocializationPICComplianceTmp) childListComplianceNew.get(x);
							exist = false;				
							if((socializationPICComplianceTmpDb.getSocializationPicComplianceId() != null && socializationPICComplianceTmpNew.getSocializationPicComplianceId() != null
								&& socializationPICComplianceTmpDb.getSocializationPicComplianceId().equals(socializationPICComplianceTmpNew.getSocializationPicComplianceId())) 
								||
								(socializationPICComplianceTmpDb.getSocializationPicComplianceId() == null && socializationPICComplianceTmpNew.getSocializationPicComplianceId() == null)
							){
								exist = true;
								break;
							}
						}

						if (!exist)
						{	// delete 
							i--;
							childListComplianceReal.remove(socializationPICComplianceTmpDb);
						}
					}				
				}	
				//PIC Complience List
				
				//PIC Followup List
				List<SocializationPICFollowupTmp> childListFollowupReal = socializationDb.getSocializationPICFollowupTmps(); 
				List<SocializationPICFollowupTmp> childListFollowupNew = socializationNew.getSocializationPICFollowupTmps(); 
				if(childListFollowupNew != null) {
					SocializationPICFollowupTmp socializationPICFollowupTmp = null;
					SocializationPICFollowupTmp socializationPICFollowupTmpDb = null;
					SocializationPICFollowupTmp socializationPICFollowupTmpNew = null;
					boolean exist = false;
					
					// untuk insert data baru dan update data lama 
					for(int x=0; x<childListFollowupNew.size(); x++)
					{
						socializationPICFollowupTmpNew = (SocializationPICFollowupTmp) childListFollowupNew.get(x);
						
						exist = false;
						for(int i=0; i<childListFollowupReal.size(); i++)
						{
							socializationPICFollowupTmpDb = (SocializationPICFollowupTmp) childListFollowupReal.get(i);										
							if(socializationPICFollowupTmpDb.getSocializationPicFollowupId() != null && socializationPICFollowupTmpNew.getSocializationPicFollowupId() != null
									&& socializationPICFollowupTmpDb.getSocializationPicFollowupId().equals(socializationPICFollowupTmpNew.getSocializationPicFollowupId())
							){
								exist = true;
								break;
							}
						}
						
						if (exist)
						{	// update 			
							socializationPICFollowupTmpDb.setUser1(socializationPICFollowupTmpNew.getUser1());
							socializationPICFollowupTmpDb.setUser2(socializationPICFollowupTmpNew.getUser2());
							socializationPICFollowupTmpDb.setUser3(socializationPICFollowupTmpNew.getUser3());
							socializationPICFollowupTmpDb.setTargetDate(socializationPICFollowupTmpNew.getTargetDate());
							socializationPICFollowupTmpDb.setNotes(socializationPICFollowupTmpNew.getNotes());
							socializationPICFollowupTmpDb.setDivisionId(socializationPICFollowupTmpNew.getDivisionId());
							socializationPICFollowupTmpDb.setRescheduleReason(socializationPICFollowupTmpNew.getRescheduleReason());
							socializationPICFollowupTmpDb.setLastUpdateBy(userLogin);
							socializationPICFollowupTmpDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
							socializationPICFollowupTmpDb.setDelId(new Long(0));
							socializationPICFollowupTmpDb.setEnabledFlag(Constants.CONSTANT_YES);
						}
						else
						{	// insert 
							socializationPICFollowupTmp = new SocializationPICFollowupTmp();
							socializationPICFollowupTmp.setUser1(socializationPICFollowupTmpNew.getUser1());
							socializationPICFollowupTmp.setUser2(socializationPICFollowupTmpNew.getUser2());
							socializationPICFollowupTmp.setUser3(socializationPICFollowupTmpNew.getUser3());
							socializationPICFollowupTmp.setTargetDate(socializationPICFollowupTmpNew.getTargetDate());
							socializationPICFollowupTmp.setNotes(socializationPICFollowupTmpNew.getNotes());	
							socializationPICFollowupTmp.setSocialization(socializationDb);
							socializationPICFollowupTmp.setDivisionId(socializationPICFollowupTmpNew.getDivisionId());
							socializationPICFollowupTmpDb.setRescheduleReason(socializationPICFollowupTmpNew.getRescheduleReason());
							socializationPICFollowupTmp.setCreatedBy(userLogin);
							socializationPICFollowupTmp.setCreationDate(new Timestamp(new Date().getTime()));
							socializationPICFollowupTmp.setDelId(new Long(0));
							socializationPICFollowupTmp.setEnabledFlag(Constants.CONSTANT_YES);		
							
							childListFollowupReal.add(socializationPICFollowupTmp);
						}
					}
									
					for(int i=0; i<childListFollowupReal.size(); i++)
					{
						socializationPICFollowupTmpDb = (SocializationPICFollowupTmp) childListFollowupReal.get(i);

						for(int x=0; x<childListFollowupNew.size(); x++)
						{
							socializationPICFollowupTmpNew =(SocializationPICFollowupTmp) childListFollowupNew.get(x);
							exist = false;				
							if((socializationPICFollowupTmpDb.getSocializationPicFollowupId() != null && socializationPICFollowupTmpNew.getSocializationPicFollowupId() != null
								&& socializationPICFollowupTmpDb.getSocializationPicFollowupId().equals(socializationPICFollowupTmpNew.getSocializationPicFollowupId())) 
								||
								(socializationPICFollowupTmpDb.getSocializationPicFollowupId() == null && socializationPICFollowupTmpNew.getSocializationPicFollowupId() == null)
							){
								exist = true;
								break;
							}
						}

						if (!exist)
						{	// delete 
							i--;
							childListFollowupReal.remove(socializationPICFollowupTmpDb);
						}
					}				
				}	
				//PIC Followup List
		

				socializationDb.setLastUpdateBy(userLogin);
				socializationDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
				socializationDb.setDelId(new Long(0));
				socializationDb.setEnabledFlag(Constants.CONSTANT_YES);
				
				socializationTmpDao.update(socializationDb);
	}
	
        
}
