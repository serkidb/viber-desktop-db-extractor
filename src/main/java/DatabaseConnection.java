import com.opencsv.CSVWriter;
import ezvcard.Ezvcard;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.StructuredName;
import org.sqlite.core.DB;

import java.applet.Applet;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * Created by Serkid on 25/6/2018.
 */
public class DatabaseConnection {
    private Connection conn = null;
    private DB db;
    private Statement stmt;
    private ResultSet rs;
    private String url;

    public void connect(File file) {

        this.url = file.getPath();

        try {
            // db parameters
            String url = "jdbc:sqlite:"+this.url;
            // create a connection to the database
            this.conn = DriverManager.getConnection(url);
            DatabaseMetaData md = conn.getMetaData();
            md.getTables(null, null, "contact", null);
            String sql = "SELECT * FROM Contact";
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    public ArrayList<ArrayList<String>> getNames() {
        ArrayList<ArrayList<String>> myList = new ArrayList<ArrayList<String>>();
        try {
            while (rs.next()) {
                if (rs.getString("name") != null) {
                    ArrayList<String> tempList = new ArrayList<String>();
                    tempList.add(rs.getString("name"));
                    tempList.add(rs.getString("number"));
                    myList.add(tempList);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myList;
    }

    public void writeCSV() {

        int count = 0;
        CSVWriter csvWriter = null;
        try {
            csvWriter = new CSVWriter(new FileWriter("example.csv"));

            csvWriter.writeNext(new String[]{"Name","Given Name","Additional Name","Family Name","Yomi Name","Given Name Yomi","Additional Name Yomi","Family Name Yomi","Name Prefix","Name Suffix","Initials","Nickname","Short Name","Maiden Name","Birthday","Gender","Location","Billing Information","Directory Server","Mileage","Occupation","Hobby","Sensitivity","Priority","Subject","Notes","Language","Photo","Group Membership","E-mail 1 - Type","E-mail 1 - Value","Phone 1 - Type","Phone 1 - Value","Phone 2 - Type","Phone 2 - Value","Website 1 - Type","Website 1 - Value"});
            try {
                while (rs.next()) {
                    if (rs.getString("name") != null) {
                        System.out.println(rs.getString("number") + ":" + rs.getString("name"));


                        csvWriter.writeNext(new String[]{rs.getString("name"),"","","","","","","","","","","","","","","","","","","","","","","","","","","","*myContacts","","","Mobile",rs.getString("number"),",",",",",","",""});


                        count++;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(count);


    }

    //Method to write Hotmail CSV

    //Method to write VCF
    public void exportToVCard()
    {

        Collection<VCard> myVCards = new ArrayList<VCard>();
        File file = new File("vcard.vcf");

             try {
                while (rs.next()) {
                    if (rs.getString("name") != null) {
                        VCard myVCard = new VCard();
                        TelephoneType type = TelephoneType.CELL;
                        StructuredName n = new StructuredName();
                        n.setGiven(rs.getString("name"));
                        myVCard.addTelephoneNumber(rs.getString("number"),type);
                        myVCard.setStructuredName(n);
                        myVCards.add(myVCard);



                    }
                }
                 Ezvcard.write().go(file);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                 e.printStackTrace();
             }









    }

}


