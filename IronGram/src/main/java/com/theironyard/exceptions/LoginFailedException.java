package com.theironyard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by jeff on 7/28/16.
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Login Failed")
public class LoginFailedException extends RuntimeException {
}
