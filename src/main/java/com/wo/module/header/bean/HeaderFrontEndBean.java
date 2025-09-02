package com.wo.module.header.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;

import com.wo.module.common.bean.CommonPagingFEBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.header.service.HeaderFrontEndService;
import com.wo.module.header.vo.NotificationDtlVo;
import com.wo.module.log.model.LogLogin;
import com.wo.module.log.service.LogLoginService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.FacesUtil.FacesScope;
import com.wo.module.user.model.User;

public class HeaderFrontEndBean extends CommonPagingFEBean<NotificationDtlVo> implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(HeaderFrontEndBean.class);

//	public FacesUtil facesUtil;
	public String username;
	public String nik;
	public Boolean isAdmin;
	private String showDataNotif;
	private String lastUpdateQuestionNotAnswered;
	private List<NotificationDtlVo> listAllData;

	public Long countPertanyaanBelumDijawab;
	public String maxDatePertanyaanBelumDijawab;
	public Long countPertanyaanSudahDijawab;
	public String maxDatePertanyaanSudahDijawab;
	public Long countPertanyaanPerluDijawab;
	public String maxDatePertanyaanPerluDijawab;
	public Long countPertanyaanPerluDitutup;
	public String maxDatePertanyaanPerluDitutup;

	public Long countTindakLanjutSosialisasi;
	public String maxDateTindakLanjutSosialisasi;
	public Long countTindakLanjutDenda;
	public String maxDateTindakLanjutDenda;
	public Long countTindakLanjutSuratMasuk;
	public String maxDateTindakLanjutSuratMasuk;
	public Long countTindakLanjutRegulatoryReport;
	public String maxDateTindakLanjutRegulatoryReport;

	public Long countTindakLanjutAudit;
	public String maxDateTindakLanjutAudit;
	public Long countTindakLanjutComplianceTesting;
	public String maxDateTindakLanjutComplianceTesting;
	public Long countTindakLanjutRegulationMonitor;
	public String maxDateTindakLanjutRegulationMonitor;

	public Long countSosialisasiPerluVerifikasi;
	public String maxDateSosialisasiPerluVerifikasi;
	public Long countDendaPerluVerifikasi;
	public String maxDateDendaPerluVerifikasi;
	public Long countSuratMasukPerluVerifikasi;
	public String maxDateSuratMasukPerluVerifikasi;
	public Long countRegulatoryReportPerluVerifikasi;
	public String maxDateAuditPerluVerifikasi;
	public Long countAuditPerluVerifikasi;
	public String maxDateComplianceTestingPerluVerifikasi;
	public Long countComplianceTestingPerluVerifikasi;
	public String maxDateRegulationMoitorPerluVerifikasi;
	public Long countRegulationMoitorPerluVerifikasi;

	public Long countPeraturanInternalBaru;
	public String maxDatePeraturanInternalBaru;
	public Long countPeraturanEksternalBaru;
	public String maxDatePeraturanEksternalBaru;
	public Long countArtikelBaru;
	public String maxDateArtikelBaru;
	public Long countOpiniBaru;
	public String maxDateOpiniBaru;
	public Long countDiskusiBaru;
	public String maxDateDiskusiBaru;
	public Long countLitigasiBaru;
	public String maxDateLitigasiBaru;
	public Long countCpsa;
	public String maxDateCpsa;
	public Long countCpsaApproval;
	public String maxDateCpsaApproval;

	private String linkTentangMaybank;
	private String linkElearning;

	private HeaderFrontEndService headerFrontEndService;
	private LogLoginService loginService;

	@SuppressWarnings("unused")
	@PostConstruct
	public void init() {
		if (FacesContext.getCurrentInstance() != null && FacesContext.getCurrentInstance().getExternalContext() != null
				&& FacesContext.getCurrentInstance().getExternalContext().getRequest() != null) {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			if (request != null && request.getSession(true) != null) {

				String viewId = request.getRequestURI();
				viewId = viewId.replaceAll("/compliance", "");
				HttpSession session = request.getSession(true);
				User user = (User) session.getAttribute(Constants.SESSION_EMPLOYEE);
				if (user != null) {
					String data[] = user.getName().split(" ");
					if (data.length > 0) {
						username = data[0];
					} else {
						username = user.getName();
					}

					nik = user.getNik();
				}

				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));

					Date date = null;
					int i = 0;
					showDataNotif = "";
					if (listAllData == null)
						listAllData = new ArrayList<NotificationDtlVo>();
					super.init();

					linkTentangMaybank = facesUtil.retrieveCorporatePortalLink();
					linkElearning = facesUtil.retrieveElearningLink();

					isAdmin = headerFrontEndService.getIsAdmin(nik);

					/*countPertanyaanBelumDijawab = headerFrontEndService.getCountPertanyaanBelumDijawab(nik);
					date = headerFrontEndService.getMaxDatePertanyaanBelumDijawab(nik);
					if (date != null) {
						maxDatePertanyaanBelumDijawab = sdf.format(date);
					}

					if ( countPertanyaanBelumDijawab > 0) {
						showDataNotif = showDataNotif + "countPertanyaanBelumDijawab ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countPertanyaanBelumDijawab,
								maxDatePertanyaanBelumDijawab, "/pages/qaFE/qaFE.faces?faces-redirect=true",
								"Pertanyaan belum dijawab");
						listAllData.add(ndv);
					}*/

					countPertanyaanSudahDijawab = headerFrontEndService.getCountPertanyaanSudahDijawab(nik);
					date = null;
					date = headerFrontEndService.getMaxDatePertanyaanSudahDijawab(nik);
					if (date != null) {
						maxDatePertanyaanSudahDijawab = sdf.format(date);
					}

					if (/* i<3 && */ countPertanyaanSudahDijawab > 0) {
						showDataNotif = showDataNotif + " countPertanyaanSudahDijawab ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countPertanyaanSudahDijawab,
								maxDatePertanyaanSudahDijawab, "/pages/qaFE/qaFE.faces?faces-redirect=true",
								"Pertanyaan sudah dijawab");
						listAllData.add(ndv);
					}

					countPertanyaanPerluDijawab = headerFrontEndService.getCountPertanyaanPerluDijawab(nik);
					date = null;
					date = headerFrontEndService.getMaxDatePertanyaanPerluDijawab(nik);
					if (date != null) {
						maxDatePertanyaanPerluDijawab = sdf.format(date);
					}

					if (/* i<3 && */ countPertanyaanPerluDijawab > 0) {
						showDataNotif = showDataNotif + " countPertanyaanPerluDijawab ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countPertanyaanPerluDijawab,
								maxDatePertanyaanPerluDijawab, "/pages/qaFE/qaFE.faces?faces-redirect=true",
								"Pertanyaan perlu dijawab");
						listAllData.add(ndv);
					}

					countPertanyaanPerluDitutup = headerFrontEndService.getCountPertanyaanPerluDitutup(nik);
					date = null;
					date = headerFrontEndService.getMaxDatePertanyaanPerluDitutup(nik);
					if (date != null) {
						maxDatePertanyaanPerluDitutup = sdf.format(date);
					}

					if (/* i<3 && */ countPertanyaanPerluDitutup > 0) {
						showDataNotif = showDataNotif + " countPertanyaanPerluDitutup ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countPertanyaanPerluDitutup,
								maxDatePertanyaanPerluDitutup, "/pages/qaFE/qaFE.faces?faces-redirect=true",
								"Pertanyaan perlu ditutup");
						listAllData.add(ndv);
					}

					countTindakLanjutSosialisasi = headerFrontEndService.getCountTindakLanjutSosialisasi(nik);
					date = null;
					date = headerFrontEndService.getMaxDateTindakLanjutSosialisasi(nik);
					if (date != null) {
						maxDateTindakLanjutSosialisasi = sdf.format(date);
					}

					if (/* i<3 && */ countTindakLanjutSosialisasi > 0) {
						showDataNotif = showDataNotif + " countTindakLanjutSosialisasi ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countTindakLanjutSosialisasi,
								maxDateTindakLanjutSosialisasi,
								"/pages/socializationFE/socializationFE.faces?faces-redirect=true",
								"Tindak Lanjut Sosialisasi");
						listAllData.add(ndv);
					}

					countTindakLanjutDenda = headerFrontEndService.getCountTindakLanjutDenda(nik);
					date = null;
					date = headerFrontEndService.getMaxDateTindakLanjutDenda(nik);
					if (date != null) {
						maxDateTindakLanjutDenda = sdf.format(date);
					}

					if (/* i<3 && */ countTindakLanjutDenda > 0) {
						showDataNotif = showDataNotif + " countTindakLanjutDenda ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countTindakLanjutDenda, maxDateTindakLanjutDenda,
								"/pages/fineFE/fineFE.faces?faces-redirect=true", "Tindak Lanjut Denda");
						listAllData.add(ndv);
					}

					countTindakLanjutSuratMasuk = headerFrontEndService.getCountTindakLanjutSuratMasuk(nik);
					date = null;
					date = headerFrontEndService.getMaxDateTindakLanjutSuratMasuk(nik);
					if (date != null) {
						maxDateTindakLanjutSuratMasuk = sdf.format(date);
					}

					if (/* i<3 && */ countTindakLanjutSuratMasuk > 0) {
						showDataNotif = showDataNotif + " countTindakLanjutSuratMasuk ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countTindakLanjutSuratMasuk,
								maxDateTindakLanjutSuratMasuk,
								"/pages/correspondenceFE/correspondenceFE.faces?faces-redirect=true",
								"Tindak Lanjut Surat Masuk");
						listAllData.add(ndv);
					}

					countTindakLanjutRegulatoryReport = headerFrontEndService.getCountTindakLanjutRegulatoryReport(nik);
					date = null;
					date = headerFrontEndService.getMaxDateTindakLanjutRegulatoryReport(nik);
					if (date != null) {
						maxDateTindakLanjutRegulatoryReport = sdf.format(date);
					}

					if (/* i<3 && */ countTindakLanjutRegulatoryReport > 0) {
						showDataNotif = showDataNotif + " countTindakLanjutRegulatoryReport ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countTindakLanjutRegulatoryReport,
								maxDateTindakLanjutRegulatoryReport,
								"/pages/regulatoryReportingFE/regulatoryReportingFE.faces?faces-redirect=true",
								"Tindak Lanjut Regulatory Reporting");
						listAllData.add(ndv);
					}

					countTindakLanjutAudit = headerFrontEndService.getCountTindakLanjutAudit(nik);
					date = null;
					date = headerFrontEndService.getMaxDateTindakLanjutAudit(nik);
					if (date != null) {
						maxDateTindakLanjutAudit = sdf.format(date);
					}

					if (/* i<3 && */ countTindakLanjutAudit > 0) {
						showDataNotif = showDataNotif + " countTindakLanjutAudit ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countTindakLanjutAudit, maxDateTindakLanjutAudit,
								"/pages/auditFE/auditFE.faces?faces-redirect=true", "Tindak Lanjut Audit");
						listAllData.add(ndv);
					}

					countTindakLanjutComplianceTesting = headerFrontEndService
							.getCountTindakLanjutComplianceTesting(nik);
					date = null;
					date = headerFrontEndService.getMaxDateTindakLanjutComplianceTesting(nik);
					if (date != null) {
						maxDateTindakLanjutComplianceTesting = sdf.format(date);
					}

					if (/* i<3 && */ countTindakLanjutComplianceTesting > 0) {
						showDataNotif = showDataNotif + " countTindakLanjutComplianceTesting ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countTindakLanjutComplianceTesting,
								maxDateTindakLanjutComplianceTesting,
								"/pages/complianceTestingFE/complianceTestingFE.faces?faces-redirect=true",
								"Tindak Lanjut Compliance Testing");
						listAllData.add(ndv);
					}

					countTindakLanjutRegulationMonitor = headerFrontEndService
							.getCountTindakLanjutRegulationMonitor(nik);
					date = null;
					date = headerFrontEndService.getMaxDateTindakLanjutRegulationMonitor(nik);
					if (date != null) {
						maxDateTindakLanjutRegulationMonitor = sdf.format(date);
					}

					if (/* i<3 && */ countTindakLanjutRegulationMonitor > 0) {
						showDataNotif = showDataNotif + " countTindakLanjutRegulationMonitor ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countTindakLanjutRegulationMonitor,
								maxDateTindakLanjutRegulationMonitor,
								"/pages/regulationMonitoringFE/regulationMonitoringFE.faces?faces-redirect=true",
								"Tindak Lanjut Regulation Monitoring");
						listAllData.add(ndv);
					}

					countSosialisasiPerluVerifikasi = headerFrontEndService.getCountSosialisasiPerluVerifikasi(nik,
							"/pages/regulationSocializationVerification/regulationSocializationVerification.faces");
					date = null;
					date = headerFrontEndService.getMaxDateSosialisasiPerluVerifikasi(nik,
							"/pages/regulationSocializationVerification/regulationSocializationVerification.faces");
					if (date != null) {
						maxDateSosialisasiPerluVerifikasi = sdf.format(date);
					}

					if (/* i<3 && */ countSosialisasiPerluVerifikasi > 0) {
						showDataNotif = showDataNotif + " countSosialisasiPerluVerifikasi ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countSosialisasiPerluVerifikasi,
								maxDateSosialisasiPerluVerifikasi,
								"/pages/regulationSocializationVerification/regulationSocializationVerification.faces?faces-redirect=true",
								"Sosialisasi Perlu Verifikasi");
						listAllData.add(ndv);
					}

					countDendaPerluVerifikasi = headerFrontEndService.getCountDendaPerluVerifikasi(nik,
							"/pages/trcFineApproval/trcFineApproval.faces");
					date = null;
					date = headerFrontEndService.getMaxDateDendaPerluVerifikasi(nik,
							"/pages/trcFineApproval/trcFineApproval.faces");
					if (date != null) {
						maxDateDendaPerluVerifikasi = sdf.format(date);
					}

					if (/* i<3 && */ countDendaPerluVerifikasi > 0) {
						showDataNotif = showDataNotif + " countDendaPerluVerifikasi ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countDendaPerluVerifikasi,
								maxDateDendaPerluVerifikasi,
								"/pages/trcFineApproval/trcFineApproval.faces?faces-redirect=true",
								"Denda Perlu Verifikasi");
						listAllData.add(ndv);
					}

					countSuratMasukPerluVerifikasi = headerFrontEndService.getCountSuratMasukPerluVerifikasi(nik,
							"/pages/trcCorrespondenceApproval/trcCorrespondenceApproval.faces");
					date = null;
					date = headerFrontEndService.getMaxDateSuratMasukPerluVerifikasi(nik,
							"/pages/trcCorrespondenceApproval/trcCorrespondenceApproval.faces");
					if (date != null) {
						maxDateSuratMasukPerluVerifikasi = sdf.format(date);
					}

					if (/* i<3 && */ countSuratMasukPerluVerifikasi > 0) {
						showDataNotif = showDataNotif + " countSuratMasukPerluVerifikasi ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countSuratMasukPerluVerifikasi,
								maxDateSuratMasukPerluVerifikasi,
								"/pages/trcCorrespondenceApproval/trcCorrespondenceApproval.faces?faces-redirect=true",
								"Surat Masuk Perlu Verifikasi");
						listAllData.add(ndv);
					}

					// countRegulatoryReportPerluVerifikasi =
					// headerFrontEndService.getCountRegulatoryReportPerluVerifikasi(nik,viewId);
					countAuditPerluVerifikasi = headerFrontEndService.getCountAuditPerluVerifikasi(nik,
							"/pages/trcAuditVerification/trcAuditVerification.faces");
					date = null;
					date = headerFrontEndService.getMaxDateAuditPerluVerifikasi(nik,
							"/pages/trcAuditVerification/trcAuditVerification.faces");
					if (date != null) {
						maxDateAuditPerluVerifikasi = sdf.format(date);
					}

					if (/* i<3 && */ countAuditPerluVerifikasi > 0) {
						showDataNotif = showDataNotif + " countAuditPerluVerifikasi ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countAuditPerluVerifikasi,
								maxDateAuditPerluVerifikasi,
								"/pages/trcAuditVerification/trcAuditVerification.faces?faces-redirect=true",
								"Audit Perlu Verifikasi");
						listAllData.add(ndv);
					}

					countComplianceTestingPerluVerifikasi = headerFrontEndService
							.getCountComplianceTestingPerluVerifikasi(nik,
									"/pages/trcComplianceReviewApproval/trcComplianceReviewApproval.faces");
					date = null;
					date = headerFrontEndService.getMaxDateComplianceTestingPerluVerifikasi(nik,
							"/pages/trcComplianceReviewApproval/trcComplianceReviewApproval.faces");
					if (date != null) {
						maxDateComplianceTestingPerluVerifikasi = sdf.format(date);
					}

					if (/* i<3 && */ countComplianceTestingPerluVerifikasi > 0) {
						showDataNotif = showDataNotif + " countComplianceTestingPerluVerifikasi ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countComplianceTestingPerluVerifikasi,
								maxDateComplianceTestingPerluVerifikasi,
								"/pages/trcComplianceReviewApproval/trcComplianceReviewApproval.faces?faces-redirect=true",
								"Compliance Testing Perlu Verifikasi");
						listAllData.add(ndv);
					}

					countRegulationMoitorPerluVerifikasi = headerFrontEndService
							.getCountRegulationMoitorPerluVerifikasi(nik,
									"/pages/regulationMonitoringVerification/regulationMonitoringVerification.faces");
					date = null;
					date = headerFrontEndService.getMaxDateRegulationMoitorPerluVerifikasi(nik,
							"/pages/regulationMonitoringVerification/regulationMonitoringVerification.faces");
					if (date != null) {
						maxDateRegulationMoitorPerluVerifikasi = sdf.format(date);
					}

					if (/* i<3 && */ countRegulationMoitorPerluVerifikasi > 0) {
						showDataNotif = showDataNotif + " countRegulationMoitorPerluVerifikasi ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countRegulationMoitorPerluVerifikasi,
								maxDateRegulationMoitorPerluVerifikasi,
								"/pages/regulationMonitoringVerification/regulationMonitoringVerification.faces?faces-redirect=true",
								"Regulation Monitoring Perlu Verifikasi");
						listAllData.add(ndv);
					}

					countPeraturanInternalBaru = headerFrontEndService.getCountPeraturanInternalBaru(nik,
							"/compliance/pages/internalRegulationFE/internalRegulationFE.faces");
					date = null;
					date = headerFrontEndService.getMaxDatePeraturanInternalBaru(nik,
							"/compliance/pages/internalRegulationFE/internalRegulationFE.faces");
					if (date != null) {
						maxDatePeraturanInternalBaru = sdf.format(date);
					}

					if (/* i<3 && */ countPeraturanInternalBaru > 0) {
						showDataNotif = showDataNotif + " countPeraturanInternalBaru ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countPeraturanInternalBaru,
								maxDatePeraturanInternalBaru,
								"/pages/internalRegulationFE/internalRegulationFE.faces?faces-redirect=true",
								"Peraturan Internal baru");
						listAllData.add(ndv);
					}

					countPeraturanEksternalBaru = headerFrontEndService.getCountPeraturanEksternalBaru(nik,
							"/compliance/pages/externalRegulationFE/externalRegulationFE.faces");
					date = null;
					date = headerFrontEndService.getMaxDatePeraturanEksternalBaru(nik,
							"/compliance/pages/externalRegulationFE/externalRegulationFE.faces");
					if (date != null) {
						maxDatePeraturanEksternalBaru = sdf.format(date);
					}

					if (/* i<3 && */ countPeraturanEksternalBaru > 0) {
						showDataNotif = showDataNotif + " countPeraturanEksternalBaru ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countPeraturanEksternalBaru,
								maxDatePeraturanEksternalBaru,
								"/pages/externalRegulationFE/externalRegulationFE.faces?faces-redirect=true",
								"Peraturan Eksternal baru");
						listAllData.add(ndv);
					}

					countArtikelBaru = headerFrontEndService.getCountArtikelBaru(nik,
							"/compliance/pages/articleFE/articleFE.faces", user.getDivisionName());
					date = null;
					date = headerFrontEndService.getMaxDateArtikelBaru(nik,
							"/compliance/pages/articleFE/articleFE.faces", user.getDivisionName());
					if (date != null) {
						maxDateArtikelBaru = sdf.format(date);
					}
					

					if (/* i<3 && */ countArtikelBaru > 0) {
						showDataNotif = showDataNotif + " countArtikelBaru ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countArtikelBaru, maxDateArtikelBaru,
								"/pages/articleFE/articleFE.faces?faces-redirect=true", "Artikel baru");
						listAllData.add(ndv);
					}
					
					countOpiniBaru = headerFrontEndService.getCountOpiniBaru(nik,
							"/compliance/pages/opinionFE/opinionFE.faces", user.getDivisionName());
					date = null;
					date = headerFrontEndService.getMaxDateOpiniBaru(nik,
							"/compliance/pages/opinionFE/opinionFE.faces", user.getDivisionName());
					if (date != null) {
						maxDateOpiniBaru = sdf.format(date);
					}
					
					if (/* i<3 && */ countOpiniBaru > 0) {
						showDataNotif = showDataNotif + " countOpiniBaru ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countOpiniBaru, maxDateArtikelBaru,
								"/pages/opinionFE/opinionFE.faces?faces-redirect=true", "Opini baru");
						listAllData.add(ndv);
					}
					
					countDiskusiBaru = headerFrontEndService.getCountDiskusiBaru(nik,
							"/compliance/pages/discussionFE/discussionFE.faces");
					date = null;
					date = headerFrontEndService.getMaxDateDiskusiBaru(nik,
							"/compliance/pages/discussionFE/discussionFE.faces");
					if (date != null) {
						maxDateDiskusiBaru = sdf.format(date);
					}

					if (/* i<3 && */ countDiskusiBaru > 0) {
						showDataNotif = showDataNotif + " countDiskusiBaru ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countDiskusiBaru, maxDateDiskusiBaru,
								"/pages/discussionFE/discussionFE.faces?faces-redirect=true", "Diskusi baru");
						listAllData.add(ndv);
					}
					
				/*	countLitigasiBaru = headerFrontEndService.getCountViewerLitigasi(nik,
							"/compliance/pages/litigationViewFE/litigationViewFE.faces");
					date = null;
					date = headerFrontEndService.getCountMaxDateViewerLitigasi(nik,"/compliance/pages/litigationViewFE/litigationViewFE.faces");
					if (date != null) {
						maxDateLitigasiBaru = sdf.format(date);
					}

					if (countLitigasiBaru > 0) {
						showDataNotif = showDataNotif + " countLitigasiBaru ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countLitigasiBaru, maxDateLitigasiBaru,
								"/pages/litigationViewFE/litigationViewFE.faces?faces-redirect=true", "Litigasi baru");
						listAllData.add(ndv);
					} */
					
					// ayu 20221110
					countCpsa = headerFrontEndService.getCountCpsa(nik);
					date = null;
					date = headerFrontEndService.getMaxDateCpsa(nik);
					if (date != null) {
						maxDateCpsa = sdf.format(date);
					}

					if (/* i<3 && */ countCpsa > 0) {
						showDataNotif = showDataNotif + " countCpsa ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countCpsa,
								maxDateCpsa,
								"/pages/cpsaFE/compliancePlanSelfAssessmentFE.faces?faces-redirect=true",
								"CPSA");
						listAllData.add(ndv);
					}
					
					countCpsaApproval = headerFrontEndService.getCountCpsaApproval(nik);
					date = null;
					date = headerFrontEndService.getMaxDateCpsaApproval(nik);
					if (date != null) {
						maxDateCpsaApproval = sdf.format(date);
					}
					
					if (/* i<3 && */ countCpsaApproval > 0) {
						showDataNotif = showDataNotif + " countCpsaApproval ";
						i++;

						NotificationDtlVo ndv = new NotificationDtlVo(countCpsaApproval,
								maxDateCpsaApproval,
								"/pages/cpsaApprovalFE/cpsaApprovalFE.faces?faces-redirect=true",
								"CPSA Approval");
						listAllData.add(ndv);
					}
					
					setListData(listAllData);
					
					search();

				} catch (Exception e) {
					e.printStackTrace();
				}
				/*
				 * QA qa = headerFrontEndService.getQAQuestionNotAnswered(); if(qa!=null)
				 * lastUpdateQuestionNotAnswered = qa.getLastUpdateDate() != null ?
				 * sdf.format(qa.getLastUpdateDate()) : "";
				 */
				
				removeSession();
			}
		}
	}

	public void searchAll() {
		String searchAll = facesUtil.retrieveRequestParam("SEARCH_VAL");

		String result = "/pages/searchAllFE/searchAllFE.faces?SEARCH_VAL=" + searchAll;
		String baseContextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(baseContextPath + result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doHandleLogout() {
		logger.debug("loginBean doHandleLogout");
		
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			HttpSession session = request.getSession(true);
			if (session.getAttribute(Constants.SESSION_LOGIN_ATTEMPT) != null) {
				LogLogin login = (LogLogin) session.getAttribute(Constants.SESSION_LOGIN_ATTEMPT);
				login.setLastLogout(new Timestamp(new Date().getTime()));
				loginService.update(login);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		facesUtil.removeAllSessionAttribute();
		facesUtil.removeAllManagedBeans(FacesScope.SESSION_SCOPE);
		
		
		/*Map<String, Object> requestCookieMap = FacesContext.getCurrentInstance()
				   .getExternalContext()
				   .getRequestCookieMap();
		
		if(requestCookieMap.get("userName") != null) {
			try {
				requestCookieMap.remove("userName");
			}catch(Exception e) {
				e.printStackTrace();	
			}
		}*/

		try {
			facesUtil.redirectScreen(Constants.NAVIGATE_LOGIN_REDIRECT_NEW);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		HeaderFrontEndBean.logger = logger;
	}

	public String getLastUpdateQuestionNotAnswered() {
		return lastUpdateQuestionNotAnswered;
	}

	public void setLastUpdateQuestionNotAnswered(String lastUpdateQuestionNotAnswered) {
		this.lastUpdateQuestionNotAnswered = lastUpdateQuestionNotAnswered;
	}

	public HeaderFrontEndService getHeaderFrontEndService() {
		return headerFrontEndService;
	}

	public void setHeaderFrontEndService(HeaderFrontEndService headerFrontEndService) {
		this.headerFrontEndService = headerFrontEndService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public LogLoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(LogLoginService loginService) {
		this.loginService = loginService;
	}

	public String getNik() {
		return nik;
	}

	public void setNik(String nik) {
		this.nik = nik;
	}

	public Long getCountPertanyaanBelumDijawab() {
		return countPertanyaanBelumDijawab;
	}

	public void setCountPertanyaanBelumDijawab(Long countPertanyaanBelumDijawab) {
		this.countPertanyaanBelumDijawab = countPertanyaanBelumDijawab;
	}

	public Long getCountPertanyaanSudahDijawab() {
		return countPertanyaanSudahDijawab;
	}

	public void setCountPertanyaanSudahDijawab(Long countPertanyaanSudahDijawab) {
		this.countPertanyaanSudahDijawab = countPertanyaanSudahDijawab;
	}

	public Long getCountPertanyaanPerluDijawab() {
		return countPertanyaanPerluDijawab;
	}

	public void setCountPertanyaanPerluDijawab(Long countPertanyaanPerluDijawab) {
		this.countPertanyaanPerluDijawab = countPertanyaanPerluDijawab;
	}

	public Long getCountPertanyaanPerluDitutup() {
		return countPertanyaanPerluDitutup;
	}

	public void setCountPertanyaanPerluDitutup(Long countPertanyaanPerluDitutup) {
		this.countPertanyaanPerluDitutup = countPertanyaanPerluDitutup;
	}

	public Long getCountTindakLanjutSosialisasi() {
		return countTindakLanjutSosialisasi;
	}

	public void setCountTindakLanjutSosialisasi(Long countTindakLanjutSosialisasi) {
		this.countTindakLanjutSosialisasi = countTindakLanjutSosialisasi;
	}

	public Long getCountTindakLanjutDenda() {
		return countTindakLanjutDenda;
	}

	public void setCountTindakLanjutDenda(Long countTindakLanjutDenda) {
		this.countTindakLanjutDenda = countTindakLanjutDenda;
	}

	public Long getCountTindakLanjutSuratMasuk() {
		return countTindakLanjutSuratMasuk;
	}

	public void setCountTindakLanjutSuratMasuk(Long countTindakLanjutSuratMasuk) {
		this.countTindakLanjutSuratMasuk = countTindakLanjutSuratMasuk;
	}

	public Long getCountTindakLanjutRegulatoryReport() {
		return countTindakLanjutRegulatoryReport;
	}

	public void setCountTindakLanjutRegulatoryReport(Long countTindakLanjutRegulatoryReport) {
		this.countTindakLanjutRegulatoryReport = countTindakLanjutRegulatoryReport;
	}

	public Long getCountTindakLanjutAudit() {
		return countTindakLanjutAudit;
	}

	public void setCountTindakLanjutAudit(Long countTindakLanjutAudit) {
		this.countTindakLanjutAudit = countTindakLanjutAudit;
	}

	public Long getCountTindakLanjutComplianceTesting() {
		return countTindakLanjutComplianceTesting;
	}

	public void setCountTindakLanjutComplianceTesting(Long countTindakLanjutComplianceTesting) {
		this.countTindakLanjutComplianceTesting = countTindakLanjutComplianceTesting;
	}

	public Long getCountTindakLanjutRegulationMonitor() {
		return countTindakLanjutRegulationMonitor;
	}

	public void setCountTindakLanjutRegulationMonitor(Long countTindakLanjutRegulationMonitor) {
		this.countTindakLanjutRegulationMonitor = countTindakLanjutRegulationMonitor;
	}

	public Long getCountSosialisasiPerluVerifikasi() {
		return countSosialisasiPerluVerifikasi;
	}

	public void setCountSosialisasiPerluVerifikasi(Long countSosialisasiPerluVerifikasi) {
		this.countSosialisasiPerluVerifikasi = countSosialisasiPerluVerifikasi;
	}

	public Long getCountDendaPerluVerifikasi() {
		return countDendaPerluVerifikasi;
	}

	public void setCountDendaPerluVerifikasi(Long countDendaPerluVerifikasi) {
		this.countDendaPerluVerifikasi = countDendaPerluVerifikasi;
	}

	public Long getCountSuratMasukPerluVerifikasi() {
		return countSuratMasukPerluVerifikasi;
	}

	public void setCountSuratMasukPerluVerifikasi(Long countSuratMasukPerluVerifikasi) {
		this.countSuratMasukPerluVerifikasi = countSuratMasukPerluVerifikasi;
	}

	public Long getCountRegulatoryReportPerluVerifikasi() {
		return countRegulatoryReportPerluVerifikasi;
	}

	public void setCountRegulatoryReportPerluVerifikasi(Long countRegulatoryReportPerluVerifikasi) {
		this.countRegulatoryReportPerluVerifikasi = countRegulatoryReportPerluVerifikasi;
	}

	public Long getCountAuditPerluVerifikasi() {
		return countAuditPerluVerifikasi;
	}

	public void setCountAuditPerluVerifikasi(Long countAuditPerluVerifikasi) {
		this.countAuditPerluVerifikasi = countAuditPerluVerifikasi;
	}

	public Long getCountComplianceTestingPerluVerifikasi() {
		return countComplianceTestingPerluVerifikasi;
	}

	public void setCountComplianceTestingPerluVerifikasi(Long countComplianceTestingPerluVerifikasi) {
		this.countComplianceTestingPerluVerifikasi = countComplianceTestingPerluVerifikasi;
	}

	public Long getCountRegulationMoitorPerluVerifikasi() {
		return countRegulationMoitorPerluVerifikasi;
	}

	public void setCountRegulationMoitorPerluVerifikasi(Long countRegulationMoitorPerluVerifikasi) {
		this.countRegulationMoitorPerluVerifikasi = countRegulationMoitorPerluVerifikasi;
	}

	public Long getCountPeraturanInternalBaru() {
		return countPeraturanInternalBaru;
	}

	public void setCountPeraturanInternalBaru(Long countPeraturanInternalBaru) {
		this.countPeraturanInternalBaru = countPeraturanInternalBaru;
	}

	public Long getCountPeraturanEksternalBaru() {
		return countPeraturanEksternalBaru;
	}

	public void setCountPeraturanEksternalBaru(Long countPeraturanEksternalBaru) {
		this.countPeraturanEksternalBaru = countPeraturanEksternalBaru;
	}

	public Long getCountArtikelBaru() {
		return countArtikelBaru;
	}

	public void setCountArtikelBaru(Long countArtikelBaru) {
		this.countArtikelBaru = countArtikelBaru;
	}

	public Long getCountDiskusiBaru() {
		return countDiskusiBaru;
	}

	public void setCountDiskusiBaru(Long countDiskusiBaru) {
		this.countDiskusiBaru = countDiskusiBaru;
	}

	public String getMaxDatePertanyaanBelumDijawab() {
		return maxDatePertanyaanBelumDijawab;
	}

	public void setMaxDatePertanyaanBelumDijawab(String maxDatePertanyaanBelumDijawab) {
		this.maxDatePertanyaanBelumDijawab = maxDatePertanyaanBelumDijawab;
	}

	public String getMaxDatePertanyaanSudahDijawab() {
		return maxDatePertanyaanSudahDijawab;
	}

	public void setMaxDatePertanyaanSudahDijawab(String maxDatePertanyaanSudahDijawab) {
		this.maxDatePertanyaanSudahDijawab = maxDatePertanyaanSudahDijawab;
	}

	public String getMaxDatePertanyaanPerluDijawab() {
		return maxDatePertanyaanPerluDijawab;
	}

	public void setMaxDatePertanyaanPerluDijawab(String maxDatePertanyaanPerluDijawab) {
		this.maxDatePertanyaanPerluDijawab = maxDatePertanyaanPerluDijawab;
	}

	public String getMaxDatePertanyaanPerluDitutup() {
		return maxDatePertanyaanPerluDitutup;
	}

	public void setMaxDatePertanyaanPerluDitutup(String maxDatePertanyaanPerluDitutup) {
		this.maxDatePertanyaanPerluDitutup = maxDatePertanyaanPerluDitutup;
	}

	public String getMaxDateTindakLanjutSosialisasi() {
		return maxDateTindakLanjutSosialisasi;
	}

	public void setMaxDateTindakLanjutSosialisasi(String maxDateTindakLanjutSosialisasi) {
		this.maxDateTindakLanjutSosialisasi = maxDateTindakLanjutSosialisasi;
	}

	public String getMaxDateTindakLanjutDenda() {
		return maxDateTindakLanjutDenda;
	}

	public void setMaxDateTindakLanjutDenda(String maxDateTindakLanjutDenda) {
		this.maxDateTindakLanjutDenda = maxDateTindakLanjutDenda;
	}

	public String getMaxDateTindakLanjutSuratMasuk() {
		return maxDateTindakLanjutSuratMasuk;
	}

	public void setMaxDateTindakLanjutSuratMasuk(String maxDateTindakLanjutSuratMasuk) {
		this.maxDateTindakLanjutSuratMasuk = maxDateTindakLanjutSuratMasuk;
	}

	public String getMaxDateTindakLanjutAudit() {
		return maxDateTindakLanjutAudit;
	}

	public void setMaxDateTindakLanjutAudit(String maxDateTindakLanjutAudit) {
		this.maxDateTindakLanjutAudit = maxDateTindakLanjutAudit;
	}

	public String getMaxDateTindakLanjutComplianceTesting() {
		return maxDateTindakLanjutComplianceTesting;
	}

	public void setMaxDateTindakLanjutComplianceTesting(String maxDateTindakLanjutComplianceTesting) {
		this.maxDateTindakLanjutComplianceTesting = maxDateTindakLanjutComplianceTesting;
	}

	public String getMaxDateTindakLanjutRegulationMonitor() {
		return maxDateTindakLanjutRegulationMonitor;
	}

	public void setMaxDateTindakLanjutRegulationMonitor(String maxDateTindakLanjutRegulationMonitor) {
		this.maxDateTindakLanjutRegulationMonitor = maxDateTindakLanjutRegulationMonitor;
	}

	public String getMaxDateSosialisasiPerluVerifikasi() {
		return maxDateSosialisasiPerluVerifikasi;
	}

	public void setMaxDateSosialisasiPerluVerifikasi(String maxDateSosialisasiPerluVerifikasi) {
		this.maxDateSosialisasiPerluVerifikasi = maxDateSosialisasiPerluVerifikasi;
	}

	public String getMaxDateDendaPerluVerifikasi() {
		return maxDateDendaPerluVerifikasi;
	}

	public void setMaxDateDendaPerluVerifikasi(String maxDateDendaPerluVerifikasi) {
		this.maxDateDendaPerluVerifikasi = maxDateDendaPerluVerifikasi;
	}

	public String getMaxDateSuratMasukPerluVerifikasi() {
		return maxDateSuratMasukPerluVerifikasi;
	}

	public void setMaxDateSuratMasukPerluVerifikasi(String maxDateSuratMasukPerluVerifikasi) {
		this.maxDateSuratMasukPerluVerifikasi = maxDateSuratMasukPerluVerifikasi;
	}

	public String getMaxDateAuditPerluVerifikasi() {
		return maxDateAuditPerluVerifikasi;
	}

	public void setMaxDateAuditPerluVerifikasi(String maxDateAuditPerluVerifikasi) {
		this.maxDateAuditPerluVerifikasi = maxDateAuditPerluVerifikasi;
	}

	public String getMaxDateComplianceTestingPerluVerifikasi() {
		return maxDateComplianceTestingPerluVerifikasi;
	}

	public void setMaxDateComplianceTestingPerluVerifikasi(String maxDateComplianceTestingPerluVerifikasi) {
		this.maxDateComplianceTestingPerluVerifikasi = maxDateComplianceTestingPerluVerifikasi;
	}

	public String getMaxDateRegulationMoitorPerluVerifikasi() {
		return maxDateRegulationMoitorPerluVerifikasi;
	}

	public void setMaxDateRegulationMoitorPerluVerifikasi(String maxDateRegulationMoitorPerluVerifikasi) {
		this.maxDateRegulationMoitorPerluVerifikasi = maxDateRegulationMoitorPerluVerifikasi;
	}

	public String getMaxDatePeraturanInternalBaru() {
		return maxDatePeraturanInternalBaru;
	}

	public void setMaxDatePeraturanInternalBaru(String maxDatePeraturanInternalBaru) {
		this.maxDatePeraturanInternalBaru = maxDatePeraturanInternalBaru;
	}

	public String getMaxDatePeraturanEksternalBaru() {
		return maxDatePeraturanEksternalBaru;
	}

	public void setMaxDatePeraturanEksternalBaru(String maxDatePeraturanEksternalBaru) {
		this.maxDatePeraturanEksternalBaru = maxDatePeraturanEksternalBaru;
	}

	public String getMaxDateArtikelBaru() {
		return maxDateArtikelBaru;
	}

	public void setMaxDateArtikelBaru(String maxDateArtikelBaru) {
		this.maxDateArtikelBaru = maxDateArtikelBaru;
	}

	public String getMaxDateDiskusiBaru() {
		return maxDateDiskusiBaru;
	}

	public void setMaxDateDiskusiBaru(String maxDateDiskusiBaru) {
		this.maxDateDiskusiBaru = maxDateDiskusiBaru;
	}

	public String getMaxDateTindakLanjutRegulatoryReport() {
		return maxDateTindakLanjutRegulatoryReport;
	}

	public void setMaxDateTindakLanjutRegulatoryReport(String maxDateTindakLanjutRegulatoryReport) {
		this.maxDateTindakLanjutRegulatoryReport = maxDateTindakLanjutRegulatoryReport;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getShowDataNotif() {
		return showDataNotif;
	}

	public void setShowDataNotif(String showDataNotif) {
		this.showDataNotif = showDataNotif;
	}

	public String getLinkTentangMaybank() {
		return linkTentangMaybank;
	}

	public void setLinkTentangMaybank(String linkTentangMaybank) {
		this.linkTentangMaybank = linkTentangMaybank;
	}

	public String getLinkElearning() {
		return linkElearning;
	}

	public void setLinkElearning(String linkElearning) {
		this.linkElearning = linkElearning;
	}

	@Override
	public List<NotificationDtlVo> searchData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria,
			int first, int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		setListData(listAllData);
		return getListData().stream().skip(first).limit(pageSize)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public Long searchCountData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria)
			throws Exception {
		return (listAllData == null || listAllData.isEmpty())? 0l : new Long(listAllData.size());
	}

	public List<NotificationDtlVo> getListAllData() {
		return listAllData;
	}

	public void setListAllData(List<NotificationDtlVo> listAllData) {
		this.listAllData = listAllData;
	}

	public Long getCountLitigasiBaru() {
		return countLitigasiBaru;
	}

	public void setCountLitigasiBaru(Long countLitigasiBaru) {
		this.countLitigasiBaru = countLitigasiBaru;
	}

	public String getMaxDateLitigasiBaru() {
		return maxDateLitigasiBaru;
	}

	public void setMaxDateLitigasiBaru(String maxDateLitigasiBaru) {
		this.maxDateLitigasiBaru = maxDateLitigasiBaru;
	}

	public Long getCountOpiniBaru() {
		return countOpiniBaru;
	}

	public void setCountOpiniBaru(Long countOpiniBaru) {
		this.countOpiniBaru = countOpiniBaru;
	}

	public String getMaxDateOpiniBaru() {
		return maxDateOpiniBaru;
	}

	public void setMaxDateOpiniBaru(String maxDateOpiniBaru) {
		this.maxDateOpiniBaru = maxDateOpiniBaru;
	}

	public Long getCountCpsa() {
		return countCpsa;
	}

	public void setCountCpsa(Long countCpsa) {
		this.countCpsa = countCpsa;
	}

	public String getMaxDateCpsa() {
		return maxDateCpsa;
	}

	public void setMaxDateCpsa(String maxDateCpsa) {
		this.maxDateCpsa = maxDateCpsa;
	}

	public Long getCountCpsaApproval() {
		return countCpsaApproval;
	}

	public void setCountCpsaApproval(Long countCpsaApproval) {
		this.countCpsaApproval = countCpsaApproval;
	}

	public String getMaxDateCpsaApproval() {
		return maxDateCpsaApproval;
	}

	public void setMaxDateCpsaApproval(String maxDateCpsaApproval) {
		this.maxDateCpsaApproval = maxDateCpsaApproval;
	}
	
	
}