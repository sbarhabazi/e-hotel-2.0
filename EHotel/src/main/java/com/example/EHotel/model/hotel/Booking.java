package com.example.EHotel.model.hotel;

// Import pour la gestion des dates (Java 8+)
import java.time.LocalDate;

// Import de l'entité client associée à la réservation
import com.example.EHotel.model.customer.Customer;

// Annotations JPA pour le mapping objet-relationnel
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// Annotations Lombok pour la génération automatique de code boilerplate
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité JPA représentant une réservation de chambre dans le système E-Hotel.
 *
 * Correspond à la table "booking" en base de données.
 * Une réservation est créée par un client pour une chambre donnée,
 * sur une période définie (date de début et date de fin).
 *
 * Cycle de vie d'une réservation :
 *   1. Un client recherche et réserve une chambre → création d'un Booking
 *   2. À l'arrivée du client, le Booking est transformé en Rental (location active)
 *   3. Le Booking est ensuite supprimé (archivé dans BookingArchive)
 *
 * Relations :
 *   - ManyToOne avec Customer : plusieurs réservations peuvent appartenir au même client
 *   - ManyToOne avec Room     : plusieurs réservations peuvent concerner la même chambre
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "booking") // Lie cette classe à la table "booking" en base de données
public class Booking {

    /**
     * Identifiant unique de la réservation (clé primaire).
     * Généré automatiquement par une séquence SQL.
     */
    @Id
    @Column(name = "id_booking")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idBooking;

    /**
     * Client ayant effectué cette réservation.
     * Relation ManyToOne : un client peut avoir plusieurs réservations.
     * La colonne "sin_customer" est la clé étrangère (NAS du client).
     */
    @ManyToOne
    @JoinColumn(name = "sin_customer", nullable = false)
    private Customer customer;

    /**
     * Chambre réservée.
     * Relation ManyToOne : une chambre peut être réservée plusieurs fois (à des périodes différentes).
     * La colonne "id_room" est la clé étrangère.
     */
    @ManyToOne
    @JoinColumn(name = "id_room", nullable = false)
    private Room room;

    /** Date de début de la réservation (premier jour d'occupation) */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** Date de fin de la réservation (dernier jour d'occupation) */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /**
     * Constructeur personnalisé sans l'identifiant (utilisé lors de la création d'une nouvelle réservation).
     * L'ID sera généré automatiquement par la base de données.
     *
     * @param customer  Le client effectuant la réservation
     * @param room      La chambre réservée
     * @param startDate Date de début de la réservation
     * @param endDate   Date de fin de la réservation
     */
    public Booking(Customer customer, Room room, LocalDate startDate, LocalDate endDate) {
        this.customer = customer;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
