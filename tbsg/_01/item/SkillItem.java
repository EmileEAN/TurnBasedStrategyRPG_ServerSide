package eean_games.tbsg._01.item;

import eean_games.main.DeepCopyable;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.extension_method.NullPreventionAssignmentMethods;
import eean_games.tbsg._01.skill.ActiveSkill;

//Can be used to execute a Skill
public class SkillItem extends BattleItem implements DeepCopyable<Item>
{
    public SkillItem(int _id, String _name, byte[] _iconAsByteArray, eRarity _rarity, int _sellingPrice,
    		ActiveSkill _skill)
    {
    	super(_id, _name, _iconAsByteArray, _rarity, _sellingPrice);
        Skill = (ActiveSkill)(NullPreventionAssignmentMethods.CoalesceNullAndReturnDeepCopyOptionally(_skill, true));
    }

    //Getters
    public eRarity getRarity() { return rarity; }
    
    public ActiveSkill getSkill() { return Skill; }
    //End Getters
    
    //Private Fields
    private ActiveSkill Skill;
    //End Private Fields

    //Public Methods
    public SkillItem DeepCopy() { return DeepCopyInternally(); }
    //End Public Methods

    //Protected Methods
    @Override
    protected SkillItem DeepCopyInternally()
    {
        SkillItem copy = (SkillItem)super.DeepCopyInternally();

        copy.Skill = Skill.DeepCopy();

        return copy;
    }
    // EndProtected Methods
}
