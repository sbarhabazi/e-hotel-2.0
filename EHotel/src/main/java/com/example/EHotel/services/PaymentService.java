package com.example.EHotel.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

// Import de l'entité Payment (paiement)
import com.example.EHotel.model.hotel.Payment;
// Import du dépôt JPA pour les opérations sur les paiements
import com.example.EHotel.repositories.hotel.PaymentRepository;

/**
 * Service gérant la logique métier liée aux paiements.
 *
 * Cette classe est annotée @Service et @Transactional.
 * Elle permet l'enregistrement et la consultation des paiements associés aux locations.
 *
 * Un paiement est toujours lié à une location (Rental) existante et représente
 * le règlement financier effectué par le client pour son séjour.
 */
@Service        // Déclare cette classe comme service Spring (couche métier)
@Transactional  // Toutes les méthodes s'exécutent dans un contexte transactionnel
public class PaymentService {

    /**
     * Dépôt JPA pour l'accès à la base de données (table "payment").
     * Injecté automatiquement par Spring via @Autowired.
     */
    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Enregistre un nouveau paiement en base de données.
     *
     * @param payment Le paiement à persister (doit être associé à un Rental existant)
     */
    @SuppressWarnings("null")
    public void addPayment(Payment payment) {
        paymentRepository.save(payment);
    }

    /**
     * Récupère la liste de tous les paiements enregistrés dans le système.
     * Utilisée pour afficher l'historique complet des paiements.
     *
     * @return Liste complète de tous les paiements (vide si aucun)
     */
    public List<Payment> getPayments() {
        return paymentRepository.findAll();
    }
}
