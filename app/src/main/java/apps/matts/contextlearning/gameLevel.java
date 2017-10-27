package apps.matts.contextlearning;

import java.util.Random;

/**
 * Created by Matthew on 4/3/2017.
   Edited by Robert Plaugher 10/27/2017
 */
public class gameLevel {
    private String word;
    private String guess;
    private String guessShown;
    private char[] guessLetters;

    public gameLevel()
    {
        word = "";
        guess = "";
        guessShown = "";
        guessLetters = new char[8];
    }
    
    //Getters and setters below AKA Data Hiding
    public String getWord(String word)
    {
        return word;
    }
    
    private void setWord()
    {
        this.word = word;
    }
    
    public String getGuess(String guess)
    {
        return guess;
    }
    
    private void setGuess()
    {
        this.guess = guess;   
    }

    public String getGuessLetters(String let)
    {
        return let;
    }
    
    private String setGuessLetters()
    {
        String let = String.copyValueOf(guessLetters);
    }

    public char[] getGuessLettersChar()
    {
        return guessLetters;
    }
    
    private char[] setGuessLettersChar()
    {
        ;
    }

    public String getGuessShown()
    {
        return guessShown;
    }

    public void setLevel(String levelWord)
    {
        word = levelWord;
        guess = "";
        setGuessShown();
        setExtraLetters();
    }

    private void setGuessShown()
    {
        guessShown = guess;
        for(int i = guess.length(); i < word.length(); i++)
        {
            String pad;
            if(i == 0)
            {
                pad = "__";
            }
            else
            {
                pad = " __";
            }

            guessShown = guessShown + pad;
        }

    }

    private void shuffleLetters(char[] ar)
    {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            char a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    private void setExtraLetters()
    {
        char[] wordArry = word.toCharArray();
        for (int i = 0; i < word.length(); i++)
        {
            guessLetters[i] = wordArry[i];
            //System.out.print(guessLetters[i] + "\n");
        }
        for(int i = word.length(); i < 8; i++)
        {
            Random r = new Random();
            char randomadd = (char)(r.nextInt(26) + 'A');
            guessLetters[i] = randomadd;
            //System.out.print(guessLetters[i] + "\n");
        }
        shuffleLetters(guessLetters);
        //System.out.print(guessLetters + "\n");
    }

    public void guessLetter(Character letter)
    {
        if(guess.length() < word.length())
        {
            guess = guess + letter;
            setGuessShown();
        }

    }

    public void removeGuessLetter()
    {
        if(guess.length() == 1)
        {
            guess = "";
            setGuessShown();
        }
        else if(guess.length() > 1 )
        {
            int endLength = guess.length();
            guess = guess.substring(0, endLength-1);
            setGuessShown();
        }

    }

    public Boolean makeGuess()
    {
        return word.equals(guess);
    }



}
