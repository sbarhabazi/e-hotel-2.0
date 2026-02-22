package com.example.EHotel.repositories.hotelchain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.EHotel.model.hotelchain.PhoneNumberHotelChain;

/**
 * Interface dépôt JPA pour les numéros de téléphone des chaînes hôtelières.
 * Correspond à la table "phone_number_hotel_chain".
 *
 * Étend JpaRepository<PhoneNumberHotelChain, String> où String est la clé primaire (le numéro).
 * Gère les numéros de contact centralisés des chaînes hôtelières.
 */
@Repository
public interface PhoneNumberHotelChainRepository extends JpaRepository<PhoneNumberHotelChain, String> {
    // Toutes les méthodes nécessaires sont héritées de JpaRepository
}
