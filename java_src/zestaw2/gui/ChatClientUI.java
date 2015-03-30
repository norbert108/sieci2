package zestaw2.gui;

import zestaw2.logic.ChatClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
public class ChatClientUI extends JFrame {

    private ChatClient chatClient;
    private String channelName;

    private JTextPane chatWindow = new JTextPane();

    private JPanel bottomPanel = new JPanel();
    private JButton channelListButton = new JButton("Channels");
    private JTextField inputField = new JTextField();
    private JButton sendButton = new JButton("Send");

    public ChatClientUI(ChatClient chatClient, String channelName){
        this.chatClient = chatClient;
        this.channelName = channelName;

        this.setLayout(new BorderLayout());

        chatWindow.setPreferredSize(new Dimension(400, 600));
        chatWindow.setEditable(false);

        JPanel chatWindowPanel = new JPanel(new BorderLayout());
        chatWindowPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        chatWindowPanel.add(chatWindow);
        this.add(chatWindowPanel);

        channelListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChannelListUI(chatClient);
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chatClient.sendMessage(channelName, inputField.getText());
            }
        });

        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(channelListButton, BorderLayout.LINE_START);
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.LINE_END);
        this.add(bottomPanel, BorderLayout.PAGE_END);

        displayChatMessage("Connected to channel " + this.channelName + " as " + this.chatClient.getNickname());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                chatClient.leaveChannel(channelName);
            }
        });

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();
    }

    public void displayClientMessage(String nickname, String time, String message){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(time);
        stringBuilder.append(" ");
        stringBuilder.append(nickname);
        stringBuilder.append(": ");
        stringBuilder.append(message);

        addLine(stringBuilder.toString());
    }

    public void displayChatMessage(String message){
        String text = "* " + message + " *";

        this.addLine(text);
    }

    private void addLine(String text){
        StyledDocument document = (StyledDocument) this.chatWindow.getDocument();

        text += "\n";
        try {
            document.insertString(document.getLength(), text, null);
        } catch (BadLocationException e){
            e.printStackTrace();
        }
    }
}
