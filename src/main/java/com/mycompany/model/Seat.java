package com.mycompany.model;

import com.mycompany.model.Level.Type;

public class Seat {

	private final Type type;
	private final int row;
	private final int seatNumber;

	public Seat(Type type, int row, int seatNumber) {
		super();
		this.type = type;
		this.row = row;
		this.seatNumber = seatNumber;
	}

	public Type getType() {
		return type;
	}

	public int getRow() {
		return row;
	}

	public int getSeatNumber() {
		return seatNumber;
	}

	@Override
	public String toString() {
		return "Seat [type=" + type + ", row=" + row + ", seatNumber=" + seatNumber + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + row;
		result = prime * result + seatNumber;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Seat other = (Seat) obj;
		if (row != other.row)
			return false;
		if (seatNumber != other.seatNumber)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
