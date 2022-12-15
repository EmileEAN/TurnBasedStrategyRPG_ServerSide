package eean_games.tbsg._01.gacha;

import eean_games.tbsg._01.RarityMeasurable;

public class GachaObjectInfo 
{
	public GachaObjectInfo(RarityMeasurable _object, int _relativeOccurenceValue)
	{
		object = _object;
		relativeOccurenceValue = _relativeOccurenceValue;
	}
	
    public RarityMeasurable object;
    public int relativeOccurenceValue;
}
