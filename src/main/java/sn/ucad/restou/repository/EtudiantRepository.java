package sn.ucad.restou.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sn.ucad.restou.entity.Etudiant;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends CrudRepository<Etudiant, Long> {
    Optional<Etudiant> findByEmail(String email);
    Optional<Etudiant> findByNumeroCarte(String numeroCarte);
    Iterable<Etudiant> findByNom(String nom);
}