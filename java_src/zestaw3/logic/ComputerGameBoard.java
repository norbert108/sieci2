package zestaw3.logic;

import zestaw3.gui.IGameBoard;

public class ComputerGameBoard extends IGameBoard {

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
