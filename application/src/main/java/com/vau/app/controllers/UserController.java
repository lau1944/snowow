package com.vau.app.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.;
import org.springframework.http.MediaType;
@RestController() @RequestMapping(value="/user") public class UserController {	@GetMapping(produces=MediaType.APPLICATION_JSON_VALUE, value="/info") public User getUserInfo() { 

}}