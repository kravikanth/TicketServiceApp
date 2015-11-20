package com.mycompany.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mycompany.model.Level.Type;

public class SimplePerformanceVenue {

	private static final int ORCHESTRA_ROWS = 25;

	private static final int ORCHESTRA_SEATS_IN_ROW = 50;

	private static final int MAIN_ROWS = 20;

	private static final int BALCONY1_ROWS = 15;

	private static final int BALCONY2_ROWS = 15;

	private static final int MAIN_SEATS_IN_ROW = 100;

	private static final int BALCONY1_SEATS_IN_ROW = 100;

	private static final int BALCONY2_SEATS_IN_ROW = 100;

	// Stage is out of scope for this application,
	// but added for completeness of the class
	private final Stage stage = new Stage();

	private final List<Level> levels;
	private final List<Type> levelTypes;

	public SimplePerformanceVenue() {

		List<Level> levelsTemp = new ArrayList<>();
		List<Type> levelTypesTemp = new ArrayList<>();

		Type orchestraLevelType = new Type(1, "Orchestra", 100.00);
		Type mainLevelType = new Type(2, "Main", 75.00);
		Type balcony1LevelType = new Type(3, "Balcony 1", 50.00);
		Type balcony2LevelType = new Type(4, "Balcony 2", 40.00);

		Level orchestraLevel = new Level(orchestraLevelType, ORCHESTRA_ROWS, ORCHESTRA_SEATS_IN_ROW);
		Level mainLevel = new Level(mainLevelType, MAIN_ROWS, MAIN_SEATS_IN_ROW);
		Level balcony1Level = new Level(balcony1LevelType, BALCONY1_ROWS, BALCONY1_SEATS_IN_ROW);
		Level balcony2Level = new Level(balcony2LevelType, BALCONY2_ROWS, BALCONY2_SEATS_IN_ROW);

		levelsTemp.add(orchestraLevel);
		levelsTemp.add(mainLevel);
		levelsTemp.add(balcony1Level);
		levelsTemp.add(balcony2Level);
		levelTypesTemp.add(orchestraLevelType);
		levelTypesTemp.add(mainLevelType);
		levelTypesTemp.add(balcony1LevelType);
		levelTypesTemp.add(balcony2LevelType);
		levels = Collections.unmodifiableList(levelsTemp);
		levelTypes = Collections.unmodifiableList(levelTypesTemp);

	}

	public List<Level> getLevels() {
		return levels;
	}
	public List<Type> getLevelTypes() {
		return levelTypes;
	}
	public Stage getStage() {
		return stage;
	}

	@Override
	public String toString() {
		return "SimplePerformanceVenue [stage=" + stage + ", levels=" + levels + "]";
	}

}
