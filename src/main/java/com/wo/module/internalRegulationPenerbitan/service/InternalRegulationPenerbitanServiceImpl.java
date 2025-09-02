package com.wo.module.internalRegulationPenerbitan.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.internalRegulationPenerbitan.constant.InternalRegulationPenerbitanConstants;
import com.wo.module.internalRegulationPenerbitan.constant.InternalRegulationPenerbitanRomawiConstants;
import com.wo.module.internalRegulationPenerbitan.dao.InternalRegulationPenerbitanDao;
import com.wo.module.internalRegulationPenerbitan.dao.InternalRegulationPenerbitanPicTpgDao;
import com.wo.module.internalRegulationPenerbitan.dao.InternalRegulationPenerbitanPicTpkDao;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitan;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanPicTpg;
import com.wo.module.internalRegulationPenerbitan.model.InternalRegulationPenerbitanPicTpk;
import com.wo.module.internalRegulationPenerbitan.vo.InternalRegulationPenerbitanVo;
import com.wo.module.parameter.dao.ParameterDetailDao;
import com.wo.module.runningNumber.service.RunningNumberService;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Transactional
@Service("internalRegulationPenerbitanService")
public class InternalRegulationPenerbitanServiceImpl implements InternalRegulationPenerbitanService, Serializable{

	private static final long serialVersionUID = -6660093983209263014L;

	@Autowired
	@Qualifier("internalRegulationPenerbitanDao")
	private InternalRegulationPenerbitanDao internalRegulationPenerbitanDao;
	
	@Autowired
	@Qualifier("internalRegulationPenerbitanPicTpgDao")
	private InternalRegulationPenerbitanPicTpgDao internalRegulationPenerbitanPicTpgDao;
	
	@Autowired
	@Qualifier("internalRegulationPenerbitanPicTpkDao")
	private InternalRegulationPenerbitanPicTpkDao internalRegulationPenerbitanPicTpkDao;
	
	@Autowired
	@Qualifier("parameterDetailDao")
	private ParameterDetailDao parameterDetailDao;
	
	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;

	@Autowired
	@Qualifier("runningNumberService")
	private RunningNumberService runningNumberService;
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<InternalRegulationPenerbitanVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return internalRegulationPenerbitanDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		return internalRegulationPenerbitanDao.searchCountData(searchCriteria);
	}
	
	@Override
	public InternalRegulationPenerbitan findById(Long id) {
		return internalRegulationPenerbitanDao.findById(id);
	}
	
	private String monthConvertRomawi(String month) {
    	String romawiData = "";
    	if(month.equals("01")) {
    		romawiData = InternalRegulationPenerbitanRomawiConstants.ROMAWI_MONTH_1.getDesc();
    	}else if(month.equals("02")) {
    		romawiData = InternalRegulationPenerbitanRomawiConstants.ROMAWI_MONTH_2.getDesc();
    	}else if(month.equals("03")) {
    		romawiData = InternalRegulationPenerbitanRomawiConstants.ROMAWI_MONTH_3.getDesc();
    	}else if(month.equals("04")) {
    		romawiData = InternalRegulationPenerbitanRomawiConstants.ROMAWI_MONTH_4.getDesc();
    	}else if(month.equals("05")) {
    		romawiData = InternalRegulationPenerbitanRomawiConstants.ROMAWI_MONTH_5.getDesc();
    	}else if(month.equals("06")) {
    		romawiData = InternalRegulationPenerbitanRomawiConstants.ROMAWI_MONTH_6.getDesc();
    	}else if(month.equals("07")) {
    		romawiData = InternalRegulationPenerbitanRomawiConstants.ROMAWI_MONTH_7.getDesc();
    	}else if(month.equals("08")) {
    		romawiData = InternalRegulationPenerbitanRomawiConstants.ROMAWI_MONTH_8.getDesc();
    	}else if(month.equals("09")) {
    		romawiData = InternalRegulationPenerbitanRomawiConstants.ROMAWI_MONTH_9.getDesc();
    	}else if(month.equals("10")) {
    		romawiData = InternalRegulationPenerbitanRomawiConstants.ROMAWI_MONTH_10.getDesc();
    	}else if(month.equals("11")) {
    		romawiData = InternalRegulationPenerbitanRomawiConstants.ROMAWI_MONTH_11.getDesc();
    	}else if(month.equals("12")) {
    		romawiData = InternalRegulationPenerbitanRomawiConstants.ROMAWI_MONTH_12.getDesc();
    	}
    		    	
    	return romawiData;
    }
	
	private String getRefenceNumber(String userLogin) {
		String refenceNumber = "";
		try {
			SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
			SimpleDateFormat sdfYear = new SimpleDateFormat("YYYY");
			StringBuilder addOn = new StringBuilder();

			String refenceName = InternalRegulationPenerbitanConstants.RUNNING_NUMBER_TYPE;

			String bulanRomawi = monthConvertRomawi(sdfMonth.format(new Date()));
			String tahunCurrent = sdfYear.format(new Date());
			refenceNumber = refenceName + "/";
			refenceNumber += tahunCurrent ;

			Integer runningNumberSeq = runningNumberService.getRunningNumberSeq(refenceNumber, tahunCurrent, refenceName, userLogin);

			addOn.append(runningNumberSeq);
			int addOnLength = addOn.length();
			for (int i = 0; i < (6 - addOnLength); i++) {
				addOn.insert(0, "0");
			}
			
			refenceNumber += "/" + bulanRomawi + "/";
			refenceNumber += addOn.toString();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return refenceNumber;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void saveStep1(InternalRegulationPenerbitan irg, String userLogin, List<InternalRegulationPenerbitanPicTpg> dataIrgPenerbitanPicTpgDeleteList, 
			List<InternalRegulationPenerbitanPicTpk> dataIrgPenerbitanPicTpkDeleteList) throws Exception {		
		irg.setDelId(new Long(0));
		irg.setEnabledFlag(Constants.CONSTANT_YES);
		if(irg.getCreatedBy() == null || irg.getCreatedBy().equals(InternalRegulationPenerbitanConstants.STRING_EMPTY)) {
			irg.setCreatedBy(userLogin);
			irg.setReferenceNo(getRefenceNumber(userLogin));
			irg.setCreationDate(new Timestamp(new Date().getTime()));
			internalRegulationPenerbitanDao.save(irg);
		}else {			
			irg.setLastUpdateBy(userLogin);
			irg.setLastUpdateDate(new Timestamp(new Date().getTime()));			
			internalRegulationPenerbitanDao.update(irg);
		}
		
		if(dataIrgPenerbitanPicTpgDeleteList != null && dataIrgPenerbitanPicTpgDeleteList.size() > 0) {
			for(InternalRegulationPenerbitanPicTpg picTpg : dataIrgPenerbitanPicTpgDeleteList) {
				if(picTpg != null && picTpg.getIrgPicId() != null && picTpg.getIrgPicId() > 0) {
					internalRegulationPenerbitanPicTpgDao.delete(picTpg);
				}
			}
		}
		
		if(dataIrgPenerbitanPicTpkDeleteList != null && dataIrgPenerbitanPicTpkDeleteList.size() > 0) {
			for(InternalRegulationPenerbitanPicTpk picTpk : dataIrgPenerbitanPicTpkDeleteList) {
				if(picTpk != null && picTpk.getIrgPicId() != null && picTpk.getIrgPicId() > 0) {
					internalRegulationPenerbitanPicTpkDao.delete(picTpk);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void saveStep2(InternalRegulationPenerbitan irg, String userLogin) throws Exception {
				
		irg.setDelId(new Long(0));
		irg.setEnabledFlag(Constants.CONSTANT_YES);
		irg.setLastUpdateBy(userLogin);
		irg.setLastUpdateDate(new Timestamp(new Date().getTime()));			
		internalRegulationPenerbitanDao.update(irg);		
	}

	@Override
	public List<String> getDataRegulationTitle(String regulationTitle) {
		return internalRegulationPenerbitanDao.getDataRegulationTitle(regulationTitle);
	}

	@Override
	public Boolean isCheckDataIrgByTitleAndNo(String regulationNo, String irgTitle, Long irgId) {
		return internalRegulationPenerbitanDao.isCheckDataIrgByTitleAndNo(regulationNo, irgTitle, irgId);
	}

	@Override
	public void update(InternalRegulationPenerbitan irg) {
		internalRegulationPenerbitanDao.update(irg);
	}

	@Override
	public List<User> getPicsIrg() {
		return internalRegulationPenerbitanDao.getPicsIrg();
	}

}
