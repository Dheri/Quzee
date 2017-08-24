/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.Serializable;

/**
 *
 * @author Parteek Dheri
 */
public class Question implements Serializable {

    private static final long serialVersionUID = 8776516353157279513L; // i dont know why, but without it you will get a big fat error.
    /**
     * Contain question at index 0, four options at index 1,2,3,4
     */
    private String[] ques = new String[5];

    /**
     * contains state for all options
     */
    private boolean[] rightAnswer = new boolean[4];

    public Question() {
    }

    /**
     *
     * @param q Question
     * @param o1 Option 1.
     * @param o2 Option 2.
     * @param o3 Option 3.
     * @param o4 Option 4.
     * @param answerStr state of all options. 0 for false, rest all for true
     */
    public Question(String q, String o1, String o2, String o3, String o4, String answerStr) {
        ques[0] = q;
        ques[1] = o1;
        ques[2] = o2;
        ques[3] = o3;
        ques[4] = o4;
        rightAnswer = parseRightAnswer(answerStr);

    }

    /**
     *
     * @param answerStr String representation of right options, 0 is for false,
     * rest all true.
     * @return booleans values corresponding to specific options
     */
    private boolean[] parseRightAnswer(String in) {
        String answerStr = in.trim();
        boolean[] ans = new boolean[4];
        try {
            for (int i = 0; i < ans.length; i++) {
                ans[i] = (answerStr.charAt(i)) != '0';
            }

        } catch (Exception e) {
            System.out.print(e);
            e.printStackTrace();
        }
        return ans;
    }

    /**
     * @return the question and options
     */
    public String[] getQues() {
        return ques;
    }

    /**
     * @return the rightAnswer
     */
    public boolean[] getRightAnswer() {
        return rightAnswer;
    }

    /**
     * @param rightAnswer the rightAnswer to set
     */
    public void setRightAnswer(boolean[] rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    @Override
    public String toString() {
        String message = "Question : " + getQues()[0] + "\nA : " + getQues()[1]
                + "\tB : " + getQues()[2] + "\nC : " + getQues()[3]
                + "\tD : " + getQues()[4];
        return message;
    }

    public String toStringRight() {
        String message = "Question : " + getQues()[0]
                + "\nA : " + getQues()[1] + "<" + rightAnswer[0] + ">"
                + "\tB : " + getQues()[2] + "<" + rightAnswer[1] + ">"
                + "\nC : " + getQues()[3] + "<" + rightAnswer[2] + ">"
                + "\tD : " + getQues()[4] + "<" + rightAnswer[3] + ">";
        return message;
    }

}
