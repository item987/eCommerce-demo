package io.item987.ecommerce.api;

import io.item987.ecommerce.api.dto.ErrorResponse;
import io.item987.ecommerce.order.OrderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(APIExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleError(Exception exception) {
        logger.error("Server error", exception);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server error (see server log for details)");
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResponse> handleOrderError(OrderException exception) {
        logger.debug("Order error", exception);
        return createErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    private static ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus httpStatus, String errorMessage) {
        return ResponseEntity.status(httpStatus).headers(new HttpHeaders()).body(new ErrorResponse(errorMessage));
    }

}
