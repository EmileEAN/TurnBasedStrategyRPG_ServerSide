package eean_games.tbsg._01.recipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eean_games.tbsg._01.CoreValues;
import eean_games.tbsg._01.item.Item;
import eean_games.tbsg._01.item.ItemMaterial;

public class ItemRecipe
{
	public ItemRecipe(Item _product, List<ItemMaterial> _materials, int _cost)
	{
		this(_product, _materials, _cost, null);
	}
    public ItemRecipe(Item _product, List<ItemMaterial> _materials, int _cost, Item _itemToUpgrade)
    {
        Product = _product;

        m_materials = new ItemMaterial[CoreValues.MAX_NUM_OF_ELEMENTS_IN_RECIPE];
        if (_materials != null && _materials.size() == m_materials.length)
        {
            for (int i = 0; i < CoreValues.MAX_NUM_OF_ELEMENTS_IN_RECIPE; i++)
            {
                m_materials[i] = _materials.get(i);
            }
        }

        Cost = _cost;
        
        ItemToUpgrade = _itemToUpgrade;
    }

    //Public Read-only Fields
    public final Item Product;
    public final Item ItemToUpgrade;

    public final int Cost; // Golds
    //End Public Read-only Fields
    
    //Getters
    public List<ItemMaterial> getMaterials() { return Collections.unmodifiableList(Arrays.asList(m_materials)); }
    //End Getters

    //Private Read-only Fields
    private final ItemMaterial[] m_materials;
    //End Read-only Private Fields
}
