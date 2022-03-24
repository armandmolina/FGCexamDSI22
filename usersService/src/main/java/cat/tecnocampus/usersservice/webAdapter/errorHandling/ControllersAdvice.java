package cat.tecnocampus.usersservice.webAdapter.errorHandling;

import cat.tecnocampus.usersservice.application.appController.exception.UserDoesNotExistException;
import cat.tecnocampus.usersservice.application.appController.exception.UserServiceBadLuchkException;
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

    @ExceptionHandler(UserServiceBadLuchkException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleNotFoundInDB (HttpServletRequest request, Exception ex) {
        String url = request.getRequestURL().toString();
        logger.error("Request: " + url + " raised " + ex);
        return ex.getMessage();
    }
}
