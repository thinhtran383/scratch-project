package com.example.foodordering.repositories;

import com.example.foodordering.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuItemsRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findMenuItemByName(String name);

    @Query("Select m.imageUrl from MenuItem m where m.id = :id")
    String findAllImageUrl(Long id);
}
