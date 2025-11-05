package DomainLayer;

public class Truck {
    private String licensePlate;
    private LicenseType requiredLicense;
    private double truckWeight;
    private double maxWeight;
    private String model;

    public Truck(String licensePlate, double truckWeight, double maxWeight, String model) {
        if (truckWeight < 0 || maxWeight < 0) {
            throw new IllegalArgumentException();
        }

        this.requiredLicense = (maxWeight < 12000) ? LicenseType.A : LicenseType.C1;
        this.licensePlate = licensePlate;
        this.truckWeight = truckWeight;
        this.maxWeight = maxWeight;
        this.model = model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
    public LicenseType getRequiredLicense() {
        return requiredLicense;
    }
    public void setRequiredLicense(LicenseType requiredLicense) {
        this.requiredLicense = requiredLicense;
    }
    public double getTruckWeight() {
        return truckWeight;
    }
    public void setTruckWeight(double truckWeight) {
        this.truckWeight = truckWeight;
    }
    public double getMaxWeight() {
        return maxWeight;
    }
    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }
    public boolean canCarry(double weight) {
        return weight + truckWeight <= maxWeight;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Truck truck = (Truck) o;
        return licensePlate.equals(truck.licensePlate);
    }

    @Override
    public String toString() {
        return "Truck license plate: " + licensePlate + ", Max weight: " + maxWeight + ", required license: " + requiredLicense + ", model: " + model;
    }

    public static Truck fromDTO(DTO.TruckDTO dto) {
        return new Truck(
                dto.licensePlate(),
                dto.truckWeight(),
                dto.maxWeight(),
                dto.model()
        );
    }


    public DTO.TruckDTO toDTO() {
        return new DTO.TruckDTO(
                licensePlate,
                requiredLicense.name(),
                truckWeight,
                maxWeight,
                model
        );
    }


}
