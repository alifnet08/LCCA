/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.aboutUs.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.aboutUs.model.AboutUs;

/**
 *
 * @author hendra
 */
public interface AboutUsDao extends GenericDAO<AboutUs, Long>, RetrieverDataPage<AboutUs> {
	
}
