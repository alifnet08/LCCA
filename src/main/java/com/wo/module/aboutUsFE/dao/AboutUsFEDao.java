package com.wo.module.aboutUsFE.dao;

import java.util.List;

import com.wo.module.aboutUs.model.AboutUs;
import com.wo.module.aboutUsFE.vo.AboutUsFEVo;
import com.wo.module.common.dao.GenericDAO;

public interface AboutUsFEDao extends GenericDAO<AboutUs, Long>{

	public List<AboutUsFEVo> getAllAboutUsFEVoData();
	
}
