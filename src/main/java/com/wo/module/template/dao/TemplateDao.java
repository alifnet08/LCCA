/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.template.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.template.model.Template;

/**
 *
 * @author hendra
 */
public interface TemplateDao extends GenericDAO<Template, Long>, RetrieverDataPage<Template> {
	
}
