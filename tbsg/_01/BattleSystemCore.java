package eean_games.tbsg._01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import eean_games.main.CoreFunctions;
import eean_games.main.Linq;
import eean_games.main._2DCoord;
import eean_games.main.eRelationType;
import eean_games.main.eSortType;
import eean_games.main.extension_method.BigDecimalExtension;
import eean_games.main.extension_method.IntegerExtension;
import eean_games.main.extension_method._2DCoordExtension;
import eean_games.tbsg._01.animationInfo.MovementAnimationInfo;
import eean_games.tbsg._01.effect.DamageEffect;
import eean_games.tbsg._01.effect.DrainEffect;
import eean_games.tbsg._01.effect.Effect;
import eean_games.tbsg._01.effect.HealEffect;
import eean_games.tbsg._01.effect.ComplexTargetSelectionEffect;
import eean_games.tbsg._01.effect.MovementEffect;
import eean_games.tbsg._01.effect.StatusEffectAttachmentEffect;
import eean_games.tbsg._01.effect.TileTargetingEffect;
import eean_games.tbsg._01.effect.UnitTargetingEffect;
import eean_games.tbsg._01.effect.UnitTargetingEffectsWrapperEffect;
import eean_games.tbsg._01.enumerable.eActivationTurnClassification;
import eean_games.tbsg._01.enumerable.eEffectiveness;
import eean_games.tbsg._01.enumerable.eElement;
import eean_games.tbsg._01.enumerable.eEventTriggerTiming;
import eean_games.tbsg._01.enumerable.eGamePhase;
import eean_games.tbsg._01.enumerable.eGender;
import eean_games.tbsg._01.enumerable.eStatusType;
import eean_games.tbsg._01.enumerable.eStringMatchType;
import eean_games.tbsg._01.enumerable.eTargetUnitClassification;
import eean_games.tbsg._01.enumerable.eTileType;
import eean_games.tbsg._01.enumerable.eUnitAttributeType;
import eean_games.tbsg._01.event_log.ActionLog_Attack;
import eean_games.tbsg._01.event_log.ActionLog_Move;
import eean_games.tbsg._01.event_log.ActionLog_TileTargetingSkill;
import eean_games.tbsg._01.event_log.ActionLog_UnitTargetingSkill;
import eean_games.tbsg._01.event_log.EffectTrialLog_DamageEffect;
import eean_games.tbsg._01.event_log.EffectTrialLog_HealEffect;
import eean_games.tbsg._01.event_log.EffectTrialLog_MovementEffect;
import eean_games.tbsg._01.event_log.EffectTrialLog_StatusEffectAttachmentEffect;
import eean_games.tbsg._01.event_log.EventLog;
import eean_games.tbsg._01.event_log.StatusEffectLog_HPModification;
import eean_games.tbsg._01.event_log.TurnChangeEventLog;
import eean_games.tbsg._01.Field;
import eean_games.tbsg._01.player.PlayerOnBoard;
import eean_games.tbsg._01.skill.ActiveSkill;
import eean_games.tbsg._01.skill.CostRequiringSkill;
import eean_games.tbsg._01.skill.Skill;
import eean_games.tbsg._01.skill.UltimateSkill;
import eean_games.tbsg._01.status_effect.BackgroundStatusEffectData;
import eean_games.tbsg._01.status_effect.BuffStatusEffect;
import eean_games.tbsg._01.status_effect.BuffStatusEffectData;
import eean_games.tbsg._01.status_effect.DamageStatusEffect;
import eean_games.tbsg._01.status_effect.DamageStatusEffectData;
import eean_games.tbsg._01.status_effect.DebuffStatusEffect;
import eean_games.tbsg._01.status_effect.DebuffStatusEffectData;
import eean_games.tbsg._01.status_effect.ForegroundStatusEffect;
import eean_games.tbsg._01.status_effect.ForegroundStatusEffectData;
import eean_games.tbsg._01.status_effect.HealStatusEffect;
import eean_games.tbsg._01.status_effect.HealStatusEffectData;
import eean_games.tbsg._01.status_effect.StatusEffect;
import eean_games.tbsg._01.status_effect.TargetRangeModStatusEffect;
import eean_games.tbsg._01.status_effect.TargetRangeModStatusEffectData;
import eean_games.tbsg._01.unit.UnitInstance;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BattleSystemCore
{
    public BattleSystemCore(Field _field)
    {
    	isMatchEnd = false;
    	didPlayer1GetMatchEndStatus = false;
    	didPlayer2GetMatchEndStatus = false;
    	
        Field = _field;

        CurrentTurnPlayer = Field.getPlayers().get(0); //Players[0] Starts the Game

        CurrentPlayerTurns = new int[] { 1, 0 };

        CurrentPhase = eGamePhase.BeginningOfMatch;

        UpdateSP(CurrentTurnPlayer);

        m_eventLogs = new CopyOnWriteArrayList<EventLog>();
    }
    
    //Public Read-only Fields
    public final Field Field;
    //End Public Read-only Fields
    
    //Getters    
    public boolean getIsMatchEnd() { return isMatchEnd; }
    public boolean getIsPlayer1Winner() { return isPlayer1Winner; }
    public boolean getCanBattleSystemInstanceBeDeleted() { return isMatchEnd && didPlayer1GetMatchEndStatus && didPlayer2GetMatchEndStatus; }
    
    public int[] getCurrentPlayerTurns() { return CurrentPlayerTurns; }
    public BigDecimal getCurrentFullTurns() 
    {
        BigDecimal result = new BigDecimal("0.0");
        for (int playerTurn : CurrentPlayerTurns)
        {
            result = result.add(BigDecimal.valueOf(playerTurn).divide(BigDecimal.valueOf(CurrentPlayerTurns.length)));
        }
        return result; //0.5 per player turn (2 Players).
    }
    
    public PlayerOnBoard getCurrentTurnPlayer() { return CurrentTurnPlayer; }
    
    public eGamePhase getCurrentPhase() { return CurrentPhase; }
    
    public List<EventLog> getEventLogs() { return Collections.unmodifiableList(new ArrayList<EventLog>(m_eventLogs)); }
    //End Getters

    //Private Fields
    private boolean isMatchEnd;
    private boolean isPlayer1Winner;
    private boolean didPlayer1GetMatchEndStatus;
    private boolean didPlayer2GetMatchEndStatus;
    
    private PlayerOnBoard CurrentTurnPlayer;

    private eGamePhase CurrentPhase;
    //End Private Fields

    //Private Read-only Fields
    private int[] CurrentPlayerTurns;
    
    private final List<EventLog> m_eventLogs;
    //End Private Read-only Fields

    //Public Methods
    public Map<_2DCoord, Boolean> GetMovableAndSelectableArea(UnitInstance _unit)
    {
        Map<_2DCoord, Boolean> targetArea = new HashMap<_2DCoord, Boolean>();

        for (_2DCoord coord : GetMovableArea(_unit))
        {
            if (_unit.OwnerInstance.Moved
                //|| _unit.moveBinded
                || Field.Board.Sockets[coord.X][coord.Y].Unit != null)
                targetArea.put(coord, false);
            else
                targetArea.put(coord, true);
        }

        return targetArea;
    }
    public List<_2DCoord> GetMovableArea(UnitInstance _unit)
    {
        List<_2DCoord> targetArea = new ArrayList<_2DCoord>();

        List<_2DCoord> relativeTargetArea = Calculator.RelativeTargetArea(_unit, true, this);

        for (_2DCoord relativeCoord : relativeTargetArea)
        {
            _2DCoord realTargetAreaCoord = Field.ToRealCoord(Field.UnitLocation(_unit), Field.RelativeCoordToCorrectDirection(_unit.OwnerInstance, relativeCoord));

            if (Field.IsCoordWithinBoard(realTargetAreaCoord))
                targetArea.add(realTargetAreaCoord);
            //else the value(s) of realTargetAreaCoord is(are) out of the board 
        }

        return targetArea;
    }

    ///<summary>
    ///The boolean value of the dictionary returned represents whether the coord is selectable.
    ///</summary>
    public Map<_2DCoord, Boolean> GetAttackTargetableAndSelectableArea(UnitInstance _attacker)
    {
    	Map<_2DCoord, Boolean> targetArea = new HashMap<_2DCoord, Boolean>();

        for (_2DCoord coord : GetAttackTargetableArea(_attacker))
        {
            UnitInstance unitAtCoordXY = Field.Board.Sockets[coord.X][coord.Y].Unit;

            if (!_attacker.OwnerInstance.Attacked
                //&& !_attacker.attackBinded
                && (unitAtCoordXY != null) ? (unitAtCoordXY.OwnerInstance != _attacker.OwnerInstance) : false)
                targetArea.put(coord, true);
            else
                targetArea.put(coord, false);
        }

        return targetArea;
    }
    public List<_2DCoord> GetAttackTargetableArea(UnitInstance _attacker)
    {
        List<_2DCoord> targetArea = new ArrayList<_2DCoord>();

        List<_2DCoord> relativeTargetArea = Calculator.RelativeTargetArea(_attacker, false, this);

        for (_2DCoord relativeCoord : relativeTargetArea)
        {
            _2DCoord realTargetAreaCoord = Field.ToRealCoord(Field.UnitLocation(_attacker), Field.RelativeCoordToCorrectDirection(_attacker.OwnerInstance, relativeCoord));

            if (Field.IsCoordWithinBoard(realTargetAreaCoord))
                targetArea.add(realTargetAreaCoord);
            //else the value(s) of realTargetAreaCoord is(are) out of the board 
        }

        return targetArea;
    }

    public Map<_2DCoord, Boolean> GetSkillTargetableAndSelectableArea(UnitInstance _skillUser, CostRequiringSkill _skill)
    {
        if (_skillUser == null || _skill == null)
            return null;

        Map<_2DCoord, Boolean> targetArea = new HashMap<_2DCoord, Boolean>();

        List<_2DCoord> realTargetArea = GetSkillTargetableArea(_skillUser, _skill);

        if (_skillUser.AreResourcesEnoughForSkillExecution(_skill))
        {
            List<_2DCoord> selectableCoords = GetEffectSelectableCoords(_skillUser, _skill, _skill.BaseInfo.getEffect(), realTargetArea);
            for (_2DCoord coord : realTargetArea)
            {
                if (selectableCoords.contains(coord))
                    targetArea.put(coord, true);
                else
                    targetArea.put(coord, false);
            }
        }
        else
            for (_2DCoord coord : realTargetArea) { targetArea.put(coord, false); }

        return targetArea;
    }
    public Map<_2DCoord, Boolean> GetSkillTargetableAndSelectableArea(UnitInstance _skillUser, UltimateSkill _skill)
    {
        if (_skillUser == null || _skill == null)
            return null;

        Map<_2DCoord, Boolean> targetArea = new HashMap<_2DCoord, Boolean>();

        List<_2DCoord> realTargetArea = GetSkillTargetableArea(_skillUser, _skill);

        List<_2DCoord> selectableCoords = GetEffectSelectableCoords(_skillUser, _skill, _skill.BaseInfo.getEffect(), realTargetArea);
        for (_2DCoord coord : realTargetArea)
        {
            if (selectableCoords.contains(coord))
                targetArea.put(coord, true);
            else
                targetArea.put(coord, false);
        }

        return targetArea;
    }
    public List<_2DCoord> GetSkillTargetableArea(UnitInstance _skillUser, ActiveSkill _skill)
    {
        List<_2DCoord> targetArea = new ArrayList<_2DCoord>();

        List<_2DCoord> relativeTargetArea = Calculator.RelativeTargetArea(_skillUser, false, this, _skill);

        for (_2DCoord relativeCoord : relativeTargetArea)
        {
            _2DCoord realTargetAreaCoord = Field.ToRealCoord(Field.UnitLocation(_skillUser), Field.RelativeCoordToCorrectDirection(_skillUser.OwnerInstance, relativeCoord));

            if (Field.IsCoordWithinBoard(realTargetAreaCoord))
                targetArea.add(realTargetAreaCoord);
        }

        return targetArea;
    }

    //Information required for UI
    private List<_2DCoord> GetEffectSelectableCoords(UnitInstance _effectUser, Skill _skill, Effect _effect, List<_2DCoord> _targetableCoords)
    {
        List<_2DCoord> candidateCoords = new ArrayList<_2DCoord>();

        try
        {
            if (_effect instanceof UnitTargetingEffect) //--------------------------------------------UnitTargetingEffect-------------------------------------
            {
                List<UnitInstance> targetPreCandidates = new ArrayList<UnitInstance>();
                UnitTargetingEffect detailedEffect = (UnitTargetingEffect)_effect;

                targetPreCandidates = FindUnitsByTargetClassification(_effectUser, detailedEffect.TargetClassification, _targetableCoords);
                for (UnitInstance targetPreCandidate : targetPreCandidates)
                {
                    if (!(_effect instanceof HealEffect && targetPreCandidate.RemainingHP >= Calculator.MaxHP(targetPreCandidate))) //If the remaining HP of the precandidate unit is not full at the same time that the _effect is heal effect
                    {
                        if (detailedEffect.getActivationCondition().IsTrue(this, _effectUser, _skill, _effect, _targetableCoords, targetPreCandidate)) //If activation conditions against the unit is true
                            candidateCoords.add(Field.UnitLocation(targetPreCandidate)); //Add coord to the list of target candidates
                    }
                }
            }
            else if (_effect instanceof TileTargetingEffect) //-----------------------------------------------TileTargetingEffects------------------------------------------
            {
                List<Socket> targetPreCandidates = Field.GetSocketsInCoords(_targetableCoords);
                TileTargetingEffect detailedEffect = (TileTargetingEffect)_effect;
                
                for(Socket targetPreCandidate : targetPreCandidates)
                {
                	if (((_effect instanceof MovementEffect) ? targetPreCandidate.Unit == null : true) //If the _effect is MovementEffect, check whether there is no unit in the socket
                		&& detailedEffect.getActivationCondition().IsTrue(this, _effectUser, _skill, _effect, _targetableCoords, targetPreCandidate)) //If activation conditions against the socket is true
                	{
                		candidateCoords.add(Field.SocketLocation(targetPreCandidate)); //Add coord to the list of target candidates
                	}
                }
            }

            return candidateCoords;
        }
        catch(Exception ex)
        {
            candidateCoords.clear();
            return candidateCoords;
        }
    }

    public boolean MoveUnit(UnitInstance _unit, _2DCoord _destination)
    {
    	_2DCoord initialLocation = Field.UnitLocation(_unit);
    	
        if (ChangeUnitLocation(_unit, _destination)) //If moved successfully
        {
            _unit.OwnerInstance.Moved = true;
            
            m_eventLogs.add(new ActionLog_Move(getCurrentFullTurns(), Field.GetUnitIndex(_unit), _unit.BaseInfo.Name, _unit.Nickname, initialLocation, _destination));
            
            return true;
        }

        return false;
    }

    //Chekck for any unexpected/wrong information before actually executing the skill.
    //No error shoud be returned if available SP and Item costs have been checked. _taretCoords must be coords included : the set of coords returned by the GetSkillTargetableAndSelectableArea() function.
    public void RequestAttack(UnitInstance _attacker, List<_2DCoord> _targetCoords)
    {
        if (!_attacker.OwnerInstance.Attacked)
        {
        	int tileIndex_attackerLocation = _2DCoordExtension.ToIndex(Field.UnitLocation(_attacker));
        	
            List<UnitInstance> targets = new ArrayList<UnitInstance>();

            for (_2DCoord _targetCoord : _targetCoords)
            {
                targets.add(Field.Board.Sockets[_targetCoord.X][_targetCoord.Y].Unit);
            }

            for (UnitInstance target : targets)
            {
            	int tileIndex_targetLocation = _2DCoordExtension.ToIndex(Field.UnitLocation(target));
                
                m_eventLogs.add(new ActionLog_Attack(getCurrentFullTurns(), Field.GetUnitIndex(_attacker), _attacker.BaseInfo.Name, _attacker.Nickname, tileIndex_attackerLocation, Field.GetUnitIndex(target), tileIndex_targetLocation));
            	
                ActiveSkill skill = SharedGameDataContainer.getBasicAttack();

                List<_2DCoord> attackTargetableArea = GetAttackTargetableArea(_attacker);
                
                ProcessStatusEffects(eEventTriggerTiming.OnActionExecuted, _attacker);
                ProcessStatusEffects(eEventTriggerTiming.OnActiveSkillExecuted, _attacker, skill, attackTargetableArea, targets);

                ExecuteEffect(_attacker, skill, skill.BaseInfo.getEffect(), attackTargetableArea, Linq.cast(targets, Object.class), target);
            }

            _attacker.OwnerInstance.Attacked = true;
        }
    }

    //Chekck for any unexpected/wrong information before actually executing the skill.
    //No error shoud be returned if available SP and Item costs have been checked. _taretCoords must be coords included : the set of coords returned by the GetSkillTargetableAndSelectableArea() function.
    //If working correctly, all targetCandidates must be eventual targets.
    public void RequestSkillUse(UnitInstance _skillUser, ActiveSkill _skill, List<_2DCoord> _targetCoords, List<_2DCoord> _secondaryTargetCoords)
    {
        if (_skillUser == null
            || _skill == null
            || _targetCoords == null
            || (_skill.BaseInfo.getEffect() instanceof ComplexTargetSelectionEffect && _secondaryTargetCoords == null))
        {
            return;
        }

        if (_skill instanceof CostRequiringSkill)
        {
            CostRequiringSkill skill = (CostRequiringSkill)_skill;
            if (!_skillUser.AreResourcesEnoughForSkillExecution(skill))
                return;
        }

        int tileIndex_skillUser = _2DCoordExtension.ToIndex(Field.UnitLocation(_skillUser));

        List<Object> targetCandidates = new ArrayList<Object>();
        List<Object> secondaryTargetCandidates = new ArrayList<Object>();
        
        if (_skill.BaseInfo.getEffect() instanceof UnitTargetingEffect) //----------------------------------UnitTargetingEffect-----------------
        {
            List<UnitInstance> tmp_targetCandidates = Field.GetUnitsInCoords(_targetCoords);
            List<UnitInstance> tmp_secondaryTargetCandidates = Field.GetUnitsInCoords(_secondaryTargetCoords);
            
            List<TargetInfo> targetsInfo = new ArrayList<TargetInfo>();
            for (UnitInstance targetCandidate : tmp_targetCandidates) { targetsInfo.add(new TargetInfo(targetCandidate.BaseInfo.Name, targetCandidate.Nickname, targetCandidate.OwnerInstance.Name)); }
            List<TargetInfo> secondaryTargetsInfo = new ArrayList<TargetInfo>();
            for (UnitInstance secondaryTargetCandidate : tmp_targetCandidates) { targetsInfo.add(new TargetInfo(secondaryTargetCandidate.BaseInfo.Name, secondaryTargetCandidate.Nickname, secondaryTargetCandidate.OwnerInstance.Name)); }

            m_eventLogs.add(new ActionLog_UnitTargetingSkill(getCurrentFullTurns(), Field.GetUnitIndex(_skillUser), _skillUser.BaseInfo.Name, _skillUser.Nickname, _skill.BaseInfo.Name, tileIndex_skillUser, _skill.BaseInfo.SkillActivationAnimationId, targetsInfo, secondaryTargetsInfo));
            targetCandidates = Linq.cast(tmp_targetCandidates, Object.class);
            secondaryTargetCandidates = Linq.cast(tmp_secondaryTargetCandidates, Object.class);
        }
        else if (_skill.BaseInfo.getEffect() instanceof TileTargetingEffect) //----------------------------------TileTargetingEffect-------------------
        {
            m_eventLogs.add(new ActionLog_TileTargetingSkill(getCurrentFullTurns(), Field.GetUnitIndex(_skillUser), _skillUser.BaseInfo.Name, _skillUser.Nickname, _skill.BaseInfo.Name, tileIndex_skillUser, _skill.BaseInfo.SkillActivationAnimationId, _targetCoords, _secondaryTargetCoords));
            targetCandidates = Linq.cast(Field.GetSocketsInCoords(_targetCoords), Object.class);
            secondaryTargetCandidates = Linq.cast(Field.GetSocketsInCoords(_secondaryTargetCoords), Object.class);
        }

        List<_2DCoord> skillTargetableArea = GetSkillTargetableArea(_skillUser, _skill);

        ProcessStatusEffects(eEventTriggerTiming.OnActionExecuted, _skillUser);
        ProcessStatusEffects(eEventTriggerTiming.OnActiveSkillExecuted, _skillUser, _skill, skillTargetableArea, targetCandidates);
        
        InitiateEffectExecution(_skillUser, _skill, _skill.BaseInfo.getEffect(), GetSkillTargetableArea(_skillUser, _skill), targetCandidates, secondaryTargetCandidates);

        if (_skill instanceof CostRequiringSkill)
            _skillUser.OwnerInstance.RemainingSP -= ((CostRequiringSkill)_skill).BaseInfo.SPCost;
    }
    
    private void InitiateEffectExecution(UnitInstance _effectUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_effectUser == null
                || _skill == null
                || _effect == null
                || _effectRange == null
                || _targets == null
                || (_skill.BaseInfo.getEffect() instanceof ComplexTargetSelectionEffect && _secondaryTargetsForComplexTargetSelectionEffect == null))
        {
        	return;
        }
        
        for (Object target : _targets)
        {
        	ExecuteEffect(_effectUser, _skill, _effect, _effectRange, _targets, target, _secondaryTargetsForComplexTargetSelectionEffect);
        }
    }

    private void ExecuteEffect(UnitInstance _effectUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target)
    {
    	ExecuteEffect(_effectUser, _skill, _effect, _effectRange, _targets, _target, null);
    }
    private void ExecuteEffect(UnitInstance _effectUser, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
        if (_effectUser == null
            || _skill == null
            || _effect == null
            || _effectRange == null
            || _target == null
            || _targets == null
            || (_skill.BaseInfo.getEffect() instanceof ComplexTargetSelectionEffect && _secondaryTargetsForComplexTargetSelectionEffect == null))
        {
            return;
        }

        boolean isActivationConditionTrue = _effect.getActivationCondition().IsTrue(this, _effectUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
        
        int timesToApply = _effect.getTimesToApply().ToValue(Integer.class, this, _effectUser, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

        _2DCoord _effectUserLocation = Field.UnitLocation(_effectUser);
        
        if (_effect instanceof UnitTargetingEffect) //------------------------------------------------------------------UnitTargetingEffects----------------------------------------------------
        {
            UnitInstance target = (UnitInstance)_target;
            List<UnitInstance> targets = Linq.cast(_targets, UnitInstance.class);
            List<UnitInstance> secondaryTargetsForComplexTargetSelectionEffect = (_secondaryTargetsForComplexTargetSelectionEffect != null) ? Linq.cast(_secondaryTargetsForComplexTargetSelectionEffect, UnitInstance.class) : null;
            
            _2DCoord targetLocation = Field.UnitLocation(target);
            int tileIndex_targetLocation = _2DCoordExtension.ToIndex(targetLocation);
            
            if (_effect instanceof UnitTargetingEffectsWrapperEffect) //--------------------UnitTargetingEffectsWrapper---------------------
            {
            	for (Effect unitTargetingEffect : _effect.getSecondaryEffects())
            	{
            		ExecuteEffect(_effectUser, _skill, unitTargetingEffect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
            	}
            }
            else if (_effect instanceof DamageEffect) //---------------------------------------------Damage--------------------------------------------
            {
                DamageEffect effect = (DamageEffect)_effect;

                if (!isActivationConditionTrue)
                	m_eventLogs.add(new EffectTrialLog_DamageEffect(getCurrentFullTurns(), null, false, false, false, Field.GetUnitIndex(target), target.BaseInfo.Name, target.Nickname, tileIndex_targetLocation, false, false, eEffectiveness.Neutral, 0, target.RemainingHP));
                else
                {
                    for (int i = 1; i <= timesToApply; i++)
                    {
                        if (Calculator.DoesSucceed(this, _effectUser, _skill, effect, _effectRange, _targets, target, _secondaryTargetsForComplexTargetSelectionEffect)) //If the effect succeeded
                        {
                        	DamageInfo damageInfo = Calculator.Damage(this, _effectUser, _effectUserLocation, _skill, effect, _effectRange, targets, target, secondaryTargetsForComplexTargetSelectionEffect);
                            DealDamage(damageInfo.damage, target);

                            m_eventLogs.add(new EffectTrialLog_DamageEffect(getCurrentFullTurns(), _effect.AnimationInfo, false, true, true, Field.GetUnitIndex(target), target.BaseInfo.Name, target.Nickname, tileIndex_targetLocation, false, damageInfo.isCritical, damageInfo.effectiveness, damageInfo.damage, target.RemainingHP));
                            
                            //Execute weakened effect against
                            int diffusionDistance = _effect.getDiffusionDistance().ToValue(Integer.class, this, _effectUser, _skill, _effect, _effectRange, _targets, target, _secondaryTargetsForComplexTargetSelectionEffect);
                            if (diffusionDistance > 0)
                            	ExecuteDiffusedDamageEffect(diffusionDistance, _effectUser, _effectUserLocation, _skill, effect, _effectRange, _targets, targets, targetLocation, _secondaryTargetsForComplexTargetSelectionEffect, secondaryTargetsForComplexTargetSelectionEffect);
                            
                            //Execute Secondary Effects
                            if (_effect.getSecondaryEffects().size() > 0)
                            {
                                for (Effect secondaryEffect : _effect.getSecondaryEffects())
                                {
                                    ExecuteEffect(_effectUser, _skill, secondaryEffect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
                                }
                            }
                        }
                        else //If the effect failed
                            m_eventLogs.add(new EffectTrialLog_DamageEffect(getCurrentFullTurns(), _effect.AnimationInfo, false, true, false, Field.GetUnitIndex(target), target.BaseInfo.Name, target.Nickname, tileIndex_targetLocation, false, false, eEffectiveness.Neutral, 0, target.RemainingHP));
                    }
                }
            }
            else if (_effect instanceof DrainEffect) //---------------------------------------------Drain(Damage + Heal)--------------------------------------------
            {
            	DrainEffect effect = (DrainEffect)_effect;

                if (!isActivationConditionTrue)
                	m_eventLogs.add(new EffectTrialLog_DamageEffect(getCurrentFullTurns(), null, false, false, false, Field.GetUnitIndex(target), target.BaseInfo.Name, target.Nickname, tileIndex_targetLocation, false, false, eEffectiveness.Neutral, 0, target.RemainingHP));
                else
                {
                    for (int i = 1; i <= timesToApply; i++)
                    {
                        if (Calculator.DoesSucceed(this, _effectUser, _skill, effect, _effectRange, _targets, target, _secondaryTargetsForComplexTargetSelectionEffect)) //If the effect succeeded
                        {
                        	DamageInfo damageInfo = Calculator.Damage(this, _effectUser, _effectUserLocation, _skill, new DamageEffect(effect), _effectRange, targets, target, secondaryTargetsForComplexTargetSelectionEffect);
                            DealDamage(damageInfo.damage, target);

                            m_eventLogs.add(new EffectTrialLog_DamageEffect(getCurrentFullTurns(), _effect.AnimationInfo, false, true, true, Field.GetUnitIndex(target), target.BaseInfo.Name, target.Nickname, tileIndex_targetLocation, false, damageInfo.isCritical, damageInfo.effectiveness, damageInfo.damage, target.RemainingHP));
                            
                            BigDecimal drainingEfficiency = effect.getDrainingEfficiency().ToValue(BigDecimal.class, this, null, null, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
                            for (UnitInstance secondaryTarget : Linq.cast(_secondaryTargetsForComplexTargetSelectionEffect, UnitInstance.class))
                            {
                            	int restoringAmount = (BigDecimalExtension.divide(BigDecimalExtension.multiply(drainingEfficiency, damageInfo.damage), _secondaryTargetsForComplexTargetSelectionEffect.size())).setScale(0, RoundingMode.FLOOR).intValue();
                            	RestoreHP(restoringAmount, secondaryTarget);
                            	
                                m_eventLogs.add(new EffectTrialLog_HealEffect(getCurrentFullTurns(), _effect.AnimationInfo, false, true, true, Field.GetUnitIndex(secondaryTarget), secondaryTarget.BaseInfo.Name, secondaryTarget.Nickname, _2DCoordExtension.ToIndex(Field.UnitLocation(secondaryTarget)), false, restoringAmount, secondaryTarget.RemainingHP));
                            }
                            
                            //Execute weakened effect against
                            int diffusionDistance = _effect.getDiffusionDistance().ToValue(Integer.class, this, _effectUser, _skill, _effect, _effectRange, _targets, target, _secondaryTargetsForComplexTargetSelectionEffect);
                            if (diffusionDistance > 0)
                            	ExecuteDiffusedDrainEffect(diffusionDistance, _effectUser, _effectUserLocation, _skill, effect, _effectRange, _targets, targets, target, targetLocation, _secondaryTargetsForComplexTargetSelectionEffect, secondaryTargetsForComplexTargetSelectionEffect);
                            
                            //Execute Secondary Effects
                            if (_effect.getSecondaryEffects().size() > 0)
                            {
                                for (Effect secondaryEffect : _effect.getSecondaryEffects())
                                {
                                    ExecuteEffect(_effectUser, _skill, secondaryEffect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
                                }
                            }
                        }
                        else //If the effect failed
                            m_eventLogs.add(new EffectTrialLog_DamageEffect(getCurrentFullTurns(), _effect.AnimationInfo, false, true, false, Field.GetUnitIndex(target), target.BaseInfo.Name, target.Nickname, tileIndex_targetLocation, false, false, eEffectiveness.Neutral, 0, target.RemainingHP));
                    }
                }
            }
            else if (_effect instanceof HealEffect) //-------------------------------------------Heal---------------------------------------------
            {
            	HealEffect effect = (HealEffect)_effect;

            	if (!isActivationConditionTrue)
                	m_eventLogs.add(new EffectTrialLog_HealEffect(getCurrentFullTurns(), null, false, false, false, Field.GetUnitIndex(target), target.BaseInfo.Name, target.Nickname, tileIndex_targetLocation, false, 0, target.RemainingHP));
            	else
            	{
            		if (Calculator.DoesSucceed(this, _effectUser, _skill, effect, _effectRange, _targets, target, _secondaryTargetsForComplexTargetSelectionEffect)) //If the effect succeeded
                    {
                        HealInfo healInfo = Calculator.HealValue(this, _effectUser, _effectUserLocation, _skill, effect, _effectRange, targets, target, secondaryTargetsForComplexTargetSelectionEffect);
                        RestoreHP(healInfo.hpAmount, target);

                        m_eventLogs.add(new EffectTrialLog_HealEffect(getCurrentFullTurns(), _effect.AnimationInfo, false, true, true, Field.GetUnitIndex(target), target.BaseInfo.Name, target.Nickname, tileIndex_targetLocation, healInfo.isCritical, healInfo.hpAmount, target.RemainingHP));
                    
                        //Execute weakened effect against
                        int diffusionDistance = _effect.getDiffusionDistance().ToValue(Integer.class, this, _effectUser, _skill, _effect, _effectRange, _targets, target, _secondaryTargetsForComplexTargetSelectionEffect);
                        if (diffusionDistance > 0)
                        	ExecuteDiffusedHealEffect(diffusionDistance, _effectUser, _effectUserLocation, _skill, effect, _effectRange, _targets, targets, targetLocation, _secondaryTargetsForComplexTargetSelectionEffect, secondaryTargetsForComplexTargetSelectionEffect);
                        
                        //Execute Secondary Effects
                        if (_effect.getSecondaryEffects().size() > 0)
                        {
                            for (Effect secondaryEffect : _effect.getSecondaryEffects())
                            {
                                ExecuteEffect(_effectUser, _skill, secondaryEffect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
                            }
                        }
                    }
                    else //If the effect failed
                        m_eventLogs.add(new EffectTrialLog_HealEffect(getCurrentFullTurns(), _effect.AnimationInfo, false, true, false, Field.GetUnitIndex(target), target.BaseInfo.Name, target.Nickname, tileIndex_targetLocation, false, 0, target.RemainingHP));
            	}
            }
            else if (_effect instanceof StatusEffectAttachmentEffect) //-------------------------------------StatusEffectAttachment--------------------------------------------
            {
            	StatusEffectAttachmentEffect effect = (StatusEffectAttachmentEffect)_effect;

            	if (!isActivationConditionTrue)
                	m_eventLogs.add(new EffectTrialLog_StatusEffectAttachmentEffect(getCurrentFullTurns(), null, false, false, false, Field.GetUnitIndex(target), target.BaseInfo.Name, target.Nickname, tileIndex_targetLocation, 0));
            	else
            	{
            		if (Calculator.DoesSucceed(this, _effectUser, _skill, effect, _effectRange, _targets, target, _secondaryTargetsForComplexTargetSelectionEffect)) //If the effect succeeded
                    {
                        StatusEffect statusEffect = null;
                        if (effect.getDataOfStatusEffectToAttach() instanceof BuffStatusEffectData)
                        {
                        	BuffStatusEffectData data = (BuffStatusEffectData)(effect.getDataOfStatusEffectToAttach());
                            statusEffect = new BuffStatusEffect(data, _effectUser, this, _effectUser, _skill, effect, _effectRange, _targets, target, _skill.Level, _secondaryTargetsForComplexTargetSelectionEffect);
                        }
                        else if (effect.getDataOfStatusEffectToAttach() instanceof DebuffStatusEffectData)
                        {
                        	DebuffStatusEffectData data = ((DebuffStatusEffectData)effect.getDataOfStatusEffectToAttach());
                            statusEffect = new DebuffStatusEffect(data, _effectUser, this, _effectUser, _skill, effect, _effectRange, _targets, target, _skill.Level, _secondaryTargetsForComplexTargetSelectionEffect);
                        }
                        else if (effect.getDataOfStatusEffectToAttach() instanceof TargetRangeModStatusEffectData)
                        {
                        	TargetRangeModStatusEffectData data = ((TargetRangeModStatusEffectData)effect.getDataOfStatusEffectToAttach());
                            statusEffect = new TargetRangeModStatusEffect(data, _effectUser, this, _effectUser, _skill, effect, _effectRange, _targets, target, _skill.Level, _secondaryTargetsForComplexTargetSelectionEffect);
                        }
                        else if (effect.getDataOfStatusEffectToAttach() instanceof DamageStatusEffectData)
                        {
                        	DamageStatusEffectData data = ((DamageStatusEffectData)effect.getDataOfStatusEffectToAttach());
                            statusEffect = new DamageStatusEffect(data, _effectUser, this, _effectUser, _skill, effect, _effectRange, _targets, target, _skill.Level, _secondaryTargetsForComplexTargetSelectionEffect);
                        }
                        else if (effect.getDataOfStatusEffectToAttach() instanceof HealStatusEffectData)
                        {
                        	HealStatusEffectData data = ((HealStatusEffectData)effect.getDataOfStatusEffectToAttach());
                            statusEffect = new HealStatusEffect(data, _effectUser, this, _effectUser, _skill, effect, _effectRange, _targets, target, _skill.Level, _secondaryTargetsForComplexTargetSelectionEffect);
                        }
                        
                        AttachEffect(statusEffect, target);

                        m_eventLogs.add(new EffectTrialLog_StatusEffectAttachmentEffect(getCurrentFullTurns(), _effect.AnimationInfo, false, true, true, Field.GetUnitIndex(target), target.BaseInfo.Name, target.Nickname, tileIndex_targetLocation, effect.getDataOfStatusEffectToAttach().Id));
                    
                        //Execute weakened effect against
                        int diffusionDistance = _effect.getDiffusionDistance().ToValue(Integer.class, this, _effectUser, _skill, _effect, _effectRange, _targets, target, _secondaryTargetsForComplexTargetSelectionEffect);
                        if (diffusionDistance > 0)
                        	ExecuteDiffusedStatusEffectAttachmentEffect(diffusionDistance, _effectUser, _effectUserLocation, _skill, effect, _effectRange, _targets, targetLocation, _secondaryTargetsForComplexTargetSelectionEffect);
                        
                        //Execute Secondary Effects
                        if (_effect.getSecondaryEffects().size() > 0)
                        {
                            for (Effect secondaryEffect : _effect.getSecondaryEffects())
                            {
                                ExecuteEffect(_effectUser, _skill, secondaryEffect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);
                            }
                        }
                    }
                    else //If the effect failed
                        m_eventLogs.add(new EffectTrialLog_StatusEffectAttachmentEffect(getCurrentFullTurns(), _effect.AnimationInfo, false, true, false, Field.GetUnitIndex(target), target.BaseInfo.Name, target.Nickname, tileIndex_targetLocation, 0));
            	}
            }
        }
        else if (_effect instanceof TileTargetingEffect) //-------------------------------------------TileTargetingEffects------------------------------------------------
        {
        	Socket target = (Socket)_target;

        	if (_effect instanceof MovementEffect) //----------------------------------------Movement--------------------------------------
        	{
				MovementEffect effect = (MovementEffect)_effect;
        		
				if (!isActivationConditionTrue)
                	m_eventLogs.add(new EffectTrialLog_MovementEffect(getCurrentFullTurns(), null, false, null, null));
				else
				{
	        		_2DCoord destination = Field.SocketLocation(target);
	        		
					for (int i = 1; i <= timesToApply; i++)
	        		{
	        			if (IntegerExtension.isOdd(i))
	        			{
	        				MoveUnit(_effectUser, destination);
	                		m_eventLogs.add(new EffectTrialLog_MovementEffect(getCurrentFullTurns(), (MovementAnimationInfo)(effect.AnimationInfo), true, destination, _effectUserLocation));
	        			}
	        			else
	        			{
	        				MoveUnit(_effectUser, _effectUserLocation);
	                		m_eventLogs.add(new EffectTrialLog_MovementEffect(getCurrentFullTurns(), (MovementAnimationInfo)(effect.AnimationInfo), true, _effectUserLocation, destination));
	        			}
	        			
	                    if (_effect.getSecondaryEffects().size() > 0)
	                    {
	                    	List<_2DCoord> steppedCoords = Calculator.coordsBetween(_effectUserLocation, destination);
	                    	steppedCoords.add(_effectUserLocation);
	                    	steppedCoords.add(destination);
	                    	
	                        for (Effect secondaryEffect : _effect.getSecondaryEffects())
	                        {
	                        	List<Object> targetsInSteppedCoords = new ArrayList<Object>();
	                        	if (secondaryEffect instanceof UnitTargetingEffect)
	                        	{
	                        		List<UnitInstance> unitsInSteppedCoords = FindUnitsByTargetClassification(_effectUser, ((UnitTargetingEffect)secondaryEffect).TargetClassification, steppedCoords);
	                        		for (UnitInstance unitInSteppedCoords : unitsInSteppedCoords)
	                        		{
	                        			if (secondaryEffect.getActivationCondition().IsTrue(this, _effectUser, _skill, secondaryEffect, _effectRange, Linq.cast(unitsInSteppedCoords, Object.class), unitInSteppedCoords))
	                        					targetsInSteppedCoords.add(unitInSteppedCoords);
	                        		}
	                        	}
	                        	else if (secondaryEffect instanceof TileTargetingEffect)
	                        	{
	                        		List<Socket> socketsInSteppedCoords = Field.GetSocketsInCoords(steppedCoords);
	                        		for (Socket socketInSteppedCoords : socketsInSteppedCoords)
	                        		{
	                        			if (secondaryEffect.getActivationCondition().IsTrue(this, _effectUser, _skill, secondaryEffect, _effectRange, Linq.cast(socketsInSteppedCoords, Object.class), socketInSteppedCoords))
	                    					targetsInSteppedCoords.add(socketInSteppedCoords);
	                        		}
	                        	}
	                        	
	                        	for (Object targetInSteppedCoords : targetsInSteppedCoords)
	                        	{
	                        		ExecuteEffect(_effectUser, _skill, secondaryEffect, _effectRange, targetsInSteppedCoords, targetInSteppedCoords);
	                        	}
	                        }
	                    }
	        		}
				}
        	}
        }
    }

    private void ExecuteDiffusedDamageEffect(int _diffusionDistance, UnitInstance _effectUser, _2DCoord _effectUserLocation, ActiveSkill _skill, DamageEffect _effect, List<_2DCoord> _effectRange, List<Object> _targetsAsObjects, List<UnitInstance> _targetsAsUnitInstances, _2DCoord _targetLocation, List<Object> _secondaryTargetsAsObjects, List<UnitInstance> _secondaryTargetsAsUnitInstances)
    {
    	for (int distance = _diffusionDistance; distance >= 1; distance--)
    	{
    		List<_2DCoord> coordsInDistance = Calculator.coordsInDistance(_targetLocation, distance);
    		for (UnitInstance targetCandidate : Field.GetUnitsInCoords(coordsInDistance))
    		{
    			int tileIndex_targetLocation = _2DCoordExtension.ToIndex(Field.UnitLocation(targetCandidate));
    			
    			if (_effect.getActivationCondition().IsTrue(this, _effectUser, _skill, _effect, _effectRange, _targetsAsObjects, targetCandidate, _secondaryTargetsAsObjects))
    			{
    				if (Calculator.DoesSucceed(this, _effectUser, _skill, _effect, _effectRange, _targetsAsObjects, targetCandidate, _secondaryTargetsAsObjects)) //If the effect succeeded
    				{
    					DamageInfo damageInfo = Calculator.Damage(this, _effectUser, _effectUserLocation, _skill, _effect, _effectRange, _targetsAsUnitInstances, targetCandidate, _secondaryTargetsAsUnitInstances, distance);
                        DealDamage(damageInfo.damage, targetCandidate);

                        m_eventLogs.add(new EffectTrialLog_DamageEffect(getCurrentFullTurns(), _effect.AnimationInfo, true, true, true, Field.GetUnitIndex(targetCandidate), targetCandidate.BaseInfo.Name, targetCandidate.Nickname, tileIndex_targetLocation, false, damageInfo.isCritical, damageInfo.effectiveness, damageInfo.damage, targetCandidate.RemainingHP));
    				}
    				else //If the effect failed
    					m_eventLogs.add(new EffectTrialLog_DamageEffect(getCurrentFullTurns(), _effect.AnimationInfo, true, true, false, Field.GetUnitIndex(targetCandidate), targetCandidate.BaseInfo.Name, targetCandidate.Nickname, tileIndex_targetLocation, false, false, eEffectiveness.Neutral, 0, targetCandidate.RemainingHP));
    			}
    			else //If the effect did not meet the activation requirements
    				m_eventLogs.add(new EffectTrialLog_DamageEffect(getCurrentFullTurns(), null, true, false, false, Field.GetUnitIndex(targetCandidate), targetCandidate.BaseInfo.Name, targetCandidate.Nickname, tileIndex_targetLocation, false, false, eEffectiveness.Neutral, 0, targetCandidate.RemainingHP));
    		}
    	}
    }
    
    private void ExecuteDiffusedDrainEffect(int _diffusionDistance, UnitInstance _effectUser, _2DCoord _effectUserLocation, ActiveSkill _skill, DrainEffect _effect, List<_2DCoord> _effectRange, List<Object> _targetsAsObjects, List<UnitInstance> _targetsAsUnitInstances, UnitInstance _target, _2DCoord _targetLocation, List<Object> _secondaryTargetsAsObjects, List<UnitInstance> _secondaryTargetsAsUnitInstances)
    {
    	for (int distance = _diffusionDistance; distance >= 1; distance--)
    	{
    		List<_2DCoord> coordsInDistance = Calculator.coordsInDistance(_targetLocation, distance);
    		for (UnitInstance targetCandidate : Field.GetUnitsInCoords(coordsInDistance))
    		{
    			int tileIndex_targetLocation = _2DCoordExtension.ToIndex(Field.UnitLocation(targetCandidate));
    			
    			if (_effect.getActivationCondition().IsTrue(this, _effectUser, _skill, _effect, _effectRange, _targetsAsObjects, targetCandidate, _secondaryTargetsAsObjects))
    			{
    				if (Calculator.DoesSucceed(this, _effectUser, _skill, _effect, _effectRange, _targetsAsObjects, targetCandidate, _secondaryTargetsAsObjects)) //If the effect succeeded
    				{
    					DamageInfo damageInfo = Calculator.Damage(this, _effectUser, _effectUserLocation, _skill, new DamageEffect(_effect), _effectRange, _targetsAsUnitInstances, targetCandidate, _secondaryTargetsAsUnitInstances, distance);
                        DealDamage(damageInfo.damage, targetCandidate);
                        
                        m_eventLogs.add(new EffectTrialLog_DamageEffect(getCurrentFullTurns(), _effect.AnimationInfo, true, true, true, Field.GetUnitIndex(targetCandidate), targetCandidate.BaseInfo.Name, targetCandidate.Nickname, tileIndex_targetLocation, false, damageInfo.isCritical, damageInfo.effectiveness, damageInfo.damage, targetCandidate.RemainingHP));
                        
                        BigDecimal drainingEfficiency = _effect.getDrainingEfficiency().ToValue(BigDecimal.class, this, null, null, _skill, _effect, _effectRange, _targetsAsObjects, _target, _secondaryTargetsAsObjects);
                        for (UnitInstance secondaryTarget : _secondaryTargetsAsUnitInstances)
                        {
                        	int restoringAmount = (BigDecimalExtension.divide(BigDecimalExtension.multiply(drainingEfficiency, damageInfo.damage), _secondaryTargetsAsUnitInstances.size())).setScale(0, RoundingMode.FLOOR).intValue();
                        	RestoreHP(restoringAmount, secondaryTarget);
                        	
                            m_eventLogs.add(new EffectTrialLog_HealEffect(getCurrentFullTurns(), _effect.AnimationInfo, true, true, true, Field.GetUnitIndex(secondaryTarget), secondaryTarget.BaseInfo.Name, secondaryTarget.Nickname, _2DCoordExtension.ToIndex(Field.UnitLocation(secondaryTarget)), false, restoringAmount, secondaryTarget.RemainingHP));
                        }
    				}
    				else //If the effect failed
    					m_eventLogs.add(new EffectTrialLog_DamageEffect(getCurrentFullTurns(), _effect.AnimationInfo, true, true, false, Field.GetUnitIndex(targetCandidate), targetCandidate.BaseInfo.Name, targetCandidate.Nickname, tileIndex_targetLocation, false, false, eEffectiveness.Neutral, 0, targetCandidate.RemainingHP));
    			}
    			else //If the effect did not meet the activation requirements
    				m_eventLogs.add(new EffectTrialLog_DamageEffect(getCurrentFullTurns(), null, true, false, false, Field.GetUnitIndex(targetCandidate), targetCandidate.BaseInfo.Name, targetCandidate.Nickname, tileIndex_targetLocation, false, false, eEffectiveness.Neutral, 0, targetCandidate.RemainingHP));
    		}
    	}
    }
    
    private void ExecuteDiffusedHealEffect(int _diffusionDistance, UnitInstance _effectUser, _2DCoord _effectUserLocation, ActiveSkill _skill, HealEffect _effect, List<_2DCoord> _effectRange, List<Object> _targetsAsObjects, List<UnitInstance> _targetsAsUnitInstances, _2DCoord _targetLocation, List<Object> _secondaryTargetsAsObjects, List<UnitInstance> _secondaryTargetsAsUnitInstances)
    {
    	for (int distance = _diffusionDistance; distance >= 1; distance--)
    	{
    		List<_2DCoord> coordsInDistance = Calculator.coordsInDistance(_targetLocation, distance);
    		for (UnitInstance targetCandidate : Field.GetUnitsInCoords(coordsInDistance))
    		{
    			int tileIndex_targetLocation = _2DCoordExtension.ToIndex(Field.UnitLocation(targetCandidate));
    			
    			if (_effect.getActivationCondition().IsTrue(this, _effectUser, _skill, _effect, _effectRange, _targetsAsObjects, targetCandidate, _secondaryTargetsAsObjects))
    			{
    				if (Calculator.DoesSucceed(this, _effectUser, _skill, _effect, _effectRange, _targetsAsObjects, targetCandidate, _secondaryTargetsAsObjects)) //If the effect succeeded
    				{
    					HealInfo healInfo = Calculator.HealValue(this, _effectUser, _effectUserLocation, _skill, _effect, _effectRange, _targetsAsUnitInstances, targetCandidate, _secondaryTargetsAsUnitInstances, distance);
                        RestoreHP(healInfo.hpAmount, targetCandidate);

                        m_eventLogs.add(new EffectTrialLog_HealEffect(getCurrentFullTurns(), _effect.AnimationInfo, true, true, true, Field.GetUnitIndex(targetCandidate), targetCandidate.BaseInfo.Name, targetCandidate.Nickname, tileIndex_targetLocation, healInfo.isCritical, healInfo.hpAmount, targetCandidate.RemainingHP));
    				}
    				else //If the effect failed
    					m_eventLogs.add(new EffectTrialLog_HealEffect(getCurrentFullTurns(), _effect.AnimationInfo, true, true, false, Field.GetUnitIndex(targetCandidate), targetCandidate.BaseInfo.Name, targetCandidate.Nickname, tileIndex_targetLocation, false, 0, targetCandidate.RemainingHP));
    			}
    			else //If the effect did not meet the activation requirements
    				m_eventLogs.add(new EffectTrialLog_HealEffect(getCurrentFullTurns(), null, true, false, false, Field.GetUnitIndex(targetCandidate), targetCandidate.BaseInfo.Name, targetCandidate.Nickname, tileIndex_targetLocation, false, 0, targetCandidate.RemainingHP));
    		}
    	}
    }
    
    private void ExecuteDiffusedStatusEffectAttachmentEffect(int _diffusionDistance, UnitInstance _effectUser, _2DCoord _effectUserLocation, ActiveSkill _skill, StatusEffectAttachmentEffect _effect, List<_2DCoord> _effectRange, List<Object> _targetsAsObjects, _2DCoord _targetLocation, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	for (int distance = _diffusionDistance; distance >= 1; distance--)
    	{
    		List<_2DCoord> coordsInDistance = Calculator.coordsInDistance(_targetLocation, distance);
    		for (UnitInstance targetCandidate : Field.GetUnitsInCoords(coordsInDistance))
    		{
    			int tileIndex_targetLocation = _2DCoordExtension.ToIndex(Field.UnitLocation(targetCandidate));
    			
    			if (_effect.getActivationCondition().IsTrue(this, _effectUser, _skill, _effect, _effectRange, _targetsAsObjects, targetCandidate, _secondaryTargetsForComplexTargetSelectionEffect))
    			{
    				if (Calculator.DoesSucceed(this, _effectUser, _skill, _effect, _effectRange, _targetsAsObjects, targetCandidate, _secondaryTargetsForComplexTargetSelectionEffect)) //If the effect succeeded
    				{
                        StatusEffect statusEffect = null;
                        if (_effect.getDataOfStatusEffectToAttach() instanceof BuffStatusEffectData)
                        {
                        	BuffStatusEffectData data = (BuffStatusEffectData)(_effect.getDataOfStatusEffectToAttach());
                            statusEffect = new BuffStatusEffect(data, _effectUser, this, _effectUser, _skill, _effect, _effectRange, _targetsAsObjects, targetCandidate, _skill.Level, _secondaryTargetsForComplexTargetSelectionEffect);
                        }
                        else if (_effect.getDataOfStatusEffectToAttach() instanceof DebuffStatusEffectData)
                        {
                        	DebuffStatusEffectData data = ((DebuffStatusEffectData)_effect.getDataOfStatusEffectToAttach());
                            statusEffect = new DebuffStatusEffect(data, _effectUser, this, _effectUser, _skill, _effect, _effectRange, _targetsAsObjects, targetCandidate, _skill.Level, _secondaryTargetsForComplexTargetSelectionEffect);
                        }
                        else if (_effect.getDataOfStatusEffectToAttach() instanceof TargetRangeModStatusEffectData)
                        {
                        	TargetRangeModStatusEffectData data = ((TargetRangeModStatusEffectData)_effect.getDataOfStatusEffectToAttach());
                            statusEffect = new TargetRangeModStatusEffect(data, _effectUser, this, _effectUser, _skill, _effect, _effectRange, _targetsAsObjects, targetCandidate, _skill.Level, _secondaryTargetsForComplexTargetSelectionEffect);
                        }
                        else if (_effect.getDataOfStatusEffectToAttach() instanceof DamageStatusEffectData)
                        {
                        	DamageStatusEffectData data = ((DamageStatusEffectData)_effect.getDataOfStatusEffectToAttach());
                            statusEffect = new DamageStatusEffect(data, _effectUser, this, _effectUser, _skill, _effect, _effectRange, _targetsAsObjects, targetCandidate, _skill.Level, _secondaryTargetsForComplexTargetSelectionEffect);
                        }
                        else if (_effect.getDataOfStatusEffectToAttach() instanceof HealStatusEffectData)
                        {
                        	HealStatusEffectData data = ((HealStatusEffectData)_effect.getDataOfStatusEffectToAttach());
                            statusEffect = new HealStatusEffect(data, _effectUser, this, _effectUser, _skill, _effect, _effectRange, _targetsAsObjects, targetCandidate, _skill.Level, _secondaryTargetsForComplexTargetSelectionEffect);
                        }
                        
                        AttachEffect(statusEffect, targetCandidate);

                        m_eventLogs.add(new EffectTrialLog_StatusEffectAttachmentEffect(getCurrentFullTurns(), _effect.AnimationInfo, true, true, true, Field.GetUnitIndex(targetCandidate), targetCandidate.BaseInfo.Name, targetCandidate.Nickname, tileIndex_targetLocation, _effect.getDataOfStatusEffectToAttach().Id));
    				}
    				else //If the effect failed
    					m_eventLogs.add(new EffectTrialLog_StatusEffectAttachmentEffect(getCurrentFullTurns(), _effect.AnimationInfo, true, true, false, Field.GetUnitIndex(targetCandidate), targetCandidate.BaseInfo.Name, targetCandidate.Nickname, tileIndex_targetLocation, 0));
    			}
    			else //If the effect did not meet the activation requirements
    				m_eventLogs.add(new EffectTrialLog_StatusEffectAttachmentEffect(getCurrentFullTurns(), null, true, false, false, Field.GetUnitIndex(targetCandidate), targetCandidate.BaseInfo.Name, targetCandidate.Nickname, tileIndex_targetLocation, 0));
    		}
    	}
    }
    
    private boolean DoesActivationTurnClassificationMatch(PlayerOnBoard _player, eActivationTurnClassification _actionTurnClassification)
    {
        switch (_actionTurnClassification)
        {
            default: //case eBackgroundActivationTiming.Always
                return true;
            case OnMyTurn:
                return _player == CurrentTurnPlayer;
            case OnOpponentTurn:
                return _player != CurrentTurnPlayer;
        }
    }

    private boolean DoesEventTriggerTimingMatchGamePhase(PlayerOnBoard _player, eEventTriggerTiming _eventTriggerTiming)
    {
        switch (_eventTriggerTiming)
        {
            case OnStatusEffectActivated:
                return true; //It can be triggered at any game phase

            case BeginningOfMatch:
                return CurrentPhase == eGamePhase.BeginningOfMatch;

            case BeginningOfTurn:
                return CurrentPhase == eGamePhase.BeginningOfTurn;

            default: //Any value other than the ones listed above or below is considered as DuringTurn
                return CurrentPhase == eGamePhase.DuringTurn;

            case EndOfTurn:
                return CurrentPhase == eGamePhase.EndOfTurn;
        }
    }

    @SuppressWarnings("unchecked")
	private void ExecuteStatusEffectsIfExecutable(eEventTriggerTiming _eventTriggerTiming, UnitInstance _effectHolder, ForegroundStatusEffect _statusEffect, Object... _params)
    {
        try
        {
            switch (_eventTriggerTiming)
            {
                case OnActionExecuted:
                    {
                        UnitInstance actor = (UnitInstance)_params[0];

                        if (_statusEffect.getActivationCondition().IsTrue(this, _effectHolder, _statusEffect, actor))
                            ExecuteStatusEffect(_effectHolder, _statusEffect, actor);
                    }
                    break;

                case OnMoved:
                    {
                        UnitInstance actor = (UnitInstance)_params[0];
                        int actor_previousLocationTileIndex = (int)_params[1];

                        if (_statusEffect.getActivationCondition().IsTrue(this, _effectHolder, _statusEffect, actor, actor_previousLocationTileIndex))
                            ExecuteStatusEffect(_effectHolder, _statusEffect, actor, actor_previousLocationTileIndex);
                    }
                    break;

                case OnAttackExecuted:
                case OnTargetedByAction:
                case OnTargetedByAttack:
                    {
                        UnitInstance actor = (UnitInstance)_params[0];
                        ActiveSkill skill = (ActiveSkill)_params[1];
                        List<_2DCoord> effectRange = (List<_2DCoord>)_params[2];
                        List<Object> targets = (List<Object>)_params[3];

                        if (_statusEffect.getActivationCondition().IsTrue(this, _effectHolder, _statusEffect, actor, skill, effectRange, targets))
                            ExecuteStatusEffect(_effectHolder, _statusEffect, actor, skill, effectRange, targets);
                    }
                    break;

                case OnActiveSkillExecuted:
                case OnItemUsed:
                    {
                        UnitInstance actor = (UnitInstance)_params[0];
                        ActiveSkill skill = (ActiveSkill)_params[1];
                        List<_2DCoord> effectRange = (List<_2DCoord>)_params[2];
                        List<Object> targets = (List<Object>)_params[3];
                        List<Object> secondaryTargetsForComplexTargetSelectionEffect = (List<Object>)_params[6];

                        if (_statusEffect.getActivationCondition().IsTrue(this, _effectHolder, _statusEffect, actor, skill, effectRange, targets, secondaryTargetsForComplexTargetSelectionEffect))
                            ExecuteStatusEffect(_effectHolder, _statusEffect, actor, skill, effectRange, targets, secondaryTargetsForComplexTargetSelectionEffect);
                    }
                    break;

                case OnTargetedBySkill:
                case OnTargetedByItemSkill:
                    {
                        UnitInstance actor = (UnitInstance)_params[0];
                        ActiveSkill skill = (ActiveSkill)_params[1];
                        List<_2DCoord> effectRange = (List<_2DCoord>)_params[2];
                        List<Object> targets = (List<Object>)_params[3];
                        Object target = _params[5];
                        List<Object> secondaryTargetsForComplexTargetSelectionEffect = (List<Object>)_params[6];

                        if (_statusEffect.getActivationCondition().IsTrue(this, _effectHolder, _statusEffect, actor, skill, effectRange, targets, target, secondaryTargetsForComplexTargetSelectionEffect))
                            ExecuteStatusEffect(_effectHolder, _statusEffect, actor, skill, effectRange, targets, target, secondaryTargetsForComplexTargetSelectionEffect);
                    }
                    break;

                case OnEffectSuccess:
                case OnHitByEffect:
                    {
                        UnitInstance actor = (UnitInstance)_params[0];
                        ActiveSkill skill = (ActiveSkill)_params[1];
                        Effect effect = (Effect)_params[2];
                        List<_2DCoord> effectRange = (List<_2DCoord>)_params[3];
                        List<Object> targets = (List<Object>)_params[4];
                        Object target = _params[5];
                        List<Object> secondaryTargetsForComplexTargetSelectionEffect = (List<Object>)_params[6];
                        int target_previousRemainingHP = (int)(_params[7]);
                        int target_previousLocationTileIndex = (int)(_params[8]);
                        List<StatusEffect> statusEffects = (List<StatusEffect>)_params[9];
                        eTileType previousTileType = (eTileType)_params[10];

                        if (_statusEffect.getActivationCondition().IsTrue(this, _effectHolder, _statusEffect, actor, skill, effect, effectRange, targets, target, secondaryTargetsForComplexTargetSelectionEffect, target_previousRemainingHP, target_previousLocationTileIndex, statusEffects, previousTileType))
                            ExecuteStatusEffect(_effectHolder, _statusEffect, actor, skill, effect, effectRange, targets, target, secondaryTargetsForComplexTargetSelectionEffect, target_previousRemainingHP, target_previousLocationTileIndex, statusEffects, previousTileType);
                    }
                    break;

                case OnEffectFailure:
                case OnEvadedEffect:
                    {
                        UnitInstance actor = (UnitInstance)_params[0];
                        ActiveSkill skill = (ActiveSkill)_params[1];
                        Effect effect = (Effect)_params[2];
                        List<_2DCoord> effectRange = (List<_2DCoord>)_params[3];
                        List<Object> targets = (List<Object>)_params[4];
                        Object target = _params[5];
                        List<Object> secondaryTargetsForComplexTargetSelectionEffect = (List<Object>)_params[6];

                        if (_statusEffect.getActivationCondition().IsTrue(this, _effectHolder, _statusEffect, actor, skill, effect, effectRange, targets, target, secondaryTargetsForComplexTargetSelectionEffect))
                            ExecuteStatusEffect(_effectHolder, _statusEffect, actor, skill, effect, effectRange, targets, target, secondaryTargetsForComplexTargetSelectionEffect);
                    }
                    break;

                case OnStatusEffectActivated:
                    {
                        UnitInstance effectHolderOfActivatedEffect = (UnitInstance)_params[0];
                        StatusEffect activatedStatusEffect = (StatusEffect)_params[1]; //The StatusEffect that triggered OnStatusEffectActivated

                        if (_statusEffect.getActivationCondition().IsTrue(this, _effectHolder, _statusEffect, effectHolderOfActivatedEffect, activatedStatusEffect))
                            ExecuteStatusEffect(_effectHolder, _statusEffect, effectHolderOfActivatedEffect, activatedStatusEffect);
                    }
                    break;

                default: //BeginningOfMatch, BeginningOfTurn, and EndOfTurn
                    {
                        if (_statusEffect.getActivationCondition().IsTrue(this, _effectHolder, _statusEffect))
                            ExecuteStatusEffect(_effectHolder, _statusEffect);
                    }
                    break;
            }
        }
        catch(Exception ex)
        {

        }
    }

    private void ExecuteStatusEffect(UnitInstance _effectHolder, ForegroundStatusEffect _statusEffect)
    {
    	ExecuteStatusEffect(_effectHolder, _statusEffect, null, null, null, null, null, null, null, 0, 0, null, null, null, eTileType.values()[0]);
    }
    private void ExecuteStatusEffect(UnitInstance _effectHolder, ForegroundStatusEffect _statusEffect, UnitInstance _actor)
    {
    	ExecuteStatusEffect(null, _statusEffect, _actor, null, null, null, null, null, null, 0, 0, null, null, null, eTileType.values()[0]);
    }
    private void ExecuteStatusEffect(UnitInstance _effectHolder, ForegroundStatusEffect _statusEffect, UnitInstance _effectHolderOfActivatedEffect, StatusEffect _statusEffectActivated)
    {
    	ExecuteStatusEffect(_effectHolder, _statusEffect, null, null, null, null, null, null, null, 0, 0, null, _effectHolderOfActivatedEffect, _statusEffectActivated, eTileType.values()[0]);
    }
    private void ExecuteStatusEffect(UnitInstance _effectHolder, ForegroundStatusEffect _statusEffect, UnitInstance _actor, int _targetPreviousLocationTileIndex)
    {
    	ExecuteStatusEffect(_effectHolder, _statusEffect, _actor, null, null, null, null, null, null, 0, _targetPreviousLocationTileIndex, null, null, null, eTileType.values()[0]);
    }
    private void ExecuteStatusEffect(UnitInstance _effectHolder, ForegroundStatusEffect _statusEffect, UnitInstance _actor, ActiveSkill _skill, List<_2DCoord> _effectRange, List<Object> _targets)
    {
    	ExecuteStatusEffect(_effectHolder, _statusEffect, _actor, _skill, null, _effectRange, _targets, null, null, 0, 0, null, null, null, eTileType.values()[0]);
    }
    private void ExecuteStatusEffect(UnitInstance _effectHolder, ForegroundStatusEffect _statusEffect, UnitInstance _actor, ActiveSkill _skill, List<_2DCoord> _effectRange, List<Object> _targets, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	ExecuteStatusEffect(_effectHolder, _statusEffect, _actor, _skill, null, _effectRange, _targets, null, _secondaryTargetsForComplexTargetSelectionEffect, 0, 0, null, null, null, eTileType.values()[0]);
    }
    private void ExecuteStatusEffect(UnitInstance _effectHolder, ForegroundStatusEffect _statusEffect, UnitInstance _actor, ActiveSkill _skill, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	ExecuteStatusEffect(_effectHolder, _statusEffect, _actor, _skill, null, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, 0, 0, null, null, null, eTileType.values()[0]);
    }
    private void ExecuteStatusEffect(UnitInstance _effectHolder, ForegroundStatusEffect _statusEffect, UnitInstance _actor, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect)
    {
    	ExecuteStatusEffect(_effectHolder, _statusEffect, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, 0, 0, null, null, null, eTileType.values()[0]);
    }
    private void ExecuteStatusEffect(UnitInstance _effectHolder, ForegroundStatusEffect _statusEffect, UnitInstance _actor, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect, int _targetPreviousHP, int _targetPreviousLocationTileIndex, List<StatusEffect> _statusEffects, eTileType _previousTileType)
    {
    	ExecuteStatusEffect(_effectHolder, _statusEffect, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, _targetPreviousHP, _targetPreviousLocationTileIndex, _statusEffects, null, null, eTileType.values()[0]);
    }
    private void ExecuteStatusEffect(UnitInstance _effectHolder, ForegroundStatusEffect _statusEffect, UnitInstance _actor, ActiveSkill _skill, Effect _effect, List<_2DCoord> _effectRange, List<Object> _targets, Object _target, List<Object> _secondaryTargetsForComplexTargetSelectionEffect, int _targetPreviousHP, int _targetPreviousLocationTileIndex, List<StatusEffect> _statusEffects, UnitInstance _effectHolderOfActivatedEffect, StatusEffect _statusEffectActivated, eTileType _previousTileType)
    {
        if (_statusEffect instanceof DamageStatusEffect)
        {
            DamageStatusEffect damageStatusEffect = (DamageStatusEffect)_statusEffect;

            BigDecimal damage = damageStatusEffect.getDamage().ToValue(BigDecimal.class, this, _effectHolder, damageStatusEffect, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, _targetPreviousHP, _targetPreviousLocationTileIndex, _statusEffects, _effectHolderOfActivatedEffect, _statusEffectActivated, _previousTileType);

            damage = Calculator.ApplyBuffAndDebuffStatusEffects_Simple(damage, this, _effectHolder, eStatusType.DamageResistance, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

            DealDamage(damage.intValue(), _effectHolder);

            m_eventLogs.add(new StatusEffectLog_HPModification(getCurrentFullTurns(), Field.GetUnitIndex(_effectHolder), _effectHolder.BaseInfo.Name, _effectHolder.Nickname, false, damage.intValue(), _effectHolder.RemainingHP));
        } 
        else if (_statusEffect instanceof HealStatusEffect)
        {
            HealStatusEffect healStatusEffect = (HealStatusEffect)_statusEffect;

            BigDecimal hpAmount = healStatusEffect.getHPAmount().ToValue(BigDecimal.class, this, _effectHolder, healStatusEffect, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect, _targetPreviousHP, _targetPreviousLocationTileIndex, _statusEffects, _effectHolderOfActivatedEffect, _statusEffectActivated, _previousTileType);

            hpAmount = Calculator.ApplyBuffAndDebuffStatusEffects_Simple(hpAmount, this, _effectHolder, eStatusType.FixedHeal_Self, _actor, _skill, _effect, _effectRange, _targets, _target, _secondaryTargetsForComplexTargetSelectionEffect);

            RestoreHP(hpAmount.intValue(), _effectHolder);

            m_eventLogs.add(new StatusEffectLog_HPModification(getCurrentFullTurns(), Field.GetUnitIndex(_effectHolder), _effectHolder.BaseInfo.Name, _effectHolder.Nickname, true, hpAmount.intValue(), _effectHolder.RemainingHP));
        }
        
        if (_statusEffect.getDuration().ActivationTimes > 0)
        	_statusEffect.getDuration().ActivationTimes--; //Subtract one from the remaining activation times
    }

    public boolean DoesStatusEffectActivationPhaseMatch(BackgroundStatusEffectData _statusEffectData, UnitInstance _effectHolder)
    {
        return DoesActivationTurnClassificationMatch(_effectHolder.OwnerInstance, _statusEffectData.ActivationTurnClassification);
    }
    public boolean DoesStatusEffectActivationPhaseMatch(ForegroundStatusEffectData _statusEffectData, UnitInstance _effectHolder, eEventTriggerTiming _eventTriggerTiming)
    {
        return DoesActivationTurnClassificationMatch(_effectHolder.OwnerInstance, _statusEffectData.ActivationTurnClassification)
                && _statusEffectData.EventTriggerTiming == _eventTriggerTiming
                && DoesEventTriggerTimingMatchGamePhase(_effectHolder.OwnerInstance, _statusEffectData.EventTriggerTiming);
    }
    private boolean DoesStatusEffectActivationPhaseMatch(ForegroundStatusEffect _statusEffect, UnitInstance _effectHolder, eEventTriggerTiming _eventTriggerTiming)
    {
        return DoesActivationTurnClassificationMatch(_effectHolder.OwnerInstance, _statusEffect.ActivationTurnClassification)
                && _statusEffect.EventTriggerTiming == _eventTriggerTiming
                && DoesEventTriggerTimingMatchGamePhase(_effectHolder.OwnerInstance, _statusEffect.EventTriggerTiming);
    }

    //To be used in order to execute ForegroundStatusEffects
    private void ProcessStatusEffects(eEventTriggerTiming _eventTriggerTiming, Object... _params)
    {
        for (UnitInstance u : Field.getUnits())
        {
            if (u.getStatusEffects().size() > 0)
            {
                List<ForegroundStatusEffect> statusEffects = Linq.where(Linq.ofType(u.getStatusEffects(), ForegroundStatusEffect.class),
                														x -> DoesStatusEffectActivationPhaseMatch(x, u, _eventTriggerTiming));

                Calculator.AddPassiveSkillForegroundStatusEffects(statusEffects, this, u, _eventTriggerTiming);
                Calculator.AddEquipmentForegroundStatusEffects(statusEffects, this, u, _eventTriggerTiming);

                for (ForegroundStatusEffect se : statusEffects)
                {
                    ExecuteStatusEffectsIfExecutable(_eventTriggerTiming, u, se, _params);
                }

                RemoveExpiredStatusEffect(u);
            }
        }
    }

    public void UpdateStatusEffects()
    {
        for (UnitInstance u : Field.getUnits())
        {
            if (u.getStatusEffects().size() > 0)
            {
                DecreaseStatusEffectsDurationTurns(u);
                RemoveExpiredStatusEffect(u);
            }
        }
    }

    private void DecreaseStatusEffectsDurationTurns(UnitInstance _unit)
    {
        if (CurrentPhase == eGamePhase.EndOfTurn)
        {
            for (StatusEffect se : Linq.where(_unit.getStatusEffects(), x ->  CoreFunctions.Compare(x.getDuration().Turns, eRelationType.GreaterThan, BigDecimal.ZERO))) //For each status effect that could be activated during more than one player turn
            {
                se.getDuration().Turns = se.getDuration().Turns.subtract(new BigDecimal("0.5")); //Subtract one player turn
            }
        }
    }

    private void RemoveExpiredStatusEffect(UnitInstance _unit)
    {
    	List<StatusEffect> removingStatusEffects = new ArrayList<StatusEffect>();
        for (StatusEffect se : _unit.getStatusEffects())
        {
            if (se.getDuration().ActivationTimes <= 0
                &&  CoreFunctions.Compare(se.getDuration().Turns, eRelationType.LessThanOrEqualTo, BigDecimal.ZERO)
                && (se.getDuration().getWhileCondition().getConditionSets().size() == 0
                		|| (se.getDuration().getWhileCondition().getConditionSets().size() > 0 && !se.getDuration().getWhileCondition().IsTrue(this, _unit, se))))
            {
                removingStatusEffects.add(se); //Lifetime of the status effect has ended and, thus, remove it
            }
        }
        
        for (StatusEffect se : removingStatusEffects)
        {
        	_unit.getStatusEffects().remove(se);
        }
    }

    public void ChangeSelectedUnit(PlayerOnBoard _player, int _alliedUnitIndex)
    {
        if (_alliedUnitIndex >= _player.AlliedUnits.size()
            || _alliedUnitIndex < 0)
        {
            UnselectUnits(_player);
            return;
        }

        if (_player.AlliedUnits.get(_alliedUnitIndex).IsAlive)
        {
            _player.SelectedUnitIndex = _alliedUnitIndex;
            return;
        }

        List<UnitInstance> unitsAlive = Linq.where(_player.AlliedUnits, x -> x.IsAlive == true);

        if (unitsAlive.size() == 0)
        {
            EndMatch(_player);
            return;
        }
    }

    public void ChangeTurn(PlayerOnBoard _player)
    {
    	if (_player != CurrentTurnPlayer)
    		return;
    	
        CurrentPhase = eGamePhase.EndOfTurn;
        ProcessStatusEffects(eEventTriggerTiming.EndOfTurn);
        UpdateStatusEffects();

        m_eventLogs.add(new TurnChangeEventLog(getCurrentFullTurns(), CurrentTurnPlayer.IsPlayer1 ? 1 : 2, CurrentTurnPlayer.IsPlayer1 ? 2 : 1)); //Log to indicate turn transition

        if (CurrentTurnPlayer == Field.getPlayers().get(0))
        {
            CurrentTurnPlayer = Field.getPlayers().get(1);
            CurrentPlayerTurns[1]++;
        }
        else
        {
            CurrentTurnPlayer = Field.getPlayers().get(0);
            CurrentPlayerTurns[0]++;
        }

        UpdateActionSelectionStatus(CurrentTurnPlayer);
        UpdateSP(CurrentTurnPlayer);

        CurrentPhase = eGamePhase.BeginningOfTurn;
        ProcessStatusEffects(eEventTriggerTiming.BeginningOfTurn);
    }

    public void EndMatch(PlayerOnBoard _loser)
    {
        isMatchEnd = true;

        if (Field.getPlayers().get(0) == _loser)
            isPlayer1Winner = false;
        else
            isPlayer1Winner = true;
    }
    
    public void confirmEndMatchStatusAcquisition(PlayerOnBoard _player)
    {
    	if (!isMatchEnd)
    		return;
    		
    	if (_player == Field.getPlayers().get(0)) //If _player is Player 1
    		didPlayer1GetMatchEndStatus = true;
    	else //If _player is Player 2
    		didPlayer2GetMatchEndStatus = true;
    }
    //End Public Methods

    //Public Methods (Command)
    public List<UnitInstance> FindUnitsByName(String _name, boolean _negateStringMatchType, eStringMatchType _stringMatchType)
    {
    	return FindUnitsByName(_name, _negateStringMatchType, _stringMatchType, null);
    }
    public List<UnitInstance> FindUnitsByName(String _name, boolean _negateStringMatchType, eStringMatchType _stringMatchType, List<UnitInstance> _units)
    {
        List<UnitInstance> tmp_units;

        if (_units == null)
            tmp_units = Field.getUnits();
        else
            tmp_units = _units;

        switch (_stringMatchType)
        {
            default: //ExactMatch
                if (_negateStringMatchType)
                    return Linq.where(tmp_units, x -> !x.BaseInfo.Name.equals(_name));
                else return Linq.where(tmp_units, x -> x.BaseInfo.Name.equals(_name));
            case Contains:
                if (_negateStringMatchType)
                    return Linq.where(tmp_units, x -> !x.BaseInfo.Name.contains(_name));
                else return Linq.where(tmp_units, x -> x.BaseInfo.Name.contains(_name));
            case StartsWith:
                if (_negateStringMatchType)
                    return Linq.where(tmp_units, x -> !x.BaseInfo.Name.startsWith(_name));
                else return Linq.where(tmp_units, x -> x.BaseInfo.Name.startsWith(_name));
            case EndsWith:
                if (_negateStringMatchType)
                    return Linq.where(tmp_units, x -> !x.BaseInfo.Name.endsWith(_name));
                else return Linq.where(tmp_units, x -> x.BaseInfo.Name.endsWith(_name));
        }
    }

    public List<UnitInstance> FindUnitsByLabel(String _label, boolean _excludeLabel)
    {
    	return FindUnitsByLabel(_label, _excludeLabel, null);
    }
    public List<UnitInstance> FindUnitsByLabel(String _label, boolean _excludeLabel, List<UnitInstance> _units)
    {
        List<UnitInstance> tmp_units;

        if (_units == null)
            tmp_units = Field.getUnits();
        else
            tmp_units = _units;

        if (_excludeLabel)
            return Linq.where(tmp_units, x -> !x.BaseInfo.getLabels().contains(_label));
        else return Linq.where(tmp_units, x -> x.BaseInfo.getLabels().contains(_label));
    }

    public List<UnitInstance> FindUnitsByGender(eGender _gender, boolean _excludeGender)
    {
    	return FindUnitsByGender(_gender, _excludeGender, null);
    }
    public List<UnitInstance> FindUnitsByGender(eGender _gender, boolean _excludeGender, List<UnitInstance> _units)
    {
        List<UnitInstance> tmp_units;

        if (_units == null)
            tmp_units = Field.getUnits();
        else
            tmp_units = _units;

        if (_excludeGender)
            return Linq.where(tmp_units, x -> x.BaseInfo.Gender != _gender);
        else return Linq.where(tmp_units, x -> x.BaseInfo.Gender == _gender);
    }

    public List<UnitInstance> FindUnitsByElement(eElement _element, boolean _excludeElement)
    {
    	return FindUnitsByElement(_element, _excludeElement, eElement.None, null);
    }
    public List<UnitInstance> FindUnitsByElement(eElement _element1, boolean _excludeElement, eElement _element2)
    {
    	return FindUnitsByElement(_element1, _excludeElement, _element2, null);
    }
    public List<UnitInstance> FindUnitsByElement(eElement _element1, boolean _excludeElements, eElement _element2, List<UnitInstance> _units)
    {
        List<UnitInstance> tmp_units;

        if (_units == null)
            tmp_units = Field.getUnits();
        else
            tmp_units = _units;

        if (_element2 == eElement.None)
        {
            if (_excludeElements)
                return Linq.where(tmp_units, x -> x.BaseInfo.getElements().get(0) != _element1 && x.BaseInfo.getElements().get(1) != _element1);
            else return Linq.where(tmp_units, x -> x.BaseInfo.getElements().get(0) == _element1 || x.BaseInfo.getElements().get(1) == _element1);
        }
        else
        {
            if (_excludeElements)
                return Linq.where(tmp_units, x -> (x.BaseInfo.getElements().get(0) != _element1 && x.BaseInfo.getElements().get(1) != _element2)
                                        || (x.BaseInfo.getElements().get(0) != _element2 && x.BaseInfo.getElements().get(1) != _element1));
            else
                return Linq.where(tmp_units, x -> (x.BaseInfo.getElements().get(0) == _element1 && x.BaseInfo.getElements().get(1) == _element2)
                                        || (x.BaseInfo.getElements().get(0) == _element2 && x.BaseInfo.getElements().get(1) == _element1));
        }
    }

    public List<UnitInstance> FindUnitsByAttributeValue(eUnitAttributeType _attributeType, eRelationType _relation, int _value)
    {
    	return FindUnitsByAttributeValue(_attributeType, _relation, _value);
    }
    public List<UnitInstance> FindUnitsByAttributeValue(eUnitAttributeType _attributeType, eRelationType _relation, int _value, List<UnitInstance> _units)
    {
        List<UnitInstance> tmp_units;

        if (_units == null)
            tmp_units = Field.getUnits();
        else
            tmp_units = _units;

        switch (_attributeType)
        {
            default: //Level
                return Linq.where(tmp_units, x -> CoreFunctions.Compare(Calculator.Level(x), _relation, _value));
            case MaxHP:
                return Linq.where(tmp_units, x -> CoreFunctions.Compare(Calculator.MaxHP(x), _relation, _value));
            case RemainingHP:
                return Linq.where(tmp_units, x -> CoreFunctions.Compare(x.RemainingHP, _relation, _value));
            case PhyStr:
                return Linq.where(tmp_units, x -> CoreFunctions.Compare(Calculator.PhysicalStrength(x), _relation, _value));
            case PhyRes:
                return Linq.where(tmp_units, x -> CoreFunctions.Compare(Calculator.PhysicalResistance(x), _relation, _value));
            case MagStr:
                return Linq.where(tmp_units, x -> CoreFunctions.Compare(Calculator.MagicalStrength(x), _relation, _value));
            case MagRes:
                return Linq.where(tmp_units, x -> CoreFunctions.Compare(Calculator.MagicalResistance(x), _relation, _value));
            case Vitality:
                return Linq.where(tmp_units, x -> CoreFunctions.Compare(Calculator.Vitality(x), _relation, _value));
        }
    }

    public List<UnitInstance> FindUnitsByAttributeValueRanking(eUnitAttributeType _attributeType, eSortType _sortType, int _ranking)
    {
    	return FindUnitsByAttributeValueRanking(_attributeType, _sortType, _ranking, null);
    }
    public List<UnitInstance> FindUnitsByAttributeValueRanking(eUnitAttributeType _attributeType, eSortType _sortType, int _ranking, List<UnitInstance> _units)
    {
        try
        {
            List<UnitInstance> tmp_units;

            if (_units == null)
                tmp_units = Field.getUnits();
            else
                tmp_units = _units;

            tmp_units = SortUnitsByAttributeValue(_attributeType, _sortType, tmp_units);

            List<Integer> rankedValues = SetAttributeValueRanking(tmp_units, _attributeType);

            final int rankingIndex = (_ranking - 1 < 0 || _ranking - 1 >= rankedValues.size()) ? 0 : _ranking - 1;

            switch (_attributeType)
            {
                default: //Level
                    return Linq.where(tmp_units, x -> Calculator.Level(x) == rankedValues.get(rankingIndex));
                case MaxHP:
                    return Linq.where(tmp_units, x -> Calculator.MaxHP(x) == rankedValues.get(rankingIndex));
                case RemainingHP:
                    return Linq.where(tmp_units, x -> x.RemainingHP == rankedValues.get(rankingIndex));
                case PhyStr:
                    return Linq.where(tmp_units, x -> Calculator.PhysicalStrength(x) == rankedValues.get(rankingIndex));
                case PhyRes:
                    return Linq.where(tmp_units, x -> Calculator.PhysicalResistance(x) == rankedValues.get(rankingIndex));
                case MagStr:
                    return Linq.where(tmp_units, x -> Calculator.MagicalStrength(x) == rankedValues.get(rankingIndex));
                case MagRes:
                    return Linq.where(tmp_units, x -> Calculator.MagicalResistance(x) == rankedValues.get(rankingIndex));
                case Vitality:
                    return Linq.where(tmp_units, x -> Calculator.Vitality(x) == rankedValues.get(rankingIndex));
            }
        }
        catch (Exception ex)
        {
            System.out.println("Field.FindUnitsByAttributeValueRanking() : " + ex.toString());
            return null;
        }
    }

    public List<UnitInstance> FindUnitsByTargetClassification(UnitInstance _referenceUnit, eTargetUnitClassification _targetClassification)
    {
    	return FindUnitsByTargetClassification(_referenceUnit, _targetClassification, null);
    }
    public List<UnitInstance> FindUnitsByTargetClassification(UnitInstance _referenceUnit, eTargetUnitClassification _targetClassification, List<_2DCoord> _targetRange)
    {
        List<UnitInstance> targets = new ArrayList<UnitInstance>();

        if (_targetClassification == eTargetUnitClassification.Self
            || _targetClassification == eTargetUnitClassification.SelfAndAlly
            || _targetClassification == eTargetUnitClassification.SelfAndEnemy
            || _targetClassification == eTargetUnitClassification.Any
            || _targetClassification == eTargetUnitClassification.SelfAndAllyInRange
            || _targetClassification == eTargetUnitClassification.SelfAndEnemyInRange
            || _targetClassification == eTargetUnitClassification.UnitInRange
            || _targetClassification == eTargetUnitClassification.SelfAndAllyOnBoard
            || _targetClassification == eTargetUnitClassification.SelfAndEnemyOnBoard
            || _targetClassification == eTargetUnitClassification.UnitOnBoard)
        {
            targets.add(_referenceUnit);
        }

        switch (_targetClassification)
        {
            case Self:
                break;
            case Ally:
            case SelfAndAlly:
                targets.addAll(_referenceUnit.OwnerInstance.AlliedUnits);
                break;
            case Enemy:
            case SelfAndEnemy:
                targets.addAll(Linq.where(Field.getUnits(), x -> x.OwnerInstance != _referenceUnit.OwnerInstance));
                break;
            case AllyAndEnemy:
            case Any:
                targets.addAll(_referenceUnit.OwnerInstance.AlliedUnits);
                targets.addAll(Linq.where(Field.getUnits(), x -> x.OwnerInstance != _referenceUnit.OwnerInstance));
                break;
            case AllyOnBoard:
            case SelfAndAllyOnBoard:
                targets.addAll(Linq.where(_referenceUnit.OwnerInstance.AlliedUnits, x -> x.IsAlive));
                break;
            case EnemyOnBoard:
            case SelfAndEnemyOnBoard:
                targets.addAll(Linq.where(Field.getUnits(), x -> x.OwnerInstance != _referenceUnit.OwnerInstance && x.IsAlive));
                break;
            case AllyAndEnemyOnBoard:
            case UnitOnBoard:
                targets.addAll(Linq.where(_referenceUnit.OwnerInstance.AlliedUnits, x -> x.IsAlive));
                targets.addAll(Linq.where(Field.getUnits(), x -> x.OwnerInstance != _referenceUnit.OwnerInstance && x.IsAlive));
                break;
            case AllyDefeated:
                targets.addAll(Linq.where(_referenceUnit.OwnerInstance.AlliedUnits, x -> !x.IsAlive));
                break;
            case EnemyDefeated:
                targets.addAll(Linq.where(Field.getUnits(), x -> x.OwnerInstance != _referenceUnit.OwnerInstance && !x.IsAlive));
                break;
            case AllyAndEnemyDefeated:
                targets.addAll(Linq.where(_referenceUnit.OwnerInstance.AlliedUnits, x -> !x.IsAlive));
                targets.addAll(Linq.where(Field.getUnits(), x -> x.OwnerInstance != _referenceUnit.OwnerInstance && !x.IsAlive));
                break;
            default: //InRange
                {
                    if (_targetRange != null) //If the target range is null, then it is not possible to lookup any unit.
                    {
                        for (_2DCoord coord : _targetRange)
                        {
                            if (coord.X >= 0 && coord.X <= CoreValues.SIZE_OF_A_SIDE_OF_BOARD - 1
                                && coord.Y >= 0 && coord.Y <= CoreValues.SIZE_OF_A_SIDE_OF_BOARD - 1) //it will search inside of the board
                            {
                                if (Field.Board.Sockets[coord.X][coord.Y].Unit != null)
                                {
                                    if (_targetClassification == eTargetUnitClassification.AllyInRange
                                        || _targetClassification == eTargetUnitClassification.SelfAndAllyInRange
                                        || _targetClassification == eTargetUnitClassification.AllyAndEnemyInRange
                                        || _targetClassification == eTargetUnitClassification.UnitInRange)
                                    {
                                        if (Field.Board.Sockets[coord.X][coord.Y].Unit.OwnerInstance == _referenceUnit.OwnerInstance)
                                            targets.add(Field.Board.Sockets[coord.X][coord.Y].Unit);
                                    }

                                    if (_targetClassification == eTargetUnitClassification.EnemyInRange
                                        || _targetClassification == eTargetUnitClassification.SelfAndEnemyInRange
                                        || _targetClassification == eTargetUnitClassification.AllyAndEnemyInRange
                                        || _targetClassification == eTargetUnitClassification.UnitInRange)
                                    {
                                        if (Field.Board.Sockets[coord.X][coord.Y].Unit.OwnerInstance != _referenceUnit.OwnerInstance)
                                            targets.add(Field.Board.Sockets[coord.X][coord.Y].Unit);
                                    }
                                }
                            }
                        }
                    }
                }
                break;
        }

        return targets;
    }
    //End Public Methods (Command)

    //Private Methods
    private boolean ChangeUnitLocation(UnitInstance _unit, _2DCoord _destination)
    {
        _2DCoord currentLocation = Field.UnitLocation(_unit);

        try
        {
            if (_destination.X < 0 || Field.Board.Sockets.length <= _destination.X
                || _destination.Y < 0 || Field.Board.Sockets[0].length <= _destination.Y)
                return false;

            if (Field.Board.Sockets[_destination.X][_destination.Y].Unit == null) //if there is no Unit assigned to the destination Socket
            {
                Field.Board.Sockets[currentLocation.X][currentLocation.Y].Unit = null; //remove _unit from the current Socket
                Field.Board.Sockets[_destination.X][_destination.Y].Unit = _unit; //assign _unit to the destination Socket

                return true;
            }
            //else if destination Socket is occupied
            return false;
        }
        catch (Exception ex)
        {
            System.out.println("Field: at ChangeUnitLocation() " + ex.toString());
            return false;
        }
    }

    ///<summary>
    ///PreCondition: _damage is positive value or 0; _target has been initialized successfully and _target.isAlive is true;
    ///PostCondition: _damage will be subtracted from _target.RemainingHP; If _damage > _target.RemainingHP, _target.RemainingHP will be set to 0;
    ///</summary>
    ///<param name="_damage"></param>
    ///<param name="_target"></param>
    private void DealDamage(int _damage, UnitInstance _target)
    {
        if (_target.RemainingHP - _damage > 0)
            _target.RemainingHP -= _damage;
        else
        {
            _target.RemainingHP = 0;
            UpdateUnitLiveStatus(_target);
            Field.RemoveNonAliveUnitFromBoard(_target);
        }
    }

    ///<summary>
    ///PreCondition: _hpValueToRestore is positive value or 0; _target has been initialized successfully and _target.isAlive is true;
    ///PostCondition: _hpValueToRestore will be added to _target.RemainingHP; If _hpValueToRestore + _target.RemainingHP > MaxHP, _target.RemainingHP will be set to MaxHP;
    ///</summary>
    ///<param name="_hpValueToRestore"></param>
    ///<param name="_target"></param>
    private void RestoreHP(int _hpValueToRestore, UnitInstance _target)
    {
        int maxHP = Calculator.MaxHP(_target);

        if (_target.RemainingHP + _hpValueToRestore < maxHP)
            _target.RemainingHP += _hpValueToRestore;
        else
            _target.RemainingHP = maxHP;
    }

    ///<summary>
    ///PreCondition: _effectToAttach and _target have been initialized successfully.
    ///PostCondition: A cloned instance of _effectToAttach will be added to _target.ContinuousEffects;
    ///</summary>
    ///<param name="_effectToAttach"></param>
    ///<param name="_target"></param>
    private void AttachEffect(StatusEffect _effectToAttach, UnitInstance _target)
    {
        _target.getStatusEffects().add(_effectToAttach.DeepCopy()); //add a cloned instance not to modify data : original instance
    }

    private List<UnitInstance> SortUnitsByAttributeValue(eUnitAttributeType _attributeType, eSortType _sortType, List<UnitInstance> _units)
    {
        List<UnitInstance> tmp_units;

        switch (_attributeType)
        {
            default: //Level
                tmp_units = Linq.orderBy(_units, x -> Calculator.Level(x));
                break;
            case MaxHP:
            	tmp_units = Linq.orderBy(_units, x -> Calculator.MaxHP(x));
                break;
            case RemainingHP:
            	tmp_units = Linq.orderBy(_units, x -> x.RemainingHP);
                break;
            case PhyStr:
            	tmp_units = Linq.orderBy(_units, x -> Calculator.PhysicalStrength(x));
                break;
            case PhyRes:
            	tmp_units = Linq.orderBy(_units, x -> Calculator.PhysicalResistance(x));
                break;
            case MagStr:
            	tmp_units = Linq.orderBy(_units, x -> Calculator.MagicalStrength(x));
                break;
            case MagRes:
            	tmp_units = Linq.orderBy(_units, x -> Calculator.MagicalResistance(x));
                break;
            case Vitality:
            	tmp_units = Linq.orderBy(_units, x -> Calculator.Vitality(x));
                break;
        }

        if (_sortType == eSortType.Descending)
            Collections.reverse(tmp_units);

        return tmp_units;
    }

    private List<Integer> SetAttributeValueRanking(List<UnitInstance> _sortedUnits, eUnitAttributeType _attributeType)
    {
        List<Integer> tmp_values = new ArrayList<Integer>();

        for (UnitInstance unit : _sortedUnits)
        {
            int tmp_value;

            switch (_attributeType)
            {
                default: //Level
                    tmp_value = Calculator.Level(unit);
                    break;
                case MaxHP:
                    tmp_value = Calculator.MaxHP(unit);
                    break;
                case RemainingHP:
                    tmp_value = unit.RemainingHP;
                    break;
                case PhyStr:
                    tmp_value = Calculator.PhysicalStrength(unit);
                    break;
                case PhyRes:
                    tmp_value = Calculator.PhysicalResistance(unit);
                    break;
                case MagStr:
                    tmp_value = Calculator.MagicalStrength(unit);
                    break;
                case MagRes:
                    tmp_value = Calculator.MagicalResistance(unit);
                    break;
                case Vitality:
                    tmp_value = Calculator.Vitality(unit);
                    break;
            }

            if (!tmp_values.contains(tmp_value))
                tmp_values.add(tmp_value);
        }

        return tmp_values;
    }

    private void UpdateSP(PlayerOnBoard _player)
    {
        if (Field.getPlayers().get(0) == _player)
            _player.MaxSP = (CurrentPlayerTurns[0] > CoreValues.MAX_SP) ? CoreValues.MAX_SP : CurrentPlayerTurns[0];
        else
            _player.MaxSP = (CurrentPlayerTurns[1] > CoreValues.MAX_SP) ? CoreValues.MAX_SP : CurrentPlayerTurns[1];

        _player.RemainingSP = _player.MaxSP;
    }

    private void UpdateActionSelectionStatus(PlayerOnBoard _player)
    {
        _player.Moved = false;
        _player.Attacked = false;
    }

    private void UnselectUnits(PlayerOnBoard _player)
    {
        _player.SelectedUnitIndex = -1;
    }

    private void UpdateUnitLiveStatus(UnitInstance _unit)
    {
        if (_unit.RemainingHP <= 0 && _unit.IsAlive)
        {
            _unit.IsAlive = false;

            PlayerOnBoard owner = _unit.OwnerInstance;
            if (owner.SelectedUnitIndex >= 0 
                && owner.SelectedUnitIndex < owner.AlliedUnits.size()
                && _unit == owner.AlliedUnits.get(owner.SelectedUnitIndex))
            {
                UnselectUnits(owner);
            }

            if (!Linq.any(owner.AlliedUnits, x -> x.IsAlive)) //If no unit of the specific player is alive
                EndMatch(owner); //The player has lost
        }
        else if (_unit.RemainingHP > 0 && !_unit.IsAlive)
            _unit.IsAlive = true;
    }
    //End Private Fields
}
