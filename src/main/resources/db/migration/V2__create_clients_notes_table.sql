CREATE TABLE IF NOT EXISTS client_notes (
    id INT PRIMARY KEY,
    client_id INT NOT NULL,
    note_text VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_client_notes_client
    FOREIGN KEY (client_id)
    REFERENCES clients(id)
    ON DELETE CASCADE
    );