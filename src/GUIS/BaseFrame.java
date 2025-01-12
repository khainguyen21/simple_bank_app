package GUIS;

import db_objs.User;

import javax.swing.*;
/*
    Creating an abstract class helps us set up the blueprint that our GUIs will follow, for example
    in each of the GUIs they will be the same size and will need to invoke their own addGuiComponents()
    which will be unique to each subclass
 */


public abstract class BaseFrame extends JFrame {

    // Store user information
    protected User user;

    public BaseFrame(String title)
    {
        initialize(title);
    }

    public BaseFrame(String title, User user)
    {
        this.user = user;
        initialize(title);
    }

    public void initialize(String title)
    {
        // add title to the bar
        setTitle(title);

        // set size
        setSize(420, 600);

        // terminate program when hits the close (X) button to close GUI
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // set layout to null to have absolute layout which allows us to manually specify the size and position
        // of each gui components
        setLayout(null);

        // prevent gui from being resize
        setResizable(false);

        // launch GUI in the center of the screen
        setLocationRelativeTo(null);

        // call on the subclass addGuiComponent()
        addGuiComponents();

    }

    // this method will need to be defined by subclasses when this class is being inherited from
    protected  abstract void addGuiComponents();

}
