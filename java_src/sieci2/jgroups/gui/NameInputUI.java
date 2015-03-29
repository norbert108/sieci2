package sieci2.jgroups.gui;

import sieci2.jgroups.logic.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NameInputUI extends JFrame{

    private JButton okButton = new JButton("OK");
    private JTextField nicknameField = new JTextField("nickname");

    private void init(){
        this.setLayout(new BorderLayout());

        nicknameField.setPreferredSize(new Dimension(150, 20));

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(nicknameField);
        panel.add(okButton);
        this.add(panel);

        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.pack();
    }

    public NameInputUI(){
        init();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChatClientUI(new ChatClient(nicknameField.getText()));
                dispose();
            }
        });
    }

    public NameInputUI(ChatClient chatClient){
        init();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chatClient.createChannel(nicknameField.getText());
                dispose();
            }
        });
    }
}
