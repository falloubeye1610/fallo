package sn.ucad.restou.service;

import org.springframework.stereotype.Service;
import sn.ucad.restou.entity.Ticket;
import sn.ucad.restou.exception.ResourceNotFoundException;
import sn.ucad.restou.repository.TicketRepository;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Iterable<Ticket> recupererTous() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> recupererParId(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket creer(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket mettreAJour(Long id, Ticket ticket) {
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("Ticket non trouvé");
        }
        ticket.setId(id);
        return ticketRepository.save(ticket);
    }

    public void supprimer(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("Ticket non trouvé");
        }
        ticketRepository.deleteById(id);
    }

    public Iterable<Ticket> recupererParStatut(Boolean utilise) {
        return ticketRepository.findByUtilise(utilise);
    }
    
    public Iterable<Ticket> recupererParEtudiantId(Long etudiantId) {
        return ticketRepository.findByEtudiantId(etudiantId);
    }

    public Ticket valider(Long id) {
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket non trouvé", "code", id));
        if (ticket.getUtilise()) {
            throw new ResourceNotFoundException("Ce ticket a déjà été utilisé", "code", id);
        }
        if (ticket.getDateValidite().isBefore(java.time.LocalDate.now())) {
            throw new ResourceNotFoundException("Ce ticket est expiré", "code", id);
        }
        ticket.setUtilise(true);
        return ticketRepository.save(ticket);
    }
}
