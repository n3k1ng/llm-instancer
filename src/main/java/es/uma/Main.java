package es.uma;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        // #region Initialize the agents
        
        ChatLanguageModel AImodel = OpenAiChatModel.builder()
            .apiKey(System.getenv("OPEN_API_KEY"))
            .modelName("gpt-4o")
            .build();

        IModelAnalyzer modelAnalyzer = AiServices.builder(IModelAnalyzer.class)
            .chatLanguageModel(AImodel)
            .build();

        // #endregion	
        
        // Load constants
        final CategoryPrompts CATEGORY_PROMPTS = new CategoryPrompts();
        final String CURRENT_TIME = Utils.getTime();
        final String DIAGRAM = "hammers";
        final String INSTACE_PATH = "./src/main/resources/instances/" + DIAGRAM + "/" + CURRENT_TIME + "/";
        final String PROMPT_PATH = "./src/main/resources/prompts/" + DIAGRAM + "/";
        
        // Read model and example
        String modelUML = Utils.readFile(PROMPT_PATH + "diagram.use"); 

        // Create class diagram modelDescription in plain English
        String modelDescription = modelAnalyzer.chat(modelUML);
        Utils.saveFile(modelDescription, INSTACE_PATH, "output.md");

        // For each category, create thread to create list and SOIL
        final ReentrantLock lock = new ReentrantLock();
        List<Thread> threads = new ArrayList<>();
        CATEGORY_PROMPTS.list.forEach( (categoryId, categoryPrompt) -> {
            
            Chatter chatter = new Chatter(categoryId, categoryPrompt, modelDescription, INSTACE_PATH, PROMPT_PATH, AImodel, lock);
            Thread thread = new Thread(chatter);
            thread.start();
            threads.add(thread);

        });

        threads.forEach( (thread) -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
    }

}