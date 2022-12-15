package eean_games.tbsg._01.enumerable;

import eean_games.tbsg._01.CoreValues;

public enum eRarity {
    //The numbers express the max level of units of the rarity
    Common(CoreValues.LEVEL_DIFFERENCE_BETWEEN_RARITIES),
    Uncommon(2 * CoreValues.LEVEL_DIFFERENCE_BETWEEN_RARITIES),
    Rare(3 * CoreValues.LEVEL_DIFFERENCE_BETWEEN_RARITIES),
    Epic(4 * CoreValues.LEVEL_DIFFERENCE_BETWEEN_RARITIES),
    Legendary(5 * CoreValues.LEVEL_DIFFERENCE_BETWEEN_RARITIES);
	
	private int numericValue;
	
	private eRarity(int _numericValue) {
		numericValue = _numericValue;
	}
	
	public int numericValue() {
		return numericValue;
	}
}
