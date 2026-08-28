package com.example.model

enum class NotificationType {
    NEW_PHOTO,
    NEW_COMMENT,
    NEW_REACTION,
    ROOM_INVITE,
    CLOUD_BACKUP,
    AI_HIGHLIGHT_READY
}

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestampFormatted: String,
    val type: NotificationType,
    val authorName: String = "",
    val authorAvatarColor: Long = 0xFF6366F1,
    val roomId: String? = null,
    val photoId: String? = null,
    val isRead: Boolean = false
)
