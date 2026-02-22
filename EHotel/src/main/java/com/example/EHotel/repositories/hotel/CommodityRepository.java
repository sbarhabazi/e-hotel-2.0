package com.example.EHotel.repositories.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.EHotel.model.hotel.Commodity;

/**
 * Interface dépôt JPA pour les opérations sur les équipements/commodités (table "commodity").
 *
 * Étend JpaRepository<Commodity, String> où String est la clé primaire (nom de la commodité).
 * Permet de gérer le catalogue des équipements disponibles dans les chambres.
 */
@Repository
public interface CommodityRepository extends JpaRepository<Commodity, String> {
    // Toutes les méthodes nécessaires sont héritées de JpaRepository
}
