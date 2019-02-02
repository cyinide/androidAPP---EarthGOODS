package com.example.guestuser.damirkrkalicearthgoods.helper;

public class MyApiResult {
    public String errorMessage = "";
    public boolean isException = false;
    public int resultCode = 0;
    public String value;

    public static MyApiResult OK(String value) {
        MyApiResult result = new MyApiResult();

        result.value = value;
        result.isException = false;
        result.resultCode = 200;
        result.errorMessage = "";

        return result;
    }

    public static MyApiResult Error(int exceptionCode, String exceptionMessage) {
        MyApiResult result = new MyApiResult();
        result.isException = true;
        result.resultCode = exceptionCode;
        result.errorMessage = exceptionMessage;

        return result;
    }
}
