package com.oswald.servlet;


import com.oswald.model.User;
import com.oswald.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.security.MessageDigest;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SessionFactory sessionFactory;

    @Override
    public void init() throws ServletException {
        // Initialize Hibernate session factory only once
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve form data
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Server-side input validation
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.getWriter().write("Username and password are required!");
            return;
        }

        if (username.length() < 3 || username.length() > 50 || password.length() < 6 || password.length() > 50) {
            response.getWriter().write("Invalid username or password length!");
            return;
        }

        // Hash the password (for demonstration, using SHA-256)
        //String hashedPassword = hashPassword(password);

        try (Session session = sessionFactory.openSession();) {
            // Query the database for the user
            User user = session.createQuery("FROM User WHERE username = :username AND password = :password", User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();

            if (user != null) {
                // Successful login: Create a session
                HttpSession httpSession = request.getSession();
                httpSession.setAttribute("username", username);

                System.out.println("Redirecting to welcome.jsp..."); // Debug log

                // Redirect to a welcome page
                //response.sendRedirect("welcome.jsp");
               // response.sendRedirect(request.getContextPath() + "/welcome.jsp");
                //request.getRequestDispatcher("welcome.jsp").forward(request, response);
                response.sendRedirect(request.getContextPath() + "/welcome.jsp");


            } else {
                // Failed login
                System.out.println("Invalid username or password."); // Debug log

                response.getWriter().write("Invalid username or password!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Database error: " + e.getMessage());
        }
    }

    // Helper method to hash the password (using SHA-256 for demonstration)
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        HibernateUtil.shutdown();
    }


}




