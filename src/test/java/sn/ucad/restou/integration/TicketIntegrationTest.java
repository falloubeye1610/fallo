package sn.ucad.restou.integration;

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
@DisplayName("Tests d'integration Ticket")
class TicketIntegrationTest {
    @LocalServerPort
    private int port;
    private RestClient restClient;
    private Long etudiantId;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + port);
        
        // 1. CREER UN ETUDIANT
        String etudiantJson = """
                {
                "nom": "Tall",
                "prenom": "Oumar",
                "email": "oumar.tall@ucad.edu.sn",
                "numeroCarte": "ETU-2024-505"
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

    // Exercice 2 : Test d'intégration complet d'achat et validation de ticket
    @Test
    @DisplayName("Scenario complet: Achat et Validation d'un ticket")
    void scenarioComplet_AchatEtValidation() {
        // 2. ACHAT TICKET (CREATION)
        String ticketJson = String.format("""
                {
                "etudiant": { "id": %d },
                "codeTicket": "TKT-2024-999",
                "dateAchat": "2024-01-01T10:00:00",
                "dateValidite": "2030-12-31",
                "prix": 500.0
                }
                """, etudiantId);

        String createResponse = restClient.post()
                .uri("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .body(ticketJson)
                .retrieve()
                .body(String.class);
        
        Long ticketId = extractId(createResponse);
        assertTrue(createResponse.contains("TKT-2024-999"));
        assertTrue(createResponse.contains("\"utilise\":false"));

        // 3. VALIDATION TICKET
        String validationResponse = restClient.patch()
                .uri("/api/tickets/" + ticketId + "/valider")
                .retrieve()
                .body(String.class);
        
        // Verifier que le ticket est maintenant utilise
        assertTrue(validationResponse.contains("\"utilise\":true"));

        // 4. VERIFICATION DOUBLE VALIDATION DOIT ECHOUER
        assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            restClient.patch()
                    .uri("/api/tickets/" + ticketId + "/valider")
                    .retrieve()
                    .body(String.class);
        });
    }
}
