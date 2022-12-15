package eean_games.tbsg._01.equipment;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.RarityMeasurable;
import eean_games.tbsg._01.enumerable.eArmourClassification;
import eean_games.tbsg._01.enumerable.eGender;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.status_effect.StatusEffectData;

public class ArmourData extends EquipmentData implements DeepCopyable<EquipmentData>, RarityMeasurable
{
    public ArmourData(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, List<StatusEffectData> _statusEffectsData,
        eArmourClassification _armourClassification, eGender _targetGender)
    {
    	super(_id, _name, _iconAsBytes, _rarity, _statusEffectsData);
    	
        ArmourClassification = _armourClassification;
        TargetGender = _targetGender;
    }

    //Public Read-only Fields
    public final eArmourClassification ArmourClassification;

    public final eGender TargetGender;
    //End Public Read-only Fields

    //Getters
    public eRarity getRarity() { return rarity; }
    //End Getters
    
    //Public Methods
    public ArmourData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected ArmourData DeepCopyInternally() { return (ArmourData)super.DeepCopyInternally(); }
    //End Protected Methods
}
