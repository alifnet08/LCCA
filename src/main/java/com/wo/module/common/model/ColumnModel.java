package com.wo.module.common.model;

import java.io.Serializable;
import java.util.List;

public class ColumnModel implements Serializable {

	private static final long serialVersionUID = 3064000061993855409L;
	private int row;
	private String header;
	private String property;
	private List<ColumnModel> columnModels;
	
	public ColumnModel() {
		
	}
	
	public ColumnModel(String header) {
		this.header = header;
	}
	
	public ColumnModel(int row, String header, String property) {
		this.row = row;
		this.header = header;
		this.property = property;
	}

	public ColumnModel(String header, String property) {
		this.header = header;
		this.property = property;
	}

	public String getHeader() {
		return header;
	}

	public String getProperty() {
		return property;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public List<ColumnModel> getColumnModels() {
		return columnModels;
	}

	public void setColumnModels(List<ColumnModel> columnModels) {
		this.columnModels = columnModels;
	}
}
