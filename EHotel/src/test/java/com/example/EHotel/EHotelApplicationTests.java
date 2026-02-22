package com.example.EHotel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Classe de tests d'intégration pour l'application E-Hotel.
 *
 * @SpringBootTest : charge le contexte Spring complet pour les tests d'intégration.
 * Cette annotation démarre l'application entière (avec base de données) pour vérifier
 * que tous les composants se configurent et s'initialisent correctement.
 *
 * Note : Cette classe requiert une connexion à la base de données PostgreSQL configurée
 * dans application.properties pour s'exécuter avec succès.
 */
@SpringBootTest
class EHotelApplicationTests {

    /**
     * Test de chargement du contexte Spring.
     *
     * Vérifie que l'application démarre sans erreur : toutes les beans Spring
     * (contrôleurs, services, dépôts) sont correctement configurés et injectés.
     *
     * Ce test échoue si :
     *   - Une dépendance manque ou est mal configurée
     *   - La connexion à la base de données échoue
     *   - Une annotation est incorrecte
     *   - Une configuration Spring Boot est invalide
     */
    @Test
    void contextLoads() {
        // Ce test est intentionnellement vide : son seul rôle est de vérifier
        // que le contexte Spring se charge sans exception.
        // Si le contexte charge correctement, le test réussit automatiquement.
    }

}
