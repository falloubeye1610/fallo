package sn.ucad.restou.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sn.ucad.restou.entity.Etudiant;
import sn.ucad.restou.entity.Ticket;
import sn.ucad.restou.exception.ResourceNotFoundException;
import sn.ucad.restou.repository.TicketRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du TicketService")
public class TicketServiceTest {
    @Mock
    private TicketRepository ticketRepository;
    @InjectMocks
    private TicketService ticketService;
    private Ticket ticket;
    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant("Ndiaye", "Moussa", "moussa@ucad.edu.sn", "ETU-2024-001");
        etudiant.setId(1L);

        ticket = new Ticket();
        ticket.setEtudiant(etudiant);
        ticket.setCodeTicket("TKT-2024-001");
        ticket.setDateAchat(LocalDateTime.now());
        ticket.setDateValidite(LocalDate.now().plusDays(2));
        ticket.setPrix(500.0);
        ticket.setUtilise(false);
        ticket.setId(1L);
    }

    @Test
    @DisplayName("creer-Sauvegarde et retourne le ticket")
    void creer_avecTicketValide_retourneTicket() {
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket resultat = ticketService.creer(ticket);

        assertNotNull(resultat);
        assertEquals("TKT-2024-001", resultat.getCodeTicket());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    @DisplayName("recupererTous-Retourne la liste des tickets")
    void recupererTous_retourneListeTickets() {
        Ticket ticket2 = new Ticket();
        ticket2.setCodeTicket("TKT-2024-002");
        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticket, ticket2));

        Iterable<Ticket> resultat = ticketService.recupererTous();

        assertNotNull(resultat);
        List<Ticket> tickets = (List<Ticket>) resultat;
        assertEquals(2, tickets.size());
        assertEquals("TKT-2024-001", tickets.get(0).getCodeTicket());
        assertEquals("TKT-2024-002", tickets.get(1).getCodeTicket());
    }

    @Test
    @DisplayName("recupererParId-avecIdExistant_retourneTicket")
    void recupererParId_avecIdExistant_retourneTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Ticket resultat = ticketService.recupererParId(1L).orElse(null);

        assertNotNull(resultat);
        assertEquals("TKT-2024-001", resultat.getCodeTicket());
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("mettreAJour-avecIdExistant_retourneTicketMisAJour")
    void mettreAJour_avecIdExistant_retourneTicketMisAJour() {
        Ticket ticketMisAJour = new Ticket();
        ticketMisAJour.setCodeTicket("TKT-2024-009");
        ticketMisAJour.setId(1L);
        when(ticketRepository.existsById(1L)).thenReturn(true);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticketMisAJour);

        Ticket resultat = ticketService.mettreAJour(1L, ticketMisAJour);

        assertNotNull(resultat);
        assertEquals("TKT-2024-009", resultat.getCodeTicket());
        verify(ticketRepository, times(1)).existsById(1L);
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    @DisplayName("mettreAJour-avecIdNonExistant_lanceException")
    void mettreAJour_avecIdNonExistant_lanceException() {
        Ticket ticketMisAJour = new Ticket();
        ticketMisAJour.setId(1L);
        when(ticketRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> ticketService.mettreAJour(1L, ticketMisAJour));
        verify(ticketRepository, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("supprimer-avecIdExistant_supprimeTicket")
    void supprimer_avecIdExistant_supprimeTicket() {
        when(ticketRepository.existsById(1L)).thenReturn(true);

        ticketService.supprimer(1L);

        verify(ticketRepository, times(1)).existsById(1L);
        verify(ticketRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("supprimer-avecIdNonExistant_lanceException")
    void supprimer_avecIdNonExistant_lanceException() {
        when(ticketRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> ticketService.supprimer(999L));
        verify(ticketRepository, times(1)).existsById(999L);
    }

    @Test
    @DisplayName("valider-avecIdValide_retourneTicketValide")
    void valider_avecIdValide_retourneTicketValide() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket resultat = ticketService.valider(1L);

        assertTrue(resultat.getUtilise());
        verify(ticketRepository, times(1)).save(ticket);
    }

    // Exercice 3 : Utilisez @ParameterizedTest pour tester plusieurs cas de validation invalides en une seule méthode
    @ParameterizedTest
    @CsvSource({
        "true, 1, 'Ce ticket a déjà été utilisé'",
        "false, -1, 'Ce ticket est expiré'"
    })
    @DisplayName("valider-cas invalides (déjà utilisé ou expiré) lance exception")
    void valider_casInvalides_lanceException(boolean utilise, int daysToAdd, String expectedResourceMsg) {
        Ticket invalidTicket = new Ticket();
        invalidTicket.setUtilise(utilise);
        invalidTicket.setDateValidite(LocalDate.now().plusDays(daysToAdd));

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(invalidTicket));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> ticketService.valider(1L));
        assertEquals(expectedResourceMsg, ex.getRessource());
    }

    @Test
    @DisplayName("valider-avecIdNonExistant_lanceException")
    void valider_avecIdNonExistant_lanceException() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> ticketService.valider(999L));
        assertEquals("Ticket non trouvé", ex.getRessource());
    }
}

