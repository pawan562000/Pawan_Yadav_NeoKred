package com.shoppingList.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppingList.entities.ShoppingItem;

@Repository
public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long> {
}
