package com.hss.springai;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

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
import com.alibaba.cloud.ai.dashscope.chat.MessageFormat;
import com.alibaba.cloud.ai.dashscope.common.DashScopeApiConstants;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesis;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesisParam;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

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
            new UrlResource("https://github.com/HeTreeTerr/spring-ai-parent/raw/refs/heads/master/quick-start/src/main/resources/files/output.mp3"), 
            transcriptionOptions);
        
        AudioTranscriptionResponse response = transcriptionModel.call(prompt);
        System.out.println(response.getResult().getOutput());
        
    }

    /**
     * 多模态 - 图片识别文本
     * @param dashScopeChatModel
     */
    @Test
    public void testMultimodal(@Autowired DashScopeChatModel dashScopeChatModel){
        
        var audioResource = new ClassPathResource("/files/KD_CT5.jpg");

        Media media = new Media(MimeTypeUtils.IMAGE_JPEG, audioResource);

        DashScopeChatOptions options = DashScopeChatOptions.builder()
        .withMultiModel(true) // 开启多模态    
        .withModel("qwen-vl-max-latest")
        .build();
        
        Prompt prompt = Prompt.builder().chatOptions(options)
            .messages(UserMessage.builder()
                .media(media)
                .text("识别图片")
                .build())
            .build();

        ChatResponse response = dashScopeChatModel.call(prompt);
        System.out.println(response.getResult().getOutput().getText());
    }

    /**
     * 多模态 - 语音识别文本
     * todo 测试验证异常
     * @param dashScopeChatModel
     */
    @Test
    public void testMultimodalSpeechToText(@Autowired DashScopeChatModel dashScopeChatModel){

        var audioResource = new ClassPathResource("/files/output.mp3");

        Media media = new Media(new MimeType("audio","mpeg3"), audioResource);

        DashScopeChatOptions options = DashScopeChatOptions.builder()
        .withMultiModel(true) // 开启多模态    
        .withModel("qwen-omni-turbo")
        .build();
        
        Prompt prompt = Prompt.builder().chatOptions(options)
            .messages(UserMessage.builder()
                .media(media)
                .metadata(Map.of(DashScopeApiConstants.MESSAGE_FORMAT, MessageFormat.VIDEO))
                .text("识别语音文件")
                .build())
            .build();

        ChatResponse response = dashScopeChatModel.call(prompt);
        System.out.println(response.getResult().getOutput().getText());
    }

    /**
     * 文本生成视频
     * @throws InputRequiredException 
     * @throws NoApiKeyException 
     * @throws ApiException 
     */
    @Test
    public void text2Video() throws ApiException, NoApiKeyException, InputRequiredException{
        VideoSynthesis vs = new VideoSynthesis();

        VideoSynthesisParam param = VideoSynthesisParam.builder()
                    .model("wanx2.1-t2v-turbo")
                    .prompt("一只小猫在月光下奔跑")
                    .size("1280*720")
                    .apiKey(System.getenv("ALI_AI_KEY"))
                    .build();
        
        System.out.println("please wait...");
        VideoSynthesisResult result = vs.call(param);
        System.out.println(result.getOutput().getVideoUrl());
    }
}