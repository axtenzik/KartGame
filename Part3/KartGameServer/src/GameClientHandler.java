import java.io.*;
import java.net.*;

public class GameClientHandler implements Runnable
{
    private Socket server;

    private BufferedReader inputStream;
    private String line;
    private DataOutputStream outputStream;

    private static int[] p1Kart = {0, 0, 0, 0};
    private static int[] p2Kart = {0, 0, 0, 0};

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
        if (player == "p1")
        {
            SendMessage("other_kart " + p2Kart[0] + " " + p2Kart[1] + " " + p2Kart[2] + " " + p2Kart[3]);
        }
        else
        {
            SendMessage("other_kart " + p1Kart[0] + " " + p1Kart[1] + " " + p1Kart[2] + " " + p1Kart[3]);
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
        String[] responseParts = response.split(" ");

        switch (responseParts[0])
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
            case "update_kart":
                if (player == "p1")
                {
                    p1Kart[0] = Integer.parseInt(responseParts[1]);
                    p1Kart[1] = Integer.parseInt(responseParts[2]);
                    p1Kart[2] = Integer.parseInt(responseParts[3]);
                    p1Kart[3] = Integer.parseInt(responseParts[4]);
                }
                else
                {
                    p2Kart[0] = Integer.parseInt(responseParts[1]);
                    p2Kart[1] = Integer.parseInt(responseParts[2]);
                    p2Kart[2] = Integer.parseInt(responseParts[3]);
                    p2Kart[3] = Integer.parseInt(responseParts[4]);
                }
                SendKart();

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