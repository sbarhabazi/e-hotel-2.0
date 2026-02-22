package com.example.EHotel.dtos;

// Annotations de validation Jakarta (Bean Validation)
import jakarta.validation.constraints.Min;     // Valeur minimale
import jakarta.validation.constraints.NotNull; // Champ obligatoire

// Annotations Lombok pour la génération automatique de code boilerplate
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) pour la création d'une nouvelle chambre.
 *
 * Ce DTO est utilisé dans le formulaire de création de chambre (/room/add).
 * Il transporte les informations nécessaires pour créer une chambre dans un hôtel :
 * numéro, disponibilité, prix, vue, extensibilité, capacité et hôtel associé.
 *
 * Les validations garantissent la cohérence des données saisies avant
 * leur persistance en base de données.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomDTO {

    /**
     * Numéro de la chambre dans l'hôtel (minimum 1, obligatoire).
     * Exemple : 101, 202, 315
     */
    @NotNull
    @Min(1)
    private Integer roomNumber;

    /**
     * Disponibilité initiale de la chambre (obligatoire).
     * true = chambre disponible à la réservation
     * false = chambre indisponible (maintenance, travaux, etc.)
     */
    @NotNull
    private Boolean availability;

    /**
     * Prix par nuit de la chambre en dollars (minimum 0, obligatoire).
     * Un prix de 0 est autorisé (cas exceptionnel de chambre gratuite).
     */
    @NotNull
    @Min(0)
    private Double price;

    /**
     * Type de vue depuis la chambre (obligatoire).
     * Exemples : "mer", "montagne", "jardin", "ville", "piscine"
     */
    @NotNull
    private String view;

    /**
     * Indique si la chambre peut être agrandie (obligatoire).
     * true = possibilité d'ajouter un lit ou d'ouvrir une cloison
     * false = superficie fixe
     */
    @NotNull
    private Boolean extensible;

    /**
     * Capacité maximale de la chambre (obligatoire).
     * Stockée sous forme de chaîne pour flexibilité.
     * Exemples : "simple", "double", "triple", "quadruple"
     */
    @NotNull
    private String capacity;

    /**
     * Identifiant de l'hôtel auquel appartient cette chambre (obligatoire).
     * Doit correspondre à un Hotel existant en base de données.
     */
    @NotNull
    private Integer idHotel;
}
