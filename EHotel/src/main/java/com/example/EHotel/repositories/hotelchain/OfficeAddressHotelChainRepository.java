package com.example.EHotel.repositories.hotelchain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.EHotel.model.hotelchain.OfficeAddressHotelChain;

/**
 * Interface dépôt JPA pour les adresses de bureaux des chaînes hôtelières.
 * Correspond à la table "office_address_hotel_chain".
 *
 * Étend JpaRepository<OfficeAddressHotelChain, Integer> pour bénéficier des opérations CRUD.
 * Gère les adresses physiques des bureaux (siège social, bureaux régionaux)
 * des chaînes hôtelières.
 */
@Repository
public interface OfficeAddressHotelChainRepository extends JpaRepository<OfficeAddressHotelChain, Integer> {
    // Toutes les méthodes nécessaires sont héritées de JpaRepository
}
