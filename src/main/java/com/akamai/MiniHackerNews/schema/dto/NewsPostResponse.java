package com.akamai.MiniHackerNews.schema.dto;

public class NewsPostResponse
{
    private Long post_id;
    private String post;
    private String userName;
    private String link;
    private String postTime;
    private int votes;

    public NewsPostResponse(){}

    public int getVotes(){return (votes);}
    public String getPost(){return (post);}
    public String getLink(){return (link);}
    public Long getPost_id(){return (post_id);}
    public String getPostTime(){return postTime;}
    public String getUserName(){return (userName);}

    public void setLink(String link){this.link = link;}
    public void setPost(String post){this.post = post;}
    public void setVotes(int votes){this.votes = votes;}
    public void setPost_id(Long post_id){this.post_id = post_id;}
    public void setUserName(String userName){this.userName = userName;}

    public void setPostTime(long postTime)
    {
        if (postTime < 1)
        {
            this.postTime = "Just now";
        }
        else
        {
            long timeValue;
            String timeUnit;
            if(postTime < 24)
            {
                timeValue = postTime;
                timeUnit = "hour";
            }
            else
            {
                timeValue = postTime / 24;
                timeUnit = "day";
            }

            this.postTime = timeValue + " " + timeUnit + (timeValue == 1 ? "" : "s") + " ago";
        }
    }
}