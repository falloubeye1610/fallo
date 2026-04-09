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
@DisplayName("Tests du TicketController")
class TicketControllerTest {
    @LocalServerPort
    private int port;
    private RestClient restClient;
    private Long etudiantId;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + port);
        
        // Créer un étudiant pour pouvoir créer des tickets
        String etudiantJson = """
                {
                "nom": "Diop",
                "prenom": "Awa",
                "email": "awa.diop@ucad.edu.sn",
                "numeroCarte": "ETU-2024-101"
                }
                """;
        String response = restClient.post()
                .uri("/api/etudiants")
                .contentType(MediaType.APPLICATION_JSON)
                .body(etudiantJson)
                .retrieve()
                .body(String.class);
        etudiantId = extractId(response);
    }

    private Long extractId(String json) {
        Pattern pattern = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        throw new RuntimeException("ID non trouve dans: " + json);
    }
    
    private String createTicketJson(String code) {
        // La date de validite doit etre dans le futur
        return String.format("""
                {
                "etudiant": { "id": %d },
                "codeTicket": "%s",
                "dateAchat": "2024-01-01T10:00:00",
                "dateValidite": "2030-12-31",
                "prix": 500.0
                }
                """, etudiantId, code);
    }

    @Test
    @DisplayName("GET /api/tickets-Retourne 200")
    void recupererTous_retourne200() {
        String response = restClient.get()
                .uri("/api/tickets")
                .retrieve()
                .body(String.class);
        assertNotNull(response);
    }

    @Test
    @DisplayName("GET /api/tickets/{id}-Retourne 404 si inexistant")
    void recupererParId_retourne404() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> {
            restClient.get()
                    .uri("/api/tickets/999")
                    .retrieve()
                    .body(String.class);
        });
    }

    @Test
    @DisplayName("POST /api/tickets-Retourne 201 avec donnees valides")
    void creer_retourne201() {
        String json = createTicketJson("TKT-2024-001");
        String response = restClient.post()
                .uri("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .body(String.class);
        assertNotNull(response);
        assertTrue(response.contains("TKT-2024-001"));
        assertTrue(response.contains("\"id\""));
    }

    @Test
    @DisplayName("POST puis GET-Cree et recupere ticket")
    void creerPuisRecuperer() {
        String json = createTicketJson("TKT-2024-002");
        String createResponse = restClient.post()
                .uri("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .body(String.class);
        Long id = extractId(createResponse);

        String getResponse = restClient.get()
                .uri("/api/tickets/" + id)
                .retrieve()
                .body(String.class);
        assertTrue(getResponse.contains("TKT-2024-002"));
    }

    @Test
    @DisplayName("DELETE-Supprime ticket")
    void supprimer() {
        String json = createTicketJson("TKT-2024-003");
        String createResponse = restClient.post()
                .uri("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .body(String.class);
        Long id = extractId(createResponse);

        restClient.delete()
                .uri("/api/tickets/" + id)
                .retrieve()
                .toBodilessEntity();

        assertThrows(HttpClientErrorException.NotFound.class, () -> {
            restClient.get()
                    .uri("/api/tickets/" + id)
                    .retrieve()
                    .body(String.class);
        });
    }

    @Test
    @DisplayName("POST- Retourne 400 si invalide")
    void creer_retourne400() {
        String json = """
                {
                "etudiant": null,
                "codeTicket": "",
                "dateAchat": "2024-01-01T10:00:00",
                "dateValidite": "2020-01-01",
                "prix": -10.0
                }
                """;
        assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            restClient.post()
                    .uri("/api/tickets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .retrieve()
                    .body(String.class);
        });
    }
}
