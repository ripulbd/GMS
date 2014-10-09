package mir.gla.ac.uk.gms;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;

/**
 * This class represents a comment. Each comment should have a name of the commenter, the timestamp representing when it's been created 
 * and the body of the comment.
 * Many news sites allow users to up vote or down vote a comment represented by the upVote and downVote variables.
 * Each comment may have a list of replies, each reply is represented with a comment object. 
 * @author 	Md. Sadek Ferdous
 * @version 1.0
 * @since 	11/08/2014
 *
 */
public class CommentDocument extends BasicDBObject {
	private static final long serialVersionUID = 3604113644826493375L;
	private String userName, timeStamp, commentBody, id, parent;
	private int upVote, downVote;
	private ArrayList<CommentDocument> replies;
	
	public CommentDocument(){
		userName = "";
		timeStamp = "";
		commentBody = "";
		id = "";
		parent = "";
		replies = new ArrayList<CommentDocument>();
	}
	
	public String getUserName() {
		return (String) super.get("userName");
		//eturn (String) super.get("bookName");
	}
	public void setUserName(String userName) {
		super.put("userName", userName);
		this.userName = userName;
	}
	
	public String getTimeStamp() {
		//return timeStamp;
		return (String) super.get("timeStamp");
	}
	public void setTimeStamp(String timestamp) {
		super.put("timeStamp", timestamp);
		this.timeStamp = timestamp;
	}
	public String getCommentBody() {
		//return commentBody;
		return (String) super.get("commentBody");
	}
	public void setCommentBody(String commentBody) {
		super.put("commentBody", commentBody);
		this.commentBody = commentBody;
	}
	public int getUpVote() {
		//return upVote;
		return (Integer) super.get("upVote");
	}
	public void setUpVote(int upVote) {
		super.put("upVote", upVote);
		this.upVote = upVote;
	}
	public int getDownVote() {
		//return downVote;
		return (Integer) super.get("downVote");
	}
	public void setDownVote(int downVote) {
		super.put("downVote", downVote);
		this.downVote = downVote;
	}
	public ArrayList<CommentDocument> getReplies() {
		//return replies;
		return (ArrayList<CommentDocument>) super.get("replies");
	}
	public void setReplies(ArrayList<CommentDocument> replies) {
		super.put("replies", replies);
		this.replies = replies;
	}
	
	/*@Override
	public String toString(){
		String returnString = "[++======================================================================================\n";
		returnString = returnString + "++UserName:" + userName + ", TimeStamp:" + timeStamp +", UpVote:" + upVote + ", DownVote:" + downVote + "\n";
		returnString += "++Main Comment:\n";
		returnString += "++---------------------------------------\n";
		returnString += "++" + commentBody + "\n";
		returnString += "++---------------------------------------\n";
		if(replies.size() > 0){
			returnString += "++This comment has the following replies:\n";
			for(CommentDocument comment: replies){
				returnString += "++++++++++++++++++++++++++++++++++++++++\n";
				returnString += "UserName:" + comment.getUserName() + ", TimeStamp:" + comment.getTimeStamp() + ", UpVote:" + comment.getUpVote() + ", DownVote:" + comment.getDownVote() + "\n";
				returnString += "Reply Comment:\n";
				returnString += "++---------------------------------------\n";
				returnString += comment.getCommentBody() + "\n";
				returnString += "++---------------------------------------\n";
			}
		}
		returnString += "++======================================================================================]\n";
		return returnString;
	}*/
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
	
	public boolean hasReply(){		
		return replies.size() != 0;
	}
	
	public String recursiveString(CommentDocument doc, int plusIndex){
		String returnString = "";
			int plusLoop = 0;
			while(plusLoop < plusIndex){
				returnString += "+";
				plusLoop++;
			}
			returnString += "++This comment has following replies:\n";
			for(CommentDocument comment: doc.getReplies()){
				returnString += "++++++++++++++++++++++++++++++++++++++++\n";
				returnString += "UserName:" + comment.userName + ", TimeStamp:" + comment.timeStamp + ", UpVote:" + comment.upVote + ", DownVote:" + comment.downVote + "\n";
				returnString += "Reply Comment:\n";
				returnString += "++---------------------------------------\n";
				returnString += comment.commentBody + "\n";
				plusLoop = 0;
				while(plusLoop < plusIndex){
					returnString += "+";
					plusLoop++;
				}
				returnString += "++---------------------------------------\n";
				if(comment.hasReply()){
					returnString += recursiveString(comment, plusIndex + 2);
				}
			}
		
		return returnString;
	}

	@Override
	public String toString(){
		String returnString = "[++======================================================================================\n";
		returnString = returnString + "++UserName:" + super.get("userName") + ", TimeStamp:" + super.get("timeStamp") +", UpVote:" + super.get("upVote") + ", DownVote:" + super.get("downVote") + "\n";
		returnString += "++Main Comment:\n";
		returnString += "++---------------------------------------\n";
		returnString += "++" + super.get("commentBody") + "\n";
		returnString += "++---------------------------------------\n";
		//ArrayList<CommentDocument> replyComment = (ArrayList<CommentDocument>) super.get("replies");
		if(replies.size() > 0){
			returnString += "++This comment has following replies:\n";
			for(CommentDocument comment: replies){
				returnString += "++++++++++++++++++++++++++++++++++++++++\n";
				returnString += "UserName:" + comment.userName + ", TimeStamp:" + comment.timeStamp + ", UpVote:" + comment.upVote + ", DownVote:" + comment.downVote + "\n";
				returnString += "Reply Comment:\n";
				returnString += "++---------------------------------------\n";
				returnString += comment.commentBody + "\n";
				returnString += "++---------------------------------------\n";
				if(comment.hasReply()){
					returnString += recursiveString(comment, 2);
				}
			}
		}
		returnString += "++======================================================================================]\n";
		return returnString;
	}
	
	/**
	* Always treat de-serialization as a full-blown constructor, by validating
	* the final state of the de-serialized object.
	*/
	private void readObject(ObjectInputStream aInputStream)
	throws ClassNotFoundException, IOException {
	    // always perform the default de-serialization first
	    aInputStream.defaultReadObject();
	}
	 
	/**
	* This is the default implementation of writeObject. Customise if
	* necessary.
	*/
	private void writeObject(ObjectOutputStream aOutputStream)
	throws IOException {
	    // perform the default serialization for all non-transient, non-static
	    // fields
	    aOutputStream.defaultWriteObject();
	}
}
