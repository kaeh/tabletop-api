package com.tabletop.endpoint;

import com.tabletop.errors.CharacterIdMismatchException;
import com.tabletop.errors.CharacterNotFoundException;
import com.tabletop.persistence.repository.CharacterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import com.tabletop.persistence.model.TTCharacter;

@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    private final CharacterRepository characterRepository;

    public CharacterController(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @GetMapping
    public Iterable<TTCharacter> findAll(@RequestParam(required = false) String name) {
        if (name != null) {
            return characterRepository.findByName(name);
        }

        return characterRepository.findAll();
    }

    @GetMapping("/{id}")
    public TTCharacter findOne(@PathVariable Long id) {
        return characterRepository.findById(id)
            .orElseThrow(CharacterNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TTCharacter create(@RequestBody TTCharacter TTCharacter) {
        return characterRepository.save(TTCharacter);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        characterRepository.findById(id)
            .orElseThrow(CharacterNotFoundException::new);

        characterRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public TTCharacter update(@PathVariable Long id, @RequestBody TTCharacter TTCharacter) {
        if (!TTCharacter.getId().equals(id)) {
            throw new CharacterIdMismatchException();
        }

        characterRepository.findById(id)
            .orElseThrow(CharacterNotFoundException::new);

        return characterRepository.save(TTCharacter);
    }
}
