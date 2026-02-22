package com.example.EHotel.model.employee;

// Import de l'entité hôtel (hôtel d'affectation de l'employé)
import com.example.EHotel.model.hotel.Hotel;

// Annotations JPA pour le mapping objet-relationnel
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Entité JPA représentant un employé dans le système E-Hotel.
 *
 * Correspond à la table "employee" en base de données.
 * Un employé est identifié par son NAS (Numéro d'Assurance Sociale), possède
 * un rôle dans l'hôtel et est affecté à un hôtel précis.
 *
 * Un employé peut également être le gérant (manager) d'un hôtel,
 * dans ce cas sa référence apparaît dans le champ Hotel.manager.
 *
 * Relations :
 *   - ManyToOne avec Hotel : plusieurs employés peuvent travailler dans le même hôtel
 *   - OneToOne (inversée) avec Hotel.manager : un employé peut être gérant d'un hôtel
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "employee") // Lie cette classe à la table "employee" en base de données
public class Employee {

    /**
     * NAS (Numéro d'Assurance Sociale) de l'employé - clé primaire naturelle.
     * Format attendu : "XXX-XXX-XXX" (ex : "987-654-321")
     * Identifiant unique et permanent de l'employé dans le système.
     */
    @Id
    @Column(name = "sin_employee")
    private String sinEmployee;

    /** Prénom de l'employé (obligatoire, 2 à 50 caractères) */
    @Column(name = "firstname", nullable = false)
    private String firstname;

    /** Nom de famille de l'employé (obligatoire, 2 à 50 caractères) */
    @Column(name = "lastname", nullable = false)
    private String lastname;

    /**
     * Rôle ou poste de l'employé dans l'hôtel.
     * Exemples : "Réceptionniste", "Gérant", "Femme de chambre", "Concierge", "Chef cuisinier"
     * Ce champ est facultatif (nullable).
     */
    @Column(name = "role")
    private String role;

    /** Numéro de rue de l'adresse de résidence de l'employé */
    @Column(name = "street_number", nullable = false)
    private Integer streetNumber;

    /** Nom de la rue de l'adresse de résidence de l'employé */
    @Column(name = "street_name", nullable = false)
    private String streetName;

    /** Ville de résidence de l'employé */
    @Column(name = "city", nullable = false)
    private String city;

    /**
     * Code postal de l'adresse de résidence de l'employé.
     * Format canadien attendu : "A1A 1A1"
     */
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    /** Pays de résidence de l'employé */
    @Column(name = "country", nullable = false)
    private String country;

    /**
     * Hôtel où travaille cet employé.
     * Relation ManyToOne : plusieurs employés peuvent travailler dans le même hôtel.
     * La colonne "id_hotel" est la clé étrangère. Peut être null si non affecté.
     */
    @ManyToOne
    @JoinColumn(name = "id_hotel")
    private Hotel hotel;

}
