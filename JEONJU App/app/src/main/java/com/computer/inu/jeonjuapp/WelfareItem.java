package com.computer.inu.jeonjuapp;

public class WelfareItem {
    private String title;
    private String contents;
    private String name, tel, who, style, howSupport, howApplication, lifeCycle;

    public String getTitle(){
        return title;
    }
    public String getContents(){ return contents; }
    public String getName(){ return name; }
    public String getTel(){ return tel; }
    public String getWho(){ return who; }
    public String getStyle(){ return style; }
    public String getHowSupport(){ return howSupport; }
    public String getHowApplication(){ return howApplication; }
    public String getLifeCycle(){ return lifeCycle; }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setContents(String contents) { this.contents = contents; }
    public void setLifeCycle(String lifeCycle) { this.lifeCycle = lifeCycle; }
}