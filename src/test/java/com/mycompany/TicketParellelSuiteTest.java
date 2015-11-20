package com.mycompany;

import java.util.Optional;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.mycompany.model.HouseFullException;
import com.mycompany.model.SeatHold;
import com.mycompany.model.SimplePerformanceVenue;

public class TicketParellelSuiteTest {
	TicketService ticketService = new TicketServiceManager(new SimplePerformanceVenue());
	private static final int TOTAL_SEATS = 25 * 50 + 20 * 100 + 15 * 100 + 15 * 100;

	@BeforeTest
	public void beforeTest() {
		long id = Thread.currentThread().getId();
		System.out.println("Before *** test Thread id is: " + id);

	}

	@BeforeClass
	public void beforeClass() {
		long id = Thread.currentThread().getId();
		System.out.println("Before test-class " + ". Thread id is: " + id);
	}

	@DataProvider(parallel = true)
	public Object[][] maxPerLevelDataProvider() {
		return new Object[][] { { 25 * 50, 1, "self1@mycompany.com", TOTAL_SEATS },
				{ 20 * 100, 2, "self2@mycompany.com", TOTAL_SEATS },
				{ 15 * 100, 3, "self3@mycompany.com", TOTAL_SEATS },
				{ 15 * 100, 4, "self4@mycompany.com", TOTAL_SEATS } };
	}

	@Test(dataProvider = "maxPerLevelDataProvider", invocationCount = 10)
	public void maxPerLevelTest(int numSeats, int level, String customerEmail, int totalSeatsAvailable) {
		long id = Thread.currentThread().getId();

		TicketService ticketService = new TicketServiceManager(new SimplePerformanceVenue());
		Assert.assertEquals(totalSeatsAvailable, ticketService.numSeatsAvailable(Optional.empty()));
		Assert.assertEquals(numSeats, ticketService.numSeatsAvailable(Optional.of(level)));

		SeatHold seatsHeld = ticketService.findAndHoldSeats(numSeats, Optional.of(level), Optional.of(level),
				customerEmail);
		Assert.assertNotNull(seatsHeld);
		Assert.assertEquals(seatsHeld.getCustomerEmail(), customerEmail);
		Assert.assertNotNull(seatsHeld.getSeatByLevelMap());
		Assert.assertNotNull(seatsHeld.getSeatByLevelMap().get(level));
		Assert.assertEquals(seatsHeld.getSeatByLevelMap().get(level).size(), numSeats);
		System.out.println("maxPerLevelTest. Thread id is: " + id);

	}

	@Test(dataProvider = "maxPerLevelDataProvider", expectedExceptions = HouseFullException.class)
	public void holdWithHouseFullException(int numSeats, int level, String customerEmail, int totalSeats) throws HouseFullException {
		TicketService ticketService = new TicketServiceManager(new SimplePerformanceVenue());
		ticketService.findAndHoldSeats(numSeats + 1, Optional.of(level), Optional.of(level), customerEmail);
	}

	@DataProvider(parallel = true)
	public Object[][] parallelDataProvider() {
		return new Object[][] { { 25 * 50 + 50, 1, 2, "self1@mycompany.com" },
				{ 20 * 100 - 50, 2, 2, "self2@mycompany.com" }, { 15 * 100 + 100, 3, 4, "self3@mycompany.com" },
				{ 15 * 100 - 100, 4, 4, "self4@mycompany.com" } };
	}

	@Test(dataProvider = "parallelDataProvider")
	public void parallelTest(int numSeats, int minLevel, int maxLevel, String customerEmail) {
		System.out.println("parallelTest " + numSeats + "|" + minLevel + "|" + maxLevel + "|" + customerEmail);
		long id = Thread.currentThread().getId();

		SeatHold seatsHeld = ticketService.findAndHoldSeats(numSeats, Optional.of(minLevel), Optional.of(maxLevel),
				customerEmail);
		Assert.assertNotNull(seatsHeld);
		Assert.assertEquals(seatsHeld.getCustomerEmail(), customerEmail);
		Assert.assertNotNull(seatsHeld.getSeatByLevelMap());
		Assert.assertNotNull(seatsHeld.getSeatByLevelMap().get(minLevel));
		Assert.assertNotNull(seatsHeld.getSeatByLevelMap().get(maxLevel));
		int total = seatsHeld.getSeatByLevelMap().get(minLevel).size();
		if (minLevel != maxLevel) {
			total += seatsHeld.getSeatByLevelMap().get(maxLevel).size();
		}
		Assert.assertEquals(total, numSeats);
		System.out.println("Sample test-method . Thread id is: " + id);

	}

	
	@AfterClass
	public void afterClass() {
		long id = Thread.currentThread().getId();
		System.out.println("After test-method. Thread id is: " + id);
	}

}