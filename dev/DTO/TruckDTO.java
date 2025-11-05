package DTO;

    public record TruckDTO(
            String licensePlate,
            String requiredLicense,
            double truckWeight,
            double maxWeight,
            String model
    ) {}

