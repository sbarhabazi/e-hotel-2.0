# Manuel d'utilisation — E-Hotel

## Table des matières

1. [Introduction](#1-introduction)
2. [Démarrage de l'application](#2-démarrage-de-lapplication)
3. [Page d'accueil](#3-page-daccueil)
4. [Portail Client — Recherche et réservation](#4-portail-client--recherche-et-réservation)
   - 4.1 [Rechercher une chambre disponible](#41-rechercher-une-chambre-disponible)
   - 4.2 [Réserver une chambre](#42-réserver-une-chambre)
5. [Portail Employé — Gestion des clients](#5-portail-employé--gestion-des-clients)
   - 5.1 [Consulter la liste des clients](#51-consulter-la-liste-des-clients)
   - 5.2 [Ajouter un client](#52-ajouter-un-client)
   - 5.3 [Modifier un client](#53-modifier-un-client)
   - 5.4 [Supprimer un client](#54-supprimer-un-client)
6. [Portail Manager — Gestion de l'hôtel](#6-portail-manager--gestion-de-lhôtel)
   - 6.1 [Gestion des hôtels](#61-gestion-des-hôtels)
   - 6.2 [Gestion des chambres](#62-gestion-des-chambres)
   - 6.3 [Gestion des employés](#63-gestion-des-employés)
7. [Gestion des réservations et check-in](#7-gestion-des-réservations-et-check-in)
   - 7.1 [Consulter les réservations](#71-consulter-les-réservations)
   - 7.2 [Effectuer un check-in (transformer en location)](#72-effectuer-un-check-in-transformer-en-location)
   - 7.3 [Consulter les locations actives](#73-consulter-les-locations-actives)
8. [Gestion des paiements](#8-gestion-des-paiements)
   - 8.1 [Enregistrer un paiement](#81-enregistrer-un-paiement)
   - 8.2 [Consulter l'historique des paiements](#82-consulter-lhistorique-des-paiements)
9. [Référence des URLs](#9-référence-des-urls)
10. [Messages d'erreur courants](#10-messages-derreur-courants)

---

## 1. Introduction

**E-Hotel** est une application web de gestion hôtelière multi-établissements. Elle permet de gérer l'ensemble du cycle de vie d'un séjour hôtelier :

```
Recherche → Réservation → Check-in → Location → Paiement
```

L'application est organisée en **trois portails** distincts selon le rôle de l'utilisateur :

| Portail | Utilisateur | Fonctions principales |
|---|---|---|
| **Client** | Tout visiteur | Recherche et réservation de chambres |
| **Employé** | Personnel de l'hôtel | Gestion des clients |
| **Manager** | Gérant d'établissement | Gestion hôtels, chambres, employés, check-in, paiements |

> **Note :** L'application ne gère pas d'authentification. Les portails sont directement accessibles via leurs URLs respectives.

---

## 2. Démarrage de l'application

### Prérequis
- PostgreSQL doit être démarré avec la base `db_hotel` accessible
- Identifiants de connexion : utilisateur `postgres`, mot de passe `password`

### Lancement

```bash
cd e-hotel/EHotel
chmod +x mvnw
./mvnw spring-boot:run
```

L'application est prête lorsque le message suivant apparaît dans la console :

```
Started EHotelApplication in X.XXX seconds
```

Ouvrir ensuite un navigateur et accéder à : **http://localhost:8080**

---

## 3. Page d'accueil

**URL :** `http://localhost:8080/`

La page d'accueil présente trois boutons de navigation :

| Bouton | Destination |
|---|---|
| **Client** | Portail de recherche de chambres |
| **Employé** | Portail de gestion des clients |
| **Manager** | Portail de gestion de l'hôtel |

---

## 4. Portail Client — Recherche et réservation

### 4.1 Rechercher une chambre disponible

**URL :** `http://localhost:8080/room/search`

Ce formulaire permet de trouver des chambres disponibles selon plusieurs critères.

**Champs du formulaire :**

| Champ | Description | Exemple |
|---|---|---|
| Date de début de réservation | Date d'arrivée souhaitée | 2025-06-01 |
| Date de fin de réservation | Date de départ souhaitée | 2025-06-07 |
| Capacité de la chambre | Type de chambre souhaité | Double |
| Prix maximum | Budget maximal par nuit (en $) | 200 |
| Chaîne hôtelière | Chaîne préférée | Marriott International |
| Nombre d'étoiles de l'hôtel (min) | Classement minimum souhaité | 3 |
| Nombre de chambres de l'hôtel (min) | Taille minimale de l'établissement | 50 |

**Types de capacité disponibles :**
- Simple, Double, Triple, Quadruple, Suite, Penthouse

**Chaînes hôtelières disponibles :**
1. Marriott International
2. Hilton Worldwide Holdings Inc.
3. InterContinental Hotels Group
4. AccorHotels
5. Wyndham Hotels & Resorts

**Résultats :**

Après soumission, les chambres disponibles s'affichent dans un tableau avec : la chaîne hôtelière, le nom de l'hôtel, le nombre d'étoiles, la ville, le numéro de chambre, la capacité, la vue et le prix.

Cliquer sur **"Réserver"** pour passer à l'étape suivante.

---

### 4.2 Réserver une chambre

**URL :** `http://localhost:8080/room/book/{id}` (redirigée automatiquement depuis "Réserver")

Ce formulaire collecte les informations du client et les dates de séjour.

> **Fonctionnement :** Si le NAS saisi correspond à un client déjà enregistré, ses informations existantes seront utilisées. Sinon, un nouveau compte client est créé automatiquement.

**Champs du formulaire :**

| Champ | Description | Format / Contrainte |
|---|---|---|
| NAS Client | Numéro d'Assurance Sociale | 9 chiffres (ex : 123456789) |
| Prénom | Prénom du client | Texte libre |
| Nom | Nom de famille | Texte libre |
| Numéro de rue | Partie numérique de l'adresse | Texte libre |
| Nom de rue | Nom de la rue | Texte libre |
| Ville | Ville de résidence | Texte libre |
| Code postal | Format canadien | Ex : A1A 1A1 |
| Pays | Pays de résidence | Canada / États-Unis / Mexique |
| Date de début | Date d'arrivée | Aujourd'hui ou plus tard |
| Date de fin | Date de départ | Après ou égale à la date de début |

Après soumission réussie, la réservation est enregistrée et vous êtes redirigé vers la page de recherche.

---

## 5. Portail Employé — Gestion des clients

**URL d'accueil :** `http://localhost:8080/employee`

### 5.1 Consulter la liste des clients

**URL :** `http://localhost:8080/employee/customers`

Affiche un tableau de tous les clients enregistrés avec leurs informations complètes (NAS, nom, prénom, adresse).

Depuis ce tableau, il est possible de :
- Cliquer **"Modifier"** pour modifier un client
- Cliquer **"Supprimer"** pour supprimer un client

---

### 5.2 Ajouter un client

**URL :** `http://localhost:8080/employee/customer/add`

**Champs du formulaire :**

| Champ | Description | Contrainte |
|---|---|---|
| NAS | Numéro d'Assurance Sociale (clé unique) | 9 chiffres |
| Prénom | Prénom du client | Obligatoire |
| Nom | Nom de famille | Obligatoire |
| Numéro de rue | Partie numérique de l'adresse | Obligatoire |
| Nom de rue | Nom de la rue | Obligatoire |
| Ville | Ville de résidence | Obligatoire |
| Code postal | Code postal canadien | Format : A1A 1A1 |
| Pays | Pays de résidence | Canada / États-Unis / Mexique |

Cliquer **"Ajouter"** pour enregistrer le client.

---

### 5.3 Modifier un client

**URL :** `http://localhost:8080/employee/customer/update/{NAS}`

Le formulaire est pré-rempli avec les données actuelles du client. Le NAS ne peut pas être modifié (clé primaire).

Modifier les champs souhaités puis cliquer **"Modifier"**.

---

### 5.4 Supprimer un client

Depuis la liste des clients (`/employee/customers`), cliquer le bouton **"Supprimer"** sur la ligne du client concerné.

> **Attention :** La suppression est irréversible. Un client ayant des réservations actives ne peut pas être supprimé (contrainte de clé étrangère en base de données).

---

## 6. Portail Manager — Gestion de l'hôtel

**URL d'accueil :** `http://localhost:8080/manager`

### 6.1 Gestion des hôtels

#### Consulter la liste des hôtels
**URL :** `http://localhost:8080/hotel/hotels`

Affiche tous les hôtels enregistrés. Depuis ce tableau, il est possible d'ajouter, modifier ou supprimer un hôtel, et d'accéder aux chambres d'un hôtel.

#### Ajouter un hôtel
**URL :** `http://localhost:8080/hotel/hotel/add`

| Champ | Description | Contrainte |
|---|---|---|
| Nom | Nom de l'hôtel | Obligatoire |
| Nombre de chambres | Capacité totale de l'établissement | Nombre entier positif |
| Nombre d'étoiles | Classement (1 à 5) | Entre 1 et 5 |
| Courriel | Adresse email de contact | Format email valide |
| Numéro de rue | Partie numérique de l'adresse | Nombre entier |
| Nom de rue | Nom de la rue | Obligatoire |
| Ville | Ville de l'hôtel | Obligatoire |
| Code postal | Code postal | Format canadien A1A 1A1 |
| Pays | Pays de l'hôtel | Canada / États-Unis / Mexique |
| Chaîne hôtelière | Chaîne à laquelle appartient l'hôtel | Sélection dans la liste |
| Gérant | Employé désigné comme gérant | Sélection dans la liste |

#### Modifier un hôtel
**URL :** `http://localhost:8080/hotel/hotel/update/{id}`

Le formulaire est pré-rempli. Modifier les champs souhaités et cliquer **"Modifier"**.

#### Supprimer un hôtel
Depuis la liste des hôtels, cliquer **"Supprimer"** sur la ligne correspondante.

> **Attention :** Un hôtel ayant des chambres ou des employés associés ne peut pas être supprimé directement.

---

### 6.2 Gestion des chambres

#### Consulter les chambres d'un hôtel
**URL :** `http://localhost:8080/room/list/{idHotel}`

Affiche toutes les chambres de l'hôtel sélectionné avec leurs caractéristiques. Un sélecteur permet de changer d'hôtel.

#### Ajouter une chambre
**URL :** `http://localhost:8080/room/add`

| Champ | Description | Valeurs possibles |
|---|---|---|
| Numéro de chambre | Identifiant lisible (ex : "101") | Texte libre |
| Disponibilité | Statut de disponibilité | Disponible / Non disponible |
| Capacité | Type de chambre | Simple, Double, Triple, Quadruple, Suite, Penthouse |
| Prix | Prix par nuit (en $) | Nombre décimal (ex : 120.50) |
| Vue | Vue depuis la chambre | Mer, Montagne, Ville, Jardin, Piscine, Lac, Forêt, Rivière, Parc, Cour intérieure, Rue, Autre |
| Extensible | Lit supplémentaire possible | Oui / Non |
| Hôtel | Hôtel auquel appartient la chambre | Sélection dans la liste |

#### Modifier une chambre
**URL :** `http://localhost:8080/room/update/{id}`

Le formulaire est pré-rempli avec les données actuelles. L'hôtel associé ne peut pas être changé lors d'une modification.

#### Supprimer une chambre
Depuis la liste des chambres, cliquer **"Supprimer"** sur la ligne correspondante.

---

### 6.3 Gestion des employés

#### Consulter la liste des employés
**URL :** `http://localhost:8080/manager/employees`

Affiche tous les employés avec leurs informations (NAS, nom, rôle, hôtel d'affectation).

#### Ajouter un employé
**URL :** `http://localhost:8080/manager/employee/add`

| Champ | Description | Contrainte |
|---|---|---|
| NAS | Numéro d'Assurance Sociale | 9 chiffres (clé unique) |
| Prénom | Prénom de l'employé | Obligatoire |
| Nom | Nom de famille | Obligatoire |
| Rôle | Poste dans l'hôtel | Voir liste ci-dessous |
| Numéro de rue | Adresse | Obligatoire |
| Nom de rue | Adresse | Obligatoire |
| Ville | Ville de résidence | Obligatoire |
| Code postal | Code postal canadien | Format : A1A 1A1 |
| Pays | Pays de résidence | Canada / États-Unis / Mexique |
| Hôtel | Hôtel d'affectation | Sélection dans la liste |

**Rôles disponibles :** Manager, Réceptionniste, Femme de ménage, Maintenance, Sécurité, Cuisinier, Serveur, Concierge, Valet, Chauffeur, Thérapeute spa, Instructeur fitness, Maître nageur, Comptable, Ressources humaines, Marketing, Service client

#### Modifier un employé
**URL :** `http://localhost:8080/manager/employee/update/{NAS}`

Le NAS ne peut pas être modifié.

#### Supprimer un employé
Depuis la liste des employés, cliquer **"Supprimer"**.

> **Attention :** Un employé désigné comme gérant d'un hôtel ne peut pas être supprimé tant qu'il occupe cette fonction.

---

## 7. Gestion des réservations et check-in

### 7.1 Consulter les réservations

**URL :** `http://localhost:8080/booking/bookings`

Affiche toutes les réservations actives avec les informations du client, de la chambre et les dates prévues.

Depuis ce tableau, deux actions sont possibles :
- **"Check-in"** : transformer la réservation en location active
- **"Supprimer"** : annuler la réservation

---

### 7.2 Effectuer un check-in (transformer en location)

Depuis la liste des réservations (`/booking/bookings`), cliquer **"Check-in"** sur la réservation concernée.

**Ce que fait le check-in :**
1. Crée une nouvelle **location active** (Rental) avec les mêmes données que la réservation
2. **Supprime** la réservation originale
3. Redirige vers la liste des réservations

> La location apparaît maintenant dans la liste des locations actives (`/booking/rentals`).

---

### 7.3 Consulter les locations actives

**URL :** `http://localhost:8080/booking/rentals`

Affiche toutes les locations en cours avec les informations du client, de la chambre et les dates de séjour.

Depuis ce tableau, cliquer **"Paiement"** pour enregistrer un paiement pour une location.

---

## 8. Gestion des paiements

### 8.1 Enregistrer un paiement

**URL :** `http://localhost:8080/payment/add/{id}` (accessible depuis la liste des locations)

| Champ | Description | Valeurs / Contrainte |
|---|---|---|
| Montant | Montant du paiement en $ | Nombre décimal (ex : 840.00) |
| Date | Date du paiement | Aujourd'hui ou dans le futur |
| Méthode de paiement | Mode de règlement utilisé | Carte de crédit, Carte de débit, Espèces, Chèque, Virement bancaire |
| Statut du paiement | État du paiement | Payé, En attente, Remboursé |

Cliquer **"Enregistrer"** pour sauvegarder le paiement.

---

### 8.2 Consulter l'historique des paiements

**URL :** `http://localhost:8080/payment/payments`

Affiche l'ensemble des paiements enregistrés avec le montant, la date, la méthode et le statut.

---

## 9. Référence des URLs

### Portail Client
| Action | URL |
|---|---|
| Rechercher une chambre | `GET /room/search` |
| Lancer la recherche | `POST /room/search` |
| Sélectionner une chambre | `GET /room/select/{id}` |
| Formulaire de réservation | `GET /room/book/{id}` |
| Confirmer la réservation | `POST /room/booking` |

### Portail Employé
| Action | URL |
|---|---|
| Accueil employé | `GET /employee` |
| Liste des clients | `GET /employee/customers` |
| Formulaire ajout client | `GET /employee/customer/add` |
| Ajouter un client | `POST /employee/customer/add` |
| Formulaire modification client | `GET /employee/customer/update/{NAS}` |
| Modifier un client | `POST /employee/customer/update/{NAS}` |
| Supprimer un client | `POST /employee/customer/delete/{NAS}` |

### Portail Manager — Hôtels
| Action | URL |
|---|---|
| Accueil manager | `GET /manager` |
| Liste des hôtels | `GET /hotel/hotels` |
| Formulaire ajout hôtel | `GET /hotel/hotel/add` |
| Ajouter un hôtel | `POST /hotel/hotel/add` |
| Formulaire modification hôtel | `GET /hotel/hotel/update/{id}` |
| Modifier un hôtel | `POST /hotel/hotel/update/{id}` |
| Supprimer un hôtel | `POST /hotel/hotel/delete/{id}` |

### Portail Manager — Chambres
| Action | URL |
|---|---|
| Liste des chambres d'un hôtel | `GET /room/list/{idHotel}` |
| Formulaire ajout chambre | `GET /room/add` |
| Ajouter une chambre | `POST /room/add` |
| Formulaire modification chambre | `GET /room/update/{id}` |
| Modifier une chambre | `POST /room/update/{id}` |
| Supprimer une chambre | `POST /room/delete/{id}` |

### Portail Manager — Employés
| Action | URL |
|---|---|
| Liste des employés | `GET /manager/employees` |
| Formulaire ajout employé | `GET /manager/employee/add` |
| Ajouter un employé | `POST /manager/employee/add` |
| Formulaire modification employé | `GET /manager/employee/update/{NAS}` |
| Modifier un employé | `POST /manager/employee/update/{NAS}` |
| Supprimer un employé | `POST /manager/employee/delete/{NAS}` |

### Réservations, Locations, Paiements
| Action | URL |
|---|---|
| Liste des réservations | `GET /booking/bookings` |
| Effectuer un check-in | `POST /booking/transform/{id}` |
| Liste des locations actives | `GET /booking/rentals` |
| Formulaire paiement | `GET /payment/add/{id}` |
| Enregistrer un paiement | `POST /payment/add/{id}` |
| Historique des paiements | `GET /payment/payments` |

---

## 10. Messages d'erreur courants

### Erreurs de formulaire (affichées en rouge sous le champ)

| Message | Cause | Solution |
|---|---|---|
| "Le champ est obligatoire" | Champ laissé vide | Remplir le champ manquant |
| "Format de NAS invalide" | NAS ne contient pas 9 chiffres | Saisir exactement 9 chiffres (ex : 123456789) |
| "Format de code postal invalide" | Code postal ne respecte pas A1A 1A1 | Respecter le format canadien (lettre-chiffre-lettre espace chiffre-lettre-chiffre) |
| "Format d'email invalide" | Email mal formaté | Saisir une adresse email valide (ex : hotel@exemple.ca) |
| "Nombre d'étoiles invalide" | Valeur hors de 1 à 5 | Saisir un nombre entre 1 et 5 |
| "La date de fin doit être après la date de début" | Date de fin antérieure à la date de début | Corriger les dates de séjour |
| "La date doit être aujourd'hui ou dans le futur" | Date passée saisie | Saisir une date présente ou future |

### Erreurs d'infrastructure

| Erreur | Cause probable | Solution |
|---|---|---|
| Page blanche ou erreur 500 | Base de données non démarrée | Démarrer PostgreSQL et vérifier la connexion |
| "password authentication failed" | Mauvais mot de passe PostgreSQL | Corriger `spring.datasource.password` dans `application.properties` |
| Erreur 404 | URL incorrecte | Vérifier l'URL dans la section "Référence des URLs" |
| Données non affichées | Tables vides en base | Exécuter le fichier `schema.sql` pour initialiser les données |
