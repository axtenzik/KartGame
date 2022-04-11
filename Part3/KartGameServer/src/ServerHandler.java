import java.io.IOException;
import java.net.*;

public class ServerHandler
{
    private static GameClientHandler[] handlers;
    private static int maxClients = 2;
    private static int activeClients = 0;
    public static int player = 0;

    public static void Handler()
    {
        ServerSocket service = null;
        Socket server;

        handlers = new GameClientHandler[maxClients];

        try
        {
            service = new ServerSocket(5000);
        }
        catch (IOException e)
        {
            System.err.println("IO Exception: " + e);
        }

        try
        {
            while (activeClients < maxClients)
            {
                server = service.accept();

                GameClientHandler handler = new GameClientHandler(server);

                Thread t = new Thread(handler);
                t.start();

                handlers[activeClients] = handler;

                activeClients++;
                System.out.println(activeClients);
            }
        }
        catch (IOException e)
        {
            System.err.println("IO Exception: " + e);
        }

        while (true)
        {
            boolean allClientsAreActive = false;

            for (int i = 0; i < activeClients; i++)
            {
                GameClientHandler handler = handlers[i];

                if (handler.isAlive())
                {
                    allClientsAreActive = true;
                    break;
                }
            }

            if (!allClientsAreActive)
            {
                break;
            }

            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {

            }
        }
    }

    public static void StartGame()
    {
        for (int i = 0; i < activeClients; i++)
        {
            GameClientHandler handler = handlers[i];

            handler.SendMessage("start");
        }
    }

    public static void Collision()
    {
        for (int i = 0; i < activeClients; i++)
        {
            GameClientHandler handler = handlers[i];

            handler.SendMessage("collision");
        }
    }

    public static void GameWon()
    {
        for (int i = 0; i < activeClients; i++)
        {
            GameClientHandler handler = handlers[i];

            handler.SendMessage("won");
        }
    }

    public static void CloseAll()
    {
        for (int i = 0; i < activeClients; i++)
        {
            GameClientHandler handler = handlers[i];

            handler.SendMessage("CLOSE");
        }
        System.exit(0);
    }
}
