package sn.ucad.restou.dto;

public class AuthResponse {
    private String token;
    private String email;
    private String prenom;
    private String nom;
    private String role;

    public AuthResponse(String token, String email, String prenom, String nom, String role) {
        this.token = token;
        this.email = email;
        this.prenom = prenom;
        this.nom = nom;
        this.role = role;
    }

    // Getters seulement (pas de setters pour les champs immutables)
    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getRole() {
        return role;
    }

}
