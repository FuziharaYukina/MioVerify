package org.miowing.mioverify.controller;

import org.miowing.mioverify.exception.*;
import org.miowing.mioverify.pojo.response.ErrResp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionProcessor {
    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<?> handleLoginFailed() {
        return new ResponseEntity<>(
                new ErrResp().setError(ErrType.FORBIDDEN_OP.v())
                        .setErrorMessage("Invalid credentials. Invalid username or password."),
                HttpStatus.FORBIDDEN
        );
    }
    @ExceptionHandler(NoProfileException.class)
    public ResponseEntity<?> handleNoProfile() {
        return new ResponseEntity<>(
                new ErrResp().setError(ErrType.FORBIDDEN_OP.v())
                        .setErrorMessage("No available profile."),
                HttpStatus.FORBIDDEN
        );
    }
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidToken() {
        return new ResponseEntity<>(
                new ErrResp().setError(ErrType.FORBIDDEN_OP.v())
                        .setErrorMessage("Invalid token."),
                HttpStatus.FORBIDDEN
        );
    }
    @ExceptionHandler({ProfileNotFoundException.class, UserMismatchException.class, InvalidSessionException.class})
    public ResponseEntity<?> handleNoContent() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorized() {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(TextureNotFoundException.class)
    public ResponseEntity<?> handleNotFound() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(
            {
                    AttackDefenseException.class,
                    ForbiddenUploadException.class,
                    ProfileMismatchException.class,
                    TextureDeleteException.class,
                    FeatureNotSupportedException.class,
                    DuplicateUserNameException.class,
                    DuplicateProfileNameException.class
            }
    )
    public ResponseEntity<?> handleForbidden() {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}