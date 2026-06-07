# Backend PHP - API MySQL

## Lancer

a) Avec serveur intégré PHP (recommandé)
```bash
cd backend-php
php -S 127.0.0.1:8000 -t .
```

Puis appeler :
- `http://127.0.0.1:8000/api/index.php/tasks?user_id=1`

b) Alternative : Apache/Nginx
- Assurez-vous que le document root pointe vers `backend-php/`
- Les routes sont dans `api/index.php`.

## Variables d'environnement
Le fichier `config/db.php` utilise :
- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASS`

Exemple PowerShell/Windows CMD :
```bat
set DB_NAME=assistant_vocal
set DB_USER=root
set DB_PASS=...
php -S 127.0.0.1:8000 -t .
```

## Endpoints (JSON)
- `GET    /api/tasks?user_id=...`
- `POST   /api/tasks` (body JSON)
- `PATCH  /api/tasks/{id}`
- `DELETE /api/tasks/{id}`

- `GET    /api/reminders?user_id=...`
- `POST   /api/reminders`
- `PATCH  /api/reminders/{id}`
- `DELETE /api/reminders/{id}`

- `POST   /api/goals`
- `GET    /api/goals?user_id=...`

- `GET    /api/work_sessions?user_id=...`

- `GET    /api/dashboard?user_id=...`

