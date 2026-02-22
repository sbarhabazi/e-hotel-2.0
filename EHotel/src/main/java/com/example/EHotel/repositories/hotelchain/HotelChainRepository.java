package com.example.EHotel.repositories.hotelchain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.EHotel.model.hotelchain.HotelChain;

/**
 * Interface dépôt JPA pour les opérations sur les chaînes hôtelières (table "hotel_chain").
 *
 * Étend JpaRepository<HotelChain, Integer> pour bénéficier des opérations CRUD standard.
 * Les chaînes hôtelières sont des données de référence utilisées dans les formulaires
 * de création d'hôtel et de recherche de chambres.
 */
@Repository
public interface HotelChainRepository extends JpaRepository<HotelChain, Integer> {
    // Toutes les méthodes nécessaires sont héritées de JpaRepository
}
