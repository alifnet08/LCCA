/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.regulationMonitoring.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.common.constant.Constants;
import com.wo.module.regulationMonitoring.dao.RegMonitoringTmpDAO;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICComplianceTmp;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpTmp;
import com.wo.module.regulationMonitoring.model.RegMonitoringRegulationTmp;
import com.wo.module.regulationMonitoring.model.RegMonitoringTmp;

@Transactional
@Service("regMonitoringTmpService")
public class RegMonitoringTmpServiceImpl implements RegMonitoringTmpService {
	@Autowired
	@Qualifier("regMonitoringTmpDAO")
	private RegMonitoringTmpDAO regMonitoringTmpDAO;

	public RegMonitoringTmpDAO getRegMonitoringTmpDAO() {
		return regMonitoringTmpDAO;
	}

	public void setRegMonitoringTmpDAO(RegMonitoringTmpDAO regMonitoringTmpDAO) {
		this.regMonitoringTmpDAO = regMonitoringTmpDAO;
	}

	@Override
	public void save(RegMonitoringTmp regulation) {
		regMonitoringTmpDAO.save(regulation);
	}

	@Override
	public void update(RegMonitoringTmp regulation) {
		regMonitoringTmpDAO.update(regulation);
	}

	@Override
	public void delete(RegMonitoringTmp regulation) {
		regMonitoringTmpDAO.delete(regulation);
	}

	@Override
	public RegMonitoringTmp findById(Long id) {
		return regMonitoringTmpDAO.findById(id);
	}

	@Override
	public void updateDataAlreadyExist(RegMonitoringTmp regMonitoringNew, RegMonitoringTmp regMonitoringDb,
			String userLogin) throws Exception {

		regMonitoringDb.setCounterType(regMonitoringNew.getCounterType());
		regMonitoringDb.setFollowUp(regMonitoringNew.getFollowUp());
		regMonitoringDb.setJenisKetentuan(regMonitoringNew.getJenisKetentuan());
		regMonitoringDb.setNotes(regMonitoringNew.getNotes());
		regMonitoringDb.setReminderStatus(regMonitoringNew.getReminderStatus());
		regMonitoringDb.setStatus("DATA_ACTIVE");

		// RegulationList
		List<RegMonitoringRegulationTmp> childListRegulationReal = regMonitoringDb.getRegMonitoringRegulationTmps();
		List<RegMonitoringRegulationTmp> childListRegulationNew = regMonitoringNew.getRegMonitoringRegulationTmps();
		if (childListRegulationNew != null) {
			RegMonitoringRegulationTmp regMonitoringRegulationTmp = null;
			RegMonitoringRegulationTmp regMonitoringRegulationTmpDb = null;
			RegMonitoringRegulationTmp regMonitoringRegulationTmpNew = null;
			boolean exist = false;

			// untuk insert data baru dan update data lama
			for (int x = 0; x < childListRegulationNew.size(); x++) {
				regMonitoringRegulationTmpNew = (RegMonitoringRegulationTmp) childListRegulationNew.get(x);

				exist = false;
				for (int i = 0; i < childListRegulationReal.size(); i++) {
					regMonitoringRegulationTmpDb = (RegMonitoringRegulationTmp) childListRegulationReal.get(i);
					if (regMonitoringRegulationTmpDb.getRegMonitoringRegulationTmpId() != null
							&& regMonitoringRegulationTmpNew.getRegMonitoringRegulationTmpId() != null
							&& regMonitoringRegulationTmpDb.getRegMonitoringRegulationTmpId()
									.equals(regMonitoringRegulationTmpNew.getRegMonitoringRegulationTmpId())) {
						exist = true;
						break;
					}
				}

				if (exist) { // update
					regMonitoringRegulationTmpDb.setRegulation(regMonitoringRegulationTmpNew.getRegulation());

					regMonitoringRegulationTmpDb.setLastUpdateBy(userLogin);
					regMonitoringRegulationTmpDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
					regMonitoringRegulationTmpDb.setDelId(new Long(0));
					regMonitoringRegulationTmpDb.setEnabledFlag(Constants.CONSTANT_YES);
					regMonitoringRegulationTmpDb.setPrimaryFlag(regMonitoringRegulationTmpNew.getPrimaryFlag());
				} else { // insert
					regMonitoringRegulationTmp = new RegMonitoringRegulationTmp();
					regMonitoringRegulationTmp.setRegulation(regMonitoringRegulationTmpNew.getRegulation());
					regMonitoringRegulationTmp.setRegMonitoringTmp(regMonitoringDb);
					regMonitoringRegulationTmp.setCreatedBy(userLogin);
					regMonitoringRegulationTmp.setCreationDate(new Timestamp(new Date().getTime()));
					regMonitoringRegulationTmp.setDelId(new Long(0));
					regMonitoringRegulationTmp.setEnabledFlag(Constants.CONSTANT_YES);
					regMonitoringRegulationTmp.setPrimaryFlag(regMonitoringRegulationTmpNew.getPrimaryFlag());
					childListRegulationReal.add(regMonitoringRegulationTmp);
				}
			}

			for (int i = 0; i < childListRegulationReal.size(); i++) {
				regMonitoringRegulationTmpDb = (RegMonitoringRegulationTmp) childListRegulationReal.get(i);

				for (int x = 0; x < childListRegulationNew.size(); x++) {
					regMonitoringRegulationTmpNew = (RegMonitoringRegulationTmp) childListRegulationNew.get(x);
					exist = false;
					if ((regMonitoringRegulationTmpDb.getRegMonitoringRegulationTmpId() != null
							&& regMonitoringRegulationTmpNew.getRegMonitoringRegulationTmpId() != null
							&& regMonitoringRegulationTmpDb.getRegMonitoringRegulationTmpId()
									.equals(regMonitoringRegulationTmpNew.getRegMonitoringRegulationTmpId()))
							|| (regMonitoringRegulationTmpDb.getRegMonitoringRegulationTmpId() == null
									&& regMonitoringRegulationTmpNew.getRegMonitoringRegulationTmpId() == null)) {
						exist = true;
						break;
					}
				}

				if (!exist) { // delete
					i--;
					childListRegulationReal.remove(regMonitoringRegulationTmpDb);
				}
			}
		}
		// RegulationList

		// PIC Complience List
		List<RegMonitoringPICComplianceTmp> childListComplianceReal = regMonitoringDb
				.getRegMonitoringPICComplianceTmps();
		List<RegMonitoringPICComplianceTmp> childListComplianceNew = regMonitoringNew
				.getRegMonitoringPICComplianceTmps();
		if (childListComplianceNew != null) {
			RegMonitoringPICComplianceTmp regMonitoringPICComplianceTmp = null;
			RegMonitoringPICComplianceTmp regMonitoringPICComplianceTmpDb = null;
			RegMonitoringPICComplianceTmp regMonitoringPICComplianceTmpNew = null;
			boolean exist = false;

			// untuk insert data baru dan update data lama
			for (int x = 0; x < childListComplianceNew.size(); x++) {
				regMonitoringPICComplianceTmpNew = (RegMonitoringPICComplianceTmp) childListComplianceNew.get(x);

				exist = false;
				for (int i = 0; i < childListComplianceReal.size(); i++) {
					regMonitoringPICComplianceTmpDb = (RegMonitoringPICComplianceTmp) childListComplianceReal.get(i);
					if (regMonitoringPICComplianceTmpDb.getRegMonitoringPicComplianceTmpId() != null
							&& regMonitoringPICComplianceTmpNew.getRegMonitoringPicComplianceTmpId() != null
							&& regMonitoringPICComplianceTmpDb.getRegMonitoringPicComplianceTmpId()
									.equals(regMonitoringPICComplianceTmpNew.getRegMonitoringPicComplianceTmpId())) {
						exist = true;
						break;
					}
				}

				if (exist) { // update
					regMonitoringPICComplianceTmpDb.setUser(regMonitoringPICComplianceTmpNew.getUser());

					regMonitoringPICComplianceTmpDb.setLastUpdateBy(userLogin);
					regMonitoringPICComplianceTmpDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
					regMonitoringPICComplianceTmpDb.setDelId(new Long(0));
					regMonitoringPICComplianceTmpDb.setEnabledFlag(Constants.CONSTANT_YES);
				} else { // insert
					regMonitoringPICComplianceTmp = new RegMonitoringPICComplianceTmp();
					regMonitoringPICComplianceTmp.setUser(regMonitoringPICComplianceTmpNew.getUser());
					regMonitoringPICComplianceTmp.setRegMonitoringTmp(regMonitoringDb);
					regMonitoringPICComplianceTmp.setCreatedBy(userLogin);
					regMonitoringPICComplianceTmp.setCreationDate(new Timestamp(new Date().getTime()));
					regMonitoringPICComplianceTmp.setDelId(new Long(0));
					regMonitoringPICComplianceTmp.setEnabledFlag(Constants.CONSTANT_YES);

					childListComplianceReal.add(regMonitoringPICComplianceTmp);
				}
			}

			for (int i = 0; i < childListComplianceReal.size(); i++) {
				regMonitoringPICComplianceTmpDb = (RegMonitoringPICComplianceTmp) childListComplianceReal.get(i);

				for (int x = 0; x < childListComplianceNew.size(); x++) {
					regMonitoringPICComplianceTmpNew = (RegMonitoringPICComplianceTmp) childListComplianceNew.get(x);
					exist = false;
					if ((regMonitoringPICComplianceTmpDb.getRegMonitoringPicComplianceTmpId() != null
							&& regMonitoringPICComplianceTmpNew.getRegMonitoringPicComplianceTmpId() != null
							&& regMonitoringPICComplianceTmpDb.getRegMonitoringPicComplianceTmpId()
									.equals(regMonitoringPICComplianceTmpNew.getRegMonitoringPicComplianceTmpId()))
							|| (regMonitoringPICComplianceTmpDb.getRegMonitoringPicComplianceTmpId() == null
									&& regMonitoringPICComplianceTmpNew.getRegMonitoringPicComplianceTmpId() == null)) {
						exist = true;
						break;
					}
				}

				if (!exist) { // delete
					i--;
					childListComplianceReal.remove(regMonitoringPICComplianceTmpDb);
				}
			}
		}
		// PIC Complience List

		// PIC Followup List
		List<RegMonitoringPICFollowUpTmp> childListFollowupReal = regMonitoringDb.getRegMonitoringPICFollowUpTmps();
		List<RegMonitoringPICFollowUpTmp> childListFollowupNew = regMonitoringNew.getRegMonitoringPICFollowUpTmps();
		if (childListFollowupNew != null) {
			RegMonitoringPICFollowUpTmp regMonitoringPICFollowUpTmp = null;
			RegMonitoringPICFollowUpTmp regMonitoringPICFollowUpTmpDb = null;
			RegMonitoringPICFollowUpTmp regMonitoringPICFollowUpTmpNew = null;
			boolean exist = false;

			// untuk insert data baru dan update data lama
			for (int x = 0; x < childListFollowupNew.size(); x++) {
				regMonitoringPICFollowUpTmpNew = (RegMonitoringPICFollowUpTmp) childListFollowupNew.get(x);

				exist = false;
				for (int i = 0; i < childListFollowupReal.size(); i++) {
					regMonitoringPICFollowUpTmpDb = (RegMonitoringPICFollowUpTmp) childListFollowupReal.get(i);
					if (regMonitoringPICFollowUpTmpDb.getRegMonitoringPicFollowUpTmpId() != null
							&& regMonitoringPICFollowUpTmpNew.getRegMonitoringPicFollowUpTmpId() != null
							&& regMonitoringPICFollowUpTmpDb.getRegMonitoringPicFollowUpTmpId()
									.equals(regMonitoringPICFollowUpTmpNew.getRegMonitoringPicFollowUpTmpId())) {
						exist = true;
						break;
					}
				}

				if (exist) { // update
					regMonitoringPICFollowUpTmpDb.setUser1(regMonitoringPICFollowUpTmpNew.getUser1());
					regMonitoringPICFollowUpTmpDb.setUser2(regMonitoringPICFollowUpTmpNew.getUser2());
					regMonitoringPICFollowUpTmpDb.setUser3(regMonitoringPICFollowUpTmpNew.getUser3());
					regMonitoringPICFollowUpTmpDb.setTargetDate(regMonitoringPICFollowUpTmpNew.getTargetDate());
					regMonitoringPICFollowUpTmpDb.setNotes(regMonitoringPICFollowUpTmpNew.getNotes());
					regMonitoringPICFollowUpTmpDb.setDivisionId(regMonitoringPICFollowUpTmpNew.getDivisionId());
					regMonitoringPICFollowUpTmpDb
							.setRescheduleReason(regMonitoringPICFollowUpTmpNew.getRescheduleReason());
					regMonitoringPICFollowUpTmpDb.setLastUpdateBy(userLogin);
					regMonitoringPICFollowUpTmpDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
					regMonitoringPICFollowUpTmpDb.setDelId(new Long(0));
					regMonitoringPICFollowUpTmpDb.setEnabledFlag(Constants.CONSTANT_YES);
				} else { // insert
					regMonitoringPICFollowUpTmp = new RegMonitoringPICFollowUpTmp();
					regMonitoringPICFollowUpTmp.setUser1(regMonitoringPICFollowUpTmpNew.getUser1());
					regMonitoringPICFollowUpTmp.setUser2(regMonitoringPICFollowUpTmpNew.getUser2());
					regMonitoringPICFollowUpTmp.setUser3(regMonitoringPICFollowUpTmpNew.getUser3());
					regMonitoringPICFollowUpTmp.setTargetDate(regMonitoringPICFollowUpTmpNew.getTargetDate());
					regMonitoringPICFollowUpTmp.setNotes(regMonitoringPICFollowUpTmpNew.getNotes());
					regMonitoringPICFollowUpTmp.setRegMonitoringTmp(regMonitoringDb);
					regMonitoringPICFollowUpTmp.setDivisionId(regMonitoringPICFollowUpTmpNew.getDivisionId());
					regMonitoringPICFollowUpTmpDb
							.setRescheduleReason(regMonitoringPICFollowUpTmpNew.getRescheduleReason());
					regMonitoringPICFollowUpTmp.setCreatedBy(userLogin);
					regMonitoringPICFollowUpTmp.setCreationDate(new Timestamp(new Date().getTime()));
					regMonitoringPICFollowUpTmp.setDelId(new Long(0));
					regMonitoringPICFollowUpTmp.setEnabledFlag(Constants.CONSTANT_YES);

					childListFollowupReal.add(regMonitoringPICFollowUpTmp);
				}
			}

			for (int i = 0; i < childListFollowupReal.size(); i++) {
				regMonitoringPICFollowUpTmpDb = (RegMonitoringPICFollowUpTmp) childListFollowupReal.get(i);

				for (int x = 0; x < childListFollowupNew.size(); x++) {
					regMonitoringPICFollowUpTmpNew = (RegMonitoringPICFollowUpTmp) childListFollowupNew.get(x);
					exist = false;
					if ((regMonitoringPICFollowUpTmpDb.getRegMonitoringPicFollowUpTmpId() != null
							&& regMonitoringPICFollowUpTmpNew.getRegMonitoringPicFollowUpTmpId() != null
							&& regMonitoringPICFollowUpTmpDb.getRegMonitoringPicFollowUpTmpId()
									.equals(regMonitoringPICFollowUpTmpNew.getRegMonitoringPicFollowUpTmpId()))
							|| (regMonitoringPICFollowUpTmpDb.getRegMonitoringPicFollowUpTmpId() == null
									&& regMonitoringPICFollowUpTmpNew.getRegMonitoringPicFollowUpTmpId() == null)) {
						exist = true;
						break;
					}
				}

				if (!exist) { // delete
					i--;
					childListFollowupReal.remove(regMonitoringPICFollowUpTmpDb);
				}
			}
		}
		// PIC Followup List

		regMonitoringDb.setLastUpdateBy(userLogin);
		regMonitoringDb.setLastUpdateDate(new Timestamp(new Date().getTime()));
		regMonitoringDb.setDelId(new Long(0));
		regMonitoringDb.setEnabledFlag(Constants.CONSTANT_YES);

		regMonitoringTmpDAO.update(regMonitoringDb);
	}

}
