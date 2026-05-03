package sn.ucad.restou.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'GERANT')")
    public Iterable<Etudiant> listerTous() {
        return etudiantService.listerTous();
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Etudiant> recupererParId(@PathVariable Long id) {
        return etudiantService.recupererParId(id)
                .map(etudiant -> ResponseEntity.ok().body(etudiant))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Etudiant> creer(@Valid @RequestBody Etudiant etudiant) {
        Etudiant nouveauEtudiant = etudiantService.creer(etudiant);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauEtudiant);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Etudiant> modifier(@PathVariable Long id, @Valid @RequestBody Etudiant etudiant) {
        return ResponseEntity.ok(etudiantService.modifier(id, etudiant));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        etudiantService.supprimer(id);
        return ResponseEntity.noContent().build();

    }
    
    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'CAISSIER')")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(etudiantService.count());
    }

    

}
