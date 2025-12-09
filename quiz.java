// Ekrem
// 5/11/2025
// VERSION 1
// Double or quit quiz game at a level 8 including records, file I/O and ADTs


import java.io.*;
import java.util.Random;
import java.util.Scanner;

class QuestionRecord {
    String question;
    String answer;
    boolean used;
}

class QuizGame {

    // =========================
    // GETTERS & SETTERS
    // =========================
    public static boolean isUsed(QuestionRecord q) {     //checks whether a question is used
        return q.used;
    }

    public static String getQuestion(QuestionRecord q) {    //returns a question 
        return q.question;
    }

    public static String getAnswer(QuestionRecord q) {        //returns an answer
        return q.answer.toLowerCase();
    }

    public static void setUsed(boolean used, QuestionRecord q) {          //sets a question to used 
        q.used = used;
    }

    public static QuestionRecord createQuestion(String question, String answer) {    //creates a new question
        QuestionRecord q = new QuestionRecord();
        q.question = question;
        q.answer = answer.toLowerCase();
        q.used = false;
        return q;
    }

    // =========================
    // MAIN PROGRAM
    // =========================
    public static void main(String[] args) throws IOException {
        readRules();

        int players = 0;
        while (players <= 0) {                                                        //input validates the number of plays (0<x)
            System.out.println("How many players? (must be 1 or more)");
            String inputValue = input();
            
            if (!inputValue.isEmpty()){
                players = Integer.parseInt(inputValue);
            }
        }

        QuestionRecord[] questions = loadOrCreateSet();

        for (int i = 1; i <= players; i++) {
            System.out.println("PLAYER " + i + "'s turn:");
            resetUsedQuestions(questions);
            playQuiz(questions);
        }
    }

    public static void readRules() {  //method that reads rules
        System.out.println("DOUBLE OR QUIT QUIZ");
        System.out.println("If you get a question right, your pot will double.");
        System.out.println("If you get a question wrong, your pot will be cut in half.");
        System.out.println("Starting pot: 500");
    }

    public static void playQuiz(QuestionRecord[] questionBank) {       //initialises the starting pot and starts the quiz
        final int STARTING_POT = 500;
        int pot = STARTING_POT;

        for (int i = 0; i < questionBank.length; i++) {
            QuestionRecord q = getRandomUnusedQuestion(questionBank);
            pot = askQuestion(q, pot);
        }

        System.out.println("Final pot: " + pot);
    }

    // =========================
    // FILE METHODS
    // =========================
    public static QuestionRecord[] loadOrCreateSet() throws IOException {   //decides whether to load a set or create a new one, calling the appropriate method
        while (true) {
            System.out.println("Enter file name to load OR type NEW:");
            String choice = input();

            if (choice.equalsIgnoreCase("NEW")) {
                return createNewSet();
            } else {
                File file = new File(choice);
                if (file.exists()) {
                    return readFromFile(choice);
                } else {
                    System.out.println("File does not exist.");
                }
            }
        }
    }

    public static QuestionRecord[] readFromFile(String filename) throws IOException {        // stores the questions from a file into an array to be read from
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        int count = 0;

        while (reader.readLine() != null) {
            count++;
             }
            reader.close();
        reader = new BufferedReader(new FileReader(filename));
        QuestionRecord[] questions = new QuestionRecord[count];

        String line;
        int index = 0;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");

            questions[index] = createQuestion(parts[0], parts[1]);
            index++;
        }

        reader.close();
        return questions;
    }

    public static QuestionRecord[] createNewSet() throws IOException {         // used to create a new set of questions, asking how much and then taking inputs
        System.out.println("How many questions?");
        int size = Integer.parseInt(input());

        QuestionRecord[] questions = new QuestionRecord[size];

        for (int i = 0; i < size; i++) {
            System.out.println("Enter question:");
            String qText = input();

            System.out.println("Enter answer:");
            String aText = input().toLowerCase();

            questions[i] = createQuestion(qText, aText);
        }

        System.out.println("Enter file name to save:");
        String filename = input();
        writeToFile(filename, questions);

        return questions;
    }

    public static void writeToFile(String filename, QuestionRecord[] questions) throws IOException {       //writes the input questions to file seperated by a comma
        PrintWriter writer = new PrintWriter(filename);

        for (int i = 0; i < questions.length; i++) {
            writer.println(
                    getQuestion(questions[i]) + "," +
                            getAnswer(questions[i])
            );
        }

        writer.close();
    }

    // =========================
    // GAME LOGIC
    // =========================
    public static QuestionRecord getRandomUnusedQuestion(QuestionRecord[] questions) {         //makes sure the next question is unused
        int index = getRandomIndex(questions.length);

        while (isUsed(questions[index])) {
            index = getRandomIndex(questions.length);
        }

        setUsed(true, questions[index]);
        return questions[index];
    }

    public static int askQuestion(QuestionRecord q, int pot) {              //actually asks the questions, halfs pot if incorrect doubles if correct.
        System.out.println("Current pot: " + pot); 
        System.out.println("Question: " + getQuestion(q));
        System.out.print("Your answer: ");

        String userAnswer = input().toLowerCase();

        if (userAnswer.equals(getAnswer(q))) {
            pot *= 2;
            System.out.println("Correct! New pot: " + pot);
        } else {
            pot /= 2;
            System.out.println("Wrong! Correct answer was: " + getAnswer(q));
            System.out.println("New pot: " + pot);
        }

        return pot;
    }

    public static void resetUsedQuestions(QuestionRecord[] questions) {      //resets all the used questions to false when a new players turn has come
        for (int i = 0; i < questions.length; i++) {
            setUsed(false, questions[i]);
        }
    }
    // =========================
    // GENERAL METHODS
    // =========================
    public static String input() {              //general input method 
        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
    public static int getRandomIndex(int length) { // general random method
        final Random R = new Random();
        return R.nextInt(length);
    }
}
