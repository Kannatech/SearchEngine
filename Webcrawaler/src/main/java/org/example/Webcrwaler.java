package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
public class Webcrwaler {

    private HashSet<String> urllink;

    private int Max_Depth=2;
    public Connection connection;
    public Webcrwaler(){
        connection=DatabaseConnection.getConnection();
        urllink=new HashSet<String>();
    }
    public void getPageTextandLinks(String url,int depth){
        if(!urllink.contains(url)){
            if(urllink.add(url)){
                System.out.println(url);
            }
            try {
                Document document = Jsoup.connect(url).timeout(5000).get();
                String text=document.text().length()<500?document.text():document.text().substring(0,499);
                System.out.println(text);

                PreparedStatement preparedStatement= connection.prepareStatement("Insert into pages values(?,?,?)");
                preparedStatement.setString(1,document.title());
                preparedStatement.setString(2,url);
                preparedStatement.setString(3,text);
                preparedStatement.executeUpdate();


                depth++;
                if(depth>Max_Depth){
                    return;
                }
                Elements availableLinksonpage = document.select("a[href]");
                for(Element currentLink:availableLinksonpage){
                    getPageTextandLinks(currentLink.attr("abs:href"),depth);
                }
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            } catch (SQLException e) {
                //throw new RuntimeException(e);
                e.printStackTrace();
            }
        }

    }
    public static void main(String[] args) {

        Webcrwaler crwaler=new Webcrwaler();
        crwaler.getPageTextandLinks("https://www.javatpoint.com",0);
    }

}
