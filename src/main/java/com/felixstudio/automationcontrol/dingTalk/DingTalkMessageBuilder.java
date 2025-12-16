package com.felixstudio.automationcontrol.dingTalk;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.felixstudio.automationcontrol.dto.task.TaskProgress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

// 构建钉钉消息的工具类
@Slf4j
@Component
public class DingTalkMessageBuilder {

    private final String robotCode;

    public DingTalkMessageBuilder(@Value("${dingtalk.robotCode}") String robotCode) {
        this.robotCode = robotCode;
    }

    public JSONObject sampleText(String message,String openConversationId){
        JSONObject messageJson = new JSONObject();
        messageJson.put("content", message);
        JSONObject msgObject = new JSONObject();
        msgObject.put("msgParam", messageJson.toString());
        msgObject.put("msgKey", "sampleText");
        msgObject.put("robotCode",robotCode);
        msgObject.put("openConversationId", openConversationId);
        return msgObject;
    }
    public JSONObject buildAtMessage(String message, List<String> atMobiles){
        JSONObject messageBody = new JSONObject();
        messageBody.put("msgtype", "text");
        JSONObject textContent = new JSONObject();
        textContent.put("content", message);
        messageBody.put("text", textContent);
        if(atMobiles != null && !atMobiles.isEmpty()){
            JSONObject atObject = new JSONObject();
            atObject.put("atMobiles", atMobiles);
            atObject.put("isAtAll", false);
            messageBody.put("at", atObject);
        }
        return messageBody;
    }
    public JSONObject buildFileMessage(String mediaId, String originalFilename, String fileType,String openConversationId) {
        JSONObject messageJson = new JSONObject();
        messageJson.put("mediaId", mediaId);
        messageJson.put("fileName", originalFilename);
        messageJson.put("fileType", fileType);
        JSONObject bodyObject = new JSONObject();
        bodyObject.put("msgParam", messageJson.toString());
        bodyObject.put("msgKey", "sampleFile");
        bodyObject.put("robotCode", robotCode);
        bodyObject.put("openConversationId", openConversationId);
        return bodyObject;
    }

    public JSONObject buildPresaleQueryMarkdownMessage(String queryDay, List<TaskProgress> data,String openConversationId) {
        StringBuilder sb = new StringBuilder();
        // 标题
        sb.append("## \uD83D\uDCC5 ").append(queryDay).append(" 任务进度查询结果(共").append(data.size()).append("个店铺)").append("\n\n");
        // 失败提示
        boolean hasFailure = data.stream().anyMatch(e -> e.getFailed() > 0 || e.getFailedGoods() > 0);
        if (hasFailure) {
            sb.append("> ⚠️ **注意**：部分店铺存在失败任务或失败商品，请重点关注！\n\n");
        } else {
            sb.append("> ✅ 所有任务已运行完毕，无待处理项。\n\n");
        }
        sb.append("---\n\n");
        for (TaskProgress p : data) {
            // 安全获取值（避免 NPE）
            int pending = safeInt(p.getPending());
            int running = safeInt(p.getRunning());
            int success = safeInt(p.getSuccess());
            int failed = safeInt(p.getFailed());
            int canceled = safeInt(p.getCanceled());
            int notGoods = safeInt(p.getNotGoods());
            int successGoods = safeInt(p.getSuccessGoods());
            int failedGoods = safeInt(p.getFailedGoods());
            int canceledGoods = safeInt(p.getCanceledGoods());

            // 判断是否异常（用于图标）
            String icon = "🏪";
            if (failed > 0 || failedGoods > 0 || notGoods > 10) {
                icon = "🔴";
            } else if (pending > 0 || running > 0) {
                icon = "🔄";
            }

            sb.append("### ").append(icon).append(" ").append(safeStr(p.getShopName())).append("\n");

            // 任务状态摘要
            if (pending > 0 || running > 0) {
                sb.append("- 待运行：").append(pending)
                        .append(" | 运行中：").append(running)
                        .append(" | 已完成：").append(success + failed + canceled).append("\n");
            } else {
                sb.append("- 已完成任务：").append(success + failed + canceled+ notGoods)
                        .append("（成功：").append(success)
                        .append("，失败：").append(failed)
                        .append("，取消：").append(canceled)
                        .append(",未搜索到:").append(notGoods).append("）\n");
            }

            // 商品级结果
            sb.append("- 商品结果：成功 ").append(successGoods);
            if (failedGoods > 0) {
                sb.append("，失败 ").append(failedGoods);
            }
            if(canceledGoods > 0){
                sb.append(",重复搜索到商品 ").append(canceledGoods);
            }
            sb.append("\n\n");
        }

        sb.append("---\n");
        sb.append("📌 **说明**：  \n");
        sb.append("- “未搜索到商品”可能因商品下架、关键词变更等导致，不一定是系统错误  \n");
        sb.append("- 任务状态 = 待运行 + 运行中 + (成功+失败+取消)  \n");
        sb.append("- 🔴 表示存在失败或大量未找到商品，🔄 表示仍在运行\n");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("robotCode", robotCode);
        jsonObject.put("openConversationId", openConversationId);
        jsonObject.put("msgKey", "sampleMarkdown");
        JSONObject markdownObject = new JSONObject();
        markdownObject.put("title", "## \uD83D\uDCC5 " + queryDay + " 任务进度查询结果(共" + data.size() + "个店铺)" + "\n\n");
        markdownObject.put("text", sb.toString());
        jsonObject.put("msgParam", markdownObject.toString());
        return jsonObject;
    }
    // 工具方法：安全处理 null
    private static int safeInt(Integer value) {
        return value == null ? 0 : value;
    }

    private static String safeStr(String str) {
        return str == null ? "未知店铺" : str;
    }
}
