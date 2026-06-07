# TODO - Assistant Intelligent Personnel

## Etape 1 — Préparation
- [x] Créer `sql/schema.sql` (MySQL : users, reminders, tasks, work_sessions, goals, command_history, indices)

## Etape 2 — Backend PHP (API CRUD MySQL)
- [x] Créer dossier `backend-php/`
- [x] Ajouter `config/db.php` (PDO + env)
- [x] Ajouter routes API : tasks, reminders, goals, work_sessions, dashboard
- [ ] Ajouter auth simple (JWT ou token) (si requis)
- [x] Ajouter `backend-php/README.md`

## Etape 3 — Backend Python (FastAPI : voix + actions + tracking + stats)
- [ ] Créer dossier `backend-python/`
- [ ] Ajouter `requirements.txt`
- [ ] Implémenter TTS (pyttsx3) + STT (SpeechRecognition)
- [ ] Implémenter Command Router (regex + mapping actions)
- [ ] Implémenter actions : ouvrir navigateur + recherche Google, ouvrir/fermer apps, musique (simple)
- [ ] Implémenter tracker psutil (Windows) + persistance dans MySQL
- [ ] Implémenter endpoints FastAPI : `/api/voice/command`, `/api/actions/...`, `/api/track/...`, `/api/stats/dashboard`
- [ ] Ajouter `backend-python/README.md`

## Etape 4 — Frontend web (HTML/CSS/JS moderne)
- [ ] Créer dossier `frontend/`
- [ ] UI dashboard + tâches + rappels + statistiques + historique commandes
- [ ] Microphone bouton + affichage transcript + génération réponse
- [ ] Graphiques (Chart.js)
- [ ] Connexions aux API (fetch)

## Etape 5 — Guide d’installation & run
- [ ] Créer `docs/INSTALL.md` (étapes complètes)
- [ ] Ajouter scripts `scripts/start_all.bat`
- [ ] Lancer smoke test : schéma MySQL + endpoints + UI

## Etape 6 — Qualité
- [ ] Vérifier conventions de nommage + validations de champs
- [ ] Ajouter logs et gestion erreurs

