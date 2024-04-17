package tukano.impl.rest;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tukano.api.java.Blobs;
import tukano.api.rest.RestBlobs;
import tukano.api.service.util.Result;
import tukano.impl.srv.java.JavaBlobs;

import java.util.logging.Logger;

public class RestBlobsClass implements RestBlobs {

    Blobs blobServer = new JavaBlobs();

    private static Logger Log = Logger.getLogger(JavaBlobs.class.getName());

    @Override
    public void upload(String blobId, byte[] bytes) {
        Log.info(String.format("REST upload blob " + blobId));

        checkResult(blobServer.upload(blobId, bytes));
    }

    @Override
    public byte[] download(String blobId) {
        Log.info(String.format("REST download blob " + blobId));

        return checkResult(blobServer.download(blobId));
    }


    protected <T> T checkResult(Result<T> result){
        if(result.isOK())
            return result.value();
        else
            throw new WebApplicationException(convertStatus(result));
    }


    static protected Response.Status convertStatus(Result<?> result ) { //é possivel que haja casos a mais
        switch( result.error() ) {
            case CONFLICT:
                return Response.Status.CONFLICT;
            case NOT_FOUND:
                return Response.Status.NOT_FOUND;
            case FORBIDDEN:
                return Response.Status.FORBIDDEN;
            case BAD_REQUEST:
                return Response.Status.BAD_REQUEST;
            case NOT_IMPLEMENTED:
                return Response.Status.NOT_IMPLEMENTED;
            default:
                return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }
}
