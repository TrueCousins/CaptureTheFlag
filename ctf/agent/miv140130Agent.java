package ctf.agent;

import ctf.common.AgentEnvironment;
import ctf.agent.Agent;
import ctf.common.AgentAction;

import java.util.Random;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class miv140130Agent extends Agent {
    static boolean firstTurn = true;
    static boolean mapSizeFound = false;

    static int mapSize;
    static int whoAmI; //1 is north pawn, 2 is south pawn
    static int pawn2Turn = 0;

    static int pawn1Row;
    static int pawn1Col;
    static int pawn2Row;
    static int pawn2Col;
    
    static char gameMap[][];
        
    //Stack<pathObj> pawn1Moves = new Stack<>();
    //Stack<pathObj> pawn2Moves = new Stack<>();
    public int getMove(AgentEnvironment inEnvironment) {
        int makeMove;
       
        if(firstTurn) { 

            if(inEnvironment.isAgentNorth(inEnvironment.OUR_TEAM, false))
                whoAmI = 2;
            else 
                whoAmI = 1;
            firstTurn = false;
        } //end if
        
        if(!mapSizeFound) {
            if(whoAmI == 1) {
                whoAmI = 2;
                return AgentAction.MOVE_SOUTH;
            } // end if
            else { // whoAmI == 2
                
                if(inEnvironment.isFlagNorth(inEnvironment.OUR_TEAM, true)) {
                    mapSize = 2 * (pawn2Turn + 2);
                    mapSizeFound = true;
                    System.out.println("pawn2Turn = " + pawn2Turn);
                    System.out.println("mapSize = " + mapSize);
                    gameMap = new char[mapSize][mapSize];

                    // fill map with "unexplored"" symbols
                    for(int i = 0; i < mapSize; i++) {
                        for(int j = 0; j < mapSize; j++) {
                            gameMap[i][j] = '.';
                        } // end nested for
                    } //end nested for

                    // initialize game map
                    for(int i = 0; i < mapSize; i++) {
                        if(i == pawn2Turn + 2) {
                            gameMap[i][0] = 'i';
                            gameMap[i][mapSize - 1] = 'i';
                        } // end if
                        else {
                            gameMap[i][0] = 'o';
                            gameMap[i][mapSize -1] = 'o';
                        } // end else
                    } //end for

                    pawn1Row = (mapSize / 2) - 2;
                    pawn2Row = (mapSize / 2) + 1;

                    if(inEnvironment.isAgentEast(inEnvironment.ENEMY_TEAM, false)) {
                        pawn1Col = 0;
                        pawn2Col = 0;
                    } //end if
                    else {
                        pawn1Col = (mapSize - 1);
                        pawn2Col = (mapSize - 1);
                    } //end else

                    printMap(gameMap);
                } // end nested if
                else {
                    whoAmI = 1;
                    pawn2Turn++;
                    return AgentAction.MOVE_NORTH;
                } // end nested else
            } // end else
        } // end if

        /*********************************BEHAVIORS*********************************************/

        boolean northBlocked = false;
        boolean southBlocked = false;
        boolean eastBlocked = false;
        boolean westBlocked = false;

        boolean northBlockByTeam = false;
        boolean southBlockByTeam = false;
        boolean eastBlockByTeam = false;
        boolean westBlockByTeam = false;

        int pawnRow;
        int pawnCol;

        if(whoAmI == 1) {
            pawnRow = pawn1Row;
            pawnCol = pawn1Col;
        } // end if
        else {
            pawnRow = pawn2Row;
            pawnCol = pawn2Col;
        } //end else

        // north obstacle check
        if(inEnvironment.isObstacleNorthImmediate()) {
            northBlocked = true;

            if(inEnvironment.isAgentNorth(inEnvironment.OUR_TEAM, true) || inEnvironment.isBaseNorth(inEnvironment.OUR_TEAM, true)) 
                northBlockByTeam = true;
                
            if(pawnRow != 0) {
                if(!northBlockByTeam) 
                    gameMap[pawnRow - 1][pawnCol] = 'x';
                else 
                    gameMap[pawnRow - 1][pawnCol] = 'o';
            } // end nested if
        } // end if

        // south obstacle check
        if(inEnvironment.isObstacleSouthImmediate()) {
            southBlocked = true;

            if(inEnvironment.isAgentSouth(inEnvironment.OUR_TEAM, true) || inEnvironment.isBaseSouth(inEnvironment.OUR_TEAM, true)) 
                southBlockByTeam = true;
                
            if(pawnRow != (mapSize - 1)) {
                if(!southBlockByTeam)
                    gameMap[pawnRow + 1][pawnCol] = 'x';
                else 
                    gameMap[pawnRow + 1][pawnCol] = 'o';
            } // end nested if
        } // end if

        // east obstacle check
        if(inEnvironment.isObstacleEastImmediate()) {
            eastBlocked = true;

            if(inEnvironment.isAgentEast(inEnvironment.OUR_TEAM, true) || inEnvironment.isBaseEast(inEnvironment.OUR_TEAM, true)) 
                eastBlockByTeam = true;
                
            if(pawnCol != (mapSize - 1)) {
                if(!eastBlockByTeam)
                    gameMap[pawnRow][pawnCol + 1] = 'x';
                else 
                 gameMap[pawnRow][pawnCol + 1] = 'o';
            } // end nested if
        } // end if

        // west obstacle check
        if(inEnvironment.isObstacleWestImmediate()) {
            westBlocked = true;

            if(inEnvironment.isAgentWest(inEnvironment.OUR_TEAM, true) || inEnvironment.isBaseWest(inEnvironment.OUR_TEAM, true)) 
                westBlockByTeam = true;
                
            if(pawnCol != 0) {
                if(!westBlockByTeam)
                    gameMap[pawnRow][pawnCol - 1] = 'x';
                else 
                    gameMap[pawnRow][pawnCol - 1] = 'o';
            } //end if
        } // end if

        if (whoAmI == 1) {
            whoAmI = 2; // change player for next turn
            
        } // end else
        else {
            whoAmI = 1;  // change player for next turn
           
        } // end else
        return 1;
    } // end getMove

    public void printMap(char gameMap[][]) {
        for(int i = 0; i < mapSize; i++) {
            for(int j = 0; j < mapSize; j++) 
                System.out.print(gameMap[i][j]);
            
            System.out.print("\n");
        } // end for
    } // end printMap
} // end miv140130Agent