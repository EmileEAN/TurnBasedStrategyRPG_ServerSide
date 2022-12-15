package eean_games.tbsg._01.enumerable;

public enum eEventTriggerTiming {
    BeginningOfMatch, // eActivationTurnClassification will be ignored

    //All the values below might be used in conjunction with eActivationTurnClassification
    BeginningOfTurn,

    //Can be triggered by actions executed by Units or any other event not produced by a Unit
    OnStatusEffectActivated,

    //When a Unit is performing an action
    OnActionExecuted,
    OnMoved,
    OnAttackExecuted,
    OnActiveSkillExecuted,
    OnItemUsed,
    OnEffectSuccess,
    OnEffectFailure,

    //When an action has been done against a Unit
    OnTargetedByAction,
    OnTargetedByAttack,
    OnTargetedBySkill,
    OnTargetedByItemSkill,
    OnHitByEffect,
    OnEvadedEffect,

    EndOfTurn
}
