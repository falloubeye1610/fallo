package sn.ucad.restou.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")

public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    @NotNull(message = "L'étudiant est obligatoire")
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Etudiant etudiant;

    @NotBlank(message = "Le code du ticket est obligatoire")
    @Pattern(regexp = "^TKT-\\d{4}-\\d{3}$",
            message = "Le code du ticket doit être au format TKT-YYYY-NNN (exemple : TKT-2024-001)")  
    @Column(name = "code_ticket", nullable = false, unique = true)
    private String codeTicket;

    @NotNull(message = "La date d'achat est obligatoire")
    @PastOrPresent(message = "La date d'achat ne peut pas être dans le futur")
    @Column(name = "date_achat", nullable = false)
    private LocalDateTime dateAchat;

    @NotNull(message = "La date de validite est obligatoire")
    @FutureOrPresent(message = "La date de validité doit être aujourd'hui ou dans le futur")
    @Column(name = "date_validite", nullable = false)
    private LocalDate dateValidite;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    @Column(nullable = false)
    private Double prix;

    @Column(nullable = false)
    private boolean utilise = false;

    // Constructeurs

    public Ticket(Etudiant etudiant, String codeTicket, LocalDateTime dateAchat, LocalDate dateValidite, Double prix) {
        this.etudiant = etudiant;
        this.codeTicket = codeTicket;
        this.dateAchat = dateAchat;
        this.dateValidite = dateValidite;
        this.prix = prix;
    }

    public Ticket() {
    }   
    
    // Getters et setters
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Etudiant getEtudiant() {
        return etudiant;
    }
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }
    public String getCodeTicket() {
        return codeTicket;
    }
    public void setCodeTicket(String codeTicket) {
        this.codeTicket = codeTicket;
    }
    public LocalDateTime getDateAchat() {
        return dateAchat;
    }
    public void setDateAchat(LocalDateTime dateAchat) {
        this.dateAchat = dateAchat;
    }
    public LocalDate getDateValidite() {
        return dateValidite;
    }
    public void setDateValidite(LocalDate dateValidite) {
        this.dateValidite = dateValidite;
    }
    public Double getPrix() {
        return prix;
    }
    public void setPrix(Double prix) {
        this.prix = prix;
    }
    public boolean getUtilise() {
        return utilise;
    }
    public void setUtilise(boolean utilise) {
        this.utilise = utilise;
    }

}
