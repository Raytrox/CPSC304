package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class Log_in {

    public JPanel Login;
    private JTextField UserID;
    private JButton loginButton;
    private JButton registerButton;
    private JPasswordField Pwd;
    Functions reg = new Functions();

    public Log_in() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection con = Connections.getConnection();
                    String user_id = UserID.getText();
                    char[] pwd_temp = Pwd.getPassword();
                    String pwd = String.valueOf(pwd_temp);
                    boolean logged;

                    try{
                        logged = reg.c_login(user_id,pwd,con);
                        if(logged){
                            //TODO: if log in success, jump to customer_UI, example:
                            JOptionPane.showMessageDialog(null,"success");
                            JFrame frame = JFrames.get_frame();
                            frame.setTitle("customer_UI");
                            customer_UI cui = new customer_UI();
                            frame.setContentPane(cui.customer_UI);
                            frame.pack();
                            frame.setVisible(true);
                            cui.setCID(user_id);
                        }else{
                            logged = reg.s_login(user_id,pwd,con);
                            if(logged){
                                //TODO: if log in success, jump to staff_UI
                                JOptionPane.showMessageDialog(null,"success");
                                JFrame frame = JFrames.get_frame();
                                frame.setTitle("staff_UI");
                                staff_UI sui = new staff_UI();
                                frame.setContentPane(sui.JPanel2);
                                frame.pack();
                                frame.setVisible(true);
                                sui.sid_s = user_id;
                            }else {
                                JOptionPane.showMessageDialog(null, "Fail, record not match");
                            }
                        }
                    }catch (java.sql.SQLException e2){
                        JOptionPane.showMessageDialog(null,e2.getMessage());
                    }
                }catch (java.sql.SQLException e1){
                    JOptionPane.showMessageDialog(null,"Connection error");
                }
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFrame frame = JFrames.get_frame();
                    frame.setTitle("Register");
                    frame.setContentPane(new gui().panel1);
                    frame.pack();
                    frame.setVisible(true);
                }catch(Exception e1){

                }
            }
        });
    }
}


