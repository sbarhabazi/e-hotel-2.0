package com.example.EHotel.model.hotel;

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
 * Entité JPA représentant un équipement ou service d'hôtel (commodité) dans le système E-Hotel.
 *
 * Correspond à la table "commodity" en base de données.
 * Une commodité est un équipement ou service disponible dans une chambre.
 * Exemples : "WiFi", "Climatisation", "Télévision", "Mini-bar", "Coffre-fort", "Baignoire"
 *
 * La clé primaire est le nom de la commodité (identifiant naturel unique).
 * Les commodités sont associées aux chambres via l'entité RoomCommodity
 * (relation plusieurs-à-plusieurs entre Room et Commodity).
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "commodity") // Lie cette classe à la table "commodity" en base de données
public class Commodity {

    /**
     * Nom de la commodité (clé primaire naturelle).
     * Exemple : "WiFi", "Climatisation", "Télévision"
     * Doit être unique car utilisé directement comme identifiant.
     */
    @Id
    @Column(name = "name")
    private String name;
}
