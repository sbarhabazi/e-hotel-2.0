package com.example.EHotel.services;

// Annotations Spring pour l'injection de dépendances et la gestion des transactions
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Import du DTO de critères de recherche
import com.example.EHotel.dtos.RoomSearchCriteriaDTO;
// Import de l'entité Room
import com.example.EHotel.model.hotel.Room;
// Import du dépôt JPA pour les opérations sur les chambres
import com.example.EHotel.repositories.hotel.RoomRepository;

// Annotation pour la gestion transactionnelle
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Service gérant la logique métier liée aux chambres d'hôtel.
 *
 * Cette classe est annotée @Service et @Transactional.
 * Elle expose les opérations CRUD sur les chambres ainsi qu'une méthode
 * de recherche avancée basée sur plusieurs critères (prix, capacité, dates, etc.).
 *
 * Utilise une injection par constructeur (meilleure pratique recommandée par Spring)
 * plutôt que l'injection par champ (@Autowired directement sur le champ).
 */
@Service        // Déclare cette classe comme service Spring (couche métier)
@Transactional  // Toutes les méthodes s'exécutent dans un contexte transactionnel
public class RoomService {

    /**
     * Dépôt JPA pour l'accès à la base de données (table "room").
     * Déclaré final car injecté par constructeur (immuabilité garantie).
     */
    private final RoomRepository roomRepository;

    /**
     * Constructeur avec injection de dépendance par constructeur.
     * Cette approche est préférable à l'injection par champ car elle permet
     * les tests unitaires et garantit l'immuabilité de la référence.
     *
     * @param roomRepository Le dépôt JPA des chambres, injecté par Spring
     */
    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Recherche les chambres disponibles selon les critères fournis.
     * Délègue la requête JPQL complexe au dépôt RoomRepository.
     *
     * Les critères incluent : capacité, prix max, chaîne hôtelière, classement,
     * nombre de chambres de l'hôtel et disponibilité sur les dates souhaitées.
     *
     * @param criteria Objet contenant tous les critères de filtrage
     * @return Liste des chambres correspondant aux critères (vide si aucune)
     */
    public List<Room> findAvailableRooms(RoomSearchCriteriaDTO criteria) {
        return roomRepository.findAvailableRooms(criteria);
    }

    /**
     * Récupère une chambre spécifique par son identifiant.
     *
     * @param id Identifiant unique de la chambre
     * @return La chambre correspondante, ou null si non trouvée
     */
    public Room findRoomById(int id) {
        return roomRepository.findById(id).orElse(null);
    }

    /**
     * Récupère toutes les chambres appartenant à un hôtel spécifique.
     *
     * @param hotelId Identifiant de l'hôtel dont on veut les chambres
     * @return Liste des chambres de cet hôtel (vide si l'hôtel n'a pas de chambres)
     */
    public List<Room> findRoomsByHotelId(int hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    /**
     * Enregistre une nouvelle chambre en base de données.
     *
     * @param room La chambre à persister
     * @return La chambre sauvegardée (avec son ID généré si applicable)
     */
    @SuppressWarnings("null")
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    /**
     * Supprime une chambre de la base de données par son identifiant.
     *
     * @param id Identifiant de la chambre à supprimer
     */
    public void deleteRoom(int id) {
        roomRepository.deleteById(id);
    }

    /**
     * Met à jour une chambre existante en base de données.
     *
     * @param room La chambre avec les nouvelles valeurs à persister
     * @return La chambre mise à jour
     */
    @SuppressWarnings("null")
    public Room updateRoom(Room room) {
        return roomRepository.save(room);
    }

    /**
     * Récupère toutes les chambres de tous les hôtels.
     *
     * @return Liste complète de toutes les chambres en base
     */
    public List<Room> findAllRooms() {
        return roomRepository.findAll();
    }

    /**
     * Génère un nouvel identifiant unique pour une chambre.
     * Stratégie : trouve l'ID maximum existant et l'incrémente de 1.
     * Si aucune chambre n'existe, retourne 1.
     *
     * @return Le prochain identifiant disponible pour une chambre
     */
    public Integer findUnusedId() {
        return roomRepository.findAll().stream()
                // Extrait les IDs de toutes les chambres existantes
                .map(Room::getIdRoom)
                // Filtre les IDs null (sécurité)
                .filter(id -> id != null)
                // Trouve la valeur maximale
                .max(Integer::compareTo)
                // Si aucune chambre n'existe, utilise 0 comme base, puis ajoute 1
                .orElse(0) + 1;
    }
}
