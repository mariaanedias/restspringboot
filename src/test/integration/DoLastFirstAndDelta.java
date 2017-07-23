/****** IBM Confidential *******
* IoT for Electronics 
*  Copyright IBM Corp. 2016 ****/

package com.ibm.iotcloud.electronics.metering.integration;

import java.util.Calendar;
import java.util.ResourceBundle;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoAnnotations.Mock;

import com.ibm.iotcloud.electronics.metering.core.CloudantRW;
import com.ibm.iotcloud.electronics.metering.core.ReportMetering;
import com.ibm.iotcloud.electronics.metering.core.TotalOfDevices;
import com.ibm.iotcloud.electronics.metering.entities.ServiceUsage;
import com.ibm.iotcloud.electronics.metering.exceptions.RegistrationException;
import com.ibm.iotcloud.electronics.metering.util.IoTElectronicsResourceBundle;

public class DoLastFirstAndDelta {

	private static ResourceBundle systemProperties = IoTElectronicsResourceBundle.getSystemConfigurationBundle();
	private static final String ID_TURN_OF_THE_MONTH = systemProperties
			.getString("metering.cloudant.turnofthemonth.id");

	@Mock
	private CloudantRW cloudantRWMock;

	@Before
	public void initializeMocks() {
		MockitoAnnotations.initMocks(this);
	}

	// Do 1st 
	@Test
	public void shouldDoFirst() {
		try {

			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			ReportMetering report = new ReportMetering(cloudantMock, totalOfDevicesMock);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.when(totalOfDevicesMock.getTotalAppliancesWithDeleted(Mockito.any(ServiceUsage.class))).thenReturn(4l);

			ServiceUsage su = new ServiceUsage();
			reportSpy.first(su, Calendar.getInstance());

			Mockito.verify(totalOfDevicesMock, Mockito.times(1)).getTotalAppliancesWithDeleted(Mockito.any(ServiceUsage.class));
			Assert.assertEquals(4l, su.getLastSubmissionTotal());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Do 1st 
	@Test (expected = RegistrationException.class)
	public void shouldDoFirstException() throws RegistrationException {

			CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
			TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

			ReportMetering report = new ReportMetering(cloudantMock, totalOfDevicesMock);
			ReportMetering reportSpy = Mockito.spy(report);

			Mockito.when(totalOfDevicesMock.getTotalAppliancesWithDeleted(Mockito.any(ServiceUsage.class))).thenThrow(RegistrationException.class);

			ServiceUsage su = new ServiceUsage();
			reportSpy.first(su, Calendar.getInstance());

	}
	
		// Do Last 
		@Test
		public void shouldDoLastDONTHasPrevious() {
			try {

				CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
				TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

				ReportMetering report = new ReportMetering(cloudantMock, totalOfDevicesMock);
				ReportMetering reportSpy = Mockito.spy(report);

				Mockito.when(totalOfDevicesMock.getTotalAppliancesWithDeleted(Mockito.any(ServiceUsage.class))).thenReturn(4l);

				ServiceUsage su = new ServiceUsage();
				reportSpy.last(su, false);

				Mockito.verify(totalOfDevicesMock, Mockito.never()).calculateDelta(Mockito.any(ServiceUsage.class));
				Mockito.verify(totalOfDevicesMock, Mockito.times(1)).getTotalAppliancesWithDeleted(Mockito.any(ServiceUsage.class));
				Assert.assertEquals(4l, su.getLastSubmissionTotal());
				Assert.assertEquals(4l, su.getDelta());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		// Do Last 
		@Test
		public void shouldDoLastHasPrevious() {
			try {

				CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
				TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

				ReportMetering report = new ReportMetering(cloudantMock, totalOfDevicesMock);
				ReportMetering reportSpy = Mockito.spy(report);

				Mockito.when(totalOfDevicesMock.calculateDelta(Mockito.any(ServiceUsage.class))).thenReturn(2l);

				ServiceUsage su = new ServiceUsage();
				reportSpy.last(su, true);

				Mockito.verify(totalOfDevicesMock, Mockito.times(1)).calculateDelta(Mockito.any(ServiceUsage.class));
				Mockito.verify(totalOfDevicesMock, Mockito.never()).getTotalAppliancesWithDeleted(Mockito.any(ServiceUsage.class));
				Assert.assertEquals(2l, su.getDelta());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Do Last 
		@Test (expected = RegistrationException.class)
		public void shouldDoLastDeltaException() throws RegistrationException {

				CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
				TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

				ReportMetering report = new ReportMetering(cloudantMock, totalOfDevicesMock);
				ReportMetering reportSpy = Mockito.spy(report);

				Mockito.when(totalOfDevicesMock.calculateDelta(Mockito.any(ServiceUsage.class))).thenThrow(RegistrationException.class);

				ServiceUsage su = new ServiceUsage();
				reportSpy.last(su, true);
		}
		
		// Do Last 
		@Test (expected = RegistrationException.class)
		public void shouldDoLastException() throws RegistrationException {

				CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
				TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

				ReportMetering report = new ReportMetering(cloudantMock, totalOfDevicesMock);
				ReportMetering reportSpy = Mockito.spy(report);

				Mockito.when(totalOfDevicesMock.getTotalAppliancesWithDeleted(Mockito.any(ServiceUsage.class))).thenThrow(RegistrationException.class);

				ServiceUsage su = new ServiceUsage();
				reportSpy.last(su, false);
		}
		
		
		// Delta 
		@Test
		public void shouldDoDelta() {
			try {

				CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
				TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

				ReportMetering report = new ReportMetering(cloudantMock, totalOfDevicesMock);
				ReportMetering reportSpy = Mockito.spy(report);

				Mockito.when(totalOfDevicesMock.calculateDelta(Mockito.any(ServiceUsage.class))).thenReturn(4l);

				ServiceUsage su = new ServiceUsage();
				reportSpy.delta(su);

				Mockito.verify(totalOfDevicesMock, Mockito.times(1)).calculateDelta(Mockito.any(ServiceUsage.class));
				Assert.assertEquals(4l, su.getDelta());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Delta
		@Test (expected = RegistrationException.class)
		public void shouldDoDeltaException() throws RegistrationException {

				CloudantRW cloudantMock = Mockito.mock(CloudantRW.class);
				TotalOfDevices totalOfDevicesMock = Mockito.mock(TotalOfDevices.class);

				ReportMetering report = new ReportMetering(cloudantMock, totalOfDevicesMock);
				ReportMetering reportSpy = Mockito.spy(report);

				Mockito.when(totalOfDevicesMock.calculateDelta(Mockito.any(ServiceUsage.class))).thenThrow(RegistrationException.class);

				ServiceUsage su = new ServiceUsage();
				reportSpy.delta(su);

		}
}
