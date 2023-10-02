package com.ishravlabs.ratelimitsb.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class DemoController {

    @GetMapping("/")
    public String home(){
        return ("<h1>welcome</h1>");
    }

    @GetMapping("/user")
    public String user(){
        return ("<h1>welcome user</h1>");
    }

    @GetMapping("/admin")
    public String admin(){
        return ("<h1>welcome admin</h1>");
    }


    @GetMapping("/set-cookie")
    public String setCookie(HttpServletResponse response) {
        // Create a new cookie
        Cookie cookie = new Cookie("myCookie", "cookieValue");

        // Set the HttpOnly flag to make the cookie HttpOnly
        cookie.setHttpOnly(true);

        // Add the cookie to the response
        response.addCookie(cookie);

        return "cookie-set-success";
    }
}
