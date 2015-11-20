package com.mycompany.model;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SeatHold {

	private final int id;
	private final String customerEmail;
	private final Map<Integer, List<Seat>> seatByLevelMap;
	private final Instant holdStartTime;

	public SeatHold(int id, String customerEmail, Map<Integer, List<Seat>> seatMap, Instant holdStartTime) {
		super();
		this.id = id;
		this.customerEmail = customerEmail;
		this.seatByLevelMap = Collections.unmodifiableMap(seatMap);
		
		this.holdStartTime = holdStartTime;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public Map<Integer, List<Seat>> getSeatByLevelMap() {
		return seatByLevelMap;
	}

	public Instant getHoldStartTime() {
		return holdStartTime;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "SeatHold [id=" + id + ", customerEmail=" + customerEmail + ", seatByLevelMap=" + seatByLevelMap
				+ ", startTime=" + holdStartTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customerEmail == null) ? 0 : customerEmail.hashCode());
		result = prime * result + ((holdStartTime == null) ? 0 : holdStartTime.hashCode());
		result = prime * result + id;
		result = prime * result + ((seatByLevelMap == null) ? 0 : seatByLevelMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SeatHold other = (SeatHold) obj;
		if (customerEmail == null) {
			if (other.customerEmail != null)
				return false;
		} else if (!customerEmail.equals(other.customerEmail))
			return false;
		if (holdStartTime == null) {
			if (other.holdStartTime != null)
				return false;
		} else if (!holdStartTime.equals(other.holdStartTime))
			return false;
		if (id != other.id)
			return false;
		if (seatByLevelMap == null) {
			if (other.seatByLevelMap != null)
				return false;
		} else if (!seatByLevelMap.equals(other.seatByLevelMap))
			return false;
		return true;
	}

}
