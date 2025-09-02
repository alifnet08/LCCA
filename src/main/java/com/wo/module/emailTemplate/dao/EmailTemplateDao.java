/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.emailTemplate.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.emailTemplate.model.EmailTemplate;

/**
 *
 * @author hendra
 */
public interface EmailTemplateDao extends GenericDAO<EmailTemplate, Long>, RetrieverDataPage<EmailTemplate> {
	
	public Boolean isEmailTemplateDuplicate(EmailTemplate emailTemplate);
	
	public Integer getEditEmailTemplateByName(Long id, String emailName) throws Exception;
	
	public EmailTemplate getEmailTemplateByEmailTemplateCode(String code);
}
