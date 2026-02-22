package com.example.EHotel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Import du dépôt JPA pour les opérations sur les chaînes hôtelières
import com.example.EHotel.repositories.hotelchain.HotelChainRepository;

import jakarta.transaction.Transactional;

// Import de l'entité HotelChain (chaîne hôtelière)
import com.example.EHotel.model.hotelchain.HotelChain;
import java.util.List;

/**
 * Service gérant la logique métier liée aux chaînes hôtelières.
 *
 * Cette classe est annotée @Service et @Transactional.
 * Elle expose des méthodes de lecture uniquement (pas de création/modification/suppression)
 * car les chaînes hôtelières sont des données de référence gérées directement en base.
 *
 * Les chaînes hôtelières sont utilisées :
 *   - Dans le formulaire de création/modification d'hôtel (sélection de la chaîne)
 *   - Dans le formulaire de recherche de chambres (filtrage par chaîne)
 */
@Service        // Déclare cette classe comme service Spring (couche métier)
@Transactional  // Toutes les méthodes s'exécutent dans un contexte transactionnel
public class HotelChainService {

    /**
     * Dépôt JPA pour l'accès à la base de données (table "hotel_chain").
     * Injecté automatiquement par Spring via @Autowired.
     */
    @Autowired
    private HotelChainRepository hotelChainRepository;

    /**
     * Récupère la liste de toutes les chaînes hôtelières disponibles.
     * Utilisée pour peupler les listes déroulantes dans les formulaires.
     *
     * @return Liste de toutes les chaînes hôtelières (vide si aucune)
     */
    public List<HotelChain> getHotelChains() {
        return hotelChainRepository.findAll();
    }

    /**
     * Récupère une chaîne hôtelière spécifique par son identifiant.
     *
     * @param id Identifiant unique de la chaîne hôtelière
     * @return La chaîne hôtelière correspondante, ou null si non trouvée
     */
    public HotelChain getHotelChain(int id) {
        return hotelChainRepository.findById(id).orElse(null);
    }
}
