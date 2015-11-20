package com.mycompany.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class Reservation {

	private final int id;
	private final String customerEmail;
	private Map<Integer, List<Seat>> seatByLevelMap;
	private final Instant reservationTime;
	// may not be required at the moment
	private final Payment paymentInfo = new Payment();

	public Reservation(int id, SeatHold seatHold, Instant reservationTime) {
		super();
		this.id = id;
		this.customerEmail = seatHold.getCustomerEmail();
		this.seatByLevelMap = seatHold.getSeatByLevelMap();
		this.reservationTime = reservationTime;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public Map<Integer, List<Seat>> getSeatByLevelMap() {
		return seatByLevelMap;
	}

	public Instant getHoldStartTime() {
		return reservationTime;
	}

	public int getId() {
		return id;
	}

	public Instant getReservationTime() {
		return reservationTime;
	}

	public Payment getPaymentInfo() {
		return paymentInfo;
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", customerEmail=" + customerEmail + ", seatByLevelMap=" + seatByLevelMap
				+ ", reservationTime=" + reservationTime + "]";
	}


}
