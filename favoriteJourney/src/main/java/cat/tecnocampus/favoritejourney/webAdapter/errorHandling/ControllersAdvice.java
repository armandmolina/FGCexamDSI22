package cat.tecnocampus.favoritejourney.webAdapter.errorHandling;

import cat.tecnocampus.favoritejourney.application.appUseCases.exceptions.UserDoesNotExistException;
import cat.tecnocampus.favoritejourney.domain.exceptions.SameOriginDestinationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllersAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ControllersAdvice.class);

    @ExceptionHandler(UserDoesNotExistException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserDoesNotExist(HttpServletRequest request, Exception exception) {
        String url = request.getRequestURL().toString();
        logger.error("Request: " + url + " raised " + exception);
        return exception.getMessage();
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundInDB (HttpServletRequest request, Exception ex) {
        String url = request.getRequestURL().toString();
        logger.error("Request: " + url + " raised " + ex);
        return "Resource not found";
    }

    @ExceptionHandler(SameOriginDestinationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleSameOriginDestination(HttpServletRequest request) {
        return "Origin and destination must be different";
    }
}
