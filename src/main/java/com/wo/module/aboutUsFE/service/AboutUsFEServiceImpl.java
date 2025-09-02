package com.wo.module.aboutUsFE.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.aboutUsFE.dao.AboutUsFEDao;
import com.wo.module.aboutUsFE.vo.AboutUsFEVo;

@Transactional
@Service("aboutUsFEService")
public class AboutUsFEServiceImpl implements AboutUsFEService, Serializable{

	private static final long serialVersionUID = 2345022108494101697L;

	@Autowired
	@Qualifier("aboutUsFEDao")
	private AboutUsFEDao aboutUsFEDao;
	
	@Override
	public List<AboutUsFEVo> getAllAboutUsFEVoData() {
		return aboutUsFEDao.getAllAboutUsFEVoData();
	}

	public AboutUsFEDao getAboutUsFEDao() {
		return aboutUsFEDao;
	}

	public void setAboutUsFEDao(AboutUsFEDao aboutUsFEDao) {
		this.aboutUsFEDao = aboutUsFEDao;
	}

	
}
