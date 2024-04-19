package tukano.impl.client.common;

import tukano.api.java.Blobs;
import tukano.api.service.util.Result;

public class RetryBlobsClient extends RetryClient implements Blobs {

    final Blobs impl;

    public RetryBlobsClient(Blobs impl) {
        this.impl = impl;
    }

    @Override
    public Result<Void> upload(String blobId, byte[] bytes) {
        return reTry(() -> impl.upload(blobId, bytes));
    }

    @Override
    public Result<byte[]> download(String blobId) {
        return reTry(() -> impl.download(blobId));
    }
}