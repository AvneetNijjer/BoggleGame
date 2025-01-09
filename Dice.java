import java.util.Random;

public class Dice
{
    public char display;
    public boolean clicked;

    public Dice ()
    {
	generateRandomDiceNumber ();
    }


    public void generateRandomDiceNumber ()
    {
	int freqRanges[] = {111607, 196573, 272382, 347830, 419465, 488974, 555518, 612869, 667762, 713150, 749458, 783302, 814973, 845102, 875136, 899841, 920561, 938682, 956461, 969360, 980376, 990450, 993352, 996074, 998039, 1000000};
	char letters[] = {'E', 'A', 'R', 'I', 'O', 'T', 'N', 'S', 'L', 'C', 'U', 'D', 'P', 'M', 'H', 'G', 'B', 'F', 'Y', 'W', 'K', 'V', 'X', 'Z', 'J', 'Q'};
	int rand = (int) (Math.random () * 1000000) + 1;
	for (int i = 0 ; i < letters.length - 1 ; i++)
	{
	    if (rand <= freqRanges [0])
		display = letters [0];
	    else if (rand >= freqRanges [i] && rand <= freqRanges [i + 1])
		display = letters [i];
	}
    }


    public char getFace ()
    {
	return display;
    }


    public boolean getClicked ()
    {
	return clicked;
    }


    public void setClicked (boolean value)
    {
	clicked = value;
    }


    public String getPicName ()
    {
	String state = clicked ? "Selected":
	"Unselected";
	return display + " " + state + ".png";
    }


    public String toString ()
    {
	return "The letter is " + display;
    }
}
