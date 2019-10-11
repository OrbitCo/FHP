package com.fmcna.fhpckd.beans.staging;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author vidhishanandhikonda
 *
 * This is a Java bean class that represents the data for Member Split request
 * 
 */
public class MemberSplit {

	private SplitMergeMember original;
	private List<SplitMergeMember> newRows;

	public SplitMergeMember getOriginal() {
		return original;
	}

	public void setOriginal(SplitMergeMember original) {
		this.original = original;
	}

	public List<SplitMergeMember> getNewRows() {
		return newRows;
	}

	public void setNewRows(List<SplitMergeMember> newRow) {
		this.newRows = newRow;
	}
	
	public void addNewRow(SplitMergeMember splitMerge) {
		if (this.newRows == null) {
			this.newRows = new ArrayList<SplitMergeMember>();
		}

		this.newRows.add(splitMerge);
	}

}
