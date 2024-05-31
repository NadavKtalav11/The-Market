package Util;



    public enum ExceptionsEnum{
        sessionOver{
            public String toString() {
                return "your session was over please log in again";
            }
        },
        userIsNotMember{
            public String toString() {
                return "your session was over please log in again";
            }
        },
        storeNotExist{
            public String toString() {
                return "Store does not exist";
            }
        },
        userNotExist{
            public String toString() {
                return "User does not exist";
            }
        },
        productNotExistInStore{
            public String toString() {
                return "product does not exist in store";
            }
        },
        productQuantityNotExist{
            public String toString() {return "The quantity you entered isn't available in the store";}
        },
        productQuantityIsNegative{
            public String toString() {return "The quantity you entered is negative";}
        },
        purchasePolicyIsNotMet{
            public String toString() {return "The product doesn't meet the store purchase policy";}
        },
        discountPolicyIsNotMet{
            public String toString() {return "The product doesn't meet the store discount policy";}
        },
        userCartIsEmpty{
            public String toString() {return "User cart is empty, there's nothing to purchase";}
        },
        ExternalSupplyServiceIsNotAvailable{
            public String toString() {return "Unfortunately, there is no shipping for the user address";}
        },
        createShiftingError{
            public String toString() {return "Unfortunately, there was problem in creating the shifting";}
        },
        productNotExistInMarket{
            public String toString() {
                return "product does not exist in market";
            }
        },

    }



