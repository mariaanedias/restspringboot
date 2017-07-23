/****** IBM Confidential *******
* IoT for Electronics 
*  Copyright IBM Corp. 2016 ****/

package com.ibm.iotcloud.electronics.metering.integration;

import static org.junit.Assert.assertEquals;

import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ibm.iotcloud.electronics.metering.core.RegistrationInfo;
import com.ibm.iotcloud.electronics.metering.entities.ServiceInstance;
import com.ibm.iotcloud.electronics.metering.entities.ServiceUsage;
import com.ibm.iotcloud.electronics.metering.exceptions.AuthenticationException;
import com.ibm.iotcloud.electronics.metering.exceptions.RegistrationException;
import com.ibm.iotcloud.electronics.metering.util.Authenticate;
import com.ibm.iotcloud.electronics.metering.util.IoTElectronicsResourceBundle;

public class RegistrationInfoTest {
	
	private static ResourceBundle systemProperties = IoTElectronicsResourceBundle.getSystemConfigurationBundle();

	private static final String TOTAL_CURRENT_API = systemProperties.getString("registration.count.appliances");
	private static final String BASE_URL = systemProperties.getString("registration.base.url");
	private static String protocol = systemProperties.getString("metering.protocol");
	private static int port = Integer.parseInt(systemProperties.getString("metering.port"));

	@Before
	public void initializeMock() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldCallCurrentTotal() {
		try {
			ServiceUsage s = new ServiceUsage();
			String url = BASE_URL+"/"+TOTAL_CURRENT_API+null+"/"+ null;
			s.setUrl(url);
			
			Authenticate authenticate = org.mockito.Mockito.mock(Authenticate.class);
			Mockito.when(authenticate.doGetTotals(s, s.getServiceInstanceId(), BASE_URL, port, protocol)).thenReturn("4");
			RegistrationInfo registration = new RegistrationInfo(authenticate);
			long lo = 0l;
			lo = registration.callCurrentTotal(s);
		
			Mockito.verify(authenticate, Mockito.times(1)).doGetTotals(s, s.getServiceInstanceId(), BASE_URL, port, protocol);
			assertEquals(lo, 4l);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	@Test
	public void shouldCallCurrentTotalStringResult() {
		try {
			ServiceUsage s = new ServiceUsage();
			String url = BASE_URL+"/"+TOTAL_CURRENT_API+null+"/"+ null;
			s.setUrl(url);
			
			Authenticate authenticate = org.mockito.Mockito.mock(Authenticate.class);
			Mockito.when(authenticate.doGetTotals(s, s.getServiceInstanceId(), BASE_URL, port, protocol)).thenReturn("This should become 0l");
			RegistrationInfo registration = new RegistrationInfo(authenticate);
			long lo = 0l;
			lo = registration.callCurrentTotal(s);
			Mockito.verify(authenticate, Mockito.times(1)).doGetTotals(s, s.getServiceInstanceId(), BASE_URL, port, protocol);
			assertEquals(lo, 0l);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	@Test(expected = RegistrationException.class)
	public void shouldCallCurrentTotalException() throws RegistrationException {
		ServiceUsage s = new ServiceUsage();
		String url = BASE_URL + "/" + TOTAL_CURRENT_API + null + "/" + null;
		s.setUrl(url);
		Authenticate authenticate = org.mockito.Mockito.mock(Authenticate.class);
		RegistrationInfo registration = new RegistrationInfo(authenticate);
		Mockito.when(registration.callCurrentTotal(s)).thenThrow(RegistrationException.class);
		long lo = 0l;
		lo = registration.callCurrentTotal(s);
	}
	
	@Test(expected = RegistrationException.class)
	public void shouldCallCurrentTotalExceptionInAuthenticate() throws RegistrationException, AuthenticationException {
		ServiceUsage s = new ServiceUsage();
		String url = BASE_URL + "/" + TOTAL_CURRENT_API + null + "/" + null;
		s.setUrl(url);

		Authenticate authenticate = org.mockito.Mockito.mock(Authenticate.class);
		RegistrationInfo registration = new RegistrationInfo(authenticate);
		Mockito.when(authenticate.doGetTotals(s, s.getServiceInstanceId(), BASE_URL, port, protocol))
				.thenThrow(new AuthenticationException(""));
		long lo = 0l;
		lo = registration.callCurrentTotal(s);
	}


	@Test
	public void shouldCallDeletedTotal() {
		try {
			ServiceUsage s = new ServiceUsage();
			String url = BASE_URL+"/"+TOTAL_CURRENT_API+null+"/"+ null;
			s.setUrl(url);
			
			Authenticate authenticate = org.mockito.Mockito.mock(Authenticate.class);
			Mockito.when(authenticate.doGetTotals(s, s.getServiceInstanceId(), BASE_URL, port, protocol)).thenReturn("2");
			RegistrationInfo registration = new RegistrationInfo(authenticate);
			long lo = 0l;
			lo = registration.callTotalWithDeletedFromMonth(s);
		
			Mockito.verify(authenticate, Mockito.times(1)).doGetTotals(s, s.getServiceInstanceId(), BASE_URL, port, protocol);
			assertEquals(lo, 2l);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	@Test
	public void shouldCallDeletedTotalStringResult() {
		try {
			ServiceUsage s = new ServiceUsage();
			String url = BASE_URL+"/"+TOTAL_CURRENT_API+null+"/"+ null;
			s.setUrl(url);
			
			Authenticate authenticate = org.mockito.Mockito.mock(Authenticate.class);
			Mockito.when(authenticate.doGetTotals(s, s.getServiceInstanceId(), BASE_URL, port, protocol)).thenReturn("This should become 0l");
			RegistrationInfo registration = new RegistrationInfo(authenticate);
			long lo = 0l;
			lo = registration.callTotalWithDeletedFromMonth(s);
			Mockito.verify(authenticate, Mockito.times(1)).doGetTotals(s, s.getServiceInstanceId(), BASE_URL, port, protocol);
			assertEquals(lo, 0l);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	@Test(expected = RegistrationException.class)
	public void shouldCallDeletedTotalException() throws RegistrationException {
		ServiceUsage s = new ServiceUsage();
		String url = BASE_URL + "/" + TOTAL_CURRENT_API + null + "/" + null;
		s.setUrl(url);
		Authenticate authenticate = org.mockito.Mockito.mock(Authenticate.class);
		RegistrationInfo registration = new RegistrationInfo(authenticate);
		Mockito.when(registration.callTotalWithDeletedFromMonth(s)).thenThrow(RegistrationException.class);
		long lo = 0l;
		lo = registration.callCurrentTotal(s);
	}
	
	@Test(expected = RegistrationException.class)
	public void shouldCallDeletedTotalExceptionInAuthenticate() throws RegistrationException, AuthenticationException {
		ServiceUsage s = new ServiceUsage();
		String url = BASE_URL + "/" + TOTAL_CURRENT_API + null + "/" + null;
		s.setUrl(url);

		Authenticate authenticate = org.mockito.Mockito.mock(Authenticate.class);
		RegistrationInfo registration = new RegistrationInfo(authenticate);
		Mockito.when(authenticate.doGetTotals(s, s.getServiceInstanceId(), BASE_URL, port, protocol))
				.thenThrow(new AuthenticationException(""));
		long lo = 0l;
		lo = registration.callTotalWithDeletedFromMonth(s);
	}
	
	
	@Test
	public void shouldCallResetCount() {
		try {
			ServiceInstance s = new ServiceInstance();
			String url = BASE_URL+"/"+TOTAL_CURRENT_API+null+"/"+ null;
			s.setUrl(url);
			
			Authenticate authenticate = org.mockito.Mockito.mock(Authenticate.class);
			Mockito.when(authenticate.doPutResetCount(s, s.getServiceInstanceId(), BASE_URL, port, protocol)).thenReturn("2");
			RegistrationInfo registration = new RegistrationInfo(authenticate);
			registration.resetCount(s, "0909");
		
			Mockito.verify(authenticate, Mockito.times(1)).doPutResetCount(s, s.getServiceInstanceId(), BASE_URL, port, protocol);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	
	@Test(expected = RegistrationException.class)
	public void shouldCallResetCountException() throws RegistrationException {
		ServiceInstance s = new ServiceInstance();
		String url = BASE_URL + "/" + TOTAL_CURRENT_API + null + "/" + null;
		s.setUrl(url);
		RegistrationInfo registration = org.mockito.Mockito.mock(RegistrationInfo.class);
		Mockito.doThrow(RegistrationException.class).when(registration).resetCount(s, "0909");
		registration.resetCount(s, "0909");
	}
	
	@Test(expected = RegistrationException.class)
	public void shouldCallResetCountExceptionInAuthenticate() throws RegistrationException, AuthenticationException {
		ServiceInstance s = new ServiceInstance();
		String url = BASE_URL + "/" + TOTAL_CURRENT_API + null + "/" + null;
		s.setUrl(url);

		Authenticate authenticate = org.mockito.Mockito.mock(Authenticate.class);
		RegistrationInfo registration = new RegistrationInfo(authenticate);
		Mockito.when(authenticate.doPutResetCount(s, s.getServiceInstanceId(), BASE_URL, port, protocol))
		.thenThrow(new AuthenticationException(""));
		registration.resetCount(s, "0909");
	}
	

}
