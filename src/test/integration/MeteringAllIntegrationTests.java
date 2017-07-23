/****** IBM Confidential *******
* IoT for Electronics 
*  Copyright IBM Corp. 2016 ****/

package com.ibm.iotcloud.electronics.metering.integration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CloudantRWTest.class, DoLastFirstAndDelta.class, DoSubmissionsAndResetCount.class, MeteringPOSTTest.class, RegistrationInfoTest.class,
	ReportMeteringAllServicesPerRegion.class, ReportMeteringTest.class, ReportMeteringUsageTest.class, TotalOfDevicesTest.class,
	TurnOfTheMonthScenarioTest.class, TurnOfTheMonthScenarioError.class})
public class MeteringAllIntegrationTests {

}
