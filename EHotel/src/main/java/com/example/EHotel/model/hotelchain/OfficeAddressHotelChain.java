package com.example.EHotel.model.hotelchain;

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
 * Entité JPA représentant une adresse de bureau d'une chaîne hôtelière.
 *
 * Correspond à la table "office_address_hotel_chain" en base de données.
 * Une chaîne hôtelière peut avoir plusieurs bureaux (siège social, bureaux régionaux,
 * centres administratifs, etc.) répartis dans différentes villes ou pays.
 * Chaque adresse de bureau est identifiée par un code postal unique.
 *
 * Relation :
 *   - ManyToOne avec HotelChain : plusieurs bureaux peuvent appartenir à la même chaîne
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "office_address_hotel_chain") // Lie cette classe à la table correspondante
public class OfficeAddressHotelChain {

    /**
     * Identifiant unique de l'adresse de bureau (clé primaire générée automatiquement).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** Numéro de rue du bureau */
    @Column(name = "street_number", nullable = false)
    private Integer streetNumber;

    /** Nom de la rue du bureau */
    @Column(name = "street_name", nullable = false)
    private String streetName;

    /** Ville où est situé le bureau */
    @Column(name = "city", nullable = false)
    private String city;

    /**
     * Code postal du bureau (unique en base de données).
     * Garantit que deux bureaux ne partagent pas le même code postal.
     */
    @Column(name = "postal_code", unique = true, nullable = false)
    private String postalCode;

    /** Pays où est situé le bureau */
    @Column(name = "country", nullable = false)
    private String country;

    /**
     * Chaîne hôtelière propriétaire de ce bureau.
     * Relation ManyToOne : une chaîne peut avoir plusieurs bureaux.
     * La colonne "id_hotel_chain" est la clé étrangère.
     */
    @ManyToOne
    @JoinColumn(name = "id_hotel_chain", nullable = false)
    private HotelChain hotelChain;
}
