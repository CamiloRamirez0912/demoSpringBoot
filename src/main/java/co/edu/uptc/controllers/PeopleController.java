package co.edu.uptc.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.edu.uptc.models.PersonModel;
import co.edu.uptc.services.PeopleManagerService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @PostMapping("/add")
  public PersonModel addPerson(@RequestBody PersonModel person) {
    try {
      peopleManagerService.addPerson(person);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return person;
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<PersonModel> getPersonById(@PathVariable Long id) {
    try {
      PersonModel person = peopleManagerService.getPersonById(id);
      if (person != null) {
        return ResponseEntity.ok(person);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(500).build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<PersonModel> deletePerson(@PathVariable Long id) {
    try {
      PersonModel deletedPerson = peopleManagerService.deletePersonById(id);
      if (deletedPerson != null) {
        return ResponseEntity.ok(deletedPerson);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(500).build();
    }
  }

}