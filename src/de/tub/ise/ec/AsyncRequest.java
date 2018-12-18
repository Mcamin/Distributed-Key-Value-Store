package de.tub.ise.ec;

import de.tub.ise.hermes.AsyncCallbackRecipient;
import de.tub.ise.hermes.Response;

public class AsyncRequest implements AsyncCallbackRecipient {

    private Response response;
    private boolean echoSuccessful;

    public boolean isEchoSuccessful() {
        return echoSuccessful;
    }

    public void setEchoSuccessful(boolean echoSuccessful) {
        this.echoSuccessful = echoSuccessful;
    }


    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }


    @Override
    public void callback(Response resp) {
        setResponse(resp);
        setEchoSuccessful(resp.responseCode());
    }

}

