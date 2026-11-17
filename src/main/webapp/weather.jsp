
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>SMART WEATHER | SMARTWEB AI</title>

    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/navbar.css">
    
</head>

<body class="weather-page">

   <%@ include file="/WEB-INF/navbar.jspf" %>

    <main class="hero">

        <div class="hero-content">

            <p class="subtitle">SMART WEATHER</p>

            <h1>La météo de votre <span>ville</span></h1>

            <p class="description">
                Découvrez les conditions météorologiques actuelles
                et les informations essentielles de votre ville.
            </p>

            <div class="weather-search">

                <input
                    type="text"
                    id="city"
                    placeholder="Entrez le nom de votre ville..."
                >

                <button class="btn" id="searchBtn">
                    Rechercher
                </button>

            </div>

            <div id="weatherResult" class="weather-result">

                <div class="weather-icon">🌤️</div>

                <h2>Bienvenue sur SMART WEATHER</h2>

                <p>
                    Saisissez le nom d'une ville pour consulter
                    ses informations météorologiques.
                </p>

            </div>

        </div>

    </main>

    <footer>
        <p>© 2026 SMARTWEB AI BY HENRI — Tous droits réservés.</p>
    </footer>
<script src="js/weather.js"></script>
<script src="js/navbar.js"></script>
</body>
</html>
