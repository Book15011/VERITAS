package com.example.loginregister;

public class Englishcourse_words_fc {
    private String word;
    private String englishDefinition;
    private String thaiDefinition;
    private String koreanDefinition;

    public Englishcourse_words_fc(String word, String englishDefinition, String thaiDefinition, String koreanDefinition) {
        this.word = word;
        this.englishDefinition = englishDefinition;
        this.thaiDefinition = thaiDefinition;
        this.koreanDefinition = koreanDefinition;
    }

    // Getters for all the fields
    public String getWord() {
        return word;
    }

    public String getEnglishDefinition() {
        return englishDefinition;
    }

    public String getThaiDefinition() {
        return thaiDefinition;
    }

    public String getKoreanDefinition() {
        return koreanDefinition;
    }
}
