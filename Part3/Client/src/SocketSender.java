import java.io.*;
import java.net.*;

public class SocketSender implements Runnable
{
    private static Socket clientSocket = null;
    private static DataOutputStream outputStream = null;
    private static BufferedReader inputStream = null;
    private static String responseLine;
    private static ObjectOutput objectOutput = null;
    private static ObjectInput objectInput = null;

    public static DisplayPanel displayPanel = null;

    private static String serverHost = "localhost";

    private static int ownKart;
    private static int otherKart;

    public void run()
    {
        try
        {
            serverHost = displayPanel.ipBox.getText();
            int serverPort = Integer.parseInt(displayPanel.portBox.getText());
            clientSocket = new Socket(serverHost, serverPort);
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            objectInput = new ObjectInputStream(clientSocket.getInputStream());
            objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());

        }
        catch (UnknownHostException e)
        {
            System.err.println("Unknown host error: " + e);
        }
        catch (IOException e)
        {
            System.err.println("IO Exception: " + e);
        }

        if (clientSocket != null && outputStream != null && inputStream != null && objectOutput != null && objectInput != null)
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
                objectOutput.close();
                objectInput.close();
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
        //SendKart();

        //SendMessage("ping");
    }

    public static void SendOwnKart()
    {
        SendMessage("update_kart");
        SendKart();
        ReceiveOtherPlayers();
    }

    private static void SendKart()
    {
        try
        {
            objectOutput.writeObject(displayPanel.racers[displayPanel.player]);
            objectOutput.flush();
        }
        catch (Exception e)
        {

        }
    }

    private static void ReceiveOtherPlayers()
    {
        try
        {
            displayPanel.racers[otherKart] = (Kart) objectInput.readObject();
        }
        catch (Exception e)
        {

        }
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
        System.out.println(response);
        switch (response)
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

                SendMessage("kart");
                SendKart();

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

                SendMessage("kart");
                SendKart();

                displayPanel.repaint();
                displayPanel.waitingLabel.setVisible(false);
                displayPanel.exitButton.setEnabled(false);
                displayPanel.exitButton.setVisible(false);
                SendMessage("start");
                break;
            case "start":
                displayPanel.Start();
                break;
            case "win":
                displayPanel.Win();
                break;
            case "collision":
                displayPanel.Crashed();
                break;
            case "other_kart":
                ReceiveOtherPlayers();

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
