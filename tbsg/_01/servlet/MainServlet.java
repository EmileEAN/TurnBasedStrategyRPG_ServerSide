package eean_games.tbsg._01.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eean_games.main.Linq;
import eean_games.main._2DCoord;
import eean_games.tbsg._01.CoreValues;
import eean_games.tbsg._01.DataLoader;
import eean_games.tbsg._01.MatchDataContainer;
import eean_games.tbsg._01.SessionManager;
import eean_games.tbsg._01.SharedGameDataContainer;
import eean_games.tbsg._01.Team;
import eean_games.tbsg._01.equipment.Accessory;
import eean_games.tbsg._01.equipment.Armour;
import eean_games.tbsg._01.equipment.Weapon;
import eean_games.tbsg._01.gacha.Gacha;
import eean_games.tbsg._01.player.Player;
import eean_games.tbsg._01.unit.Unit;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static int updateNum = 0; //Sent to client applications so that they know whether to reload some contents
	public static void increaseUpdateNum() 
	{ 
		if (updateNum != Integer.MAX_VALUE)
			updateNum++; 
		else
			updateNum = 0; //Restart count
	} 
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("resource")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try 
		{
			//-------------------------------Connection----------------------------------------
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection con = DriverManager
					.getConnection(CoreValues.URL, CoreValues.USERNAME, CoreValues.PASSWORD);
			//---------------------------------------------------------------------------------
			
			String query;
			PreparedStatement statement;
			
			int recordsAffected;
			ResultSet resultSet;
			
			String subject = request.getParameter("subject");
			if (subject == null)
				return;
			
			if (!subject.equals("CheckConnection")
					&& !subject.equals("GetMissingEventLogs")
					&& !subject.equals("CheckMatchingStatus")
					&& !subject.equals("CheckSessionsValidity")
					&& !subject.equals("UpdateConnectionStatus")
					&& !subject.equals("CheckForUpdate"))
			{
				System.out.println("Received post request:" + subject);
			}				
			
			String result = "";
			switch(subject)
			{
				case "CheckConnection":
					break; //Return empty String. The client would not receive any result if a connection error occurs.
			
				case "CheckUserNameValidity":
					{
						String userName = request.getParameter("userName");
						
						query = "select Id from Players where UserName=?";
						statement = con.prepareStatement(query);
						statement.setString(1, userName);
						
						resultSet = statement.executeQuery();
						if (!resultSet.next())
							result = "valid";
						else
							result = "invalid";
					}
					break;
				
				case "CheckPlayerCredentialsValidityAndAttemptToLogin":
					{	
						String userName = request.getParameter("userName");
						String password = request.getParameter("password");
						
						query = "select Id from Players where UserName=? AND Password=?";
						statement = con.prepareStatement(query);
						statement.setString(1, userName);
						statement.setString(2, password);
						
						resultSet = statement.executeQuery();
						if (resultSet.next())
						{
							int id = resultSet.getInt("Id");
							Player player = Linq.first(SharedGameDataContainer.getPlayers(), x->x.Id == id);
							String sessionId = SessionManager.initiateSession(player);
							if (sessionId != "")
								result = "success:" + sessionId;
							else
								result = "currentlyInUse"; //Someone may have already logged into the account
						}
						else
						{
							query = "select Id from Players where UserName=?";
							statement = con.prepareStatement(query);
							statement.setString(1, userName);
							
							resultSet = statement.executeQuery();
							if(resultSet.next())
								result = "wrongPassword";
							else
								result = "noAccount";
						}
					}
					break;
					
				case "UpdateConnectionStatus":
					{
						String sessionId = request.getParameter("sessionId");
						SessionManager.updateConnectionStatus(sessionId);
					}
					break;
					
				case "CheckForUpdate":
					{
						String sessionId = request.getParameter("sessionId");
						result = String.valueOf(updateNum);
						SessionManager.updateConnectionStatus(sessionId);
					}
					break;
					
				case "CreateAccount":
					{
						String userName = request.getParameter("userName");
						String password = request.getParameter("password");
						String playerName = request.getParameter("playerName");
						String securityQuestion = request.getParameter("securityQuestion");
						String answer = request.getParameter("answer");
						int gems = CoreValues.GEMS_TO_GIVE_TO_NEW_PLAYER;
						int gold = CoreValues.GOLD_TO_GIVE_TO_NEW_PLAYER;
						
						query = "insert into Players (UserName, Password, PlayerName, SecurityQuestion, Answer, GemsOwned, GoldOwned) values (?, ?, ?, ?, ?, ?, ?)";
						statement = con.prepareStatement(query);
						statement.setString(1, userName);
						statement.setString(2, password);
						statement.setString(3, playerName);
						statement.setString(4, securityQuestion);
						statement.setString(5, answer);
						statement.setInt(6, gems);
						statement.setInt(7, gold);

						recordsAffected = statement.executeUpdate();
						
						if (recordsAffected != 0)
						{
							query = "select Id from Players where UserName = ?";
							statement = con.prepareStatement(query);
							statement.setString(1, userName);
							
							ResultSet resultSet_playerId = statement.executeQuery();
							if (resultSet_playerId.next())
							{
								int playerId = resultSet_playerId.getInt("Id");
								
								//The insert commands below are for testing purposes and will be removed/modified for the final version
								//Create PlayableUnits for the player
								query = "insert into PlayableUnits (BaseUnitId, OwnerPlayerId, AccumulatedExperience, Nickname) values (?, ?, ?, ?)";
								statement = con.prepareStatement(query);
								statement.setInt(1, 1);
								statement.setInt(2, playerId);
								statement.setInt(3, 10000000);
								statement.setString(4, "Charlotte");
								statement.executeUpdate();
								
								statement = con.prepareStatement(query);
								statement.setInt(1, 2);
								statement.setInt(2, playerId);
								statement.setInt(3, 10000000);
								statement.setString(4, "Ille");
								statement.executeUpdate();
								
								statement = con.prepareStatement(query);
								statement.setInt(1, 3);
								statement.setInt(2, playerId);
								statement.setInt(3, 10000000);
								statement.setString(4, "Rudolph");
								statement.executeUpdate();
								
								statement = con.prepareStatement(query);
								statement.setInt(1, 4);
								statement.setInt(2, playerId);
								statement.setInt(3, 10000000);
								statement.setString(4, "Serin");
								statement.executeUpdate();
								
								statement = con.prepareStatement(query);
								statement.setInt(1, 5);
								statement.setInt(2, playerId);
								statement.setInt(3, 10000000);
								statement.setString(4, "Yukito");
								statement.executeUpdate();
								
								//Check if the units have been added successfully and, if so, get the Id for each of them
								query = "select Id from PlayableUnits where OwnerPlayerId = ?";
								statement = con.prepareStatement(query);
								statement.setInt(1, playerId);
								
								ResultSet resultSet_unitIds = statement.executeQuery();
								List<Integer> unitIds = new ArrayList<Integer>();
								while (resultSet_unitIds.next())
								{
									unitIds.add(resultSet_unitIds.getInt("Id"));
								}
								
								if (unitIds.size() == 5)
								{								
									//Create Team for the player
									query = "insert into Teams (Member1Id, Member2Id, Member3Id, Member4Id, Member5Id, OwnerId) values (?, ?, ?, ?, ?, ?)";
									statement = con.prepareStatement(query);
									statement.setInt(1, unitIds.get(0));
									statement.setInt(2, unitIds.get(1));
									statement.setInt(3, unitIds.get(2));
									statement.setInt(4, unitIds.get(3));
									statement.setInt(5, unitIds.get(4));
									statement.setInt(6, playerId);
									recordsAffected = statement.executeUpdate();
									
									if (recordsAffected != 0)
									{
										//Add new player to SharedGameDataContainer
										SharedGameDataContainer.addPlayer(con, playerId);
										result = "success";	
									}
									else
										result = "failure";
								}
								else
									result = "failure";
							}
							else
								result = "failure";
						}
						else
							result = "failure";
					}
					break;
				
				case "LoadCoreGameData":
					{
						//Asking for the userName and password again in order to increase security
						String userName = request.getParameter("userName");
						String password = request.getParameter("password");
						String sessionId = request.getParameter("sessionId");
						
						query = "select Id from Players where UserName=? AND Password=?";
						statement = con.prepareStatement(query);
						statement.setString(1, userName);
						statement.setString(2, password);
						
						resultSet = statement.executeQuery();
						if (resultSet.next())
						{
							int playerId = resultSet.getInt("Id");
							
							if (!SessionManager.updateConnectionStatus(sessionId))
								result = "sessionExpired";
							else
							{
								result = SharedGameDataContainer.getAllDataAsResponseString(con, playerId);
								result = result + DataLoader.getPlayerDataAsResponseString(playerId);
								result = result + DataLoader.getGachaDispensationOptionsRemainingAttemptsAsResponseStrings(con, playerId);
							}
						}
						else
							result = "invalidCredentials";
					}
					break;
					
				case "LoadGachaData":
					{
						String sessionId = request.getParameter("sessionId");
						
						if (!SessionManager.updateConnectionStatus(sessionId))
							result = "sessionExpired";
						else
						{
							int playerId = SessionManager.getPlayerId(sessionId);
							
							result = result + SharedGameDataContainer.getGachaDataAsResponseString();
							result = result + DataLoader.getGachaDispensationOptionsRemainingAttemptsAsResponseStrings(con, playerId);
						}
					}
					break;
					
				case "ChangeUnitLockStatus":
					{
						String sessionId = request.getParameter("sessionId");
						int playerId = SessionManager.getPlayerId(sessionId);
						String uniqueIdString = request.getParameter("uniqueId");
						int uniqueId = Integer.parseInt(uniqueIdString);
						String lockString = request.getParameter("lock");
						boolean lock = Boolean.parseBoolean(lockString);
						
						if (!SessionManager.updateConnectionStatus(sessionId))
							result = "sessionExpired";
						else
						{
							query = "update PlayableUnits set IsLocked=? where Id=? AND OwnerPlayerId=?";
							statement = con.prepareStatement(query);
							statement.setBoolean(1, lock);
							statement.setInt(2, uniqueId);
							statement.setInt(3, playerId);
							
							recordsAffected = statement.executeUpdate();
							if (recordsAffected > 0)
							{
								Player player = Linq.first(SharedGameDataContainer.getPlayers(), x -> x.Id == playerId);
								Unit unit = Linq.first(player.UnitsOwned, x -> x.UniqueId == uniqueId);
								unit.IsLocked = lock;
								
								if (lock == true)
									result = "locked";
								else
									result = "unlocked";
							}
							else
								result = "error";
						}
					}
					break;
				
				case "ChangeWeaponLockStatus":
					{
						String sessionId = request.getParameter("sessionId");
						int playerId = SessionManager.getPlayerId(sessionId);
						String uniqueIdString = request.getParameter("uniqueId");
						int uniqueId = Integer.parseInt(uniqueIdString);
						String lockString = request.getParameter("lock");
						boolean lock = Boolean.parseBoolean(lockString);
						
						if (!SessionManager.updateConnectionStatus(sessionId))
							result = "sessionExpired";
						else
						{
							query = "update UsableWeapons set IsLocked=? where Id=? AND OwnerPlayerId=?";
							statement = con.prepareStatement(query);
							statement.setBoolean(1, lock);
							statement.setInt(2, uniqueId);
							statement.setInt(3, playerId);
							
							recordsAffected = statement.executeUpdate();
							if (recordsAffected > 0)
							{
								Player player = Linq.first(SharedGameDataContainer.getPlayers(), x -> x.Id == playerId);
								Weapon weapon = Linq.first(player.WeaponsOwned, x -> x.UniqueId == uniqueId);
								weapon.IsLocked = lock;
								
								if (lock == true)
									result = "locked";
								else
									result = "unlocked";
							}
							else
								result = "error";
						}
					}
					break;
					
				case "ChangeArmourLockStatus":
					{
						String sessionId = request.getParameter("sessionId");
						int playerId = SessionManager.getPlayerId(sessionId);
						String uniqueIdString = request.getParameter("uniqueId");
						int uniqueId = Integer.parseInt(uniqueIdString);
						String lockString = request.getParameter("lock");
						boolean lock = Boolean.parseBoolean(lockString);
						
						if (!SessionManager.updateConnectionStatus(sessionId))
							result = "sessionExpired";
						else
						{
							query = "update UsableArmours set IsLocked=? where Id=? AND OwnerPlayerId=?";
							statement = con.prepareStatement(query);
							statement.setBoolean(1, lock);
							statement.setInt(2, uniqueId);
							statement.setInt(3, playerId);
							
							recordsAffected = statement.executeUpdate();
							if (recordsAffected > 0)
							{
								Player player = Linq.first(SharedGameDataContainer.getPlayers(), x -> x.Id == playerId);
								Armour armour = Linq.first(player.ArmoursOwned, x -> x.UniqueId == uniqueId);
								armour.IsLocked = lock;
								
								if (lock == true)
									result = "locked";
								else
									result = "unlocked";
							}
							else
								result = "error";
						}
					}
					break;
				
				case "ChangeAccessoryLockStatus":
					{
						String sessionId = request.getParameter("sessionId");
						int playerId = SessionManager.getPlayerId(sessionId);
						String uniqueIdString = request.getParameter("uniqueId");
						int uniqueId = Integer.parseInt(uniqueIdString);
						String lockString = request.getParameter("lock");
						boolean lock = Boolean.parseBoolean(lockString);
						
						if (!SessionManager.updateConnectionStatus(sessionId))
							result = "sessionExpired";
						else
						{
							query = "update UsableAccessories set IsLocked=? where Id=? AND OwnerPlayerId=?";
							statement = con.prepareStatement(query);
							statement.setBoolean(1, lock);
							statement.setInt(2, uniqueId);
							statement.setInt(3, playerId);
							
							recordsAffected = statement.executeUpdate();
							if (recordsAffected > 0)
							{
								Player player = Linq.first(SharedGameDataContainer.getPlayers(), x -> x.Id == playerId);
								Accessory accessory = Linq.first(player.AccessoriesOwned, x -> x.UniqueId == uniqueId);
								accessory.IsLocked = lock;
								
								if (lock == true)
									result = "locked";
								else
									result = "unlocked";
							}
							else
								result = "error";
						}
					}
					break;
					
				case "ChangeMember":
					{
						String sessionId = request.getParameter("sessionId");
						int playerId = SessionManager.getPlayerId(sessionId);
						String teamIndexString = request.getParameter("teamIndex");
						int teamIndex = Integer.parseInt(teamIndexString);
						String memberIndexString = request.getParameter("memberIndex");
						int memberIndex = Integer.parseInt(memberIndexString);
						String targetUnitIdString = request.getParameter("targetUnitId");
						int targetUnitId = Integer.parseInt(targetUnitIdString);
						
						if (!SessionManager.updateConnectionStatus(sessionId))
							result = "sessionExpired";
						else
						{
							Player player = Linq.first(SharedGameDataContainer.getPlayers(), x -> x.Id == playerId);
							if (teamIndex < 0 || teamIndex >= player.Teams.size())
							{
								Team team = player.Teams.get(teamIndex);
								if (memberIndex < 0 || memberIndex >= CoreValues.MAX_MEMBERS_PER_TEAM)
								{	
									Unit member = team.members[memberIndex];
									int memberUniqueId = member.UniqueId;
									
									Unit targetUnit = Linq.first(player.UnitsOwned, x -> x.UniqueId == targetUnitId);
									
									List<Unit> members = Arrays.asList(team.members);
									int memberIndex_targetUnit = members.indexOf(targetUnit);
									if (memberIndex_targetUnit != -1) //if the targetUnit is also a member of the same team
									{
										// Swap the two members
										int memberIndex_formerMember = (memberIndex < memberIndex_targetUnit) ? memberIndex : memberIndex_targetUnit;
										int memberIndex_latterMember = (memberIndex < memberIndex_targetUnit) ? memberIndex_targetUnit : memberIndex;
										int uniqueId_formerMember = (memberIndex < memberIndex_targetUnit) ? memberUniqueId : targetUnitId;
										int uniqueId_latterMember = (memberIndex < memberIndex_targetUnit) ? targetUnitId : memberUniqueId;
										
										query = "update Teams set Member" + String.valueOf(memberIndex_latterMember + 1) + "Id=NULL where OwnerId=? order by Id limit ?,1";
										statement = con.prepareStatement(query);
										statement.setInt(1, playerId);
										statement.setInt(2, teamIndex);
										
										recordsAffected = statement.executeUpdate();
										if (recordsAffected > 0)
										{
											query = "update Teams set Member" + String.valueOf(memberIndex_formerMember + 1) + "Id=? where OwnerId=? order by Id limit ?,1";
											statement = con.prepareStatement(query);
											statement.setInt(1, uniqueId_latterMember);
											statement.setInt(2, playerId);
											statement.setInt(3, teamIndex);
											
											recordsAffected = statement.executeUpdate();
											if (recordsAffected > 0)
											{
												query = "update Teams set Member" + String.valueOf(memberIndex_latterMember + 1) + "Id=? where OwnerId=? order by Id limit ?,1";
												statement = con.prepareStatement(query);
												statement.setInt(1, uniqueId_formerMember);
												statement.setInt(2, playerId);
												statement.setInt(3, teamIndex);
												
												recordsAffected = statement.executeUpdate();
												if (recordsAffected > 0)
												{												
													//Apply changes to data in SharedGameDataContainer
													team.members[memberIndex] = targetUnit;
													team.members[memberIndex_targetUnit] = member;
													
													result = "changed";
												}
												else
													result = "error";
											}
											else
												result = "error";
										}
										else
											result = "error";
									}
									else //If the targetUnit is not a member of the team
									{
										//Change the member for the targetUnit
										query = "update Teams set Member" + String.valueOf(memberIndex + 1) + "Id=? where OwnerId=? order by Id limit ?,1";
										statement = con.prepareStatement(query);
										statement.setInt(1, targetUnitId);
										statement.setInt(2, playerId);
										statement.setInt(3, teamIndex);
										
										recordsAffected = statement.executeUpdate();
										if (recordsAffected > 0)
										{
											//Apply changes to the data in SharedGameDataContainer
											team.members[memberIndex] = targetUnit;
											
											result = "changed";
										}
										else
											result = "error";
									}
								}
								else
									result = "error";
							}
							else
								result = "error";
						}
					}
					break;
					
				case "ChangeEquipment":
					{
						String sessionId = request.getParameter("sessionId");
						int playerId = SessionManager.getPlayerId(sessionId);
						String targetUnitIdString = request.getParameter("targetUnitId");
						int targetUnitId = Integer.parseInt(targetUnitIdString);
						String targetEquipmentTypeString = request.getParameter("targetEquipmentType");
						String targetEquipmentIdString = request.getParameter("targetEquipmentId");
						int targetEquipmentId = Integer.parseInt(targetEquipmentIdString);
						
						if (!SessionManager.updateConnectionStatus(sessionId))
							result = "sessionExpired";
						else
						{
							Player player = Linq.first(SharedGameDataContainer.getPlayers(), x -> x.Id == playerId);
							Unit targetUnit = Linq.first(player.UnitsOwned, x -> x.UniqueId == targetUnitId);
							
							switch (targetEquipmentTypeString)
							{
								case "UnitMainWeapon":
									{
										int mainWeaponId = (targetUnit.mainWeapon != null) ? targetUnit.mainWeapon.UniqueId : 0;
										Weapon targetWeapon = Linq.first(player.WeaponsOwned, x -> x.UniqueId == targetEquipmentId);
										if (!Linq.containsAny(targetUnit.BaseInfo.getEquipableWeaponClassifications(), targetWeapon.BaseInfo.getWeaponClassifications()))
											result = "unequipable";
										else if (targetUnit.subWeapon == targetWeapon) //If the target equipment is set as the sub weapon of the same targetUnit
										{
											//Swap the main and sub weapons
											query = "update PlayableUnits set SubWeaponId Id=NULL where Id=?";
											statement = con.prepareStatement(query);
											statement.setInt(1, targetUnitId);
											
											recordsAffected = statement.executeUpdate();
											if (recordsAffected > 0)
											{
												query = "update PlayableUnits set MainWeaponId=? where Id=?";
												statement = con.prepareStatement(query);
												statement.setInt(1, targetEquipmentId);
												statement.setInt(2, targetUnitId);
												
												recordsAffected = statement.executeUpdate();
												if (recordsAffected > 0)
												{
													if (mainWeaponId != 0)
													{
														query = "update PlayableUnits set SubWeaponId=? where Id=?";
														statement = con.prepareStatement(query);
														statement.setInt(1, mainWeaponId);
														statement.setInt(2, targetUnitId);
													}
													else
													{
														query = "update PlayableUnits set SubWeaponId=NULL where Id=?";
														statement = con.prepareStatement(query);
														statement.setInt(1, targetUnitId);
													}
													
													recordsAffected = statement.executeUpdate();
													if (recordsAffected > 0)
													{
														//Apply changes to data in SharedGameDataContainer
														Weapon tmp = targetUnit.mainWeapon;
														targetUnit.mainWeapon = targetUnit.subWeapon;
														targetUnit.subWeapon = tmp;
														
														result = "changed";
													}
													else
														result = "error";
												}
												else
													result = "error";
											}
											else
												result = "error";
										}
										else if (Linq.any(player.UnitsOwned, x -> x != targetUnit && (x.mainWeapon == targetWeapon || x.subWeapon == targetWeapon)))
										{
											Unit holderUnit = Linq.first(player.UnitsOwned, x -> x.mainWeapon == targetWeapon 
																								|| x.subWeapon == targetWeapon);
											
											//Attempt to swap the weapons of the two units
											String columnName = (holderUnit.mainWeapon == targetWeapon) ? "MainWeaponId" : "SubWeaponId";
											query = "update PlayableUnits set " + columnName + "=NULL where Id=?";
											statement = con.prepareStatement(query);
											statement.setInt(1, holderUnit.UniqueId);
											
											recordsAffected = statement.executeUpdate();
											if (recordsAffected > 0)
											{
												query = "update PlayableUnits set MainWeaponId=? where Id=?";
												statement = con.prepareStatement(query);
												statement.setInt(1, targetEquipmentId);
												statement.setInt(2, targetUnitId);
												
												recordsAffected = statement.executeUpdate();
												if (recordsAffected > 0)
												{
													boolean canHolderEquipSwappingEquipment = Linq.containsAny(holderUnit.BaseInfo.getEquipableWeaponClassifications(), targetWeapon.BaseInfo.getWeaponClassifications());
													if (mainWeaponId != 0 && canHolderEquipSwappingEquipment)
													{
														query = "update PlayableUnits set " + columnName + "=? where Id=?";
														statement = con.prepareStatement(query);
														statement.setInt(1, mainWeaponId);
														statement.setInt(2, holderUnit.UniqueId);
													}
													else
													{
														query = "update PlayableUnits set " + columnName + "=NULL where Id=?";
														statement = con.prepareStatement(query);
														statement.setInt(1, holderUnit.UniqueId);
													}
													
													recordsAffected = statement.executeUpdate();
													if (recordsAffected > 0)
													{
														//Apply changes to data in SharedGameDataContainer
														Weapon tmp = targetUnit.mainWeapon;
														if (columnName.equals("MainWeaponId"))
														{
															targetUnit.mainWeapon = holderUnit.mainWeapon;
															holderUnit.mainWeapon = canHolderEquipSwappingEquipment ? tmp : null;
														}
														else
														{
															targetUnit.mainWeapon = holderUnit.subWeapon;
															holderUnit.subWeapon = canHolderEquipSwappingEquipment ? tmp : null;;
														}
														
														result = "changed";
													}
													else
														result = "error";
												}
												else
													result = "error";
											}
											else
												result = "error";
										}
										else //If no other unit is holding the equipment
										{
											query = "update PlayableUnits set MainWeaponId=? where Id=?";
											statement = con.prepareStatement(query);
											statement.setInt(1, targetEquipmentId);
											statement.setInt(2, targetUnitId);
											
											recordsAffected = statement.executeUpdate();
											if (recordsAffected > 0)
											{
												//Apply changes to data in SharedGameDataContainer
												targetUnit.mainWeapon = targetWeapon;
											
												result = "changed";
											}
										}
									}
									break;
									
								case "UnitSubWeapon":
									{
										int subWeaponId = (targetUnit.subWeapon != null) ? targetUnit.subWeapon.UniqueId : 0;
										Weapon targetWeapon = Linq.first(player.WeaponsOwned, x -> x.UniqueId == targetEquipmentId);
										if (!Linq.containsAny(targetUnit.BaseInfo.getEquipableWeaponClassifications(), targetWeapon.BaseInfo.getWeaponClassifications()))
											result = "unequipable";
										else if (targetUnit.mainWeapon == targetWeapon) //If the target equipment is set as the main weapon of the same targetUnit
										{
											//Swap the sub and main weapons
											query = "update PlayableUnits set MainWeaponId=NULL where Id=?";
											statement = con.prepareStatement(query);
											statement.setInt(1, targetUnitId);
											
											recordsAffected = statement.executeUpdate();
											if (recordsAffected > 0)
											{
												query = "update PlayableUnits set SubWeaponId=? where Id=?";
												statement = con.prepareStatement(query);
												statement.setInt(1, targetEquipmentId);
												statement.setInt(2, targetUnitId);
												
												recordsAffected = statement.executeUpdate();
												if (recordsAffected > 0)
												{
													if (subWeaponId != 0)
													{
														query = "update PlayableUnits set MainWeaponId=? where Id=?";
														statement = con.prepareStatement(query);
														statement.setInt(1, subWeaponId);
														statement.setInt(2, targetUnitId);
													}
													else
													{
														query = "update PlayableUnits set MainWeaponId=NULL where Id=?";
														statement = con.prepareStatement(query);
														statement.setInt(1, targetUnitId);
													}
													
													recordsAffected = statement.executeUpdate();
													if (recordsAffected > 0)
													{
														//Apply changes to data in SharedGameDataContainer
														Weapon tmp = targetUnit.subWeapon;
														targetUnit.subWeapon = targetUnit.mainWeapon;
														targetUnit.mainWeapon = tmp;
														
														result = "changed";
													}
													else
														result = "error";
												}
												else
													result = "error";
											}
											else
												result = "error";
										}
										else if (Linq.any(player.UnitsOwned, x -> x != targetUnit && (x.subWeapon == targetWeapon || x.mainWeapon == targetWeapon)))
										{
											Unit holderUnit = Linq.first(player.UnitsOwned, x -> x.subWeapon == targetWeapon 
																								|| x.mainWeapon == targetWeapon);
											
											//Attempt to swap the weapons of the two units
											String columnName = (holderUnit.subWeapon == targetWeapon) ? "SubWeaponId" : "MainWeaponId";
											query = "update PlayableUnits set " + columnName + "=NULL where Id=?";
											statement = con.prepareStatement(query);
											statement.setInt(1, holderUnit.UniqueId);
											
											recordsAffected = statement.executeUpdate();
											if (recordsAffected > 0)
											{
												query = "update PlayableUnits set SubWeaponId=? where Id=?";
												statement = con.prepareStatement(query);
												statement.setInt(1, targetEquipmentId);
												statement.setInt(2, targetUnitId);
												
												recordsAffected = statement.executeUpdate();
												if (recordsAffected > 0)
												{
													boolean canHolderEquipSwappingEquipment = Linq.containsAny(holderUnit.BaseInfo.getEquipableWeaponClassifications(), targetWeapon.BaseInfo.getWeaponClassifications());
													if (subWeaponId != 0 && canHolderEquipSwappingEquipment)
													{
														query = "update PlayableUnits set " + columnName + "=? where Id=?";
														statement = con.prepareStatement(query);
														statement.setInt(1, subWeaponId);
														statement.setInt(2, holderUnit.UniqueId);
													}
													else
													{
														query = "update PlayableUnits set " + columnName + "=NULL where Id=?";
														statement = con.prepareStatement(query);
														statement.setInt(1, holderUnit.UniqueId);
													}
													
													recordsAffected = statement.executeUpdate();
													if (recordsAffected > 0)
													{
														//Apply changes to data in SharedGameDataContainer
														Weapon tmp = targetUnit.subWeapon;
														if (columnName.equals("MainWeaponId"))
														{
															targetUnit.subWeapon = holderUnit.mainWeapon;
															holderUnit.mainWeapon = canHolderEquipSwappingEquipment ? tmp : null;;
														}
														else
														{
															targetUnit.subWeapon = holderUnit.subWeapon;
															holderUnit.subWeapon = canHolderEquipSwappingEquipment ? tmp : null;;
														}
														
														result = "changed";
													}
													else
														result = "error";
												}
												else
													result = "error";
											}
											else
												result = "error";
										}
										else //If no other unit is holding the equipment
										{
											query = "update PlayableUnits set SubWeaponId=? where Id=?";
											statement = con.prepareStatement(query);
											statement.setInt(1, targetEquipmentId);
											statement.setInt(2, targetUnitId);
											
											recordsAffected = statement.executeUpdate();
											if (recordsAffected > 0)
											{
												//Apply changes to data in SharedGameDataContainer
												targetUnit.subWeapon = targetWeapon;
											
												result = "changed";
											}
										}
									}
									break;
									
								case "UnitArmour":
									{
										int armourId = (targetUnit.armour != null) ? targetUnit.armour.UniqueId : 0;
										Armour targetArmour = Linq.first(player.ArmoursOwned, x -> x.UniqueId == targetEquipmentId);
										if (!targetUnit.BaseInfo.getEquipableArmourClassifications().contains(targetArmour.BaseInfo.ArmourClassification))
											result = "unequipable";
										else if (Linq.any(player.UnitsOwned, x -> x != targetUnit && x.armour == targetArmour))
										{
											Unit holderUnit = Linq.first(player.UnitsOwned, x -> x.armour == targetArmour);
											
											//Attempt to swap the armours of the two units
											query = "update PlayableUnits set ArmourId=NULL where Id=?";
											statement = con.prepareStatement(query);
											statement.setInt(1, holderUnit.UniqueId);
											
											recordsAffected = statement.executeUpdate();
											if (recordsAffected > 0)
											{
												query = "update PlayableUnits set ArmourId=? where Id=?";
												statement = con.prepareStatement(query);
												statement.setInt(1, targetEquipmentId);
												statement.setInt(2, targetUnitId);
												
												recordsAffected = statement.executeUpdate();
												if (recordsAffected > 0)
												{
													boolean canHolderEquipSwappingEquipment = holderUnit.BaseInfo.getEquipableArmourClassifications().contains(targetArmour.BaseInfo.ArmourClassification);
													if (armourId != 0 && canHolderEquipSwappingEquipment)
													{
														query = "update PlayableUnits set ArmourId=? where Id=?";
														statement = con.prepareStatement(query);
														statement.setInt(1, armourId);
														statement.setInt(2, holderUnit.UniqueId);
													}
													else
													{
														query = "update PlayableUnits set ArmourId=NULL where Id=?";
														statement = con.prepareStatement(query);
														statement.setInt(1, holderUnit.UniqueId);
													}
													
													recordsAffected = statement.executeUpdate();
													if (recordsAffected > 0)
													{
														//Apply changes to data in SharedGameDataContainer
														Armour tmp = targetUnit.armour;
														targetUnit.armour = holderUnit.armour;
														holderUnit.armour = canHolderEquipSwappingEquipment ? tmp : null;
														
														result = "changed";
													}
													else
														result = "error";
												}
												else
													result = "error";
											}
											else
												result = "error";
										}
										else //If no other unit is holding the equipment
										{
											query = "update PlayableUnits set ArmourId=? where Id=?";
											statement = con.prepareStatement(query);
											statement.setInt(1, targetEquipmentId);
											statement.setInt(2, targetUnitId);
											
											recordsAffected = statement.executeUpdate();
											if (recordsAffected > 0)
											{
												//Apply changes to data in SharedGameDataContainer
												targetUnit.armour = targetArmour;
											
												result = "changed";
											}
										}
									}
									break;
									
								case "UnitAccessory":
									{
										int accessoryId = (targetUnit.accessory != null) ? targetUnit.accessory.UniqueId : 0;
										Accessory targetAccessory = Linq.first(player.AccessoriesOwned, x -> x.UniqueId == targetEquipmentId);
										if (!targetUnit.BaseInfo.getEquipableAccessoryClassifications().contains(targetAccessory.BaseInfo.accessoryClassification))
											result = "unequipable";
										else if (Linq.any(player.UnitsOwned, x -> x != targetUnit && x.accessory == targetAccessory))
										{
											Unit holderUnit = Linq.first(player.UnitsOwned, x -> x.accessory == targetAccessory);
											
											//Attempt to swap the accessories of the two units
											query = "update PlayableUnits set AccessoryId=NULL where Id=?";
											statement = con.prepareStatement(query);
											statement.setInt(1, holderUnit.UniqueId);
											
											recordsAffected = statement.executeUpdate();
											if (recordsAffected > 0)
											{
												query = "update PlayableUnits set AccessoryId=? where Id=?";
												statement = con.prepareStatement(query);
												statement.setInt(1, targetEquipmentId);
												statement.setInt(2, targetUnitId);
												
												recordsAffected = statement.executeUpdate();
												if (recordsAffected > 0)
												{
													boolean canHolderEquipSwappingEquipment = holderUnit.BaseInfo.getEquipableAccessoryClassifications().contains(targetAccessory.BaseInfo.accessoryClassification);
													if (accessoryId != 0 && canHolderEquipSwappingEquipment)
													{
														query = "update PlayableUnits set AccessoryId=? where Id=?";
														statement = con.prepareStatement(query);
														statement.setInt(1, accessoryId);
														statement.setInt(2, holderUnit.UniqueId);
													}
													else
													{
														query = "update PlayableUnits set AccessoryId=NULL where Id=?";
														statement = con.prepareStatement(query);
														statement.setInt(1, holderUnit.UniqueId);
													}
													
													recordsAffected = statement.executeUpdate();
													if (recordsAffected > 0)
													{
														//Apply changes to data in SharedGameDataContainer
														Accessory tmp = targetUnit.accessory;
														targetUnit.accessory = holderUnit.accessory;
														holderUnit.accessory = canHolderEquipSwappingEquipment ? tmp : null;
														
														result = "changed";
													}
													else
														result = "error";
												}
												else
													result = "error";
											}
											else
												result = "error";
										}
										else //If no other unit is holding the equipment
										{
											query = "update PlayableUnits set AccessoryId=? where Id=?";
											statement = con.prepareStatement(query);
											statement.setInt(1, targetEquipmentId);
											statement.setInt(2, targetUnitId);
											
											recordsAffected = statement.executeUpdate();
											if (recordsAffected > 0)
											{
												//Apply changes to data in SharedGameDataContainer
												targetUnit.accessory = targetAccessory;
											
												result = "changed";
											}
										}
									}
									break;
									
								default:
									result = "error";
									break;
							}
						}						
					}
					break;
				
				case "StartMatching":
					{
						String sessionId = request.getParameter("sessionId");
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						String teamIndexString = request.getParameter("teamIndex");
						int teamIndex = Integer.parseInt(teamIndexString);
						
						if (!SessionManager.updateConnectionStatus(sessionId))
							result = "sessionExpired";
						else
						{
							Player player = Linq.first(SharedGameDataContainer.getPlayers(), x -> x.Id == playerId);
							
							result = MatchDataContainer.assignPlayerToMatch(player, teamIndex);
						}
					}
					break;
				
				case "CheckMatchingStatus":
					{
						String sessionId = request.getParameter("sessionId");
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						
						if (!SessionManager.updateConnectionStatus(sessionId))
							result = "sessionExpired";
						else
						{
							Player player = Linq.first(SharedGameDataContainer.getPlayers(), x -> x.Id == playerId);
							
							if (MatchDataContainer.hasPlayerBeenAssignedToMatch(player))
								result = "matched";
							else
								result = "matching";
						}
					}
					break;
					
				case "CancelMatching":
				{
					String sessionId = request.getParameter("sessionId");
					String playerIdString = request.getParameter("playerId");
					int playerId = Integer.parseInt(playerIdString);
					
					if (!SessionManager.updateConnectionStatus(sessionId))
						result = "sessionExpired";
					else
					{
						Player player = Linq.first(SharedGameDataContainer.getPlayers(), x -> x.Id == playerId);
						
						result = MatchDataContainer.cancelPlayerMatching(player);
					}
				}
				break;
					
				case "GetMatchInfo":
					{
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						
						result = DataLoader.getMatchDataAsResponseString(playerId);
					}
					break;
					
				case "GetPlayerInfo":
					{
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						
						result = DataLoader.getMatchPlayerInfoAsResponseString(playerId);
					}
					break;
				
				case "GetOpponentInfo":
					{
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						
						result = DataLoader.getMatchOpponentInfoAsResponseString(playerId);
					}
					break;
					
				case "ChangeUnit":
					{
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						String unitIndexString = request.getParameter("unitIndex");
						int unitIndex = Integer.parseInt(unitIndexString);
						
						MatchDataContainer.changeUnit(playerId, unitIndex);
					}
					break;
					
				case "MoveUnit":
					{
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						String destinationString = request.getParameter("destination");
						_2DCoord destination = StringToCoord(destinationString);
						
						MatchDataContainer.moveUnit(playerId, destination);
					}
					break;
					
				case "Attack":
					{
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						String targetCoordsString = request.getParameter("targetCoords");
						List<_2DCoord> targetCoords = StringToCoords(targetCoordsString);
						
						MatchDataContainer.attack(playerId, targetCoords);
					}
					break;
					
				case "UseSkill":
					{
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						String skillName = request.getParameter("skillName");
						String targetCoordsString = request.getParameter("targetCoords");
						List<_2DCoord> targetCoords = StringToCoords(targetCoordsString);
						String secondaryTargetCoordsString = request.getParameter("secondaryTargetCoords");
						List<_2DCoord> secondaryTargetCoords = StringToCoords(secondaryTargetCoordsString);
						
						MatchDataContainer.useSkill(playerId, skillName, targetCoords, secondaryTargetCoords);
					}
					break;
					
				case "ChangeTurn":
					{
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						
						MatchDataContainer.changeTurn(playerId);
					}
					break;
				
				case "Concede":
					{
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						
						MatchDataContainer.concede(playerId);
					}
					break;
					
				case "GetMatchEndStatus":
					{
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						
						result = DataLoader.getMatchEndStatusAsResponseString(playerId);
						
						MatchDataContainer.confirmEndMatchStatusAcquisition(playerId);
					}
					break;
					
				case "GetMissingEventLogs":
					{
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						String latestLogIndexString = request.getParameter("latestLogIndex");
						int latestLogIndex = Integer.parseInt(latestLogIndexString);
						
						result = DataLoader.getMissingEventLogsAsResponseString(playerId, latestLogIndex);
					}
					break;
					
				case "GetMaxNumOfTargets":
					{
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						String skillName = request.getParameter("skillName");
						
						result = DataLoader.getMaxNumberOfTargetsAsResponseString(playerId, skillName);
					}
					break;
					
				case "GetMovableAndSelectableArea":
					{
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						
						result = DataLoader.getMovableAndSelectableAreaAsResponseString(playerId);
					}
					break;
				
				case "GetAttackTargetableAndSelectableArea":
					{
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						
						result = DataLoader.getAttackTargetableAndSelectableAreaAsResponseString(playerId);
					}
					break;
				
				case "GetSkillTargetableAndSelectableArea":
					{
						String playerIdString = request.getParameter("playerId");
						int playerId = Integer.parseInt(playerIdString);
						String skillName = request.getParameter("skillName");
						
						result = DataLoader.getSkillTargetableAndSelectableAreaAsResponseString(playerId, skillName);
					}
					break;
					
				case "RollGacha":
					{
						String sessionId = request.getParameter("sessionId");
						int playerId = SessionManager.getPlayerId(sessionId);
						String gachaIdString = request.getParameter("gachaId");
						int gachaId = Integer.parseInt(gachaIdString);
						String dispensationOptionIdString = request.getParameter("dispensationOptionId");
						int dispensationOptionId = Integer.parseInt(dispensationOptionIdString);
						
						if (!SessionManager.updateConnectionStatus(sessionId))
							result = "sessionExpired";
						else
						{
							Gacha gacha = Linq.firstOrDefault(SharedGameDataContainer.getGachas(), x -> x.Id == gachaId);
							if (gacha == null)
								result = "error";
							else
							{
								boolean isDispensationOptionAvailable = Linq.any(gacha.getDispensationOptions(), x -> x.id == dispensationOptionId);
								if (!isDispensationOptionAvailable)
									result = "error";
								else
								{
									int remainingAttempts = Linq.first(SharedGameDataContainer.getGachaDispensatioAvailabilityInfos(),
																		x -> x.playerId == playerId && x.gachaId == gacha.Id && x.dispensationOptionId == dispensationOptionId)
																		.remainingAttempts;
									
									if (remainingAttempts != 0)
									{
										List<Object> objects = gacha.dispenseObjects(con, playerId, dispensationOptionId);
										if (objects == null)
											result = "error";
										else
										{
											SharedGameDataContainer.addGachaObjects(playerId, gacha.gachaClassification, objects);
											result = DataLoader.getGachaObjectsAsResponseString(gacha.gachaClassification, objects);
										}
									}
									else
										result = "error";
								}
							}
						}
					}
					break;
					
				default:
					break;
			}
			
			PrintWriter printWriter = response.getWriter();
			printWriter.flush();
			printWriter.print(result);
			
			doGet(request, response);
			
			if (!subject.equals("CheckConnection") //String is empty
					&& !subject.equals("LoadCoreGameData") //String is too long! //Display custom message
					&& !subject.equals("CheckMatchingStatus") //Called too often!
					&& !subject.equals("CheckSessionsValidity") //Called too often!
					&& !subject.equals("UpdateConnectionStatus") //Called too often!
					&& !subject.equals("CheckForUpdate") //Called too often!
					&& !subject.equals("CheckPlayerCredentialsValidityAndAttemptToLogin") //Display custom message
					&& !subject.equals("ChangeUnit") //Response will always be empty
					&& !subject.equals("MoveUnit") //Response will always be empty
					&& !subject.equals("Attack") //Response will always be empty
					&& !subject.equals("UseSkill") //Response will always be empty
					&& !subject.equals("ChangeTurn") //Response will always be empty
					&& !subject.equals("Concede") //Response will always be empty
					&& !subject.equals("GetMovableAndSelectableArea") //String might be long!
					&& !subject.equals("GetAttackTargetableAndSelectableArea") //String might be long!
					&& !subject.equals("GetSkillTargetableAndSelectableArea") //String might be long!
					&& !subject.equals("GetMissingEventLogs")) //Called too often!
			{
				System.out.println("Response: " + result);	
			}
			
			if (subject.equals("LoadCoreGameData"))
			{
				if (result.length() > 20) //It is not an error message
					System.out.println("Provided game data!");
				else
					System.out.println("Did not provide game data.");
			}
			
			if (subject.equals("CheckPlayerCredentialsValidityAndAttemptToLogin"))
			{
				String message = "";
				if (result.contains("success:"))
				{
					String sessionId = result.substring("success:".length());
					int numOfActiveSessions = SessionManager.getNumberOfSessions();
					
					message = "New session (" + sessionId + ") established!"
							+ "\nWe have " + String.valueOf(numOfActiveSessions) + " active session(s)!";
				}
				else
					message = result;
				
				System.out.println(message);
			}
			
			con.close();
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}
	
	private static List<_2DCoord> StringToCoords(String _string)
	{
		List<_2DCoord> result = new ArrayList<_2DCoord>();
		
		if (!_string.equals(""))
		{
			String[] coordStrings = _string.split(";");
			
			for (String coordString : coordStrings)
			{
				result.add(StringToCoord(coordString));
			}
		}
		
		return result;
	}
	
	private static _2DCoord StringToCoord(String _string)
	{
		_string = _string.substring(1, _string.length() - 1); //Remove the parenthesis
		String[] xyStrings = _string.split(","); //Get the sections representing the x and y of the coord
		int x = Integer.parseInt(xyStrings[0]);
		int y = Integer.parseInt(xyStrings[1]);
		
		return new _2DCoord(x, y);
	}
}
