package com.wo.module.internalRegulationObsolete.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.internalRegulationObsolete.model.InternalRegulationObsolete;
import com.wo.module.internalRegulationObsolete.model.InternalRegulationTest;
import com.wo.module.internalRegulationObsolete.vo.InternalRegulationObsoleteVo;
import com.wo.module.user.model.User;

public interface InternalRegulationObsoleteDao extends GenericDAO<InternalRegulationObsolete, Long>, 
		RetrieverDataPage<InternalRegulationObsoleteVo>{

	List<InternalRegulationTest> findAllInternalRegulationTest();

	InternalRegulationTest findInternalRegulationTestById(Long internalRegulationId);

	boolean checkIfExistByIrgId(Long irgId);

	List<String> getDataObsoleteTitle(String query);

	boolean checkIRGObsoleteByObsoleteTitle(String judulObsolete);

	List<User> findAllUniquePicIrgNames();

	List<InternalRegulationObsoleteVo> getAllData();

	boolean checkIRGObsoleteByObsoleteNum(String nomorObsolete);

}
