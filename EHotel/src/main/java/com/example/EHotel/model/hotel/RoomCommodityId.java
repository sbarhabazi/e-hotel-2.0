package com.example.EHotel.model.hotel;

// Import pour la sérialisation (requis pour les clés primaires composites JPA)
import java.io.Serializable;

// Annotations JPA pour la clé primaire composite embarquée
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

// Annotations Lombok pour la génération automatique de code boilerplate
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;  // Génère equals() et hashCode() pour comparaison de clés
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe représentant la clé primaire composite de la table "room_commodity".
 *
 * Cette classe est annotée avec @Embeddable pour indiquer qu'elle peut être
 * intégrée dans une entité JPA comme clé primaire composite via @EmbeddedId.
 *
 * Implémente Serializable car les clés primaires composites doivent être sérialisables
 * selon les spécifications JPA (nécessaire pour le cache de second niveau et les proxies).
 *
 * La clé est composée de deux colonnes :
 *   - idRoom       : identifiant de la chambre (correspond à la colonne "id_room")
 *   - commodityName : nom de la commodité (correspond à la colonne "commodity_name")
 */
@Embeddable     // Indique que cette classe peut être embarquée dans une entité JPA
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode   // Génère equals() et hashCode() basés sur tous les champs (obligatoire pour les clés composites)
public class RoomCommodityId implements Serializable {

    /**
     * Numéro de version pour la sérialisation.
     * Assure la compatibilité lors de la désérialisation d'objets.
     */
    private static final long serialVersionUID = 1L;

    /** Identifiant de la chambre (première partie de la clé composite) */
    @Column(name = "id_room")
    private Integer idRoom;

    /** Nom de la commodité (deuxième partie de la clé composite) */
    @Column(name = "commodity_name")
    private String commodityName;
}
