package com.wo.module.common.model;

public class MergedCell {
	private int rowStart;
	private int rowEnd;
	private int colStart;
	private int colEnd;
	private Object id;
	private int idxRow;

	public int getRowStart() {
		return rowStart;
	}

	public void setRowStart(int rowStart) {
		this.rowStart = rowStart;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

	public int getColStart() {
		return colStart;
	}

	public void setColStart(int colStart) {
		this.colStart = colStart;
	}

	public int getColEnd() {
		return colEnd;
	}

	public void setColEnd(int colEnd) {
		this.colEnd = colEnd;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public int getIdxRow() {
		return idxRow;
	}

	public void setIdxRow(int idxRow) {
		this.idxRow = idxRow;
	}
}
