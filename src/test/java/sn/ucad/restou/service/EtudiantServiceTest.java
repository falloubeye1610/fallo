package sn.ucad.restou.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sn.ucad.restou.entity.Etudiant;
import sn.ucad.restou.exception.ResourceNotFoundException;
import sn.ucad.restou.repository.EtudiantRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du EtudiantService")
class EtudiantServiceTest {
    @Mock
    private EtudiantRepository etudiantRepository;
    @InjectMocks
    private EtudiantService etudiantService;
    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant("Diallo", "Fatou",
                "fatou.diallo@ucad.edu.sn", "ETU-2024-001");
        etudiant.setId(1L);
    }

    @Test
    @DisplayName("creer-Sauvegarde et retourne l'etudiant")
    void creer_avecEtudiantValide_retourneEtudiant() {
        // ARRANGE
        when(etudiantRepository.save(any(Etudiant.class)))
                .thenReturn(etudiant);
        // ACT
        Etudiant resultat = etudiantService.creer(etudiant);
        // ASSERT
        assertNotNull(resultat);
        assertEquals("Diallo", resultat.getNom());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    @DisplayName("recupererTous-Retourne la liste des etudiants")
    void recupererTous_retourneListeEtudiants() {
        // ARRANGE
        Etudiant etudiant2 = new Etudiant("Sow", "Aminata",
                "aminata.sow@ucad.edu.sn", "ETU-2024-002");
        when(etudiantRepository.findAll())
                .thenReturn(Arrays.asList(etudiant, etudiant2));
        // ACT
        Iterable<Etudiant> resultat = etudiantService.recupererTous();
        // ASSERT
        assertNotNull(resultat);
        List<Etudiant> liste = (List<Etudiant>) resultat;
        assertEquals(2, liste.size());
    }

    @Test
    @DisplayName("recupererParId-Retourne l'etudiant si existe")
    void recupererParId_avecIdExistant_retourneEtudiant() {
        // ARRANGE
        when(etudiantRepository.findById(1L))
                .thenReturn(Optional.of(etudiant));
        // ACT
        Optional<Etudiant> resultat = etudiantService.recupererParId(1L);
        // ASSERT
        assertTrue(resultat.isPresent());
        assertEquals("Diallo", resultat.get().getNom());
    }

    @Test
    @DisplayName("recupererParId-Retourne vide si n'existe pas")
    void recupererParId_avecIdInexistant_retourneVide() {
        // ARRANGE
        when(etudiantRepository.findById(999L))
                .thenReturn(Optional.empty());
        // ACT
        Optional<Etudiant> resultat = etudiantService.recupererParId(999L);
        // ASSERT
        assertTrue(resultat.isEmpty());
    }

    @Test
    @DisplayName("mettreAJour-Lance exception si ID n'existe pas")
    void mettreAJour_avecIdInexistant_lanceException() {
        // ARRANGE
        when(etudiantRepository.findById(999L))
                .thenReturn(Optional.empty());
        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            etudiantService.mettreAJour(999L, etudiant);
        });
        verify(etudiantRepository, never()).save(any());
    }

    @Test
    @DisplayName("supprimer- Supprime l'etudiant existant")
    void supprimer_avecIdExistant_supprimeLEtudiant() {
        // ARRANGE
        when(etudiantRepository.findById(1L))
                .thenReturn(Optional.of(etudiant));
        // ACT
        etudiantService.supprimer(1L);
        // ASSERT
        verify(etudiantRepository).findById(1L);
        verify(etudiantRepository).delete(etudiant);
    }
}
