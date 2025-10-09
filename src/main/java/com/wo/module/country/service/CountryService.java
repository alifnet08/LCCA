package com.wo.module.country.service;

import java.sql.Timestamp;
import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.country.vo.CountryVO;

public interface CountryService extends RetrieverDataPage<CountryVO> {

	void update(List<CountryVO> countryFileList, String userCode, String fileName ,Timestamp buttonPressedDate) throws Exception;

	String getFileName();
	
}
