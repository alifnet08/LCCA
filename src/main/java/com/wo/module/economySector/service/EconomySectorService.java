package com.wo.module.economySector.service;

import java.sql.Timestamp;
import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.economySector.vo.EconomySectorVO;

public interface EconomySectorService extends RetrieverDataPage<EconomySectorVO> {

	void update(List<EconomySectorVO> ecoSecFileList, String userCode, String fileName, Timestamp buttonPressedDate) throws Exception;

	String getFileName();
	
}
