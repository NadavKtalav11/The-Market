package Util;

public class APIResponse<T> {
    private T data;
    private String errorMassage;


    public APIResponse(T data , String errorMassage){
        this.data = data;
        this.errorMassage = errorMassage;
    }

}
