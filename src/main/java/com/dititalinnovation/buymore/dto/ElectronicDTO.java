package com.dititalinnovation.buymore.dto;

import com.dititalinnovation.buymore.enums.ElectronicType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElectronicDTO {

    private Long id;

    @NotNull
    @Size(min = 1, max = 150)
    private String name;

    @NotNull
    @Size(min = 1, max = 150)
    private String brand;

    @NotNull
    @Max(500)
    private Integer max;

    @NotNull
    @Max(500)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ElectronicType type;


}
