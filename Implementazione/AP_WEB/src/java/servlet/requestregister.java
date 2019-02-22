/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import ap_utility.ClientConnector;
import ap_utility.Pacchetto;
import ap_utility.ServerException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author saponaro.andrea
 */
@WebServlet(name = "requestregister", urlPatterns = {"/requestregister"})
public class requestregister extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet requestregister</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet requestregister at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.h
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String answer = ClientConnector.request(Pacchetto.incapsula(2, request.getParameter("nome"), request.getParameter("cognome"), request.getParameter("mail"), request.getParameter("password"), request.getParameter("cell"), request.getParameter("indirizzo"), request.getParameter("tipocliente")));
            String[] results = Pacchetto.estrai(answer);
            request.setAttribute("BoolMail", Boolean.parseBoolean(results[1]));
            request.setAttribute("BoolPsw", Boolean.parseBoolean(results[2]));
            request.setAttribute("BoolCell", Boolean.parseBoolean(results[3]));
            request.setAttribute("BoolIndirizzo", Boolean.parseBoolean(results[4]));
            request.getRequestDispatcher("esito.jsp").forward(request, response);
        } catch (ServerException ex) {
            Logger.getLogger(loginrequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
