package com.example.EHotel.dtos;

// Import pour la gestion des dates (Java 8+)
import java.time.LocalDate;

// Annotations de validation Jakarta (Bean Validation)
import jakarta.validation.constraints.DecimalMin;      // Valeur décimale minimale stricte
import jakarta.validation.constraints.FutureOrPresent; // Date présente ou future
import jakarta.validation.constraints.NotNull;         // Champ obligatoire

// Annotations Lombok pour la génération automatique de code boilerplate
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) pour l'enregistrement d'un paiement.
 *
 * Ce DTO est utilisé dans le formulaire de paiement (/payment/add/{id}).
 * Il collecte les informations nécessaires pour enregistrer le règlement
 * d'une location (Rental) par un client.
 *
 * Un paiement est toujours lié à une location existante (identifiée par idRental).
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddPaymentDTO {

    /**
     * Identifiant de la location (Rental) pour laquelle ce paiement est effectué.
     * Doit correspondre à un Rental existant en base de données.
     */
    @NotNull
    private Integer idRental;

    /**
     * Date du paiement (doit être aujourd'hui ou dans le futur, obligatoire).
     * @FutureOrPresent empêche d'enregistrer un paiement avec une date passée.
     */
    @NotNull
    @FutureOrPresent
    private LocalDate paymentDate;

    /**
     * Méthode de paiement utilisée par le client (obligatoire).
     * Exemples : "Carte de crédit", "Espèces", "Virement bancaire", "Chèque"
     */
    @NotNull
    private String paymentMethod;

    /**
     * Montant du paiement en dollars (strictement supérieur à 0, obligatoire).
     */
    @NotNull
    @DecimalMin(value = "0.01", message = "Le montant doit être strictement supérieur à 0.")
    private Double amount;

    /**
     * Statut du paiement au moment de l'enregistrement (obligatoire).
     * Exemples : "Payé", "En attente de confirmation", "Remboursé"
     */
    @NotNull
    private String paymentStatus;
}
