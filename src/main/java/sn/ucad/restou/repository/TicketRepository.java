package sn.ucad.restou.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sn.ucad.restou.entity.Ticket;
import java.util.Optional;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    Optional<Ticket> findByCodeTicket(String codeTicket);
    Iterable<Ticket> findByUtilise(Boolean utilise);
    Iterable<Ticket> findByEtudiantId(Long etudiantId);
}
