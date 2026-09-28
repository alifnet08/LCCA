package com.wo.module.notary.constant;

public abstract class NotaryConstants {
	public final static String NAVIGATE_EDIT = "notaryEdit.faces";
	public final static String NAVIGATE_SEARCH = "notary.faces";
	
	public final static String TEMPLATE_FILE_PATH = "com/wo/template/templateNotary.xlsx";
	
	public final static String TEMPLATE_FILE_NAME = "Notary";
	public final static String TEMPLATE_FILE_NAME_ERROR = "ErrorNotary";
	public final static String TEMPLATE_FILE_NAME_SHEET_NOTARY = "Notary";
	public final static String TEMPLATE_FILE_NAME_SHEET_CATEGORY = "ListKategori";
	
	public final static String SEARCH_BY_AREA = "SEARCH_BY_AREA";
	public final static String SEARCH_BY_NOTARY_NAME = "SEARCH_BY_NOTARY_NAME";
	public final static String SEARCH_BY_AREA_CODE = "SEARCH_BY_AREA_CODE";
	
	public final static String SIGN_MINUS = "-";
	public final static String SIGN_PLUS = "+";
	
	public final static int EXCEL_ROW_IDX_GRID_START_DATA = 1;
	
	public final static int EXCEL_ROW_IDX_LIST_GRADE_GRID_HEADER = 0;
	public final static int EXCEL_ROW_IDX_LIST_GRADE_GRID_START_DATA = 1;
	
	public final static int EXCEL_COL_IDX_COMPANY_NAME = 0;
	public final static int EXCEL_COL_IDX_PLAN_TYPE = 1;
	
	public final static int EXCEL_ROW_IDX_LIST_CATEGORY_GRID_START_DATA = 1;
	
	public final static int EXCEL_COL_IDX_LIST_CATEGORY_CATEGORY_NAME = 0;
	
	public final static String EMPTY = "";
	
	public final static String STATUS_WAITING_APPROVAL_CDU_CHECKER = "waiting approval CDU Checker";
	public final static String STATUS_WAITING_APPROVAL_LEGAL = "waiting approval Legal";
	public final static String STATUS_WAITING_APPROVAL_SPV_LEGAL = "waiting approval SPV Legal";
	public final static String STATUS_COMPLETE = "complete";
	public final static String STATUS_REJECTED = "rejected";
	public final static String STATUS_REVISION = "revision";
	public final static String RESPONSIBILITY_CDU_CHECKER = "Role CDU Checker";
	public final static String RESPONSIBILITY_CDU_MAKER = "Role CDU Maker";
	public final static String RESPONSIBILITY_LEGAL = "Legal";
	public final static String RESPONSIBILITY_SPV_LEGAL = "SPV Legal";
	public final static String NAVIGATE_TASK = "notaryTask.faces";
	public final static String NAVIGATE_TASK_EDIT = "notaryTaskEdit.faces";
	public final static String SEARCH_BY_STATUS = "SEARCH_BY_STATUS";
	public final static String REVISI_TARGET_MAKER = "MAKER";
	public final static String REVISI_TARGET_LEGAL = "LEGAL";
	public final static String JENIS_PENGAJUAN_NOTARIS_BARU = "Notaris Baru";
	public final static String JENIS_PENGAJUAN_PERPANJANGAN = "Perpanjangan";
	public final static String JENIS_PENGAJUAN_UPDATE_DOKUMEN = "Update Dokumen";
	public final static String SESSION_JENIS_PENGAJUAN = "notaryJenisPengajuan";

	public final static String[] NOTARY_DOCUMENT_TYPES = {
			"Surat Permohonan Rekanan Notaris",
			"SK Pengangkatan Notaris",
			"Berita acara sumpah jabatan Notaris",
			"SK Pengangkatan PPAT",
			"Berita acara sumpah jabatan PPAT",
			"Bukti Kerjasama dengan Bank",
			"Surat Pernyataan pernyataan tidak sedang tersangkut pelanggaran kode etik Notaris/PPAT (draft MBI)",
			"Curriculum Vitae (CV)",
			"Bukti kepemilikan kantor",
			"Foto Kantor (tampak depan, dalam dan filling cabinet)",
			"Lampiran tarif biaya Notaris",
			"Surat keterangan dari ikatan Notaris dan PPAT memiliki rekam jejak yang baik",
			"GoAML",
			"Form AP Assessment Notaris/PPAT",
			"Bukti kepemilikan rekening di Maybank Indonesia",
			"Hasil screening melalui sistem SironKYC Tidak terdapat catatan negative atau lolos screening melalui system Pandawa",
			"SLIK 1 bulan terakhir sebelum pengajuan dengan hasil kolektibilitas lancar",
			"Formulir Third Party Assessment Checklist (aspek PDP) yang lengkap diisi secara benar dan telah ditandatangani notaris",
			"Lampiran Kesepakatan Kerjasama Ketentuan Pelindungan Data Pribadi (PDP) antara Pengendali Data Pribadi dengan Prosesor Data Pribadi"
	};
	
}
