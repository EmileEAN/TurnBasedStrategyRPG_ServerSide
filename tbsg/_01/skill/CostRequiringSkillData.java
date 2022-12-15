package eean_games.tbsg._01.skill;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eean_games.main.DeepCopyable;
import eean_games.main.extension_method.MapExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.CoreValues;
import eean_games.tbsg._01.Tag;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.status_effect.BackgroundStatusEffectData;

public abstract class CostRequiringSkillData extends ActiveSkillData implements DeepCopyable<SkillData>
{
    public CostRequiringSkillData(int _id, String _name, byte[] _iconAsBytes, List<BackgroundStatusEffectData> _temporalStatusEffectsData, int _skillActivationAnimationId, Tag _maxNumberOfTargets, Effect _effect,
        int _spCost, Map<Integer, Integer> _itemCosts)
    {
    	super(_id, _name, _iconAsBytes, _temporalStatusEffectsData, _skillActivationAnimationId, _maxNumberOfTargets, _effect);
    	
        if (_spCost < CoreValues.MIN_SP_COST)
            _spCost = CoreValues.MIN_SP_COST;
        else if (_spCost > CoreValues.MAX_SP)
            _spCost = CoreValues.MAX_SP;

        SPCost = _spCost;

        m_itemCosts = MapExtension.CoalesceNullAndReturnCopyOptionally(_itemCosts, eCopyType.Deep);
    }

    //Public Read-only Fields
    public final int SPCost;
    //End Public Read-only Fields
    
    //Getters
    public Map<Integer, Integer> getItemCosts() { return Collections.unmodifiableMap(m_itemCosts); } //Key => Item Id, Value => Quantity
    //End Getters

    //Private Fields
    private Map<Integer, Integer> m_itemCosts;
    //End Private Fields

    //Public Methods
    public CostRequiringSkillData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected CostRequiringSkillData DeepCopyInternally()
    {
        CostRequiringSkillData copy = (CostRequiringSkillData)super.DeepCopyInternally();

        copy.m_itemCosts = new HashMap<Integer, Integer>(m_itemCosts); //Shallow Copy

        return copy;
    }
    //End Protected Methods
}
