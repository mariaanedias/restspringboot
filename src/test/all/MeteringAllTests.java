/****** IBM Confidential *******
* IoT for Electronics 
*  Copyright IBM Corp. 2016 ****/

package com.ibm.iotcloud.electronics.metering.all;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.iotcloud.electronics.metering.integration.MeteringAllIntegrationTests;
import com.ibm.iotcloud.electronics.metering.unit.MeteringAllUnitTests;

@RunWith(Suite.class)
@SuiteClasses({ MeteringAllUnitTests.class, MeteringAllIntegrationTests.class })
public class MeteringAllTests {
}
