package eean_games.tbsg._01;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import eean_games.main.DeepCopyable;
import eean_games.main._2DCoord;
import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.enumerable.eTileType;
import eean_games.tbsg._01.skill.Skill;
import eean_games.tbsg._01.status_effect.StatusEffect;
import eean_games.tbsg._01.unit.UnitInstance;

public final class ComplexCondition implements DeepCopyable<ComplexCondition>, Cloneable
{
    public ComplexCondition(List<List<Condition>> _conditionSets)
    {
        m_conditionSets = ListExtension.CoalesceNullAndReturnCopyOptionally(_conditionSets, eCopyType.Deep);
    }
    
    //Getters
    public List<List<Condition>> getConditionSets() { return Collections.unmodifiableList(m_conditionSets.stream().map(x -> Collections.unmodifiableList(x)).collect(Collectors.toList())); }
    /*List<Condition> represents the AND relationship between each Condition, and the List<Condition> instances have an OR relation in between.
   	Example:
    A && (B || C)  (Where A, B, C are instances of Condition class)
    which means (A and B) or (A and C)
    may be stored as
    ConditionSets[0][0] == A
    ConditionSets[0][1] == B
    ConditionSets[1][0] == A
    ConditionSets[1][1] == C*/
    //End Getters
    
    //Private Fields
    private List<List<Condition>> m_conditionSets;
    //End Private Fields

    //Public Methods
    public boolean IsTrue(BattleSystemCore _system, UnitInstance _effectHolder, StatusEffect _statusEffect)
    {
    	return IsTrue(_system, _effectHolder, _statusEffect, null, null, null, null, null, null, null, 0, 0, null, null, null, eTileType.values()[0]);
    }
    public boolean IsTrue(BattleSystemCore _system, UnitInstance _effectHolder, StatusEffect _statusEffect, UnitInstance _actor)
    {
    	return IsTrue(_system, null, _statusEffect, _actor, null, null, null, null, null, null, 0, 0, null, null, null, eTileType.values()[0]);
    }
    public boolean IsTrue(BattleSystemCore _system, UnitInstance _effectHolder, StatusEffect _statusEffect, UnitInstance _effectHolderOfActivatedEffect, StatusEffect _statusEffectActivated)
    {
    	return IsTrue(_system, _effectHolder, _statusEffect, null, null, null, null, null, null, null, 0, 0, null, _effectHolderOfActivatedEffect, _statusEffectActivated, eTileType.values()[0]);
    }
    public boolean IsTrue(BattleSystemCore _system, UnitInstance _effectHolder, StatusEffect _statusEffect, UnitInstance _actor, int _targetPreviousLocationTileIndex)
    {
    	return IsTrue(_system, _effectHolder, _statusEffect, _actor, null, null, null, null, null, null, 0, _targetPreviousLocationTileIndex, null, null, null, eTileType.values()[0]);
    }
    public boolean IsTrue(BattleSystemCore _system, UnitInstance _effectHolder, StatusEffect _statusEffect, UnitInstance _actor, Skill _skill, Effect _effect)
    {
    	return IsTrue(_system, _effectHolder, _statusEffect, _actor, _skill, _effect, null, null, null, null, 0, 0, null, null, null, eTileType.values()[0]);
    }
    public boolean IsTrue(BattleSystemCore _system, UnitInstance _actor, Skill _skill, Effect _effect, List<_2DCoord> _effectRange, Object _target)
    {
    	return IsTrue(_system, null, null, _actor, _skill, _effect, _effectRange, null, _target, null, 0, 0, null, null, null, eTileType.values()[0]);
    }
    public boolean IsTrue(BattleSystemCore _system, UnitInstance _actor, Skill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target)
    {
    	return IsTrue(_system, null, null, _actor, _skill, _effect, _effectRange, _targets, _target, null, 0, 0, null, null, null, eTileType.values()[0]);
    }
    public boolean IsTrue(BattleSystemCore _system, UnitInstance _actor, Skill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	return IsTrue(_system, null, null, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, 0, 0, null, null, null, eTileType.values()[0]);
    }
    public boolean IsTrue(BattleSystemCore _system, UnitInstance _effectHolder, StatusEffect _statusEffect, UnitInstance _actor, Skill _skill, List<_2DCoord> _effectRange, List<Object> _targets)
    {
    	return IsTrue(_system, _effectHolder, _statusEffect, _actor, _skill, null, _effectRange, _targets, null, null, 0, 0, null, null, null, eTileType.values()[0]);
    }
    public boolean IsTrue(BattleSystemCore _system, UnitInstance _effectHolder, StatusEffect _statusEffect, UnitInstance _actor, Skill _skill, List<_2DCoord> _effectRange, List<Object> _targets, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	return IsTrue(_system, _effectHolder, _statusEffect, _actor, _skill, null, _effectRange, _targets, null, _secondaryTargetsForComplexTargetSelectionEffect, 0, 0, null, null, null, eTileType.values()[0]);
    }
    public boolean IsTrue(BattleSystemCore _system, UnitInstance _effectHolder, StatusEffect _statusEffect, UnitInstance _actor, Skill _skill, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	return IsTrue(_system, _effectHolder, _statusEffect, _actor, _skill, null, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, 0, 0, null, null, null, eTileType.values()[0]);
    }
    public boolean IsTrue(BattleSystemCore _system, UnitInstance _effectHolder, StatusEffect _statusEffect, UnitInstance _actor, Skill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	return IsTrue(_system, _effectHolder, _statusEffect, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, 0, 0, null, null, null, eTileType.values()[0]);
    }
    public boolean IsTrue(BattleSystemCore _system, UnitInstance _effectHolder, StatusEffect _statusEffect, UnitInstance _actor, Skill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect, int _targetPreviousHP, int _targetPreviousLocationTileIndex, List<StatusEffect> _statusEffects, eTileType _previousTileType)
    {
    	return IsTrue(_system, _effectHolder, _statusEffect, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, _targetPreviousHP, _targetPreviousLocationTileIndex, _statusEffects, null, null, eTileType.values()[0]);
    }
    public boolean IsTrue(BattleSystemCore _system, UnitInstance _effectHolder, StatusEffect _statusEffect, UnitInstance _actor, Skill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect, int _targetPreviousHP, int _targetPreviousLocationTileIndex, List<StatusEffect> _statusEffects, UnitInstance _effectHolderOfActivatedEffect, StatusEffect _statusEffectActivated, eTileType _previousTileType)
    {
        if (m_conditionSets.size() > 0)
        {
            for (int i = 0; i < m_conditionSets.size(); i++)
            {
                if (InnerIsTrue(i, _system, _effectHolder, _statusEffect, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, _targetPreviousHP, _targetPreviousLocationTileIndex, _statusEffects, _effectHolderOfActivatedEffect, _statusEffectActivated, _previousTileType))
                    return true;
            }

            return false;
        }
        else
            return true;
    }

    public ComplexCondition DeepCopy()
    {
		try {
			ComplexCondition copy = (ComplexCondition)super.clone();
			
	        copy.m_conditionSets = ListExtension.DeepCopy(m_conditionSets);

	        return copy;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    //End Public Methods

    //Private Methods
    private boolean InnerIsTrue(int _innerListIndex, BattleSystemCore _system, UnitInstance _effectHolder, StatusEffect _statusEffect, UnitInstance _actor, Skill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect, int _targetPreviousHP, int _targetPreviousLocationTileIndex, List<StatusEffect> _statusEffects, UnitInstance _effectHolderOfActivatedEffect, StatusEffect _statusEffectActivated, eTileType _previousTileType)
    {
        for (Condition condition : getConditionSets().get(_innerListIndex))
        {
            if (!condition.IsTrue(_system, _effectHolder, _statusEffect, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, _targetPreviousHP, _targetPreviousLocationTileIndex, _statusEffects, _effectHolderOfActivatedEffect, _statusEffectActivated, _previousTileType))
                return false;
        }

        return true;
    }
    //End Private Methods
}
