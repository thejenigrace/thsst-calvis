package EnvironmentConfiguration.model;

/**
 * Created by Ivan on 12/29/2015.
 */
public class ErrorMessage {

    private String category;
    private String message;

    public ErrorMessage(String category, String message){
        this.category = category;
        this.message = message;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
