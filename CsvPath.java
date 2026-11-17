package util;

import jakarta.servlet.ServletContext;

public final class CsvPath {

    private CsvPath() {
    }

    /**
     * Chemin du fichier users.csv.
     * - Si la variable d'environnement DATA_DIR existe : DATA_DIR/users.csv
     * - Sinon : WEB-INF/users.csv (comportement habituel en local)
     */
    public static String users(ServletContext context) {

        String dossier = System.getenv("DATA_DIR");

        if (dossier != null && !dossier.isBlank()) {
            return dossier.trim().replace("\"", "") + "/users.csv";
        }

        return context.getRealPath("/WEB-INF") + "/users.csv";
    }
}
