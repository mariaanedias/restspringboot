package com.ibm.iotcloud.electronics.metering.unit;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.iotcloud.electronics.metering.core.InitMeteringTimer;

public class InitMeteringTimerTest {

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
	public void testInitMeteringTimer00() {
		String hourMinute = "00";
		boolean init = InitMeteringTimer.initTimer(hourMinute);
		assertTrue(init);
	}
	
	@Test
	public void testInitMeteringTimer30() {
		String hourMinute = "30";
		boolean init = InitMeteringTimer.initTimer(hourMinute);
		assertTrue(init);
	}
	
	@Test
	public void testInitMeteringTimer60() {
		String hourMinute = "60";
		boolean init = InitMeteringTimer.initTimer(hourMinute);
		assertTrue(init);
	}
	
	@Test
	public void testInitMeteringTimer90() {
		String hourMinute = "90";
		boolean init = InitMeteringTimer.initTimer(hourMinute);
		assertTrue(init);
	}
	
	@Test
	public void testNotInitMeteringTimer01() {
		String hourMinute = "01";
		boolean init = InitMeteringTimer.initTimer(hourMinute);
		Assert.assertFalse(init);
	}
	
	@Test
	public void testNotInitMeteringTimer31() {
		String hourMinute = "31";
		boolean init = InitMeteringTimer.initTimer(hourMinute);
		Assert.assertFalse(init);
	}
	
	@Test
	public void testNotInitMeteringTimer61() {
		String hourMinute = "61";
		boolean init = InitMeteringTimer.initTimer(hourMinute);
		Assert.assertFalse(init);
	}
	
	@Test
	public void testNotInitMeteringTimer91() {
		String hourMinute = "91";
		boolean init = InitMeteringTimer.initTimer(hourMinute);
		Assert.assertFalse(init);
	}
	
	@Test
	public void testNotInitMeteringTimer89() {
		String hourMinute = "89";
		boolean init = InitMeteringTimer.initTimer(hourMinute);
		Assert.assertFalse(init);
	}
	
	@Test
	public void testNotInitMeteringTimer43() {
		String hourMinute = "43";
		boolean init = InitMeteringTimer.initTimer(hourMinute);
		Assert.assertFalse(init);
	}
}
