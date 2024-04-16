package tukano.api;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Follow {

    @Id
    private int followId;
    private String followerId;
    private String followedId;

    public Follow(int followId, String followerId, String followedId){
        this.followId = followId;
        this.followerId = followerId;
        this.followedId = followedId;
    }

    public Follow() {}

    public String getFollowerId(){
        return followerId;
    }

    public String getFollowedId(){
        return followedId;
    }

}
