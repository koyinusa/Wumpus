import java.util.Scanner;
import java.util.Random;

public class Game {

    public static final int MAX_ELEMENT = 3;
    public static final int MIN_ELEMENT = 0;
    
    private Scanner input = new Scanner(System.in);
    private GameItem[][] board = new GameItem[4][4];
    private int gameScore;

    private int playerPositionXAxis;
    private int playerPositionYAxis;

    public Game(){
        //Initialize the board on object initialization
        setBoard();
    }

    private int getRandom(int max){
        Random random = new Random();
        return random.nextInt(max);
    }

    private void positionRandomItem(GameItem item){
        while(true){
            int xAxis = getRandom(3);
            int yAxis = getRandom(3);
            if (board[xAxis][yAxis] == null){
                board[xAxis][yAxis] = item;
                return;
            }
        }
    }

    private void fillClearGroud(){
        int playerPosition = getRandom(9);
        int emptyCounter = 0;
        for (int xAxis = 0; xAxis < 4; xAxis ++){
            for (int yAxis = 0; yAxis < 4; yAxis ++){
                if (board[xAxis][yAxis] == null){
                    board[xAxis][yAxis] = new ClearGroud();

                    //Position the player on a ClearGroud position
                    if (emptyCounter == playerPosition){
                        this.playerPositionXAxis = xAxis;
                        this.playerPositionYAxis = yAxis;
                    }

                    emptyCounter++;
                }
            }
        }
    }

    public void runGame(){
        display();
        senseNearby();
        menu();
    }

    private void setBoard(){

        board = new  GameItem[4][4];

        //Set the position of the Wimpus
        board[getRandom(3)][getRandom(3)] = new Wumpus();
        
        //Position the Golds
        int maxGold = getRandom(2) + 1;
        for (int i = 0; i< maxGold; i++){
            positionRandomItem(new Gold());
        }

        //Position the Pits
        for (int i = 0; i< 3; i++){
            positionRandomItem(new Pit());
        }

        //Fill the remaining empty spaces with Clear ground
        fillClearGroud();
    }

    private void display(){
        for (int x = 0; x<= MAX_ELEMENT; x++){
            System.out.println();
            for (int y = 0; y<= MAX_ELEMENT; y++){
                if (x == playerPositionXAxis && y == playerPositionYAxis){
                    //Display the player position as *
                    System.out.print(String.format("%s \t", "*"));                    
                }
                else {
                    System.out.print(String.format("%c \t", board[x][y].display()));
                }
            }
        }
    }

    private String getSenseText(GameItem gameItem){
        if (gameItem instanceof Pit){
            return "Breeze";
        }
        else if (gameItem instanceof Gold){
            return "Faint Glitter";
        }
        else if (gameItem instanceof Wumpus){
            return "Vile Smell";
        }
        else {
            return "NONE";
        }
    }

    private int transposePosition(int position){
        if (position < MIN_ELEMENT){
            return MAX_ELEMENT;
        }
        else if (position > MAX_ELEMENT){
            return MIN_ELEMENT;
        }
        else {
            return position;
        }
    }

    private void senseNearby(){
        GameItem topItem = board[playerPositionXAxis][transposePosition(playerPositionYAxis-1)];
        System.out.println("TOP ITEM: "+getSenseText(topItem));
        
        GameItem rightItem = board[transposePosition(playerPositionXAxis+1)][playerPositionYAxis];
        System.out.println("RIGHT ITEM: "+getSenseText(rightItem));

        GameItem leftItem = board[transposePosition(playerPositionXAxis-1)][playerPositionYAxis];
        System.out.println("LEFT ITEM: "+getSenseText(leftItem));

        GameItem bottomItem = board[playerPositionXAxis][transposePosition(playerPositionYAxis+1)];
        System.out.println("BOTTOM ITEM: "+getSenseText(bottomItem));
    }

    private void requestInput(){
        System.out.println();
        int menu = input.nextInt();
        if (menu == 1){
            movePlayerLeft();
        }
        else if (menu == 2){
            movePlayerRight();
        }
        else if (menu == 3){
            movePlayerUp();
        }
        else if (menu == 4){
            movePlayerDown();
        }
        else if (menu == 5){
            quit();
            return;
        }

        calculateMovement();
        display();
        requestInput();
    }

    private void menu(){
        System.out.println("======Wumpus=======");
        System.out.println("Presss 1 to Move player left");
        System.out.println("Presss 2 to Move player right");
        System.out.println("Presss 3 to Move player up");
        System.out.println("Presss 4 to Move player down");
        System.out.println("Presss 5 to Quit");
        requestInput();
    }

    private void calculateMovement(){
        GameItem gameItem = board[playerPositionXAxis][playerPositionYAxis];
        if (gameItem instanceof Pit || gameItem instanceof Wumpus){
            //Player dies
            System.out.println("Player dead");
            quit();
            return;
        }
        else if (gameItem instanceof Gold){
            gameScore += 1;
            gameItem = new ClearGroud();
        }
    }

    private void movePlayerLeft(){
        if (playerPositionYAxis != MIN_ELEMENT){
            playerPositionYAxis += -1;
        }
        else {
            playerPositionYAxis = MAX_ELEMENT;
        }
    }

    private void movePlayerRight(){
        if (playerPositionYAxis != MAX_ELEMENT){
            playerPositionYAxis += 1;
        }
        else {
            playerPositionYAxis = MIN_ELEMENT;
        }
    }

    private void movePlayerUp(){
        if (playerPositionXAxis != MIN_ELEMENT){
            playerPositionXAxis += -1;
        }
        else {
            playerPositionXAxis = MAX_ELEMENT;
        }
    }

    private void movePlayerDown(){
        if (playerPositionXAxis != MAX_ELEMENT){
            playerPositionXAxis += 1;
        }
        else {
            playerPositionXAxis = MIN_ELEMENT;
        }
    }


    private void quit(){
        System.exit(0);
    }

}