package Service;

import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;;

public class MessageService {
    MessageDAO messageDAO;
    AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public Message addMessage(Message message) {
        String messageText = message.getMessage_text();

        if (messageText != "" && messageText.length() < 255 && accountDAO.accountIDExists(message.getPosted_by())) {
            return messageDAO.insertMessage(message);
        } else {
            return null;
        }
    }
}
