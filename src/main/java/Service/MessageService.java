package Service;

import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO;
    AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    private boolean isValid(String messageText) {
        return messageText != null && messageText != "" && messageText.length() < 255;
    }

    private boolean isValid(Message message) {
        String messageText = message.getMessage_text();
        return isValid(messageText);
    }

    public Message addMessage(Message message) {
        if (isValid(message) && accountDAO.accountIDExists(message.getPosted_by())) {
            return messageDAO.insertMessage(message);
        }

        return null;
    }

    public List<Message> getAllMessages() {
        return messageDAO.selectAllMessages();
    }

    public Message getMessageByID(int id) {
        return messageDAO.selectMessageByID(id);
    }

    public Message deleteMessageByID(int id) {
        return messageDAO.deleteMessageByID(id);
    }

    public Message updateMessageByID(int id, String messageText) {
        if (isValid(messageText)) {
            return messageDAO.updateMessageByID(id, messageText);
        }

        return null;
    }

    public List<Message> getMessagesByAccountID(int id){
        return messageDAO.selectMessagesByAccountID(id);
    }
}
