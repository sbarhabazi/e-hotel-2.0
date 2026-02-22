package com.example.EHotel.model.hotel;

// Annotations JPA pour le mapping objet-relationnel
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Annotations Lombok pour la génération automatique de code boilerplate
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité JPA représentant un type de problème ou défaut pouvant affecter une chambre.
 *
 * Correspond à la table "problem" en base de données.
 * Un problème est un défaut référentiel (catalogue des types d'incidents)
 * qui peut être associé à des chambres via la relation RoomProblem.
 *
 * Exemples de problèmes : "Plomberie défectueuse", "Climatisation en panne",
 *                          "Télévision cassée", "Fenêtre brisée"
 *
 * Relation :
 *   - Cette entité est utilisée dans RoomProblem pour indiquer quels problèmes
 *     affectent quelles chambres (relation plusieurs-à-plusieurs).
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "problem") // Lie cette classe à la table "problem" en base de données
public class Problem {

    /**
     * Identifiant unique du problème (clé primaire).
     * Généré automatiquement par la stratégie AUTO (séquence ou auto-incrément selon la BDD).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * Nom court du problème (unique, obligatoire).
     * Identifiant lisible du type d'incident.
     * Exemple : "Plomberie défectueuse"
     */
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    /**
     * Description détaillée du problème (obligatoire).
     * Explique la nature exacte de l'incident et ses impacts potentiels.
     */
    @Column(name = "description", nullable = false)
    private String description;
}
