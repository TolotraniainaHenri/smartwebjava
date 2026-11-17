document.addEventListener("DOMContentLoaded", function () {

    const searchBtn = document.getElementById("searchBtn");
    const cityInput = document.getElementById("city");
    const weatherResult = document.getElementById("weatherResult");

    searchBtn.addEventListener("click", searchWeather);

    cityInput.addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            searchWeather();
        }
    });

    async function searchWeather() {

        const city = cityInput.value.trim();

        if (city === "") {
            alert("Veuillez entrer le nom d'une ville !");
            return;
        }

        weatherResult.innerHTML = `
            <div class="weather-icon">🔍</div>
            <h2>Recherche en cours...</h2>
            <p>Connexion à Open-Meteo...</p>
        `;

        searchBtn.disabled = true;

        try {

            const response = await fetch(
                "weather?city=" + encodeURIComponent(city)
            );

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.error || "Erreur météo.");
            }

            console.log("Données météo reçues :", data);

            displayWeather(data, city);

        } catch (error) {

            weatherResult.innerHTML = `
                <div class="weather-icon">⚠️</div>
                <h2>Erreur</h2>
                <p>${error.message}</p>
            `;

            console.error(error);

        } finally {
            searchBtn.disabled = false;
        }
    }

    /*function displayWeather(data, city) {

        const current = data.current;

        const temperature = current.temperature_2m;
        const humidity = current.relative_humidity_2m;
        const wind = current.wind_speed_10m;
        const apparent = current.apparent_temperature;

        weatherResult.innerHTML = `
            <div class="weather-icon">🌤️</div>

            <h2>${city}</h2>

            <div class="temperature">
                ${temperature} °C
            </div>

            <p>Température ressentie : ${apparent} °C</p>
            <p>💧 Humidité : ${humidity}%</p>
            <p>💨 Vitesse du vent : ${wind} km/h</p>
        `;
    }*/
	function displayWeather(data, city) {

	    //const current = data.current;
		const current = data.weather.current;

		const cityName = data.city;
		const region = data.region;
		const country = data.country;

	    const temperature = current.temperature_2m;
	    const humidity = current.relative_humidity_2m;
	    const wind = current.wind_speed_10m;
	    const apparent = current.apparent_temperature;
	    const weatherCode = current.weather_code;

	    let icon = "🌤️";
	    let description = "Conditions météorologiques";

	    if (weatherCode === 0) {
	        icon = "☀️";
	        description = "Ciel dégagé";
	    } else if (weatherCode === 1) {
	        icon = "🌤️";
	        description = "Principalement dégagé";
	    } else if (weatherCode === 2) {
	        icon = "⛅";
	        description = "Partiellement nuageux";
	    } else if (weatherCode === 3) {
	        icon = "☁️";
	        description = "Ciel couvert";
	    } else if ([45, 48].includes(weatherCode)) {
	        icon = "🌫️";
	        description = "Brouillard";
	    } else if ([51, 53, 55, 56, 57].includes(weatherCode)) {
	        icon = "🌦️";
	        description = "Bruine";
	    } else if ([61, 63, 65, 66, 67, 80, 81, 82].includes(weatherCode)) {
	        icon = "🌧️";
	        description = "Pluie";
	    } else if ([71, 73, 75, 77, 85, 86].includes(weatherCode)) {
	        icon = "❄️";
	        description = "Neige";
	    } else if ([95, 96, 99].includes(weatherCode)) {
	        icon = "⛈️";
	        description = "Orage";
	    }

	    weatherResult.innerHTML = `
	        <div class="weather-icon">${icon}</div>

			<h2>${cityName}, ${country} 🌍</h2>
			<p>${region}</p>

	        <h3>${description}</h3>

	        <div class="temperature">
	            ${temperature} °C
	        </div>

	        <p>🌡️ Température ressentie : ${apparent} °C</p>
	        <p>💧 Humidité : ${humidity}%</p>
	        <p>💨 Vitesse du vent : ${wind} km/h</p>
	    `;
	}

});