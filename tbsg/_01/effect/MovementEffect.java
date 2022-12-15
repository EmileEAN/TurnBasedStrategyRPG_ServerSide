package eean_games.tbsg._01.effect;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.animationInfo.MovementAnimationInfo;

public class MovementEffect extends TileTargetingEffect implements DeepCopyable<Effect>
{
    public MovementEffect(int _id, ComplexCondition _activationCondition, Tag _timesToAppy, List<Effect> _secondaryEffects, MovementAnimationInfo _animationInfo)
    {
    	super(_id, _activationCondition, _timesToAppy, Tag.one, Tag.zero, _secondaryEffects, _animationInfo);
    }

    //Public Methods
    public MovementEffect DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected MovementEffect DeepCopyInternally() { return (MovementEffect)super.DeepCopyInternally(); }
    //End Protected Methods
}
