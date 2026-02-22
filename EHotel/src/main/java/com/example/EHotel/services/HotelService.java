package com.example.EHotel.services;

// Annotations Spring pour l'injection de dépendances et la gestion des transactions
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Import du dépôt JPA pour les opérations sur les hôtels
import com.example.EHotel.repositories.hotel.HotelRepository;
// Import de l'entité Hotel
import com.example.EHotel.model.hotel.Hotel;
import java.util.List;

// Annotation pour la gestion transactionnelle
import jakarta.transaction.Transactional;

/**
 * Service gérant la logique métier liée aux hôtels.
 *
 * Cette classe est annotée @Service (composant Spring géré par le conteneur IoC)
 * et @Transactional (toutes les méthodes s'exécutent dans une transaction).
 *
 * Elle fait le lien entre le contrôleur HotelController et le dépôt HotelRepository.
 * Toutes les opérations CRUD (Create, Read, Update, Delete) sur les hôtels
 * transitent par ce service.
 */
@Service        // Déclare cette classe comme service Spring (couche métier)
@Transactional  // Toutes les méthodes s'exécutent dans un contexte transactionnel
public class HotelService {

    /**
     * Dépôt JPA pour l'accès à la base de données (table "hotel").
     * Injecté automatiquement par Spring via @Autowired.
     */
    @Autowired
    private HotelRepository hotelRepository;

    /**
     * Récupère la liste de tous les hôtels enregistrés en base de données.
     *
     * @return Liste de tous les hôtels (vide si aucun hôtel n'existe)
     */
    public List<Hotel> getHotels() {
        return hotelRepository.findAll();
    }

    /**
     * Récupère un hôtel spécifique par son identifiant.
     *
     * @param id Identifiant unique de l'hôtel
     * @return L'hôtel correspondant, ou null si non trouvé
     */
    public Hotel getHotel(int id) {
        // orElse(null) convertit l'Optional en retournant null si l'hôtel n'existe pas
        return hotelRepository.findById(id).orElse(null);
    }

    /**
     * Enregistre un nouvel hôtel en base de données.
     *
     * @param hotel L'objet Hotel à persister
     * @SuppressWarnings("null") : supprime l'avertissement de l'IDE sur un potentiel null
     */
    @SuppressWarnings("null")
    public void addHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    /**
     * Supprime un hôtel de la base de données par son identifiant.
     *
     * @param id Identifiant de l'hôtel à supprimer
     */
    public void deleteHotel(int id) {
        hotelRepository.deleteById(id);
    }

    /**
     * Met à jour un hôtel existant en base de données.
     * JPA utilise save() pour l'insertion comme pour la mise à jour
     * (si l'entité a déjà un ID, elle est mise à jour ; sinon elle est créée).
     *
     * @param hotel L'hôtel avec les nouvelles valeurs à persister
     */
    @SuppressWarnings("null")
    public void updateHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    /**
     * Génère un nouvel identifiant unique pour un hôtel.
     * Stratégie : trouve l'ID maximum existant et l'incrémente de 1.
     * Si aucun hôtel n'existe, retourne 1.
     *
     * Note : Cette approche est simple mais non thread-safe en environnement concurrent.
     * Pour la production, il serait préférable d'utiliser une séquence de base de données.
     *
     * @return Le prochain identifiant disponible pour un hôtel
     */
    public Integer findUnusedId() {
        return hotelRepository.findAll().stream()
                // Extrait les IDs de tous les hôtels existants
                .map(Hotel::getIdHotel)
                // Filtre les IDs null (sécurité)
                .filter(id -> id != null)
                // Trouve la valeur maximale
                .max(Integer::compareTo)
                // Si aucun hôtel n'existe, utilise 0 comme base, puis ajoute 1
                .orElse(0) + 1;
    }
}
