package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


public class Game extends JFrame {
    private static final long serialVersionUID = 1L;

    Scores ThyScores;

    float totalHealth = 10;
    float totalPlanesSkipped = 20;


    private JPanel WelcomePane;
    private JPanel PlayPane;
    private JPanel SettingsPane;
    private JPanel CreditsPane;

    private JPanel SelectionPane;
    private PANE StartPane;
    private JPanel FinalPane;

    private JPanel ScoreCont;
    private JLabel Name1;
    private JLabel Name2;
    private JLabel Name3;
    private JLabel Name4;
    private JLabel Score1;
    private JLabel Score2;
    private JLabel Score3;
    private JLabel Score4;

    JLabel iconMas;
    JButton Exit[] = new JButton[4];
    // // // // // //	WELCOME
    JButton Start;
    JButton Settings;
    JButton Credits;
    // // // // // //	SELECTION
    JLabel lblName;
    JTextField field;
    JButton rht;
    JButton lft;
    JLabel Display;
    int planeIndex = 1;
    // // // // // //	START
    JProgressBar Myhealth;
    JLabel Score;
    JProgressBar PlanesLeft;
    JProgressBar Bosshealth;
    JLabel StartPaneBck;
    JButton Play;
    Player Player;
    // // // // // //	FINAL
    JLabel newhighscore;
    JLabel Result;
    // // // // // //	CREDITS
    JLabel MainDisplay;
    // // // // // //	SETTINGS
    JToggleButton MouseSet;

    boolean isMouseSet;

    int X = 175, Y = 500;
    int MouseX = 0, MouseY = 0;
    int seaFrame = 0;
    int liveBullets = 0;
    int bulletXs[] = new int[50];
    int bulletYs[] = new int[50];
    int eliveBullets = 0;
    int ebulletXs[] = new int[50];
    int ebulletYs[] = new int[50];
    int liveEnemies = 0;
    int EnemiesDown = 0;
    int enemyXs[] = new int[20];
    int enemyYs[] = new int[20];

    int normalEnemyArray[] = new int[]{1, 2, 1, 3, 1, 4, 2, 3, 2, 4};
    int waveNumber = 1;
    int bossX, bossY;
    boolean bossEvent = false;

    boolean isRightDown = false;
    boolean is1Down = false;
    boolean isLeftDown = false;
    boolean isSlashDown = false;
    boolean isUpDown = false;
    boolean isDownDown = false;
    boolean isSpaceUp = true;
    boolean isGameGoingOn = false;

    long score = 0;
    float maxPlaneSkipped = totalPlanesSkipped;

    // // // // // //

    public static void main(String[] args) {
        new Game();
    }

    public Game() {

        ThyScores = new Scores();
        try {
            Loader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            ThyScores = Loader.getScores();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        initializeGlobalSystem();

        initializeWelcomeScreen();

        initializePlayScreen();

        initializeSettingsScreen();

        initializeCreditsScreen();

        this.setFocusable(true);
        this.requestFocus();
        this.requestFocusInWindow();

    }

    private void initializeGameInput() {
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                MouseX = e.getX();
                MouseY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                MouseX = e.getX();
                MouseY = e.getY();
            }
        });

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
                    isRightDown = false;
                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
                    isLeftDown = false;
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
                    isUpDown = false;
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
                    isDownDown = false;
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD1 || e.getKeyCode() == KeyEvent.VK_E)
                    is1Down = false;
                if (e.getKeyCode() == KeyEvent.VK_SLASH || e.getKeyCode() == KeyEvent.VK_Q)
                    isSlashDown = false;
            }

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
                    isRightDown = true;
                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
                    isLeftDown = true;
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
                    isUpDown = true;
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
                    isDownDown = true;
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD1 || e.getKeyCode() == KeyEvent.VK_E)
                    is1Down = true;
                if (e.getKeyCode() == KeyEvent.VK_SLASH || e.getKeyCode() == KeyEvent.VK_Q)
                    isSlashDown = true;

                if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) && isSpaceUp) {
                    Sound.SHOOT.play();
                    StartPaneBck.add(new Bullet(X + 23, Y, liveBullets));
                    isSpaceUp = false;
                    liveBullets++;
                    new disableSpace().start();
                }

                if (e.getKeyCode() == KeyEvent.VK_R)
                    System.out.println("" + score + "  " + waveNumber + "  " + liveEnemies + "  ");
            }
        });
    }

    private class disableSpace extends Thread {
        public void run() {
            isSpaceUp = false;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isSpaceUp = true;
        }
    }

    private void initializeCreditsScreen() {

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        URL url = this.getClass().getClassLoader().getResource("DeveBy.png");
        BufferedImage bufi = null;
        try {
            bufi = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon img = new ImageIcon(bufi.getScaledInstance(300, 300, BufferedImage.SCALE_SMOOTH));

        MainDisplay = new JLabel(img);
        MainDisplay.setVisible(true);

        Exit[0].setVisible(true);
        Exit[0].setContentAreaFilled(false);
        Exit[0].setFocusPainted(false);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        MainDisplay.setBounds(50, 100, 300, 300);
        Exit[0].setBounds(50, 510, 300, 40);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        CreditsPane.add(MainDisplay);
        CreditsPane.add(Exit[0]);

        CreditsPane.repaint();
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private void initializeSettingsScreen() {

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        MouseSet = new JToggleButton(getButtonIcon("Mouse"));
        MouseSet.setSelectedIcon(getButtonIcon("Mousep"));
        MouseSet.setVisible(true);
        MouseSet.setContentAreaFilled(false);
        //MouseSet.setBorderPainted(false);
        MouseSet.setFocusPainted(false);

        Exit[1].setVisible(true);
        Exit[1].setContentAreaFilled(false);
        //Exit[1].setBorderPainted(false);
        Exit[1].setFocusPainted(false);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        MouseSet.setBounds(50, 280, 300, 40);
        Exit[1].setBounds(50, 510, 300, 40);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        SettingsPane.add(MouseSet);
        SettingsPane.add(Exit[1]);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        MouseSet.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Sound.CLICK.play();
                isMouseSet = MouseSet.isSelected();
            }
        });

    }

    private void initializePlayScreen() {

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        SelectionPane = new JPanel();
        SelectionPane.setLayout(null);
        SelectionPane.setBounds(0, 0, 400, 600);        ///16 ///38
        SelectionPane.setBackground(Color.WHITE);
        SelectionPane.setVisible(true);

        StartPane = new PANE();
        StartPane.setLayout(null);
        StartPane.setBounds(0, 0, 400, 600);        ///16 ///38
        StartPane.setBackground(Color.WHITE);
        StartPane.setVisible(false);

        FinalPane = new JPanel();
        FinalPane.setLayout(null);
        FinalPane.setBounds(0, 0, 400, 600);        ///16 ///38
        FinalPane.setBackground(Color.WHITE);
        FinalPane.setVisible(false);

        lblName = new JLabel("Please enter your name:");
        lblName.setVisible(true);
        lblName.setFont(new Font(lblName.getFont().getFontName(), Font.PLAIN, 17));

        field = new JTextField();
        field.setVisible(true);

        rht = new JButton(getSquareIcon("nxt"));
        rht.setVisible(true);
        rht.setContentAreaFilled(false);
        //rht.setBorderPainted(false);
        rht.setFocusPainted(false);

        Display = new JLabel(getPlaneIcon(planeIndex, 200));
        Display.setVisible(true);

        lft = new JButton(getSquareIcon("bck"));
        lft.setVisible(true);
        lft.setContentAreaFilled(false);
        //lft.setBorderPainted(false);
        lft.setFocusPainted(false);

        StartPaneBck = new JLabel(getSeaIcon(seaFrame));
        StartPaneBck.setVisible(true);

        Myhealth = new JProgressBar(0, (int) totalHealth);
        Myhealth.setBorderPainted(false);
        Myhealth.setVisible(true);
        Myhealth.setBackground(Color.BLACK);
        PlanesLeft = new JProgressBar(0, (int) totalPlanesSkipped);
        PlanesLeft.setBorderPainted(false);
        PlanesLeft.setVisible(true);
        PlanesLeft.setBackground(Color.BLACK);

        Score = new JLabel("" + score);
        Score.setFont(new Font(Score.getFont().getFontName(), Font.BOLD, 20));
        Score.setVisible(true);

        Bosshealth = new JProgressBar();
        Bosshealth.setBorderPainted(false);
        Bosshealth.setVisible(false);
        Bosshealth.setBackground(Color.BLACK);

        Play = new JButton(getButtonIcon("Play"));
        Play.setRolloverIcon(getButtonIcon("Playp"));
        Play.setVisible(true);
        Play.setContentAreaFilled(false);
        //Play.setBorderPainted(false);
        Play.setFocusPainted(false);

        Player = new Player();
        Player.setVisible(true);

        URL url = this.getClass().getClassLoader().getResource("NewHighscore.png");
        BufferedImage bufi = null;
        try {
            bufi = ImageIO.read(url);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ImageIcon img = new ImageIcon(bufi.getScaledInstance(300, 60, BufferedImage.SCALE_SMOOTH));
        newhighscore = new JLabel(img);
        newhighscore.setVisible(true);

        Result = new JLabel("");
        Result.setFont(new Font(Result.getFont().getFontName(), Font.BOLD, 50));
        Result.setVisible(true);
        Result.setAlignmentY(JLabel.CENTER_ALIGNMENT);

        ScoreCont = new JPanel();
        ScoreCont.setVisible(true);
        ScoreCont.setLayout(null);
        ScoreCont.setBounds(20, 200, 360, 240);        ///16 ///38
        ScoreCont.setBackground(Color.WHITE);

        Name1 = new JLabel(ThyScores.score1[0]);
        Name2 = new JLabel(ThyScores.score2[0]);
        Name3 = new JLabel(ThyScores.score3[0]);
        Name4 = new JLabel(ThyScores.score4[0]);

        Score1 = new JLabel(ThyScores.score1[1]);
        Score2 = new JLabel(ThyScores.score2[1]);
        Score3 = new JLabel(ThyScores.score3[1]);
        Score4 = new JLabel(ThyScores.score4[1]);

        Name1.setVisible(true);
        Name2.setVisible(true);
        Name3.setVisible(true);
        Name4.setVisible(true);

        Name1.setFont(new Font(Name1.getFont().getFontName(), Font.PLAIN, 17));
        Name2.setFont(new Font(Name1.getFont().getFontName(), Font.PLAIN, 17));
        Name3.setFont(new Font(Name1.getFont().getFontName(), Font.PLAIN, 17));
        Name4.setFont(new Font(Name1.getFont().getFontName(), Font.PLAIN, 17));

        Score1.setVisible(true);
        Score2.setVisible(true);
        Score3.setVisible(true);
        Score4.setVisible(true);

        Score1.setFont(new Font(Score1.getFont().getFontName(), Font.PLAIN, 17));
        Score2.setFont(new Font(Score1.getFont().getFontName(), Font.PLAIN, 17));
        Score3.setFont(new Font(Score1.getFont().getFontName(), Font.PLAIN, 17));
        Score4.setFont(new Font(Score1.getFont().getFontName(), Font.PLAIN, 17));

        Exit[2].setVisible(true);
        Exit[2].setContentAreaFilled(false);
        //Exit[2].setBorderPainted(false);
        Exit[2].setFocusPainted(false);

        Exit[3].setVisible(true);
        Exit[3].setContentAreaFilled(false);
        //Exit[3].setBorderPainted(false);
        Exit[3].setFocusPainted(false);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        lblName.setBounds(50, 50, 300, 40);
        field.setBounds(50, 100, 300, 40);
        lft.setBounds(20, 260, 40, 40);
        Display.setBounds(100, 180, 200, 200);
        rht.setBounds(340, 260, 40, 40);
        Play.setBounds(50, 460, 300, 40);
        StartPaneBck.setBounds(0, 0, 400, 600);
        Bosshealth.setBounds(0, 0, 400, 30);
        Score.setBounds(0, 30, 400, 40);
        Myhealth.setBounds(0, 560, 400, 30);
        PlanesLeft.setBounds(0, 590, 400, 10);
        Player.setBounds(X, 500, 50, 50);
        newhighscore.setBounds(50, 90, 300, 60);
        Result.setBounds(50, 300, 300, 40);
        Name1.setBounds(20, 20, 160, 30);
        Name2.setBounds(20, 70, 160, 30);
        Name3.setBounds(20, 120, 160, 30);
        Name4.setBounds(20, 170, 160, 30);
        Score1.setBounds(180, 20, 160, 30);
        Score2.setBounds(180, 70, 160, 30);
        Score3.setBounds(180, 120, 160, 30);
        Score4.setBounds(180, 170, 160, 30);
        Exit[2].setBounds(50, 510, 300, 40);
        Exit[3].setBounds(50, 510, 300, 40);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        SelectionPane.add(lblName);
        SelectionPane.add(field);
        SelectionPane.add(lft);
        SelectionPane.add(Display);
        SelectionPane.add(rht);
        SelectionPane.add(Play);
        SelectionPane.add(Exit[2]);
        StartPaneBck.add(Myhealth);
        StartPaneBck.add(Score);
        StartPaneBck.add(Bosshealth);
        StartPaneBck.add(PlanesLeft);
        StartPaneBck.add(Player);
        StartPane.add(StartPaneBck);
        ScoreCont.add(Name1);
        ScoreCont.add(Name2);
        ScoreCont.add(Name3);
        ScoreCont.add(Name4);
        ScoreCont.add(Score1);
        ScoreCont.add(Score2);
        ScoreCont.add(Score3);
        ScoreCont.add(Score4);
        FinalPane.add(ScoreCont);
        FinalPane.add(newhighscore);
        FinalPane.add(Result);
        FinalPane.add(Exit[3]);
        PlayPane.add(SelectionPane);
        PlayPane.add(StartPane);
        PlayPane.add(FinalPane);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        rht.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.CLICK.play();
                if (planeIndex <= 2)
                    planeIndex++;
                Display.setIcon(getPlaneIcon(planeIndex, 200));
            }
        });
        lft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.CLICK.play();
                if (planeIndex >= 2)
                    planeIndex--;
                Display.setIcon(getPlaneIcon(planeIndex, 200));
            }
        });
        Play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.LAUNCH.play();
                Player.health = totalHealth;
                maxPlaneSkipped = totalPlanesSkipped;
                waveNumber = 1;
                score = 0;
                SelectionPane.setVisible(false);
                StartPane.setVisible(true);
                ThyScores.score5[0] = field.getText();
                Player.setIcon(getPlaneIcon(planeIndex, 50));
                isGameGoingOn = true;
                initializeGameInput();
                new gameThread().start();
                new backGround().start();
            }
        });

    }

    private ImageIcon getPlaneIcon(int index, int size) {
        URL url;
        BufferedImage bufi = null;
        ImageIcon icon;
        if (isMouseSet) {
            url = this.getClass().getClassLoader().getResource("null.png");
            try {
                bufi = ImageIO.read(url);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            icon = new ImageIcon(bufi.getScaledInstance(1, 1, Image.SCALE_SMOOTH));
            StartPaneBck.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(icon.getImage(), new Point(0, 0), "hey"));
        }
        url = this.getClass().getClassLoader().getResource("MyPlane" + index + ".png");
        try {
            bufi = ImageIO.read(url);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        icon = new ImageIcon(bufi.getScaledInstance(size, size, Image.SCALE_SMOOTH));
        return icon;
    }

    private ImageIcon getSeaIcon(int index) {
        URL url = this.getClass().getClassLoader().getResource("Sea" + (index + 1) + ".png");
        BufferedImage bufi = null;
        try {
            bufi = ImageIO.read(url);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ImageIcon icon = new ImageIcon(bufi.getScaledInstance(400, 600, Image.SCALE_SMOOTH));
        return icon;
    }

    private ImageIcon getBulletIcon(int index) {
        URL url;
        if (index == 100)
            url = this.getClass().getClassLoader().getResource("bossbullet.png");
        else
            url = this.getClass().getClassLoader().getResource("Bullet" + index + ".png");
        BufferedImage bufi = null;
        try {
            bufi = ImageIO.read(url);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ImageIcon icon;
        if (index == 100)
            icon = new ImageIcon(bufi.getScaledInstance(10, 20, Image.SCALE_SMOOTH));
        else
            icon = new ImageIcon(bufi.getScaledInstance(5, 10, Image.SCALE_SMOOTH));
        return icon;
    }

    private ImageIcon getEnemyIcon(int index) {
        URL url = this.getClass().getClassLoader().getResource("Enemy" + index + ".png");
        BufferedImage bufi = null;
        try {
            bufi = ImageIO.read(url);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ImageIcon icon = new ImageIcon(bufi.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        return icon;
    }

    private ImageIcon getBossIcon(int index) {
        URL url = this.getClass().getClassLoader().getResource("Boss" + index + ".png");
        BufferedImage bufi = null;
        try {
            bufi = ImageIO.read(url);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ImageIcon icon = new ImageIcon(bufi.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
        return icon;
    }

    private ImageIcon getButtonIcon(String NAME) {
        URL url = this.getClass().getClassLoader().getResource(NAME + ".png");
        BufferedImage bufi = null;
        try {
            bufi = ImageIO.read(url);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ImageIcon icon = new ImageIcon(bufi.getScaledInstance(300, 40, Image.SCALE_SMOOTH));
        return icon;
    }

    private ImageIcon getSquareIcon(String NAME) {
        URL url = this.getClass().getClassLoader().getResource(NAME + ".png");
        BufferedImage bufi = null;
        try {
            bufi = ImageIO.read(url);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ImageIcon icon = new ImageIcon(bufi.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        return icon;
    }

    private void initializeWelcomeScreen() {

        URL url = this.getClass().getClassLoader().getResource("Title.png");
        BufferedImage bufi = null;
        try {
            bufi = ImageIO.read(url);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ImageIcon img = new ImageIcon(bufi.getScaledInstance(200, 200, BufferedImage.SCALE_SMOOTH));

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        iconMas = new JLabel(img);
        iconMas.setVisible(true);


        Start = new JButton(getButtonIcon("Start"));
        Start.setRolloverIcon(getButtonIcon("Startp"));
        Start.setVisible(true);
        Start.setContentAreaFilled(false);
        //Start.setBorderPainted(false);
        Start.setFocusPainted(false);

        Settings = new JButton(getButtonIcon("Settings"));
        Settings.setRolloverIcon(getButtonIcon("Settingsp"));
        Settings.setVisible(true);
        Settings.setContentAreaFilled(false);
        //Settings.setBorderPainted(false);
        Settings.setFocusPainted(false);

        Credits = new JButton(getButtonIcon("Credits"));
        Credits.setRolloverIcon(getButtonIcon("Creditsp"));
        Credits.setVisible(true);
        Credits.setContentAreaFilled(false);
        Credits.setBorderPainted(false);
        Credits.setFocusPainted(false);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        iconMas.setBounds(100, 100, 200, 200);
        Start.setBounds(50, 410, 300, 40);
        Settings.setBounds(50, 460, 300, 40);
        Credits.setBounds(50, 510, 300, 40);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        WelcomePane.add(iconMas);
        WelcomePane.add(Start);
        WelcomePane.add(Settings);
        WelcomePane.add(Credits);

        WelcomePane.repaint();
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.CLICK.play();
                WelcomePane.setVisible(false);
                PlayPane.setVisible(true);
                SettingsPane.setVisible(false);
                CreditsPane.setVisible(false);
                SelectionPane.setVisible(true);
                FinalPane.setVisible(false);
            }
        });

        Settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.CLICK.play();
                WelcomePane.setVisible(false);
                PlayPane.setVisible(false);
                SettingsPane.setVisible(true);
                CreditsPane.setVisible(false);
            }
        });

        Credits.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.CLICK.play();
                WelcomePane.setVisible(false);
                PlayPane.setVisible(false);
                SettingsPane.setVisible(false);
                CreditsPane.setVisible(true);
            }
        });

    }

    private void initializeGlobalSystem() {

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        URL url = this.getClass().getClassLoader().getResource("Title.png");

        GridBagLayout layout = new GridBagLayout();

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        this.setTitle("Burning Air For Fuel!");
        this.setSize(416, 638);
        this.setLocation(450, 60);
        this.setVisible(true);
        this.setResizable(false);
        this.setLayout(layout);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.BLACK);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        WelcomePane = new JPanel();
        WelcomePane.setLayout(null);
        WelcomePane.setBounds(0, 0, 400, 600);        ///16 ///38
        WelcomePane.setBackground(Color.WHITE);
        WelcomePane.setVisible(true);

        PlayPane = new JPanel();
        PlayPane.setLayout(null);
        PlayPane.setBounds(0, 0, 400, 600);            ///16 ///38
        PlayPane.setBackground(Color.WHITE);
        PlayPane.setVisible(false);

        SettingsPane = new JPanel();
        SettingsPane.setLayout(null);
        SettingsPane.setBounds(0, 0, 400, 600);        ///16 ///38
        SettingsPane.setBackground(Color.WHITE);
        SettingsPane.setVisible(false);

        CreditsPane = new JPanel();
        CreditsPane.setLayout(null);
        CreditsPane.setBounds(0, 0, 400, 600);        ///16 ///38
        CreditsPane.setBackground(Color.WHITE);
        CreditsPane.setVisible(false);

        Exit[0] = new JButton(getButtonIcon("Back"));
        Exit[0].setRolloverIcon(getButtonIcon("Backp"));
        Exit[1] = new JButton(getButtonIcon("Back"));
        Exit[1].setRolloverIcon(getButtonIcon("Backp"));
        Exit[2] = new JButton(getButtonIcon("Back"));
        Exit[2].setRolloverIcon(getButtonIcon("Backp"));
        Exit[3] = new JButton(getButtonIcon("Back"));
        Exit[3].setRolloverIcon(getButtonIcon("Backp"));

        Exit[0].addActionListener(new ExtButLis());
        Exit[1].addActionListener(new ExtButLis());
        Exit[2].addActionListener(new ExtButLis());
        Exit[3].addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Loader.setScores(ThyScores);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        GridBagConstraints cons = new GridBagConstraints();
        cons.weighty = cons.weightx = 1;
        cons.ipadx = 400;
        cons.ipady = 600;

        layout.setConstraints(WelcomePane, cons);
        layout.setConstraints(PlayPane, cons);
        layout.setConstraints(SettingsPane, cons);
        layout.setConstraints(CreditsPane, cons);

        this.add(WelcomePane);
        this.add(PlayPane);
        this.add(SettingsPane);
        this.add(CreditsPane);

        this.repaint();
        this.pack();

    }

    private class gameThread extends Thread {
        @Override
        public void run() {
            while (isGameGoingOn) {

                if (maxPlaneSkipped == 0 || Player.health == 0) {
                    isGameGoingOn = false;
                    ThyScores.score5[1] = "" + score;
                    ThyScores.arrange();
                    Name1.setText(ThyScores.score1[0]);
                    Name2.setText(ThyScores.score2[0]);
                    Name3.setText(ThyScores.score3[0]);
                    Name4.setText(ThyScores.score4[0]);
                    Score1.setText(ThyScores.score1[1]);
                    Score2.setText(ThyScores.score2[1]);
                    Score3.setText(ThyScores.score3[1]);
                    Score4.setText(ThyScores.score4[1]);
                    StartPane.setVisible(false);
                    FinalPane.setVisible(true);
                    Result.setText("" + score);
                }

                if (!isMouseSet) {
                    if (Player.getX() >= 350)
                        X = 349;
                    else if (Player.getX() <= 0)
                        X = 1;
                    else {
                        if (isRightDown)
                            X++;
                        if (isLeftDown)
                            X--;
                        if (is1Down)
                            X += 2;
                        if (isSlashDown)
                            X -= 2;
                    }
                    if (Player.getY() >= 550)
                        Y = 549;
                    else if (Player.getY() <= 200)
                        Y = 201;
                    else {
                        if (isUpDown)
                            Y--;
                        if (isDownDown)
                            Y++;
                    }
                } else {
                    if (MouseX >= 383)
                        MouseX = 382;
                    else if (MouseX <= 33)
                        MouseX = 34;
                    else {
                        X = MouseX - 33;
                    }
                    if (MouseY >= 550)
                        MouseY = 549;
                    else if (MouseY <= 200)
                        MouseY = 201;
                    else {
                        Y = MouseY - 55;
                    }
                }

                if (liveEnemies == 0) {
                    for (int i = 0; i < normalEnemyArray[waveNumber % 10]; i++) {
                        StartPaneBck.add(new Enemy(1, liveEnemies));
                    }
                    waveNumber++;
                }

                if (liveEnemies < 0) liveEnemies = 0;

                Myhealth.setValue((int) Player.health);
                PlanesLeft.setValue((int) maxPlaneSkipped);
                Score.setText("" + score);

                Myhealth.setForeground(new Color(1 - (Player.health / totalHealth), Player.health / totalHealth, 0f));
                PlanesLeft.setForeground(new Color(1 - (maxPlaneSkipped / totalPlanesSkipped), maxPlaneSkipped / totalPlanesSkipped, 0f));


                if (!bossEvent) {
			/*if(waveNumber%100 == 0) {
				StartPaneBck.add(new EnemyBoss(3));
				waveNumber++;
			} else */
                    if (waveNumber % 45 == 0) {
                        bossEvent = true;
                        StartPaneBck.add(new EnemyBoss(2));
                    } else if (waveNumber % 15 == 0) {
                        bossEvent = true;
                        StartPaneBck.add(new EnemyBoss(1));
                    }
                }

                if (waveNumber >= 101) {
                    waveNumber = 1;
                }

                StartPaneBck.repaint();
                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class backGround extends Thread {
        @Override
        public void run() {
            while (isGameGoingOn) {

                if (seaFrame >= 4) seaFrame = 0;

                seaFrame++;
                score++;

                StartPaneBck.setIcon(getSeaIcon(seaFrame));
                StartPane.repaint();

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class PANE extends JPanel {
        private static final long serialVersionUID = 1L;

        public void paintComponent(Graphics g) {

            super.paintComponent(g);

            Player.setLocation(X, Y);

        }
    }

    private class Bullet extends JLabel {

        private static final long serialVersionUID = 1L;
        public int tindex;

        public Bullet(int iX, int iY, int index) {
            this.setIcon(getBulletIcon(planeIndex));
            this.setVisible(true);
            this.setSize(5, 10);
            this.tindex = index;
            bulletXs[tindex] = iX;
            bulletYs[tindex] = iY;
            setLocation(bulletXs[tindex], bulletYs[tindex]);
            new bulletThread().start();
        }

        private boolean checkIfTheBulletIsSupposedToBeDestroyed() {
            boolean out = true;
            if (bulletYs[tindex] <= 5)
                out = false;


            if (!out) {
                StartPaneBck.remove(this);
                liveBullets--;
                bulletXs[tindex] = -10;
                bulletYs[tindex] = -10;
            }
            return out;
        }

        private class bulletThread extends Thread {
            @Override
            public void run() {
                while (checkIfTheBulletIsSupposedToBeDestroyed()) {

                    bulletYs[tindex] -= 2;
                    setLocation(bulletXs[tindex], bulletYs[tindex]);
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private class eBullet extends JLabel {

        private static final long serialVersionUID = 1L;
        public int tindex, vX, vrY;

        public eBullet(int iX, int iY, int yX, int yY, int index) {
            this.setIcon(getBulletIcon(100));
            this.setVisible(true);
            this.setSize(10, 20);
            this.tindex = index;
            eliveBullets++;
            vX = yX;
            vrY = yY;
            ebulletXs[tindex] = iX;
            ebulletYs[tindex] = iY;

            setLocation(ebulletXs[tindex], ebulletYs[tindex]);
            new bulletThread().start();
        }

        private boolean checkIfTheBulletIsSupposedToBeDestroyed() {
            boolean out = true;
            if (ebulletYs[tindex] >= 650)
                out = false;
			/*if(ebulletXs[tindex] >= 420)
				out = false;
			if(ebulletXs[tindex] <= -20)
					out = false;*/

            if (!out) {
                StartPaneBck.remove(this);
                eliveBullets--;
                ebulletXs[tindex] = 200;
                ebulletYs[tindex] = 620;
            }
            return out;
        }

        private class bulletThread extends Thread {
            @Override
            public void run() {
                while (checkIfTheBulletIsSupposedToBeDestroyed()) {

                    ebulletYs[tindex] += vrY;
                    ebulletXs[tindex] += vX;

                    setLocation(ebulletXs[tindex], ebulletYs[tindex]);
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private class Enemy extends JLabel {

        private static final long serialVersionUID = 1L;
        public int tX, tY, tindex, health, ticon;

        public Enemy(int icon, int index) {

            this.setIcon(getEnemyIcon(icon));
            this.setVisible(true);
            this.setSize(50, 50);
            liveEnemies++;
            ticon = icon;
            tindex = index;
            tX = (int) (Math.random() * 350);
            tY = 0;
            setLocation(tX, tY);
            switch (icon) {
                case 1:
                    health = 2;
                    break;
                case 2:
                    health = 4;
                    break;
                case 3:
                    health = 6;
                    break;
                default:
                    health = 2;
                    break;
            }
            new enemyThread().start();
        }

        private boolean checkIfTheEnemyIsSupposedToBeDestroyed() {

            boolean out = true;
            if (tY >= 600) {
                out = false;
                maxPlaneSkipped--;
            }
            if (health <= 0) {
                out = false;
                score += ticon * 100;
                EnemiesDown++;
            }

            if (!out) {
                StartPaneBck.remove(this);
                liveEnemies--;
                Sound.ENEMYDEATH.play();
            }
            return out;
        }

        private class enemyThread extends Thread {
            @Override
            public void run() {
                while (checkIfTheEnemyIsSupposedToBeDestroyed()) {

                    for (int i = 0; i < liveBullets; i++)
                        if (bulletXs[i] > tX && bulletXs[i] < (tX + 50) &&
                                bulletYs[i] > tY && bulletYs[i] < (tY + 40)) {
                            health--;
                            bulletYs[i] = 0;
                        }

                    enemyXs[tindex] = tX;
                    enemyYs[tindex] = tY;

                    switch (ticon) {
                        case 1:
                            tY++;
                            break;

                        default:
                            break;
                    }

                    setLocation(tX, tY);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private class Player extends JLabel {

        private static final long serialVersionUID = 1L;
        float health;

        public Player() {

            super();
            health = totalHealth;
            new playerThread().start();
        }

        private boolean checkIfThePlayerIsSupposedToBeDestroyed() {

            boolean out = true;
            if (health <= 0)
                out = false;

            if (!out) {
                StartPaneBck.remove(this);
            }
            return out;
        }

        private class playerThread extends Thread {
            @Override
            public void run() {
                while (checkIfThePlayerIsSupposedToBeDestroyed()) {

                    for (int i = 0; i < eliveBullets; i++)
                        if (ebulletXs[i] > getX() - 10 && ebulletXs[i] < getX() + 50 &&
                                ebulletYs[i] > getY() - 20 && ebulletYs[i] < getY() + 40) {
                            health--;
                            ebulletYs[i] = 600;
                        }

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private class EnemyBoss extends JLabel {

        private static final long serialVersionUID = 1L;
        public int health, ticon;
        float totalh;
        boolean coin = false;

        public EnemyBoss(int icon) {
            switch (icon) {
                case 1:
                    health = 20;
                    break;
                case 2:
                    health = 35;
                    break;
                case 3:
                    health = 30;
                    break;
                default:
                    health = 2;
                    break;
            }
            this.setIcon(getBossIcon(icon));
            this.setVisible(true);
            this.setSize(100, 100);
            ticon = icon;
            bossX = 150;
            bossY = -200;
            setLocation(bossX, bossY);
            bossEvent = true;

            totalh = health;
            Bosshealth.setMaximum(health);
            Bosshealth.setMinimum(0);
            Bosshealth.setVisible(true);
            new enemyThread().start();
            new ebulletThread().start();
        }

        private boolean checkIfTheEnemyIsSupposedToBeDestroyed() {

            boolean out = true;
            if (health <= 0) {
                out = false;
                Bosshealth.setVisible(false);
                score += ticon * 1000;
            }

            if (!out) {
                StartPaneBck.remove(this);
                bossEvent = false;
                Sound.BLAST.play();
            }
            return out;
        }

        private class enemyThread extends Thread {
            @Override
            public void run() {
                while (checkIfTheEnemyIsSupposedToBeDestroyed()) {

                    Bosshealth.setValue(health);
                    Bosshealth.setForeground(new Color(health / totalh, 1 - (health / totalh), 0f));
                    for (int i = 0; i < liveBullets; i++)
                        if (bulletXs[i] > bossX && bulletXs[i] < (bossX + 100) &&
                                bulletYs[i] > bossY && bulletYs[i] < (bossY + 90)) {
                            health--;
                            bulletYs[i] = 0;
                        }


                    if (bossY != 150) {
                        bossY++;
                        setLocation(bossX, bossY);
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private class ebulletThread extends Thread {
            @Override
            public void run() {
                while (checkIfTheEnemyIsSupposedToBeDestroyed()) {

                    if (bossY >= 149) {
                        if (ticon == 1) {
                            if (coin) {
                                StartPaneBck.add(new eBullet(172, 200, 0, 1, eliveBullets));
                                //eliveBullets+=1;
                                coin = !coin;
                            } else {
                                StartPaneBck.add(new eBullet(228, 200, 0, 1, eliveBullets));
                                //eliveBullets+=1;
                                coin = !coin;
                            }
                        } else if (ticon == 2) {
                            if (coin) {
                                StartPaneBck.add(new eBullet(200, 200, -1, 1, eliveBullets));
                                StartPaneBck.add(new eBullet(200, 200, 0, 1, eliveBullets + 1));
                                StartPaneBck.add(new eBullet(200, 200, 1, 1, eliveBullets + 2));
                                //eliveBullets+=3;
                                coin = !coin;
                            } else {
                                StartPaneBck.add(new eBullet(200, 200, -1, 2, eliveBullets));
                                StartPaneBck.add(new eBullet(200, 200, 1, 2, eliveBullets + 1));
                                StartPaneBck.add(new eBullet(200, 200, -1, 1, eliveBullets + 2));
                                StartPaneBck.add(new eBullet(200, 200, 1, 1, eliveBullets + 3));
                                //eliveBullets+=4;
                                coin = !coin;
                            }
                        }
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private class ExtButLis implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            Sound.CLICK.play();

            WelcomePane.setVisible(true);
            PlayPane.setVisible(false);
            SettingsPane.setVisible(false);
            CreditsPane.setVisible(false);

            SelectionPane.setVisible(false);
            StartPane.setVisible(false);
            FinalPane.setVisible(false);

        }

    }

}




























