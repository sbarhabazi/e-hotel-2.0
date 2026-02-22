
package com.example.EHotel.dtos;

// Import pour la gestion des dates (Java 8+)
import java.time.LocalDate;

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
 * DTO (Data Transfer Object) pour l'ajout et la mise à jour d'un client.
 *
 * Ce DTO est utilisé dans les formulaires gérés par l'employé :
 *   - /employee/customer/add    : création manuelle d'un client
 *   - /employee/customer/update/{sin} : modification d'un client existant
 *
 * Il collecte les informations personnelles et l'adresse du client.
 * La validation par @Valid dans le contrôleur assure l'intégrité des données.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCustomerDTO {

    /**
     * NAS (Numéro d'Assurance Sociale) du client - identifiant unique.
     * Format strict : "XXX-XXX-XXX" où X est un chiffre (ex : "123-456-789").
     */
    @NotNull
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{3}$", message = "SIN must be in the format 123-456-789")
    private String sinCustomer;

    /** Prénom du client (2 à 50 caractères, obligatoire) */
    @NotNull
    @Size(min = 2, max = 50)
    private String firstname;

    /** Nom de famille du client (2 à 50 caractères, obligatoire) */
    @NotNull
    @Size(min = 2, max = 50)
    private String lastname;

    /**
     * Date d'enregistrement du client.
     * Initialisée par défaut à la date du jour (moment de la saisie du formulaire).
     */
    private LocalDate checkInDate = LocalDate.now();

    /** Numéro de rue de l'adresse du client (minimum 1, obligatoire) */
    @NotNull
    @Min(1)
    private Integer streetNumber;

    /** Nom de la rue de l'adresse du client (2 à 50 caractères, obligatoire) */
    @NotNull
    @Size(min = 2, max = 50)
    private String streetName;

    /** Ville de résidence du client (2 à 50 caractères, obligatoire) */
    @NotNull
    @Size(min = 2, max = 50)
    private String city;

    /**
     * Code postal de l'adresse du client (format canadien A1A 1A1, obligatoire).
     * Lettres valides en première position : A, B, C, E, G, H, J, K, L, M, N, P, R, S, T, V, X, Y.
     */
    @NotNull
    @Pattern(regexp = "^[ABCEGHJKLMNPRSTVXY]{1}\\d{1}[A-Z]{1} *\\d{1}[A-Z]{1}\\d{1}$",
             message = "Postal code must be in the format A1A 1A1")
    private String postalCode;

    /** Pays de résidence du client (obligatoire) */
    @NotNull
    private String country;
}
