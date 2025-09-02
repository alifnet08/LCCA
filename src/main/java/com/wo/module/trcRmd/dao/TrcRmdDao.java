/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcRmd.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcRmd.model.TrcRmd;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.trcRmd.vo.TrcRmdSearchVo;



/**
 *
 * @author hendra
 */
public interface TrcRmdDao extends  GenericDAO<TrcRmd, Long>, RetrieverDataPage<TrcRmdSearchVo> {
	public TrcRmdPicFollowup getFirstPicFollowupId(Long rmdId) throws Exception;
}
