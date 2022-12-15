package eean_games.tbsg._01;

import eean_games.tbsg._01.enumerable.eEffectiveness;

public class DamageInfo
{
	public DamageInfo(int _damage, boolean _isCritical, eEffectiveness _effectiveness)
	{
		damage = _damage;
		isCritical = _isCritical;
		effectiveness = _effectiveness;
	}
	
	public int damage;
	public boolean isCritical;
	public eEffectiveness effectiveness;
}
