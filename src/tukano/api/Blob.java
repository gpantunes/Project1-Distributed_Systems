package tukano.api;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Blob {

    @Id
    private int blobId;

    public Blob(int blobId){
        this.blobId = blobId;
    }

    public Blob() {

    }
}
