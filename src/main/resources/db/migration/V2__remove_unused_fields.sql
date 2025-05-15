-- Suppression des colonnes inutilisées
ALTER TABLE tasks
DROP COLUMN IF EXISTS created_at,
DROP COLUMN IF EXISTS due_date,
DROP COLUMN IF EXISTS creator_id;

-- Ajout d'une valeur par défaut pour date_posted si elle n'en a pas
ALTER TABLE tasks
ALTER COLUMN date_posted SET DEFAULT CURRENT_TIMESTAMP; 