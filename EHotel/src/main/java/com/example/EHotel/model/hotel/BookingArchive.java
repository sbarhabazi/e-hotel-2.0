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
 * Entité JPA représentant une réservation archivée dans le système E-Hotel.
 *
 * Correspond à la table "booking_archieve" en base de données.
 * Contrairement à Booking (réservation active), BookingArchive stocke
 * les données dénormalisées (identifiants bruts au lieu de relations JPA)
 * pour une conservation à long terme sans dépendances.
 *
 * Note : La table est nommée "booking_archieve" (faute d'orthographe dans le schéma SQL original).
 *
 * Une réservation est archivée lorsqu'elle est annulée ou transformée en location.
 * L'archive conserve uniquement les identifiants (sinCustomer, idRoom) sans relations JPA
 * pour éviter les erreurs en cas de suppression des données sources.
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "booking_archieve") // Lie cette classe à la table d'archives des réservations
public class BookingArchive {

    /**
     * Identifiant de la réservation archivée (conservé de la réservation originale).
     * Généré automatiquement par une séquence SQL.
     */
    @Id
    @Column(name = "id_booking")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idBooking;

    /**
     * NAS (Numéro d'Assurance Sociale) du client ayant effectué la réservation.
     * Stocké directement comme String (pas de relation JPA) pour pérennité de l'archive.
     */
    @Column(name = "sin_customer", nullable = false)
    private String sinCustomer;

    /**
     * Identifiant de la chambre qui était réservée.
     * Stocké directement comme Integer (pas de relation JPA) pour pérennité de l'archive.
     */
    @Column(name = "id_room", nullable = false)
    private Integer idRoom;

    /** Date de début prévue de la réservation archivée */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** Date de fin prévue de la réservation archivée */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

}
