package com.wo.module.mstProvinceLocation.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.mstProvince.model.MstProvinceLocation;

public interface MstProvinceLocationDAO extends GenericDAO<MstProvinceLocation, Long>, RetrieverDataPage<MstProvinceLocation> {
	
	public MstProvinceLocation getProvinceLocationByProvince(String province);
}