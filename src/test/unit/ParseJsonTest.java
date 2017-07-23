package com.ibm.iotcloud.electronics.metering.unit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;
import org.hamcrest.Matchers;
import org.hamcrest.beans.SamePropertyValuesAs;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.iotcloud.electronics.metering.entities.ServiceInstance;
import com.ibm.iotcloud.electronics.metering.entities.ServiceUsage;
import com.ibm.iotcloud.electronics.metering.entities.Submission;
import com.ibm.iotcloud.electronics.metering.entities.TurnOfTheMonth;
import com.ibm.iotcloud.electronics.metering.util.ParseJson;


public class ParseJsonTest {

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
	public void testParseJSONServiceInstance() {
		ServiceInstance instance = createInstance();
		String jsonResp = createJsonServiceInstanceResp();
		ServiceInstance instanceParserd=null;
		try {
			instanceParserd = ParseJson.parseJSONServiceInstance(jsonResp);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(instanceParserd);
		Assert.assertThat(instance, SamePropertyValuesAs.samePropertyValuesAs(instanceParserd));
	}

	@Test
	public void testParseJSONIDs() {
		List<String> instance = createIDs();
		String jsonResp = createJsonIDsResp();
		List<String> instanceParserd=null;
		try {
			instanceParserd = ParseJson.parseJSONIDs(jsonResp);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(instanceParserd);
		Assert.assertThat(instance, Matchers.contains("MyID1", "MyID2"));
	}


	@Test
	public void testParseJSONServiceUsage() {
		ServiceUsage instance = createUsage();
		String jsonResp = createJsonServiceUsage();
		ServiceUsage instanceParserd=null;
		try {
			instanceParserd = ParseJson.parseJSONServiceUsage(jsonResp);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(instanceParserd);
		Assert.assertThat(instance, SamePropertyValuesAs.samePropertyValuesAs(instanceParserd));
	}

	@Test
	public void testParseJSONServiceSubmissions() {
		Submission instance = createSubmission();
		String jsonResp = createJsonSubmission();
		Submission instanceParserd=null;
		try {
			instanceParserd = ParseJson.parseJSONServiceSubmissions(jsonResp);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(instanceParserd);
		Assert.assertThat(instance, SamePropertyValuesAs.samePropertyValuesAs(instanceParserd));
	}

	@Test
	public void testParseJSONTurnOfTheMonth() {
		TurnOfTheMonth instance = createTurnOfTheMonth();
		String jsonResp = createJsonTurnOfTheMonth();
		TurnOfTheMonth instanceParserd=null;
		try {
			instanceParserd = ParseJson.parseJSONTurnOfTheMonth(jsonResp);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull(instanceParserd);
		Assert.assertThat(instance, SamePropertyValuesAs.samePropertyValuesAs(instanceParserd));
	}
	
	
	public String createJsonServiceInstanceResp() {
		return "{\"_id\":\"0123\",\"_rev\":\"456\",\"serviceID\":\"abg-7hu-ioi-nnsj\",\"orgID\":\"myOrg\",\"spaceID\":\"mySpace\",\"instanceID\":\"12345678\",\"apiKey\":\"myapikey\",\"authToken\":\"myaythtoken\",\"region\":\"us-south\",\"iotpOrgId\":\"iotporg1\",\"iotpApiKey\":\"iotpkey\",\"iotpAuthToken\":\"iotpauthtoken\",\"iotpHttpHost\":\"host1\"}";
	}
	
	public String createJsonIDsResp() {
		return "{\"total_rows\":563,\"offset\":0,\"rows\":[{\"id\":\"MyID1\",\"key\":\"key1\",\"value\":{\"rev\":\"8-49ba5d7d9e7975aa68da8ab5457f588d\"}},{\"id\":\"MyID2\",\"key\":\"key2\",\"value\":{\"rev\":\"1-776876687\"}}]}";
	}
	
	public String createJsonServiceUsage() {
		return "{\"_id\":\"12345\",\"_rev\":\"12345\",\"serviceID\":\"myserviceID1\",\"orgID\":\"myOrgID24\",\"spaceID\":\"mySpaceID24\",\"instanceID\":\"myInstanceID\",\"startDate\":1470009600000,\"endDate\":1470010620284,\"lastSubmitDate\":1470010652432,\"region\":\"us-south\",\"total\":1,\"delta\":1,\"submissionID\":\"mySubmissionID1\",\"iotpOrgId\":\"0923\",\"sent1stDay\":false}";
	}
	
	public String createJsonSubmission() {
		return "{\"_id\":\"1234\",\"_rev\":\"1-12345\",\"serviceID\":\"myID1\",\"startPeriod\":1467060572976,\"endPeriod\":1467060572976,\"lastSubmitDate\":1467060706332,\"success\":true,\"region\":\"us-south\",\"month\":\"52016\",\"urlLocation\":\"https://myurl1/v1/services/id/usage?region=us-south\", \"json\": {\"service_instances\":[{\"usage\":[{\"start\":1467060572976,\"resources\":[{\"unit\":\"REGISTERED_DEVICE\",\"quantity\":1}],\"end\":1467060572976,\"organization_guid\":\"myGUID\",\"space_guid\":\"mySpace\",\"plan_id\":\"planID1\",\"consumer\":{\"type\":\"cloud-foundry-application\",\"value\":\"value\"}}],\"service_instance_id\":\"myInstanceID\"}]}}";
	}
	
	public String createJsonTurnOfTheMonth() {
		return "{\"_id\":\"myIDTurn1\",\"_rev\":\"10-myRev10\",\"monthYear\":\"92016\",\"successLast\":true,\"successFirst\":true,\"successResetCount\":true}";
	}

	
	public static List<ServiceInstance> listServiceInstanceTest() {
		List<ServiceInstance> services = new ArrayList<>();
		
		ServiceInstance s = createInstance();
		services.add(s);
	
		return services;
	}
	
	public static List<ServiceUsage> listServiceUsageTest() {
		List<ServiceUsage> services = new ArrayList<>();
		
		ServiceUsage s = createUsage();
		services.add(s);

		services.add(s);
	
		return services;
	}

	private static ServiceUsage createUsage() {
		ServiceUsage s = new ServiceUsage();
		s.setOrganization("myOrgID24");
		s.setSpace("mySpaceID24");
		s.setServiceId("myserviceID1");
		s.setServiceInstanceId("myInstanceID");
		s.setRegion("us-south");
		s.setIotpOrgId("0923");
		s.setAlreadySentIn1stDay(false);
		s.setLastSubmissionStartDateTime(1470009600000l);
		s.setLastSubmissionEndDateTime(1470010620284l);
		s.setLastSubmissionDateTime(1470010652432l);
		s.setDelta(1l);
		s.setLastSubmissionTotal(1l);
		s.setSubmissionId("mySubmissionID1");
		s.setRevisionId("12345");
		return s;
	}
	
	private static ServiceInstance createInstance() {
		ServiceInstance s = new ServiceInstance();
		s.setOrganization("myOrg");
		s.setSpace("mySpace");
		s.setServiceId("abg-7hu-ioi-nnsj");
		s.setServiceInstanceId("12345678");
		s.setPassword("passwd");
		s.setRegion("us-south");
		s.setIotpOrgId("iotporg1");
		s.setUserId("myapikey");
		s.setPassword("myaythtoken");		
		return s;
	}
	
	private Submission createSubmission() {
		Submission si = new Submission();
		String jsonS = "{\"service_instances\":[{\"usage\":[{\"start\":1467060572976,\"resources\":[{\"unit\":\"REGISTERED_DEVICE\",\"quantity\":1}],\"end\":1467060572976,\"organization_guid\":\"myGUID\",\"space_guid\":\"mySpace\",\"plan_id\":\"planID1\",\"consumer\":{\"type\":\"cloud-foundry-application\",\"value\":\"value\"}}],\"service_instance_id\":\"myInstanceID\"}]}";		
		si.setId("1234");
		si.setRegion("us-south");
		si.setServiceId("myID1");
		si.setRevisionId("1-12345");
		try {
			si.setJson(new JSONObject(jsonS));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		si.setSuccess(true);
		si.setMonthYear("52016");
		si.setUrlLocation("https://myurl1/v1/services/id/usage?region=us-south");
		si.setLastSubmissionDate(1467060706332l);
		si.setStartPeriod(1467060572976l);
		si.setEndPeriod(1467060572976l);
		return si;
	}
	
	private TurnOfTheMonth createTurnOfTheMonth() {
		TurnOfTheMonth to = new TurnOfTheMonth();
		to.setRev("10-myRev10");
		to.setId("myIDTurn1");
		to.setMonthYear("92016");
		to.setSuccessLast(true);
		to.setSuccessFirst(true);
		to.setSuccessResetCount(true);
		return to;
	}
	
	private List<String> createIDs() {
		List<String> ids = new ArrayList<>();
		ids.add("MyID1");
		ids.add("MyID2");
		return ids;
	}	
}
