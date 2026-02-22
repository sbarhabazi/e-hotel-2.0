package com.example.EHotel.dtos;

// Import pour la gestion des dates (Java 8+)
import java.time.LocalDate;

// Annotations de validation Jakarta (Bean Validation)
import jakarta.validation.constraints.Min;         // Valeur minimale
import jakarta.validation.constraints.NotNull;     // Champ obligatoire (non null)
import jakarta.validation.constraints.Pattern;     // Validation par expression régulière
import jakarta.validation.constraints.Size;        // Longueur min/max d'une chaîne
import jakarta.validation.constraints.AssertTrue;  // Validation personnalisée (méthode booléenne)

// Annotations Lombok pour la génération automatique de code boilerplate
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) pour la création d'une réservation de chambre.
 *
 * Ce DTO est utilisé dans le formulaire de réservation (/room/booking).
 * Il transporte les données saisies par le client :
 *   - Les informations personnelles du client (NAS, nom, prénom, adresse)
 *   - Les dates de réservation souhaitées (début et fin)
 *   - L'identifiant de la chambre choisie
 *
 * Si le client n'existe pas encore en base (identifié par son NAS),
 * il est automatiquement créé lors du traitement de la réservation.
 *
 * Toutes les contraintes de validation sont appliquées via @Valid dans le contrôleur.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    /**
     * NAS (Numéro d'Assurance Sociale) du client.
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
     * Date d'enregistrement du client dans le système.
     * Par défaut, initialisée à la date du jour (date de la réservation).
     */
    private LocalDate checkInDate = LocalDate.now();

    /** Numéro de rue de l'adresse du client (minimum 1) */
    @NotNull
    @Min(1)
    private Integer streetNumber;

    /** Nom de la rue de l'adresse du client (2 à 50 caractères) */
    @NotNull
    @Size(min = 2, max = 50)
    private String streetName;

    /** Ville de résidence du client (2 à 50 caractères) */
    @NotNull
    @Size(min = 2, max = 50)
    private String city;

    /**
     * Code postal de l'adresse du client.
     * Format canadien requis : "A1A 1A1" (lettre-chiffre-lettre espace chiffre-lettre-chiffre).
     * Lettres valides pour la première position : A, B, C, E, G, H, J, K, L, M, N, P, R, S, T, V, X, Y.
     */
    @NotNull
    @Pattern(regexp = "^[ABCEGHJKLMNPRSTVXY]{1}\\d{1}[A-Z]{1} *\\d{1}[A-Z]{1}\\d{1}$",
             message = "Postal code must be in the format A1A 1A1")
    private String postalCode;

    /** Pays de résidence du client (obligatoire) */
    @NotNull
    private String country;

    /** Identifiant de la chambre choisie par le client (doit exister en base) */
    @NotNull
    private Integer idRoom;

    /** Date de début souhaitée de la réservation (premier jour d'occupation) */
    @NotNull
    private LocalDate startDate;

    /** Date de fin souhaitée de la réservation (dernier jour d'occupation) */
    @NotNull
    private LocalDate endDate;

    /**
     * Validation croisée : vérifie que la date de fin est postérieure ou égale à la date de début.
     * Cette méthode est appelée automatiquement lors de la validation via @Valid.
     *
     * @return true si la plage de dates est valide, false sinon
     */
    @AssertTrue(message = "La date de fin doit être après ou égale à la date de début.")
    public boolean isDateRangeValid() {
        // Si l'une des dates est nulle, on laisse les autres validations gérer l'erreur
        if (startDate == null || endDate == null) {
            return true;
        }
        // La date de fin ne doit pas être strictement avant la date de début
        return !endDate.isBefore(startDate);
    }
}
