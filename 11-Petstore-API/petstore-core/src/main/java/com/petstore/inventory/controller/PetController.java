package com.petstore.inventory.controller;

import com.petstore.dto.PetDTO;
import com.petstore.exception.InvalidPetException;
import com.petstore.exception.PetNotFoundException;
import com.petstore.inventory.service.PetInventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing pets in the petstore inventory system.
 * Provides endpoints for CRUD operations and inventory management of pets.
 */
@RestController
@RequestMapping("/api/pets")
@Tag(name = "Pet Management", description = "API endpoints for managing pets in the petstore inventory")
public class PetController {

    private final PetInventoryService petInventoryService;

    /**
     * Constructs a PetController with the specified PetInventoryService.
     *
     * @param petInventoryService the service for pet inventory operations
     */
    public PetController(PetInventoryService petInventoryService) {
        this.petInventoryService = petInventoryService;
    }

    /**
     * Retrieves all pets in the inventory.
     *
     * @return ResponseEntity containing a list of all pets with HTTP status 200 (OK)
     */
    @GetMapping
    @Operation(summary = "Get all pets", description = "Retrieves a list of all pets currently in the inventory")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all pets",
            content = @Content(schema = @Schema(type = "array", implementation = PetDTO.class)))
    public ResponseEntity<List<PetDTO>> getAllPets() {
        List<PetDTO> pets = petInventoryService.getAllPets();
        return ResponseEntity.ok(pets);
    }

    /**
     * Retrieves a specific pet by its unique identifier.
     *
     * @param id the ID of the pet to retrieve
     * @return ResponseEntity containing the requested pet with HTTP status 200 (OK)
     * @throws PetNotFoundException if the pet with the specified ID is not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get pet by ID", description = "Retrieves a specific pet by its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the pet",
                    content = @Content(schema = @Schema(implementation = PetDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    public ResponseEntity<PetDTO> getPetById(
            @Parameter(description = "The ID of the pet to retrieve") @PathVariable Long id) {
        try {
            PetDTO pet = petInventoryService.getPetById(id);
            return ResponseEntity.ok(pet);
        } catch (PetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new pet in the inventory.
     * Validates the pet data before creation.
     *
     * @param petDTO the pet data transfer object containing the pet details
     * @return ResponseEntity containing the created pet with HTTP status 201 (Created)
     * @throws InvalidPetException if the pet data is invalid
     */
    @PostMapping
    @Operation(summary = "Create a new pet", description = "Creates a new pet in the inventory")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pet created successfully",
                    content = @Content(schema = @Schema(implementation = PetDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pet data")
    })
    public ResponseEntity<PetDTO> createPet(
            @Valid @RequestBody
            @Parameter(description = "The pet details") PetDTO petDTO) {
        try {
            PetDTO createdPet = petInventoryService.createPet(petDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
        } catch (InvalidPetException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Updates an existing pet in the inventory.
     * Validates the updated pet data before saving.
     *
     * @param id the ID of the pet to update
     * @param petDTO the updated pet data transfer object
     * @return ResponseEntity containing the updated pet with HTTP status 200 (OK)
     * @throws PetNotFoundException if the pet with the specified ID is not found
     * @throws InvalidPetException if the updated pet data is invalid
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a pet", description = "Updates an existing pet in the inventory")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pet updated successfully",
                    content = @Content(schema = @Schema(implementation = PetDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pet not found"),
            @ApiResponse(responseCode = "400", description = "Invalid pet data")
    })
    public ResponseEntity<PetDTO> updatePet(
            @Parameter(description = "The ID of the pet to update") @PathVariable Long id,
            @Valid @RequestBody
            @Parameter(description = "The updated pet details") PetDTO petDTO) {
        try {
            PetDTO updatedPet = petInventoryService.updatePet(id, petDTO);
            return ResponseEntity.ok(updatedPet);
        } catch (PetNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidPetException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes a pet from the inventory.
     *
     * @param id the ID of the pet to delete
     * @return ResponseEntity with HTTP status 204 (No Content)
     * @throws PetNotFoundException if the pet with the specified ID is not found
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a pet", description = "Deletes a pet from the inventory")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pet deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    public ResponseEntity<Void> deletePet(
            @Parameter(description = "The ID of the pet to delete") @PathVariable Long id) {
        try {
            petInventoryService.deletePet(id);
            return ResponseEntity.noContent().build();
        } catch (PetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves all pets of a specific species.
     *
     * @param species the species to search for
     * @return ResponseEntity containing a list of pets of the specified species with HTTP status 200 (OK)
     */
    @GetMapping("/species/{species}")
    @Operation(summary = "Get pets by species", description = "Retrieves all pets of a specific species")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved pets",
            content = @Content(schema = @Schema(type = "array", implementation = PetDTO.class)))
    public ResponseEntity<List<PetDTO>> getPetsBySpecies(
            @Parameter(description = "The species to search for (e.g., DOG, CAT, BIRD)") @PathVariable String species) {
        List<PetDTO> pets = petInventoryService.getPetsBySpecies(species);
        return ResponseEntity.ok(pets);
    }

    /**
     * Retrieves all pets with inventory count below the specified threshold.
     * Useful for identifying low stock items that need reordering.
     *
     * @param threshold the inventory threshold (default: 5)
     * @return ResponseEntity containing a list of low stock pets with HTTP status 200 (OK)
     */
    @GetMapping("/inventory/low-stock")
    @Operation(summary = "Get low stock pets", description = "Retrieves all pets with inventory count below the specified threshold")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved low stock pets",
            content = @Content(schema = @Schema(type = "array", implementation = PetDTO.class)))
    public ResponseEntity<List<PetDTO>> getLowStockPets(
            @Parameter(description = "The inventory threshold (default: 5)") 
            @RequestParam(defaultValue = "5") Integer threshold) {
        List<PetDTO> lowStockPets = petInventoryService.getLowStockPets(threshold);
        return ResponseEntity.ok(lowStockPets);
    }
}
