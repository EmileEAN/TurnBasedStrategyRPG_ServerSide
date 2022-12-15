package eean_games.main.extension_method;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalExtension 
{
	public static BigDecimal valueOf(int _value) { return new BigDecimal(String.valueOf(_value)); }
	
    public static BigDecimal Reciprocal(BigDecimal _decimal) { return new BigDecimal("1.0").divide(_decimal); }
	
	public static BigDecimal sum(BigDecimal _a, int _b) { return _a.add(new BigDecimal(String.valueOf(_b))); }
	
	public static BigDecimal subtract(BigDecimal _a, int _b) { return _a.subtract(new BigDecimal(String.valueOf(_b))); }
	
	public static BigDecimal multiply(BigDecimal _a, int _b) { return _a.multiply(new BigDecimal(String.valueOf(_b))); }
	
	public static BigDecimal divide(BigDecimal _a, int _b) { return _a.divide(new BigDecimal(String.valueOf(_b)), 50, RoundingMode.HALF_UP); }
	
	public static BigDecimal divide(int _a, int _b) { return new BigDecimal(String.valueOf(_a)).divide(new BigDecimal(String.valueOf(_b)), 50, RoundingMode.HALF_UP); }
}
