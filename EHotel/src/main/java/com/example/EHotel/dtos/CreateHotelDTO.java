package com.example.EHotel.dtos;

// Annotations de validation Jakarta (Bean Validation)
import jakarta.validation.constraints.Email;    // Validation du format email
import jakarta.validation.constraints.Max;      // Valeur maximale
import jakarta.validation.constraints.Min;      // Valeur minimale
import jakarta.validation.constraints.NotNull;  // Champ obligatoire
import jakarta.validation.constraints.Pattern;  // Validation par expression régulière
import jakarta.validation.constraints.Size;     // Longueur min/max

// Annotations Lombok pour la génération automatique de code boilerplate
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) pour la création et la mise à jour d'un hôtel.
 *
 * Ce DTO est utilisé dans les formulaires :
 *   - /hotel/hotel/add    : création d'un nouvel hôtel
 *   - /hotel/hotel/update/{id} : modification d'un hôtel existant
 *
 * Il collecte toutes les informations nécessaires pour créer ou mettre à jour
 * un hôtel : informations générales, adresse, classification par étoiles,
 * chaîne hôtelière d'appartenance et gérant assigné.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateHotelDTO {

    /**
     * Identifiant de l'hôtel (null lors d'une création, renseigné lors d'une mise à jour).
     * Non soumis à validation car géré automatiquement par le système.
     */
    private Integer idHotel;

    /** Nom de l'hôtel (2 à 50 caractères, obligatoire) */
    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    /** Nombre total de chambres de l'hôtel (minimum 1, obligatoire) */
    @NotNull
    @Min(1)
    private Integer roomsNumber;

    /**
     * Classement en étoiles de l'hôtel (entre 1 et 5, obligatoire).
     * 1 étoile = économique, 5 étoiles = luxe
     */
    @NotNull
    @Min(1)
    @Max(5)
    private Integer startNumber;

    /** Adresse email de contact de l'hôtel (format email valide, obligatoire) */
    @NotNull
    @Email
    private String email;

    /** Numéro de rue de l'hôtel (minimum 1, obligatoire) */
    @NotNull
    @Min(1)
    private Integer streetNumber;

    /** Nom de la rue où est situé l'hôtel (2 à 50 caractères, obligatoire) */
    @NotNull
    @Size(min = 2, max = 50)
    private String streetName;

    /** Ville où est situé l'hôtel (2 à 50 caractères, obligatoire) */
    @NotNull
    @Size(min = 2, max = 50)
    private String city;

    /**
     * Code postal de l'hôtel (format canadien A1A 1A1, obligatoire).
     * Lettres valides en première position : A, B, C, E, G, H, J, K, L, M, N, P, R, S, T, V, X, Y.
     */
    @NotNull
    @Pattern(regexp = "^[ABCEGHJKLMNPRSTVXY]{1}\\d{1}[A-Z]{1} *\\d{1}[A-Z]{1}\\d{1}$",
             message = "Postal code must be in the format A1A 1A1")
    private String postalCode;

    /** Pays où est situé l'hôtel (obligatoire) */
    @NotNull
    private String country;

    /**
     * Identifiant de la chaîne hôtelière à laquelle appartient cet hôtel.
     * Doit correspondre à un HotelChain existant en base de données.
     */
    @NotNull
    private Integer idHotelChain;

    /**
     * NAS (Numéro d'Assurance Sociale) du gérant de l'hôtel.
     * Doit correspondre à un Employee existant en base de données.
     */
    @NotNull
    private String sinManager;
}
