package kim.milyoner;

public class Question {
    public String questionText;
    public String[] options;
    public char correctAnswer;

    public Question(String questionText, String[] options, char correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

}
