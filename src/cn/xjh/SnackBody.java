package cn.xjh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class SnackBody {
    private int x;
    private int y;
    //坐标定位
    private int speed = 15;
    private int width = 15;
    private int height = 15;
    private boolean left, right, up, down;
    private Direction dir = Direction.D, curdir = Direction.D;
    private SnackClient sc;

    public SnackBody() {
        super();
        // TODO Auto-generated constructor stub
    }

    public SnackBody(int x, int y, SnackClient sc) {
        super();
        this.x = x;
        this.y = y;
        this.sc = sc;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Direction getDir() {
        return dir;
    }

    public void draw(Graphics g) {//SnakeBody类
        /*
         * Graphics2D类是Graphics类的子类
         * 创建Rectangle2D类的对象，调用Graphics2D类的方法
         * （Retangle2D实现了Shape接口）
         */
        Graphics2D g2 = (Graphics2D) g;
        Rectangle2D r2 = new Rectangle2D.Double(x, y, width, height);
        //蛇身为黄色
        g2.setColor(Color.YELLOW);
        //蛇头为红色
        if (this == SnackClient.snackHead)
            g2.setColor(Color.RED);
        g2.fill(r2);
        if (this == SnackClient.snackHead) {
            this.speed = sc.speed;
            move();
        }
    }
    //键盘事件   按下键
    public void keyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                right = true;
                break;
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_UP:
                up = true;
                break;
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            case KeyEvent.VK_SPACE:
                sc.setPause(!sc.isPause());
                break;
        }
        judgeDir();//判断方向
    }

    //键盘事件  松开按键
    public void keyRelease(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                right = false;
                break;
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_UP:
                up = false;
                break;
            case KeyEvent.VK_DOWN:
                down = false;
                break;
        }
    }
    //判断方向
    private void judgeDir() {
        // TODO Auto-generated method stub
        if (left && !right && !up && !down)
            curdir = Direction.L;
        if (!left && right && !up && !down)
            curdir = Direction.R;
        if (!left && !right && up && !down)
            curdir = Direction.U;
        if (!left && !right && !up && down)
            curdir = Direction.D;
    }
    /*
     * 蛇体的移动根据蛇身移动
     * 第一节蛇身等于蛇头移动前的位置
     * 第二节蛇身等于第一节蛇身移动前的位置
     * 以此类推
     */
    private void move() {
        if (dir == Direction.L && curdir == Direction.R)
            curdir = dir;
        if (dir == Direction.R && curdir == Direction.L)
            curdir = dir;
        if (dir == Direction.U && curdir == Direction.D)
            curdir = dir;
        if (dir == Direction.D && curdir == Direction.U)
            curdir = dir;
        if (dir != curdir)
            dir = curdir;

        int preX = x;
        int preY = y;

        if (dir == Direction.L) {
            x -= speed;
            preX += 1;
        }

        if (dir == Direction.R) {
            x += speed;
            preX -= 1;
        }

        if (dir == Direction.U) {
            y -= speed;
            preY += 1;
        }

        if (dir == Direction.D) {
            y += speed;
            preY -= 1;
        }
        int tempX, tempY;
        for (SnackBody body : sc.snackBodys) {
            tempX = body.getX();
            tempY = body.getY();
            body.setX(preX);
            body.setY(preY);
            preX = tempX;
            preY = tempY;
        }
    }

    //矩形检测原理：  (返回蛇身矩形)
    public Rectangle getRec() {
        return new Rectangle(x, y, width, height);
    }
    //蛇头和蛇身的碰撞事件
    public boolean hitSnackBody(SnackBody sb) {
        if (this == sb)
            return false;
        if (this.getRec().intersects(sb.getRec())) {
            return true;
        }
        return false;
    }

    public boolean hitSnackBody(ArrayList<SnackBody> snackBodys) {
        for (SnackBody snackBody : snackBodys) {
            if (this != snackBody) {
                if (hitSnackBody(snackBody)) {
                    return true;
                }
            }
        }
        return false;
    }
    //蛇与墙的碰撞事件
    public boolean hitWall() {
        if (x <= 0 || y <= 0 || x >= sc.getWidth() - width
                || y >= sc.getHeight() - height) {
            return true;
        }
        return false;
    }

}
