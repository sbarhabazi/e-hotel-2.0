package com.example.EHotel.dtos;

// Annotations Lombok pour la génération automatique de code boilerplate
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Import pour la gestion des dates (Java 8+)
import java.time.LocalDate;

// Annotations de validation Jakarta (Bean Validation)
import jakarta.validation.constraints.AssertTrue; // Validation personnalisée
import jakarta.validation.constraints.Max;        // Valeur maximale
import jakarta.validation.constraints.Min;        // Valeur minimale
import jakarta.validation.constraints.NotNull;    // Champ obligatoire

/**
 * DTO (Data Transfer Object) représentant les critères de recherche de chambres disponibles.
 *
 * Ce DTO est utilisé dans le formulaire de recherche (/room/search).
 * Il permet au client de filtrer les chambres selon plusieurs critères :
 *   - Capacité souhaitée (simple, double, triple, quadruple)
 *   - Budget maximum par nuit
 *   - Chaîne hôtelière préférée
 *   - Classement minimum en étoiles de l'hôtel
 *   - Nombre minimum de chambres dans l'hôtel
 *   - Période de séjour souhaitée (dates de début et fin)
 *
 * La requête JPQL dans RoomRepository utilise ces critères pour filtrer
 * les chambres disponibles et non déjà réservées sur la période indiquée.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomSearchCriteriaDTO {

    /**
     * Capacité souhaitée de la chambre (obligatoire).
     * Valeurs attendues : "simple", "double", "triple", "quadruple"
     */
    @NotNull
    private String roomCapacity;

    /**
     * Prix maximum par nuit accepté par le client (minimum 0, obligatoire).
     * Seules les chambres dont le prix est inférieur ou égal à cette valeur seront retournées.
     */
    @NotNull
    @Min(0)
    private Double maxPrice;

    /**
     * Identifiant de la chaîne hôtelière souhaitée (minimum 1, obligatoire).
     * Permet de filtrer les résultats selon une chaîne spécifique (ex : Marriott, Hilton).
     */
    @NotNull
    @Min(1)
    private Integer hotelChainId;

    /**
     * Classement minimum en étoiles de l'hôtel souhaité (entre 1 et 5, obligatoire).
     * Seuls les hôtels avec un classement >= startNumber seront considérés.
     */
    @NotNull
    @Min(1)
    @Max(5)
    private Integer startNumber;

    /**
     * Nombre minimum de chambres dans l'hôtel (minimum 1, obligatoire).
     * Filtre les hôtels selon leur taille (capacité totale d'hébergement).
     */
    @NotNull
    @Min(1)
    private Integer roomsNumber;

    /** Date de début du séjour souhaité (premier jour d'occupation, obligatoire) */
    @NotNull
    private LocalDate startDate;

    /** Date de fin du séjour souhaité (dernier jour d'occupation, obligatoire) */
    @NotNull
    private LocalDate endDate;

    /**
     * Validation croisée : vérifie que la date de fin est postérieure ou égale à la date de début.
     * Appelée automatiquement lors de la validation @Valid dans le contrôleur.
     *
     * @return true si la plage de dates est valide, false sinon
     */
    @AssertTrue(message = "La date de fin doit être après ou égale à la date de début.")
    public boolean isDateRangeValid() {
        // Si l'une des dates est nulle, on laisse les autres contraintes @NotNull gérer l'erreur
        if (startDate == null || endDate == null) {
            return true;
        }
        // La date de fin ne doit pas être strictement avant la date de début
        return !endDate.isBefore(startDate);
    }
}
