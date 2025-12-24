package com.example.demo.servlet;

import java.io.IOException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SimpleEchoServlet extends HttpServlet {

    // ✅ MUST BE PUBLIC (tests call directly)
    @Override
    public void doGet(HttpServletRequest req,
                      HttpServletResponse resp) throws IOException {

        resp.setContentType("text/plain");
        resp.getWriter().write("OK");
    }
}
