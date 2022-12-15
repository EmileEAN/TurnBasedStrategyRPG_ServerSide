package eean_games.tbsg._01.gacha;

public enum eGachaClassification {
    Unit,
    Weapon,
    Armour,
    Accessory,
    SkillItem,
    SkillMaterial,
    ItemMaterial,
    EquipmentMaterial,
    EvolutionMaterial,
    WeaponEnhMaterial, //This is named WeaponEnhMaterial instead of WeaponEnhancementMaterial because of the limitations of the length of the corresponding table name
    UnitEnhMaterial, //This is named UnitEnhMaterial instead of UnitEnhancementMaterial because of the limitations of the length of the corresponding table name
    SkillEnhMaterial //This is named SkillEnhMaterial instead of SkillEnhancementMaterial because of the limitations of the length of the corresponding table name
}
