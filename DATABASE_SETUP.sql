-- PetCareBook database setup for MySQL
CREATE DATABASE IF NOT EXISTS petcarebook;
USE petcarebook;

-- The application uses spring.jpa.hibernate.ddl-auto=update,
-- so the four tables are created automatically on first run.
-- You normally only need to create the database and configure credentials.
