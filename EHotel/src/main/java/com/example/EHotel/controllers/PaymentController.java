package com.example.EHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Import du DTO pour la saisie d'un paiement
import com.example.EHotel.dtos.AddPaymentDTO;
// Imports des modèles nécessaires
import com.example.EHotel.model.hotel.Payment;
import com.example.EHotel.model.hotel.Rental;
// Imports des services nécessaires
import com.example.EHotel.services.PaymentService;
import com.example.EHotel.services.RentalService;

import jakarta.validation.Valid;

/**
 * Contrôleur Spring MVC gérant l'enregistrement et l'affichage des paiements.
 *
 * Préfixe de route : /payment
 *
 * Un paiement est toujours associé à une location active (Rental).
 * Le personnel de l'hôtel accède à ce contrôleur depuis la liste des locations (/booking/rentals).
 *
 * Routes disponibles :
 *   GET  /payment/payments  → Afficher l'historique de tous les paiements
 *   GET  /payment/add/{id}  → Formulaire d'enregistrement d'un paiement pour une location
 *   POST /payment/add/{id}  → Traiter l'enregistrement du paiement
 */
@Controller
@RequestMapping("/payment")
public class PaymentController {

    /** Service de gestion des paiements */
    @Autowired
    private PaymentService paymentService;

    /** Service de gestion des locations (pour vérifier que la location existe) */
    @Autowired
    private RentalService rentalService;

    /**
     * Affiche l'historique complet de tous les paiements enregistrés.
     *
     * GET /payment/payments → vue "payments.html"
     *
     * @param model Le modèle Spring MVC
     * @return Le template "payments"
     */
    @GetMapping("/payments")
    public String showPayments(Model model) {
        // Récupère tous les paiements et les passe au template
        model.addAttribute("payments", paymentService.getPayments());
        return "payments";
    }

    /**
     * Affiche le formulaire d'enregistrement d'un paiement pour une location spécifique.
     *
     * GET /payment/add/{id} → vue "add-payment.html"
     * Vérifie que la location existe avant d'afficher le formulaire.
     *
     * @param idRental L'identifiant de la location concernée (extrait de l'URL)
     * @param model    Le modèle Spring MVC
     * @return Le template "add-payment", ou redirection si location introuvable
     */
    @GetMapping("/add/{id}")
    public String showAddPaymentForm(@PathVariable("id") int idRental, Model model) {
        // Vérifie que la location existe en base de données
        Rental rental = rentalService.getRental(idRental);
        if (rental == null) {
            // Location introuvable : redirige vers la liste des locations
            return "redirect:/booking/rentals";
        }

        // Crée un DTO de paiement pré-rempli avec l'ID de la location
        AddPaymentDTO paymentDTO = new AddPaymentDTO();
        Integer id = idRental;
        paymentDTO.setIdRental(idRental);

        model.addAttribute("payment", paymentDTO);
        model.addAttribute("id", id);  // Passe l'ID séparément pour l'afficher dans le template
        return "add-payment";
    }

    /**
     * Traite la soumission du formulaire d'enregistrement d'un paiement.
     *
     * POST /payment/add/{id}
     * Valide les données, crée l'entité Payment et la persiste en base.
     *
     * @param idRental   L'identifiant de la location (extrait de l'URL)
     * @param paymentDTO Les données du formulaire de paiement (validées par @Valid)
     * @param result     Résultat de la validation Bean Validation
     * @param model      Le modèle Spring MVC
     * @return Redirection vers la liste des paiements si succès, ou le formulaire si erreur
     */
    @PostMapping("/add/{id}")
    public String addPayment(
            @PathVariable("id") int idRental,
            @Valid @ModelAttribute("payment") AddPaymentDTO paymentDTO,
            BindingResult result,
            Model model
    ) {
        // Si le formulaire contient des erreurs, réaffiche le formulaire avec les erreurs
        if (result.hasErrors()) {
            model.addAttribute("id", idRental);
            return "add-payment";
        }

        // Vérifie que la location existe toujours en base de données
        Rental rental = rentalService.getRental(idRental);
        if (rental == null) {
            return "redirect:/booking/rentals";
        }

        // Crée un nouvel objet Payment à partir des données du DTO
        Payment newPayment = new Payment();
        newPayment.setRental(rental);                          // Lie le paiement à la location
        newPayment.setAmount(paymentDTO.getAmount());          // Montant du paiement
        newPayment.setPaymentDate(paymentDTO.getPaymentDate()); // Date du paiement
        newPayment.setPaymentMethod(paymentDTO.getPaymentMethod()); // Méthode de paiement
        newPayment.setPaymentStatus(paymentDTO.getPaymentStatus()); // Statut du paiement

        // Persiste le paiement en base de données
        paymentService.addPayment(newPayment);

        // Redirige vers l'historique des paiements après enregistrement réussi
        return "redirect:/payment/payments";
    }
}
