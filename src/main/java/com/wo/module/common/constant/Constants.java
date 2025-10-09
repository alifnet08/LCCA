package com.wo.module.common.constant;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public abstract class Constants {
	
	public static final String ACTION_ADD = "ADD";
	
	public static final String ACTION_EDIT = "EDIT";
	
	public static final String ACTION_VIEW = "VIEW";
	
	public final static String NAVIGATE_HOME = "home.faces";
	
	public final static String NAVIGATE_LOGIN_REDIRECT = "/home.faces?faces-redirect=true";
	
	public final static String NAVIGATE_LOGIN_REDIRECT_NEW = "/pages/login/login.faces?isLogout=true";
	
	public final static String NAVIGATE_HOME_KARYAWAN_REDIRECT = "karirKaryawan.faces?faces-redirect=true";
	
	public final static String NAVIGATE_REGISTER = "register.faces";
	
	public final static String NAVIGATE_FORGOT_PASSWORD = "forgotPassword.faces";
	
	public final static String NAVIGATE_JOB_LIST = "jobList.faces";
	
	public final static String NAVIGATE_DATA_IJP = "dataIjp.faces";
	
	public final static String NAVIGATE_DATA_KAGUM = "dataKagum.faces";
	
	public final static String NAVIGATE_UPDATE_PROFILE = "updateProfile.faces";
	
	public final static String NAVIGATE_MAIN = "main.faces";
			
	public final static String NAVIGATE_MAIN_REDIRECT = "main.faces?faces-redirect=true";
	
	public final static String ENABLED_FLAG_TRUE = "Y";

	public final static String ENABLED_FLAG_FALSE = "N";
	
	public final static String CONSTANT_YES = "Y";
	
	public final static String CONSTANT_NO = "N";
	
	public final static String REMINDER_ACTIVE = "REMINDER_ACTIVE";
	
	public final static String HOST_NAME_APPLICATION = "HOST_NAME_APPLICATION";
	
	public final static String LOCATION_PROVINCE = "LOCATION_PROVINCE";
	
	public final static String LOCATION_KABUPATEN = "LOCATION_KABUPATEN";
	
	public final static String LOCATION_KECAMATAN = "LOCATION_KECAMATAN";
	
	public final static String INPUT_DATE_FORMAT = "dd-MMM-yyyy";
	
	public final static String INPUT_DATE_PROFILE_FORMAT = "MM/dd/yyyy";
	
	public final static String INPUT_DATE_LOCALE = "en";
	
	public final static String EMAIL_SUCCESS = "SEND EMAIL SUCCESS";
	
	public final static String SESSION_KANDIDAT = "SESSION_KANDIDAT";
	
	public final static String SESSION_EMPLOYEE = "SESSION_EMPLOYEE";
	
	public final static String SESSION_LINK_CORPORATE_PORTAL = "SESSION_LINK_CORPORATE_PORTAL";
	
	public final static String SESSION_LINK_ELEARNING = "SESSION_LINK_ELEARNING";
	
	public final static String SESSION_NIK = "SESSION_NIK";
	
	public final static String SESSION_PAGE_SELECTED = "PAGE_SELECTED";
	
	public final static String SESSION_LANGUAGE = "SESSION_LANGUAGE";
	
	public final static String SESSION_ADMIN = "SESSION_ADMIN";
	
	public final static String SESSION_MENU = "SESSION_MENU";
	
	public final static String SESSION_ALLOWED_MENU = "SESSION_ALLOWED_MENU";
	
	public final static String SESSION_NEED_REDIRECT = "SESSION_NEED_REDIRECT";
	
	public final static String SESSION_LOGIN_ATTEMPT = "SESSION_LOGIN_ATTEMPT";
	
	public final static String LOGIN_HOME_ADMIN_URL = "/pages/login/login.faces";
	
	public final static String LOGIN_MAIN_ADMIN_URL = "/pages/main.faces?faces-redirect=true";
	
	public final static String LOGIN_MAIN_ADMIN_EMP_URL = "/pages/karirKaryawan.faces";
	
	public final static String LOGIN_INDEX_ADMIN_URL_REDIRECT = "indexAdmin.faces?faces-redirect=true";
	
	public final static String SYSTEM_ADMIN_URL_REDIRECT = "systemAdmin.faces?faces-redirect=true";
	
	public final static String TRANSLATOR_URL_REDIRECT = "translator.faces?faces-redirect=true";
	
	public final static String DASHBOARD_URL_REDIRECT = "indexAdmin.faces?faces-redirect=true";
	
	public final static String SETTING_CONTENT_BERANDA_URL_REDIRECT = "settingContentBeranda.faces?faces-redirect=true";
	
	public final static String SETTING_CONTENT_VACANCY_BANNER_URL_REDIRECT = "settingContentVacancyBanner.faces?faces-redirect=true";
	
	public final static String SETTING_CONTENT_KARYAWAN_BANNER_URL_REDIRECT = "settingContentKaryawanBanner.faces?faces-redirect=true";
	
	public final static String FOOTER_TULISAN_URL_REDIRECT = "footer.faces?faces-redirect=true";
	
	public final static String LOGIN_ADMIN_URL_REDIRECT = "loginAdmin.faces?faces-redirect=true";
	
	public final static String JOB_LIST_URL = "/pages/jobList.faces";
	
	public final static String UTF8 = "UTF-8";
	
	public final static String URL_ENCODE = "application/x-www-form-urlencoded";
	
	public final static String NOS_ENGINE = "nos_engine";
	
	public final static String METHOD_POST = "POST";
	
	public final static String LDAP_PATH_URL = "identity/v1/token";
	
	public final static String POSTING_INTERNAL = "POSTING_INTERNAL";
	
	public final static String POSTING_EXTERNAL = "POSTING_EXTERNAL";
	
	public final static String LDAP_LOGIN_API = "LDAP_LOGIN_API";
	
	public final static String KAGUM = "KAGUM";
	
	public final static String IJP = "IJP";
	
	public final static String FLAG_IJP_KAGUM = "FLAG_IJP_KAGUM";
	
	public final static String FLAG_IJP_KAGUM2 = "formKarirKaryawan:FLAG_IJP_KAGUM";
	
	
	public final static String VACANCY_DESCRIPTION = "VACANCY_DESCRIPTION";
	
	public final static String GENDER = "GENDER";
	
	public final static String AGE_FROM = "AGE_FROM";
	
	public final static String AGE_TO = "AGE_TO";
	
	public final static String EDUCATION = "EDUCATION";
	
	public final static String GPA = "GPA";
	
	public final static String SALARY_FROM = "SALARY_FROM";
	
	public final static String SALARY_TO = "SALARY_TO";
	
	public final static String EXPERIENCE_FROM = "EXPERIENCE_FROM";
	
	public final static String EXPERIENCE_TO = "EXPERIENCE_TO";
	
	public final static String COLUMNS_PER_GRID = "COLUMNS_PER_GRID";
	
	public final static String ROWS_PER_GRID = "ROWS_PER_GRID";
	
	public final static String SEPARATOR_UNDERLINE = "_";

	public final static String SEPARATOR_DOT = ".";
	
	public final static String APPLICANT_IDENTIFIER = "nationalityIdentifier";
	
	public final static String APPLICANT_MOBILE_NUMBER = "mobileNumber";
	
	public final static String APPLICANT_EMAIL_ADDRESS = "emailAddress";

	public static final String SYS_VAL_SERVER_URL = "SYS_VAL_SERVER_URL";
	
	public static final String KAGUM_EMAIL_CONTENT = "KAGUM_EMAIL_CONTENT";
	
	public static final String REGISTER_EMAIL_CONTENT = "REGISTER_EMAIL_CONTENT";

	public static final String NAVIGATE_JOB_LIST_HOME = "jobListHome.faces";
	
	public static final int DEFAULT_PAGINATION_ROWS = 10;
	
	public static final String BANNER_FILE_PATH = "BANNER_FILE_PATH";
	
	public static final String BANNER_DTL_FILE_PATH = "BANNER_DTL_FILE_PATH";
	
	public static final String VACANCY_BANNER_FILE_PATH = "VACANCY_BANNER_FILE_PATH";
	
	public static final String KARYAWAN_BANNER_FILE_PATH = "KARYAWAN_BANNER_FILE_PATH";
	
	public static final String KARYAWAN_BANNER_DTL_FILE_PATH = "KARYAWAN_BANNER_DTL_FILE_PATH";
	
	public static final String MODE_SAVE = "SAVE";
	
	public static final String MODE_UPDATE = "UPDATE";
	
	public final static String CMS_SUPERADMIN = "CMS_SUPERADMIN";
	
	public final static String MAX_CV_FILE_SIZE = "MAX_CV_FILE_SIZE";
	
	public final static String MAX_PHOTO_FILE_SIZE = "MAX_PHOTO_FILE_SIZE";
	
	public final static String COMPLIANCE_DOC_TYPE_KETENTUAN_EKSTERNAL = "Ketentuan Eksternal";
	
	public final static String COMPLIANCE_DOC_TYPE_KETENTUAN_INTERNAL = "Ketentuan Internal";
	
	public final static String COMPLIANCE_DOC_TYPE_KORESPONDENSI_SURAT_MASUK = "Korespondensi Surat Masuk";
	
	public final static String COMPLIANCE_DOC_TYPE_DOKUMEN_TINDAK_LANJUT_DARI_PIC = "Dokumen Tindak Lanjut dari PIC";
	
	public final static String COMPLIANCE_DOC_TYPE_PIC_FOLLOWUP = "PIC Followup";
	
	public final static String COMPLIANCE_DOC_TYPE_RMD = "RMD";
	
	public final static String COMPLIANCE_DOC_TYPE_DOKUMEN_COMPLIANCE_REVIEW = "Dokumen Compliance Review";
	
	public final static String COMPLIANCE_DOC_TYPE_FOLLOWUP_POINTS = "Poin Poin Tindak Lanjut";
	
	public final static String COMPLIANCE_DOC_TYPE_AUDIT_FINDINGS = "Temuan Pemeriksaan";
	
	public final static String COMPLIANCE_DOC_TYPE_AUDIT_BANK_RESPONSE = "Tanggapan Bank";
	
	public final static String COMPLIANCE_DOC_TYPE_AUDIT_BANK_COMMITMENT = "Komitmen Bank";
	public final static String COMPLIANCE_DOC_TYPE_AUDIT_ATTACHMENT = "Lampiran Audit";
	public final static String COMPLIANCE_DOC_TYPE_AUDIT_FOLLOWUP = "Audit PIC Followup";
	public final static String COMPLIANCE_DOC_TYPE_AUDIT_FOLLOWUP_LETTER = "Audit PIC Followup Letter";
	
	public final static String COMPLIANCE_DOC_TYPE_REGULATORY_REPORTING = "Regulatory Reporting";
	
	public final static String COMPLIANCE_DOC_TYPE_BUKTI_PEMBAYARAN_DENDA = "Bukti Pembayaran Denda";
	
	public final static String COMPLIANCE_DOC_TYPE_IRG_ATTACHMENT = "IRG Attachment";
	
	public final static int DEFAULT_PAGING_NUMBER = 10;
	
	public final static String FILE_SEPARATOR = System.getProperty("file.separator");
	
	public final static String SAME_DATA_VALUE = "SAME_DATA_VALUE";
	
	public final static String  REMINDER_PIC1 = "REMINDER_PIC1";
	
	public final static String  REMINDER_PIC2 = "REMINDER_PIC2";
	
	public final static String  REMINDER_PIC3 = "REMINDER_PIC3";
	
	public final static String  MENU_ID_APPROVAL_REQUEST_INTERNAL_REGULATION = "MENU_ID_APPROVAL_REQUEST_INTERNAL_REGULATION";
	
	public final static String  MENU_ID_APPROVAL_REQUEST_EXTERNAL_REGULATION = "MENU_ID_APPROVAL_REQUEST_EXTERNAL_REGULATION";
	
	public final static String  MENU_ID_APPROVAL_REQUEST_ARTICLE = "MENU_ID_APPROVAL_REQUEST_ARTICLE";
	
	public final static String  MENU_ID_APPROVAL_REQUEST_AUDIT = "MENU_ID_APPROVAL_REQUEST_AUDIT";
	
	public final static String  MENU_ID_APPROVAL_REQUEST_FAQ = "MENU_ID_APPROVAL_REQUEST_FAQ";
	
	public final static String  MENU_ID_FOLLOWUP_CONFIRMATION_SOCIALIZATION = "FOLLOWUP_CONFIRMATION_SOCIALIZATION";
	
	public final static String  MENU_ID_FOLLOWUP_CONFIRMATION_CORRESPONDENCE = "FOLLOWUP_CONFIRMATION_CORRESPONDENCE";
	
	public final static String  MENU_ID_FOLLOWUP_CONFIRMATION_FINE = "FOLLOWUP_CONFIRMATION_FINE";
	
	public final static String  MENU_ID_FOLLOWUP_CONFIRMATION_REG_MONITORING = "FOLLOWUP_CONFIRMATION_REG_MONITORING";
	
	public final static String  MENU_ID_FOLLOWUP_CONFIRMATION_CORRESPONDENCE_AML = "FOLLOWUP_CONFIRMATION_CORRESPONDENCE_AML";
	
	public final static String  MENU_ID_FOLLOWUP_CONFIRMATION_RMD = "FOLLOWUP_CONFIRMATION_RMD";
	
	public final static String  MENU_ID_FOLLOWUP_CONFIRMATION_COMPLIANCE_REVIEW = "FOLLOWUP_CONFIRMATION_COMPLIANCE_REVIEW";
	
	public final static String  MENU_ID_COMPLIANCE_REVIEW_VIEW = "MENU_ID_COMPLIANCE_REVIEW_VIEW";
	
	public final static String  MENU_ID_SOCIALIZATION_VIEW = "SOCIALIZATION_VIEW";
	
	public final static String  MENU_ID_CORRESPONDENCE_VIEW = "CORRESPONDENCE_VIEW";
	
	public final static String  MENU_ID_FINE_VIEW = "FINE_VIEW";
	
	public final static String  MENU_ID_REG_MONITORING_VIEW = "REG_MONITORING_VIEW";
	
	public final static String  MENU_ID_CORRESPONDENCE_AML_VIEW = "CORRESPONDENCE_AML_VIEW";
	
	public final static String  MENU_ID_FOLLOWUP_CONFIRMATION_AUDIT = "FOLLOWUP_CONFIRMATION_AUDIT";
	
	public final static String  MENU_ID_AUDIT_VIEW = "AUDIT_VIEW";
	
	public final static String  MENU_ID_LITIGATION_VIEW = "LITIGATION_VIEW";
	
	public final static String  MENU_ID_CPAS = "CPSA";
	
	public final static String  MENU_ID_CPAS_FE = "CPSA_FE";
	
	public final static String  MENU_ID_CPAS_APPROVAL_FE = "CPSA_APPROVAL_FE";
	
	public final static String  MENU_ID_CPAS_APPROVED_FE = "CPSA_APPROVED_FE";
	
	public final static String  MENU_ID_CPAS_REJECTED_FE = "CPSA_REJECTED_FE";
	
	public final static String TRUE = "true";
	
	public final static String QUERY_SELECT = "select";
	public final static String QUERY_UPDATE = "update";
	public final static String QUERY_DELETE = "delete";
	public final static String QUERY_PROCEDURE = "procedure";
	public final static String QUERY_FUNCTION = "function";
	public final static String QUERY_TRIGGER = "trigger";
	public final static String QUERY_CALL = "call";
	
	public final static String COMPLIANCE_REVIEW_DOCUMENT = "Compliance Review Document";
	
	public final static String OUTGOING_LETTER = "Outgoing Letter";
	public final static String RECEIPT = "Receipt";
	public final static String ARTICLE = "Article";
	
	public final static String DOC_NUM = "doc_num";
	public final static String CONFIRMATION_TYPE = "confirmation_type";
	public final static String CONFIRMATION_TYPE_AND_DOC_NUM = "confirmation_type - doc_num";
	public final static String NOTIFICATION_TYPE_AND_DOC_NUM = "notification_type - doc_num";
	
	public final static String OSCAR_CHECKER = "REGULATION_CHECKER";
	public final static String AUDIT_CHECKER = "AUDIT_CHECKER";
	public final static String ARTICLE_CHECKER = "ARTICLE_CHECKER";
	public final static String ARTICLE_CHECKER_CRA = "ARTICLE_CHECKER_CRA";
	public final static String ARTICLE_CHECKER_CLL = "ARTICLE_CHECKER_CLL";
	public final static String EMAIL_APPROVAL  = "EMAIL_APPROVAL";
	
	protected static String calg = "Blowfish"; // AES. DES, Blowfish
	protected static int keyLen = 128;		// 128 for AES, Blowfish, 64 for DES
	
	public static String encryptString(String str){
		SecretKeySpec key = readkey();
		byte[] messb = str.getBytes();
		byte[] ct = encrypt(messb, key);
		
		return bintohex(ct);
	}
	
	public static String decryptString(String str){
		SecretKeySpec key = readkey();
		byte[] pt = decrypt(hextobin(str), key);

		String dmess = new String(pt);
		
		return dmess;
	}
	
	// encrypt message t with key k
		public static byte[] encrypt(byte[] t, SecretKeySpec k) {

			try {
				Cipher c = Cipher.getInstance(calg);

				c.init(Cipher.ENCRYPT_MODE, k);

				return c.doFinal(t);

			} catch (Exception e) {
				System.err.println("Encryption failed: " + e);
			}

			return null;
		}


		// decrypt message t with key k
		public static byte[] decrypt(byte[] t, SecretKeySpec k) {

			try {
				Cipher c = Cipher.getInstance(calg);

				c.init(Cipher.DECRYPT_MODE, k);

				return c.doFinal(t);

			} catch (Exception e) {
				System.err.println("Decryption failed: " + e);
			}

			return null;
		}


		// reads key string from user, returns SecretKeySpec
		public static SecretKeySpec readkey() {
			SecretKeySpec kp = null;
			String line;
			byte [] bin = null;

			try {
				line = "417b8b2d167b9046a4041e83509b2610";

				// check if input is all hex or not
				boolean ishex = true;
				for (int i = 0; i < line.length(); i++)
					if (Character.digit(line.charAt(i), 16) < 0) {
						ishex = false;
						break;
					}

				// check hex key length
				if (ishex && line.length() != keyLen/4)
					System.err.println("Wrong hex ley lenght (" + line.length() +
									   "/" + keyLen/4 + ")");

				// make binary key
				if (ishex)
					bin = hextobin(line);
				else
					bin = asciitobin(line);

				// make key for crypto algorithm
				kp = new SecretKeySpec(bin, calg);

				//System.out.println("Key = |" + bintohex(kp.getEncoded()) + "|");

			} catch (Exception e) {
				System.err.println("Key generation failed" + e);
			}

			return kp;
		} // readkey()


		// make binary out of hex string
		public static byte[] hextobin(String s) {
			int len = (s.length()+1)/2;
			byte[] A = new byte[len];
			for (int i = 0; i < len; i++)
				A[i] = Integer.valueOf(s.substring(i*2, i*2+2), 16).byteValue();
			return A;
		}

		
		// returns new 128 bit key using MD5 of the string s
		public static byte[] asciitobin(String s) {
			byte[] A = null;
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				A = md.digest(s.getBytes());
			} catch (Exception e) {
	            System.err.println("Digest failed" + e);
	        }
			return A;
		}

		
		// returns new hex string representation of A
		public static String bintohex(byte[] A) {
			int len = A.length;
			StringBuffer sb = new StringBuffer(len*2);
			for (int i = 0; i < len; i++) {
				if ((A[i] & 0xFF) < 0x10)
					sb.append("0");
				sb.append(Integer.toHexString(A[i] & 0xFF));
			}
			return sb.toString();
		}
		
		public static <T> List<List<T>> chopped(List<T> list, final int L) {
		    List<List<T>> parts = new ArrayList<List<T>>();
		    final int N = list.size();
		    for (int i = 0; i < N; i += L) {
		        parts.add(new ArrayList<T>(
		            list.subList(i, Math.min(N, i + L)))
		        );
		    }
		    return parts;
		}
	
}
