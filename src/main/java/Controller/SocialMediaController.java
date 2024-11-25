package Controller;

import static org.mockito.ArgumentMatchers.nullable;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController()
    {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::registerAccount);
        app.post("/login", this::verifyLogin);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageByID);
        app.patch("/messages/{message_id}", this::updateMessageByID);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUser);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerAccount(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account adddedAccount = accountService.addAccount(account);
        if(adddedAccount != null){
            ctx.json(mapper.writeValueAsString(adddedAccount));
        }else{
            ctx.status(400);
        }
    }

    private void verifyLogin(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        Account loggedInUser = accountService.loginUser(account);

        if(loggedInUser != null){
            ctx.json(loggedInUser);
        }else{
            ctx.status(401);
        }
    }

    private void createMessage(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage =  messageService.addMessage(message);
        if(addedMessage != null){
            ctx.json(mapper.writeValueAsString(addedMessage));
        }else{
            ctx.status(400);
        }
    }

    private void getAllMessages(Context ctx) throws JsonMappingException, JsonProcessingException {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = messageService.getMessageByID(messageId);

        if(message != null)
            ctx.json(message);
    }

    private void deleteMessageByID(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        
        Message message = messageService.deleteMessageByID(messageId);

        if(message != null)
            ctx.json(message);
    }

    private void updateMessageByID(Context ctx) throws JsonMappingException, JsonProcessingException{
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        String messageText = message.getMessage_text();

        Message updatedMessage = messageService.updateMessageByID(messageId, messageText);

        if(updatedMessage != null){
            ctx.json(updatedMessage);
        }
        else{
            ctx.status(400);
        }

    }

    private void getAllMessagesByUser(Context ctx) {
        int accountID = Integer.parseInt(ctx.pathParam("account_id"));

        List<Message> messages = messageService.getAllMessagesByUser(accountID);

        if(messages != null)
            ctx.json(messages);
    }


}