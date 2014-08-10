import java.util.Random;

import comp102x.ColorImage;
import comp102x.assignment.GameLogic;
import comp102x.assignment.GameRecord;
import comp102x.assignment.Goal;

public class StudentLogic implements GameLogic{
    
    /**
     * Calculates correct image and it's position and scale from passed information
     * 
     * @param depthImages    an array containing football images at different depths
     * @param initialStep    the starting step size
     * @param currentStep    the size of the current step
     * @param finalStep      the size of the final step
     * @param initialScale   the sacle at the start
     * @param finalScale     the desired scale at the end
     * @param initialX       the position in the X direction at the start
     * @param finalX         the X location at the end
     * @param initialY       the position in the Y direction at the start
     * @param finalY         the Y location at the end      
     * @return               a colour image for an intermediate part of the animation
     */
    public ColorImage generateIntermediateFootballImage(ColorImage[] depthImages, int initialStep, int currentStep, int finalStep, double initialScale, double finalScale, int initialX, int finalX, int initialY, int finalY) {
        
        // calculate index, position and scale using given formulas.
        /**
         * The index of the image in the depthImage array
         */
        int imageIndex = ((depthImages.length - 1) * (currentStep - initialStep)) / (finalStep - initialStep);
        /**
         * The X position intermediate image should be displayed at
         */
        int xPosition = initialX + ((finalX - initialX)*(currentStep - initialStep)) / (finalStep - initialStep);
        /**
         * The Y position intermediate image should be displayed at
         */
        int yPosition = initialY + ((finalY - initialY)*(currentStep - initialStep)) / (finalStep - initialStep);
        /**
         * The scale of the intermediate image
         */
        double scale = initialScale + ((finalScale - initialScale)*(currentStep - initialStep)) / (finalStep - initialStep);
        
        //create the new ColorImage that will be returned.
        /**
         * Contains the end result of the function
         */
        ColorImage genImg = depthImages[imageIndex];
        
        //set it's X, Y and scale to those calculated.
        genImg.setX(xPosition);
        genImg.setY(yPosition);
        genImg.setScale(scale);
        
        //return the image
        return genImg;
    }

/**
 * Finds Goals flagged as moveable and swaps there positions with hit goals
 * Randomness generated by finding all posible goals and using a random number to select from them
 * 
 * @param Goal              A 2D array of the displayed goals
 */
    public void updateGoalPositions(Goal[][] goals) {
        // write your code after this line
        /**
         * Contains the locations available to be moved to
         * options[0] x locations options[1] ylocations
         * maximum 8 posibilities
         */
        int[][] options = new int[2][8];
        /**
         * The number of available location options
         */
        int numOps = 0;
        /**
         * Contains information as to if a Goal has already been moved
         */
        boolean[][] moved = new boolean[goals.length][goals[0].length]; //false default by lang spec
        /**
        * Used in creation of the random numbers
        */
        Random randomObject = new Random();
        /**
        * The random index of options which will be switched with current goal
        */
        int randInt  = 0;
        /**
         * A temporary Goal variable to allow swap to take place
         */
        Goal tempGoal;
        //looping over all goals
        for (int i = 0; i < goals.length; i++) {
            for (int j = 0; j < goals[i].length; j++) {
                if ((goals[i][j].getType() == Goal.MOVABLE) && !(moved[i][j]) && !(goals[i][j].isHit())) {
                    numOps = 0;
                    //checking which goals are hit and not yet moved and moveable
                    //sanity checks on i and j to prevent index out of bounds
                    if ((i != 0) && (j != 0) && (goals[i-1][j-1].isHit()) && !(moved[i-1][j-1])) {
                        options[0][numOps] = i-1;
                        options[1][numOps] = j-1;
                        numOps++;
                    }
                    if ((i != 0) && (goals[i-1][j].isHit()) && !(moved[i-1][j])) {
                        options[0][numOps] = i-1;
                        options[1][numOps] = j;
                        numOps++;
                    }
                    if ((i != 0) && (j != goals[i].length - 1) && (goals[i-1][j+1].isHit()) && !(moved[i-1][j+1])) {
                        options[0][numOps] = i-1;
                        options[1][numOps] = j+1;
                        numOps++;
                    }
                    if ((j != 0) && (goals[i][j-1].isHit()) && !(moved[i][j-1])) {
                        options[0][numOps] = i;
                        options[1][numOps] = j-1;
                        numOps++;
                    }
                    if ((j != goals[i].length - 1) && (goals[i][j+1].isHit()) && !(moved[i][j+1])) {
                        options[0][numOps] = i;
                        options[1][numOps] = j+1;
                        numOps++;
                    }
                    if ((i != goals.length - 1) && (j != 0) && (goals[i+1][j-1].isHit()) && !(moved[i+1][j-1])) {
                        options[0][numOps] = i+1;
                        options[1][numOps] = j-1;
                        numOps++;
                    }
                    if ((i != goals.length - 1) && (goals[i+1][j].isHit()) && !(moved[i+1][j])) {
                        options[0][numOps] = i+1;
                        options[1][numOps] = j;
                        numOps++;
                    }
                    if ((i != goals.length - 1) && (j != goals[i].length - 1) && (goals[i+1][j+1].isHit()) && !(moved[i+1][j+1])) {
                        options[0][numOps] = i+1;
                        options[1][numOps] = j+1;
                        numOps++;
                    }
                    if (numOps == 0) {
                        ; //do nothing, no goals to swap with
                    }
                    else {
                        //assigning  random number
                        randInt  = randomObject.nextInt(numOps);  
                        //swaps the two Goals
                        tempGoal = goals[options[0][randInt]][options[1][randInt]];
                        goals[options[0][randInt]][options[1][randInt]] = goals[i][j];
                        goals[i][j] = tempGoal;
                        moved[options[0][randInt]][options[1][randInt]] = true;
                        moved[i][j] = true;
                    }
                    
                }
            }
        }
            
       
        
        
        
        
    }

    /**
     * updates the highscore table by incorperating the passed score
     * 
     * @param highScoreRecords      array of up to 10 records containing previous high scores
     * @param name                  the name of user creating new record
     * @param level                 the dificulty of the game
     * @param score                 the achieved score
     * @return                      an array of up to 10 sorted records containing new high scores
     */
    public GameRecord[] updateHighScoreRecords(GameRecord[] highScoreRecords, String name, int level, int score) {
        /**
         * a record for the passed information
         */
        GameRecord newRecord = new GameRecord(name,level,score);
        //check if no records
        if (highScoreRecords.length == 0) {
            //create a new array containg one record
            return new GameRecord[] {newRecord};
        }
        
        /**
         * Flag for if a record requires deleting, -1 for none else index of record.
         */
        int flag = -1;
        //check if user already has a record
        for (int i = 0; i < highScoreRecords.length; i++) {
            if (highScoreRecords[i].getName().equals(newRecord.getName())) {
                //user already has record
                if (compareRecord(newRecord, highScoreRecords[i]) == 1) {
                    //new record is better
                    //flag old record for removal
                    flag = i;
                    //loop may now be exited as only one record has that name
                    break;
                }
                else {
                    //old record is better or equal, no change
                    return highScoreRecords;
                }
            }
        }
        //insertion sort in record
        /**
         * Stores size of array to be returned
         */
        int newSize = 1+ highScoreRecords.length;
        if (flag != -1) {
            newSize--;
        }
        if (newSize > 10) {
            newSize = 10;
        }
        /**
         * Array into which recors will be sorted
         */
        GameRecord[] newScoreRecords = new GameRecord[newSize];
        /**
         * Index to insert at
         */
        int index = 0;
        /**
         * Has the new record been inserted? 
         */
        boolean inserted = false;
        for (int i = 0; index < newScoreRecords.length; index++) {
            //removes item flagged for deletion by skipping over it in original array
            if (i == flag) {
                i++;
                //inserts new record if not already done so
                if (!(inserted)) {
                    newScoreRecords[index] = newRecord;
                    continue;
                }
            }
            if (i == highScoreRecords.length) {
                //checks for out of bounds of highScoreRecords
                //inserts new record if not already done so
                if (!(inserted)) {
                    newScoreRecords[index] = newRecord;
                    continue;
                }
            }
            if (!(inserted)) {
                //record not already inserted, check if should go next
                if (compareRecord(highScoreRecords[i],newRecord) != -1) {
                    //record not next, copy across record
                    newScoreRecords[index] = highScoreRecords[i++];
                }
                else {
                    //record next, insert it an flag inserted
                    newScoreRecords[index] = newRecord;
                    inserted = true;
                }
            }
            else {
                //coppying remainder of array, loop condition will drop excessive elemnts
                newScoreRecords[index] = highScoreRecords[i++];
            }
        }
        return newScoreRecords;
    }
    
    
    /**
     * compares two GameRecords and compares their scores
     * 
     * @param A                     the first record to be compared
     * @param B                     the second record to be compared
     * @return                      1 if A has > score/level than B, 0 if equal, -1 if B > A
     */
    public int compareRecord(GameRecord A, GameRecord B) {
        if (A.getScore() > B.getScore()) {
            //A has greater score
            return 1;
        }
        else if (A.getScore() == B.getScore()) {
            //scores equal
            if (A.getLevel() > B.getLevel()) {
                //A greater level
                return 1;
            }
            else if (A.getLevel() == B.getLevel()){
                //equal score and level
                return 0;
            }
            else {
                //B greater level
                return -1;
            }
        }
        else {
            //B greater score
            return -1;
        }        
    }
}

