package com.ibm.iotcloud.electronics.metering.unit;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.ibm.iotcloud.electronics.metering.entities.ServiceUsage;

public class ListServiceUsageMatcher extends TypeSafeMatcher<List<ServiceUsage>> {

	private List<ServiceUsage> usageList;

	public ListServiceUsageMatcher(List<ServiceUsage> usageList) {
		this.usageList = usageList;
	}

	public void describeTo(Description desc) {
		desc.appendText("Check list elements");
	}

	protected boolean matchesSafely(List<ServiceUsage> listUsage) {
		if (listUsage.size() > 0) {
			for (int i = 0; i < listUsage.size(); i++) {
				if (!(listUsage.get(0).getName().equals(usageList.get(0).getName()))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}