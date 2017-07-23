/****** IBM Confidential *******
* IoT for Electronics 
*  Copyright IBM Corp. 2016 ****/

package com.ibm.iotcloud.electronics.metering.integration;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.wink.json4j.JSONObject;
import org.hamcrest.beans.SamePropertyValuesAs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoAnnotations.Mock;

import com.ibm.iotcloud.electronics.metering.core.MeteringPOST;
import com.ibm.iotcloud.electronics.metering.entities.Submission;
import com.ibm.iotcloud.electronics.metering.exceptions.PostException;

public class MeteringPOSTTest {

	@Mock
	private HttpClient defaultHttpClient;
	
	@Before
	public void initializeMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldpostServiceUsageAllServices() {
		//Submission
		final Submission sub = new Submission();
		sub.setSuccess(true);
		sub.setUrlLocation("URLLocationTest");

		try {
			// Json
			JSONObject json = new JSONObject(createJsonSubmission());
			
			ProtocolVersion pv = new ProtocolVersion("http", 1, 0);
			
			//201 = response code OK from BSS API
			StatusLine s  = new BasicStatusLine(pv, 201, "");
			final HttpResponse response = new BasicHttpResponse(s);
			response.addHeader("Location", "URLLocationTest");
		
			MeteringPOST mPostClient = new MeteringPOST(defaultHttpClient); 
			MeteringPOST meteringPostMock = Mockito.mock(MeteringPOST.class);
			
			Mockito.when(defaultHttpClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
			Mockito.when(meteringPostMock.postUsageAllServices("1", "us-south", json)).thenReturn(sub);
			
			Submission submission = mPostClient.postUsageAllServices("1", "us-south", json);
			
			Mockito.verify(defaultHttpClient, Mockito.times(1)).execute(Mockito.any(HttpPost.class));
			sub.setLastSubmissionDate(submission.getLastSubmissionDate());
			Assert.assertNotNull(submission);
			Assert.assertNotNull(submission.getUrlLocation());
			Assert.assertThat(submission, SamePropertyValuesAs.samePropertyValuesAs(sub));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(expected = PostException.class)
	public void shouldpostServiceUsageAllServicesBSSReturnError() throws Exception {
		//Submission
		final Submission sub = new Submission();
		sub.setSuccess(true);
		sub.setUrlLocation("URLLocationTest");
	
		// Json
		JSONObject json = new JSONObject(createJsonSubmission());

		ProtocolVersion pv = new ProtocolVersion("http", 1, 0);
		
		//If BSS return any code not 201
		StatusLine s = new BasicStatusLine(pv, 404, "");
		final HttpResponse response = new BasicHttpResponse(s);
		response.addHeader("Location", "URLLocationTest");

		MeteringPOST mPostClient = new MeteringPOST(defaultHttpClient);
		MeteringPOST meteringPostMock = Mockito.mock(MeteringPOST.class);

		Mockito.when(defaultHttpClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
		Mockito.when(meteringPostMock.postUsageAllServices("1", "us-south", json)).thenReturn(sub);

		mPostClient.postUsageAllServices("1", "us-south", json);
	}

	@Test(expected = PostException.class)
	public void shouldpostServiceUsageAllServicesException() throws Exception {
		//Submission
		final Submission sub = new Submission();
		sub.setSuccess(true);
		sub.setUrlLocation("URLLocationTest");
		// Json
		JSONObject json = new JSONObject(createJsonSubmission());

		ProtocolVersion pv = new ProtocolVersion("http", 1, 0);
		StatusLine s = new BasicStatusLine(pv, 202, "");
		final HttpResponse response = new BasicHttpResponse(s);
		response.addHeader("Location", "URLLocationTest");

		MeteringPOST mPostClient = new MeteringPOST(defaultHttpClient);
		MeteringPOST meteringPostMock = Mockito.mock(MeteringPOST.class);

		Mockito.when(defaultHttpClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
		Mockito.when(meteringPostMock.postUsageAllServices("1", "us-south", json)).thenThrow(PostException.class);

		Submission submission = mPostClient.postUsageAllServices("1", "us-south", json);
	}


	
	public String createJsonSubmission() {
		return "{\"_id\":\"1234\",\"_rev\":\"1-12345\",\"serviceID\":\"myID1\",\"startPeriod\":1467060572976,\"endPeriod\":1467060572976,\"lastSubmitDate\":1467060706332,\"success\":true,\"region\":\"us-south\",\"month\":\"52016\",\"urlLocation\":\"https://myurl1/v1/services/id/usage?region=us-south\", \"json\": {\"service_instances\":[{\"usage\":[{\"start\":1467060572976,\"resources\":[{\"unit\":\"REGISTERED_DEVICE\",\"quantity\":1}],\"end\":1467060572976,\"organization_guid\":\"myGUID\",\"space_guid\":\"mySpace\",\"plan_id\":\"planID1\",\"consumer\":{\"type\":\"cloud-foundry-application\",\"value\":\"value\"}}],\"service_instance_id\":\"myInstanceID\"}]}}";
	}
}
