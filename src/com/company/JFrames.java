package com.company;

import javax.swing.*;

/**
 * Created by AA on 2017-03-27.
 */
public class JFrames {
    private static JFrame frame = null;

    private JFrames (){
        frame = new JFrame();
    }

    public static JFrame get_frame(){
        if(frame == null) {
            new JFrames();
        }
        return frame;
    }
}
