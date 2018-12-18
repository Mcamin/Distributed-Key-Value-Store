package de.tub.ise.ec;

import de.tub.ise.ec.kv.FileSystemKVStore;
import de.tub.ise.ec.kv.KeyValueInterface;
import de.tub.ise.hermes.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class primaryNode implements IRequestHandler {

    private static final int port = 80;
    private KeyValueInterface store;
    private static final String  DB_Dir = ".//masterNodeDB";
    private static final String GET = "read";
    private static final String ADD = "create";
    private static final String DEL = "delete";
    private static final String SET = "update";
    private static final String SYNC = "Sync";
    private static final String ASYNC = "Async";
    private Receiver receiver;

    /**
     * Initiate the master server
     * */
    public primaryNode(){
        store = new FileSystemKVStore(DB_Dir);
        RequestHandlerRegistry reg = RequestHandlerRegistry.getInstance();
        reg.registerHandler("primaryNode", this);
    }

    /***
     *  Handle the request received by the client
     * @param request: the request received
     * @return response to the client
     */
    @Override
    public Response handleRequest(Request request) {

        //get request items
        List<Serializable> commands = request.getItems();
        List<String> key=null;
        Command c = (Command) commands.get(0);
        // Initiate masterClient
        Client masterClient = new Client("18.197.152.162",80);

        try {

            key=store.getKeys();

            //check the sending mode
            if(c.getMode().equals(ASYNC) ||c.getMode().equals(SYNC)){
                //Handle operation
                switch (c.getOperation())
                {
                    case  GET:

                     if(key!=null && key.contains(c.getKey())){
                     c.setValue((String) store.getValue(c.getKey()));
                     c.setResponseMsg("Master: Item successfully retrieved.\n");}
                     else{
                         c.setResponseMsg("Master: An Item with key "+c.getKey()+" does not exist.\n");
                     }
                 break;
                 case  ADD:
                     if(key!=null && key.contains(c.getKey())){
                         c.setResponseMsg("Master: An Item with key "+c.getKey()+" already exist.\n");
                     }else
                         {
                         //initiate client request
                         masterClient.createRequest(c, "secondaryNode", "master");
                         //send the request
                         if (c.getMode().equals(ASYNC)) {
                             System.out.println("Sending the request in Async Mode");
                             masterClient.sendReqMasterAsync(store);
                         } else {
                             System.out.println("Sending the request in Sync Mode");
                             c = masterClient.sendReqMaster(store);
                         }
                         store.store(c.getKey(), c.getValue());
                         Date date = new Date();
                         //save Commit timestamp and set response message
                         c.setMasterTimestamp(date);
                         c.setResponseMsg("Master: Item successfully added.\n");
                         }
                         break;
                     case DEL:


                    if(key!=null && key.contains(c.getKey())){
                        store.delete(c.getKey());
                        Date date1 = new Date();
                        //save Commit timestamp and set response message
                        c.setMasterTimestamp(date1);
                        c.setResponseMsg("Master: Item successfully deleted.\n");
                        //initiate client request
                        masterClient.createRequest(c,"secondaryNode","master");
                    //send the request
                    if(c.getMode().equals(ASYNC)){
                        masterClient.sendReqMasterAsync(store);
                    }else{
                        c=masterClient.sendReqMaster(store);
                    }
                    }
                    else
                        {
                        c.setResponseMsg("Master: The Element you are trying to delete does not exist.\n");
                    }
                break;
                case  SET:
                    if(key!=null && key.contains(c.getKey())){
                    //Fix: could not overwrite the file with the new value using store method
                    c.setOldValue((String)store.getValue(c.getKey()));
                    store.delete(c.getKey());
                    store.store(c.getKey(), c.getValue());
                    Date date2 = new Date();
                    //save Commit timestamp and set response message
                    c.setMasterTimestamp(date2);
                    c.setResponseMsg("Master: Item successfully updated.\n");
                    //initiate client request
                    masterClient.createRequest(c,"secondaryNode","master");
                    //send the request
                    if(c.getMode().equals(ASYNC)){
                        masterClient.sendReqMasterAsync(store);
                    }else{
                        c=masterClient.sendReqMaster(store);
                    }
                    }else{
                        c.setResponseMsg("Master: The Element you are trying to update does not exist.\n");
                    }
                break;
                default:
                 c.setResponseMsg("Master: Invalid Operation.\n");
                }
            }
            else{
                c.setResponseMsg("Master: Invalid Send Mode.\n");
            }


        } catch (NullPointerException e) {
            c.setResponseMsg("Master: Null Pointer Somewhere\n");
        }

        return new Response(c.getResponseMsg(), true, request, c);
    }


    /**
     *  if true, the server will send back a response to the client
     * @return true /false
     */
    @Override
    public boolean requiresResponse() {
        return true;
    }

    /**
     * Start the Master Server on the Default Port
     */
    public void serverStart(){
        try {System.out.println("Master Server started...");
            receiver = new Receiver(this.port);
            receiver.start();
        } catch (IOException e) {
            System.out.println("Connection error: " + e);
        }
    }

    /**
     * Stop the Server
     */
    public void serverStop(){
        receiver.terminate();
        System.out.println("Server stopped...");
    }

    /**
     * Start the server
     * Command: java -jar primaryNode.jar
     * @param args:"Start" to start the server
     */
    public static void main(String[] args) {
        primaryNode n1 = new primaryNode();
        n1.serverStart();

    }

}
