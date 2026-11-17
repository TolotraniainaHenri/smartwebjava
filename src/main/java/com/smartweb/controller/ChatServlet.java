package com.smartweb.controller;

import java.io.IOException;

import com.smartweb.service.ChatService;
import com.smartweb.util.JsonUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // true = affiche la vraie cause de l'erreur dans le chat.
    // Mettre false en production.
    private static final boolean DEBUG = true;

    private ChatService chatService;

    @Override
    public void init() throws ServletException {
        chatService = new ChatService();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Récupérer le message
        String message = request.getParameter("message");

        if (message == null || message.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(
                "{\"error\":\"Veuillez saisir un message.\"}"
            );
            return;
        }

        try {

            // Envoyer le message à Groq
            String reply = chatService.sendMessage(message.trim());

            // Retourner la réponse
            response.getWriter().write(
                "{\"reply\":\"" + JsonUtil.escape(reply) + "\"}"
            );

        } catch (Exception e) {

            log("Erreur SMART CHAT", e);

            String error = DEBUG
                    ? "SMART CHAT : " + e.getMessage()
                    : "Une erreur est survenue avec SMART CHAT.";

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(
                "{\"error\":\"" + JsonUtil.escape(error) + "\"}"
            );
        }
    }
}