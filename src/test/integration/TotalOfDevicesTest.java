/****** IBM Confidential *******
* IoT for Electronics 
*  Copyright IBM Corp. 2016 ****/

package com.ibm.iotcloud.electronics.metering.integration;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.ibm.iotcloud.electronics.metering.core.RegistrationInfo;
import com.ibm.iotcloud.electronics.metering.core.TotalOfDevices;
import com.ibm.iotcloud.electronics.metering.entities.ServiceUsage;
import com.ibm.iotcloud.electronics.metering.exceptions.RegistrationException;


@RunWith(PowerMockRunner.class)
@PrepareForTest({TotalOfDevices.class, RegistrationInfo.class})
public class TotalOfDevicesTest {

	@Before
	public void initializeMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void shouldGetTotalAppliancesCurrent() {
		try {
			ServiceUsage s = PowerMockito.mock(ServiceUsage.class);
			RegistrationInfo registration = PowerMockito.mock(RegistrationInfo.class);
			TotalOfDevices total = new TotalOfDevices(registration);
			PowerMockito.when(registration.callCurrentTotal(s)).thenReturn(4l);
			
			long lo = 0l;
			lo = total.getTotalAppliancesCurrent(s);
			
			Mockito.verify(registration).callCurrentTotal(s);
			Mockito.verify(registration, Mockito.times(1)).callCurrentTotal(s);	
			Mockito.verify(s, Mockito.times(1)).setLastSubmissionTotal(4l);
			assertEquals(lo, 4l);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(expected = RegistrationException.class)
	public void shouldGetTotalAppliancesCurrentException() throws RegistrationException {
		ServiceUsage s = new ServiceUsage();
		RegistrationInfo registration = PowerMockito.mock(RegistrationInfo.class);
		TotalOfDevices total = new TotalOfDevices(registration);
		Mockito.when(registration.callCurrentTotal(s)).thenThrow(RegistrationException.class);
		long lo = total.getTotalAppliancesCurrent(s);
	}
		
	@Test
	public void shouldGetTotalAppliancesDeleted() {
		try {
			ServiceUsage s = PowerMockito.mock(ServiceUsage.class);
			RegistrationInfo registration = PowerMockito.mock(RegistrationInfo.class);
			TotalOfDevices total = new TotalOfDevices(registration);
			PowerMockito.when(registration.callTotalWithDeletedFromMonth(s)).thenReturn(2l);

			long lo = 0l;
			lo = total.getTotalAppliancesWithDeleted(s);

			Mockito.verify(registration).callTotalWithDeletedFromMonth(s);
			Mockito.verify(registration, Mockito.times(1)).callTotalWithDeletedFromMonth(s);
			assertEquals(lo, 2l);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(expected = RegistrationException.class)
	public void shouldGetTotalAppliancesDeletedException() throws RegistrationException {
		ServiceUsage s = new ServiceUsage();
		RegistrationInfo registration = PowerMockito.mock(RegistrationInfo.class);
		TotalOfDevices total = new TotalOfDevices(registration);
		Mockito.when(registration.callTotalWithDeletedFromMonth(s)).thenThrow(RegistrationException.class);
		long lo = total.getTotalAppliancesWithDeleted(s);
	}
	
	@Test
	public void shouldCalculateDelta() {
		try {
			
			ServiceUsage s = PowerMockito.mock(ServiceUsage.class);
			PowerMockito.when(s.getLastSubmissionTotal()).thenReturn(0l);
			
			RegistrationInfo registration = PowerMockito.mock(RegistrationInfo.class);
			TotalOfDevices total = new TotalOfDevices(registration);
			
			TotalOfDevices totalMock = PowerMockito.spy(total);
			PowerMockito.when(totalMock.getTotalAppliancesWithDeleted(s)).thenReturn(2l);
			
			PowerMockito.doAnswer(new Answer < Object >() {
				public Object answer(InvocationOnMock invocation) {
					Object[] args = invocation.getArguments();
					assertEquals(2, args[0]);
					return null;
				}
			}).when(totalMock).getTotalAppliancesCurrent(s);
			
			long lo = 0l;
			lo = total.calculateDelta(s);
			
			Mockito.verify(totalMock, Mockito.times(1)).getTotalAppliancesWithDeleted(s);
			assertEquals(lo, 2l);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(expected = RegistrationException.class)
	public void shouldCalculateDeltaException() throws RegistrationException {
		ServiceUsage s = new ServiceUsage();
		RegistrationInfo registration = PowerMockito.mock(RegistrationInfo.class);
		TotalOfDevices total = new TotalOfDevices(registration);
		TotalOfDevices totalMock = PowerMockito.spy(total);
		Mockito.when(totalMock.getTotalAppliancesWithDeleted(s)).thenThrow(RegistrationException.class);
		long lo = total.calculateDelta(s);
	}
}
