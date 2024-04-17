package tukano.api;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Blob {

    @Id
    private String blobId;
    private byte[] bytes;

    public Blob() {

    }

    public Blob(String blobId, byte[] bytes) {
        this.blobId = blobId;
        this.bytes = bytes;
    }

    public String getBlobId() {
        return blobId;
    }

    public byte[] getBytes(){
        return bytes;
    }
}
