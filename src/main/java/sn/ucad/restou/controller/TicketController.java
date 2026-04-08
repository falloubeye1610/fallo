package sn.ucad.restou.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ucad.restou.entity.Ticket;
import sn.ucad.restou.service.TicketService;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public Iterable<Ticket> recupereTous() {
        return ticketService.recupererTous();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> recupereParId(@PathVariable Long id) {
        return ticketService.recupererParId(id)
                .map(ticket -> ResponseEntity.ok().body(ticket))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/etudiant/{etudiantId}")
    public Iterable<Ticket> recupererParEtudiant(@PathVariable Long etudiantId) {
        return ticketService.recupererParEtudiantId(etudiantId);
    }
    
    @PostMapping
    public ResponseEntity<Ticket> creer(@RequestBody Ticket ticket) {
        Ticket nouveauTicket = ticketService.creer(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauTicket);
    }

    @PatchMapping("/{id}/valider")
    public ResponseEntity<?> valider(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(ticketService.valider(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        ticketService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

}
