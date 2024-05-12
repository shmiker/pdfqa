package com.pdfqa.pdfqa;

public class Question {
    private String content;
    private String answer;

    public Question(String content, String answer) {
        this.content = content;
        this.answer = answer;
    }

    public String getContent() {
        return content;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "content='" + content + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
