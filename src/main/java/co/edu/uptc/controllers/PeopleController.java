package co.edu.uptc.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.edu.uptc.models.PersonModel;
import co.edu.uptc.services.PeopleManagerService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/prog2/202214307/people")
public class PeopleController {

  @Autowired
  PeopleManagerService peopleManagerService;

  @GetMapping("/all")
  public ResponseEntity<String> getFileJsonPersons() {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(peopleManagerService.getFilePersons());
  }

  /*@GetMapping("/id/{id}")
  public PersonDto getById(@PathVariable Long id) {
    try {
      return PersonDto.toPersonDto(peopleManagerService.getPersonById(id));
    } catch (IOException e) {
      System.out.println(e.getMessage());
      return null;
    }
  }*/

  @PostMapping("/add")
  public PersonModel addPerson(@RequestBody PersonModel person) {
    try {
      peopleManagerService.addPerson(person);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return person;
  }

  /*@DeleteMapping("/{id}")
  public PersonModel deletePerson(@PathVariable Long id) {
    PersonModel personDeleted = null;
    try {
      personDeleted = peopleManagerService.deletePerson(id);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return personDeleted;
  }*/

}