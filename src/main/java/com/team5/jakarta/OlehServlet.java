package com.team5.jakarta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "olehServlet", value = "/oleh-servlet")
public class OlehServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Hello from Oleh Korniichuk!</h1>");
        out.println("<p>Here's a picture of a pickle below:</p>");
        out.println("<img src='https://media.istockphoto.com/id/869118084/photo/salt-cucumber-isolated-on-a-white-background.jpg?s=612x612&w=0&k=20&c=SSfUTbNwerYjMdHb66gbXcjhdifwvFQoIBrec_Ywq-o=' alt='A Picture of a Pickle' width='500' height='300'>");
        out.println("<p>Here's <a href='https://en.wikipedia.org/wiki/Pickled_cucumber'>some pickle info</a>.</p>");
        out.println("<p><a href='javascript:window.history.back();'>Go back</a></p>");
        out.println("</body></html>");

    }
}
