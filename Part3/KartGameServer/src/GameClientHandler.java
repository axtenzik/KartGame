import java.io.*;
import java.net.*;

public class GameClientHandler implements Runnable
{
    private Socket server;

    private BufferedReader inputStream;
    private String line;
    private DataOutputStream outputStream;

    private ObjectOutput objectOutput = null;
    private ObjectInput objectInput = null;

    private static Kart p1Kart = null;
    private static Kart p2Kart = null;

    private String player;

    private boolean alive = true;

    GameClientHandler(Socket socketServer)
    {
        server = socketServer;
    }

    public boolean isAlive()
    {
        return alive;
    }

    public void run()
    {
        try
        {
            inputStream = new BufferedReader(new InputStreamReader(server.getInputStream()));
            outputStream = new DataOutputStream(server.getOutputStream());
            objectInput = new ObjectInputStream(server.getInputStream());
            objectOutput = new ObjectOutputStream(server.getOutputStream());

            while (true)
            {
                line = ReceiveMessage();

                if (line != null)
                {
                    HandleClientResponse(line);
                    if (line.equals("CLOSE"))
                    {
                        ServerHandler.CloseAll();
                        break;
                    }
                }

                try
                {
                    Thread.sleep(1);
                }
                catch(InterruptedException e)
                {

                }
            }

            outputStream.close();
            inputStream.close();
            objectOutput.close();
            objectInput.close();
            server.close();
        }
        catch (Exception e)
        {
            System.err.println(e);
        }

        alive = false;
    }


    public void SendMessage(String message)
    {
        try
        {
            outputStream.writeBytes(message + "\n");
        }
        catch (Exception e)
        {

        }
    }

    private void SendKart()
    {

    }

    private void SendOtherKart()
    {
        SendMessage("other_kart");

        Kart send = null;

        switch (player)
        {
            case "p1":
                send = p2Kart;
                break;
            case "p2":
                send = p1Kart;
                break;
        }

        try
        {
            objectOutput.writeObject(send);
            objectOutput.flush();
        }
        catch (Exception e)
        {

        }
    }


    private void ReceiveKart()
    {
        Kart input = null;

        try
        {
            input = (Kart) objectInput.readObject();
            switch (player)
            {
                case "p1":
                    p1Kart = input;
                    break;
                case "p2":
                    p2Kart = input;
                    break;
            }
        }
        catch (Exception e)
        {

        }
    }

    private String ReceiveMessage()
    {
        try
        {
            return inputStream.readLine();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private void HandleClientResponse(String response)
    {
        //String[] responseParts = response.split(" ");
        System.out.println(response);

        switch (response)
        {
            case "ping":
                try
                {
                    Thread.sleep(1000);
                }
                catch (Exception e)
                {

                }

                SendMessage("pong");

                break;
            case "request":
                //Return player number
                if (ServerHandler.player == 0)
                {
                    player = "p1";
                    SendMessage(player);
                    ServerHandler.player++;
                }
                else if (ServerHandler.player == 1)
                {
                    player = "p2";
                    SendMessage(player);
                    ServerHandler.player++;
                }
                else
                {
                    SendMessage("full");
                }

                break;
            case "start":
                ServerHandler.StartGame();

                break;
            case "kart":
                ReceiveKart();

                break;
            case "update_kart":
                ReceiveKart();
                SendOtherKart();

                break;
            case "collision":
                ServerHandler.Collision();

                break;
            case "win":
                //send win to clients
                break;
        }
    }
}