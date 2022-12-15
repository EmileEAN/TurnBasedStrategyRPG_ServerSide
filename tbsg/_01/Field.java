package eean_games.tbsg._01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eean_games.main._2DCoord;
import eean_games.tbsg._01.enumerable.eTileType;
import eean_games.tbsg._01.player.Player;
import eean_games.tbsg._01.player.PlayerOnBoard;
import eean_games.tbsg._01.unit.UnitInstance;

public class Field
{
    private Field(Board _board, PlayerOnBoard _player1, PlayerOnBoard _player2)
    {
        Board = _board;

        m_players = new PlayerOnBoard[] { _player1, _player2 };

        //Set Initial position for each unit that Players[0] owns
        for (int i = 0; i < m_players[0].AlliedUnits.size(); i++)
        {
            switch(i)
            {
                default: //case 0;
                    Board.Sockets[1][0].Unit = m_players[0].AlliedUnits.get(i);
                    break;
                case 1:
                    Board.Sockets[2][1].Unit = m_players[0].AlliedUnits.get(i);
                    break;
                case 2:
                    Board.Sockets[3][0].Unit = m_players[0].AlliedUnits.get(i);
                    break;
                case 3:
                    Board.Sockets[4][1].Unit = m_players[0].AlliedUnits.get(i);
                    break;
                case 4:
                    Board.Sockets[5][0].Unit = m_players[0].AlliedUnits.get(i);
                    break;
            }
        }

        int size = CoreValues.SIZE_OF_A_SIDE_OF_BOARD;
        //Set Initial position for each unit that Players[1] owns
        for (int i = 0; i < m_players[1].AlliedUnits.size(); i++)
        {
            switch (i)
            {
                default: //case 0;
                    Board.Sockets[size - 2][size - 1].Unit = m_players[1].AlliedUnits.get(i);
                    break;
                case 1:
                    Board.Sockets[size - 3][size - 2].Unit = m_players[1].AlliedUnits.get(i);
                    break;
                case 2:
                    Board.Sockets[size - 4][size - 1].Unit = m_players[1].AlliedUnits.get(i);
                    break;
                case 3:
                    Board.Sockets[size - 5][size - 2].Unit = m_players[1].AlliedUnits.get(i);
                    break;
                case 4:
                    Board.Sockets[size - 6][size - 1].Unit = m_players[1].AlliedUnits.get(i);
                    break;
            }
        }

        //Assign all existing units into list
        units = new ArrayList<UnitInstance>();
        for (PlayerOnBoard pob : m_players)
        {
            for (UnitInstance unit : pob.AlliedUnits)
            {
                units.add(unit);
            }
        }
    }

    //Public Read-only Fields
    public final Board Board;
    //End Public Read-only Fields
    
    //Getters
    public List<PlayerOnBoard> getPlayers() { return Collections.unmodifiableList(Arrays.asList(m_players)); }
    
    public List<UnitInstance> getUnits() { return Collections.unmodifiableList(units); }
    //End Getters
    
    //Private Fields
    private List<UnitInstance> units;
    //End Private Fields
    
    //Private Read-only Fields
    private final PlayerOnBoard[] m_players;
    //End Private Read-only Fields
    
    //Public Functions
    public static Field NewField(Player _player1, int _player1TeamIndex, Player _player2, int _player2TeamIndex, List<eTileType> _tileSet)
    {
        Board board = new Board(_tileSet);

        PlayerOnBoard player1 = new PlayerOnBoard(_player1, _player1TeamIndex, true);
        PlayerOnBoard player2 = new PlayerOnBoard(_player2, _player2TeamIndex, false);

        if (player1.AlliedUnits.size() < 1 || player2.AlliedUnits.size() < 1)
        	return null;
        
        return new Field(board, player1, player2);
    }

    public int GetUnitIndex(UnitInstance _unit)
    {
        int unitIndex = 0;
        while (unitIndex < units.size())
        {
            if (units.get(unitIndex) == _unit)
                return unitIndex;

            unitIndex++;
        }

        return -1; // This will be returned when _unit was not found within units
    }

    public List<UnitInstance> GetUnitsInCoords(List<_2DCoord> _targetCoords)
    {
        List<UnitInstance> result = new ArrayList<UnitInstance>();
        for (_2DCoord targetCoord : _targetCoords)
        {
            if (IsCoordWithinBoard(targetCoord))
            {
            	UnitInstance unit = Board.Sockets[targetCoord.X][targetCoord.Y].Unit;
            	if (unit != null)
            		result.add(unit);
            }
        }

        return result;
    }

    public List<Socket> GetSocketsInCoords(List<_2DCoord> _targetCoords)
    {
        List<Socket> result = new ArrayList<Socket>();
        for (_2DCoord targetCoord : _targetCoords)
        {
            if (IsCoordWithinBoard(targetCoord))
            {
                result.add(Board.Sockets[targetCoord.X][targetCoord.Y]);
            }
        }

        return result;
    }

    /// <summary>
    /// PreCondition: _unit has been initialized successfully.
    /// PostCondition: If succeeded, will return a valid _2DCoord within Board. Will return _2DCoord(-1, -1) if failed.
    /// </summary>
    /// <returns></returns>
    public _2DCoord UnitLocation(UnitInstance _unit)
    {
        for (int x = 1; x <= CoreValues.SIZE_OF_A_SIDE_OF_BOARD; x++)
        {
            for (int y = 1; y <= CoreValues.SIZE_OF_A_SIDE_OF_BOARD; y++)
            {
                if (Board.Sockets[x - 1][y - 1].Unit == _unit)
                    return new _2DCoord(x - 1, y - 1);
            }
        }

        return new _2DCoord(-1, -1); // invalid coordinate for a board
    }

    public _2DCoord SocketLocation(Socket _socket)
    {
        for (int x = 1; x <= CoreValues.SIZE_OF_A_SIDE_OF_BOARD; x++)
        {
            for (int y = 1; y <= CoreValues.SIZE_OF_A_SIDE_OF_BOARD; y++)
            {
                if (Board.Sockets[x - 1][y - 1] == _socket)
                    return new _2DCoord(x - 1, y - 1);
            }
        }

        return new _2DCoord(-1, -1); // invalid coordinate for a board
    }
    
    public void RemoveNonAliveUnitFromBoard(UnitInstance _unit)
    {
        if (_unit == null)
            return;

        _2DCoord currentLocation = UnitLocation(_unit);
        if (!_unit.IsAlive
            && IsCoordWithinBoard(currentLocation))
            this.Board.Sockets[currentLocation.X][currentLocation.Y].Unit = null; // Remove unit assigned to the Socket
    }

    //public void RemoveNonAliveUnitsFromBoard()
    //{
    //    foreach (PlayerOnBoard p in Players)
    //    {
    //        foreach (UnitInstance u in p.AlliedUnits)
    //        {
    //            _2DCoord currentLocation = UnitLocation(u);

    //            if (!u.IsAlive
    //                && currentLocation.X != -1) // currentLocation.X == -1 means that UnitLocation(c) returned a coordinate out of the board (UnitBase not found)
    //                this.Board.Sockets[currentLocation.X, currentLocation.Y].Unit = null; // Remove unit assigned to the Socket
    //        }
    //    }
    //}

    public _2DCoord ToRealCoord(_2DCoord _referencePoint, _2DCoord _relativePointWithCorrectDirection)
    {
        return _referencePoint.sum(_relativePointWithCorrectDirection);
    }

    public _2DCoord RelativeCoordToCorrectDirection(PlayerOnBoard _ownerPlayer, _2DCoord _coord)
    {

        eFieldDirection realDirection = eFieldDirection.POSITIVE_Y; //Set this as default value

        //Set the actual value of realDirection
        if (_ownerPlayer == m_players[0])
            realDirection = eFieldDirection.POSITIVE_Y;
        else //if (_ownerPlayer == Players[1]) --> the perspective of Players[1] is 180 degrees different from that of Players[0]
            realDirection = eFieldDirection.NEGATIVE_Y;

        //Rotate _coord based on realDirection
        switch (realDirection)
        {
            case POSITIVE_X:
                _coord.Rotate90DegreesAnticlockwise(1);
                break;
            case NEGATIVE_Y:
                _coord.Rotate90DegreesAnticlockwise(2);
                break;
            case NEGATIVE_X:
                _coord.Rotate90DegreesAnticlockwise(3);
                break;
            default:
                break;
        }

        return _coord;
    }

    public boolean IsCoordWithinBoard(_2DCoord _coord)
    {
        if (_coord.X >= 0 && _coord.X <= CoreValues.SIZE_OF_A_SIDE_OF_BOARD - 1
                && _coord.Y >= 0 && _coord.Y <= CoreValues.SIZE_OF_A_SIDE_OF_BOARD - 1)
        {
            return true;
        }

        return false;
    }
    //End Public Methods
}
