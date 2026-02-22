package com.example.EHotel.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Import de l'entité Customer (client)
import com.example.EHotel.model.customer.Customer;
// Import du dépôt JPA pour les opérations sur les clients
import com.example.EHotel.repositories.customer.CustomerRepository;

import jakarta.transaction.Transactional;

/**
 * Service gérant la logique métier liée aux clients de l'hôtel.
 *
 * Cette classe est annotée @Service et @Transactional.
 * Elle expose toutes les opérations CRUD (Create, Read, Update, Delete) sur les clients.
 *
 * Les clients sont identifiés par leur NAS (Numéro d'Assurance Sociale).
 * Un client peut être créé automatiquement lors d'une réservation (dans RoomController)
 * ou manuellement par un employé (dans EmployeeController).
 */
@Service        // Déclare cette classe comme service Spring (couche métier)
@Transactional  // Toutes les méthodes s'exécutent dans un contexte transactionnel
public class CustomerService {

    /**
     * Dépôt JPA pour l'accès à la base de données (table "customer").
     * Injecté automatiquement par Spring via @Autowired.
     */
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Enregistre un nouveau client en base de données.
     *
     * @param customer Le client à persister
     */
    @SuppressWarnings("null")
    public void addCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    /**
     * Récupère un client spécifique par son NAS (Numéro d'Assurance Sociale).
     *
     * @param sinCustomer Le NAS du client (format "XXX-XXX-XXX")
     * @return Le client correspondant, ou null si non trouvé
     */
    public Customer getCustomer(String sinCustomer) {
        return customerRepository.findBySinCustomer(sinCustomer).orElse(null);
    }

    /**
     * Supprime un client de la base de données par son NAS.
     * Attention : cette opération peut échouer si le client a des réservations actives
     * (contraintes de clé étrangère en base de données).
     *
     * @param sinCustomer Le NAS du client à supprimer
     */
    public void deleteCustomer(String sinCustomer) {
        customerRepository.deleteBySinCustomer(sinCustomer);
    }

    /**
     * Met à jour les informations d'un client existant en base de données.
     *
     * @param customer Le client avec les nouvelles valeurs à persister
     */
    @SuppressWarnings("null")
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    /**
     * Récupère la liste de tous les clients enregistrés dans le système.
     *
     * @return Liste complète de tous les clients (vide si aucun)
     */
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }
}
