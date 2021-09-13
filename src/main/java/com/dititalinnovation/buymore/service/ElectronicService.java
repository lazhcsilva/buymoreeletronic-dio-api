package com.dititalinnovation.buymore.service;

import com.dititalinnovation.buymore.dto.ElectronicDTO;
import com.dititalinnovation.buymore.entity.Electronic;
import com.dititalinnovation.buymore.exception.BuyMoreExceededException;
import com.dititalinnovation.buymore.exception.ElectronicAlreadyRegisteredException;
import com.dititalinnovation.buymore.exception.ElectronicNotFoundException;
import com.dititalinnovation.buymore.mapper.ElectronicMapper;
import com.dititalinnovation.buymore.repository.ElectronicRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ElectronicService {

    private final ElectronicRepository electronicRepository;
    private final ElectronicMapper electronicMapper = ElectronicMapper.INSTANCE;

    public ElectronicDTO createElectronic(ElectronicDTO electronicDTO) throws ElectronicAlreadyRegisteredException{
        verifyIfIsAlreadyRegistered(electronicDTO.getName());
        Electronic electronic = electronicMapper.toModel(electronicDTO);
        Electronic savedElectronic = electronicRepository.save(electronic);
        return electronicMapper.toDTO(savedElectronic);
    }

    public ElectronicDTO findByName(String name) throws ElectronicNotFoundException {
        Electronic foundElectronic = electronicRepository.findByName(name)
                .orElseThrow(() -> new ElectronicNotFoundException(name));
        return electronicMapper.toDTO(foundElectronic);
    }

    public List<ElectronicDTO> listAll() {
        return electronicRepository.findAll()
                .stream()
                .map(electronicMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws ElectronicNotFoundException {
        verifyIfExists(id);
        electronicRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws ElectronicAlreadyRegisteredException {
        Optional<Electronic> optSavedElectronic = electronicRepository.findByName(name);
        if (optSavedElectronic.isPresent()) {
            throw new ElectronicAlreadyRegisteredException(name);
        }
    }

    private Electronic verifyIfExists(Long id) throws ElectronicNotFoundException {
        return electronicRepository.findById(id)
                .orElseThrow(() -> new ElectronicNotFoundException(id));
    }

    public ElectronicDTO increment(Long id, int quantityToIncrement) throws ElectronicNotFoundException, BuyMoreExceededException {
        Electronic electronicToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + electronicToIncrementStock.getQuantity();
        if (quantityAfterIncrement <= electronicToIncrementStock.getMax()) {
            electronicToIncrementStock.setQuantity(electronicToIncrementStock.getQuantity() + quantityToIncrement);
            Electronic incrementedElectronicStock = electronicRepository.save(electronicToIncrementStock);
            return electronicMapper.toDTO(incrementedElectronicStock);
        }
        throw new BuyMoreExceededException(id, quantityToIncrement);
    }

}
