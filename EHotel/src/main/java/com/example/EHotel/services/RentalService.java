package com.example.EHotel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Import de l'entité Rental (location active)
import com.example.EHotel.model.hotel.Rental;
// Import du dépôt JPA pour les opérations sur les locations
import com.example.EHotel.repositories.hotel.RentalRepository;
import java.util.List;

import jakarta.transaction.Transactional;

/**
 * Service gérant la logique métier liée aux locations actives.
 *
 * Cette classe est annotée @Service et @Transactional.
 * Elle gère le cycle de vie des locations : création (depuis une réservation transformée),
 * consultation, suppression et mise à jour.
 *
 * Un Rental est créé lorsqu'un Booking (réservation) est transformé en location effective
 * lors de l'arrivée du client à l'hôtel. Un paiement (Payment) peut ensuite lui être associé.
 */
@Service        // Déclare cette classe comme service Spring (couche métier)
@Transactional  // Toutes les méthodes s'exécutent dans un contexte transactionnel
public class RentalService {

    /**
     * Dépôt JPA pour l'accès à la base de données (table "rental").
     * Injecté automatiquement par Spring via @Autowired.
     */
    @Autowired
    private RentalRepository rentalRepository;

    /**
     * Enregistre une nouvelle location en base de données.
     * Appelé lors de la transformation d'un Booking en Rental.
     *
     * @param rental La location à persister
     */
    @SuppressWarnings("null")
    public void addRental(Rental rental) {
        rentalRepository.save(rental);
    }

    /**
     * Récupère une location spécifique par son identifiant.
     *
     * @param id Identifiant unique de la location
     * @return La location correspondante, ou null si non trouvée
     */
    public Rental getRental(int id) {
        return rentalRepository.findById(id).orElse(null);
    }

    /**
     * Supprime une location de la base de données par son identifiant.
     *
     * @param id Identifiant de la location à supprimer
     */
    public void deleteRental(int id) {
        rentalRepository.deleteById(id);
    }

    /**
     * Met à jour une location existante en base de données.
     *
     * @param rental La location avec les nouvelles valeurs à persister
     */
    @SuppressWarnings("null")
    public void updateRental(Rental rental) {
        rentalRepository.save(rental);
    }

    /**
     * Génère un nouvel identifiant unique pour une location.
     * Stratégie : trouve l'ID maximum existant et l'incrémente de 1.
     *
     * @return Le prochain identifiant disponible pour une location
     */
    public Integer findUnusedId() {
        return rentalRepository.findAll().stream()
                .map(Rental::getIdRental)
                .filter(id -> id != null)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }

    /**
     * Récupère toutes les locations actives dans le système.
     *
     * @return Liste complète de toutes les locations actives (vide si aucune)
     */
    public List<Rental> getRentals() {
        return rentalRepository.findAll();
    }
}
