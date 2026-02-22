package com.example.EHotel.dtos;

// Annotations de validation Jakarta (Bean Validation)
import jakarta.validation.constraints.Min;     // Valeur minimale
import jakarta.validation.constraints.NotNull; // Champ obligatoire
import jakarta.validation.constraints.Pattern; // Validation par expression régulière
import jakarta.validation.constraints.Size;    // Longueur min/max d'une chaîne

// Annotations Lombok pour la génération automatique de code boilerplate
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) pour l'ajout et la mise à jour d'un employé.
 *
 * Ce DTO est utilisé dans les formulaires du gestionnaire (/manager) :
 *   - /manager/employee/add    : création d'un nouvel employé
 *   - /manager/employee/update/{sin} : modification d'un employé existant
 *
 * Il collecte les informations personnelles, professionnelles et l'adresse de l'employé.
 * Le NAS (Numéro d'Assurance Sociale) est l'identifiant unique de l'employé.
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AddEmployeeDTO {

    /**
     * NAS (Numéro d'Assurance Sociale) de l'employé - identifiant unique.
     * Format strict : "XXX-XXX-XXX" où X est un chiffre (ex : "987-654-321").
     */
    @NotNull
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{3}$", message = "SIN must be in the format 123-456-789")
    private String sinEmployee;

    /** Prénom de l'employé (2 à 50 caractères, obligatoire) */
    @NotNull
    @Size(min = 2, max = 50)
    private String firstname;

    /** Nom de famille de l'employé (2 à 50 caractères, obligatoire) */
    @NotNull
    @Size(min = 2, max = 50)
    private String lastname;

    /**
     * Rôle ou poste occupé par l'employé dans l'hôtel (obligatoire).
     * Exemples : "Réceptionniste", "Gérant", "Femme de chambre", "Concierge"
     */
    @NotNull
    private String role;

    /** Numéro de rue de l'adresse de résidence de l'employé (minimum 1, obligatoire) */
    @NotNull
    @Min(1)
    private Integer streetNumber;

    /** Nom de la rue de l'adresse de résidence (2 à 50 caractères, obligatoire) */
    @NotNull
    @Size(min = 2, max = 50)
    private String streetName;

    /** Ville de résidence de l'employé (2 à 50 caractères, obligatoire) */
    @NotNull
    @Size(min = 2, max = 50)
    private String city;

    /**
     * Code postal de l'adresse de résidence (format canadien A1A 1A1, obligatoire).
     * Lettres valides en première position : A, B, C, E, G, H, J, K, L, M, N, P, R, S, T, V, X, Y.
     */
    @NotNull
    @Pattern(regexp = "^[ABCEGHJKLMNPRSTVXY]{1}\\d{1}[A-Z]{1} *\\d{1}[A-Z]{1}\\d{1}$",
             message = "Postal code must be in the format A1A 1A1")
    private String postalCode;

    /** Pays de résidence de l'employé (obligatoire) */
    @NotNull
    private String country;

    /**
     * Identifiant de l'hôtel où travaille cet employé (obligatoire).
     * Doit correspondre à un Hotel existant en base de données.
     */
    @NotNull
    private Integer idHotel;
}
