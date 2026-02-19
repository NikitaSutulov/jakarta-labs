package com.team5.jakarta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "nazarServlet", value = "/nazar-servlet")
public class NazarServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Hello from Назар Вовк ІМ-52мп!</h1>");
        out.println("<p>Here's a picture of me below:</p>");
        out.println("<img src='https://avatars.githubusercontent.com/u/89912149?s=400&u=bcfdf109d28daf7c9fa8ee98d446756c1aeed393&v=4' alt='Nazar's Picture' width='400' height='400'>");
        out.println("<p>Here's <a href='https://github.com/Nazofer'>my GitHub profile</a>.</p>");
        out.println("<p><a href='javascript:window.history.back();'>Go back</a></p>");
        out.println("</body></html>");

    }
}
