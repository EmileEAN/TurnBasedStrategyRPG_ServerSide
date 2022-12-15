package eean_games.tbsg._01.gacha;

public class AlternativeDispensationInfo 
{
	public AlternativeDispensationInfo(int _applyAtXthDispensation, ValuePerRarity _ratioPerRarity)
	{
		applyAtXthDispensation = _applyAtXthDispensation;
		ratioPerRarity = _ratioPerRarity;
	}
	
    public int applyAtXthDispensation;
    public ValuePerRarity ratioPerRarity;
}
