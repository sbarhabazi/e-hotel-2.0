package com.example.EHotel.repositories.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Import de l'entité Employee
import com.example.EHotel.model.employee.Employee;

/**
 * Interface dépôt JPA pour les opérations sur les employés (table "employee").
 *
 * Étend JpaRepository<Employee, String> où String est le type de la clé primaire (NAS).
 * Fournit automatiquement les opérations CRUD de base.
 *
 * Ajoute des méthodes personnalisées générées par convention de nommage Spring Data JPA :
 *   - findBySinEmployee()   : recherche par NAS de l'employé
 *   - deleteBySinEmployee() : suppression par NAS
 *
 * @Repository : déclare cette interface comme composant Spring de la couche d'accès aux données
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    /**
     * Recherche un employé par son NAS (Numéro d'Assurance Sociale).
     *
     * Méthode générée automatiquement par Spring Data JPA :
     * "findBy" + "SinEmployee" (nom du champ dans l'entité Employee)
     *
     * @param sinEmployee Le NAS de l'employé à rechercher (format "XXX-XXX-XXX")
     * @return Un Optional contenant l'employé si trouvé, vide sinon
     */
    Optional<Employee> findBySinEmployee(String sinEmployee);

    /**
     * Supprime un employé par son NAS (Numéro d'Assurance Sociale).
     *
     * Méthode générée automatiquement par Spring Data JPA.
     * Attention : si l'employé est gérant d'un hôtel (Hotel.sin_manager), la suppression
     * peut échouer en raison de la contrainte de clé étrangère.
     *
     * @param sinEmployee Le NAS de l'employé à supprimer
     */
    void deleteBySinEmployee(String sinEmployee);
}
