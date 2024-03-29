package bot.core.vk;

import bot.Bootstrapper;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;

import java.util.List;

public class VKCore {

    private VkApiClient vkApiClient;
    private static int ts;
    private GroupActor groupActor;
    private static int maxMsgId = -1;

    public VKCore() throws ClientException, ApiException {
        TransportClient transportClient = HttpTransportClient.getInstance();
        vkApiClient = new VkApiClient(transportClient);
        groupActor = new GroupActor(
                Bootstrapper.Configurations.getVkGroupId(),
                Bootstrapper.Configurations.getVkAccessToken()
        );
        ts = vkApiClient.messages().getLongPollServer(groupActor).execute().getTs();
    }

    public Message getMessage() throws ClientException, ApiException {
        MessagesGetLongPollHistoryQuery events_query = vkApiClient
                .messages()
                .getLongPollHistory(groupActor)
                .ts(ts);
        if(maxMsgId > 0)
            events_query.maxMsgId(maxMsgId);
        List<Message> messages = events_query
                .execute()
                .getMessages()
                .getItems();
        if(!messages.isEmpty()) {
            UpdateTs();
        }
        if(!messages.isEmpty() && !messages.get(0).isOut()){
            int message_id = messages.get(0).getId();
            if(message_id > maxMsgId) {
                maxMsgId = message_id;
            }
            return messages.get(0);
        }
        return null;
    }

    public VkApiClient getVkApiClient() {
        return vkApiClient;
    }

    public GroupActor getGroupActor() {
        return groupActor;
    }

    private void UpdateTs() throws ClientException, ApiException{
            ts = vkApiClient
                    .messages()
                    .getLongPollServer(groupActor)
                    .execute()
                    .getTs();
    }
}
