package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class Main {
    //test, all test,
    public static void main(String[] args)throws Exception {
	//connection starts
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1522:ug", "ora_p3l0b", "a58055154");

        Functions reg = new Functions();
        /*staff/customer register
         *
         *  cid: input has to be numbers
         * email: must have @ in the middle
         * gender: M or F
         * brithday: valid date
         * address:
         * price: has to be positive number
         * postcode: valid format (X#X #X#)
         * password: encrypt user input by SHA-1
         * city:
         *
         */
        String cid, email,name, gender, birthday, address, postcode, password, city,sid, m_type;
        int month;
        //user input
        cid = "231231";
        name = "jason".toLowerCase();
        email = "jason@gmail.com".toLowerCase();
        gender = "M".toUpperCase();
        birthday = "1990-07-05";
        address = "123 asd st".toLowerCase();
        postcode = "C2R Q2F".toUpperCase();
        password = "123";
        city = "vancouver".toLowerCase();
        sid = "202006";
        m_type = "student";

        //customer register
        try {
            reg.c_register(cid, email, name, gender, birthday, address, postcode, password, city, con);
        }catch (Exception e){
            //int position = e.getMessage().indexOf("ORA-");
            System.out.println("c_register: "+e.getMessage());
        }

        //staff register
        try{
            reg.s_register(sid, name, birthday, password,"sssss", con);
        }catch (Exception e){
            //int position = e.getMessage().indexOf("ORA-");
            System.out.println("s_register: "+e.getMessage());
        }

        //customer log_in
        try{
            System.out.println(reg.c_login(cid, password,con));
        }catch (Exception e){
            //int position = e.getMessage().indexOf("ORA-");
            System.out.println("c_login: "+e.getMessage());
        }

        //staff log_in
        try{
            System.out.println(reg.s_login(sid, password,con));
        }catch (Exception e){
            //int position = e.getMessage().indexOf("ORA-");
            System.out.println("s_login: "+e.getMessage());
        }

        //search expiry date of membership if he/she is not drop in customer
        //cid: char(6)
        //m_type: membership type;
        //con: connection
        try{
            System.out.println(reg.expirationDate("123123", m_type,con));
        }catch (Exception e){
            //int position = e.getMessage().indexOf("ORA-");
            System.out.println("expirationDate: "+e.getMessage());
        }

        //cid: char(6)
        //m_type: membership type
        //month: # of month membership
        //con: connection
        try{
            System.out.println(reg.buy_membership(cid,m_type,2,con));
        }catch (Exception e){
            //int position = e.getMessage().indexOf("ORA-");
            System.out.println("buy_membership: "+e.getMessage());
        }


        //OCID: unique id for offered class
        //type: class type, eg, yoga
        //hour: 24 hour
        //return true if success, throw exception if denied.
        try{
            System.out.println(reg.open_class("4003","yoga", "109090", 18, 0, 0, "ssss",con));
        }catch (Exception e){
            //int position = e.getMessage().indexOf("ORA-");
            System.out.println("open_class: "+e.getMessage());
        }

        //Search_for_facility_within_same_city return arraylist of fid,
        // empty set means no facility within same city,
        // there could be more then one facility, represented by fid
        try{
            ArrayList<Interested_Field> items =reg.Search_for_facility_within_same_city("V8B 0N2",con);
            for (Interested_Field item: items){
                System.out.println(item.fid);
            }
        }catch (Exception e){
            //int position = e.getMessage().indexOf("ORA-");
            System.out.println("Search_for_facility_within_same_city: "+e.getMessage());
        }

        //return arraylist of cid
        //empty means no such customer
        try{
            System.out.println(reg.customers_who_signup_every_class(con));
        }catch (Exception e){
            //int position = e.getMessage().indexOf("ORA-");
            System.out.println("customers_who_signup_every_class: "+e.getMessage());
        }

        //return arraylist of ocid
        //
        try{
            System.out.println(reg.search_for_class_by_type("yoga",con));
        }catch (Exception e){
            //int position = e.getMessage().indexOf("ORA-");
            System.out.println("search_for_class_by_type: "+e.getMessage());
        }


        //use view table temp;
        try{
            System.out.println(reg.search_for_class_by_time("13","00","00",con));
        }catch (Exception e){
            //int position = e.getMessage().indexOf("ORA-");
            System.out.println("search_for_class_by_time: "+e.getMessage());
        }

        //failed update, check constraint violated
        try{
            System.out.println(reg.update_membership_fee("dropin",-1,0,"111",con));
        }catch (Exception e){
            //int position = e.getMessage().indexOf("ORA-");
            System.out.println("update_membership_fee: "+e.getMessage());
        }


        //failed update, check constraint passed
        try{
            System.out.println(reg.update_membership_fee("dropin",0,0,"11",con));
        }catch (Exception e){
            //int position = e.getMessage().indexOf("ORA-");
            System.out.println("update_membership_fee: "+e.getMessage());
        }

        //delete
        try{
            System.out.println(reg.delete_customer("123123",con));
        }catch (Exception e){
            //int position = e.getMessage().indexOf("ORA-");
            System.out.println("update_membership_fee: "+e.getMessage());
        }

        try{
            System.out.println(reg.signUpClass("233333","7755", con));
        }catch (Exception e){
            //int position = e.getMessage().indexOf("ORA-");
            System.out.println("signUpClass: "+e.getMessage());
        }


        //connection ends
        con.close();
    }
}


