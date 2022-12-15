package eean_games.tbsg._01.animationInfo;

public class ProjectileAnimationInfo extends AnimationInfo
{
	public ProjectileAnimationInfo(int _projectileGenerationPointId, int _projectileId, int _hitEffectId)
	{
		projectileGenerationPointId = _projectileGenerationPointId;
		projectileId = _projectileId;
		hitEffectId = _hitEffectId;
	}
	
	//Public Read-only Fields
	public final int projectileGenerationPointId;
	public final int projectileId;
	public final int hitEffectId;
	//End Public Read-only Fields
}
