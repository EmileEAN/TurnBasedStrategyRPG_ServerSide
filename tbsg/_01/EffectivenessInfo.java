package eean_games.tbsg._01;

import java.math.BigDecimal;

import eean_games.tbsg._01.enumerable.eEffectiveness;

public class EffectivenessInfo 
{
	public EffectivenessInfo(eEffectiveness _effectiveness, BigDecimal _correctionRate)
	{
		correctionRate = _correctionRate;
		effectiveness = _effectiveness;
	}
	
	public eEffectiveness effectiveness;
	public BigDecimal correctionRate;
}
