package eean_games.tbsg._01.item;

import java.util.Collections;
import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.RarityMeasurable;
import eean_games.tbsg._01.enumerable.eElement;
import eean_games.tbsg._01.enumerable.eRarity;

// Used to increase the level of Skills
public class SkillEnhancementMaterial extends EnhancementMaterial implements DeepCopyable<Item>, RarityMeasurable
{
    public SkillEnhancementMaterial(int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, int _sellingPrice, int _levelsToEnhance,
    		List<eRarity> _targetingRarities, List<eElement> _targetingElements, List<String> _targetingLabels)
    {
    	super(_id, _name, _iconAsBytes, _rarity, _sellingPrice, _levelsToEnhance);
    	m_targetingRarities = ListExtension.CoalesceNullAndReturnCopyOptionally(_targetingRarities, eCopyType.Shallow);
    	m_targetingElements = ListExtension.CoalesceNullAndReturnCopyOptionally(_targetingElements, eCopyType.Shallow);;
    	m_targetingLabels = ListExtension.CoalesceNullAndReturnCopyOptionally(_targetingLabels, eCopyType.Shallow);
    }

    //Getters
    public eRarity getRarity() { return rarity; }
    
    public List<eRarity> getTargetingRarities() { return Collections.unmodifiableList(m_targetingRarities); }
    public List<eElement> getTargetingElements() { return Collections.unmodifiableList(m_targetingElements); }
    public List<String> getTargetingLabels() { return Collections.unmodifiableList(m_targetingLabels); }
    //End Getters
    
    //Private Fields
    private List<eRarity> m_targetingRarities;
    private List<eElement> m_targetingElements;
    private List<String> m_targetingLabels;
    //End Private Fields
    
    //Public Methods
    public SkillEnhancementMaterial DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    protected SkillEnhancementMaterial DeepCopyInternally() 
    {
    	SkillEnhancementMaterial copy = (SkillEnhancementMaterial)super.DeepCopyInternally();

        copy.m_targetingRarities = ListExtension.DeepCopy(m_targetingRarities);
        copy.m_targetingElements = ListExtension.DeepCopy(m_targetingElements);
        copy.m_targetingLabels = ListExtension.DeepCopy(m_targetingLabels);
        
        return copy;
    }
    //End Protected Methods
}
