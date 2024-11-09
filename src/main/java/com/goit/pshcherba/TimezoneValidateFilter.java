package com.goit.pshcherba;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;


/**
 * This filter validates the "timezone" parameter in incoming HTTP requests to ensure it
 * represents a valid timezone format. If the timezone parameter is missing or empty,
 * a default value of "UTC+0" is applied. If the timezone parameter is invalid, the filter
 * responds with a 400 Bad Request status and an error message.
 * <p>
 * Example of valid timezone formats: "UTC+3", "UTC-05:00".
 *
 * @see Filter
 */
@WebFilter("/time")
public class TimezoneValidateFilter implements Filter {

    /**
     * Validates the "timezone" parameter of the HTTP request.
     * If the timezone parameter is null or empty, it assigns "UTC+0" as a default.
     * If the timezone parameter is invalid, it sets the response status to 400 (Bad Request)
     * and displays an error message.
     *
     * @param req the ServletRequest object containing the client request
     * @param resp the ServletResponse object for the server's response to the client
     * @param chain the FilterChain for invoking the next filter or the resource
     * @throws IOException if an input or output error occurs while handling the request
     * @throws ServletException if the request could not be handled
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        HttpServletResponse httpServletResponse = (HttpServletResponse) resp;

        String timezone = req.getParameter("timezone");

        if (timezone == null || timezone.isEmpty()) {
            timezone = "UTC+0";
            req.setAttribute("timezone", timezone);
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        timezone = timezone.replace(" ", "+");

        try {
            System.out.println("ZoneID: " + ZoneId.of(timezone));
            req.setAttribute("timezone", timezone);
            chain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            httpServletResponse.setContentType("text/html");
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try (PrintWriter out = httpServletResponse.getWriter()) {
                out.println("<html><body>");
                out.println("<h1>Invalid timezone</h1>");
                out.println("</body></html>");
                System.out.println("ZoneID is false.");
            }
        }
    }
}
