package eean_games.tbsg._01.effect;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.animationInfo.AnimationInfo;
import eean_games.tbsg._01.enumerable.eTargetUnitClassification;

public class UnitSwapEffect extends UnitTargetingEffect implements DeepCopyable<Effect>
{
    public UnitSwapEffect(int _id, ComplexCondition _activationCondition, Tag _successRate, List<Effect> _secondaryEffects, AnimationInfo _animationInfo, eTargetUnitClassification _targetClassification)
    {
    	super(_id, _activationCondition, Tag.newTag("<#1/>"), _successRate, null, _secondaryEffects, _animationInfo, _targetClassification);
    }

    //Public Methods
    public UnitSwapEffect DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected UnitSwapEffect DeepCopyInternally() { return (UnitSwapEffect)super.DeepCopyInternally(); }
    //End Protected Methods
}
