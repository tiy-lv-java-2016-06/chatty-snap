package com.theironyard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by EddyJ on 7/31/16.
 */
@ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE, reason = "File not an image")
public class FileNotAnImage extends RuntimeException {
}
