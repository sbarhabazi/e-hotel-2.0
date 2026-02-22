package com.example.EHotel.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Import de l'entité Booking (réservation)
import com.example.EHotel.model.hotel.Booking;
// Import du dépôt JPA pour les opérations sur les réservations
import com.example.EHotel.repositories.hotel.BookingRepository;

import jakarta.transaction.Transactional;

/**
 * Service gérant la logique métier liée aux réservations de chambres.
 *
 * Cette classe est annotée @Service et @Transactional.
 * Elle gère le cycle de vie des réservations : création, consultation, suppression.
 * La transformation d'une réservation (Booking) en location (Rental) est gérée
 * dans le BookingController avec l'aide du RentalService.
 */
@Service        // Déclare cette classe comme service Spring (couche métier)
@Transactional  // Toutes les méthodes s'exécutent dans un contexte transactionnel
public class BookingService {

    /**
     * Dépôt JPA pour l'accès à la base de données (table "booking").
     * Injecté automatiquement par Spring via @Autowired.
     */
    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Enregistre une nouvelle réservation en base de données.
     *
     * @param booking La réservation à persister
     */
    @SuppressWarnings("null")
    public void addBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    /**
     * Récupère une réservation spécifique par son identifiant.
     *
     * @param id Identifiant unique de la réservation
     * @return La réservation correspondante, ou null si non trouvée
     */
    public Booking getBooking(int id) {
        return bookingRepository.findById(id).orElse(null);
    }

    /**
     * Récupère toutes les réservations d'un client spécifique, identifié par son NAS.
     *
     * @param sinCustomer Le NAS (Numéro d'Assurance Sociale) du client
     * @return Liste des réservations du client (vide si aucune)
     */
    public List<Booking> getBookings(String sinCustomer) {
        return bookingRepository.findByCustomerSinCustomer(sinCustomer);
    }

    /**
     * Récupère toutes les réservations actives dans le système.
     *
     * @return Liste complète de toutes les réservations (vide si aucune)
     */
    public List<Booking> getBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Supprime une réservation de la base de données par son identifiant.
     * Appelée lors de la transformation d'un Booking en Rental (enregistrement du client).
     *
     * @param id Identifiant de la réservation à supprimer
     */
    public void deleteBooking(int id) {
        bookingRepository.deleteById(id);
    }

    /**
     * Met à jour une réservation existante en base de données.
     *
     * @param booking La réservation avec les nouvelles valeurs à persister
     */
    @SuppressWarnings("null")
    public void updateBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    /**
     * Génère un nouvel identifiant unique pour une réservation.
     * Stratégie : trouve l'ID maximum existant et l'incrémente de 1.
     *
     * @return Le prochain identifiant disponible pour une réservation
     */
    public Integer findUnusedId() {
        return bookingRepository.findAll().stream()
                .map(Booking::getIdBooking)
                .filter(id -> id != null)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }

}
