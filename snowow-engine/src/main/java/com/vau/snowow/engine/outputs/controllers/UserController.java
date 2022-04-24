package com.vau.snowow.engine.outputs.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import com.vau.snowow.engine.outputs.models.*;
@RestController() @RequestMapping(value="/user") public class UserController {	@GetMapping(produces="application/json", value="/info") public User getUserInfo() { 

}}