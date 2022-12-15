package eean_games.tbsg._01;

import java.util.List;

import eean_games.tbsg._01.unit.Unit;

public class Team
{
    public Team(/*List<MemberSet> _memberSets, */List<Unit> _members, ItemSet _itemSet)
    {
    	/*
        //Assign sets of members and equipments
        if (_memberSets == null)
            MemberSets = new MemberSet[0];
        else
        {
        	if (_memberSets.size() >= CoreValues.MAX_MEMBERS_PER_TEAM)
                MemberSets = new MemberSet[CoreValues.MAX_MEMBERS_PER_TEAM];
            else
                MemberSets = new MemberSet[_memberSets.size()];

            for (int i = 1; i <= MemberSets.length; i++)
            {
                MemberSets[i - 1] = _memberSets.get(i - 1);
            }
        }
        */
    	
    	//Assign set of units
		members = new Unit[CoreValues.MAX_MEMBERS_PER_TEAM];
		int numOfGivenMembers = (_members == null) ? 0 : _members.size();	
		for (int i = 1; i <= members.length; i++)
		{
			if (i > numOfGivenMembers)
				members[i - 1] = null;
			else
				members[i - 1] = _members.get(i - 1);
		}

        //Assign set of items
        if (_itemSet == null)
            ItemSet = null;
        else
        	ItemSet = new ItemSet(_itemSet.Id, _itemSet.quantityPerItem);
    }

    //Public Fields
    public Unit[] members;
    //End Public Fields
    
    //Public Read-only Fields
    //public final MemberSet[] MemberSets; //Array elements are modifiable
    public final ItemSet ItemSet;
    //End Public Read-only Fields

}
