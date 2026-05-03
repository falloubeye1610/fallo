package sn.ucad.restou.service;

import org.springframework.stereotype.Service;
import sn.ucad.restou.entity.Etudiant;
import sn.ucad.restou.exception.ResourceNotFoundException;
import sn.ucad.restou.repository.EtudiantRepository;
import java.util.Optional;

@Service
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;

    public EtudiantService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    public Etudiant creer(Etudiant etudiant) {
        // Logique pour créer un étudiant 
        return etudiantRepository.save(etudiant);
    }

    public Iterable<Etudiant> listerTous() {
        // Logique pour récupérer tous les étudiants
        return etudiantRepository.findAll();
    }

    public Optional<Etudiant> recupererParId(Long id) {
        // Logique pour récupérer un étudiant par son ID 
        return etudiantRepository.findById(id);
    }

    public Etudiant modifier(Long id, Etudiant etudiantDetails) {
        // Logique pour mettre à jour un étudiant (exemple : validation, transformation, etc.)
        // Ici, nous allons simplement retourner l'étudiant tel quel pour l'exemple
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant", "id", id));
        etudiant.setNom(etudiantDetails.getNom());
        etudiant.setPrenom(etudiantDetails.getPrenom());
        etudiant.setEmail(etudiantDetails.getEmail());
        etudiant.setNumeroCarte(etudiantDetails.getNumeroCarte());
        return etudiantRepository.save(etudiant);
    }

    public void supprimer(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant", "id", id));
        etudiantRepository.delete(etudiant);

    }
}
