package zestaw2.gui;

import zestaw2.logic.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NameInputUI extends JFrame {

    private JButton okButton = new JButton("OK");
    private JTextField nameField = new JTextField();

    private void init() {
        this.setLayout(new BorderLayout());

        nameField.setPreferredSize(new Dimension(150, 20));

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(nameField);
        panel.add(okButton);
        this.add(panel);

        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.pack();
    }

    public NameInputUI() {
        init();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = nameField.getText();

                if (!nickname.equals("")) {
                    new ChannelListUI(new ChatClient(nickname));
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Nickname cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public NameInputUI(ChatClient chatClient) {
        init();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String channelName = nameField.getText();

                try {
                    chatClient.joinChannel(channelName);
                    dispose();
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Channel name must be valid ipv4 multicast address!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
