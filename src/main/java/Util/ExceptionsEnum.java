package Util;



    public enum ExceptionsEnum{
        sessionOver{
            public String toString() {
                return "your session was over please log in again";
            }
        },
        userIsNotMember{
            public String toString() {
                return "User is not logged in, so he can't perform this operation";
            }
        },
        storeNotExist{
            public String toString() {
                return "Store does not exist";
            }
        },
        storeAlreadyOpen{
            public String toString() {
                return "The store is already open";
            }
        },
        userNotExist{
            public String toString() {
                return "User does not exist";
            }
        },
        usernameNotFound{
            public String toString(){ return "Username was not found"; }
        },
        usernameAlreadyExist{
            public String toString(){ return  "Username already exists."; }
        },
        productNotExistInStore{
            public String toString() {
                return "product does not exist in store";
            }
        },
        productAlreadyExistInStore{
            public String toString() {
                return "Product already exist in this store";
            }
        },
        productQuantityNotExist{
            public String toString() {return "The quantity you entered isn't available in the store";}
        },
        productQuantityIsNegative{
            public String toString() {return "The quantity you entered is negative";}
        },
        productNotExistInCart{
            public String toString() {
                return "product does not exist in cart";
            }
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
        userIsNotStoreOwner{
            public String toString() {
                return "User is not a store owner, only store owner can perform this operation";}
        },
        storeOwnerIsNotFounder {
            public String toString() {
                return "Only store founder can close a store";
            }
        },
        illegalStoreName{
            public String toString() {
                return "Illegal store name. Store name is empty.";}
        },
        memberIsAlreadyStoreOwner{
            public String toString() {
                return "Member is already owner of this store";
            }
        },
        memberAlreadyHasRoleInThisStore{
            public String toString() {
                return "Member already has a role in this store";
            }
        },
        notNominatorOfThisManager{
            public String toString() {
                return "Store owner is not the store manager's nominator";
            }
        },
        notManager{
            public String toString() {
                return "User is not a manager of this store";
            }
        },
        usernameOrPasswordIncorrect{
            public String toString(){ return "username or password is incorrect"; }
        },
        userAlreadyLoggedIn{
            public String toString(){ return "user is already logged in"; }
        },
        priceRangeInvalid{
            public String toString(){ return "The price range you entered is invalid"; }
        },
        productRateInvalid{
            public String toString(){ return "The product rate you entered is invalid"; }
        },
        storeRateInvalid{
            public String toString(){ return "The store rate you entered is invalid"; }
        },
        categoryNotExist{
            public String toString(){ return "The product category you entered is invalid"; }
        },
        noInventoryPermissions{
            public String toString(){ return "User has no inventory permissions"; }
        },
        notSystemManager{
            public String toString(){ return "The user is not a system manager"; }
        },
        emptyField{
            public String toString(){ return "Fields cannot be empty"; }
        },
        passwordInvalid{
            public String toString(){ return "password must contains at least one digit, lowercase letter and uppercase letter.\n password must contains at least 8 characters"; }
        },
        memberCannotRegister{
            public String toString(){ return "member cannot register"; }
        },
        invalidFormatDate{
            public String toString(){ return "Invalid date format."; }
        },
        futureDate{
            public String toString(){ return "Birthday cannot be in the future."; }
        },
        invalidCountry{
            public String toString(){ return "Country is not exist"; }
        },
        invalidCity{
            public String toString(){ return "City is not exist"; }
        },
        rulesNotMatchOpeators{
            public String toString(){ return "The number of operators must be one less than the number of rules"; }
        },
        InvalidOperator{
            public String toString(){ return "The operator must be 'AND' or 'OR' or 'COND'"; }
        },
    }



