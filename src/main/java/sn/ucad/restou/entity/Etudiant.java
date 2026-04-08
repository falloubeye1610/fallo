package sn.ucad.restou.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "etudiants")
public class Etudiant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit être entre 2 et 50 caractères")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50, message = "Le prénom doit être entre 2 et 50 caractères")
    @Column(nullable = false)   
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email n'est pas valide")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Le numéro de carte est obligatoire")
    @Pattern(regexp = "^ETU-\\d{4}-\\d{3}$",
            message = "Le numéro de carte doit être au format ETU-YYYY-NNN (exemple : ETU-2024-001)")
    @Column(name = "numero_carte", nullable = false, unique = true)
    private String numeroCarte;

    // Constructeurs, getters et setters

    public Etudiant(String nom, String prenom, String email, String numeroCarte) {
        this.nom = nom;
        this.prenom = prenom;   
        this.email = email;
        this.numeroCarte = numeroCarte;
    }

    public Etudiant() {
    }

    public Long getId() {
        return id;
    }  
    public void setId(Long id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNumeroCarte() {
        return numeroCarte;
    }
    public void setNumeroCarte(String numeroCarte) {
        this.numeroCarte = numeroCarte;
    }
}