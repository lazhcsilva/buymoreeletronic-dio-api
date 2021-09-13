package com.dititalinnovation.buymore.controller;

import com.dititalinnovation.buymore.dto.ElectronicDTO;
import com.dititalinnovation.buymore.dto.QuantityDTO;
import com.dititalinnovation.buymore.exception.BuyMoreExceededException;
import com.dititalinnovation.buymore.exception.ElectronicAlreadyRegisteredException;
import com.dititalinnovation.buymore.exception.ElectronicNotFoundException;
import com.dititalinnovation.buymore.service.ElectronicService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/electronics")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ElectronicController implements ElectronicControllerDocs{

    private final ElectronicService electronicService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ElectronicDTO createElectronic(@RequestBody @Valid ElectronicDTO electronicDTO) throws ElectronicAlreadyRegisteredException{
        return electronicService.createElectronic(electronicDTO);
    }

    @GetMapping("/{name}")
    public ElectronicDTO findByName(@PathVariable String name) throws ElectronicNotFoundException {
        return electronicService.findByName(name);
    }

    @GetMapping
    public List<ElectronicDTO> listElectronics() {
        return electronicService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws ElectronicNotFoundException {
        electronicService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public ElectronicDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws ElectronicNotFoundException, BuyMoreExceededException {
        return electronicService.increment(id, quantityDTO.getQuantity());
    }

}
