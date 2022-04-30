package com.vau.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.util.Map;
import java.util.HashMap;

import com.vau.app.models.*;

@RestController()
@RequestMapping(value = "/user")
public class UserV1Controller {
    @GetMapping(produces = "application/json", value = "/info")
    public Map<String, Object> getUserInfo(@RequestHeader() Map<String, Object> headers, @RequestParam() Map<String, Object> params) {
        return new HashMap<>() {{
            put("name", "101");
            put("age", 22);
            put("school", new Object[]{new HashMap<>() {{
                put("name", params.get("school"));
                put("age", 4);
            }}, new HashMap<>() {{
                put("name", "Cornell University");
                put("age", 2);
            }},});
        }};
    }
}