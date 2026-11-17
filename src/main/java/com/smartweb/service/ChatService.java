package com.smartweb.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.smartweb.util.JsonUtil;

public class ChatService {

    private static final String API_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    // Modèle par défaut. Peut être remplacé sans toucher au code
    // en définissant la variable d'environnement GROQ_MODEL.
    private static final String DEFAULT_MODEL =
            "openai/gpt-oss-120b";

    private static final String SYSTEM_PROMPT =
            "Tu es SMART CHAT, un assistant IA intelligent, "
          + "polyvalent, pédagogique et professionnel. "
          + "Comprends toujours l'intention de l'utilisateur, "
          + "même si son message contient des fautes. "
          + "Corrige naturellement ses erreurs d'orthographe "
          + "et de grammaire lorsque cela est pertinent. "
          + "Réponds de manière intelligente, claire, précise "
          + "et naturelle. Réponds dans la langue utilisée "
          + "par l'utilisateur. Ne juge jamais l'utilisateur.";

    private final HttpClient client;

    public ChatService() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    public String sendMessage(String message) throws Exception {

        String apiKey = getEnv("GROQ_API_KEY");

        if (apiKey == null) {
            throw new IllegalStateException(
                    "La clé API GROQ_API_KEY est introuvable."
            );
        }

        String model = getEnv("GROQ_MODEL");

        if (model == null) {
            model = DEFAULT_MODEL;
        }

        String json = "{"
                + "\"model\":\"" + JsonUtil.escape(model) + "\","
                + "\"messages\":["
                + "{"
                + "\"role\":\"system\","
                + "\"content\":\"" + JsonUtil.escape(SYSTEM_PROMPT) + "\""
                + "},"
                + "{"
                + "\"role\":\"user\","
                + "\"content\":\"" + JsonUtil.escape(message) + "\""
                + "}"
                + "],"
                + "\"temperature\":0.7"
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .timeout(Duration.ofSeconds(60))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            // Le corps de la réponse contient l'explication de Groq
            throw new Exception(
                    "Erreur Groq HTTP " + response.statusCode()
                    + " (modèle : " + model + ") : " + response.body()
            );
        }

        return extractContent(response.body());
    }

    // Lit une variable d'environnement et nettoie les espaces/guillemets
    private String getEnv(String name) {

        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim().replace("\"", "");
    }

    // Extrait le champ "content" de la réponse
    private String extractContent(String json) throws Exception {

        String marker = "\"content\":";
        int start = json.indexOf(marker);

        if (start == -1) {
            throw new Exception("Réponse Groq invalide.");
        }

        start += marker.length();

        // Ignorer les espaces avant la valeur
        while (start < json.length()
                && Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        if (start >= json.length() || json.charAt(start) != '"') {
            throw new Exception("Réponse Groq vide.");
        }

        start++; // passer le guillemet ouvrant

        StringBuilder result = new StringBuilder();
        boolean escaped = false;

        for (int i = start; i < json.length(); i++) {

            char c = json.charAt(i);

            if (escaped) {
                switch (c) {
                    case 'n': result.append('\n'); break;
                    case 'r': result.append('\r'); break;
                    case 't': result.append('\t'); break;
                    case 'b': result.append('\b'); break;
                    case 'f': result.append('\f'); break;
                    case '"': result.append('"');  break;
                    case '\\': result.append('\\'); break;
                    case '/': result.append('/');  break;
                    case 'u':
                        if (i + 4 < json.length()) {
                            result.append((char) Integer.parseInt(
                                    json.substring(i + 1, i + 5), 16));
                            i += 4;
                        }
                        break;
                    default:
                        result.append('\\').append(c);
                }
                escaped = false;

            } else if (c == '\\') {
                escaped = true;

            } else if (c == '"') {
                return result.toString();

            } else {
                result.append(c);
            }
        }

        throw new Exception("Impossible de lire la réponse Groq.");
    }
}