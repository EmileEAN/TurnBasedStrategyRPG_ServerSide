package eean_games.tbsg._01.skill;

import java.util.List;
import java.util.Map;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.status_effect.BackgroundStatusEffectData;

public class OrdinarySkillData extends CostRequiringSkillData implements DeepCopyable<SkillData>
{
    public OrdinarySkillData(int _id, String _name, byte[] _iconAsBytes, List<BackgroundStatusEffectData> _temporalStatusEffectsData, int _skillActivationAnimationId, Tag _maxNumberOfTargets, Effect _effect, int _spCost, Map<Integer, Integer> _itemCosts)
    {
    	super(_id, _name, _iconAsBytes, _temporalStatusEffectsData, _skillActivationAnimationId, _maxNumberOfTargets, _effect, _spCost, _itemCosts);
    }

    //Public Methods
    public OrdinarySkillData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected OrdinarySkillData DeepCopyInternally() { return (OrdinarySkillData)super.DeepCopyInternally(); }
    //End Protected Methods
}