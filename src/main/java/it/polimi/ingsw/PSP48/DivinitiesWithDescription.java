package it.polimi.ingsw.PSP48;

import java.io.Serializable;

/**
 * Associates a divinity with its name
 */
public class DivinitiesWithDescription implements Serializable {
    private final String name;
    private final String description;

    /**
     * Initializes the object
     *
     * @param name        the name of the divinity
     * @param description the description of the divinity
     */
    public DivinitiesWithDescription(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Divinity's name getter
     *
     * @return the divinity's name
     */
    public String getName() {
        return name;
    }

    /**
     * Divinity's description getter
     *
     * @return the divinity's description
     */
    public String getDescription() {
        return description;
    }

}
