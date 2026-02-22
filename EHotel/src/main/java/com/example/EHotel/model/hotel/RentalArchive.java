package com.example.EHotel.model.hotel;

// Import pour la gestion des dates (Java 8+)
import java.time.LocalDate;

// Annotations JPA pour le mapping objet-relationnel
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Annotations Lombok pour la génération automatique de code boilerplate
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité JPA représentant une location archivée dans le système E-Hotel.
 *
 * Correspond à la table "rental_archieve" en base de données.
 * Similaire à BookingArchive, cette classe stocke les données dénormalisées
 * d'une location terminée pour conservation historique à long terme.
 *
 * Note : La table est nommée "rental_archieve" (faute d'orthographe dans le schéma SQL original).
 *
 * Une location est archivée après le départ du client.
 * L'archive conserve uniquement les identifiants bruts (sinCustomer, idRoom)
 * sans relations JPA, pour éviter les problèmes lors de suppressions futures de données.
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "rental_archieve") // Lie cette classe à la table d'archives des locations
public class RentalArchive {

    /**
     * Identifiant de la location archivée (conservé de la location originale).
     * Généré automatiquement par une séquence SQL.
     */
    @Id
    @Column(name = "id_rental")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idRental;

    /**
     * NAS (Numéro d'Assurance Sociale) du client ayant effectué la location.
     * Stocké directement comme String (pas de relation JPA) pour pérennité de l'archive.
     */
    @Column(name = "sin_customer", nullable = false)
    private String sinCustomer;

    /**
     * Identifiant de la chambre qui était louée.
     * Stocké directement comme Integer (pas de relation JPA) pour pérennité de l'archive.
     */
    @Column(name = "id_room", nullable = false)
    private Integer idRoom;

    /** Date de début effective de la location archivée */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** Date de fin effective de la location archivée */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
