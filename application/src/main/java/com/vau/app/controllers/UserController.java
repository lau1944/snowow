package com.vau.app.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.HashMap;
import com.vau.app.models.*;
@RestController() @RequestMapping(value="/user") public class UserController {	@GetMapping(produces="application/json", value="/info") public Map<String, Object> getUserInfo() { 
return new HashMap<>(){{put("name","Jimmy");put("age",22);put("school",new Object[]{new HashMap<>(){{put("name","New York University");put("age",4);}},new HashMap<>(){{put("name","Cornell University");put("age",2);}},});}};
}}