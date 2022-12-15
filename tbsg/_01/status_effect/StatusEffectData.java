package eean_games.tbsg._01.status_effect;

import java.util.Arrays;

import eean_games.main.DeepCopyable;
import eean_games.main.extension_method.ArrayExtension;
import eean_games.tbsg._01.ComplexCondition;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;

public abstract class StatusEffectData implements DeepCopyable<StatusEffectData>, Cloneable
{
    public StatusEffectData(int _id, DurationData _duration, ComplexCondition _activationCondition, byte[] _iconAsBytes)
    {
    	Id = _id;
    	
        Duration = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_duration, true);

        ActivationCondition = NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_activationCondition, true);

        m_iconAsBytes = ArrayExtension.CoalesceNullAndReturnCopyOptionally(_iconAsBytes, true);
    }

    //Public Read-only Fields
    public final int Id;
    //End Public Read-only Fields
    
    //Getters
    public DurationData getDuration() { return Duration; }
    public ComplexCondition getActivationCondition() { return ActivationCondition; }
    
    public byte[] getIconAsBytes() { return m_iconAsBytes; }
    //End Getters
    
    //Private Fields
    private DurationData Duration;
    private ComplexCondition ActivationCondition;
    
    private byte[] m_iconAsBytes;
    //End Private Fields

    //Public Methods
    public StatusEffectData DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected StatusEffectData DeepCopyInternally()
    {
		try {
			StatusEffectData copy = (StatusEffectData)super.clone();
			
	        copy.Duration = Duration.DeepCopy();
	        copy.ActivationCondition = ActivationCondition.DeepCopy();

	        copy.m_iconAsBytes = Arrays.copyOf(m_iconAsBytes, m_iconAsBytes.length);
	        
	        return copy;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    //End Protected Methods
}
