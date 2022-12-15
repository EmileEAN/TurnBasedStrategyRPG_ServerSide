package eean_games.tbsg._01.skill;

import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;
import eean_games.tbsg._01.status_effect.BackgroundStatusEffectData;
import eean_games.tbsg._01.status_effect.StatusEffectData;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main.Linq;
import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.effect.Effect;

public abstract class ActiveSkillData extends SkillData implements DeepCopyable<SkillData>
{
    public ActiveSkillData(int _id, String _name, byte[] _iconAsBytes, List<BackgroundStatusEffectData> _temporalStatusEffectsData, int _skillActivationAnimationId,
        Tag _maxNumberOfTargets, Effect _effect)
    {
    	super(_id, _name, _iconAsBytes, Linq.ofType(ListExtension.CoalesceNullAndReturnCopyOptionally(_temporalStatusEffectsData, eCopyType.None), StatusEffectData.class), _skillActivationAnimationId);
    	
        MaxNumberOfTargets = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_maxNumberOfTargets, true);

        Effect = _effect; // Get the reference to the original instance
    }
    
    //Getters
    public Tag getMaxNumberOfTargets() { return MaxNumberOfTargets; } //Tag value must be positive integer
    
    public Effect getEffect() { return Effect; }
    //End Getters
    
    //Private Fields
    private Tag MaxNumberOfTargets; //Tag value must be positive integer
    
    private Effect Effect;
    //End Private Fields
    
    //Public Methods
    public ActiveSkillData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected ActiveSkillData DeepCopyInternally()
    {
        ActiveSkillData copy = (ActiveSkillData)super.DeepCopyInternally();

        copy.MaxNumberOfTargets = MaxNumberOfTargets.DeepCopy();

        copy.Effect = Effect;

        return copy;
    }
    //End Protected Methods
}
