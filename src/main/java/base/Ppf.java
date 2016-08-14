package base;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;

/**
 * Created by Brindavan on 8/14/2016.
 */
public class Ppf {

    //Ppf parameters
    int s_num;
    int account_num;
    String name;
    LocalDate last_repayment;
    LocalDate next_repayment;
    Connection conn;

    public Ppf(int s_num, int account_num, String name, LocalDate last_repayment, LocalDate next_repayment){
        this.s_num = s_num;
        this.account_num = account_num;
        this.name = name;
        this.last_repayment = last_repayment;
        this.next_repayment = next_repayment;
    }

    public Ppf(Connection conn, int s_num){
        this.conn = conn;
        this.s_num = s_num;
        this.populate(s_num);
    }

    public Ppf(int s_num, LocalDate next_repayment){
        this.s_num = s_num;
        this.next_repayment = next_repayment;
    }

    public void populate(int s_num){

        String raw_query = "SELECT account_num, name, last_repayment, next_repayment FROM ppf WHERE s_num=";
        String complete_query = raw_query + Integer.toString(s_num);
        try{
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(complete_query);
            while(rs.next()){
            this.account_num = rs.getInt(1);
                this.name = rs.getString(2);
                Date lr = rs.getDate(3);
                this.last_repayment = Instant.ofEpochMilli(lr.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                Date nr = rs.getDate(4);
                this.next_repayment = Instant.ofEpochMilli(nr.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    /*
    Getter Functions
     */
    public int getS_num(){
        return this.s_num;
    }

    public int getAccount_num(){
        return this.account_num;
    }

    public String getName(){
        return this.name;
    }

    public LocalDate getLast_repayment(){
        return this.last_repayment;
    }

    public LocalDate getNext_repayment(){
        return this.next_repayment;
    }
}
