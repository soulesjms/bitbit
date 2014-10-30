/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripturejournalapp;

/**
 *
 * @author adam
 */
public class Topic {

    private String topic;
    private String subTopic;
    
    public Topic() {

    }

    public Topic(String subt) {
        if (isTopic(subt)) {
          this.subTopic = subt;
          //find and set topic name
          this.topic = Index.getIndex().getTopicTitle(subt);
        }
        else
        {
            System.err.println("ERROR: Bad topic");
        }
    }
    
    @Override
    public String toString() {
        return this.topic;
    }

    public Boolean isTopic(String t) {
        return Index.getIndex().isTopic(t);
    }

    public void add(String topicName) {
        this.topic = topicName;
    }

    public void setTopic(String topicName) {
        this.topic = topicName;
    }

    public String getTopic() {
        return this.topic;
    }
     
    public String getSubTopic() {
        return this.subTopic;
    }
}
