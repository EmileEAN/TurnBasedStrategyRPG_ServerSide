package eean_games.tbsg._01.enumerable;

public enum eTargetRangeClassification {
    //4 Coordinates
    Cross_I(0),
    DiagonalCross_I(1),

    //8 Coordinates
    Cross_II(2),
    DiagonalCross_II(3),
    Square(4),
    Knight(5),
    FixedDistance_II(6),

    //12 Coordinates
    Cross_III(7),
    DiagonalCross_III(8),
    Cross_Alter(9),
    DiagonalCross_Alter(10),
    Cross_Knight(11),
    DiagonalCross_Knight(12),
    FixedDistance_III(13);
	
	private int index;
	
	private eTargetRangeClassification(int _index) {
		index = _index;
	}
	
	public int index() {
		return index;
	}
}
