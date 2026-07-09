package main;
import java.time.LocalDateTime;

public class ClientNote {

    private int id;
    private int clientId;
    private String noteText;
    private LocalDateTime createdAt;

    public ClientNote(ClientNoteBuilder builder) {
        this.id = builder.id;
        this.clientId = builder.clientId;
        this.noteText = builder.noteText;
        this.createdAt = builder.createdAt;
    }

    public int getId() {
        return id;
    }

    public int getClientId() {
        return clientId;
    }

    public String getNoteText() {
        return noteText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String toString() {
        return "ClientNote{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", noteText='" + noteText + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public static class ClientNoteBuilder {
        private int id;
        private int clientId;
        private String noteText;
        private LocalDateTime createdAt;

        public ClientNoteBuilder SId(int id) {
            this.id = id;
            return this;
        }

        public ClientNoteBuilder SClientId(int clientId) {
            this.clientId = clientId;
            return this;
        }

        public ClientNoteBuilder SNoteText(String noteText) {
            this.noteText = noteText;
            return this;
        }

        public ClientNoteBuilder SCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ClientNote build() {
            return new ClientNote(this);
        }
    }
}
