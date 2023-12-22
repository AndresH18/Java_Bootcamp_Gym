package com.javabootcamp.gym.data.model;

public enum TrainingType {
    FITNESS,
    YOGA,
    ZUMBA,
    STRETCHING,
    RESISTANCE;

    /**
     * Returns the enum constant of this type from the specified id (Ordinal).
     * The id must be an ordinal value of the enum
     *
     * @param id ordinal value of the enum.
     * @return {@link TrainingType} for the corresponding ordinal id
     * @throws IllegalArgumentException If the id is not an ordinal value of {@link TrainingType}
     */
    public static TrainingType byId(int id) throws IllegalArgumentException {
        for (TrainingType type : TrainingType.values()) {
            if (type.ordinal() == id)
                return type;
        }
        throw new IllegalArgumentException();
    }

}
