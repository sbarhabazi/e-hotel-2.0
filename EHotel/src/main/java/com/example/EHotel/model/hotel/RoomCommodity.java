package com.example.EHotel.model.hotel;

// Annotations JPA pour le mapping objet-relationnel avec clé composite embarquée
import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

// Annotations Lombok pour la génération automatique de code boilerplate
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité JPA représentant la relation entre une chambre et ses équipements (commodités).
 *
 * Correspond à la table "room_commodity" en base de données.
 * Cette classe implémente une relation plusieurs-à-plusieurs (Many-to-Many) entre
 * Room (chambre) et Commodity (équipement) à l'aide d'une table de jointure.
 *
 * Utilise une clé primaire composite (RoomCommodityId) composée de :
 *   - idRoom       : identifiant de la chambre
 *   - commodityName : nom de la commodité
 *
 * Exemple : la chambre 101 possède "WiFi", "Climatisation" et "Télévision"
 */
@Entity
@Getter           // Génère automatiquement tous les getters
@Setter           // Génère automatiquement tous les setters
@NoArgsConstructor    // Constructeur par défaut requis par JPA
@AllArgsConstructor   // Constructeur avec tous les champs
@Table(name = "room_commodity") // Lie cette classe à la table de jointure en base de données
public class RoomCommodity {

    /**
     * Clé primaire composite embarquée.
     * Contient les deux colonnes de jointure : id_room et commodity_name.
     * @EmbeddedId signale que la clé primaire est définie dans une classe séparée.
     */
    @EmbeddedId
    private RoomCommodityId id = new RoomCommodityId();

    /**
     * Chambre concernée par cette association.
     * @MapsId("idRoom") lie le champ "idRoom" de la clé composite à cette relation.
     */
    @ManyToOne
    @MapsId("idRoom")
    @JoinColumn(name = "id_room")
    private Room room;

    /**
     * Commodité (équipement) associée à la chambre.
     * @MapsId("commodityName") lie le champ "commodityName" de la clé composite à cette relation.
     */
    @ManyToOne
    @MapsId("commodityName")
    @JoinColumn(name = "commodity_name")
    private Commodity commodity;
}
