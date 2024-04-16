package tukano.api;

public class Follow {

    private String followerId;
    private String followedId;

    public Follow(String followerId, String followedId){
        this.followerId = followerId;
        this.followedId = followedId;
    }

    public String getFollowerId(){
        return followerId;
    }

    public String getFollowedId(){
        return followedId;
    }

}
