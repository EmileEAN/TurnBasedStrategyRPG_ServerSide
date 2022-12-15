package eean_games.tbsg._01.recipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eean_games.tbsg._01.CoreValues;
import eean_games.tbsg._01.equipment.ArmourData;
import eean_games.tbsg._01.item.EquipmentMaterial;

public class ArmourRecipe
{
	public ArmourRecipe(ArmourData _product, List<EquipmentMaterial> _materials, int _cost)
	{
		this(_product, _materials, _cost, null);
	}
    public ArmourRecipe(ArmourData _product, List<EquipmentMaterial> _materials, int _cost, ArmourData _armourToUpgrade)
    {
        Product = _product;

        m_materials = new EquipmentMaterial[CoreValues.MAX_NUM_OF_ELEMENTS_IN_RECIPE];
        if (_materials != null && _materials.size() == m_materials.length)
        {
            for (int i = 0; i < CoreValues.MAX_NUM_OF_ELEMENTS_IN_RECIPE; i++)
            {
                m_materials[i] = _materials.get(i);
            }
        }
        
        Cost = _cost;

        ArmourToUpgrade = _armourToUpgrade;
    }

    //Public Read-only Fields
    public final ArmourData Product;
    public final ArmourData ArmourToUpgrade;

    public final int Cost; // Golds
    //End Public Read-only Fields
    
    //Getters
    public List<EquipmentMaterial> getMaterials() { return Collections.unmodifiableList(Arrays.asList(m_materials)); }
    //End Getters

    //Private Read-only Fields
    private final EquipmentMaterial[] m_materials;
    //End Read-only Private Fields
}
