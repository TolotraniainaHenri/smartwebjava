package com.smartweb.util;

/**
 * Utilitaires JSON minimalistes (sans bibliothèque externe).
 */
public final class JsonUtil {

    private JsonUtil() {
    }

    /**
     * Échappe une chaîne pour l'insérer dans une valeur JSON.
     */
    public static String escape(String text) {

        if (text == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder(text.length() + 16);

        for (int i = 0; i < text.length(); i++) {

            char c = text.charAt(i);

            switch (c) {
                case '\\': sb.append("\\\\"); break;
                case '"':  sb.append("\\\""); break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                case '\b': sb.append("\\b");  break;
                case '\f': sb.append("\\f");  break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }

        return sb.toString();
    }
}