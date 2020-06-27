package it.polimi.ingsw.PSP48;

import java.io.Serializable;

/**
 * Associates a divinity with its name
 */
public class DivinitiesWithDescription implements Serializable {
    private final String name;
    private final String description;

    public DivinitiesWithDescription(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return the divinity's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the divinity's description
     */
    public String getDescription() {
        return description;
    }

}
