package com.wo.module.internalRegulationPenerbitan.constant;

public enum InternalRegulationPenerbitanRomawiConstants {
	
	ROMAWI_MONTH_1("01", "I"),
	ROMAWI_MONTH_2("02", "II"),
	ROMAWI_MONTH_3("03", "III"),
	ROMAWI_MONTH_4("04", "IV"),
	ROMAWI_MONTH_5("05", "V"),
	ROMAWI_MONTH_6("06", "VI"),
	ROMAWI_MONTH_7("07", "VII"),
	ROMAWI_MONTH_8("08", "VIII"),
	ROMAWI_MONTH_9("09", "IX"),
	ROMAWI_MONTH_10("10", "X"),
	ROMAWI_MONTH_11("11", "XI"),
	ROMAWI_MONTH_12("12", "XII");
				
	private String val;
    private String desc;
    
    private InternalRegulationPenerbitanRomawiConstants(String val, String desc) {
        this.val = val;
        this.desc = desc;
    }      

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
    
    
	
}