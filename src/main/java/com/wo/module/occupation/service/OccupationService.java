package com.wo.module.occupation.service;

import java.sql.Timestamp;
import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.occupation.vo.OccupationVO;

public interface OccupationService extends RetrieverDataPage<OccupationVO> {

	void update(List<OccupationVO> occupationFileList, String userCode, String fileName, Timestamp buttonPressedDate) throws Exception;

	String getFileName();
	
}
