package eean_games.tbsg._01.effect;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.animationInfo.AnimationInfo;

public class TileSwapEffect extends TileTargetingEffect implements DeepCopyable<Effect>
{
    public TileSwapEffect(int _id, ComplexCondition _activationCondition, Tag _timesToApply, Tag _successRate, Tag _diffusionDistance, List<Effect> _secondaryEffects, AnimationInfo _animationInfo)
    {
    	super(_id, _activationCondition, _timesToApply, _successRate, _diffusionDistance, _secondaryEffects, _animationInfo);
    }

    //Public Methods
    public TileSwapEffect DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected TileSwapEffect DeepCopyInternally() { return (TileSwapEffect)super.DeepCopyInternally(); }
    //End Protected Methods
}