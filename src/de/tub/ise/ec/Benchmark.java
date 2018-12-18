package de.tub.ise.ec;

public class Benchmark {
	/**
	 * Start the benchmark
     * Command: java -jar benchnark.jar
	 * @param args : args
	 * @throws InterruptedException :Exception
	 */
	public static void main(String[] args) throws InterruptedException {
		// masterNode: Init
		//primaryNode n1 =new primaryNode();
		//n1.serverStart();
		//slaveNode: Init
		//secondaryNode n2 =new secondaryNode();
		//n2.serverStart();
		//Client: init
        //Client sync = new Client("127.0.0.1",8080);

        /*Testing The Sync Mode*/
        Client sync = new Client("35.158.189.187",80);
		System.out.println("Sending Requests in Sync Mode\n");
		for(int i=0;i<100;i++)
		{System.out.println("Starting Request Number: "+i);
			sync.createRequest(new Command("monkey","banana"+i,"update","Sync"),"primaryNode","client");
		sync.sendReqclient();
		Thread.sleep(1000);}

		/*Testing The Async Mode*/
        //Client async = new Client("127.0.0.1",8080);
		Client async = new Client("35.158.189.187",80);
		System.out.println("Sending Requests in Async Mode\n");
		for(int i=100;i<200;i++)
		{System.out.println("Starting Request Number: "+i);
            async.createRequest(new Command("monkey","banana"+i,"update","Async"),"primaryNode","client");
            async.sendReqclient();
			Thread.sleep(1000);}

	}
}
