package eean_games.tbsg._01.unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eean_games.main.DeepCopyable;
import eean_games.main.Linq;
import eean_games.main.extension_method.ArrayExtension;
import eean_games.main.extension_method.ListExtension;
import eean_games.main.extension_method.StringExtension;
import eean_games.main.extension_method.eCopyType;
import eean_games.tbsg._01.RarityMeasurable;
import eean_games.tbsg._01.enumerable.eAccessoryClassification;
import eean_games.tbsg._01.enumerable.eArmourClassification;
import eean_games.tbsg._01.enumerable.eElement;
import eean_games.tbsg._01.enumerable.eGender;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.enumerable.eTargetRangeClassification;
import eean_games.tbsg._01.enumerable.eWeaponClassification;
import eean_games.tbsg._01.recipe.UnitEvolutionRecipe;
import eean_games.tbsg._01.skill.SkillData;

public class UnitData implements DeepCopyable<UnitData>, Cloneable, RarityMeasurable
{
    /// <summary>
    /// Ctor
    /// PreCondition: _iconAsByteArray, _spriteAsByteArray has been initialized successfully; _specieTypes.Count > 0; _equipableWeaponTypes.Count > 0; _equipableArmourTypes.Count > 0; _equipableAccessoryTypes.Count > 0;
    /// _maxLvHP > 0; _maxLvPhyStr > 0; _maxLvPhyRes > 0; _maxLvMagStr > 0; _maxLvMarRes > 0;  _maxLvVit > 0; _skills.Count > 0;
    /// PostCondition: Will be initialized successfully.
    /// </summary>
    public UnitData(int _id, String _name, byte[] _iconAsBytes, eGender _gender, eRarity _rarity, eTargetRangeClassification _movementRangeClassification, eTargetRangeClassification _nonMovementActionRangeClassification, List<eElement> _elements,
                         List<eWeaponClassification> _equipableWeaponClassifications, List<eArmourClassification> _equipableArmourClassifications, List<eAccessoryClassification> _equipableAccessoryClassifications,
                            int _maxLvHP, int _maxLvPhyStr, int _maxLvPhyRes, int _maxLvMagStr, int _maxLvMagRes, int _maxLvVit, List<SkillData> _skills, List<String> _labels, String _description,
                            List<UnitEvolutionRecipe> _progressiveEvolutionRecipes, UnitEvolutionRecipe _retrogressiveEvolutionRecipe)
    {
        Id = _id;

        Name = StringExtension.CoalesceNull(_name);

        m_iconAsBytes = ArrayExtension.CoalesceNullAndReturnCopyOptionally(_iconAsBytes, true);

        //Assign Gender
        Gender = _gender;

        //Assign Rarity
        rarity = _rarity;

        MovementRangeClassification = _movementRangeClassification;
        NonMovementActionRangeClassification = _nonMovementActionRangeClassification;

        /*-----------------
        Assign Elements
        -----------------*/
        m_elements = new eElement[2];

        List<eElement> tmp_elementsWithoutRedundancy = new ArrayList<eElement>(); //Store every value of eElement type within _elements, without including any duplicated value.
        for (eElement element : _elements)
        {
        	if (element != eElement.None && !Linq.any(tmp_elementsWithoutRedundancy, element::equals)) //If tmp_elementsWithoutRedundancy does not already contain the given value
        		tmp_elementsWithoutRedundancy.add(element); // Add the value to the list
        }
        
        Collections.sort(tmp_elementsWithoutRedundancy); //Sort the list based on the order of values in the eElement enum
        
        //Assign argument values
        for (int i = 1; i <= m_elements.length; i++)
        {
        	if (tmp_elementsWithoutRedundancy.size() < i) //If tmp_elementsWithoutRedundancy contains less items than i
        		m_elements[i - 1] = eElement.None; //Set eElement.None
        	else
        		m_elements[i - 1] = tmp_elementsWithoutRedundancy.get(i - 1); //Set the corresponding value in tmp_elementsWithoutRedundancy
        }

        m_equipableWeaponClassifications = ListExtension.CoalesceNullAndReturnCopyOptionally(_equipableWeaponClassifications, eCopyType.Deep);
        m_equipableArmourClassifications = ListExtension.CoalesceNullAndReturnCopyOptionally(_equipableArmourClassifications, eCopyType.Deep);
        m_equipableAccessoryClassifications = ListExtension.CoalesceNullAndReturnCopyOptionally(_equipableAccessoryClassifications, eCopyType.Deep);


        //Assign Attribute Values at MAX Level
        MaxLevel_HP = _maxLvHP;
        MaxLevel_PhysicalStrength = _maxLvPhyStr;
        MaxLevel_PhysicalResistance = _maxLvPhyRes;
        MaxLevel_MagicalStrength = _maxLvMagStr;
        MaxLevel_MagicalResistance = _maxLvMagRes;
        MaxLevel_Vitality = _maxLvVit;

        //Assign Skills
        m_skills = ListExtension.CoalesceNullAndReturnCopyOptionally(_skills, eCopyType.Shallow);

        //Assign Labels
        m_labels = ListExtension.CoalesceNullAndReturnCopyOptionally(_labels, eCopyType.Deep);
        
        Description = StringExtension.CoalesceNull(_description);
        
        progressiveEvolutionRecipes = ListExtension.CoalesceNullAndReturnCopyOptionally(_progressiveEvolutionRecipes, eCopyType.Deep);
        retrogressiveEvolutionRecipe = _retrogressiveEvolutionRecipe; //Null is allowed
    
        areRecipesModifiable = true;
    }
    
    //Public Read-only Fields
    public final int Id;
    public final String Name;
    
    public final eGender Gender;
    
    public final eTargetRangeClassification MovementRangeClassification;
    public final eTargetRangeClassification NonMovementActionRangeClassification;
    
    public final int MaxLevel_HP;
    public final int MaxLevel_PhysicalStrength;
    public final int MaxLevel_PhysicalResistance;
    public final int MaxLevel_MagicalStrength;
    public final int MaxLevel_MagicalResistance;
    public final int MaxLevel_Vitality;
    
    public final String Description;
    //End Public Read-only Fields
    
    //Getters
    public byte[] getIconAsBytes() { return m_iconAsBytes; }
    
    public eRarity getRarity() { return rarity; }
    
    public List<eElement> getElements() { return Collections.unmodifiableList(Arrays.asList(m_elements)); } 

    public List<eWeaponClassification> getEquipableWeaponClassifications() { return Collections.unmodifiableList(m_equipableWeaponClassifications); } 
    public List<eArmourClassification> getEquipableArmourClassifications() { return Collections.unmodifiableList(m_equipableArmourClassifications); } 
    public List<eAccessoryClassification> getEquipableAccessoryClassifications() { return Collections.unmodifiableList(m_equipableAccessoryClassifications); } 
    
    public List<SkillData> getSkills() { return Collections.unmodifiableList(m_skills); } 

    public List<String> getLabels() { return Collections.unmodifiableList(m_labels); } 
    
    public List<UnitEvolutionRecipe> getProgressiveEvolutionRecipes() 
    { 
    	if (areRecipesModifiable)
    		return progressiveEvolutionRecipes;
    	else
    		return Collections.unmodifiableList(progressiveEvolutionRecipes); 
    }
    public UnitEvolutionRecipe getRetrogressiveEvolutionRecipe() { return retrogressiveEvolutionRecipe; }
    //End Getters
    
    //Setters
    public void setRetrogressiveEvolutionRecipe(UnitEvolutionRecipe _recipe)
    {
    	if (areRecipesModifiable)
    		retrogressiveEvolutionRecipe = _recipe;
    }
    //End Setters
    
    //Protected Read-only Fields
    protected final eRarity rarity;
    //End Protected Read-only Fields
    
    //Private Fields
    private byte[] m_iconAsBytes;

    private eElement[] m_elements; //All can be eElement.None

    private List<eWeaponClassification> m_equipableWeaponClassifications;
    private List<eArmourClassification> m_equipableArmourClassifications;
    private List<eAccessoryClassification> m_equipableAccessoryClassifications;

    private List<SkillData> m_skills; // Store references to original instances of SkillData.

    private List<String> m_labels;
    
    private List<UnitEvolutionRecipe> progressiveEvolutionRecipes;
    private UnitEvolutionRecipe retrogressiveEvolutionRecipe;
    
    private boolean areRecipesModifiable;
    //End Private Fields

    //Public Methods
    public void DisableModification() { areRecipesModifiable = false; }
    
    public UnitData DeepCopy()
    {
		try 
		{
			UnitData copy = (UnitData)super.clone();
			
	        copy.m_iconAsBytes = Arrays.copyOf(m_iconAsBytes, m_iconAsBytes.length);

	        copy.m_elements = ArrayExtension.DeepCopy(m_elements, eElement.class);

	        copy.m_equipableWeaponClassifications = ListExtension.DeepCopy(m_equipableWeaponClassifications);
	        copy.m_equipableArmourClassifications = ListExtension.DeepCopy(m_equipableArmourClassifications);
	        copy.m_equipableAccessoryClassifications = ListExtension.DeepCopy(m_equipableAccessoryClassifications);

	        copy.m_skills = ListExtension.DeepCopy(m_skills);
	        
	        copy.m_labels = ListExtension.DeepCopy(m_labels);

	        copy.progressiveEvolutionRecipes = ListExtension.DeepCopy(progressiveEvolutionRecipes);
	        
	        return copy;
		} 
		catch (CloneNotSupportedException e) 
		{
			e.printStackTrace();
			return null;
		}
    }
    //End Public Methods
}