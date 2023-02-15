import javax.swing.*;

public class frame extends JFrame {
    frame(){
        this.add(new panel());
        this.setTitle("SnakeGame"); // name of title
        this.setResizable(false); // cannot let user to resize screen
        this.pack(); // set window to preferable size
        this.show(); // make visible to screen
    }
}
