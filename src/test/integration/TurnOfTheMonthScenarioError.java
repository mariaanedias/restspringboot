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
import org.lightcouch.NoDocumentException;
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

public class TurnOfTheMonthScenarioError {

	private static ResourceBundle systemProperties = IoTElectronicsResourceBundle.getSystemConfigurationBundle();
	 private static final String ID_TURN_OF_THE_MONTH = systemProperties.getString("metering.cloudant.turnofthemonth.id");

	@Mock
	private CloudantRW cloudantRWMock;
	
	@Before
	public void initializeMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	// Turn of the month scenario - 1st time => TurnOfTheMonth is null and 1st
	// submission fail = true
	@Test
	public void shouldMonthYearIsNullFail() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setMonthYear(CheckTurnOfTheMonth.getMonthYear());
			turn.setSuccessLast(false);
			turn.setSuccessResetCount(false);
			turn.setSuccessFirst(false);
			turn.setId(ID_TURN_OF_THE_MONTH);

			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.when(cloudantMock.getLastMonth(null)).thenReturn(turn);
			Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(false, true);
			Mockito.doThrow(CloudantException.class).when(cloudantMock)
					.updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));

			boolean b = reportSpy.doTurnOfTheMonthScenario();

			Mockito.verify(cloudantMock, Mockito.times(1)).insertTurOfMonth(Mockito.any(TurnOfTheMonth.class));

			Mockito.verify(cloudantMock, Mockito.times(3)).getLastMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(cloudantMock, Mockito.times(1)).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(reportSpy, Mockito.times(1)).doFirstSubmission(turn.getMonthYear(), false);
			Mockito.verify(reportSpy, Mockito.times(1)).reportMeteringAllServicesPerRegion(false, true);
			Assert.assertTrue(b);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Turn of the month scenario => TurnOfTheMonth is not null and is the same
	// recorded in the database
	// Scenarios have failed, try again -> last succeed, reset fail
	@Test
	public void shouldMonthYearIsNOTNullAndCurrentIsSameOfFromDatabaseResetCountFail() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setMonthYear(CheckTurnOfTheMonth.getMonthYear());

			RegistrationInfo registrationMock = Mockito.mock(RegistrationInfo.class);

			// Test to all false
			turn.setSuccessLast(false);
			turn.setSuccessResetCount(false);
			turn.setSuccessFirst(false);
			turn.setId(ID_TURN_OF_THE_MONTH);

			List<ServiceInstance> services = new ArrayList<>();
			ServiceInstance si = new ServiceInstance();
			si.setUserId("Test");
			si.setIotpOrgId("Test");
			services.add(si);

			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn,
					registrationMock, services);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenReturn(turn);
			Mockito.doNothing().when(cloudantMock).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(false, true);
			Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(true, false);
			Mockito.doThrow(RegistrationException.class).when(registrationMock)
					.resetCount(Mockito.any(ServiceInstance.class), Mockito.any(String.class));

			boolean b = reportSpy.doTurnOfTheMonthScenario();

			// Call reset count again and do 1st submission again, but never
			// call last submission again since it has already succeed
			Mockito.verify(cloudantMock, Mockito.times(2)).getLastMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(reportSpy, Mockito.times(1)).doLastSubmission(turn.getMonthYear(), false);
			Mockito.verify(reportSpy, Mockito.times(1)).resetCount(turn.getMonthYear(), false);
			Mockito.verify(reportSpy, Mockito.times(1)).doFirstSubmission(turn.getMonthYear(), true); // Because
																										// reset
																										// count
																										// fail
			Assert.assertTrue(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Turn of the month scenario - TurnOfTheMonth is not null and is NOT the
	// same recorded in the database
	// Reset Count fail
	@Test
	public void shouldMonthYearIsNOTNullAndCurrentIsNOTSameOfFromDatabaseResetCountFail() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);
			RegistrationInfo registrationMock = Mockito.mock(RegistrationInfo.class);

			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);

			// MonthYear is not the same in the database
			turn.setMonthYear("0000");
			turn.setSuccessLast(true);
			turn.setSuccessResetCount(false);
			turn.setSuccessFirst(false);
			turn.setId(ID_TURN_OF_THE_MONTH);

			List<ServiceInstance> services = new ArrayList<>();
			ServiceInstance si = new ServiceInstance();
			si.setUserId("Test");
			si.setIotpOrgId("Test");
			services.add(si);

			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn,
					registrationMock, services);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenReturn(turn);

			// Mockito.doThrow(CloudantException.class).when(cloudantMock).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(false, true);
			Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(true, false);
			Mockito.doThrow(RegistrationException.class).when(registrationMock)
					.resetCount(Mockito.any(ServiceInstance.class), Mockito.any(String.class));

			boolean b = reportSpy.doTurnOfTheMonthScenario();

			Mockito.verify(cloudantMock, Mockito.times(3)).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(cloudantMock, Mockito.times(3)).getLastMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(reportSpy, Mockito.times(1)).doLastSubmission(turn.getMonthYear(), false);
			Mockito.verify(reportSpy, Mockito.times(1)).resetCount(turn.getMonthYear(), false);
			Mockito.verify(reportSpy, Mockito.times(1)).doFirstSubmission(turn.getMonthYear(), true); // Because
																										// reset
																										// count
																										// returned
																										// error
			Mockito.verify(reportSpy, Mockito.times(1)).reportMeteringAllServicesPerRegion(false, true);
			Mockito.verify(reportSpy, Mockito.times(1)).reportMeteringAllServicesPerRegion(true, false);
			Assert.assertTrue(b);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	// Turn of the month scenario - CloudantException
	@Test(expected = CloudantException.class)
	public void shouldDoTurnOfTheMonthException() throws CloudantException {
		CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
		MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
		TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

		TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
		turn.setMonthYear(CheckTurnOfTheMonth.getMonthYear());
		turn.setSuccessLast(false);
		turn.setSuccessResetCount(false);
		turn.setSuccessFirst(false);
		turn.setId(ID_TURN_OF_THE_MONTH);

		ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
		ReportMetering reportSpy = Mockito.spy(report);

		CloudantException clo = new CloudantException("TestExc");
		Mockito.doThrow(clo).when(cloudantMock).getLastMonth(Mockito.any(TurnOfTheMonth.class));

		reportSpy.doTurnOfTheMonthScenario();
	}
	
	// Turn of the month scenario - CloudantException - NoDocumentException
	@Test
	public void shouldDoTurnOfTheMonthExceptionNoDocument() {

		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setMonthYear(CheckTurnOfTheMonth.getMonthYear());
			turn.setSuccessLast(false);
			turn.setSuccessResetCount(false);
			turn.setSuccessFirst(false);
			turn.setId(ID_TURN_OF_THE_MONTH);

			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			CloudantException clo = new CloudantException("no document", new NoDocumentException("no document"));
			Mockito.doThrow(clo).when(cloudantMock).getLastMonth(Mockito.any(TurnOfTheMonth.class));

			boolean b = reportSpy.doTurnOfTheMonthScenario();

			//After 1st exception, TurnOfTheMonth is set to null, and code runs normally, enter in the else - //1st time - so create register
			Mockito.verify(cloudantMock, Mockito.times(1)).insertTurOfMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(cloudantMock, Mockito.times(3)).getLastMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(cloudantMock, Mockito.times(1)).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(reportSpy, Mockito.times(1)).doFirstSubmission(turn.getMonthYear(), false);
			Mockito.verify(reportSpy, Mockito.times(1)).reportMeteringAllServicesPerRegion(false, true);
			Assert.assertFalse(b);
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains("no document"));
		}
	}
}
