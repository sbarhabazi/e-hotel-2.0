package com.example.EHotel.model.hotel;

// Import pour la gestion des dates (Java 8+)
import java.time.LocalDate;

// Import de l'entité client associée à la location
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
 * Entité JPA représentant une location active dans le système E-Hotel.
 *
 * Correspond à la table "rental" en base de données.
 * Un Rental est créé lorsqu'un Booking (réservation) est confirmé à l'arrivée
 * du client : la réservation se transforme en location effective.
 *
 * Cycle de vie d'une location :
 *   1. Création à partir d'un Booking lors de l'enregistrement du client
 *   2. Suivi pendant toute la durée du séjour
 *   3. Un paiement (Payment) peut être associé à cette location
 *   4. Après le départ, la location est archivée dans RentalArchive
 *
 * Relations :
 *   - ManyToOne avec Customer : un client peut avoir plusieurs locations actives
 *   - ManyToOne avec Room     : une chambre peut être louée plusieurs fois (à des périodes différentes)
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "rental") // Lie cette classe à la table "rental" en base de données
public class Rental {

    /**
     * Identifiant unique de la location (clé primaire).
     * Généré automatiquement par une séquence SQL.
     */
    @Id
    @Column(name = "id_rental")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idRental;

    /**
     * Client occupant la chambre pendant cette location.
     * Relation ManyToOne : un client peut avoir plusieurs locations.
     * La colonne "sin_customer" est la clé étrangère (NAS du client).
     */
    @ManyToOne
    @JoinColumn(name = "sin_customer", nullable = false)
    private Customer customer;

    /**
     * Chambre louée.
     * Relation ManyToOne : une chambre peut être louée plusieurs fois.
     * La colonne "id_room" est la clé étrangère.
     */
    @ManyToOne
    @JoinColumn(name = "id_room", nullable = false)
    private Room room;

    /** Date de début effective de la location (date d'arrivée du client) */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** Date de fin prévue de la location (date de départ du client) */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
