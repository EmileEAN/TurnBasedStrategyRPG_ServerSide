package eean_games.tbsg._01.equipment;

import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.enumerable.eArmourClassification;
import eean_games.tbsg._01.enumerable.eGender;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;
import eean_games.tbsg._01.status_effect.StatusEffectData;

public class Armour implements DeepCopyable<Armour>, Cloneable
{
    public Armour(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, List<StatusEffectData> _statusEffectsData, eArmourClassification _armourClassification, eGender _targetGender,
                    int _uniqueId, boolean _isLocked)
    {
        BaseInfo = new ArmourData(_id, _name, _iconAsBytes, _rarity, _statusEffectsData, _armourClassification, _targetGender);

        UniqueId = _uniqueId;
        
        IsLocked = _isLocked;
    }
    public Armour(ArmourData _armourData, int _uniqueId, boolean _isLocked)
    {
        BaseInfo = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_armourData, false);

        UniqueId = _uniqueId;
        
        IsLocked = _isLocked;
    }

    //Public Read-only Fields
    public final ArmourData BaseInfo; //Store reference to original instance

    public final int UniqueId;
    //End Public Read-only Fields

    //Public Fields
    public boolean IsLocked;
    //End Public Fields
    
    //Public Methods
    public Armour DeepCopy()
    {
        try {
			return (Armour)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
    }
    //End Public Methods
}
