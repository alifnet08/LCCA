/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.header.dao;

import java.util.Date;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.qa.model.QA;
/**
 *
 * @author hendra
 * 
 * Modification Alex
 */
public interface HeaderFrontEndDao extends  GenericDAO<QA, Long>{

	public Long getCountPertanyaanBelumDijawab(String nik) throws Exception;
	
	public Date getMaxDatePertanyaanBelumDijawab(String nik) throws Exception;
	
	public Long getCountPertanyaanSudahDijawab(String nik) throws Exception;
	
	public Date getMaxDatePertanyaanSudahDijawab(String nik) throws Exception;
	
	public Long getCountPertanyaanPerluDijawab(String nik) throws Exception;
	
	public Date getMaxDatePertanyaanPerluDijawab(String nik) throws Exception;
	
	public Long getCountPertanyaanPerluDitutup(String nik) throws Exception;
	
	public Date getMaxDatePertanyaanPerluDitutup(String nik) throws Exception; 
	
	public Long getCountTindakLanjutSosialisasi(String nik) throws Exception;
	
	public Date getMaxDateTindakLanjutSosialisasi(String nik) throws Exception;
	
	public Long getCountTindakLanjutDenda(String nik) throws Exception;
	
	public Date getMaxDateTindakLanjutDenda(String nik) throws Exception;
	
	public Long getCountTindakLanjutSuratMasuk(String nik) throws Exception;
	
	public Date getMaxDateTindakLanjutSuratMasuk(String nik) throws Exception;
	
	public Long getCountTindakLanjutRegulatoryReport(String nik) throws Exception;
	
	public Date getMaxDateTindakLanjutRegulatoryReport(String nik) throws Exception;
	
	public Long getCountTindakLanjutAudit(String nik) throws Exception;
	
	public Date getMaxDateTindakLanjutAudit(String nik) throws Exception;
	
	public Long getCountTindakLanjutComplianceTesting(String nik) throws Exception;
	
	public Date getMaxDateTindakLanjutComplianceTesting(String nik) throws Exception;
	
	public Long getCountTindakLanjutRegulationMonitor(String nik) throws Exception;
	
	public Date getMaxDateTindakLanjutRegulationMonitor(String nik) throws Exception;
	
	public Long getCountSosialisasiPerluVerifikasi(String nik,String action) throws Exception;
	
	public Date getMaxDateSosialisasiPerluVerifikasi(String nik,String action) throws Exception;
	
	public Long getCountDendaPerluVerifikasi(String nik,String action) throws Exception;
	
	public Date getMaxDateDendaPerluVerifikasi(String nik,String action) throws Exception; 
	
	public Long getCountSuratMasukPerluVerifikasi(String nik,String action) throws Exception;
	
	public Date getMaxDateSuratMasukPerluVerifikasi(String nik,String action) throws Exception;
	
	public Long getCountRegulatoryReportPerluVerifikasi(String nik,String action) throws Exception;
	
	public Long getCountAuditPerluVerifikasi(String nik,String action) throws Exception;
	
	public Date getMaxDateAuditPerluVerifikasi(String nik,String action) throws Exception; 
	
	public Long getCountComplianceTestingPerluVerifikasi(String nik,String action) throws Exception;
	
	public Date getMaxDateComplianceTestingPerluVerifikasi(String nik,String action) throws Exception;
	
	public Long getCountRegulationMoitorPerluVerifikasi(String nik,String action) throws Exception;
	
	public Date getMaxDateRegulationMoitorPerluVerifikasi(String nik,String action) throws Exception;
	
	public Long getCountPeraturanInternalBaru(String nik,String action) throws Exception;
	
	public Date getMaxDatePeraturanInternalBaru(String nik,String action) throws Exception;
	
	public Long getCountPeraturanEksternalBaru(String nik,String action) throws Exception;
	
	public Date getMaxDatePeraturanEksternalBaru(String nik,String action) throws Exception;
	
	public Long getCountOpiniBaru(String nik,String action,String divisionName) throws Exception;
	
	public Date getMaxDateOpiniBaru(String nik,String action,String divisionName) throws Exception;
	
	public Long getCountArtikelBaru(String nik,String action,String divisionName) throws Exception;
	
	public Date getMaxDateArtikelBaru(String nik,String action,String divisionName) throws Exception;
	
	public Long getCountDiskusiBaru(String nik,String action) throws Exception;
	
	public Date getMaxDateDiskusiBaru(String nik,String action) throws Exception;
	
	public Boolean getIsAdmin(String nik) throws Exception;
	
	public Long getCountViewerLitigasi(String nik,String action) throws Exception;
	
	public Date getCountMaxDateViewerLitigasi(String nik,String action) throws Exception; 
	
	public Long getCountCpsa(String nik) throws Exception;
	
	public Date getMaxDateCpsa(String nik) throws Exception;
	
	public Long getCountCpsaApproval(String nik) throws Exception;
	
	public Date getMaxDateCpsaApproval(String nik) throws Exception;
	
}
