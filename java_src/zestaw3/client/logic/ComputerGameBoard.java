package zestaw3.client.logic;

import zestaw3.client.gui.GameBoard;

public class ComputerGameBoard extends GameBoard {

    private String playerName;

    public ComputerGameBoard(int maxShips){
        super(maxShips, "CPU");

        this.playerName = "CPU";
    }


    /***
     * Function called when player clicks on opponents board, which triggers computer response
     * @param x
     * @param y
     */
    @Override
    public void movePlayed(int x, int y) {

    }

    @Override
    public String getNick(){
        return this.nick;
    }
}
