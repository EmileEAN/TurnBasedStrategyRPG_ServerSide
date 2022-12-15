package eean_games.main;

import java.math.BigDecimal;
import java.math.RoundingMode;

import eean_games.main.eRelationType;

public final class CoreFunctions 
{
    //Public Methods
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean Compare(Object _a, eRelationType _relation, Object _b)
    {
        try
        {
        	if (_a instanceof Comparable && _b instanceof Comparable)
                return compare_actualDefinition((Comparable)_a, _relation, (Comparable)_b);
            else
            {
                switch (_relation)
                {
                    case EqualTo: return _a.equals(_b);
                    case NotEqualTo: return !_a.equals(_b);
                    default: return false; //Other comparisons cannot be applied to a non-Comparable type
                }
            }
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    //_a and _b will be calculated as BigDecimal
    public static <T extends Number> BigDecimal Sum(T _a, T _b)
    {
        try
        {
            BigDecimal a = new BigDecimal(_a.toString());
            BigDecimal b = new BigDecimal(_b.toString());

            return a.add(b);
        }
        catch (Exception ex)
        {
            return null;
        }
    }
    public static <T extends Number> BigDecimal Subtract(T _a, T _b)
    {
        try
        {
            BigDecimal a = new BigDecimal(_a.toString());
            BigDecimal b = new BigDecimal(_b.toString());

            return a.subtract(b);
        }
        catch (Exception ex)
        {
            return null;
        }
    }
    public static <T extends Number> BigDecimal Multiply(T _a, T _b)
    {
        try
        {
            BigDecimal a = new BigDecimal(_a.toString());
            BigDecimal b = new BigDecimal(_b.toString());

            return a.multiply(b);
        }
        catch (Exception ex)
        {
            return null;
        }
    }
    public static <T extends Number> BigDecimal Divide(T _a, T _b, int _scale, RoundingMode _roundingMode)
    {
        try
        {
            BigDecimal a = new BigDecimal(_a.toString());
            BigDecimal b = new BigDecimal(_b.toString());

            return a.divide(b, _scale, _roundingMode);
        }
        catch (Exception ex)
        {
            return null;
        }
    }
    //End Public Methods

    //Private Methods
    private static <T extends Comparable<T>> boolean compare_actualDefinition(T _a, eRelationType _relation, T _b)
    {
        switch (_relation)
        {
            case EqualTo:
                return _a.compareTo(_b) == 0;
            case NotEqualTo:
                return _a.compareTo(_b) != 0;
            case GreaterThan:
                return _a.compareTo(_b) > 0;
            case LessThan:
                return _a.compareTo(_b) < 0;
            case GreaterThanOrEqualTo:
                return _a.compareTo(_b) >= 0;
            case LessThanOrEqualTo:
                return _a.compareTo(_b) <= 0;
            default:
                return false;
        }
    }
    //End Private Methods
}
