package eu.openanalytics.japyter;

import static eu.openanalytics.japyter.client.Shell.InspectDetailLevel.FINE;
import static eu.openanalytics.japyter.model.gen.HistoryRequest.HistAccessType.TAIL;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.openanalytics.japyter.client.Shell;
import eu.openanalytics.japyter.client.IoPub.BroadcastListener;
import eu.openanalytics.japyter.client.IoPub.MessageListener;
import eu.openanalytics.japyter.client.Stdin.AbstractStdinHandler;
import eu.openanalytics.japyter.client.Stdin.StdinHandler;
import eu.openanalytics.japyter.model.Message;
import eu.openanalytics.japyter.model.gen.Broadcast;
import eu.openanalytics.japyter.model.gen.ExecuteRequest;
import eu.openanalytics.japyter.model.gen.HistoryRequest;

public class Test {
public static void main(String[] args) throws IOException {
	
	   final StdinHandler randomStdinHandler = new AbstractStdinHandler()
       {
           @Override
           public String prompt(final String text, final boolean password)
           {
               return RandomStringUtils.randomAlphanumeric(20);
           }
       };	
	try(Japyter japyter = Japyter.fromConfigFile(new File("c:\\temp\\connect.txt"))
            .withUserName("it-test")
            .withStdinHandler(randomStdinHandler)
            .build())
	{

       japyter.getIoPub().subscribe(new BroadcastListener()
       {
           @Override
           public void handle(final Broadcast b)
           {
               System.out.println("IoPub Broadcast: " + b);
           }
       });
       japyter.getIoPub().subscribe(new MessageListener()
       {
           @Override
           public void handle(final Message m)
           {
               System.out.println("IoPub Message: " + m);
           }
       });

       System.out.println("Heartbeat state: {}\n"+ japyter.getHeartbeat().getState());

       System.out.println("\n\nType ENTER to stop\n");

       System.out.println("Heartbeat state: {}\n"+ japyter.getHeartbeat().getState());
       System.out.println("\n\nStopping...");
   	Shell shell = japyter.getShell();
	   System.out.println("ExecuteReply: " + shell.execute(new ExecuteRequest().withCode("3+7")));
	   System.out.println("ExecuteReply: " + shell.execute(new ExecuteRequest().withCode("iconify(\"c:\\\\temp\\\\IMG_6597.JPG\",\"c:\\\\temp\\\\icon3.jpg\",20,20)")));
	   
    System.out.println("InspectReply: " + shell.inspect("123.", 4, FINE));
    System.out.println("CompleteReply: " + shell.complete("123.", 4));
    System.out.println("IsCompleteReply: " + shell.isComplete("123."));
    System.out.println("HistoryReply: "
                + shell.history(new HistoryRequest().withHistAccessType(TAIL).withN(10)));

    System.out.println("\n\n*** CONTROL TESTS ***\n");
    System.out.println("ConnectReply: " + japyter.getControl().shutdown(true));

    System.out.println("\n\n*** IO_PUB TESTS ***\n");
    waitEnter();
   }
   finally
   {
	   
       System.exit(0);
   }

    
}
private static void waitEnter()
{
    try (Scanner s = new Scanner(System.in))
    {
        s.nextLine();
    }
}
}
