/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.emailTemplate.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.emailTemplate.model.EmailTemplate;

public interface EmailTemplateService extends RetrieverDataPage<EmailTemplate> {

	public void save(EmailTemplate entity);

	public void update(EmailTemplate entity);

	public void delete(EmailTemplate entity);

	public EmailTemplate findById(Long id);

	public Boolean isEmailTemplateDuplicate(EmailTemplate emailTemplate);
	
	public Integer getEditEmailTemplateByIdAndName(Long id, String emailName) throws Exception;

	public EmailTemplate getEmailTemplateByEmailTemplateCode(String code);
}
