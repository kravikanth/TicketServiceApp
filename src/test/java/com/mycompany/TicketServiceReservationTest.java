package com.mycompany;

import java.util.Optional;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mycompany.model.SeatHold;
import com.mycompany.model.SimplePerformanceVenue;

public class TicketServiceReservationTest {
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
	public void testSeatReserve() {
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

		String reserveId = ticketService.reserveSeats(seatsHeld.getId(), "self@mycompany.com");

		Assert.assertNotNull(reserveId);

		System.out.println("Sample test-method. Thread id is: " + id);
	}

	@Test(invocationCount = 1, expectedExceptions = ValidationExcption.class)
	public void testSeatReserveDifferntEmail() throws ValidationExcption {
		long id = Thread.currentThread().getId();
		TicketService ticketService = new TicketServiceManager(new SimplePerformanceVenue());
		SeatHold seatsHeld = ticketService.findAndHoldSeats(25 * 30, Optional.of(1), Optional.of(1),
				"self@mycompany.com");
		Assert.assertNotNull(seatsHeld);
		Assert.assertEquals(seatsHeld.getCustomerEmail(), "self@mycompany.com");
		ticketService.reserveSeats(seatsHeld.getId(), "differnt@mycompany.com");

		System.out.println("testSeatReserveDifferntEmail. Thread id is: " + id);
	}

	@Test(invocationCount = 1, expectedExceptions = ValidationExcption.class)
	public void testSeatReserveNullCustomerEmail() throws ValidationExcption {
		long id = Thread.currentThread().getId();
		TicketService ticketService = new TicketServiceManager(new SimplePerformanceVenue());
		ticketService.reserveSeats(5, null);
		System.out.println("testSeatReserveInvalidCustomerEmail. Thread id is: " + id);
	}

	@Test(invocationCount = 1, expectedExceptions = ValidationExcption.class)
	public void testSeatReserveInvalidCustomerEmail() throws ValidationExcption {
		long id = Thread.currentThread().getId();
		TicketService ticketService = new TicketServiceManager(new SimplePerformanceVenue());
		ticketService.reserveSeats(1, "");
		System.out.println("testSeatReserveInvalidCustomerEmail. Thread id is: " + id);
	}

}