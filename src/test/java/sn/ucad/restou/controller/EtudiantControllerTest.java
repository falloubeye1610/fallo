package sn.ucad.restou.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Tests du EtudiantController")
class EtudiantControllerTest {
    @LocalServerPort
    private int port;
    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + port);
    }

    // Methode utilitaire pour extraire l'ID du JSON
    private Long extractId(String json) {
        Pattern pattern = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        throw new RuntimeException("ID non trouve dans: " + json);
    }

    @Test
    @DisplayName("GET /api/etudiants-Retourne 200")
    void recupererTous_retourne200() {
        String response = restClient.get()
                .uri("/api/etudiants")
                .retrieve()
                .body(String.class);
        assertNotNull(response);
    }

    @Test
    @DisplayName("GET /api/etudiants/{id}-Retourne 404 si inexistant")
    void recupererParId_retourne404() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> {
            restClient.get()
                    .uri("/api/etudiants/999")
                    .retrieve()
                    .body(String.class);
        });
    }

    @Test
    @DisplayName("POST /api/etudiants-Retourne 201 avec donnees valides")
    void creer_retourne201() {
        String json = """
                {
                "nom": "Ndiaye",
                "prenom": "Moussa",
                "email": "moussa@ucad.edu.sn",
                "numeroCarte": "ETU-2024-003"
                }
                """;
        String response = restClient.post()
                .uri("/api/etudiants")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .body(String.class);
        assertNotNull(response);
        assertTrue(response.contains("Ndiaye"));
        assertTrue(response.contains("\"id\""));
    }

    @Test
    @DisplayName("POST puis GET-Cree et recupere etudiant")
    void creerPuisRecuperer() {
        // Creer
        String json = """
                {
                "nom": "Diallo",
                "prenom": "Fatou",
                "email": "fatou@ucad.edu.sn",
                "numeroCarte": "ETU-2024-001"
                }
                """;
        String createResponse = restClient.post()
                .uri("/api/etudiants")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .body(String.class);
        // Extraire l'ID avec regex
        Long id = extractId(createResponse);
        // Recuperer
        String getResponse = restClient.get()
                .uri("/api/etudiants/" + id)
                .retrieve()
                .body(String.class);
        assertTrue(getResponse.contains("Diallo"));
    }

    @Test
    @DisplayName("DELETE-Supprime etudiant")
    void supprimer() {
        // Creer
        String json = """
                {
                "nom": "Sow",
                "prenom": "Aminata",
                "email": "aminata@ucad.edu.sn",
                "numeroCarte": "ETU-2024-002"
                }
                """;
        String createResponse = restClient.post()
                .uri("/api/etudiants")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .body(String.class);
        Long id = extractId(createResponse);
        // Supprimer
        restClient.delete()
                .uri("/api/etudiants/" + id)
                .retrieve()
                .toBodilessEntity();
        // Verifier suppression
        assertThrows(HttpClientErrorException.NotFound.class, () -> {
            restClient.get()
                    .uri("/api/etudiants/" + id)
                    .retrieve()
                    .body(String.class);
        });
    }

    @Test
    @DisplayName("POST- Retourne 400 si invalide")
    void creer_retourne400() {
        String json = """
                {
                "nom": "",
                "prenom": "",
                "email": "invalide",
                "numeroCarte": ""
                }
                """;
        assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            restClient.post()
                    .uri("/api/etudiants")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .retrieve()
                    .body(String.class);
        });
    }
}