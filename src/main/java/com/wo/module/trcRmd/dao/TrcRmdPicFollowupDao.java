/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcRmd.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.trcRmd.vo.TrcRmdSearchVo;



/**
 *
 * @author neirkate
 */
public interface TrcRmdPicFollowupDao extends  GenericDAO<TrcRmdPicFollowup, Long>, RetrieverDataPage<TrcRmdSearchVo> {
	public List<TrcRmdPicFollowup> getTrcRmdPicFollowupByRmdId(Long rmdId) throws Exception;
}