package com.example.EHotel.repositories.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.EHotel.model.hotel.Payment;

/**
 * Interface dépôt JPA pour les opérations sur les paiements (table "payment").
 *
 * Étend JpaRepository<Payment, Integer> pour bénéficier des opérations CRUD standard.
 * Utilisé pour enregistrer et récupérer les paiements associés aux locations.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    // Toutes les méthodes nécessaires sont héritées de JpaRepository
}
