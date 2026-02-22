package com.example.EHotel.model.hotelchain;

// Annotations JPA pour le mapping objet-relationnel
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
 * Entité JPA représentant une adresse email associée à une chaîne hôtelière.
 *
 * Correspond à la table "emails_hotel_chain" en base de données.
 * Une chaîne hôtelière peut avoir plusieurs emails de contact (direction générale,
 * service client, réservations, etc.). L'email lui-même est la clé primaire,
 * garantissant son unicité dans le système.
 *
 * Relation :
 *   - ManyToOne avec HotelChain : plusieurs emails peuvent appartenir à la même chaîne
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "emails_hotel_chain") // Lie cette classe à la table correspondante en base de données
public class EmailsHotelChain {

    /**
     * Adresse email (clé primaire naturelle, unique dans le système).
     * Format email standard (ex : "contact@marriott.com").
     */
    @Id
    private String email;

    /**
     * Chaîne hôtelière associée à cette adresse email.
     * Relation ManyToOne : une chaîne peut avoir plusieurs adresses email.
     * La colonne "id_hotel_chain" est la clé étrangère.
     */
    @ManyToOne
    @JoinColumn(name = "id_hotel_chain", nullable = false)
    private HotelChain hotelChain;
}
