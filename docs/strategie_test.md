# stratégie de test - Projet AutomationExercise

## 1. Objectif du document
Ce document décrit la stratégie de test mise en place sur le projet AutomationExercise.
Il a pour objectif de cadrer la démarche qualité globale ; types de tests, niveaux de tests, priorisation, risques et stratégie d'automatisation.
Ce document s'inscrit dans une logique **ISTQB** et sert de référence pour : 
- la conception des tests
- l'exécution manuelle
- l'automatisation


## 2. Contexte du projet 
AutomationExercise est un site e-commerce de démonstration utilisé comme support de portfolio QA.

Le projet vise à démontrer : 
- une démarche de test structurée (EX -> CT -> exécutions -> bugs -> traçabilité)
- une automatisation ciblée et pertinente (UI + API)
- un socle technique propre et maintenable (POM, waits/wrappers stabilité, CI)

## 3. Périmètre de test
### 3.1 Périmètre couvert
**Interface Web (UI)** :
  - authentification / compte utilisateur
  - catalogue produits
  - panier
  - checkout / paiement
  - formulaire de contact

**API publiques** : 
  - consultation produits / marques
  - recherche
  - authentification
  - CRUD utilisateur (create, read, update, delete)


### 3.2 Hors périmètre
- tests de performance (site tiers de démonstration)
- tests de sécurité approfondis
- tests de compatibilité navigateurs étendues
- tests d'accessibilité avancés

Ces aspects pourraient être traités sur une application interne ou un environnement dédié, hors site tiers.

## 4. Niveaux et types de test

### 4.1 Niveaux de test
- **Tests système (UI Web)**
- **Tests système (API)** : aussi appelé tests de service, exécutés sur un environnement complet.

Chaque niveau est traité indépendamment mais de manière cohérente au sein de la stratégie globale.


### 4.2 Types de tests 
- Tests fonctionnels (positifs / négatifs)
- Tests exploratoires (documentés via chartes)
- Tests automatisés (smoke et régression ciblée)

## 5. Priorisation et approche basée sur le risque

Les cas de test sont priorisés selon leur **impact métier** : 
- **Priorité Haute (H)** : parcours critiques positifs (ex : login OK, ajout panier, checkout, paiement)
- **Priorité Moyenne (M)** : fonctionnalités importantes mais non bloquantes (ex : login KO, validations, erreurs utilisateur)
- **Priorité Basse (B)** : cas limites ou secondaires


Cette priorisation guide : 
- l'ordre d'exécution manuelle
- le choix des tests automatisés
- la définition du smoke test

## 6. Tests manuels (réalisés)
Les tests manuels ont été réalisés en amont de l'automatisation :
- exigences fonctionnelles formalisées
- cas de test détaillés
- exécution documentée dans un journal de test
- anomalies tracées dans un rapport de bugs
- traçabilité assurée via une RTM

Les documents produits constituent la base de référence du projet.

## 7. Tests exploratoires
Les tests exploratoires sont cadrés à l'aide de **chartes exploratoires**. 

Ils visent à :
- explorer les zones à risque
- détecter des comportements inattendus
- compléter les tests scriptés

Les chartes sont documentées dans le fichier charters_exploratoires.md.

## 8. Stratégie d'automatisation

### 8.1 Objectifs
- Automatiser les **parcours critiques**
- Obtenir un feedback rapide sur la stabilité de l'application
- Fournir des preuves exploitables en CI

### 8.2 Smoke tests automatisés
Le smoke automatisé est basé sur un sous-ensemble de cas de test prioritaires (H) afin de fournir un feedback rapide.
Il a pour objectif de vérifier que l'application est :
- accessible
- utilisable sur ses fonctionnalités vitales

Il inclut : 
- **UI** :
  - Connexion utilisateur
  - Ajout au panier
  - Accès au formulaire de paiement
- **API** : Récupération de la liste des produits

Les tests smoke sont courts, stables et exécutables en CI.

Si le smoke échoue, la campagne de tests s'arrête (go/no-go).

Note : - Le smoke automatisé est composé de cas de test prioritaires (H) les plus stables et représentatifs du parcours critique.
       - Le paiement complet (EX-20) est exclu du smoke pour stabilité et couvert en régression.

### 8.3 Régression automatisée
Une régression automatisée ciblée est  effectuée :
- couverture élargie
- scénarios positifs et négatifs
- maintien de la traçabilité avec les CT-ID

La régression automatisée ciblée a été effectuée sur : 
- **UI** : couverture critique (H) + validations clés
- **API** : en cours


### 8.4 Critères de succès
La stratégie de test est considérée comme efficace lorsque : 
- le smoke automatisé (UI et API) est vert en exécution locale et en CI,
- les tests smoke sont stables (absence de flaky identifié),
- les preuves d'exécution sont disponibles (logs, captures, reporting),
- la traçabilité entre exigences, cas de test et tests automatisés est maintenue.

## 9. Environnement et exécution
### 9.1 Exécution locale
- Environnement : Windows
- Outils : Maven, Java, navigateur Chrome
- Paramétrage via variables d'environnement (`TEST_USER_EMAIL`, `TEST_USER_PASSWORD`, `BASE_URL`, `API_BASE_URL`, `HEADLESS`)

### 9.2 Intégration continue (CI)
- GitHub Actions
- `api-smoke` puis `ui-smoke`
- UI en **headless**

## 10. Gestion des données de test
- compte de test fixe pour les tests necéssitant un login (variables d'environnement)
- test création/compte : données uniques + nettoyage (delete account)
- Tests smoke conçus pour être **idempotents**
- Pas de dépendance forte entre les tests

## 11. Risques identifiés et mesures associées
|Risque | Description | Mesure | 
|------ | ----------- | ------- | 
|R1 | Différences local / CI | Versions figées, exécution headless |
|R2 | Instabilité du site tiers | Tests courts, ciblés |
|R3 | Overlays / pubs (google vignette..) | Contournement via wrappers click, retries, clean URL |
|R4 | Tests flaky | Attentes explicites, retry, assertLoaded|   
|R5 | Données persistantes | création unique + cleanup systématique | 

## 12. Traçabilité
La traçabilité est assurée entre exigences, cas de test, exécutions et anomalies.
À l'étape d'automatisation, les tests automatisés reprennent les CT-ID afin de maintenir ce lien.



## 13. Évolution du projet
Le projet est évolutif et pourra intégrer ultérieurement : 
- Une couverture automatisée plus large
- du reporting enrichi
- d'autres types de tests (accessibilité, performance, sécurité)

## 14. Glossaire
- **CI (Continuous Integration)** : exécuter automatiquement les tests à chaque push (GitHub Actions) pour un feedback rapide.
- **Smoke tests** : tests vitaux, très courts. S’ils échouent, on arrête.
- **Headless** : navigateur sans interface (plus rapide/stable en CI).
- **Idempotent** : rejouer le test ne change pas l’état (ex. GET, lecture de titre).
- **Flaky** : test instable (passe/échoue sans changement de code). On limite ça avec des smokes simples, headless et une config propre.
- **Mock** : faux service/API (ex. WireMock) pour stabiliser ou simuler des erreurs (utilisé plus tard).



