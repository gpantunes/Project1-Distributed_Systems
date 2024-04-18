package tukano.api;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Follow {

    @Id
    private String followerId;
    private String followedId;

    public Follow(String followerId, String followedId){
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
