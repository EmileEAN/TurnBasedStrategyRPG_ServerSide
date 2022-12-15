package eean_games.tbsg._01.effect;

import java.util.*;
import eean_games.main.*;
import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.*;
import eean_games.tbsg._01.animationInfo.AnimationInfo;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;

public abstract class Effect implements DeepCopyable<Effect>, Cloneable
{
    public Effect(int _id, ComplexCondition _activationCondition, Tag _timesToApply, Tag _successRate, Tag _diffusionDistance, List<Effect> _secondaryEffects, AnimationInfo _animationInfo)
    {
    	Id = _id;
    	
        ActivationCondition = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_activationCondition, true);

        TimesToApply = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_timesToApply, true);

        SuccessRate = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_successRate, true);

        DiffusionDistance = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_diffusionDistance, true);

        m_secondaryEffects = ListExtension.CoalesceNullAndReturnCopyOptionally(_secondaryEffects, eCopyType.Shallow);

        AnimationInfo = _animationInfo;
        
        isSecondaryEffectListModifiable = true;
    }
    
    //Public Read-only Fields
    public final int Id;
    
    public final AnimationInfo AnimationInfo; //Will be null only for the base damage effect
    //End Public Read-only Fields
    
    //Getters
    public ComplexCondition getActivationCondition() { return ActivationCondition; }

    public Tag getTimesToApply() { return TimesToApply; } //Tag value must be positive integer
    public Tag getSuccessRate() { return SuccessRate; } //Tag value must be between 0 and 1

    public Tag getDiffusionDistance() { return DiffusionDistance; } //Tag value must be 0 or a positive integer

    public List<Effect> getSecondaryEffects() 
    { 
    	if (isSecondaryEffectListModifiable)
    		return m_secondaryEffects;
    	else
    		return Collections.unmodifiableList(m_secondaryEffects); 
    }
    //End Getters
    
    //Private Fields
    private ComplexCondition ActivationCondition;

    private Tag TimesToApply;
    private Tag SuccessRate;

    private Tag DiffusionDistance;

    private List<Effect> m_secondaryEffects;
    
    private boolean isSecondaryEffectListModifiable;
    //End Private Fields

    //Public Methods
    public void DisableModification() { isSecondaryEffectListModifiable = false; }
    
    public Effect DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected Effect DeepCopyInternally()
    {
		try 
		{
			Effect copy = (Effect)super.clone();
			
	        copy.ActivationCondition = ActivationCondition.DeepCopy();

	        copy.TimesToApply = TimesToApply.DeepCopy();
	        copy.SuccessRate = SuccessRate.DeepCopy();

	        copy.DiffusionDistance = SuccessRate.DeepCopy();

	        copy.m_secondaryEffects = ListExtension.DeepCopy(m_secondaryEffects);

	        return copy;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
    }
    //End Public Methods
}
