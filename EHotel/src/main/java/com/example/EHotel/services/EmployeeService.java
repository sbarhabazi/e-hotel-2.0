package com.example.EHotel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Import de l'entité Employee (employé)
import com.example.EHotel.model.employee.Employee;
// Import du dépôt JPA pour les opérations sur les employés
import com.example.EHotel.repositories.employee.EmployeeRepository;

import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Service gérant la logique métier liée aux employés de l'hôtel.
 *
 * Cette classe est annotée @Service et @Transactional.
 * Elle expose toutes les opérations CRUD sur les employés.
 *
 * Les employés sont identifiés par leur NAS (Numéro d'Assurance Sociale).
 * Un employé peut avoir différents rôles (réceptionniste, gérant, femme de chambre, etc.)
 * et est affecté à un hôtel précis.
 *
 * Certains employés sont également désignés comme gérants d'hôtel (Hotel.manager).
 */
@Service        // Déclare cette classe comme service Spring (couche métier)
@Transactional  // Toutes les méthodes s'exécutent dans un contexte transactionnel
public class EmployeeService {

    /**
     * Dépôt JPA pour l'accès à la base de données (table "employee").
     * Injecté automatiquement par Spring via @Autowired.
     */
    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Enregistre un nouvel employé en base de données.
     *
     * @param employee L'employé à persister
     */
    @SuppressWarnings("null")
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    /**
     * Récupère un employé spécifique par son NAS (Numéro d'Assurance Sociale).
     *
     * @param sinEmployee Le NAS de l'employé (format "XXX-XXX-XXX")
     * @return L'employé correspondant, ou null si non trouvé
     */
    public Employee getEmployee(String sinEmployee) {
        return employeeRepository.findBySinEmployee(sinEmployee).orElse(null);
    }

    /**
     * Supprime un employé de la base de données par son NAS.
     * Attention : si l'employé est gérant d'un hôtel, la suppression peut
     * affecter la contrainte de clé étrangère Hotel.sin_manager.
     *
     * @param sinEmployee Le NAS de l'employé à supprimer
     */
    public void deleteEmployee(String sinEmployee) {
        employeeRepository.deleteBySinEmployee(sinEmployee);
    }

    /**
     * Met à jour les informations d'un employé existant en base de données.
     *
     * @param employee L'employé avec les nouvelles valeurs à persister
     */
    @SuppressWarnings("null")
    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    /**
     * Récupère la liste de tous les employés enregistrés dans le système.
     *
     * @return Liste complète de tous les employés (vide si aucun)
     */
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }
}
