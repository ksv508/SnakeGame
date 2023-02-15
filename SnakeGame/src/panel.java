import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Random;

import static java.awt.event.KeyEvent.*;

public class panel extends JPanel implements ActionListener {
    static int width = 1200;
    static int height  = 600;
    /// grid unit size
    static int unit = 50;
    // one unit  pixel
    int score;
    int fx,fy;
    // food x,y coordinates
    int length = 3;
    // starting length of snake
    char dir = 'R';
    // direction of moving snake
    boolean flag = false;
    // true - game on, false - game over
    Random random;
    // for swanning food randomly
    Timer timer;
    // timer of update
    int delay = 130;
    int totalUnit = (width * height) / unit;
    int xSnake[] = new int[totalUnit];
    int ySnake [] = new int[totalUnit];
    panel(){
        this.setPreferredSize(new Dimension(width,height));
        this.setBackground(Color.BLACK);
        this.addKeyListener(new myKey());
        // enable keyboard input to the application
        this.setFocusable(true); // always get keyboard input
        random = new Random();
        gameStart();
    }

    public void gameStart(){
        flag = true;
        spawnFood();
        // timer to check on the game state on each 160 mms
        timer = new Timer(delay,this);
        timer.start();
    }
    public void spawnFood(){
        fx = random.nextInt((int)width/unit) * unit;  // always divisible by 50 coordinates
        fy = random.nextInt((int)height/unit) * unit;
    }

    public void paintComponent(Graphics graphic){ // creating graphic for the snake and game
           super.paintComponent(graphic);
           draw(graphic);
    }
    public void draw(Graphics graphic){
        if(flag){
            graphic.setColor(Color.ORANGE);
            graphic.fillOval(fx,fy,unit,unit);
            // to spawn the snake body
            for(int i = 0 ; i < length ; i++){
                // Head Color
                if(i == 0){
                    graphic.setColor(Color.RED);
                    graphic.fillRect(xSnake[0],ySnake[0],unit,unit);
                }
                // Snake Color
                else{
                    graphic.setColor(Color.GREEN);
                    graphic.fillRect(xSnake[i],ySnake[i],unit,unit);
                }
            }
            // for the Score  Display
            graphic.setColor(Color.CYAN);
            graphic.setFont(new Font("Comic Sans",Font.BOLD, 40));
            FontMetrics font = getFontMetrics(graphic.getFont());
            // score table position
            graphic.drawString("Score :  " + score, (width- font.stringWidth("Score :  "+ score))/2,graphic.getFont().getSize());

        }
        else{
            gameOver(graphic);
        }
    }
    public void gameOver(Graphics graphic){
        // Score When GAME OVER
        graphic.setColor(Color.CYAN);
        graphic.setFont(new Font("Comic Sans",Font.BOLD, 40));
        FontMetrics font = getFontMetrics(graphic.getFont());
        // score table position
        graphic.drawString("Score :  " + score, (width- font.stringWidth("Score :  "+ score))/2,graphic.getFont().getSize());

        // display GAme Over
        graphic.setColor(Color.RED);
        graphic.setFont(new Font("Comic Sans",Font.BOLD, 100));
        FontMetrics fontEnd = getFontMetrics(graphic.getFont());
        // score table position
        graphic.drawString("GAME OVER", (width- fontEnd.stringWidth("GAME OVER"))/2,height/2);

        // display  Press R to replay
        graphic.setColor(Color.WHITE);
        graphic.setFont(new Font("Comic Sans",Font.BOLD, 75));
        FontMetrics fontEndReplay = getFontMetrics(graphic.getFont());
        // score table position
        graphic.drawString("Press R to Relay Game", (width- fontEndReplay.stringWidth("Press R to Relay Game"))/2,height/2 - 150);
    }

    public void move(){
        // for all other body parts
        for(int i = length ; i > 0 ; i--){
            xSnake[i] = xSnake[i-1];
            ySnake[i] = ySnake[i-1];
        }
        // Update snake head
        switch (dir){
            case 'U' :
                ySnake[0] = ySnake[0] - unit;
                break;
            case 'D':
                ySnake[0] = ySnake[0] + unit;
                break;
            case 'L' :
                xSnake[0] = xSnake[0] - unit;
                break;
            case 'R' :
                xSnake[0] = xSnake[0] + unit;
                break;
        }
    }
    void check(){
        // Checking if head strikes snake body
        for(int i = length; i > 0 ; i--){
            if(xSnake[0] == xSnake[i] && ySnake[0] == ySnake[i])  flag = false;
        }
        // checking snake head hit with walls
        if(xSnake[0] < 0 || xSnake[0] > width || ySnake[0] < 0 || ySnake[0] > height)  flag = false;

        // if flag is false stop timer
        if(flag == false) timer.stop();
    }
    public void foodEaten(){
        for(int i = length; i > 0 ; i--){
            if(xSnake[0] == fx && ySnake[0] == fy){
                length++;
                score++;
                spawnFood();
            }
        }
    }
    public class myKey extends KeyAdapter{
        public void keyPressed(KeyEvent k){
            switch (k.getKeyCode()){
                case VK_UP:
                    if(dir != 'D'){   // we cannt move Down wile moving up
                        dir = 'U';
                    }
                    break;
                case VK_DOWN:
                    if(dir != 'U'){ // siimilarly cannt move up while  moving down
                        dir = 'D';
                    }
                    break;
                case VK_LEFT:
                    if(dir != 'R'){
                        dir = 'L';
                    }
                    break;
                case VK_RIGHT:
                    if(dir != 'L'){
                        dir = 'R';
                    }
                    break;
                case VK_R:
                    if(!flag){
                        score = 0;
                        length = 3;
                        dir = 'R';
                        Arrays.fill(xSnake,0);
                        Arrays.fill(ySnake,0);
                        gameStart();
                    }
                    break;
            }
        }
    }
    public void actionPerformed(ActionEvent e){
        if(flag){
            move();
            foodEaten();
            check();
        }
        // explicility calls the paintcomponent function
        repaint();
    }
}
