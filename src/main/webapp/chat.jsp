<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<% 
    if (session.getAttribute("email") == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    String email = (String) session.getAttribute("email");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>SMART CHAT | AI Assistant</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
</head>

<body class="chat-page">

    <div class="chat-container">

        <!-- BARRE LATERALE -->
        <aside class="chat-sidebar">

            <div class="chat-logo">
                <i class="fa-solid fa-robot"></i>
                <span>SMART CHAT</span>
            </div>

            <button class="new-chat-btn" id="newChatBtn">
                <i class="fa-solid fa-plus"></i>
                Nouvelle conversation
            </button>

            <div class="chat-history">
                <p class="history-title">CONVERSATIONS</p>
                <div id="conversationList"></div>
            </div>

            <div class="chat-user">
                <i class="fa-solid fa-circle-user"></i>

                <div>
                    <strong>Utilisateur</strong>
                    <small><%= email %></small>
                </div>
            </div>

        </aside>

        <!-- ZONE PRINCIPALE -->
        <main class="chat-main">

            <header class="chat-header">
                <div>
                    <h2>SMART CHAT <span>AI</span></h2>
                    <p>Votre assistant intelligent personnel</p>
                </div>

                <a href="logout.jsp" class="chat-logout">
                    <i class="fa-solid fa-right-from-bracket"></i>
                </a>
            </header>

            <section class="chat-messages" id="chatMessages">

                <div class="welcome-message">
                    <div class="welcome-icon">
                        <i class="fa-solid fa-robot"></i>
                    </div>

                    <h1>Bonjour ! Bienvenue sur SMART CHAT.</h1>

                    <p>
                        Je suis votre assistant intelligent.
                        Posez-moi une question, je suis prêt à vous aider !
                    </p>
                </div>

            </section>

            <!-- ZONE DE SAISIE -->
            <footer class="chat-input-area">

                <div class="chat-input-box">

                    <textarea
                        id="userMessage"
                        placeholder="Écrivez votre message ici..."
                        rows="1"></textarea>

                    <button id="sendBtn" title="Envoyer">
                        <i class="fa-solid fa-paper-plane"></i>
                    </button>

                </div>

                <p class="chat-disclaimer">
                    SMART CHAT peut faire des erreurs. Vérifiez les informations importantes.
                </p>

            </footer>

        </main>

    </div>
<script src="${pageContext.request.contextPath}/js/chat.js"></script>
</body>
</html>