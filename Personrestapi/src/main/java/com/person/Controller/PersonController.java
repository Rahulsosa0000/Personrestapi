package com.person.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.person.entity.Person;
import com.person.repository.PersonRepository;

@RestController
public class PersonController {
	
	@Autowired
	private PersonRepository personRepository;

	// Get all persons
	// URL: http://localhost:8080/persons
	@GetMapping("/persons")
	public List<Person> getAllPersons() {
		return personRepository.findAll();
	}
	
	// Get a person by ID
	// URL: http://localhost:8080/persons/{id}
	@GetMapping("/persons/{id}")
	public ResponseEntity<Person> getPerson(@PathVariable Long id) {
		Optional<Person> person = personRepository.findById(id);
		return person.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	// Add a new person
	// URL: http://localhost:8080/persons/add
	@PostMapping("/persons/add")
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {
		Person newPerson = personRepository.save(person);
		return ResponseEntity.status(HttpStatus.CREATED).body(newPerson);
	}
	
	// Update a person by ID
	// URL: http://localhost:8080/persons/update/{id}
	@PutMapping("/persons/update/{id}")
	public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person updatedPerson) {
		Optional<Person> optionalPerson = personRepository.findById(id);
		if (optionalPerson.isPresent()) {
			Person person = optionalPerson.get();
			person.setName(updatedPerson.getName());
			person.setEmail(updatedPerson.getEmail());
			personRepository.save(person);
			return ResponseEntity.ok(person);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	// Delete a person by ID
	// URL: http://localhost:8080/persons/delete/{id}
	@DeleteMapping("/persons/delete/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removePerson(@PathVariable Long id) {
		personRepository.deleteById(id);
	}
}
