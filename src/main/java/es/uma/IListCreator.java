package es.uma;

import dev.langchain4j.service.SystemMessage;

public interface IListCreator {
    String system = """
            Your task is to generate a complete and diverse set of instances for a given category based on a provided conceptual model description. Each instance must:
            1- Be self-contained: Include all required attributes, relationships, and related entities in full detail.
            2- Follow the model: Adhere to the constraints, multiplicities, and relationships defined in the UML specification.
            3- Avoid duplication: Build on prior instances without redundancy, clearly describing any interconnections, and no duplicate namings.
            4- Regional diversity: Incorporate varied scenarios, including regional, linguistic, or cultural differences.
            5- Structural diversity: Include instances with different shapes and complexity, and create varied examples by changing entity attributes.
            """;

    @SystemMessage(system)
    String chat(String message);
}
