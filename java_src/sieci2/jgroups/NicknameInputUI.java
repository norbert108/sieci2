package sieci2.jgroups;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NicknameInputUI extends JFrame{

    public NicknameInputUI(){
        this.setLayout(new BorderLayout());

        JTextField nicknameField = new JTextField("nickname");
        nicknameField.setPreferredSize(new Dimension(150, 20));
        JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChatClientUI(new ChatClient(nicknameField.getText()));
                dispose();
            }
        });

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
}
