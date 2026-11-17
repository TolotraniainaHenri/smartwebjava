
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Vérifier si l'utilisateur est connecté
    if (session.getAttribute("email") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Empêcher le navigateur de conserver la page en cache[cite: 3]
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SMARTWEB AI | Accueil</title>
    <link rel="stylesheet" href="css/navbar.css">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/sky.css">
    
</head>
<body>

    <%@ include file="/WEB-INF/navbar.jspf" %>

    <main class="hero">
        <div class="hero-content">
            <p class="subtitle">L'INTELLIGENCE AU SERVICE DE VOTRE QUOTIDIEN</p>

            <h1>Bienvenue sur <span>SMARTWEB AI</span></h1>

           <!-- <p class="description">
                Découvrez une nouvelle expérience numérique grâce à
                l'intelligence artificielle et aux informations météorologiques
                en temps réel.
            </p> --> 

            <div class="cards">

                <div class="card">
                    <div class="icon">🌤️</div>
                    <h2>Smart Weather</h2>
                    <p>Consultez les prévisions météorologiques de votre ville.</p>
                    <a href="weather.jsp" class="btn">Découvrir la météo</a>
                </div>

                <div class="card">
                    <div class="icon">🤖</div>
                    <h2>Smart Chat</h2>
                    <p>Discutez avec votre assistant intelligent propulsé par l'IA.</p>
                    <a href="chat.jsp" class="btn">Parler à l'IA</a>
                </div>

            </div>
        </div>
    </main>

    <footer>
        <p>© 2026 SMARTWEB AI BY HENRI — Tous droits réservés.</p>
    </footer>
<script src="js/navbar.js"></script>
</body>
</html>