package Service;

import Model.Message;

import java.util.List;

import DAO.MessageDAO;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        this.messageDAO = new MessageDAO();
    }

    public Message addMessage(Message message){
        if(message.getMessage_text() == "" ||
        message.getMessage_text().length() > 255||
        !messageDAO.userExists(message.getPosted_by()))
            return null;
        else
            return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int messageID){
        return messageDAO.getMessageByID(messageID);
    }

    public Message deleteMessageByID(int messageID){
        return messageDAO.deleteMessageByID(messageID);
    }

    public Message updateMessageByID(int messageID, String messageText){
        if(messageDAO.getMessageByID(messageID) == null ||
            messageText == "" || 
            messageText.length() > 255)
                return null;
                
        return messageDAO.patchMessageByID(messageID, messageText);
    }

    public List<Message> getAllMessagesByUser(int accountID){
        return messageDAO.getAllMessagesByUser(accountID);
    }
}
