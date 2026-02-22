package com.example.EHotel.repositories.hotelchain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.EHotel.model.hotelchain.EmailsHotelChain;

/**
 * Interface dépôt JPA pour les emails des chaînes hôtelières (table "emails_hotel_chain").
 *
 * Étend JpaRepository<EmailsHotelChain, String> où String est la clé primaire (l'email).
 * Gère les adresses email de contact associées aux chaînes hôtelières.
 */
@Repository
public interface EmailsHotelChainRepository extends JpaRepository<EmailsHotelChain, String> {
    // Toutes les méthodes nécessaires sont héritées de JpaRepository
}
