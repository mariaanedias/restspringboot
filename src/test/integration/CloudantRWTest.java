/****** IBM Confidential *******
* IoT for Electronics 
*  Copyright IBM Corp. 2016 ****/

package com.ibm.iotcloud.electronics.metering.integration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hamcrest.beans.SamePropertyValuesAs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoAnnotations.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cloudant.client.api.Database;
import com.ibm.iotcloud.electronics.metering.core.CloudantRW;
import com.ibm.iotcloud.electronics.metering.entities.ServiceInstance;
import com.ibm.iotcloud.electronics.metering.util.Authenticate;
import com.ibm.iotcloud.electronics.metering.util.IoTElectronicsResourceBundle;
import com.ibm.iotcloud.electronics.metering.util.ParseJson;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ParseJson.class })
public class CloudantRWTest {

	private static ResourceBundle cloudantProperties = IoTElectronicsResourceBundle.getCloudantConfigurationBundle();

	@Mock
	private CloudantRW cloudantRWMock;

	@Mock
	private Database db1;

	private static final String USER_DB = cloudantProperties.getString("cloudant.client.user");
	private static final String PASS_DB = cloudantProperties.getString("cloudant.client.password");
	private static final int PORT = Integer.parseInt(cloudantProperties.getString("url.cloudant.port"));
	private static final String PROTOCOL = cloudantProperties.getString("url.cloudant.protocol");
	private static String URL_USAGE_HOST = cloudantProperties.getString("url.cloudant.list.usage.host");
	private static final String URL_LIST_SERVICE = cloudantProperties.getString("url.cloudant.list.services");

	@Before
	public void initializeMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldGetIds() {
		try {
			Authenticate au = Mockito.mock(Authenticate.class);
			db1 = Mockito.mock(Database.class);

			CloudantRW cloudant = new CloudantRW(au, au, db1, db1, db1);
			CloudantRW cloudantSpy = Mockito.spy(cloudant);

			String idsStr = createJsonIDsResp();

			List<String> instances = ParseJson.parseJSONIDs(idsStr);

			Mockito.when(cloudantSpy.getAuthenticationUsage()).thenReturn(au);
			Mockito.when(au.doGet(URL_LIST_SERVICE, USER_DB, PASS_DB, URL_USAGE_HOST, PORT, PROTOCOL))
					.thenReturn(idsStr);

			List<String> instancesS = cloudantSpy.getIDs(URL_LIST_SERVICE, 1);

			Mockito.verify(cloudantSpy, Mockito.times(1)).getAuthenticationUsage();
			Mockito.verify(cloudantSpy, Mockito.times(1)).getIDs(Mockito.anyString(), Mockito.anyInt());
			Assert.assertThat(instancesS, SamePropertyValuesAs.samePropertyValuesAs(instances));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void shouldGetServiceInstances() {
		try {
			Authenticate au = Mockito.mock(Authenticate.class);
			db1 = Mockito.mock(Database.class);

			CloudantRW cloudant = new CloudantRW(au, au, db1, db1, db1);
			CloudantRW cloudantSpy = Mockito.spy(cloudant);

			String idsStr = createJsonServiceInstanceResp();
			String idStrIDs = createJsonIDsResp();

			List<String> instances = ParseJson.parseJSONIDs(idStrIDs);

			ServiceInstance se = ParseJson.parseJSONServiceInstance(createJsonServiceInstanceResp());
			List<ServiceInstance> instancesList = new ArrayList<>();
			instancesList.add(se);

			Mockito.when(cloudantSpy.getDB()).thenReturn(db1);
			InputStream input = new ByteArrayInputStream(idsStr.getBytes());
			Mockito.when(db1.find(Mockito.anyString())).thenReturn(input);

			//Stub the ParseJson static call inside CloudantRW class
			PowerMockito.stub(PowerMockito.method(ParseJson.class, "parseJSONServiceInstance")).toReturn(se);

			List<ServiceInstance> instancesS = cloudantSpy.getServiceInstances(instances);
			Mockito.verify(cloudantSpy, Mockito.times(1)).getDB();
			// 1 ID
			Mockito.verify(db1, Mockito.times(1)).find(Mockito.anyString());
			PowerMockito.verifyStatic(Mockito.times(1));
			Assert.assertThat(instancesS, SamePropertyValuesAs.samePropertyValuesAs(instancesList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void shouldListServices() {
		try {
			Authenticate au = Mockito.mock(Authenticate.class);
			db1 = Mockito.mock(Database.class);

			CloudantRW cloudant = new CloudantRW(au, au, db1, db1, db1);
			CloudantRW cloudantSpy = Mockito.spy(cloudant);

			String idsStr = createJsonServiceInstanceResp();
			String idStrIDs = createJsonIDsResp();

			List<String> instances = ParseJson.parseJSONIDs(idStrIDs);

			ServiceInstance se = ParseJson.parseJSONServiceInstance(createJsonServiceInstanceResp());
			List<ServiceInstance> instancesList = new ArrayList<>();
			instancesList.add(se);

			Mockito.when(cloudantSpy.getAuthenticationUsage()).thenReturn(au);
			Mockito.when(au.doGet(URL_LIST_SERVICE, USER_DB, PASS_DB, URL_USAGE_HOST, PORT, PROTOCOL))
					.thenReturn(idsStr);
			Mockito.when(cloudantSpy.getDB()).thenReturn(db1);
			InputStream input = new ByteArrayInputStream(idsStr.getBytes());
			Mockito.when(db1.find(Mockito.anyString())).thenReturn(input);
			
			//Stub the ParseJson static calls inside CloudantRW class
			PowerMockito.stub(PowerMockito.method(ParseJson.class, "parseJSONIDs")).toReturn(instances);
			PowerMockito.stub(PowerMockito.method(ParseJson.class, "parseJSONServiceInstance")).toReturn(se);
			
			List<ServiceInstance> instancesS = cloudantSpy.listServices();
			
			Mockito.verify(cloudantSpy, Mockito.times(1)).getAuthenticationUsage();
			Mockito.verify(cloudantSpy, Mockito.times(1)).getIDs(Mockito.anyString(), Mockito.anyInt());
			Mockito.verify(cloudantSpy, Mockito.times(1)).getDB();
			// 1 ID
			Mockito.verify(db1, Mockito.times(1)).find(Mockito.anyString());
			Assert.assertThat(instancesS, SamePropertyValuesAs.samePropertyValuesAs(instancesList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String createJsonServiceInstanceResp() {
		return "{\"_id\":\"0123\",\"_rev\":\"456\",\"serviceID\":\"abg-7hu-ioi-nnsj\",\"orgID\":\"myOrg\",\"spaceID\":\"mySpace\",\"instanceID\":\"12345678\",\"apiKey\":\"myapikey\",\"authToken\":\"myaythtoken\",\"region\":\"us-south\",\"iotpOrgId\":\"iotporg1\",\"iotpApiKey\":\"iotpkey\",\"iotpAuthToken\":\"iotpauthtoken\",\"iotpHttpHost\":\"host1\"}";
	}

	public String createJsonIDsResp() {
		return "{\"total_rows\":563,\"offset\":0,\"rows\":[{\"id\":\"MyID1\",\"key\":\"key1\",\"value\":{\"rev\":\"8-49ba5d7d9e7975aa68da8ab5457f588d\"}}]}";
	}

	public static List<ServiceInstance> listServiceInstanceTest() {
		List<ServiceInstance> services = new ArrayList<>();

		ServiceInstance s = createInstance();
		services.add(s);

		return services;
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
}