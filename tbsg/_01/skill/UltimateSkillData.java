package eean_games.tbsg._01.skill;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.status_effect.BackgroundStatusEffectData;

public class UltimateSkillData extends ActiveSkillData implements DeepCopyable<SkillData>
{
    public UltimateSkillData(int _id, String _name, byte[] _iconAsBytes, List<BackgroundStatusEffectData> _temporalStatusEffectsData, int _skillActivationAnimationId, Tag _maxNumberOfTargets, Effect _effect)
    {
    	super(_id, _name, _iconAsBytes, _temporalStatusEffectsData, _skillActivationAnimationId, _maxNumberOfTargets, _effect);
    }

    //Public Methods
    public UltimateSkillData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected UltimateSkillData DeepCopyInternally() { return (UltimateSkillData)super.DeepCopyInternally(); }
    //End Protected Methods
}
