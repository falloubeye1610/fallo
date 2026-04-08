package sn.ucad.restou.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Erreur de validation (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex, WebRequest request) {

            List<ErrorResponse.FieldError> details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorResponse.FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
            ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), 
                "Bad Request",
                "Erreur de validation des données",
                request.getDescription(false).replace("uri = ", ""),
                details
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Ressource non trouvée (404 Not Found)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
        ResourceNotFoundException ex, WebRequest request) {

            ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(), 
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri = ", "")
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // Violation de contrainte d'intégrité (409 Conflict)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
        DataIntegrityViolationException ex, WebRequest request) {

            String message = "Une contrainte d'intégrité a été violée. ";
            if(ex.getMessage() != null){
                if(ex.getMessage().contains("EMAIL")){
                    message = "Cet email est déjà utilisé par un autre étudiant.";
                }
                else if(ex.getMessage().contains("NUMERO_CARTE")){
                    message = "Ce numéro de carte est déjà utilisé par un autre étudiant.";
                }
                else if(ex.getMessage().contains("CODE_TICKET")){
                    message = "Ce code de ticket est déjà utilisé.";
                }
                else if(ex.getMessage().contains("FK_ETUDIANT")){
                    message = "L'étudiant associé au ticket n'existe pas.";
                }
            }
            ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(), 
                "Conflict",
                message,
                request.getDescription(false).replace("uri = ", "")
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }   

    // Erreur générique (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
        Exception ex, WebRequest request) {

            ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "Internal Server Error",
                "Une erreur inattendue est survenue",
                request.getDescription(false).replace("uri = ", "")
            );
            ex.printStackTrace(); // Log de l'exception pour le débogage
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(response);
    }

}


