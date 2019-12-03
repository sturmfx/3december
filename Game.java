package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;

public class Game extends JFrame implements KeyListener
{
    public static int width = 800;
    public static int height = 800;
    public static boolean up = false;
    public static boolean down = false;
    public static boolean right = false;
    public static boolean left = false;
    public static long tick_in_nano = 1000000;
    public static long time1 = System.nanoTime();
    public static long time2;
    public static int tick_counter = 10;
    public static int temp_tick_counter = tick_counter;
    public static int spawn_counter = 1000;
    public static int temp_spawn_counter = spawn_counter;
    public static int hp = 200;
    public static int p_width = 50;
    public static int p_height = 50;
    public static double x = width/2;
    public static double y = height/2;
    public static double speed = 0.2;
    public static Color color = Color.BLUE;

    public static int bullet_damage  = 101;
    public static double bullet_speed = 5.0;
    public static long bullet_life = 10000000000L;
    public static int bullet_width = 20;
    public static int bullet_height = 20;
    public static Color bullet_color = Color.GREEN;

    public static int npc_rate_of_fire = 1000;
    public static int npc_damage = 10;
    public static int npc_hp = 300;
    public static double npc_speed = 0.1;
    public static int npc_width = 100;
    public static int npc_height = 100;


    public static Canvas canvas = new Canvas();
    public static JLabel shoots_firedl = new JLabel("Shoots fired: ");
    public static JLabel shoots_hitl = new JLabel("Shoots hit: ");
    public static JLabel enemy_destroyedl = new JLabel("Enemies destroyed: ");
    public static JLabel total_enemiesl = new JLabel("Total enemies: ");
    public static JFrame statistics = new JFrame();


    public BufferStrategy bufferStrategy;
    public Graphics g;
    public static boolean continue_game = true;

    public static int shoots_fired = 0;
    public static int shoots_hit = 0;
    public static int enemy_destroyed = 0;
    public static int total_enemies = 0;

    @Override
    public void keyTyped(KeyEvent e)
    {
        if (e.getKeyChar() == 'p')
        {
            continue_game = !continue_game;
        }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(continue_game)
        {
            if (e.getKeyCode() == KeyEvent.VK_D)
            {
                right = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_A)
            {
                left = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_W)
            {
                up = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_S)
            {
                down = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if(continue_game)
        {
            if (e.getKeyCode() == KeyEvent.VK_D)
            {
                right = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_A)
            {
                left = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_W)
            {
                up = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_S)
            {
                down = false;
            }
        }
    }

    public Game()
    {
        super();
        canvas.setSize(width, height);
        canvas.setVisible(true);
        canvas.setFocusable(false);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        statistics.setLayout(new FlowLayout());
        statistics.setSize(200, 200);
        statistics.add(shoots_firedl);
        statistics.add(shoots_hitl);
        statistics.add(total_enemiesl);
        statistics.add(enemy_destroyedl);

        add(canvas);
        addKeyListener(this);
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        statistics.setResizable(false);
        statistics.setVisible(true);
        statistics.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        canvas.createBufferStrategy(4);
        canvas.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {

            }
            @Override
            public void mousePressed(MouseEvent e)
            {
                if(continue_game)
                {
                    newPlayerBullet(e);
                    shoots_fired++;
                }
            }
        });

        loop();
    }

    public static void newPlayerBullet(MouseEvent e)
    {
        Bullet.newPlayerBullet(e);
    }

    public static void generateNPC()
    {
        NPC.generateNPC(Color.RED, npc_speed, npc_hp, npc_damage, 20000000000L, npc_rate_of_fire, (int) x, (int) y, npc_width, npc_height);
        total_enemies++;
    }

    public void loop()
    {
        long a = 0;
        while(true)
        {
            while (continue_game && hp > 0)
            {
                time2 = System.nanoTime();
                if (time2 - time1 > tick_in_nano)
                {
                    temp_tick_counter--;
                    time1 = time2;
                    temp_spawn_counter--;
                    if (temp_spawn_counter < 0)
                    {
                        temp_spawn_counter = spawn_counter;
                        generateNPC();
                    }
                    if (up && !down && !right && !left && ((int) y > -1))
                    {
                        y = y - speed;
                    }
                    if (!up && down && !right && !left && (y < Game.height - p_height * 0.75))
                    {
                        y = y + speed;
                    }
                    if (!up && !down && right && !left && ((int) x < Game.width - p_width * 0.25))
                    {
                        x = x + speed;
                    }
                    if (!up && !down && !right && left && ((int) x > -1))
                    {
                        x = x - speed;
                    }

                    if (up && !down && right && !left && ((int) y > -1) && ((int) x < Game.width - p_width * 0.25))
                    {
                        y = y - speed;
                        x = x + speed;
                    }
                    if (!up && down && right && !left && (y < Game.height - p_height * 0.75) && ((int) x < Game.width - p_width * 0.25))
                    {
                        y = y + speed;
                        x = x + speed;
                    }
                    if (up && !down && !right && left && ((int) y > -1) && ((int) x > -1))
                    {
                        y = y - speed;
                        x = x - speed;
                    }
                    if (!up && down && !right && left && (y < Game.height - p_height * 0.75) && ((int) x > -1))
                    {
                        y = y + speed;
                        x = x - speed;
                    }

                    NPC.updateAllNPC();
                    Bullet.updateAllBullet();

                    if (temp_tick_counter < 0)
                    {
                        temp_tick_counter = tick_counter;
                        bufferStrategy = canvas.getBufferStrategy();
                        g = bufferStrategy.getDrawGraphics();
                        g.clearRect(0, 0, width, height);
                        g.setColor(color);
                        g.fillOval((int) x - p_width / 2, (int) y - p_height / 2, p_width, p_height);
                        g.setColor(Color.BLACK);
                        g.drawString(String.valueOf(hp), (int) x, (int) y);
                        NPC.draw_all(g);
                        Bullet.draw_all(g);
                        bufferStrategy.show();
                        g.dispose();

                        shoots_firedl.setText("Shoots fired: " + shoots_fired);
                        shoots_hitl.setText("Shoots hit: " + shoots_hit);
                        enemy_destroyedl.setText("Enemies destroyed: " + enemy_destroyed);
                        total_enemiesl.setText("Total enemies: " + total_enemies);

                    }

                }

            }
            a++;
        }
    }
}
