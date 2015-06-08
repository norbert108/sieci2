package zestaw3.client.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// TODO: autoresize game board
// TODO: fix field ids over 27
// TODO: cell colors
// TODO: square cells

public abstract class GameBoard extends JPanel{
    protected String nick;
    private JTable board;
    protected DefaultTableModel boardTableModel;

    public abstract void movePlayed(int x, int y);
    public abstract String getNick();

    public GameBoard(Integer boardSize, String nick){
        this.nick = nick;

        this.setLayout(new FlowLayout());

        boardTableModel = new DefaultTableModel();
        this.board = new JTable(boardSize, boardSize + 1);
        this.board.setModel(boardTableModel);

        // add click listener
        this.board.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable target = (JTable) e.getSource();
                int row = target.getSelectedRow();
                int column = target.getSelectedColumn();

                movePlayed(row, column);
            }
        });

        // create column identifiers
        String[] columnIdentifiers = new String[boardSize + 1];

        columnIdentifiers[0] = "";

        char firstId = 'A';
        int overflow = 0;
        for(int i=1; i<boardSize + 1; i++){
            columnIdentifiers[i] = String.valueOf((char) ((firstId + i - 1) % (26 + firstId))) + (overflow != 0 ? overflow : "");
            if((i - 1) % 26 == 0 && (i - 1) != 0) overflow++;
        }
        boardTableModel.setColumnIdentifiers(columnIdentifiers);

        // create row identifiers
        boardTableModel.setRowCount(boardSize);
        for(int i=0; i < boardSize; i++){
            boardTableModel.setValueAt(i+1, i, 0);
        }

        // fill table with empty strings
        for(int i=1; i<boardSize+1; i++){
            for(int j=0; j<boardSize; j++){
                boardTableModel.setValueAt("", j, i);
            }
        }

        this.add(new JScrollPane(board));
        this.setVisible(true);
    }
}
