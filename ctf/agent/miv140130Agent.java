package ctf.agent;

import ctf.common.AgentEnvironment;
import jdk.nashorn.internal.objects.NativeDataView;
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

    static int pawn1xPos;
    static int pawn1yPos;
    static int pawn2xPos;
    static int pawn2yPos;
    
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
                whoAmI = 1;
                
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

                    pawn1yPos = (mapSize / 2) - 2;
                    pawn2yPos = (mapSize / 2) + 1;

                    if(inEnvironment.isAgentEast(inEnvironment.ENEMY_TEAM, false)) {
                        pawn1xPos = 0;
                        pawn2xPos = 0;
                    } //end if
                    else {
                        pawn1xPos = (mapSize - 1);
                        pawn2xPos = (mapSize - 1);
                    } //end else

                    printMap(gameMap);
                } // end nested if
                else {
                    pawn2Turn++;
                    return AgentAction.MOVE_NORTH;
                } // end nested else
            } // end else
        } // end if

        /*********************************BEHAVIORS*********************************************/

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