package de.tub.ise.ec;

import de.tub.ise.ec.kv.FileSystemKVStore;
import de.tub.ise.ec.kv.KeyValueInterface;
import de.tub.ise.hermes.*;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class secondaryNode implements IRequestHandler {

    private static final int port = 80;
    private KeyValueInterface store;
    private static final String  DB_Dir = ".//slaveNodeDB";
    private static final String GET = "read";
    private static final String ADD = "create";
    private static final String DEL = "delete";
    private static final String SET = "update";
    private static final String ROOT = "master";
    private static SimpleDateFormat formatter;
    private Receiver receiver;

    /**
     * Initiate the slave server
     */
    public secondaryNode(){
        store = new FileSystemKVStore(DB_Dir);
        RequestHandlerRegistry reg = RequestHandlerRegistry.getInstance();
        reg.registerHandler("secondaryNode", this);
        formatter = new SimpleDateFormat("HH:mm:ss:SSS");
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
        List<String> key=store.getKeys();
        Command c = (Command) commands.get(0);

            //Handle Get Operation
            if(c.getOperation().equals(GET)) {
                try {

                    //TODO:FIX nullpointerexception in case DB Is empty
                    // * Case file with key does not exist response :GET

                        if(key.contains(c.getKey())){
                            c.setValue((String) store.getValue(c.getKey()));
                            c.setResponseMsg("Slave: Item successfully  retrieved.\n");}
                        else{
                            c.setResponseMsg("Slave: An Item with key"+c.getKey()+" does not exist.\n"); }


                }
                catch (NullPointerException e) {
                    c.setResponseMsg("Slave: The Database is Empty.\n");
                }
            }

            //Handle Master RequestsOnly For Modifications
            else if(request.getOriginator().equals(ROOT)){
                switch (c.getOperation()){

                    case  ADD:
                        store.store(c.getKey(),c.getValue());
                        Date date1 = new Date();
                        //save Commit timestamp and set response message
                        c.setSlaveTimestamp(date1);
                        c.setResponseMsg(c.getResponseMsg()+"Slave: Item successfully  added.\n");
                        break;

                    case DEL:
                        store.delete(c.getKey());
                        Date date2 = new Date();
                        //save Commit timestamp and set response message
                        c.setSlaveTimestamp(date2);
                        c.setResponseMsg(c.getResponseMsg()+"Slave: Item successfully  deleted.\n");
                        break;

                    case  SET:
                        //Update Fix: could not overwrite the file with the new value using store method
                        store.delete(c.getKey());
                        store.store(c.getKey(), c.getValue());
                        Date date3 = new Date();
                        //save Commit timestamp and set response message
                        c.setSlaveTimestamp(date3);
                        c.setResponseMsg(c.getResponseMsg()+"Slave: Item successfully  updated.\n");
                        break;
                }
            }

            else{c.setResponseMsg("Slave: Operation Rejected. You are only Allowed to Read from this node.\n");}

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
        try {System.out.println("Slave Server started...");
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
    }


    /**
     * Start the server
     * Command: java -jar secondaryNode.jar
     * @param args:"Start" to start the server
     */
    public static void main(String[] args) {
        secondaryNode n1 = new secondaryNode();
        n1.serverStart();
    }

}
