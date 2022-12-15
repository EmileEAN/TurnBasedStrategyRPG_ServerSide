package eean_games.tbsg._01;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.lang.Math;

public final class CoreValues 
{
	public static final String URL = "jdbc:mysql://localhost:3306/GameDB?useSSL=false&serverTimezone=CST6CDT";
	public static final String USERNAME = "?????";
	public static final String PASSWORD = "?????";
	
	public static final String GAME_VERSION = "0.5.2";
	
	public static final int GEMS_TO_GIVE_TO_NEW_PLAYER = 100;
	public static final int GOLD_TO_GIVE_TO_NEW_PLAYER = 10000;
	
    public static final BigDecimal DEFAULT_CRITICAL_RATE = new BigDecimal("0.05");
    public static final int MIN_SP_COST = 1;
    public static final int MAX_SP = 7;
    public static final int MIN_BASE_ATTRIBUTE_VALUE = 1;
    public static final int MAX_BASE_ATTRIBUTE_VALUE = 999;
    public static final int MAX_BASE_HP_VALUE = 9999;
    public static final int REQUIRED_EXPERIENCE_FOR_FIRST_LEVEL_UP = 9;
    public static final int SIZE_OF_A_SIDE_OF_BOARD = 7;
    public static final int MAX_MEMBERS_PER_TEAM = 5;
    public static final int MAX_ITEMS_PER_TEAM = 10;
    public static final int DAMAGE_BASE_VALUE = 50;
    public static final int MAX_NUM_OF_ELEMENTS_IN_RECIPE = 5;
    public static final int LEVEL_DIFFERENCE_BETWEEN_RARITIES = 20;
    public static final int MAX_SKILL_LEVEL = 10;
    public static final BigDecimal MULTIPLIER_FOR_ELEMENT_MATCH = new BigDecimal("2.0");
    public static final BigDecimal MULTIPLIER_FOR_EFFECTIVE_ELEMENT = new BigDecimal("2.0");
    public static final BigDecimal MULTIPLIER_FOR_INEFFECTIVE_ELEMENT = new BigDecimal("0.5");
    public static final BigDecimal MULTIPLIER_FOR_CRITICALHIT = new BigDecimal("2.0");
    public static final BigDecimal MULTIPLIER_FOR_TILETYPEMATCH = new BigDecimal("2.0");
    public static final double MULTIPLIER_FOR_ANGLE_TO_RADIAN = Math.PI / 180;
    public static final BigDecimal LEVEL_EXPERIENCE_MULTIPLIER = new BigDecimal("1.0294118"); //This value allows the max accumulated experience value to be the closest to 5,000,000. The value would be 5,050,601.
    public static final int SP_AT_INITIALIZATION = 7;
    public static final BigDecimal POW_ADJUSTMENT_CONST_A = new BigDecimal("19").divide(new BigDecimal("9999"), 50, RoundingMode.HALF_UP);
    public static final BigDecimal POW_ADJUSTMENT_CONST_B = new BigDecimal("1.0").subtract(POW_ADJUSTMENT_CONST_A);
}
