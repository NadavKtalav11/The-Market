package DomainLayer.SupplyServices;

public abstract class IExternalProvisionService {

    public abstract boolean getDelivery(String userName, String shopName, int packageID, String address, String postalCode, String country, String city);

    public abstract boolean cancelDelivery();

    public abstract boolean checkServiceAvailability();
}
