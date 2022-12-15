package eean_games.tbsg._01;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import eean_games.main.*;
import eean_games.main.extension_method.StringExtension;
import eean_games.tbsg._01.skill.Skill;
import eean_games.tbsg._01.status_effect.StatusEffect;
import eean_games.tbsg._01.unit.Unit;
import eean_games.tbsg._01.unit.UnitInstance;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.enumerable.eElement;
import eean_games.tbsg._01.enumerable.eGender;
import eean_games.tbsg._01.enumerable.eStringMatchType;
import eean_games.tbsg._01.enumerable.eTargetUnitClassification;
import eean_games.tbsg._01.enumerable.eTileType;
import eean_games.tbsg._01.enumerable.eUnitAttributeType;

public final class Tag implements DeepCopyable<Tag>
{	
	private Tag(String _string, String _completeTagString)
    {
		string = StringExtension.CoalesceNull(_string);

        childrenTags = new ArrayList<Tag>();

        parentTag = null;
        
        completeTagString = _completeTagString;
    }
	private Tag(String _string)
	{
		this(_string, (Tag)null);
	}
	private Tag(String _string, Tag _parent)
	{
		string = StringExtension.CoalesceNull(_string);

        childrenTags = new ArrayList<Tag>();

        parentTag = _parent;
        
        completeTagString = "";
	}

	//Public Read-only Fields
    public final Tag parentTag;

    public final String string;
    
    public final String completeTagString; //Only the root Tag will hold a value for this field.
    
    public static final Tag zero = newTag("<#0/>");
    public static final Tag one = newTag("<#1/>");
	//End Public Read-only Fields
	
    //Getters
    public final List<Tag> getChildrenTags() { return Collections.unmodifiableList(childrenTags); }
    //End Getters
    
    //Private Fields    
    private List<Tag> childrenTags;
    //End Private Fields

    //Public Methods
    public static Tag newTag(String _tagString)
    {
        if (_tagString == null)
            return new Tag(null);

        List<String> tagStrings = DivideIntoTagStrings(_tagString);

        if (tagStrings.size() < 1)
            return new Tag(null);

        Tag t = new Tag(tagStrings.get(0), _tagString); //First tag will always be the root tag

        Tag currentTag = t;

        for (int i = 2; i <= tagStrings.size(); i++)
        {
            if (!tagStrings.get(i - 1).startsWith("</"))
            {
                if (tagStrings.get(i - 1).endsWith("/>"))
                    currentTag.childrenTags.add(new Tag(tagStrings.get(i - 1).substring(0, tagStrings.get(i - 1).length() - 2) + ">", currentTag)); //Remove the "/"
                else
                    currentTag.childrenTags.add(new Tag(tagStrings.get(i - 1), currentTag));

                if (!tagStrings.get(i - 1).endsWith("/>"))
                    currentTag = currentTag.childrenTags.get(currentTag.childrenTags.size() - 1);
            }
            else
                currentTag = currentTag.parentTag;
        }

        return t;
    }

    public <T> T ToValue(Class<T> _class, BattleSystemCore _system)
    {
    	return ToValue(_class, _system, null, null, null, null, null, null, null, null, null, 0, 0, null, null, null, eTileType.values()[0]);
    }
    public <T> T ToValue(Class<T> _class, BattleSystemCore _system, UnitInstance _effectHolder, StatusEffect _statusEffect)
    {
    	return ToValue(_class, _system, _effectHolder, _statusEffect, null, null, null, null, null, null, null, 0, 0, null, null, null, eTileType.values()[0]);
    }
    public <T> T ToValue(Class<T> _class, BattleSystemCore _system, UnitInstance _actor, Skill _skill, List<_2DCoord> _effectRange)
    {
    	return ToValue(_class, _system, null, null, _actor, _skill, null, _effectRange, null, null, null, 0, 0, null, null, null, eTileType.values()[0]);    	
    }
    public <T> T ToValue(Class<T> _class, BattleSystemCore _system, UnitInstance _actor, Skill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	return ToValue(_class, _system, null, null, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, 0, 0, null, null, null, eTileType.values()[0]);
    }
    public <T> T ToValue(Class<T> _class, BattleSystemCore _system, StatusEffect _statusEffect, UnitInstance _actor, Skill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	return ToValue(_class, _system, null, _statusEffect, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, 0, 0, null, null, null, eTileType.values()[0]);
    }
    public <T> T ToValue(Class<T> _class, BattleSystemCore _system, UnitInstance _effectHolder, StatusEffect _statusEffect, UnitInstance _actor, Skill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	return ToValue(_class, _system, _effectHolder, _statusEffect, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, 0, 0, null, null, null, eTileType.values()[0]);
    }
    public <T> T ToValue(Class<T> _class, BattleSystemCore _system, UnitInstance _effectHolder, StatusEffect _statusEffect, UnitInstance _actor, Skill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect, int _targetPreviousHP, int _targetPreviousLocationTileIndex, List<StatusEffect> _statusEffects, UnitInstance _effectHolderOfActivatedEffect, StatusEffect _statusEffectActivated, eTileType _previousTileType)
    {
        try
        {
            Object result;

            if (childrenTags.size() != 0)
                result = TranslateFunctionTagToValue(_system);
            else
                result = TranslateSimpleValueTagToValue(_effectHolder, _statusEffect, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, _targetPreviousHP, _targetPreviousLocationTileIndex, _statusEffects, _effectHolderOfActivatedEffect, _statusEffectActivated, _previousTileType);

            if (result == null)
                return null;

            if (_class.isInstance(result))
                return _class.cast(result);
            else if (result instanceof Integer && _class == BigDecimal.class)
            	return _class.cast(new BigDecimal(((Integer)result).intValue()));
            else if (result instanceof BigDecimal && _class == Integer.class)
            	return _class.cast(Integer.valueOf(((BigDecimal)result).intValue()));
            else
            	return null;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    /**The returned Tag will be treated as the root Tag. That is, it Will not have any parent Tag.*/
    public Tag DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Private Methods
    private static List<String> DivideIntoTagStrings(String _tagString)
    {
        if (_tagString == null)
            return new ArrayList<String>();

        List<String> tagStrings = new ArrayList<String>();

        while (!_tagString.equals(""))
        {
            int countCharsInTag = 1; //the first character '<'

            for (int i = 2; i <= _tagString.length(); i++)
            {
                //ignore the first character in commandCopy which should be '<'
                if (_tagString.charAt(i - 1) != '<')
                    countCharsInTag++;
                else
                    break;
            }

            tagStrings.add(_tagString.substring(0, countCharsInTag));
            _tagString = _tagString.substring(countCharsInTag);
        }

        return tagStrings;
    }

    @SuppressWarnings("unchecked")
	private Object TranslateFunctionTagToValue(BattleSystemCore _system)
    {
        List<Object> tagValues = new ArrayList<Object>();

        for (Tag tag : childrenTags)
        {
            tagValues.add(tag.ToValue(Object.class, _system));
        }

        Object result;

        try
        {
            switch (string)
            {
                //return Object
                case "<$GetFirstOrDefault>":
                    {
                        if ((tagValues.get(0)) instanceof Collection)
                            result = ((Collection<?>)(tagValues.get(0)));
                        else
                            result = null;
                    }
                    break;
                case "<$MergeListsWithoutDuplicates>":
                    {
                        List<Object> list = new ArrayList<Object>();
                        for (Object tagValue : tagValues)
                        {
                            if (!(tagValue instanceof List))
                            {
                                list.clear();
                                break;
                            }

                            list.addAll((List<?>)tagValue);
                        }

                        result = Linq.distinct(list);
                    }
                    break;
                //End return Object

                //return BigDecimal
                case "<$Sum>":
                    {
                        BigDecimal decimal_result = new BigDecimal((tagValues.get(0)).toString());
                        for (int i = 1; i < tagValues.size(); i++)
                        {
                          	decimal_result = CoreFunctions.Sum(decimal_result, (Number)(tagValues.get(i)));
                        }
                        result = decimal_result;
                    }
                    break;
                case "<$Subtract>":
                    {
                    	BigDecimal decimal_result = new BigDecimal((tagValues.get(0)).toString());
                        for (int i = 1; i < tagValues.size(); i++)
                        {
                        	decimal_result = CoreFunctions.Subtract(decimal_result, (Number)(tagValues.get(i)));
                        }
                        result = decimal_result;
                    }
                    break;
                case "<$Multiply>":
                    {
                    	BigDecimal decimal_result = new BigDecimal((tagValues.get(0)).toString());
                        for (int i = 1; i < tagValues.size(); i++)
                        {
                        	decimal_result = CoreFunctions.Multiply(decimal_result, (Number)(tagValues.get(i)));
                        }
                        result = decimal_result;
                    }
                    break;
                case "<$Divide>":
                    {
                    	BigDecimal decimal_result = new BigDecimal((tagValues.get(0)).toString());
                        for (int i = 1; i < tagValues.size(); i++)
                        {
                        	decimal_result = CoreFunctions.Divide(decimal_result, (Number)(tagValues.get(i)), 50, RoundingMode.HALF_UP);
                        }
                        result = decimal_result;
                    }
                    break;
                //End return BigDecimal
                    
                //return Integer
                case "<$Count>":
                    result = ((Collection<?>)(tagValues.get(0))).size();
                    break;
                case "<$GetLevel>":
                    result = Calculator.Level((Unit)(tagValues.get(0)));
                    break;
                case "<$GetMaxHP>":
                    result = Calculator.MaxHP((Unit)(tagValues.get(0)));
                    break;
                case "<$GetPhyStr>":
                    result = Calculator.PhysicalStrength((Unit)(tagValues.get(0)));
                    break;
                case "<$GetPhyRes>":
                    result = Calculator.PhysicalResistance((Unit)(tagValues.get(0)));
                    break;
                case "<$GetMagStr>":
                    result = Calculator.MagicalStrength((Unit)(tagValues.get(0)));
                    break;
                case "<$GetMagRes>":
                    result = Calculator.MagicalResistance((Unit)(tagValues.get(0)));
                    break;
                case "<$GetVit>":
                    result = Calculator.Vitality((Unit)(tagValues.get(0)));
                    break;
                //End return Integer

                //return boolean
                case "<$ContainsElement>":
                	{
                		if (tagValues.size() < 2)
                			result = false;
                		else
                			result = ((Unit)(tagValues.get(0))).BaseInfo.getElements().contains((eElement)(tagValues.get(1)));
                	}
                	break;
                //End return boolean
                    
                //return List<UnitInstance>
                case "<$FindUnitsByName>":
                    {
                        if (tagValues.size() < 3)
                            result = null;
                        else if (tagValues.size() == 3)
                            result = _system.FindUnitsByName(tagValues.get(0).toString(), (boolean)(tagValues.get(1)), (eStringMatchType)(tagValues.get(2)));
                        else
                            result = _system.FindUnitsByName(tagValues.get(0).toString(), (boolean)(tagValues.get(1)), (eStringMatchType)(tagValues.get(2)), (List<UnitInstance>)(tagValues.get(3)));
                    }
                    break;
                case "<$FindUnitsByLabel>":
                    {
                        if (tagValues.size() < 2)
                            result = null;
                        else if (tagValues.size() == 2)
                            result = _system.FindUnitsByLabel(tagValues.get(0).toString(), (boolean)(tagValues.get(1)));
                        else
                            result = _system.FindUnitsByLabel(tagValues.get(0).toString(), (boolean)(tagValues.get(1)), (List<UnitInstance>)(tagValues.get(2)));
                    }
                    break;
                case "<$FindUnitsByGender>":
                    {
                        if (tagValues.size() < 3)
                            result = null;
                        else if (tagValues.size() == 3)
                            result = _system.FindUnitsByGender((eGender)(tagValues.get(0)), (boolean)(tagValues.get(1)));
                        else
                            result = _system.FindUnitsByGender((eGender)(tagValues.get(0)), (boolean)(tagValues.get(1)), (List<UnitInstance>)(tagValues.get(2)));
                    }
                    break;
                case "<$FindUnitsByElement>":
                    {
                        if (tagValues.size() < 2)
                            result = null;
                        else if (tagValues.size() == 2)
                            result = _system.FindUnitsByElement((eElement)(tagValues.get(0)), (boolean)(tagValues.get(1)));
                        else if (tagValues.size() == 3)
                            result = _system.FindUnitsByElement((eElement)(tagValues.get(0)), (boolean)(tagValues.get(1)), (eElement)(tagValues.get(2)));
                        else
                            result = _system.FindUnitsByElement((eElement)(tagValues.get(0)), (boolean)(tagValues.get(1)), (eElement)(tagValues.get(2)), (List<UnitInstance>)(tagValues.get(3)));
                    }
                    break;
                case "<$FindUnitsByAttributeValue>":
                    {
                        if (tagValues.size() < 3)
                            result = null;
                        else if (tagValues.size() == 3)
                            result = _system.FindUnitsByAttributeValue((eUnitAttributeType)(tagValues.get(0)), (eRelationType)(tagValues.get(1)), (int)(tagValues.get(2)));
                        else
                            result = _system.FindUnitsByAttributeValue((eUnitAttributeType)(tagValues.get(0)), (eRelationType)(tagValues.get(1)), (int)(tagValues.get(2)), (List<UnitInstance>)(tagValues.get(3)));
                    }
                    break;
                case "<$FindUnitsByAttributeValueRanking>":
                    {
                        if (tagValues.size() < 3)
                            result = null;
                        else if (tagValues.size() == 3)
                            result = _system.FindUnitsByAttributeValueRanking((eUnitAttributeType)(tagValues.get(0)), (eSortType)(tagValues.get(1)), (int)(tagValues.get(2)));
                        else
                            result = _system.FindUnitsByAttributeValueRanking((eUnitAttributeType)(tagValues.get(0)), (eSortType)(tagValues.get(1)), (int)(tagValues.get(2)), (List<UnitInstance>)(tagValues.get(3)));
                    }
                    break;
                case "<$FindUnitsByTargetClassification>":
                    {
                        if (tagValues.size() < 2)
                            result = null;
                        else if (tagValues.size() == 2)
                            result = _system.FindUnitsByTargetClassification((UnitInstance)((tagValues.get(0))), (eTargetUnitClassification)(tagValues.get(1)));
                        else
                            result = _system.FindUnitsByTargetClassification((UnitInstance)((tagValues.get(0))), (eTargetUnitClassification)(tagValues.get(1)), (List<_2DCoord>)(tagValues.get(2)));
                    }
                    break;
                //End return List<UnitInstance>

                default:
                    result = null;
                    break;
            }
        }
        catch (Exception ex)
        {
            return null;
        }

        return result;
    }

    private Object TranslateSimpleValueTagToValue(UnitInstance _effectHolder, StatusEffect _statusEffect, UnitInstance _actor, Skill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect, int _targetPreviousHP, int _targetPreviousLocationTileIndex, List<StatusEffect> _statusEffects, UnitInstance _effectHolderOfActivatedEffect, StatusEffect _statusEffectActivated, eTileType _previousTileType)
    {
        try
        {
            if (string.startsWith("<#")) //Number
            {
                String tagStringCopy = string;

                tagStringCopy = tagStringCopy.substring(2, tagStringCopy.length() - 2); //Remove "<#" and "/>"

                return new BigDecimal(tagStringCopy);
            }
            else if (string.startsWith("<S=")) //String
            {
                String tagStringCopy = string;

                tagStringCopy = tagStringCopy.substring(3, tagStringCopy.length() - 2); //Remove "<S=" and "/>"

                return tagStringCopy;
            }
            else if (string.startsWith("<E=")) //Enum
            {
                String tagStringCopy = string;

                tagStringCopy = tagStringCopy.substring(3, tagStringCopy.length() - 2); //Remove "<E=" and "/>"

                String[] enumTypeAndValueString = tagStringCopy.split("."); // Will be separated into enum type and enum value.

                return eean_games.tbsg._01.extension_method.StringExtension.ToCorrespondingEnumValue(enumTypeAndValueString[1], enumTypeAndValueString[0]);
            }
            else if (string == "<True/>") return true;
            else if (string == "<False/>") return false;
            else if (string == "<EffectHolder/>") return _effectHolder;
            else if (string == "<EffectUser/>") return _actor;
            else if (string == "<StatusEffectOriginSkillLevel/>") return _statusEffect.OriginSkillLevel;
            else if (string == "<StatusEffectEquipmentLevel/>") return _statusEffect.EquipmentLevel;
            else if (string == "<Skill/>") return _skill;
            else if (string == "<SkillName/>") return _skill.BaseInfo.Name;
            else if (string == "<SkillLevel/>") return _skill.Level;
            else if (string == "<Effect/>") return _effect;
            else if (string == "<EffectRange/>") return _effectRange;
            else if (string == "<Target/>") return _target;
            else
                return null;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    private Tag DeepCopyInternally()
    {
    	return DeepCopyInternally(null);
    }
    private Tag DeepCopyInternally(Tag _parentTag)
    {
        List<Tag> childrenTagCopies = new ArrayList<Tag>();
        for (Tag childTag : childrenTags)
        {
            childrenTagCopies.add(childTag.DeepCopyInternally(this));
        }

        if (_parentTag == null)
        	return new Tag(string, completeTagString);
        else
        	return new Tag(string, _parentTag);
    }
    //End Private Methods
}
