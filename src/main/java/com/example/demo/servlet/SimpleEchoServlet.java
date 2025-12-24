package com.example.demo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/simple-echo")
public class SimpleEchoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);

        String name = request.getParameter("name");
        String message;

        if (name == null || name.trim().isEmpty()) {
            message = "Hello, Guest";
        } else {
            message = "Hello, " + name.trim();
        }

        PrintWriter out = response.getWriter();
        out.print(message);
        out.flush();
    }
}
