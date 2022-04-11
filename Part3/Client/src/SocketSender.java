import java.io.*;
import java.net.*;

public class SocketSender implements Runnable
{
    private static Socket clientSocket = null;
    private static DataOutputStream outputStream = null;
    private static BufferedReader inputStream = null;
    private static String responseLine;

    public static DisplayPanel displayPanel = null;

    private static String serverHost = "localhost";

    private static int ownKart = 0;
    private static int otherKart = 1;

    public void run()
    {
        try
        {
            serverHost = displayPanel.ipBox.getText();
            int serverPort = Integer.parseInt(displayPanel.portBox.getText());
            clientSocket = new Socket(serverHost, serverPort);
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        }
        catch (UnknownHostException e)
        {
            System.err.println("Unknown host error: " + e);
        }
        catch (IOException e)
        {
            System.err.println("IO Exception: " + e);
        }
        catch (Exception e)
        {
            System.err.println("Exception: " + e);
        }

        if (clientSocket != null && outputStream != null && inputStream != null)
        {
            try
            {
                Initialise();

                while (true)
                {
                    responseLine = ReceiveMessage();

                    if (responseLine != null)
                    {
                        HandleServerResponse(responseLine);
                        if (responseLine.equals("CLOSE"))
                        {
                            break;
                        }
                    }
                }

                outputStream.close();
                inputStream.close();
                clientSocket.close();

                displayPanel.CloseClient();
            }
            catch (UnknownHostException e)
            {
                System.err.println("Unknown host exception: " + e);
            }
            catch (IOException e)
            {
                System.err.println("IO Exception: " + e);
            }
        }
        else
        {
            System.err.println("null error");
        }
    }

    private static void Initialise()
    {
        SendMessage("request");
    }

    public static void SendOwnKart()
    {
        int kartX = displayPanel.racers[ownKart].kartPosition[0];
        int kartY = displayPanel.racers[ownKart].kartPosition[1];
        int kartAngle = displayPanel.racers[ownKart].kartAngle;
        int kartSpeed = displayPanel.racers[ownKart].kartSpeed;
        SendMessage("update_kart " + kartX + " " + kartY + " " + kartAngle + " " + kartSpeed);
    }


    public static void SendMessage(String message)
    {
        try
        {
            outputStream.writeBytes(message + "\n");
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }

    private static String ReceiveMessage()
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

    private static void HandleServerResponse(String response)
    {
        String[] responseParts = response.split(" ");
        switch (responseParts[0])
        {
            case "pong":
                try
                {
                    Thread.sleep(1000);
                    System.out.println(response);
                }
                catch (Exception e)
                {

                }

                SendMessage("ping");

                break;
            case "p1":
                displayPanel.player = 0;
                ownKart = 0;
                otherKart = 1;
                System.out.println(response);

                displayPanel.repaint();
                displayPanel.waitingLabel.setVisible(true);
                displayPanel.exitButton.setBounds(250, 375, 350, 75);
                displayPanel.exitButton.setEnabled(true);
                displayPanel.exitButton.setVisible(true);
                break;
            case "p2":
                displayPanel.player = 1;
                ownKart = 1;
                otherKart = 0;

                displayPanel.repaint();
                SendMessage("start");
                break;
            case "start":
                displayPanel.Start();
                displayPanel.waitingLabel.setVisible(false);
                displayPanel.exitButton.setEnabled(false);
                displayPanel.exitButton.setVisible(false);
                break;
            case "win":
                displayPanel.Win();
                break;
            case "collision":
                displayPanel.Crashed();
                break;
            case "other_kart":
                displayPanel.racers[otherKart].kartPosition[0] = Integer.parseInt(responseParts[1]);
                displayPanel.racers[otherKart].kartPosition[1] = Integer.parseInt(responseParts[2]);
                displayPanel.racers[otherKart].prevKartPos[0] = Integer.parseInt(responseParts[1]);
                displayPanel.racers[otherKart].prevKartPos[1] = Integer.parseInt(responseParts[2]);
                displayPanel.racers[otherKart].kartAngle = Integer.parseInt(responseParts[3]);
                displayPanel.racers[otherKart].kartSpeed = Integer.parseInt(responseParts[4]);

                break;
        }
    }

    private static void ShutdownClient()
    {
        try
        {
            outputStream.close();
            inputStream.close();
            clientSocket.close();
        }
        catch (IOException e)
        {

        }
    }
}
