package eean_games.tbsg._01.effect;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.animationInfo.AnimationInfo;
import eean_games.tbsg._01.enumerable.eTargetUnitClassification;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;
import eean_games.tbsg._01.status_effect.StatusEffectData;

public class StatusEffectAttachmentEffect extends UnitTargetingEffect implements DeepCopyable<Effect>
{
    public StatusEffectAttachmentEffect(int _id, ComplexCondition _activationCondition, Tag _timesToApply, Tag _successRate, Tag _diffusionDistance, List<Effect> _secondaryEffects, AnimationInfo _animationInfo, eTargetUnitClassification _targetClassification,
        StatusEffectData _dataOfStatusEffectToAttach)
    {
    	super(_id, _activationCondition, _timesToApply, _successRate, _diffusionDistance, _secondaryEffects, _animationInfo, _targetClassification);
    	
        DataOfStatusEffectToAttach = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_dataOfStatusEffectToAttach, true);
    }

    //Getters
    public StatusEffectData getDataOfStatusEffectToAttach() { return DataOfStatusEffectToAttach; }
    //End Getters
    
    //Private Fields
    private StatusEffectData DataOfStatusEffectToAttach;
    //End Private Fields

    //Public Methods
    public StatusEffectAttachmentEffect DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods
    
    //Protected Methods
    @Override
    protected StatusEffectAttachmentEffect DeepCopyInternally()
    {
        StatusEffectAttachmentEffect copy = (StatusEffectAttachmentEffect)super.DeepCopyInternally();

        copy.DataOfStatusEffectToAttach = DataOfStatusEffectToAttach.DeepCopy();

        return copy;
    }
    //End Protected Methods
}
