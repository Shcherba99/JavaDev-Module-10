package com.goit.pshcherba;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


/**
 * A servlet that responds with the current date and time in a specified timezone.
 * The timezone is retrieved from a request attribute set by a previous filter.
 * If the timezone is valid, the servlet displays the formatted date and time.
 * <p>
 * Example URL: /time (expects "timezone" attribute to be set in the request)
 *
 * @see HttpServlet
 */
@WebServlet("/time")
public class TimeServlet extends HttpServlet {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");


    /**
     * Handles HTTP GET requests by returning the current date and time in the
     * specified timezone. The timezone should be provided as a request attribute
     * ("timezone") set by a preceding filter.
     *
     * @param req  the HttpServletRequest object that contains the client's request
     * @param resp the HttpServletResponse object that contains the server's response
     * @throws IOException if an input or output error occurs while handling the request
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        String timezone = (String) req.getAttribute("timezone");


        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><body>");
            out.println("<h1>" + getCurrentTimeInTimeZone(timezone) + "</h1>");
            out.println("</body></html>");
        }
    }

    /**
     * Returns the current date and time in the specified timezone as a formatted string.
     * The timezone should be a valid identifier according to ZoneId.SHORT_IDS.
     *
     * @param timezone the timezone identifier
     * @return a formatted string representing the current date and time in the specified timezone
     */
    private String getCurrentTimeInTimeZone(String timezone) {
        ZoneId zoneId = ZoneId.of(timezone, ZoneId.SHORT_IDS);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        return zonedDateTime.format(formatter);
    }
}
