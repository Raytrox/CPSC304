package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Functions {
///

    public boolean c_register(String cid, String email, String name, String gender, String birthday, String address,
                           String postcode, String password, String city, Connection con) throws SQLException {
        //insert city to table first
        try {
            insert_city(city, postcode, con);
        } catch (Exception e) {
            int position = e.getMessage().indexOf("ORA-");
            System.out.println(e.getMessage().substring(position + 4, position + 9));
        }

        //parse given string format to sql date
        String[] splitted_birthday = birthday.split("-");
        int year = Integer.parseInt(splitted_birthday[0]);
        int month = Integer.parseInt(splitted_birthday[1]);
        int day = Integer.parseInt(splitted_birthday[2]);

        Calendar target_date = translateDate(year, month, day);
        Date birthday_sql = new Date(target_date.getTimeInMillis());
        PreparedStatement ps = con.prepareStatement("INSERT INTO ORA_P3L0B.CUSTOMER (CID, NAME, EMAIL, GENDER, BIRTHDAY,"
                + " ADDRESS, POSTCODE, PASSWORD) VALUES (?,?,?,?,?,?,?,?)");
        ps.setString(1, cid);
        ps.setString(2, name);
        ps.setString(3, email);
        ps.setString(4, gender);
        ps.setDate(5, birthday_sql);
        ps.setString(6, address);
        ps.setString(7, postcode);
        ps.setString(8, password);
        ps.executeUpdate();
        ps.close();
        return true;
    }


    public boolean s_register(String sid, String name, String birthday, String password, String fid,Connection con) throws SQLException {
        //parse given string format to sql date
        String[] splitted_birthday = birthday.split("-");
        int year = Integer.parseInt(splitted_birthday[0]);
        int month = Integer.parseInt(splitted_birthday[1]);
        int day = Integer.parseInt(splitted_birthday[2]);

        Calendar target_date = translateDate(year, month, day);
        Date birthday_sql = new Date(target_date.getTimeInMillis());
        Date today = new Date(Calendar.getInstance().getTimeInMillis());

        PreparedStatement ps = con.prepareStatement("INSERT INTO ORA_P3L0B.STAFF (SID, NAME, BIRTHDAY, PASSWORD) VALUES  (?,?,?,?)");
        ps.setString(1, sid);
        ps.setString(2, name);
        ps.setDate(3, birthday_sql);
        ps.setString(4, password);
        ps.executeUpdate();
        ps = con.prepareStatement("insert into ORA_P3L0B.WORKS (FID, SID, STARTFROM) VALUES(?,?,?)");
        ps.setString(1, fid);
        ps.setString(2, sid);
        ps.setDate(3, today);
        ps.executeUpdate();
        ps.close();
        return true;
    }


    public void insert_city(String city, String postcode, Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO ORA_P3L0B.LOCATION (POSTCODE, CITY) VALUES (?,?)");
        ps.setString(1, postcode);
        ps.setString(2, city);
        ps.executeUpdate();
        ps.close();
    }

    public boolean c_login(String cid, String password, Connection con) throws SQLException {
        String c_password = "";
        PreparedStatement ps = con.prepareStatement
                ("SELECT PASSWORD " +
                        "FROM ORA_P3L0B.CUSTOMER " +
                        "WHERE cid = ?");
        ps.setString(1, cid);
        ResultSet temp = ps.executeQuery();
        while (temp.next()) {
            c_password = temp.getString("PASSWORD");
        }
        ps.close();
        if (c_password.trim().equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean s_login(String sid, String password, Connection con) throws SQLException {
        String s_password = "";
        PreparedStatement ps = con.prepareStatement
                ("SELECT PASSWORD " +
                        "FROM ORA_P3L0B.STAFF " +
                        "WHERE sid = ?");
        ps.setString(1, sid);
        ResultSet temp = ps.executeQuery();
        while (temp.next()) {
            s_password = temp.getString("PASSWORD");
        }
        ps.close();
        if (s_password.trim().equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    public String expirationDate(String cid, String m_type,Connection con) throws SQLException{
        Date expirationDate = null;
        String type=m_type;
        if(type.equals("dropin")){
            return ("No membership subscribed.");
        }else {
            PreparedStatement ps = con.prepareStatement
                    ("SELECT EXPIREDATE FROM ORA_P3L0B.CUSTMER_HAS_MEMBERSHIP WHERE CID =? AND TYPE =?");
            ps.setString(1, cid);
            ps.setString(2, type);
            ResultSet temp = ps.executeQuery();
            while (temp.next()) {
                expirationDate = temp.getDate("EXPIREDATE");
            }
            ps.close();
            return expirationDate.toString();
        }
    }

    public String buy_membership(String cid, String m_type, int month, Connection con) throws SQLException{
        String type=m_type;
        Date target_day = new Date(Calendar.getInstance().getTimeInMillis()+TimeUnit.DAYS.toMillis(30)* month);

        PreparedStatement ps = con.prepareStatement("INSERT INTO ORA_P3L0B.CUSTMER_HAS_MEMBERSHIP (CID, TYPE, EXPIREDATE) VALUES  (?,?,?)");
        ps.setString(1, cid);
        ps.setString(2, type);
        ps.setDate(3, target_day);
        ps.executeUpdate();
        ps.close();
        return target_day.toString();
    }

    public boolean open_class(String OCID, String type, String fid, int hour, int minute, int second, String SID, Connection con) throws SQLException{
        Time target_time = new Time(translateTime(hour, minute, second).getTimeInMillis());
        Date today = new Date(Calendar.getInstance().getTimeInMillis());

        PreparedStatement ps = con.prepareStatement("INSERT INTO ORA_P3L0B.OFFERED_COURSES (OCID, FID, CTYPE, TIME) VALUES  (?,?,?,?)");
        ps.setString(1, OCID);
        ps.setString(2, fid);
        ps.setString(3, type);
        ps.setTime(4, target_time);
        ps.executeUpdate();
        ps = con.prepareStatement("insert into ORA_P3L0B.teaches (SID, OCID) VALUES(?,?)");
        ps.setString(1,SID);
        ps.setString (2,OCID);
        ps.executeUpdate();
        ps.close();

        return true;
    }



    public ArrayList<Interested_Field> Search_for_facility_within_same_city(String postcode, Connection con) throws SQLException{
        ArrayList<Interested_Field> target_facility = new ArrayList<Interested_Field>();
        PreparedStatement ps = con.prepareStatement("SELECT F.FID,F.POSTCODE,F.ADDRESS,F.CAPACITY FROM ORA_P3L0B.FACILITY F, ORA_P3L0B.LOCATION L WHERE L.POSTCODE = F.POSTCODE AND L.POSTCODE = ?");
        ps.setString(1, postcode);

        ResultSet temp = ps.executeQuery();
        while (temp.next()) {
            Interested_Field item = new Interested_Field();
            item.setFid(temp.getString("FID"));
            item.setPostcode(temp.getString("POSTCODE"));
            item.setAddress(temp.getString("ADDRESS"));
            item.setCapacity(temp.getInt("CAPACITY"));
            target_facility.add(item);
        }
        ps.close();
        return target_facility;
    }


    public ArrayList<Interested_Field> customers_who_signup_every_class(Connection con) throws  SQLException{
        ArrayList<Interested_Field> customers = new ArrayList<Interested_Field>();
        PreparedStatement ps = con.prepareStatement("select distinct t1.cid, c.name, c.email, c.GENDER, c.BIRTHDAY from takes t1, customer c where t1.cid = c.cid and not exists " +
                "(select o.ocid from offered_courses o minus " +
                "(select t2.ocid " +
                "from takes t2 where t1.cid = t2.cid))");

        ResultSet temp = ps.executeQuery();
        while (temp.next()) {
            Interested_Field item = new Interested_Field();
            item.setCid(temp.getString("CID"));
            item.setCustomer_name(temp.getString("NAME"));
            item.setEmail(temp.getString("EMAIL"));
            item.setGender(temp.getString("GENDER"));
            item.setBirthday(temp.getDate("BIRTHDAY").toString());
            customers.add(item);
        }
        ps.close();
        return customers;
    }


    public ArrayList<Interested_Field> search_for_class_by_type(String type, Connection con) throws SQLException{
        ArrayList<Interested_Field> target_class = new ArrayList<Interested_Field>();
        PreparedStatement ps = con.prepareStatement("select OCID,fid, ctype, time from offered_courses where ctype like ?");
        ps.setString(1, '%'+type+'%');
        ResultSet temp = ps.executeQuery();
        while (temp.next()) {
            Interested_Field item = new Interested_Field();
            item.setOcid(temp.getString("OCID"));
            item.setFid(temp.getString("fid"));
            item.setClass_type(temp.getString("ctype"));
            item.setClass_time(temp.getTime("time").toString());
            target_class.add(item);
        }
        ps.close();
        return target_class;
    }

    public ArrayList<Interested_Field> search_for_class_by_time(String hour, String minute, String second, Connection con) throws SQLException{
        ArrayList<Interested_Field> target_class = new ArrayList<Interested_Field>();
        String target_time = hour+":"+minute+":"+second;

        PreparedStatement ps = con.prepareStatement("create or replace view temp(ocid, fid, ctype, time) as(select ocid, fid, ctype, to_char(time, 'hh24:mi:ss') as time from offered_courses)");
        ps.executeQuery();
        ps.close();

        ps = con.prepareStatement("select ocid, fid, ctype, time from temp where time = ?");
        ps.setString(1, target_time);
        ResultSet temp = ps.executeQuery();
        while (temp.next()) {
            Interested_Field item = new Interested_Field();
            item.setOcid(temp.getString("ocid"));
            item.setFid(temp.getString("fid"));
            item.setClass_type(temp.getString("ctype"));
            item.setClass_time(temp.getTime("time").toString());
            target_class.add(item);
        }
        ps.executeUpdate("drop view temp");
        ps.close();
        return target_class;
    }

    public boolean update_membership_fee(String type, int monthly_payment, int drop_in_payment,String sid, Connection con) throws SQLException{
        Date today = new Date(Calendar.getInstance().getTimeInMillis());
        PreparedStatement ps = con.prepareStatement("update membership set monthly_payment = ?, dropin_payment = ? where type = ?");
        ps.setInt(1, monthly_payment);
        ps.setInt(2, drop_in_payment);
        ps.setString(3, type);
        ps.executeUpdate();
        ps = con.prepareStatement("insert into manages (sid, type, modified) values (?,?,?)");
        ps.setString(1,sid);
        ps.setString(2,type);
        ps.setDate(3,today);
        ps.executeUpdate();
        ps.close();
        return true;
    }

    public boolean delete_customer(String cid, Connection con)throws SQLException{
        PreparedStatement ps = con.prepareStatement("delete from customer where cid = ?");
        ps.setString(1, cid);
        ps.executeUpdate();
        ps.close();
        return true;
    }

    public boolean signUpClass(String cid, String ocid, Connection con)throws SQLException{
        PreparedStatement ps = con.prepareStatement("INSERT INTO ORA_P3L0B.takes (CID, OCID) values (?,?)");
        ps.setString(1, cid);
        ps.setString(2, ocid);
        ps.executeUpdate();
        ps.close();
        return true;
    }

    public String universal_query(String query, String [] constraint, Connection con) throws SQLException{
        String result = query+"\n\n";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet temp = ps.executeQuery();
        while (temp.next()) {
            for(int i = 0; i < constraint.length;i++) {
                result += constraint[i] + ": " + temp.getString(constraint[i]) + "\n";
            }
            result +="\n";
        }
        ps.close();
        return result;
    }


    private static Calendar translateDate(int year, int month, int day) {
        // get a calendar using the default time zone and locale.
        Calendar calendar = Calendar.getInstance();

        // set Date portion to January 1, 1970
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);

        // normalize the object
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    private static Calendar translateTime(int hour, int minute, int second) {
        // get a calendar using the default time zone and locale.
        Calendar calendar = Calendar.getInstance();

        // set Date portion to January 1, 1970
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);

        // normalize the object
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }
}
