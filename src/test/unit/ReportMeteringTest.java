package com.ibm.iotcloud.electronics.metering.unit;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;
import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.iotcloud.electronics.metering.core.ReportMetering;
import com.ibm.iotcloud.electronics.metering.entities.Plan;
import com.ibm.iotcloud.electronics.metering.entities.ServiceInstance;
import com.ibm.iotcloud.electronics.metering.entities.ServiceUsage;
import com.ibm.iotcloud.electronics.metering.exceptions.CreateJsonException;
import com.ibm.iotcloud.electronics.metering.exceptions.PostException;
import com.ibm.iotcloud.electronics.metering.exceptions.RegistrationException;
import com.ibm.iotcloud.electronics.metering.json.CreateMeteringJSON;

public class ReportMeteringTest {

	static List<ServiceUsage> eugbList = null;
	static List<ServiceUsage> ussouthList = null;
	static List<ServiceUsage> ausydList = null;

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

	@SuppressWarnings("unchecked")
	@Test
	public void testSeparateRegions() throws PostException, RegistrationException {
		List<ServiceUsage> submissionsToSend = listServicesTest(false);
		ReportMetering r = new ReportMetering(submissionsToSend);
		r.separateRegions(submissionsToSend);
		assertTrue(r.getServicesEuGb().size() == 2);
		assertTrue(r.getServicesSouth().size() == 2);
		assertTrue(r.getServicesSyd().size() == 2);
		assertTrue(r.getServicesSouth().get(0).getName().equals("Test2"));
		Assert.assertEquals(eugbList, r.getServicesEuGb());
		Assert.assertEquals(ussouthList, r.getServicesSouth());
		Assert.assertEquals(ausydList, r.getServicesSyd());
		
		Assert.assertThat(eugbList,
		contains(
			    HasPropertyWithValue.hasProperty("name", Is.is("Test")), 
			    HasPropertyWithValue.hasProperty("name", Is.is("Test1"))));
		
		//Using a custom matcher
		ListServiceUsageMatcher lsum = new ListServiceUsageMatcher(r.getServicesSouth());
		Assert.assertThat(ussouthList, lsum);
	}
	
	@Test
	public void testCreateMeteringJson() {
		List<ServiceUsage> usages = listServicesTest(true);
		JSONObject jo =null;
		try {
			jo = CreateMeteringJSON.createMeteringJson(usages);
		} catch (CreateJsonException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(jo);
		Assert.assertEquals(jo.toString(), createJsonTest().toString());
	}
	
	@Test
	public void checkDONTHasPreviousUsage() {
		List<ServiceUsage> usages = listServicesTest(true);
		ReportMetering r = new ReportMetering(usages);
		ServiceInstance si = new ServiceInstance();
		si.setServiceInstanceId("id1");
		ServiceUsage sr = r.checkIfHasPreviousUsage(si);
		Assert.assertNull(sr);
	}
	
	@Test
	public void checkHasPreviousUsage() {
		List<ServiceUsage> usages = listServicesTest(true);
		ReportMetering r = new ReportMetering(usages);
		ServiceInstance si = new ServiceInstance();
		si.setServiceInstanceId("abg-7hu-ioi-nnsj-Test");
		ServiceUsage sr = r.checkIfHasPreviousUsage(si);
		Assert.assertNotNull(sr);
		Assert.assertEquals(si.getServiceInstanceId(), sr.getServiceInstanceId());
	}
	
	
	@Test
	public void testCreateMeteringJsonZeroDelta() {
		List<ServiceUsage> usages = listServicesTest(false);
		JSONObject jo =null;
		try {
			jo = CreateMeteringJSON.createMeteringJson(usages);
		} catch (CreateJsonException e) {
			e.printStackTrace();
		}
		Assert.assertNull(jo);
	}

	public static List<ServiceUsage> listServicesTest(boolean delta) {
		List<ServiceUsage> services = new ArrayList<>();
		eugbList = new ArrayList<>();
		ussouthList = new ArrayList<>();
		ausydList = new ArrayList<>();
		
		ServiceUsage s = createUsage("Test", "eu-gb", delta);
		services.add(s);
		eugbList.add(s);

		s = createUsage("Test1", "eu-gb", delta);
		services.add(s);
		eugbList.add(s);

		s = createUsage("Test2", "us-south", delta);
		services.add(s);
		ussouthList.add(s);

		s = createUsage("Test3", "us-south", delta);
		services.add(s);
		ussouthList.add(s);

		s = createUsage("Test4", "au-syd", delta);
		services.add(s);
		ausydList.add(s);

		s = createUsage("Test5", "au-syd", delta);
		services.add(s);
		ausydList.add(s);

		return services;
	}

	private static ServiceUsage createUsage(String name, String region, boolean delta) {
		ServiceUsage s = new ServiceUsage();
		s.setName(name);
		s.setOrganization("iot");
		s.setServiceId("abg-7hu-ioi-nnsj");
		s.setServiceInstanceId("abg-7hu-ioi-nnsj-"+name);
		s.setPassword("passwd");
		s.setPlan(new Plan("Paid Plan"));
		s.setRegion(region);
		s.setUserId("userTest");
		if(delta) s.setDelta(10);
		return s;
	}
	
	private static JSONObject createJsonTest() {
		String jsonString ="{\"service_instances\":[{\"usage\":[{\"start\":0,\"resources\":[{\"unit\":\"REGISTERED_DEVICE\",\"quantity\":10}],\"end\":0,\"organization_guid\":\"iot\",\"space_guid\":null,\"plan_id\":\"Paid Plan\",\"consumer\":{\"type\":\"cloud-foundry-application\",\"value\":\"abg-7hu-ioi-nnsj-Test\"}}],\"service_instance_id\":\"abg-7hu-ioi-nnsj-Test\"},{\"usage\":[{\"start\":0,\"resources\":[{\"unit\":\"REGISTERED_DEVICE\",\"quantity\":10}],\"end\":0,\"organization_guid\":\"iot\",\"space_guid\":null,\"plan_id\":\"Paid Plan\",\"consumer\":{\"type\":\"cloud-foundry-application\",\"value\":\"abg-7hu-ioi-nnsj-Test1\"}}],\"service_instance_id\":\"abg-7hu-ioi-nnsj-Test1\"},{\"usage\":[{\"start\":0,\"resources\":[{\"unit\":\"REGISTERED_DEVICE\",\"quantity\":10}],\"end\":0,\"organization_guid\":\"iot\",\"space_guid\":null,\"plan_id\":\"Paid Plan\",\"consumer\":{\"type\":\"cloud-foundry-application\",\"value\":\"abg-7hu-ioi-nnsj-Test2\"}}],\"service_instance_id\":\"abg-7hu-ioi-nnsj-Test2\"},{\"usage\":[{\"start\":0,\"resources\":[{\"unit\":\"REGISTERED_DEVICE\",\"quantity\":10}],\"end\":0,\"organization_guid\":\"iot\",\"space_guid\":null,\"plan_id\":\"Paid Plan\",\"consumer\":{\"type\":\"cloud-foundry-application\",\"value\":\"abg-7hu-ioi-nnsj-Test3\"}}],\"service_instance_id\":\"abg-7hu-ioi-nnsj-Test3\"},{\"usage\":[{\"start\":0,\"resources\":[{\"unit\":\"REGISTERED_DEVICE\",\"quantity\":10}],\"end\":0,\"organization_guid\":\"iot\",\"space_guid\":null,\"plan_id\":\"Paid Plan\",\"consumer\":{\"type\":\"cloud-foundry-application\",\"value\":\"abg-7hu-ioi-nnsj-Test4\"}}],\"service_instance_id\":\"abg-7hu-ioi-nnsj-Test4\"},{\"usage\":[{\"start\":0,\"resources\":[{\"unit\":\"REGISTERED_DEVICE\",\"quantity\":10}],\"end\":0,\"organization_guid\":\"iot\",\"space_guid\":null,\"plan_id\":\"Paid Plan\",\"consumer\":{\"type\":\"cloud-foundry-application\",\"value\":\"abg-7hu-ioi-nnsj-Test5\"}}],\"service_instance_id\":\"abg-7hu-ioi-nnsj-Test5\"}]}";
		JSONObject json = null;
		try {
			json = new JSONObject(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
}
