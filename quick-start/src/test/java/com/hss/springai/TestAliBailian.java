package com.hss.springai;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.api.DashScopeSpeechSynthesisApi;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioTranscriptionModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioTranscriptionOptions;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;

import reactor.core.publisher.Flux;

/**
 * 测试阿里百炼模型
 */
@SpringBootTest
public class TestAliBailian {
        
    /**
     * 阻塞式响应
     * @param dashScopeChatModel
     */
    @Test
    public void testQwen(@Autowired DashScopeChatModel dashScopeChatModel){
        String content = dashScopeChatModel.call("你好，你是谁");
        System.out.println(content);
    }

    /**
     * 流式响应
     * @param dashScopeChatModel
     */
    @Test
    public void testQwenStream(@Autowired DashScopeChatModel dashScopeChatModel){
        
        Flux<ChatResponse> stream = dashScopeChatModel.stream(new Prompt("你好，你是谁", DashScopeChatOptions
            .builder()
            .withModel(DashScopeApi.ChatModel.QWEN_PLUS.getModel())
            .build()));
        stream.toIterable().forEach(e -> System.out.println(e.getResult().getOutput().getText()));
    }

    /**
     * 文本生成图片
     * @param imageModel
     */
    @Test
    public void testText2Img(@Autowired DashScopeImageModel imageModel){
        DashScopeImageOptions imageOptions = DashScopeImageOptions.builder()
            /* // 图片数量
            .withN(1)
            // 图片宽度
            .withWidth(512)
            // 图片高度
            .withHeight(512) */
            // 模型名称
            //.withModel("wanx2.1-t2i-turbo")
            .withModel("wanx-v1")
            .build();
        
        ImageResponse imageResponse = imageModel.call(new ImagePrompt("程序员范云祥",imageOptions));
        String imageUrl = imageResponse.getResult().getOutput().getUrl();
        
        //图片url
        System.out.println(imageUrl);

        //图片base64
        /* System.out.println(imageResponse.getResult().getOutput().getB64Json()); */

    }


    /**
     * 文本生成语音
     * @param speechSynthesisModel
     */
    @Test
    public void testText2Audio(@Autowired DashScopeSpeechSynthesisModel speechSynthesisModel){
        DashScopeSpeechSynthesisOptions options = DashScopeSpeechSynthesisOptions.builder()
            .voice("longyingtian") // 人声
            //.speed(null) // 语速
            .model("cosyvoice-v2") // 模型
            //.responseFormat(DashScopeSpeechSynthesisApi.ResponseFormat.MP3)
            .build();

        SpeechSynthesisResponse response = speechSynthesisModel.call(
            new SpeechSynthesisPrompt("大家好，我是MC天问。", options));

        File file = new File(System.getProperty("user.dir") + "/output.mp3");
        try(FileOutputStream fos = new FileOutputStream(file)){
            ByteBuffer byteBuffer = response.getResult().getOutput().getAudio();
            fos.write(byteBuffer.array());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 语音识别文本
     * todo 测试验证异常
     * @param transcriptionModel
     */
    @Test
    public void testAudio2Test(@Autowired DashScopeAudioTranscriptionModel transcriptionModel) throws MalformedURLException {
        DashScopeAudioTranscriptionOptions transcriptionOptions = DashScopeAudioTranscriptionOptions.builder()
            //.withModel(null) // 模型
            .build();

        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(
            new UrlResource("https://github.com/HeTreeTerr/spring-ai-parent/blob/master/quick-start/output.mp3"), 
            transcriptionOptions);
        
        AudioTranscriptionResponse response = transcriptionModel.call(prompt);
        System.out.println(response.getResult().getOutput());
        
    }
}