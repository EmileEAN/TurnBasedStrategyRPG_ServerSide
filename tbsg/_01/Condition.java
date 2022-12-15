package eean_games.tbsg._01;

import java.util.List;

import eean_games.main.*;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.enumerable.eTileType;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;
import eean_games.tbsg._01.skill.Skill;
import eean_games.tbsg._01.status_effect.StatusEffect;
import eean_games.tbsg._01.unit.UnitInstance;

public final class Condition implements DeepCopyable<Condition>, Cloneable
{
    public Condition(Tag _a, eRelationType _relationType, Tag _b)
    {
        A = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_a, true);

        RelationType = _relationType;

        B = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_b, true);
    }

    //Public Read-only Fields
    public final eRelationType RelationType;
    //End Public Read-only Fields
    
    //Getters
    public Tag getA() { return A; }
    public Tag getB() { return B; }
    //End Getters
    
    //Private Fields
    private Tag A;
    private Tag B;
    //End Private Fields

    //Public Methods
    public boolean IsTrue(BattleSystemCore _system, UnitInstance _effectHolder, StatusEffect _statusEffect, UnitInstance _actor, Skill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect, int _targetPreviousHP, int _targetPreviousLocationTileIndex, List<StatusEffect> _statusEffects, UnitInstance _effectHolderOfActivatedEffect, StatusEffect _statusEffectActivated, eTileType _previousTileType)
    {
        try
        {
            Object valueA = A.ToValue(Object.class, _system, _effectHolder, _statusEffect, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, _targetPreviousHP, _targetPreviousLocationTileIndex, _statusEffects, _effectHolderOfActivatedEffect, _statusEffectActivated, _previousTileType);
            Object valueB = B.ToValue(Object.class, _system, _effectHolder, _statusEffect, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, _targetPreviousHP, _targetPreviousLocationTileIndex, _statusEffects, _effectHolderOfActivatedEffect, _statusEffectActivated, _previousTileType);

            return CoreFunctions.Compare(valueA, RelationType, valueB);
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public Condition DeepCopy()
    {
		try {
			Condition copy = (Condition)super.clone();
			
	        copy.A = A.DeepCopy();
	        copy.B = B.DeepCopy();

	        return copy;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    //End Public Methods
}
