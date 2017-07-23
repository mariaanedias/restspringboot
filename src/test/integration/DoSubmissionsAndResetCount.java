/****** IBM Confidential *******
* IoT for Electronics 
*  Copyright IBM Corp. 2016 ****/

package com.ibm.iotcloud.electronics.metering.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoAnnotations.Mock;

import com.ibm.iotcloud.electronics.metering.core.CheckTurnOfTheMonth;
import com.ibm.iotcloud.electronics.metering.core.CloudantRW;
import com.ibm.iotcloud.electronics.metering.core.MeteringPOST;
import com.ibm.iotcloud.electronics.metering.core.RegistrationInfo;
import com.ibm.iotcloud.electronics.metering.core.ReportMetering;
import com.ibm.iotcloud.electronics.metering.core.TotalOfDevices;
import com.ibm.iotcloud.electronics.metering.entities.ServiceInstance;
import com.ibm.iotcloud.electronics.metering.entities.TurnOfTheMonth;
import com.ibm.iotcloud.electronics.metering.exceptions.CloudantException;
import com.ibm.iotcloud.electronics.metering.exceptions.RegistrationException;
import com.ibm.iotcloud.electronics.metering.util.IoTElectronicsResourceBundle;

public class DoSubmissionsAndResetCount {

	private static ResourceBundle systemProperties = IoTElectronicsResourceBundle.getSystemConfigurationBundle();
	private static final String ID_TURN_OF_THE_MONTH = systemProperties
			.getString("metering.cloudant.turnofthemonth.id");

	@Mock
	private CloudantRW cloudantRWMock;

	@Before
	public void initializeMocks() {
		MockitoAnnotations.initMocks(this);
	}

	// Do 1st submission
	@Test
	public void shouldDo1stSubmissionOk() {
		try {

			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setId(ID_TURN_OF_THE_MONTH);

			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenReturn(turn);
			Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(false, true);
			Mockito.doNothing().when(cloudantMock).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));

			boolean b = reportSpy.doFirstSubmission(turn.getMonthYear(), false);

			Mockito.verify(cloudantMock, Mockito.times(1)).getLastMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(cloudantMock, Mockito.times(1)).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			;
			Mockito.verify(reportSpy, Mockito.times(1)).reportMeteringAllServicesPerRegion(false, true);
			Assert.assertFalse(b);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Do 1st submission fail
	@Test
	public void shouldDo1stSubmissionFail() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setMonthYear(CheckTurnOfTheMonth.getMonthYear());
			turn.setId(ID_TURN_OF_THE_MONTH);

			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenReturn(turn);
			Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(false, true);
			Mockito.doThrow(CloudantException.class).when(cloudantMock)
					.updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));

			boolean b = reportSpy.doFirstSubmission(turn.getMonthYear(), false);

			Assert.assertTrue(b);
		} catch (Exception e) {

		}
	}

	// Do Last submission
	@Test
	public void shouldDoLastSubmissionOk() {
		try {

			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setId(ID_TURN_OF_THE_MONTH);

			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenReturn(turn);
			Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(true, false);
			Mockito.doNothing().when(cloudantMock).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));

			boolean b = reportSpy.doLastSubmission(turn.getMonthYear(), false);

			Mockito.verify(cloudantMock, Mockito.times(1)).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			;
			Mockito.verify(reportSpy, Mockito.times(1)).reportMeteringAllServicesPerRegion(true, false);
			Assert.assertFalse(b);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Do Last submission fail
	@Test
	public void shouldDoLastSubmissionFail() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setMonthYear(CheckTurnOfTheMonth.getMonthYear());
			turn.setId(ID_TURN_OF_THE_MONTH);

			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenReturn(turn);
			Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(true, false);
			Mockito.doThrow(CloudantException.class).when(cloudantMock)
					.updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));

			boolean b = reportSpy.doLastSubmission(turn.getMonthYear(), false);

			Assert.assertTrue(b);
		} catch (Exception e) {

		}
	}

	// Do Reset Count
	@Test
	public void shouldDoResetCountOk() {
		try {

			List<ServiceInstance> services = new ArrayList<>();
			ServiceInstance si = new ServiceInstance();
			si.setUserId("Test");
			si.setIotpOrgId("Test");
			services.add(si);

			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);
			RegistrationInfo registrationMock = Mockito.mock(RegistrationInfo.class);

			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setId(ID_TURN_OF_THE_MONTH);

			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn,
					registrationMock, services);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenReturn(turn);
			Mockito.doNothing().when(cloudantMock).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.doNothing().when(registrationMock).resetCount(Mockito.any(ServiceInstance.class),
					Mockito.any(String.class));

			boolean b = reportSpy.resetCount(turn.getMonthYear(), false);

			Mockito.verify(reportSpy, Mockito.never()).readServices();
			Mockito.verify(cloudantMock, Mockito.times(1)).getLastMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(cloudantMock, Mockito.times(1)).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Assert.assertFalse(b);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Do Reset Count
	@Test
	public void shouldDoResetCountOkFailTrue() {
		try {

			List<ServiceInstance> services = new ArrayList<>();
			ServiceInstance si = new ServiceInstance();
			si.setUserId("Test");
			si.setIotpOrgId("Test");
			services.add(si);

			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);
			RegistrationInfo registrationMock = Mockito.mock(RegistrationInfo.class);

			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setId(ID_TURN_OF_THE_MONTH);

			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn,
					registrationMock, services);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenReturn(turn);
			Mockito.doNothing().when(cloudantMock).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.doNothing().when(registrationMock).resetCount(Mockito.any(ServiceInstance.class),
					Mockito.any(String.class));

			boolean b = reportSpy.resetCount(turn.getMonthYear(), true);

			Mockito.verify(reportSpy, Mockito.never()).readServices();
			Mockito.verify(cloudantMock, Mockito.never()).getLastMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(cloudantMock, Mockito.never()).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Assert.assertTrue(b);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Do Reset Count
	@Test
	public void shouldDoResetCountOkServicesNull() {
		try {

			List<ServiceInstance> services = null;

			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);
			RegistrationInfo registrationMock = Mockito.mock(RegistrationInfo.class);

			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setId(ID_TURN_OF_THE_MONTH);

			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn,
					registrationMock, services);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenReturn(turn);
			Mockito.doNothing().when(cloudantMock).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.doNothing().when(registrationMock).resetCount(Mockito.any(ServiceInstance.class),
					Mockito.any(String.class));
			Mockito.when(cloudantMock.listServices()).thenReturn(new ArrayList<ServiceInstance>());

			boolean b = reportSpy.resetCount(turn.getMonthYear(), false);

			Mockito.verify(cloudantMock, Mockito.times(1)).listServices();
			Mockito.verify(reportSpy, Mockito.times(1)).readServices();
			Mockito.verify(registrationMock, Mockito.never()).resetCount(Mockito.any(ServiceInstance.class),
					Mockito.anyString());
			Mockito.verify(cloudantMock, Mockito.times(1)).getLastMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(cloudantMock, Mockito.times(1)).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Assert.assertFalse(b);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Do Reset Count
	@Test
	public void shouldDoResetCountOkServicesNOTNull() {
		try {

			List<ServiceInstance> services = new ArrayList<>();
			ServiceInstance si = new ServiceInstance();
			si.setUserId("Test");
			si.setIotpOrgId("Test");
			services.add(si);

			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);
			RegistrationInfo registrationMock = Mockito.mock(RegistrationInfo.class);

			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setId(ID_TURN_OF_THE_MONTH);

			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn,
					registrationMock, services);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenReturn(turn);
			Mockito.doNothing().when(cloudantMock).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.doNothing().when(registrationMock).resetCount(Mockito.any(ServiceInstance.class),
					Mockito.any(String.class));
			Mockito.when(cloudantMock.listServices()).thenReturn(services);

			boolean b = reportSpy.resetCount(turn.getMonthYear(), false);

			Mockito.verify(cloudantMock, Mockito.never()).listServices();
			Mockito.verify(reportSpy, Mockito.never()).readServices();
			Mockito.verify(registrationMock, Mockito.times(1)).resetCount(Mockito.any(ServiceInstance.class),
					Mockito.anyString());
			Mockito.verify(cloudantMock, Mockito.times(1)).getLastMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(cloudantMock, Mockito.times(1)).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Assert.assertFalse(b);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Do Reset Count
	@Test
	public void shouldDoResetCountOkListServicesEmpty() {
		try {

			List<ServiceInstance> services = new ArrayList<>();

			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);
			RegistrationInfo registrationMock = Mockito.mock(RegistrationInfo.class);

			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setId(ID_TURN_OF_THE_MONTH);

			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn,
					registrationMock, services);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenReturn(turn);
			Mockito.doNothing().when(cloudantMock).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.doNothing().when(registrationMock).resetCount(Mockito.any(ServiceInstance.class),
					Mockito.any(String.class));
			Mockito.when(cloudantMock.listServices()).thenReturn(services);

			boolean b = reportSpy.resetCount(turn.getMonthYear(), false);

			Mockito.verify(cloudantMock, Mockito.times(1)).listServices();
			Mockito.verify(reportSpy, Mockito.times(1)).readServices();
			Mockito.verify(registrationMock, Mockito.never()).resetCount(Mockito.any(ServiceInstance.class),
					Mockito.anyString()); //Never calls here because service is an empty list
			Mockito.verify(cloudantMock, Mockito.times(1)).getLastMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(cloudantMock, Mockito.times(1)).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Assert.assertFalse(b);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	//Do Reset Count
	@Test
	public void shouldDoResetCountCloudantException() {
		try {
			
			List<ServiceInstance> services = new ArrayList<>();
			ServiceInstance si = new ServiceInstance();
			si.setUserId("Test");
			si.setIotpOrgId("Test");
			services.add(si);

			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);
			RegistrationInfo registrationMock = Mockito.mock(RegistrationInfo.class);

			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setId(ID_TURN_OF_THE_MONTH);

			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn,
					registrationMock, services);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenThrow(CloudantException.class);
			Mockito.doNothing().when(cloudantMock).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.doNothing().when(registrationMock).resetCount(Mockito.any(ServiceInstance.class), Mockito.any(String.class));
			
			boolean b = reportSpy.resetCount(turn.getMonthYear(), false);
			
			Mockito.verify(reportSpy, Mockito.never()).readServices();
			Mockito.verify(cloudantMock, Mockito.times(1)).getLastMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(cloudantMock, Mockito.never()).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(registrationMock, Mockito.times(1)).resetCount(Mockito.any(ServiceInstance.class), Mockito.anyString());
			Assert.assertTrue(b);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Do Reset Count
	@Test
	public void shouldDoResetCountRegistrationException() {
		try {
			
			List<ServiceInstance> services = new ArrayList<>();
			ServiceInstance si = new ServiceInstance();
			si.setUserId("Test");
			si.setIotpOrgId("Test");
			services.add(si);
			ServiceInstance si1 = new ServiceInstance();
			si.setUserId("Test1");
			si.setIotpOrgId("Test1");
			services.add(si1);

			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);
			RegistrationInfo registrationMock = Mockito.mock(RegistrationInfo.class);

			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setId(ID_TURN_OF_THE_MONTH);

			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn,
					registrationMock, services);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.doThrow(RegistrationException.class).when(registrationMock).resetCount(Mockito.any(ServiceInstance.class), Mockito.any(String.class));
			
			boolean b = reportSpy.resetCount(turn.getMonthYear(), false);
			
			Mockito.verify(reportSpy, Mockito.never()).readServices();
			Mockito.verify(registrationMock, Mockito.times(1)).resetCount(Mockito.any(ServiceInstance.class), Mockito.anyString());
			Mockito.verify(cloudantMock, Mockito.never()).getLastMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(cloudantMock, Mockito.never()).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Assert.assertTrue(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
