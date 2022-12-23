package com.Accio;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/Search")

public class Search extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        String keyword=request.getParameter("keyword");
        System.out.println(keyword);
        try{
            Connection connection= DatabaseConnection.getConnection();
            ResultSet resultSet=connection.createStatement().executeQuery(" select pagetitle,pagelink, (length(pagedata)-length(replace(pagedata,'"+keyword+"',\"\")))/length('"+keyword+"') as countoccurences from pages order by countoccurences desc limit 30;");
            ArrayList<SearchResult>  results=new ArrayList<SearchResult>();
            while (resultSet.next())
            {
               SearchResult searchResult=new SearchResult();
               searchResult.setPagetitle(resultSet.getString("pagetitle"));
               searchResult.setPagelink(resultSet.getString("pagelink"));
               results.add(searchResult);
            }
            for(SearchResult result:results)
            {
                System.out.println(result.getPagelink()+" "+result.getPagetitle()+"/n");
            }
            request.setAttribute("results",results);
            request.getRequestDispatcher("/search.jsp").forward(request,response);
            response.setContentType("text/html");
            PrintWriter out=response.getWriter();
        }catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        catch(ServletException servletException)
        {
            servletException.printStackTrace();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

}
