package com.example.EHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

// Import du DTO pour la gestion des employés
import com.example.EHotel.dtos.AddEmployeeDTO;
// Imports des modèles nécessaires
import com.example.EHotel.model.employee.Employee;
import com.example.EHotel.model.hotel.Hotel;
// Imports des services nécessaires
import com.example.EHotel.services.EmployeeService;
import com.example.EHotel.services.HotelService;

import jakarta.validation.Valid;

import java.util.List;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Contrôleur Spring MVC gérant les fonctionnalités accessibles aux gérants d'hôtel.
 *
 * Préfixe de route : /manager
 *
 * Ce contrôleur permet aux gérants de gérer le personnel de l'hôtel (CRUD complet) :
 * Routes disponibles :
 *   GET  /manager/              → Page d'accueil du portail gérant
 *   GET  /manager/employees     → Afficher la liste de tous les employés
 *   GET  /manager/employee/add  → Formulaire d'ajout d'un employé
 *   POST /manager/employee/add  → Traiter l'ajout d'un nouvel employé
 *   POST /manager/employee/delete/{sin} → Supprimer un employé par son NAS
 *   GET  /manager/employee/update/{sin} → Formulaire de modification d'un employé
 *   POST /manager/employee/update/{sin} → Traiter la modification d'un employé
 */
@Controller
@RequestMapping("/manager")
public class ManagerController {

    /** Service de gestion des employés (opérations CRUD sur la table "employee") */
    @Autowired
    private EmployeeService employeeService;

    /** Service de gestion des hôtels (pour la sélection de l'hôtel d'affectation) */
    @Autowired
    private HotelService hotelService;

    /**
     * Affiche la page d'accueil du portail gérant.
     *
     * GET/POST /manager/ → vue "manager.html"
     *
     * @return Le nom du template "manager"
     */
    @RequestMapping("")
    public String showManagerHome() {
        return "manager";  // Template : src/main/resources/templates/manager.html
    }

    /**
     * Affiche la liste de tous les employés enregistrés dans le système.
     *
     * GET /manager/employees → vue "employees.html"
     *
     * @param model Le modèle Spring MVC
     * @return Le template "employees"
     */
    @GetMapping("/employees")
    public String showsEmployees(Model model) {
        List<Employee> employees = employeeService.getEmployees();
        model.addAttribute("employees", employees);
        return "employees";
    }

    /**
     * Affiche le formulaire d'ajout d'un nouvel employé.
     *
     * GET /manager/employee/add → vue "add-employee-form.html"
     * Charge la liste des hôtels pour le sélecteur d'affectation.
     *
     * @param model Le modèle Spring MVC
     * @return Le template "add-employee-form"
     */
    @GetMapping("/employee/add")
    public String showAddEmployeeForm(Model model) {
        // Charge tous les hôtels pour le sélecteur d'hôtel d'affectation
        List<Hotel> hotels = hotelService.getHotels();
        // Crée un DTO vide pour le formulaire
        model.addAttribute("employee", new AddEmployeeDTO());
        model.addAttribute("hotels", hotels);
        return "add-employee-form";
    }

    /**
     * Traite la soumission du formulaire d'ajout d'un employé.
     *
     * POST /manager/employee/add
     * Valide les données et crée un nouvel employé en base de données.
     *
     * @param employeeDTO Les données du formulaire (NAS, nom, prénom, rôle, adresse, hôtel)
     * @param result      Résultat de la validation Bean Validation
     * @param model       Le modèle Spring MVC
     * @return Redirection vers la liste des employés si succès, ou le formulaire si erreur
     */
    @PostMapping("/employee/add")
    public String addEmployee(@Valid @ModelAttribute("employee") AddEmployeeDTO employeeDTO,
                               BindingResult result, Model model) {

        if (result.hasErrors()) {
            // Erreurs de validation : recharge la liste des hôtels et réaffiche le formulaire
            List<Hotel> hotels = hotelService.getHotels();
            model.addAttribute("hotels", hotels);
            return "add-employee-form";
        }

        // Crée un nouvel objet Employee à partir des données du DTO
        Employee newEmployee = new Employee();
        // Récupère l'hôtel d'affectation depuis la BDD
        Hotel hotel = hotelService.getHotel(employeeDTO.getIdHotel());

        // Copie les données du DTO vers l'entité Employee
        newEmployee.setSinEmployee(employeeDTO.getSinEmployee());
        newEmployee.setFirstname(employeeDTO.getFirstname());
        newEmployee.setLastname(employeeDTO.getLastname());
        newEmployee.setHotel(hotel);
        newEmployee.setRole(employeeDTO.getRole());
        newEmployee.setStreetNumber(employeeDTO.getStreetNumber());
        newEmployee.setStreetName(employeeDTO.getStreetName());
        newEmployee.setCity(employeeDTO.getCity());
        newEmployee.setPostalCode(employeeDTO.getPostalCode());
        newEmployee.setCountry(employeeDTO.getCountry());

        // Persiste le nouvel employé en base de données
        employeeService.addEmployee(newEmployee);

        return "redirect:/manager/employees";
    }

    /**
     * Supprime un employé par son NAS (Numéro d'Assurance Sociale).
     *
     * POST /manager/employee/delete/{sinEmployee}
     *
     * @param sinEmployee Le NAS de l'employé à supprimer (extrait de l'URL)
     * @return Redirection vers la liste des employés
     */
    @PostMapping("/employee/delete/{sinEmployee}")
    public String deleteEmployee(@PathVariable("sinEmployee") String sinEmployee) {
        employeeService.deleteEmployee(sinEmployee);
        return "redirect:/manager/employees";
    }

    /**
     * Affiche le formulaire de modification d'un employé existant.
     *
     * GET /manager/employee/update/{sinEmployee} → vue "update-employee-form.html"
     * Pré-remplit le formulaire avec les données actuelles de l'employé.
     *
     * @param sinEmployee Le NAS de l'employé à modifier (extrait de l'URL)
     * @param model       Le modèle Spring MVC
     * @return Le template "update-employee-form", ou redirection si employé introuvable
     */
    @GetMapping("/employee/update/{sinEmployee}")
    public String showUpdateEmployeeForm(@PathVariable("sinEmployee") String sinEmployee, Model model) {
        Employee existingEmployee = employeeService.getEmployee(sinEmployee);
        if (existingEmployee == null) {
            return "redirect:/manager/employees";
        }

        // Crée un DTO pré-rempli avec les données actuelles de l'employé
        AddEmployeeDTO employeeDTO = new AddEmployeeDTO();
        employeeDTO.setSinEmployee(existingEmployee.getSinEmployee());
        employeeDTO.setFirstname(existingEmployee.getFirstname());
        employeeDTO.setLastname(existingEmployee.getLastname());
        employeeDTO.setIdHotel(existingEmployee.getHotel().getIdHotel());
        employeeDTO.setRole(existingEmployee.getRole());
        employeeDTO.setStreetNumber(existingEmployee.getStreetNumber());
        employeeDTO.setStreetName(existingEmployee.getStreetName());
        employeeDTO.setCity(existingEmployee.getCity());
        employeeDTO.setPostalCode(existingEmployee.getPostalCode());
        employeeDTO.setCountry(existingEmployee.getCountry());

        List<Hotel> hotels = hotelService.getHotels();
        model.addAttribute("employee", employeeDTO);
        model.addAttribute("hotels", hotels);
        return "update-employee-form";
    }

    /**
     * Traite la soumission du formulaire de modification d'un employé.
     *
     * POST /manager/employee/update/{sinEmployee}
     * Valide les données et met à jour l'employé en base de données.
     *
     * @param sinEmployee Le NAS de l'employé à modifier (extrait de l'URL)
     * @param employeeDTO Les nouvelles données du formulaire (validées par @Valid)
     * @param result      Résultat de la validation Bean Validation
     * @param model       Le modèle Spring MVC
     * @return Redirection vers la liste si succès, ou le formulaire si erreur
     */
    @PostMapping("/employee/update/{sinEmployee}")
    public String updateEmployee(
            @PathVariable("sinEmployee") String sinEmployee,
            @Valid @ModelAttribute("employee") AddEmployeeDTO employeeDTO,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            List<Hotel> hotels = hotelService.getHotels();
            model.addAttribute("hotels", hotels);
            return "update-employee-form";
        }

        Employee updatedEmployee = employeeService.getEmployee(sinEmployee);
        if (updatedEmployee == null) {
            return "redirect:/manager/employees";
        }

        // Récupère le nouvel hôtel d'affectation depuis la BDD
        Hotel hotel = hotelService.getHotel(employeeDTO.getIdHotel());
        if (hotel == null) {
            List<Hotel> hotels = hotelService.getHotels();
            model.addAttribute("hotels", hotels);
            return "update-employee-form";
        }

        // Met à jour tous les champs de l'employé avec les nouvelles valeurs
        updatedEmployee.setSinEmployee(sinEmployee);  // Le NAS (clé primaire) ne change pas
        updatedEmployee.setFirstname(employeeDTO.getFirstname());
        updatedEmployee.setLastname(employeeDTO.getLastname());
        updatedEmployee.setHotel(hotel);
        updatedEmployee.setRole(employeeDTO.getRole());
        updatedEmployee.setStreetNumber(employeeDTO.getStreetNumber());
        updatedEmployee.setStreetName(employeeDTO.getStreetName());
        updatedEmployee.setCity(employeeDTO.getCity());
        updatedEmployee.setPostalCode(employeeDTO.getPostalCode());
        updatedEmployee.setCountry(employeeDTO.getCountry());

        // Persiste les modifications en base de données
        employeeService.updateEmployee(updatedEmployee);

        return "redirect:/manager/employees";
    }

}
