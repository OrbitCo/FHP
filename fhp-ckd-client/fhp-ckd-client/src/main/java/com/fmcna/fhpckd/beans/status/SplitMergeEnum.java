package com.fmcna.fhpckd.beans.status;

public enum SplitMergeEnum {
	SPLIT("Split"),
	MERGE("Merge"),
	UNKNOWN("Unknown");

	private final String type;

	private SplitMergeEnum(String type) {
		this.type = type;
	}

	public static SplitMergeEnum fromValue(String value) {
		if (value != null) {
			for (SplitMergeEnum type : values()) {
				if (type.type.equalsIgnoreCase(value)) {
					return type;
				}
			}
		}
		return getDefault();
	}

	public String getType() {
		return this.type;
	}
	
	public static SplitMergeEnum getDefault() {
		return UNKNOWN;
	}

}
