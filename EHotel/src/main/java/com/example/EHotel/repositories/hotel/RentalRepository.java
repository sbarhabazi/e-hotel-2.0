package com.example.EHotel.repositories.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.EHotel.model.hotel.Rental;

/**
 * Interface dépôt JPA pour les opérations sur les locations actives (table "rental").
 *
 * Étend JpaRepository<Rental, Integer> pour bénéficier des opérations CRUD standard.
 * Toutes les opérations nécessaires (findAll, findById, save, deleteById)
 * sont héritées de JpaRepository.
 */
@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
    // Toutes les méthodes nécessaires sont héritées de JpaRepository
}
