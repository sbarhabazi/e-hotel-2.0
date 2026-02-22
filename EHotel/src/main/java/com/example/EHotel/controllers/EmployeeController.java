package com.example.EHotel.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Import du DTO pour la gestion des clients
import com.example.EHotel.dtos.AddCustomerDTO;
// Import du modèle Client
import com.example.EHotel.model.customer.Customer;
// Import du service de gestion des clients
import com.example.EHotel.services.CustomerService;

import jakarta.validation.Valid;

/**
 * Contrôleur Spring MVC gérant les fonctionnalités accessibles aux employés de l'hôtel.
 *
 * Préfixe de route : /employee
 *
 * Cet contrôleur permet aux employés de gérer les clients (CRUD complet) :
 * Routes disponibles :
 *   GET  /employee/              → Page d'accueil du portail employé
 *   GET  /employee/customers     → Afficher la liste de tous les clients
 *   GET  /employee/customer/add  → Formulaire d'ajout d'un client
 *   POST /employee/customer/add  → Traiter l'ajout d'un nouveau client
 *   POST /employee/customer/delete/{sin} → Supprimer un client par son NAS
 *   GET  /employee/customer/update/{sin} → Formulaire de modification d'un client
 *   POST /employee/customer/update/{sin} → Traiter la modification d'un client
 *
 * Note : La gestion des employés eux-mêmes est dans ManagerController.
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

    /** Service de gestion des clients (opérations CRUD sur la table "customer") */
    @Autowired
    private CustomerService customerService;

    /**
     * Affiche la page d'accueil du portail employé.
     *
     * GET/POST /employee/ → vue "employee.html"
     * Utilise @RequestMapping (au lieu de @GetMapping) pour accepter GET et POST.
     *
     * @return Le nom du template "employee"
     */
    @RequestMapping("")
    public String showEmployeeHome() {
        return "employee";  // Template : src/main/resources/templates/employee.html
    }

    /**
     * Affiche la liste de tous les clients enregistrés dans le système.
     *
     * GET /employee/customers → vue "customers.html"
     *
     * @param model Le modèle Spring MVC
     * @return Le template "customers"
     */
    @GetMapping("/customers")
    public String showCustomers(Model model) {
        List<Customer> customers = customerService.getCustomers();
        model.addAttribute("customers", customers);
        return "customers";
    }

    /**
     * Affiche le formulaire d'ajout d'un nouveau client.
     *
     * GET /employee/customer/add → vue "add-customer-form.html"
     *
     * @param model Le modèle Spring MVC
     * @return Le template "add-customer-form"
     */
    @GetMapping("/customer/add")
    public String showAddCustomerForm(Model model) {
        // Crée un DTO vide pour initialiser le formulaire
        model.addAttribute("customer", new AddCustomerDTO());
        return "add-customer-form";
    }

    /**
     * Traite la soumission du formulaire d'ajout d'un client.
     *
     * POST /employee/customer/add
     * Valide les données et crée un nouveau client en base de données.
     *
     * @param customerDTO Les données du formulaire (NAS, nom, prénom, adresse)
     * @param result      Résultat de la validation Bean Validation
     * @return Redirection vers la liste des clients si succès, ou le formulaire si erreur
     */
    @PostMapping("/customer/add")
    public String addCustomer(@Valid @ModelAttribute("customer") AddCustomerDTO customerDTO,
                               BindingResult result) {

        if (result.hasErrors()) {
            // Erreurs de validation : réaffiche le formulaire avec les messages d'erreur
            return "add-customer-form";
        }

        // Crée un nouvel objet Customer à partir des données du DTO
        Customer newCustomer = new Customer();
        newCustomer.setSinCustomer(customerDTO.getSinCustomer());
        newCustomer.setFirstname(customerDTO.getFirstname());
        newCustomer.setLastname(customerDTO.getLastname());
        newCustomer.setCheckInDate(customerDTO.getCheckInDate());
        newCustomer.setStreetNumber(customerDTO.getStreetNumber());
        newCustomer.setStreetName(customerDTO.getStreetName());
        newCustomer.setCity(customerDTO.getCity());
        newCustomer.setPostalCode(customerDTO.getPostalCode());
        newCustomer.setCountry(customerDTO.getCountry());

        // Persiste le nouveau client en base de données
        customerService.addCustomer(newCustomer);

        // Redirige vers la liste des clients après ajout réussi
        return "redirect:/employee/customers";
    }

    /**
     * Supprime un client par son NAS (Numéro d'Assurance Sociale).
     *
     * POST /employee/customer/delete/{sinCustomer}
     *
     * @param sinCustomer Le NAS du client à supprimer (extrait de l'URL)
     * @return Redirection vers la liste des clients
     */
    @PostMapping("/customer/delete/{sinCustomer}")
    public String deleteCustomer(@PathVariable("sinCustomer") String sinCustomer) {
        customerService.deleteCustomer(sinCustomer);
        return "redirect:/employee/customers";
    }

    /**
     * Affiche le formulaire de modification d'un client existant.
     *
     * GET /employee/customer/update/{sinCustomer} → vue "update-customer-form.html"
     * Pré-remplit le formulaire avec les données actuelles du client.
     *
     * @param sinCustomer Le NAS du client à modifier (extrait de l'URL)
     * @param model       Le modèle Spring MVC
     * @return Le template "update-customer-form", ou redirection si client introuvable
     */
    @GetMapping("/customer/update/{sinCustomer}")
    public String showUpdateCustomerForm(@PathVariable("sinCustomer") String sinCustomer, Model model) {
        Customer existingCustomer = customerService.getCustomer(sinCustomer);
        if (existingCustomer == null) {
            return "redirect:/employee/customers";
        }

        // Crée un DTO pré-rempli avec les données actuelles du client
        AddCustomerDTO customerDTO = new AddCustomerDTO();
        customerDTO.setSinCustomer(existingCustomer.getSinCustomer());
        customerDTO.setFirstname(existingCustomer.getFirstname());
        customerDTO.setLastname(existingCustomer.getLastname());
        customerDTO.setCheckInDate(existingCustomer.getCheckInDate());
        customerDTO.setStreetNumber(existingCustomer.getStreetNumber());
        customerDTO.setStreetName(existingCustomer.getStreetName());
        customerDTO.setCity(existingCustomer.getCity());
        customerDTO.setPostalCode(existingCustomer.getPostalCode());
        customerDTO.setCountry(existingCustomer.getCountry());

        model.addAttribute("customer", customerDTO);
        return "update-customer-form";
    }

    /**
     * Traite la soumission du formulaire de modification d'un client.
     *
     * POST /employee/customer/update/{sinCustomer}
     * Valide les données et met à jour le client en base de données.
     *
     * @param sinCustomer Le NAS du client à modifier (extrait de l'URL)
     * @param customerDTO Les nouvelles données du formulaire (validées par @Valid)
     * @param result      Résultat de la validation Bean Validation
     * @return Redirection vers la liste si succès, ou le formulaire si erreur
     */
    @PostMapping("/customer/update/{sinCustomer}")
    public String updateCustomer(
            @PathVariable("sinCustomer") String sinCustomer,
            @Valid @ModelAttribute("customer") AddCustomerDTO customerDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "update-customer-form";
        }

        Customer existingCustomer = customerService.getCustomer(sinCustomer);
        if (existingCustomer == null) {
            return "redirect:/employee/customers";
        }

        // Met à jour tous les champs du client avec les nouvelles valeurs
        existingCustomer.setSinCustomer(sinCustomer);  // Le NAS (clé primaire) ne change pas
        existingCustomer.setFirstname(customerDTO.getFirstname());
        existingCustomer.setLastname(customerDTO.getLastname());
        existingCustomer.setCheckInDate(customerDTO.getCheckInDate());
        existingCustomer.setStreetNumber(customerDTO.getStreetNumber());
        existingCustomer.setStreetName(customerDTO.getStreetName());
        existingCustomer.setCity(customerDTO.getCity());
        existingCustomer.setPostalCode(customerDTO.getPostalCode());
        existingCustomer.setCountry(customerDTO.getCountry());

        // Persiste les modifications en base de données
        customerService.updateCustomer(existingCustomer);
        return "redirect:/employee/customers";
    }



}
