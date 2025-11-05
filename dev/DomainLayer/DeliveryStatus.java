package DomainLayer;

import java.util.ArrayList;

public enum DeliveryStatus {
    planned, inProgress,locked, completed, cancelled, assignedDriver;

    public ArrayList<DeliveryStatus> getPossibleNextStatus(){
        ArrayList<DeliveryStatus> possibleNextStatus = new ArrayList<>();
        switch(this){
            case planned:
                possibleNextStatus.add(DeliveryStatus.inProgress);
                possibleNextStatus.add(DeliveryStatus.cancelled);
                break;
            case inProgress:
                possibleNextStatus.add(DeliveryStatus.completed);
                possibleNextStatus.add(DeliveryStatus.cancelled);
                break;
            case completed:
            case cancelled:
        }
        return possibleNextStatus;
    }
}
