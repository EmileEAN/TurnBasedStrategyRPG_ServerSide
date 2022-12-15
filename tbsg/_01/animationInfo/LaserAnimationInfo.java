package eean_games.tbsg._01.animationInfo;

public class LaserAnimationInfo extends AnimationInfo
{
	public LaserAnimationInfo(int _laserGenerationPointId, int _laserEffectId, int _hitEffectId)
	{
		laserGenerationPointId = _laserGenerationPointId;
		laserEffectId = _laserEffectId;
		hitEffectId = _hitEffectId;
	}
	
	//Public Read-only Fields
	public final int laserGenerationPointId;
	public final int laserEffectId;
	public final int hitEffectId;
	//End Public Read-only Fields
}
