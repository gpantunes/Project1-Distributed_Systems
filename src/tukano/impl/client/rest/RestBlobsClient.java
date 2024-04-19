package tukano.impl.client.rest;

import java.net.URI;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import tukano.api.java.Blobs;
import tukano.api.rest.RestBlobs;
import tukano.api.service.util.Result;

public class RestBlobsClient extends RestClient implements Blobs {

	String PATH = "/blobs";
	String BLOB_ID = "blobId";

	public RestBlobsClient(URI serverUri) {
		super(serverUri, RestBlobs.PATH);
	}

	@Override
	public Result<Void> upload(String blobId, byte[] bytes) {
		Response r = target
				.path(blobId)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(bytes, MediaType.APPLICATION_JSON));
		return super.responseContents(r, Status.OK, null);
	}

	@Override
	public Result<byte[]> download(String blobId) {
		Response r = target
				.path(blobId)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
		return super.responseContents(r, Status.OK, new GenericType<byte[]>() {
		});
	}

}
