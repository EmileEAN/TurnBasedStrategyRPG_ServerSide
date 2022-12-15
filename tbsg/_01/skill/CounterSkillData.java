package eean_games.tbsg._01.skill;

import java.util.List;
import java.util.Map;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.enumerable.eEventTriggerTiming;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;
import eean_games.tbsg._01.status_effect.BackgroundStatusEffectData;

public class CounterSkillData extends CostRequiringSkillData implements DeepCopyable<SkillData>
{
    public CounterSkillData(int _id, String _name, byte[] _iconAsBytes, List<BackgroundStatusEffectData> _temporalStatusEffectsData, int _skillActivationAnimationId, Tag _maxNumberOfTargets, Effect _effect, int _spCost, Map<Integer, Integer> _itemCosts,
        eEventTriggerTiming _eventTriggerTiming, ComplexCondition _activationCondition)
    {
    	super(_id, _name, _iconAsBytes, _temporalStatusEffectsData, _skillActivationAnimationId, _maxNumberOfTargets, _effect, _spCost, _itemCosts);
    	
    	EventTriggerTiming = _eventTriggerTiming;
    	ActivationCondition = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_activationCondition, true);
    }
    
    //Public Read-only Fields
    public eEventTriggerTiming EventTriggerTiming;
    //End Public Read-only Fields

    //Getters
    public ComplexCondition getActivationCondition() { return ActivationCondition; }
    //End Getters
    
    //Private Fields
    private ComplexCondition ActivationCondition;
    //End Private Fields
    
    //Public Methods
    public CounterSkillData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods
    

    //Protected Methods
    @Override
    protected CounterSkillData DeepCopyInternally() 
    { 
    	CounterSkillData copy = (CounterSkillData)super.DeepCopyInternally(); 
    	
        copy.ActivationCondition = ActivationCondition.DeepCopy();

        return copy;
    }
    //End Protected Methods
}
