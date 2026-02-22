package com.example.EHotel.dtos;

// Annotations Lombok pour la génération automatique de code boilerplate
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) simple pour la sélection d'un hôtel dans la liste des chambres.
 *
 * Ce DTO est utilisé dans la page room-list.html pour permettre à l'utilisateur
 * de changer d'hôtel via un sélecteur de formulaire.
 * Il ne contient qu'un seul champ : l'identifiant de l'hôtel sélectionné.
 *
 * Utilisé dans le contrôleur RoomController.listRooms() pour pré-sélectionner
 * l'hôtel actuel dans le menu déroulant de la vue.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomSearchByIdHotel {

    /**
     * Identifiant de l'hôtel dont on souhaite afficher les chambres.
     * Utilisé pour pré-remplir le sélecteur d'hôtel dans la vue room-list.html.
     */
    private int hotelId;
}
