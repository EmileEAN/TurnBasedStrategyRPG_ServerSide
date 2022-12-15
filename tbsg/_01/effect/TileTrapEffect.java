package eean_games.tbsg._01.effect;

import java.util.Collections;
import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.animationInfo.AnimationInfo;

public class TileTrapEffect extends TileTargetingEffect implements DeepCopyable<Effect>
{
    public TileTrapEffect(int _id, ComplexCondition _activationCondition, Tag _timesToApply, Tag _successRate, Tag _diffusionDistance, List<Effect> _secondaryEffects, AnimationInfo _animationInfo,
        List<Effect> _effectsToAttach)
    {
    	super(_id, _activationCondition, _timesToApply, _successRate, _diffusionDistance, _secondaryEffects, _animationInfo);
    	
        m_effectsToAttach = ListExtension.CoalesceNullAndReturnCopyOptionally(_effectsToAttach, eCopyType.Shallow);
    }

    //Getters
    public List<Effect> getEffectsToAttach() { return Collections.unmodifiableList(m_effectsToAttach); }
    //End Getters

    //Private Fields
    private List<Effect> m_effectsToAttach;
    //End Private Fields

    //Public Methods
    public TileTrapEffect DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected TileTrapEffect DeepCopyInternally()
    {
        TileTrapEffect copy = (TileTrapEffect)super.DeepCopyInternally();

        copy.m_effectsToAttach = ListExtension.DeepCopy(m_effectsToAttach);

        return copy;
    }
    //End Protected Methods
}
