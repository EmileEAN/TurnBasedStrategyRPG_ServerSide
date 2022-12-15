package eean_games.tbsg._01.equipment;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.RarityMeasurable;
import eean_games.tbsg._01.enumerable.eAccessoryClassification;
import eean_games.tbsg._01.enumerable.eGender;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.status_effect.StatusEffectData;

public class AccessoryData extends EquipmentData implements DeepCopyable<EquipmentData>, RarityMeasurable
{
    public AccessoryData(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, List<StatusEffectData> _statusEffectsData,
        eAccessoryClassification _accessoryClassification, eGender _targetGender)
    {
    	super(_id, _name, _iconAsBytes, _rarity, _statusEffectsData);
    	
        accessoryClassification = _accessoryClassification;
        TargetGender = _targetGender;
    }

    //Public Read-only Fields
    public final eAccessoryClassification accessoryClassification;

    public final eGender TargetGender;
    //End Public Read-only Fields

    //Getters
    public eRarity getRarity() { return rarity; }
    //End Getters
    
    //Public Methods
    public AccessoryData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected AccessoryData DeepCopyInternally() { return (AccessoryData)super.DeepCopyInternally(); }
    //End Protected Methods
}
