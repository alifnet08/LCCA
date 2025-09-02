package com.wo.module.internalRegulationObsolete.service;

import java.util.List;

import org.primefaces.model.StreamedContent;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.internalRegulationObsolete.model.InternalRegulationObsolete;
import com.wo.module.internalRegulationObsolete.model.InternalRegulationTest;
import com.wo.module.internalRegulationObsolete.vo.InternalRegulationObsoleteVo;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.user.model.User;

public interface InternalRegulationObsoleteService extends RetrieverDataPage<InternalRegulationObsoleteVo>  {

	List<InternalRegulationTest> findAllInternalRegulationTest();

	void save(InternalRegulationObsolete regulationObsolete);

	void update(InternalRegulationObsolete regulationObsolete);

	InternalRegulationTest findInternalRegulationTestById(Long internalRegulationId);

	InternalRegulationObsolete findById(Long idLong);

	boolean checkIfExistByIrgId(Long irgId);

	List<String> getDataObsoleteTitle(String query);

	void saveData(InternalRegulationObsolete regulationObsolete, FacesUtil facesUtil);

	void delete(InternalRegulationObsolete irgObsolete);

	boolean checkIRGObsoleteByObsoleteTitle(String judulObsolete);

	List<User> findAllUniquePicIrgNames();

	List<InternalRegulationObsoleteVo> getAllData();

	boolean checkIRGObsoleteByObsoleteNum(String nomorObsolete);
	
}
