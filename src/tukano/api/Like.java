package tukano.api;

public class Like {

    private String userId;
    private String shortId;

    public Like(String userId, String shortId){
        this.userId = userId;
        this.shortId = shortId;
    }

    public String getUserId(){
        return userId;
    }

    public String getShortId(){
        return shortId;
    }

}
