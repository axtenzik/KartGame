import java.io.*;
import java.net.*;

public class GameClientHandler implements Runnable
{
    private Socket server;

    private BufferedReader inputStream;
    private String line;
    private DataOutputStream outputStream;

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

        }

        alive = false;
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

    private void ReceiveKart()
    {

    }

    private void HandleClientResponse(String response)
    {
        String[] responseParts = response.split(" ");
        System.out.println(response);

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
                //Return player number
                if (ServerHandler.player == 0)
                {
                    SendMessage("p1");
                    ServerHandler.player++;
                }
                else if (ServerHandler.player == 1)
                {
                    SendMessage("p2");
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
                // update position of player kart
                ReceiveKart();
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