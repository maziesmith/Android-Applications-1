package com.assignment.rahulhosmani.multinotepad;

public class Notes {

    private String strTitle;
    private String strDate;
    private String strContent;

    private static int counter =1;

    public Notes(String noteTitle, String noteDate, String noteContent){
        this.strTitle = noteTitle;
        this.strDate = noteDate;
        this.strContent = noteContent;
        counter++;
    }

    public String getTitle(){
        return strTitle;
    }

    public String getDate(){
        return strDate;
    }

    public String getContent(){
        return strContent;
    }

    @Override
    public String toString(){
        return strTitle + " (" + strDate + "), " + strContent;

    }

}
