package com.example.EHotel.repositories.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.EHotel.model.hotel.PhoneNumberHotel;

/**
 * Interface dépôt JPA pour les numéros de téléphone des hôtels (table "phone_number_hotel").
 *
 * Étend JpaRepository<PhoneNumberHotel, String> où String est la clé primaire (le numéro).
 * Permet de gérer les numéros de contact associés aux hôtels.
 */
@Repository
public interface PhoneNumberHotelRepository extends JpaRepository<PhoneNumberHotel, String> {
    // Toutes les méthodes nécessaires sont héritées de JpaRepository
}
