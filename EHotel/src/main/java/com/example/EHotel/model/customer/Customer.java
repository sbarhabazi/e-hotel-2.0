package com.example.EHotel.model.customer;

// Import pour la gestion des dates (Java 8+)
import java.time.LocalDate;

// Import du DTO client (utilisé pour le constructeur de conversion)
import com.example.EHotel.dtos.AddCustomerDTO;

// Annotations JPA pour le mapping objet-relationnel
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Annotations Lombok pour la génération automatique de code boilerplate
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité JPA représentant un client dans le système E-Hotel.
 *
 * Correspond à la table "customer" en base de données.
 * Un client est identifié de manière unique par son NAS (Numéro d'Assurance Sociale)
 * et possède des informations personnelles et une adresse complète.
 *
 * Le client est créé automatiquement lors d'une première réservation (BookingDTO)
 * si son NAS n'existe pas déjà en base, ou manuellement par un employé.
 *
 * Relations (dans d'autres entités) :
 *   - OneToMany avec Booking : un client peut avoir plusieurs réservations
 *   - OneToMany avec Rental  : un client peut avoir plusieurs locations actives
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "customer") // Lie cette classe à la table "customer" en base de données
public class Customer {

    /**
     * NAS (Numéro d'Assurance Sociale) du client - clé primaire naturelle.
     * Format attendu : "XXX-XXX-XXX" (ex : "123-456-789")
     * Identifiant unique et permanent du client dans le système.
     */
    @Id
    @Column(name = "sin_customer")
    private String sinCustomer;

    /** Prénom du client (obligatoire, 2 à 50 caractères) */
    @Column(name = "firstname", nullable = false)
    private String firstname;

    /** Nom de famille du client (obligatoire, 2 à 50 caractères) */
    @Column(name = "lastname", nullable = false)
    private String lastname;

    /**
     * Date d'enregistrement du client dans le système (date de première inscription).
     * Correspond généralement à la date de première réservation ou visite.
     * Par défaut : date du jour lors de la création.
     */
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    /** Numéro de rue de l'adresse du client */
    @Column(name = "street_number", nullable = false)
    private Integer streetNumber;

    /** Nom de la rue de l'adresse du client */
    @Column(name = "street_name", nullable = false)
    private String streetName;

    /** Ville de résidence du client */
    @Column(name = "city", nullable = false)
    private String city;

    /**
     * Code postal de l'adresse du client.
     * Format canadien attendu : "A1A 1A1"
     */
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    /** Pays de résidence du client */
    @Column(name = "country", nullable = false)
    private String country;

    /**
     * Constructeur de conversion depuis un DTO client.
     * Permet de créer facilement un Customer à partir d'un AddCustomerDTO
     * reçu depuis le formulaire web.
     *
     * @param customer Le DTO contenant les données du formulaire de création
     */
    public Customer(AddCustomerDTO customer) {
        this.sinCustomer = customer.getSinCustomer();
        this.firstname = customer.getFirstname();
        this.lastname = customer.getLastname();
        this.checkInDate = customer.getCheckInDate();
        this.streetNumber = customer.getStreetNumber();
        this.streetName = customer.getStreetName();
        this.city = customer.getCity();
        this.postalCode = customer.getPostalCode();
        this.country = customer.getCountry();
    }
}
