package com.example.EHotel.repositories.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.EHotel.model.hotel.Problem;

/**
 * Interface dépôt JPA pour les opérations sur les types de problèmes (table "problem").
 *
 * Étend JpaRepository<Problem, Integer> pour bénéficier des opérations CRUD.
 * Gère le catalogue des types d'incidents pouvant affecter les chambres.
 */
@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {
    // Toutes les méthodes nécessaires sont héritées de JpaRepository
}
