package com.themotherzuccers.gitefficient.gitevents;

import java.util.Scanner;
//
abstract class GitModification {
    Scanner sc = new Scanner(System.in);

    String message;

    public GitModification(String msg){ //Constructor with a string for commit message
        this.message = msg;
    }

    public void setCommit(String msg){
        this.message = msg;//Somehow runs with toneanalyzer thing
    }

    public String getCommit(){
        return message;
    }

    //Constructor with a string for message, getter and setter for that
    //String Message = InputFromJSONFile; //Value is placeholder for whatever attribute of CommitMessage from JSON file will be

//    public String CommitMessage(){
//        return Message;
    }

    /*Logical idea for searching through the files after a list of them is made
    ArrayList<Integer> mylist = new ArrayList<Integer>();
        while (sc.hasNextInt()) {
            int i = sc.nextInt();
            mylist.add(i);
    }
    */
    //GitAddition
    //GitSubtraction





