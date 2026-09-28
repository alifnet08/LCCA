/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.notary.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.faq.model.Faq;
import com.wo.module.notary.model.Notary;

/**
 *
 * @author hendra
 */
public interface NotaryDao extends GenericDAO<Notary, Long>, RetrieverDataPage<Notary> {

	public String getLastNoPengajuan(String prefixLike) throws Exception;

}
