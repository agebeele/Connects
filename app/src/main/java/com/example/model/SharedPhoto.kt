package com.example.model

enum class CloudBackupState {
    SYNCED,
    UPLOADING,
    LOCAL_ONLY
}

data class ReactionCount(
    val emoji: String,
    val count: Int,
    val userReacted: Boolean = false
)

data class CommentItem(
    val id: String,
    val authorName: String,
    val authorAvatarColor: Long,
    val text: String,
    val timestampFormatted: String
)

data class SharedPhoto(
    val id: String,
    val roomId: String,
    val roomName: String,
    val authorId: String,
    val authorName: String,
    val authorAvatarColor: Long,
    val imageResId: Int? = null,
    val localUri: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val timestampFormatted: String = "Hace un momento",
    val caption: String = "",
    val filterName: String = "Normal",
    val privacy: PrivacyLevel = PrivacyLevel.FRIENDS_ONLY,
    val backupState: CloudBackupState = CloudBackupState.SYNCED,
    val reactions: List<ReactionCount> = emptyList(),
    val comments: List<CommentItem> = emptyList(),
    val isAiHighlightSuggested: Boolean = false,
    val highlightScore: Float = 0.85f,
    val highlightReason: String = "",
    val isVideo: Boolean = false,
    val videoDurationSeconds: Int = 0
)
