package eean_games.tbsg._01.skill;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.enumerable.eTargetUnitClassification;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;
import eean_games.tbsg._01.status_effect.StatusEffectData;

public class PassiveSkillData extends SkillData implements DeepCopyable<SkillData>
{
    public PassiveSkillData(int _id, String _name, byte[] _iconAsBytes, List<StatusEffectData> _statusEffectsData, int _skillActivationAnimationId,
        eTargetUnitClassification _targetClassification, ComplexCondition _activationCondition)
    {
    	super(_id, _name, _iconAsBytes, _statusEffectsData, _skillActivationAnimationId);
    	
        TargetClassification = _targetClassification;
        ActivationCondition = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_activationCondition, true);
    }

    //Public Read-only Fields
    public final eTargetUnitClassification TargetClassification;
    //End Public Read-only Fields
    
    //Getters
    public ComplexCondition getActivationCondition() { return ActivationCondition; }
    //End Getters
    
    //Private Fields
    private ComplexCondition ActivationCondition;
    //End Private Fields

    //Public Methods
    public PassiveSkillData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected PassiveSkillData DeepCopyInternally() 
    {
        PassiveSkillData copy = (PassiveSkillData)super.DeepCopyInternally();

        copy.ActivationCondition = ActivationCondition.DeepCopy();

        return copy;
    }
    //End Protected Methods
}
