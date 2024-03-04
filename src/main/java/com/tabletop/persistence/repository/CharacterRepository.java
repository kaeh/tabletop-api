package com.tabletop.persistence.repository;

import com.tabletop.persistence.model.TTCharacter;
import org.springframework.data.repository.CrudRepository;

public interface CharacterRepository extends CrudRepository<TTCharacter, Long> {
    Iterable<TTCharacter> findByName(String name);
}
