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
 * Classe représentant la clé primaire composite de la table "room_problem".
 *
 * Cette classe est annotée avec @Embeddable pour être intégrée dans l'entité
 * RoomProblem comme clé primaire composite via @EmbeddedId.
 *
 * Implémente Serializable car les clés primaires composites doivent être sérialisables
 * selon les spécifications JPA.
 *
 * La clé est composée de deux colonnes :
 *   - idRoom    : identifiant de la chambre (correspond à la colonne "id_room")
 *   - idProblem : identifiant du problème (correspond à la colonne "id_problem")
 */
@Embeddable     // Indique que cette classe peut être embarquée dans une entité JPA
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode   // Génère equals() et hashCode() (obligatoire pour les clés composites JPA)
public class RoomProblemId implements Serializable {

    /**
     * Numéro de version pour la sérialisation.
     * Assure la compatibilité lors de la désérialisation d'objets.
     */
    private static final long serialVersionUID = 1L;

    /** Identifiant de la chambre affectée (première partie de la clé composite) */
    @Column(name = "id_room")
    private Integer idRoom;

    /** Identifiant du problème signalé (deuxième partie de la clé composite) */
    @Column(name = "id_problem")
    private Integer idProblem;
}
