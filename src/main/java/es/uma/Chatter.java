package es.uma;

import java.util.concurrent.locks.ReentrantLock;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;

public class Chatter implements Runnable {
    String categoryId;
    String categoryPrompt;
    String modelDescription;
    String instancePath;
    String promptPath;
    ChatLanguageModel AImodel;
    ReentrantLock lock;
    
    public Chatter(String categoryId, String categoryPrompt, String modelDescription, String instancePath, String promptPath, ChatLanguageModel AImodel, ReentrantLock lock) {
        this.categoryId = categoryId;
        this.categoryPrompt = categoryPrompt;
        this.modelDescription = modelDescription;
        this.instancePath = instancePath;
        this.promptPath = promptPath;
        this.AImodel = AImodel;
        this.lock = lock;
    }

    public void run() {
        Use use = new Use();
        String exampleSOIL = Utils.readFile(promptPath + "examples/example_1.soil");

        // Create agents
        ChatMemory listCreatorMemory = MessageWindowChatMemory.withMaxMessages(24);
        IListCreator listCreator = AiServices.builder(IListCreator.class)
            .chatLanguageModel(AImodel)
            .chatMemory(listCreatorMemory)
            .build();

        ChatMemory instantiatorMemory = MessageWindowChatMemory.withMaxMessages(24);
        IModelInstantiator modelInstantiator = AiServices.builder(IModelInstantiator.class)
            .chatLanguageModel(AImodel)
            .chatMemory(instantiatorMemory)
            .build();

        // Create list
        String list = listCreator.chat(categoryPrompt, modelDescription);
                    
        // Create SOIL and check constraints
        String instanceSOIL = modelInstantiator.chat(list, exampleSOIL);
        lock.lock();
        Utils.saveFile(instanceSOIL, instancePath, "temp.soil", false);
        lock.unlock();
        if (!categoryId.equals("invalid")) { // Check only for valid instances
            String check = use.check(promptPath + "diagram.use", instancePath + "temp.soil", modelDescription.substring(modelDescription.indexOf("Invariants")));  
            
            if (check != "OK")
                instanceSOIL = modelInstantiator.chat("The list and output is partially incorrect: \n" + check + "\n Please provide the corrected full output");    
            lock.lock();
            Utils.saveFile(instanceSOIL + "\n\n", instancePath, "outputValid.soil");
            lock.unlock();
        } else {
            lock.lock();
            Utils.saveFile(instanceSOIL + "\n\n", instancePath, "outputInvalid.soil");
            lock.unlock();
        }
        
        lock.lock();
        Utils.saveFile("\n\n" + categoryPrompt + list, instancePath, "output.md");
        Utils.saveFile(instanceSOIL + "\n\n", instancePath, "output" + ".soil");
        Utils.saveFile(instanceSOIL + "\n\n", instancePath, categoryId + ".soil");
        Utils.saveFile("\n" + "```\n" + instanceSOIL + "\n```", instancePath, "output.md");
        lock.unlock();

        use.close();
    }
}
