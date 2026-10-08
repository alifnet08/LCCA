package com.wo.module.notary.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.notary.constant.NotaryConstants;
import com.wo.module.parameter.model.ParameterDetail;

public class Notary extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6395152164269025885L;

	private Long notaryId;
	private String area;
	private String notaryName;
	private String address;
	private ParameterDetail notaryCategory;
	private String areaCode;
	private String phoneNo;
	private String faxNo;
	private String email;
	private String mobileNo;
	private String workArea;
	private String note;
	private String notaryNo;
	private String userPengaju;
	private Date tanggalPengajuan;
	private String status;
	private String jenisPengajuan;
	private Date tanggalPensiun;
	private Date tanggalBerakhirPks;
	private String catatanRevisi;
	private String latestHistoryStatus;
	private String listingStatus;
	private String pendingListingStatus;
	private String catatanKhusus;
	private String catatanKhususFileId;
	private String catatanKhususFileName;
	private String catatanKhususRole;
	private String jenisBeforeCatatan;
	private List<NotaryDocument> notaryDocuments = new ArrayList<NotaryDocument>();
	
	public Long getNotaryId() {
		return notaryId;
	}

	public void setNotaryId(Long notaryId) {
		this.notaryId = notaryId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getNotaryName() {
		return notaryName;
	}

	public void setNotaryName(String notaryName) {
		this.notaryName = notaryName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ParameterDetail getNotaryCategory() {
		return notaryCategory;
	}

	public void setNotaryCategory(ParameterDetail notaryCategory) {
		this.notaryCategory = notaryCategory;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getWorkArea() {
		return workArea;
	}

	public void setWorkArea(String workArea) {
		this.workArea = workArea;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNotaryNo() {
		return notaryNo;
	}

	public void setNotaryNo(String notaryNo) {
		this.notaryNo = notaryNo;
	}

	public String getUserPengaju() {
		return userPengaju;
	}

	public void setUserPengaju(String userPengaju) {
		this.userPengaju = userPengaju;
	}

	public Date getTanggalPengajuan() {
		return tanggalPengajuan;
	}

	public void setTanggalPengajuan(Date tanggalPengajuan) {
		this.tanggalPengajuan = tanggalPengajuan;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getJenisPengajuan() {
		return jenisPengajuan;
	}

	public void setJenisPengajuan(String jenisPengajuan) {
		this.jenisPengajuan = jenisPengajuan;
	}

	public Date getTanggalPensiun() {
		return tanggalPensiun;
	}

	public void setTanggalPensiun(Date tanggalPensiun) {
		this.tanggalPensiun = tanggalPensiun;
	}

	public Date getTanggalBerakhirPks() {
		return tanggalBerakhirPks;
	}

	public void setTanggalBerakhirPks(Date tanggalBerakhirPks) {
		this.tanggalBerakhirPks = tanggalBerakhirPks;
	}

	public String getCatatanRevisi() {
		return catatanRevisi;
	}

	public void setCatatanRevisi(String catatanRevisi) {
		this.catatanRevisi = catatanRevisi;
	}

	public String getLatestHistoryStatus() {
		return latestHistoryStatus;
	}

	public void setLatestHistoryStatus(String latestHistoryStatus) {
		this.latestHistoryStatus = latestHistoryStatus;
	}

	public boolean isRevisionForLegal() {
		return NotaryConstants.STATUS_REVISION.equals(status)
				&& NotaryConstants.HISTORY_REVISION_SPV_TO_LEGAL.equals(latestHistoryStatus);
	}

	public String getListingStatus() {
		return listingStatus;
	}

	public void setListingStatus(String listingStatus) {
		this.listingStatus = listingStatus;
	}

	public String getListingStatusLabel() {
		if (listingStatus == null || listingStatus.trim().length() == 0) {
			return NotaryConstants.LISTING_STATUS_ACTIVE;
		}
		return listingStatus;
	}

	public String getPendingListingStatus() {
		return pendingListingStatus;
	}

	public void setPendingListingStatus(String pendingListingStatus) {
		this.pendingListingStatus = pendingListingStatus;
	}

	public String getCatatanKhusus() {
		return catatanKhusus;
	}

	public void setCatatanKhusus(String catatanKhusus) {
		this.catatanKhusus = catatanKhusus;
	}

	public String getCatatanKhususFileId() {
		return catatanKhususFileId;
	}

	public void setCatatanKhususFileId(String catatanKhususFileId) {
		this.catatanKhususFileId = catatanKhususFileId;
	}

	public String getCatatanKhususFileName() {
		return catatanKhususFileName;
	}

	public void setCatatanKhususFileName(String catatanKhususFileName) {
		this.catatanKhususFileName = catatanKhususFileName;
	}

	public String getCatatanKhususRole() {
		return catatanKhususRole;
	}

	public void setCatatanKhususRole(String catatanKhususRole) {
		this.catatanKhususRole = catatanKhususRole;
	}

	public boolean isCatatanKhususPengajuan() {
		if (NotaryConstants.JENIS_PENGAJUAN_CATATAN_KHUSUS.equals(jenisPengajuan)) {
			return true;
		}
		return pendingListingStatus != null && pendingListingStatus.trim().length() > 0;
	}

	public String getJenisPengajuanTampil() {
		if (isFreezeOrDelisting(pendingListingStatus) && jenisBeforeCatatan != null
				&& jenisBeforeCatatan.trim().length() > 0) {
			return jenisBeforeCatatan;
		}
		return jenisPengajuan;
	}

	private boolean isFreezeOrDelisting(String listingStatus) {
		return NotaryConstants.LISTING_STATUS_FREEZE.equals(listingStatus)
				|| NotaryConstants.LISTING_STATUS_DELISTING.equals(listingStatus);
	}

	public String getJenisBeforeCatatan() {
		return jenisBeforeCatatan;
	}

	public void setJenisBeforeCatatan(String jenisBeforeCatatan) {
		this.jenisBeforeCatatan = jenisBeforeCatatan;
	}

	public List<NotaryDocument> getNotaryDocuments() {
		return notaryDocuments;
	}

	public void setNotaryDocuments(List<NotaryDocument> notaryDocuments) {
		this.notaryDocuments = notaryDocuments;
	}

}
