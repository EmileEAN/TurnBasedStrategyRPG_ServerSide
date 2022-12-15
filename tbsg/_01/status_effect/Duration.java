package eean_games.tbsg._01.status_effect;

import java.math.BigDecimal;
import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main._2DCoord;
import eean_games.tbsg._01.BattleSystemCore;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;
import eean_games.tbsg._01.skill.ActiveSkill;
import eean_games.tbsg._01.unit.UnitInstance;

public final class Duration implements DeepCopyable<Duration>, Cloneable
{
    public Duration(int _activationTimes, BigDecimal _turns, ComplexCondition _whileConditions)
    {
        ActivationTimes = _activationTimes;
        Turns = _turns;
        WhileCondition = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_whileConditions, true);
    }

    public Duration(DurationData _data, BattleSystemCore _system, StatusEffect _statusEffect, UnitInstance _effectUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        DurationData tmp_data = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_data, false);

        ActivationTimes = tmp_data.getActivationTimes().ToValue(Integer.class, _system, null, _statusEffect, _effectUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
        Turns = tmp_data.getTurns().ToValue(BigDecimal.class, _system, null, _statusEffect, _effectUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
        WhileCondition = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_data.getWhileCondition(), true);
    }

    //Public Methods
    public int ActivationTimes;
    public BigDecimal Turns; //Decimal number. 0.5 represents a player turn. 1 represents a turn of each player.
    //End Public Methods
    
    //Getters
    public ComplexCondition getWhileCondition() { return WhileCondition; }
    //End Getters
    
    //Private Fields
    private ComplexCondition WhileCondition;
    //End Private Fields

    //Public Methods
    public Duration DeepCopy()
    {
		try {
			Duration copy = (Duration)super.clone();
			
	        copy.WhileCondition = WhileCondition.DeepCopy();

	        return copy;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
    }
    //End Public Methods
}
