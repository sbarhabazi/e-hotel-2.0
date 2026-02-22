package com.example.EHotel.model.hotelchain;

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
 * Entité JPA représentant un numéro de téléphone associé à une chaîne hôtelière.
 *
 * Correspond à la table "phone_number_hotel_chain" en base de données.
 * Une chaîne hôtelière peut avoir plusieurs numéros de téléphone centralisés
 * (standard général, service clientèle, réservations centrales, etc.).
 * Le numéro de téléphone est la clé primaire, garantissant son unicité.
 *
 * Relation :
 *   - ManyToOne avec HotelChain : plusieurs numéros peuvent appartenir à la même chaîne
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "phone_number_hotel_chain") // Lie cette classe à la table correspondante
public class PhoneNumberHotelChain {

    /**
     * Le numéro de téléphone (clé primaire naturelle, unique dans le système).
     * Format libre (ex : "+1-800-555-0199").
     */
    @Id
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Chaîne hôtelière associée à ce numéro de téléphone.
     * Relation ManyToOne : une chaîne peut avoir plusieurs numéros de téléphone.
     * La colonne "id_hotel_chain" est la clé étrangère.
     */
    @ManyToOne
    @JoinColumn(name = "id_hotel_chain", nullable = false)
    private HotelChain hotelChain;

}
