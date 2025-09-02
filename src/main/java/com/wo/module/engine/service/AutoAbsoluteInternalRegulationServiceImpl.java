package com.wo.module.engine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.engine.dao.AutoAbsoluteInternalRegulationDao;
import com.wo.module.engine.vo.AutoAbsoluteInternalRegulationVO;

@Transactional
@Service("autoAbsoluteInternalRegulationService")
public class AutoAbsoluteInternalRegulationServiceImpl implements AutoAbsoluteInternalRegulationService {

	@Autowired
	@Qualifier("autoAbsoluteInternalRegulationDao")
	private AutoAbsoluteInternalRegulationDao autoAbsoluteInternalRegulationDao;
	
	@Override
	public List<AutoAbsoluteInternalRegulationVO> getDataAutoAbsoluteInternalRegulation() {
		return autoAbsoluteInternalRegulationDao.getDataAutoAbsoluteInternalRegulation();
	}

}
