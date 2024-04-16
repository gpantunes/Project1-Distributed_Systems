package tukano.api;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Like {

    @Id
    private int likeId;
    private String userId;
    private String shortId;

    public Like(int likeId, String userId, String shortId){
        this.likeId = likeId;
        this.userId = userId;
        this.shortId = shortId;
    }

    public Like() {}

    public int getLikeId(){
        return likeId;
    }

    public String getUserId(){
        return userId;
    }

    public String getShortId(){
        return shortId;
    }

}
