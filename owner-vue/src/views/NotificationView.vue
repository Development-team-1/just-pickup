<template>
  <div class="dashboard">
    <v-subheader class="py-0 d-flex justify-space-between rounded-lg">
      <h3>알림</h3>
    </v-subheader>
    <br>
    <template
        v-for="(item, index) in notifications">
      <v-list-item three-line :key="item.id">
        <v-list-item-content>
          <v-list-item-title>{{item.title}}</v-list-item-title>
          <v-list-item-subtitle>
            {{item.message}}
          </v-list-item-subtitle>
          <v-list-item-subtitle>
            {{item.time}}
          </v-list-item-subtitle>
        </v-list-item-content>

        <v-list-item-action>
          <v-checkbox
              disabled
              v-if="item.prevRead"
              v-model="item.read"
              hide-details></v-checkbox>
          <v-checkbox
              v-else
              v-model="item.read"
              @click="clickRead(item.id, item.read)"
              hide-details></v-checkbox>
        </v-list-item-action>
      </v-list-item>
      <v-divider
          v-if="index < notifications.length - 1"
          :key="'divider-' + index"
      ></v-divider>
    </template>
  </div>
</template>

<script>
import notificationApi from "../api/notification";

export default {
  name: "NotificationView",
  mounted() {
    this.search();
  },
  data: function() {
    return {
      notifications: []
    }
  },
  methods: {
    search: async function() {
      const response = await notificationApi.requestNotification();
      this.render(response.data);
    },
    render: function(json) {
      const notifications = json.data.notifications;
      notifications.forEach(notification => {
        this.notifications.push({
          id: notification.id,
          message: notification.message,
          title: notification.title,
          prevRead: notification.read,
          read: notification.read,
          time: notification.time
        });
      });
    },
    clickRead: async function(id, isRead) {
      await notificationApi.patchNotification(id, isRead);
      if (isRead) {
        alert("해당 알림은 읽음 처리되었습니다.");
        this.$emit("minusCount");
      } else {
        alert("해당 알림은 읽음 해제 처리되었습니다.");
        this.$emit("plusCount");
      }
    }
  }
}
</script>

<style scoped>

</style>