package sn.ucad.restou.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List <FieldError> details;

    public ErrorResponse(int status , String error , String message , String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ErrorResponse(int status , String error , String messages , String path, List<FieldError> details) {
        this(status, error, messages, path);
        this.details = details;
    }

    // Classes interneres pour les erreurs de champ
    
    public static class FieldError {
        private String champ;
        private String message;

        public FieldError(String champ, String message) {
            this.champ = champ;
            this.message = message;
        }

        public String getChamp() {
            return champ;
        }

        public String getMessage() {
            return message;
        }
    }

    // Getters et setters

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public List<FieldError> getDetails() {
        return details;
    }  
}
