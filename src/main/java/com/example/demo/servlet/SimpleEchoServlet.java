package com.example.demo.servlet;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
@WebServlet(urlPatterns = {"/ping", "/health"})
public class SimpleEchoServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/plain");
        if (req.getRequestURI().contains("ping")) res.getWriter().write("PONG");
        else res.getWriter().write("0K");
    }
}