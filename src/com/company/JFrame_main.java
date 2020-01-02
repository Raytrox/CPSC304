package com.company;

import javax.swing.*;

public class JFrame_main {
    //start here
    public static void main(String[] args) {
        JFrame frame = JFrames.get_frame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Login");
        frame.setContentPane(new Log_in().Login);
        frame.pack();
        frame.setVisible(true);
    }
}
