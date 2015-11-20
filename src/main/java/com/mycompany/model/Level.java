package com.mycompany.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level {
	
	public static class Type {

		private final int id;
		private final String levelName;
		private final double price$;

		public Type(int levelId, String levelName, double price) {
			super();
			this.id = levelId;
			this.levelName = levelName;
			this.price$ = price;
		}

		public int getId() {
			return id;
		}

		public String getLevelName() {
			return levelName;
		}

		public double getPrice() {
			return price$;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
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
			Type other = (Type) obj;
			if (id != other.id)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "LevelType [levelId=" + id + ", levelName=" + levelName + ", price$=" + price$ + "]";
		}

	}
	
	private final Type type;
	private final List<Row> rows;

	public Level(Type type, int rowsCount, int seatsInRow) {
		super();
		this.type = type;
		List<Row> rows = new ArrayList<>();
		for (int rowNumber = 1; rowNumber <= rowsCount; rowNumber++) {
			rows.add(new Row(type, rowNumber, seatsInRow));
		}

		this.rows = Collections.unmodifiableList(rows);
	}

	public Type getType() {
		return type;
	}

	public List<Row> getRows() {
		return rows;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Level other = (Level) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Level [type=" + type + ", rows=" + rows + "]";
	}

}
