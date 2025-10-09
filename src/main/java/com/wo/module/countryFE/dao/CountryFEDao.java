package com.wo.module.countryFE.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.country.model.Country;
import com.wo.module.countryFE.vo.CountryFEVO;

public interface CountryFEDao extends GenericDAO<Country, Long>, RetrieverDataPage<CountryFEVO>{

}
