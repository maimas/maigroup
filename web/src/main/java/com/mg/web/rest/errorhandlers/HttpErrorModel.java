
package com.mg.web.rest.errorhandlers;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class HttpErrorModel {

    private HashMap<String, Object> map = new HashMap<>();
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    HttpErrorModel(HttpServletRequest request, Exception e) {
        initError(request, e);
    }

    private Map<String, Object> initError(HttpServletRequest request, Exception e) {
        map.put("path", request.getServletPath());
        map.put("timestamp", new Date(System.currentTimeMillis()));

        setMessage(e.getLocalizedMessage());
        setStatus(status);
        return map;
    }

    void setStatus(HttpStatus status) {
        this.status = status;
        map.put("status", status.value());
        map.put("error", status.getReasonPhrase());
    }

    HttpStatus getStatus() {
        return status;
    }

    void setMessage(String message) {
        map.put("message", message);
    }

    void setErrorDetails(String msg) {
        map.put("errorDetails", msg);
    }

    HashMap<String, Object> getMapped() {
        return map;
    }
}
