/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.emailTemplate.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.paging.SearchObject;
import com.wo.module.emailTemplate.dao.EmailTemplateDao;
import com.wo.module.emailTemplate.model.EmailTemplate;

@Transactional
@Service("emailTemplateService")
public class EmailTemplateServiceImpl implements EmailTemplateService {
    @Autowired
    @Qualifier("emailTemplateDao")
    private EmailTemplateDao emailTemplateDao;
    

	public EmailTemplateDao getEmailTemplateDao() {
		return emailTemplateDao;
	}

	public void setEmailTemplateDao(EmailTemplateDao emailTemplateDao) {
		this.emailTemplateDao = emailTemplateDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<EmailTemplate> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return emailTemplateDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return emailTemplateDao.searchCountData(searchCriteria);
	}
	
	public void save(EmailTemplate entity) {
		emailTemplateDao.save(entity);
	}
	
	public void update(EmailTemplate entity) {
		emailTemplateDao.update(entity);
	}
	
	public void delete(EmailTemplate entity) {
		emailTemplateDao.delete(entity);
	}
  
    public EmailTemplate findById(Long id) {
    	return emailTemplateDao.getById(id);
    }
    
    public Boolean isEmailTemplateDuplicate(EmailTemplate emailTemplate) {
    	return emailTemplateDao.isEmailTemplateDuplicate(emailTemplate);
    }

	@Override
	public Integer getEditEmailTemplateByIdAndName(Long id, String emailName) throws Exception {
		return emailTemplateDao.getEditEmailTemplateByName(id, emailName);
	}
	
	public EmailTemplate getEmailTemplateByEmailTemplateCode(String code) {
		return emailTemplateDao.getEmailTemplateByEmailTemplateCode(code);
	}
    
        
}
