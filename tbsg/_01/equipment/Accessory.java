package eean_games.tbsg._01.equipment;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.enumerable.eAccessoryClassification;
import eean_games.tbsg._01.enumerable.eGender;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;
import eean_games.tbsg._01.status_effect.StatusEffectData;

public class Accessory implements DeepCopyable<Accessory>, Cloneable
{
    public Accessory(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, List<StatusEffectData> _statusEffectsData, eAccessoryClassification _accessoryClassification, eGender _targetGender,
                     int _uniqueId, boolean _isLocked)
    {
        BaseInfo = new AccessoryData(_id, _name, _iconAsBytes, _rarity, _statusEffectsData, _accessoryClassification, _targetGender);

        UniqueId = _uniqueId;
        
        IsLocked = _isLocked;
    }
    public Accessory(AccessoryData _accessoryData, int _uniqueId, boolean _isLocked)
    {
        BaseInfo = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_accessoryData, false);

        UniqueId = _uniqueId;
        
        IsLocked = _isLocked;
    }

    //Public Read-only Fields
    public final AccessoryData BaseInfo; //Store reference to original instance

    public final int UniqueId;
    //End Public Read-only Fields
    
    //Public Fields
    public boolean IsLocked;
    //End Public Fields

    //Public Methods
    public Accessory DeepCopy()
    {
        try {
			return (Accessory)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
    }
    //End Public Methods
}
