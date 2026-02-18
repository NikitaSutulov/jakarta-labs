package com.team5.jakarta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "nikitaServlet", value = "/nikita-servlet")
public class NikitaServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Hello from Nikita Sutulov!</h1>");
        out.println("<p>Here's a picture of me below:</p>");
        out.println("<img src='https://avatars.githubusercontent.com/u/64411952?v=4' alt='Nikita's Picture' width='300' height='300'>");
        out.println("<p>Here's <a href='https://github.com/NikitaSutulov'>my GitHub profile</a>.</p>");
        out.println("<p><a href='javascript:window.history.back();'>Go back</a></p>");
        out.println("</body></html>");

    }
}
