package com.example.EHotel.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Imports des DTOs et modèles nécessaires
import com.example.EHotel.dtos.CreateHotelDTO;
import com.example.EHotel.model.employee.Employee;
import com.example.EHotel.model.hotel.Hotel;
import com.example.EHotel.model.hotelchain.HotelChain;
// Imports des services utilisés
import com.example.EHotel.services.EmployeeService;
import com.example.EHotel.services.HotelChainService;
import com.example.EHotel.services.HotelService;

import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/**
 * Contrôleur Spring MVC gérant toutes les opérations liées aux hôtels.
 *
 * Préfixe de route : /hotel
 * Toutes les URL de ce contrôleur commencent par "/hotel".
 *
 * Routes disponibles :
 *   GET  /hotel/hotels          → Afficher la liste de tous les hôtels
 *   GET  /hotel/hotel/add       → Afficher le formulaire d'ajout d'hôtel
 *   POST /hotel/hotel/add       → Traiter la soumission du formulaire d'ajout
 *   POST /hotel/hotel/delete/{id} → Supprimer un hôtel par son ID
 *   GET  /hotel/hotel/update/{id} → Afficher le formulaire de modification
 *   POST /hotel/hotel/update/{id} → Traiter la soumission du formulaire de modification
 *
 * @Controller : déclare cette classe comme contrôleur Spring MVC (retourne des vues HTML)
 * @RequestMapping : définit le préfixe d'URL commun à toutes les routes de ce contrôleur
 */
@Controller
@RequestMapping("/hotel")
public class HotelController {

    /** Service de gestion des hôtels (opérations CRUD) */
    @Autowired
    private HotelService hotelService;

    /** Service de gestion des chaînes hôtelières (pour les listes déroulantes) */
    @Autowired
    private HotelChainService hotelChainService;

    /** Service de gestion des employés (pour la sélection du gérant) */
    @Autowired
    private EmployeeService employeeService;

    /**
     * Affiche la liste de tous les hôtels enregistrés dans le système.
     *
     * GET /hotel/hotels → vue "hotels.html"
     *
     * @param model Le modèle Spring MVC pour passer des données à la vue
     * @return Le nom du template Thymeleaf à afficher ("hotels")
     */
    @GetMapping("/hotels")
    public String showsHotels(Model model) {
        // Récupère tous les hôtels et les passe au template via le modèle
        List<Hotel> hotels = hotelService.getHotels();
        model.addAttribute("hotels", hotels);
        return "hotels";  // Retourne le nom du template : src/main/resources/templates/hotels.html
    }

    /**
     * Affiche le formulaire de création d'un nouvel hôtel.
     *
     * GET /hotel/hotel/add → vue "add-hotel-form.html"
     * Pré-remplit les listes déroulantes avec les chaînes et employés disponibles.
     *
     * @param model Le modèle Spring MVC
     * @return Le nom du template "add-hotel-form"
     */
    @GetMapping("/hotel/add")
    public String showAddHotelForm(Model model) {
        // Charge toutes les chaînes hôtelières pour le sélecteur
        List<HotelChain> hotelChains = hotelChainService.getHotelChains();
        // Charge tous les employés pour le sélecteur de gérant
        List<Employee> employees = employeeService.getEmployees();
        // Crée un DTO vide pour lier le formulaire (Thymeleaf th:object)
        model.addAttribute("hotel", new CreateHotelDTO());
        model.addAttribute("hotelChains", hotelChains);
        model.addAttribute("employees", employees);

        return "add-hotel-form";
    }

    /**
     * Traite la soumission du formulaire de création d'un hôtel.
     *
     * POST /hotel/hotel/add
     * Valide les données, crée l'entité Hotel et la sauvegarde.
     * En cas d'erreur, réaffiche le formulaire avec les messages d'erreur.
     *
     * @param hotel         Le DTO contenant les données du formulaire (validé par @Valid)
     * @param result        Résultat de la validation Bean Validation
     * @param model         Le modèle Spring MVC
     * @return Redirection vers la liste des hôtels si succès, ou le formulaire si erreur
     */
    @PostMapping("/hotel/add")
    public String addHotel(@Valid @ModelAttribute("hotel") CreateHotelDTO hotel, BindingResult result, Model model) {

        // Si le formulaire contient des erreurs de validation, on réaffiche le formulaire
        if (result.hasErrors()) {
            List<HotelChain> hotelChains = hotelChainService.getHotelChains();
            List<Employee> employees = employeeService.getEmployees();
            model.addAttribute("hotelChains", hotelChains);
            model.addAttribute("employees", employees);
            return "add-hotel-form";
        }

        // Crée un nouvel objet Hotel à partir des données du DTO
        Hotel newHotel = new Hotel();
        // Récupère la chaîne hôtelière sélectionnée depuis la BDD
        HotelChain hotelChain = hotelChainService.getHotelChain(hotel.getIdHotelChain());
        // Récupère le gérant sélectionné depuis la BDD
        Employee manager = employeeService.getEmployee(hotel.getSinManager());

        // Vérifie que la chaîne et le gérant existent réellement en BDD
        if (hotelChain == null || manager == null) {
            List<HotelChain> hotelChains = hotelChainService.getHotelChains();
            List<Employee> employees = employeeService.getEmployees();
            model.addAttribute("hotelChains", hotelChains);
            model.addAttribute("employees", employees);
            return "add-hotel-form";
        }

        // Copie les données du DTO vers l'entité Hotel
        newHotel.setName(hotel.getName());
        newHotel.setRoomsNumber(hotel.getRoomsNumber());
        newHotel.setStartNumber(hotel.getStartNumber());
        newHotel.setEmail(hotel.getEmail());
        newHotel.setStreetName(hotel.getStreetName());
        newHotel.setStreetNumber(hotel.getStreetNumber());
        newHotel.setCity(hotel.getCity());
        newHotel.setPostalCode(hotel.getPostalCode());
        newHotel.setCountry(hotel.getCountry());
        newHotel.setHotelChain(hotelChain);
        newHotel.setManager(manager);
        // Génère un ID unique pour le nouvel hôtel (ID max + 1)
        newHotel.setIdHotel(hotelService.findUnusedId());

        // Persiste le nouvel hôtel en base de données
        hotelService.addHotel(newHotel);

        // Redirige vers la liste des hôtels après succès (pattern PRG : Post-Redirect-Get)
        return "redirect:/hotel/hotels";
    }

    /**
     * Supprime un hôtel par son identifiant.
     *
     * POST /hotel/hotel/delete/{id}
     * Utilise POST (et non DELETE) car les formulaires HTML ne supportent que GET et POST.
     *
     * @param id L'identifiant de l'hôtel à supprimer (extrait de l'URL)
     * @return Redirection vers la liste des hôtels
     */
    @PostMapping("/hotel/delete/{id}")
    public String deleteHotel(@PathVariable("id") int id) {
        hotelService.deleteHotel(id);
        // Redirige vers la liste après suppression
        return "redirect:/hotel/hotels";
    }

    /**
     * Affiche le formulaire de modification d'un hôtel existant.
     *
     * GET /hotel/hotel/update/{id} → vue "update-hotel-form.html"
     * Pré-remplit le formulaire avec les données actuelles de l'hôtel.
     *
     * @param id    L'identifiant de l'hôtel à modifier (extrait de l'URL)
     * @param model Le modèle Spring MVC
     * @return Le template "update-hotel-form", ou redirection si hôtel introuvable
     */
    @GetMapping("/hotel/update/{id}")
    public String showUpdateHotelForm(@PathVariable("id") int id, Model model) {
        // Récupère l'hôtel existant depuis la BDD
        Hotel existingHotel = hotelService.getHotel(id);
        if (existingHotel == null) {
            // L'hôtel n'existe pas, redirige vers la liste
            return "redirect:/hotel/hotels";
        }

        // Crée un DTO et le pré-remplit avec les données actuelles de l'hôtel
        CreateHotelDTO hotelDTO = new CreateHotelDTO();
        hotelDTO.setIdHotel(existingHotel.getIdHotel());
        hotelDTO.setName(existingHotel.getName());
        hotelDTO.setRoomsNumber(existingHotel.getRoomsNumber());
        hotelDTO.setStartNumber(existingHotel.getStartNumber());
        hotelDTO.setEmail(existingHotel.getEmail());
        hotelDTO.setStreetName(existingHotel.getStreetName());
        hotelDTO.setCity(existingHotel.getCity());
        hotelDTO.setPostalCode(existingHotel.getPostalCode());
        hotelDTO.setCountry(existingHotel.getCountry());
        hotelDTO.setIdHotelChain(existingHotel.getHotelChain().getId());
        hotelDTO.setSinManager(existingHotel.getManager().getSinEmployee());

        // Charge les listes pour les sélecteurs du formulaire
        List<HotelChain> hotelChains = hotelChainService.getHotelChains();
        List<Employee> employees = employeeService.getEmployees();

        model.addAttribute("hotel", hotelDTO);
        model.addAttribute("hotelChains", hotelChains);
        model.addAttribute("employees", employees);

        return "update-hotel-form";
    }

    /**
     * Traite la soumission du formulaire de modification d'un hôtel.
     *
     * POST /hotel/hotel/update/{id}
     * Valide les données, met à jour l'entité Hotel et la sauvegarde.
     *
     * @param id     L'identifiant de l'hôtel à modifier (extrait de l'URL)
     * @param hotel  Le DTO contenant les nouvelles données du formulaire (validé par @Valid)
     * @param result Résultat de la validation Bean Validation
     * @param model  Le modèle Spring MVC
     * @return Redirection vers la liste des hôtels si succès, ou le formulaire si erreur
     */
    @PostMapping("/hotel/update/{id}")
    public String updateHotel(
            @PathVariable("id") int id,
            @Valid @ModelAttribute("hotel") CreateHotelDTO hotel,
            BindingResult result,
            Model model
    ) {
        // Si le formulaire contient des erreurs, on réaffiche le formulaire avec les erreurs
        if (result.hasErrors()) {
            List<HotelChain> hotelChains = hotelChainService.getHotelChains();
            List<Employee> employees = employeeService.getEmployees();
            model.addAttribute("hotelChains", hotelChains);
            model.addAttribute("employees", employees);
            return "update-hotel-form";
        }

        // Récupère l'hôtel existant à mettre à jour depuis la BDD
        Hotel updatedHotel = hotelService.getHotel(id);
        if (updatedHotel == null) {
            return "redirect:/hotel/hotels";
        }

        // Récupère la chaîne et le gérant depuis la BDD pour vérification
        HotelChain hotelChain = hotelChainService.getHotelChain(hotel.getIdHotelChain());
        Employee manager = employeeService.getEmployee(hotel.getSinManager());
        if (hotelChain == null || manager == null) {
            List<HotelChain> hotelChains = hotelChainService.getHotelChains();
            List<Employee> employees = employeeService.getEmployees();
            model.addAttribute("hotelChains", hotelChains);
            model.addAttribute("employees", employees);
            return "update-hotel-form";
        }

        // Met à jour tous les champs de l'hôtel avec les nouvelles valeurs
        updatedHotel.setName(hotel.getName());
        updatedHotel.setRoomsNumber(hotel.getRoomsNumber());
        updatedHotel.setStartNumber(hotel.getStartNumber());
        updatedHotel.setEmail(hotel.getEmail());
        updatedHotel.setStreetName(hotel.getStreetName());
        updatedHotel.setCity(hotel.getCity());
        updatedHotel.setPostalCode(hotel.getPostalCode());
        updatedHotel.setCountry(hotel.getCountry());
        updatedHotel.setHotelChain(hotelChain);
        updatedHotel.setManager(manager);

        // Persiste les modifications en base de données
        hotelService.updateHotel(updatedHotel);

        return "redirect:/hotel/hotels";
    }


}
