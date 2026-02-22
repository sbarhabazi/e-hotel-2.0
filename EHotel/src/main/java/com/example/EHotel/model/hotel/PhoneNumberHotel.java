package com.example.EHotel.model.hotel;

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
 * Entité JPA représentant un numéro de téléphone associé à un hôtel.
 *
 * Correspond à la table "phone_number_hotel" en base de données.
 * Un hôtel peut avoir plusieurs numéros de téléphone (standard, réservations,
 * restauration, spa, etc.). La clé primaire est le numéro de téléphone lui-même,
 * garantissant son unicité dans le système.
 *
 * Relation :
 *   - ManyToOne avec Hotel : plusieurs numéros de téléphone peuvent appartenir au même hôtel
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "phone_number_hotel") // Lie cette classe à la table correspondante en base de données
public class PhoneNumberHotel {

    /**
     * Le numéro de téléphone (clé primaire naturelle, unique dans le système).
     * Format libre (peut inclure indicatif pays, tirets, etc.).
     * Exemple : "+1-514-555-0100"
     */
    @Id
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Hôtel auquel appartient ce numéro de téléphone.
     * Relation ManyToOne : un hôtel peut avoir plusieurs numéros de téléphone.
     * La colonne "id_hotel" est la clé étrangère.
     */
    @ManyToOne
    @JoinColumn(name = "id_hotel", nullable = false)
    private Hotel hotel;
}
