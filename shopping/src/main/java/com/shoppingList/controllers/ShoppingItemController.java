package com.shoppingList.controllers;

import java.util.List;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingList.entities.ShoppingItem;
import com.shoppingList.exceptions.ResourceNotFoundException;
import com.shoppingList.repositories.ShoppingItemRepository;

@RestController
@RequestMapping("/api/shopping")
public class ShoppingItemController {

	@Autowired
	private ShoppingItemRepository shoppingItemRepository;

	@PreAuthorize("hasRole('NORMAL')")
	@GetMapping
	public List<ShoppingItem> getAllItems() {
		return shoppingItemRepository.findAll();
	}

	@PreAuthorize("hasRole('NORMAL')")
	@GetMapping("/{id}")
	public ShoppingItem getItemById(@PathVariable Long id) {
		return shoppingItemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ShoppingItem addItem(@RequestBody ShoppingItem item) {
		return shoppingItemRepository.save(item);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ShoppingItem updateItem(@PathVariable Long id, @RequestBody ShoppingItem updatedItem) {
		ShoppingItem existingItem = shoppingItemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));

		existingItem.setName(updatedItem.getName());
		existingItem.setQuantity(updatedItem.getQuantity());

		return shoppingItemRepository.save(existingItem);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public void deleteItem(@PathVariable Long id) {
		ShoppingItem itemToDelete = shoppingItemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
		shoppingItemRepository.delete(itemToDelete);
	}
}