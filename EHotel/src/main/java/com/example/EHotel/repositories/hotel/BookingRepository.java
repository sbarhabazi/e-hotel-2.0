package com.example.EHotel.repositories.hotel;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Import de l'entité Booking
import com.example.EHotel.model.hotel.Booking;

/**
 * Interface dépôt JPA pour les opérations sur les réservations (table "booking").
 *
 * Étend JpaRepository<Booking, Integer> pour bénéficier des opérations CRUD de base.
 *
 * Ajoute des méthodes personnalisées :
 *   - findById(int) : redéfinit pour utiliser un int primitif
 *   - findByCustomerSinCustomer() : convention de nommage Spring Data JPA
 *     pour rechercher les réservations d'un client par son NAS
 *
 * @Repository : déclare cette interface comme composant Spring de la couche d'accès aux données
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    /**
     * Récupère une réservation par son identifiant.
     * Redéfinit la méthode héritée pour utiliser un int (primitif) au lieu d'Integer.
     *
     * @param id L'identifiant de la réservation
     * @return Un Optional contenant la réservation si trouvée, vide sinon
     */
    Optional<Booking> findById(int id);

    /**
     * Récupère toutes les réservations d'un client identifié par son NAS.
     *
     * Spring Data JPA génère automatiquement la requête à partir du nom de la méthode :
     *   "findBy" + "Customer" (relation ManyToOne) + "SinCustomer" (champ du Client)
     * → SQL : SELECT * FROM booking b JOIN customer c ON b.sin_customer = c.sin_customer
     *          WHERE c.sin_customer = ?
     *
     * @param sinCustomer Le NAS (Numéro d'Assurance Sociale) du client
     * @return Liste des réservations actives du client (vide si aucune)
     */
    List<Booking> findByCustomerSinCustomer(String sinCustomer);
}
