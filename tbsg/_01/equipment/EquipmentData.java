package eean_games.tbsg._01.equipment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main.extension_method.ArrayExtension;
import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.StringExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.RarityMeasurable;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.status_effect.StatusEffectData;

public abstract class EquipmentData implements DeepCopyable<EquipmentData>, Cloneable, RarityMeasurable
{
    public EquipmentData(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, List<StatusEffectData> _statusEffectsData)
    {
        Id = _id;

        Name = StringExtension.CoalesceNull(_name);

        m_iconAsBytes = ArrayExtension.CoalesceNullAndReturnCopyOptionally(_iconAsBytes, true);

        rarity = _rarity;

        m_statusEffectsData = ListExtension.CoalesceNullAndReturnCopyOptionally(_statusEffectsData, eCopyType.Deep);
    }

    //Public Read-only Fields
    public final int Id;
    public final String Name;
    //End Public Read-only Fields
    
    //Getters
    public byte[] getIconAsBytes() { return m_iconAsBytes; }

    
    
    public List<StatusEffectData> getStatusEffectsData() { return Collections.unmodifiableList(m_statusEffectsData); }
    //End Getters
    
    //Protected Read-only Fields
    protected final eRarity rarity;
    //End Protected Read-only Fields
    
    //Private Fields
    private byte[] m_iconAsBytes;
    
    private List<StatusEffectData> m_statusEffectsData;
    //End Private Fields

    //Public Methods
    public EquipmentData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected EquipmentData DeepCopyInternally()
    {
		try {
			EquipmentData copy = (EquipmentData)super.clone();
			
	        copy.m_iconAsBytes = Arrays.copyOf(m_iconAsBytes, m_iconAsBytes.length);

	        copy.m_statusEffectsData = ListExtension.DeepCopy(m_statusEffectsData);

	        return copy;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
    }
    //End Protected Methods
}
