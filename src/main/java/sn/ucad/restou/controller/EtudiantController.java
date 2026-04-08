package sn.ucad.restou.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import sn.ucad.restou.entity.Etudiant;
import sn.ucad.restou.service.EtudiantService;


@RestController
@RequestMapping("/api/etudiants")
public class EtudiantController {

    private final EtudiantService etudiantService;

    public EtudiantController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @GetMapping
    public Iterable<Etudiant> recupereTous() {
        return etudiantService.recupererTous();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Etudiant> recupereParId(@PathVariable Long id) {
        return etudiantService.recupererParId(id)
                .map(etudiant -> ResponseEntity.ok().body(etudiant))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Etudiant> creer(@Valid @RequestBody Etudiant etudiant) {
        Etudiant nouveauEtudiant = etudiantService.creer(etudiant);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauEtudiant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Etudiant> mettreAJour(@PathVariable Long id, @Valid @RequestBody Etudiant etudiant) {
        return ResponseEntity.ok(etudiantService.mettreAJour(id, etudiant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        etudiantService.supprimer(id);
        return ResponseEntity.noContent().build();
    
    }
}
