/****** IBM Confidential *******
* IoT for Electronics 
*  Copyright IBM Corp. 2016 ****/

package com.ibm.iotcloud.electronics.metering.integration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoAnnotations.Mock;

import com.ibm.iotcloud.electronics.metering.core.CheckTurnOfTheMonth;
import com.ibm.iotcloud.electronics.metering.core.CloudantRW;
import com.ibm.iotcloud.electronics.metering.core.MeteringPOST;
import com.ibm.iotcloud.electronics.metering.core.ReportMetering;
import com.ibm.iotcloud.electronics.metering.core.TotalOfDevices;
import com.ibm.iotcloud.electronics.metering.entities.ServiceInstance;
import com.ibm.iotcloud.electronics.metering.entities.ServiceUsage;
import com.ibm.iotcloud.electronics.metering.entities.TurnOfTheMonth;
import com.ibm.iotcloud.electronics.metering.exceptions.CloudantException;
import com.ibm.iotcloud.electronics.metering.exceptions.RegistrationException;
import com.ibm.iotcloud.electronics.metering.util.IoTElectronicsResourceBundle;

public class ReportMeteringUsageTest {

	private static ResourceBundle systemProperties = IoTElectronicsResourceBundle.getSystemConfigurationBundle();

	@Mock
	private CloudantRW cloudantRWMock;

	@Before
	public void initializeMocks() {
		MockitoAnnotations.initMocks(this);
	}

	// List Usage to Post
	@Test
	public void shouldCreateServiceUsageToPostFirst() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.doNothing().when(reportSpy).first(Mockito.any(ServiceUsage.class), Mockito.any(Calendar.class));
			Mockito.doNothing().when(reportSpy).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.doNothing().when(reportSpy).delta(Mockito.any(ServiceUsage.class));

			List<ServiceInstance> instances = listServiceInstanceTest();
			reportSpy.createListUsageToPOST(instances, false, true);

			Mockito.verify(reportSpy, Mockito.times(1)).checkIfHasPreviousUsage(Mockito.any(ServiceInstance.class));
			
			Mockito.verify(reportSpy, Mockito.times(1)).first(Mockito.any(ServiceUsage.class),
					Mockito.any(Calendar.class));
			Mockito.verify(reportSpy, Mockito.never()).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.verify(reportSpy, Mockito.never()).delta(Mockito.any(ServiceUsage.class));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// List Usage to Post
	@Test
	public void shouldCreateServiceUsageToPostLast() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.doNothing().when(reportSpy).first(Mockito.any(ServiceUsage.class), Mockito.any(Calendar.class));
			Mockito.doNothing().when(reportSpy).last(Mockito.any(ServiceUsage.class), Mockito.eq(false));
			Mockito.doNothing().when(reportSpy).delta(Mockito.any(ServiceUsage.class));

			List<ServiceInstance> instances = listServiceInstanceTest();
			reportSpy.createListUsageToPOST(instances, true, false);

			Mockito.verify(reportSpy, Mockito.times(1)).checkIfHasPreviousUsage(Mockito.any(ServiceInstance.class));
			Mockito.verify(reportSpy, Mockito.never()).first(Mockito.any(ServiceUsage.class),
					Mockito.any(Calendar.class));
			Mockito.verify(reportSpy, Mockito.times(1)).last(Mockito.any(ServiceUsage.class), Mockito.eq(false));
			Mockito.verify(reportSpy, Mockito.never()).delta(Mockito.any(ServiceUsage.class));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// List Usage to Post
	@Test
	public void shouldCreateServiceUsageToPostFirstHasPreviousUsage() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			List<ServiceInstance> instances = listServiceInstanceTest();

			Mockito.doNothing().when(reportSpy).first(Mockito.any(ServiceUsage.class),
					Mockito.eq(CheckTurnOfTheMonth.getCalendarFirst()));
			Mockito.doNothing().when(reportSpy).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.doNothing().when(reportSpy).delta(Mockito.any(ServiceUsage.class));
			Mockito.when(reportSpy.checkIfHasPreviousUsage(instances.get(0))).thenReturn(new ServiceUsage());

			reportSpy.createListUsageToPOST(instances, false, true);

			Mockito.verify(reportSpy, Mockito.times(1)).checkIfHasPreviousUsage(Mockito.any(ServiceInstance.class));
			Mockito.verify(reportSpy, Mockito.times(1)).first(Mockito.any(ServiceUsage.class),
					Mockito.eq(CheckTurnOfTheMonth.getCalendarFirst()));
			Mockito.verify(reportSpy, Mockito.never()).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.verify(reportSpy, Mockito.never()).delta(Mockito.any(ServiceUsage.class));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// List Usage to Post
	@Test
	public void shouldCreateServiceUsageToPostLastHasPreviousUsage() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			List<ServiceInstance> instances = listServiceInstanceTest();

			Mockito.doNothing().when(reportSpy).first(Mockito.any(ServiceUsage.class), Mockito.any(Calendar.class));
			Mockito.doNothing().when(reportSpy).last(Mockito.any(ServiceUsage.class), Mockito.eq(true));
			Mockito.doNothing().when(reportSpy).delta(Mockito.any(ServiceUsage.class));
			Mockito.when(reportSpy.checkIfHasPreviousUsage(instances.get(0))).thenReturn(new ServiceUsage());

			reportSpy.createListUsageToPOST(instances, true, false);

			Mockito.verify(reportSpy, Mockito.times(1)).checkIfHasPreviousUsage(Mockito.any(ServiceInstance.class));
			Mockito.verify(reportSpy, Mockito.never()).first(Mockito.any(ServiceUsage.class),
					Mockito.any(Calendar.class));
			Mockito.verify(reportSpy, Mockito.times(1)).last(Mockito.any(ServiceUsage.class), Mockito.eq(true));
			Mockito.verify(reportSpy, Mockito.never()).delta(Mockito.any(ServiceUsage.class));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// List Usage to Post
	@Test
	public void shouldCreateServiceUsageToPostListNull() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.doNothing().when(reportSpy).first(Mockito.any(ServiceUsage.class), Mockito.any(Calendar.class));
			Mockito.doNothing().when(reportSpy).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.doNothing().when(reportSpy).delta(Mockito.any(ServiceUsage.class));

			List<ServiceInstance> instances = new ArrayList<>();
			reportSpy.createListUsageToPOST(instances, false, true);

			Mockito.verify(reportSpy, Mockito.never()).checkIfHasPreviousUsage(Mockito.any(ServiceInstance.class));
			Mockito.verify(reportSpy, Mockito.never()).first(Mockito.any(ServiceUsage.class),
					Mockito.any(Calendar.class));
			Mockito.verify(reportSpy, Mockito.never()).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.verify(reportSpy, Mockito.never()).delta(Mockito.any(ServiceUsage.class));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// List Usage to Post
	@Test
	public void shouldCreateServiceUsageToPostNullUserID() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.doNothing().when(reportSpy).first(Mockito.any(ServiceUsage.class), Mockito.any(Calendar.class));
			Mockito.doNothing().when(reportSpy).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.doNothing().when(reportSpy).delta(Mockito.any(ServiceUsage.class));

			List<ServiceInstance> instances = new ArrayList<>();
			ServiceInstance si = new ServiceInstance();
			si.setIotpOrgId("test");
			instances.add(si);

			reportSpy.createListUsageToPOST(instances, false, true);

			Mockito.verify(reportSpy, Mockito.never()).checkIfHasPreviousUsage(Mockito.any(ServiceInstance.class));
			Mockito.verify(reportSpy, Mockito.never()).first(Mockito.any(ServiceUsage.class),
					Mockito.any(Calendar.class));
			Mockito.verify(reportSpy, Mockito.never()).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.verify(reportSpy, Mockito.never()).delta(Mockito.any(ServiceUsage.class));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// List Usage to Post
	@Test
	public void shouldCreateServiceUsageToPostNullIoTPOrg() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.doNothing().when(reportSpy).first(Mockito.any(ServiceUsage.class), Mockito.any(Calendar.class));
			Mockito.doNothing().when(reportSpy).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.doNothing().when(reportSpy).delta(Mockito.any(ServiceUsage.class));

			List<ServiceInstance> instances = new ArrayList<>();
			ServiceInstance si = new ServiceInstance();
			si.setUserId("test");
			instances.add(si);

			reportSpy.createListUsageToPOST(instances, false, true);

			Mockito.verify(reportSpy, Mockito.never()).checkIfHasPreviousUsage(Mockito.any(ServiceInstance.class));
			Mockito.verify(reportSpy, Mockito.never()).first(Mockito.any(ServiceUsage.class),
					Mockito.any(Calendar.class));
			Mockito.verify(reportSpy, Mockito.never()).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.verify(reportSpy, Mockito.never()).delta(Mockito.any(ServiceUsage.class));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// List Usage to Post
	@Test
	public void shouldCreateServiceUsageToPostAllFalse() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.doNothing().when(reportSpy).first(Mockito.any(ServiceUsage.class), Mockito.any(Calendar.class));
			Mockito.doNothing().when(reportSpy).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.doNothing().when(reportSpy).delta(Mockito.any(ServiceUsage.class));

			List<ServiceInstance> instances = listServiceInstanceTest();

			// If both false and has not previous usage, do 1st
			reportSpy.createListUsageToPOST(instances, false, false);

			Mockito.verify(reportSpy, Mockito.times(1)).checkIfHasPreviousUsage(Mockito.any(ServiceInstance.class));
			Mockito.verify(reportSpy, Mockito.times(1)).first(Mockito.any(ServiceUsage.class),
					Mockito.any(Calendar.class));
			Mockito.verify(reportSpy, Mockito.never()).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.verify(reportSpy, Mockito.never()).delta(Mockito.any(ServiceUsage.class));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// List Usage to Post
	@Test
	public void shouldCreateServiceUsageToPostAllTrue() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.doNothing().when(reportSpy).first(Mockito.any(ServiceUsage.class), Mockito.any(Calendar.class));
			Mockito.doNothing().when(reportSpy).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.doNothing().when(reportSpy).delta(Mockito.any(ServiceUsage.class));

			List<ServiceInstance> instances = listServiceInstanceTest();

			// If both true and has not previous usage, do last
			reportSpy.createListUsageToPOST(instances, true, true);

			Mockito.verify(reportSpy, Mockito.times(1)).checkIfHasPreviousUsage(Mockito.any(ServiceInstance.class));
			Mockito.verify(reportSpy, Mockito.never()).first(Mockito.any(ServiceUsage.class),
					Mockito.any(Calendar.class));
			Mockito.verify(reportSpy, Mockito.times(1)).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.verify(reportSpy, Mockito.never()).delta(Mockito.any(ServiceUsage.class));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// List Usage to Post
	@Test
	public void shouldCreateServiceUsageToPostAllFalseHasPreviousUsage() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			List<ServiceInstance> instances = listServiceInstanceTest();

			Mockito.doNothing().when(reportSpy).first(Mockito.any(ServiceUsage.class), Mockito.any(Calendar.class));
			Mockito.doNothing().when(reportSpy).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.doNothing().when(reportSpy).delta(Mockito.any(ServiceUsage.class));
			Mockito.when(reportSpy.checkIfHasPreviousUsage(instances.get(0))).thenReturn(new ServiceUsage());

			// If both false and has previous usage, do delta
			reportSpy.createListUsageToPOST(instances, false, false);

			Mockito.verify(reportSpy, Mockito.times(1)).checkIfHasPreviousUsage(Mockito.any(ServiceInstance.class));
			Mockito.verify(reportSpy, Mockito.never()).first(Mockito.any(ServiceUsage.class),
					Mockito.any(Calendar.class));
			Mockito.verify(reportSpy, Mockito.never()).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.verify(reportSpy, Mockito.times(1)).delta(Mockito.any(ServiceUsage.class));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// List Usage to Post
	@Test
	public void shouldCreateServiceUsageToPostAllTrueHasPreviousUsage() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth();
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			List<ServiceInstance> instances = listServiceInstanceTest();

			Mockito.doNothing().when(reportSpy).first(Mockito.any(ServiceUsage.class), Mockito.any(Calendar.class));
			Mockito.doNothing().when(reportSpy).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.doNothing().when(reportSpy).delta(Mockito.any(ServiceUsage.class));
			Mockito.when(reportSpy.checkIfHasPreviousUsage(instances.get(0))).thenReturn(new ServiceUsage());

			// If both true and has previous usage, do last
			reportSpy.createListUsageToPOST(instances, true, true);

			Mockito.verify(reportSpy, Mockito.times(1)).checkIfHasPreviousUsage(Mockito.any(ServiceInstance.class));
			Mockito.verify(reportSpy, Mockito.never()).first(Mockito.any(ServiceUsage.class),
					Mockito.any(Calendar.class));
			Mockito.verify(reportSpy, Mockito.times(1)).last(Mockito.any(ServiceUsage.class), Mockito.anyBoolean());
			Mockito.verify(reportSpy, Mockito.never()).delta(Mockito.any(ServiceUsage.class));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// List Usage to Post
	@Test(expected = CloudantException.class)
	public void shouldCreateServiceUsageToPostCloudantException() throws CloudantException, RegistrationException {
		CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
		MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
		TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

		TurnOfTheMonth turn = new TurnOfTheMonth();
		ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
		ReportMetering reportSpy = Mockito.spy(report);

		List<ServiceInstance> instances = listServiceInstanceTest();

		Mockito.doThrow(CloudantException.class).when(reportSpy).first(Mockito.any(ServiceUsage.class), Mockito.any(Calendar.class));

		// If both true and has previous usage, do last
		reportSpy.createListUsageToPOST(instances, false, true);
	}
	
	// List Usage to Post
	@Test(expected = RegistrationException.class)
	public void shouldCreateServiceUsageToPostRegistrationException() throws CloudantException, RegistrationException {
		CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
		MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
		TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

		TurnOfTheMonth turn = new TurnOfTheMonth();
		ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
		ReportMetering reportSpy = Mockito.spy(report);

		List<ServiceInstance> instances = listServiceInstanceTest();

		Mockito.doThrow(RegistrationException.class).when(reportSpy).last(Mockito.any(ServiceUsage.class),
				Mockito.anyBoolean());

		// If both true and has previous usage, do last
		reportSpy.createListUsageToPOST(instances, true, false);
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
