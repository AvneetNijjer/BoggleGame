//Name: Avneet Singh Nijjer
//Date: 1/8/2024
//Purpose: Final Grid Game Project Boggle
//Theme: Casino Theme

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.Applet;
import java.io.*;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.applet.*;
import sun.audio.*;
import java.io.FileInputStream.*;


public class GridStarter extends Applet implements ActionListener
{
    //For screens
    Panel p_card;
    Panel card1, card2, card3, card4;
    CardLayout cdLayout = new CardLayout ();

    JLabel words[] = new JLabel [50];
    JButton undo;
    String word = "";
    String userWords = "";

    int x = -2;
    int y = -2;

    int row = 4;
    int col = 4;

    //For sound
    AudioClip soundFile;

    //Arrays to track and make the grid
    boolean buttonClicked[] [] = new boolean [row] [col];
    int pressed[] [] = new int [row] [col];
    JButton gridGame[] = new JButton [row * col];
    char letters[] [] = new char [row] [col];
    boolean sLetter[] [] = new boolean [row] [col];

    //Dictionary Array
    String array[] = new String [370105];

    //The current word being made
    JLabel currentWordLabel = new JLabel ("");
    JTextArea correctWordsTextArea;

    //For the unselecting method
    private int lastSelectedButtonIndex = -1;
    private char lastSelectedButtonLetter = ' ';

    //For Timer
    private int elapsedSeconds;
    private Timer stopwatchTimer;
    private JLabel stopwatchLabel;


    //Determines the pictures
    JLabel pics[] = new JLabel [row * col];
    int sqDimension = 72;
    String picEnd = " Unselected";
    String picFileType = ".png";
    JLabel turnpic;

    //Settings
    JTextField choice, name, first;

    //Formatting
    Color backgroundColour = Color.black;
    Color buttonColour = Color.black;
    Color buttonText = Color.white;
    Color titleColour = Color.white;
    Font titleFont = new Font ("Arial", Font.PLAIN, 30);
    Font promptFont = new Font ("Arial", Font.PLAIN, 20);
    Dimension boardSquare = new Dimension (96, 96);

    public void init ()
    {
	p_card = new Panel ();
	p_card.setLayout (cdLayout);

	soundFile = getAudioClip (getDocumentBase (), "background.wav");
	soundFile.loop ();

	opening ();
	instructions ();
	settings ();
	readIn ();
	setLayout (new FlowLayout (FlowLayout.CENTER));
	gameScreen ();
	resize (400, 650);
	setLayout (new BorderLayout ());
	add ("Center", p_card);
    }


    public void opening ()
    { //Opening screen for the game, has a splash screen picture
	card1 = new Panel ();
	card1.setBackground (backgroundColour);

	JButton next = new JButton (createImageIcon ("opening.gif"));
	next.setDoubleBuffered (true);
	next.setPreferredSize (new Dimension (400, 650));
	next.setActionCommand ("s2");
	next.addActionListener (this);
	next.setBackground (buttonColour);
	next.setForeground (buttonText);

	card1.add (next);
	p_card.add ("1", card1);
    }


    public void instructions ()
    { // Instructions screen, displays the instructions picture and buttons to proceed
	card2 = new Panel ();
	card2.setBackground (backgroundColour);

	JButton gameScreen = new JButton (createImageIcon ("instructions.gif"));
	gameScreen.setDoubleBuffered (true);
	gameScreen.setActionCommand ("s4");
	gameScreen.addActionListener (this);
	gameScreen.setPreferredSize (new Dimension (400, 650));
	gameScreen.setBackground (buttonColour);
	gameScreen.setForeground (buttonText);

	card2.add (gameScreen);
	p_card.add ("2", card2);
    }



    public void settings ()
    { //Settings screen, user can input some details
	card3 = new Panel ();
	card3.setBackground (backgroundColour);
	JLabel title = new JLabel ("Choose your settings:");
	title.setFont (titleFont);
	title.setForeground (titleColour);
	Panel p = new Panel ();
	JLabel namePmt = new JLabel ("Your name:");
	namePmt.setFont (promptFont);
	name = new JTextField (10);
	name.setFont (promptFont);
	Panel p2 = new Panel ();
	JLabel choicePmt = new JLabel ("X or O:");
	choicePmt.setFont (promptFont);
	choice = new JTextField (4);
	choice.setFont (promptFont);
	Panel p3 = new Panel ();
	JLabel firstPmt = new JLabel ("Go first? y or n?");
	firstPmt.setFont (promptFont);
	first = new JTextField (4);
	first.setFont (promptFont);
	JButton entrance = new JButton ("To the game");
	entrance.setActionCommand ("s4");
	entrance.addActionListener (this);
	entrance.setPreferredSize (new Dimension (300, 50));
	entrance.setBackground (buttonColour);
	entrance.setForeground (buttonText);

	card3.add (title);
	p.add (namePmt);
	p.add (name);
	p2.add (choicePmt);
	p2.add (choice);
	p3.add (firstPmt);
	p3.add (first);
	card3.add (p);
	card3.add (p2);
	card3.add (p3);
	card3.add (entrance);
	p_card.add ("3", card3);
    }


    //Method to create buttons for the grid
    public void createButton (JButton a, String actionCommand, int fontSize, int x, int y, boolean clickable, boolean border, boolean text)
    {
	a.setActionCommand (actionCommand);
	a.addActionListener (this);
	if (text)

	    a.setFont (new Font ("Arial", Font.BOLD, fontSize));
	a.setBackground (new Color (0, 0, 0));
	a.setPreferredSize (new Dimension (x, y));
	a.setEnabled (clickable);
	a.setBorderPainted (border);

    }


    //To read into the dictionary file in order to find the word
    public void readIn ()
    {
	BufferedReader in;
	try
	{
	    in = new BufferedReader (new FileReader ("Dictionary.txt"));
	    for (int i = 0 ; i < array.length ; i++)
	    {
		array [i] = in.readLine ();
		if (array [i] == null)
		{
		    System.out.println ("Error: End of file reached prematurely at line " + (i + 1));
		    break;
		}
		array [i] = array [i].trim ();
	    }
	    in.close ();
	}
	catch (FileNotFoundException e)
	{
	    System.out.println ("Error: File not found. Make sure the file path is correct.");
	}
	catch (IOException e)
	{
	    System.out.println ("Error reading file: " + e.getMessage ());
	}
    }


    //Method that binary searches the Dictionary to find the word
    public boolean BinarySearch (String find)
    {
	int high = array.length;
	int low = 0;
	boolean foundit = false;
	int mid = 0;
	while (high >= low && !foundit)
	{
	    mid = (high + low) / 2;
	    if (array [mid].equalsIgnoreCase (find))
		foundit = true;
	    else if (find.compareTo (array [mid]) > 0)
		low = mid + 1;
	    else
		high = mid - 1;
	}
	return foundit;
    }


    //Finds the neighbours to the button clicked on the grid
    public void area (int i, int j)
    {
	for (int m = 0 ; m < row ; m++)
	{
	    for (int n = 0 ; n < col ; n++)
	    {
		if (buttonClicked [m] [n] == true)
		    gridGame [m * col + n].setEnabled (true);
		else
		    gridGame [m * col + n].setEnabled (false);
	    }
	}

	gridGame [i * col + j].setEnabled (true);


	if (i - 1 >= 0 && j + 1 < col)
	    gridGame [(i - 1) * col + (j + 1)].setEnabled (true);
	if (i + 1 < row && j - 1 >= 0)
	    gridGame [(i + 1) * col + (j - 1)].setEnabled (true);
	if (i + 1 < row)
	    gridGame [(i + 1) * col + j].setEnabled (true);
	if (i - 1 >= 0 && j - 1 >= 0)
	    gridGame [(i - 1) * col + (j - 1)].setEnabled (true);
	if (i - 1 >= 0)
	    gridGame [(i - 1) * col + j].setEnabled (true);
	if (i + 1 < row && j + 1 < col)
	    gridGame [(i + 1) * col + (j + 1)].setEnabled (true);
	if (j - 1 >= 0)
	    gridGame [i * col + (j - 1)].setEnabled (true);
	if (j + 1 < col)
	    gridGame [i * col + (j + 1)].setEnabled (true);

    }


    // Update the undoLastSelection method to handle char instead of String
    public void undoLastSelection ()
    {
	if (lastSelectedButtonIndex != -1)
	{
	    int x = lastSelectedButtonIndex / col;
	    int y = lastSelectedButtonIndex % col;

	    // Deselect the last selected button
	    buttonClicked [x] [y] = false;
	    word = word.substring (0, word.length () - 1);
	    currentWordLabel.setText ("Current Word: " + word);
	    gridGame [lastSelectedButtonIndex].setBackground (new Color (0, 0, 0));

	    // Clear the last selected button information
	    lastSelectedButtonIndex = -1;
	    lastSelectedButtonLetter = ' ';
	}
    }



    public void gameScreen ()
    {
	// Main game screen, includes the grid and points counter
	card4 = new Panel ();
	card4.setBackground (backgroundColour);

	// Panel for title
	Panel p1 = new Panel ();
	JLabel title = new JLabel ("Boggle");
	title.setFont (titleFont);
	title.setForeground (titleColour);
	title.setHorizontalAlignment (JLabel.CENTER);
	title.setVerticalAlignment (JLabel.CENTER);

	// title.setPreferredSize (new Dimension (350, 30));
	p1.add (title);


	// Grid creation
	Panel grid = new Panel (new GridLayout (row, col));
	int m = 0;
	for (int i = 0 ; i < row ; i++)
	{
	    for (int j = 0 ; j < col ; j++)
	    {
		buttonClicked [i] [j] = false;
		Dice d = new Dice ();
		gridGame [m] = new JButton (createImageIcon (d.getPicName ()));
		letters [i] [j] = d.getFace ();
		sLetter [i] [j] = d.getClicked ();
		createButton (gridGame [m], "" + m, 20, 72, 72, true, true, false);
		grid.add (gridGame [m]);
		m++;
	    }
	}
	add (grid);

	// Panel for submit button
	Panel p2 = new Panel ();
	JButton submit = new JButton ("Submit");
	submit.addActionListener (this);
	submit.setActionCommand ("Submit");
	submit.setPreferredSize (new Dimension (100, 30));
	submit.setBackground (buttonColour);
	submit.setForeground (buttonText);
	p2.add (submit);

	// Panel for reset, instructions, and settings buttons
	Panel p3 = new Panel ();
	JButton reset = new JButton ("Reset");
	reset.addActionListener (this);
	reset.setActionCommand ("Reset");
	reset.setPreferredSize (new Dimension (100, 30));
	reset.setBackground (buttonColour);
	reset.setForeground (buttonText);
	p3.add (reset);

	JButton instruct = new JButton ("Instructions");
	instruct.addActionListener (this);
	instruct.setActionCommand ("Instructions");
	instruct.setPreferredSize (new Dimension (120, 30));
	instruct.setBackground (buttonColour);
	instruct.setForeground (buttonText);
	p3.add (instruct);

	JButton settings = new JButton ("Settings");
	settings.addActionListener (this);
	settings.setActionCommand ("Settings");
	settings.setPreferredSize (new Dimension (100, 30));
	settings.setBackground (buttonColour);
	settings.setForeground (buttonText);
	p3.add (settings);

	Panel p4 = new Panel ();
	// Stopwatch timer label
	stopwatchLabel = new JLabel ("Time: 0s");
	stopwatchLabel.setFont (new Font ("Arial", Font.PLAIN, 12));
	stopwatchLabel.setForeground (new Color (255, 255, 255));
	stopwatchLabel.setPreferredSize (new Dimension (150, 20));
	stopwatchLabel.setHorizontalAlignment (0);

	title.setPreferredSize (new Dimension (350, 50));

	stopwatchTimer = new Timer (1000, new ActionListener ()
	{
	    public void actionPerformed (ActionEvent evt)
	    {
		elapsedSeconds++;
		stopwatchLabel.setText ("Time: " + elapsedSeconds + "s");
	    }
	}
	);

	// Current word label
	currentWordLabel = new JLabel ("Current Word: ");
	currentWordLabel.setFont (new Font ("Arial", Font.PLAIN, 12));
	currentWordLabel.setForeground (new Color (255, 255, 255));
	currentWordLabel.setPreferredSize (new Dimension (150, 20));
	currentWordLabel.setHorizontalAlignment (0);


	// Text area for correct words
	correctWordsTextArea = new JTextArea (10, 20);
	correctWordsTextArea.setEditable (false);
	JScrollPane scrollPane = new JScrollPane (correctWordsTextArea);
	correctWordsTextArea.setBackground (new Color (0, 0, 0));
	correctWordsTextArea.setForeground (new Color (255, 255, 255));
	p4.add (scrollPane);


	// Adding components to the game screen
	card4.add (p1);
	card4.add (currentWordLabel);
	card4.add (stopwatchLabel);
	card4.add (grid);
	card4.add (p2);
	card4.add (p3);
	card4.add (p4);

	p_card.add ("4", card4);
    }


    // Method to unselect all the buttons and area of the clicked buttons on the board
    // Majorly used to reset the board
    public void unSelectAllButtons ()
    {
	for (int i = 0 ; i < row * col ; i++)
	{
	    gridGame [i].setBackground (new Color (0, 0, 0));
	    gridGame [i].setEnabled (true);
	}
	for (int m = 0 ; m < row ; m++)
	{
	    for (int n = 0 ; n < col ; n++)
	    {
		buttonClicked [m] [n] = false;
	    }
	}
    }


    public void actionPerformed (ActionEvent e)
    { //Does all the actions for buttons, screens etc
	if (e.getActionCommand ().equals ("s1"))
	    cdLayout.show (p_card, "1");
	else if (e.getActionCommand ().equals ("s2"))
	{
	    cdLayout.show (p_card, "2");
	    soundEffect ("click");
	}
	else if (e.getActionCommand ().equals ("s3"))
	    cdLayout.show (p_card, "3");
	else if (e.getActionCommand ().equals ("s4"))
	{
	    cdLayout.show (p_card, "4");
	    stopwatchTimer.start ();
	    soundEffect ("click");
	}

	//Listens for the Reset Buttons actionlistener
	else if (e.getActionCommand ().equals ("Reset"))
	{
	    // Clear the current word label and the word variable
	    currentWordLabel.setText ("Current Word: ");
	    word = "";
	    correctWordsTextArea.setText ("");
	    // Unselect all buttons
	    unSelectAllButtons ();
	    stopwatchTimer.stop ();
	    elapsedSeconds = 0;
	    stopwatchLabel.setText ("Time: 0s");
	    soundEffect ("click");

	}
	else if (e.getActionCommand ().equals ("settings"))
	{
	    cdLayout.show (p_card, "3");
	}
	else if (e.getActionCommand ().equals ("instruct"))
	{
	    cdLayout.show (p_card, "2");
	    soundEffect ("click");
	}

	else if (e.getActionCommand ().equals ("Submit"))
	{
	    // Check if the word is in the dictionary
	    if (BinarySearch (word.toLowerCase ()))
	    {
		// Add the lowercase version of the word to the userWords string
		userWords += word.toLowerCase () + " ";

		// Update the correctWordsTextArea with the correct word
		correctWordsTextArea.append (word.toLowerCase () + "\n");

		// Reset the current word label and clear the word variable
		currentWordLabel.setText ("Current Word: ");
		word = "";

		// Unselect all buttons
		unSelectAllButtons ();
		//Sound Effects
		soundEffect ("click");
	    }

	    else
	    {
		// Word is not in the dictionary, handle accordingly (display a message, etc.)
		System.out.println ("Word not found in the dictionary: " + word);

		// Reset the current word label and clear the word variable
		currentWordLabel.setText ("Current Word: ");
		word = "";

		// Unselect all buttons
		unSelectAllButtons ();
	    }
	}
	// Update the else part for button clicks
	else
	{
	    int n = Integer.parseInt (e.getActionCommand ());
	    int x = n / col;
	    int y = n % col;

	    // Check if the button is already selected
	    if (buttonClicked [x] [y])
	    {
		// If the button is already selected, deselect it
		buttonClicked [x] [y] = false;
		word = word.substring (0, word.length () - 1);
		currentWordLabel.setText ("Current Word: " + word);
		gridGame [n].setBackground (new Color (0, 0, 0));

		// Set the last selected button as the current button
		lastSelectedButtonIndex = n;
		lastSelectedButtonLetter = letters [x] [y];

		// Disable the area around the last selected button
		area (x, y);
	    }
	    else
	    {
		// If the button is not selected, proceed with the selection logic
		buttonClicked [x] [y] = true;
		word += letters [x] [y];
		currentWordLabel.setText ("Current Word: " + word);
		gridGame [n].setBackground (new Color (255, 255, 0));

		// Enable the area around the clicked button
		area (x, y);
	    }
	}
    }


    //For a single sound effect
    public void soundEffect (String filepath)
    {
	//initialize objects
	//declare sound effect player
	AudioPlayer SEP = AudioPlayer.player;
	//declare sound effect
	AudioStream SE;
	//declare audio data
	AudioData MA;
	//set as single run (NOT LOOP)
	AudioDataStream play = null;

	try
	{
	    //set file
	    SE = new AudioStream (new FileInputStream (filepath + ".wav"));
	    MA = SE.getData ();
	    //set data to play once (NOT LOOP)
	    play = new AudioDataStream (MA);
	}
	catch (IOException error)
	{
	    System.out.println ("Audio - File not found.");
	}
	SEP.start (play);
    }


    protected static ImageIcon createImageIcon (String path)
    {
	java.net.URL imgURL = GridStarter.class.getResource (path);
	if (imgURL != null)
	{
	    return new ImageIcon (imgURL);
	}

	else
	{
	    System.err.println ("Couldn't find file: " + path);
	    return null;
	}
    }
}
