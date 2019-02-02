package com.example.guestuser.damirkrkalicearthgoods.data;

public class GenerateImageHelper {

    public String encodedImageBase64;

    public String fileName;

    public LoginUser credentials;


    public GenerateImageHelper(String encodedImageBase64, String fileName, LoginUser credentials) {
        this.encodedImageBase64 = encodedImageBase64;
        this.fileName = fileName;
        this.credentials = credentials;
    }

    public String getEncodedImageBase64() {
        return encodedImageBase64;
    }

    public void setEncodedImageBase64(String encodedImageBase64) {
        this.encodedImageBase64 = encodedImageBase64;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }



}
