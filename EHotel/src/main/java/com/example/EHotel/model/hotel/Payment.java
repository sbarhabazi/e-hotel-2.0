package com.example.EHotel.model.hotel;

// Import pour la gestion des dates de paiement (Java 8+)
import java.time.LocalDate;

// Annotations JPA pour le mapping objet-relationnel
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
 * Entité JPA représentant un paiement associé à une location dans le système E-Hotel.
 *
 * Correspond à la table "payment" en base de données.
 * Un paiement est lié à une location (Rental) et enregistre les détails financiers
 * du règlement effectué par le client.
 *
 * Relation :
 *   - ManyToOne avec Rental : plusieurs paiements peuvent être liés à la même location
 *     (ex : acompte + solde, ou paiements partiels)
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "payment") // Lie cette classe à la table "payment" en base de données
public class Payment {

    /**
     * Identifiant unique du paiement (clé primaire).
     * Généré automatiquement via la stratégie IDENTITY (auto-incrément SQL).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment")
    private Integer idPayment;

    /**
     * Location associée à ce paiement.
     * Relation ManyToOne : une location peut avoir plusieurs paiements.
     * La colonne "id_rental" est la clé étrangère.
     */
    @ManyToOne
    @JoinColumn(name = "id_rental", nullable = false)
    private Rental rental;

    /** Date à laquelle le paiement a été effectué */
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    /** Montant du paiement en dollars (doit être >= 0) */
    @Column(name = "amount", nullable = false)
    private Double amount;

    /**
     * Méthode de paiement utilisée.
     * Exemples : "Carte de crédit", "Espèces", "Virement bancaire", "Chèque"
     */
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    /**
     * Statut du paiement.
     * Exemples : "Payé", "En attente", "Remboursé", "Échoué"
     */
    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;
}
