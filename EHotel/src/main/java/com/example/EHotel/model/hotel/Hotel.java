package com.example.EHotel.model.hotel;

// Import de l'entité employé (gérant de l'hôtel)
import com.example.EHotel.model.employee.Employee;
// Import de l'entité chaîne hôtelière (propriétaire de l'hôtel)
import com.example.EHotel.model.hotelchain.HotelChain;

// Annotations JPA pour le mapping objet-relationnel
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// Annotations Lombok pour la génération automatique de code boilerplate
import lombok.AllArgsConstructor;  // Génère un constructeur avec tous les champs
import lombok.Getter;              // Génère les méthodes getXxx()
import lombok.NoArgsConstructor;   // Génère un constructeur sans argument
import lombok.Setter;              // Génère les méthodes setXxx()

/**
 * Entité JPA représentant un hôtel dans le système E-Hotel.
 *
 * Correspond à la table "hotel" en base de données.
 * Un hôtel appartient à une chaîne hôtelière et est géré par un employé (gérant).
 *
 * Relations :
 *   - ManyToOne avec HotelChain : plusieurs hôtels peuvent appartenir à une même chaîne
 *   - OneToOne  avec Employee   : un seul employé est le gérant d'un hôtel donné
 */
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs pour les tests et instanciations
@Entity               // Marque cette classe comme entité persistante JPA
@Table(name = "hotel") // Lie cette classe à la table "hotel" en base de données
public class Hotel {

    /** Identifiant unique de l'hôtel (clé primaire, géré manuellement) */
    @Id
    @Column(name = "id_hotel")
    private Integer idHotel;

    /** Nom de l'hôtel (obligatoire) */
    @Column(name = "name", nullable = false)
    private String name;

    /** Nombre total de chambres dans l'hôtel (obligatoire, doit être >= 1) */
    @Column(name = "rooms_number", nullable = false)
    private Integer roomsNumber;

    /** Classement en étoiles de l'hôtel (1 à 5, obligatoire) */
    @Column(name = "start_number", nullable = false)
    private Integer startNumber;

    /** Adresse email de contact de l'hôtel (obligatoire, format email) */
    @Column(name = "email", nullable = false)
    private String email;

    /** Numéro de rue de l'adresse physique de l'hôtel */
    @Column(name = "street_number", nullable = false)
    private Integer streetNumber;

    /** Nom de la rue de l'adresse physique de l'hôtel */
    @Column(name = "street_name", nullable = false)
    private String streetName;

    /** Ville où est situé l'hôtel */
    @Column(name = "city", nullable = false)
    private String city;

    /** Code postal de l'hôtel (unique en base, format canadien A1A 1A1) */
    @Column(name = "postal_code", unique = true, nullable = false)
    private String postalCode;

    /** Pays où est situé l'hôtel */
    @Column(name = "country", nullable = false)
    private String country;

    /**
     * Chaîne hôtelière à laquelle appartient cet hôtel.
     * Relation ManyToOne : plusieurs hôtels peuvent appartenir à la même chaîne.
     * La colonne "id_hotel_chain" est la clé étrangère en base.
     */
    @ManyToOne
    @JoinColumn(name = "id_hotel_chain", nullable = false)
    private HotelChain hotelChain;

    /**
     * Gérant de l'hôtel (employé responsable).
     * Relation OneToOne : un seul gérant par hôtel.
     * La colonne "sin_manager" stocke le NAS (Numéro d'Assurance Sociale) du gérant.
     */
    @OneToOne
    @JoinColumn(name = "sin_manager")
    private Employee manager;
}
