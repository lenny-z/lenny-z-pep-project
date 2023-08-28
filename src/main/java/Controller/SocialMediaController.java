package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;

import Service.AccountService;
import Service.MessageService;

import java.util.List;

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
        Account addedAccount = accountService.addAccount(account);

        if (addedAccount == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(addedAccount)).status(200);
        }
    }

    private void postLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        Account selectedAccount = accountService.getAccountByUsernameAndPassword(account.getUsername(),
                account.getPassword());

        if (selectedAccount == null) {
            context.status(401);
        } else {
            context.json(mapper.writeValueAsString(selectedAccount)).status(200);
        }
    }

    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);

        if (addedMessage == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(addedMessage)).status(200);
        }
    }

    private void getMessagesHandler(Context context) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages).status(200);
    }

    private void getMessageByIDHandler(Context context) throws JsonProcessingException {
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageByID(id);

        if (message == null) {
            context.status(200);
        } else {
            context.json(message).status(200);
        }
    }

    private void deleteMessageByIDHandler(Context context) throws JsonProcessingException {
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.deleteMessageByID(id);

        if (message == null) {
            context.status(200);
        } else {
            context.json(message).status(200);
        }
    }
}