package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by Haochen on 2017-03-27.
 */
public class staff_UI {
    private JTextField update_type_textbox;
    private JTextField update_dropin_textbox;
    private JTextField update_monthly_textbox;
    private JButton updateButton;
    private JTextField del_custid_textbox;
    private JButton deleteCustomerButton;
    private JTextField open_class_ID;
    private JTextField open_class_type;
    private JTextField open_class_hr;
    private JTextField open_class_min;
    private JButton createClassButton;
    private JButton checkLoyalCustomersButton;
    public JPanel JPanel;
    public JPanel JPanel2;
    private JTextField open_facility_textbox;
    private JButton aggregation;
    private JRadioButton sum1;
    private JRadioButton count1;
    private JRadioButton max1;
    private JRadioButton min1;
    private JRadioButton avg1;
    private JRadioButton emtpy1;
    private JRadioButton sum2;
    private JRadioButton max2;
    private JRadioButton min2;
    private JRadioButton avg2;
    private JRadioButton count2;
    private JRadioButton emtpy2;
    private JButton log_out;
    private JButton checkMeaningButton;
    private JLabel meaning_label;
    private String sum = "sum";
    private String avg = "avg";
    private String max = "max";
    private String min = "min";
    private String count = "count";
    private String meaning = "no meaning";
    public String sid_s="";


    public staff_UI() {
        try {
            Functions func = new Functions();
            Connection con = Connections.getConnection();
            updateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (func.update_membership_fee(update_type_textbox.getText(), Integer.parseInt(update_monthly_textbox.getText()), Integer.parseInt(update_dropin_textbox.getText()),sid_s, con)) {
                            JOptionPane.showMessageDialog(null, "Success!");

                        } else {
                            JOptionPane.showMessageDialog(null, "Failed!");
                        }

                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Failed!" + e1);

                    }


                }
            });

            deleteCustomerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if(del_custid_textbox.getText().length() !=6){
                            JOptionPane.showMessageDialog(null, "id should have length of 6");
                        }
                        else if (func.delete_customer(del_custid_textbox.getText(), con)) {
                            JOptionPane.showMessageDialog(null, "Success!");

                        } else {
                            JOptionPane.showMessageDialog(null, "Failed!");
                        }

                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Failed!" + e1);

                    }

                }
            });
            createClassButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String id = open_class_ID.getText();
                    String type = open_class_type.getText();
                    String fid = open_facility_textbox.getText();
                    Integer hr = 0;
                    Integer min = 0;

                    try {
                        hr = Integer.parseInt(open_class_hr.getText());
                        min = Integer.parseInt(open_class_min.getText());
                        if(id.length()!=4){
                            JOptionPane.showMessageDialog(null, "class id should be length of 4");
                        } else if(fid.length() !=6){
                            JOptionPane.showMessageDialog(null, "facility id should be length of 6");
                        } else if(hr > 24 ||  hr < 0){

                            JOptionPane.showMessageDialog(null, "invalid hour");
                        } else if(min > 59 ||  hr < 0){
                            JOptionPane.showMessageDialog(null, "invalid min");
                        } else if (func.open_class(id,type,fid,hr,min,00,sid_s, con)) {
                            JOptionPane.showMessageDialog(null, "Success!");

                        } else {
                            JOptionPane.showMessageDialog(null, "Failed!");
                        }

                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Failed!" + e1);

                    }

                }

            });
            checkLoyalCustomersButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        ArrayList<Interested_Field> returnedArray;
                        returnedArray = func.customers_who_signup_every_class(con);
                        Object[] cols = {"CID","NAME","EMAIL","GENDER","BIRTHDAY"};
                        Object [][] rows = new Object[returnedArray.size()][5];
                        for (int i = 0; i < returnedArray.size();i++) {
                            rows[i][0] = returnedArray.get(i).getCid();
                            rows[i][1] = returnedArray.get(i).getCustomer_name();
                            rows[i][2] = returnedArray.get(i).getEmail();
                            rows[i][3] = returnedArray.get(i).getGender();
                            rows[i][4] = returnedArray.get(i).getBirthday();
                        }
                        JTable table = new JTable(rows, cols);
                        JOptionPane.showMessageDialog(null, new JScrollPane(table));

                    } catch (Exception e2) {
                        JOptionPane.showMessageDialog(null, "Failed!" + e2);


                    }
                }
            });

            aggregation.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String query;
                        String result = "";
                        if (count1.isSelected()) {
                            if (avg2.isSelected()) {
                                meaning = "find the number of membership type that their membership fee is greater than the average membership fee.";
                                query = "select count(*) from membership where monthly_payment > (select avg(monthly_payment) from membership)";
                                String[] constraint = {"count(*)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (sum2.isSelected()) {
                                meaning = "the amount of profit each membership receives (shows type, amount of that type, total profit)";
                                query = "select type, count(*) as total, sum(monthly_payment) as profit from (select t.type, m.monthly_payment from custmer_has_membership t, membership m where t.type = m.type) group by type";
                                String []constraint = {"type","total","profit"};
                                result = func.universal_query(query, constraint, con);
                            } else if (max2.isSelected()) {
                                meaning = "most amount of people working in a facility and the facility";
                                query ="select fid, count(fid) from works group by fid having count(fid) = (select max (mycount) from (select fid, count(fid) mycount from works group by fid))";
                                String []constraint = {"fid","count(fid)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (min2.isSelected()) {
                                meaning = "least amount of people working in a facility and that facility";
                                query ="select fid, count(fid) from works group by fid having count(fid) = (select min (mycount) from (select fid, count(fid) as mycount from works group by fid))";
                                String []constraint = {"fid", "count(fid)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (count2.isSelected()) {
                                meaning = "the total for each type of memeber currently in db";
                                query ="select type, count(*) from custmer_has_membership group by type";
                                String []constraint = {"type","count(*)"};
                                result = func.universal_query(query, constraint, con);
                            } else {
                                meaning = "courses and the amount of people in said course";
                                query ="select ocid as course, count(*) as people from takes group by ocid";
                                String []constraint = {"course", "people"};
                                result = func.universal_query(query, constraint, con);
                            }
                        } else if (avg1.isSelected()) {
                            if (avg2.isSelected()) {
                                meaning = "find the average monthly_payment that is more than the average monthly_payment among all the types.";
                                query ="select avg(monthly_payment) from membership where monthly_payment > (select avg(monthly_payment) from membership)";
                                String []constraint = {"avg(monthly_payment)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (sum2.isSelected()) {
                                meaning = "find the avg total for a family with a child, student, adult and senior";
                                query ="select avg(total) from (select sum(monthly_payment) as total from membership)";
                                String []constraint = {"avg(total)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (max2.isSelected()) {
                                meaning = "get the average payment that does not include the most expensive payment (adult)";
                                query ="select avg(monthly_payment) from membership where monthly_payment < (select max(monthly_payment) from membership)";
                                String []constraint = {"avg(monthly_payment)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (min2.isSelected()) {
                                meaning = "find the average payment that does not include a child (cheapest payment)";
                                query ="select avg(monthly_payment) from membership where monthly_payment > (select min(monthly_payment) from membership)";
                                String []constraint = {"avg(monthly_payment)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (count2.isSelected()) {
                                meaning = "average amount of people in a course";
                                query ="select avg(people) from (select count(*) as people from takes group by ocid)";
                                String []constraint = {"avg(people)"};
                                result = func.universal_query(query, constraint, con);
                            } else {
                                meaning = "find the average capacity of facilities";
                                query ="select avg(capacity) as AVGcapacity from facility";
                                String []constraint = {"AVGcapacity"};
                                result = func.universal_query(query, constraint, con);
                            }
                        } else if (sum1.isSelected()) {
                            if (avg2.isSelected()) {
                                meaning = "find the sum of capacity that is greater than the average capacity in all location.";
                                query ="select sum(capacity) from facility where capacity>(select avg(capacity) from facility)";
                                String []constraint = {"sum(capacity)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (sum2.isSelected()) {
                                meaning = "total capicity of all facilities";
                                query ="select sum(capacity) from (select sum(capacity) as capacity from facility)";
                                String []constraint = {"sum(capacity)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (max2.isSelected()) {
                                meaning = "find how many people registered in the most popular membership type";
                                query ="select sum(typenum) from (select type, count(*) typenum from manages group by type having count(*) = (select max(typenum2) from (select type, count(*) typenum2 from manages group by type)))";
                                String []constraint = {"sum(typenum)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (min2.isSelected()) {
                                meaning = "find the summation of capacity of facility that have the minimum capacity among the all";
                                query ="select sum(typenum) from (select type, count(*) typenum from manages group by type having count(*) = (select min(typenum2) from (select type, count(*) typenum2 from manages group by type)))";
                                String []constraint = {"sum(typenum)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (count2.isSelected()) {
                                meaning = "the amount of employees our rec centre has";
                                query ="select sum(worker) from (select count(*) as worker from staff)";
                                String []constraint = {"sum(worker)"};
                                result = func.universal_query(query, constraint, con);
                            } else {
                                meaning = "total capicity of all facilities";
                                query ="select sum(capacity) from facility";
                                String []constraint = {"sum(capacity)"};
                                result = func.universal_query(query, constraint, con);
                            }
                        } else if (max1.isSelected()) {
                            if (avg2.isSelected()) {
                                meaning = "find the max monthly_payment that is less than the average monthly_payment among all the types.";
                                query ="select max(monthly_payment) from membership where monthly_payment < (select avg(monthly_payment) from membership)";
                                String []constraint = {"max(monthly_payment)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (sum2.isSelected()) {
                                meaning="find city where we have max total capacity";
                                query ="select city from (select l.city, sum(f.capacity) as total from location l, facility f where l.postcode = f.postcode group by l.city) where total = (select max(total) as max_total from (select l.city, sum(f.capacity) as total from location l, facility f where l.postcode = f.postcode group by l.city))";
                                String []constraint = {"city"};
                                result = func.universal_query(query, constraint, con);
                            } else if (max2.isSelected()) {
                                meaning = "the largest capacity of all our facilities";
                                query ="select max(capacity) from (select max(capacity) as capacity from facility)";
                                String []constraint = {"max(capacity)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (min2.isSelected()) {
                                meaning = "the maximum capacity of the smallest facility";
                                query ="select max(lowmax) as Capacity_Of_Smallest_Facility from (select min(capacity) as lowmax from facility)";
                                String []constraint = {"Capacity_Of_Smallest_Facility"};
                                result = func.universal_query(query, constraint, con);
                            } else if (count2.isSelected()) {
                                meaning = "most amount of people working in a facility";
                                query ="select max(people) from (select count(*) as people from works group by fid)";
                                String []constraint = {"max(people)"};
                                result = func.universal_query(query, constraint, con);
                            } else {
                                meaning = "the largest capacity of all our facilities";
                                query ="select max(capacity) from facility";
                                String []constraint = {"max(capacity)"};
                                result = func.universal_query(query, constraint, con);
                            }
                        } else if (min1.isSelected()) {
                            if (avg2.isSelected()) {
                                meaning = "find the minimum monthly_payment that is greater than the average monthly_payment among all the types.";
                                query ="select min(monthly_payment) from membership where monthly_payment > (select avg(monthly_payment) from membership)";
                                String []constraint = {"min(monthly_payment)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (sum2.isSelected()) {
                                query ="select min(capacity) from (select sum(capacity) as capacity from facility)";
                                String []constraint = {"min(capacity)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (max2.isSelected()) {
                                meaning = "find the minimum number of staff that works in the largest facility";
                                query ="select min(fcount) from (select fid, count(*) fcount from works where fid = (select fid from (select fid, capacity from facility where capacity = (select max(capacity) from facility))) group by fid)";
                                String []constraint = {"min(fcount)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (min2.isSelected()) {
                                query ="select min(capacity) from (select min(capacity) as capacity from facility)";
                                String []constraint = {"min(capacity)"};
                                result = func.universal_query(query, constraint, con);
                            } else if (count2.isSelected()) {
                                meaning = "Least popular class with ocid and amount of people (min + count)";
                                query ="select ocid, count(ocid) from takes group by ocid having count(ocid) = (select min(mycount) from (select ocid, count(ocid) mycount from takes group by ocid))";
                                String []constraint = {"ocid", "count(ocid)"};
                                result = func.universal_query(query, constraint, con);
                            } else {
                                meaning = "the smallest capacity of all our facilities";
                                query ="select min(capacity) from facility";
                                String []constraint = {"min(capacity)"};
                                result = func.universal_query(query, constraint, con);
                            }
                        } else if (emtpy1.isSelected()) {
                            if (avg2.isSelected()) {
                                meaning = "find facilities where the capacity is below the average of all facilities";
                                query =" select fid, capacity from facility where capacity < (select avg(capacity) from facility)";
                                String []constraint = {"fid", "capacity"};
                                result = func.universal_query(query, constraint, con);
                            } else if (sum2.isSelected()) {
                                //TODO
                                query ="";
                                String []constraint = {};
                                result = func.universal_query(query, constraint, con);
                            } else if (max2.isSelected()) {
                                meaning = "the largest facility with its capacity";
                                query ="select fid, capacity from facility where capacity = (select max(capacity) from facility)";
                                String []constraint = {"fid", "capacity"};
                                result = func.universal_query(query, constraint, con);
                            } else if (min2.isSelected()) {
                                meaning = "smallest facility with it’s capacity";
                                query ="select fid, capacity from facility where capacity = (select min(capacity) from facility)";
                                String []constraint = {"fid", "capacity"};
                                result = func.universal_query(query, constraint, con);
                            } else if (count2.isSelected()) {
                                //TODO
                                query ="";
                                String []constraint = {};
                                result = func.universal_query(query, constraint, con);
                            } else {
                                //TODO
                                query ="";
                                String []constraint = {};
                                result = func.universal_query(query, constraint, con);
                            }
                        } else {

                                result = "Error, please select an aggretion";
                        }
                        meaning_label.setText(meaning);
                        JOptionPane.showMessageDialog(null, result);
                    }catch(Throwable t){
                        JOptionPane.showMessageDialog(null, "error: "+ t.getMessage());
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
        } catch (Exception e ) {

        }


    }
}
