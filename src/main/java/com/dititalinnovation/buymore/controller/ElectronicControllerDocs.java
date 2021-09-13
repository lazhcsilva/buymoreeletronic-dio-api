package com.dititalinnovation.buymore.controller;

import com.dititalinnovation.buymore.dto.ElectronicDTO;
import com.dititalinnovation.buymore.exception.ElectronicAlreadyRegisteredException;
import com.dititalinnovation.buymore.exception.ElectronicNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api("Manages buy more")
public interface ElectronicControllerDocs {

    @ApiOperation(value = "Electronic creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Sucess electronic creation"),
            @ApiResponse(code = 400, message = "Missing required fields or wrong field range value.")
    })
    ElectronicDTO createElectronic(ElectronicDTO electronicDTO) throws ElectronicAlreadyRegisteredException;

    @ApiOperation(value = "Returns electronic found by a given name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success electronic found in the system"),
            @ApiResponse(code = 400, message = "Electronic with given name not found.")
    })
    ElectronicDTO findByName(@PathVariable String name) throws ElectronicNotFoundException;

    @ApiOperation(value = "Returns a list of all electronics registered in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all electronics registered in the system")
    })
    List<ElectronicDTO> listElectronics();

    @ApiOperation(value = "Delete a electronic found by a given valid Id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success electronic deleted in the system"),
            @ApiResponse(code = 404, message = "Electronic with given id not found.")
    })
    void deleteById(@PathVariable Long id) throws ElectronicNotFoundException;

}
