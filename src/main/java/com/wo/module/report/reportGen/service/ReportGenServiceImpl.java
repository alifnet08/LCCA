package com.wo.module.report.reportGen.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.utility.SCMApiDelete;
import com.wo.module.common.utility.SCMApiDeleteImpl;
import com.wo.module.parameter.service.ParameterDetailService;
import com.wo.module.report.reportGen.dao.ReportGenDAO;
import com.wo.module.report.reportGen.model.ReportGen;

@Transactional
@Service("reportGenService")
public class ReportGenServiceImpl implements ReportGenService {

	@Autowired
	@Qualifier("reportGenDAO")
	private ReportGenDAO reportGenDAO;

	@Override
	public void save(ReportGen reportGen) throws Exception {
		// long count = reportGenDAO.searchCountData(new ArrayList());
		// institution.setInstitutionCode(InstitutionConstant.INSTITUTION_KEY_CODE
		// + String.valueOf(count + 1));
		reportGenDAO.save(reportGen);
		reportGenDAO.flush();
	}

	@Override
	public void update(ReportGen reportGen) throws Exception {
		reportGenDAO.update(reportGen);
		reportGenDAO.flush();
	}

	@Override
	public ReportGen findById(Long id) {
		return reportGenDAO.findById(id);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ReportGen> searchData(
			List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		return reportGenDAO.searchData(searchCriteria, first, pageSize,
				sortField, sortOrder);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria)
			throws Exception {
		return reportGenDAO.searchCountData(searchCriteria);
	}

	@Override
	public void bulkDelete(ReportGen[] selectedReportGen, String userId, ParameterDetailService parameterDetailService,
			FileUtil fileUtil) {
		
		try {
			List<String> fileIds = null;
			for(ReportGen reportGen : selectedReportGen) {
				ReportGen reportGenDb = reportGenDAO.findById(reportGen.getReportGenId());
				reportGenDb.setReportGenFile(null);
				reportGenDb.setLastUpdateBy(userId);
				reportGenDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
				reportGenDb.setEnabledFlag(CommonConstants.ENABLED_FLAG_FALSE);
				
				if(fileIds == null)
					fileIds = new ArrayList<String>();
				
				fileIds.add(reportGen.getReportGenFileId());
				
				reportGenDAO.update(reportGenDb);
			}
			
			SCMApiDelete delete = new SCMApiDeleteImpl(parameterDetailService, fileUtil);
			delete.bulkDelete(fileIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public ReportGenDAO getReportGenDAO() {
		return reportGenDAO;
	}

	public void setReportGenDAO(ReportGenDAO reportGenDAO) {
		this.reportGenDAO = reportGenDAO;
	}

	@Override
	public void delete(ReportGen reportGen) throws Exception {
		try {
			reportGenDAO.delete(reportGen);
			reportGenDAO.flush();
		} catch (ConstraintViolationException cx) {
			reportGenDAO.rollback();
			throw cx;
		} catch (HibernateException hx) {
			reportGenDAO.rollback();
			throw hx;
		} catch (Exception ex) {
			reportGenDAO.rollback();
			throw ex;
		}
	}


}
