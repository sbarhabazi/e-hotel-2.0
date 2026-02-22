package com.example.EHotel.model.hotelchain;

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
 * Entité JPA représentant une chaîne hôtelière dans le système E-Hotel.
 *
 * Correspond à la table "hotel_chain" en base de données.
 * Une chaîne hôtelière est un groupe propriétaire de plusieurs hôtels.
 * Elle possède un nom unique, un nombre d'hôtels déclarés, et peut avoir
 * plusieurs adresses de bureaux, emails et numéros de téléphone associés.
 *
 * Relations (dans d'autres entités) :
 *   - OneToMany avec Hotel              : une chaîne possède plusieurs hôtels
 *   - OneToMany avec EmailsHotelChain   : une chaîne peut avoir plusieurs emails de contact
 *   - OneToMany avec PhoneNumberHotelChain : une chaîne peut avoir plusieurs téléphones
 *   - OneToMany avec OfficeAddressHotelChain : une chaîne peut avoir plusieurs bureaux
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "hotel_chain") // Lie cette classe à la table "hotel_chain" en base de données
public class HotelChain {

    /**
     * Identifiant unique de la chaîne hôtelière (clé primaire).
     * Généré automatiquement par la stratégie AUTO.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_hotel_chain", nullable = false)
    private Integer id;

    /**
     * Nom commercial unique de la chaîne hôtelière.
     * Exemples : "Marriott", "Hilton", "Hyatt", "IHG"
     * La contrainte unique empêche les doublons.
     */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * Nombre d'hôtels déclarés dans cette chaîne.
     * Ce champ est informatif et peut différer du nombre réel d'hôtels enregistrés.
     */
    @Column(name = "hotels_number")
    private int numberOfHotels;

}
