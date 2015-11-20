package com.mycompany.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mycompany.model.Level.Type;

public class Row {

	private final Type levelType;
	private final int rowNumber;
	private final List<Seat> seats;

	public List<Seat> getSeats() {
		return seats;
	}

	public Row(Type type, int rowNumber, int seatsInRow) {
		super();
		this.rowNumber = rowNumber;
		this.levelType = type;
		List<Seat> seatList = new ArrayList<>();
		for (int seatNumber = 1; seatNumber <= seatsInRow; seatNumber++) {
			seatList.add(new Seat(type, rowNumber, seatNumber));
		}
		this.seats = Collections.unmodifiableList(seatList);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((levelType == null) ? 0 : levelType.hashCode());
		result = prime * result + rowNumber;
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
		Row other = (Row) obj;
		if (levelType == null) {
			if (other.levelType != null)
				return false;
		} else if (!levelType.equals(other.levelType))
			return false;
		if (rowNumber != other.rowNumber)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Row [rowNumber=" + rowNumber + ", seats=" + seats + "]";
	}

}
