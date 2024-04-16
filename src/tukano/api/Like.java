package tukano.api;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Like {

    @Id
    private String likeId;
    private String userId;
    private String shortId;

    public Like(String likeId, String userId, String shortId){
        this.likeId = likeId;
        this.userId = userId;
        this.shortId = shortId;
    }

    public Like() {}

    public String getLikeId(){
        return likeId;
    }

    public String getUserId(){
        return userId;
    }

    public String getShortId(){
        return shortId;
    }

}
