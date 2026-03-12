# spring-ai-parent
spring ai 基础使用

```
# ===========模型keys获取
# 1、deepseek apikeys 获取地址
https://platform.deepseek.com/api_keys

# 2、百炼 apikeys 获取地址
https://bailian.console.aliyun.com/cn-beijing/?tab=model#/api-key

# 3、ollama 本地模型
https://ollama.com/download/windows
模型:qwen3:4b

# ===========多模型动态配置验证
# 切换至：deepseek 模型
http://127.0.0.1:8082/more-platform-model/chat?message=%E4%BD%A0%E6%98%AF%E8%B0%81&platform=deepseek&model=deepseek-chat&temperature=0.7
# 切换至：百炼模型
http://127.0.0.1:8082/more-platform-model/chat?message=%E4%BD%A0%E6%98%AF%E8%B0%81&platform=dashscope&model=qwen-plus&temperature=0.7

```
