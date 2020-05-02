package it.polimi.ingsw.PSP48;

import java.io.Serializable;

public class DivinitiesWithDescription implements Serializable {
    private final String name;
    private final String description;

    public DivinitiesWithDescription(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
