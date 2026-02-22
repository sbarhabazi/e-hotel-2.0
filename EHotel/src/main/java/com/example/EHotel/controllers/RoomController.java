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

// Imports des DTOs utilisés
import com.example.EHotel.dtos.BookingDTO;
import com.example.EHotel.dtos.CreateRoomDTO;
import com.example.EHotel.dtos.RoomSearchByIdHotel;
import com.example.EHotel.dtos.RoomSearchCriteriaDTO;
// Imports des modèles nécessaires
import com.example.EHotel.model.customer.Customer;
import com.example.EHotel.model.hotel.Booking;
import com.example.EHotel.model.hotel.Hotel;
import com.example.EHotel.model.hotel.Room;
// Imports des services utilisés
import com.example.EHotel.services.BookingService;
import com.example.EHotel.services.CustomerService;
import com.example.EHotel.services.HotelService;
import com.example.EHotel.services.RoomService;

import jakarta.validation.Valid;

/**
 * Contrôleur Spring MVC gérant toutes les opérations liées aux chambres et réservations.
 *
 * Préfixe de route : /room
 * Toutes les URL de ce contrôleur commencent par "/room".
 *
 * Routes disponibles :
 *   GET  /room/search          → Afficher le formulaire de recherche de chambres
 *   POST /room/search          → Traiter la recherche avec critères et afficher les résultats
 *   GET  /room/select/{id}     → Rediriger vers le formulaire de réservation d'une chambre
 *   GET  /room/book/{idRoom}   → Afficher le formulaire de réservation pour une chambre
 *   POST /room/booking         → Traiter la soumission du formulaire de réservation
 *   GET  /room/list/{idHotel}  → Afficher toutes les chambres d'un hôtel
 *   POST /room/delete/{id}     → Supprimer une chambre
 *   GET  /room/update/{id}     → Afficher le formulaire de modification d'une chambre
 *   POST /room/update/{id}     → Traiter la modification d'une chambre
 *   GET  /room/add             → Afficher le formulaire d'ajout d'une chambre
 *   POST /room/add             → Traiter l'ajout d'une nouvelle chambre
 */
@Controller
@RequestMapping("/room")
public class RoomController {

    /** Service de gestion des chambres (recherche, CRUD) */
    @Autowired
    private RoomService roomService;

    /** Service de gestion des clients (pour vérifier/créer le client lors d'une réservation) */
    @Autowired
    private CustomerService customerService;

    /** Service de gestion des réservations (ajout d'un Booking) */
    @Autowired
    private BookingService bookingService;

    /** Service de gestion des hôtels (pour la sélection d'hôtel) */
    @Autowired
    private HotelService hotelService;

    /**
     * Affiche le formulaire de recherche de chambres disponibles.
     *
     * GET /room/search → vue "search.html"
     * Initialise un DTO vide pour le formulaire de critères.
     *
     * @param model Le modèle Spring MVC
     * @return Le template "search"
     */
    @GetMapping("/search")
    public String showSearchForm(Model model) {
        // Crée un DTO vide pour initialiser le formulaire de recherche
        model.addAttribute("criteria", new RoomSearchCriteriaDTO());
        return "search";
    }

    /**
     * Traite la soumission du formulaire de recherche et affiche les résultats.
     *
     * POST /room/search → vue "search.html" avec les résultats
     * Valide les critères, lance la recherche et retourne les chambres correspondantes.
     *
     * @param criteria      Les critères de recherche saisis par l'utilisateur (validés)
     * @param model         Le modèle Spring MVC
     * @param bindingResult Résultat de la validation Bean Validation
     * @return Le template "search" avec ou sans résultats selon la validité des données
     */
    @PostMapping("/search")
    public String searchRooms(@Valid @ModelAttribute("criteria") RoomSearchCriteriaDTO criteria,
                               Model model, BindingResult bindingResult) {

        // Si les critères contiennent des erreurs, réaffiche le formulaire sans résultats
        if (bindingResult.hasErrors()) {
            return "search";
        }

        // Recherche les chambres disponibles selon les critères en base de données
        List<Room> rooms = roomService.findAvailableRooms(criteria);
        // Passe les résultats au template pour affichage
        model.addAttribute("rooms", rooms);
        return "search";
    }

    /**
     * Redirige vers le formulaire de réservation d'une chambre.
     *
     * GET /room/select/{id} → redirect vers /room/book/{id}
     * Ce endpoint est utilisé depuis le bouton "Sélectionner" dans les résultats de recherche.
     *
     * @param id Identifiant de la chambre sélectionnée
     * @return Redirection vers le formulaire de réservation
     */
    @GetMapping("/select/{id}")
    public String selectRoom(@PathVariable("id") int id) {
        return "redirect:/room/book/" + id;
    }

    /**
     * Affiche le formulaire de réservation pour une chambre spécifique.
     *
     * GET /room/book/{idRoom} → vue "booking-form.html"
     * Vérifie que la chambre existe et initialise le DTO de réservation.
     *
     * @param idRoom L'identifiant de la chambre à réserver (extrait de l'URL)
     * @param model  Le modèle Spring MVC
     * @return Le template "booking-form", ou redirection si chambre introuvable
     */
    @GetMapping("/book/{idRoom}")
    public String showBookingForm(@PathVariable("idRoom") int idRoom, Model model) {
        // Vérifie que la chambre existe en base de données
        Room room = roomService.findRoomById(idRoom);
        if (room == null) {
            // Chambre non trouvée : redirige vers la recherche
            return "redirect:/room/search";
        }

        // Crée un DTO de réservation pré-rempli avec l'ID de la chambre
        BookingDTO booking = new BookingDTO();
        booking.setIdRoom(idRoom);
        model.addAttribute("booking", booking);
        return "booking-form";
    }

    /**
     * Traite la soumission du formulaire de réservation.
     *
     * POST /room/booking
     * Crée le client s'il n'existe pas, puis crée la réservation.
     * Implémente la logique "upsert client" : si le NAS existe, on utilise le client existant.
     *
     * @param bookingDTO    Les données de réservation soumises (NAS, dates, adresse, etc.)
     * @param bindingResult Résultat de la validation Bean Validation
     * @param model         Le modèle Spring MVC
     * @return Redirection vers la page de recherche après succès, ou le formulaire si erreur
     */
    @PostMapping("/booking")
    public String bookRoom(@Valid @ModelAttribute("booking") BookingDTO bookingDTO,
                            BindingResult bindingResult, Model model) {

        // Si le formulaire contient des erreurs de validation, réaffiche le formulaire
        if (bindingResult.hasErrors()) {
            return "booking-form";
        }

        // Vérifie que la chambre sélectionnée existe toujours en base
        Room room = roomService.findRoomById(bookingDTO.getIdRoom());
        if (room == null) {
            bindingResult.rejectValue("idRoom", "booking.idRoom", "La chambre sélectionnée est introuvable.");
            return "booking-form";
        }

        // Vérifie si le client existe déjà en base (recherche par NAS)
        Customer existingCustomer = customerService.getCustomer(bookingDTO.getSinCustomer());

        // Logique "upsert" : si le client n'existe pas, on le crée automatiquement
        Customer customer = existingCustomer;
        if (customer == null) {
            // Crée un nouveau client avec les informations saisies dans le formulaire
            customer = new Customer(
                bookingDTO.getSinCustomer(),
                bookingDTO.getFirstname(),
                bookingDTO.getLastname(),
                bookingDTO.getCheckInDate(),
                bookingDTO.getStreetNumber(),
                bookingDTO.getStreetName(),
                bookingDTO.getCity(),
                bookingDTO.getPostalCode(),
                bookingDTO.getCountry()
            );
            // Persiste le nouveau client en base de données
            customerService.addCustomer(customer);
        }

        // Crée l'objet Booking liant le client à la chambre sur les dates spécifiées
        Booking booking = new Booking(
            customer,
            room,
            bookingDTO.getStartDate(),
            bookingDTO.getEndDate()
        );

        // Persiste la réservation en base de données
        bookingService.addBooking(booking);

        // Redirige vers la page de recherche après réservation réussie
        return "redirect:/room/search";
    }

    /**
     * Affiche la liste des chambres d'un hôtel spécifique.
     *
     * GET /room/list/{idHotel} → vue "room-list.html"
     * Permet au personnel de l'hôtel de gérer les chambres d'un établissement.
     *
     * @param idHotel L'identifiant de l'hôtel (extrait de l'URL)
     * @param model   Le modèle Spring MVC
     * @return Le template "room-list"
     */
    @GetMapping("/list/{idHotel}")
    public String listRooms(@PathVariable("idHotel") int idHotel, Model model) {
        // Récupère toutes les chambres de l'hôtel sélectionné
        List<Room> rooms = roomService.findRoomsByHotelId(idHotel);
        // Récupère tous les hôtels pour le sélecteur permettant de changer d'hôtel
        List<Hotel> hotels = hotelService.getHotels();
        // Crée un DTO pré-sélectionné avec l'hôtel actuel pour le sélecteur
        RoomSearchByIdHotel roomSearch = new RoomSearchByIdHotel();
        roomSearch.setHotelId(idHotel);
        model.addAttribute("rooms", rooms);
        model.addAttribute("hotels", hotels);
        model.addAttribute("roomSearch", roomSearch);
        return "room-list";
    }

    /**
     * Supprime une chambre par son identifiant.
     *
     * POST /room/delete/{id}
     * Après suppression, redirige vers la liste des chambres de l'hôtel parent.
     *
     * @param id L'identifiant de la chambre à supprimer (extrait de l'URL)
     * @return Redirection vers la liste des chambres de l'hôtel, ou vers l'hôtel 1 si introuvable
     */
    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable("id") int id) {
        Room existingRoom = roomService.findRoomById(id);
        if (existingRoom == null || existingRoom.getHotel() == null) {
            // Chambre introuvable : redirige vers l'hôtel par défaut (ID 1)
            return "redirect:/room/list/1";
        }

        // Mémorise l'ID de l'hôtel avant la suppression pour la redirection
        int hotelId = existingRoom.getHotel().getIdHotel();
        roomService.deleteRoom(id);
        // Redirige vers la liste des chambres de l'hôtel parent
        return "redirect:/room/list/" + hotelId;
    }

    /**
     * Affiche le formulaire de modification d'une chambre.
     *
     * GET /room/update/{id} → vue "update-room-form.html"
     * Pré-remplit le formulaire avec les données actuelles de la chambre.
     *
     * @param id    L'identifiant de la chambre à modifier (extrait de l'URL)
     * @param model Le modèle Spring MVC
     * @return Le template "update-room-form", ou redirection si chambre introuvable
     */
    @GetMapping("/update/{id}")
    public String showUpdateRoomForm(@PathVariable("id") int id, Model model) {
        Room existingRoom = roomService.findRoomById(id);
        if (existingRoom == null) {
            return "redirect:/room/list/1";
        }

        // Passe la chambre existante au formulaire pour pré-remplissage
        model.addAttribute("room", existingRoom);
        return "update-room-form";
    }

    /**
     * Traite la soumission du formulaire de modification d'une chambre.
     *
     * POST /room/update/{id}
     * Met à jour les champs modifiables de la chambre (sans changer l'hôtel associé).
     *
     * @param id   L'identifiant de la chambre à modifier (extrait de l'URL)
     * @param room L'objet Room contenant les nouvelles valeurs (depuis le formulaire)
     * @return Redirection vers la liste des chambres de l'hôtel parent
     */
    @PostMapping("/update/{id}")
    public String updateRoom(@PathVariable("id") int id, @ModelAttribute("room") Room room) {
        Room updatedRoom = roomService.findRoomById(id);
        if (updatedRoom == null || updatedRoom.getHotel() == null) {
            return "redirect:/room/list/1";
        }

        // Mémorise l'ID de l'hôtel pour la redirection après mise à jour
        int hotelId = updatedRoom.getHotel().getIdHotel();

        // Met à jour uniquement les champs modifiables (l'hôtel associé reste inchangé)
        updatedRoom.setRoomNumber(room.getRoomNumber());
        updatedRoom.setAvailability(room.getAvailability());
        updatedRoom.setPrice(room.getPrice());
        updatedRoom.setView(room.getView());
        updatedRoom.setExtensible(room.getExtensible());
        updatedRoom.setCapacity(room.getCapacity());

        // Persiste les modifications en base de données
        roomService.updateRoom(updatedRoom);
        return "redirect:/room/list/" + hotelId;
    }

    /**
     * Affiche le formulaire d'ajout d'une nouvelle chambre.
     *
     * GET /room/add → vue "add-room-form.html"
     * Charge la liste des hôtels pour le sélecteur.
     *
     * @param model Le modèle Spring MVC
     * @return Le template "add-room-form"
     */
    @GetMapping("/add")
    public String showAddRoomForm(Model model) {
        // Charge tous les hôtels pour le sélecteur d'affectation de la chambre
        List<Hotel> hotels = hotelService.getHotels();
        model.addAttribute("hotels", hotels);
        // Crée un DTO vide pour le formulaire
        model.addAttribute("room", new CreateRoomDTO());

        return "add-room-form";
    }

    /**
     * Traite la soumission du formulaire d'ajout d'une nouvelle chambre.
     *
     * POST /room/add
     * Valide les données, crée l'entité Room et la persiste en base.
     *
     * @param roomInfo      Le DTO contenant les données de la nouvelle chambre (validé)
     * @param bindingResult Résultat de la validation Bean Validation
     * @param model         Le modèle Spring MVC
     * @return Redirection vers la liste des chambres de l'hôtel, ou le formulaire si erreur
     */
    @PostMapping("/add")
    public String addRoom(@Valid @ModelAttribute("room") CreateRoomDTO roomInfo,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // Recharge la liste des hôtels pour le sélecteur
            List<Hotel> hotels = hotelService.getHotels();
            model.addAttribute("hotels", hotels);
            return "add-room-form";
        }

        Room room = new Room();
        // Récupère l'hôtel sélectionné depuis la BDD
        Hotel hotel = hotelService.getHotel(roomInfo.getIdHotel());
        if (hotel == null) {
            // L'hôtel sélectionné n'existe pas
            bindingResult.rejectValue("idHotel", "room.idHotel", "L'hôtel sélectionné est introuvable.");
            List<Hotel> hotels = hotelService.getHotels();
            model.addAttribute("hotels", hotels);
            return "add-room-form";
        }

        // Copie les données du DTO vers l'entité Room
        room.setRoomNumber(roomInfo.getRoomNumber());
        room.setAvailability(roomInfo.getAvailability());
        room.setPrice(roomInfo.getPrice());
        room.setView(roomInfo.getView());
        room.setExtensible(roomInfo.getExtensible());
        room.setCapacity(roomInfo.getCapacity());
        room.setHotel(hotel);
        // Génère un ID unique pour la nouvelle chambre
        room.setIdRoom(roomService.findUnusedId());

        // Persiste la nouvelle chambre en base de données
        roomService.saveRoom(room);

        // Redirige vers la liste des chambres de l'hôtel associé
        return "redirect:/room/list/" + hotel.getIdHotel();
    }

}
