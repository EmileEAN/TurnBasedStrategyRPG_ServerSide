package eean_games.tbsg._01.enumerable;

public enum eStatusType {
    MaxHP,
    PhyStr,
    PhyRes,
    MagStr,
    MagRes,
    Vit,
    Pre,
    Eva,
    Cri,
    CriRes,
    DamageForce,
    FixedDamage, // Add/Subtract or Multiply/Divide value to final healing amount for the target of effect used by the StatusEffectHolder
    DamageResistance, // Add/Subtract or Multiply/Divide value to final healing amount when the StatusEffectHolder is being healed
    HealForce,
    FixedHeal, // Add/Subtract or Multiply/Divide value to final healing amount for the target of effect used by the StatusEffectHolder
    FixedHeal_Self, // Add/Subtract or Multiply/Divide value to final healing amount when the StatusEffectHolder is being healed
    NumOfTargets
}
