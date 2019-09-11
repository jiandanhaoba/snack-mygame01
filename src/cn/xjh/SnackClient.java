package cn.xjh;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class SnackClient extends JFrame {
    private static SnackClient sc;
    private SnackBody snackFood;
    private boolean gameState = true;
    //当前游戏状态
    private MyComponent mc;
    //游戏
    private boolean pause = false;
    private JMenu menu;
    private JMenuBar menubar;

    private JMenuItem lowSpeedItem;
    //低等级
    private JMenuItem midSpeedItem;
    //中等级
    private JMenuItem heightSpeedItem;
    //高等级
    private JMenuItem restartItem;
    //重新开始
    public int speed = 15;
    static int score = 0;
    private MaxScore ms;

    ArrayList<SnackBody> snackBodys = new ArrayList<SnackBody>();
    //蛇身子（创建一个储存蛇身的ArrayList对象）
    static SnackBody snackHead;
    //蛇头
    private static final long serialVersionUID = 1L;

    public SnackClient() {
        //初始化菜单栏
        initMenu();
        mc = new MyComponent();
        mc.setBackground(Color.BLACK);
        add(mc);
        setTitle("贪吃蛇_XJH");//设置窗口的标题
        setSize(800, 600);//设置窗口  宽高
        setLocationRelativeTo(null);
        //将窗口置于屏幕中央（设置窗口相对于指定组件的位置）
        setVisible(true);//设置为可见
        setResizable(false);//设置窗口大小不可变
        snackHead = new SnackBody(100, 100, this);
        for (int i = 0; i < 2; i++) {
            snackBodys.add(new SnackBody(-100, -100, this));
        }
        ms = new MaxScore();
        showSnackFood();
        addKeyListener(new MyKeyListener());
        new paintThread().start();
        //为窗口添加监听器（点击关闭键则结束线程）
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void initMenu() {
        // TODO Auto-generated method stub
        menu = new JMenu("参数设置");
        lowSpeedItem = new JMenuItem("低等级");
        midSpeedItem = new JMenuItem("中等级");
        heightSpeedItem = new JMenuItem("高等级");
        restartItem = new JMenuItem("重新开始");
        lowSpeedItem.addActionListener(new ActionListener() {
            //响应用户点击的按钮（低等级）
            public void actionPerformed(ActionEvent e) {
                speed = 15;
            }
        });
        midSpeedItem.addActionListener(new ActionListener() {
            //响应用户点击的按钮（中等级）
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                speed = 17;
            }
        });
        heightSpeedItem.addActionListener(new ActionListener() {
            //响应用户点击的按钮（高等级）
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                speed = 20;
            }
        });
        restartItem.addActionListener(new ActionListener() {
            //响应用户点击的按钮（重新开始）
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                dispose();
                //销毁窗口, 释放窗口及其所有子组件占用的资源, 之后再次调用 setVisible(true) 将会重构窗口
                sc=new SnackClient();
            }
        });
        menu.add(lowSpeedItem);
        menu.add(midSpeedItem);
        menu.add(heightSpeedItem);
        menu.add(restartItem);
        menubar = new JMenuBar();
        menubar.add(menu);
        setJMenuBar(menubar);
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        sc = new SnackClient();
    }

    public class MyComponent extends JPanel {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        protected void paintComponent(Graphics g) {
            // TODO Auto-generated method stub
            super.paintComponent(g);

            if (!gameState) {
                Font font = new Font("微软雅黑", Font.BOLD, 60);
                g.setFont(font);
                g.setColor(Color.RED);
                g.drawString("GAME OVER!", this.getWidth() / 2 - 150,
                        this.getHeight() / 2);
                return;
            }
            // 如果蛇撞到蛇身 或蛇撞到墙则游戏结束
            if (snackHead.hitWall() || snackHead.hitSnackBody(snackBodys)) {
                gameState = false;
            }
            // 如果蛇头吃到食物
            if (snackHead.hitSnackBody(snackFood)) {
                snackBodys.add(snackFood);
                score++;
                // 刷新食物
                showSnackFood();
            }
            // 画食物
            snackFood.draw(g);
            // 画蛇身
            if (gameState) {
                for (SnackBody sb : snackBodys) {
                    sb.draw(g);
                }
            }
            // 画蛇头
            if (gameState) {
                snackHead.draw(g);
            }
            try {
                g.drawString(
                        "当前分数：" + score * 100 + "      游戏最高分:"
                                + ms.getMaxScore(), 0, 10);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    //随机出现食物
    public void showSnackFood() {
        Random r = new Random();
        boolean exit = true;
        while (exit) {
            int x = r.nextInt(mc.getWidth() - snackHead.getWidth());
            int y = r.nextInt(mc.getHeight() - snackHead.getHeight());
            snackFood = new SnackBody(x, y, sc);
            if (snackFood.hitSnackBody(snackHead)
                    || snackFood.hitSnackBody(snackBodys)) {
                continue;
            }
            exit = false;
        }
    }
    //键盘事件
    public class MyKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            snackHead.keyPress(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub
            snackHead.keyRelease(e);
        }
    }
    //游戏刷新
    public class paintThread extends Thread {
        public void run() {
            // TODO Auto-generated method stub
            while (true) {//线程将一直处于运行当中，只有在游戏结束的时候
                if (!pause) {
                    repaint();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}

