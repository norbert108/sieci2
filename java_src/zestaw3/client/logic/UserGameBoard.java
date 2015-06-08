package zestaw3.client.logic;

import zestaw3.client.gui.GameBoard;

public class UserGameBoard extends GameBoard {

    private int shipCount = 0;
    private int maxShips;
    private boolean shipsPlaced = false;

    public UserGameBoard(int boardSize, int maxShips, String nick){
        super(boardSize, nick);

        this.maxShips = maxShips;
    }

    /***
     * Method should be available only during ships setting phase
     * @param x
     * @param y
     */
    @Override
    public void movePlayed(int x, int y) {
        if(shipsPlaced) return;

        if(this.boardTableModel.getValueAt(x, y).equals("X")){
            this.boardTableModel.setValueAt("", x, y);
            shipCount--;
        } else {
            if (shipCount < maxShips){
                this.boardTableModel.setValueAt("X", x, y);
                shipCount++;
            } else {
                System.out.println("To many ships, remove some or finish.");
            }
        }
    }

    @Override
    public String getNick() {
        return this.nick;
    }

    public boolean finishSettingShips(){
        if(shipCount == maxShips){
            shipsPlaced = true;
            return true;
        } else {
            System.out.println("Incorrect number of ships. " + shipCount + " placed, " + maxShips + " required.");
            return false;
        }
    }
}
