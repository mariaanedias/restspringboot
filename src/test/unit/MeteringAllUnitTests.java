package com.ibm.iotcloud.electronics.metering.unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CheckTurnOfTheMonthTest.class, ParseJsonTest.class, ReportMeteringTest.class,
		TotalOfDevicesTest.class, InitMeteringTimerTest.class})
public class MeteringAllUnitTests {

}
