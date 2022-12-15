package eean_games.tbsg._01.skill;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main.extension_method.ArrayExtension;
import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.StringExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.status_effect.StatusEffectData;

public abstract class SkillData implements DeepCopyable<SkillData>, Cloneable
{
    public SkillData(int _id, String _name, byte[] _iconAsBytes, List<StatusEffectData> _statusEffectsData, int _skillActivationSkillActivationAnimationId)
    {
        Id = _id;

        Name = StringExtension.CoalesceNull(_name);

        m_iconAsBytes = ArrayExtension.CoalesceNullAndReturnCopyOptionally(_iconAsBytes, true);

        m_statusEffectsData = ListExtension.CoalesceNullAndReturnCopyOptionally(_statusEffectsData, eCopyType.Deep);

        SkillActivationAnimationId = _skillActivationSkillActivationAnimationId;
    }

    //Public Read-only Fields
    public final int Id;
    public final String Name;
    
    public final int SkillActivationAnimationId;
    //End Public Read-only Fields
    
    //Getters
    public byte[] getIconAsBytes() { return m_iconAsBytes; }
    
    public List<StatusEffectData> getStatusEffectsData() { return Collections.unmodifiableList(m_statusEffectsData); }
    //End Getters
    
    //Private Fields
    private byte[] m_iconAsBytes;
    
    private List<StatusEffectData> m_statusEffectsData;
    //End Private Fields

    //Public Methods
    public SkillData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected SkillData DeepCopyInternally()
    {
		try {
			SkillData copy = (SkillData)super.clone();
			
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
