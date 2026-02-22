package com.example.EHotel.repositories.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.EHotel.model.hotel.BookingArchive;

/**
 * Interface dépôt JPA pour les opérations sur les réservations archivées (table "booking_archieve").
 *
 * Étend JpaRepository<BookingArchive, Integer> pour bénéficier des opérations CRUD.
 * Permet de stocker et consulter l'historique des réservations transformées en locations
 * ou annulées.
 */
@Repository
public interface BookingArchiveRepository extends JpaRepository<BookingArchive, Integer> {
    // Toutes les méthodes nécessaires sont héritées de JpaRepository
}
