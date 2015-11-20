package com.mycompany;

import java.util.Optional;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mycompany.model.SeatHold;
import com.mycompany.model.SimplePerformanceVenue;

public class TicketServiceTimeoutTest {
	TicketService ticketService = new TicketServiceManager(new SimplePerformanceVenue());

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

	@Test(invocationCount = 1)
	public void testTimeOutClaimSeats() {
		long id = Thread.currentThread().getId();
		TicketService ticketService = new TicketServiceManager(new SimplePerformanceVenue());
		SeatHold seatsHeld = ticketService.findAndHoldSeats(25 * 30, Optional.of(1), Optional.of(1),
				"self@mycompany.com");
		Assert.assertEquals(ticketService.numSeatsAvailable(Optional.of(1)), 25 * 20);
		Assert.assertNotNull(seatsHeld);
		Assert.assertEquals(seatsHeld.getCustomerEmail(), "self@mycompany.com");
		Assert.assertNotNull(seatsHeld.getSeatByLevelMap());
		Assert.assertNotNull(seatsHeld.getSeatByLevelMap().get(1));
		Assert.assertEquals(seatsHeld.getSeatByLevelMap().get(1).size(), 25 * 30);
		try {
			Thread.sleep(TicketServiceManager.TIME_OUT_MILLIS * 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// after Time out all seats are available
		Assert.assertEquals(ticketService.numSeatsAvailable(Optional.of(1)), 25 * 50);
		System.out.println("Sample test-method . Thread id is: " + id);
	}

	@AfterClass
	public void afterClass() {
		long id = Thread.currentThread().getId();
		System.out.println("After test-method. Thread id is: " + id);
	}

}