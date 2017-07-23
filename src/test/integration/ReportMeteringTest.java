/****** IBM Confidential *******
* IoT for Electronics 
*  Copyright IBM Corp. 2016 ****/

package com.ibm.iotcloud.electronics.metering.integration;

import java.io.IOException;
import java.util.ArrayList;
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
import com.ibm.iotcloud.electronics.metering.entities.Submission;
import com.ibm.iotcloud.electronics.metering.entities.TurnOfTheMonth;
import com.ibm.iotcloud.electronics.metering.exceptions.CloudantException;
import com.ibm.iotcloud.electronics.metering.exceptions.PostException;
import com.ibm.iotcloud.electronics.metering.util.IoTElectronicsResourceBundle;

public class ReportMeteringTest {

	private static ResourceBundle systemProperties = IoTElectronicsResourceBundle.getSystemConfigurationBundle();
    private static final String ID_TURN_OF_THE_MONTH = systemProperties.getString("metering.cloudant.turnofthemonth.id");

	
	@Mock
	private CloudantRW cloudantRWMock;
	
	@Before
	public void initializeMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldGenerateReport() {
		try {
			
			List<Submission> subs =  listSubmissions();
			
			//Cloudant			
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);
			
			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setMonthYear(CheckTurnOfTheMonth.getMonthYear());
			
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);
			
			Mockito.when(cloudantMock.listSubmissions()).thenReturn(subs);
			Mockito.when(cloudantMock.findSubmissionsFailed(subs)).thenReturn(subs);
			Mockito.when(cloudantMock.getLastMonth(null)).thenReturn(turn);
			
			Mockito.when(meteringPOSTMock.postServiceUsageAllServices(null, null, null)).thenReturn(subs.get(0));
			
			//Fail return false
			Mockito.doReturn(false).when(reportSpy).doTurnOfTheMonthScenario();
			reportSpy.generateReport();
			
			Mockito.verify(cloudantMock, Mockito.times(1)).listSubmissions();
			Mockito.verify(cloudantMock, Mockito.times(1)).findSubmissionsFailed(subs);
			Mockito.verify(meteringPOSTMock, Mockito.times(4)).postServiceUsageAllServices(null, null, null);
			Mockito.verify(cloudantMock, Mockito.times(4)).updateLastSubmission(subs.get(0));
			Mockito.verify(reportSpy, Mockito.times(1)).doTurnOfTheMonthScenario();
			
			//Call normal submission
			Mockito.verify(reportSpy, Mockito.times(1)).reportMeteringAllServicesPerRegion(false, false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void shouldGenerateReportFailTurnOfTheMonth() {
		try {
			
			List<Submission> subs =  listSubmissions();
			
			//Cloudant			
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);
			
			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setMonthYear(CheckTurnOfTheMonth.getMonthYear());
			
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);
			
			Mockito.when(cloudantMock.listSubmissions()).thenReturn(subs);
			Mockito.when(cloudantMock.findSubmissionsFailed(subs)).thenReturn(subs);
			Mockito.when(cloudantMock.getLastMonth(null)).thenReturn(turn);
			
			Mockito.when(meteringPOSTMock.postServiceUsageAllServices(null, null, null)).thenReturn(subs.get(0));
			
			//Fail return true
			Mockito.doReturn(true).when(reportSpy).doTurnOfTheMonthScenario();
			reportSpy.generateReport();
			
			Mockito.verify(cloudantMock, Mockito.times(1)).listSubmissions();
			Mockito.verify(cloudantMock, Mockito.times(1)).findSubmissionsFailed(subs);
			Mockito.verify(meteringPOSTMock, Mockito.times(4)).postServiceUsageAllServices(null, null, null);
			Mockito.verify(cloudantMock, Mockito.times(4)).updateLastSubmission(subs.get(0));
			Mockito.verify(reportSpy, Mockito.times(1)).doTurnOfTheMonthScenario();
			
			//Don't do the submission
			Mockito.verify(reportSpy, Mockito.times(0)).reportMeteringAllServicesPerRegion(false, false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//Test Exceptions
	
	//Cloudant
	@Test
	public void shouldDontGenerateReportCloudantException() throws CloudantException {
			List<Submission> subs =  listSubmissions();
			
			//Cloudant			
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);
			
			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setMonthYear(CheckTurnOfTheMonth.getMonthYear());
			
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);
			
			//Don't throw Exception, but catch it and log 
			//All should return green - choose anyone
			Mockito.when(cloudantMock.listSubmissions()).thenThrow(CloudantException.class);
			//Mockito.when(cloudantMock.findSubmissionsFailed(subs)).thenThrow(CloudantException.class);
			//Mockito.doThrow(CloudantException.class).when(cloudantMock).updateLastSubmission(subs.get(0));
			//Mockito.doThrow(CloudantException.class).when(reportSpy).doTurnOfTheMonthScenario();
	}
	
	@Test
	public void shouldDontGenerateReportCloudantExceptionVoidMethod() throws CloudantException {
			List<Submission> subs =  listSubmissions();
			
			//Cloudant			
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);
			
			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setMonthYear(CheckTurnOfTheMonth.getMonthYear());
			
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);
			
			//Don't throw Exception, but catch it and log 
			Mockito.doThrow(CloudantException.class).when(reportSpy).doTurnOfTheMonthScenario();
	}
	
	
	@Test
	public void shouldDontGenerateReportPostException() throws IOException, PostException {
			List<Submission> subs =  listSubmissions();
			
			//Cloudant			
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);
			
			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setMonthYear(CheckTurnOfTheMonth.getMonthYear());
			
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			
			//Don't throw Exception, but catch it and log 
			Mockito.when(meteringPOSTMock.postServiceUsageAllServices(null, null, null)).thenThrow(PostException.class);
	}
	
	@Test
	public void shouldDontGenerateReportException() throws Exception {
			List<Submission> subs =  listSubmissions();
			
			//Cloudant			
			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			MeteringPOST meteringPOSTMock = Mockito.mock(MeteringPOST.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);
			
			TurnOfTheMonth turn = new TurnOfTheMonth(ID_TURN_OF_THE_MONTH);
			turn.setMonthYear(CheckTurnOfTheMonth.getMonthYear());
			
			ReportMetering report = new ReportMetering(cloudantMock, meteringPOSTMock, totalOfDevicesMock, turn);
			ReportMetering reportSpy = Mockito.spy(report);
			
			//Don't throw Exception, but catch it and log 
			Mockito.doThrow(Exception.class).when(reportSpy).reportMeteringAllServicesPerRegion(false, false);
	}
	
	
	
	public static List<Submission> listSubmissions() {
		List<Submission> subs = new ArrayList<>();
		Submission s = new Submission();
		s.setId("1");
		
		Submission s1 = new Submission();
		s1.setId("2");
		
		
		Submission s2 = new Submission();
		s2.setId("3");
		
		
		Submission s3 = new Submission();
		s3.setId("4");
		
		subs.add(s);
		subs.add(s1);
		subs.add(s2);
		subs.add(s3);
		return subs;
	}
}
