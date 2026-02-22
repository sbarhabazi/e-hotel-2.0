package com.example.EHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Imports des modèles nécessaires
import com.example.EHotel.model.hotel.Booking;
import com.example.EHotel.model.hotel.Rental;
// Imports des services nécessaires
import com.example.EHotel.services.BookingService;
import com.example.EHotel.services.RentalService;

/**
 * Contrôleur Spring MVC gérant l'affichage des réservations, leur transformation en locations,
 * ainsi que l'affichage des locations actives.
 *
 * Préfixe de route : /booking
 *
 * Routes disponibles :
 *   GET  /booking/bookings         → Afficher toutes les réservations actives
 *   POST /booking/transform/{id}   → Transformer une réservation en location (check-in)
 *   GET  /booking/rentals          → Afficher toutes les locations actives
 *
 * Ce contrôleur implémente le processus clé du check-in :
 * lorsqu'un client arrive à l'hôtel, sa réservation (Booking) est "transformée"
 * en location active (Rental) et la réservation originale est supprimée.
 */
@Controller
@RequestMapping("/booking")
public class BookingController {

    /** Service de gestion des réservations */
    @Autowired
    private BookingService bookingService;

    /** Service de gestion des locations actives */
    @Autowired
    private RentalService rentalService;

    /**
     * Affiche la liste de toutes les réservations actives dans le système.
     *
     * GET /booking/bookings → vue "bookings.html"
     *
     * @param model Le modèle Spring MVC
     * @return Le template "bookings"
     */
    @GetMapping("/bookings")
    public String showBookings(Model model) {
        // Récupère toutes les réservations actives et les passe au template
        model.addAttribute("bookings", bookingService.getBookings());
        return "bookings";
    }

    /**
     * Transforme une réservation (Booking) en location active (Rental).
     *
     * POST /booking/transform/{id}
     * Implémente le processus de check-in :
     *   1. Récupère la réservation par son ID
     *   2. Crée une nouvelle location (Rental) avec les mêmes données
     *   3. Supprime la réservation originale (devenue inutile)
     *   4. Redirige vers la liste des réservations
     *
     * @param id L'identifiant de la réservation à transformer (extrait de l'URL)
     * @return Redirection vers la liste des réservations
     */
    @PostMapping("/transform/{id}")
    public String transformBooking(@PathVariable("id") int id) {
        // Récupère la réservation existante
        Booking booking = bookingService.getBooking(id);
        if (booking == null) {
            // Réservation introuvable : redirige sans modification
            return "redirect:/booking/bookings";
        }

        // Crée un nouvel objet Rental à partir des données de la réservation
        Rental rental = new Rental();
        rental.setCustomer(booking.getCustomer());   // Même client
        rental.setRoom(booking.getRoom());           // Même chambre
        rental.setStartDate(booking.getStartDate()); // Même date de début
        rental.setEndDate(booking.getEndDate());     // Même date de fin

        // Persiste la nouvelle location en base de données
        rentalService.addRental(rental);

        // Supprime la réservation originale (elle est maintenant convertie en location)
        bookingService.deleteBooking(id);

        return "redirect:/booking/bookings";
    }

    /**
     * Affiche la liste de toutes les locations actives dans le système.
     *
     * GET /booking/rentals → vue "rentals.html"
     * Depuis cette page, le personnel peut enregistrer un paiement pour une location.
     *
     * @param model Le modèle Spring MVC
     * @return Le template "rentals"
     */
    @GetMapping("/rentals")
    public String showRentals(Model model) {
        // Récupère toutes les locations actives et les passe au template
        model.addAttribute("rentals", rentalService.getRentals());
        return "rentals";
    }


}
