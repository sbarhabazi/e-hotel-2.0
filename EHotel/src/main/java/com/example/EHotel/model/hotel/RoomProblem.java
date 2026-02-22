package com.example.EHotel.model.hotel;

// Annotations JPA pour le mapping avec clé composite embarquée
import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

// Annotations Lombok pour la génération automatique de code boilerplate
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité JPA représentant l'association entre une chambre et un problème signalé.
 *
 * Correspond à la table "room_problem" en base de données.
 * Cette classe implémente une relation plusieurs-à-plusieurs (Many-to-Many)
 * entre Room (chambre) et Problem (problème/défaut) via une table de jointure.
 *
 * Utilise une clé primaire composite (RoomProblemId) composée de :
 *   - idRoom    : identifiant de la chambre concernée
 *   - idProblem : identifiant du type de problème
 *
 * Exemple : la chambre 101 a un problème "Climatisation en panne" et "Plomberie défectueuse"
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "room_problem") // Lie cette classe à la table de jointure en base de données
public class RoomProblem {

    /**
     * Clé primaire composite embarquée.
     * Contient les deux colonnes de jointure : id_room et id_problem.
     */
    @EmbeddedId
    private RoomProblemId id = new RoomProblemId();

    /**
     * Chambre affectée par le problème.
     * @MapsId("idRoom") lie le champ "idRoom" de la clé composite à cette relation.
     */
    @ManyToOne
    @MapsId("idRoom")
    @JoinColumn(name = "id_room")
    private Room room;

    /**
     * Type de problème affectant la chambre.
     * @MapsId("idProblem") lie le champ "idProblem" de la clé composite à cette relation.
     */
    @ManyToOne
    @MapsId("idProblem")
    @JoinColumn(name = "id_problem")
    private Problem problem;

}
