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
 * Entité JPA représentant une chambre d'hôtel dans le système E-Hotel.
 *
 * Correspond à la table "room" en base de données.
 * Chaque chambre est associée à un hôtel précis et possède des caractéristiques
 * propres (capacité, prix, vue, extensibilité, disponibilité).
 *
 * Relation :
 *   - ManyToOne avec Hotel : plusieurs chambres appartiennent au même hôtel
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "room") // Lie cette classe à la table "room" en base de données
public class Room {

    /** Identifiant unique de la chambre (clé primaire, géré manuellement) */
    @Id
    @Column(name = "id_room")
    private Integer idRoom;

    /** Numéro de la chambre dans l'hôtel (ex : 101, 202, etc.) */
    @Column(name = "room_number", nullable = false)
    private Integer roomNumber;

    /**
     * Disponibilité de la chambre.
     * true = chambre disponible à la réservation
     * false = chambre indisponible (en maintenance ou déjà réservée)
     */
    @Column(name = "availability", nullable = false)
    private Boolean availability;

    /** Prix par nuit de la chambre (en dollars) */
    @Column(name = "price", nullable = false)
    private Double price;

    /** Type de vue depuis la chambre (ex : "mer", "montagne", "jardin", "ville") */
    @Column(name = "view", nullable = false)
    private String view;

    /**
     * Indique si la chambre peut être agrandie (lit supplémentaire, cloison amovible).
     * true = chambre extensible, false = non extensible
     */
    @Column(name = "extensible", nullable = false)
    private Boolean extensible;

    /**
     * Capacité maximale de la chambre en nombre de personnes.
     * Stockée sous forme de chaîne (ex : "simple", "double", "triple", "quadruple").
     */
    @Column(name = "capacity", nullable = false)
    private String capacity;

    /**
     * Hôtel auquel appartient cette chambre.
     * Relation ManyToOne : plusieurs chambres peuvent appartenir au même hôtel.
     * La colonne "id_hotel" est la clé étrangère en base.
     */
    @ManyToOne
    @JoinColumn(name = "id_hotel", nullable = false)
    private Hotel hotel;
}
