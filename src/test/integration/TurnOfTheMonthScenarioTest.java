/****** IBM Confidential *******
* IoT for Electronics 
*  Copyright IBM Corp. 2016 ****/

package com.ibm.iotcloud.electronics.metering.integration;

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
import com.ibm.iotcloud.electronics.metering.core.ReportMetering;
import com.ibm.iotcloud.electronics.metering.core.TotalOfDevices;
import com.ibm.iotcloud.electronics.metering.entities.TurnOfTheMonth;
import com.ibm.iotcloud.electronics.metering.util.IoTElectronicsResourceBundle;

public class TurnOfTheMonthScenarioTest {

	private static ResourceBundle systemProperties = IoTElectronicsResourceBundle.getSystemConfigurationBundle();
	 private static final String ID_TURN_OF_THE_MONTH = systemProperties.getString("metering.cloudant.turnofthemonth.id");

	@Mock
	private CloudantRW cloudantRWMock;
	
	@Before
	public void initializeMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	//Turn of the month scenario - 1st time => TurnOfTheMonth is null
	@Test
	public void shouldMonthYearIsNull() {
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
			Mockito.doNothing().when(cloudantMock).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			
			boolean b = reportSpy.doTurnOfTheMonthScenario();
			
			Mockito.verify(cloudantMock, Mockito.times(1)).insertTurOfMonth(Mockito.any(TurnOfTheMonth.class));;
			Mockito.verify(cloudantMock, Mockito.times(3)).getLastMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(cloudantMock, Mockito.times(1)).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(reportSpy, Mockito.times(1)).doFirstSubmission(turn.getMonthYear(), false);
			Mockito.verify(reportSpy, Mockito.times(1)).reportMeteringAllServicesPerRegion(false, true);
			Assert.assertFalse(b);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//Turn of the month scenario => TurnOfTheMonth is not null and is the same recorded in the database, scenarios have failed
	@Test
	public void shouldMonthYearIsNOTNullAndCurrentIsSameOfFromDatabaseFalse() {
		try {
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setMonthYear(CheckTurnOfTheMonth.getMonthYear());
			
			//Test to all false
			turn.setSuccessLast(false);
			turn.setSuccessResetCount(false);
			turn.setSuccessFirst(false);
			turn.setId(ID_TURN_OF_THE_MONTH);

			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenReturn(turn);
			Mockito.doNothing().when(cloudantMock).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(false, true);
			Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(true, false);
			
			boolean b = reportSpy.doTurnOfTheMonthScenario();
			
			Mockito.verify(cloudantMock, Mockito.times(3)).getLastMonth(Mockito.any(TurnOfTheMonth.class));
			Mockito.verify(reportSpy, Mockito.times(1)).doFirstSubmission(turn.getMonthYear(), false);
			Mockito.verify(reportSpy, Mockito.times(1)).doLastSubmission(turn.getMonthYear(), false);
			Mockito.verify(reportSpy, Mockito.times(1)).resetCount(turn.getMonthYear(), false);
			Assert.assertFalse(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
		//Turn of the month scenario => TurnOfTheMonth is not null and is the same recorded in the database, scenarios have succeeded 
		@Test
		public void shouldMonthYearIsNOTNullAndCurrentIsSameOfFromDatabaseTrue() {
			try {
				CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
				MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
				TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

				TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
				turn.setMonthYear(CheckTurnOfTheMonth.getMonthYear());
				
				//Test to all true
				turn.setSuccessLast(true);
				turn.setSuccessResetCount(true);
				turn.setSuccessFirst(true);
				turn.setId(ID_TURN_OF_THE_MONTH);

				ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
				ReportMetering reportSpy = Mockito.spy(report);

				Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenReturn(turn);
				Mockito.doNothing().when(cloudantMock).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
				Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(false, true);
				Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(true, false);
				
				boolean b = reportSpy.doTurnOfTheMonthScenario();
				
				Mockito.verify(cloudantMock, Mockito.times(1)).getLastMonth(Mockito.any(TurnOfTheMonth.class));
				Mockito.verify(reportSpy, Mockito.never()).doFirstSubmission(turn.getMonthYear(), false);
				Mockito.verify(reportSpy, Mockito.never()).doLastSubmission(turn.getMonthYear(), false);
				Mockito.verify(reportSpy, Mockito.never()).resetCount(turn.getMonthYear(), false);
				
				Assert.assertFalse(b);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	
		//Turn of the month scenario => TurnOfTheMonth is not null and is the same recorded in the database, some scenarios have succeeded and some failed
		@Test
		public void shouldMonthYearIsNOTNullAndCurrentIsSameOfFromDatabaseMix() {
			try {
				CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
				MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
				TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

				TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
				turn.setMonthYear(CheckTurnOfTheMonth.getMonthYear());
				
				turn.setSuccessLast(true);
				turn.setSuccessResetCount(false);
				turn.setSuccessFirst(false);
				turn.setId(ID_TURN_OF_THE_MONTH);

				ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
				ReportMetering reportSpy = Mockito.spy(report);

				Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenReturn(turn);
				Mockito.doNothing().when(cloudantMock).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
				Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(false, true);
				Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(true, false);
				
				boolean b = reportSpy.doTurnOfTheMonthScenario();
				
				//Call reset count again and do 1st submission again, but never call last submission again since it has already succeed
				Mockito.verify(cloudantMock, Mockito.times(3)).getLastMonth(Mockito.any(TurnOfTheMonth.class));
				Mockito.verify(reportSpy, Mockito.times(1)).doFirstSubmission(turn.getMonthYear(), false);
				Mockito.verify(reportSpy, Mockito.never()).doLastSubmission(turn.getMonthYear(), false);
				Mockito.verify(reportSpy, Mockito.times(1)).resetCount(turn.getMonthYear(), false);
				Assert.assertFalse(b);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//Turn of the month scenario  => TurnOfTheMonth is not null and is the same recorded in the database
		//Last submission and reset count have succeed, 1st submission have not 
		@Test
		public void shouldMonthYearIsNOTNullAndCurrentIsSameOfFromDatabase1stSubmissionFail() {
			try {
				CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
				MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
				TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

				TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
				turn.setMonthYear(CheckTurnOfTheMonth.getMonthYear());
				
				//Test to all true
				turn.setSuccessLast(true);
				turn.setSuccessResetCount(true);
				turn.setSuccessFirst(false);
				turn.setId(ID_TURN_OF_THE_MONTH);

				ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
				ReportMetering reportSpy = Mockito.spy(report);

				Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenReturn(turn);
				Mockito.doNothing().when(cloudantMock).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
				Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(false, true);
				Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(true, false);
				
				boolean b = reportSpy.doTurnOfTheMonthScenario();
				
				//Call 1st submission again, but never call last submission and reset count again since it has already succeed
				Mockito.verify(cloudantMock, Mockito.times(2)).getLastMonth(Mockito.any(TurnOfTheMonth.class));
				Mockito.verify(reportSpy, Mockito.times(1)).doFirstSubmission(turn.getMonthYear(), false);
				Mockito.verify(reportSpy, Mockito.never()).doLastSubmission(turn.getMonthYear(), false);
				Mockito.verify(reportSpy, Mockito.never()).resetCount(turn.getMonthYear(), false);
				Assert.assertFalse(b);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		
		//Turn of the month scenario - TurnOfTheMonth is not null and is NOT the same recorded in the database
		@Test
		public void shouldMonthYearIsNOTNullAndCurrentIsNOTSameOfFromDatabase() {
			try {
				CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
				MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
				TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

				TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
				
				//MonthYear is not the same in the database
				turn.setMonthYear("0000");
				turn.setSuccessLast(true);
				turn.setSuccessResetCount(true);
				turn.setSuccessFirst(true);
				turn.setId(ID_TURN_OF_THE_MONTH);

				ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
				ReportMetering reportSpy = Mockito.spy(report);

				Mockito.when(cloudantMock.getLastMonth(Mockito.any(TurnOfTheMonth.class))).thenReturn(turn);
				Mockito.doNothing().when(cloudantMock).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
				Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(false, true);
				Mockito.doNothing().when(reportSpy).reportMeteringAllServicesPerRegion(true, false);
				
				boolean b = reportSpy.doTurnOfTheMonthScenario();
				
				Mockito.verify(cloudantMock, Mockito.times(4)).updateTurnOfTheMonth(Mockito.any(TurnOfTheMonth.class));
				Mockito.verify(cloudantMock, Mockito.times(4)).getLastMonth(Mockito.any(TurnOfTheMonth.class));
				Mockito.verify(reportSpy, Mockito.times(1)).doFirstSubmission(turn.getMonthYear(), false);
				Mockito.verify(reportSpy, Mockito.times(1)).doLastSubmission(turn.getMonthYear(), false);
				Mockito.verify(reportSpy, Mockito.times(1)).resetCount(turn.getMonthYear(), false);
				Mockito.verify(reportSpy, Mockito.times(1)).reportMeteringAllServicesPerRegion(false, true);
				Mockito.verify(reportSpy, Mockito.times(1)).reportMeteringAllServicesPerRegion(true, false);
				Assert.assertFalse(b);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
