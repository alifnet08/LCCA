package com.wo.module.common.vo;

import java.io.Serializable;
import java.lang.reflect.Field;

public class TableOfValuesVO implements Serializable {

	private static final long serialVersionUID = -7313266396227606229L;

	private String column_id;

	private String column_02;

	private String column_03;

	private String column_04;
	
	private String column_05;

	private String column_06;
	
	private String column_07;
	
	private String column_08;
	
	private String column_09;
	
	private String column_10;

	public String getColumn_id() {
		return column_id;
	}

	public void setColumn_id(String columnId) {
		column_id = columnId;
	}

	public String getColumn_02() {
		return column_02;
	}

	public void setColumn_02(String column02) {
		column_02 = column02;
	}

	public String getColumn_03() {
		return column_03;
	}

	public void setColumn_03(String column03) {
		column_03 = column03;
	}

	public String getColumn_04() {
		return column_04;
	}

	public void setColumn_04(String column_04) {
		this.column_04 = column_04;
	}

	public String getColumn_05() {
		return column_05;
	}

	public void setColumn_05(String column_05) {
		this.column_05 = column_05;
	}

	public String getColumn_06() {
		return column_06;
	}

	public void setColumn_06(String column_06) {
		this.column_06 = column_06;
	}

	public String getColumn_07() {
		return column_07;
	}

	public void setColumn_07(String column_07) {
		this.column_07 = column_07;
	}

	public String getColumn_08() {
		return column_08;
	}

	public void setColumn_08(String column_08) {
		this.column_08 = column_08;
	}

	public String getColumn_09() {
		return column_09;
	}

	public void setColumn_09(String column_09) {
		this.column_09 = column_09;
	}

	public String getColumn_10() {
		return column_10;
	}

	public void setColumn_10(String column_10) {
		this.column_10 = column_10;
	}
	
	public String toString() {
        StringBuffer strBuff = new StringBuffer();
        strBuff.append("[SOUT TableOfValues]>>>>> ");
            try {
                Field[] fields = TableOfValuesVO.class.getDeclaredFields();
                for (int i = 0; i < fields.length; i++)
                    strBuff.append(fields[i].getName()).append(" = ").append(fields[i].get(this)).append(", ");
            } catch(IllegalAccessException iae) { }
        return strBuff.toString();
    }
}