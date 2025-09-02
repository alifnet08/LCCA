package com.wo.module.internalRegulationPenerbitan.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitan;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanPicTpg;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanPicTpk;
import com.wo.module.internalRegulationPenerbitan.vo.InternalRegulationPenerbitanVo;
import com.wo.module.user.model.User;

public interface InternalRegulationPenerbitanService extends RetrieverDataPage<InternalRegulationPenerbitanVo> {
	
	public InternalRegulationPenerbitan findById(Long id);
	
	public List<String> getDataRegulationTitle(String regulationTitle);

	public void saveStep1(InternalRegulationPenerbitan irg, String userLogin, List<InternalRegulationPenerbitanPicTpg> dataIrgPenerbitanPicTpgDeleteList, 
			List<InternalRegulationPenerbitanPicTpk> dataIrgPenerbitanPicTpkDeleteList ) throws Exception;
	
	public void saveStep2(InternalRegulationPenerbitan irg, String userLogin) throws Exception;
	
	public Boolean isCheckDataIrgByTitleAndNo(String regulationNo, String irgTitle, Long irgId);
	
	public void update(InternalRegulationPenerbitan irg);

	public List<User> getPicsIrg();
	
}
