package zestaw3.client.gui;

import zestaw3.client.logic.UserGameBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameGUI extends JFrame {

    private String playerNick;

    public GameGUI(String playerNick){
        this.playerNick = playerNick;

        // create left panel
        JPanel playerPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel(playerNick));

        JButton doneButton = new JButton("Done");
        topPanel.add(doneButton);

        playerPanel.add(topPanel, BorderLayout.PAGE_START);

        UserGameBoard playerBoard = new UserGameBoard(10, 5, "xD");
        playerPanel.add(playerBoard, BorderLayout.CENTER);

        doneButton.addActionListener(
            new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                     if(playerBoard.finishSettingShips())
                         doneButton.setVisible(false);
                 }
            }
        );

        //create right panel
        JPanel opponentPanel = new JPanel();
        GameBoard opponentBoard = new GameBoard(10, "XDDDD") {
            @Override
            public void movePlayed(int x, int y) {
                System.out.println("row " + x + "col " + y);
            }

            @Override
        public String getNick() {
                return "xD";
            }
        };
        opponentPanel.add(opponentBoard);

        this.setLayout(new FlowLayout());
        this.add(playerPanel);
        this.add(opponentPanel);

        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.pack();
    }
}
