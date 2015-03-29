package sieci2.jgroups.gui;

import sieci2.jgroups.logic.ChatClient;
import sieci2.jgroups.logic.ChatOperationProtos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChannelListUI extends JFrame{

    private ChatClient chatClient;

    private JPanel controlsPanel = new JPanel();
    private JButton connectButton = new JButton("Connect");
    private JButton newChannelButton = new JButton("New channel");

    private JTable channelListTable = new JTable();
    private DefaultTableModel channelListTableModel = new DefaultTableModel();
    private JTable memberListTable = new JTable();

    private JPanel channelListPanel = new JPanel(new BorderLayout());
    private JPanel memberListPanel = new JPanel(new BorderLayout());
    private DefaultTableModel memberListTableModel = new DefaultTableModel();

    public ChannelListUI(ChatClient chatClient){
        this.chatClient = chatClient;

        this.setLayout(new GridLayout(1, 2));

        channelListTableModel.setColumnIdentifiers(new String[]{"Channel"});
        channelListTable.setModel(channelListTableModel);
        channelListTable.setPreferredSize(new Dimension(150, 300));
        channelListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                loadChannelMembersList((String)channelListTableModel.getValueAt(channelListTable.getSelectedRow(), 0));
            }
        });
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
                String channelName = (String)channelListTableModel.getValueAt(channelListTable.getSelectedRow(), 0);
                if(channelName != null && !channelName.equals("")){
                    try{
                        chatClient.joinChannel(channelName);
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });
        controlsPanel.add(connectButton);

        newChannelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NameInputUI(chatClient);
            }
        });
        controlsPanel.add(newChannelButton);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(memberListPanel, BorderLayout.CENTER);
        rightPanel.add(controlsPanel, BorderLayout.PAGE_END);

        this.add(channelListPanel);
        this.add(rightPanel);

        loadChannelList();

        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.pack();
    }

    private void loadChannelList(){
        this.channelListTableModel.setRowCount(0);
        for(String channelName:  chatClient.getState().keySet()){
            this.channelListTableModel.addRow(new String[] {channelName});
        }
        this.channelListTableModel.fireTableDataChanged();
    }

    private void loadChannelMembersList(String channel){
        this.memberListTableModel.setRowCount(0);
        for(ChatOperationProtos.ChatAction chatAction: chatClient.getState().get(channel)){
            this.memberListTableModel.addRow(new String[] {chatAction.getNickname()});
        }
        this.memberListTableModel.fireTableDataChanged();
    }
}
