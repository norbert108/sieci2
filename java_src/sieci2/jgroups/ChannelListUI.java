package sieci2.jgroups;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChannelListUI extends JFrame{

    private JPanel controlsPanel = new JPanel();
    private JButton connectButton = new JButton("Connect");
    private JButton newChannelButton = new JButton("New channel");

    private JTable channelListTable = new JTable();
    private DefaultTableModel channelListTableModel = new DefaultTableModel();
    private JTable memberListTable = new JTable();

    private JPanel channelListPanel = new JPanel(new BorderLayout());
    private JPanel memberListPanel = new JPanel(new BorderLayout());
    private DefaultTableModel memberListTableModel = new DefaultTableModel();

    public ChannelListUI(){
        this.setLayout(new GridLayout(1, 2));

        channelListTableModel.setColumnIdentifiers(new String[]{"Channel"});
        channelListTable.setModel(channelListTableModel);
        channelListTable.setPreferredSize(new Dimension(150, 300));
        channelListPanel.add(channelListTable);
        channelListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        memberListTableModel.setColumnIdentifiers(new String[]{"nickname"});
        memberListTable.setModel(memberListTableModel);
        memberListTable.setPreferredSize(new Dimension(150, 200));
        memberListPanel.add(memberListTable, BorderLayout.CENTER);
        memberListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Connect");
            }
        });
        controlsPanel.add(connectButton);

        newChannelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("New Channel");
            }
        });
        controlsPanel.add(newChannelButton);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(memberListPanel, BorderLayout.CENTER);
        rightPanel.add(controlsPanel, BorderLayout.PAGE_END);

        this.add(channelListPanel);
        this.add(rightPanel);

        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.pack();
    }
}
