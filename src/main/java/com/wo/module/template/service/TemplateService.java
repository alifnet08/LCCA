/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.template.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.template.model.Template;

public interface TemplateService extends RetrieverDataPage<Template> {

	public void save(Template entity);

	public void update(Template entity);

	public void delete(Template entity);

	public Template findById(Long id);
	
}
