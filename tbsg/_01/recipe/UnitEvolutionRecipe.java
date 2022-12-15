package eean_games.tbsg._01.recipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eean_games.tbsg._01.CoreValues;
import eean_games.tbsg._01.item.EvolutionMaterial;
import eean_games.tbsg._01.unit.UnitData;

public class UnitEvolutionRecipe
{
    public UnitEvolutionRecipe(UnitData _unitAfterEvolution, List<EvolutionMaterial> _materials, int _cost)
    {
        UnitAfterEvolution = _unitAfterEvolution;

        m_materials = new EvolutionMaterial[CoreValues.MAX_NUM_OF_ELEMENTS_IN_RECIPE];
        if (_materials != null && _materials.size() == m_materials.length)
        {
            for (int i = 0; i < CoreValues.MAX_NUM_OF_ELEMENTS_IN_RECIPE; i++)
            {
                m_materials[i] = _materials.get(i);
            }
        }
        
        Cost = _cost;
    }
    
    //Public Read-only Fields
    public final UnitData UnitAfterEvolution;

    public final int Cost; // Golds
    //End Public Read-only Fields
    
    //Getters
    public List<EvolutionMaterial> getMaterials() { return Collections.unmodifiableList(Arrays.asList(m_materials)); }
    //End Getters

    //Private Read-only Fields
    private final EvolutionMaterial[] m_materials;
    //End Read-only Private Fields
}