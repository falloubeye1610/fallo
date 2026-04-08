// package sn.ucad.restou.service;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import sn.ucad.restou.entity.Etudiant;
// import sn.ucad.restou.entity.Ticket;
// import sn.ucad.restou.exception.ResourceNotFoundException;
// import sn.ucad.restou.repository.EtudiantRepository;
// import sn.ucad.restou.repository.TicketRepository;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// @DisplayName("Tests du TicketService")
// public class TicketServiceTest {
//     @Mock
//     private TicketRepository ticketRepository;
//     @InjectMocks
//     private TicketService ticketService;
//     private Ticket ticket;

//     @BeforeEach
//     void setUp() {
//         ticket = new Ticket();
//         ticket.setType("Repas");
//         ticket.setDescription("Ticket pour un repas au restaurant universitaire");
//         ticket.setId(1L);
//     }

//     @Test
//     @DisplayName("creer-Sauvegarde et retourne le ticket")
//     void creer_avecTicketValide_retourneTicket() {
//         // ARRANGE
//         when(ticketRepository.save(any(Ticket.class)))
//                 .thenReturn(ticket);
//         // ACT
//         Ticket resultat = ticketService.creer(ticket);
//         // ASSERT
//         assertNotNull(resultat);
//         assertEquals("Repas", resultat.getType());
//         verify(ticketRepository, times(1)).save(ticket);
//     }

//     @Test
//     @DisplayName("recupererTous-Retourne la liste des tickets")
//     void recupererTous_retourneListeTickets() {
//         // ARRANGE
//         Ticket ticket2 = new Ticket();
//         ticket2.setType("Boisson");
//         ticket2.setDescription("Ticket pour une boisson au restaurant universitaire");
//         when(ticketRepository.findAll())
//                 .thenReturn(Arrays.asList(ticket, ticket2));
//         // ACT
//         Iterable<Ticket> resultat = ticketService.recupererTous();
//         // ASSERT
//         assertNotNull(resultat);
//         List<Ticket> tickets = (List<Ticket>) resultat;
//         assertEquals(2, tickets.size());
//         assertEquals("Repas", tickets.get(0).getType());
//         assertEquals("Boisson", tickets.get(1).getType());
//     }

//     @Test
//     @DisplayName("recupererParId-avecIdExistant_retourneTicket")
//     void recupererParId_avecIdExistant_retourneTicket() {
//         // ARRANGE
//         when(ticketRepository.findById(1L))
//                 .thenReturn(Optional.of(ticket));
//         // ACT
//         Ticket resultat = ticketService.recupererParId(1L);
//         // ASSERT
//         assertNotNull(resultat);
//         assertEquals("Repas", resultat.getType());
//         verify(ticketRepository, times(1)).findById(1L);
//     }

//     @Test
//     @DisplayName("recupererParId-avecIdNonExistant_lanceException")
//     void recupererParId_avecIdNonExistant_lanceException() {
//         // ARRANGE
//         when(ticketRepository.findById(999L))
//                 .thenReturn(Optional.empty());
//         // ACT & ASSERT
//         assertThrows(ResourceNotFoundException.class, () -> {
//             ticketService.recupererParId(999L);
//         });
//         verify(ticketRepository, times(1)).findById(999L);
//     }

//     @Test
//     @DisplayName("mettreAJour-avecIdExistant_retourneTicketMisAJour")
//     void mettreAJour_avecIdExistant_retourneTicketMisAJour() {
//         // ARRANGE
//         Ticket ticketMisAJour = new Ticket();
//         ticketMisAJour.setType("Repas");
//         ticketMisAJour.setDescription("Ticket pour un repas au restaurant universitaire - mis à jour");
//         ticketMisAJour.setId(1L);
//         when(ticketRepository.findById(1L))
//                 .thenReturn(Optional.of(ticket));
//         when(ticketRepository.save(any(Ticket.class)))
//                 .thenReturn(ticketMisAJour);
//         // ACT
//         Ticket resultat = ticketService.mettreAJour(1L, ticketMisAJour);
//         // ASSERT
//         assertNotNull(resultat);
//         assertEquals("Ticket pour un repas au restaurant universitaire - mis à jour", resultat.getDescription());
//         verify(ticketRepository, times(1)).findById(1L);
//         verify(ticketRepository, times(1)).save(any(Ticket.class));
//     }

//     @Test
//     @DisplayName("mettreAJour-avecIdNonExistant_lanceException")
//     void mettreAJour_avecIdNonExistant_lanceException() {
//         // ARRANGE
//         Ticket ticketMisAJour = new Ticket("Repas", "Ticket pour un repas au restaurant universitaire - mis à jour");
//         ticketMisAJour.setId(1L);
//         when(ticketRepository.findById(1L))
//                 .thenReturn(Optional.empty());
//         // ACT & ASSERT
//         assertThrows(ResourceNotFoundException.class, () -> {
//             ticketService.mettreAJour(1L, ticketMisAJour);
//         });
//         verify(ticketRepository, times(1)).findById(1L);
//     }

//     @Test
//     @DisplayName("supprimer-avecIdExistant_supprimeTicket")
//     void supprimer_avecIdExistant_supprimeTicket() {
//         // ARRANGE
//         when(ticketRepository.findById(1L))
//                 .thenReturn(Optional.of(ticket));
//         // ACT
//         ticketService.supprimer(1L);
//         // ASSERT
//         verify(ticketRepository, times(1)).findById(1L);
//         verify(ticketRepository, times(1)).delete(ticket);
//     }

//     @Test
//     @DisplayName("supprimer-avecIdNonExistant_lanceException")
//     void supprimer_avecIdNonExistant_lanceException() {
//         // ARRANGE
//         when(ticketRepository.findById(999L))
//                 .thenReturn(Optional.empty());
//         // ACT & ASSERT
//         assertThrows(ResourceNotFoundException.class, () -> {
//             ticketService.supprimer(999L);
//         });
//         verify(ticketRepository, times(1)).findById(999L);
//     }

// }
