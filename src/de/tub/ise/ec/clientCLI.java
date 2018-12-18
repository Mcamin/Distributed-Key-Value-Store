package de.tub.ise.ec;

import java.util.Scanner;

public class clientCLI {

    /**
     * User Commandline Interface
     * @param client the client
     * @param sc the scanner
     *
     */
    static void printUserMenu(Client client, Scanner sc){

        //init
        int choice ;
        int mode;
        String reqMode="Sync";


        //Operation To execute
        do{
            System.out.println("\n***Key Value Store dashboard***\n");
            System.out.println("  1) Create");
            System.out.println("  2) Read");
            System.out.println("  3) Update");
            System.out.println("  4) Delete");
            System.out.println("  5) Quit");
            System.out.println();
            System.out.println("Enter Choice :");
            choice = sc.nextInt();

            if(choice <1 || choice >5 ){
                System.out.println("Invalid choice. Please choose 1-5");
            }
        }
        while(choice <1 || choice >5); //continue looping until get a correct choice
        if(choice==5)System.exit(0);
        //Sending Mode
        do{
            System.out.println("\n***Choose the request sending mode***\n");
            System.out.println("  1) Sync");
            System.out.println("  2) Async");
            System.out.println();
            System.out.println("Enter Choice :");
            mode = sc.nextInt();

            if(mode <1 || mode >3 ){
                System.out.println("Invalid choice. Please choose 1-3");
            }
        }
        while(mode <1 || mode >3); //continue looping until get a correct mode
        // process the mode
        if(mode==2){reqMode="Async";}

        switch (choice)
        {
            case 1:
                clientCLI.createItem(client,sc,reqMode);
                break;
            case  2:
                clientCLI.readItem(client,sc,reqMode);
                break;
            case 3:
                clientCLI.updateItem(client,sc,reqMode);
                break;
            case 4:
                clientCLI.deleteItem(client,sc,reqMode);
                break;
            case 5:
                sc.nextLine();
                break;

        }
        //redisplay this menu unless the user wants to quit
        if(choice != 5){
            clientCLI.printUserMenu(client,sc);

        }
    }


    /**
     *  Add an item to the database
     * @param client the client
     * @param sc the Scanner
     */
    private static void createItem(Client client,Scanner sc,String mode){
        String key;
        String value;
        do{
            System.out.println("Enter the key of the item you want to add");
            key = sc.next();
            if(key.trim()==""){
                System.out.println("Invalid key. Please try again.");
            }
        }while(key.trim()=="");
        do{
            System.out.println("Enter the value of the item you want to add");
            value = sc.next();
            if(value.trim()==""){
                System.out.println("Invalid value. Please try again.");
            }
        }while(value.trim()=="");
        client.createRequest(new Command(key,value,"create",mode),"primaryNode","client");
        client.sendReqclient();
    }

    /**
     *  Update an item in the database
     * @param client the client
     * @param sc the Scanner
     */
    private static void updateItem(Client client,Scanner sc,String mode){
        String key;
        String value;
        do{
            System.out.println("Enter the key of the item you want to update");
            key = sc.next();
            if(key.trim()==""){
                System.out.println("Invalid key. Please try again.");
            }
        }while(key.trim()=="");
        do{
            System.out.println("Enter new value of the item you want to update");
            value = sc.next();
            if(value.trim()==""){
                System.out.println("Invalid value. Please try again.");
            }
        }while(value.trim()=="");
        client.createRequest(new Command(key,value,"update",mode),"primaryNode","client");
        client.sendReqclient();
    }

    /**
     *  Delete an item from the database
     * @param client the client
     * @param sc the Scanner
     */
    private static void deleteItem(Client client,Scanner sc,String mode){
        String key;
        do{
            System.out.println("Enter the key of the item you want to delete");
            key = sc.next();
            if(key.trim()==""){
                System.out.println("Invalid key. Please try again.");
            }
        }while(key.trim()=="");

        client.createRequest(new Command(key,"delete",mode),"primaryNode","client");
        client.sendReqclient();
    }

    /**
     *  Read an item from the database
     * @param client the client
     * @param sc the Scanner
     */
    private static void readItem(Client client,Scanner sc,String mode){
        String key;
        do{
            System.out.println("Enter the key of the item you want to retrieve");
            key = sc.next();
            if(key.trim()==""){
                System.out.println("Invalid key. Please try again.");
            }
        }while(key.trim()=="");
        client.createRequest(new Command(key,"read",mode),"primaryNode","clientCLI");
        client.sendReqclient();
    }

    /**
     * Start the clientCLI
     * Command: java -jar clientCLI.jar
     * @param args:"Ip Address" to connect to the server with that ip
     */
    public static void main(String[]args){
        // masterNode: Init
        //primaryNode n1 =new primaryNode();
        //n1.serverStart();
        //slaveNode: Init
        //secondaryNode n2 =new secondaryNode();
       // n2.serverStart();
        Client cl = new Client("35.158.189.187",80);
        Scanner sc = new Scanner(System.in);
       while(true){
        clientCLI.printUserMenu(cl,sc);}
    }
}
