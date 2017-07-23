/****** IBM Confidential *******
* IoT for Electronics 
*  Copyright IBM Corp. 2016 ****/

package com.ibm.iotcloud.electronics.metering.integration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.wink.json4j.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoAnnotations.Mock;

import com.ibm.iotcloud.electronics.metering.core.CloudantRW;
import com.ibm.iotcloud.electronics.metering.core.MeteringPOST;
import com.ibm.iotcloud.electronics.metering.core.ReportMetering;
import com.ibm.iotcloud.electronics.metering.core.TotalOfDevices;
import com.ibm.iotcloud.electronics.metering.entities.ServiceInstance;
import com.ibm.iotcloud.electronics.metering.entities.ServiceUsage;
import com.ibm.iotcloud.electronics.metering.entities.Submission;
import com.ibm.iotcloud.electronics.metering.entities.TurnOfTheMonth;
import com.ibm.iotcloud.electronics.metering.exceptions.CloudantException;
import com.ibm.iotcloud.electronics.metering.exceptions.CreateJsonException;
import com.ibm.iotcloud.electronics.metering.exceptions.PostException;
import com.ibm.iotcloud.electronics.metering.exceptions.RegistrationException;
import com.ibm.iotcloud.electronics.metering.util.IoTElectronicsResourceBundle;

public class ReportMeteringAllServicesPerRegion {

	private static ResourceBundle systemProperties = IoTElectronicsResourceBundle.getSystemConfigurationBundle();
	private static final String EU_GB = systemProperties.getString("service.metering.region.eu.gb");
	private static final String US_SOUTH = systemProperties.getString("service.metering.region.us.south");
	private static final String AU_SYD = systemProperties.getString("service.metering.region.au.syd");

	@Mock
	private CloudantRW cloudantRWMock;

	@Before
	public void initializeMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldSeparateRegions() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			List<ServiceUsage> usages = listServiceUsageTest();
			List<ServiceInstance> instances = listServiceInstanceTest();

			Mockito.when(cloudantMock.listSubmissionsUsage()).thenReturn(usages);
			Mockito.when(reportSpy.readServices()).thenReturn(instances);
			Mockito.doNothing().when(reportSpy).createListUsageToPOST(instances, true, false);
			Mockito.doNothing().when(reportSpy).separateRegions(Mockito.anyList());
			Mockito.doNothing().when(reportSpy).createJSON();

			reportSpy.separateRegions(true, false);

			Mockito.verify(cloudantMock, Mockito.times(1)).listSubmissionsUsage();
			Mockito.verify(reportSpy, Mockito.times(2)).readServices();
			Mockito.verify(reportSpy, Mockito.times(1)).createListUsageToPOST(instances, true, false);
			Mockito.verify(reportSpy, Mockito.times(1)).separateRegions(Mockito.anyList());
			Mockito.verify(reportSpy, Mockito.times(1)).createJSON();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(expected = CloudantException.class)
	public void shouldSeparateRegionsCloudantException()
			throws CloudantException, RegistrationException, CreateJsonException {
		CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
		MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
		TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

		TurnOfTheMonth turn = new TurnOfTheMonth();
		ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
		ReportMetering reportSpy = Mockito.spy(report);

		Mockito.when(cloudantMock.listSubmissionsUsage()).thenThrow(CloudantException.class);

		reportSpy.separateRegions(true, false);
	}

	@Test(expected = RegistrationException.class)
	public void shouldSeparateRegionsRegistrationException()
			throws CloudantException, RegistrationException, CreateJsonException {
		CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
		MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
		TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

		TurnOfTheMonth turn = new TurnOfTheMonth();
		ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
		ReportMetering reportSpy = Mockito.spy(report);

		List<ServiceInstance> instances = listServiceInstanceTest();
		List<ServiceUsage> usages = listServiceUsageTest();

		Mockito.when(cloudantMock.listSubmissionsUsage()).thenReturn(usages);
		Mockito.when(reportSpy.readServices()).thenReturn(instances);
		Mockito.doThrow(RegistrationException.class).when(reportSpy).createListUsageToPOST(instances, true, false);

		reportSpy.separateRegions(true, false);
	}

	@Test(expected = CreateJsonException.class)
	public void shouldSeparateRegionsCreateJsonException()
			throws CloudantException, RegistrationException, CreateJsonException {
		CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
		MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
		TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

		TurnOfTheMonth turn = new TurnOfTheMonth();
		ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
		ReportMetering reportSpy = Mockito.spy(report);

		List<ServiceInstance> instances = listServiceInstanceTest();
		List<ServiceUsage> usages = listServiceUsageTest();

		Mockito.when(cloudantMock.listSubmissionsUsage()).thenReturn(usages);
		Mockito.when(reportSpy.readServices()).thenReturn(instances);
		Mockito.doNothing().when(reportSpy).createListUsageToPOST(instances, true, false);
		Mockito.doNothing().when(reportSpy).separateRegions(Mockito.anyList());
		Mockito.doThrow(CreateJsonException.class).when(reportSpy).createJSON();

		reportSpy.separateRegions(true, false);
	}

	@Test
	public void shouldReportAllServicesPerRegion() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			JSONObject json = new JSONObject();
			
			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn, json, json, listServiceUsageTestNull());
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.doNothing().when(reportSpy).separateRegions(Mockito.anyBoolean(), Mockito.anyBoolean());
			Submission s = new Submission();
			Mockito.when(meteringPOSTMock.postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(US_SOUTH),
					Mockito.any(JSONObject.class))).thenReturn(s);
			Mockito.when(meteringPOSTMock.postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(EU_GB),
					Mockito.any(JSONObject.class))).thenReturn(s);
			Mockito.when(meteringPOSTMock.postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(AU_SYD),
					Mockito.any(JSONObject.class))).thenReturn(s);
			//Mockito.doNothing().when(reportSpy).updateStatus(Mockito.anyList(), Mockito.eq(s));
			com.cloudant.client.api.model.Response resp = new com.cloudant.client.api.model.Response();
			Mockito.when(cloudantMock.insertLastSubmission(s)).thenReturn(resp);
			Mockito.doNothing().when(cloudantMock).insertLastTotal(Mockito.any(ServiceUsage.class));


			reportSpy.reportMeteringAllServicesPerRegion(true, false);

			Mockito.verify(reportSpy, Mockito.times(1)).separateRegions(Mockito.anyBoolean(), Mockito.anyBoolean());
			Mockito.verify(meteringPOSTMock, Mockito.times(1)).postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(US_SOUTH),
					Mockito.any(JSONObject.class));
			Mockito.verify(meteringPOSTMock, Mockito.times(1)).postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(EU_GB),
					Mockito.any(JSONObject.class));
			Mockito.verify(meteringPOSTMock, Mockito.times(1)).postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(AU_SYD),
					Mockito.any(JSONObject.class));
			Mockito.verify(reportSpy, Mockito.times(3)).updateStatus(Mockito.anyList(), Mockito.eq(s));
			Mockito.verify(cloudantMock, Mockito.times(3)).insertLastSubmission(s);
			Mockito.verify(cloudantMock, Mockito.times(3)).insertLastTotal(Mockito.any(ServiceUsage.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Test
	public void shouldReportAllServicesPerRegionAySydJsonNull() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			JSONObject json = new JSONObject();
			
			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn, json, null, listServiceUsageTest());
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.doNothing().when(reportSpy).separateRegions(Mockito.anyBoolean(), Mockito.anyBoolean());
			Submission s = new Submission();
			Mockito.when(meteringPOSTMock.postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(US_SOUTH),
					Mockito.any(JSONObject.class))).thenReturn(s);
			Mockito.when(meteringPOSTMock.postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(EU_GB),
					Mockito.any(JSONObject.class))).thenReturn(s);
			Mockito.when(meteringPOSTMock.postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(AU_SYD),
					Mockito.any(JSONObject.class))).thenReturn(s);
			Mockito.doNothing().when(reportSpy).updateStatus(Mockito.anyList(), Mockito.eq(s));

			reportSpy.reportMeteringAllServicesPerRegion(true, false);

			Mockito.verify(reportSpy, Mockito.times(1)).separateRegions(Mockito.anyBoolean(), Mockito.anyBoolean());
			Mockito.verify(meteringPOSTMock, Mockito.times(1)).postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(US_SOUTH),
					Mockito.any(JSONObject.class));
			Mockito.verify(meteringPOSTMock, Mockito.times(1)).postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(EU_GB),
					Mockito.any(JSONObject.class));
			Mockito.verify(meteringPOSTMock, Mockito.never()).postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(AU_SYD),
					Mockito.any(JSONObject.class));
			Mockito.verify(reportSpy, Mockito.times(2)).updateStatus(Mockito.anyList(), Mockito.eq(s));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void shouldReportAllServicesPerRegionJsonNull() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			JSONObject json = new JSONObject();
			
			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn, null, null, listServiceUsageTest());
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.doNothing().when(reportSpy).separateRegions(Mockito.anyBoolean(), Mockito.anyBoolean());
			Submission s = new Submission();
			Mockito.when(meteringPOSTMock.postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(US_SOUTH),
					Mockito.any(JSONObject.class))).thenReturn(s);
			Mockito.when(meteringPOSTMock.postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(EU_GB),
					Mockito.any(JSONObject.class))).thenReturn(s);
			Mockito.when(meteringPOSTMock.postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(AU_SYD),
					Mockito.any(JSONObject.class))).thenReturn(s);
			Mockito.doNothing().when(reportSpy).updateStatus(Mockito.anyList(), Mockito.eq(s));

			reportSpy.reportMeteringAllServicesPerRegion(true, false);

			Mockito.verify(reportSpy, Mockito.times(1)).separateRegions(Mockito.anyBoolean(), Mockito.anyBoolean());
			Mockito.verify(meteringPOSTMock, Mockito.never()).postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(US_SOUTH),
					Mockito.any(JSONObject.class));
			Mockito.verify(meteringPOSTMock, Mockito.never()).postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(EU_GB),
					Mockito.any(JSONObject.class));
			Mockito.verify(meteringPOSTMock, Mockito.never()).postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(AU_SYD),
					Mockito.any(JSONObject.class));
			Mockito.verify(reportSpy, Mockito.never()).updateStatus(Mockito.anyList(), Mockito.eq(s));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void shouldReportAllServicesPerRegionPostException() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			JSONObject json = new JSONObject();
			
			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn, json, json, listServiceUsageTest());
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.doNothing().when(reportSpy).separateRegions(Mockito.anyBoolean(), Mockito.anyBoolean());
			Submission s = new Submission();
			Mockito.when(meteringPOSTMock.postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(EU_GB),
					Mockito.any(JSONObject.class))).thenReturn(s);
			Mockito.when(meteringPOSTMock.postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(US_SOUTH),
					Mockito.any(JSONObject.class))).thenThrow(PostException.class);

			reportSpy.reportMeteringAllServicesPerRegion(true, false);

			//PostException occurs to us-south, u-gb (first one) runs ok
			Mockito.verify(reportSpy, Mockito.times(1)).separateRegions(Mockito.anyBoolean(), Mockito.anyBoolean());
			Mockito.verify(meteringPOSTMock, Mockito.times(1)).postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(US_SOUTH),
					Mockito.any(JSONObject.class));
			Mockito.verify(meteringPOSTMock, Mockito.times(1)).postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(EU_GB),
					Mockito.any(JSONObject.class));
			Mockito.verify(meteringPOSTMock, Mockito.never()).postServiceUsageAllServices(Mockito.anyString(), Mockito.eq(AU_SYD),
					Mockito.any(JSONObject.class));
			Mockito.verify(reportSpy, Mockito.times(1)).updateStatus(Mockito.anyList(), Mockito.eq(s));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test (expected = CreateJsonException.class)
	public void shouldReportAllServicesPerRegionCreateJsonException() throws CreateJsonException, IOException, RegistrationException, CloudantException {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			JSONObject json = new JSONObject();
			
			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn, json, json, listServiceUsageTest());
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.doThrow(CreateJsonException.class).when(reportSpy).separateRegions(Mockito.anyBoolean(), Mockito.anyBoolean());

			reportSpy.reportMeteringAllServicesPerRegion(true, false);
	}
	
	@Test
	public void shouldUpdateStatus() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			JSONObject json = new JSONObject();
			
			List<ServiceUsage> usages = listServiceUsageTest();
			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn, json, json, usages);
			ReportMetering reportSpy = Mockito.spy(report);
			
			Submission s = new Submission();
			com.cloudant.client.api.model.Response resp = new com.cloudant.client.api.model.Response();
			Mockito.when(cloudantMock.insertLastSubmission(s)).thenReturn(resp);
			Mockito.doNothing().when(cloudantMock).updateLastTotal(Mockito.any(ServiceUsage.class));

			reportSpy.updateStatus(usages, s);

			Mockito.verify(cloudantMock, Mockito.times(1)).insertLastSubmission(s);
			Mockito.verify(cloudantMock, Mockito.times(3)).updateLastTotal(Mockito.any(ServiceUsage.class));
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void shouldUpdateStatusListNull() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			JSONObject json = new JSONObject();
			
			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn, json, json, listServiceUsageTest());
			ReportMetering reportSpy = Mockito.spy(report);
			
			Submission s = new Submission();
			com.cloudant.client.api.model.Response resp = new com.cloudant.client.api.model.Response();
			Mockito.when(cloudantMock.insertLastSubmission(s)).thenReturn(resp);
			Mockito.doNothing().when(cloudantMock).updateLastTotal(Mockito.any(ServiceUsage.class));

			//If list is null nevel call methods
			reportSpy.updateStatus(null, s);

			Mockito.verify(cloudantMock, Mockito.never()).insertLastSubmission(s);
			Mockito.verify(cloudantMock, Mockito.never()).updateLastTotal(Mockito.any(ServiceUsage.class));
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void shouldUpdateStatusListItemNull() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			JSONObject json = new JSONObject();
			
			List<ServiceUsage> usages = listServiceUsageTest();
			
			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn, json, json, usages);
			ReportMetering reportSpy = Mockito.spy(report);
			
			Submission s = new Submission();
			com.cloudant.client.api.model.Response resp = new com.cloudant.client.api.model.Response();
			Mockito.when(cloudantMock.insertLastSubmission(s)).thenReturn(resp);
			Mockito.doNothing().when(cloudantMock).updateLastTotal(Mockito.any(ServiceUsage.class));

			usages.add(null); // Adding null element
			usages.add(null); // Adding null element
			
			reportSpy.updateStatus(usages, s);

			Mockito.verify(cloudantMock, Mockito.times(1)).insertLastSubmission(s);
			Mockito.verify(cloudantMock, Mockito.times(3)).updateLastTotal(Mockito.any(ServiceUsage.class));
			//There are 5 elements in the list but only call 3 times, because 2 elements are null
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static List<ServiceInstance> listServiceInstanceTest() {
		List<ServiceInstance> services = new ArrayList<>();

		ServiceInstance s = createInstance();
		services.add(s);

		return services;
	}
	

	public static List<ServiceUsage> listServiceUsageTestNull() {
		List<ServiceUsage> services = new ArrayList<>();

		ServiceUsage s = createUsage(US_SOUTH, null);
		services.add(s);

		ServiceUsage s1 = createUsage(EU_GB, null);
		services.add(s1);

		
		ServiceUsage s2 = createUsage(AU_SYD, null);
		services.add(s2);

		return services;
	}

	public static List<ServiceUsage> listServiceUsageTest() {
		List<ServiceUsage> services = new ArrayList<>();

		ServiceUsage s = createUsage(US_SOUTH, "12345");
		services.add(s);

		ServiceUsage s1 = createUsage(EU_GB, "12345");
		services.add(s1);

		
		ServiceUsage s2 = createUsage(AU_SYD, "12345");
		services.add(s2);

		return services;
	}

	private static ServiceUsage createUsage(String region, String revisionId) {
		ServiceUsage s = new ServiceUsage();
		s.setOrganization("myOrgID24");
		s.setSpace("mySpaceID24");
		s.setServiceId("myserviceID1");
		s.setServiceInstanceId("myInstanceID");
		s.setRegion(region);
		s.setIotpOrgId("0923");
		s.setAlreadySentIn1stDay(false);
		s.setLastSubmissionStartDateTime(1470009600000l);
		s.setLastSubmissionEndDateTime(1470010620284l);
		s.setLastSubmissionDateTime(1470010652432l);
		s.setDelta(1l);
		s.setLastSubmissionTotal(1l);
		s.setSubmissionId("mySubmissionID1");
		s.setRevisionId(revisionId);
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
}