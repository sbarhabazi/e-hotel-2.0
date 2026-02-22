package com.example.EHotel.repositories.hotel;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// Import du DTO de critères de recherche (utilisé dans la requête JPQL)
import com.example.EHotel.dtos.RoomSearchCriteriaDTO;
// Import de l'entité Room
import com.example.EHotel.model.hotel.Room;
import java.util.List;

/**
 * Interface dépôt JPA pour les opérations sur les chambres (table "room").
 *
 * Étend JpaRepository<Room, Integer> ce qui fournit automatiquement :
 *   - findAll()       : récupère toutes les chambres
 *   - findById(id)    : récupère une chambre par son ID
 *   - save(room)      : sauvegarde ou met à jour une chambre
 *   - deleteById(id)  : supprime une chambre par son ID
 *   - count()         : compte le nombre de chambres
 *   - etc.
 *
 * Ajoute des méthodes personnalisées :
 *   - findAvailableRooms() : recherche avancée avec JPQL et critères multiples
 *   - findByHotelId()      : récupère les chambres d'un hôtel spécifique
 *
 * @Repository : déclare cette interface comme composant Spring de la couche d'accès aux données
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    /**
     * Recherche les chambres disponibles selon des critères multiples.
     *
     * Requête JPQL personnalisée qui filtre les chambres selon :
     *   - availability = true          : chambre disponible à la réservation
     *   - capacity = roomCapacity      : capacité correspondant au critère
     *   - price <= maxPrice            : prix inférieur ou égal au budget maximum
     *   - hotel.hotelChain.id = hotelChainId : appartient à la chaîne hôtelière sélectionnée
     *   - hotel.startNumber >= startNumber   : classement étoiles minimum de l'hôtel
     *   - hotel.roomsNumber >= roomsNumber   : taille minimale de l'hôtel
     *   - Non réservée sur la période        : aucun chevauchement avec les réservations existantes
     *
     * La sous-requête élimine les chambres déjà réservées pour la période demandée
     * en vérifiant qu'aucun Booking ne chevauche les dates startDate et endDate.
     *
     * @param criteria L'objet DTO contenant tous les critères de filtrage
     * @return Liste des chambres disponibles correspondant à tous les critères
     */
    @Query("""
            SELECT r
            FROM Room r
            JOIN r.hotel h
            WHERE r.availability = true
              AND r.capacity = :#{#criteria.roomCapacity}
              AND r.price <= :#{#criteria.maxPrice}
              AND h.hotelChain.id = :#{#criteria.hotelChainId}
              AND h.startNumber >= :#{#criteria.startNumber}
              AND h.roomsNumber >= :#{#criteria.roomsNumber}
              AND NOT EXISTS (
                    SELECT 1
                    FROM Booking b
                    WHERE b.room.idRoom = r.idRoom
                      AND b.startDate <= :#{#criteria.endDate}
                      AND b.endDate >= :#{#criteria.startDate}
                  )
            """)
    List<Room> findAvailableRooms(@Param("criteria") RoomSearchCriteriaDTO criteria);

    /**
     * Récupère une chambre par son identifiant.
     * Redéfinit la méthode héritée pour utiliser un int (primitif) au lieu d'Integer.
     *
     * @param id L'identifiant de la chambre
     * @return Un Optional contenant la chambre si trouvée, vide sinon
     */
    Optional<Room> findById(int id);

    /**
     * Récupère toutes les chambres appartenant à un hôtel spécifique.
     *
     * Requête JPQL simple : filtre les chambres par l'ID de leur hôtel parent.
     *
     * @param hotelId L'identifiant de l'hôtel dont on veut les chambres
     * @return Liste des chambres de cet hôtel (vide si l'hôtel n'a pas de chambres)
     */
    @Query("SELECT r FROM Room r WHERE r.hotel.idHotel = :hotelId")
    List<Room> findByHotelId(@Param("hotelId") int hotelId);
}
