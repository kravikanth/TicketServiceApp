package com.mycompany;

import java.util.Optional;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mycompany.model.SeatHold;
import com.mycompany.model.SimplePerformanceVenue;

public class TicketServiceTest {
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

	@Test
	public void testSeatHoldSameLevel() {
		long id = Thread.currentThread().getId();
		TicketService ticketService = new TicketServiceManager(new SimplePerformanceVenue());
		SeatHold seatsHeld = ticketService.findAndHoldSeats(10, Optional.of(1), Optional.of(1), "self@mycompany.com");
		Assert.assertNotNull(seatsHeld);
		Assert.assertEquals(seatsHeld.getCustomerEmail(), "self@mycompany.com");
		Assert.assertNotNull(seatsHeld.getSeatByLevelMap());
		Assert.assertNotNull(seatsHeld.getSeatByLevelMap().get(1));
		Assert.assertEquals(seatsHeld.getSeatByLevelMap().get(1).size(), 10);
		Assert.assertEquals(seatsHeld.getCustomerEmail(), "self@mycompany.com");
		System.out.println("testSeatHoldSameLevel. Thread id is: " + id);
	}

	@Test
	public void testSeatHoldMultiLevel() {
		long id = Thread.currentThread().getId();
		TicketService ticketService = new TicketServiceManager(new SimplePerformanceVenue());
		SeatHold seatsHeld = ticketService.findAndHoldSeats(25 * 50, Optional.of(1), Optional.of(3),
				"self@mycompany.com");
		Assert.assertNotNull(seatsHeld);
		Assert.assertEquals(seatsHeld.getCustomerEmail(), "self@mycompany.com");
		Assert.assertNotNull(seatsHeld.getSeatByLevelMap());
		Assert.assertNotNull(seatsHeld.getSeatByLevelMap().get(1));
		Assert.assertEquals(seatsHeld.getSeatByLevelMap().get(1).size(), 25 * 50);
		Assert.assertEquals(seatsHeld.getCustomerEmail(), "self@mycompany.com");
		System.out.println("testSeatHoldMultiLevel. Thread id is: " + id);
	}

}