package apps.matts.contextlearning;

/**
 * Created by Matthew on 9/17/2017.
 */

public class QuestionInfo {
    private String Hint;
    private String Word;
    private int Level;

    public QuestionInfo() {
    }

    public String getWord() {
        return Word;
    }

    public void setWord(String word) {
        Word = word;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public String getHint() {

        return Hint;
    }

    public void setHint(String hint) {
        Hint = hint;
    }
}
