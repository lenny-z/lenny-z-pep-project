package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;

import Service.AccountService;
import Service.MessageService;
import Service.UserErrorException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.sql.SQLException;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIDHandler);
        app.get("accounts/{account_id}/messages", this::getMessagesByAccountIDHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void postAccountHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        try {
            Account addedAccount = accountService.addAccount(account);
            context.json(addedAccount).status(200);
        } catch (SQLException | UserErrorException e) {
            context.status(400);
        }

    }

    private void postLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        try {
            Account selectedAccount = accountService.getAccountByUsernameAndPassword(account.getUsername(),
                    account.getPassword());

            if (selectedAccount == null) {
                context.status(401);
            } else {
                context.json(selectedAccount).status(200);
            }
        } catch (SQLException e) {
            context.status(401);
        }
    }

    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);

        try {
            Message addedMessage = messageService.addMessage(message);
            context.json(addedMessage).status(200);
        } catch (SQLException | UserErrorException e) {
            context.status(400);
        }
    }

    private void getMessagesHandler(Context context) throws JsonProcessingException {
        try {
            context.json(messageService.getAllMessages()).status(200);
        } catch (SQLException e) {
            context.json(new ArrayList<Message>()).status(200);
        }
    }

    private void getMessageByIDHandler(Context context) throws JsonProcessingException {
        int id = Integer.parseInt(context.pathParam("message_id"));

        try {
            Message message = messageService.getMessageByID(id);

            if (message == null) {
                context.status(200);
            } else {
                context.json(message).status(200);
            }
        } catch (SQLException e) {
            context.status(200);
        }
    }

    private void deleteMessageByIDHandler(Context context) throws JsonProcessingException {
        int id = Integer.parseInt(context.pathParam("message_id"));
        try {
            Message message = messageService.deleteMessageByID(id);

            if (message == null) {
                context.status(200);
            } else {
                context.json(message).status(200);
            }
        } catch (SQLException e) {
            context.status(200);
        }
    }

    private void updateMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(context.pathParam("message_id"));

        Map<String, String> messageMap = mapper.readValue(context.body(), new TypeReference<Map<String, String>>() {
        });

        String messageText = messageMap.get("message_text");

        try {
            Message message = messageService.updateMessageByID(id, messageText);

            if (message == null) {
                context.status(400);
            } else {
                context.json(message).status(200);
            }
        } catch (SQLException | UserErrorException e) {
            context.status(400);
        }

    }

    private void getMessagesByAccountIDHandler(Context context) throws JsonProcessingException {
        int id = Integer.parseInt(context.pathParam("account_id"));

        try {
            context.json(messageService.getMessagesByAccountID(id)).status(200);
        } catch (SQLException e) {
            context.json(new ArrayList<Message>()).status(200);
        }
    }
}