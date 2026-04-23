package com.petstore.inventory.service;

import com.petstore.dto.PetDTO;
import com.petstore.exception.InvalidPetException;
import com.petstore.exception.InsufficientStockException;
import com.petstore.exception.PetNotFoundException;
import com.petstore.inventory.repository.PetRepository;
import com.petstore.model.Pet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing pet inventory operations in the petstore system.
 * Provides CRUD operations and inventory management functionalities.
 * All database operations are transactional for data consistency.
 */
@Service
@Transactional
public class PetInventoryService {

    private static final Integer MINIMUM_INVENTORY = 0;
    private static final String INVALID_PRICE_MESSAGE = "Pet price must be greater than 0";
    private static final String INVALID_INVENTORY_MESSAGE = "Inventory count cannot be negative";
    private static final String PET_NOT_FOUND_MESSAGE = "Pet not found with id: ";
    private static final String INSUFFICIENT_STOCK_MESSAGE = "Insufficient stock for pet id: ";
    private static final String INVALID_QUANTITY_MESSAGE = "Quantity must be greater than 0";

    private final PetRepository petRepository;

    /**
     * Constructs a PetInventoryService with the specified PetRepository.
     *
     * @param petRepository the repository for pet data access
     */
    public PetInventoryService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    /**
     * Creates a new pet in the inventory.
     * Validates that the price is positive and inventory count is non-negative.
     *
     * @param petDTO the pet data transfer object containing the pet details
     * @return the created pet as a PetDTO with the assigned ID
     * @throws InvalidPetException if the pet data is invalid
     */
    public PetDTO createPet(PetDTO petDTO) throws InvalidPetException {
        validatePetData(petDTO);

        Pet pet = new Pet(
                petDTO.getName(),
                petDTO.getSpecies().toString(),
                BigDecimal.valueOf(petDTO.getPrice()),
                petDTO.getInventoryCount()
        );

        Pet savedPet = petRepository.save(pet);
        return convertToDTO(savedPet);
    }

    /**
     * Retrieves a pet by its ID.
     *
     * @param id the pet ID
     * @return the pet as a PetDTO
     * @throws PetNotFoundException if the pet is not found
     */
    @Transactional(readOnly = true)
    public PetDTO getPetById(Long id) throws PetNotFoundException {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(PET_NOT_FOUND_MESSAGE + id));
        return convertToDTO(pet);
    }

    /**
     * Retrieves all pets in the inventory.
     *
     * @return a list of all pets as PetDTOs
     */
    @Transactional(readOnly = true)
    public List<PetDTO> getAllPets() {
        List<PetDTO> result = new ArrayList<>();
        for (Pet pet : petRepository.findAll()) {
            result.add(convertToDTO(pet));
        }
        return result;
    }

    /**
     * Retrieves all pets of a specific species.
     *
     * @param species the species to search for
     * @return a list of pets matching the specified species
     */
    @Transactional(readOnly = true)
    public List<PetDTO> getPetsBySpecies(String species) {
        List<PetDTO> result = new ArrayList<>();
        for (Pet pet : petRepository.findBySpecies(species)) {
            result.add(convertToDTO(pet));
        }
        return result;
    }

    /**
     * Updates an existing pet in the inventory.
     * Validates that the updated price is positive and inventory count is non-negative.
     *
     * @param id the ID of the pet to update
     * @param petDTO the updated pet data transfer object
     * @return the updated pet as a PetDTO
     * @throws PetNotFoundException if the pet is not found
     * @throws InvalidPetException if the updated pet data is invalid
     */
    public PetDTO updatePet(Long id, PetDTO petDTO) throws PetNotFoundException, InvalidPetException {
        validatePetData(petDTO);

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(PET_NOT_FOUND_MESSAGE + id));

        pet.setName(petDTO.getName());
        pet.setSpecies(petDTO.getSpecies().toString());
        pet.setPrice(BigDecimal.valueOf(petDTO.getPrice()));
        pet.setInventoryCount(petDTO.getInventoryCount());

        Pet updatedPet = petRepository.save(pet);
        return convertToDTO(updatedPet);
    }

    /**
     * Deletes a pet from the inventory.
     *
     * @param id the ID of the pet to delete
     * @throws PetNotFoundException if the pet is not found
     */
    public void deletePet(Long id) throws PetNotFoundException {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(PET_NOT_FOUND_MESSAGE + id));
        petRepository.delete(pet);
    }

    /**
     * Decrements the inventory count for a specific pet.
     * Used when a pet is sold or removed from stock.
     *
     * @param petId the ID of the pet
     * @param quantity the quantity to decrement
     * @throws PetNotFoundException if the pet is not found
     * @throws InsufficientStockException if the current inventory is less than the quantity to decrement
     * @throws InvalidPetException if the quantity is invalid
     */
    public void decrementStock(Long petId, Integer quantity) throws PetNotFoundException, InsufficientStockException, InvalidPetException {
        validateQuantity(quantity);

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException(PET_NOT_FOUND_MESSAGE + petId));

        if (pet.getInventoryCount() < quantity) {
            throw new InsufficientStockException(INSUFFICIENT_STOCK_MESSAGE + petId + ". Available: " + pet.getInventoryCount() + ", Requested: " + quantity);
        }

        pet.setInventoryCount(pet.getInventoryCount() - quantity);
        petRepository.save(pet);
    }

    /**
     * Increments the inventory count for a specific pet.
     * Used when new stock is received.
     *
     * @param petId the ID of the pet
     * @param quantity the quantity to increment
     * @throws PetNotFoundException if the pet is not found
     * @throws InvalidPetException if the quantity is invalid
     */
    public void incrementStock(Long petId, Integer quantity) throws PetNotFoundException, InvalidPetException {
        validateQuantity(quantity);

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException(PET_NOT_FOUND_MESSAGE + petId));

        pet.setInventoryCount(pet.getInventoryCount() + quantity);
        petRepository.save(pet);
    }

    /**
     * Retrieves all pets with inventory count below the specified threshold.
     * Useful for identifying low stock items that need reordering.
     *
     * @param threshold the inventory threshold
     * @return a list of pets with inventory count below the threshold
     */
    @Transactional(readOnly = true)
    public List<PetDTO> getLowStockPets(Integer threshold) {
        List<PetDTO> result = new ArrayList<>();
        for (Pet pet : petRepository.findByInventoryCountGreaterThan(threshold - 1)) {
            if (pet.getInventoryCount() < threshold) {
                result.add(convertToDTO(pet));
            }
        }
        return result;
    }

    /**
     * Validates pet data to ensure it meets business requirements.
     * Price must be greater than 0, and inventory count must be non-negative.
     *
     * @param petDTO the pet data transfer object to validate
     * @throws InvalidPetException if the pet data is invalid
     */
    private void validatePetData(PetDTO petDTO) throws InvalidPetException {
        if (petDTO.getPrice() == null || petDTO.getPrice() <= 0) {
            throw new InvalidPetException(INVALID_PRICE_MESSAGE);
        }
        if (petDTO.getInventoryCount() != null && petDTO.getInventoryCount() < MINIMUM_INVENTORY) {
            throw new InvalidPetException(INVALID_INVENTORY_MESSAGE);
        }
    }

    /**
     * Validates that the quantity is positive.
     *
     * @param quantity the quantity to validate
     * @throws InvalidPetException if the quantity is not positive
     */
    private void validateQuantity(Integer quantity) throws InvalidPetException {
        if (quantity == null || quantity <= 0) {
            throw new InvalidPetException(INVALID_QUANTITY_MESSAGE);
        }
    }

    /**
     * Converts a Pet entity to a PetDTO.
     *
     * @param pet the Pet entity
     * @return the converted PetDTO
     */
    private PetDTO convertToDTO(Pet pet) {
        return new PetDTO(
                pet.getId(),
                pet.getName(),
                com.petstore.Species.valueOf(pet.getSpecies()),
                pet.getPrice().doubleValue(),
                pet.getInventoryCount()
        );
    }
}
