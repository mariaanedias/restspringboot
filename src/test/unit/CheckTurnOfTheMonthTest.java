package com.ibm.iotcloud.electronics.metering.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.iotcloud.electronics.metering.core.CheckTurnOfTheMonth;

public class CheckTurnOfTheMonthTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetMonthYear() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		int month = (cal.get(Calendar.MONDAY)+1);
		int year = cal.get(Calendar.YEAR);
        assertEquals(month, CheckTurnOfTheMonth.getMonth());
        assertEquals(year, CheckTurnOfTheMonth.getYear());
        assertEquals(month+""+year, CheckTurnOfTheMonth.getMonthYear());
	}

	@Test
	public void testGetCalendar() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		assertNotNull(CheckTurnOfTheMonth.getCalendar());
		assertEquals(cal.getTimeZone(), CheckTurnOfTheMonth.getCalendar().getTimeZone());
	}

	@Test
	public void testGetCalendarFirst() {
		Calendar cal =  Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR, cal.getActualMinimum(Calendar.HOUR)+3);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);
		assertEquals(cal.get(Calendar.DAY_OF_MONTH), CheckTurnOfTheMonth.getCalendarFirst().get(Calendar.DAY_OF_MONTH));
		assertEquals(CheckTurnOfTheMonth.getCalendarFirst().get(Calendar.DAY_OF_MONTH), 1);
	}

	@Test
	public void testGetCalendarLast() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR, cal.getActualMaximum(Calendar.HOUR)-12);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		cal.set(Calendar.AM_PM, Calendar.AM);
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date hora = CheckTurnOfTheMonth.getCalendarLast().getTime(); 
		String fDate= sdf.format(hora);
		
		assertEquals(cal.get(Calendar.DAY_OF_MONTH), CheckTurnOfTheMonth.getCalendarLast().get(Calendar.DAY_OF_MONTH));
		//assertEquals(CheckTurnOfTheMonth.getCalendarLast().get(Calendar.DAY_OF_MONTH), 30);
		assertEquals(fDate, "23:59:59");
	}
}
