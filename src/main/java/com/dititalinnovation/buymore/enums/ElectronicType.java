package com.dititalinnovation.buymore.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ElectronicType {

    TV("TV"),
    NOTEBOOK("Notebook"),
    DESKTOP("Desktop"),
    SCANNER("Scanner"),
    TABLET("Tablet");

    private final String description;

}
