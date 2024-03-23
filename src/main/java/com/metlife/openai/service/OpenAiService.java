package com.metlife.openai.service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AzureKeyCredential;

import java.util.ArrayList;
import java.util.List;

public class OpenAiService {

    public static void main(String[] args) {
        try {

            OpenAIClient client = new OpenAIClientBuilder()
                    .credential(new AzureKeyCredential("5c3ba330529b440da5a305a385d7c827"))
                    .endpoint("https://oai-metlife-team005.openai.azure.com/")
                    .buildClient();

            String deploymentOrModelName = "gpt4-metlife-team05";

            String txt = "함께 파이썬 공부할 스터디원을 구합니다. 인원은 5명.\n" +
                    "매주 두번 온라인 미팅을 통해 함께 공부할 예정입니다. \n" +
                    "관심있는 분들은 신청 눌러주세요";

            List<ChatRequestMessage> chatMessages = new ArrayList<>();
            chatMessages.add(new ChatRequestUserMessage("본문을 읽고 본문에서 한 단어 길이의 키워드 4개 추출해봐\n 본문: " + txt));


            ChatCompletions chatCompletions = client.getChatCompletions(deploymentOrModelName,
                    new ChatCompletionsOptions(chatMessages));

            System.out.printf("Model ID=%s is created at %s.%n", chatCompletions.getId(), chatCompletions.getCreatedAt());
            for (ChatChoice choice : chatCompletions.getChoices()) {
                ChatResponseMessage message = choice.getMessage();
                System.out.printf("Index: %d, Chat Role: %s.%n", choice.getIndex(), message.getRole());
                System.out.println("Message:");
                System.out.println(message.getContent());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
