package com.example.model;

import com.quanta.async.QuantaBaseModel;

public class School extends QuantaBaseModel{

	private String schoolId;
	private String schoolName;
	
	
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	
	
}
