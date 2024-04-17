package tukano.impl.srv.java;

import tukano.api.Blob;
import tukano.api.java.Blobs;


import tukano.api.service.util.Result;
import tukano.impl.Hibernate;


import java.util.function.Consumer;

public class JavaBlobs implements tukano.api.java.Blobs{

    @Override
    public Result<Void> upload(String blobId, byte[] bytes) {

        Hibernate.getInstance().persist(new Blob(blobId, bytes));

        return Result.ok();
    }

    @Override
    public Result<byte[]> download(String blobId) {

        var blobList = Hibernate.getInstance().sql("SELECT * FROM Blob WHERE blobId = '"
                + blobId + "'", Blob.class);

        Blob blob = blobList.get(0);
        byte[] content = blob.getBytes();

        return Result.ok(content);
    }

    @Override
    public Result<Void> downloadToSink(String blobId, Consumer<byte[]> sink) {
        return Blobs.super.downloadToSink(blobId, sink);
    }
}
