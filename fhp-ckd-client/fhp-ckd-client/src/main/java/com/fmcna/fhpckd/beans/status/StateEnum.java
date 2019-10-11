package com.fmcna.fhpckd.beans.status;


public enum StateEnum {

	IN_PROGRESS("In Progress"),
	COMPLETE("Complete"),
	AWAITING("Awaiting"),
	ERROR("Error"),
	ABORTED("Aborted"),
	UNKNOWN("Unknown");

	private final String state;

	private StateEnum( String state) {
		this.state = state;
	}

	public static StateEnum fromValue(String value) {  
		if (value != null) {  
			for (StateEnum state : values()) {  
				if (state.state.equals(value)) {  
					return state;  
				}  
			}  
		}  
		return getDefault();
	}

	public String getState() {
		return this.state;
	}

	public static StateEnum getDefault() {
		return UNKNOWN;
	}

}
