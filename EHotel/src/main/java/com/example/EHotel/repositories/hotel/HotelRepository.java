package com.example.EHotel.repositories.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.EHotel.model.hotel.Hotel;

/**
 * Interface dépôt JPA pour les opérations sur les hôtels (table "hotel").
 *
 * Étend JpaRepository<Hotel, Integer> pour bénéficier des opérations CRUD standard :
 *   - findAll()      : récupère tous les hôtels
 *   - findById(id)   : récupère un hôtel par son ID
 *   - save(hotel)    : sauvegarde ou met à jour un hôtel
 *   - deleteById(id) : supprime un hôtel par son ID
 *
 * Pas de méthode personnalisée requise : les opérations standard suffisent.
 * La logique de génération d'ID se fait dans HotelService (findUnusedId).
 */
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    // Toutes les méthodes nécessaires sont héritées de JpaRepository
}
