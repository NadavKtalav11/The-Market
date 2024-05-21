package DomainLayer.SupplyServices;

public class ProvisionReal extends IExternalProvisionService {
    private RestAPI real;
    private int transactionId = -1;

    public ProvisionReal(String baseUrl) {
        this.real = new RestAPI(baseUrl);
    }

    private boolean checkValidTransactionID(int transactionId) {
        return transactionId >= 10000 && transactionId <= 100000;
    }

    @Override
    public boolean checkServiceAvailability() {
        JSONObject checkDic = new JSONObject();
        checkDic.put("action_type", "handshake");
        RestAPI.Response response = real.post(checkDic.toString());
        return response.isOk();
    }

    @Override
    public boolean getDelivery(String userName, String shopName, int packageID, String address, String postalCode, String country, String city) {
        if (checkServiceAvailability()) {
            JSONObject supplyDic = new JSONObject();
            supplyDic.put("action_type", "supply");
            supplyDic.put("name", userName);
            supplyDic.put("address", address);
            supplyDic.put("city", city);
            supplyDic.put("country", country);
            supplyDic.put("zip", postalCode);

            RestAPI.Response response = real.post(supplyDic.toString());
            if (response.isOk()) {
                try {
                    int newResponse = Integer.parseInt(response.getText());
                    if (checkValidTransactionID(newResponse)) {
                        this.transactionId = newResponse;
                        Logger.reportInfo(this.getClass().getName(), userName + " - post request for sending delivery success!");
                        return true;
                    } else {
                        Logger.reportError(this.getClass().getName(), userName + " - transaction id is incorrect");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    Logger.reportError(this.getClass().getName(), userName + " - response text is invalid");
                    return false;
                }
            } else {
                Logger.reportError(this.getClass().getName(), userName + " - post request failed - " + response.getStatusCode());
                return false;
            }
        } else {
            Logger.reportError(this.getClass().getName(), userName + " - handshake failed ");
            return false;
        }
    }

    @Override
    public boolean cancelDelivery() {
        if (checkServiceAvailability()) {
            JSONObject cancelSupplyDic = new JSONObject();
            cancelSupplyDic.put("action_type", "cancel_supply");
            cancelSupplyDic.put("transaction_id", this.transactionId);

            RestAPI.Response response = real.post(cancelSupplyDic.toString());
            if (response.isOk() && response.getText().equals("1")) {
                Logger.reportInfo(this.getClass().getName(), "post request for canceling delivery successes!");
                return true;
            } else {
                Logger.reportError(this.getClass().getName(), "post request for canceling delivery failed - " + response.getStatusCode());
                return false;
            }
        } else {
            Logger.reportError(this.getClass().getName(), "handshake failed");
            return false;
        }
    }
}