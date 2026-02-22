package com.example.EHotel.repositories.customer;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Import de l'entité Customer
import com.example.EHotel.model.customer.Customer;

/**
 * Interface dépôt JPA pour les opérations sur les clients (table "customer").
 *
 * Étend JpaRepository<Customer, String> où String est le type de la clé primaire (NAS).
 * Fournit automatiquement les opérations CRUD de base.
 *
 * Ajoute des méthodes personnalisées générées par convention de nommage Spring Data JPA :
 *   - findBySinCustomer() : recherche par NAS (équivalent au findById mais plus explicite)
 *   - deleteBySinCustomer() : suppression par NAS (nécessaire car la clé est de type String)
 *
 * @Repository : déclare cette interface comme composant Spring de la couche d'accès aux données
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    /**
     * Recherche un client par son NAS (Numéro d'Assurance Sociale).
     *
     * Méthode générée automatiquement par Spring Data JPA à partir du nom :
     * "findBy" + "SinCustomer" (nom du champ dans l'entité Customer)
     *
     * @param sinCustomer Le NAS du client à rechercher (format "XXX-XXX-XXX")
     * @return Un Optional contenant le client si trouvé, vide sinon
     */
    Optional<Customer> findBySinCustomer(String sinCustomer);

    /**
     * Supprime un client par son NAS (Numéro d'Assurance Sociale).
     *
     * Méthode générée automatiquement par Spring Data JPA.
     * Attention : cette opération peut lever une exception si le client a des
     * réservations ou locations actives (violation de contrainte de clé étrangère).
     *
     * @param sinCustomer Le NAS du client à supprimer
     */
    void deleteBySinCustomer(String sinCustomer);
}
