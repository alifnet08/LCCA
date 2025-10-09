/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.header.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.header.dao.HeaderFrontEndDao;

@Transactional
@Service("headerFrontEndService")
public class HeaderFrontEndServiceImpl implements HeaderFrontEndService {
    @Autowired
    @Qualifier("headerFrontEndDao")
    private HeaderFrontEndDao headerFrontEndDao;

	@Override
	public Long getCountPertanyaanBelumDijawab(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountPertanyaanBelumDijawab(nik);
	}

	@Override
	public Long getCountPertanyaanSudahDijawab(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountPertanyaanSudahDijawab(nik);
	}

	@Override
	public Long getCountPertanyaanPerluDijawab(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountPertanyaanPerluDijawab(nik);
	}

	@Override
	public Long getCountPertanyaanPerluDitutup(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountPertanyaanPerluDitutup(nik);
	}

	@Override
	public Long getCountTindakLanjutSosialisasi(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountTindakLanjutSosialisasi(nik);
	}

	@Override
	public Long getCountTindakLanjutDenda(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountTindakLanjutDenda(nik);
	}

	@Override
	public Long getCountTindakLanjutSuratMasuk(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountTindakLanjutSuratMasuk(nik);
	}

	@Override
	public Long getCountTindakLanjutRegulatoryReport(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountTindakLanjutRegulatoryReport(nik);
	}

	@Override
	public Long getCountTindakLanjutAudit(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountTindakLanjutAudit(nik);
	}

	@Override
	public Long getCountTindakLanjutComplianceTesting(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountTindakLanjutComplianceTesting(nik);
	}

	@Override
	public Long getCountTindakLanjutRegulationMonitor(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountTindakLanjutRegulationMonitor(nik);
	}

	@Override
	public Long getCountSosialisasiPerluVerifikasi(String nik, String action) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountSosialisasiPerluVerifikasi(nik,action);
	}

	@Override
	public Long getCountDendaPerluVerifikasi(String nik, String action) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountDendaPerluVerifikasi(nik,action);
	}

	@Override
	public Long getCountSuratMasukPerluVerifikasi(String nik, String action) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountSuratMasukPerluVerifikasi(nik,action);
	}

	@Override
	public Long getCountRegulatoryReportPerluVerifikasi(String nik, String action) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountRegulatoryReportPerluVerifikasi(nik,action);
	}

	@Override
	public Long getCountAuditPerluVerifikasi(String nik, String action) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountAuditPerluVerifikasi(nik,action);
	}

	@Override
	public Long getCountComplianceTestingPerluVerifikasi(String nik, String action) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountComplianceTestingPerluVerifikasi(nik,action);
	}

	@Override
	public Long getCountRegulationMoitorPerluVerifikasi(String nik, String action) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getCountRegulationMoitorPerluVerifikasi(nik,action);
	}

	public Long getCountPeraturanInternalBaru(String nik, String action) throws Exception{
		return headerFrontEndDao.getCountPeraturanInternalBaru(nik,action);
	}
	
	public Long getCountPeraturanEksternalBaru(String nik, String action) throws Exception{
		return headerFrontEndDao.getCountPeraturanEksternalBaru(nik,action);
	}
	
	public Long getCountArtikelBaru(String nik, String action,String divisionName) throws Exception{
		return headerFrontEndDao.getCountArtikelBaru(nik,action,divisionName);
	}
	
	public Long getCountDiskusiBaru(String nik, String action) throws Exception{
		return headerFrontEndDao.getCountDiskusiBaru(nik,action);
	}

	@Override
	public Date getMaxDatePertanyaanBelumDijawab(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDatePertanyaanBelumDijawab(nik);
	}

	@Override
	public Date getMaxDatePertanyaanSudahDijawab(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDatePertanyaanSudahDijawab(nik);
	}

	@Override
	public Date getMaxDatePertanyaanPerluDijawab(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDatePertanyaanPerluDijawab(nik);
	}

	@Override
	public Date getMaxDatePertanyaanPerluDitutup(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDatePertanyaanPerluDitutup(nik);
	}

	@Override
	public Date getMaxDateTindakLanjutSosialisasi(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDateTindakLanjutSosialisasi(nik);
	}

	@Override
	public Date getMaxDateTindakLanjutDenda(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDateTindakLanjutDenda(nik);
	}

	@Override
	public Date getMaxDateTindakLanjutSuratMasuk(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDateTindakLanjutSuratMasuk(nik);
	}

	@Override
	public Date getMaxDateTindakLanjutAudit(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDateTindakLanjutAudit(nik);
	}

	@Override
	public Date getMaxDateTindakLanjutComplianceTesting(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDateTindakLanjutComplianceTesting(nik);
	}

	@Override
	public Date getMaxDateTindakLanjutRegulationMonitor(String nik) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDateTindakLanjutRegulationMonitor(nik);
	}

	@Override
	public Date getMaxDateSosialisasiPerluVerifikasi(String nik, String action) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDateSosialisasiPerluVerifikasi(nik, action);
	}

	@Override
	public Date getMaxDateDendaPerluVerifikasi(String nik, String action) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDateDendaPerluVerifikasi(nik, action);
	}

	@Override
	public Date getMaxDateSuratMasukPerluVerifikasi(String nik, String action) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDateSuratMasukPerluVerifikasi(nik, action);
	}

	@Override
	public Date getMaxDateAuditPerluVerifikasi(String nik, String action) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDateAuditPerluVerifikasi(nik, action);
	}

	@Override
	public Date getMaxDateComplianceTestingPerluVerifikasi(String nik, String action) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDateComplianceTestingPerluVerifikasi(nik, action);
	}

	@Override
	public Date getMaxDateRegulationMoitorPerluVerifikasi(String nik, String action) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDateRegulationMoitorPerluVerifikasi(nik, action);
	}

	@Override
	public Date getMaxDatePeraturanInternalBaru(String nik, String action) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDatePeraturanInternalBaru(nik, action);
	}

	@Override
	public Date getMaxDatePeraturanEksternalBaru(String nik, String action) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDatePeraturanEksternalBaru(nik, action);
	}

	@Override
	public Date getMaxDateArtikelBaru(String nik, String action,String divisionName) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDateArtikelBaru(nik, action,divisionName);
	}

	@Override
	public Date getMaxDateDiskusiBaru(String nik, String action) throws Exception {
		// TODO Auto-generated method stub
		return headerFrontEndDao.getMaxDateDiskusiBaru(nik, action);
	}
	
	public Date getMaxDateTindakLanjutRegulatoryReport(String nik) throws Exception{
		return headerFrontEndDao.getMaxDateTindakLanjutRegulatoryReport(nik);
	}
    
	public Boolean getIsAdmin(String nik) throws Exception{
		return headerFrontEndDao.getIsAdmin(nik);
	}
	
	public Long getCountViewerLitigasi(String nik,String action) throws Exception{
		return headerFrontEndDao.getCountViewerLitigasi(nik, action);
	}
	
	public Date getCountMaxDateViewerLitigasi(String nik,String action) throws Exception{
		return headerFrontEndDao.getCountMaxDateViewerLitigasi(nik, action);
	}
	
	public Long getCountOpiniBaru(String nik,String action,String divisionName) throws Exception{
		return headerFrontEndDao.getCountOpiniBaru(nik, action, divisionName);
	}
	
	public Date getMaxDateOpiniBaru(String nik,String action,String divisionName) throws Exception{
		return headerFrontEndDao.getMaxDateOpiniBaru(nik, action, divisionName);
	}
	
	public Long getCountCpsa(String nik) throws Exception {
		return headerFrontEndDao.getCountCpsa(nik);
	}

	public Date getMaxDateCpsa(String nik) throws Exception {
		return headerFrontEndDao.getMaxDateCpsa(nik);
	}

	@Override
	public Long getCountCpsaApproval(String nik) throws Exception {
		return headerFrontEndDao.getCountCpsaApproval(nik);
	}

	@Override
	public Date getMaxDateCpsaApproval(String nik) throws Exception {
		return headerFrontEndDao.getMaxDateCpsaApproval(nik);
	}
        
}
