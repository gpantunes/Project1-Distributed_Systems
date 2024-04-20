package tukano.impl.srv.java;

import tukano.api.Blob;
import tukano.api.java.Blobs;


import tukano.api.service.util.Result;
import tukano.impl.Hibernate;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static tukano.api.service.util.Result.ErrorCode.INTERNAL_ERROR;

public class JavaBlobs implements tukano.api.java.Blobs{

    private static Logger Log = Logger.getLogger(JavaBlobs.class.getName());

    @Override
    public Result<Void> upload(String blobId, byte[] bytes) {
        String filePath = "blobFiles/" + blobId;
        String directoryPath = "blobFiles/";

        try {
            Path directory = Paths.get(directoryPath);
            if (!Files.exists(directory))
                Files.createDirectories(directory);

            // Convert byte array to Path object
            Path path = Paths.get(filePath);

            // Write the bytes to the file
            Files.write(path, bytes);

        } catch (IOException e) {
            return Result.error(INTERNAL_ERROR);
        }

        Hibernate.getInstance().persist(new Blob(blobId, blobId));

        return Result.ok();
    }

    @Override
    public Result<byte[]> download(String blobId) {

        var blobList = Hibernate.getInstance().sql("SELECT * FROM Blob WHERE blobId = '"
                + blobId + "'", Blob.class);

        Blob blob = blobList.get(0);
        byte[] content;

        try{
            String filePath = "blobFiles/" + blobId;
            Path path = Paths.get(filePath);

            content = Files.readAllBytes(path);
        }catch (IOException e){
            return Result.error(INTERNAL_ERROR);
        }


        return Result.ok(content);
    }

    @Override
    public Result<Void> downloadToSink(String blobId, Consumer<byte[]> sink) {
        return Blobs.super.downloadToSink(blobId, sink);
    }
}
