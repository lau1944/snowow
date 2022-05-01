package com.vau.app.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.HashMap;
import com.vau.app.service.*;
import com.vau.app.models.*;
@RestController() @RequestMapping(value="/bread") public class BreadV1Controller {	@Autowired() private IRemoteService service;	@GetMapping(produces="application/json", value="/info") public Object getBread(	@RequestHeader() Map<String, Object> headers, 	@RequestParam() Map<String, Object> params){ 
return service.GET("https://api.thecatapi.com/v1/images/search", new HashMap<>() {{ put("breed_id", "beng"); }}, new HashMap<>() {{ put("x-api-key", "your-api-key"); }});
}}