package com.example.EHotel.repositories.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.EHotel.model.hotel.RentalArchive;

/**
 * Interface dépôt JPA pour les opérations sur les locations archivées (table "rental_archieve").
 *
 * Étend JpaRepository<RentalArchive, Integer> pour bénéficier des opérations CRUD.
 * Permet de stocker et consulter l'historique des locations terminées.
 */
@Repository
public interface RentalArchiveRepository extends JpaRepository<RentalArchive, Integer> {
    // Toutes les méthodes nécessaires sont héritées de JpaRepository
}
