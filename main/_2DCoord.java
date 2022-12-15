package eean_games.main;

import eean_games.tbsg._01.CoreValues;

public final class _2DCoord implements Comparable<_2DCoord>, DeepCopyable<_2DCoord>, Cloneable
{
    public _2DCoord(int _x, int _y)
    {
        X = _x;
        Y = _y;
    }

    //Getters
    public int getX() { return X; }
    public int getY() { return Y; }
    //End Getters
    
    //Private Fields
    public int X;
    public int Y;
    //End Private Fields
    
    //Public Methods    
    @Override
    public int compareTo(_2DCoord _targetCoord)
    {
        if (_targetCoord == null) return -1;

        Integer tmp_x = X;
        Integer tmp_y = Y;
        
        if (X == _targetCoord.X)
            return tmp_y.compareTo(_targetCoord.Y);
        else
            return tmp_x.compareTo(_targetCoord.X);
    }

    @Override
    public boolean equals(Object _object)
    {
        if (_object == null) return false;
        if (this == _object) return true;
        if (_object.getClass() != this.getClass()) return false;

        _2DCoord targetCoord = (_2DCoord)_object;
        
        return X == targetCoord.X && Y == targetCoord.Y;
    }

    public _2DCoord sum(_2DCoord _target)
    {
        int x = X + _target.X;
        int y = Y + _target.Y;

        return new _2DCoord(x, y);
    }

    public void Rotate90DegreesAnticlockwise(int _timesToRotate)
    {
        while(_timesToRotate > 0)
        {
            double tmp = (double)X;
            double multiplier = CoreValues.MULTIPLIER_FOR_ANGLE_TO_RADIAN;
            double referenceAngle = 90.0;
            X = (int)Math.round(X * Math.cos(referenceAngle * multiplier) + Y * Math.sin(referenceAngle * multiplier));
            Y = (int)Math.round(Y * Math.cos(referenceAngle * multiplier) - tmp * Math.sin(referenceAngle * multiplier));

            _timesToRotate--;
        }
    }

    public void InvertXY()
    {
        int tmp = X;
        X = Y;
        Y = tmp;
    }

    public _2DCoord DeepCopy()
    {
    	try {
			return (_2DCoord)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    //End Public Methods
}
