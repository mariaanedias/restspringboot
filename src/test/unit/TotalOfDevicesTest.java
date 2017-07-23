package com.ibm.iotcloud.electronics.metering.unit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.iotcloud.electronics.metering.core.TotalOfDevices;
import com.ibm.iotcloud.electronics.metering.entities.ServiceUsage;

public class TotalOfDevicesTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCalculateDelta() {
		ServiceUsage su = new ServiceUsage();
		su.setLastSubmissionTotal(100);
		long delta = TotalOfDevices.delta(su, 120);
		Assert.assertTrue(delta==20);
	}

}
