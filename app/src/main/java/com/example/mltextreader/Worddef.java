package com.example.mltextreader;

public class Worddef {

        private String def;
        private String word;


        public Worddef() {
        }

        public Worddef(String def, String word) {
            this.def = def;
            this.word = word;
        }

        public String getDef() {
            return def;
        }

        public void setDef(String def) {
            this.def = def;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

}
