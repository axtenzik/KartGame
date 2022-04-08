import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

public class KartPanel extends JPanel implements KeyListener
{
    private final int kartFrames = 16;
    private final static String kart1Name = "Green";
    private final static String kart2Name = "Green";
    private final int[] kart1Position = {375, 500};
    private final int[] kart2Position = {375, 550};
    private final int[] prevKart1pos = {375, 500};
    private final int[] prevKart2pos = {375, 550};
    private int kart1Checkpoint = 0;
    private int kart2Checkpoint = 0;
    private int kart1Speed = 0;
    private int kart2Speed = 0;
    private int currentKart1 = 4;
    private int currentKart2 = 4;
    protected ImageIcon[] kart1;
    protected ImageIcon[] kart2;
    private final double speedFactor = 0.2;
    private final int timeStep = 20;
    private int AIDelay = 0;
    private int AIWait = 100;


    KartPanel()
    {
        //this.setBounds(0, 0, 200, 200);
        setPreferredSize(new Dimension(850, 650));
        this.setLayout(null);
        //Components added here

        //LoadImages
        kart1 = new ImageIcon[kartFrames];
        kart2 = new ImageIcon[kartFrames];

        for (int i = 0; i < kart1.length; i++)
        {
            kart1[i] = new ImageIcon(Objects.requireNonNull(getClass().getResource(kart1Name + i + ".png")));
            kart2[i] = new ImageIcon(Objects.requireNonNull(getClass().getResource(kart2Name + i + ".png")));
        }

        //Sets timer for 100 milliseconds
        Timer t = new Timer(timeStep, this::actionPerformed);
        t.start();
    }

    //There are definitely better ways of doing this instead of 2 switches with each case, might change later
    private void EvaluatePosition()
    {
        //prevKart1pos = kart1Position;
        //prevKart2pos = kart2Position;
        //Length of sides of triangle with hypotenuse length 1
        //45 degrees each side 0.707
        //22.5 degrees:
        //0.383
        //0.924
        switch (currentKart1)
        {
            case 0 -> kart1Position[1] = (int)Math.floor(kart1Position[1] - (kart1Speed * speedFactor));
            case 1 -> {
                kart1Position[0] = (int) Math.ceil(kart1Position[0] + (0.383 * kart1Speed * speedFactor));
                kart1Position[1] = (int) Math.floor(kart1Position[1] - (0.924 * kart1Speed * speedFactor));
            }
            case 2 -> {
                kart1Position[0] = (int) Math.ceil(kart1Position[0] + (0.707 * kart1Speed * speedFactor));
                kart1Position[1] = (int) Math.floor(kart1Position[1] - (0.707 * kart1Speed * speedFactor));
            }
            case 3 -> {
                kart1Position[0] = (int) Math.ceil(kart1Position[0] + (0.924 * kart1Speed * speedFactor));
                kart1Position[1] = (int) Math.floor(kart1Position[1] - (0.383 * kart1Speed * speedFactor));
            }
            case 4 -> kart1Position[0] = (int)Math.ceil(kart1Position[0] + (kart1Speed * speedFactor));
            case 5 -> {
                kart1Position[0] = (int) Math.ceil(kart1Position[0] + (0.924 * kart1Speed * speedFactor));
                kart1Position[1] = (int) Math.ceil(kart1Position[1] + (0.383 * kart1Speed * speedFactor));
            }
            case 6 -> {
                kart1Position[0] = (int) Math.ceil(kart1Position[0] + (0.707 * kart1Speed * speedFactor));
                kart1Position[1] = (int) Math.ceil(kart1Position[1] + (0.707 * kart1Speed * speedFactor));
            }
            case 7 -> {
                kart1Position[0] = (int) Math.ceil(kart1Position[0] + (0.383 * kart1Speed * speedFactor));
                kart1Position[1] = (int) Math.ceil(kart1Position[1] + (0.924 * kart1Speed * speedFactor));
            }
            case 8 -> kart1Position[1] = (int)Math.ceil(kart1Position[1] + (kart1Speed * speedFactor));
            case 9 -> {
                kart1Position[0] = (int) Math.floor(kart1Position[0] - (0.383 * kart1Speed * speedFactor));
                kart1Position[1] = (int) Math.ceil(kart1Position[1] + (0.924 * kart1Speed * speedFactor));
            }
            case 10 -> {
                kart1Position[0] = (int) Math.floor(kart1Position[0] - (0.707 * kart1Speed * speedFactor));
                kart1Position[1] = (int) Math.ceil(kart1Position[1] + (0.707 * kart1Speed * speedFactor));
            }
            case 11 -> {
                kart1Position[0] = (int) Math.floor(kart1Position[0] - (0.924 * kart1Speed * speedFactor));
                kart1Position[1] = (int) Math.ceil(kart1Position[1] + (0.383 * kart1Speed * speedFactor));
            }
            case 12 -> kart1Position[0] = (int)Math.floor(kart1Position[0] - (kart1Speed * speedFactor));
            case 13 -> {
                kart1Position[0] = (int) Math.floor(kart1Position[0] - (0.924 * kart1Speed * speedFactor));
                kart1Position[1] = (int) Math.floor(kart1Position[1] - (0.383 * kart1Speed * speedFactor));
            }
            case 14 -> {
                kart1Position[0] = (int) Math.floor(kart1Position[0] - (0.707 * kart1Speed * speedFactor));
                kart1Position[1] = (int) Math.floor(kart1Position[1] - (0.707 * kart1Speed * speedFactor));
            }
            case 15 -> {
                kart1Position[0] = (int) Math.floor(kart1Position[0] - (0.383 * kart1Speed * speedFactor));
                kart1Position[1] = (int) Math.floor(kart1Position[1] - (0.924 * kart1Speed * speedFactor));
            }
        }
        switch (currentKart2)
        {
            case 0 -> kart2Position[1] = (int)Math.floor(kart2Position[1] - (kart2Speed * speedFactor));
            case 1 -> {
                kart2Position[0] = (int) Math.ceil(kart2Position[0] + (0.383 * kart2Speed * speedFactor));
                kart2Position[1] = (int) Math.floor(kart2Position[1] - (0.924 * kart2Speed * speedFactor));
            }
            case 2 -> {
                kart2Position[0] = (int) Math.ceil(kart2Position[0] + (0.707 * kart2Speed * speedFactor));
                kart2Position[1] = (int) Math.floor(kart2Position[1] - (0.707 * kart2Speed * speedFactor));
            }
            case 3 -> {
                kart2Position[0] = (int) Math.ceil(kart2Position[0] + (0.924 * kart2Speed * speedFactor));
                kart2Position[1] = (int) Math.floor(kart2Position[1] - (0.383 * kart2Speed * speedFactor));
            }
            case 4 -> kart2Position[0] = (int)Math.ceil(kart2Position[0] + (kart2Speed * speedFactor));
            case 5 -> {
                kart2Position[0] = (int) Math.ceil(kart2Position[0] + (0.924 * kart2Speed * speedFactor));
                kart2Position[1] = (int) Math.ceil(kart2Position[1] + (0.383 * kart2Speed * speedFactor));
            }
            case 6 -> {
                kart2Position[0] = (int) Math.ceil(kart2Position[0] + (0.707 * kart2Speed * speedFactor));
                kart2Position[1] = (int) Math.ceil(kart2Position[1] + (0.707 * kart2Speed * speedFactor));
            }
            case 7 -> {
                kart2Position[0] = (int) Math.ceil(kart2Position[0] + (0.383 * kart2Speed * speedFactor));
                kart2Position[1] = (int) Math.ceil(kart2Position[1] + (0.924 * kart2Speed * speedFactor));
            }
            case 8 -> kart2Position[1] = (int)Math.ceil(kart2Position[1] + (kart2Speed * speedFactor));
            case 9 -> {
                kart2Position[0] = (int) Math.floor(kart2Position[0] - (0.383 * kart2Speed * speedFactor));
                kart2Position[1] = (int) Math.ceil(kart2Position[1] + (0.924 * kart2Speed * speedFactor));
            }
            case 10 -> {
                kart2Position[0] = (int) Math.floor(kart2Position[0] - (0.707 * kart2Speed * speedFactor));
                kart2Position[1] = (int) Math.ceil(kart2Position[1] + (0.707 * kart2Speed * speedFactor));
            }
            case 11 -> {
                kart2Position[0] = (int) Math.floor(kart2Position[0] - (0.924 * kart2Speed * speedFactor));
                kart2Position[1] = (int) Math.ceil(kart2Position[1] + (0.383 * kart2Speed * speedFactor));
            }
            case 12 -> kart2Position[0] = (int)Math.floor(kart2Position[0] - (kart2Speed * speedFactor));
            case 13 -> {
                kart2Position[0] = (int) Math.floor(kart2Position[0] - (0.924 * kart2Speed * speedFactor));
                kart2Position[1] = (int) Math.floor(kart2Position[1] - (0.383 * kart2Speed * speedFactor));
            }
            case 14 -> {
                kart2Position[0] = (int) Math.floor(kart2Position[0] - (0.707 * kart2Speed * speedFactor));
                kart2Position[1] = (int) Math.floor(kart2Position[1] - (0.707 * kart2Speed * speedFactor));
            }
            case 15 -> {
                kart2Position[0] = (int) Math.floor(kart2Position[0] - (0.383 * kart2Speed * speedFactor));
                kart2Position[1] = (int) Math.floor(kart2Position[1] - (0.924 * kart2Speed * speedFactor));
            }
        }
    }

    private void EvaluateCollisions()
    {
        /*if (kart1Position[0] < 40 || 760 < kart1Position[0])
        {
            kart1Speed = 0;
            kart1Position[0] = Math.max(40, kart1Position[0]);
            kart1Position[0] = Math.min(760, kart1Position[0]);
        }
        if (kart1Position[1] < 90 || 560 < kart1Position[1])
        {
            kart1Speed = 0;
            kart1Position[1] = Math.max(90, kart1Position[1]);
            kart1Position[1] = Math.min(560, kart1Position[1]);
        }
        if ((110 < kart1Position[0] && kart1Position[0] < 690) && (160 < kart1Position[1] && kart1Position[1] < 490))
        {
            //kart1Speed = 0;
        }

        if (kart2Position[0] < 40 || 760 < kart2Position[0])
        {
            kart2Speed = 0;
            kart2Position[0] = Math.max(40, kart2Position[0]);
            kart2Position[0] = Math.min(760, kart2Position[0]);
        }
        if (kart2Position[1] < 90 || 560 < kart2Position[1])
        {
            kart2Speed = 0;
            kart2Position[1] = Math.max(90, kart2Position[1]);
            kart2Position[1] = Math.min(560, kart2Position[1]);
        }*/

        /*
        if prev position in box
        new position where expect

         */
        /*if (490 <= prevKart1pos[1] && prevKart1pos[1] <= 560)
        {
            if (prevKart1pos[0] <= 425 && kart1Position[0] > 425)
            {
                kart1Checkpoint += 1;
            }
            else if (prevKart1pos[0] >= 425 && kart1Position[0] < 425)
            {
                kart1Checkpoint -= 1;
            }
            if (kart1Position[0] < 40 || 760 < kart1Position[0])
            {
                kart1Speed = 0;
                kart1Position[0] = Math.max(40, kart1Position[0]);
                kart1Position[0] = Math.min(760, kart1Position[0]);
            }
            if (!(490 <= kart1Position[1] && kart1Position[1] <= 560))
            {
                if (690 <= kart1Position[0]) //due to previous if second part of statement is always true so been deleted
                {
                    kart1Checkpoint += 1;
                }
                else if (kart1Position[0] <= 110)
                {
                    kart1Checkpoint -= 1;
                }
                else
                {
                    kart1Speed = 0;
                    kart1Position[1] = Math.max(490, kart1Position[1]);
                    kart1Position[1] = Math.min(560, kart1Position[1]);
                }
            }
        }
        else if (690 <= prevKart1pos[0] && prevKart1pos[0] <= 760)
        {
            if (kart1Position[1] < 90 || 560 < kart1Position[1])
            {
                kart1Speed = 0;
                kart1Position[1] = Math.max(90, kart1Position[1]);
                kart1Position[1] = Math.min(560, kart1Position[1]);
            }
            if (!(690 <= kart1Position[0] && kart1Position[0] <= 760))
            {
                if (490 <= kart1Position[1] && kart1Position[1] <= 560)
                {
                    kart1Checkpoint -= 1;
                }
                else if (90 <= kart1Position[1] && kart1Position[1] <= 160)
                {
                    kart1Checkpoint += 1;
                }
                else
                {
                    kart1Speed = 0;
                    kart1Position[0] = Math.max(690, kart1Position[0]);
                    kart1Position[0] = Math.min(760, kart1Position[0]);
                }
            }
        }
        else if (90 <= prevKart1pos[1] && prevKart1pos[1] <= 160)
        {
            if (kart1Position[0] < 40 || 760 < kart1Position[0])
            {
                kart1Speed = 0;
                kart1Position[0] = Math.max(40, kart1Position[0]);
                kart1Position[0] = Math.min(760, kart1Position[0]);
            }
            if (!(90 <= kart1Position[1] && kart1Position[1] <= 160))
            {
                if (690 <= kart1Position[0])
                {
                    kart1Checkpoint -= 1;
                }
                else if (40 <= kart1Position[0] && kart1Position[0] <= 110)
                {
                    kart1Checkpoint += 1;
                }
                else
                {
                    kart1Speed = 0;
                    kart1Position[1] = Math.max(90, kart1Position[1]);
                    kart1Position[1] = Math.min(160, kart1Position[1]);
                }
            }
        }
        else if (40 <= prevKart1pos[0] && prevKart1pos[0] <= 110)
        {
            if (kart1Position[1] < 90 || 560 < kart1Position[1])
            {
                kart1Speed = 0;
                kart1Position[1] = Math.max(90, kart1Position[1]);
                kart1Position[1] = Math.min(560, kart1Position[1]);
            }
            if (!(40 <= kart1Position[0] && kart1Position[0] <= 110))
            {
                if (490 <= kart1Position[1] && kart1Position[1] <= 560)
                {
                    //I don't think this line of code ever runs
                    //as soon as it would get to this point, the first if statement will have run instead
                    kart1Checkpoint += 1;
                }
                else if (90 <= kart1Position[1] && kart1Position[1] <= 160)
                {
                    kart1Checkpoint -= 1;
                }
                else
                {
                    kart1Speed = 0;
                    kart1Position[0] = Math.max(40, kart1Position[0]);
                    kart1Position[0] = Math.min(110, kart1Position[0]);
                }
            }
        }
        else
        {
            kart1Speed = 0;
            kart1Position[0] = Math.max(40, kart1Position[0]);
            kart1Position[0] = Math.min(760, kart1Position[0]);
            kart1Position[1] = Math.max(90, kart1Position[1]);
            kart1Position[1] = Math.min(560, kart1Position[1]);
        }
        prevKart1pos[0] = kart1Position[0];
        prevKart1pos[1] = kart1Position[1];*/


        if (490 <= prevKart2pos[1] && prevKart2pos[1] <= 560)
        {
            if (prevKart2pos[0] <= 425 && kart2Position[0] > 425)
            {
                kart2Checkpoint += 1;
            }
            else if (prevKart2pos[0] >= 425 && kart2Position[0] < 425)
            {
                kart2Checkpoint -= 1;
            }
            if (kart2Position[0] < 40 || 760 < kart2Position[0])
            {
                kart2Speed = 0;
                kart2Position[0] = Math.max(40, kart2Position[0]);
                kart2Position[0] = Math.min(760, kart2Position[0]);
            }
            if (!(490 <= kart2Position[1] && kart2Position[1] <= 560))
            {
                if (690 <= kart2Position[0] && kart2Position[0] <= 760)
                {
                    kart2Checkpoint += 1;
                }
                else if (40 <= kart2Position[0] && kart2Position[0] <= 110)
                {
                    kart2Checkpoint -= 1;
                }
                else
                {
                    kart2Speed = 0;
                    kart2Position[1] = Math.max(490, kart2Position[1]);
                    kart2Position[1] = Math.min(560, kart2Position[1]);
                }
            }
        }
        else if (90 <= prevKart2pos[1] && prevKart2pos[1] <= 160)
        {
            if (kart2Position[0] < 40 || 760 < kart2Position[0])
            {
                kart2Speed = 0;
                kart2Position[0] = Math.max(40, kart2Position[0]);
                kart2Position[0] = Math.min(760, kart2Position[0]);
            }
            if (!(90 <= kart2Position[1] && kart2Position[1] <= 160))
            {
                if (690 <= kart2Position[0] && kart2Position[0] <= 760)
                {
                    kart2Checkpoint -= 1;
                }
                else if (40 <= kart2Position[0] && kart2Position[0] <= 110)
                {
                    kart2Checkpoint += 1;
                }
                else
                {
                    kart2Speed = 0;
                    kart2Position[1] = Math.max(90, kart2Position[1]);
                    kart2Position[1] = Math.min(160, kart2Position[1]);
                }
            }
        }
        else if (40 <= prevKart2pos[0] && prevKart2pos[0] <= 110)
        {
            if (kart2Position[1] < 90 || 560 < kart2Position[1])
            {
                kart2Speed = 0;
                kart2Position[1] = Math.max(90, kart2Position[1]);
                kart2Position[1] = Math.min(560, kart2Position[1]);
            }
            if (!(40 <= kart2Position[0] && kart2Position[0] <= 110))
            {
                if (490 <= kart2Position[1] && kart2Position[1] <= 560)
                {
                    kart1Checkpoint += 1;
                }
                else if (90 <= kart2Position[1] && kart2Position[1] <= 160)
                {
                    kart2Checkpoint -= 1;
                }
                else
                {
                    kart2Speed = 0;
                    kart2Position[0] = Math.max(40, kart2Position[0]);
                    kart2Position[0] = Math.min(110, kart2Position[0]);
                }
            }
        }
        else if (690 <= prevKart2pos[0] && prevKart2pos[0] <= 760)
        {
            if (kart2Position[1] < 90 || 560 < kart2Position[1])
            {
                kart2Speed = 0;
                kart2Position[1] = Math.max(90, kart2Position[1]);
                kart2Position[1] = Math.min(560, kart2Position[1]);
            }
            if (!(690 <= kart2Position[0] && kart2Position[0] <= 760))
            {
                if (490 <= kart2Position[1] && kart2Position[1] <= 560)
                {
                    kart2Checkpoint -= 1;
                }
                else if (90 <= kart2Position[1] && kart2Position[1] <= 160)
                {
                    kart2Checkpoint += 1;
                }
                else
                {
                    kart2Speed = 0;
                    kart2Position[0] = Math.max(690, kart2Position[0]);
                    kart2Position[0] = Math.min(760, kart2Position[0]);
                }
            }
        }
        else
        {
            kart2Speed = 0;
            kart2Position[0] = Math.max(40, kart2Position[0]);
            kart2Position[0] = Math.min(760, kart2Position[0]);
            kart2Position[1] = Math.max(90, kart2Position[1]);
            kart2Position[1] = Math.min(560, kart2Position[1]);
        }
        prevKart2pos[0] = kart2Position[0];
        prevKart2pos[1] = kart2Position[1];
    }

    private void EvColV2()
    {
        if (760 < kart1Position[0] || kart1Position[0] < 40)
        {
            kart1Speed = 0;
            kart1Position[0] = Math.max(40, Math.min(760, kart1Position[0]));
        }
        if (560 < kart1Position[1] || kart1Position[1] < 90)
        {
            kart1Speed = 0;
            kart1Position[1] = Math.max(90, Math.min(560, kart1Position[1]));
        }

        if (490 <= prevKart1pos[1] && prevKart1pos[1] <= 560)
        {
            if (prevKart1pos[0] <= 425 && kart1Position[0] > 425)
            {
                kart1Checkpoint += 1;
                System.out.println(kart1Checkpoint);
            }
            else if (prevKart1pos[0] >= 425 && kart1Position[0] < 425)
            {
                kart1Checkpoint -= 1;
                System.out.println(kart1Checkpoint);
            }
            if (!(490 <= kart1Position[1] && kart1Position[1] <= 560) && 110 < kart1Position[0] && kart1Position[0] < 690)
            {
                kart1Speed = 0;
                kart1Position[1] = Math.max(490, kart1Position[1]);
            }
        }
        else if (690 <= prevKart1pos[0] && prevKart1pos[0] <= 760)
        {
            if (!(690 <= kart1Position[0] && kart1Position[0] <= 760) && 160 < kart1Position[1] && kart1Position[1] < 490)
            {
                kart1Speed = 0;
                kart1Position[0] = Math.max(690, kart1Position[0]);
            }
        }
        else if (90 <= prevKart1pos[1] && prevKart1pos[1] <= 160)
        {
            if(!(90 <= kart1Position[1] && kart1Position[1] <= 160) && 110 < kart1Position[0] && kart1Position[0] < 690)
            {
                kart1Speed = 0;
                kart1Position[1] = Math.min(160, kart1Position[1]);
            }
        }
        else if (40 <= prevKart1pos[0] && prevKart1pos[0] <= 110)
        {
            if (!(40 <= kart1Position[0] && kart1Position[0] <= 110) && 160 < kart1Position[1] && kart1Position[1] < 490)
            {
                kart1Speed = 0;
                kart1Position[0] = Math.min(110, kart1Position[0]);
            }
        }
        else
        {
            System.out.println("Why is this triggering?");
        }
        prevKart1pos[0] = kart1Position[0];
        prevKart1pos[1] = kart1Position[1];
    }

    public void CheckWin()
    {
        /*if (kart1Checkpoint > 6)
        {
            System.out.println("kart 1 is winner");
        }*/
    }

    public void keyTyped(KeyEvent e)
    {

    }

    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_LEFT -> currentKart1 = (currentKart1 + 15) % kartFrames;
            case KeyEvent.VK_RIGHT -> currentKart1 = (currentKart1 + 1) % kartFrames;
            case KeyEvent.VK_UP -> kart1Speed = Math.min(kart1Speed + 10, 100);
            case KeyEvent.VK_DOWN -> kart1Speed = Math.max(kart1Speed - 10, 0);
            case KeyEvent.VK_A -> currentKart2 = (currentKart2 + 15) % kartFrames;
            case KeyEvent.VK_D -> currentKart2 = (currentKart2 + 1) % kartFrames;
            case KeyEvent.VK_W -> kart2Speed = Math.min(kart2Speed + 10, 100);
            case KeyEvent.VK_S -> kart2Speed = Math.max(kart2Speed - 10, 0);
        }
    }

    public void keyReleased(KeyEvent e)
    {

    }

    public void actionPerformed(ActionEvent e)
    {
        P2AI();
        EvaluatePosition();
        EvaluateCollisions();
        EvColV2();
        CheckWin();
        repaint();
    }

    private void P2AI()
    {
        AIDelay += timeStep;

        if (AIDelay < AIWait)
        {
            return;
        }

        if (500 <= kart2Position[1] && kart2Position[1] <= 550 && kart2Position[0] < 700)
        {
            if (currentKart2 == 4)
            {
                kart2Speed = 10;
            }
            else
            {
                AIDelay = 0;
                currentKart2 = (currentKart2 + 15) % kartFrames;
            }
        }
        else if (700 <= kart2Position[0] && kart2Position[0] <= 750 && kart2Position[1] > 150)
        {
            if (currentKart2 == 0)
            {
                kart2Speed = 10;
            }
            else
            {
                AIDelay = 0;
                currentKart2 = (currentKart2 + 15) % kartFrames;
            }
        }
        else if (100 <= kart2Position[1] && kart2Position[1] <= 150 && kart2Position[0] > 100)
        {
            if (currentKart2 == 12)
            {
                kart2Speed = 10;
            }
            else
            {
                AIDelay = 0;
                currentKart2 = (currentKart2 + 15) % kartFrames;
            }
        }
        else if (50 <= kart2Position[0] && kart2Position[0] <= 100 && kart2Position[1] < 500)
        {
            if (currentKart2 == 8)
            {
                kart2Speed = 10;
            }
            else
            {
                AIDelay = 0;
                currentKart2 = (currentKart2 + 15) % kartFrames;
            }
        }
    }

    public void paintComponent(Graphics g)
    {
        //display image
        super.paintComponent(g);

        g.setColor(Color.green);
        g.fillRect(0, 0, 850, 650);

        g.setColor(Color.gray);
        g.fillRect(50, 100, 750, 500);

        Color c1 = Color.green;
        g.setColor( c1 );
        g.fillRect( 150, 200, 550, 300 ); // grass

        Color c2 = Color.black;
        g.setColor( c2 );
        g.drawRect( 50, 100, 750, 500 );  // outer edge
        g.drawRect( 150, 200, 550, 300 ); // inner edge

        Color c3 = Color.yellow;
        g.setColor( c3 );
        g.drawRect( 100, 150, 650, 400 ); // mid-lane marker

        Color c4 = Color.white;
        g.setColor( c4 );
        g.drawLine( 425, 500, 425, 600 ); // start line

        kart1[currentKart1].paintIcon(this, g, kart1Position[0], kart1Position[1]);
        kart2[currentKart2].paintIcon(this, g, kart2Position[0], kart2Position[1]);
    }
}