import base.Ppf;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Brindavan on 8/14/2016.
 */
public class PpfTest {

    /*
    Database connection parameters
     */
    String url = "jdbc:mysql://localhost:3306/";
    String db_name = "brindaone";
    String driver = "com.mysql.jdbc.Driver";
    String username = "root";
    String password = "root";

    Connection conn;

    public void establishConnection(){
        System.out.println("Trying to establish connection to " + db_name);

        try{
            Class.forName(driver).newInstance();
            long opTime = System.currentTimeMillis();
            conn = DriverManager.getConnection(url+db_name,username,password);
            System.out.println("Connection Successfully Established at " + new Timestamp(opTime) + "\n \n");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void displayContents() {
        if (conn == null) {
            System.out.println("Error: Database not connected");
            System.exit(1);
        }
        System.out.format ("%5s%15s%40s%20s%20s","SNo.","Account Number","Name","Last Repayment","Next Repayment");
        System.out.println("");
        try{
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `ppf`");

            while(resultSet.next()){
                System.out.format ("%5d%15d%40s%20s%20s",resultSet.getInt(1),resultSet.getInt(2),resultSet.getString(3),resultSet.getDate(4).toString(),resultSet.getDate(5).toString());
                System.out.println("");
                /*
                Print without formatting
                System.out.println(resultSet.getInt(1)+ "\t " +resultSet.getInt(2)+ "\t " +resultSet.getString(3)+ "\t " +resultSet.getDate(4).toString()+ "\t " +resultSet.getDate(5).toString());
                */
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("\n \n");
    }

    public void endConnection(){
        if(conn != null){
            try {
                conn.close();
                long opTime = System.currentTimeMillis();
                System.out.println("Connection to database " + db_name + " successfully terminated at " + new Timestamp(opTime));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void twoMonthsPending(){

        ArrayList<Ppf> ppfList = new ArrayList<>();
        ArrayList<Integer> displayList = new ArrayList<>();

        if(conn == null){
            System.out.println("Error: Database not connected");
            System.exit(1);
        }
        try{
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT s_num, name, next_repayment FROM ppf WHERE 1");
            while(resultSet.next()){
                Ppf temp = new Ppf(resultSet.getInt(1), resultSet.getDate(3).toLocalDate());
                ppfList.add(temp);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        LocalDate now = LocalDate.now();
        now = now.plusMonths(2);
        for(int i=0; i<ppfList.size(); i++){
            LocalDate ld = ppfList.get(i).getNext_repayment();
            if(ld.isBefore(now)){
                displayList.add(ppfList.get(i).getS_num());
            }
        }

        if(displayList.size()==0){
            System.out.println("No PPF Repayments in the coming 2 months");
        }
        else{
            System.out.println("Here are the PPF Portfolios pending for repayment in the next 2 months");
            String raw_statement = "SELECT * FROM ppf WHERE s_num=";
            for(int i=0; i<displayList.size(); i++){
             try{
                 Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(raw_statement + displayList.get(i).toString());
                 while(rs.next()){
                 System.out.format ("%5d%15d%40s%20s%20s",rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getDate(4).toString(),rs.getDate(5).toString());
                 System.out.println("");
                 }
             }
             catch(SQLException e){
                 e.printStackTrace();
             }
            }
        }

        //Display ppfList
        /*
        for(int i=0; i<ppfList.size(); i++){
            System.out.println(ppfList.get(i).getS_num() + "\t" + ppfList.get(i).getNext_repayment());
        }
        */
    }

    public static void main(String[] args){
        PpfTest ppfTest = new PpfTest();
        ppfTest.establishConnection();
        ppfTest.twoMonthsPending();
        //ppfTest.displayContents();
        ppfTest.endConnection();
    }
}
