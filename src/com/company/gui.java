package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class gui {
    private JTabbedPane tabbedPane1;
    public JPanel panel1;
    private JTextField reg_email_textbox;
    private JTextField reg_cid_textbox;
    private JTextField reg_fullname_textbox;
    private JRadioButton reg_male_radio;
    private JRadioButton reg_female_radio;
    private JTextField reg_year_textbox;
    private JTextField reg_day_textbox;
    private JTextField reg_month_textbox;
    private JTextField reg_address_textbox;
    private JTextField reg_postal_textbox;
    private JTextField reg_city_textbox;
    private JPasswordField c_pwd;
    private JPasswordField c_pwd2;
    private JButton registerButton;
    private JTextField sid;
    private JTextField s_name;
    private JTextField s_year;
    private JTextField s_day;
    private JTextField s_month;
    private JPasswordField s_pwd_1;
    private JPasswordField s_pwd_2;
    private JButton s_reg;
    private JButton log_out;
    private JButton backToLogInButton;
    private JPasswordField staffcode;
    private JTextField fid;

    public gui() throws Exception{
        try {
            Connection con = Connections.getConnection();
            Functions reg = new Functions();
            registerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)  {

                    String reg_cid = reg_cid_textbox.getText().toLowerCase();
                    String reg_email = reg_email_textbox.getText().toLowerCase();
                    String reg_name = reg_fullname_textbox.getText().toLowerCase();
                    String reg_address = reg_address_textbox.getText().toLowerCase();
                    String reg_postal = reg_postal_textbox.getText().toUpperCase();
                    String reg_password = String.valueOf(c_pwd.getPassword());
                    String reg_password_1 =String.valueOf(c_pwd2.getPassword());
                    String reg_city = reg_city_textbox.getText().toLowerCase();
                    String year = reg_year_textbox.getText();
                    String month =  reg_month_textbox.getText();
                    String day = reg_day_textbox.getText();
                    String reg_birthday = year  + "-" + month + "-" +  day;
                    String reg_gender = "Other";

                    try {
                        if (reg_male_radio.isSelected()) {
                            reg_gender = "M";
                        } else if (reg_female_radio.isSelected()) {
                            reg_gender = "F";
                        }


                        if(reg_cid.length() != 6){
                            JOptionPane.showMessageDialog(null, "id should be exactly 6 characters");
                        }else if(!reg_email.contains("@")){
                            JOptionPane.showMessageDialog(null, "invalid e-mail format");
                        }else if(!reg_password_1.equals(reg_password) || reg_password_1.length()==0){
                            JOptionPane.showMessageDialog(null, "password is not the same");
                        }else if(!reg_male_radio.isSelected()&&!reg_female_radio.isSelected()){
                            JOptionPane.showMessageDialog(null, "gender should be selected");
                        }else if(!isValidPostalCode(reg_postal)){
                            JOptionPane.showMessageDialog(null, "post code should be x#x #x#");
                        }
                        else if (reg_address.length()==0
                                ||reg_city.length()==0
                                ||day.length()==0
                                ||month.length()==0
                                ||year.length()==0
                                ||reg_name.length() ==0){
                            JOptionPane.showMessageDialog(null, "address/city/birthday should not leave empty.");
                        }else if (reg.c_register(reg_cid, reg_email, reg_name, reg_gender, reg_birthday, reg_address, reg_postal, reg_password, reg_city, con)){
                            JOptionPane.showMessageDialog(null, "worked");
                            JFrame frame = JFrames.get_frame();
                            frame.setTitle("Login");
                            frame.setContentPane(new Log_in().Login);
                            frame.pack();
                            frame.setVisible(true);
                        }

                    } catch (Throwable err) {
                        JOptionPane.showMessageDialog(null, "error:" + err);
                    }

                }
            });

            s_reg.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String sids = sid.getText().toLowerCase();
                    String sname = s_name.getText();
                    String syear = s_year.getText();
                    String smonth =  s_month.getText();
                    String sday = s_day.getText();
                    String s_birthday = syear  + "-" + smonth + "-" +  sday;
                    String spwd1 = String.valueOf(s_pwd_1.getPassword());
                    String spwd2 = String.valueOf(s_pwd_2.getPassword());
                    String fid_s = fid.getText();
                    try{
                        if(sids.length() !=6){
                            JOptionPane.showMessageDialog(null, "id should be exactly 6 characters");
                        }else if(!fid_s.equals("109090")&&
                                !fid_s.equals("109091")&&
                                !fid_s.equals("109092")&&
                                !fid_s.equals("109093")&&
                                !fid_s.equals("109094")){
                            JOptionPane.showMessageDialog(null, "invalid fid!");
                        } else if(sname.length()==0
                                ||syear.length()==0
                                ||smonth.length()==0
                                ||sday.length()==0){
                            JOptionPane.showMessageDialog(null, "name/year/month/day should not be empty");
                        }else if(!spwd1.equals(spwd2) || spwd1.length()==0){
                            JOptionPane.showMessageDialog(null, "password is not the same");
                        }else if(!String.valueOf(staffcode.getPassword()).equals("111111")){
                            JOptionPane.showMessageDialog(null, "wrong code!!!!!!!");
                        }
                        else if(reg.s_register(sids,sname, s_birthday,spwd1,fid_s,con)){
                            JOptionPane.showMessageDialog(null, "worked");
                            JFrame frame = JFrames.get_frame();
                            frame.setTitle("Login");
                            frame.setContentPane(new Log_in().Login);
                            frame.pack();
                            frame.setVisible(true);
                        }
                    }catch (Throwable err){
                        JOptionPane.showMessageDialog(null, "error:" + err);
                    }
                }
            });
        } catch (Exception a) {
            JOptionPane.showMessageDialog(null, "Connecion error");
        }


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
        backToLogInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = JFrames.get_frame();
                frame.setTitle("Login");
                frame.setContentPane(new Log_in().Login);
                frame.pack();
                frame.setVisible(true);
            }
        });
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
