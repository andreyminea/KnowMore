package com.example.knlgsharing;

import java.io.Serializable;

public class Post implements Serializable
{
    private String Title;
    private String Moderator;
    private String Day;
    private String Time;
    private String Image;
    private String Description;
    private Long SeatsLeft;
    private String EmailModerator;
    private String Participants;
    private String Link;

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public Post(String title, String moderator, String day, String time, String image,
                String description, Long seatsLeft, String emailModerator, String participants, String link) {

        Title = title;
        Moderator = moderator;
        Day = day;
        Time = time;
        Image = image;
        Description = description;
        SeatsLeft = seatsLeft;
        EmailModerator = emailModerator;
        Participants = participants;
        Link = link;
    }

    public Post()
    {}

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getModerator() {
        return Moderator;
    }

    public void setModerator(String moderator) {
        Moderator = moderator;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Long getSeatsLeft() {
        return SeatsLeft;
    }

    public void setSeatsLeft(Long seatsLeft) {
        SeatsLeft = seatsLeft;
    }

    public String getEmailModerator() {
        return EmailModerator;
    }

    public void setEmailModerator(String emailModerator) {
        EmailModerator = emailModerator;
    }

    public String getParticipants() {
        return Participants;
    }

    public void setParticipants(String participants) {
        Participants = participants;
    }

    public Boolean equalsPost(Post p)
    {
        if(Title.equals(p.getTitle()) &&
                Description.equals(p.getDescription()) &&
                Time.equals(p.getTime()) &&
                Day.equals(p.getDay()) &&
                SeatsLeft.equals(p.getSeatsLeft())
        )
        {
            return true;
        }
        else
            return false;
    }


}
