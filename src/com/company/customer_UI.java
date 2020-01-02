package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Objects;

public class customer_UI {
    public JPanel customer_UI;
    private JTextField facility_textbox;
    private JButton searchFacilityButton;
    private JTextField class_type_textbox;
    private JButton searchClassButton;
    private JTextField class_hour_textbox;
    private JButton searchClassButton1;
    private JTextField class_min_textbox;
    private JTextField textField1;
    private JButton registerButton;
    private JButton log_out;
    private String cid;

    public void setCID(String cid){
        this.cid = cid;
    }

    public customer_UI() {
        try {
            Functions func = new Functions();
            Connection con = Connections.getConnection();
            searchFacilityButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ArrayList<Interested_Field> returnedArray = new ArrayList<>();
                    try {
                        String postCode = facility_textbox.getText().toUpperCase();
                        if(!isValidPostalCode(postCode)){
                            JOptionPane.showMessageDialog(null, "invalid post code");
                        }else{
                            returnedArray = func.Search_for_facility_within_same_city(postCode, con);
                            Object[] cols = {"FID","post code","Address","capacity"};
                            Object [][] rows = new Object[returnedArray.size()][4];
                            for (int i = 0; i < returnedArray.size();i++) {
                                rows[i][0] = returnedArray.get(i).getFid();
                                rows[i][1] = returnedArray.get(i).getPostcode();
                                rows[i][2] = returnedArray.get(i).getAddress();
                                rows[i][3] = returnedArray.get(i).getCapacity();
                            }
                            JTable table = new JTable(rows, cols);
                            JOptionPane.showMessageDialog(null, new JScrollPane(table));

                        }
                    } catch (Exception e01) {

                    }

                }
            });

            searchClassButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ArrayList<Interested_Field> returnedArray = new ArrayList<>();
                    try {
                        String c_type =class_type_textbox.getText().toLowerCase();
                        returnedArray = func.search_for_class_by_type(c_type, con);
                        Object[] cols = {"OCID","FID","CLASS TYPE","TIME"};
                        Object [][] rows = new Object[returnedArray.size()][4];
                        for (int i = 0; i < returnedArray.size();i++) {
                            rows[i][0] = returnedArray.get(i).getOcid();
                            rows[i][1] = returnedArray.get(i).getFid();
                            rows[i][2] = returnedArray.get(i).getClass_type();
                            rows[i][3] = returnedArray.get(i).getClass_time();
                        }
                        JTable table = new JTable(rows, cols);
                        JOptionPane.showMessageDialog(null, new JScrollPane(table));
                    } catch (Exception e02) {
                    }
                }
            });

            searchClassButton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ArrayList<Interested_Field> returnedArray = new ArrayList<>();
                    try {
                        String hour = class_hour_textbox.getText();
                        String min = class_min_textbox.getText();
                        if(hour.length()==1){
                            hour = '0'+hour;
                        }
                        if(min.length()==1){
                            min = '0'+min;
                        }
                        returnedArray = func.search_for_class_by_time(hour,min, "00", con);
                        Object[] cols = {"OCID","FID","CLASS TYPE","TIME"};
                        Object [][] rows = new Object[returnedArray.size()][4];
                        for (int i = 0; i < returnedArray.size();i++) {
                            rows[i][0] = returnedArray.get(i).getOcid();
                            rows[i][1] = returnedArray.get(i).getFid();
                            rows[i][2] = returnedArray.get(i).getClass_type();
                            rows[i][3] = returnedArray.get(i).getClass_time();
                        }
                        JTable table = new JTable(rows, cols);
                        JOptionPane.showMessageDialog(null, new JScrollPane(table));
                    } catch (Exception e02) {

                    }

                }
            });

            registerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String ocid = textField1.getText().toLowerCase();
                        if (ocid.length() != 4) {
                            JOptionPane.showMessageDialog(null, "id should have length of 4");
                        } else if (func.signUpClass(cid, ocid, con)) {
                            JOptionPane.showMessageDialog(null, "register successed");
                        } else {
                            JOptionPane.showMessageDialog(null, "register failed");
                        }
                    }catch (Throwable t){
                        JOptionPane.showMessageDialog(null, "error"+t.getMessage());
                    }
                }
            });

            log_out.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame frame = JFrames.get_frame();
                    frame.setTitle("Login");
                    frame.setContentPane(new Log_in().Login);
                    frame.pack();
                    frame.setVisible(true);
                }
            });
        } catch (Exception e) {

        }


    }
    public boolean isValidPostalCode(String postcode){
        //7 length
        if(postcode.length()!=7)
            return false;
        postcode = postcode.toUpperCase();
        char[] digit = postcode.toCharArray();
        return (Character.isLetter(digit[0]) && Character.isDigit(digit[1]) && Character.isLetter(digit[2])
                && digit[3] == ' ' && Character.isDigit(digit[4]) && Character.isLetter(digit[5]) && Character.isDigit(digit[6]));

    }


}
