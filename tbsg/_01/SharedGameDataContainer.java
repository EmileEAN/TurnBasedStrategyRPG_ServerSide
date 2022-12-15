package eean_games.tbsg._01;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.codec.binary.Base64;

import eean_games.main.Linq;
import eean_games.main.MapEntry;
import eean_games.main.eRelationType;
import eean_games.tbsg._01.animationInfo.AnimationInfo;
import eean_games.tbsg._01.animationInfo.LaserAnimationInfo;
import eean_games.tbsg._01.animationInfo.MovementAnimationInfo;
import eean_games.tbsg._01.animationInfo.ProjectileAnimationInfo;
import eean_games.tbsg._01.animationInfo.SimpleAnimationInfo;
import eean_games.tbsg._01.animationInfo.eNonMovementAnimationClassification;
import eean_games.tbsg._01.effect.DamageEffect;
import eean_games.tbsg._01.effect.DrainEffect;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.effect.HealEffect;
import eean_games.tbsg._01.effect.MovementEffect;
import eean_games.tbsg._01.effect.StatusEffectAttachmentEffect;
import eean_games.tbsg._01.effect.TileTargetingEffect;
import eean_games.tbsg._01.effect.UnitTargetingEffect;
import eean_games.tbsg._01.effect.UnitTargetingEffectsWrapperEffect;
import eean_games.tbsg._01.equipment.EquipmentData;
import eean_games.tbsg._01.enumerable.eAccessoryClassification;
import eean_games.tbsg._01.enumerable.eActivationTurnClassification;
import eean_games.tbsg._01.enumerable.eArmourClassification;
import eean_games.tbsg._01.enumerable.eAttackClassification;
import eean_games.tbsg._01.enumerable.eCostType;
import eean_games.tbsg._01.enumerable.eElement;
import eean_games.tbsg._01.enumerable.eEventTriggerTiming;
import eean_games.tbsg._01.enumerable.eGender;
import eean_games.tbsg._01.enumerable.eModificationMethod;
import eean_games.tbsg._01.enumerable.eRarity;
import eean_games.tbsg._01.enumerable.eStatusType;
import eean_games.tbsg._01.enumerable.eTargetRangeClassification;
import eean_games.tbsg._01.enumerable.eTargetUnitClassification;
import eean_games.tbsg._01.enumerable.eTileType;
import eean_games.tbsg._01.enumerable.eWeaponClassification;
import eean_games.tbsg._01.enumerable.eWeaponType;
import eean_games.tbsg._01.equipment.Accessory;
import eean_games.tbsg._01.equipment.AccessoryData;
import eean_games.tbsg._01.equipment.Armour;
import eean_games.tbsg._01.equipment.ArmourData;
import eean_games.tbsg._01.equipment.LevelableTransformableWeapon;
import eean_games.tbsg._01.equipment.LevelableWeapon;
import eean_games.tbsg._01.equipment.OrdinaryWeapon;
import eean_games.tbsg._01.equipment.TransformableWeapon;
import eean_games.tbsg._01.equipment.Weapon;
import eean_games.tbsg._01.equipment.WeaponData;
import eean_games.tbsg._01.gacha.AlternativeDispensationInfo;
import eean_games.tbsg._01.gacha.DispensationOption;
import eean_games.tbsg._01.gacha.Gacha;
import eean_games.tbsg._01.gacha.GachaDispensationAvailabilityInfo;
import eean_games.tbsg._01.gacha.GachaObjectInfo;
import eean_games.tbsg._01.gacha.ValuePerRarity;
import eean_games.tbsg._01.gacha.eGachaClassification;
import eean_games.tbsg._01.item.BattleItem;
import eean_games.tbsg._01.item.EnhancementMaterial;
import eean_games.tbsg._01.item.EquipmentMaterial;
import eean_games.tbsg._01.item.EquipmentTradingItem;
import eean_games.tbsg._01.item.EvolutionMaterial;
import eean_games.tbsg._01.item.GachaCostItem;
import eean_games.tbsg._01.item.Item;
import eean_games.tbsg._01.item.ItemMaterial;
import eean_games.tbsg._01.item.SkillEnhancementMaterial;
import eean_games.tbsg._01.item.SkillItem;
import eean_games.tbsg._01.item.SkillMaterial;
import eean_games.tbsg._01.item.UnitEnhancementMaterial;
import eean_games.tbsg._01.item.UnitTradingItem;
import eean_games.tbsg._01.item.WeaponEnhancementMaterial;
import eean_games.tbsg._01.player.Player;
import eean_games.tbsg._01.recipe.AccessoryRecipe;
import eean_games.tbsg._01.recipe.ArmourRecipe;
import eean_games.tbsg._01.recipe.ItemRecipe;
import eean_games.tbsg._01.recipe.UnitEvolutionRecipe;
import eean_games.tbsg._01.recipe.WeaponRecipe;
import eean_games.tbsg._01.skill.ActiveSkill;
import eean_games.tbsg._01.skill.ActiveSkillData;
import eean_games.tbsg._01.skill.CounterSkill;
import eean_games.tbsg._01.skill.CounterSkillData;
import eean_games.tbsg._01.skill.OrdinarySkill;
import eean_games.tbsg._01.skill.OrdinarySkillData;
import eean_games.tbsg._01.skill.PassiveSkillData;
import eean_games.tbsg._01.skill.Skill;
import eean_games.tbsg._01.skill.SkillData;
import eean_games.tbsg._01.skill.UltimateSkill;
import eean_games.tbsg._01.skill.UltimateSkillData;
import eean_games.tbsg._01.status_effect.BackgroundStatusEffectData;
import eean_games.tbsg._01.status_effect.BuffStatusEffectData;
import eean_games.tbsg._01.status_effect.DamageStatusEffectData;
import eean_games.tbsg._01.status_effect.DebuffStatusEffectData;
import eean_games.tbsg._01.status_effect.DurationData;
import eean_games.tbsg._01.status_effect.ForegroundStatusEffectData;
import eean_games.tbsg._01.status_effect.HealStatusEffectData;
import eean_games.tbsg._01.status_effect.StatusEffectData;
import eean_games.tbsg._01.status_effect.TargetRangeModStatusEffectData;
import eean_games.tbsg._01.unit.Unit;
import eean_games.tbsg._01.unit.UnitData;

public class SharedGameDataContainer 
{
    //Getters
    public static List<Player> getPlayers() { return Collections.unmodifiableList(players); }
    public static List<GachaDispensationAvailabilityInfo> getGachaDispensatioAvailabilityInfos() { return Collections.unmodifiableList(gachaDispensationAvailabilityInfos); }
    
    public static List<TileSet> getTileSets() { return Collections.unmodifiableList(tileSets); }
    
    public static List<UnitData> getUnits() { return Collections.unmodifiableList(units); }
    public static List<WeaponData> getWeapons() { return Collections.unmodifiableList(weapons); }
    public static List<ArmourData> getArmours() { return Collections.unmodifiableList(armours); }
    public static List<AccessoryData> getAccessories() { return Collections.unmodifiableList(accessories); }
    public static List<Item> getItems() { return Collections.unmodifiableList(items); }
    
    public static List<SkillData> getSkills() { return Collections.unmodifiableList(skills); }
    public static List<Effect> getEffects() { return Collections.unmodifiableList(effects); }
	public static List<StatusEffectData> getStatusEffects() { return Collections.unmodifiableList(statusEffects); }
	
	public static List<WeaponRecipe> getWeaponRecipes() { return Collections.unmodifiableList(weaponRecipes); }
	public static List<ArmourRecipe> getArmourRecipes() { return Collections.unmodifiableList(armourRecipes); }
	public static List<AccessoryRecipe> getAccessoryRecipes() { return Collections.unmodifiableList(accessoryRecipes); }
	public static List<ItemRecipe> getItemRecipes() { return Collections.unmodifiableList(itemRecipes); }
	
	public static List<Gacha> getGachas() {return Collections.unmodifiableList(gachas); }
	
	public static OrdinarySkill getBasicAttack() { return basicAttack; }
	
	public static boolean getIsInitialized() { return isInitialized; }
	//End Getters
    
    //Private Static Fields
	private static List<Player> players;
	private static List<GachaDispensationAvailabilityInfo> gachaDispensationAvailabilityInfos;

    private static List<UnitData> units;
    private static List<String> unitsAsResponseStrings;
    private static List<WeaponData> weapons;
    private static List<String> weaponsAsResponseStrings;
    private static List<ArmourData> armours;
    private static List<String> armoursAsResponseStrings;
    private static List<AccessoryData> accessories;
    private static List<String> accessoriesAsResponseStrings;
    private static List<Item> items;
    private static List<String> itemsAsResponseStrings;
    
    private static List<SkillData> skills;
    private static List<String> skillsAsResponseStrings;
    private static List<Effect> effects;
    private static List<String> effectsAsResponseStrings;
    private static List<StatusEffectData> statusEffects;
    private static List<String> statusEffectsAsResponseStrings;
    
    private static List<WeaponRecipe> weaponRecipes;
    private static List<String> weaponRecipesAsResponseStrings;
    private static List<ArmourRecipe> armourRecipes;
    private static List<String> armourRecipesAsResponseStrings;
    private static List<AccessoryRecipe> accessoryRecipes;
    private static List<String> accessoryRecipesAsResponseStrings;
    private static List<ItemRecipe> itemRecipes;
    private static List<String> itemRecipesAsResponseStrings;
    
    private static List<Gacha> gachas;
    private static List<String> gachasAsResponseStrings;
    
    private static List<TileSet> tileSets;
    
    private static OrdinarySkill basicAttack;
    
    private static boolean isInitialized = false;
    //End Private Static Fields
    
    public static String getAllDataAsResponseString(Connection _con, int _playerId)
    {
    	if (!isInitialized)
    		return "loadingError";
    	
    	String result = "";
    	
    	result = result + "<StatusEffects>";
    	for (String responseString : statusEffectsAsResponseStrings)
    	{
    		result = result + responseString;
    	}
    	result = result + "</StatusEffects>";
    	
    	result = result + "<Effects>";
    	for (String responseString : effectsAsResponseStrings)
    	{
    		result = result + responseString;
    	}
    	result = result + "</Effects>";
    	
    	result = result + "<Skills>";
    	for (String responseString : skillsAsResponseStrings)
    	{
    		result = result + responseString;
    	}
    	result = result + "</Skills>";
    	
    	result = result + "<Items>";
    	for (String responseString : itemsAsResponseStrings)
    	{
    		result = result + responseString;
    	}
    	result = result + "</Items>";
    	
    	result = result + "<Accessories>";
    	for (String responseString : accessoriesAsResponseStrings)
    	{
    		result = result + responseString;
    	}
    	result = result + "</Accessories>";
    	
    	result = result + "<Armours>";
    	for (String responseString : armoursAsResponseStrings)
    	{
    		result = result + responseString;
    	}
    	result = result + "</Armours>";
    	
    	result = result + "<Weapons>";
    	for (String responseString : weaponsAsResponseStrings)
    	{
    		result = result + responseString;
    	}
    	result = result + "</Weapons>";
    	
    	result = result + "<Units>";
    	for (String responseString : unitsAsResponseStrings)
    	{
    		result = result + responseString;
    	}
    	result = result + "</Units>";
    
    	result = result + "<ItemRecipes>";
    	for (String responseString : itemRecipesAsResponseStrings)
    	{
    		result = result + responseString;
    	}
    	result = result + "</ItemRecipes>";
    	
    	result = result + "<AccessoryRecipes>";
    	for (String responseString : accessoryRecipesAsResponseStrings)
    	{
    		result = result + responseString;
    	}
    	result = result + "</AccessoryRecipes>";
    	
    	result = result + "<ArmourRecipes>";
    	for (String responseString : armourRecipesAsResponseStrings)
    	{
    		result = result + responseString;
    	}
    	result = result + "</ArmourRecipes>";
    	
    	result = result + "<WeaponRecipes>";
    	for (String responseString : weaponRecipesAsResponseStrings)
    	{
    		result = result + responseString;
    	}
    	result = result + "</WeaponRecipes>";
    	
    	result = result + "<Gachas>";
    	for (String responseString : gachasAsResponseStrings)
    	{
    		result = result + responseString;
    	}
    	result = result + "</Gachas>";
    	
    	return result;
    }

    public static String getGachaDataAsResponseString()
    {
    	if (!isInitialized)
    		return "loadingError";
    	
    	String result = "";
    	
    	result = result + "<Gachas>";
    	for (String responseString : gachasAsResponseStrings)
    	{
    		result = result + responseString;
    	}
    	result = result + "</Gachas>";
    	
    	return result;
    }
    
    public static void initialize()
    {  	   	
    	if (isInitialized)
    		return;
    	
    	if (units == null)
    		units = Collections.synchronizedList(new ArrayList<UnitData>());
    	
    	if (unitsAsResponseStrings == null)
    		unitsAsResponseStrings = Collections.synchronizedList(new ArrayList<String>());
    		
    	if (weapons == null)
    		weapons = Collections.synchronizedList(new ArrayList<WeaponData>());
    	
    	if (weaponsAsResponseStrings == null)
    		weaponsAsResponseStrings = Collections.synchronizedList(new ArrayList<String>());
    	
    	if (armours == null)
    		armours = Collections.synchronizedList(new ArrayList<ArmourData>());
    	
    	if (armoursAsResponseStrings == null)
    		armoursAsResponseStrings = Collections.synchronizedList(new ArrayList<String>());
    	
    	if (accessories == null)
    		accessories = Collections.synchronizedList(new ArrayList<AccessoryData>());
    	
    	if (accessoriesAsResponseStrings == null)
    		accessoriesAsResponseStrings = Collections.synchronizedList(new ArrayList<String>());
    	
    	if (items == null)
    		items = Collections.synchronizedList(new ArrayList<Item>());
    	
    	if (itemsAsResponseStrings == null)
    		itemsAsResponseStrings = Collections.synchronizedList(new ArrayList<String>());
    	
    	if (skills == null)
    		skills = Collections.synchronizedList(new ArrayList<SkillData>());
    	
    	if (skillsAsResponseStrings == null)
    		skillsAsResponseStrings = Collections.synchronizedList(new ArrayList<String>());
    	
    	if (effects == null)
    		effects = Collections.synchronizedList(new ArrayList<Effect>());
    	
    	if (effectsAsResponseStrings == null)
    		effectsAsResponseStrings = Collections.synchronizedList(new ArrayList<String>());
    	
    	if (statusEffects == null)
    		statusEffects = Collections.synchronizedList(new ArrayList<StatusEffectData>());
    	
    	if (statusEffectsAsResponseStrings == null)
    		statusEffectsAsResponseStrings = Collections.synchronizedList(new ArrayList<String>());

    	if (weaponRecipes == null)
    		weaponRecipes = Collections.synchronizedList(new ArrayList<WeaponRecipe>());
    	
    	if (weaponRecipesAsResponseStrings == null)
    		weaponRecipesAsResponseStrings = Collections.synchronizedList(new ArrayList<String>());
    	
    	if (armourRecipes == null)
    		armourRecipes = Collections.synchronizedList(new ArrayList<ArmourRecipe>());
    	
    	if (armourRecipesAsResponseStrings == null)
    		armourRecipesAsResponseStrings = Collections.synchronizedList(new ArrayList<String>());
    	
    	if (accessoryRecipes == null)
    		accessoryRecipes = Collections.synchronizedList(new ArrayList<AccessoryRecipe>());
    	
    	if (accessoryRecipesAsResponseStrings == null)
    		accessoryRecipesAsResponseStrings = Collections.synchronizedList(new ArrayList<String>());
    	
    	if (itemRecipes == null)
    		itemRecipes = Collections.synchronizedList(new ArrayList<ItemRecipe>());
    	
    	if (itemRecipesAsResponseStrings == null)
    		itemRecipesAsResponseStrings = Collections.synchronizedList(new ArrayList<String>());
    	
    	if (gachas == null)
    		gachas = Collections.synchronizedList(new ArrayList<Gacha>());
    	
    	if (gachasAsResponseStrings == null)
    		gachasAsResponseStrings = Collections.synchronizedList(new ArrayList<String>());
    	
    	if (tileSets == null)
    		tileSets = Collections.synchronizedList(new ArrayList<TileSet>());
    	
    	if (players == null)
    		players = Collections.synchronizedList(new ArrayList<Player>());
    	
    	if (gachaDispensationAvailabilityInfos == null)
    		gachaDispensationAvailabilityInfos = Collections.synchronizedList(new ArrayList<GachaDispensationAvailabilityInfo>());
    	
    	try 
    	{
    		//-------------------------------Connection----------------------------------------
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection con = DriverManager
					.getConnection(CoreValues.URL, CoreValues.USERNAME, CoreValues.PASSWORD);
			//---------------------------------------------------------------------------------
    		
			loadStatusEffects(con);
    		loadEffects(con);
    		loadSecondaryEffects(con);
    		loadSkills(con);
    		loadUnits(con);
    		loadWeapons(con);
    		loadTransformableWeaponsData(con);
    		loadLevelableTransformableWeaponsData(con);
    		loadArmours(con);
    		loadAccessories(con);
    		loadItems(con);
    		loadUnitEvolutionRecipes(con);
    		
    		loadWeaponRecipes(con);
    		loadArmourRecipes(con);
    		loadAccessoryRecipes(con);
    		loadItemRecipes(con);
    		
    		loadGachas(con);
    		
    		loadTileSets(con);
    		
    		loadPlayers(con);
    		loadSkillInheritors(con);
    		
    		loadGachaDispensationAvailabilityInfos(con);
    		
    		loadStatusEffectsAsResponseStrings();
    		loadEffectsAsResponseStrings();
    		loadSkillsAsResponseStrings();
    		loadUnitsAsResponseStrings();
    		loadWeaponsAsResponseStrings();
    		loadArmoursAsResponseStrings();
    		loadAccessoriesAsResponseStrings();
    		loadItemsAsResponseStrings();
    		loadWeaponRecipesAsResponseStrings();
    		loadArmourRecipesAsResponseStrings();
    		loadAccessoryRecipesAsResponseStrings();
    		loadItemRecipesAsResponseStrings();
    		loadGachasAsResponseStrings();
    		
    		basicAttack = new OrdinarySkill((OrdinarySkillData)(Linq.first(skills, x -> x.Id == -1)), 0);
    		
        	isInitialized = true;
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }

    public static void addPlayer(Connection _con, int _playerId)
    {
    	try 
    	{
    		String query = "select * from Players where Id=?"; //Load the whole row for the player
    		PreparedStatement statement = _con.prepareStatement(query);
    		statement.setInt(1, _playerId);
    		
    		ResultSet resultSet = statement.executeQuery();
    		
    		if (resultSet.next())
    		{
    			Player newPlayer = loadPlayer(_con, resultSet);
    			players.add(newPlayer);
    			
        		//Load gacha dispensation availability info for the player
            	for (Gacha gacha : gachas)
            	{
            		for (DispensationOption dispensationOption : gacha.getDispensationOptions())
            		{
            			int remainingAttempts = gacha.remainingAttempts(_con, newPlayer.Id, dispensationOption);
        				gachaDispensationAvailabilityInfos.add(new GachaDispensationAvailabilityInfo(newPlayer.Id, gacha.Id, dispensationOption.id, remainingAttempts));
            		}
            	}
    		}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }
    
    public static void addGachaObjects(int _playerId, eGachaClassification _gachaClassification, List<Object> _objects)
    {
    	Player player = Linq.firstOrDefault(players, x -> x.Id == _playerId);
    	if (player == null)
    		return;
    	
    	switch (_gachaClassification)
        {
            default: //case Unit
            	for (Object object : _objects) { player.UnitsOwned.add((Unit)object); }
                break;
                
            case Weapon:
            	for (Object object : _objects) { player.WeaponsOwned.add((Weapon)object); }
                break;
            
            case Armour:
            	for (Object object : _objects) { player.ArmoursOwned.add((Armour)object); }
        		break;
            
            case Accessory:
            	for (Object object : _objects) { player.AccessoriesOwned.add((Accessory)object); }
                break;
            
            case SkillItem:
            case SkillMaterial:
            case ItemMaterial:
            case EquipmentMaterial:
            case EvolutionMaterial:
    		case WeaponEnhMaterial:
    		case UnitEnhMaterial:
    		case SkillEnhMaterial:
	            {
	            	for (Object object : _objects) 
	            	{ 
	            		Item item = (Item)object;
	            		if (player.ItemsOwned.containsKey(item))
	            		{
	            			int quantity = player.ItemsOwned.get(item);
	            			player.ItemsOwned.replace(item, quantity + 1);
	            		}
	            		else
	            			player.ItemsOwned.put(item, 1);       		
	            	}
	            }
            	break;
        }
    }
    
    private static void loadUnitsAsResponseStrings()
    {
    	for (UnitData unitData : units)
    	{
    		unitsAsResponseStrings.add(unitDataToResponseString(unitData));
    	}
    }
    
    private static String unitDataToResponseString(UnitData _unitData)
    {
    	String responseString = "<UnitData>";
		
		responseString = responseString + "<Id>" + String.valueOf(_unitData.Id) + "</Id>";
		responseString = responseString  + "<Name>" + _unitData.Name + "</Name>";
		responseString = responseString + "<IconAsBytes>" + Base64.encodeBase64String(_unitData.getIconAsBytes()) + "</IconAsBytes>";
		responseString = responseString + "<Gender>" + _unitData.Gender.toString() + "</Gender>";
		responseString = responseString + "<Rarity>" + _unitData.getRarity().toString() + "</Rarity>";
		responseString = responseString + "<MovementRangeClassification>" + _unitData.MovementRangeClassification.toString() + "</MovementRangeClassification>";
		responseString = responseString + "<NonMovementActionRangeClassification>" + _unitData.NonMovementActionRangeClassification.toString() + "</NonMovementActionRangeClassification>";
		responseString = responseString + "<Element1>" + _unitData.getElements().get(0).toString() + "</Element1>";
		responseString = responseString + "<Element2>" + _unitData.getElements().get(1).toString() + "</Element2>";
		
		responseString = responseString + "<EquipableWeaponClassifications>";
		for (eWeaponClassification classification : _unitData.getEquipableWeaponClassifications())
		{
			responseString = responseString + "<WeaponClassification>";
			responseString = responseString + classification.toString();
			responseString = responseString + "</WeaponClassification>";
		}
		responseString = responseString + "</EquipableWeaponClassifications>";
		
		responseString = responseString + "<EquipableArmourClassifications>";
		for (eArmourClassification classification : _unitData.getEquipableArmourClassifications())
		{
			responseString = responseString + "<ArmourClassification>";
			responseString = responseString + classification.toString();
			responseString = responseString + "</ArmourClassification>";
		}
		responseString = responseString + "</EquipableArmourClassifications>";
		
		responseString = responseString + "<EquipableAccessoryClassifications>";
		for (eAccessoryClassification classification : _unitData.getEquipableAccessoryClassifications())
		{
			responseString = responseString + "<AccessoryClassification>";
			responseString = responseString + classification.toString();
			responseString = responseString + "</AccessoryClassification>";
		}
		responseString = responseString + "</EquipableAccessoryClassifications>";
		
		responseString = responseString + "<MaxLevelHP>" + String.valueOf(_unitData.MaxLevel_HP) + "</MaxLevelHP>";
		responseString = responseString + "<MaxLevelPhysicalStrength>" + String.valueOf(_unitData.MaxLevel_PhysicalStrength) + "</MaxLevelPhysicalStrength>";
		responseString = responseString + "<MaxLevelPhysicalResistance>" + String.valueOf(_unitData.MaxLevel_PhysicalResistance) + "</MaxLevelPhysicalResistance>";
		responseString = responseString + "<MaxLevelMagicalStrength>" + String.valueOf(_unitData.MaxLevel_MagicalStrength) + "</MaxLevelMagicalStrength>";
		responseString = responseString + "<MaxLevelMagicalResistance>" + String.valueOf(_unitData.MaxLevel_MagicalResistance) + "</MaxLevelMagicalResistance>";
		responseString = responseString + "<MaxLevelVitality>" + String.valueOf(_unitData.MaxLevel_Vitality) + "</MaxLevelVitality>";
		
		responseString = responseString + "<SkillIds>";
		for (SkillData skillData : _unitData.getSkills())
		{
			responseString = responseString + "<SkillId>";
			responseString = responseString + String.valueOf(skillData.Id);
			responseString = responseString + "</SkillId>";
		}
		responseString = responseString + "</SkillIds>";
		
		responseString = responseString + "<Labels>";
		for (String label : _unitData.getLabels())
		{
			responseString = responseString + "<Label>";
			responseString = responseString + label;
			responseString = responseString + "</Label>";
		}
		responseString = responseString + "</Labels>";
		
		responseString = responseString + "<Description>" + _unitData.Description + "</Description>";
		
		responseString = responseString + "<ProgressiveEvolutionRecipes>";
		for (UnitEvolutionRecipe progressiveEvolutionRecipe : _unitData.getProgressiveEvolutionRecipes())
		{
			responseString = responseString + unitEvolutionRecipeToResponseString(progressiveEvolutionRecipe, "ProgressiveEvolutionRecipe");
		}
		responseString = responseString + "</ProgressiveEvolutionRecipes>";
		
		UnitEvolutionRecipe retrogressiveEvolutionRecipe = _unitData.getRetrogressiveEvolutionRecipe();
		if (retrogressiveEvolutionRecipe != null)
			responseString = responseString + unitEvolutionRecipeToResponseString(retrogressiveEvolutionRecipe, "RetrogressiveEvolutionRecipe");
		
		responseString = responseString + "</UnitData>";
		
    	return responseString;
    }
    
    private static String unitEvolutionRecipeToResponseString(UnitEvolutionRecipe _unitEvolutionRecipe, String _tagTitle)
    {
    	String responseString = "<" + _tagTitle + ">";
    	
    	responseString = responseString + "<AfterEvolutionUnitId>" + String.valueOf(_unitEvolutionRecipe.UnitAfterEvolution.Id) + "</AfterEvolutionUnitId>";
    	
    	responseString = responseString + "<MaterialIds>";
    	for (int i = 0; i < CoreValues.MAX_NUM_OF_ELEMENTS_IN_RECIPE; i++)
    	{
    		responseString = responseString + "<MaterialId>" + String.valueOf(_unitEvolutionRecipe.getMaterials().get(i)) + "</MaterialId>";
    	}
    	responseString = responseString + "</MaterialIds>";
    	
    	responseString = responseString + "<Cost>" + String.valueOf(_unitEvolutionRecipe.Cost) + "</Cost>";
    	
    	responseString = responseString + "</" + _tagTitle + ">";
    	
    	return responseString;
    }
    
    private static void loadWeaponsAsResponseStrings()
    {
    	for (WeaponData weaponData : weapons)
    	{
    		weaponsAsResponseStrings.add(weaponDataToResponseString(weaponData));
    	}
    }
    
    private static String weaponDataToResponseString(WeaponData _weaponData)
    {
    	String responseString = "<WeaponData>";
		
		responseString = responseString + "<Id>" + String.valueOf(_weaponData.Id) + "</Id>";
		responseString = responseString  + "<Name>" + _weaponData.Name + "</Name>";
		responseString = responseString + "<IconAsBytes>" + Base64.encodeBase64String(_weaponData.getIconAsBytes()) + "</IconAsBytes>";
		responseString = responseString + "<Rarity>" + _weaponData.getRarity().toString() + "</Rarity>";
		
		responseString = responseString + "<StatusEffectDataIds>";
		for (StatusEffectData statusEffectData : _weaponData.getStatusEffectsData())
		{
			responseString = responseString + "<StatusEffectDataId>";
			responseString = responseString + String.valueOf(statusEffectData.Id);
			responseString = responseString + "</StatusEffectDataId>";
		}
		responseString = responseString + "</StatusEffectDataIds>";
		
		responseString = responseString + "<WeaponType>" + _weaponData.WeaponType.toString() + "</WeaponType>";

		responseString = responseString + "<WeaponClassifications>";
		for (eWeaponClassification classification : _weaponData.getWeaponClassifications())
		{
			responseString = responseString + "<WeaponClassification>";
			responseString = responseString + classification.toString();
			responseString = responseString + "</WeaponClassification>";
		}
		responseString = responseString + "</WeaponClassifications>";
		
		if (_weaponData.MainWeaponSkill != null)
		{
			responseString = responseString + "<MainWeaponSkillId>" + _weaponData.MainWeaponSkill.BaseInfo.Id + "</MainWeaponSkillId>";
			responseString = responseString + "<MainWeaponSkillLevel>" + _weaponData.MainWeaponSkill.Level + "</MainWeaponSkillLevel>";
		}
		
		if (_weaponData.WeaponType == eWeaponType.LevelableTransformable || _weaponData.WeaponType == eWeaponType.Transformable)
		{
			responseString = responseString + "<TargetWeaponsInCaseTypeIsTransformable>";
			for (WeaponData targetWeaponData : _weaponData.getTransformableWeapons())
			{
				responseString = responseString + "<WeaponId>";
				responseString = responseString + String.valueOf(targetWeaponData.Id);
				responseString = responseString + "</WeaponId>";
			}
			responseString = responseString + "</TargetWeaponsInCaseTypeIsTransformable>";
		}
		
		responseString = responseString + "</WeaponData>";
		
    	return responseString;
    }
    
    private static void loadArmoursAsResponseStrings()
    {
    	for (ArmourData armourData : armours)
    	{
    		armoursAsResponseStrings.add(armourDataToResponseString(armourData));
    	}
    }
    
    private static String armourDataToResponseString(ArmourData _armourData)
    {
    	String responseString = "<ArmourData>";
		
		responseString = responseString + "<Id>" + String.valueOf(_armourData.Id) + "</Id>";
		responseString = responseString  + "<Name>" + _armourData.Name + "</Name>";
		responseString = responseString + "<IconAsBytes>" + Base64.encodeBase64String(_armourData.getIconAsBytes()) + "</IconAsBytes>";
		responseString = responseString + "<Rarity>" + _armourData.getRarity().toString() + "</Rarity>";
		
		responseString = responseString + "<StatusEffectDataIds>";
		for (StatusEffectData statusEffectData : _armourData.getStatusEffectsData())
		{
			responseString = responseString + "<StatusEffectDataId>";
			responseString = responseString + String.valueOf(statusEffectData.Id);
			responseString = responseString + "</StatusEffectDataId>";
		}
		responseString = responseString + "</StatusEffectDataIds>";
		
		responseString = responseString + "<ArmourClassification>" + _armourData.ArmourClassification.toString() + "</ArmourClassification>";
		responseString = responseString + "<TargetGender>" + _armourData.TargetGender.toString() + "</TargetGender>";
		
		responseString = responseString + "</ArmourData>";
		
    	return responseString;
    }
    
    private static void loadAccessoriesAsResponseStrings()
    {
    	for (AccessoryData accessoryData : accessories)
    	{
    		accessoriesAsResponseStrings.add(accessoryDataToResponseString(accessoryData));
    	}
    }
    
    private static String accessoryDataToResponseString(AccessoryData _accessoryData)
    {
    	String responseString = "<AccessoryData>";
		
		responseString = responseString + "<Id>" + String.valueOf(_accessoryData.Id) + "</Id>";
		responseString = responseString  + "<Name>" + _accessoryData.Name + "</Name>";
		responseString = responseString + "<IconAsBytes>" + Base64.encodeBase64String(_accessoryData.getIconAsBytes()) + "</IconAsBytes>";
		responseString = responseString + "<Rarity>" + _accessoryData.getRarity().toString() + "</Rarity>";
		
		responseString = responseString + "<StatusEffectDataIds>";
		for (StatusEffectData statusEffectData : _accessoryData.getStatusEffectsData())
		{
			responseString = responseString + "<StatusEffectDataId>";
			responseString = responseString + String.valueOf(statusEffectData.Id);
			responseString = responseString + "</StatusEffectDataId>";
		}
		responseString = responseString + "</StatusEffectDataIds>";
		
		responseString = responseString + "<AccessoryClassification>" + _accessoryData.accessoryClassification.toString() + "</AccessoryClassification>";
		responseString = responseString + "<TargetGender>" + _accessoryData.TargetGender.toString() + "</TargetGender>";
		
		responseString = responseString + "</AccessoryData>";
		
    	return responseString;
    }
    
    private static void loadItemsAsResponseStrings()
    {
    	for (Item item : items)
    	{
    		itemsAsResponseStrings.add(itemToResponseString(item));
    	}
    }
    
    private static String itemToResponseString(Item _item)
    {
    	String responseString = "";
		
		if (_item instanceof SkillItem)
			responseString = responseString + "<SkillItem>";
    	else if (_item instanceof SkillMaterial)
    		responseString = responseString + "<SkillMaterial>";
    	else if (_item instanceof ItemMaterial)
    		responseString = responseString + "<ItemMaterial>";
    	else if (_item instanceof EquipmentMaterial)
    		responseString = responseString + "<EquipmentMaterial>";
    	else if (_item instanceof EvolutionMaterial)
    		responseString = responseString + "<EvolutionMaterial>";
    	else if (_item instanceof WeaponEnhancementMaterial)
    		responseString = responseString + "<WeaponEnhancementMaterial>";
    	else if (_item instanceof UnitEnhancementMaterial)
    		responseString = responseString + "<UnitEnhancementMaterial>";
    	else if (_item instanceof SkillEnhancementMaterial)
    		responseString = responseString + "<SkillEnhancementMaterial>";
    	else if (_item instanceof GachaCostItem)
    		responseString = responseString + "<GachaCostItem>";
    	else if (_item instanceof EquipmentTradingItem)
    		responseString = responseString + "<EquipmentTradingItem>";
    	else if (_item instanceof UnitTradingItem)
    		responseString = responseString + "<UnitTradingItem>";
		
		responseString = responseString + "<Id>" + String.valueOf(_item.Id) + "</Id>";
		responseString = responseString  + "<Name>" + _item.Name + "</Name>";
		responseString = responseString + "<IconAsBytes>" + Base64.encodeBase64String(_item.getIconAsBytes()) + "</IconAsBytes>";
		responseString = responseString + "<Rarity>" + _item.getRarity().toString() + "</Rarity>";
		responseString = responseString + "<SellingPrice>" + String.valueOf(_item.SellingPrice) + "</SellingPrice>";
		
		if (_item instanceof SkillItem)
		{
			SkillItem skillItem = (SkillItem)_item;
			
			responseString = responseString + "<SkillId>" + String.valueOf(skillItem.getSkill().BaseInfo.Id) + "</SkillId>";
			responseString = responseString + "<SkillLevel>" + String.valueOf(skillItem.getSkill().Level) + "</SkillLevel>";
			
			responseString = responseString + "</SkillItem>";
		}
		else if (_item instanceof SkillMaterial)
			responseString = responseString + "</SkillMaterial>";
    	else if (_item instanceof ItemMaterial)
    		responseString = responseString + "</ItemMaterial>";
		else if (_item instanceof EquipmentMaterial)
			responseString = responseString + "</EquipmentMaterial>";
    	else if (_item instanceof EvolutionMaterial)
    		responseString = responseString + "</EvolutionMaterial>";
    	else if (_item instanceof WeaponEnhancementMaterial)
    	{
    		WeaponEnhancementMaterial weaponEnhancementMaterial = (WeaponEnhancementMaterial)_item;
    		
    		responseString = responseString + "<EnhancementValue>" + String.valueOf(weaponEnhancementMaterial.enhancementValue) + "</EnhancementValue>";
    		
    		responseString = responseString + "<TargetingWeaponClassifications>";
    		for (eWeaponClassification classification : weaponEnhancementMaterial.getTargetingWeaponClassifications())
    		{
    			responseString = responseString + "<Classification>" + String.valueOf(classification) + "</Classification>";
    		}
    		responseString = responseString + "</TargetingWeaponClassifications>";
    		
    		responseString = responseString + "</WeaponEnhancementMaterial>";
    	}
    	else if (_item instanceof UnitEnhancementMaterial)
    	{
    		UnitEnhancementMaterial unitEnhancementMaterial = (UnitEnhancementMaterial)_item;
    		
    		responseString = responseString + "<EnhancementValue>" + String.valueOf(unitEnhancementMaterial.enhancementValue) + "</EnhancementValue>";
    		
    		responseString = responseString + "<BonusElements>";
    		for (eElement element : unitEnhancementMaterial.getBonusElements())
    		{
    			responseString = responseString + "<Element>" + String.valueOf(element) + "</Element>";
    		}
    		responseString = responseString + "</BonusElements>";
    		
    		responseString = responseString + "</UnitEnhancementMaterial>";
    	}
    	else if (_item instanceof SkillEnhancementMaterial)
    	{
    		SkillEnhancementMaterial skillEnhancementMaterial = (SkillEnhancementMaterial)_item;
    		
    		responseString = responseString + "<EnhancementValue>" + String.valueOf(skillEnhancementMaterial.enhancementValue) + "</EnhancementValue>";
    		
    		responseString = responseString + "<TargetingRarities>";
    		for (eRarity rarity : skillEnhancementMaterial.getTargetingRarities())
    		{
    			responseString = responseString + "<Rarity>" + String.valueOf(rarity) + "</Rarity>";
    		}
    		responseString = responseString + "</TargetingRarities>";
    		
    		responseString = responseString + "<TargetingElements>";
    		for (eElement element : skillEnhancementMaterial.getTargetingElements())
    		{
    			responseString = responseString + "<Element>" + String.valueOf(element) + "</Element>";
    		}
    		responseString = responseString + "</TargetingElements>";
    		
    		responseString = responseString + "<TargetingLabels>";
    		for (String label : skillEnhancementMaterial.getTargetingLabels())
    		{
    			responseString = responseString + "<Label>" + String.valueOf(label) + "</Label>";
    		}
    		responseString = responseString + "</TargetingLabels>";
    		
    		responseString = responseString + "</SkillEnhancementMaterial>";
    	}
    	else if (_item instanceof GachaCostItem)
    		responseString = responseString + "</GachaCostItem>";
    	else if (_item instanceof EquipmentTradingItem)
    		responseString = responseString + "</EquipmentTradingItem>";
    	else if (_item instanceof UnitTradingItem)
    		responseString = responseString + "</UnitTradingItem>";
		
    	return responseString;
    }
    
    private static void loadWeaponRecipesAsResponseStrings()
    {
    	for (WeaponRecipe weaponRecipe : weaponRecipes)
    	{
    		weaponRecipesAsResponseStrings.add(weaponRecipeToResponseString(weaponRecipe));
    	}
    }
    
    private static String weaponRecipeToResponseString(WeaponRecipe _weaponRecipe)
    {
    	String responseString = "<WeaponRecipe>";
		
		responseString = responseString + "<ProductId>" + String.valueOf(_weaponRecipe.Product.Id) + "</ProductId>";

		responseString = responseString + "<MaterialIds>";
		for (EquipmentMaterial equipmentMaterial : _weaponRecipe.getMaterials())
		{
			responseString = responseString + "<MaterialId>";
			responseString = responseString + String.valueOf(equipmentMaterial.Id);
			responseString = responseString + "</MaterialId>";
		}
		responseString = responseString + "</MaterialIds>";
		
		if (_weaponRecipe.WeaponToUpgrade != null)
			responseString = responseString + "<UpgradingWeaponId>" + String.valueOf(_weaponRecipe.WeaponToUpgrade.Id) + "</UpgradingWeaponId>";
		
		responseString = responseString + "<Cost>" + String.valueOf(_weaponRecipe.Cost) + "</Cost>";
		
		responseString = responseString + "</WeaponRecipe>";
		
    	return responseString;
    }
    
    private static void loadArmourRecipesAsResponseStrings()
    {
    	for (ArmourRecipe armourRecipe : armourRecipes)
    	{
    		armourRecipesAsResponseStrings.add(armourRecipeToResponseString(armourRecipe));
    	}
    }
    
    private static String armourRecipeToResponseString(ArmourRecipe _armourRecipe)
    {
    	String responseString = "<ArmourRecipe>";
		
		responseString = responseString + "<ProductId>" + String.valueOf(_armourRecipe.Product.Id) + "</ProductId>";

		responseString = responseString + "<MaterialIds>";
		for (EquipmentMaterial equipmentMaterial : _armourRecipe.getMaterials())
		{
			responseString = responseString + "<MaterialId>";
			responseString = responseString + String.valueOf(equipmentMaterial.Id);
			responseString = responseString + "</MaterialId>";
		}
		responseString = responseString + "</MaterialIds>";
		
		if (_armourRecipe.ArmourToUpgrade != null)
			responseString = responseString + "<UpgradingArmourId>" + String.valueOf(_armourRecipe.ArmourToUpgrade.Id) + "</UpgradingArmourId>";
		
		responseString = responseString + "<Cost>" + String.valueOf(_armourRecipe.Cost) + "</Cost>";
		
		responseString = responseString + "</ArmourRecipe>";
		
    	return responseString;
    }
    
    private static void loadAccessoryRecipesAsResponseStrings()
    {
    	for (AccessoryRecipe accessoryRecipe : accessoryRecipes)
    	{
    		accessoryRecipesAsResponseStrings.add(accessoryRecipeToResponseString(accessoryRecipe));
    	}
    }
    
    private static String accessoryRecipeToResponseString(AccessoryRecipe _accessoryRecipe)
    {
    	String responseString = "<AccessoryRecipe>";
		
		responseString = responseString + "<ProductId>" + String.valueOf(_accessoryRecipe.Product.Id) + "</ProductId>";

		responseString = responseString + "<MaterialIds>";
		for (EquipmentMaterial equipmentMaterial : _accessoryRecipe.getMaterials())
		{
			responseString = responseString + "<MaterialId>";
			responseString = responseString + String.valueOf(equipmentMaterial.Id);
			responseString = responseString + "</MaterialId>";
		}
		responseString = responseString + "</MaterialIds>";
		
		if (_accessoryRecipe.AccessoryToUpgrade != null)
			responseString = responseString + "<UpgradingAccessoryId>" + String.valueOf(_accessoryRecipe.AccessoryToUpgrade.Id) + "</UpgradingAccessoryId>";
		
		responseString = responseString + "<Cost>" + String.valueOf(_accessoryRecipe.Cost) + "</Cost>";
		
		responseString = responseString + "</AccessoryRecipe>";
		
    	return responseString;
    }
    
    private static void loadItemRecipesAsResponseStrings()
    {
    	for (ItemRecipe itemRecipe : itemRecipes)
    	{
    		itemRecipesAsResponseStrings.add(itemRecipeToResponseString(itemRecipe));
    	}
    }
    
    private static String itemRecipeToResponseString(ItemRecipe _itemRecipe)
    {
    	String responseString = "<ItemRecipe>";
		
		responseString = responseString + "<ProductId>" + String.valueOf(_itemRecipe.Product.Id) + "</ProductId>";

		responseString = responseString + "<MaterialIds>";
		for (ItemMaterial itemMaterial : _itemRecipe.getMaterials())
		{
			responseString = responseString + "<MaterialId>";
			responseString = responseString + String.valueOf(itemMaterial.Id);
			responseString = responseString + "</MaterialId>";
		}
		responseString = responseString + "</MaterialIds>";
		
		if (_itemRecipe.ItemToUpgrade != null)
			responseString = responseString + "<UpgradingItemId>" + String.valueOf(_itemRecipe.ItemToUpgrade.Id) + "</UpgradingItemId>";
		
		responseString = responseString + "<Cost>" + String.valueOf(_itemRecipe.Cost) + "</Cost>";
		
		responseString = responseString + "</ItemRecipe>";
		
    	return responseString;
    }
    
    private static void loadGachasAsResponseStrings()
    {
    	gachasAsResponseStrings.clear();
    	
    	for (Gacha gacha : gachas)
    	{
    		gachasAsResponseStrings.add(gachaToResponseString(gacha));
    	}
    }
    
    private static String gachaToResponseString(Gacha _gacha)
    {
    	String responseString = "<Gacha>";
		
		responseString = responseString + "<Id>" + String.valueOf(_gacha.Id) + "</Id>";
		responseString = responseString  + "<Title>" + _gacha.Title + "</Title>";
		responseString = responseString  + "<GachaClassification>" + _gacha.gachaClassification.toString() + "</GachaClassification>";
		
		responseString = responseString + "<GachaObjectInfos>";
		for (GachaObjectInfo gachaObjectInfo : _gacha.getGachaObjectInfos())
		{
			int objectId = 0;
			switch (_gacha.gachaClassification)
			{
				default: // case Unit
					objectId = ((UnitData)(gachaObjectInfo.object)).Id;
					break;
					
				case Weapon:
				case Armour:
				case Accessory:
					objectId = ((EquipmentData)(gachaObjectInfo.object)).Id;
					break;
					
				case SkillItem:
				case SkillMaterial:
				case ItemMaterial:
				case EquipmentMaterial:
				case EvolutionMaterial:
	    		case WeaponEnhMaterial:
	    		case UnitEnhMaterial:
	    		case SkillEnhMaterial:
					objectId = ((Item)(gachaObjectInfo.object)).Id;
					break;
			}
			
			responseString = responseString + "<GachaObjectInfo>";
			responseString = responseString + "<ObjectId>" + String.valueOf(objectId) + "</ObjectId>";
			responseString = responseString + "<RelativeOccurrenceValue>" + String.valueOf(gachaObjectInfo.relativeOccurenceValue) + "</RelativeOccurrenceValue>";
			responseString = responseString + "</GachaObjectInfo>";
		}
		responseString = responseString + "</GachaObjectInfos>";
		
		responseString = responseString + valuePerRarityToResponseString(_gacha.DefaultDispensationValues, "DefaultDispensationValues");
		
		if (_gacha.AlternativeDispensationInfo != null)
		{
			responseString = responseString + "<AlternativeDispensationInfo>";
			responseString = responseString + "<ApplyAtXthDispensation>" + String.valueOf(_gacha.AlternativeDispensationInfo.applyAtXthDispensation) + "</ApplyAtXthDispensation>";
			responseString = responseString + valuePerRarityToResponseString(_gacha.AlternativeDispensationInfo.ratioPerRarity, "RatioPerRarity");
			responseString = responseString + "</AlternativeDispensationInfo>";
		}
		
		responseString = responseString + "<DispensationOptions>";
		for (DispensationOption dispensationOption : _gacha.getDispensationOptions())
		{	
			responseString = responseString + "<DispensationOption>";
			responseString = responseString + "<Id>" + String.valueOf(dispensationOption.id) + "</Id>";
			responseString = responseString + "<CostType>" + String.valueOf(dispensationOption.costType) + "</CostType>";
			responseString = responseString + "<CostItemId>" + String.valueOf(dispensationOption.costItemId) + "</CostItemId>";
			responseString = responseString + "<CostValue>" + String.valueOf(dispensationOption.costValue) + "</CostValue>";
			responseString = responseString + "<TimesToDispense>" + String.valueOf(dispensationOption.timesToDispense) + "</TimesToDispense>";
			responseString = responseString + "<IsNumberOfAttemptsPerDay>" + String.valueOf(dispensationOption.isNumberOfAttemptsPerDay) + "</IsNumberOfAttemptsPerDay>";
			responseString = responseString + "</DispensationOption>";
		}
		responseString = responseString + "</DispensationOptions>";
		
		responseString = responseString + "<BannerImageAsBytes>" + Base64.encodeBase64String(_gacha.getBannerImageAsBytes()) + "</BannerImageAsBytes>";
		responseString = responseString + "<BackgroundImageAsBytes>" + Base64.encodeBase64String(_gacha.getGachaSceneBackgroundImageAsBytes()) + "</BackgroundImageAsBytes>";

		responseString = responseString + "<LevelOfObjects>" + String.valueOf(_gacha.levelOfObjects) + "</LevelOfObjects>";
		
    	responseString = responseString + "</Gacha>";
		
    	return responseString;
    }
    
    private static String valuePerRarityToResponseString(ValuePerRarity _valuePerRarity, String _tagTitle)
    {
    	String responseString = "<" + _tagTitle + ">";
    	
		responseString = responseString + "<Common>" + String.valueOf(_valuePerRarity.common) + "</Common>";
		responseString = responseString + "<Uncommon>" + String.valueOf(_valuePerRarity.uncommon) + "</Uncommon>";
		responseString = responseString + "<Rare>" + String.valueOf(_valuePerRarity.rare) + "</Rare>";
		responseString = responseString + "<Epic>" + String.valueOf(_valuePerRarity.epic) + "</Epic>";
		responseString = responseString + "<Legendary>" + String.valueOf(_valuePerRarity.legendary) + "</Legendary>";
    	
    	responseString = responseString + "</" + _tagTitle + ">";
    	
    	return responseString;
    }
    
    private static void loadSkillsAsResponseStrings()
    {
    	for (SkillData skillData : skills)
    	{
    		skillsAsResponseStrings.add(SkillDataToResponseString(skillData));
    	}
    }
    
    private static String SkillDataToResponseString(SkillData _skillData)
    {
    	String responseString = "";
    	
    	if (_skillData instanceof ActiveSkillData)
    	{
    		ActiveSkillData activeSkillData = (ActiveSkillData)_skillData;
			
    		if (activeSkillData instanceof OrdinarySkillData)
    			responseString = responseString + "<OrdinarySkillData>";
        	else if (activeSkillData instanceof CounterSkillData)
        		responseString = responseString + "<CounterSkillData>";
        	else if (activeSkillData instanceof UltimateSkillData)
        		responseString = responseString + "<UltimateSkillData>";
    		
    		responseString = responseString + "<Id>" + String.valueOf(activeSkillData.Id) + "</Id>";
			responseString = responseString  + "<Name>" + activeSkillData.Name + "</Name>";
			responseString = responseString + "<IconAsBytes>" + Base64.encodeBase64String(activeSkillData.getIconAsBytes()) + "</IconAsBytes>";
			
			responseString = responseString + "<TemporalStatusEffectDataIds>";
			for (BackgroundStatusEffectData temporalStatusEffectData : Linq.ofType(activeSkillData.getStatusEffectsData(), BackgroundStatusEffectData.class))
			{
				responseString = responseString + "<StatusEffectDataId>";
				responseString = responseString + String.valueOf(temporalStatusEffectData.Id);
				responseString = responseString + "</StatusEffectDataId>";
			}
			responseString = responseString + "</TemporalStatusEffectDataIds>";
			
			responseString = responseString + "<SkillActivationAnimationId>" + String.valueOf(activeSkillData.SkillActivationAnimationId) + "</SkillActivationAnimationId>";
			responseString = responseString + "<MaxNumberOfTargets>" + activeSkillData.getMaxNumberOfTargets().completeTagString + "</MaxNumberOfTargets>";
			responseString = responseString + "<EffectId>" + String.valueOf(activeSkillData.getEffect().Id) + "</EffectId>";
			
			if (activeSkillData instanceof OrdinarySkillData)
			{
				OrdinarySkillData ordinarySkillData = (OrdinarySkillData)activeSkillData;
				
				responseString = responseString + "<SPCost>" + ordinarySkillData.SPCost + "</SPCost>";
				responseString = responseString + ItemCostsToResponseString(ordinarySkillData.getItemCosts(), "ItemCosts");
				
    			responseString = responseString + "</OrdinarySkillData>";
			}
    		else if (activeSkillData instanceof CounterSkillData)
			{
	    		CounterSkillData counterSkillData = (CounterSkillData)activeSkillData;
				
				responseString = responseString + "<SPCost>" + counterSkillData.SPCost + "</SPCost>";
				responseString = responseString + ItemCostsToResponseString(counterSkillData.getItemCosts(), "ItemCosts");
				responseString = responseString + "<EventTriggerTiming>" + counterSkillData.EventTriggerTiming.toString() + "</EventTriggerTiming>";
				responseString = responseString + complexConditionToResponseString(counterSkillData.getActivationCondition(), "ActivationCondition");
			
    			responseString = responseString + "</CounterSkillData>";
			}
	    	else if (activeSkillData instanceof UltimateSkillData)
	    		responseString = responseString + "</UltimateSkillData>";
    	}
    	else if (_skillData instanceof PassiveSkillData)
    	{
			responseString = responseString + "<PassiveSkillData>";
    		
    		PassiveSkillData passiveSkillData = (PassiveSkillData)_skillData;
    		
    		responseString = responseString + "<Id>" + String.valueOf(passiveSkillData.Id) + "</Id>";
			responseString = responseString  + "<Name>" + passiveSkillData.Name + "</Name>";
			responseString = responseString + "<IconAsBytes>" + Base64.encodeBase64String(passiveSkillData.getIconAsBytes()) + "</IconAsBytes>";
			
			responseString = responseString + "<TemporalStatusEffectDataIds>";
			for (StatusEffectData temporalStatusEffectData : passiveSkillData.getStatusEffectsData())
			{
				responseString = responseString + "<StatusEffectDataId>";
				responseString = responseString + String.valueOf(temporalStatusEffectData.Id);
				responseString = responseString + "</StatusEffectDataId>";
			}
			responseString = responseString + "</TemporalStatusEffectDataIds>";
			
			responseString = responseString + "<SkillActivationAnimationId>" + String.valueOf(passiveSkillData.SkillActivationAnimationId) + "</SkillActivationAnimationId>";
			responseString = responseString + "<TargetClassification>" + passiveSkillData.TargetClassification.toString() + "</TargetClassification>";
			responseString = responseString + "<ActivationCondition>" + complexConditionToResponseString(passiveSkillData.getActivationCondition(), "ActivationCondition") + "</ActivationCondition>";
			
			
	    	responseString = responseString + "</PassiveSkillData>";
    	}
    	
    	return responseString;
    }
    
    private static String ItemCostsToResponseString(Map<Integer, Integer> _itemCosts, String _tagTitle)
    {
    	String responseString = "<" + _tagTitle + ">";
    	
    	for (Map.Entry<Integer, Integer> itemCost : _itemCosts.entrySet())
    	{
        	responseString = responseString + "<ItemCost>";
        	responseString = responseString + "<ItemId>" + String.valueOf(itemCost.getKey()) + "</ItemId>";
        	responseString = responseString + "<Quantity>" + String.valueOf(itemCost.getValue()) + "</Quantity>";
        	responseString = responseString + "</ItemCost>";
    	}
    	
    	responseString = responseString + "</" + _tagTitle + ">";
    	
    	return responseString;
    }
    
    private static void loadEffectsAsResponseStrings()
    {
    	for (Effect effect : effects)
    	{
        	effectsAsResponseStrings.add(effectToResponseString(effect));
    	}
    }
    
    private static String effectToResponseString(Effect _effect)
    {
    	String responseString = "";
    	
    	if (_effect instanceof UnitTargetingEffect)
    	{
    		UnitTargetingEffect unitTargetingEffect = (UnitTargetingEffect)_effect;
			
    		if (unitTargetingEffect instanceof UnitTargetingEffectsWrapperEffect)
    			responseString = responseString + "<UnitTargetingEffectsWrapperEffect>";
    		else if (unitTargetingEffect instanceof DamageEffect)
    			responseString = responseString + "<DamageEffect>";
    		else if (unitTargetingEffect instanceof DrainEffect)
    			responseString = responseString + "<DrainEffect>";
        	else if (unitTargetingEffect instanceof HealEffect)
        		responseString = responseString + "<HealEffect>";
        	else if (unitTargetingEffect instanceof StatusEffectAttachmentEffect)
        		responseString = responseString + "<StatusEffectAttachmentEffect>";
    		
    		responseString = responseString + "<Id>" + String.valueOf(unitTargetingEffect.Id) + "</Id>";
			responseString = responseString  + complexConditionToResponseString(unitTargetingEffect.getActivationCondition(), "ActivationCondition");
			if (!(unitTargetingEffect instanceof UnitTargetingEffectsWrapperEffect))
			{
				responseString = responseString + "<TimesToApply>" + unitTargetingEffect.getTimesToApply().completeTagString + "</TimesToApply>";
				responseString = responseString + "<SuccessRate>" + unitTargetingEffect.getSuccessRate().completeTagString + "</SuccessRate>";
				responseString = responseString + "<DiffusionDistance>" + unitTargetingEffect.getDiffusionDistance().completeTagString + "</DiffusionDistance>";
				
				responseString = responseString + animationInfoToResponseString(unitTargetingEffect.AnimationInfo);
			}
			
			responseString = responseString + "<SecondaryEffectIds>";
			for (Effect secondaryEffect : unitTargetingEffect.getSecondaryEffects())
			{
				responseString = responseString + "<SecondaryEffectId>";
				responseString = responseString + String.valueOf(secondaryEffect.Id);
				responseString = responseString + "</SecondaryEffectId>";
			}
			responseString = responseString + "</SecondaryEffectIds>";
			
			responseString = responseString + "<TargetClassification>" + unitTargetingEffect.TargetClassification.toString() + "</TargetClassification>";
			
			if (unitTargetingEffect instanceof UnitTargetingEffectsWrapperEffect)
			{
    			responseString = responseString + "</UnitTargetingEffectsWrapperEffect>";
			}
			if (unitTargetingEffect instanceof DamageEffect)
			{
				DamageEffect damageEffect = (DamageEffect)unitTargetingEffect;
				
				responseString = responseString + "<AttackClassification>" + damageEffect.AttackClassification.toString() + "</AttackClassification>";
				responseString = responseString + "<Value>" + damageEffect.getValue().completeTagString + "</Value>";
				responseString = responseString + "<IsFixedValue>" + String.valueOf(damageEffect.IsFixedValue) + "</IsFixedValue>";
				responseString = responseString + "<Element>" + damageEffect.Element.toString() + "</Element>";
			
    			responseString = responseString + "</DamageEffect>";
			}
			if (unitTargetingEffect instanceof DrainEffect)
			{
				DrainEffect drainEffect = (DrainEffect)unitTargetingEffect;
				
				responseString = responseString + "<MaxNumberOfSecondaryTargets>" + drainEffect.getMaxNumberOfSecondaryTargets().completeTagString + "</MaxNumberOfSecondaryTargets>";
				responseString = responseString + "<AttackClassification>" + drainEffect.AttackClassification.toString() + "</AttackClassification>";
				responseString = responseString + "<Value>" + drainEffect.getValue().completeTagString + "</Value>";
				responseString = responseString + "<IsFixedValue>" + String.valueOf(drainEffect.IsFixedValue) + "</IsFixedValue>";
				responseString = responseString + "<Element>" + drainEffect.Element.toString() + "</Element>";
				responseString = responseString + "<DrainingEfficiency>" + drainEffect.getDrainingEfficiency().completeTagString + "</DrainingEfficiency>";
				responseString = responseString + "<HealAnimationInfo>" + animationInfoToResponseString(drainEffect.healAnimationInfo) + "</HealAnimationInfo>";
				
    			responseString = responseString + "</DrainEffect>";
			}
	    	else if (unitTargetingEffect instanceof HealEffect)
			{
	    		HealEffect healEffect = (HealEffect)unitTargetingEffect;
				
				responseString = responseString + "<Value>" + healEffect.getValue().completeTagString + "</Value>";
				responseString = responseString + "<IsFixedValue>" + String.valueOf(healEffect.IsFixedValue) + "</IsFixedValue>";
			
    			responseString = responseString + "</HealEffect>";
			}
	    	else if (unitTargetingEffect instanceof StatusEffectAttachmentEffect)
			{
	    		StatusEffectAttachmentEffect statusEffectAttachmentEffect = (StatusEffectAttachmentEffect)unitTargetingEffect;
				
	    		responseString = responseString + "<StatusEffectDataId>" + String.valueOf(statusEffectAttachmentEffect.getDataOfStatusEffectToAttach().Id) + "</StatusEffectDataId>";
	    		
	    		responseString = responseString + "</StatusEffectAttachmentEffect>";
			}
    	}
    	else if (_effect instanceof TileTargetingEffect)
    	{
    		TileTargetingEffect tileTargetingEffect = (TileTargetingEffect)_effect;
			
    		if (tileTargetingEffect instanceof MovementEffect)
    			responseString = responseString + "<MovementEffect>";
    		
    		responseString = responseString + "<Id>" + String.valueOf(tileTargetingEffect.Id) + "</Id>";
			responseString = responseString  + complexConditionToResponseString(tileTargetingEffect.getActivationCondition(), "ActivationCondition");
			responseString = responseString + "<TimesToApply>" + tileTargetingEffect.getTimesToApply().completeTagString + "</TimesToApply>";
			if (!(tileTargetingEffect instanceof MovementEffect))
				responseString = responseString + "<SuccessRate>" + tileTargetingEffect.getSuccessRate().completeTagString + "</SuccessRate>";
			responseString = responseString + "<DiffusionDistance>" + tileTargetingEffect.getDiffusionDistance().completeTagString + "</DiffusionDistance>";
			
			responseString = responseString + "<SecondaryEffectIds>";
			for (Effect secondaryEffect : tileTargetingEffect.getSecondaryEffects())
			{
				responseString = responseString + "<SecondaryEffectId>";
				responseString = responseString + String.valueOf(secondaryEffect.Id);
				responseString = responseString + "</SecondaryEffectId>";
			}
			responseString = responseString + "</SecondaryEffectIds>";
			
			responseString = responseString + animationInfoToResponseString(tileTargetingEffect.AnimationInfo);
			
			if (tileTargetingEffect instanceof MovementEffect)
			{
				@SuppressWarnings("unused")
				MovementEffect movementEffect = (MovementEffect)tileTargetingEffect;
				
    			responseString = responseString + "</MovementEffect>";
			}
    	}
    	
    	return responseString;
    }
    
    public static String animationInfoToResponseString(AnimationInfo _animationInfo)
    {
    	String responseString = "";
    	if (_animationInfo == null)
    		return responseString;
    	
		if (_animationInfo instanceof SimpleAnimationInfo)
		{
			SimpleAnimationInfo simpleAnimationInfo = (SimpleAnimationInfo)_animationInfo;
			responseString = responseString + "<SimpleAnimationInfo>";
			responseString = responseString + "<HitEffectId>" + String.valueOf(simpleAnimationInfo.hitEffectId) + "</HitEffectId>";
			responseString = responseString + "</SimpleAnimationInfo>";
		}
		else if (_animationInfo instanceof ProjectileAnimationInfo)
		{
			ProjectileAnimationInfo projectileAnimationInfo = (ProjectileAnimationInfo)_animationInfo;
			responseString = responseString + "<ProjectileAnimationInfo>";
			responseString = responseString + "<ProjectileGenerationPointId>" + String.valueOf(projectileAnimationInfo.projectileGenerationPointId) + "</ProjectileGenerationPointId>";
			responseString = responseString + "<ProjectileId>" + String.valueOf(projectileAnimationInfo.projectileId) + "</ProjectileId>";
			responseString = responseString + "<HitEffectId>" + String.valueOf(projectileAnimationInfo.hitEffectId) + "</HitEffectId>";
			responseString = responseString + "</ProjectileAnimationInfo>";
		}
		else if (_animationInfo instanceof LaserAnimationInfo)
		{
			LaserAnimationInfo laserAnimationInfo = (LaserAnimationInfo)_animationInfo;
			responseString = responseString + "<LaserAnimationInfo>";
			responseString = responseString + "<LaserGenerationPointId>" + String.valueOf(laserAnimationInfo.laserGenerationPointId) + "</LaserGenerationPointId>";
			responseString = responseString + "<LaserEffectId>" + String.valueOf(laserAnimationInfo.laserEffectId) + "</LaserEffectId>";
			responseString = responseString + "<HitEffectId>" + String.valueOf(laserAnimationInfo.hitEffectId) + "</HitEffectId>";
			responseString = responseString + "</LaserAnimationInfo>";
		}
		else if (_animationInfo instanceof MovementAnimationInfo)
		{
			MovementAnimationInfo movementAnimationInfo = (MovementAnimationInfo)_animationInfo;
			responseString = responseString + "<MovementAnimationInfo>";
			responseString = responseString + "<AttachmentEffectId>" + String.valueOf(movementAnimationInfo.attachmentEffectId) + "</AttachmentEffectId>";
			responseString = responseString + "</MovementAnimationInfo>";
		}
		
		return responseString;
    }
    
    private static void loadStatusEffectsAsResponseStrings()
    {
    	for (StatusEffectData statusEffectData : statusEffects)
    	{
    		statusEffectsAsResponseStrings.add(statusEffectDataToResponseString(statusEffectData));
    	}
    }
    
    private static String statusEffectDataToResponseString(StatusEffectData _statusEffectData)
    {
    	String responseString = "";
    	
    	if (_statusEffectData instanceof BackgroundStatusEffectData)
    	{
    		BackgroundStatusEffectData backgroundStatusEffectData = (BackgroundStatusEffectData)_statusEffectData;
			
    		if (backgroundStatusEffectData instanceof BuffStatusEffectData)
    			responseString = responseString + "<BuffStatusEffectData>";
        	else if (backgroundStatusEffectData instanceof DebuffStatusEffectData)
        		responseString = responseString + "<DebuffStatusEffectData>";
        	else if (backgroundStatusEffectData instanceof TargetRangeModStatusEffectData)
        		responseString = responseString + "<TargetRangeModStatusEffectData>";
    		
    		responseString = responseString + "<Id>" + String.valueOf(backgroundStatusEffectData.Id) + "</Id>";
    		responseString = responseString + durationDataToResponseString(backgroundStatusEffectData.getDuration(), "Duration");
			responseString = responseString + complexConditionToResponseString(backgroundStatusEffectData.getActivationCondition(), "ActivationCondition");
			responseString = responseString + "<IconAsBytes>" + Base64.encodeBase64String(backgroundStatusEffectData.getIconAsBytes()) + "</IconAsBytes>";
			responseString = responseString + "<ActivationTurnClassification>" + backgroundStatusEffectData.ActivationTurnClassification.toString() + "</ActivationTurnClassification>";
			
			if (backgroundStatusEffectData instanceof BuffStatusEffectData)
			{
				BuffStatusEffectData buffStatusEffectData = (BuffStatusEffectData)backgroundStatusEffectData;
				
				responseString = responseString + "<TargetStatusType>" + buffStatusEffectData.TargetStatusType.toString() + "</TargetStatusType>";
				responseString = responseString + "<Value>" + buffStatusEffectData.getValue().completeTagString + "</Value>";
				responseString = responseString + "<IsSum>" + String.valueOf(buffStatusEffectData.IsSum) + "</IsSum>";
				
    			responseString = responseString + "</BuffStatusEffectData>";
			}
	    	else if (backgroundStatusEffectData instanceof DebuffStatusEffectData)
			{
	    		DebuffStatusEffectData debuffStatusEffectData = (DebuffStatusEffectData)backgroundStatusEffectData;
				
				responseString = responseString + "<TargetStatusType>" + debuffStatusEffectData.TargetStatusType.toString() + "</TargetStatusType>";
				responseString = responseString + "<Value>" + debuffStatusEffectData.getValue().completeTagString + "</Value>";
				responseString = responseString + "<IsSum>" + String.valueOf(debuffStatusEffectData.IsSum) + "</IsSum>";
			
    			responseString = responseString + "</DebuffStatusEffectData>";
			}
	    	else if (backgroundStatusEffectData instanceof TargetRangeModStatusEffectData)
			{
	    		TargetRangeModStatusEffectData targetRangeModStatusEffectData = (TargetRangeModStatusEffectData)backgroundStatusEffectData;
				
				responseString = responseString + "<IsMovementRangeClassification>" + String.valueOf(targetRangeModStatusEffectData.IsMovementRangeClassification) + "</IsMovementRangeClassification>";
	    		responseString = responseString + "<TargetRangeClassification>" + targetRangeModStatusEffectData.TargetRangeClassification.toString() + "</TargetRangeClassification>";
				responseString = responseString + "<ModificationMethod>" + targetRangeModStatusEffectData.ModificationMethod.toString() + "</ModificationMethod>";

	    		responseString = responseString + "</TargetRangeModStatusEffectData>";
			}
    	}
    	else if (_statusEffectData instanceof ForegroundStatusEffectData)
    	{
    		ForegroundStatusEffectData foregroundStatusEffectData = (ForegroundStatusEffectData)_statusEffectData;
			
    		if (foregroundStatusEffectData instanceof DamageStatusEffectData)
    			responseString = responseString + "<DamageStatusEffectData>";
        	else if (foregroundStatusEffectData instanceof HealStatusEffectData)
        		responseString = responseString + "<HealStatusEffectData>";
    		
    		responseString = responseString + "<Id>" + String.valueOf(foregroundStatusEffectData.Id) + "</Id>";
    		responseString = responseString + durationDataToResponseString(foregroundStatusEffectData.getDuration(), "Duration");
			responseString = responseString + complexConditionToResponseString(foregroundStatusEffectData.getActivationCondition(), "ActivationCondition");
			responseString = responseString + "<IconAsBytes>" + Base64.encodeBase64String(foregroundStatusEffectData.getIconAsBytes()) + "</IconAsBytes>";
			responseString = responseString + "<ActivationTurnClassification>" + foregroundStatusEffectData.ActivationTurnClassification.toString() + "</ActivationTurnClassification>";
			responseString = responseString + "<EventTriggerTiming>" + foregroundStatusEffectData.EventTriggerTiming.toString() + "</EventTriggerTiming>";
			responseString = responseString + animationInfoToResponseString(foregroundStatusEffectData.AnimationInfo);
			
			if (foregroundStatusEffectData instanceof DamageStatusEffectData)
			{
				DamageStatusEffectData damageStatusEffectData = (DamageStatusEffectData)foregroundStatusEffectData;
				
				responseString = responseString + "<Value>" + damageStatusEffectData.getValue().completeTagString + "</Value>";
				
    			responseString = responseString + "</DamageStatusEffectData>";
			}
	    	else if (foregroundStatusEffectData instanceof HealStatusEffectData)
			{
	    		HealStatusEffectData healStatusEffectData = (HealStatusEffectData)foregroundStatusEffectData;
				
				responseString = responseString + "<Value>" + healStatusEffectData.getValue().completeTagString + "</Value>";
			
    			responseString = responseString + "</HealStatusEffectData>";
			}
    	}
    	
    	return responseString;
    }
    
    private static String durationDataToResponseString(DurationData _durationData, String _tagTitle)
    {
    	String responseString = "<" + _tagTitle + ">";
    	
    	responseString = responseString + "<ActivationTimes>" + _durationData.getActivationTimes().completeTagString + "</ActivationTimes>";
    	responseString = responseString + "<Turns>" + _durationData.getTurns().completeTagString + "</Turns>";
    	responseString = responseString + complexConditionToResponseString(_durationData.getWhileCondition(), "WhileCondition");
    	
    	responseString = responseString + "</" + _tagTitle + ">";;
    	
    	return responseString;
    }
    
    private static String complexConditionToResponseString(ComplexCondition _complexCondition, String _tagTitle)
    {
    	String responseString = "<" + _tagTitle + ">";
    	
    	for (List<Condition> conditionSet : _complexCondition.getConditionSets())
    	{
        	responseString = responseString + "<ConditionSet>";
        	
        	for (Condition condition : conditionSet)
        	{
            	responseString = responseString + conditionToResponseString(condition);
        	}
        	
        	responseString = responseString + "</ConditionSet>";
    	}
    	
    	responseString = responseString + "</" + _tagTitle + ">";
    	
    	return responseString;
    }
    
    private static String conditionToResponseString(Condition _condition)
    {
    	String responseString = "<Condition>";
    	
    	responseString = responseString + "<TagA>" + _condition.getA().completeTagString + "</TagA>";
    	responseString = responseString + "<RelationType>" + _condition.RelationType.toString() + "</RelationType>";
    	responseString = responseString + "<TagB>" + _condition.getB().completeTagString + "</TagB>";
    	
    	responseString = responseString + "</Condition>";
    	
    	return responseString;
    }
    
    private static void loadItems(Connection _con)
    {
    	items.clear();
    	
    	try 
    	{	    		
			String query = "select Id from BaseItems"; //Load the entire BaseItems.Id column
			PreparedStatement statement = _con.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int id = resultSet.getInt("Id");
				items.add(loadItem(_con, id));
			}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }
    
    private static Item loadItem(Connection _con, int _id)
    {
    	try
    	{
    		String query = "select * from BaseItems where Id=?"; //Load row in the BaseItems table for the item with _id
    		PreparedStatement statement = _con.prepareStatement(query);
        	statement.setInt(1, _id);
    		
    		ResultSet resultSet = statement.executeQuery();
    		if (!resultSet.next())
    			return null;
    		
    		String name = resultSet.getString("Name");
			int classificationId = resultSet.getInt("ClassificationId");
			int rarityId = resultSet.getInt("RarityId");
			int sellingPrice = resultSet.getInt("SellingPrice");
    		String iconPath = resultSet.getString("IconPath");
    		
			BufferedImage icon = ImageIO.read(new File(iconPath));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(icon, "png", outputStream);
	    	byte[] iconAsBytes = outputStream.toByteArray();
    		eRarity rarity = loadRarity(_con, rarityId);
    		
    		query = "select Name from ItemClassifications where Id=?"; //Get classification name for the item
    		statement = _con.prepareStatement(query);
    		statement.setInt(1, classificationId);
    		
    		ResultSet resultSet2 = statement.executeQuery();
    		if (!resultSet2.next())
    			return null;
    		
    		String classificationName = resultSet2.getString("Name");
    		switch (classificationName)
    		{
    			case "SkillItem":
    				return loadSkillItem(_con, _id, name, iconAsBytes, rarity, sellingPrice);
    			case "SkillMaterial":
    				return new SkillMaterial(_id, name, iconAsBytes, rarity, sellingPrice);
    			case "ItemMaterial":
    				return new ItemMaterial(_id, name, iconAsBytes, rarity, sellingPrice);
    			case "EquipmentMaterial":
    				return new EquipmentMaterial(_id, name, iconAsBytes, rarity, sellingPrice);
    			case "EvolutionMaterial":
    				return new EvolutionMaterial(_id, name, iconAsBytes, rarity, sellingPrice);
    			case "WeaponEnhancementMaterial":
    			case "UnitEnhancementMaterial":
    			case "SkillEnhancementMaterial":
    				return loadEnhancementMaterial(_con, classificationName, _id, name, iconAsBytes, rarity, sellingPrice);
    			case "GachaCostItem":
    				return new GachaCostItem(_id, name, iconAsBytes, rarity, sellingPrice);
    			case "EquipmentTradingItem":
    				return new EquipmentTradingItem(_id, name, iconAsBytes, rarity, sellingPrice);
    			case "UnitTradingItem":
    				return new UnitTradingItem(_id, name, iconAsBytes, rarity, sellingPrice);
    			default:
    				return null;						
    		}	
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    		return null;
    	}
    }
    
    private static SkillItem loadSkillItem(Connection _con, int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, int _sellingPrice) 
    {
    	try 
		{
			String query = "select * from SkillItems where BaseItemId=?"; //Load detailed data of the item
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int skillId = resultSet.getInt("SkillId");
			int skillLevel = resultSet.getInt("SkillLevel");
			
			SkillData skillData = Linq.first(skills, x -> x.Id == skillId);
			ActiveSkill skill = null;
			if (skillData instanceof OrdinarySkillData)
				skill = new OrdinarySkill((OrdinarySkillData)skillData, skillLevel);
			else if (skillData instanceof CounterSkillData)
				skill = new CounterSkill((CounterSkillData)skillData, skillLevel);
			else /*if (skillData instanceof UltimateSkillData)*/
				skill = new UltimateSkill((UltimateSkillData)skillData, skillLevel);
			
			return new SkillItem(_id, _name, _iconAsBytes, _rarity, _sellingPrice, skill);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
	}
    
    private static EnhancementMaterial loadEnhancementMaterial(Connection _con, String _classificationName, int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, int _sellingPrice)
    {
    	try 
		{
			String query = "select * from EnhancementMaterials where BaseItemId=?"; //Get detailed data of the material
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int enhancementValue = resultSet.getInt("EnhancementValue");
			
			switch (_classificationName)
			{
				case "WeaponEnhancementMaterial":
					return loadWeaponEnhancementMaterial(_con, _id, _name, _iconAsBytes, _rarity, _sellingPrice, enhancementValue);
				case "UnitEnhancementMaterial":
					return loadUnitEnhancementMaterial(_con, _id, _name, _iconAsBytes, _rarity, _sellingPrice, enhancementValue);
				case "SkillEnhancementMaterial":
					return loadSkillEnhancementMaterial(_con, _id, _name, _iconAsBytes, _rarity, _sellingPrice, enhancementValue);
				default:
					return null;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		} 
    }
    
    private static WeaponEnhancementMaterial loadWeaponEnhancementMaterial(Connection _con, int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, int _sellingPrice, int _enhancementValue)
    {
    	try 
		{
			List<eWeaponClassification> targetingWeaponClassifications = new ArrayList<eWeaponClassification>();
			addWeaponEnhancementMaterialWeaponClassifications(_con, _id, targetingWeaponClassifications);
			
			return new WeaponEnhancementMaterial(_id, _name, _iconAsBytes, _rarity, _sellingPrice, _enhancementValue, targetingWeaponClassifications);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		} 
    }
    
    private static UnitEnhancementMaterial loadUnitEnhancementMaterial(Connection _con, int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, int _sellingPrice, int _enhancementValue)
    {
    	try 
		{
    		List<eElement> bonusElements = new ArrayList<eElement>();
			addUnitEnhancementMaterialBonusElements(_con, _id, bonusElements);
			
			return new UnitEnhancementMaterial(_id, _name, _iconAsBytes, _rarity, _sellingPrice, _enhancementValue, bonusElements);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		} 
    }
    
    private static SkillEnhancementMaterial loadSkillEnhancementMaterial(Connection _con, int _id, String _name, byte[] _iconAsBytes, eRarity _rarity, int _sellingPrice, int _enhancementValue)
    {
    	try 
		{
			List<eRarity> targetingRarities = new ArrayList<eRarity>();
			addSkillEnhancementMaterialTargetingRarities(_con, _id, targetingRarities);
			List<eElement> targetingElements = new ArrayList<eElement>();
			addSkillEnhancementMaterialTargetingElements(_con, _id, targetingElements);
			List<String> targetingLabels = new ArrayList<String>();
			addSkillEnhancementMaterialTargetingLabels(_con, _id, targetingLabels);
			
			return new SkillEnhancementMaterial(_id, _name, _iconAsBytes, _rarity, _sellingPrice, _enhancementValue, targetingRarities, targetingElements, targetingLabels);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		} 
    }
    
    private static void addSkillEnhancementMaterialTargetingRarities(Connection _con, int _id, List<eRarity> _targetingRarities)
    {
    	addRarities(_con, _id, _targetingRarities, "select RarityId from SkillEnhancementMaterial_TargetingRarity where MaterialId=?");
    }
    private static void addRarities(Connection _con, int _id, List<eRarity> _rarities, String _query)
    {
		try 
		{
			PreparedStatement statement = _con.prepareStatement(_query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int rarityId = resultSet.getInt("RarityId");
				_rarities.add(loadRarity(_con, rarityId));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static void addSkillEnhancementMaterialTargetingElements(Connection _con, int _id, List<eElement> _targetingElements)
    {
    	addElements(_con, _id, _targetingElements, "select ElementId from SkillEnhancementMaterial_TargetingElement where MaterialId=?");
    }
    private static void addElements(Connection _con, int _id, List<eElement> _elements, String _query)
    {
		try 
		{
			PreparedStatement statement = _con.prepareStatement(_query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int elementId = resultSet.getInt("ElementId");
				_elements.add(loadElement(_con, elementId));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static void loadAccessories(Connection _con)
    {
    	accessories.clear();
    	
    	try
    	{  		
			String query = "select * from BaseAccessories"; //Load the entire BaseAccessories table
			PreparedStatement statement = _con.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int id = resultSet.getInt("Id");
				String name = resultSet.getString("Name");
				int rarityId = resultSet.getInt("RarityId");
				int classificationId = resultSet.getInt("ClassificationId");
				int targetGenderId = resultSet.getInt("TargetGenderId");
				String iconPath = resultSet.getString("IconPath");
				
				BufferedImage icon = ImageIO.read(new File(iconPath));
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				ImageIO.write(icon, "png", outputStream);
		    	byte[] iconAsBytes = outputStream.toByteArray();
				eRarity rarity = loadRarity(_con, rarityId);
				List<StatusEffectData> statusEffectsData = new ArrayList<StatusEffectData>();
				addAccessoryStatusEffectsData(_con, id, statusEffectsData);
				eAccessoryClassification classification = loadAccessoryClassification(_con, classificationId);
				eGender targetGender = loadGender(_con, targetGenderId);
				
				accessories.add(new AccessoryData(id, name, iconAsBytes, rarity, statusEffectsData, classification, targetGender));
			}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }
    
    private static void loadArmours(Connection _con)
    {
    	armours.clear();
    	
    	try
    	{  		
			String query = "select * from BaseArmours"; //Load the entire BaseArmours table
			PreparedStatement statement = _con.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int id = resultSet.getInt("Id");
				String name = resultSet.getString("Name");
				int rarityId = resultSet.getInt("RarityId");
				int classificationId = resultSet.getInt("ClassificationId");
				int targetGenderId = resultSet.getInt("TargetGenderId");
				String iconPath = resultSet.getString("IconPath");
				
				BufferedImage icon = ImageIO.read(new File(iconPath));
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				ImageIO.write(icon, "png", outputStream);
		    	byte[] iconAsBytes = outputStream.toByteArray();
				eRarity rarity = loadRarity(_con, rarityId);
				List<StatusEffectData> statusEffectsData = new ArrayList<StatusEffectData>();
				addArmourStatusEffectsData(_con, id, statusEffectsData);
				eArmourClassification classification = loadArmourClassification(_con, classificationId);
				eGender targetGender = loadGender(_con, targetGenderId);
				
				armours.add(new ArmourData(id, name, iconAsBytes, rarity, statusEffectsData, classification, targetGender));
			}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }
    
    private static void loadWeapons(Connection _con)
    {
    	weapons.clear();
    	
    	try
    	{
			String query = "select * from BaseWeapons"; //Load the entire BaseWeapons table
			PreparedStatement statement = _con.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();	
			while (resultSet.next())
			{
				int id = resultSet.getInt("Id");
				String name = resultSet.getString("Name");
				int rarityId = resultSet.getInt("RarityId");
				int typeId = resultSet.getInt("TypeId");
				String iconPath = resultSet.getString("IconPath");
				int mainWeaponSkillId = resultSet.getInt("MainWeaponSkillId");
				int mainWeaponSkillLevel = resultSet.getInt("MainWeaponSkillLevel");
				
				BufferedImage icon = ImageIO.read(new File(iconPath));
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				ImageIO.write(icon, "png", outputStream);
		    	byte[] iconAsBytes = outputStream.toByteArray();
				eRarity rarity = loadRarity(_con, rarityId);
				List<StatusEffectData> statusEffectsData = new ArrayList<StatusEffectData>();
				addWeaponStatusEffectsData(_con, id, statusEffectsData);
				eWeaponType type = loadWeaponType(_con, typeId);
				List<eWeaponClassification> classifications = new ArrayList<eWeaponClassification>();
				addWeaponWeaponClassifications(_con, id, classifications);
				
				SkillData mainWeaponSkillData = Linq.firstOrDefault(skills, x -> x.Id == mainWeaponSkillId);
				Skill mainWeaponSkill = null;
				if (mainWeaponSkillData != null)
				{
					if (mainWeaponSkillData instanceof OrdinarySkillData)
						mainWeaponSkill = new OrdinarySkill((OrdinarySkillData)mainWeaponSkillData, mainWeaponSkillLevel);
					else if (mainWeaponSkillData instanceof CounterSkillData)
						mainWeaponSkill = new CounterSkill((CounterSkillData)mainWeaponSkillData, mainWeaponSkillLevel);
					else if (mainWeaponSkillData instanceof CounterSkillData)
						mainWeaponSkill = new CounterSkill((CounterSkillData)mainWeaponSkillData, mainWeaponSkillLevel);
					else if (mainWeaponSkillData instanceof CounterSkillData)
						mainWeaponSkill = new CounterSkill((CounterSkillData)mainWeaponSkillData, mainWeaponSkillLevel);
				}
				
				weapons.add(new WeaponData(id, name, iconAsBytes, rarity, statusEffectsData, type, classifications, mainWeaponSkill, new ArrayList<WeaponData>()));
			}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }
    
    private static void loadTransformableWeaponsData(Connection _con)
    {
		try 
		{
			for (WeaponData weaponData : Linq.where(weapons, x -> x.WeaponType == eWeaponType.Transformable))
			{
				String query = "select TargetId from TransformableWeapon_TargetWeapon where WeaponId=?"; //Load the Ids for the target weapons
				PreparedStatement statement = _con.prepareStatement(query);
				statement.setInt(1, weaponData.Id);
				
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next())
				{
					int targetId = resultSet.getInt("TargetId");
					weaponData.getTransformableWeapons().add(Linq.first(weapons, x -> x.Id == targetId));
				}
				
				weaponData.DisableModification();
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static void addWeaponStatusEffectsData(Connection _con, int _id, List<StatusEffectData> _statusEffectsData)
    {
    	addEquipmentStatusEffectsData(_con, _id, _statusEffectsData, "select StatusEffectId from Weapon_StatusEffect where WeaponId=?");
    }
    private static void addArmourStatusEffectsData(Connection _con, int _id, List<StatusEffectData> _statusEffectsData)
    {
    	addEquipmentStatusEffectsData(_con, _id, _statusEffectsData, "select StatusEffectId from Armour_StatusEffect where ArmourId=?");
    }
    private static void addAccessoryStatusEffectsData(Connection _con, int _id, List<StatusEffectData> _statusEffectsData)
    {
    	addEquipmentStatusEffectsData(_con, _id, _statusEffectsData, "select StatusEffectId from Accessory_StatusEffect where AccessoryId=?");
    }
    private static void addEquipmentStatusEffectsData(Connection _con, int _id, List<StatusEffectData> _statusEffectsData, String _query)
    {
		try 
		{
			PreparedStatement statement = _con.prepareStatement(_query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int statusEffectId = resultSet.getInt("StatusEffectId");
				_statusEffectsData.add(Linq.first(statusEffects, x -> x.Id == statusEffectId));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static void addWeaponEnhancementMaterialWeaponClassifications(Connection _con, int _id, List<eWeaponClassification> _weaponClassifications)
    {
    	addWeaponClassifications(_con, _id, _weaponClassifications, "select WeaponClassificationId from WeaponEnhancementMaterial_TargetingWeaponClassification where MaterialId=?");
    }
    private static void addWeaponWeaponClassifications(Connection _con, int _id, List<eWeaponClassification> _weaponClassifications)
    {
    	addWeaponClassifications(_con, _id, _weaponClassifications, "select ClassificationId from Weapon_Classification where WeaponId=?");
    }
    private static void addWeaponClassifications(Connection _con, int _id, List<eWeaponClassification> _weaponClassifications, String _query)
    {
		try 
		{
			PreparedStatement statement = _con.prepareStatement(_query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int classificationId = resultSet.getInt("ClassificationId");
				_weaponClassifications.add(loadWeaponClassification(_con, classificationId));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static void addUnitEnhancementMaterialBonusElements(Connection _con, int _id, List<eElement> _bonusElements)
    {
		try 
		{
			String query = "select ElementId from UnitEnhancementMaterial_BonusElement where MaterialId=?";
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int bonusElementId = resultSet.getInt("ElementId");
				_bonusElements.add(loadElement(_con, bonusElementId));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static void loadLevelableTransformableWeaponsData(Connection _con)
    {
		try 
		{
			for (WeaponData weaponData : Linq.where(weapons, x -> x.WeaponType == eWeaponType.LevelableTransformable))
			{
				String query = "select TargetId from LevelableTransformableWeapon_TargetWeapon where WeaponId=?"; //Load the Ids for the target weapons
				PreparedStatement statement = _con.prepareStatement(query);
				statement.setInt(1, weaponData.Id);
				
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next())
				{
					int targetId = resultSet.getInt("TargetId");
					weaponData.getTransformableWeapons().add(Linq.first(weapons, x -> x.Id == targetId));
				}
				
				weaponData.DisableModification();
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }

    private static void loadPlayers(Connection _con)
    {
    	players.clear();
    	
    	try 
    	{	
			String query = "select * from Players"; //Load the entire Players table;
			PreparedStatement statement = _con.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				players.add(loadPlayer(_con, resultSet));
			}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }
    
    private static Player loadPlayer(Connection _con, ResultSet _resultSet)
    {
    	try 
    	{	
        	int id = _resultSet.getInt("Id");
    		String playerName = _resultSet.getString("PlayerName");
    		int gemsOwned = _resultSet.getInt("GemsOwned");
    		int goldOwned = _resultSet.getInt("GoldOwned");
    		
    		List<Weapon> weaponsOwned = new ArrayList<Weapon>();
    		addWeaponsOwned(_con, id, weaponsOwned);
    		List<Armour> armoursOwned = new ArrayList<Armour>();
    		addArmoursOwned(_con, id, armoursOwned);
    		List<Accessory> accessoriesOwned = new ArrayList<Accessory>();
    		addAccessoriesOwned(_con, id, accessoriesOwned);
    		List<Unit> unitsOwned = new ArrayList<Unit>();
    		addUnitsOwned(_con, id, unitsOwned, weaponsOwned, armoursOwned, accessoriesOwned);
    		Map<Item, Integer> itemsOwned = new HashMap<Item, Integer>();
    		addItemsOwned(_con, id, itemsOwned);
    		List<ItemSet> itemSets = new ArrayList<ItemSet>();
    		addItemSets(_con, id, itemSets);
    		List<Team> teams = new ArrayList<Team>();
    		addTeams(_con, id, unitsOwned, itemSets, teams);
    		
    		return new Player(id, playerName, unitsOwned, weaponsOwned, armoursOwned, accessoriesOwned, itemsOwned, itemSets, teams, gemsOwned, goldOwned);
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    		return null;
    	}
    }
  
    private static void addUnitsOwned(Connection _con, int _id, List<Unit> _unitsOwned, List<Weapon> _weaponsOwned, List<Armour> _armoursOwned, List<Accessory> _accessoriesOwned)
    {
    	try 
		{
			String query = "select * from PlayableUnits where OwnerPlayerId=?";
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int uniqueId = resultSet.getInt("Id");
				int baseUnitId = resultSet.getInt("BaseUnitId");
				int accumulatedExperience = resultSet.getInt("AccumulatedExperience");
				String nickname = resultSet.getString("Nickname");
				boolean isLocked = resultSet.getBoolean("IsLocked");
				int mainWeaponId = resultSet.getInt("MainWeaponId");
				int subWeaponId = resultSet.getInt("SubWeaponId");
				int armourId = resultSet.getInt("ArmourId");
				int accessoryId = resultSet.getInt("AccessoryId");
				
				UnitData baseUnit = Linq.first(units, x -> x.Id == baseUnitId);				
				Map<Integer, Integer> levelPerSkill = new HashMap<Integer, Integer>();
				addPlayableUnitLevelPerSkill(_con, _id, levelPerSkill);
				Weapon mainWeapon = Linq.firstOrDefault(_weaponsOwned, x -> x.UniqueId == mainWeaponId);
				Weapon subWeapon = Linq.firstOrDefault(_weaponsOwned, x -> x.UniqueId == subWeaponId);
				Armour armour = Linq.firstOrDefault(_armoursOwned, x -> x.UniqueId == armourId);
				Accessory accessory = Linq.firstOrDefault(_accessoriesOwned, x -> x.UniqueId == accessoryId);
				
				_unitsOwned.add(new Unit(baseUnit, uniqueId, nickname, accumulatedExperience, isLocked, levelPerSkill, mainWeapon, subWeapon, armour, accessory));
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
    }
    
    private static void addPlayableUnitLevelPerSkill(Connection _con, int _id, Map<Integer, Integer> _levelPerSkill)
    {
		try 
		{
			String query = "select SkillId, SkillLevel from PlayableUnit_SkillLevel where UnitId=?";
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int skillId = resultSet.getInt("SkillId");
				int skillLevel = resultSet.getInt("SkillLevel");
				
				_levelPerSkill.put(skillId, skillLevel);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static void loadSkillInheritors(Connection _con)
    {
		try 
		{
			for (Player player : players)
			{
				for (Unit unit : player.UnitsOwned)
				{
					String query = "select SkillInheritorUnitId, InheritingSkillId from PlayableUnits where Id=?";
					PreparedStatement statement = _con.prepareStatement(query);
					statement.setInt(1, unit.UniqueId);
					
					ResultSet resultSet = statement.executeQuery();
					while (resultSet.next())
					{
						int skillInheritorUnitId = resultSet.getInt("SkillInheritorUnitId");
						int inheritingSkillId = resultSet.getInt("InheritingSkillId");
						
						unit.SkillInheritor = Linq.firstOrDefault(player.UnitsOwned, x -> x.UniqueId == skillInheritorUnitId);
						unit.InheritingSkillId = inheritingSkillId;
					}
				}
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static void addItemsOwned(Connection _con, int _id, Map<Item, Integer> _itemsOwned)
    {
    	try 
		{
			String query = "select * from Player_ItemOwned where PlayerId=? AND Quantity > 1";
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int itemId = resultSet.getInt("ItemId");
				int quantity = resultSet.getInt("Quantity");
				
				Item item = Linq.first(items, x -> x.Id == itemId);
				_itemsOwned.put(item, quantity);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
    }
    
    private static void addWeaponsOwned(Connection _con, int _id, List<Weapon> _weaponsOwned)
    {
    	try 
		{
			String query = "select * from UsableWeapons where OwnerPlayerId=?";
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int uniqueId = resultSet.getInt("Id");
				int baseWeaponId = resultSet.getInt("BaseWeaponId");
				boolean isLocked = resultSet.getBoolean("IsLocked");
				
				WeaponData baseWeapon = Linq.first(weapons, x -> x.Id == baseWeaponId);
				
				int accumulatedExperience = 0;
				if (baseWeapon.WeaponType == eWeaponType.Levelable || baseWeapon.WeaponType == eWeaponType.LevelableTransformable)
				{
					query = "select AccumulatedExperience from UsableLevelableWeapons where BaseUsableWeaponId=?";
					statement = _con.prepareStatement(query);
					statement.setInt(1, uniqueId);
					
					ResultSet resultSet2 = statement.executeQuery();
					if (!resultSet2.next())
						return;
					
					accumulatedExperience = resultSet2.getInt("AccumulatedExperience");
				}
				
				switch (baseWeapon.WeaponType)
				{
					default: //case Ordinary:
						_weaponsOwned.add(new OrdinaryWeapon(baseWeapon, uniqueId, isLocked));
						break;
						
					case Levelable:
						_weaponsOwned.add(new LevelableWeapon(baseWeapon, uniqueId, isLocked, accumulatedExperience));
						break;
						
					case LevelableTransformable:
						_weaponsOwned.add(new LevelableTransformableWeapon(baseWeapon, uniqueId, isLocked, accumulatedExperience));
						break;
						
					case Transformable:
						_weaponsOwned.add(new TransformableWeapon(baseWeapon, uniqueId, isLocked));
						break;
				}
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
    }
    
    private static void addArmoursOwned(Connection _con, int _id, List<Armour> _armoursOwned)
    {
    	try 
		{
			String query = "select * from UsableArmours where OwnerPlayerId=?";
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int uniqueId = resultSet.getInt("Id");
				int baseArmourId = resultSet.getInt("BaseArmourId");
				boolean isLocked = resultSet.getBoolean("IsLocked");
				
				ArmourData baseArmour = Linq.first(armours, x -> x.Id == baseArmourId);
				
				_armoursOwned.add(new Armour(baseArmour, uniqueId, isLocked));
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
    }
    
    private static void addAccessoriesOwned(Connection _con, int _id, List<Accessory> _accessoriesOwned)
    {
    	try 
		{
			String query = "select * from UsableAccessories where OwnerPlayerId=?";
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int uniqueId = resultSet.getInt("Id");
				int baseAccessoryId = resultSet.getInt("BaseAccessoryId");
				boolean isLocked = resultSet.getBoolean("IsLocked");
				
				AccessoryData baseAccessory = Linq.first(accessories, x -> x.Id == baseAccessoryId);
				
				_accessoriesOwned.add(new Accessory(baseAccessory, uniqueId, isLocked));
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
    }
    
    private static void addItemSets(Connection _con, int _id, List<ItemSet> _itemSets)
    {
    	try 
		{
			String query = "select Id from BattleItemSets where OwnerId=?";
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int itemSetId = resultSet.getInt("Id");
				
				Map<BattleItem, Integer> quantityPerItem = new HashMap<BattleItem, Integer>();
				
				query = "select * from BattleItemSet_Item where ItemSetId=?";
				statement = _con.prepareStatement(query);
				statement.setInt(1, itemSetId);
				
				ResultSet resultSet2 = statement.executeQuery();
				while (resultSet2.next())
				{
					int itemId = resultSet2.getInt("ItemId");
					int quantity = resultSet2.getInt("Quantity");
					
					Item item = Linq.first(items, x -> x.Id == itemId);
					
					quantityPerItem.put((BattleItem)item, quantity);
				}
				
				_itemSets.add(new ItemSet(itemSetId, quantityPerItem));
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
    }
    
    private static void addTeams(Connection _con, int _id, List<Unit> _unitsOwned, List<ItemSet> _itemSets, List<Team> _teams)
    {
    	try 
		{
			String query = "select * from Teams where OwnerId=?";
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int member1Id = resultSet.getInt("Member1Id");
				int member2Id = resultSet.getInt("Member2Id");
				int member3Id = resultSet.getInt("Member3Id");
				int member4Id = resultSet.getInt("Member4Id");
				int member5Id = resultSet.getInt("Member5Id");
				int itemSetId = resultSet.getInt("ItemSetId");
				
				List<Unit> members = new ArrayList<Unit>();
				Unit member1 = Linq.first(_unitsOwned, x -> x.UniqueId == member1Id);
				Unit member2 = Linq.firstOrDefault(_unitsOwned, x -> x.UniqueId == member2Id);
				Unit member3 = Linq.firstOrDefault(_unitsOwned, x -> x.UniqueId == member3Id);
				Unit member4 = Linq.firstOrDefault(_unitsOwned, x -> x.UniqueId == member4Id);
				Unit member5 = Linq.firstOrDefault(_unitsOwned, x -> x.UniqueId == member5Id);
				members.add(member1);
				members.add(member2);
				members.add(member3);
				members.add(member4);
				members.add(member5);
				ItemSet itemSet = Linq.firstOrDefault(_itemSets, x -> x.Id == itemSetId);
				
				_teams.add(new Team(members, itemSet));
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
    }
    
    private static void loadUnits(Connection _con)
    {
    	units.clear();
    	
    	try 
    	{	
			String query = "select * from BaseUnits"; //Load the entire BaseUnits table;
			PreparedStatement statement = _con.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int id = resultSet.getInt("Id");
				String name = resultSet.getString("Name");
				int rarityId = resultSet.getInt("RarityId");
				int genderId = resultSet.getInt("GenderId");
				int movementRangeClassificationId = resultSet.getInt("MovementRangeClassificationId");
				int nonMovementActionRangeClassificationId = resultSet.getInt("NonMovementActionRangeClassificationId");
				int elementId1 = resultSet.getInt("ElementId1");
				int elementId2 = resultSet.getInt("ElementId2");
				int maxHPAtMaxLevel = resultSet.getInt("MaxHPAtMaxLevel");
				int physicalStrengthAtMaxLevel = resultSet.getInt("PhysicalStrengthAtMaxLevel");
				int physicalResistanceAtMaxLevel = resultSet.getInt("PhysicalResistanceAtMaxLevel");
				int magicalStrengthAtMaxLevel = resultSet.getInt("MagicalStrengthAtMaxLevel");
				int magicalResistanceAtMaxLevel = resultSet.getInt("MagicalResistanceAtMaxLevel");
				int vitalityAtMaxLevel = resultSet.getInt("VitalityAtMaxLevel");
				String description = resultSet.getString("Description");
				String iconPath = resultSet.getString("IconPath");
				
				BufferedImage icon = ImageIO.read(new File(iconPath));
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				ImageIO.write(icon, "png", outputStream);
		    	byte[] iconAsBytes = outputStream.toByteArray();
				eGender gender = loadGender(_con, genderId);
				eRarity rarity = loadRarity(_con, rarityId);
				eTargetRangeClassification movementRangeClassification = loadTargetRangeClassification(_con, movementRangeClassificationId);
				eTargetRangeClassification nonMovementActionRangeClassification = loadTargetRangeClassification(_con, nonMovementActionRangeClassificationId);
				List<eElement> elements = new ArrayList<eElement>();
				elements.add(loadElement(_con, elementId1));
				elements.add(loadElement(_con, elementId2));
				List<eWeaponClassification> equipableWeaponClassifications = new ArrayList<eWeaponClassification>();
				addUnitEquipableWeaponClassifications(_con, id, equipableWeaponClassifications);
				List<eArmourClassification> equipableArmourClassifications = new ArrayList<eArmourClassification>();
				addUnitEquipableArmourClassifications(_con, id, equipableArmourClassifications);
				List<eAccessoryClassification> equipableAccessoryClassifications = new ArrayList<eAccessoryClassification>();
				addUnitEquipableAccessoryClassifications(_con, id, equipableAccessoryClassifications);
				List<SkillData> skills = new ArrayList<SkillData>();
				addUnitSkillsData(_con, id, skills);
				List<String> labels = new ArrayList<String>();
				addUnitLabels(_con, id, labels);
				List<UnitEvolutionRecipe> progressiveEvolutionRecipes = new ArrayList<UnitEvolutionRecipe>(); //Recipes will be added afterwards
				UnitEvolutionRecipe retrogressiveEvolutionRecipe = null; //Recipe will be added afterwards
				
				units.add(new UnitData(id, name, iconAsBytes, gender, rarity, movementRangeClassification, nonMovementActionRangeClassification, elements, equipableWeaponClassifications, equipableArmourClassifications, equipableAccessoryClassifications, maxHPAtMaxLevel, physicalStrengthAtMaxLevel, physicalResistanceAtMaxLevel, magicalStrengthAtMaxLevel, magicalResistanceAtMaxLevel, vitalityAtMaxLevel, skills, labels, description, progressiveEvolutionRecipes, retrogressiveEvolutionRecipe));
			}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }
    
    private static void addUnitEquipableWeaponClassifications(Connection _con, int _id, List<eWeaponClassification> _equipableWeaponClassifications)
    {
		try 
		{
			String query = "select WeaponClassificationId from Unit_EquipableWeaponClassification where UnitId=?"; //Load the Ids for the equipable weapon classifications
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int weaponClassificationId = resultSet.getInt("WeaponClassificationId");
				_equipableWeaponClassifications.add(loadWeaponClassification(_con, weaponClassificationId));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static void addUnitEquipableArmourClassifications(Connection _con, int _id, List<eArmourClassification> _equipableArmourClassifications)
    {
		try 
		{
			String query = "select ArmourClassificationId from Unit_EquipableArmourClassification where UnitId=?"; //Load the Ids for the equipable armour classifications
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int armourClassificationId = resultSet.getInt("ArmourClassificationId");
				_equipableArmourClassifications.add(loadArmourClassification(_con, armourClassificationId));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static void addUnitEquipableAccessoryClassifications(Connection _con, int _id, List<eAccessoryClassification> _equipableAccessoryClassifications)
    {
		try 
		{
			String query = "select AccessoryClassificationId from Unit_EquipableAccessoryClassification where UnitId=?"; //Load the Ids for the equipable accessory classifications
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int accessoryClassificationId = resultSet.getInt("AccessoryClassificationId");
				_equipableAccessoryClassifications.add(loadAccessoryClassification(_con, accessoryClassificationId));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static void addUnitSkillsData(Connection _con, int _id, List<SkillData> _skills)
    {
		try 
		{
			String query = "select SkillId from Unit_Skill where UnitId=?"; //Load the Ids for the skills
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int skillId = resultSet.getInt("SkillId");
				_skills.add(Linq.first(skills, x -> x.Id == skillId));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static void addSkillEnhancementMaterialTargetingLabels(Connection _con, int _id, List<String> _targetingLabels)
    {
    	addLabels(_con, _id, _targetingLabels, "select LabelId from SkillEnhancementMaterial_TargetingLabel where MaterialId=?");
    }
    private static void addUnitLabels(Connection _con, int _id, List<String> _labels)
    {
    	addLabels(_con, _id, _labels, "select LabelId from Unit_Label where UnitId=?");
    }
    private static void addLabels(Connection _con, int _id, List<String> _labels, String _query)
    {
		try 
		{
			PreparedStatement statement = _con.prepareStatement(_query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int labelId = resultSet.getInt("LabelId");
				_labels.add(loadLabel(_con, labelId));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static String loadLabel(Connection _con, int _id)
    {
		try 
		{
			String query = "select String from Labels where Id=?";
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			return resultSet.getString("String");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static void loadUnitEvolutionRecipes(Connection _con)
    {
		try 
		{
			for (UnitData unitData : units)
			{
				String query = "select RecipeId from Unit_EvolutionRecipe where UnitId=?"; //Load the Ids for the evolution recipes
				PreparedStatement statement = _con.prepareStatement(query);
				statement.setInt(1, unitData.Id);
				
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next())
				{
					int recipeId = resultSet.getInt("RecipeId");
					unitData.getProgressiveEvolutionRecipes().add(loadUnitEvolutionRecipe(_con, recipeId));
				}
				
				query = "select RetrogressiveEvolutionRecipeId from BaseUnits where Id=?";
				statement = _con.prepareStatement(query);
				statement.setInt(1, unitData.Id);
				
				ResultSet resultSet2 = statement.executeQuery();
				if (resultSet2.next())
				{
					int retrogressiveEvolutionRecipeId = resultSet2.getInt("RetrogressiveEvolutionRecipeId");
					unitData.setRetrogressiveEvolutionRecipe(loadUnitEvolutionRecipe(_con, retrogressiveEvolutionRecipeId));
				}

				unitData.DisableModification();
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static UnitEvolutionRecipe loadUnitEvolutionRecipe(Connection _con, int _id)
    {
    	try
    	{
    		String query = "select * from UnitEvolutionRecipes where Id=?";
    		PreparedStatement statement = _con.prepareStatement(query);
    		statement.setInt(1, _id);
        	
    		ResultSet resultSet = statement.executeQuery();
    		if (!resultSet.next())
    			return null;
    		
        	int afterEvolutionUnitId = resultSet.getInt("AfterEvolutionUnitId");
        	int material1Id = resultSet.getInt("Material1Id");
        	int material2Id = resultSet.getInt("Material2Id");
        	int material3Id = resultSet.getInt("Material3Id");
        	int material4Id = resultSet.getInt("Material4Id");
        	int material5Id = resultSet.getInt("Material5Id");
        	int cost = resultSet.getInt("Cost");
    		
    		UnitData unitAfterEvolution = Linq.first(units, x -> x.Id == afterEvolutionUnitId);
    		EvolutionMaterial material1 = Linq.first(Linq.ofType(items, EvolutionMaterial.class), x -> x.Id == material1Id);
    		EvolutionMaterial material2 = Linq.first(Linq.ofType(items, EvolutionMaterial.class), x -> x.Id == material2Id);
    		EvolutionMaterial material3 = Linq.first(Linq.ofType(items, EvolutionMaterial.class), x -> x.Id == material3Id);
    		EvolutionMaterial material4 = Linq.first(Linq.ofType(items, EvolutionMaterial.class), x -> x.Id == material4Id);
    		EvolutionMaterial material5 = Linq.first(Linq.ofType(items, EvolutionMaterial.class), x -> x.Id == material5Id);
    		List<EvolutionMaterial> materials = new ArrayList<EvolutionMaterial>();
    		materials.add(material1);
    		materials.add(material2);
    		materials.add(material3);
    		materials.add(material4);
    		materials.add(material5);
    		
    		return new UnitEvolutionRecipe(unitAfterEvolution, materials, cost);
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    		return null;
    	}
    }
    
    private static void loadSkills(Connection _con)
    {
    	skills.clear();
    	
    	Effect basicEffect = Linq.first(effects, x -> x.Id == -1);
    	skills.add(new OrdinarySkillData(-1, "Basic Attack", null, null, -1, Tag.one, basicEffect, 0, null));
    	
    	try 
    	{	    		
			String query = "select Id from BaseSkills"; //Load the entire BaseSkills.Id column
			PreparedStatement statement = _con.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int id = resultSet.getInt("Id");
				skills.add(loadSkillData(_con, id));
			}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }
    
    private static SkillData loadSkillData(Connection _con, int _id)
    {
    	try
    	{
    		String query = "select * from BaseSkills where Id=?";
    		PreparedStatement statement = _con.prepareStatement(query);
        	statement.setInt(1, _id);
    		
    		ResultSet resultSet = statement.executeQuery();
    		if (!resultSet.next())
    			return null;
    		
        	int classificationId = resultSet.getInt("ClassificationId");
    		String name = resultSet.getString("Name");
    		int iconId = resultSet.getInt("IconId");
    		int skillActivationAnimationId = resultSet.getInt("SkillActivationAnimationTypeId");
    		
    		byte[] iconAsBytes = loadSkillIconAsBytes(_con, iconId);
    		
    		query = "select Name from SkillClassifications where Id=?"; //Get classification name for the skill
    		statement = _con.prepareStatement(query);
    		statement.setInt(1, classificationId);
    		
    		ResultSet resultSet2 = statement.executeQuery();
    		if (!resultSet2.next())
    			return null;
    		
    		String classificationName = resultSet2.getString("Name");
    		switch (classificationName)
    		{
    			case "Ordinary":
    				return loadOrdinarySkillData(_con, _id, name, iconAsBytes, skillActivationAnimationId);
    			case "Counter":
    				return loadCounterSkillData(_con, _id, name, iconAsBytes, skillActivationAnimationId);
    			case "Ultimate":
    				return loadUltimateSkillData(_con, _id, name, iconAsBytes, skillActivationAnimationId);
    			case "Passive":
    				return loadPassiveSkillData(_con, _id, name, iconAsBytes, skillActivationAnimationId);
    			default:
    				return null;						
    		}	
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    		return null;
    	}
    }
    
	private static OrdinarySkillData loadOrdinarySkillData(Connection _con, int _id, String _name, byte[] _iconAsBytes, int _skillActivationAnimationId)
    {
		try 
		{
			String query = "select * from ActiveSkills where BaseSkillId=?"; //Load detailed data of the skill
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int maxNumberOfTargetsTagId = resultSet.getInt("MaxNumberOfTargetsTagId");
			int effectId = resultSet.getInt("EffectId");
			
			List<BackgroundStatusEffectData> temporalStatusEffectsData = new ArrayList<BackgroundStatusEffectData>();
			addActiveSkillBackgroundStatusEffectsData(_con, _id, temporalStatusEffectsData);
			Tag maxNumberOfTargets = loadTag(_con, maxNumberOfTargetsTagId);
			Effect effect = Linq.first(effects, x -> x.Id == effectId);
			
			query = "select * from CostRequiringSkills where BaseSkillId=?"; //Load detailed data of the skill
			statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet2 = statement.executeQuery();
			if (!resultSet2.next())
				return null;
			
			int spCost = resultSet2.getInt("SPCost");
			
			Map<Integer, Integer> itemCosts = new HashMap<Integer, Integer>();
			addSkillItemCosts(_con, _id, itemCosts);
			
			return new OrdinarySkillData(_id, _name, _iconAsBytes, temporalStatusEffectsData, _skillActivationAnimationId, maxNumberOfTargets, effect, spCost, itemCosts);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
	
    private static CounterSkillData loadCounterSkillData(Connection _con, int _id, String _name, byte[] _iconAsBytes, int _animationId) 
    {
    	try 
		{
			String query = "select * from ActiveSkills where BaseSkillId=?"; //Load detailed data of the skill
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int maxNumberOfTargetsTagId = resultSet.getInt("MaxNumberOfTargetsTagId");
			int effectId = resultSet.getInt("EffectId");
			
			List<BackgroundStatusEffectData> temporalStatusEffectsData = new ArrayList<BackgroundStatusEffectData>();
			addActiveSkillBackgroundStatusEffectsData(_con, _id, temporalStatusEffectsData);
			Tag maxNumberOfTargets = loadTag(_con, maxNumberOfTargetsTagId);
			Effect effect = Linq.first(effects, x -> x.Id == effectId);
			
			query = "select * from CostRequiringSkills where BaseSkillId=?"; //Load detailed data of the skill
			statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet2 = statement.executeQuery();
			if (!resultSet2.next())
				return null;
			
			int spCost = resultSet2.getInt("SPCost");
			
			Map<Integer, Integer> itemCosts = new HashMap<Integer, Integer>();
			addSkillItemCosts(_con, _id, itemCosts);
			
			query = "select * from CounterSkills where BaseSkillId=?"; //Load detailed data of the skill
			statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet3 = statement.executeQuery();
			if (!resultSet3.next())
				return null;
			
			int eventTriggerTimingId = resultSet3.getInt("EventTriggerTimingId");
			
			eEventTriggerTiming eventTriggerTiming = loadEventTriggerTiming(_con, eventTriggerTimingId);
			ComplexCondition activationCondition = loadCounterSkillActivationCondition(_con, _id);
			
			return new CounterSkillData(_id, _name, _iconAsBytes, temporalStatusEffectsData, _animationId, maxNumberOfTargets, effect, spCost, itemCosts, eventTriggerTiming, activationCondition);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
	}
    
    private static UltimateSkillData loadUltimateSkillData(Connection _con, int _id, String _name, byte[] _iconAsBytes, int _animationId) 
    {
    	try 
		{
			String query = "select * from ActiveSkills where BaseSkillId=?"; //Load detailed data of the skill
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int maxNumberOfTargetsTagId = resultSet.getInt("MaxNumberOfTargetsTagId");
			int effectId = resultSet.getInt("EffectId");
			
			List<BackgroundStatusEffectData> temporalStatusEffectsData = new ArrayList<BackgroundStatusEffectData>();
			addActiveSkillBackgroundStatusEffectsData(_con, _id, temporalStatusEffectsData);
			Tag maxNumberOfTargets = loadTag(_con, maxNumberOfTargetsTagId);
			Effect effect = Linq.first(effects, x -> x.Id == effectId);
			
			
			return new UltimateSkillData(_id, _name, _iconAsBytes, temporalStatusEffectsData, _animationId, maxNumberOfTargets, effect);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
	}
    
    private static PassiveSkillData loadPassiveSkillData(Connection _con, int _id, String _name, byte[] _iconAsBytes, int _animationId) 
    {
    	try 
		{
			String query = "select * from PassiveSkills where BaseSkillId=?"; //Load detailed data of the skill
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int targetUnitClassificationId = resultSet.getInt("TargetUnitClassificationId");
			
			List<StatusEffectData> temporalStatusEffectsData = new ArrayList<StatusEffectData>();
			addPassiveSkillStatusEffectsData(_con, _id, temporalStatusEffectsData);
			eTargetUnitClassification targetClassification = loadTargetUnitClassification(_con, targetUnitClassificationId);
			ComplexCondition activationCondition = loadPassiveSkillActivationCondition(_con, _id);
			
			return new PassiveSkillData(_id, _name, _iconAsBytes, temporalStatusEffectsData, _animationId, targetClassification, activationCondition);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
	}
    
    private static void addActiveSkillBackgroundStatusEffectsData(Connection _con, int _id, List<BackgroundStatusEffectData> _statusEffectsData)
    {
		try 
		{
			String query = "select TemporalStatusEffectId from ActiveSkill_TemporalStatusEffect where SkillId=?"; //Load the Ids for the temporal status effects
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int temporalStatusEffectId = resultSet.getInt("TemporalStatusEffectId");
				_statusEffectsData.add(Linq.first(Linq.ofType(statusEffects, BackgroundStatusEffectData.class), x -> x.Id == temporalStatusEffectId));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static void addPassiveSkillStatusEffectsData(Connection _con, int _id, List<StatusEffectData> _statusEffectsData)
    {
		try 
		{
			String query = "select TemporalStatusEffectId from PassiveSkill_TemporalStatusEffect where SkillId=?"; //Load the Ids for the temporal status effects
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int temporalStatusEffectId = resultSet.getInt("TemporalStatusEffectId");
				_statusEffectsData.add(Linq.first(statusEffects, x -> x.Id == temporalStatusEffectId));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static void addSkillItemCosts(Connection _con, int _id, Map<Integer, Integer> _itemCosts)
    {
		try 
		{
			String query = "select ItemCostId from CostRequiringSkill_ItemCost where SkillId=?"; //Load the Ids for the item costs
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int itemCostId = resultSet.getInt("ItemCostId");
				MapEntry<Integer, Integer> itemCost = loadItemCost(_con, itemCostId);
				_itemCosts.put(itemCost.getKey(), itemCost.getValue());
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static MapEntry<Integer, Integer> loadItemCost(Connection _con, int _id)
    {
		try 
		{
			String query = "select * from ItemCosts where Id=?";
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int itemId = resultSet.getInt("ItemId");
			int quantity = resultSet.getInt("Quantity");
			
			return new MapEntry<Integer, Integer>(itemId, quantity);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static void loadEffects(Connection _con)
    {
    	effects.clear();
    	
    	effects.add(new DamageEffect(-1, null, Tag.one, Tag.one, Tag.zero, null, null, eTargetUnitClassification.EnemyInRange, eAttackClassification.Physic, Tag.one, false, eElement.None));
    	
    	try 
    	{	
			String query;
			PreparedStatement statement;
			
			ResultSet resultSet;
    		
			query = "select Id from BaseEffects"; //Load the entire BaseEffects.Id column
			statement = _con.prepareStatement(query);
			
			resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int id = resultSet.getInt("Id");
				effects.add(loadEffect(_con, id));
			}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }
    
    private static Effect loadEffect(Connection _con, int _id)
    {
    	try
    	{	
			String query = "select * from BaseEffects where Id=?";
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
    		if (!resultSet.next())
    			return null;
			
    		int classificationId = resultSet.getInt("ClassificationId");
			int timesToApplyTagId = resultSet.getInt("TimesToApplyTagId");
			int successRateTagId = resultSet.getInt("SuccessRateTagId");
			int diffusionDistanceTagId = resultSet.getInt("DiffusionDistanceTagId");
			
			ComplexCondition activationCondition = loadEffectActivationCondition(_con, _id);
			Tag timesToApply = loadTag(_con, timesToApplyTagId);
			Tag successRate = loadTag(_con, successRateTagId);
			Tag diffusionDistance = loadTag(_con, diffusionDistanceTagId);
			List<Effect> secondaryEffects = new ArrayList<Effect>(); //Data will be added afterwards
			
			query = "select Name from EffectClassifications where Id=?"; //Get classification name for the effect
			statement = _con.prepareStatement(query);
			statement.setInt(1, classificationId);
			
			ResultSet resultSet2 = statement.executeQuery();
			if (!resultSet2.next())
				return null;
			
			String classificationName = resultSet2.getString("Name");
			switch (classificationName)
			{
				case "UnitTargetingEffectsWrapper":
				case "Damage":
				case "Drain":
				case "Heal":
				case "StatusEffectAttachment":
					return loadUnitTargetingEffect(_con, classificationName, _id, activationCondition, timesToApply, successRate, diffusionDistance, secondaryEffects);
				//case "":
					//return loadNonMovementTileTargetingEffect();
				case "Movement":
					return loadMovementEffect(_con, _id, activationCondition, timesToApply, secondaryEffects);
				default:
					return null;					
			}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    		return null;
    	}
    }
    
    private static void loadSecondaryEffects(Connection _con)
    {
		try 
		{
			for (Effect effect : effects)
			{
				String query = "select SecondaryEffectId from Effect_SecondaryEffect where EffectId=?"; //Load the Ids for the secondary effects
				PreparedStatement statement = _con.prepareStatement(query);
				statement.setInt(1, effect.Id);
				
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next())
				{
					int secondaryEffectId = resultSet.getInt("SecondaryEffectId");
					effect.getSecondaryEffects().add(Linq.first(effects, x -> x.Id == secondaryEffectId));
				}
				
				effect.DisableModification();
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}   
    }
    
    private static UnitTargetingEffect loadUnitTargetingEffect(Connection _con, String _classificationName, int _id, ComplexCondition _activationCondition, Tag _timesToApply, Tag _successRate, Tag _diffusionDistance, List<Effect> _secondaryEffects)
    {
		try 
		{
			String query = "select * from UnitTargetingEffects where BaseEffectId=?"; //Load detailed data of the effect
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int targetClassificationId = resultSet.getInt("TargetClassificationId");
			int animationId = resultSet.getInt("AnimationId");
			
			eTargetUnitClassification targetClassification = loadTargetUnitClassification(_con, targetClassificationId);
			AnimationInfo animationInfo = loadNonMovementAnimationInfo(_con, animationId);
			
			switch (_classificationName)
			{
				case "UnitTargetingEffectsWrapper":
					return new UnitTargetingEffectsWrapperEffect(_id, _activationCondition, Linq.ofType(_secondaryEffects, UnitTargetingEffect.class), targetClassification);
				case "Damage":
					return loadDamageEffect(_con, _id, _activationCondition, _timesToApply, _successRate, _diffusionDistance, _secondaryEffects, animationInfo, targetClassification);
				case "Drain":
					return loadDrainEffect(_con, _id, _activationCondition, _timesToApply, _successRate, _diffusionDistance, _secondaryEffects, animationInfo, targetClassification);
				case "Heal":
					return loadHealEffect(_con, _id, _activationCondition, _timesToApply, _successRate, _diffusionDistance, _secondaryEffects, animationInfo, targetClassification);
				case "StatusEffectAttachment":
					return loadStatusEffectAttachmentEffect(_con, _id, _activationCondition, _timesToApply, _successRate, _diffusionDistance, _secondaryEffects, animationInfo, targetClassification);
				default:
					return null;					
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static DamageEffect loadDamageEffect(Connection _con, int _id, ComplexCondition _activationCondition, Tag _timesToApply, Tag _successRate, Tag _diffusionDistance, List<Effect> _secondaryEffects, AnimationInfo _animationInfo, eTargetUnitClassification _targetClassification)
    {
		try 
		{
			String query = "select * from DamageEffects where BaseEffectId=?"; //Load detailed data of the effect
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int attackClassificationId = resultSet.getInt("AttackClassificationId");
			int valueTagId = resultSet.getInt("ValueTagId");
			boolean isFixedValue = resultSet.getBoolean("IsFixedValue");
			int elementId = resultSet.getInt("ElementId");
			
			eAttackClassification attackClassification = loadAttackClassification(_con, attackClassificationId);
			Tag value = loadTag(_con, valueTagId);
			eElement element = loadElement(_con, elementId);
			
			return new DamageEffect(_id, _activationCondition, _timesToApply, _successRate, _diffusionDistance, _secondaryEffects, _animationInfo, _targetClassification, attackClassification, value, isFixedValue, element);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static DrainEffect loadDrainEffect(Connection _con, int _id, ComplexCondition _activationCondition, Tag _timesToApply, Tag _successRate, Tag _diffusionDistance, List<Effect> _secondaryEffects, AnimationInfo _animationInfo, eTargetUnitClassification _targetClassification)
    {
		try 
		{
			String query = "select * from DrainEffects where BaseEffectId=?"; //Load detailed data of the effect
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int maxNumberOfSecondaryTargetsTagId = resultSet.getInt("MaxNumberOfSecondaryTargetsTagId");
			int attackClassificationId = resultSet.getInt("AttackClassificationId");
			int valueTagId = resultSet.getInt("ValueTagId");
			boolean isFixedValue = resultSet.getBoolean("IsFixedValue");
			int elementId = resultSet.getInt("ElementId");
			int drainingEfficiencyTagId = resultSet.getInt("DrainingEfficiencyTagId");
			int healAnimationId = resultSet.getInt("HealAnimationId");
			
			Tag maxNumberOfSecondaryTargets = loadTag(_con, maxNumberOfSecondaryTargetsTagId);
			eAttackClassification attackClassification = loadAttackClassification(_con, attackClassificationId);
			Tag value = loadTag(_con, valueTagId);
			eElement element = loadElement(_con, elementId);
			Tag drainingEfficiency = loadTag(_con, drainingEfficiencyTagId);
			SimpleAnimationInfo healAnimationInfo = loadSimpleAnimationInfo(_con, healAnimationId);
			
			return new DrainEffect(_id, _activationCondition, _timesToApply, _successRate, _diffusionDistance, _secondaryEffects, _animationInfo, _targetClassification, maxNumberOfSecondaryTargets, attackClassification, value, isFixedValue, element, drainingEfficiency, healAnimationInfo);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static HealEffect loadHealEffect(Connection _con, int _id, ComplexCondition _activationCondition, Tag _timesToApply, Tag _successRate, Tag _diffusionDistance, List<Effect> _secondaryEffects, AnimationInfo _animationInfo, eTargetUnitClassification _targetClassification)
    {
		try 
		{
			String query = "select * from HealEffects where BaseEffectId=?"; //Load detailed data of the effect
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int valueTagId = resultSet.getInt("ValueTagId");
			boolean isFixedValue = resultSet.getBoolean("IsFixedValue");
			
			Tag value = loadTag(_con, valueTagId);
			
			return new HealEffect(_id, _activationCondition, _timesToApply, _successRate, _diffusionDistance, _secondaryEffects, _animationInfo, _targetClassification, value, isFixedValue);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static StatusEffectAttachmentEffect loadStatusEffectAttachmentEffect(Connection _con, int _id, ComplexCondition _activationCondition, Tag _timesToApply, Tag _successRate, Tag _diffusionDistance, List<Effect> _secondaryEffects, AnimationInfo _animationInfo, eTargetUnitClassification _targetClassification)
    {
		try 
		{
			String query = "select * from StatusEffectAttachmentEffects where BaseEffectId=?"; //Load detailed data of the effect
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int statusEffectId = resultSet.getInt("StatusEffectId");
			
			StatusEffectData statusEffectData = Linq.first(statusEffects, x -> x.Id == statusEffectId);
			
			return new StatusEffectAttachmentEffect(_id, _activationCondition, _timesToApply, _successRate, _diffusionDistance, _secondaryEffects, _animationInfo, _targetClassification, statusEffectData);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static MovementEffect loadMovementEffect(Connection _con, int _id, ComplexCondition _activationCondition, Tag _timesToApply, List<Effect> _secondaryEffects)
    {
		try 
		{
			String query = "select * from MovementEffects where BaseEffectId=?"; //Load detailed data of the effect
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int animationId = resultSet.getInt("AnimationId");
			
			MovementAnimationInfo animationInfo = loadMovementAnimationInfo(_con, animationId);
			
			return new MovementEffect(_id, _activationCondition, _timesToApply, _secondaryEffects, animationInfo);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static void loadStatusEffects(Connection _con)
    {
    	statusEffects.clear();
    	
    	try 
    	{	
			String query;
			PreparedStatement statement;
			
			ResultSet resultSet;
    		
			query = "select Id from BaseStatusEffects"; //Load the entire BaseStatusEffects.Id column
			statement = _con.prepareStatement(query);
			
			resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int id = resultSet.getInt("Id");
				statusEffects.add(loadStatusEffectData(_con, id));
			}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }
    
    private static StatusEffectData loadStatusEffectData(Connection _con, int _id)
    {
		try 
		{
			String query = "select * from BaseStatusEffects where Id=?"; 
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			boolean isBackgroundStatusEffect = resultSet.getBoolean("IsBackgroundStatusEffect");
			int durationId = resultSet.getInt("DurationId");
			int iconId = resultSet.getInt("IconId");
			
			DurationData duration = loadDurationData(_con, durationId);
			ComplexCondition activationCondition = loadStatusEffectActivationCondition(_con, _id);
			byte[] iconAsBytes = loadStatusEffectIconAsBytes(_con, iconId);
			
			if (isBackgroundStatusEffect)
				return loadBackgroundStatusEffectData(_con, _id, duration, activationCondition, iconAsBytes);
			else
				return loadForegroundStatusEffectData(_con, _id, duration, activationCondition, iconAsBytes);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static BackgroundStatusEffectData loadBackgroundStatusEffectData(Connection _con, int _id, DurationData _duration, ComplexCondition _activationCondition, byte[] _iconAsBytes)
    {
		try 
		{
			String query = "select * from BackgroundStatusEffects where BaseStatusEffectId=?"; 
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int classificationId = resultSet.getInt("ClassificationId");
			int activationTurnClassificationId = resultSet.getInt("ActivationTurnClassificationId");
			
			eActivationTurnClassification activationTurnClassification = loadActivationTurnClassification(_con, activationTurnClassificationId);
			
			query = "select Name from BackgroundStatusEffectClassifications where Id=?"; //Get classification name for the effect
			statement = _con.prepareStatement(query);
			statement.setInt(1, classificationId);
			
			resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			String classificationName = resultSet.getString("Name");
			switch (classificationName)
			{
				case "Buff":
					return loadBuffStatusEffectData(_con, _id, _duration, _activationCondition, _iconAsBytes, activationTurnClassification);
				case "Debuff":
					return loadDebuffStatusEffectData(_con, _id, _duration, _activationCondition, _iconAsBytes, activationTurnClassification);
				case "TargetRangeMod":
					return loadTargetRangeModStatusEffectData(_con, _id, _duration, _activationCondition, _iconAsBytes, activationTurnClassification);
				default:
					return null;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static BuffStatusEffectData loadBuffStatusEffectData(Connection _con, int _id, DurationData _duration, ComplexCondition _activationCondition, byte[] _iconAsBytes, eActivationTurnClassification _activationTurnClassification)
    {
		try 
		{
			String query = "select * from BuffStatusEffects where BaseStatusEffectId=?"; 
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int targetStatusClassificationId = resultSet.getInt("TargetStatusClassificationId");
			int valueTagId = resultSet.getInt("ValueTagId");
			boolean isSum = resultSet.getBoolean("IsSum");
			
			eStatusType targetStatusType = loadStatusType(_con, targetStatusClassificationId);
			Tag value = loadTag(_con, valueTagId);
			
			return new BuffStatusEffectData(_id, _duration, _activationTurnClassification, _activationCondition, _iconAsBytes, targetStatusType, value, isSum);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static DebuffStatusEffectData loadDebuffStatusEffectData(Connection _con, int _id, DurationData _duration, ComplexCondition _activationCondition, byte[] _iconAsBytes, eActivationTurnClassification _activationTurnClassification)
    {
		try 
		{
			String query = "select * from DebuffStatusEffects where BaseStatusEffectId=?"; 
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int targetStatusClassificationId = resultSet.getInt("TargetStatusClassificationId");
			int valueTagId = resultSet.getInt("ValueTagId");
			boolean isSum = resultSet.getBoolean("IsSum");
			
			eStatusType targetStatusType = loadStatusType(_con, targetStatusClassificationId);
			Tag value = loadTag(_con, valueTagId);
			
			return new DebuffStatusEffectData(_id, _duration, _activationTurnClassification, _activationCondition, _iconAsBytes, targetStatusType, value, isSum);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static TargetRangeModStatusEffectData loadTargetRangeModStatusEffectData(Connection _con, int _id, DurationData _duration, ComplexCondition _activationCondition, byte[] _iconAsBytes, eActivationTurnClassification _activationTurnClassification)
    {
		try 
		{
			String query = "select * from TargetRangeModStatusEffects where BaseStatusEffectId=?"; 
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			boolean isMovementRangeClassification = resultSet.getBoolean("IsMovementRangeClassification");
			int targetRangeClassificationId = resultSet.getInt("TargetRangeClassificationId");
			int modificationMethodId = resultSet.getInt("ModificationMethodId");
			
			eTargetRangeClassification targetRangeClassification = loadTargetRangeClassification(_con, targetRangeClassificationId);
			eModificationMethod modificationMethod = loadModificationMethod(_con, modificationMethodId);
			
			return new TargetRangeModStatusEffectData(_id, _duration, _activationTurnClassification, _activationCondition, _iconAsBytes, isMovementRangeClassification, targetRangeClassification, modificationMethod);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static ForegroundStatusEffectData loadForegroundStatusEffectData(Connection _con, int _id, DurationData _duration, ComplexCondition _activationCondition, byte[] _iconAsBytes)
    {
		try 
		{
			String query = "select * from ForegroundStatusEffects where BaseStatusEffectId=?"; 
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int classificationId = resultSet.getInt("ClassificationId");
			int activationTurnClassificationId = resultSet.getInt("ActivationTurnClassificationId");
			int eventTriggerTimingId = resultSet.getInt("EventTriggerTimingId");
			int animationId = resultSet.getInt("AnimationId");
			
			eActivationTurnClassification activationTurnClassification = loadActivationTurnClassification(_con, activationTurnClassificationId);
			eEventTriggerTiming eventTriggerTiming = loadEventTriggerTiming(_con, eventTriggerTimingId);
			SimpleAnimationInfo animationInfo = (SimpleAnimationInfo)loadNonMovementAnimationInfo(_con, animationId);
			
			query = "select Name from ForegroundStatusEffectClassifications where Id=?"; //Get classification name for the effect
			statement = _con.prepareStatement(query);
			statement.setInt(1, classificationId);
			
			resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			String classificationName = resultSet.getString("Name");
			switch (classificationName)
			{
				case "Damage":
					return loadDamageStatusEffectData(_con, _id, _duration, _activationCondition, _iconAsBytes, activationTurnClassification, eventTriggerTiming, animationInfo);
				case "Heal":
					return loadHealStatusEffectData(_con, _id, _duration, _activationCondition, _iconAsBytes, activationTurnClassification, eventTriggerTiming, animationInfo);
				default:
					return null;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static HealStatusEffectData loadHealStatusEffectData(Connection _con, int _id, DurationData _duration, ComplexCondition _activationCondition, byte[] _iconAsBytes, eActivationTurnClassification _activationTurnClassification, eEventTriggerTiming _eventTriggerTiming, SimpleAnimationInfo _animationInfo)
    {
		try 
		{
			String query = "select * from HealStatusEffects where BaseStatusEffectId=?"; 
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int valueTagId = resultSet.getInt("ValueTagId");
			
			Tag value = loadTag(_con, valueTagId);
			
			return new HealStatusEffectData(_id, _duration, _activationTurnClassification, _eventTriggerTiming, _activationCondition, _iconAsBytes, _animationInfo, value);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static DamageStatusEffectData loadDamageStatusEffectData(Connection _con, int _id, DurationData _duration, ComplexCondition _activationCondition, byte[] _iconAsBytes, eActivationTurnClassification _activationTurnClassification, eEventTriggerTiming _eventTriggerTiming, SimpleAnimationInfo _animationInfo)
    {
		try 
		{
			String query = "select * from DamageStatusEffects where BaseStatusEffectId=?"; 
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int valueTagId = resultSet.getInt("ValueTagId");
			
			Tag value = loadTag(_con, valueTagId);
			
			return new DamageStatusEffectData(_id, _duration, _activationTurnClassification, _eventTriggerTiming, _activationCondition, _iconAsBytes, _animationInfo, value);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static DurationData loadDurationData(Connection _con, int _id)
    {
		try 
		{
			String query = "select * from Durations where Id=?"; 
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int activationTimesTagId = resultSet.getInt("ActivationTimesTagId");
			int turnsTagId = resultSet.getInt("TurnsTagId");
			
			Tag activationTimes = loadTag(_con, activationTimesTagId);
			Tag turns = loadTag(_con, turnsTagId);
			ComplexCondition whileCondition = loadDurationDataWhileCondition(_con, _id);
			
			return new DurationData(activationTimes, turns, whileCondition);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }

    private static ComplexCondition loadCounterSkillActivationCondition(Connection _con, int _id)
    {
    	return loadComplexCondition(_con, _id, "select ConditionSetId from CounterSkill_ActivationConditionSet where SkillId=?");
    }
    private static ComplexCondition loadPassiveSkillActivationCondition(Connection _con, int _id)
    {
    	return loadComplexCondition(_con, _id, "select ConditionSetId from PassiveSkill_ActivationConditionSet where SkillId=?");
    }
    private static ComplexCondition loadEffectActivationCondition(Connection _con, int _id)
    {
    	return loadComplexCondition(_con, _id, "select ConditionSetId from Effect_ActivationConditionSet where EffectId=?");
    }
    private static ComplexCondition loadStatusEffectActivationCondition(Connection _con, int _id)
    {
    	return loadComplexCondition(_con, _id, "select ConditionSetId from StatusEffect_ActivationConditionSet where StatusEffectId=?");
    }
    private static ComplexCondition loadDurationDataWhileCondition(Connection _con, int _id)
    {
    	return loadComplexCondition(_con, _id, "select ConditionSetId from Duration_WhileConditionSet where DurationId=?");
    }
    private static ComplexCondition loadComplexCondition(Connection _con, int _id, String _query)
    {
    	try 
		{
    		List<List<Condition>> conditionSets = new ArrayList<List<Condition>>();
    		
	    	PreparedStatement statement;
			statement = _con.prepareStatement(_query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	while (resultSet.next())
	    	{
	    		List<Condition> conditionSet = new ArrayList<Condition>();
	    		int conditionSetId = resultSet.getInt("ConditionSetId");
	    		addConditions(_con, conditionSetId, conditionSet);
	    		conditionSets.add(conditionSet);
	    	}
	    	
	    	return new ComplexCondition(conditionSets);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
    }

    private static byte[] loadSkillIconAsBytes(Connection _con, int _id)
    {
    	return loadImageAsBytes(_con, _id, "select IconPath from SkillIcons where Id=?", "IconPath");
    }
    private static byte[] loadStatusEffectIconAsBytes(Connection _con, int _id)
    {
    	return loadImageAsBytes(_con, _id, "select IconPath from StatusEffectIcons where Id=?", "IconPath");
    }
    private static byte[] loadGachaBannerImageAsBytes(Connection _con, int _id)
    {
    	return loadImageAsBytes(_con, _id, "select ImagePath from GachaBannerImages where Id=?", "ImagePath");
    }
    private static byte[] loadGachaBackgroundImageAsBytes(Connection _con, int _id)
    {
    	return loadImageAsBytes(_con, _id, "select ImagePath from GachaBackgroundImages where Id=?", "ImagePath");
    }
    private static byte[] loadImageAsBytes(Connection _con, int _id, String _query, String _columnName)
    {
    	try 
		{
	    	PreparedStatement statement;
			statement = _con.prepareStatement(_query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return null;
	    	
	    	String imagePath = resultSet.getString(_columnName);
	    	
	    	BufferedImage image = ImageIO.read(new File(imagePath));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "png", outputStream);
	    	return outputStream.toByteArray();
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return null;
		}
    }

    private static void addConditions(Connection _con, int _id, List<Condition> _conditionSet)
    {
    	try 
		{  		
			String query = "select ConditionId from ConditionSet_Condition where ConditionSetId=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	while (resultSet.next())
	    	{
	    		int conditionId = resultSet.getInt("ConditionId");
	    		Condition condition = loadCondition(_con, conditionId);
	    		_conditionSet.add(condition);
	    	}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
    }
    
    private static Condition loadCondition(Connection _con, int _id)
    {
    	try 
		{  		
			String query = "select * from Conditions where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return null;
	    	
	    	int tagIdA = resultSet.getInt("TagIdA");
	    	int relationId = resultSet.getInt("RelationId");
	    	int tagIdB = resultSet.getInt("TagIdB");
	    	
	    	Tag tagA = loadTag(_con, tagIdA);
	    	eRelationType relation = loadRelationType(_con, relationId);
	    	Tag tagB = loadTag(_con, tagIdB);
	    	
	    	return new Condition(tagA, relation, tagB);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
    }
    
    private static Tag loadTag(Connection _con, int _id)
    {
		try 
		{
			String query = "select String from Tags where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return null;
	    	
	    	String string = resultSet.getString("String");
	    	
	    	return Tag.newTag(string);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}    	
    }
    
    private static void loadWeaponRecipes (Connection _con)
    {
    	weaponRecipes.clear();
    	
    	try 
    	{	
			String query;
			PreparedStatement statement;
			
			ResultSet resultSet;
    		
			query = "select Id from WeaponRecipes"; //Load the entire WeaponRecipes.Id column
			statement = _con.prepareStatement(query);
			
			resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int id = resultSet.getInt("Id");
				weaponRecipes.add(loadWeaponRecipe(_con, id));
			}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }
    
    private static WeaponRecipe loadWeaponRecipe(Connection _con, int _id)
    {
		try 
		{
			String query = "select * from WeaponRecipes where Id=?"; 
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int productId = resultSet.getInt("ProductId");
			int material1Id = resultSet.getInt("Material1Id");
			int material2Id = resultSet.getInt("Material2Id");
			int material3Id = resultSet.getInt("Material3Id");
			int material4Id = resultSet.getInt("Material4Id");
			int material5Id = resultSet.getInt("Material5Id");
			int upgradingWeaponId = resultSet.getInt("UpgradingWeaponId");
			int cost = resultSet.getInt("Cost");
			
			WeaponData productWeaponData = Linq.first(weapons, x -> x.Id == productId);
			List<EquipmentMaterial> materials = new ArrayList<EquipmentMaterial>();
			materials.add(Linq.first(Linq.ofType(items, EquipmentMaterial.class), x -> x.Id == material1Id));
			materials.add(Linq.first(Linq.ofType(items, EquipmentMaterial.class), x -> x.Id == material2Id));
			materials.add(Linq.first(Linq.ofType(items, EquipmentMaterial.class), x -> x.Id == material3Id));
			materials.add(Linq.first(Linq.ofType(items, EquipmentMaterial.class), x -> x.Id == material4Id));
			materials.add(Linq.first(Linq.ofType(items, EquipmentMaterial.class), x -> x.Id == material5Id));
			WeaponData upgradingWeaponData = (upgradingWeaponId > 0) ? Linq.first(weapons, x -> x.Id == upgradingWeaponId) : null;
			
			if (upgradingWeaponData == null)
				return new WeaponRecipe(productWeaponData, materials, cost);
			else
				return new WeaponRecipe(productWeaponData, materials, cost, upgradingWeaponData);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static void loadArmourRecipes (Connection _con)
    {
    	armourRecipes.clear();
    	
    	try 
    	{	
			String query;
			PreparedStatement statement;
			
			ResultSet resultSet;
    		
			query = "select Id from ArmourRecipes"; //Load the entire ArmourRecipes.Id column
			statement = _con.prepareStatement(query);
			
			resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int id = resultSet.getInt("Id");
				armourRecipes.add(loadArmourRecipe(_con, id));
			}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }
    
    private static ArmourRecipe loadArmourRecipe(Connection _con, int _id)
    {
		try 
		{
			String query = "select * from ArmourRecipes where Id=?"; 
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int productId = resultSet.getInt("ProductId");
			int material1Id = resultSet.getInt("Material1Id");
			int material2Id = resultSet.getInt("Material2Id");
			int material3Id = resultSet.getInt("Material3Id");
			int material4Id = resultSet.getInt("Material4Id");
			int material5Id = resultSet.getInt("Material5Id");
			int upgradingArmourId = resultSet.getInt("UpgradingArmourId");
			int cost = resultSet.getInt("Cost");
			
			ArmourData productArmourData = Linq.first(armours, x -> x.Id == productId);
			List<EquipmentMaterial> materials = new ArrayList<EquipmentMaterial>();
			materials.add(Linq.first(Linq.ofType(items, EquipmentMaterial.class), x -> x.Id == material1Id));
			materials.add(Linq.first(Linq.ofType(items, EquipmentMaterial.class), x -> x.Id == material2Id));
			materials.add(Linq.first(Linq.ofType(items, EquipmentMaterial.class), x -> x.Id == material3Id));
			materials.add(Linq.first(Linq.ofType(items, EquipmentMaterial.class), x -> x.Id == material4Id));
			materials.add(Linq.first(Linq.ofType(items, EquipmentMaterial.class), x -> x.Id == material5Id));
			ArmourData upgradingArmourData = (upgradingArmourId > 0) ? Linq.first(armours, x -> x.Id == upgradingArmourId) : null;
			
			if (upgradingArmourData == null)
				return new ArmourRecipe(productArmourData, materials, cost);
			else
				return new ArmourRecipe(productArmourData, materials, cost, upgradingArmourData);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static void loadAccessoryRecipes (Connection _con)
    {
    	accessoryRecipes.clear();
    	
    	try 
    	{	
			String query;
			PreparedStatement statement;
			
			ResultSet resultSet;
    		
			query = "select Id from AccessoryRecipes"; //Load the entire AccessoryRecipes.Id column
			statement = _con.prepareStatement(query);
			
			resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int id = resultSet.getInt("Id");
				accessoryRecipes.add(loadAccessoryRecipe(_con, id));
			}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }
    
    private static AccessoryRecipe loadAccessoryRecipe(Connection _con, int _id)
    {
		try 
		{
			String query = "select * from AccessoryRecipes where Id=?"; 
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int productId = resultSet.getInt("ProductId");
			int material1Id = resultSet.getInt("Material1Id");
			int material2Id = resultSet.getInt("Material2Id");
			int material3Id = resultSet.getInt("Material3Id");
			int material4Id = resultSet.getInt("Material4Id");
			int material5Id = resultSet.getInt("Material5Id");
			int upgradingAccessoryId = resultSet.getInt("UpgradingAccessoryId");
			int cost = resultSet.getInt("Cost");
			
			AccessoryData productAccessoryData = Linq.first(accessories, x -> x.Id == productId);
			List<EquipmentMaterial> materials = new ArrayList<EquipmentMaterial>();
			materials.add(Linq.first(Linq.ofType(items, EquipmentMaterial.class), x -> x.Id == material1Id));
			materials.add(Linq.first(Linq.ofType(items, EquipmentMaterial.class), x -> x.Id == material2Id));
			materials.add(Linq.first(Linq.ofType(items, EquipmentMaterial.class), x -> x.Id == material3Id));
			materials.add(Linq.first(Linq.ofType(items, EquipmentMaterial.class), x -> x.Id == material4Id));
			materials.add(Linq.first(Linq.ofType(items, EquipmentMaterial.class), x -> x.Id == material5Id));
			AccessoryData upgradingAccessoryData = (upgradingAccessoryId > 0) ? Linq.first(accessories, x -> x.Id == upgradingAccessoryId) : null;
			
			if (upgradingAccessoryData == null)
				return new AccessoryRecipe(productAccessoryData, materials, cost);
			else
				return new AccessoryRecipe(productAccessoryData, materials, cost, upgradingAccessoryData);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static void loadItemRecipes (Connection _con)
    {
    	itemRecipes.clear();
    	
    	try 
    	{	
			String query;
			PreparedStatement statement;
			
			ResultSet resultSet;
    		
			query = "select Id from ItemRecipes"; //Load the entire ItemRecipes.Id column
			statement = _con.prepareStatement(query);
			
			resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int id = resultSet.getInt("Id");
				itemRecipes.add(loadItemRecipe(_con, id));
			}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }
    
    private static ItemRecipe loadItemRecipe(Connection _con, int _id)
    {
		try 
		{
			String query = "select * from ItemRecipes where Id=?"; 
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int productId = resultSet.getInt("ProductId");
			int material1Id = resultSet.getInt("Material1Id");
			int material2Id = resultSet.getInt("Material2Id");
			int material3Id = resultSet.getInt("Material3Id");
			int material4Id = resultSet.getInt("Material4Id");
			int material5Id = resultSet.getInt("Material5Id");
			int upgradingItemId = resultSet.getInt("UpgradingItemId");
			int cost = resultSet.getInt("Cost");
			
			Item productItemData = Linq.first(items, x -> x.Id == productId);
			List<ItemMaterial> materials = new ArrayList<ItemMaterial>();
			materials.add(Linq.first(Linq.ofType(items, ItemMaterial.class), x -> x.Id == material1Id));
			materials.add(Linq.first(Linq.ofType(items, ItemMaterial.class), x -> x.Id == material2Id));
			materials.add(Linq.first(Linq.ofType(items, ItemMaterial.class), x -> x.Id == material3Id));
			materials.add(Linq.first(Linq.ofType(items, ItemMaterial.class), x -> x.Id == material4Id));
			materials.add(Linq.first(Linq.ofType(items, ItemMaterial.class), x -> x.Id == material5Id));
			Item upgradingItemData = (upgradingItemId > 0) ? Linq.first(items, x -> x.Id == upgradingItemId) : null;
			
			if (upgradingItemData == null)
				return new ItemRecipe(productItemData, materials, cost);
			else
				return new ItemRecipe(productItemData, materials, cost, upgradingItemData);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}   
    }
    
    private static void loadTileSets(Connection _con)
    {
    	tileSets.clear();
    	
    	try 
    	{	
			String query;
			PreparedStatement statement;
			
			ResultSet resultSet;
    		
			query = "select Id from TileSets"; //Load the entire TileSets.Id column
			statement = _con.prepareStatement(query);
			
			resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int id = resultSet.getInt("Id");
				tileSets.add(loadTileSet(_con, id));
			}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }
    
    private static TileSet loadTileSet(Connection _con, int _id)
    {
    	List<eTileType> tileTypes = new ArrayList<eTileType>();
    	
    	try
    	{	
			String query = "select * from TileSet_TileClassification where TileSetId=?";
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _id);
			
			ResultSet resultSet = statement.executeQuery();
    		while(resultSet.next())
    		{
        		int classificationId = resultSet.getInt("ClassificationId");
    			int relativeQuantity = resultSet.getInt("RelativeQuantity");
    			
    			eTileType tileType = loadTileType(_con, classificationId);
    			
    			for (int i = 0; i < relativeQuantity; i++)
    			{
    				tileTypes.add(tileType);
    			}
    		}
			
			return new TileSet(tileTypes);
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    		return null;
    	}
    }
    
    public static void reloadGachas()
    {
		try 
		{
			//-------------------------------Connection----------------------------------------		
			Connection con = DriverManager
					.getConnection(CoreValues.URL, CoreValues.USERNAME, CoreValues.PASSWORD);
			//---------------------------------------------------------------------------------
			
			loadGachas(con);
			loadGachasAsResponseStrings();
    		loadGachaDispensationAvailabilityInfos(con);
			
			con.close();
		} 
		catch (SQLException ex) 
		{
			ex.printStackTrace();
		}
    }
    
    private static void loadGachas(Connection _con)
    {
    	gachas.clear();
    	
    	try 
    	{	
			String query;
			PreparedStatement statement;
			
			ResultSet resultSet;
			
			DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String formattedDate = formatDate.format(date);
			
			//Load the entries in DateTimeRanges table where the date range includes current date
			query = "select Id from DateTimeRanges where StartingDateTime<=? and SecondPastEndingDateTime>?";
			statement = _con.prepareStatement(query);
			statement.setString(1, formattedDate);
			statement.setString(2, formattedDate);
			
			resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				int dateTimeRangeId = resultSet.getInt("Id");
				
				query = "select Id from Schedules where DateTimeRangeId=?";
				statement = _con.prepareStatement(query);
				statement.setInt(1, dateTimeRangeId);
				
				ResultSet resultSet2 = statement.executeQuery();
				while (resultSet2.next())
				{
					int scheduleId = resultSet2.getInt("Id");
					
					//Check whether current date matches the days of week specified for the schedule
					boolean dayOfWeekMatches = false;
					{
						query = "select DayId from Schedule_Day where ScheduleId=?";
						statement = _con.prepareStatement(query);
						statement.setInt(1, scheduleId);
						
						ResultSet resultSet3 = statement.executeQuery();
						int numberOfDays = 0;
						while (resultSet3.next())
						{
							numberOfDays++;
							
							int dayId = resultSet3.getInt("DayId");
							
							query = "select Name from Days where Id=?";
							statement = _con.prepareStatement(query);
							statement.setInt(1, dayId);
							
							ResultSet resultSet4 = statement.executeQuery();
							if (resultSet4.next())
							{
								String dayOfWeekName = resultSet4.getString("Name");
								
								DateFormat dateFormat_dayOfWeek = new SimpleDateFormat("EEEE");
								String dayOfWeekString = dateFormat_dayOfWeek.format(date);
								
								dayOfWeekMatches = dayOfWeekName.equals(dayOfWeekString);
							}
						}
						
						if (numberOfDays == 0) //No day of week has been specified for the schedule, meaning that any day is ok
							dayOfWeekMatches = true;
					}
					
					if (dayOfWeekMatches)
					{
						//Check whether current time is between the range of time specified for the schedule
						boolean isWithinTimeRange = false;
						{
							query = "select TimeRangeId from Schedule_TimeRange where ScheduleId=?";
							statement = _con.prepareStatement(query);
							statement.setInt(1, scheduleId);
							
							ResultSet resultSet3 = statement.executeQuery();
							int numberOfTimeRanges = 0;
							while (resultSet3.next())
							{
								numberOfTimeRanges++;
								
								int timeRangeId = resultSet3.getInt("TimeRangeId");
								
								DateFormat dateFormat_time = new SimpleDateFormat("HH:mm:ss");
								String timeString = dateFormat_time.format(date);
								
								query = "select * from TimeRanges where Id=? and StartingTime<=? and SecondPastEndingTime>?";
								statement = _con.prepareStatement(query);
								statement.setInt(1, timeRangeId);
								statement.setString(2, timeString);
								statement.setString(3, timeString);
								
								ResultSet resultSet4 = statement.executeQuery();
								if (resultSet4.next()) //If there is at least one entry, it means that current time is within the time range in the schedule
									isWithinTimeRange = true;
							}
							
							if (numberOfTimeRanges == 0) //No time range has been specified for the schedule, meaning that any time is ok
								isWithinTimeRange = true;
						}
						
						if (isWithinTimeRange)
						{
							//Get all gachas that with the certain schedule
							query = "select GachaId from Gacha_Schedule where ScheduleId=?";
							statement = _con.prepareStatement(query);
							statement.setInt(1, scheduleId);
							
							ResultSet resultSet3 = statement.executeQuery();
							while (resultSet3.next())
							{
								int gachaId = resultSet3.getInt("GachaId");
								gachas.add(loadGacha(_con, gachaId));
							}
						}
					}	
				}
			}
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    	}
    }
    
    private static Gacha loadGacha(Connection _con, int _id)
    {
    	try
    	{
    		String query = "select * from BaseGachas where Id=?";
    		PreparedStatement statement = _con.prepareStatement(query);
        	statement.setInt(1, _id);
    		
    		ResultSet resultSet = statement.executeQuery();
    		if (!resultSet.next())
    			return null;
    		
    		String title = resultSet.getString("Title");
        	int classificationId = resultSet.getInt("ClassificationId");
    		int valueAndRaritySetId = resultSet.getInt("ValueAndRaritySetId");
    		int bannerImageId = resultSet.getInt("BannerImageId");
    		int backgroundImageId = resultSet.getInt("BackgroundImageId");
    		int alternativeDispensationInfoId = resultSet.getInt("AlternativeDispensationInfoId");
    		
    		eGachaClassification classification = loadGachaClassification(_con, classificationId);
    		List<GachaObjectInfo> gachaObjectInfos = new ArrayList<GachaObjectInfo>();
    		addGachaObjectInfos(_con, _id, classification, gachaObjectInfos);
    		ValuePerRarity defaultDispensationValues = loadValuePerRariry(_con, valueAndRaritySetId);
    		AlternativeDispensationInfo alternativeDispensationInfo = loadAlternativeDispensationInfo(_con, alternativeDispensationInfoId);
    		List<DispensationOption> dispensationOptions = new ArrayList<DispensationOption>();
    		addGachaDispensationOptions(_con, _id, dispensationOptions);
    		byte[] bannerImageAsBytes = loadGachaBannerImageAsBytes(_con, bannerImageId);
    		byte[] backgroundImageAsBytes = loadGachaBackgroundImageAsBytes(_con, backgroundImageId);
    		
    		int levelOfObjects = 0;
    		if (classification == eGachaClassification.Unit)
    			levelOfObjects = loadLevelOfGachaObjects(_con, _id, "UnitGachas", "LevelOfUnits");
    		else if (classification == eGachaClassification.Weapon)
    			levelOfObjects = loadLevelOfGachaObjects(_con, _id, "WeaponGachas", "LevelOfLevelableWeapons");
    		
    		return new Gacha(_id, title, classification, gachaObjectInfos, defaultDispensationValues, alternativeDispensationInfo, dispensationOptions, bannerImageAsBytes, backgroundImageAsBytes, levelOfObjects);
    	}
    	catch (Exception ex)
    	{
    		System.out.println(ex.toString());
    		return null;
    	}
    }
    
    private static void addGachaObjectInfos(Connection _con, int _id, eGachaClassification _classification, List<GachaObjectInfo> _gachaObjectInfos)
    {
    	try 
		{  		
    		String queryVar = _classification.name();
			String query = "select *"
							+ " from " + queryVar + "Gacha_" + queryVar + "Occurrence"
							+ " where GachaId=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	Map<Integer, Integer> objId_occurrence = new HashMap<Integer, Integer>();
	    	ResultSet resultSet = statement.executeQuery();
	    	while (resultSet.next())
	    	{
	    		int objectId = resultSet.getInt(queryVar + "Id");
	    		int relativeOccurrenceValue = resultSet.getInt("RelativeOccurrenceValue");
	    		objId_occurrence.put(objectId, relativeOccurrenceValue);
	    	}
	    	
	    	switch (_classification)
	    	{
	    		default: //case Unit
	    	    	for (Map.Entry<Integer, Integer> entry : objId_occurrence.entrySet())
	    	    	{
	    	    		RarityMeasurable object = Linq.first(units, x -> x.Id == entry.getKey());
	    	    		_gachaObjectInfos.add(new GachaObjectInfo(object, entry.getValue()));
	    	    	}
	    	    	break;
	    	    	
	    		case Weapon:
	    	    	for (Map.Entry<Integer, Integer> entry : objId_occurrence.entrySet())
	    	    	{
	    	    		RarityMeasurable object = Linq.first(weapons, x -> x.Id == entry.getKey());
	    	    		_gachaObjectInfos.add(new GachaObjectInfo(object, entry.getValue()));
	    	    	}
	    	    	break;
	    	    	
	    		case Armour:
	    	    	for (Map.Entry<Integer, Integer> entry : objId_occurrence.entrySet())
	    	    	{
	    	    		RarityMeasurable object = Linq.first(armours, x -> x.Id == entry.getKey());
	    	    		_gachaObjectInfos.add(new GachaObjectInfo(object, entry.getValue()));
	    	    	}
	    	    	break;
	    	    	
	    		case Accessory:
	    	    	for (Map.Entry<Integer, Integer> entry : objId_occurrence.entrySet())
	    	    	{
	    	    		RarityMeasurable object = Linq.first(accessories, x -> x.Id == entry.getKey());
	    	    		_gachaObjectInfos.add(new GachaObjectInfo(object, entry.getValue()));
	    	    	}
	    	    	break;
	    	    	
	    		case SkillItem:
	    		case SkillMaterial:	
	    		case ItemMaterial:	    	    	
	    		case EquipmentMaterial:
	    		case EvolutionMaterial:
	    		case WeaponEnhMaterial:
	    		case UnitEnhMaterial:
	    		case SkillEnhMaterial:
	    	    	for (Map.Entry<Integer, Integer> entry : objId_occurrence.entrySet())
	    	    	{
	    	    		RarityMeasurable object = Linq.first(items, x -> x.Id == entry.getKey());
	    	    		_gachaObjectInfos.add(new GachaObjectInfo(object, entry.getValue()));
	    	    	}
	    	    	break;
	    	}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
    }
    
    private static void addGachaDispensationOptions(Connection _con, int _id, List<DispensationOption> _dispensationOptions)
    {
    	try 
		{  		
			String query = "select DispensationOptionId from Gacha_DispensationOption where GachaId=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	while(resultSet.next())
	    	{
	    		int dispensationOptionId = resultSet.getInt("DispensationOptionId");
	    		_dispensationOptions.add(loadDispensationOption(_con, dispensationOptionId));
	    	}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
    }
    
    private static DispensationOption loadDispensationOption(Connection _con, int _id)
    {
    	try 
		{  		
			String query = "select * from DispensationOptions where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return null;
	    	
	    	int costTypeId = resultSet.getInt("CostTypeId");
	    	int costItemId = resultSet.getInt("CostItemId");
	    	int costValue = resultSet.getInt("CostValue");
	    	int timesToDispense = resultSet.getInt("TimesToDispense");
	    	int attemptsAllowedPerPlayer = resultSet.getInt("AttemptsAllowedPerPlayer");
	    	boolean isNumberOfAttemptsPerDay = resultSet.getBoolean("IsNumberOfAttemptsPerDay");
	    	
	    	eCostType costType = loadCostType(_con, costTypeId);
	    	
	    	return new DispensationOption(_id, costType, costItemId, costValue, timesToDispense, attemptsAllowedPerPlayer, isNumberOfAttemptsPerDay);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
    }
    
    private static int loadLevelOfGachaObjects(Connection _con, int _id, String _tableName, String _columnName)
    {
    	try 
		{	    	
    		String query = "select " + _columnName + " from " + _tableName + " where BaseGachaId=?";
	    	PreparedStatement statement;
	    	statement = _con.prepareStatement(query);
    		statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return 0;
	    	
	    	int levelOfObjects = resultSet.getInt(_columnName);
	    	
	    	return levelOfObjects;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return 0;
		}
    }
    
    private static ValuePerRarity loadValuePerRariry(Connection _con, int _id)
    {
    	try 
		{	    	
    		String query = "select * from ValueAndRaritySets where Id=?";
	    	PreparedStatement statement;
	    	statement = _con.prepareStatement(query);
    		statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return null;
	    	
	    	int common = resultSet.getInt("Common");
	    	int uncommon = resultSet.getInt("Uncommon");
	    	int rare = resultSet.getInt("Rare");
	    	int epic = resultSet.getInt("Epic");
	    	int legendary = resultSet.getInt("Legendary");
	    	
	    	return new ValuePerRarity(common, uncommon, rare, epic, legendary);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
    }
    
    private static AlternativeDispensationInfo loadAlternativeDispensationInfo(Connection _con, int _id)
    {
    	try 
		{	    	
    		String query = "select * from AlternativeDispensationInfos where Id=?";
	    	PreparedStatement statement;
	    	statement = _con.prepareStatement(query);
    		statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return null;
	    	
	    	int applyAtXthDispensation = resultSet.getInt("ApplyAtXthDispensation");
	    	int valueAndRaritySetId = resultSet.getInt("ValueAndRaritySetId");
	    	
	    	ValuePerRarity ratioPerRarity = loadValuePerRariry(_con, valueAndRaritySetId);
	    	
	    	return new AlternativeDispensationInfo(applyAtXthDispensation, ratioPerRarity);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
    }
    
    private static void loadGachaDispensationAvailabilityInfos(Connection _con)
    {
    	gachaDispensationAvailabilityInfos.clear();
    	
    	for (Gacha gacha : gachas)
    	{
    		for (DispensationOption dispensationOption : gacha.getDispensationOptions())
    		{
    			for (Player player : players)
    			{
    				int remainingAttempts = gacha.remainingAttempts(_con, player.Id, dispensationOption);
    				gachaDispensationAvailabilityInfos.add(new GachaDispensationAvailabilityInfo(player.Id, gacha.Id, dispensationOption.id, remainingAttempts));
    			}
    		}
    	}
    }
    
    private static AnimationInfo loadNonMovementAnimationInfo(Connection _con, int _animationId)
    {
		try 
		{
			String query = "select AnimationClassificationId from NonMovementAnimationInfos where Id=?"; //Load the animation classification
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _animationId);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int animationClassificationId = resultSet.getInt("AnimationClassificationId");
			
			eNonMovementAnimationClassification animationClassification = loadNonMovementAnimationClassification(_con, animationClassificationId);
			
			switch (animationClassification)
			{
				default: //case Simple
					return loadSimpleAnimationInfo(_con, _animationId);
					
				case Projectile:
					return loadProjectileAnimationInfo(_con, _animationId);
					
				case Laser:
					return loadLaserAnimationInfo(_con, _animationId);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
    }
    
    private static SimpleAnimationInfo loadSimpleAnimationInfo(Connection _con, int _animationId)
    {
		try 
		{
			String query = "select * from SimpleAnimationInfos where BaseAnimationInfoId=?"; //Load the details of the animation
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _animationId);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int hitAnimationTypeId = resultSet.getInt("HitAnimationTypeId");
			
			return new SimpleAnimationInfo(hitAnimationTypeId);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
    }
    
    private static ProjectileAnimationInfo loadProjectileAnimationInfo(Connection _con, int _animationId)
    {
		try 
		{
			String query = "select * from ProjectileAnimationInfos where BaseAnimationInfoId=?"; //Load the details of the animation
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _animationId);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int projectileGenerationPointTypeId = resultSet.getInt("GenerationPointTypeId");
			int projectileTypeId = resultSet.getInt("ProjectileTypeId");
			int hitAnimationTypeId = resultSet.getInt("HitAnimationTypeId");
			
			return new ProjectileAnimationInfo(projectileGenerationPointTypeId, projectileTypeId, hitAnimationTypeId);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
    }
    
    private static LaserAnimationInfo loadLaserAnimationInfo(Connection _con, int _animationId)
    {
		try 
		{
			String query = "select * from LaserAnimationInfos where BaseAnimationInfoId=?"; //Load the details of the animation
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _animationId);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int laserGenerationPointTypeId = resultSet.getInt("GenerationPointTypeId");
			int laserAnimationTypeId = resultSet.getInt("LaserAnimationTypeId");
			int hitAnimationTypeId = resultSet.getInt("HitAnimationTypeId");
			
			return new LaserAnimationInfo(laserGenerationPointTypeId, laserAnimationTypeId, hitAnimationTypeId);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
    }
    
    private static MovementAnimationInfo loadMovementAnimationInfo(Connection _con, int _animationId)
    {
		try 
		{
			String query = "select * from MovementAnimationInfos where Id=?"; //Load the details of the animation
			PreparedStatement statement = _con.prepareStatement(query);
			statement.setInt(1, _animationId);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			int attachmentAnimationTypeId = resultSet.getInt("AttachmentAnimationTypeId");
			
			return new MovementAnimationInfo(attachmentAnimationTypeId);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
    }
    
    private static eTargetUnitClassification loadTargetUnitClassification(Connection _con, int _id)
    {
		try 
		{
			String query = "select Name from TargetUnitClassifications where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eTargetUnitClassification.values()[0];
	    	
	    	return eTargetUnitClassification.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eTargetUnitClassification.values()[0];
		}    	
    }

    private static eAttackClassification loadAttackClassification(Connection _con, int _id)
    {
		try 
		{
			String query = "select Name from AttackClassifications where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eAttackClassification.values()[0];
	    	
	    	return eAttackClassification.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eAttackClassification.values()[0];
		}    	
    }
    
    private static eElement loadElement(Connection _con, int _id)
    {
		try 
		{
			String query = "select Name from Elements where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eElement.values()[0];
	    	
	    	return eElement.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eElement.values()[0];
		}    	
    }
    
    private static eRelationType loadRelationType(Connection _con, int _id)
    {
		try 
		{
			String query = "select Name from Relations where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eRelationType.values()[0];
	    	
	    	return eRelationType.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eRelationType.values()[0];
		}    	
    }
    
    private static eActivationTurnClassification loadActivationTurnClassification(Connection _con, int _id)
    {
		try 
		{
			String query = "select Name from ActivationTurnClassifications where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eActivationTurnClassification.values()[0];
	    	
	    	return eActivationTurnClassification.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eActivationTurnClassification.values()[0];
		}    	
    }
    
    private static eEventTriggerTiming loadEventTriggerTiming(Connection _con, int _id)
    {
		try 
		{
			String query = "select Name from EventTriggerTimings where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eEventTriggerTiming.values()[0];
	    	
	    	return eEventTriggerTiming.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eEventTriggerTiming.values()[0];
		}    	
    }
    
    private static eStatusType loadStatusType(Connection _con, int _id)
    {
		try 
		{
			String query = "select Name from StatusClassifications where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eStatusType.values()[0];
	    	
	    	return eStatusType.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eStatusType.values()[0];
		}    	
    }
    
    private static eTargetRangeClassification loadTargetRangeClassification(Connection _con, int _id)
    {
		try 
		{
			String query = "select Name from TargetRangeClassifications where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eTargetRangeClassification.values()[0];
	    	
	    	return eTargetRangeClassification.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eTargetRangeClassification.values()[0];
		}    	
    }
    
    private static eModificationMethod loadModificationMethod(Connection _con, int _id)
    {
		try 
		{
			String query = "select Name from TargetRangeModificationMethods where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eModificationMethod.values()[0];
	    	
	    	return eModificationMethod.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eModificationMethod.values()[0];
		}    	
    }
    
    private static eRarity loadRarity(Connection _con, int _id)
    {
		try 
		{
			String query = "select Name from Rarities where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eRarity.values()[0];
	    	
	    	return eRarity.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eRarity.values()[0];
		}    	
    }
    
    private static eGender loadGender(Connection _con, int _id)
    {
		try 
		{
			String query = "select Name from Genders where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eGender.values()[0];
	    	
	    	return eGender.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eGender.values()[0];
		}    	
    }
    
    private static eWeaponClassification loadWeaponClassification(Connection _con, int _id)
    {
		try 
		{
			String query = "select Name from WeaponClassifications where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eWeaponClassification.values()[0];
	    	
	    	return eWeaponClassification.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eWeaponClassification.values()[0];
		}    	
    }
    
    private static eArmourClassification loadArmourClassification(Connection _con, int _id)
    {
		try 
		{
			String query = "select Name from ArmourClassifications where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eArmourClassification.values()[0];
	    	
	    	return eArmourClassification.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eArmourClassification.values()[0];
		}    	
    }
    
    private static eAccessoryClassification loadAccessoryClassification(Connection _con, int _id)
    {
		try 
		{
			String query = "select Name from AccessoryClassifications where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eAccessoryClassification.values()[0];
	    	
	    	return eAccessoryClassification.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eAccessoryClassification.values()[0];
		}    	
    }
    
    private static eWeaponType loadWeaponType(Connection _con, int _id)
    {
		try 
		{
			String query = "select Name from WeaponTypes where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eWeaponType.values()[0];
	    	
	    	return eWeaponType.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eWeaponType.values()[0];
		}    	
    }

    private static eTileType loadTileType(Connection _con, int _id)
    {
		try 
		{
			String query = "select Name from TileClassifications where Id=?";
	    	PreparedStatement statement;
			statement = _con.prepareStatement(query);
	    	statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eTileType.values()[0];
	    	
	    	return eTileType.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eTileType.values()[0];
		}    
    }
    
    private static eGachaClassification loadGachaClassification(Connection _con, int _id)
    {
		try 
		{	    	
    		String query = "select Name from GachaClassifications where Id=?"; //Get classification name for the gacha
	    	PreparedStatement statement;
	    	statement = _con.prepareStatement(query);
    		statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eGachaClassification.values()[0];
	    	
	    	return eGachaClassification.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eGachaClassification.values()[0];
		}    
    }
    
    private static eCostType loadCostType(Connection _con, int _id)
    {
		try 
		{	    	
    		String query = "select Name from CostTypes where Id=?"; //Get name for the costType
	    	PreparedStatement statement = _con.prepareStatement(query);
    		statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eCostType.values()[0];
	    	
	    	return eCostType.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eCostType.values()[0];
		}    
    }

    private static eNonMovementAnimationClassification loadNonMovementAnimationClassification(Connection _con, int _id)
    {
		try 
		{	    	
    		String query = "select Name from NonMovementAnimationClassifications where Id=?"; //Get name for the nonMovementAnimationClassification
	    	PreparedStatement statement = _con.prepareStatement(query);
    		statement.setInt(1, _id);
	    	
	    	ResultSet resultSet = statement.executeQuery();
	    	if (!resultSet.next())
	    		return eNonMovementAnimationClassification.values()[0];
	    	
	    	return eNonMovementAnimationClassification.valueOf(resultSet.getString("Name"));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return eNonMovementAnimationClassification.values()[0];
		}    
    }
}
