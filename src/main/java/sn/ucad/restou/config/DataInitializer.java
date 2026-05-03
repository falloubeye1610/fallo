package sn.ucad.restou.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import sn.ucad.restou.entity.Role;
import sn.ucad.restou.entity.Utilisateur;
import sn.ucad.restou.repository.UtilisateurRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner InitData(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {

        return args -> {

            if (utilisateurRepository.count() > 0) {
                return;
            }

            // Etudiante
            Utilisateur fatou = new Utilisateur(
                    "Diallo", "Fatou",
                    "fatou@ucad.edu.sn",
                    passwordEncoder.encode("password123"),
                    Role.ETUDIANT);
            utilisateurRepository.save(fatou);
            // Gerant
            Utilisateur moussa = new Utilisateur(
                    "Ndiaye", "Moussa",
                    "moussa@ucad.edu.sn",
                    passwordEncoder.encode("password123"),
                    Role.GERANT);
            utilisateurRepository.save(moussa);
            // Admin
            Utilisateur aminata = new Utilisateur(
                    "Sow", "Aminata",
                    "aminata@ucad.edu.sn",
                    passwordEncoder.encode("password123"),
                    Role.ADMIN);
            utilisateurRepository.save(aminata);

            // Caissier
            Utilisateur ousmane = new Utilisateur(
                    "Mbaye", "Ousmane",
                    "ousmane@ucad.edu.sn",
                    passwordEncoder.encode("password123"),
                    Role.CAISSIER);
            utilisateurRepository.save(ousmane);

            System.out.println("=== Donnees de test chargees ===");
            System.out.println("ETUDIANT : fatou@ucad.edu.sn / password123");
            System.out.println("GERANT: moussa@ucad.edu.sn / password123");
            System.out.println("ADMIN: aminata@ucad.edu.sn / password123");
            System.out.println("CAISSIER: ousmane@ucad.edu.sn / password123");

        };
    }
}
