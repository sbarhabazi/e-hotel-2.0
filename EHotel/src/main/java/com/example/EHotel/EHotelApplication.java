package com.example.EHotel;

// Import du framework Spring Boot pour le démarrage de l'application
import org.springframework.boot.SpringApplication;
// Annotation composite qui active la configuration automatique de Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application E-Hotel.
 *
 * L'annotation @SpringBootApplication regroupe trois annotations fondamentales :
 *   - @Configuration          : désigne cette classe comme source de configuration Spring
 *   - @EnableAutoConfiguration : active la configuration automatique basée sur le classpath
 *   - @ComponentScan          : détecte automatiquement les composants Spring dans le package
 *
 * Cette application est un système complet de gestion hôtelière multi-établissements
 * permettant la recherche de chambres, les réservations, la gestion des locations et paiements.
 */
@SpringBootApplication
public class EHotelApplication {

    /**
     * Point d'entrée de l'application Java.
     * Initialise et démarre le serveur Spring Boot avec le serveur Tomcat intégré.
     *
     * @param args Arguments passés en ligne de commande (non utilisés ici)
     */
    public static void main(String[] args) {
        // Démarre l'application Spring Boot en initialisant le contexte applicatif complet
        SpringApplication.run(EHotelApplication.class, args);
    }

}
