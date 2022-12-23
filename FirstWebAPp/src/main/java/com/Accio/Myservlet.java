package com.Accio;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Myservlet")

public class Myservlet extends HttpServlet {
    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();
        out.println("<h3> This is my servlet</h3>");


    }
}
