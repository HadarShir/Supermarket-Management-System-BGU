package DomainLayer;

public enum Status {
    success, failure, overWeight, destinationAlreadyExists, destinationNotFound,
    truckNotFound, truckUnavailable, driverMismatch, itemNotFound,SourceSameAsDestination,
    siteAlreadyExists, shipmentAreaNotFound, truckAlreadyExists, invalidInput,driverAlreadyExists,
    shipmentAreaAlreadyExists, invalidTimeOrder,sourceMismatch,noOrders,alreadyExists,notFound,cannotRemoveFirstSourceOrder,
    cannotRemoveFirstOrderOnly,noEstimates,invalidOrderStatus;
}
