package de.tub.ise.ec;

import java.io.Serializable;
import java.util.Date;

public class Command implements Serializable {
    private String key;
    private String operation;
    private String value;
    private String responseMsg;
    private String mode;
    private Date slaveTimestamp;
    private Date masterTimestamp;
    private String oldValue;

    /**
     * Create a Command to pass to the server (Create / Update)
     * @param key : the key of the item
     * @param value : the value of the item
     * @param operation : the operation to execute
     * @param mode : the mode of the send request to slave
     */

    public Command(String key, String value,String operation,String mode){
        super();
        this.key=key;
        this.value=value;
        this.operation=operation;
        this.responseMsg=null;
        this.mode=mode;
        this.slaveTimestamp=null;
        this.masterTimestamp=null;
    }

    /**
     * Create a Command to pass to the server (Read / Delete)
     * @param key : the key of the item
     * @param operation: the operation to execute
     * @param mode : the mode of the send request to slave
     */

    public Command(String key,String operation,String mode){
        super();
        this.key=key;
        this.operation=operation;
        this.value=null;
        this.responseMsg=null;
        this.mode=mode;
        this.slaveTimestamp=null;
        this.masterTimestamp=null;
    }

    /**
     * Operation Getter
     * @return Operation
     */

    public String getOperation()
    {return this.operation;}

    /**
     * Key Getter
     * @return Key
     */

    public String getKey()
    {return this.key;}

    /**
     * slaveTimeStamp Getter
     * @return slaveTimestamp : the slave commit timestamp
     */

    public Date getSlaveTimestamp()
    {return this.slaveTimestamp;}

    /**
     * masterTimeStamp Getter
     * @return masterTimestamp : the master commit timestamp
     */

    public Date getMasterTimestamp()
    {return this.masterTimestamp;}

    /**
     * value Getter
     * @return value : the value of the item
     */
    public String getValue()
    {return this.value;}

    /**
     * value Getter
     * @return oldValue : the old value of the item
     */
    public String getOldValue()
    {return this.oldValue;}

    /**
     * mode Getter
     * @return mode : the mode of the send request
     */
    public String getMode(){
        return  this.mode;
    }

    /**
     * responseMsg Getter
     * @return mode : the response message to send to the client
     */
    public String getResponseMsg(){
        return  this.responseMsg;
    }

    /**
     * slaveTimestamp Setter
     * @param timestamp : the commit timestamp on the slave server
     */
    public void setSlaveTimestamp(Date timestamp)
    { this.slaveTimestamp=timestamp;}

    /**
     * masterTimestamp Setter
     * @param timestamp : the commit timestamp on the master server
     */
    public void setMasterTimestamp(Date timestamp)
    { this.masterTimestamp=timestamp;}

    /**
     * value Setter
     * @param value : the value of the item to add / modify
     */
    public void setValue(String value)
    { this.value=value;}
    /**
     * oldValue Setter
     * @param oldValue : the oldValue of the item to add / modify
     */
    public void setOldValue(String oldValue)
    { this.oldValue=oldValue;}

    /**
     * responseMsg Setter
     * @param resMsg : the message to send to the client
     */
    public void setResponseMsg(String resMsg)
    {
        this.responseMsg=resMsg;
    }





}
