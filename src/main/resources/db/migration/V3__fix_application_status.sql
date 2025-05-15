-- Modification de la colonne status pour accepter les valeurs de l'énumération
ALTER TABLE applications
MODIFY COLUMN status VARCHAR(20) NOT NULL DEFAULT 'EN_ATTENTE'; 