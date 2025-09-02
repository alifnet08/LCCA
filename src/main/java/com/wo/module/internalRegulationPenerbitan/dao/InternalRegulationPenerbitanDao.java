package com.wo.module.internalRegulationPenerbitan.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitan;
import com.wo.module.internalRegulationPenerbitan.vo.InternalRegulationPenerbitanVo;
import com.wo.module.user.model.User;

public interface InternalRegulationPenerbitanDao extends  GenericDAO<InternalRegulationPenerbitan, Long>, RetrieverDataPage<InternalRegulationPenerbitanVo>{

	public List<String> getDataRegulationTitle(String regulationTitle);
	public Boolean isCheckDataIrgByTitleAndNo(String regulationNo, String irgTitle, Long irgId);
	public List<User> getPicsIrg();
}
