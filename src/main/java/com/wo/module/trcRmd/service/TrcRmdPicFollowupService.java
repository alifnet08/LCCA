/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcRmd.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.trcRmd.vo.TrcRmdSearchVo;

public interface TrcRmdPicFollowupService extends RetrieverDataPage<TrcRmdSearchVo> {
	public void save(TrcRmdPicFollowup entity);

	public void update(TrcRmdPicFollowup entity);

	public void delete(TrcRmdPicFollowup entity);

	public TrcRmdPicFollowup findById(Long id);
	
	public List<TrcRmdPicFollowup> getTrcRmdPicFollowupByRmdId(Long rmdId) throws Exception;
}
