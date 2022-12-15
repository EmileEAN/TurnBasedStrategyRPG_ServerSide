package eean_games.tbsg._01.gacha;

import eean_games.tbsg._01.enumerable.eRarity;

public class ValuePerRarity 
{
	public ValuePerRarity(int _common, int _uncommon, int _rare, int _epic, int _legendary)
	{
		common = _common;
		uncommon = _uncommon;
		rare = _rare;
		epic = _epic;
		legendary = _legendary;
	}
	
    public int legendary;
    public int epic;
    public int rare;
    public int uncommon;
    public int common;

    public int getTotalValue() { return common + uncommon + rare + epic + legendary; }
    public eRarity occurrenceValueToRarity(int _value)
    {
        if (_value < 1 || _value > getTotalValue())
            return eRarity.values()[0];

        if (_value > getTotalValue() - legendary)
            return eRarity.Legendary;
        else if (_value > getTotalValue() - legendary - epic)
            return eRarity.Epic;
        else if (_value > common + uncommon)
            return eRarity.Rare;
        else if (_value > common)
            return eRarity.Uncommon;
        else
            return eRarity.Common;
    }
}
