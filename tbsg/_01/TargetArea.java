package eean_games.tbsg._01;

import java.util.ArrayList;
import java.util.List;

import eean_games.main._2DCoord;
import eean_games.main.extension_method.ListExtension;
import eean_games.tbsg._01.enumerable.eTargetRangeClassification;

public class TargetArea
{
    //Getters
    public static List<_2DCoord> GetTargetArea(eTargetRangeClassification _targetAreaType)
    {
        return ListExtension.DeepCopy(TargetAreaSets.get(_targetAreaType.index()));
    }
    //End Getters
    
    //Private Fields
    @SuppressWarnings("serial")
	private static List<List<_2DCoord>> TargetAreaSets 
    	= new ArrayList<List<_2DCoord>>()
		{{
			//4 Coordinates
			//Cross I
			add(new ArrayList<_2DCoord>()
	        {{
	            add(new _2DCoord(0, 1));
	            add(new _2DCoord(1, 0));
	            add(new _2DCoord(0, -1));
	            add(new _2DCoord(-1, 0));
	        }});

			//Diagonal Cross I
	        add(new ArrayList<_2DCoord>()
	        {{
	            add(new _2DCoord(1, 1));
	            add(new _2DCoord(1, -1));
	            add(new _2DCoord(-1, -1));
	            add(new _2DCoord(-1, 1));
	        }});
	        //End 4 Coordinates

	        //8 Coordinates
	        //Cross II
	        add(new ArrayList<_2DCoord>()
	        {{
	            add(new _2DCoord(0, 1));
	            add(new _2DCoord(1, 0));
	            add(new _2DCoord(0, -1));
	            add(new _2DCoord(-1, 0));
	            add(new _2DCoord(0, 2));
	            add(new _2DCoord(2, 0));
	            add(new _2DCoord(0, -2));
	            add(new _2DCoord(-2, 0));
	        }});

	        //Diagonal Cross II
	        add(new ArrayList<_2DCoord>()
	        {{
	            add(new _2DCoord(1, 1));
	            add(new _2DCoord(1, -1));
	            add(new _2DCoord(-1, -1));
	            add(new _2DCoord(-1, 1));
	            add(new _2DCoord(2, 2));
	            add(new _2DCoord(2, -2));
	            add(new _2DCoord(-2, -2));
	            add(new _2DCoord(-2, 2));
	        }});

	        //Square
	        add(new ArrayList<_2DCoord>()
	        {{
	            add(new _2DCoord(0, 1));
	            add(new _2DCoord(1, 1));
	            add(new _2DCoord(1, 0));
	            add(new _2DCoord(1, -1));
	            add(new _2DCoord(0, -1));
	            add(new _2DCoord(-1, -1));
	            add(new _2DCoord(-1, 0));
	            add(new _2DCoord(-1, 1));
	        }});

	        //Knight
	        add(new ArrayList<_2DCoord>()
	        {{
	            add(new _2DCoord(1, 2));
	            add(new _2DCoord(2, 1));
	            add(new _2DCoord(2, -1));
	            add(new _2DCoord(1, -2));
	            add(new _2DCoord(-1, -2));
	            add(new _2DCoord(-2, -1));
	            add(new _2DCoord(-2, 1));
	            add(new _2DCoord(-1, 2));
	        }});

	        //Fixed Distance II
	        add(new ArrayList<_2DCoord>()
	        {{
	            add(new _2DCoord(0, 2));
	            add(new _2DCoord(1, 1));
	            add(new _2DCoord(2, 0));
	            add(new _2DCoord(1, -1));
	            add(new _2DCoord(0, -2));
	            add(new _2DCoord(-1, -1));
	            add(new _2DCoord(-2, 0));
	            add(new _2DCoord(-1, 1));
	        }});
	        //End 8 Coordinates 

	        //12 Coordinates
	        //Cross III
	        add(new ArrayList<_2DCoord>()
	        {{
	            add(new _2DCoord(0, 1));
	            add(new _2DCoord(1, 0));
	            add(new _2DCoord(0, -1));
	            add(new _2DCoord(-1, 0));
	            add(new _2DCoord(0, 2));
	            add(new _2DCoord(2, 0));
	            add(new _2DCoord(0, -2));
	            add(new _2DCoord(-2, 0));
	            add(new _2DCoord(0, 3));
	            add(new _2DCoord(3, 0));
	            add(new _2DCoord(0, -3));
	            add(new _2DCoord(-3, 0));
	        }});

	        //Diagonal Cross III
	        add(new ArrayList<_2DCoord>()
	        {{
	            add(new _2DCoord(1, 1));
	            add(new _2DCoord(1, -1));
	            add(new _2DCoord(-1, -1));
	            add(new _2DCoord(-1, 1));
	            add(new _2DCoord(2, 2));
	            add(new _2DCoord(2, -2));
	            add(new _2DCoord(-2, -2));
	            add(new _2DCoord(-2, 2));
	            add(new _2DCoord(3, 3));
	            add(new _2DCoord(3, -3));
	            add(new _2DCoord(-3, -3));
	            add(new _2DCoord(-3, 3));
	        }});

	        //Cross Alter
	        add(new ArrayList<_2DCoord>()
	        {{
	            add(new _2DCoord(0, 1));
	            add(new _2DCoord(1, 1));
	            add(new _2DCoord(1, 0));
	            add(new _2DCoord(1, -1));
	            add(new _2DCoord(0, -1));
	            add(new _2DCoord(-1, -1));
	            add(new _2DCoord(-1, 0));
	            add(new _2DCoord(-1, 1));
	            add(new _2DCoord(0, 2));
	            add(new _2DCoord(2, 0));
	            add(new _2DCoord(0, -2));
	            add(new _2DCoord(-2, 0));
	        }});

	        //Diagonal Cross Alter
	        add(new ArrayList<_2DCoord>()
	        {{
	            add(new _2DCoord(0, 1));
	            add(new _2DCoord(1, 1));
	            add(new _2DCoord(1, 0));
	            add(new _2DCoord(1, -1));
	            add(new _2DCoord(0, -1));
	            add(new _2DCoord(-1, -1));
	            add(new _2DCoord(-1, 0));
	            add(new _2DCoord(-1, 1));
	            add(new _2DCoord(2, 2));
	            add(new _2DCoord(2, -2));
	            add(new _2DCoord(-2, -2));
	            add(new _2DCoord(-2, 2));
	        }});

	        //Cross Knight
	        add(new ArrayList<_2DCoord>()
	        {{
	            add(new _2DCoord(0, 1));
	            add(new _2DCoord(1, 0));
	            add(new _2DCoord(0, -1));
	            add(new _2DCoord(-1, 0));
	            add(new _2DCoord(1, 2));
	            add(new _2DCoord(2, 1));
	            add(new _2DCoord(2, -1));
	            add(new _2DCoord(1, -2));
	            add(new _2DCoord(-1, -2));
	            add(new _2DCoord(-2, -1));
	            add(new _2DCoord(-2, 1));
	            add(new _2DCoord(-1, 2));
	        }});

	        //Diagonal Cross Knight
	        add(new ArrayList<_2DCoord>()
	        {{
	            add(new _2DCoord(1, 1));
	            add(new _2DCoord(1, -1));
	            add(new _2DCoord(-1, -1));
	            add(new _2DCoord(-1, 1));
	            add(new _2DCoord(1, 2));
	            add(new _2DCoord(2, 1));
	            add(new _2DCoord(2, -1));
	            add(new _2DCoord(1, -2));
	            add(new _2DCoord(-1, -2));
	            add(new _2DCoord(-2, -1));
	            add(new _2DCoord(-2, 1));
	            add(new _2DCoord(-1, 2));
	        }});

	        //Fixed Distance III
	        add(new ArrayList<_2DCoord>()
	        {{
	            add(new _2DCoord(0, 3));
	            add(new _2DCoord(1, 2));
	            add(new _2DCoord(2, 1));
	            add(new _2DCoord(3, 0));
	            add(new _2DCoord(2, -1));
	            add(new _2DCoord(1, -2));
	            add(new _2DCoord(0, -3));
	            add(new _2DCoord(-1, -2));
	            add(new _2DCoord(-2, -1));
	            add(new _2DCoord(-3, 0));
	            add(new _2DCoord(-2, 1));
	            add(new _2DCoord(-1, 2));
	        }});
	        //End 12 Coordinates
		}};
    //End Private Fields
}
